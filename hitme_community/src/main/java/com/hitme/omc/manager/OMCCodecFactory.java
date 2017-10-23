package com.hitme.omc.manager;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class OMCCodecFactory implements ProtocolCodecFactory {
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return new OMCProtocolEncoder();
	}

	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return new OMCProtocolDecoder();
	}
}
