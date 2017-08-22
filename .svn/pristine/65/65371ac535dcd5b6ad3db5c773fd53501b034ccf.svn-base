package com.yxm.sso.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.xiaoleilu.hutool.lang.Base64;
import com.yxm.sso.context.AppContext;
import com.yxm.sso.context.GlobalSession;
import com.yxm.sso.context.UrlPage;
import com.yxm.sso.domain.Person;
import com.yxm.sso.domain.vo.TicketVo;
import com.yxm.sso.domain.vo.UserVo;
import com.yxm.sso.mapper.PersonMapper;
import com.yxm.sso.service.PersonService;
import com.yxm.sso.util.StringUtils;
import com.yxm.sso.util.result.DealResult;
import com.yxm.sso.util.result.DealStatus;
import com.yxm.sso.util.service.TicketServiceImpl;

@Service
public class PersonServiceImpl implements PersonService {
	Log logger = LogFactory.getLog(PersonServiceImpl.class);

	@Resource
	PersonMapper personMapper;

	@Override
	public int addRecord(Person p) {
		return personMapper.addRecord(p);
	}

	@Override
	public void deleteRecord(String id) {
		personMapper.deleteRecord(id);
	}

	@Override
	public List<Person> findListBySql(String sql) {
		return personMapper.findListBySql(sql);
	}

	@Override
	public int saveOrUpdate(Person p) {
		return personMapper.saveOrUpdate(p);
	}

	@Override
	public Person getDetail(String id) {
		return personMapper.getDetail(id);
	}

	@Override
	public Person queryBySql(String sql) {
		return personMapper.queryBySql(sql);
	}

	@Override
	public List<Person> listAll() {
		return personMapper.listAll();
	}

	/**
	 * <p>
	 * 说明:验证登陆者信息,用户已经到SSO认证服务器上登陆的了
	 * </p>
	 * 
	 * @author:姚旭民
	 * @date:2017-7-13 下午4:09:16
	 */
	@Override
	public DealResult login(HttpSession session, HttpServletRequest request,
			Person person) {
		DealResult result = new DealResult();
		String ticket = request.getParameter("ticket");
		// flag,用来标示是否登陆授权成功
		boolean flag = false;
		// 回调地址
		String url = Base64.decodeStr(request.getParameter("url"));// 包含了登陆成功回调地址和退出登录的回调地址
		String backUrl = "";// 用户登录成功的回调地址
		String loginOutUrl = "";// 用户退出登录的回调地址
		String msg = null;

		// 预防措施，默认为错误
		if (url == null) {
			backUrl = UrlPage.PAGE_LOGIN;// 这里后面要做操作，如果非法域名，不做登陆操作了
		} else {
			String[] urls = url.split("&");
			if (urls.length == 2) {
				backUrl = urls[0];
				loginOutUrl = urls[1];
			} else {
				backUrl = loginOutUrl = url;// 其他情况下，都默认使用第一个作为回调地址
			}
		}

		// 查询结果
		Person tempPerson = null;
		// 进入条件，1、person为null(用户没有在SSO输入登陆)，有没有传递回调地址
		// 2、session里面没有存在登陆痕迹，
		// 3、用户没有传递ticket过来，这才开始验证登陆
		// 4、是否被授权登陆这个
		if (StringUtils.isNotBlank(person, person.getUsername(), backUrl,
				loginOutUrl)
				&& StringUtils.isBlank(ticket)
				&& !verifyExistTicket(session)
				&& ApplicationServiceImpl.allowedApplication(request)) {
			String tempTicket;// 进入了这一步代表没有ticket的存在
			// 【1】不存在ticket，查询账号，验证是否存在
			tempPerson = personMapper.verifyPerson(person);
			// 【2】如果查询得到账号，那么证明存在，为其生成ticket
			if (tempPerson != null) {// 账号验证正确，生成ticket，然后根据返回地址带上ticket重定向
				tempPerson.setPassword(null);// 抹除掉密码
				// 【3】生成加密过后的ticket，id，用户名字，用户类型，然后session进入线程池，做这个主要是为了消除登录痕迹的时候使用的
				ticket = TicketServiceImpl.encodeAndSaveTicket(
						tempPerson.getId(), tempPerson.getUsername(),
						tempPerson, backUrl, loginOutUrl, session);
				UserVo userVo = new UserVo(ticket, backUrl, loginOutUrl,
						tempPerson);
				System.out.println();
				// 【4】将登陆痕迹放置到session里面
				session.setAttribute(AppContext.KEY_SESSION_USER, userVo);
				// 【5】设置处理结果
				flag = true;// 授权成功
				// 【6】拆开backUrl，设置跳转url
				url = backUrl;
			}
		} else if (ApplicationServiceImpl.allowedApplication(request)) {// 判断规则，优先是是判断是否存在登陆痕迹，次之检查是否有ticket传递过来
			// 存在登陆痕迹的
			// 存在一种情况，别的用户在这里登陆过，例如甲用户在A网站时候重定向在这里登陆，然后这边存在了登陆痕迹，
			// 然后甲用户又去B网站，B网站还没登陆过，请求重定向到了这里，这时候甲用户是存在了登陆痕迹的，默认为登陆，这时候我们应该查询然后
			// 返回信息到B网站指定的接受ticket的路径上去
			if (verifyExistTicket(session)) {
				UserVo userVo = (UserVo) session
						.getAttribute(AppContext.KEY_SESSION_USER);
				if (userVo != null
						&& StringUtils.isNotBlank(userVo.getBackUrl(),
								userVo.getTicket())) {
					if (StringUtils.isBlank(backUrl)
							|| backUrl.equals(UrlPage.PAGE_LOGIN)) {
						backUrl = userVo.getBackUrl();
						loginOutUrl = userVo.getLoginOutUrl();
					}
					TicketServiceImpl.checkIn(userVo.getPerson().getId(),
							backUrl, loginOutUrl);
					url = backUrl;
					ticket = userVo.getTicket();
					if(GlobalSession.getSession(ticket) == null) {
						GlobalSession.addSession(ticket, session);
					}
					flag = true;
				}
			}
			// 这个代表用户没有在SSO登陆，也没有登陆痕迹，但是有ticket
			// 这种场景就是用户进行了单点登录，这时候的验证是
			// 1、用户合法性，检验是否是授权域名
			// 2、检验ticket合法性
			// 如果验证成功，重定向到最近登陆的客户网站
			else if (StringUtils.isNotBlank(ticket)) {
				// 是存在的有效验证
				TicketVo vo = TicketServiceImpl.getTicketVo(request, ticket);
				if (vo != null) {
					if (StringUtils.isBlank(url))
						url = vo.getUrl();
					result.setCode(DealStatus.SUCCESS.getCode());
					msg = "有效ticket";
					
					if(GlobalSession.getSession(ticket) == null) {
						GlobalSession.addSession(ticket, session);
					}
					flag = true;
				}
				// 是不存在的无效验证
				else {
					msg = "您没有被授权使用单点登录或者ticket失效，请重新登陆";
				}
			}

		} else {
			msg = "您未被授权";
		}

		if (flag) {
			// 如果是验证通过了的，直接改写url,url是跳转地址
			url = "redirect:" + url + "?ticket=" + ticket;// 请求重定向到发起请求的位置
			result.setTicketInfo(DealStatus.SUCCESS.getCode(),
					DealStatus.SUCCESS.getMsg(), url);
		} else {
			// 加密地址
			backUrl = Base64.encode(backUrl + "&" + loginOutUrl);
			// 默认为失败的操作，直接返回了登陆页面
			result.setTicketInfo(DealStatus.LOGIN_ERROR.getCode(), msg, backUrl);
			result.setUrl(UrlPage.PAGE_LOGIN);
		}
		return result;
	}

