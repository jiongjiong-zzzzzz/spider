package com.combanc.server.util;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.Cookie;

import com.combanc.util.Charset;
import com.combanc.util.UserAgent;

/**
 * @Title:           DownWebHtml
 * @Description:     利用Jsoup|httpclient下载htmlpage;
 * @Company:         combanc
 * @Author:          shihw
 * @Date:            2018/6/7
 * @JDK:             1.8
 * @Encoding:        UTF-8
 */
public class DownWebHtml {
	private static Logger logger = Logger.getLogger(DownWebHtml.class);
	private static String userAgent = UserAgent.getUserAgent();
	private static RequestConfig requestConfig = RequestConfig.custom()  
            .setSocketTimeout(15000)  
            .setConnectTimeout(15000)  
            .setConnectionRequestTimeout(15000)  
            .build();  
	 static CloseableHttpClient httpClient = null;  
     static CloseableHttpResponse response = null;
     static HttpEntity entity = null; 
    /**
     *
     * @param url
     * @return 返回document类型page
     * 			   下载失败  返回 null
     */			
    public static Document DownDcoument(String url){
    	int count = 3;
		
		while (count>0){
			count--;
			 Document document = null;
	        try {
	            //设置头文件，结构同selenium相似
	        	document = Jsoup.connect(url).userAgent(userAgent)
	            		.maxBodySize(0)
	                    .timeout(50000)
	                    .get();
	            if(document != null && !document.equals("")&&!document.equals("null")){
					return document;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}//while循环结束括号
		logger.debug(url+";3次下载失败");
		return null;
    }
    
    
    /**
     *
     * @param url
     * @return 返回document类型page
     */
	public static String DownHtmlByCookies(String url,Set<Cookie> cookies){
    	String body = null;
    	try {
    		//设置头文件，结构同selenium相似
    		body = Jsoup.connect(url)
    				.header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    				.header("Accept-Encoding","gzip, deflate, br")
    				.header("Accept-Language","zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2")
    				.header("Cache-Control","max-age=0")
    				.header("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:59.0) Gecko/20100101 Firefox/59.0")
    				.cookies(cookie(cookies))
    				.timeout(50000)
                    .execute()
                    .body();
    	} catch (IOException e) {
    		logger.warn("下载超时！！",e);
    		return null;
    	}
    	System.out.println(body);
    	return body;
    }

    /**
     * 
     * @param url
     * @return 返回string 类型page
     * 		   下载失败  返回 ""
     */
    public static String DownHtml(String url){
    	int count = 3;
		while (count>0){
			count--;
			String html = null;
	        try {
	            //设置头文件，结构同selenium相似
	        	html = Jsoup.connect(url).userAgent(userAgent)
	            		.maxBodySize(0)
	                    .timeout(50000)
	                    .ignoreContentType(true)
	                    .get()
	                    .html();
	            if(html != null && !html.equals("")&&!html.equals("null")){
					return html;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}//while循环结束括号
		logger.debug(url+";3次下载失败");
		return "";
    }
    /**
     * 
     * @param url
     * @return 返回string 类型page
     */
    public String DownBodyByProxy(String url){
        String body = null;
        try {
            //设置头文件，结构同selenium相似
            body = Jsoup.connect(url)
                    .header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .header("Accept-Encoding","gzip, deflate, br")
                    .header("Accept-Language","zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2")
                    .header("Cache-Control","max-age=0")
                    .header("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:59.0) Gecko/20100101 Firefox/59.0")
                    .proxy("103.250.249.148", 1080)
                    .timeout(50000)
                    .ignoreContentType(true)
                    .execute()
                    .body();
        } catch (IOException e) {
        	logger.warn("下载超时！！",e);
        	return null;
        }
        return body;
    }
    
    /**
     * 修改User-Agent为安卓访问
     * @param url
     * @return 返回string 类型page
     */
    public String DownBodyByAndroid(String url){
    	String body = null;
        try {
            //设置头文件，结构同selenium相似
        	body = Jsoup.connect(url)
                    .header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .header("Accept-Encoding","gzip, deflate, br")
                    .header("Accept-Language","zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2")
                    .header("Cache-Control","max-age=0")
                    .header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.62 Safari/537.36")
                    .timeout(50000)
                    .ignoreContentType(true)
                    .execute()
                    .body();
        } catch (IOException e) {
        	logger.warn("下载超时！！",e);
        	return null;
        }
        return body;
    }
    
    public static  String DownHtmlByClient(String httpUrl){
    	
    	 System.out.println("编码为："+Charset.getFileEncoding(httpUrl));
    	 httpClient = HttpClients.createDefault(); 
		 HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求 	
		 httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");
		 httpGet.setConfig(requestConfig);  
           // 执行请求  
		 String  responseContent="";
		 try{
	         response = httpClient.execute(httpGet);  
	         entity = response.getEntity();  
	         responseContent = EntityUtils.toString(entity, Charset.getFileEncoding(httpUrl)); 
		 }catch(Exception e){
			 e.getStackTrace();
			 logger.warn("下载超时！！",e);
	         return null;
		 }finally {
			 closeClient(httpClient, response);
		}
   	 return  responseContent;
   	 
    }

	public static String DownHtmlByClientCookies(String httpUrl, String cookie) {
//		CookieStore cookieStore = new BasicCookieStore();
//		Map<String, String> map = cookie(cookies);
//		for (Map.Entry<String, String> entry : map.entrySet()) {
//			BasicClientCookie cookie = new BasicClientCookie(entry.getKey(), entry.getValue());
//			org.apache.commons.httpclient.Cookie cookie2 = new 
//			cookieStore.addCookie(cookie);
//		}
//		httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore)// 设置Cookie
//				.build();

		HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
		httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");
		httpGet.addHeader(new BasicHeader("Cookie",cookie));//就可以免登录访问。  
		httpGet.setConfig(requestConfig);
		// 执行请求
		String responseContent = "";
		try {
			response = httpClient.execute(httpGet);
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, Charset.getFileEncoding(httpUrl));
		} catch (Exception e) {
			e.getStackTrace();
			logger.warn("下载超时！！",e);
			return null;
		} finally {
			closeClient(httpClient, response);
		}
		return responseContent;

	}
    
	public static void closeClient(CloseableHttpClient httpClient, CloseableHttpResponse response) {
		// 关闭连接,释放资源
		try {
			// 关闭连接,释放资源
			if (response != null) {
				response.close();
			}
			if (httpClient != null) {
				httpClient.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
     * 设置cookie，需要更改，要不然此cookie容易被封
     *
     * @return
     */
    public static Map<String, String> cookie(Set<Cookie> cookies) {
        Map<String, String> map = new HashMap<String, String>();
        
        for (Cookie cookie : cookies) {
            String[] s = cookie.toString().split(";");
            for (String string : s) {
                String[] ss = string.split("=");
                if (ss.length > 1) {
                    //System.out.println(ss[0].trim()+":"+ss[1].trim());
                    map.put(ss[0].trim(), ss[1].trim());
                } else {
                    //System.out.println(ss[0].trim()+":"+"");
                    map.put(ss[0].trim(), "");
                }
            }
		}
        
		return map;
    }
    /**
     * 设置cookie，需要更改，要不然此cookie容易被封
     *
     * @return
     */
	public static Map<String, String> cookie(String cookies) {
		Map<String, String> map = new HashMap<String, String>();

		String[] s = cookies.toString().split(";");
		for (String string : s) {
			String[] ss = string.split("=");
			if (ss.length > 1) {
				// System.out.println(ss[0].trim()+":"+ss[1].trim());
				map.put(ss[0].trim(), ss[1].trim());
			} else {
				// System.out.println(ss[0].trim()+":"+"");
				map.put(ss[0].trim(), "");
			}
		}

		return map;
	}
	
    
	
	
	
	
//	public static void main(String[] args) {
//		
////		Map<String, String> cookie = cookie("LGRID=20180621143421-21e522db-751d-11e8-9738-5254005c3644; path=/; domain=.lagou.com, LGSID=20180621143417-1f8c3820-751d-11e8-abae-525400f775ce; expires=星期四, 21 六月 2018 03:04:21 CST; path=/; domain=.lagou.com, LGUID=20180621143417-1f8c3b2a-751d-11e8-abae-525400f775ce; expires=星期日, 18 六月 2028 02:34:17 CST; path=/; domain=.lagou.com, showExpriedIndex=1; expires=星期四, 28 六月 2018 02:34:18 CST; path=/; domain=www.lagou.com, showExpriedMyPublish=1; expires=星期四, 28 六月 2018 02:34:18 CST; path=/; domain=www.lagou.com, SEARCH_ID=1c8ce9687b0246eabbbdcfc5a23c1b5c; expires=星期五, 22 六月 2018 02:34:21 CST; path=/; domain=www.lagou.com, PRE_UTM=; expires=星期四, 21 六月 2018 03:04:17 CST; path=/; domain=.lagou.com, JSESSIONID=ABAAABAAADEAAFIFA85E3C3E0DF3FFCDAEDEACE42F48907; path=/; domain=www.lagou.com, index_location_city=%E5%8C%97%E4%BA%AC; expires=星期六, 21 七月 2018 02:34:21 CST; path=/; domain=.lagou.com, login=true; path=/; domain=.lagou.com, showExpriedCompanyHome=1; expires=星期四, 28 六月 2018 02:34:18 CST; path=/; domain=www.lagou.com, PRE_HOST=; expires=星期四, 21 六月 2018 03:04:17 CST; path=/; domain=.lagou.com, PRE_LAND=https%3A%2F%2Fpassport.lagou.com%2Flogin%2Flogin.html; expires=星期四, 21 六月 2018 03:04:17 CST; path=/; domain=.lagou.com, hasDeliver=0; expires=星期四, 28 六月 2018 02:34:18 CST; path=/; domain=www.lagou.com, gate_login_token=97db53f153b4ff0b16ed66fbdad077fe77862a0d1a840cdb2c575abd7ca541f7; expires=星期四, 28 六月 2018 02:34:18 CST; path=/; domain=.lagou.com, X_HTTP_TOKEN=25bc9cc47ba130215674499bf7c1489f; path=/; domain=.lagou.com, LG_LOGIN_USER_ID=78873426ca2127d11b3efce9fd150e9ca5a95e476316e12199c6e5b778b211c4; expires=星期二, 09 七月 2086 05:48:25 CST; path=/; domain=.lagou.com, user_trace_token=20180621143417-b976679b-38ab-4d6a-84bf-6674cbaca27a; expires=星期五, 21 六月 2019 02:34:17 CST; path=/; domain=.lagou.com, _gid=GA1.2.918263319.1529562858; expires=星期五, 22 六月 2018 02:34:21 CST; path=/; domain=.lagou.com, Hm_lpvt_4233e74dff0ae5bd0a3d81c6ccf756e6=1529562862; path=/; domain=.lagou.com, Hm_lvt_4233e74dff0ae5bd0a3d81c6ccf756e6=1529562858; expires=星期五, 21 六月 2019 02:34:21 CST; path=/; domain=.lagou.com, _gat=1; expires=星期四, 21 六月 2018 02:35:19 CST; path=/; domain=.lagou.com, _ga=GA1.2.570303787.1529562858; expires=星期六, 20 六月 2020 02:34:21 CST; path=/; domain=.lagou.com, _putrc=7695ABBFC4E3552E123F89F2B170EADC; path=/; domain=.lagou.com, unick=%E7%9F%B3%E6%B4%AA%E7%8E%AE; path=/; domain=.lagou.com, PRE_SITE=; expires=星期四, 21 六月 2018 03:04:17 CST; path=/; domain=.lagou.com");
//		
//		String cookie = "LGRID=20180621143421-21e522db-751d-11e8-9738-5254005c3644; path=/; domain=.lagou.com, LGSID=20180621143417-1f8c3820-751d-11e8-abae-525400f775ce; expires=星期四, 21 六月 2018 03:04:21 CST; path=/; domain=.lagou.com, LGUID=20180621143417-1f8c3b2a-751d-11e8-abae-525400f775ce; expires=星期日, 18 六月 2028 02:34:17 CST; path=/; domain=.lagou.com, showExpriedIndex=1; expires=星期四, 28 六月 2018 02:34:18 CST; path=/; domain=www.lagou.com, showExpriedMyPublish=1; expires=星期四, 28 六月 2018 02:34:18 CST; path=/; domain=www.lagou.com, SEARCH_ID=1c8ce9687b0246eabbbdcfc5a23c1b5c; expires=星期五, 22 六月 2018 02:34:21 CST; path=/; domain=www.lagou.com, PRE_UTM=; expires=星期四, 21 六月 2018 03:04:17 CST; path=/; domain=.lagou.com, JSESSIONID=ABAAABAAADEAAFIFA85E3C3E0DF3FFCDAEDEACE42F48907; path=/; domain=www.lagou.com, index_location_city=%E5%8C%97%E4%BA%AC; expires=星期六, 21 七月 2018 02:34:21 CST; path=/; domain=.lagou.com, login=true; path=/; domain=.lagou.com, showExpriedCompanyHome=1; expires=星期四, 28 六月 2018 02:34:18 CST; path=/; domain=www.lagou.com, PRE_HOST=; expires=星期四, 21 六月 2018 03:04:17 CST; path=/; domain=.lagou.com, PRE_LAND=https%3A%2F%2Fpassport.lagou.com%2Flogin%2Flogin.html; expires=星期四, 21 六月 2018 03:04:17 CST; path=/; domain=.lagou.com, hasDeliver=0; expires=星期四, 28 六月 2018 02:34:18 CST; path=/; domain=www.lagou.com, gate_login_token=97db53f153b4ff0b16ed66fbdad077fe77862a0d1a840cdb2c575abd7ca541f7; expires=星期四, 28 六月 2018 02:34:18 CST; path=/; domain=.lagou.com, X_HTTP_TOKEN=25bc9cc47ba130215674499bf7c1489f; path=/; domain=.lagou.com, LG_LOGIN_USER_ID=78873426ca2127d11b3efce9fd150e9ca5a95e476316e12199c6e5b778b211c4; expires=星期二, 09 七月 2086 05:48:25 CST; path=/; domain=.lagou.com, user_trace_token=20180621143417-b976679b-38ab-4d6a-84bf-6674cbaca27a; expires=星期五, 21 六月 2019 02:34:17 CST; path=/; domain=.lagou.com, _gid=GA1.2.918263319.1529562858; expires=星期五, 22 六月 2018 02:34:21 CST; path=/; domain=.lagou.com, Hm_lpvt_4233e74dff0ae5bd0a3d81c6ccf756e6=1529562862; path=/; domain=.lagou.com, Hm_lvt_4233e74dff0ae5bd0a3d81c6ccf756e6=1529562858; expires=星期五, 21 六月 2019 02:34:21 CST; path=/; domain=.lagou.com, _gat=1; expires=星期四, 21 六月 2018 02:35:19 CST; path=/; domain=.lagou.com, _ga=GA1.2.570303787.1529562858; expires=星期六, 20 六月 2020 02:34:21 CST; path=/; domain=.lagou.com, _putrc=7695ABBFC4E3552E123F89F2B170EADC; path=/; domain=.lagou.com, unick=%E7%9F%B3%E6%B4%AA%E7%8E%AE; path=/; domain=.lagou.com, PRE_SITE=; expires=星期四, 21 六月 2018 03:04:17 CST; path=/; domain=.lagou.com";
//		
//		String downHtmlByClient = DownHtmlByClientCookies("https://www.lagou.com/jobs/4610833.html", cookie);
//		System.out.println(downHtmlByClient);
//		
//		
//	}
	
	
	
	
	
	
	
	
	
}
