package com.hitme.omc.manager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@EnableCaching
@Component
public class ConfigManager extends CachingConfigurerSupport {
	@Value("${omc.host}")
	private String omcIp;
	@Value("${omc.port}")
	private int omcPort;
	@Value("${omc.tcp.idle.time}")
	private int idleTimeout;
	@Value("${spring.redis.host}")
	private String redisHost;
	@Value("${spring.redis.port}")
	private int redisPort;
	@Value("${spring.redis.timeout}")
	private int redisTimeout;
	@Value("${spring.redis.pool.max-idle}")
	private int redisMaxIdle;
	@Value("${spring.redis.pool.max-wait}")
	private long redisMaxWaitMillis;
	@Value("${spring.redis.password}")
	private String redisPassword;
	@Value("${omc.alarm.file.dir}")
	private String alarmFileDir;

	public String getOmcIp() {
		return this.omcIp;
	}

	public void setOmcIp(String omcIp) {
		this.omcIp = omcIp;
	}

	public int getOmcPort() {
		return this.omcPort;
	}

	public void setOmcPort(int omcPort) {
		this.omcPort = omcPort;
	}

	public int getIdleTimeout() {
		return this.idleTimeout;
	}

	public void setIdleTimeout(int idleTimeout) {
		this.idleTimeout = idleTimeout;
	}

	public String getRedisHost() {
		return this.redisHost;
	}

	public void setRedisHost(String redisHost) {
		this.redisHost = redisHost;
	}

	public int getRedisPort() {
		return this.redisPort;
	}

	public void setRedisPort(int redisPort) {
		this.redisPort = redisPort;
	}

	public int getRedisTimeout() {
		return this.redisTimeout;
	}

	public void setRedisTimeout(int redisTimeout) {
		this.redisTimeout = redisTimeout;
	}

	public int getRedisMaxIdle() {
		return this.redisMaxIdle;
	}

	public void setRedisMaxIdle(int redisMaxIdle) {
		this.redisMaxIdle = redisMaxIdle;
	}

	public long getRedisMaxWaitMillis() {
		return this.redisMaxWaitMillis;
	}

	public void setRedisMaxWaitMillis(long redisMaxWaitMillis) {
		this.redisMaxWaitMillis = redisMaxWaitMillis;
	}

	public String getRedisPassword() {
		return this.redisPassword;
	}

	public void setRedisPassword(String redisPassword) {
		this.redisPassword = redisPassword;
	}

	public void setAlarmFileDir(String alarmFileDir) {
		this.alarmFileDir = alarmFileDir;
	}

	public String getAlarmFileDir() {
		return this.alarmFileDir;
	}
}
