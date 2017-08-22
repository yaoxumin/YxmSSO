package com.yxm.sso.context;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.propertyeditors.CurrencyEditor;

/**
 * <p>
 * 说明:sessionId是key，value是HttpSession对象，这样才能同通过sessionId来查找s是否存在
 * </p>
 * 
 * @author:姚旭民
 * @date:2017-7-14 上午10:19:25
 */
public class GlobalSession {
	// 存放所有全局会话,使用线程安全的线程池
	private static Map<String, HttpSession> sessions = new ConcurrentHashMap<String, HttpSession>();

	/*
	 *<p>说明:添加session</p>
	 *@author:姚旭民
	 *@data:2017-8-15 下午2:24:54
	 */
	public static void addSession(String ticket, HttpSession session) {
		sessions.put(ticket, session);
	}

	/*
	 *<p>说明:删除session</p>
	 *@author:姚旭民
	 *@data:2017-8-15 下午2:25:04
	 */
	public static void delSession(String ticket) {
		if (sessions.containsKey(ticket))
			sessions.remove(ticket);
	}

	/*
	 *<p>说明:获取session</p>
	 *@author:姚旭民
	 *@data:2017-8-15 下午2:25:14
	 */
	public static HttpSession getSession(String ticket) {
		return sessions.get(ticket);
	}
}
