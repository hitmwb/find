package com.hitme.omc.model;

import java.util.Map;

import com.hitme.omc.util.CommonTools;
import com.hitme.omc.util.TimeUtil;

public class AlarmInfo {
	private long alarmSeq;
	private int alarmId;
	private String alarmCode;
	private String alarmName;
	private int createFlag;
	private String createTime;
	private String objValue;
	private String objName;
	private String objType;
	private String neValue;
	private String neName;
	private String neType;
	private String neLocation;
	private int alarmLevel;
	private String alarmType;
	private String alarmReason;
	private String dataSource;
	private String memo;
	private String recoveryTime;

	public long getAlarmSeq() {
		return this.alarmSeq;
	}

	public void setAlarmSeq(long alarmSeq) {
		this.alarmSeq = alarmSeq;
	}

	public int getAlarmId() {
		return this.alarmId;
	}

	public void setAlarmId(int alarmId) {
		this.alarmId = alarmId;
	}

	public String getAlarmCode() {
		return this.alarmCode;
	}

	public void setAlarmCode(String alarmCode) {
		this.alarmCode = alarmCode;
	}

	public String getAlarmName() {
		return this.alarmName;
	}

	public void setAlarmName(String alarmName) {
		this.alarmName = alarmName;
	}

	public int getCreateFlag() {
		return this.createFlag;
	}

	public void setCreateFlag(int createFlag) {
		this.createFlag = createFlag;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getObjValue() {
		return this.objValue;
	}

	public void setObjValue(String objValue) {
		this.objValue = objValue;
	}

	public String getObjName() {
		return this.objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

	public String getObjType() {
		return this.objType;
	}

	public void setObjType(String objType) {
		this.objType = objType;
	}

	public String getNeLocation() {
		return this.neLocation;
	}

	public void setNeLocation(String neLocation) {
		this.neLocation = neLocation;
	}

	public String getNeValue() {
		return this.neValue;
	}

	public void setNeValue(String neValue) {
		this.neValue = neValue;
	}

	public String getNeName() {
		return this.neName;
	}

	public void setNeName(String neName) {
		this.neName = neName;
	}

	public String getNeType() {
		return this.neType;
	}

	public void setNeType(String neType) {
		this.neType = neType;
	}

	public int getAlarmLevel() {
		return this.alarmLevel;
	}

	public void setAlarmLevel(int alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	public String getAlarmType() {
		return this.alarmType;
	}

	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}

	public String getAlarmReason() {
		return this.alarmReason;
	}

	public void setAlarmReason(String alarmReason) {
		this.alarmReason = alarmReason;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getDataSource() {
		return this.dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getRecoveryTime() {
		return this.recoveryTime;
	}

	public void setRecoveryTime(String recoveryTime) {
		this.recoveryTime = recoveryTime;
	}

	public static AlarmInfo wrap(Map<String, Object> rst) {
		AlarmInfo alarmInfo = new AlarmInfo();
		alarmInfo.setAlarmSeq(CommonTools.getLong(rst, "alarm_seq"));
		alarmInfo.setAlarmId(CommonTools.getInt(rst, "alarm_id"));
		alarmInfo.setAlarmCode(CommonTools.getCommonValue(rst, "alarm_code"));
		alarmInfo.setAlarmName(CommonTools.getCommonValue(rst, "alarm_name"));
		alarmInfo.setCreateTime(TimeUtil.getFormatTime(CommonTools.getLong(rst, "create_time"), "yyyy-MM-dd HH:mm:ss"));
		alarmInfo.setObjValue(CommonTools.getCommonValue(rst, "obj_value"));
		alarmInfo.setObjName(CommonTools.getCommonValue(rst, "obj_name"));
		alarmInfo.setObjType(CommonTools.getCommonValue(rst, "obj_type"));
		alarmInfo.setNeValue(CommonTools.getCommonValue(rst, "ne_value"));
		alarmInfo.setNeName(CommonTools.getCommonValue(rst, "ne_name"));
		alarmInfo.setNeType(CommonTools.getCommonValue(rst, "ne_type"));
		alarmInfo.setNeLocation(CommonTools.getCommonValue(rst, "ne_location"));
		alarmInfo.setAlarmLevel(CommonTools.getInt(rst, "alarm_level"));
		alarmInfo.setAlarmType(CommonTools.getCommonValue(rst, "alarm_type"));
		alarmInfo.setAlarmReason(CommonTools.getCommonValue(rst, "alarm_reason"));
		alarmInfo.setDataSource(CommonTools.getCommonValue(rst, "data_source"));
		alarmInfo.setMemo(CommonTools.getCommonValue(rst, "memo"));
		alarmInfo.setRecoveryTime(
				TimeUtil.getFormatTime(CommonTools.getLong(rst, "recovery_time"), "yyyy-MM-dd HH:mm:ss"));
		return alarmInfo;
	}
}
