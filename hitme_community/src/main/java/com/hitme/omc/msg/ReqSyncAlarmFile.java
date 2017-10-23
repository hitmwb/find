package com.hitme.omc.msg;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hitme.omc.util.CommonTools;
import com.hitme.omc.util.TimeUtil;

public class ReqSyncAlarmFile extends OMCMsgBody {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReqSyncAlarmFile.class);

	private String startTime;

	private String endTime;
	private long alarmSeq;
	private int syncSource;

	protected void parseMsgContent(Map<String, String> msgDataMap) {
		if ((msgDataMap == null) || (msgDataMap.isEmpty())) {
			LOGGER.error("msgDataMap is empty");
			return;
		}
		String startTmp = CommonTools.getCommonValue(msgDataMap, "startTime");
		String endTimeTmp = CommonTools.getCommonValue(msgDataMap, "endTime");
		String alarmSeqTmp = CommonTools.getCommonValue(msgDataMap, "alarmSeq");
		String syncSourceTmp = CommonTools.getCommonValue(msgDataMap, "syncSource");
		if (StringUtils.isEmpty(startTmp)) {
			this.startTime = "1970-01-01 00:00:00";
		}
		if (StringUtils.isEmpty(endTimeTmp)) {
			this.endTime = TimeUtil.getCurrentStrTime();
		}
		if (StringUtils.isNotEmpty(alarmSeqTmp)) {
			this.alarmSeq = Long.parseLong(alarmSeqTmp);
		}
		if (StringUtils.isNotEmpty(syncSourceTmp)) {
			this.syncSource = Integer.parseInt(syncSourceTmp);
		}
	}

	public String getMsgContent() {
		this.reqId = getNewReqId();
		StringBuffer sb = new StringBuffer();
		sb.append("reqId=").append(this.reqId).append(";");
		sb.append("startTime=").append(this.startTime).append(";");
		sb.append("endTime=").append(this.endTime).append(";");
		sb.append("alarmSeq=").append(this.alarmSeq).append(";");
		sb.append("syncSource=").append(this.syncSource);
		return sb.toString();
	}

	public int getMsgType() {
		return 5;
	}

	public String getStartTime() {
		return this.startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return this.endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public long getAlarmSeq() {
		return this.alarmSeq;
	}

	public void setAlarmSeq(long alarmSeq) {
		this.alarmSeq = alarmSeq;
	}

	public int getSyncSource() {
		return this.syncSource;
	}

	public void setSyncSource(int syncSource) {
		this.syncSource = syncSource;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("reqId=").append(this.reqId).append(";");
		sb.append("startTime=").append(this.startTime).append(";");
		sb.append("endTime=").append(this.endTime).append(";");
		sb.append("alarmSeq=").append(this.alarmSeq).append(";");
		sb.append("syncSource=").append(this.syncSource);
		return sb.toString();
	}
}
