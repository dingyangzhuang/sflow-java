package com.flow.output;

import java.util.Properties;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import Utils.PropertiesTool;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class OutPutRedis {
	public static Properties pro = PropertiesTool.pro;
	public static GenericObjectPoolConfig poolConfig=new GenericObjectPoolConfig();
	public static JedisPool jedisPool=null;
	// 与redis建立连接
	static {
		// // 设置最大连接数为默认值的 5 倍
		//
		// poolConfig.setMaxTotal(GenericObjectPoolConfig.DEFAULT_MAX_TOTAL *
		// 5);
		//
		// // 设置最大空闲连接数为默认值的 3 倍
		//
		// poolConfig.setMaxIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE * 3);
		//
		// // 设置最小空闲连接数为默认值的 2 倍
		//
		// poolConfig.setMinIdle(GenericObjectPoolConfig.DEFAULT_MIN_IDLE * 2);

		// 设置开启 jmx 功能

		//poolConfig.setJmxEnabled(true);

		// 设置连接池没有连接后客户端的最大等待时间 ( 单位为毫秒 )

		poolConfig.setMaxWaitMillis(3000);
		jedisPool = new JedisPool(poolConfig, pro.getProperty("redis_ip"),
				Integer.parseInt(pro.getProperty("redis_port")), 3000, pro.getProperty("auth"), 0);

		// //创建连接
		// jedis = new Jedis(pro.getProperty("redis_ip"),
		// Integer.parseInt(pro.getProperty("redis_port")));
		// jedis.auth(pro.getProperty("auth"));
		// System.out.println("连接成功");
		// //查看服务是否运行
		// System.out.println("服务正在运行: "+jedis.ping());
	}

	/**
	 * 将日志信息写入redis中
	 */
	public static void writeRedis(String log) {
		Jedis jedis = null;
		try {
			// 1. 从连接池获取 jedis 对象
			jedis = jedisPool.getResource();
			// 2. 执行操作
			Long logNum = jedis.llen("logstash");
			if (logNum < 1000) {
				jedis.lpush("logstash", log);
			} else {
				System.out.println("log is full(1000)");
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		} finally {
			if (jedis != null) {
				// 如果使用 JedisPool ， close 操作不是关闭连接，代表归还连接池
				jedis.close();
			}
		}

	}

}
