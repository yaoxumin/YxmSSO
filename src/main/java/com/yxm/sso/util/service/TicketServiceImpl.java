package com.yxm.sso.util.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.xiaoleilu.hutool.lang.Base64;
import com.xiaoleilu.hutool.util.ObjectUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import com.yxm.sso.context.AppContext;
import com.yxm.sso.context.GlobalSession;
import com.yxm.sso.domain.Person;
import com.yxm.sso.domain.vo.TicketVo;
import com.yxm.sso.domain.vo.UserVo;
import com.yxm.sso.redis.dao.RedisDao;
import com.yxm.sso.service.impl.ApplicationServiceImpl;
import com.yxm.sso.util.StringUtils;
import com.yxm.sso.util.result.ApiResult;

/**
 * <p>
 * 说明:跨域请求处理业务类
 * </p>
 * 
 * @author:姚旭民
 * @date:2017-7-11 上午10:14:14
 */
public class TicketServiceImpl {
	static Log logger = LogFactory.getLog(TicketServiceImpl.class);
	static String USERURL = "UserUrl";
	static String TICKET = "TicketVo";

	/*
	 * <p>说明:登记入住</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-16 下午5:33:05
	 */
	public static boolean checkIn(int id, String login, String loginOut) {
		try {
			// 指定保存在redis里面的规则
			if (StringUtils.isNotBlank(id, login, loginOut)) {
				String key = "PersonLogin" + id;// redis里面的保存的key的值
				if (RedisDao.exitsMap(key)) {// 查询是否存在这个保存信息
					Map<String, String> data = new HashMap<String, String>();
					data.put(login, loginOut);
					RedisDao.saveMapData(key, data);// 创建并且保存数据
				} else {
					RedisDao.saveMapData(key, login, loginOut);// 如果存在数据，就追加数据
				}
				return true;
			}
		} catch (Exception e) {
			logger.info("RedisServiceImpl类的checkIn方法出错");
		}
		return false;
	}

	/*
	 * <p> 说明:当接受到用户的退出登录请求的时候，执行这里面的退出操作 同时， 拿出redis里面的数据，同时通知用户登录的网站，消除用户信息
	 * </p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-13 下午1:15:16
	 */
	public static boolean checkOut(int id, String ticket) {
		try {
			if (StringUtils.isNotBlank(id)) {
				String key = "PersonLogin" + id;
				// 【1】取出redis里面的 数据
				Map<String, String> url = RedisDao.getMapData(key);
				if (StringUtils.isNotBlank(url)) {
					for (Entry<String, String> entry : url.entrySet()) {
						HttpClientServiceImpl.noticeLoginOut(entry.getValue(),
								ticket);
					}
				}
			}
		} catch (Exception e) {

		}
		return false;
	}

	/*
	 * <p>说明:加密地址</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-16 下午5:29:58
	 */
	public static ApiResult encodeUrl(HttpServletRequest request) {
		ApiResult result = null;
		boolean flag = false;
		String url = null;
		if (ApplicationServiceImpl.allowedApplication(request)) {
			url = request.getParameter("url");
			if (StringUtils.isNotBlank(url)) {
				url = Base64.encode(url);
			}
		}

		if (flag) {
			result = ApiResult.defaultSuccess();
			result.setData(url);
		} else {
			result = ApiResult.defaultError();
			result.setMsg("您尚未开通此服务，请确认");
		}
		return result;
	}

	/**
	 * <p>
	 * 说明:返回加密的ticket,并且把ticket写进去redis,然后在GlobalSession保存session
	 * </p>
	 * 
	 * @author:姚旭民
	 * @date:2017-7-10 下午2:27:16
	 */
	public static String encodeAndSaveTicket(int userId, String name,
			Person person, String backUrl, String loginOutUrl,
			HttpSession session) {
		String result = null;
		try {
			// 【1】生产ticket,组成为，id，名字，类型，UUID
			result = UUID.randomUUID().toString();
			StringBuilder str = new StringBuilder();
			result = str.append(userId).append(AppContext.KEY_SPILT_DEFAULT)
					.append(name).append(AppContext.KEY_SPILT_DEFAULT)
					.append(TicketVo.class.getSimpleName())
					.append(AppContext.KEY_SPILT_DEFAULT).append(result)
					.toString();
			result = Base64.encode(result);
			TicketVo vo = new TicketVo(person);
			vo.setUrl(backUrl);// 记住回调地址
			// 【2】将数据保存到redis里面
			RedisServiceImpl.saveMapCache(vo, userId + "");
			// 【3】将session放进入session线程池，key值为ticket的值,result就是ticket
			GlobalSession.addSession(result, session);
			// 【4】将用户登录过的网站的登陆登出地址写进入redis，方便后面用户退出登录通知这些网站
			checkIn(userId, backUrl, loginOutUrl);
		} catch (Exception e) {
			System.out.println("TicketUtils的getTicket出错");
			logger.info("TicketUtils的getTicket出错");
		}
		return result;
	}

	/*
	 * <p>说明:解密地址</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-16 下午5:34:40
	 */
	public static ApiResult decodeUrl(HttpServletRequest request) {
		ApiResult result = null;
		boolean flag = false;
		String url = null;
		if (ApplicationServiceImpl.allowedApplication(request)) {
			url = request.getParameter("url");
			if (StringUtils.isNotBlank(url)) {
				url = Base64.decodeStr((url));
			}
		}

		if (flag) {
			result = ApiResult.defaultSuccess();
			result.setData(url);
		} else {
			result = ApiResult.defaultError();
			result.setMsg("您尚未开通此服务，请确认");
		}
		return result;
	}