	/*
	 * <p>Description:用户做出登出处理的时候</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-7-20 下午4:38:24
	 */
	@Override
	public DealResult loginOut(HttpSession session, HttpServletRequest request) {
		DealResult result = null;
		try {
			// 【1】检验是否是授权的网站
			if (ApplicationServiceImpl.allowedApplication(request)) {
				String ticket = request.getParameter(AppContext.KEY_TICKET);// 是否传递了参数过来
				UserVo vo = (UserVo) session
						.getAttribute(AppContext.KEY_SESSION_USER);
				// 【1】如果当前session里面的参数为null，有可能是跨域，传递了ticket过来请求退出，那么可以尝试去全局线程池里面拿session对象
				if (StringUtils.isBlank(vo) && StringUtils.isNotBlank(ticket)) {
					HttpSession tempSession = GlobalSession.getSession(ticket);
					if (StringUtils.isNotBlank(tempSession)) {
						vo = (UserVo) tempSession.getAttribute(AppContext.KEY_SESSION_USER);
					}
				}
				
				if (vo != null && vo.getTicket() != null && ticket == null) {
					ticket = vo.getTicket();
				}
				
				// 【2】清除session里面保存的数据,拿全局线程池保存的ticket进行删除
				if (vo != null && ticket != null) {
					GlobalSession.delSession(ticket);
					session.removeAttribute(AppContext.KEY_SESSION_USER);
					// 【3】清除redis里面缓存的ticket,同时通知用户登录过的网站用户已经登出
					TicketServiceImpl.delTicket(request, ticket);
					result = DealResult.defaultSuccess();
				}
			} else {
				result = DealResult.defaultError();
			}
		} catch (Exception e) {
			// 打包时报处理结果
			logger.info("PersonServiceImpl类的loginOut方法出错");
			e.printStackTrace();
		}
		return result;
	}

	/*
	 * <p>说明:检查类型里面是否存在了登陆成功的包装类型</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-9 下午5:27:33
	 */
	@Override
	public boolean verifyExistTicket(HttpSession session) {
		return session.getAttribute(AppContext.KEY_SESSION_USER) != null ? true
				: false;
	}
}
