package com.hitme.omc.msg;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OMCMsgFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(OMCMsgFactory.class);

	private static OMCMsgFactory instance = new OMCMsgFactory();

	private Map<Integer, String> cmdClassMap = new ConcurrentHashMap<Integer, String>();

	private Map<Integer, String> cmdNameMap = new ConcurrentHashMap<Integer, String>();

	private OMCMsgFactory() {
		init();
	}

	private void init() {
		this.cmdClassMap.put(Integer.valueOf(0), "com.accelink.omc.msg.RealTimeAlarm");
		this.cmdClassMap.put(Integer.valueOf(1), "com.accelink.omc.msg.ReqLoginAlarm");
		this.cmdClassMap.put(Integer.valueOf(2), "com.accelink.omc.msg.AckLoginAlarm");
		this.cmdClassMap.put(Integer.valueOf(3), "com.accelink.omc.msg.ReqSyncAlarmMsg");
		this.cmdClassMap.put(Integer.valueOf(4), "com.accelink.omc.msg.AckSyncAlarmMsg");
		this.cmdClassMap.put(Integer.valueOf(5), "com.accelink.omc.msg.ReqSyncAlarmFile");
		this.cmdClassMap.put(Integer.valueOf(6), "com.accelink.omc.msg.AckSyncAlarmFile");
		this.cmdClassMap.put(Integer.valueOf(7), "com.accelink.omc.msg.AckSyncAlarmFileResult");
		this.cmdClassMap.put(Integer.valueOf(8), "com.accelink.omc.msg.ReqHeartBeat");
		this.cmdClassMap.put(Integer.valueOf(9), "com.accelink.omc.msg.AckHeartBeat");
		this.cmdClassMap.put(Integer.valueOf(16), "com.accelink.omc.msg.CloseConnAlarm");
		this.cmdNameMap.put(Integer.valueOf(0), "realTimeAlarm");
		this.cmdNameMap.put(Integer.valueOf(1), "reqLoginAlarm");
		this.cmdNameMap.put(Integer.valueOf(2), "ackLoginAlarm");
		this.cmdNameMap.put(Integer.valueOf(3), "reqSyncAlarmMsg");
		this.cmdNameMap.put(Integer.valueOf(4), "ackSyncAlarmMsg");
		this.cmdNameMap.put(Integer.valueOf(5), "reqSyncAlarmFile");
		this.cmdNameMap.put(Integer.valueOf(6), "ackSyncAlarmFile");
		this.cmdNameMap.put(Integer.valueOf(7), "ackSyncAlarmFileResult");
		this.cmdNameMap.put(Integer.valueOf(8), "reqHeartBeat");
		this.cmdNameMap.put(Integer.valueOf(9), "ackHeartBeat");
		this.cmdNameMap.put(Integer.valueOf(16), "closeConnAlarm");
	}

	public static OMCMsgFactory getInstance() {
		if (instance == null) {
			instance = new OMCMsgFactory();
		}
		return instance;
	}

	public OMCMsgBody getOMCMsg(int type) {
		String cmdName = (String) this.cmdClassMap.get(Integer.valueOf(type));
		if (cmdName == null) {
			return null;
		}
		try {
			return (OMCMsgBody) Class.forName(cmdName).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			LOGGER.error("getOMCMsg error, type=" + type, e);
		}
		return null;
	}

	public String getOMCMsgTypeName(int type) {
		return (String) this.cmdNameMap.get(Integer.valueOf(type));
	}

	public OMCMessage createLoginAckMsg(String result, String resDesc) {
		OMCMessage omcMsg = new OMCMessage(2);
		AckLoginAlarm ackLogin = new AckLoginAlarm();
		ackLogin.setResult(result);
		ackLogin.setResDesc(resDesc);
		omcMsg.setBody(ackLogin);
		return omcMsg;
	}

	public OMCMessage createHeartBeatAckMsg(long reqId) {
		OMCMessage omcMsg = new OMCMessage(9);
		AckHeartBeat heartBeat = new AckHeartBeat();
		heartBeat.setReqId(reqId);
		omcMsg.setBody(heartBeat);
		return omcMsg;
	}

	public OMCMessage createSyncAlarmAckMsg(long reqId, String result, String resDesc) {
		OMCMessage omcMsg = new OMCMessage(4);
		AckSyncAlarmMsg syncAlarm = new AckSyncAlarmMsg();
		syncAlarm.setReqId(reqId);
		syncAlarm.setResult(result);
		syncAlarm.setResDesc(resDesc);
		omcMsg.setBody(syncAlarm);
		return omcMsg;
	}

	public OMCMessage createSyncAlarmAckFile(long reqId, String result, String resDesc) {
		OMCMessage omcMsg = new OMCMessage(6);
		AckSyncAlarmFile syncAlarm = new AckSyncAlarmFile();
		syncAlarm.setReqId(reqId);
		syncAlarm.setResult(result);
		syncAlarm.setResDesc(resDesc);
		omcMsg.setBody(syncAlarm);
		return omcMsg;
	}

	public OMCMessage createSyncAlarmAckFileRst(long reqId, String result, String fileName, String resDesc) {
		OMCMessage omcMsg = new OMCMessage(7);
		AckSyncAlarmFileResult syncAlarm = new AckSyncAlarmFileResult();
		syncAlarm.setReqId(reqId);
		syncAlarm.setResult(result);
		syncAlarm.setFileName(fileName);
		syncAlarm.setResDesc(resDesc);
		omcMsg.setBody(syncAlarm);
		return omcMsg;
	}

	public OMCMessage createRealTimeAlarm(String alarmJson) {
		OMCMessage omcMsg = new OMCMessage(0);
		RealTimeAlarm realTimeAlarm = new RealTimeAlarm();
		realTimeAlarm.setAlarmJson(alarmJson);
		omcMsg.setBody(realTimeAlarm);
		return omcMsg;
	}

	public OMCMessage createLoginReqMsg(String user, String key, String type) {
		OMCMessage omcMsg = new OMCMessage(1);
		ReqLoginAlarm reqLogin = new ReqLoginAlarm();
		reqLogin.setUser(user);
		reqLogin.setKey(key);
		reqLogin.setType(type);
		omcMsg.setBody(reqLogin);
		return omcMsg;
	}

	public OMCMessage createHeartBeatReqMsg() {
		OMCMessage omcMsg = new OMCMessage(8);
		ReqHeartBeat heartBeat = new ReqHeartBeat();
		omcMsg.setBody(heartBeat);
		return omcMsg;
	}

	public OMCMessage createSyncAlarmReqMsg(long alarmSeq) {
		OMCMessage omcMsg = new OMCMessage(3);
		ReqSyncAlarmMsg syncAlarm = new ReqSyncAlarmMsg();
		syncAlarm.setAlarmSeq(alarmSeq);
		omcMsg.setBody(syncAlarm);
		return omcMsg;
	}

	public OMCMessage createSyncAlarmReqFile(int syncSource, long alarmSeq) {
		OMCMessage omcMsg = new OMCMessage(5);
		ReqSyncAlarmFile syncAlarm = new ReqSyncAlarmFile();
		syncAlarm.setSyncSource(syncSource);
		syncAlarm.setAlarmSeq(alarmSeq);
		omcMsg.setBody(syncAlarm);
		return omcMsg;
	}

	public OMCMessage createCloseConnAlarmMsg() {
		OMCMessage omcMsg = new OMCMessage(16);
		CloseConnAlarm closeConn = new CloseConnAlarm();
		omcMsg.setBody(closeConn);
		return omcMsg;
	}
}
