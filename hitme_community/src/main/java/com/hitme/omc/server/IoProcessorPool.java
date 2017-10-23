package com.hitme.omc.server;

import org.apache.mina.core.service.IoProcessor;
import org.apache.mina.transport.socket.nio.NioSession;

import com.hitme.omc.util.ThreadUtilities;

public class IoProcessorPool {
	private static class Instance {
		private static final IoProcessorPool INS = new IoProcessorPool();
	}

	public static IoProcessorPool getInstance() {
		return Instance.INS;
	}

	public IoProcessor<NioSession> getServerIoProcessorPool() {
		return ThreadUtilities.getIoProcessorPool();
	}

	public IoProcessor<NioSession> getClientIoProcessorPool() {
		return ThreadUtilities.getIoProcessorPool();
	}
}
