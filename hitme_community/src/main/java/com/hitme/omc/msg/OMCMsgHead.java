package com.hitme.omc.msg;

import com.hitme.omc.exception.ParseException;
import com.hitme.omc.util.ProtocolUtil;

public class OMCMsgHead {
	private static final byte[] START_SIGN = { -1, -1 };

	private int msgType;

	private long timeStamp;

	private int lenOfBody;

	public OMCMsgHead() {
		this.timeStamp = getTimeStamp();
	}

	public OMCMsgHead(int msgType) {
		this.msgType = msgType;
		this.timeStamp = getTimeStamp();
	}

	public byte[] getByteData() {
		byte[] headByte = new byte[9];
		System.arraycopy(START_SIGN, 0, headByte, 0, 2);
		headByte[2] = ((byte) this.msgType);
		System.arraycopy(ProtocolUtil.cnvrtLong4Bytes(this.timeStamp), 0, headByte, 3, 4);
		System.arraycopy(ProtocolUtil.cnvrtLong2Bytes(this.lenOfBody, 2), 0, headByte, 7, 2);
		return headByte;
	}

	public void parseData(byte[] headByte) throws ParseException {
		if ((headByte == null) || (headByte.length < 9)) {
			throw new ParseException("parse data error, length not correct");
		}
		this.msgType = headByte[2];
		byte[] lenBodyByte = new byte[2];
		lenBodyByte[0] = headByte[7];
		lenBodyByte[1] = headByte[8];
		this.lenOfBody = ProtocolUtil.cnvrtByte2Int(lenBodyByte);
	}

	public int getMsgType() {
		return this.msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public long getTimeStamp() {
		this.timeStamp = (System.currentTimeMillis() / 1000L);
		return this.timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public int getLenOfBody() {
		return this.lenOfBody;
	}

	public void setLenOfBody(int lenOfBody) {
		this.lenOfBody = lenOfBody;
	}

	public String toString() {
		return "startSign = FFFF, msgType=" + this.msgType + ", timeStamp=" + this.timeStamp + ", lenOfBody="
				+ this.lenOfBody;
	}
}
