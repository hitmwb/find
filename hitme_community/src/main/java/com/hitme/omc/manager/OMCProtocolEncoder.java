package com.hitme.omc.manager;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.hitme.omc.msg.IMessage;

public class OMCProtocolEncoder extends ProtocolEncoderAdapter {
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		if (!(message instanceof IMessage)) {
			return;
		}
		IMessage msg = (IMessage) message;
		byte[] bytes = msg.getBytes();
		IoBuffer buffer = IoBuffer.allocate(bytes.length, false);
		buffer.setAutoExpand(true);
		buffer.put(bytes);
		buffer.flip();
		out.write(buffer);
	}
}
