package com.hitme.omc.msg;

import com.hitme.omc.exception.ParseException;
import com.hitme.omc.util.LogProxy;
import com.hitme.omc.util.ProtocolUtil;

public class OMCMessage implements IMessage {
	private static final LogProxy LOGGER = new LogProxy(OMCMessage.class);

	private OMCMsgHead head;

	private OMCMsgBody body;

	public OMCMessage() {
	}

	public OMCMessage(int msgType) {
		this.head = new OMCMsgHead(msgType);
	}

	public long getReqId() {
		return this.body.getReqId();
	}

	public byte[] getBytes() {
		if ((this.head == null) || (this.body == null)) {
			return new byte[0];
		}
		byte[] bodyByte = this.body.getBytaData();
		this.head.setLenOfBody(bodyByte.length);
		return ProtocolUtil.joinByteArray(new byte[][] { this.head.getByteData(), bodyByte });
	}

	public void parseBytaData(byte[] byteData) throws ParseException {
		if ((byteData == null) || (byteData.length < 10)) {
			throw new ParseException("omc message parse error. length<10");
		}
		byte[] headByte = new byte[9];
		System.arraycopy(byteData, 0, headByte, 0, 9);
		this.head = new OMCMsgHead();
		this.head.parseData(headByte);
		int msgType = this.head.getMsgType();
		this.body = OMCMsgFactory.getInstance().getOMCMsg(msgType);
		if (this.body == null) {
			LOGGER.error("parseBytaData error, body is null. msgType=" + msgType);
			return;
		}
		byte[] bodyByte = new byte[byteData.length - 9];
		System.arraycopy(byteData, 9, bodyByte, 0, byteData.length - 9);
		this.body.parseByteData(bodyByte);
	}

	public OMCMsgHead getHead() {
		return this.head;
	}

	public void setHead(OMCMsgHead head) {
		this.head = head;
	}

	public OMCMsgBody getBody() {
		return this.body;
	}

	public void setBody(OMCMsgBody body) {
		this.body = body;
	}

	public String toString() {
		return "head=[" + this.head.toString() + "],body=[" + this.body.toString() + "]";
	}

	public int getMsgType() {
		return this.body.getMsgType();
	}
}
