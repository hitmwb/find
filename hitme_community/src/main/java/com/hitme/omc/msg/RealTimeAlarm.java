package com.hitme.omc.msg;

import java.util.Map;

public class RealTimeAlarm extends OMCMsgBody {
	private String alarmJson;

	public String getMsgContent() {
		return this.alarmJson;
	}

	protected void parseMsgContent(Map<String, String> msgDataMap) {
	}

	public int getMsgType() {
		return 0;
	}

	public String getAlarmJson() {
		return this.alarmJson;
	}

	public void setAlarmJson(String alarmJson) {
		this.alarmJson = alarmJson;
	}

	protected boolean isContainMsgTypeName() {
		return false;
	}

	public String toString() {
		return getMsgContent();
	}
}
