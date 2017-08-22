package com.yxm.sso.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * <p>Title: Spring上下文工具类,用于静态获取Bean </p> 
 *
 * Created by xiaofeng 
 * @time 2017-2-1 下午11:24:29
 * @version 1.0
 */
public class SpringContextUtils implements ApplicationContextAware {
	private static ApplicationContext applicationContext; 

	// 实现了ApplicationContextAware接口，在Bean的实例化时会自动调用setApplicationContext()方法
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SpringContextUtils.applicationContext = applicationContext;
	}

	/**
	 * 获取applicationContext
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 根据BeanName获取Bean
	 * @param name
	 * @return
	 * @throws BeansException
	 */
	public static Object getBean(String name) throws BeansException {
		try {
			return applicationContext.getBean(name);
		} catch (Exception e) {
			throw new RuntimeException("获取的Bean不存在！");
		}
	}

	/**
	 * 根据BeanType获取Bean
	 * @param name
	 * @return
	 * @throws BeansException
	 */
	public static Object getBean(Class<?> type) throws BeansException {
		try {
			return applicationContext.getBean(type);
		} catch (Exception e) {
			throw new RuntimeException("获取的Bean不存在！");
		}
	}
	
	/**
	 * 根据BeanName和类型获取Bean
	 * @param name
	 * @param requiredType
	 * @return
	 * @throws BeansException
	 */
	public static <T> T getBean(String name, Class<T> requiredType)
			throws BeansException {
		return applicationContext.getBean(name, requiredType);
	}

	public static boolean containsBean(String name) {
		return applicationContext.containsBean(name);
	}

	public static boolean isSingleton(String name)
			throws NoSuchBeanDefinitionException {
		return applicationContext.isSingleton(name);
	}

	public static Class<? extends Object> getType(String name)
			throws NoSuchBeanDefinitionException {
		return applicationContext.getType(name);
	}

	public static String[] getAliases(String name)
			throws NoSuchBeanDefinitionException {
		return applicationContext.getAliases(name);
	}


}