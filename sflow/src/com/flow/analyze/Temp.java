package com.flow.analyze;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
		for(int i=0;i<2000;i++){
			OutPutRedis.writeRedis(""+i);
		}
		
	}



}
