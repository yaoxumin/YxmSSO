package com.yxm.sso.util.service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yxm.sso.domain.Person;
import com.yxm.sso.domain.vo.UserVo;
import com.yxm.sso.redis.dao.RedisDao;
import com.yxm.sso.util.StringUtils;

/*
 *<p>Description:redis的各种操作</p>
 *@author:姚旭民
 *@data:2017-8-4 下午5:20:39
 */
public class RedisServiceImpl {
	public static Log logger = LogFactory.getLog(RedisServiceImpl.class);

	/*
	 * <p>说明:删除Set数据</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @param:keys可以有多种，1、Map的key，删除整个Map; 2、Map的Map和里面的属性，删除里面的属性
	 * 
	 * @data:2017-8-9 下午4:47:09
	 */
	public static boolean delMapCache(String... keys) {
		return RedisDao.delMapData(keys);
	}

	/*
	 * <p>说明:删除Set数据</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @param:keys可以有多种，1、Set的key，删除整个Set; 2、Set的key和里面的属性，删除里面的属性
	 * 
	 * @data:2017-8-9 下午4:47:09
	 */
	public static boolean delSetCache(String... keys) {
		return RedisDao.delSetData(keys);
	}

	/*
	 * <p>说明:检验ticket是否存在</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-9 下午2:50:40
	 */
	public static boolean exitMap(String key) {
		return RedisDao.exitsMap(key);
	}

	/*
	 * <p>说明:检验redis里面是否存在了这个值</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @param: key,redis里面的值
	 * 
	 * @return: redis存在数据返回true，不存在返回false
	 * 
	 * @data:2017-8-8 下午11:13:12
	 */
	public static boolean exitSet(String key, String value) {
		System.out.println("请求资源的网站的域名为:"+value);
		Set temp = getSetData(key);
		return RedisDao.exitsSet(key, value);
	}

	/*
	 * <p>说明:获取redis缓存数据</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @param: clazz是反射的class
	 * 
	 * @param: ticket是令牌ticket
	 * 
	 * @data:2017-8-6 下午4:14:39
	 */
	public static Map getMapData(String key) {
		Map<String, String> result = null;
		try {
			result = RedisDao.getMapData(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static Set getSetData(String key) {
		return RedisDao.getSetData(key);
	}

	/*
	 * <p>说明:把对象信息写到了cache里面</>p
	 * 
	 * @author:姚旭民
	 * 
	 * @param: object任意对象
	 * 
	 * @param: 任意对象id
	 * 
	 * @data:2017-8-6 下午1:24:55
	 */
	public static boolean saveMapCache(Object object, String id) {
		try {
			Class<?> classType = object.getClass();
			String field;// 用来遍历的属性变量
			String key = classType.getSimpleName() + id;// 对象类型加id变成了redis里面的key
			Map<String, String> value = new HashMap<String, String>();// 存到redis里面的value集合

			String regex = ".*password.*";// 正则表达式敏感字过滤
			String fieldValue;
			Field[] fields;
			// 循环遍历，如果为null或者是根类Object就停止
			while (!Object.class.equals(classType) && classType != null) {
				fields = classType.getDeclaredFields();// 获取当前类所有的属性
				for (Field f : fields) {
					f.setAccessible(true);// 设置允许获得的权限
					field = f.toString().substring(
							f.toString().lastIndexOf(".") + 1); // 取出属性名称
					if (f.get(object) != null
							&& !field.toLowerCase().matches(regex)) {// 不包含有敏感字样
						fieldValue = f.get(object).toString();
						value.put(field, fieldValue);
					}
				}
				classType = classType.getSuperclass();// 向上遍历父类
			}

			if (value != null && value.size() > 0) {
				RedisDao.saveMapData(key, value);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/*
	 *<p>说明:逐条添加</p>
	 *@author:姚旭民
	 *@data:2017-8-15 下午2:53:48
	 */
	public static boolean saveMapCache(String key, String field, String value) {
		try {
			return RedisDao.saveMapData(key, field, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/*
	 * <p>说明:保存一个set进入redis缓存</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-9 上午10:00:26
	 */
	public static boolean saveSetCache(String key, Set<String> set) {
		try {
			return RedisDao.saveSetData(key, set);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * <p>说明:保存单个数据进入Set</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-9 上午10:00:26
	 */
	public static boolean saveSetCache(String key, String str) {
		try {
			return RedisDao.saveSetData(key, str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
