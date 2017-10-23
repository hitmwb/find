package com.hitme.omc.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.hitme.omc.model.AlarmInfo;
import com.hitme.omc.util.TimeUtil;

@Service
public class AlarmJdbcDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public AlarmInfo query(long alarmSeq) {
		Map<String, Object> alarmMap = this.jdbcTemplate.queryForMap("select * from t_alarm where alarm_seq = ?",
				new Object[] { Long.valueOf(alarmSeq) });
		if (CollectionUtils.isEmpty(alarmMap)) {
			return null;
		}
		return AlarmInfo.wrap(alarmMap);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AlarmInfo query(int alarmId) {
		String sql = "select * from t_alarm where alarm_id = ? ";
		List<Map<String, Object>> alarmMapList = this.jdbcTemplate.queryForList(sql,
				new Object[] { Integer.valueOf(alarmId) });
		if (CollectionUtils.isEmpty(alarmMapList)) {
			return null;
		}
		Map<String, Object> alarmMap = (Map) alarmMapList.get(0);
		if (CollectionUtils.isEmpty(alarmMap)) {
			return null;
		}
		return AlarmInfo.wrap(alarmMap);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AlarmInfo query(String neId, String objId, String alarmCode) {
		Object[] param = { neId, alarmCode };
		String sql = "select * from t_alarm where ne_value = ? and alarm_code = ?";
		if (StringUtils.isNotEmpty(objId)) {
			sql = sql + " and obj_value = ? ";
			param = new Object[] { neId, alarmCode, objId };
		}
		List<Map<String, Object>> alarmMapList = this.jdbcTemplate.queryForList(sql, param);
		if (CollectionUtils.isEmpty(alarmMapList)) {
			return null;
		}
		Map<String, Object> alarmMap = (Map) alarmMapList.get(0);
		if (CollectionUtils.isEmpty(alarmMap)) {
			return null;
		}
		return AlarmInfo.wrap(alarmMap);
	}

	public List<AlarmInfo> queryCurrAlarmList(long beginTime, long endTime) {
		List<Map<String, Object>> alarmMapList = this.jdbcTemplate.queryForList(
				"select * from t_alarm where create_time >= ? and create_time <= ?",
				new Object[] { Long.valueOf(beginTime), Long.valueOf(endTime) });
		if (CollectionUtils.isEmpty(alarmMapList)) {
			return null;
		}
		List<AlarmInfo> alarmList = new ArrayList<AlarmInfo>();
		for (Map<String, Object> alarmMap : alarmMapList) {
			alarmList.add(AlarmInfo.wrap(alarmMap));
		}
		return alarmList;
	}

	public int insert(AlarmInfo alarmInfo) {
		String sql = "insert into t_alarm(alarm_seq, alarm_id, alarm_code, alarm_name, alarm_level,  alarm_type, alarm_reason, create_time, obj_value, obj_name, obj_type,  ne_value, ne_name, ne_type, ne_location, data_source, memo)  values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		int count = this.jdbcTemplate.update(sql, new Object[] { Long.valueOf(alarmInfo.getAlarmSeq()),
				Integer.valueOf(alarmInfo.getAlarmId()), alarmInfo.getAlarmCode(), alarmInfo.getAlarmName(),
				Integer.valueOf(alarmInfo.getAlarmLevel()), alarmInfo.getAlarmType(), alarmInfo.getAlarmReason(),
				Long.valueOf(TimeUtil.getTime(alarmInfo.getCreateTime())), alarmInfo.getObjValue(),
				alarmInfo.getObjName(), alarmInfo.getObjType(), alarmInfo.getNeValue(), alarmInfo.getNeName(),
				alarmInfo.getNeType(), alarmInfo.getNeLocation(), alarmInfo.getDataSource(), alarmInfo.getMemo() });
		return count;
	}

	public int insertLog(AlarmInfo alarmInfo) {
		String sql = "insert into t_alarm_log(alarm_seq, alarm_id, alarm_code, alarm_name, alarm_level,  alarm_type, alarm_reason, create_time, obj_value, obj_name, obj_type,  ne_value, ne_name, ne_type, ne_location, create_flag, data_source, memo)  values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		int count = this.jdbcTemplate.update(sql,
				new Object[] { Long.valueOf(alarmInfo.getAlarmSeq()), Integer.valueOf(alarmInfo.getAlarmId()),
						alarmInfo.getAlarmCode(), alarmInfo.getAlarmName(), Integer.valueOf(alarmInfo.getAlarmLevel()),
						alarmInfo.getAlarmType(), alarmInfo.getAlarmReason(),
						Long.valueOf(TimeUtil.getTime(alarmInfo.getCreateTime())), alarmInfo.getObjValue(),
						alarmInfo.getObjName(), alarmInfo.getObjType(), alarmInfo.getNeValue(), alarmInfo.getNeName(),
						alarmInfo.getNeType(), alarmInfo.getNeLocation(), Integer.valueOf(alarmInfo.getCreateFlag()),
						alarmInfo.getDataSource(), alarmInfo.getMemo() });
		return count;
	}

	public int delete(long alarmSeq) {
		String sql = "delete from t_alarm where alarm_seq = ?";
		int count = this.jdbcTemplate.update(sql, new Object[] { Long.valueOf(alarmSeq) });
		return count;
	}
}
