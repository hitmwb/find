package com.hitme.omc.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hitme.omc.exception.SendMsgException;
import com.hitme.omc.msg.OMCMessage;
import com.hitme.omc.msg.OMCMsgFactory;
import com.hitme.omc.util.LogProxy;

@Service
public class AlarmSyncService {
	private static final LogProxy LOGGER = new LogProxy(AlarmSyncService.class);

	@Autowired
	private TCPClientSessionManager clientSessionManager;

	public void sendAlarmSyncMsg(long alarmSeq) {
		OMCMessage imsg = OMCMsgFactory.getInstance().createSyncAlarmReqMsg(alarmSeq);
		try {
			this.clientSessionManager.sendMessage(imsg);
		} catch (SendMsgException e) {
			LOGGER.error("sendAlarmSyncMsg error", e);
		}
	}

	public void sendAlarmSyncFile(int syncSource, long alarmSeq) {
		OMCMessage imsg = OMCMsgFactory.getInstance().createSyncAlarmReqFile(syncSource, alarmSeq);
		try {
			this.clientSessionManager.sendMessage(imsg);
		} catch (SendMsgException e) {
			LOGGER.error("sendAlarmSyncFile error", e);
		}
	}
}
