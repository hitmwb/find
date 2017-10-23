package com.hitme.omc.handler;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hitme.omc.exception.SendMsgException;
import com.hitme.omc.manager.AccountManager;
import com.hitme.omc.model.AccountInfo;
import com.hitme.omc.msg.IMessage;
import com.hitme.omc.msg.OMCMessage;
import com.hitme.omc.msg.OMCMsgBody;
import com.hitme.omc.msg.OMCMsgFactory;
import com.hitme.omc.msg.ReqHeartBeat;
import com.hitme.omc.msg.ReqLoginAlarm;
import com.hitme.omc.msg.ReqSyncAlarmFile;
import com.hitme.omc.msg.ReqSyncAlarmMsg;
import com.hitme.omc.util.LogProxy;

@Component
public class TCPSessionManager extends MessageHandler {
	private static final LogProxy LOGGER = new LogProxy(TCPSessionManager.class);

	protected Map<String, IoSession> ioSessionCache;

	protected Map<String, String> ioRegisterSessionMsgCache;

	protected Map<String, String> ioRegisterSessionFtpCache;

	public static final int TIMEOUT_DEFAULT = 30;

	@Autowired
	private AlarmReportHandler alarmReportHandler;

	@Autowired
	private AlarmSyncReportHandler alarmSyncReportHandler;

	@Autowired
	private AlarmSyncFileHandler alarmSyncFileHandler;

	private TCPSessionManager() {
		this.ioSessionCache = new ConcurrentHashMap<String, IoSession>();
		this.ioRegisterSessionMsgCache = new ConcurrentHashMap<String, String>();
		this.ioRegisterSessionFtpCache = new ConcurrentHashMap<String, String>();
	}

	public void sendMessage(IMessage imsg) throws SendMsgException {
		LOGGER.info("server begin to send tcp msg to all register iosession, imsg=" + imsg.toString());
		for (Map.Entry<String, IoSession> entity : this.ioSessionCache.entrySet()) {
			IoSession session = (IoSession) entity.getValue();

			if (checkSessionType(session, "msg")) {

				sendMessage(session, imsg);
			}
		}
	}

	public void sendMessage(String sessionId, IMessage imsg) throws SendMsgException {
		LOGGER.info("server begin to send tcp msg:" + imsg.toString() + " sessionId:" + sessionId);
		IoSession session = (IoSession) this.ioSessionCache.get(sessionId);
		if (session == null) {
			throw new SendMsgException("sendMessage error, tcp session not exist.");
		}
		if (!checkSessionLogin(session)) {
			throw new SendMsgException("sendMessage error, tcp session not login.");
		}
		sendMessage(session, imsg);
	}

	public void sendMessage(IoSession session, IMessage imsg) throws SendMsgException {
		LOGGER.info("server begin to send tcp msg:" + imsg.toString() + " session:" + session);
		try {
			TimeUnit.MILLISECONDS.sleep(50L);
			session.write(imsg);
		} catch (InterruptedException e) {
			LOGGER.error("sendMessage InterruptedException", e);
		}
	}

	public void messageReceived(IoSession session, Object msgObj) {
		if (!(msgObj instanceof OMCMessage)) {
			return;
		}
		OMCMessage omcMsg = (OMCMessage) msgObj;
		int msgType = omcMsg.getMsgType();
		OMCMsgBody omcBody = omcMsg.getBody();
		if (omcBody == null) {
			LOGGER.error("recieve a null body message.");
			return;
		}
		LOGGER.info("server recieve a new message, omcBody=" + omcBody + ", session=" + session);
		switch (msgType) {
		case 1:
			handleLogin(session, (ReqLoginAlarm) omcBody);
			break;
		case 8:
			handleHeartBeat(session, (ReqHeartBeat) omcBody);
			break;
		case 16:
			handleCloseConnection(session);
			break;
		case 3:
			handleSyncAlarmMsg(session, (ReqSyncAlarmMsg) omcBody);
			break;
		case 5:
			handleSyncAlarmFile(session, (ReqSyncAlarmFile) omcBody);
			break;
		}

	}

