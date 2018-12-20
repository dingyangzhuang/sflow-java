package com.flow.output;

import java.util.Properties;

import Utils.PropertiesTool;
import redis.clients.jedis.Jedis;

public class OutPutRedis {
	public static Properties pro = PropertiesTool.pro;
	public static Jedis jedis;
	//与redis建立连接
	static{
		//创建连接
		jedis = new Jedis(pro.getProperty("redis_ip"),
				Integer.parseInt(pro.getProperty("redis_port")));
		jedis.auth(pro.getProperty("auth"));
		System.out.println("连接成功"); 
		//查看服务是否运行 
		System.out.println("服务正在运行: "+jedis.ping());
	}
	/**
	 * 将日志信息写入redis中
	 */
	public static void writeRedis(String log){
		Long logNum = jedis.llen("logstash");
		System.out.println(jedis);
		if(logNum<1000){
			jedis.lpush("logstash", log);
		}else {
			System.out.println("log is full(1000)");
		}
		
	}
	
	
}
