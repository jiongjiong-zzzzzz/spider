package com.combanc.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.combanc.mysql.MysqlConn;
import com.combanc.pojo.Field_Rules;
/**
 * Title:           Start
 * Description:     开始执行
 * Company:         combanc
 * Author:          shihw
 * Date:            2018/8/1
 * JDK:             1.8
 * Encoding:        UTF-8
 */
public class Start_Zhilian {
	private static Logger logger = Logger.getLogger(Start_Zhilian.class);
	public static void main(String[] args) {
		// 任务执行
		logger.info("**************" + Thread.currentThread().getName() + "**************");
		long start = System.currentTimeMillis();
		logger.info("开始爬虫.........................................");
		ExecutorService pool = null;
		// 创建一个线程池
		pool = Executors.newCachedThreadPool();
		pool = Executors.newFixedThreadPool(1);
		
//		String sql_cities = "select keywords from spider_keywords where name = 'cities' and resource = '智联招聘'";
//		List<String> cities = MysqlConn.select_keywords(sql_cities);
//		
		String sql_rules = "select * from spider_rules where resource_rule = '智联招聘'";
		Field_Rules rules = MysqlConn.select_rules(sql_rules);
		
		String sql_job = "select keywords from spider_keywords where name = 'job_categories' and resource = '智联招聘'";
		List<String> job_categories = MysqlConn.select_keywords(sql_job);
		
		//采集智联招聘
		pool.execute(new Task_Zhilian("北京	530",job_categories,rules));
			
		
		// 释放资源
		pool.shutdown();

		while (true) {
			if (pool.isTerminated()) {
				long end = System.currentTimeMillis();
				logger.info("爬虫结束.........................................");
				logger.info("总共耗时" + (end - start) / 1000 + "秒");
				break;
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	/**
	 * 加载log4j日志配置文件
	 */
	private static void initLogRecord() {

		Properties props = null;
		InputStream fis = null;
		try {
			props = new Properties();
			fis = new Start_Zhilian().getClass().getResourceAsStream("/conf" + File.separator + "log4j.properties");
			props.load(fis);
			PropertyConfigurator.configure(props);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	
	
	
	
	
	
	
	
	
}
