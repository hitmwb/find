package com.hitme.omc.server;

import java.io.IOException;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hitme.omc.handler.AlarmReportHandler;
import com.hitme.omc.handler.TCPSessionManager;
import com.hitme.omc.manager.ConfigManager;
import com.hitme.omc.util.LogProxy;

@Component
public class TCPServerStarter {
	private static final LogProxy LOGGER = new LogProxy(TCPServerStarter.class);

	@Autowired
	private ConfigManager configManager;

	@Autowired
	private TCPSessionManager tcpSessionManager;

	@Autowired
	private AlarmReportHandler reportHandler;

	@PostConstruct
	public void start() {
		TCPServer tcpServer = new TCPServer(this.configManager.getOmcIp(), this.configManager.getOmcPort());
		this.tcpSessionManager.setServerName("tcp");
		this.tcpSessionManager.setIdleTimeOutInMilli(this.configManager.getIdleTimeout() * 1000);

		try {
			tcpServer.start(this.tcpSessionManager, this.configManager.getIdleTimeout() * 1000);
		} catch (IOException e) {
			LOGGER.error("start tcp inbound endpoint failed.", e);
		}

		new Thread(this.reportHandler).start();
	}
}
