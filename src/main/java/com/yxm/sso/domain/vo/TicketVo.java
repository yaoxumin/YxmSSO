package com.yxm.sso.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.yxm.sso.domain.Application;
import com.yxm.sso.domain.Person;

/*
 *<p>说明:打包进入redis的token的包装集合</p>
 *@author:姚旭民
 *@data:2017-8-10 上午10:04:31
 */
public class TicketVo implements Serializable {
	private Integer age;

	private String username;

	private String sex;

	private Integer companyid;
	
	private String url;//保存最后一次的回调地址
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public TicketVo() {
	}

	public Integer getCompanyid() {
		return companyid;
	}

	public void setCompanyid(Integer companyid) {
		this.companyid = companyid;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public TicketVo(Person person) {
		this.age = person.getAge();
		this.username = person.getUsername();
		this.sex = person.getSex();
		this.companyid = person.getCompanyid();
	}

	public static TicketVo fromJson(String json) {
		return JSON.parseObject(json, TicketVo.class);
	}
}
