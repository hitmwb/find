package com.hitme.omc.util;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class CommonTools {
	@SuppressWarnings("rawtypes")
	public static String getCommonValue(Map map, String key) {
		if ((map == null) || (StringUtils.isEmpty(key))) {
			return "";
		}
		Object valueObj = map.get(key);
		return valueObj == null ? "" : valueObj.toString();
	}

	@SuppressWarnings("rawtypes")
	public static int getInt(Map map, String key) {
		if ((map == null) || (StringUtils.isEmpty(key))) {
			return 0;
		}
		String value = getCommonValue(map, key);
		if (StringUtils.isNotEmpty(value)) {
			return Integer.parseInt(value);
		}
		return 0;
	}

	@SuppressWarnings("rawtypes")
	public static long getLong(Map map, String key) {
		if ((map == null) || (StringUtils.isEmpty(key))) {
			return 0L;
		}
		String value = getCommonValue(map, key);
		if (StringUtils.isNotEmpty(value)) {
			return Long.parseLong(value);
		}
		return 0L;
	}
}
