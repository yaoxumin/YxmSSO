package com.yxm.sso.util.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yxm.sso.util.HttpClientUtil;

/*
 *<p>说明:接口请求处理类型</p>
 *@author:姚旭民
 *@data:2017-8-13 下午4:01:26
 */
public class HttpClientServiceImpl {
	public static Log logger = LogFactory.getLog(HttpClientServiceImpl.class);
 
	/*
	 * <p>说明:通知用户网站退出这个用户</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-13 下午3:46:45
	 */
	public static boolean noticeLoginOut(String url, String ticket) {
		try {
			HttpClientUtil.get(url + "?ticket=" + ticket);
		} catch (Exception e) {
			logger.info("通知客户网站用户退出登录出错");
			e.printStackTrace();
		}
		return false;
	}
}