	/*
	 * <p>说明:身份票据解密</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-6 下午2:39:37
	 */
	public static String decodeOfTicket(String ticket) {
		String result = null;
		if (StrUtil.isNotBlank(ticket)) {
			result = Base64.decodeStr(ticket);
		}
		return result;
	}

	/*
	 * <p>说明:解密变成Map</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-6 下午10:38:49
	 */
	public static Map decodeOfMap(String ticket) {
		Map<String, String> result = new HashMap<String, String>();
		String temp = decodeOfTicket(ticket);
		result.put(AppContext.KEY_TICKET_MAP_ID,
				temp.split(AppContext.KEY_SPILT_DEFAULT)[0]);
		result.put(AppContext.KEY_TICKET_MAP_NAME,
				temp.split(AppContext.KEY_SPILT_DEFAULT)[1]);
		result.put(AppContext.KEY_TICKET_MAP_TYPE,
				temp.split(AppContext.KEY_SPILT_DEFAULT)[2]);
		result.put(AppContext.KEY_TICKET_MAP_UUID,
				temp.split(AppContext.KEY_SPILT_DEFAULT)[3]);
		return result;
	}

	/*
	 * <p>说明:删除redis里面缓存的ticket并且消除redis里面的用户痕迹</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-9 下午5:50:28
	 */
	public static boolean delTicket(HttpServletRequest request, String ticket) {
		// 【1】查看是否是有授权网站进行的删除redis数据
		if (StringUtils.isNotBlank(ticket)
				&& ApplicationServiceImpl.allowedApplication(request)) {
			// 【2】删除ticket信息
			String key = getRedisKey(ticket);
			RedisServiceImpl.delMapCache(key);
			// 【3】取出redis里面的用户的登陆过的网站的信息，然后逐一通知，该用户已经退出登录的操作
			key = key.replace(TICKET, "");
			// 【4】获取得到用户的历史登陆痕迹，操作完，删除掉
			checkOut(Integer.parseInt(key), ticket);
		}
		return false;
	}

	/*
	 * <p>说明:分解组成ticket保存在Redis里面的key</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-8 下午11:36:20
	 */
	public static String getRedisKey(String ticket) {
		String result = null;
		if (StringUtils.isNotBlank(ticket)) {
			String decodeTicket = decodeOfTicket(ticket);// 解密ticket
			if (StringUtils.isNotBlank(decodeTicket)) {
				result = decodeTicket.split(AppContext.KEY_SPILT_DEFAULT)[2]
						+ decodeTicket.split(AppContext.KEY_SPILT_DEFAULT)[0];
			}
		}
		return result;
	}

	/*
	 * <p>说明:通过ticket获取用户信息,对外接口</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-6 下午4:07:41
	 */
	public static ApiResult getUserInfo(HttpServletRequest request) {
		ApiResult result = new ApiResult();
		result.setCode(-1);
		result.setMsg("无效的ticket或者您未被授权");
		String ticket = request.getParameter(AppContext.KEY_TICKET);// 获取传输中的ticket
		// 【1】检查是否是存在的ticket，同时检查这个域名是否是客户域名
		if (StringUtils.isNotBlank(ticket)
				&& ApplicationServiceImpl.allowedApplication(request)) {
			try {
				String key = getRedisKey(ticket);
				Map<String, String> map = RedisServiceImpl.getMapData(key);
				// 【2】、域名检测合法，查找ticket是否存在数据
				if (ObjectUtil.isNotNull(map)) {// 如果在redis里面找到了数据
					result.setCode(200);
					result.setMsg("获取信息成功");
					TicketVo person = TicketVo.fromJson(JSON.toJSONString(map));// 转化成对象
					result.setData(person);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/*
	 * <p>说明:获取redis里面的对象</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-10 上午10:23:16
	 */
	public static TicketVo getTicketVo(HttpServletRequest request, String ticket) {
		TicketVo result = null;
		// 【1】检查是否是存在的ticket，同时检查这个域名是否是客户域名
		if (StringUtils.isNotBlank(ticket)
				&& ApplicationServiceImpl.allowedApplication(request)) {
			try {
				String key = getRedisKey(ticket);
				Map<String, String> map = RedisServiceImpl.getMapData(key);
				// 【2】、域名检测合法，查找ticket是否存在数据
				if (ObjectUtil.isNotNull(map)) {// 如果在redis里面找到了数据
					result = TicketVo.fromJson(JSON.toJSONString(map));// 转化成对象
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/*
	 * <p>说明:验证ticket是否存在</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-7 上午9:23:34
	 */
	public static ApiResult verifyTicket(HttpServletRequest request) {
		String ticket = request.getParameter(AppContext.KEY_TICKET);
		// 【1】首先判断这个请求过来的ip是不是被允许的客户端域名
		if (StringUtils.isNotBlank(ticket)
				&& ApplicationServiceImpl.allowedApplication(request)) {
			// 【2】验证这个ticket是否存在
			String key = getRedisKey(ticket);
			if (StringUtils.isNotBlank(key)) {
				if (RedisServiceImpl.exitMap(key)) {
					return ApiResult.defaultSuccess();
				}
			}
		}
		return ApiResult.defaultError();
	}

}
