package com.yxm.sso.util.result;

/**
 * <p>
 * 说明:一些操作处理的处理结果
 * </p>
 * 
 * @author:姚旭民
 * @date:2017-7-13 下午5:36:08
 */
public class DealResult {
	private int code; // 处理结果码
	private String msg; // 处理消息
	private Object data;// 打包处理结果
	private String ticket; // ticket的值
	private String url; // 控制器控制跳转的页面
	private String backUrl; // 重定向地址

	// 初始化一个默认的成功包装对象
	public static DealResult defaultSuccess() {
		DealResult result = new DealResult();
		result.code = DealStatus.SUCCESS.getCode();
		result.msg = DealStatus.SUCCESS.getMsg();
		return result;
	}
	
	public static DealResult defaultError() {
		DealResult result = new DealResult();
		result.code = DealStatus.ERROR.getCode();
		result.msg = DealStatus.ERROR.getMsg();
		return result;
	}

	public void setTicketInfo(int code, String msg, String backUrl) {
		this.code = code;
		this.msg = msg;
		this.url = backUrl;
		this.backUrl = backUrl;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
