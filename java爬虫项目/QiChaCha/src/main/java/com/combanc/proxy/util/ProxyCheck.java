package com.combanc.proxy.util;

import org.jsoup.Jsoup;

import com.combanc.util.UserAgent;
/**
 * @Title:           ProxyCheck
 * @Description:     检查代理ip是否可用
 * @Company:         combanc
 * @Author:          shihw
 * @Date:            2018/9/19
 * @JDK:             1.8
 * @Encoding:        UTF-8
 */
public class ProxyCheck {
	
	
	public static void main(String[] args) {
		check("50.226.134.48", "80");
		checkProxy("50.226.134.48", "80");
	}

	/**
	 * 检查Proxy是否可以使用
	 *
	 * @return
	 */
	public static boolean check(String host, String port) {
		try {
			Jsoup.connect("http://www.baidu.com").timeout(3 * 1000).proxy(host, Integer.valueOf(port)).get();
			System.out.println(host + ":" + port + "是一个有效ip...");
			return true;
		} catch (Exception e) {
			System.out.println(host + ":" + port + "是一个无效ip");
			return false;
		}

	}

	public static boolean checkProxy(String host, String port) {

		String html = null;
		try {
			html = Jsoup.connect("https://www.baidu.com/").userAgent(UserAgent.getUserAgent())
					.proxy(host, Integer.valueOf(port)).maxBodySize(0).validateTLSCertificates(false)
					.ignoreContentType(true).timeout(5000).get().html();

			 if(html!=null){
				System.out.println(host + ":" + port + "是一个有效ip...");
				return true;
			 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(host + ":" + port + "是一个无效ip");
			return false;
		}
		return false;

	}

}
