package Excel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlCon {
	/**
	 * �������ݿ������
	 * @return
	 */
	public  static Connection mysqlConn(){
	       // ����������      
		   String driver = "com.mysql.jdbc.Driver";  
		          // URLָ��Ҫ���ʵ����ݿ���world        
		   String url = "jdbc:mysql:///zhaobiao?useUnicode=true&amp;characterEncoding=UTF-8&autoReconnect=true&rewriteBatchedStatements=true";  
		          // MySQL����ʱ���û���           
		   String user = "root";           
		   // MySQL����ʱ������          
		   String password = "123";
		   Connection  conn = null;
		    // ������������        
		   try {
			Class.forName(driver);
		  // �������ݿ�  
			conn = DriverManager.getConnection(url, user, password);
		   } catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		   } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		   return conn;
	}
	/**
	 * mysql���ݿ��ѯ
	 * @param conn
	 * @param sql
	 * @return
	 */
	public  ResultSet mysqlQuery(Connection conn,Statement st,String sql){
		ResultSet rs = null;
		try {
			rs = st.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * mysql�������
	 */
	public Integer mysqlInsert(Connection conn,Statement st,String sql){
		int sucess = 0; 
		try {
		 sucess = st.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sucess;
	}
	/**
	 * �ر�����
	 * @param conn
	 * @param st
	 * @param rs
	 */
	public void closeMysqlConn(Connection conn,Statement st,ResultSet rs){
		try {
				if(conn!=null){
					conn.close();
					conn = null;
				}
				if(st!=null){
					st.close();
				}
				if(rs!=null){
					rs.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
	}
	
	
}
