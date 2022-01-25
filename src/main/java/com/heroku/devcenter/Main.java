package com.heroku.devcenter;

import java.net.URI;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

public class Main {

	public static void main(String[] args) {

		JedisPool pool;

	// First part is meant to establish the connectivity with the managed Redis server provided by Heroku	
		try {

			URI redisURI = new URI(System.getenv("REDISTOGO_URL"));
			pool = new JedisPool(new JedisPoolConfig(), redisURI.getHost(), redisURI.getPort(),
					Protocol.DEFAULT_TIMEOUT, redisURI.getUserInfo().split(":", 2)[1]);

		} catch (Exception e) {
			throw new RuntimeException("Redis couldn't be configured from URL in REDISTOGO_URL env var ");
		}


	// Second part is meant to set/get the values from redis instance	
		Jedis jedis = pool.getResource();

		try {
			String key = "testKey";
			String value = "testValue";
			jedis.set(key, value);
			System.out.println("Value set into Redis is: " + value);
			System.out.println("Value retrieved from Redis is: " + jedis.get(key));
		} finally {
			pool.returnResource(jedis);
		}

	}

}
