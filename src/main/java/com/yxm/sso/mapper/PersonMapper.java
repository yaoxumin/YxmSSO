package com.yxm.sso.mapper;

import java.util.List;

import com.yxm.sso.domain.Person;

public interface PersonMapper {
	public Person queryBySql(String sql);

	public List<Person> findListBySql(String sql);

	public List<Person> listAll();

	// 通过条件查找人
	public Person getPerson(Person person);

	// 查看账号是否存在
	public Person verifyPerson(Person person);

	public int addRecord(Person p);

	public int saveOrUpdate(Person p);

	public Person getDetail(String id);

	public void deleteRecord(String id);

	int deleteByPrimaryKey(Integer id);

	int insert(Person record);

	int insertSelective(Person record);

	Person selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Person record);

	int updateByPrimaryKey(Person record);

}
