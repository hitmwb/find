package com.hitme.omc.handler;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public abstract interface IMessageHandler {
	public abstract void sessionCreated(IoSession paramIoSession);

	public abstract void sessionOpened(IoSession paramIoSession);

	public abstract void messageReceived(IoSession paramIoSession, Object paramObject);

	public abstract void sessionClosed(IoSession paramIoSession);

	public abstract void sessionIdle(IoSession paramIoSession, IdleStatus paramIdleStatus);

	public abstract boolean isConnected(String paramString);

	public abstract void exceptionCaught(IoSession paramIoSession);
}
