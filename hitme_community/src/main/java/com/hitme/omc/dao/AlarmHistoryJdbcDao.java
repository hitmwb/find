package com.hitme.omc.dao;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.hitme.omc.model.AlarmInfo;
import com.hitme.omc.util.TimeUtil;

@Service
public class AlarmHistoryJdbcDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public AlarmInfo query(long alarmSeq) {
		Map<String, Object> alarmMap = this.jdbcTemplate.queryForMap(
				"select * from t_alarm_history where alarm_seq = ?", new Object[] { Long.valueOf(alarmSeq) });
		if (CollectionUtils.isEmpty(alarmMap)) {
			return null;
		}
		return AlarmInfo.wrap(alarmMap);
	}

	public int insert(AlarmInfo alarmInfo) {
		String sql = "insert into t_alarm_history(alarm_seq, alarm_id, alarm_code, alarm_name, alarm_level,  alarm_type, alarm_reason, create_time, obj_value, obj_name, obj_type,  ne_value, ne_name, ne_type, ne_location, data_source, memo, recovery_time)  values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		int count = this.jdbcTemplate.update(sql,
				new Object[] { Long.valueOf(alarmInfo.getAlarmSeq()), Integer.valueOf(alarmInfo.getAlarmId()),
						alarmInfo.getAlarmCode(), alarmInfo.getAlarmName(), Integer.valueOf(alarmInfo.getAlarmLevel()),
						alarmInfo.getAlarmType(), alarmInfo.getAlarmReason(),
						Long.valueOf(TimeUtil.getTime(alarmInfo.getCreateTime())), alarmInfo.getObjValue(),
						alarmInfo.getObjName(), alarmInfo.getObjType(), alarmInfo.getNeValue(), alarmInfo.getNeName(),
						alarmInfo.getNeType(), alarmInfo.getNeLocation(), alarmInfo.getDataSource(),
						alarmInfo.getMemo(), Long.valueOf(TimeUtil.getTime(alarmInfo.getRecoveryTime())) });
		return count;
	}
}
