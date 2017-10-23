package com.hitme.omc.client;

import java.util.TimerTask;

import com.hitme.omc.exception.SendMsgException;
import com.hitme.omc.msg.OMCMessage;
import com.hitme.omc.msg.OMCMsgFactory;
import com.hitme.omc.util.LogProxy;

public class HeartBeatTask extends TimerTask {
	private static final LogProxy LOGGER = new LogProxy(HeartBeatTask.class);

	private TCPClientSessionManager tcpClientSessionManager;

	public HeartBeatTask(TCPClientSessionManager tcpClientSessionManager) {
		this.tcpClientSessionManager = tcpClientSessionManager;
	}

	public void run() {
		OMCMessage imsg = OMCMsgFactory.getInstance().createHeartBeatReqMsg();
		try {
			this.tcpClientSessionManager.sendMessage(imsg);
		} catch (SendMsgException e) {
			LOGGER.error("heart beat error", e);
		}
	}
}
