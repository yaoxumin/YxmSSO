package com.yxm.sso.init;

import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.yxm.sso.context.AppContext;
import com.yxm.sso.service.impl.ApplicationServiceImpl;
import com.yxm.sso.util.db.DButil;
import com.yxm.sso.util.keyword.KeywordFilter;

/*
 *<p>说明:web项目的启动加载各种环境配置的初始化类型</p>
 *@author:姚旭民
 *@data:2017-8-7 上午9:32:44
 */
public class WebInit extends HttpServlet {
	// 启动加载类型
	public void init() throws ServletException {
		// 初始化各个系统变量
		AppContext.init();
		// 把数据保存到redis里面
		ApplicationServiceImpl.saveApplicationIdtoRedis("url");
		// 过滤规则入库
		// KeywordFilter.test();
	}

}
