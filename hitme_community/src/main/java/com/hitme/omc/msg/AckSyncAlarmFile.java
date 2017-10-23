package com.hitme.omc.msg;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hitme.omc.util.CommonTools;

public class AckSyncAlarmFile extends OMCMsgBody {
	private static final Logger LOGGER = LoggerFactory.getLogger(AckSyncAlarmFile.class);

	private String result;

	private String resDesc;

	protected void parseMsgContent(Map<String, String> msgDataMap) {
		if ((msgDataMap == null) || (msgDataMap.isEmpty())) {
			LOGGER.error("msgDataMap is empty");
			return;
		}
		this.result = CommonTools.getCommonValue(msgDataMap, "result");
		this.resDesc = CommonTools.getCommonValue(msgDataMap, "resDesc");
	}

	public String getMsgContent() {
		StringBuffer sb = new StringBuffer();
		sb.append("reqId=").append(this.reqId).append(";");
		sb.append("result=").append(this.result).append(";");
		sb.append("resDesc=").append(this.resDesc);
		return sb.toString();
	}

	public String getResult() {
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getResDesc() {
		return this.resDesc;
	}

	public void setResDesc(String resDesc) {
		this.resDesc = resDesc;
	}

	public int getMsgType() {
		return 6;
	}

	public String toString() {
		return getMsgContent();
	}
}
