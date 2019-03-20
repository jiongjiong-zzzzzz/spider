package com.combanc.mongodb;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import com.combanc.pojo.Content;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;

/**
 * Title:           MongoDBConn
 * Description:     连接mongodb
 * Company:         combanc
 * Author:          shihw
 * Date:            2018/7/20
 * JDK:             1.8
 * Encoding:        UTF-8
 */
public class MongoDBConn {
	private static Logger logger = Logger.getLogger(MongoDBConn.class);
	private static Properties properties = new Properties();
	private static InputStream mongoConf = new MongoDBConn().getClass()
			.getResourceAsStream("/conf" + File.separator + "mongodb-connection.properties");
	private static MongoClient mg = null;
	private static DB db;
	private static DBCollection users;

	public MongoDBConn() {
		
	}

	static{
		try {
			properties.load(mongoConf);
		} catch (IOException e) {
			logger.error("读取mongodb-connection.properties文件发生异常",e);
		} finally {
			try {
				if (mongoConf != null) {
					mongoConf.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		init();
	}
	
	@SuppressWarnings("deprecation")
	public static void init() {
		try {
			String[] hostports = properties.getProperty("mongodb.hostports").split(",");
			List<ServerAddress> sends = new ArrayList<ServerAddress>();
			for (String hostport : hostports) {
				String host = hostport.split(":")[0];
				int port = Integer.valueOf(hostport.split(":")[1]);
				ServerAddress sa = new ServerAddress(host, port);
				sends.add(sa);
			}
			
//	        List<MongoCredential> mongoCredentialList = new ArrayList<MongoCredential>();
//	        mongoCredentialList.add(MongoCredential.createMongoCRCredential("admin", "admin","admin".toCharArray()));
//	        mg = new MongoClient(sends,mongoCredentialList);
	        MongoClientOptions.Builder build = new MongoClientOptions.Builder(); 
	        build.connectionsPerHost(Integer.valueOf(properties.getProperty("mongodb.connectionsPerHost")));// 与目标数据库可以建立的最大链接数
	        build.connectTimeout(Integer.valueOf(properties.getProperty("mongodb.connectTimeout")));// 与数据库建立链接的超时时间
	        build.maxWaitTime(Integer.valueOf(properties.getProperty("mongodb.maxWaitTime")));// 一个线程成功获取到一个可用数据库之前的最大等待时间
	        build.threadsAllowedToBlockForConnectionMultiplier(Integer.valueOf(properties.getProperty("mongodb.threadsAllowedToBlockForConnectionMultiplier")));
	        build.socketTimeout(Integer.valueOf(properties.getProperty("mongodb.socketTimeout")));
	      
	        MongoClientOptions options = build.build();   
	        mg = new MongoClient(sends,options);
	        
	      } catch (Exception e) {
	        logger.error("连接MongoDB数据库错误",e);
	      }

		// 获取temp DB；如果默认没有创建，mongodb会自动创建
		db = mg.getDB(properties.getProperty("mongodb.database"));
		// 获取users DBCollection；如果默认没有创建，mongodb会自动创建
		users = db.getCollection(properties.getProperty("mongodb.collection"));

	}

	/**
	 * 获取数据总量
	 * @return  数量
	 */
	public int findCount() {
		int count = users.find().count();

		return count;
	}

	/**
	 * 将数据放到实体里
	 * @return Content集合
	 */
	public ArrayList<Content> find() {
		DBCursor cur = users.find();
		ArrayList<Content> list = new ArrayList<Content>();
		while (cur.hasNext()) {
			Content content = new Content();
			content.parse(cur.next());
			list.add(content);
		}
		return list;

	}

	/**
	 * 将数据放到实体里 
	 * @param start 开始条数
	 * @param end	结束条数
	 * @return Content集合
	 */
	public ArrayList<Content> limitFind(int start, int end) {
		ArrayList<Content> list = new ArrayList<Content>();
		DBCursor cur = users.find();
		DBCursor queryByPage = cur.skip(start).limit(end);
		while (queryByPage.hasNext()) {
			Content Content = new Content();
			Content.parse(cur.next());
			list.add(Content);
		}
		return list;

	}

	/**
	 * 向Collection里添加一条数据
	 * @param content Content实体
	 */
	public static void AddContent(Content content) {
		DBObject Content = new BasicDBObject();
		Content.put("_id",content.getId());
		Content.put("title", content.getTitle());
		Content.put("company_name", content.getCompany_name());
		Content.put("welfare", content.getWelfare());
		Content.put("salary", content.getSalary());
		Content.put("city", content.getCity());
		Content.put("publication_date", content.getPublication_date());
		Content.put("job_property", content.getJob_property());
		Content.put("years", content.getYears());
		Content.put("education", content.getEducation());
		Content.put("recruitment_number", content.getRecruitment_number());
		Content.put("job_category", content.getJob_category());
		Content.put("job_description", content.getJob_description());
		Content.put("company_description", content.getCompany_description());
		Content.put("company_size", content.getCompany_size());
		Content.put("company_type", content.getCompany_type());
		Content.put("company_industry", content.getCompany_industry());
		Content.put("company_web", content.getCompany_web());
		Content.put("company_address", content.getCompany_address());
		Content.put("create_time", content.getCreate_time());
		Content.put("resource", content.getResource());
		Content.put("state", 0);
		users.save(Content);
	
		// 查看是否添加成功
		DBCursor cur = users.find(Content);
		while (cur.hasNext()) {
			System.out.println(cur.next());
			//logger.info("成功添加一条数据！");
		}
	}
	
	
	
	
    public static ArrayList<Content> limitFind(){
    	ArrayList<Content> list = new ArrayList<Content>();
    	DBCursor cur = users.find(new BasicDBObject("resource","Boss直聘"));
    	DBCursor queryByPage=cur.limit(10);
        while (queryByPage.hasNext()) {  
        	Content content = new Content();
        	content.parse(cur.next());
        	list.add(content);
        }  
		return list;
    	
    }
	
	
	
	
	public static void main(String[] args) {
		
		ArrayList<Content> limitFind = limitFind();
		users = db.getCollection("recruit_data_wxj");
		for (Content content : limitFind) {
			AddContent(content);
		}
		
	}
}
