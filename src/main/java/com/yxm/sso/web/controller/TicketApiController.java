package com.yxm.sso.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.xiaoleilu.hutool.util.StrUtil;
import com.yxm.sso.context.AppContext;
import com.yxm.sso.service.PersonService;
import com.yxm.sso.util.result.ApiResult;
import com.yxm.sso.util.result.DealResult;
import com.yxm.sso.util.service.TicketServiceImpl;

/*
 *<p>Description:ticket对外接口</p>
 *@author:姚旭民
 *@data:2017-7-23 下午5:09:42
 */
@RequestMapping("/ticketApi")
@Controller
public class TicketApiController {
	Log logger = LogFactory.getLog(TicketApiController.class);
	@Resource
	PersonService personService;
	
	/*
	 *<p>说明:加密路径</p>
	 *@author:姚旭民
	 *@data:2017-8-11 下午3:27:53
	 */
	@ResponseBody
	@RequestMapping("/encodeUrl")
	public ApiResult encodeUrl(HttpSession session, HttpServletRequest request) {
		ApiResult result = null;
		result = TicketServiceImpl.encodeUrl(request);
		return result;
	}
	
	/*
	 *<p>说明:解密路径</p>
	 *@author:姚旭民
	 *@data:2017-8-11 下午3:28:06
	 */
	@ResponseBody
	@RequestMapping("/decodeUrl")
	public ApiResult decodeUrl(HttpSession session, HttpServletRequest request) {
		ApiResult result = null;
		result = TicketServiceImpl.decodeUrl(request);
		return result;
	}
	/*
	 * <p>说明:用户获取ticket</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-10 下午2:23:50
	 */
	@ResponseBody
	@RequestMapping("/getUserInfo")
	public ApiResult getUserInfo(HttpSession session, HttpServletRequest request) {
		ApiResult result = null;
		result = TicketServiceImpl.getUserInfo(request);
		return result;
	}

	/*
	 * <p>说明:用户退出登录</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-10 下午2:23:38
	 */
	@ResponseBody
	@RequestMapping("/loginOut")
	public ApiResult loginOut(HttpSession session, HttpServletRequest request) {
		return ApiResult.fromJson(personService.loginOut(session, request).toString());
	}
	

	/*
	 * <p>说明:用户验证是否存在此Ticket,主要是用户单点登录的时候使用</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-10 下午2:23:58
	 */
	@ResponseBody
	@RequestMapping("/verifyTicket")
	public ApiResult verifyTicket(HttpSession session,
			HttpServletRequest request) {
		ApiResult result = TicketServiceImpl.verifyTicket(request);
		return result;
	}
}
