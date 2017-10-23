package com.hitme.omc.rest;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitme.omc.model.AlarmInfo;
import com.hitme.omc.service.AlarmService;
import com.hitme.omc.util.LogProxy;

@RestController
public class AlarmRest {
	private static final LogProxy LOGGER = new LogProxy(AlarmRest.class);

	@Autowired
	private AlarmService alarmService;

	@RequestMapping(value = { "/alarm/create" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public Map<String, Object> createAlarmInfo(@RequestParam("alarmId") int alarmId,
			@RequestParam("alarmCode") String alarmCode, @RequestParam("alarmName") String alarmName,
			@RequestParam("createFlag") int createFlag, @RequestParam("createTime") String createTime,
			@RequestParam("neValue") String neValue, @RequestParam("neName") String neName,
			@RequestParam("objValue") String objValue, @RequestParam("objName") String objName,
			@RequestParam("objType") String objType, @RequestParam("alarmLevel") int alarmLevel,
			@RequestParam("alarmType") String alarmType, @RequestParam("alarmReason") String alarmReason,
			@RequestParam(value = "memo", required = false) String memo,
			@RequestParam("dataSource") String dataSource) {
		LOGGER.info("report a new alarm. alarmName=" + alarmName);
		String errCode = "000000";
		AlarmInfo alarm = new AlarmInfo();
		alarm.setAlarmId(alarmId);
		alarm.setAlarmCode(alarmCode);
		alarm.setAlarmName(alarmName);
		alarm.setCreateFlag(createFlag);
		alarm.setCreateTime(createTime);
		if (createFlag == 0) {
			alarm.setRecoveryTime(createTime);
		}
		alarm.setObjValue(objValue);
		alarm.setObjName(objName);
		alarm.setObjType(objType);
		alarm.setNeValue(neValue);
		alarm.setNeName(neName);
		alarm.setNeType(objType);
		alarm.setAlarmLevel(alarmLevel);
		alarm.setAlarmType(alarmType);
		alarm.setAlarmReason(alarmReason);
		alarm.setMemo(memo);
		alarm.setDataSource(dataSource);
		this.alarmService.createAlarm(alarm);
		Map<String, Object> rstMap = new HashMap<String, Object>();
		rstMap.put("errCode", errCode);
		rstMap.put("errDesc", "Success");
		return rstMap;
	}
}
