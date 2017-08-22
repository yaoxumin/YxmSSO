package com.yxm.sso.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.yxm.sso.domain.Person;
import com.yxm.sso.util.result.DealResult;

public interface PersonService {

	public Person queryBySql(String sql);

	public List<Person> findListBySql(String sql);

	public List<Person> listAll();

	public DealResult login(HttpSession session,
			HttpServletRequest request, Person person);

	public DealResult loginOut(HttpSession session, HttpServletRequest request);

	public boolean verifyExistTicket(HttpSession session);
	
	public int addRecord(Person p);

	public int saveOrUpdate(Person p);

	public Person getDetail(String id);

	public void deleteRecord(String id);
}
