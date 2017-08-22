package com.yxm.sso.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * <p>Title: Properties文件工具类</p> 
 * <p>Description: 读写properties文件</p>
 * @author feng
 * @date 2016年8月23日
 * @version 1.0
 */
public class PropertiesUtils {
	
	private PropertiesUtils() {}
	private volatile static PropertiesUtils propertiesUtil;
	private static Map<String,Properties> props;
	
	/**
	 * 实例化工具类(单例,线程同步)
	 * @return
	 */
	public static PropertiesUtils getInstance() {
		if (propertiesUtil == null) {
			synchronized (PropertiesUtils.class) {
				if (propertiesUtil == null) {
					propertiesUtil = new PropertiesUtils();
					props = new HashMap<String, Properties>();
				}
				
			}
		}
		return propertiesUtil;
	}
	
	/**
	 * 读取src目录下的配置文件
	 * @param name 带后缀的文件名
	 * @return
	 */
	public Properties load(String name) {
		if (props.get(name)!=null) {
			return props.get(name);
		} else {
			Properties prop = new Properties();
			try {
				prop.load(PropertiesUtils.class.getResourceAsStream("/"+name));
				props.put(name, prop);
				return prop;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

    /**
     * 读取工程目录下面的配置文件（任意/WEB-INF/目录下面）
     * @param propPathAndName （代表一个路径或者文件名）
     * @return
     */
	public Properties getWebRootPropertiesByName(String propPathAndName) {
        if (props.containsKey(propPathAndName)) {
            return props.get(propPathAndName);
        } else {
            Properties properties = new Properties();
            try {
            	//获取WEB-INF/classes目录
                String webRootPath = PropertiesUtils.class.getResource("/").getPath();
                //截取到WEB-INF/目录
                String rootPath = webRootPath.substring(1, webRootPath.indexOf("classes"));
                //放入输入流中
                InputStream inputStream = new BufferedInputStream(new FileInputStream(rootPath + propPathAndName));
                //加载一个流文件
                properties.load(inputStream);
                props.put(propPathAndName, properties);
                return properties;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
	
	/**
	 * 写入文件
	 * @param fileName 文件名
	 * @param obj 对应的对象
	 * @param exclude 排除的属性
	 */
	public void write(String fileName, Object obj , Object[] exclude) {
		FileOutputStream fos = null;
		Properties prop = this.load(fileName);
		String path = PropertiesUtils.class.getClassLoader().getResource(fileName).getPath();
		try {
			setProperty(prop,obj,exclude);
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		}
		try {
			fos = new FileOutputStream(path);
			prop.store(fos,null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	private void setProperty(Properties prop, Object obj, Object[] exclude) 
			throws NoSuchMethodException, SecurityException, IllegalAccessException, 
			IllegalArgumentException, InvocationTargetException {
		Class clz = obj.getClass();
		String name;
		for(Field field : clz.getDeclaredFields()) {
			if (exclude==null) {
				name = field.getName();
				executeSetProperty(clz,name,obj,prop);
			} else {
				for(int i = 0; i < exclude.length; i++) {
					name = field.getName();
					if (!name.equals(exclude[i])) {
						executeSetProperty(clz,name,obj,prop);
					}
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private void executeSetProperty(Class clz,String name,Object obj,Properties prop) 
			throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		@SuppressWarnings("unchecked")
		Method method = clz.getMethod("get"+name.substring(0, 1).toUpperCase() + name.substring(1));
		String getResult = String.valueOf(method.invoke(obj));
		if (!"null".equals(getResult)) {
			prop.setProperty(name, getResult);
		}
		
	}
}
