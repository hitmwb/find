package com.hitme.omc.handler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hitme.omc.exception.SendMsgException;
import com.hitme.omc.model.OMCAlarmInfo;
import com.hitme.omc.msg.OMCMessage;
import com.hitme.omc.msg.OMCMsgFactory;
import com.hitme.omc.util.LogProxy;
import com.hitme.omc.util.SequenceUtil;

@Component
public class AlarmReportHandler implements Runnable {
	private static final LogProxy LOGGER = new LogProxy(AlarmReportHandler.class);

	private LinkedBlockingQueue<OMCAlarmInfo> alarmReportQueue = new LinkedBlockingQueue<OMCAlarmInfo>();

	private ArrayBlockingQueue<String> blockQueue = new ArrayBlockingQueue<String>(1);

	@Autowired
	private SequenceUtil seqUtil;

	@Autowired
	private TCPSessionManager tcpSessionManager;

	private volatile long alarmSeqCurr;

	private int status = 1;

	private OMCAlarmInfo pauseAlarm;

	public void run() {
		this.alarmSeqCurr = this.seqUtil.queryCurrentSeq("AlarmReportSeq");
		LOGGER.info("alarmSeqCurr = " + this.alarmSeqCurr);
		while (this.status != -1) {

			if (this.status == 0) {
				try {
					String block = (String) this.blockQueue.take();
					LOGGER.info("blockQueue block=" + block);
				} catch (InterruptedException e) {
					LOGGER.error("blockQueue error.", e);
				}

			} else {
				try {
					OMCAlarmInfo alarm = (OMCAlarmInfo) this.alarmReportQueue.take();

					if (this.status == 0) {
						this.pauseAlarm = alarm;
					} else {
						sendAlarm(alarm);
						this.alarmSeqCurr = alarm.getAlarmSeq();
						this.seqUtil.setCurrentSeq("AlarmReportSeq", this.alarmSeqCurr);
					}
				} catch (InterruptedException | SendMsgException e) {
					LOGGER.error("take alarm from queue to send error.", e);
				}
			}
		}
	}

	public void sendAlarm(OMCAlarmInfo alarm) throws SendMsgException {
		OMCMessage omcMsg = null;
		if (this.pauseAlarm != null) {
			LOGGER.info("first send the parse alarm, alarmId=" + this.pauseAlarm.getAlarmId());
			omcMsg = OMCMsgFactory.getInstance().createRealTimeAlarm(this.pauseAlarm.getJson());
			this.tcpSessionManager.sendMessage(omcMsg);
			this.pauseAlarm = null;
		}
		LOGGER.info("take a alarm msg to send, alarmId=" + alarm.getAlarmId());
		omcMsg = OMCMsgFactory.getInstance().createRealTimeAlarm(alarm.getJson());
		this.tcpSessionManager.sendMessage(omcMsg);
	}

	public synchronized void resume() {
		LOGGER.info("current alarm thread resume.");
		this.status = 1;
		this.blockQueue.offer("Thread resume");
	}

	public synchronized void suspend() {
		LOGGER.info("current alarm thread suspend.");
		this.status = 0;
		this.blockQueue.clear();
	}

	public void realse() {
		setStatus(-1);
		this.alarmReportQueue.clear();
		this.alarmReportQueue = null;
		this.blockQueue.clear();
		this.blockQueue = null;
	}

	public void addNewAlarm(OMCAlarmInfo alarm) {
		this.alarmReportQueue.add(alarm);
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getAlarmSeqCurr() {
		return this.alarmSeqCurr;
	}

	public void setAlarmSeqCurr(long alarmSeqCurr) {
		this.alarmSeqCurr = alarmSeqCurr;
	}
}