	public synchronized void handleLogin(IoSession session, ReqLoginAlarm reqLogin) {
		String sessionId = getSessionID(session);
		String user = reqLogin.getUser();
		String key = reqLogin.getKey();
		String type = reqLogin.getType();
		String result = "fail";
		String resDesc = "";
		int failCount = ((Integer) session.getAttribute("failCount", Integer.valueOf(0))).intValue();
		AccountInfo account = AccountManager.getInstance().getAccountInfo(user);

		boolean typeCheck = (StringUtils.equals(type, "msg")) || (StringUtils.equals(type, "msg"));
		if ((StringUtils.isEmpty(user)) || (StringUtils.isEmpty(key)) || (!typeCheck)) {
			resDesc = "Req param empty or incorrect";
		} else if (account == null) {
			resDesc = "User not exist";
		} else if (!StringUtils.equals(key, account.getKey())) {
			resDesc = "User key incorrect";
		} else if (checkSessionLogin(session)) {

			resDesc = "Repeated login";
			LOGGER.info("recieve a repeated login message, session=" + session);
		} else {
			result = "succ";

			if ((StringUtils.equals(type, "msg")) && (this.ioRegisterSessionMsgCache.containsKey(user))) {
				String sessionIdTmp = (String) this.ioRegisterSessionMsgCache.get(user);
				LOGGER.warn("recieve a new login message, old session will be close. old sessionid=" + sessionIdTmp);
				clearSession(sessionIdTmp);
			} else if ((StringUtils.equals(type, "ftp")) && (this.ioRegisterSessionFtpCache.containsKey(user))) {
				String sessionIdTmp = (String) this.ioRegisterSessionFtpCache.get(user);
				LOGGER.warn("recieve a new login message, old session will be close. old sessionid=" + sessionIdTmp);
				clearSession(sessionIdTmp);
			}
		}
		LOGGER.info("handle login message end, result=" + result + ", resDesc=" + resDesc);

		OMCMessage omcMsg = OMCMsgFactory.getInstance().createLoginAckMsg(result, resDesc);
		try {
			sendMessage(session, omcMsg);
		} catch (SendMsgException e) {
			LOGGER.error("sendMessage error.", e);
		}

		LOGGER.info("send ack-login success");

		if (StringUtils.equals(result, "succ")) {

			if (StringUtils.equals(type, "msg")) {
				this.ioRegisterSessionMsgCache.put(user, sessionId);
			} else {
				this.ioRegisterSessionFtpCache.put(user, sessionId);
			}

			session.setAttribute("failCount", Integer.valueOf(0));

			session.setAttribute("loginType", type);
			session.setAttribute("user", user);
		} else {
			failCount++;
			if (failCount >= 3) {

				LOGGER.error("fail count 3 times, begin to clear session.");

				clearSession(sessionId);
				return;
			}
			session.setAttribute("failCount", Integer.valueOf(failCount));
		}
	}

	public void handleHeartBeat(IoSession session, ReqHeartBeat heartBeat) {
		long reqId = heartBeat.getReqId();
		OMCMessage omcMsg = OMCMsgFactory.getInstance().createHeartBeatAckMsg(reqId);
		try {
			sendMessage(session, omcMsg);
		} catch (SendMsgException e) {
			LOGGER.info("handleHeartBeat send msg error.");
		}
	}

	public void handleCloseConnection(IoSession session) {
		sessionClosed(session);
	}

	public void handleSyncAlarmMsg(IoSession session, ReqSyncAlarmMsg syncAlarmMsg) {
		long reqId = syncAlarmMsg.getReqId();
		String result = "succ";
		String resDesc = "";
		OMCMessage omcMsg = OMCMsgFactory.getInstance().createSyncAlarmAckMsg(reqId, result, resDesc);
		try {
			sendMessage(session, omcMsg);
		} catch (SendMsgException e) {
			LOGGER.error("handleHeartBeat send msg error.");
		}

		try {
			this.alarmReportHandler.suspend();
			this.alarmSyncReportHandler.syncAlarmMsg(session, syncAlarmMsg.getAlarmSeq(),
					this.alarmReportHandler.getAlarmSeqCurr());
		} finally {
			this.alarmReportHandler.resume();
		}
	}

