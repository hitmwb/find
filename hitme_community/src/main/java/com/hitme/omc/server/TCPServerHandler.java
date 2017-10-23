package com.hitme.omc.server;

import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.hitme.omc.handler.IMessageHandler;
import com.hitme.omc.util.LogProxy;
import com.hitme.omc.util.TimeUtil;

public class TCPServerHandler extends IoHandlerAdapter {
	private static final LogProxy LOGGER = new LogProxy(TCPServerHandler.class);
	private IMessageHandler messageHandle;
	private int idleTimeoutInMilli;

	public TCPServerHandler(IMessageHandler messageHandle, int idleTimeoutInMilli) {
		this.messageHandle = messageHandle;
		this.idleTimeoutInMilli = idleTimeoutInMilli;
	}

	public void exceptionCaught(IoSession session, Throwable cause) {
		if ((cause != null) && (!StringUtils.contains(cause.getMessage(), "Connection reset by peer"))) {
			LOGGER.warn("An exception occurred. Session=" + cause.getMessage());
		}
		if (this.messageHandle != null) {
			this.messageHandle.exceptionCaught(session);
		} else {
			closeSession(session);
		}
	}

	public void sessionOpened(IoSession session) {
		session.getConfig().setBothIdleTime(this.idleTimeoutInMilli / 1000);
		if (this.messageHandle != null) {
			this.messageHandle.sessionOpened(session);
		}
	}

	public void sessionClosed(IoSession session) {
		if (this.messageHandle != null) {
			this.messageHandle.sessionClosed(session);
		}
	}

	public void sessionCreated(IoSession session) {
		if (this.messageHandle != null) {
			this.messageHandle.sessionCreated(session);
		}
		session.getConfig().setBothIdleTime(this.idleTimeoutInMilli / 1000);
		LOGGER.info("session open create time : " + TimeUtil.getCurrentTime());
	}

	public void messageReceived(IoSession session, Object message) {
		if (this.messageHandle != null) {
			this.messageHandle.messageReceived(session, message);
		}
	}

	public void sessionIdle(IoSession session, IdleStatus status) {
		LOGGER.info("An session idle occurred. time :" + TimeUtil.getCurrentTime());
		if (this.messageHandle != null) {
			this.messageHandle.sessionIdle(session, status);
		} else {
			closeSession(session);
		}
	}

	private void closeSession(IoSession session) {
		try {
			session.closeNow();
		} catch (RuntimeException exception) {
			LOGGER.error("close session failed", exception);
		}
	}
}
