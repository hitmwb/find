package com.hitme.omc.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.hitme.omc.handler.MessageHandler;
import com.hitme.omc.manager.OMCCodecFactory;
import com.hitme.omc.util.LogProxy;
import com.hitme.omc.util.ThreadUtilities;

public class TCPServer {
	private static final LogProxy LOGGER = new LogProxy(TCPServer.class);
	private String ip;
	private int port;

	public TCPServer(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public void start(MessageHandler messageHandle, int idleTimeoutInMilli) throws IOException {
		NioSocketAcceptor acceptor = new NioSocketAcceptor(IoProcessorPool.getInstance().getServerIoProcessorPool());
		TCPServerHandler tcpServerHandler = new TCPServerHandler(messageHandle, idleTimeoutInMilli);
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new OMCCodecFactory()));
		acceptor.getFilterChain().addLast("threadPool", new ExecutorFilter(ThreadUtilities.getCommunicationService()));
		acceptor.setHandler(tcpServerHandler);
		acceptor.getSessionConfig().setReceiveBufferSize(65536);
		acceptor.getSessionConfig().setReuseAddress(true);
		acceptor.bind(new InetSocketAddress(this.ip, this.port));
		LOGGER.info("start tcp server ip is " + this.ip + " ,port is " + this.port);
	}
}
