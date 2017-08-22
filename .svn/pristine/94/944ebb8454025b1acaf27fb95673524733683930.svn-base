package com.yxm.sso.util.result;

import com.alibaba.fastjson.JSON;
import com.yxm.sso.domain.Person;

/*
 *<p>说明:用来处理请求的接口的返回的类型</p>
 *@author:姚旭民
 *@data:2017-8-7 上午9:20:33
 */
public class ApiResult {
	private int code; // 处理结果码
	private String msg; // 处理消息
	private Object data;// 打包处理结果

	public ApiResult() {}
	
	public static ApiResult defaultError() {
		ApiResult result = new ApiResult();
		result.setCode(DealStatus.ERROR.getCode());
		result.setMsg(DealStatus.ERROR.getMsg());
		return result;
	}
	
	public static ApiResult defaultSuccess() {
		ApiResult result = new ApiResult();
		result.setCode(DealStatus.SUCCESS.getCode());
		result.setMsg(DealStatus.SUCCESS.getMsg());
		return result;
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public static ApiResult fromJson(String json) {
		return JSON.parseObject(json, ApiResult.class);
	}
}
