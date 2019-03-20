package com.combanc.server.util;

import java.io.IOException;

import org.jsoup.Jsoup;

import com.combanc.pojo.IPMessage;
import com.combanc.redis.RedisIpList;
import com.combanc.util.UserAgent;

public class JsoupUtil {
	
	/**
	 * Jsoup 设置代理
	 * @param  ipMessage  ip:port
	 * @return String/null
	 */
	public static String downByJsoupProxy(String url,IPMessage ipMessage) {
		
		
		String html = null;
		try {
			html = Jsoup.connect(url)
					.userAgent(UserAgent.getUserAgent())
					.proxy(ipMessage.getIPAddress(), Integer.valueOf(ipMessage.getIPPort()))
					.maxBodySize(0)
					.validateTLSCertificates(false)
					.ignoreContentType(true)
					.timeout(15000).get().html();

            if(html != null && !html.equals("")&&!html.equals("null")){
            	ipMessage.setUseCount();
				return html;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("下载超时!");
			
			RedisIpList.setIPToList(ipMessage);
			return null;
			
		}
		return null;

	}
	
	
	/**
	 * Jsoup 设置代理
	 * @param  ipMessage  ip:port
	 * @return String/null
	 */
	public static String downBodyByJsoupProxy(String url,IPMessage ipMessage) {
		
		
		String html = null;
		try {
			html = Jsoup.connect(url)
					.userAgent(UserAgent.getUserAgent())
					.proxy(ipMessage.getIPAddress(), Integer.valueOf(ipMessage.getIPPort()))
					.maxBodySize(0)
					.validateTLSCertificates(false)
					.ignoreContentType(true)
					.timeout(15000)
					.execute().body();

            if(html != null && !html.equals("")&&!html.equals("null")){
            	ipMessage.setUseCount();
				return html;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("网页下载失败！！！");
			RedisIpList.setIPToList(ipMessage);
			return null;
			
		}
		return null;

	}
	
	
	
}
