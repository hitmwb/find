package com.hitme.omc.handler;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.hitme.omc.util.LogProxy;

public abstract class MessageHandler implements IMessageHandler {
	private static final LogProxy LOGGER = new LogProxy(MessageHandler.class);
	private String serverName;
	private int idleTimeOutInMilli;

	public int getIdleTimeOutInMilli() {
		return this.idleTimeOutInMilli;
	}

	public void setIdleTimeOutInMilli(int idleTimeOutInMilli) {
		this.idleTimeOutInMilli = idleTimeOutInMilli;
	}

	public String getServerName() {
		return this.serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public void sessionOpened(IoSession session) {
		LOGGER.info("session opened, session=" + session);
	}

	public abstract void messageReceived(IoSession paramIoSession, Object paramObject);

	public void sessionClosed(IoSession session) {
		if (session != null) {
			session.closeNow();
		}
	}

	public void sessionIdle(IoSession session, IdleStatus status) {
		if (session == null) {
			return;
		}
		sessionClosed(session);
	}

	public boolean isConnected(String ipAddress) {
		return false;
	}

	public void exceptionCaught(IoSession session) {
		LOGGER.error("tcp exceptionCaught:" + session);
	}

	public abstract void sessionCreated(IoSession paramIoSession);
}
