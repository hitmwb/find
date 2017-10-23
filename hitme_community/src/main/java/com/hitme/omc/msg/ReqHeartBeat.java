package com.hitme.omc.msg;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReqHeartBeat extends OMCMsgBody {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReqHeartBeat.class);

	protected void parseMsgContent(Map<String, String> msgDataMap) {
		LOGGER.info("OMCHeartBeatMessage.parseMsgContent nothing to do.");
	}

	public String getMsgContent() {
		return "reqId=" + getNewReqId();
	}

	public int getMsgType() {
		return 8;
	}

	public String toString() {
		return "reqId=" + this.reqId;
	}
}
