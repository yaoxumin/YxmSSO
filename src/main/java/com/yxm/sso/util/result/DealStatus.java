package com.yxm.sso.util.result;

public enum DealStatus {
	SUCCESS(200, "处理成功"),
	ERROR(-1, "处理失败,请重试或联系管理员!"),
	LOGIN_ERROR(-2, "用户名或密码错误"),
	NO_PERMISSION(10000, "不合法的资源访问!"),
	INVALIDATE(10001, "数据校验失败!"),
	DATA_EMPTY(10002, "对应的数据不存在!"),
	EXPORT_FAIL(10004, "导出数据失败!"),
	UNLOGIN(10005, "用户状态失效,请重新登录!"),
	UPLOAD_FAILURE(10006, "上传文件失败"),
	UNSOPPORTED_TYPE(10007, "不支持的文件类型!"),
	FILE_NOT_EXIST(10008, "文件不存在!");
	
	private int code;
	private String msg;
	
	DealStatus(int code, String msg) {
		this.code = code;
		this.msg = msg;
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
	
}
