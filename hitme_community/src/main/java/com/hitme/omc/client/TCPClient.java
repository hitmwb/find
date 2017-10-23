package com.hitme.omc.client;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.hitme.omc.exception.SendMsgException;
import com.hitme.omc.manager.OMCCodecFactory;
import com.hitme.omc.msg.OMCMessage;
import com.hitme.omc.msg.OMCMsgFactory;
import com.hitme.omc.server.IoProcessorPool;
import com.hitme.omc.util.LogProxy;
import com.hitme.omc.util.ThreadUtilities;

public class TCPClient implements Runnable {
	private static final LogProxy LOGGER = new LogProxy(TCPClient.class);

	private TCPClientSessionManager tcpClientSessionMessager;

	private String ip;

	private int port;

	private IoConnector connector;

	private int idleTimeoutInMilli;

	public TCPClient(String ip, int port, TCPClientSessionManager tcpClientSessionMessager, int _idleTimeoutInMilli) {
		this.ip = ip;
		this.port = port;
		this.tcpClientSessionMessager = tcpClientSessionMessager;
		this.idleTimeoutInMilli = _idleTimeoutInMilli;
	}

	public void run() {
		try {
			TimeUnit.SECONDS.sleep(15L);
		} catch (InterruptedException e) {
			LOGGER.error("sleep error", e);
		}
		init();
		boolean connState = connect();
		if (connState) {
			login("admin", "admin123", "msg");
		}
	}

	public void init() {
		LOGGER.info("TCPClient begin to init and connect...");
		this.connector = new NioSocketConnector(IoProcessorPool.getInstance().getServerIoProcessorPool());
		this.connector.setConnectTimeoutMillis(20000L);
		TCPClientHandler handler = new TCPClientHandler(this.tcpClientSessionMessager, this.idleTimeoutInMilli);
		this.connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new OMCCodecFactory()));
		this.connector.getFilterChain().addLast("threadPool",
				new ExecutorFilter(ThreadUtilities.getCommunicationService()));
		this.connector.setHandler(handler);
	}

	public boolean connect() {
		SocketAddress address = new InetSocketAddress(this.ip, this.port);
		ConnectFuture future = this.connector.connect(address);
		future.awaitUninterruptibly(10L, TimeUnit.SECONDS);
		if (!future.isConnected()) {
			LOGGER.error("TCPClient connect unsuccess." + future.getException());
			return false;
		}
		LOGGER.info("TCPClient: Connect success.");
		return true;
	}

	public void login(String user, String key, String type) {
		OMCMessage loginMsg = OMCMsgFactory.getInstance().createLoginReqMsg(user, key, type);
		try {
			this.tcpClientSessionMessager.sendMessage(loginMsg);
		} catch (SendMsgException e) {
			LOGGER.error("login error", e);
		}
	}

	public void closeConn() {
		OMCMessage closeMsg = OMCMsgFactory.getInstance().createCloseConnAlarmMsg();
		try {
			this.tcpClientSessionMessager.sendMessage(closeMsg);
			this.tcpClientSessionMessager.clearSession();
		} catch (SendMsgException e) {
			LOGGER.error("close conn error", e);
		}
	}
}
