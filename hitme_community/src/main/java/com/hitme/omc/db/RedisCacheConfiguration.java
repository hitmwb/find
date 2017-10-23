package com.hitme.omc.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.hitme.omc.manager.ConfigManager;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component
public class RedisCacheConfiguration {
	@Autowired
	private ConfigManager congigManager;

	@Bean
	public JedisPool getJedisPool() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(this.congigManager.getRedisMaxIdle());
		jedisPoolConfig.setMaxWaitMillis(this.congigManager.getRedisMaxWaitMillis());

		JedisPool pool = new JedisPool(jedisPoolConfig, this.congigManager.getRedisHost(),
				this.congigManager.getRedisPort(), this.congigManager.getRedisTimeout(),
				this.congigManager.getRedisPassword());
		return pool;
	}
}
