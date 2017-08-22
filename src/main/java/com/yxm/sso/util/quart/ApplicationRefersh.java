package com.yxm.sso.util.quart;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.yxm.sso.service.impl.ApplicationServiceImpl;

public class ApplicationRefersh extends QuartzJobBean {

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		//实现刷新授权网站数据,在quart.xml里面配置了10分钟刷新一次
		ApplicationServiceImpl.saveApplicationIdtoRedis("url");
	}
}
