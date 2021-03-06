package com.yxm.sso.redis.dao;

import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.yxm.sso.context.RedisPool;
import com.yxm.sso.util.StringUtils;

public class RedisDao {
	/*
	 * <p>说明:删除整个Map</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-9 下午2:02:17
	 */
	public static boolean delMapData(String... keys) {
		Jedis redis = null;
		try {
			redis = RedisPool.jedisPool.getResource();
			if (keys.length > 1 && keys.length < 3) {
				redis.hdel(keys[0], keys[1]);
				return true;
			} else if (keys.length == 1) {
				Map<String, String> temp = getMapData(keys[0]);
				redis.del(keys[0]);
				return true;
			}
		} catch (Exception e) {
			RedisPool.jedisPool.returnBrokenResource(redis);
			e.printStackTrace();
		} finally {
			RedisPool.jedisPool.returnResource(redis);
		}

		return false;
	}

	/*
	 * <p>说明:删除一个或者多个</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-9 下午2:07:00
	 */
	public static boolean delSetData(String... keys) {
		Jedis redis = null;
		try {
			redis = RedisPool.jedisPool.getResource();
			if (keys.length > 1 && keys.length < 3) {// 删除一部分的数据
				redis.srem(keys[0], keys[1]);
			} else if (keys.length == 1) {// 删除整个
				redis.smembers(keys[0]);
			}
			return true;
		} catch (Exception e) {
			RedisPool.jedisPool.returnBrokenResource(redis);
		} finally {
			RedisPool.jedisPool.returnResource(redis);
		}

		return false;
	}

	/*
	 * <p>说明:查看是否存在该键值对</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-7 上午9:29:23
	 */
	public static boolean exitsMap(String... keys) {
		boolean result = false;
		Jedis jedis = null;
		if (StringUtils.isNotBlank(keys)) {
			try {
				jedis = RedisPool.jedisPool.getResource();
				if (keys.length > 1 && keys.length < 3) {
					result = jedis.hexists(keys[0], keys[1]);
				} else if (keys.length == 1) {
					result = jedis.exists(keys[0]);
				}
			} catch (Exception e) {
				RedisPool.jedisPool.returnBrokenResource(jedis);
			} finally {
				RedisPool.jedisPool.returnResource(jedis);
			}
		}
		return result;
	}

	/*
	 * <p>说明:检验set是否存在</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-9 下午2:47:09
	 */
	public static boolean exitsSet(String key, String value) {
		boolean result = false;
		Jedis jedis = null;
		if (StringUtils.isNotBlank(key, value)) {
			try {
				jedis = RedisPool.jedisPool.getResource();
				Set<String> temp = getSetData(key);
				result = jedis.sismember(key, value);
			} catch (Exception e) {
				RedisPool.jedisPool.returnBrokenResource(jedis);
			} finally {
				RedisPool.jedisPool.returnResource(jedis);
			}
		}
		return result;
	}

	/*
	 * <p>说明:检查Set集合里面是否存在了这个值</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-9 下午1:58:58
	 */
	public static boolean exitsSetFiled(String key, String value) {
		boolean result = false;
		Jedis jedis = null;
		try {
			jedis = RedisPool.jedisPool.getResource();
			result = jedis.sismember(key, value);
		} catch (Exception e) {
			RedisPool.jedisPool.returnBrokenResource(jedis);
		} finally {
			RedisPool.jedisPool.returnResource(jedis);
		}
		return result;
	}

	/**
	 * <p>
	 * 说明:获取保存在redis里面的特定map
	 * </p>
	 * 
	 * @author:姚旭民
	 * @date:2017-7-14 下午5:39:53
	 */
	public static Map<String, String> getMapData(String key) {
		Map<String, String> dataMap = null;
		Jedis redis = null;
		try {
			JedisPool temp = RedisPool.jedisPool;
			redis = RedisPool.jedisPool.getResource();
			dataMap = redis.hgetAll(key);
		} catch (Exception e) {
			e.printStackTrace();
			RedisPool.jedisPool.returnBrokenResource(redis);
		} finally {
			RedisPool.jedisPool.returnResource(redis);
		}
		return dataMap;
	}

	/**
	 * <p>
	 * 说明:获取map里面特定的键值对信息
	 * </p>
	 * 
	 * @author:姚旭民
	 * @date:2017-7-14 下午5:39:42
	 */
	public static String getMapData(String flag, String field) {
		Jedis redis = null;
		String result = "";
		try {
			redis = RedisPool.jedisPool.getResource();
			result = redis.hget(flag, field);
		} catch (Exception e) {
			// 销毁对象
			RedisPool.jedisPool.returnBrokenResource(redis);
		} finally {
			// 还原到连接池
			RedisPool.jedisPool.returnResource(redis);
		}
		return result;
	}

	/*
	 * <p>说明:获取redis里面保存的信息</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-9 下午1:57:42
	 */
	public static Set getSetData(String key) {
		Jedis redis = null;
		Set result = null;
		try {
			redis = RedisPool.jedisPool.getResource();
			result = redis.smembers(key);
		} catch (Exception e) {
			// 销毁对象
			RedisPool.jedisPool.returnBrokenResource(redis);
		} finally {
			// 还原到连接池
			RedisPool.jedisPool.returnResource(redis);
		}
		return result;
	}

	/**
	 * <p>
	 * 说明:在redis里面保存map对象,key-value模式
	 * </p>
	 * 
	 * @author:姚旭民
	 * @date:2017-7-14 下午5:42:21
	 */
	public static void saveMapData(String key, Map mapData) {
		Jedis redis = null;
		try {
			redis = RedisPool.jedisPool.getResource();
			redis.hmset(key, mapData);
		} catch (Exception e) {
			// 销毁对象
			RedisPool.jedisPool.returnBrokenResource(redis);
		} finally {
			// 还原到连接池
			RedisPool.jedisPool.returnResource(redis);
		}
	}

	// redis的list存储比较适合来做消息队列
	// public static boolean saveListData(String key, List<String> list){
	// Jedis redis = null;
	// try {
	// redis = RedisPool.jedisPool.getResource();
	// } catch(Exception e) {
	//
	// } finally {
	//
	// }
	//
	// }

	/**
	 * <p>
	 * 说明:为特定map对象保存键值对
	 * </p>
	 * 
	 * @author:姚旭民
	 * @date:2017-7-14 上午11:35:56
	 */
	public static boolean saveMapData(String key, String field, String value) {
		boolean result = false;
		Jedis redis = null;
		try {
			redis = RedisPool.jedisPool.getResource();
			redis.hset(key, field, value);
			result = true;
		} catch (Exception e) {
			// 销毁对象
			RedisPool.jedisPool.returnBrokenResource(redis);
		} finally {
			// 还原到连接池
			RedisPool.jedisPool.returnResource(redis);
		}
		
		return result;
	}

	/*
	 * <p>说明:将信息保存在set，感觉这里的set更像一种往常list在用的形式</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-9 上午9:57:15
	 */
	public static boolean saveSetData(String key, Set<String> set) {
		boolean result = false;
		Jedis redis = null;
		try {
			redis = RedisPool.jedisPool.getResource();
			for (String str : set) {
				redis.sadd(key, str);
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return result;
	}

	/*
	 * <p>说明:填写单个数据进入set</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-9 上午9:58:19
	 */
	public static boolean saveSetData(String key, String str) {
		boolean result = false;
		Jedis redis = null;
		try {
			redis = RedisPool.jedisPool.getResource();
			redis.sadd(key, str);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return result;
	}
}
