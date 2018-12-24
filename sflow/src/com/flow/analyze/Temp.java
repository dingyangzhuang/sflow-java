package com.flow.analyze;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.flow.output.OutPutRedis;

import Utils.DataConvert;
import Utils.PropertiesTool;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

public class Temp {
	public static void main(String[] args) throws IOException {
		InputStream in = new BufferedInputStream(
				new FileInputStream("./conf/sflow.properties"));
		Properties pro = new Properties();
		pro.load(in);
		in.close();
		System.out.println(pro.getProperty("redis_ip"));
		
	}



}
