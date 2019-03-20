package com.wb.util;

import org.apache.log4j.Logger;
import org.logicalcobwebs.proxool.configuration.JAXPConfigurator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.*;

/**
 * 数据库链接辅助类
 * @author wd
 * @version jdk 1.7
 * @date 2018年7月20日
 */
public class JdbcUtil {


	private static Logger logger = Logger.getLogger(JdbcUtil.class);
	public static boolean autoCommit = true;
	
	static{
		configure();
	}
	
	public static synchronized void configure(){
		InputStream is = null;

		try {
			//is = JdbcUtil.class.getClassLoader().getResourceAsStream("conf/proxool.xml");
			is = new FileInputStream("conf/proxool.xml");
			Reader reader = new InputStreamReader(is);
			JAXPConfigurator.configure(reader, false);
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if(null != is){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 得到一个数据库连接对象，在使用前，请保证数据库连接配置文件存在，并正确配置
	 * 
	 * @return 返回数据库连接对象Connection
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 *             抛出类无法找到异常
	 * @throws SQLException
	 *             抛出SQL异常
	 */
	public static synchronized Connection getConn(String dbName)
			throws SQLException {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("proxool." + dbName);
		} catch (Exception e) {
			logger.info("get connection fail, will be get again！\t" + e.getMessage());
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return getConn(dbName);
		}
		return conn;
	}
	
	/**
	 * 关闭数据库连接的相关对象
	 * @param rs  要关闭的ResultSet 对象
	 * @param pstmt  要关闭的Statement 对象
	 * @param conn  要关闭的Connection 对象
	 */
	public static void close(ResultSet rs, Statement pstmt, Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				logger.error("Cloes ResulstSet Error!");
			}
		}
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				logger.error("Cloes Statement Error!");
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("Cloes Statement Error!");
			}
		}

	}


}
