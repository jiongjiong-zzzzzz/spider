package com.combanc.test;

import java.util.ArrayList;

import com.combanc.mongodb.MongoDBConn;
import com.combanc.pojo.Company_Url;
import com.combanc.pojo.IPMessage;
import com.combanc.proxy.GetProxy;
import com.combanc.server.util.JsoupUtil;
/**
 * @Title:           TianYanCha_ParseContent
 * @Description:     解析天眼查公司基本信息
 * @Company:         combanc
 * @Author:          shihw
 * @Date:            2018/10/25
 * @JDK:             1.8
 * @Encoding:        UTF-8
 */
public class TianYanCha_ParseContent {

	public static void main(String[] args) {
		
		//从mongo读取 公司信息url
		ArrayList<Company_Url> company_Urls = MongoDBConn.find();
		//取一个IP
		IPMessage ipMessage = GetProxy.getPorxy();
		for (Company_Url company_Url : company_Urls) {
			
			String id = company_Url.getId();
			String company_name = company_Url.getCompany_name();
			//拼接 基本信息 页面
			String url = company_Url.getCompany_url()+"#base";
			String html;
			
			while(true){
				//下载网页
				html = JsoupUtil.downByJsoupProxy(url,ipMessage);
				
				System.out.println(ipMessage.toString()+"使用次数为："+ipMessage.getUseCount());
				if(html != null){
					if(html.length() < 1000){
						System.out.println("网页信息不全！");
						
						ipMessage = GetProxy.getPorxy();
					}else if(html.contains("会员登录 - 企查查")){
						System.out.println("需要登录！！");
						ipMessage = GetProxy.getPorxy();
					}else{
						//不为空解析json 获取页数
						ContentByXpath.getContentByXpath(html, id,url,company_name);	
						break;
					}
				}else{
					//重新取一个ip 
					ipMessage = GetProxy.getPorxy();
				}
			}
		}
		
	}
}		
