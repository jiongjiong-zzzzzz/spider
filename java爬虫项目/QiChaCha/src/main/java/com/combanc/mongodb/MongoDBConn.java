package com.combanc.mongodb;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;

import com.combanc.pojo.Company_Info;
import com.combanc.pojo.Company_Url;
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
 * Date:            2018/10/24
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
	private static DBCollection users2;

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
		users2 = db.getCollection(properties.getProperty("mongodb.collection2"));

	}

	/**
	 * 获取数据总量
	 * @return  数量
	 */
	public int findCount() {
		int count = users.find().count();

		return count;
	}

	/**从mongo里读取 state 不为1的url
	 * 将数据放到Company_Url实体里
	 * @return Company_Url集合
	 */
	public static ArrayList<Company_Url> find() {
		DBObject Content = new BasicDBObject();
		String ageStr = "function (){return this.state != 1};";
		
		Content.put("$where",ageStr);
		DBCursor cur = users.find(Content);
		ArrayList<Company_Url> list = new ArrayList<Company_Url>();
		while (cur.hasNext()) {
			Company_Url content = new Company_Url();
			content.parse(cur.next());
			list.add(content);
		}
		return list;

	}
	
	

	/**
	 * 将数据放到 Company_Url 实体里 
	 * @param start 开始条数
	 * @param end	结束条数
	 * @return Company_Url集合
	 */
	public ArrayList<Company_Url> limitFind(int start, int end) {
		ArrayList<Company_Url> list = new ArrayList<Company_Url>();
		DBCursor cur = users.find();
		DBCursor queryByPage = cur.skip(start).limit(end);
		while (queryByPage.hasNext()) {
			Company_Url Content = new Company_Url();
			Content.parse(cur.next());
			list.add(Content);
		}
		return list;

	}

	
	
	
	/**
	 * 解析成功将state字段 改为1
	 * @param url
	 */
    public  void updatesucess(String url){         
        users.update(  
             new BasicDBObject("url",url),           
             new BasicDBObject("$set" ,new BasicDBObject("state",1)),  
             false,//如果users中不存在state字段，是否更新，false表示不更新  
             false//只修改第一条，true表示修改多条  
        );        
   }  
   
	/**
	 * 解析失败将state字段 改为2
	 * @param url
	 */
   public  void updatede(String url){         
       users.update(  
            new BasicDBObject("url",url),           
            new BasicDBObject("$set" ,new BasicDBObject("state",2)),  
            false,//如果users中不存在state字段，是否更新，false表示不更新  
            false//只修改第一条，true表示修改多条  
       );        
  }
	
	
	
	
	/**
	 * 向company_url里添加一条数据
	 * @param content Content实体
	 */
	public static int AddUrl(Company_Url content) {
		DBObject Content = new BasicDBObject();
		Content.put("id", content.getId());
		Content.put("company_name", content.getCompany_name());
		Content.put("company_url", content.getCompany_url());
		Content.put("state", 0);
		users.save(Content);
	
		// 查看是否添加成功
		DBCursor cur = users.find(Content);
		while (cur.hasNext()) {
			System.out.println(cur.next());
			//logger.info("成功添加一条数据！");
			return 1;
		}
		return 0;
	}
	
	
	
	
	/**
	 * 向company_info里添加一条数据
	 * @param content Content实体
	 */
	public static int AddContent(Company_Info content) {
		DBObject Content = new BasicDBObject();
		Content.put("id", content.getId());
		Content.put("company_name", content.getConpany_name());
		Content.put("register_capital",content.getRegister_capital());
		Content.put("contributed_capital", content.getContributed_capital());
		Content.put("operating_state", content.getOperating_state());
		Content.put("establish_data", content.getEstablish_data());
		Content.put("credit_code", content.getCredit_Code());
		Content.put("taxpayer_code", content.getTaxpayer_code());
		Content.put("register_code", content.getRegister_code());
		Content.put("organization_code", content.getOrganization_code());
		Content.put("company_type", content.getCompany_type());
		Content.put("company_industry", content.getCompany_industry());
		Content.put("approval_date", content.getApproval_date());
		Content.put("register_office",content.getRegister_office());
		Content.put("area", content.getArea());
		Content.put("english_name", content.getEnglish_name());
		Content.put("before_name", content.getBefore_name());
		Content.put("insured_num", content.getInsured_num());
		Content.put("company_size", content.getCompany_size());
		Content.put("business_term", content.getBusiness_term());
		Content.put("company_address",content.getCompany_address());
		Content.put("business_scope", content.getBusiness_scope());
		Content.put("state", 0);
		users2.save(Content);
	
		// 查看是否添加成功
		DBCursor cur = users2.find(Content);
		while (cur.hasNext()) {
			System.out.println(cur.next());
			//logger.info("成功添加一条数据！");
			return 1;
		}
		return 0;
	}
}
