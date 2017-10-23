package com.hitme.omc.msg;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class OMCMsgBody {
	private static final Logger LOGGER = LoggerFactory.getLogger(OMCMsgBody.class);
	private static long curSeq = 0L;
	protected long reqId;

	public byte[] getBytaData() {
		String msgBody = getMsgBody();
		if (msgBody == null) {
			return null;
		}
		return msgBody.getBytes();
	}

	public String getMsgBody() {
		String msgTypeName = getMsgTypeName();
		if (StringUtils.isEmpty(msgTypeName)) {
			LOGGER.error("msgTypeName is empty.");
			return null;
		}
		StringBuffer sb = new StringBuffer();
		String msgContent = getMsgContent();
		if (isContainMsgTypeName()) {
			sb.append(msgTypeName);
			if (StringUtils.isNotEmpty(msgContent)) {
				sb.append(";");
				sb.append(msgContent);
			}

		} else if (StringUtils.isNotEmpty(msgContent)) {
			sb.append(msgContent);
		}

		return sb.toString();
	}

	public void parseByteData(byte[] dataByte) {
		if ((dataByte == null) || (dataByte.length == 0)) {
			return;
		}
		String msgBody = new String(dataByte);
		int msgType = getMsgType();
		LOGGER.info("begin to parse msg. msgBody = " + msgBody);
		if (msgType == 0) {
			((RealTimeAlarm) this).setAlarmJson(msgBody);
		} else {
			String[] msgBodyArr = msgBody.split("\\;");
			if ((msgBodyArr == null) || (msgBodyArr.length < 2)) {
				return;
			}
			Map<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("msgType", msgBodyArr[0]);
			for (int i = 1; i < msgBodyArr.length; i++) {
				if (!StringUtils.isEmpty(msgBodyArr[i])) {

					String[] msgContentArr = msgBodyArr[i].split("\\=");
					if ((msgContentArr != null) && (msgContentArr.length >= 2)) {

						dataMap.put(msgContentArr[0], msgContentArr[1]);
					}
				}
			}
			String reqIdStr = (String) dataMap.get("reqId");
			try {
				if (StringUtils.isNotEmpty(reqIdStr)) {
					this.reqId = Long.parseLong(reqIdStr);
				}
			} catch (NumberFormatException e) {
				LOGGER.error("parseByteData reqId not correct. reqId=" + reqIdStr);
			}
			parseMsgContent(dataMap);
		}
	}

	protected abstract void parseMsgContent(Map<String, String> paramMap);

	public abstract String getMsgContent();

	public abstract int getMsgType();

	protected boolean isContainMsgTypeName() {
		return true;
	}

	public String getMsgTypeName() {
		int msgType = getMsgType();
		return OMCMsgFactory.getInstance().getOMCMsgTypeName(msgType);
	}

	public long getNewReqId() {
		this.reqId = generateSequence();
		return this.reqId;
	}

	public long getReqId() {
		return this.reqId;
	}

	public void setReqId(long _reqId) {
		this.reqId = _reqId;
	}

	public long getSequence() {
		return generateSequence();
	}

	public static synchronized long generateSequence() {
		if (curSeq == 2147483647L) {
			curSeq = 0L;
		}
		return curSeq++;
	}

	public String toString() {
		return getMsgContent();
	}
}
