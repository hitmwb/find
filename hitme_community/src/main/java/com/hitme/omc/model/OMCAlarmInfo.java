package com.hitme.omc.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class OMCAlarmInfo {

	private long alarmSeq;

	private String alarmTitle;

	private int alarmStatus;

	private String alarmType;

	private int origSeverity;

	private String eventTime;

	private String alarmId;

	private String specificProblemID;

	private String specificProblem;

	private String neUID;

	private String neName;

	private String neType;

	private String objectUID;

	private String objectName;

	private String objectType;

	private String locationInfo;

	private String addInfo;

	public long getAlarmSeq() {
		return this.alarmSeq;
	}

	public void setAlarmSeq(long alarmSeq) {
		this.alarmSeq = alarmSeq;
	}

	public String getJson() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}

	public String getAlarmTitle() {
		return this.alarmTitle;
	}

	public void setAlarmTitle(String alarmTitle) {
		this.alarmTitle = alarmTitle;
	}

	public int getAlarmStatus() {
		return this.alarmStatus;
	}

	public void setAlarmStatus(int alarmStatus) {
		this.alarmStatus = alarmStatus;
	}

	public String getAlarmType() {
		return this.alarmType;
	}

	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}

	public int getOrigSeverity() {
		return this.origSeverity;
	}

	public void setOrigSeverity(int origSeverity) {
		this.origSeverity = origSeverity;
	}

	public String getEventTime() {
		return this.eventTime;
	}

	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}

	public String getAlarmId() {
		return this.alarmId;
	}

	public void setAlarmId(String alarmId) {
		this.alarmId = alarmId;
	}

	public String getSpecificProblemID() {
		return this.specificProblemID;
	}

	public void setSpecificProblemID(String specificProblemID) {
		this.specificProblemID = specificProblemID;
	}

	public String getSpecificProblem() {
		return this.specificProblem;
	}

	public void setSpecificProblem(String specificProblem) {
		this.specificProblem = specificProblem;
	}

	public String getNeUID() {
		return this.neUID;
	}

	public void setNeUID(String neUID) {
		this.neUID = neUID;
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

	public String getObjectUID() {
		return this.objectUID;
	}

	public void setObjectUID(String objectUID) {
		this.objectUID = objectUID;
	}

	public String getObjectName() {
		return this.objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getObjectType() {
		return this.objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getLocationInfo() {
		return this.locationInfo;
	}

	public void setLocationInfo(String locationInfo) {
		this.locationInfo = locationInfo;
	}

	public String getAddInfo() {
		return this.addInfo;
	}

	public void setAddInfo(String addInfo) {
		this.addInfo = addInfo;
	}
}
