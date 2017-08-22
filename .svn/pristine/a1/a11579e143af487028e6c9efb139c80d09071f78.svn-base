package com.yxm.sso.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yxm.sso.domain.Person;
import com.yxm.sso.service.PersonService;
import com.yxm.sso.util.result.DealResult;

/**
 * <p>
 * 说明:登陆控制层
 * </p>
 * 
 * @author:姚旭民
 * @date:2017-7-13 下午3:31:23
 */
@Controller
@RequestMapping("/user")
public class PersonController {
	Log logger = LogFactory.getLog(PersonController.class);
	@Resource
	PersonService personService;
	
	/**
	 * <p>
	 * 说明:如果是登录的话，那么只需要可以验证的参数就可以了
	 * </p>
	 * 
	 * @author:姚旭民
	 * @date:2017-7-13 下午3:45:52
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView loginPost(HttpSession session,
			HttpServletRequest request,
			@ModelAttribute Person person) {
		ModelAndView model = new ModelAndView();
		DealResult temp = personService.login(session, request, person);
		model.addObject("result", temp);
		model.setViewName(temp.getUrl());
		return model;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView loginGet(HttpSession session,
			HttpServletRequest request,
			@ModelAttribute Person person) {
		ModelAndView model = new ModelAndView();
		DealResult temp = personService.login(session, request, person);
		model.addObject("result", temp);
		model.setViewName(temp.getUrl());
		return model;
	}

	/*
	 * <p>Description:用户登出的操作</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-7-20 下午3:30:21
	 */
	@RequestMapping("/loginOut")
	public @ResponseBody
	DealResult loginOut(HttpSession session, HttpServletRequest request) {
		return personService.loginOut(session, request);
	}

	/**
	 * <p>
	 * 说明:ticket验证，如果有人发来ticket验证，同时修改保存起来的backUrl，方便后面验证请求信息的时候进行验证
	 * </p>
	 * 
	 * @author:姚旭民
	 * @date:2017-7-13 下午3:58:29
	 */
	@RequestMapping("/verifyTicket")
	public ModelAndView verifyTicket(HttpSession session, HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		DealResult temp = personService.login(session, request, null);
		model.addObject("result", temp);
		model.setViewName(temp.getUrl());
		return model;
	}
}
