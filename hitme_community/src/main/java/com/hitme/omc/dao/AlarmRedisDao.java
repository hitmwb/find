package com.hitme.omc.dao;

import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.hitme.omc.db.RedisClientTemplate;
import com.hitme.omc.model.OMCAlarmInfo;
import com.hitme.omc.util.TimeUtil;

@Configuration
@EnableCaching
@Service("alarmRedisDao")
public class AlarmRedisDao {
	@Autowired
	private RedisClientTemplate redisClientTemplate;
	@Value("${redis.alarm.expire}")
	private int expire;

	public void add(OMCAlarmInfo alarmInfo) {
		String alarmJson = alarmInfo.getJson();

		add("Alarm:" + alarmInfo.getAlarmSeq(), alarmJson);

		String createTime = alarmInfo.getEventTime();
		long timeSeq = TimeUtil.getTime(createTime);
		this.redisClientTemplate.zadd("AlarmTime:", timeSeq, alarmJson);
	}

	public void add(String key, String alarmJson) {
		this.redisClientTemplate.setex(key, this.expire * 86400, alarmJson);
	}

	public List<String> queryAlarmBySeq(long alarmSeq, long alarmSeqCurr) {
		int len = (int) (alarmSeqCurr - alarmSeq + 1L);
		String[] keys = new String[len];
		for (long req = alarmSeq; req <= alarmSeqCurr; req += 1L) {
			int index = (int) (req - alarmSeq);
			keys[index] = ("Alarm:" + req);
		}
		return this.redisClientTemplate.mget(keys);
	}

	public Set<String> queryAlarmByTimeSeq(long beginTime, long endTime) {
		return this.redisClientTemplate.zrangeByScore("AlarmTime:", beginTime, endTime);
	}
}
