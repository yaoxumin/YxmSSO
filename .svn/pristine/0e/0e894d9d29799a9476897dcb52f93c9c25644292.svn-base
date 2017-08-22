package com.yxm.sso.util.keyword;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.yxm.sso.redis.dao.RedisDao;

public class KeywordFilter {
	/** 直接禁止的 */
	public static Map<String, HashMap<String, Map>> currentMap = new ConcurrentHashMap<String, HashMap<String, Map>>();
	public static HashMap keysMap = new HashMap();
	public static HashMap nowhash = null;
	public static Object wordMap;// map子节点
	private static int matchType = 2; // 1:最小长度匹配 2：最大长度匹配

	/*
	 * <p>说明:各个渠道的过滤字符</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-20 下午2:55:06
	 */
	public static void addKeywords(int channelId, List<String> keywords) {
		keysMap = new HashMap();// 创建对象
		for (int i = 0; i < keywords.size(); i++) {
			String key2 = keywords.get(i).trim();// 去掉空白
			nowhash = keysMap;
			for (int j = 0; j < key2.length(); j++) {
				char word = key2.charAt(j);
				wordMap = nowhash.get(word);
				if (wordMap != null) {// 检查数据
					nowhash = (HashMap) wordMap;
				} else {
					HashMap<String, String> newWordHash = new HashMap<String, String>();
					newWordHash.put("isEnd", "0");
					nowhash.put(word, newWordHash);
					nowhash = newWordHash;
				}
				if (j == key2.length() - 1) {
					nowhash.put("isEnd", "1");
				}
			}
		}

		// 查看是否生成了过滤规则，如果生成了规律规则，保存进入内存中
		if (keysMap != null && keysMap.size() > 0) {
			currentMap.put("ChannelId" + channelId, keysMap);
		}

		keysMap = null;
		nowhash = null;
		wordMap = null;
	}

	/*
	 * <p>说明:替换掉对应的渠道规定掉敏感字</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-20 上午11:41:47
	 */
	public static String repword(int channelId, String txt, String rep) {
		keysMap = currentMap.get("ChannelId" + channelId);
		nowhash = keysMap;
		int l = txt.length();
		char word;
		StringBuilder keyword = new StringBuilder();// 敏感字
		for (int i = 0; i < l; i++) {
			word = txt.charAt(i);
			wordMap = nowhash.get(word);
			if (wordMap != null) {// 找到类似敏感字的字体，开始查询
				keyword.append(word);
				nowhash = (HashMap) wordMap;
				// 遍历到这一步，就符合完整的关键字模板
				if (((String) nowhash.get("isEnd")).equals("1")) {// 确定是敏感字，开始替换
					txt = txt.replaceAll(keyword.toString(), rep);
					nowhash = keysMap;
					keyword.delete(0, keyword.length());// 清空数据
					l = txt.length();// 重新获取字符长度
				}
			} else {// 这个字不是敏感字，直接排除
				nowhash = keysMap;
				keyword.delete(0, keyword.length());// 清空数据
				continue;
			}
		}
		// 清除内存指向
		keysMap = null;
		nowhash = null;
		wordMap = null;
		return txt;

	}

	/*
	 * <p>说明:检查是否存在敏感字</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-20 下午3:00:06
	 */
	private static int checkKeyWords(String txt, int channelId, int begin) {
		int result = 0;
		keysMap = currentMap.get("ChannelId" + channelId);
		nowhash = keysMap;
		int l = txt.length();
		char word = 0;
		for (int i = begin; i < l; i++) {
			word = txt.charAt(i);
			wordMap = nowhash.get(word);
			if (wordMap != null) {
				result++;
				nowhash = (HashMap) wordMap;
				if (((String) nowhash.get("isEnd")).equals("1")) {
					keysMap = null;
					nowhash = null;
					wordMap = null;
					return result;
				}
			} else {
				result = 0;
				break;
			}
		}
		
		keysMap = null;
		nowhash = null;
		wordMap = null;
		return result;
	}

	/**
	 * 返回txt中关键字的列表
	 */
	public static Set<String> getTxtKeyWords(String txt, int channelId) {
		Set set = new HashSet();
		String key;
		int l = txt.length();
		for (int i = 0; i < l;) {
			int len = checkKeyWords(txt, channelId, i);
			if (len > 0) {
				key = (txt.substring(i, i + len));//挑选出来的关键字
				set.add(key);
				txt = txt.replaceAll(key, "");//挑选出来的关键字替换成空白，加快挑选速度
				l = txt.length();
			} else {
				i++;
			}
		}
		txt = null;
		return set;
	}

	/**
	 * 仅判断txt中是否有关键字
	 */
	public boolean isKeyWords(String txt, int channelId) {
		for (int i = 0; i < txt.length(); i++) {
			int len = checkKeyWords(txt, channelId, i);
			if (len > 0) {
				return true;
			}
		}
		txt = null;
		return false;
	}

	public static void main(String[] arg) {
		List<String> keywords = new ArrayList<String>();
		keywords.add("傻×");
		keywords.add("傻D");
		KeywordFilter.addKeywords(1, keywords);
		String txt = "老王是傻×傻×傻A傻B傻C傻D傻×傻×";
		Set set = KeywordFilter.getTxtKeyWords(txt, 1);
		System.out.println("文中包含的敏感字为:" + set);
		System.out.println("原文:" + txt);
		System.out.println("敏感字过滤后:" + repword(1, txt, "*"));
	}
}
