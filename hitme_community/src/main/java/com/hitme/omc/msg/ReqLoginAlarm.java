package com.hitme.omc.msg;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hitme.omc.util.CommonTools;

public class ReqLoginAlarm extends OMCMsgBody {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReqLoginAlarm.class);

	private String user;

	private String key;
	private String type;

	protected void parseMsgContent(Map<String, String> msgDataMap) {
		if ((msgDataMap == null) || (msgDataMap.isEmpty())) {
			LOGGER.error("msgDataMap is empty");
			return;
		}
		this.user = CommonTools.getCommonValue(msgDataMap, "user");
		this.key = CommonTools.getCommonValue(msgDataMap, "key");
		this.type = CommonTools.getCommonValue(msgDataMap, "type");
	}

	public String getMsgContent() {
		StringBuffer sb = new StringBuffer();
		sb.append("user=").append(this.user).append(";");
		sb.append("key=").append(this.key).append(";");
		sb.append("type=").append(this.type);
		return sb.toString();
	}

	public String getUser() {
		return this.user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getMsgType() {
		return 1;
	}

	public String toString() {
		return getMsgContent();
	}
}
