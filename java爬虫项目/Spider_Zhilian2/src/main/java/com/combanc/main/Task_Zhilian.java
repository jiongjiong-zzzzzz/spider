package com.combanc.main;

import java.util.List;

import com.combanc.pojo.Field_Rules;
import com.combanc.server.ContentByXpath;
import com.combanc.server.DownWebHtml;
import com.combanc.server.UrlByJson;
import com.combanc.util.ReplaceContent;

/**
 * @Title: 			Task_zhilian 
 * @Description: 	读取关键词拼接URL 解析列表页; 
 * @Company: 		combanc 
 * @Author: 		shihw
 * @Date: 			2018/9/4
 * @JDK: 			1.8 
 * @Encoding: 		UTF-8
 */
public class Task_Zhilian implements Runnable{
	//city  城市
	private String city = null;
	private List<String> job_categories = null;
	private Field_Rules field_Rules;
	//每页显示的数量
	private int pageJDNum = 60;
	//本次任务采集JD总数
	private int total_JDNum = 0;
	public Task_Zhilian() {
		// TODO Auto-generated constructor stub
	}
	
	public Task_Zhilian(String city ,List<String> job_categories,Field_Rules field_Rules) {
		super();
		this.city = city;
		this.field_Rules = field_Rules;
		this.job_categories = job_categories;
	}


	
	
	public void run() {
		String url;
		String html;
		String cityCode = city.split("\t")[1];
		
		
	
		try {
			for (String job : job_categories) {
				String[] keywords = job.split(" ")[0].split("\t");
				String[] codes = job.split(" ")[1].split("\t");
				String job_category = keywords[2];
			
			
				url = "https://fe-api.zhaopin.com/c/i/sou"
						+ "?pageSize="+pageJDNum
						+ "&cityId="+cityCode
//						+ "&workExperience=-1"
//						+ "&education=-1"
//						+ "&companyType=-1"
//						+ "&employm"
//						+ "&jobWelfareTag=-1"
						+ "&jobType="+codes[2]
//						+ "&kt=3"
						+ "&lastUrlQuery=%7B\"p\":"+1+","
						+ "\"pageSize\":\""+pageJDNum+"\","
						+ "\"jl\":\""+cityCode+"\","
						+ "\"jt\":\""+codes[0]+","+codes[1]+","+codes[2]+","+"\","
//						+ "\"kt\":\"3\""
						+ "%7D";
				System.out.println(url);
				html = DownWebHtml.DownHtml(url);
				int JDNum = UrlByJson.ParsePageNumByJson(html);
				System.out.println(JDNum);
				int pageNum = getPageNum(JDNum, pageJDNum);
				int start = 0;
				for (int i = 1; i <= pageNum; i++) {
					
					url = "https://fe-api.zhaopin.com/c/i/sou"
							+ "?start="+start
							+ "&pageSize="+pageJDNum
							+ "&cityId="+cityCode
//							+ "&workExperience=-1"
//							+ "&education=-1"
//							+ "&companyType=-1"
//							+ "&employm"
//							+ "&jobWelfareTag=-1"
							+ "&jobType="+codes[2]
//							+ "&kt=3"
							+ "&lastUrlQuery=%7B\"p\":"+i+","
							+ "\"pageSize\":\""+pageJDNum+"\","
							+ "\"jl\":\""+cityCode+"\","
							+ "\"jt\":\""+codes[0]+","+codes[1]+","+codes[2]+","+"\","
//							+ "\"kt\":\"3\""
							+ "%7D";
					html = DownWebHtml.DownHtml(url);
					System.out.println(url);
					List<String> urlList = UrlByJson.ParseUrlByJson(html);
					for (String JDUrl : urlList) {
						
						boolean isUrl = ReplaceContent.replaceContentUrl(JDUrl, field_Rules.getResource_rule());
						if(isUrl){
								String body = DownWebHtml.DownHtml(JDUrl);
								boolean success = ContentByXpath.getContentByXpath(body, JDUrl, null,field_Rules);
								if(success){
									total_JDNum ++;
								}else{
									System.out.println("失败");
								}
						}
					}
					start += pageJDNum;
				}
				
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}	
		} finally {
		}
		
	}
	
	
	/**
	 * 获取 页数
	 * @param jdNum jd的数量
	 * @param num 	每页的jd个数
	 * @return  采集页数
	 */
	public int getPageNum(int JDNum, int num) {
		//向上取整
		int totalPageNum = (int) Math.ceil(Double.valueOf(JDNum) / Double.valueOf(num));
//		//总页数>=采集页数  
//		if(totalPageNum>=field_Rules.getPage_num_rule()){
//			//return  采集页数
//			return field_Rules.getPage_num_rule();
//		}
		return totalPageNum;
	}
	
	
	
	
}