	public void handleSyncAlarmFile(IoSession session, ReqSyncAlarmFile syncAlarmFile) {
		long reqId = syncAlarmFile.getReqId();
		String startTime = syncAlarmFile.getStartTime();
		String endTime = syncAlarmFile.getEndTime();
		long alarmSeq = syncAlarmFile.getAlarmSeq();
		int syncSource = syncAlarmFile.getSyncSource();
		String result = "succ";
		String resDesc = "";
		if (!checkSessionType(session, "ftp")) {
			LOGGER.info("tcp session type error.");
			result = "fail";
			resDesc = "tcp login type error";
		} else if (this.alarmSyncFileHandler.checkParam(startTime, endTime, alarmSeq, syncSource)) {
			LOGGER.info(
					"alarmseq error, alarmseq=" + alarmSeq + ", currSeq=" + this.alarmReportHandler.getAlarmSeqCurr());
			result = "fail";
			resDesc = "Req param empty or incorrect";
		}

		OMCMessage omcMsg = OMCMsgFactory.getInstance().createSyncAlarmAckFile(reqId, result, resDesc);
		try {
			sendMessage(session, omcMsg);
		} catch (SendMsgException e) {
			LOGGER.error("handleHeartBeat send msg error.");
		}

		String fileName = this.alarmSyncFileHandler.createAlarmFile(startTime, endTime, alarmSeq, syncSource,
				this.alarmReportHandler.getAlarmSeqCurr());
		if (StringUtils.isEmpty(fileName)) {
			result = "fail";
			resDesc = "unknow reason";
		}

		OMCMessage omcMsgRst = OMCMsgFactory.getInstance().createSyncAlarmAckFileRst(reqId, result, fileName, resDesc);
		try {
			sendMessage(session, omcMsgRst);
		} catch (SendMsgException e) {
			LOGGER.error("handleHeartBeat send msg error.");
		}
	}

	public String getSessionID(InetSocketAddress inetAddress) {
		String ipAddress = "";
		int port = 0;
		if (null != inetAddress) {
			ipAddress = inetAddress.getHostString();
			port = inetAddress.getPort();
		}
		return ipAddress + "|" + port;
	}

	public String getSessionID(IoSession session) {
		String sessionId = (String) session.getAttribute("sessionId");
		if (StringUtils.isNotEmpty(sessionId)) {
			return sessionId;
		}

		InetSocketAddress inetAddress = (InetSocketAddress) session.getRemoteAddress();
		return getSessionID(inetAddress);
	}

	public String getSessionUser(IoSession session) {
		return (String) session.getAttribute("user");
	}

	public void sessionClosed(IoSession session) {
		String sessionId = (String) session.getAttribute("sessionId");

		if (StringUtils.isNotEmpty(sessionId)) {
			LOGGER.info("tcp session closed ,sessionId : " + sessionId);
			clearSession(sessionId);
		}
	}

	public boolean isConnected(String sessionId) {
		boolean flag = false;
		IoSession ioSesison = (IoSession) this.ioSessionCache.get(sessionId);

		if (null == ioSesison) {
			LOGGER.error("tcp iosession is null ,sessionId: " + sessionId);
			return flag;
		}
		if ((ioSesison.isConnected()) && (sessionId.equals(this.ioRegisterSessionMsgCache.get(sessionId)))) {
			LOGGER.info("tcp iosession is register ,sessionId: " + sessionId);
			flag = true;
		} else {
			LOGGER.error("tcp iosession is not register ,sessionId: " + sessionId);
		}

		return flag;
	}

	public void clearSession(String sessionId) {
		if (StringUtils.isEmpty(sessionId)) {
			return;
		}
		IoSession session = (IoSession) this.ioSessionCache.remove(sessionId);
		String user = getSessionUser(session);
		if (StringUtils.isNotEmpty(user)) {
			this.ioRegisterSessionMsgCache.remove(user);
			this.ioRegisterSessionFtpCache.remove(user);
		}
		if (session != null) {
			session.closeNow();
		}
	}

	public void sessionCreated(IoSession session) {
		InetSocketAddress inetAddress = (InetSocketAddress) session.getRemoteAddress();
		String sessionId = getSessionID(inetAddress);
		LOGGER.info("tcp session is put sessionId:" + sessionId);
		this.ioSessionCache.put(sessionId, session);
		session.setAttributeIfAbsent("sessionId", sessionId);
	}

	public void sessionIdle(IoSession session, IdleStatus status) {
		if (session == null) {
			LOGGER.info("session is null. session = " + session);
			return;
		}

		if (!checkSessionLogin(session)) {
			String sessionId = getSessionID(session);
			clearSession(sessionId);
		}
	}

	public boolean checkSessionLogin(IoSession session) {
		if (session == null) {
			return false;
		}
		String type = (String) session.getAttribute("loginType");

		return (StringUtils.equals(type, "msg")) || (StringUtils.equals(type, "ftp"));
	}

	public boolean checkSessionType(IoSession session, String type) {
		if (session == null) {
			return false;
		}
		String loginType = (String) session.getAttribute("loginType");

		return StringUtils.equals(type, loginType);
	}
}
