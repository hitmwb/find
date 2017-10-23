package com.hitme.omc.msg;

import java.util.Map;

import com.hitme.omc.util.LogProxy;

public class AckHeartBeat extends OMCMsgBody {
	private static final LogProxy LOGGER = new LogProxy(AckHeartBeat.class);

	public String getMsgContent() {
		return "reqId=" + this.reqId;
	}

	protected void parseMsgContent(Map<String, String> msgDataMap) {
		LOGGER.info("AckHeartBeat.parseMsgContent nothing to do.");
	}

	public int getMsgType() {
		return 9;
	}

	public String toString() {
		return "reqId=" + this.reqId;
	}
}
