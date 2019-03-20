package com.combanc.mysql;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.combanc.pojo.Field_Rules;
import com.combanc.redis.RedisPool;

/**
 * Title:           Mysql
 * Description:     增删改查
 * Company:         combanc
 * Author:          shihw
 * Date:            2018/7/31
 * JDK:             1.8
 * Encoding:        UTF-8
 */
public class MysqlConn {
	private final static Logger logger = LoggerFactory.getLogger(MysqlConn.class);
	static Properties props = new Properties();
	static Connection conn = null;
	static PreparedStatement pstat = null;
	static ResultSet rs = null;

	static{
	     InputStream in = RedisPool.class.getResourceAsStream("/conf" + File.separator + "mysql-connection.properties");
	     try {
			props.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info("读取mysql配置异常！",e);
		}
	}
	
	
	/**
	 * 连接数据库
	 * @return 连接  
	 */
	public static Connection buildMysql() {
		  
		try {
			Class.forName(props.getProperty("jdbc_driverClassName"));
			conn = DriverManager.getConnection(props.getProperty("jdbc_url"), 
					props.getProperty("jdbc_username"), 
					props.getProperty("jdbc_password"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			logger.info("mysql连接异常！",e);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.info("mysql连接异常！",e);
		}
		return conn;

	}

	/**
	 * 查询 关键词
	 * 
	 * @param sql
	 *            查询语句
	 * @return keywords集合
	 */
	public static List<String> select_Company(String sql) {
		List<String> list = new ArrayList<>();
		conn = buildMysql();
		try {
			pstat = conn.prepareStatement(sql);
			rs = pstat.executeQuery();
			while (rs.next()) {
				String keyWord = rs.getString("company_name");
				list.add(keyWord);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeMysql();
		}
		return list;
	}

	/**
	 * 查询 规则
	 * 
	 * @param sql
	 *            查询语句
	 * @return keywords集合
	 */
	public static Field_Rules select_rules(String sql) {
		conn = buildMysql();
		Field_Rules rules = null;
		try {
			pstat = conn.prepareStatement(sql);
			rs = pstat.executeQuery();
			while (rs.next()) {
				String url = rs.getString("url");
				String url_list_rule = rs.getString("url_list_rule");
				String id_rule = rs.getString("id_rule");
				String title_rule = rs.getString("title_rule");
				String company_name_rule = rs.getString("company_name_rule");
				String welfare_rule = rs.getString("welfare_rule");
				String salary_rule = rs.getString("salary_rule");
				String city_rule = rs.getString("city_rule");
				String publication_date_rule = rs.getString("publication_date_rule");
				String job_property_rule = rs.getString("job_property_rule");
				String years_rule = rs.getString("years_rule");
				String education_rule = rs.getString("education_rule");
				String recruitment_number_rule = rs.getString("recruitment_number_rule");
				String job_category_rule = rs.getString("job_category_rule");
				String job_description_rule = rs.getString("job_description_rule");
				String company_description_rule = rs.getString("company_description_rule");
				String company_size_rule = rs.getString("company_size_rule");
				String company_type_rule = rs.getString("company_type_rule");
				String company_industry_rule = rs.getString("company_industry_rule");
				String company_web_rule = rs.getString("company_web_rule");
				String company_address_rule = rs.getString("company_address_rule");
				String create_time_rule = rs.getString("create_time_rule");
				String next_page_rule = rs.getString("next_page_rule");
				String jd_num_rule = rs.getString("jd_num_rule");
				int page_num_rule = rs.getInt("page_num_rule");
				String resource_rule = rs.getString("resource_rule");
				rules = new Field_Rules(url, url_list_rule, id_rule, title_rule, company_name_rule,
						welfare_rule, salary_rule, city_rule, publication_date_rule, job_property_rule, years_rule,
						education_rule, recruitment_number_rule, job_category_rule, job_description_rule,
						company_description_rule, company_size_rule, company_type_rule, company_industry_rule,
						company_web_rule, company_address_rule, create_time_rule, next_page_rule, jd_num_rule,
						page_num_rule, resource_rule);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeMysql();
		}
		return rules;
	}

	public int exceuteUpdate(String sql, Object... prams) {
		int result = 0;
		conn = buildMysql();
		try {
			pstat = conn.prepareStatement(sql);
			for (int i = 0; i < prams.length; i++) {
				pstat.setObject(i + 1, prams[i]);
			}

			result = pstat.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeMysql();
		}
		return result;
	}

	// 关闭数据库
	public static void closeMysql() {

		try {
			if (conn != null) {
				conn.close();
			}
			if (pstat != null) {
				pstat.close();
			}
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
