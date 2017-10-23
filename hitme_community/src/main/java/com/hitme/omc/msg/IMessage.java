package com.hitme.omc.msg;

import com.hitme.omc.exception.ParseException;

public abstract interface IMessage {
	public abstract byte[] getBytes();

	public abstract void parseBytaData(byte[] paramArrayOfByte) throws ParseException;

	public abstract int getMsgType();

	public abstract long getReqId();
}
