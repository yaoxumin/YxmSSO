package com.yxm.sso.context;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yxm.sso.util.PropertiesUtils;

/**
 * <p>
 * 说明:系统的上下文环境
 * </p>
 * 
 * @author:姚旭民
 * @date:2017-7-10 下午2:22:00
 */
public class AppContext {
	static Log log = LogFactory.getLog(AppContext.class);
	// 各种类型的key的值
	public static String KEY_TICKET;
	public static String KEY_APPLICAITON;
	public static String KEY_TICKET_BACKURL;// 请求重定向的地址
	public static String KEY_SESSION_USER;// 保存在session里面的key
	public static String KEY_SPILT_DEFAULT;
	public static String KEY_TICKET_MAP_ID;
	public static String KEY_TICKET_MAP_NAME;
	public static String KEY_TICKET_MAP_TYPE;
	public static String KEY_TICKET_MAP_UUID;

	/*
	 * <p>说明:从properties里面初始化变量</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-17 上午9:30:26
	 */
	public static void init() {
		try {
			Class<?> clazz = AppContext.class;
			Method m;// 反射的执行方法
			Field[] files = clazz.getDeclaredFields();// 遍历所有属性
			Properties properties = PropertiesUtils.getInstance().load(
					"config/variate.properties");
			String field;
			for (Field f : files) {
				// 获取得到属性名称
				field = f.toString().substring(
						f.toString().lastIndexOf(".") + 1);
				if (field.equals("log")) {
					continue;
				}
				// 将属性名称set进入AppContest
				m = clazz.getDeclaredMethod("set" + field, String.class);//第二个参数是你的方法要传递的参数类型
				m.invoke(null, properties.get(field));//初始化静态方法和对象方法不一样，第一个参数要传递Null,后面传递正常参数
			}
			System.out.println("【AppContext】init|初始化系统环境变量完成");
			log.info("【AppContext】init|初始化系统环境变量完成");
		} catch (Exception e) {
			log.error("执行错误", e);
			e.printStackTrace();
		}
	}

//	public static void main(String[] args) {
//		init();
//	}

	public static void setKEY_TICKET(String kEY_TICKET) {
		KEY_TICKET = kEY_TICKET;
	}

	public static void setKEY_APPLICAITON(String kEY_APPLICAITON) {
		KEY_APPLICAITON = kEY_APPLICAITON;
	}

	public static void setKEY_TICKET_BACKURL(String kEY_TICKET_BACKURL) {
		KEY_TICKET_BACKURL = kEY_TICKET_BACKURL;
	}

	public static void setKEY_SESSION_USER(String kEY_SESSION_USER) {
		KEY_SESSION_USER = kEY_SESSION_USER;
	}

	public static void setKEY_SPILT_DEFAULT(String kEY_SPILT_DEFAULT) {
		KEY_SPILT_DEFAULT = kEY_SPILT_DEFAULT;
	}

	public static void setKEY_TICKET_MAP_ID(String kEY_TICKET_MAP_ID) {
		KEY_TICKET_MAP_ID = kEY_TICKET_MAP_ID;
	}

	public static void setKEY_TICKET_MAP_NAME(String kEY_TICKET_MAP_NAME) {
		KEY_TICKET_MAP_NAME = kEY_TICKET_MAP_NAME;
	}

	public static void setKEY_TICKET_MAP_TYPE(String kEY_TICKET_MAP_TYPE) {
		KEY_TICKET_MAP_TYPE = kEY_TICKET_MAP_TYPE;
	}

	public static void setKEY_TICKET_MAP_UUID(String kEY_TICKET_MAP_UUID) {
		KEY_TICKET_MAP_UUID = kEY_TICKET_MAP_UUID;
	}
}
