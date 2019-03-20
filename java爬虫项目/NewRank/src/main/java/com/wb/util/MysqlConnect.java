package com.wb.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.wb.pojo.Account;
import com.wb.pojo.KeyWord;
import com.wb.pojo.urlList;

public class MysqlConnect {
	// private static ArrayList<WebSite> list = new ArrayList<>();
	Connection conn = null;
	PreparedStatement pstat = null;
	ResultSet rs = null;
	static MysqlConnect jdbc_MYSQL = new MysqlConnect();

	public Connection buildMysql() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
//			 conn = DriverManager.getConnection("jdbc:mysql://mengxsh.cn:49677/digital",
//			 "wangbiao", "wangbiao");
			conn = DriverManager.getConnection("jdbc:mysql://192.168.3.69:3306/digital?characterEncoding=UTF-8",
					"wangbiao", "wangbiao");
			// conn =
			// DriverManager.getConnection("jdbc:mysql://127.0.0.1/b_wechat?characterEncoding=UTF-8",
			// "root",
			// "123");
			if (conn != null) {
				System.out.println("已连接");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;

	}

	public ArrayList<urlList> selecturl() {
		ArrayList<urlList> list = new ArrayList<urlList>();
		Connection conn = jdbc_MYSQL.buildMysql();
		String sql = "select url,keyword,cid,flag,type from t_urllist where flag=0 and type=3 limit 20000";
		ResultSet rs = null;
		try {
			pstat = conn.prepareStatement(sql);
			rs = pstat.executeQuery();
			while (rs.next()) {
				String url = rs.getString("url");
				String key = rs.getString("keyword");
				int cid = rs.getInt("cid");
				int flag = rs.getInt("flag");
				int type = rs.getInt("type");
				urlList urllist = new urlList(key, url, flag, type, cid);
				list.add(urllist);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public ResultSet repeaturl(Connection conn, String sql) {
		ResultSet rs = null;
		try {
			pstat = conn.prepareStatement(sql);
			rs = pstat.executeQuery();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public int repeatCount(Connection conn, String sql) {
		ResultSet rs = null;
		int num = 0;
		try {
			pstat = conn.prepareStatement(sql);
			rs = pstat.executeQuery();

			while (rs.next()) {
				num = rs.getInt("count");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return num;
	}

	public int repeatState(Connection conn, String sql) {
		ResultSet rs = null;
		int state = 0;
		try {
			pstat = conn.prepareStatement(sql);
			rs = pstat.executeQuery();

			while (rs.next()) {
				state = rs.getInt("state");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return state;
	}

	public void WriteStringToFile(String filePath, String text) {
		try {
			File file = new File(filePath);
			PrintStream ps = new PrintStream(new FileOutputStream(file));
			// ps.println("http://www.jb51.net");// 往文件里写入字符串
			ps.append(text + ";");// 在已有的基础上添加字符串
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int update(Connection conn, String sql) {
		int num = 0;
		try {
			pstat = conn.prepareStatement(sql);
			num = pstat.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeMysql();
		}
		return num;

	}

	public void closeMysql() {

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

	public String getSiteType(Connection conn, String sql) {
		ResultSet rs = null;
		String sitetype = "";
		try {
			pstat = conn.prepareStatement(sql);
			rs = pstat.executeQuery();

			while (rs.next()) {
				sitetype = rs.getString("name");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeMysql();
		}
		return sitetype;
	}

	public static void setrule(String href, String titlerule, String sourcerule, String publishtimerule,
			String contentrule, int sitetype, int ruletype, int state, String key, int cid) {

		Connection conn = jdbc_MYSQL.buildMysql();
		String sql = "insert into t_jobqueue (url,titlerule,sourcerule,publishtimerule,contentrule,ruletype,state,abbreviation,cid) values"
				+ " ('" + href + "','" + titlerule + "','" + sourcerule + "','" + publishtimerule + "','" + contentrule
				+ "'," + ruletype + "," + state + ",'" + key + "'," + cid + ")";
		System.out.println(sql);
		jdbc_MYSQL.update(conn, sql);
	}

	public int repeatCount() {
		Connection conn = jdbc_MYSQL.buildMysql();
		String sql = "select count(1) as count  from jobqueue";
		ResultSet rs = null;
		int num = 0;
		try {
			pstat = conn.prepareStatement(sql);
			rs = pstat.executeQuery();

			while (rs.next()) {
				num = rs.getInt("count");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return num;
	}

	public int update(String sql) {
		Connection conn = jdbc_MYSQL.buildMysql();
		int num = 0;
		try {
			pstat = conn.prepareStatement(sql);
			num = pstat.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeMysql();
		}
		return num;

	}

	public ArrayList<KeyWord> selectKeyWord(Connection conn, String sql) {
		ArrayList<KeyWord> list = new ArrayList<KeyWord>();
		ResultSet rs = null;
		try {
			pstat = conn.prepareStatement(sql);
			rs = pstat.executeQuery();
			while (rs.next()) {
				int cid = Integer.parseInt(rs.getString("id"));
				String keyWord = rs.getString("abbreviation");
				String company_name = rs.getString("company_name");
				// System.out.println(keyWord);
				KeyWord keyword = new KeyWord(cid, keyWord, company_name);
				list.add(keyword);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeMysql();
		}
		return list;

	}

	public ArrayList<KeyWord> selectKeyWord2(Connection conn, String sql) {
		ArrayList<KeyWord> list = new ArrayList<KeyWord>();
		ResultSet rs = null;
		try {
			pstat = conn.prepareStatement(sql);
			rs = pstat.executeQuery();
			while (rs.next()) {
				int cid = Integer.parseInt(rs.getString("id"));
				String keyWord = rs.getString("company_name");
				// System.out.println(keyWord);
				KeyWord keyword = new KeyWord(cid, keyWord);
				list.add(keyword);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeMysql();
		}
		return list;

	}

	public int insertWechat(String abbreviation, String head, String p_title, String wechatid, String subtool,
			String approve, String type, String time) {
		Connection conn = buildMysql();
		String sql = "insert into t_wechat (abbreviation,head,ptitle,wechatid,subtool,approve,type,time) values ('"
				+ abbreviation + "','" + head + "','" + p_title + "','" + wechatid + "','" + subtool + "','" + approve
				+ "','" + type + "','" + time + "')";
		System.out.println(sql);
		int num = 0;
		try {
			pstat = conn.prepareStatement(sql);
			num = pstat.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeMysql();
		}
		return num;

	}

	public int insertHY(String text, String company_name) throws IOException {
//		Connection conn = buildMysql();
		String sql = "update t_company set business_license_pic='" + text + "' where company_name='" + company_name
				+ "';";
		System.out.println(sql);
		FileOutputStream fos = new FileOutputStream("C:\\Users\\HuSan\\Documents\\SQL.txt", true);
		// true表示在文件末尾追加
		fos.write(sql.getBytes());
		fos.close();
		// WriteStringToFile("C:\\Users\\HuSan\\Documents\\SQL.txt", sql);
		// int num = 0;
		// try {
		// pstat = conn.prepareStatement(sql);
		// num = pstat.executeUpdate();
		//// conn.commit();
		// System.out.println(num);
		// } catch (SQLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } finally {
		// closeMysql();
		// }
		// return num;
		return 0;

	}

	public int insertAccount(int tid, int did, int company_id, String aname, String account_id, String alogo,
			String subtool, int fans, int sendNums, String approve) {
		Connection conn = buildMysql();
		String sql = "insert into t_company_account (tid,did,company_id,aname,account_id,alogo,subtool,fans,snum,approve) values ("
				+ tid + "," + did + "," + company_id + ",'" + aname + "','" + account_id + "','" + alogo + "','"
				+ subtool + "'," + fans + "," + sendNums + ",'" + approve + "')";
		System.out.println(sql);
		int num = 0;
		try {
			pstat = conn.prepareStatement(sql);
			num = pstat.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeMysql();
		}
		return num;
	}

	public int insertAccount2(Account account) {
		Connection conn = buildMysql();
		String sql = "insert into t_company_account (tid,did,company_id,aname,account_id,alogo,subtool,fans,snum,approve) values ("
				+ account.getTid() + "," + account.getDid() + "," + account.getCompany_id() + ",'" + account.getAname()
				+ "','" + account.getAccount_id() + "','" + account.getAlogo() + "','" + account.getSubtool() + "',"
				+ account.getFans() + "," + account.getSnum() + ",'" + account.getApprove() + "')";
		System.out.println(sql);
		int num = 0;
		try {
			pstat = conn.prepareStatement(sql);
			num = pstat.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeMysql();
		}
		return num;
	}

	public int insertWeibo(String abbreviation, String headurl, String p_title, String tubiao, String jiehsao,
			int weibo, int guanzhu, int fans, String type) {
		Connection conn = buildMysql();
		String sql = "insert into t_accountNumber (abbreviation,head,ptitle,approveType,description,sendNumber,attention,fans,type) values ('"
				+ abbreviation + "','" + headurl + "','" + p_title + "','" + tubiao + "','" + jiehsao + "'," + weibo
				+ "," + guanzhu + "," + fans + ",'" + type + "')";
		System.out.println(sql);
		int num = 0;
		try {
			pstat = conn.prepareStatement(sql);
			num = pstat.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeMysql();
		}
		return num;

	}

	public int insertNews(int cid, String rtime, String link, String title, String cf, String body, String images,
			String reid, String reidsrc, String website, int readNum, int praisenum, int recommendnum, int tnum) {
		Connection conn = buildMysql();
		String sql = "insert into t_news (cid,rtime,link,title,cf,body,images,reid,reidsrc,website,pv,lnum,conum,tnum) values ("
				+ cid + ",'" + rtime + "','" + link + "','" + title + "','" + cf + "','" + body + "','" + images + "','"
				+ reid + "','" + reidsrc + "','" + website + "'," + readNum + "," + praisenum + "," + recommendnum + ","
				+ tnum + ")";
		System.out.println(sql);
		int num = 0;
		try {
			pstat = conn.prepareStatement(sql);
			num = pstat.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeMysql();
		}
		return num;

	}

	public int insertNews2(int cid, String rtime, String link, String title, String cf, String body, String images,
			String reid, String reidsrc, String website, int readNum, int praisenum, int recommendnum, int tnum,
			String section) {
		Connection conn = buildMysql();
		String sql = "insert into t_news (cid,rtime,link,title,cf,body,images,reid,reidsrc,website,pv,lnum,conum,tnum,sections) values ("
				+ cid + ",'" + rtime + "','" + link + "','" + title + "','" + cf + "','" + body + "','" + images + "','"
				+ reid + "','" + reidsrc + "','" + website + "'," + readNum + "," + praisenum + "," + recommendnum + ","
				+ tnum + ",'" + section + "')";
		System.out.println(sql);
		int num = 0;
		try {
			pstat = conn.prepareStatement(sql);
			num = pstat.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeMysql();
		}
		return num;

	}

	public int insertUrl(String url, String key, int cid, int type) {
		Connection conn = buildMysql();
		String sql = "insert into t_urllist (url,keyword,cid,type) values ('" + url + "','" + key + "'," + cid + ","
				+ type + ")";
		System.out.println(sql);
		int num = 0;
		try {
			pstat = conn.prepareStatement(sql);
			num = pstat.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeMysql();
		}
		return num;

	}

	public int insertToutiao(int id, String abbreviation, String url, String title, String time, String source,
			String content, int comments, String type) {
		Connection conn = buildMysql();
		String sql = "insert into t_data2 (cid,abbreviation,href,title,time,source,content,comments,type) values ('"
				+ id + "','" + abbreviation + "','" + url + "','" + title + "','" + time + "','" + source + "','"
				+ content + "'," + comments + ",'" + type + "')";
		System.out.println(sql);
		int num = 0;
		try {
			pstat = conn.prepareStatement(sql);
			num = pstat.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeMysql();
		}
		return num;

	}

	public int insertWeibo(int id, String abbreviation, String author, String title, String time, String content,
			int likes, int zhuanfa, int comments, String type) {
		Connection conn = buildMysql();
		String sql = "insert into t_data2 (cid,abbreviation,title,author,time,content,transpond,likes,comments,type) values ('"
				+ id + "','" + abbreviation + "','" + title + "','" + author + "','" + time + "','" + content + "',"
				+ zhuanfa + "," + likes + "," + comments + ",'" + type + "')";
		System.out.println(sql);
		int num = 0;
		try {
			pstat = conn.prepareStatement(sql);
			num = pstat.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeMysql();
		}
		return num;

	}

	public int updateflag(String href) {
		Connection conn = buildMysql();
		String sql = "update t_urllist set flag=1 where url='" + href + "'";
		System.out.println(sql);
		int num = 0;
		try {
			pstat = conn.prepareStatement(sql);
			num = pstat.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeMysql();
		}
		return num;

	}

	public int updateflag2(String href) {
		Connection conn = buildMysql();
		String sql = "update t_urllist set flag=2 where url='" + href + "'";
		System.out.println(sql);
		int num = 0;
		try {
			pstat = conn.prepareStatement(sql);
			num = pstat.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeMysql();
		}
		return num;

	}

	public int insertCity(String city) {
		Connection conn = buildMysql();
		String sql = "insert into t_city (city) values ('" + city + "')";
		System.out.println(sql);
		int num = 0;
		try {
			pstat = conn.prepareStatement(sql);
			num = pstat.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeMysql();
		}
		return num;

	}

}
