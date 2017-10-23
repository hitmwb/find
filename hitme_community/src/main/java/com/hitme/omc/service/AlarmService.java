package com.hitme.omc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hitme.omc.dao.AlarmHistoryJdbcDao;
import com.hitme.omc.dao.AlarmJdbcDao;
import com.hitme.omc.dao.AlarmRedisDao;
import com.hitme.omc.handler.AlarmReportHandler;
import com.hitme.omc.handler.AlarmSyncFileHandler;
import com.hitme.omc.model.AlarmInfo;
import com.hitme.omc.model.OMCAlarmInfo;
import com.hitme.omc.util.SequenceUtil;
import com.hitme.omc.util.TimeUtil;

@Service
public class AlarmService {
	@Autowired
	private SequenceUtil sequenceUtil;
	@Autowired
	private AlarmRedisDao alarmRedisDao;
	@Autowired
	private AlarmJdbcDao alarmJdbcDao;
	@Autowired
	private AlarmHistoryJdbcDao alarmHistoryJdbcDao;
	@Autowired
	private AlarmReportHandler reportHandler;
	@Autowired
	private AlarmSyncFileHandler syncFileHandler;

	public int createAlarm(AlarmInfo alarmInfo) {
		if (alarmInfo == null) {
			return 0;
		}
		long seq = this.sequenceUtil.incrementSeq("AlarmSeq");
		alarmInfo.setAlarmSeq(seq);
		OMCAlarmInfo alarm = convert2OMCAlarm(alarmInfo);

		this.reportHandler.addNewAlarm(alarm);

		this.alarmRedisDao.add(alarm);

		this.alarmJdbcDao.insertLog(alarmInfo);

		AlarmInfo currAlarm = this.alarmJdbcDao.query(alarmInfo.getAlarmId());
		if ((currAlarm == null) && (alarmInfo.getCreateFlag() == 1)) {
			this.alarmJdbcDao.insert(alarmInfo);
		} else if ((currAlarm != null) && (alarmInfo.getCreateFlag() == 0)) {

			alarmInfo.setCreateTime(currAlarm.getCreateTime());
			this.alarmHistoryJdbcDao.insert(alarmInfo);
			this.alarmJdbcDao.delete(currAlarm.getAlarmSeq());
		}
		return 1;
	}

	public List<String> searchAlarmLog(long alarmSeq, long alarmReqCurr) {
		List<String> alarmList = new ArrayList<String>();
		if ((alarmSeq > alarmReqCurr) || (alarmReqCurr - alarmSeq > 1000L)) {
			return alarmList;
		}
		return this.alarmRedisDao.queryAlarmBySeq(alarmSeq, alarmReqCurr);
	}

	public List<String> searchAlarmLog(String beginTime, String endTime) {
		List<String> alarmList = new ArrayList<String>();
		long beginTimeSeq = 0L;
		long endTimeSeq = 0L;
		if (StringUtils.isEmpty(beginTime)) {
			beginTimeSeq = 0L;
		} else {
			beginTimeSeq = TimeUtil.getTime(beginTime);
		}
		if (StringUtils.isEmpty(endTime)) {
			endTimeSeq = TimeUtil.getCurrentTime();
		} else {
			endTimeSeq = TimeUtil.getTime(endTime);
		}
		Set<String> alarmSet = this.alarmRedisDao.queryAlarmByTimeSeq(beginTimeSeq, endTimeSeq);
		alarmList.addAll(alarmSet);
		return alarmList;
	}

	public List<String> searchAlarmCurr(String beginTime, String endTime) {
		List<AlarmInfo> alarmInfoList = null;
		long beginTimeSeq = 0L;
		long endTimeSeq = 0L;
		if (StringUtils.isEmpty(beginTime)) {
			beginTimeSeq = 0L;
		} else {
			beginTimeSeq = TimeUtil.getTime(beginTime);
		}
		if (StringUtils.isEmpty(endTime)) {
			endTimeSeq = TimeUtil.getCurrentTime();
		} else {
			endTimeSeq = TimeUtil.getTime(endTime);
		}
		alarmInfoList = this.alarmJdbcDao.queryCurrAlarmList(beginTimeSeq, endTimeSeq);
		List<String> alarmList = new ArrayList<String>();
		if (CollectionUtils.isEmpty(alarmInfoList)) {
			return alarmList;
		}
		for (AlarmInfo alarm : alarmInfoList) {
			OMCAlarmInfo omcAlarm = convert2OMCAlarm(alarm);
			alarmList.add(omcAlarm.getJson());
		}
		return alarmList;
	}

	public String syncAlarmFile(String startTime, String endTime, long alarmSeq, int syncSource, long currSeq) {
		String filePath = this.syncFileHandler.createAlarmFile(startTime, endTime, alarmSeq, syncSource, currSeq);
		return filePath;
	}

	public OMCAlarmInfo convert2OMCAlarm(AlarmInfo alarmInfo) {
		OMCAlarmInfo omcAlarm = new OMCAlarmInfo();
		omcAlarm.setAlarmSeq(alarmInfo.getAlarmSeq());
		omcAlarm.setAlarmId(String.valueOf(alarmInfo.getAlarmId()));
		omcAlarm.setAlarmStatus(alarmInfo.getCreateFlag());
		omcAlarm.setAlarmType(alarmInfo.getAlarmType());
		omcAlarm.setOrigSeverity(alarmInfo.getAlarmLevel());
		omcAlarm.setEventTime(alarmInfo.getCreateTime());
		omcAlarm.setAlarmTitle(alarmInfo.getAlarmName());
		omcAlarm.setSpecificProblemID(alarmInfo.getAlarmReason());
		omcAlarm.setSpecificProblem(alarmInfo.getAlarmReason());
		omcAlarm.setNeUID(alarmInfo.getNeValue());
		omcAlarm.setNeName(alarmInfo.getNeName());
		omcAlarm.setNeType(alarmInfo.getNeType());
		omcAlarm.setObjectUID(alarmInfo.getObjValue());
		omcAlarm.setObjectName(alarmInfo.getObjName());
		omcAlarm.setObjectType(alarmInfo.getObjType());
		omcAlarm.setLocationInfo(alarmInfo.getNeLocation());
		omcAlarm.setAddInfo(alarmInfo.getMemo());
		return omcAlarm;
	}
}
