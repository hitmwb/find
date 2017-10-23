package com.hitme.omc.client;

import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.hitme.omc.msg.IMessage;
import com.hitme.omc.util.LogProxy;
import com.hitme.omc.util.TimeUtil;

public class TCPClientHandler extends IoHandlerAdapter {
	private static final LogProxy LOGGER = new LogProxy(TCPClientHandler.class);

	private TCPClientSessionManager messageHandle;

	private int idleTimeoutInMilli;

	public TCPClientHandler(TCPClientSessionManager sessionManager, int idleTimeoutInMilli) {
		this.messageHandle = sessionManager;
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
	}

	public void sessionClosed(IoSession session) {
		if (this.messageHandle != null) {
			this.messageHandle.clearSession(session);
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
			this.messageHandle.messageReceived(session, (IMessage) message);
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
