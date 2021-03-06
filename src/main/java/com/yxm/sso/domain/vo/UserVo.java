package com.yxm.sso.domain.vo;

import java.io.Serializable;

import com.yxm.sso.domain.Application;
import com.yxm.sso.domain.Person;

public class UserVo implements Serializable {
	private String ticket;// 当前账号拥有的ticket
	private Person person;// ticket包含的用户信息
	private Application app;// 用户所在的系统的信息
	private String backUrl;//用户的回调地址
	private String loginOutUrl; //用户退出登录的时候的回调地址

	public UserVo() {
	}
	
	public UserVo(String ticket, String backUrl, String loginOutUrl, Person person) {
		this.ticket = ticket;
		this.backUrl = backUrl;
		this.loginOutUrl = loginOutUrl;
		this.person = person;
	}
	
	public UserVo(String ticket, String backUrl, String loginOutUrl, Person person, Application app) {
		this.ticket = ticket;
		this.backUrl = backUrl;
		this.loginOutUrl = loginOutUrl;
		this.person = person;
		this.app = app;
	}
	
	public UserVo(String ticket, Person person) {
		this.ticket = ticket;
		this.person = person;
	}

	public Application getApp() {
		return app;
	}

	public void setApp(Application app) {
		this.app = app;
	}

	public String getBackUrl() {
		return backUrl;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getLoginOutUrl() {
		return loginOutUrl;
	}

	public void setLoginOutUrl(String loginOutUrl) {
		this.loginOutUrl = loginOutUrl;
	}
}
