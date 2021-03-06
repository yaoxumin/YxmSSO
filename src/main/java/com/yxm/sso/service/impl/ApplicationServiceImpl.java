package com.yxm.sso.service.impl;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.yxm.sso.context.AppContext;
import com.yxm.sso.domain.Application;
import com.yxm.sso.mapper.ApplicationMapper;
import com.yxm.sso.service.ApplicationService;
import com.yxm.sso.util.StringUtils;
import com.yxm.sso.util.db.DButil;
import com.yxm.sso.util.service.RedisServiceImpl;

@Service
public class ApplicationServiceImpl implements ApplicationService {
	@Resource
	ApplicationMapper applicationMapper;

	/*
	 * <p>Description:查询所有有权限的应用的列表</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-7-20 上午8:47:46
	 */
	@Override
	public List<Application> listAll() {
		return applicationMapper.listAll();
	}

	/*
	 * <p>Description:自定义查询</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-7-20 上午10:40:08
	 */
	@Override
	public Application queryBySql(String sql) {
		return applicationMapper.queryBySql(sql);
	}

	/*
	 * <p>Description:查询集合</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-7-24 上午10:15:02
	 */
	@Override
	public List<Application> findListBySql(String sql) {
		return applicationMapper.findListBySql(sql);
	}

	/*
	 * <p>说明:检查是否是合法的域名</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-9 下午4:15:58
	 */
	public static boolean allowedApplication(HttpServletRequest request) {
		// request.getLocalAddr();
		// String addr = request.getRemoteAddr();//返回发出请求的客户机的ip
		String name = request.getRemoteHost();// 返回发出请求的客户机的完整主机名
		// String name2 = request.getServerName();//服务器名字
		// String path = request.getServletPath();//获取客户端所请求的脚本文件的文件路径
		// int port = request.getServerPort();//端口
		return exitApplicationOfRedis(name);
	}

	/*
	 * <p>说明:项目启动的时候加载数据进入redis缓存起来</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-9 上午9:37:30
	 */
	public static boolean saveApplicationIdtoRedis(String key) {
		// 【1】启动的时候，加载数据网站数据到redis里面，后面要做定时刷新和加载
		String sql = "SELECT " + key + " FROM " + AppContext.KEY_APPLICAITON;
		Set set = DButil.getInstance().findBySql(sql, key);
		return RedisServiceImpl.saveSetCache(AppContext.KEY_APPLICAITON, set);
	};

	/*
	 * <p>说明:查看redis里面是不是存在了这个路径</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-9 下午1:48:39
	 */
	public static boolean exitApplicationOfRedis(String url) {
		return RedisServiceImpl.exitSet(AppContext.KEY_APPLICAITON, url);
	}

	public static boolean delApplicationOfRedis(String url) {
		return RedisServiceImpl.exitSet(AppContext.KEY_APPLICAITON, url);
	}

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return applicationMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(Application record) {
		return applicationMapper.insert(record);
	}

	@Override
	public int insertSelective(Application record) {
		return applicationMapper.insertSelective(record);
	}

	@Override
	public Application selectByPrimaryKey(Integer id) {
		return applicationMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(Application record) {
		return applicationMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(Application record) {
		return applicationMapper.updateByPrimaryKey(record);
	}
}
