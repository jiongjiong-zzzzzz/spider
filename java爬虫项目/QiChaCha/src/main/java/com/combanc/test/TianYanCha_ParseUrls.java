package com.combanc.test;

import java.util.List;

import com.combanc.mysql.MysqlConn;
import com.combanc.pojo.IPMessage;
import com.combanc.proxy.GetProxy;
import com.combanc.redis.RedisHash;
import com.combanc.server.util.JsoupUtil;

/**
 * @Title:           TianYanCha_ParseUrls
 * @Description:     解析天眼查公司信息URL链接
 * @Company:         combanc
 * @Author:          shihw
 * @Date:            2018/10/24
 * @JDK:             1.8
 * @Encoding:        UTF-8
 */
public class TianYanCha_ParseUrls {
	
	
	public static void main(String[] args) {
		
		
		String sql = "select * from company_list";
		//查询公司名录
		List<String> company_list = MysqlConn.select_Company(sql);
		
		//从redis取一个Ip
		IPMessage ipMessage = GetProxy.getPorxy();
		
		for (String company : company_list) {
			//判断 该公司是否 采过
			boolean falx = RedisHash.operateHash("company_list", company);
			if (!falx) {
				String html = "";
				
				String url = "https://www.qichacha.com/search?key="+company;
				
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
							 UrlByXpath.ParseUrlByXpath(html);
							
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
	
}
