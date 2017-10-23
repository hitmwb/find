package com.hitme.omc.msg;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloseConnAlarm extends OMCMsgBody {
	private static final Logger LOGGER = LoggerFactory.getLogger(CloseConnAlarm.class);

	protected void parseMsgContent(Map<String, String> msgDataMap) {
		LOGGER.info("OMCCloseMessage.parseMsgContent nothing to do.");
	}

	public String getMsgContent() {
		return null;
	}

	public int getMsgType() {
		return 16;
	}
}
