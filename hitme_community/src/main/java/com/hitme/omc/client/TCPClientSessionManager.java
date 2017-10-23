package com.hitme.omc.client;

import java.util.Timer;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.util.ExpirationListener;
import org.apache.mina.util.ExpiringMap;
import org.springframework.stereotype.Component;

import com.hitme.omc.exception.SendMsgException;
import com.hitme.omc.handler.MessageHandler;
import com.hitme.omc.handler.ReturnHandler;
import com.hitme.omc.msg.AckLoginAlarm;
import com.hitme.omc.msg.AckSyncAlarmFile;
import com.hitme.omc.msg.AckSyncAlarmMsg;
import com.hitme.omc.msg.IMessage;
import com.hitme.omc.msg.OMCMessage;
import com.hitme.omc.msg.OMCMsgBody;
import com.hitme.omc.msg.RealTimeAlarm;
import com.hitme.omc.util.LogProxy;

@Component
public class TCPClientSessionManager extends MessageHandler {
	private static final LogProxy LOGGER = new LogProxy(TCPClientSessionManager.class);

	protected IoSession msgIoSession;

	public static final int TIMEOUT_DEFAULT = 30;

	protected ExpiringMap<String, ReturnHandler<Object>> sessionMessage = new ExpiringMap<String, ReturnHandler<Object>>();

	public TCPClientSessionManager() {
		this.sessionMessage.setExpirationInterval(30);
		this.sessionMessage.addExpirationListener(new ExpirationListener<ReturnHandler<Object>>() {

			public void expired(ReturnHandler<Object> handler) {
				TCPClientSessionManager.LOGGER.error("msg expired");
			}
		});
	}

	public void sendMessage(IMessage imsg) throws SendMsgException {
		LOGGER.info("client begin to send tcp msg to " + this.msgIoSession);
		try {
			TimeUnit.MILLISECONDS.sleep(50L);
			this.msgIoSession.write(imsg);
		} catch (InterruptedException e) {
			LOGGER.error("sendMessage InterruptedException", e);
		}
	}

	public void messageReceived(IoSession ioSession, Object imsg) {
		if (!(imsg instanceof OMCMessage)) {
			return;
		}
		OMCMessage omcMsg = (OMCMessage) imsg;
		int msgType = omcMsg.getMsgType();
		OMCMsgBody omcBody = omcMsg.getBody();
		if (omcBody == null) {
			LOGGER.error("recieve a null body message.");
			return;
		}
		LOGGER.info("client recieve a new message, omcBody=" + omcBody + ", session=" + this.msgIoSession);
		switch (msgType) {
		case 2:
			handleLoginAck((AckLoginAlarm) omcBody);
			break;
		case 4:
			handleSyncAlarmMsgAck((AckSyncAlarmMsg) omcBody);
			break;
		case 0:
			handleRealTimeAlarm((RealTimeAlarm) omcBody);
			break;
		case 6:
			handleSyncAlarmFileAck((AckSyncAlarmFile) omcBody);
			break;
		}

	}

	public synchronized void handleLoginAck(AckLoginAlarm reqLogin) {
		LOGGER.info("client handleLoginAck msg = " + reqLogin);

		if (StringUtils.equals("succ", reqLogin.getResult())) {
			Timer timer = new Timer();
			timer.schedule(new HeartBeatTask(this), 10000L, 60000L);
		}
	}

	public void handleSyncAlarmMsgAck(AckSyncAlarmMsg syncAlarmMsg) {
		LOGGER.info("client handleSyncAlarmMsgAck msg = " + syncAlarmMsg);
	}

	public void handleRealTimeAlarm(RealTimeAlarm realTimeAlarm) {
		LOGGER.info("client handleRealTimeAlarm msg = " + realTimeAlarm);
	}

	public void handleSyncAlarmFileAck(AckSyncAlarmFile syncAlarmFile) {
		LOGGER.info("handleSyncAlarmFileAck msg = " + syncAlarmFile);
	}

	public boolean isConnected() {
		boolean flag = false;

		if (null == this.msgIoSession) {
			LOGGER.error("tcp iosession is null ,msgIoSession: " + this.msgIoSession);
			return flag;
		}
		if (this.msgIoSession.isConnected()) {
			LOGGER.info("tcp iosession is register ,msgIoSession: " + this.msgIoSession);
			flag = true;
		} else {
			LOGGER.error("tcp iosession is not register ,msgIoSession: " + this.msgIoSession);
		}
		return flag;
	}

	public void clearSession() {
		clearSession(this.msgIoSession);
	}

	public void clearSession(IoSession session) {
		if (session != null) {
			sessionClosed(this.msgIoSession);
		}
	}

	public void sessionCreated(IoSession session) {
		this.msgIoSession = session;
		LOGGER.info("tcp session create:" + session);
	}
}
