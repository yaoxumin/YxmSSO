package com.yxm.sso.service;

import java.util.List;
import java.util.Set;

import com.yxm.sso.domain.Application;

public interface ApplicationService {
	public Application queryBySql(String sql);

	public List<Application> findListBySql(String sql);

	public List<Application> listAll();
	
	int deleteByPrimaryKey(Integer id);

	int insert(Application record);

	int insertSelective(Application record);

	Application selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Application record);

	int updateByPrimaryKey(Application record);
}
