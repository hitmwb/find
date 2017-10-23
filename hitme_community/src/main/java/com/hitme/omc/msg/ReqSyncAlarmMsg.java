package com.hitme.omc.msg;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hitme.omc.util.CommonTools;

public class ReqSyncAlarmMsg extends OMCMsgBody {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReqSyncAlarmMsg.class);

	private long alarmSeq;

	protected void parseMsgContent(Map<String, String> msgDataMap) {
		if ((msgDataMap == null) || (msgDataMap.isEmpty())) {
			LOGGER.error("msgDataMap is empty");
			return;
		}
		String alarmSeqStr = CommonTools.getCommonValue(msgDataMap, "alarmSeq");
		if (StringUtils.isNotEmpty(alarmSeqStr)) {
			this.alarmSeq = Long.parseLong(alarmSeqStr);
		}
	}

	public String getMsgContent() {
		this.reqId = getNewReqId();
		StringBuffer sb = new StringBuffer();
		sb.append("reqId=").append(this.reqId).append(";");
		sb.append("alarmSeq=").append(this.alarmSeq);
		return sb.toString();
	}

	public int getMsgType() {
		return 3;
	}

	public long getAlarmSeq() {
		return this.alarmSeq;
	}

	public void setAlarmSeq(long alarmSeq) {
		this.alarmSeq = alarmSeq;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("reqId=").append(this.reqId).append(";");
		sb.append("alarmSeq=").append(this.alarmSeq);
		return sb.toString();
	}
}
