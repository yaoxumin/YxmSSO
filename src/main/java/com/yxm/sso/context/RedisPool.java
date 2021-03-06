package com.yxm.sso.context;

import java.util.Properties;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.yxm.sso.util.PropertiesUtils;
/**
 *<p>说明:redis连接池</p>
 *@author:姚旭民
 *@date:2017-7-14 上午10:25:37
 */
public class RedisPool {
	public static RedisPool redisPool = getInstance();
	public static JedisPool jedisPool;
	public static synchronized RedisPool getInstance() {
		if (null == redisPool) {
			redisPool = new RedisPool();
		}
		return redisPool;
	}
	
	public RedisPool() {
		if (null == jedisPool) {
			init();
		}
	}
	
	/**
	 *<p>说明:初始化jedis</p>
	 *@author:姚旭民
	 *@date:2017-7-14 上午10:31:05
	 */
	private static JedisPoolConfig initPoolConfig() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		//控制一个pool最多有多少个状态为idle的jedis的实例
		jedisPoolConfig.setMaxIdle(1000);
		//最大能够保持空闲状态的对象数
		jedisPoolConfig.setMaxIdle(300);
		//超时时间
		jedisPoolConfig.setMaxWaitMillis(1000);
		// 在borrow一个jedis实例时，是否提前进行alidate操作；如果为true，则得到的jedis实例均是可以用的
		jedisPoolConfig.setTestOnBorrow(true);
		//在还会给pool时，是否提前进行validate操作
		jedisPoolConfig.setTestOnReturn(true);
		
		return jedisPoolConfig;
	}
	
	/**
	 *<p>说明:初始化jedis池</p>
	 *@author:姚旭民
	 *@date:2017-7-14 上午10:37:44
	 */
	public static void init() {
		JedisPoolConfig jedisPoolConfig = initPoolConfig();
		Properties pro = PropertiesUtils.getInstance().load("config/redis.properties");
		String host = pro.getProperty("redis.host");
		int port = Integer.parseInt(pro.getProperty("redis.port"));
		int timeout = Integer.parseInt(pro.getProperty("redis.timeout"));
		//构造连接池
		jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout);
	}
	
}
