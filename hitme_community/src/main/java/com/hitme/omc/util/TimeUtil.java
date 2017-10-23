package com.hitme.omc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.apache.commons.lang3.StringUtils;

public abstract class TimeUtil {
	private static final LogProxy logger = new LogProxy(TimeUtil.class);
	public static final String DATE_FORMAT1 = "yyyy-MM-dd HH:mm:ss";
	public static final String PERIOD_TIME_START = "00:00:00";
	public static final String PERIOD_TIME_END = "23:59:59";
	public static final String DEFAULT_BEGIN_TIME = "1970-01-01 00:00:00";
	public static final int TIME_UNIT_SECOND = 13;
	public static final int TIME_UNIT_MINUTES = 12;
	public static final int TIME_UNIT_HOUR = 11;
	public static final int TIME_UNIT_DAY = 5;
	private static final List<Integer> timeUnitList = new ArrayList<Integer>();
	public static final String TIMEZONE_UTC = "GMT";

	static {
		timeUnitList.add(Integer.valueOf(13));
		timeUnitList.add(Integer.valueOf(12));
		timeUnitList.add(Integer.valueOf(11));
		timeUnitList.add(Integer.valueOf(5));
	}

	public static String getCurrentStrTime() {
		return getCurrentStrTime("yyyy-MM-dd HH:mm:ss");
	}

	public static String getCurrentStrTime(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = new Date();
		String currentTime = sdf.format(date);

		return currentTime;
	}

	public static String getStrCurrentStrTime() {
		return getCurrentStrTime("yyyyMMddHHmmss");
	}

	public static String getCurrentStrTimeMills() {
		return getCurrentStrTime("yyyy-MM-dd HH:mm:ss:SSS");
	}

	public static String fomateTime(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);

		String currentTime = sdf.format(date);

		return currentTime;
	}

	public static long getCurrentTime() {
		return System.currentTimeMillis();
	}

	public static long getTime(String timeString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		long currentTime = 0L;
		try {
			currentTime = sdf.parse(timeString).getTime();
		} catch (ParseException e) {
			logger.error("COMMON_ERROR", e);
		}
		return currentTime;
	}

	public static boolean checkValidDate(String signTime, String expiresInStr) {
		if (StringUtils.isEmpty(expiresInStr)) {
			logger.error("checkValidDate expiresInStr is empty.");
			return false;
		}
		try {
			int expiresIn = Integer.parseInt(expiresInStr);
			return checkValidDate(signTime, expiresIn, 13);
		} catch (NumberFormatException e) {
			logger.error("checkValidDate NumberFormatException expiresInStr=" + expiresInStr);
		}
		return false;
	}

	public static boolean checkValidDate(String signTime, int expiresIn, int calTimeUnit) {
		if (StringUtils.isEmpty(signTime)) {
			logger.error("checkValidDate signTime is empty.");
			return false;
		}
		if (expiresIn <= 0) {
			logger.error("checkValidDate expiresIn is not correct.");
			return false;
		}
		if (!timeUnitList.contains(Integer.valueOf(calTimeUnit))) {
			logger.error("checkValidDate calTimeUnit is not correct.");
			return false;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date preDate = sdf.parse(signTime);
			return checkValidDate(preDate.getTime(), expiresIn, calTimeUnit);
		} catch (ParseException e) {
			logger.error("checkValidDate ParseException signTime=" + signTime);
		}
		return false;
	}

	public static boolean checkValidDate(long signTime, int expiresIn, int calTimeUnit) {
		if (signTime <= 0L) {
			logger.error("checkValidDate signTime is empty.");
			return false;
		}
		if (!timeUnitList.contains(Integer.valueOf(calTimeUnit))) {
			logger.error("checkValidDate calTimeUnit is not correct.");
			return false;
		}
		Date preDate = new Date(signTime);
		Calendar cal = Calendar.getInstance();
		cal.setTime(preDate);
		cal.add(calTimeUnit, expiresIn);
		if (cal.getTimeInMillis() >= System.currentTimeMillis()) {
			return true;
		}
		return false;
	}

	public static String getFormatTime(long time, String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return sdf.format(cal.getTime());
	}

	public static String getUTCTimeStr(long time) {
		TimeZone fromTimeZone = TimeZone.getTimeZone("GMT");
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT"));
		cal.setTimeInMillis(time);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		format.setTimeZone(fromTimeZone);
		String timeStr = format.format(cal.getTime());
		return timeStr;
	}

	public static boolean checkInPeriodTime(long currTime, long startTime, long endTime) {
		if ((currTime > endTime) || (currTime < startTime)) {
			return false;
		}
		String currTimeStr = getUTCTimeStr(currTime).substring(11);
		String startTimeStr = getUTCTimeStr(startTime).substring(11);
		String endTimeStr = getUTCTimeStr(endTime).substring(11);
		if (startTimeStr.compareTo(endTimeStr) <= 0) {
			if ((currTimeStr.compareTo(startTimeStr) >= 0) && (currTimeStr.compareTo(endTimeStr) <= 0)) {
				return true;
			}
		} else if (((currTimeStr.compareTo(startTimeStr) >= 0) && (currTimeStr.compareTo("23:59:59") <= 0))
				|| ((currTimeStr.compareTo("00:00:00") >= 0) && (currTimeStr.compareTo(endTimeStr) <= 0))) {
			return true;
		}
		return false;
	}
}
