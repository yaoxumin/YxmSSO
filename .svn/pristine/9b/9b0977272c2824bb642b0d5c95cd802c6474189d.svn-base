package com.yxm.sso.util.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import com.yxm.sso.util.PropertiesUtils;
import com.yxm.sso.util.StringUtils;

public class DButil {
	private static DButil db = getInstance();
	private static Connection conn;

	public synchronized static DButil getInstance() {
		if (db == null) {
			db = new DButil();
			init();
		}
		return db;
	}

	/*
	 * <p>说明:初始化配置</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @data:2017-8-9 下午2:31:38
	 */
	private static void init() {
		Properties pro = PropertiesUtils.getInstance().load(
				"config/jdbc.properties");
		try {
			String url = pro.getProperty("url");
			String name = pro.getProperty("username");
			String password = pro.getProperty("password");
			// 1.加载驱动程序
			Class.forName("com.mysql.jdbc.Driver");
			// 2.获得数据库的连接
			conn = DriverManager.getConnection(url, name, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * <p>说明:获取数据库里面对应的数据</p>
	 * 
	 * @author:姚旭民
	 * 
	 * @param:sql,查询的语句
	 * 
	 * @param:key,想要获取的关键的信息
	 * 
	 * @data:2017-8-9 上午11:43:44
	 */
	public Set findBySql(String sql, String key) {
		Set<String> result = new HashSet<String>();
		// 3.通过数据库的连接操作数据库，实现增删改查
		Statement stmt;
		if (StringUtils.isNotBlank(sql)) {
			try {
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);// 选择import,java.sql.ResultSet;
				if (StringUtils.isBlank(key))
					key = "*";
				while (rs.next()) {// 如果对象中有数据，就会循环打印出来
					result.add(rs.getString(key));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
