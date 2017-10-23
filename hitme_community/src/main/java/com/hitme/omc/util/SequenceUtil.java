package com.hitme.omc.util;

import com.hitme.omc.db.RedisClientTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SequenceUtil {
	@Autowired
	private RedisClientTemplate redis;

	public long incrementSeq(String key) {
		return this.redis.incr(key).longValue();
	}

	public long decrementSeq(String key) {
		return this.redis.decr(key).longValue();
	}

	public void setCurrentSeq(String key, long seq) {
		this.redis.set(key, String.valueOf(seq));
	}

	public long queryCurrentSeq(String key) {
		String value = this.redis.get(key);
		if (StringUtils.isEmpty(value)) {
			return 0L;
		}
		return Long.parseLong(value);
	}
}
