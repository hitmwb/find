package com.hitme.omc.handler;

import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hitme.omc.exception.SendMsgException;
import com.hitme.omc.msg.OMCMessage;
import com.hitme.omc.msg.OMCMsgFactory;
import com.hitme.omc.service.AlarmService;
import com.hitme.omc.util.LogProxy;

@Component
public class AlarmSyncReportHandler {
	private static final LogProxy LOGGER = new LogProxy(AlarmSyncReportHandler.class);

	@Autowired
	private TCPSessionManager tcpSessionManager;

	@Autowired
	private AlarmService alarmService;

	public void syncAlarmMsg(IoSession session, long alarmSeq, long currSeq) {
		List<String> alarmJsonList = this.alarmService.searchAlarmLog(alarmSeq, currSeq);
		if (CollectionUtils.isEmpty(alarmJsonList)) {
			LOGGER.warn("there has no alarm to sync. alarmSeq=" + alarmSeq + ", currSeq=" + currSeq);
			return;
		}
		try {
			for (String alarmJson : alarmJsonList) {
				OMCMessage imsg = OMCMsgFactory.getInstance().createRealTimeAlarm(alarmJson);
				this.tcpSessionManager.sendMessage(session, imsg);
			}
		} catch (SendMsgException e) {
			LOGGER.error("syncAlarmMsg send msg error. alarmSeq=" + alarmSeq + ", currSeq=" + currSeq, e);
		}
	}
}
