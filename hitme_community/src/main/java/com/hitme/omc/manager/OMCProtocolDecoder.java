package com.hitme.omc.manager;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.hitme.omc.exception.ParseException;
import com.hitme.omc.msg.IMessage;
import com.hitme.omc.msg.OMCMessage;

public class OMCProtocolDecoder extends CumulativeProtocolDecoder {
	public boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		IMessage recvMsg = null;
		try {
			byte[] bytes = new byte[in.limit()];
			in.get(bytes);
			recvMsg = new OMCMessage();
			recvMsg.parseBytaData(bytes);
		} catch (ParseException e) {
			throw e;
		}
		out.write(recvMsg);
		return true;
	}
}
