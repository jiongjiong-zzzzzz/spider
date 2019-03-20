package com.combanc.server.util;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.combanc.pojo.IPMessage;
import com.combanc.redis.RedisIpList;
import com.combanc.util.UserAgent;
/**
 * Title:           HttpClientUtil
 * Description:     设置Proxy下载Html
 * Company:         combanc
 * Author:          shihw
 * Date:            2018/6/7
 * JDK:             1.8
 * Encoding:        UTF-8
 */
public class HttpClientUtil {
	 static CloseableHttpClient httpClient = null;  
     static CloseableHttpResponse response = null;
     static HttpEntity entity = null; 
	
	
	
	//使用代理进行网站爬取
    public static String getHtml(String url,IPMessage ipMessage) {
        int httpStatus;

        // 设置代理访问和超时处理
        HttpHost proxy = new HttpHost(ipMessage.getIPAddress(), Integer.parseInt(ipMessage.getIPPort()));
        RequestConfig config = RequestConfig.custom()
        		.setProxy(proxy)
        		.setConnectTimeout(15000)
        		.setSocketTimeout(15000).build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(config);

        httpGet.setHeader("User-Agent", UserAgent.getUserAgent());
        httpClient = HttpClients.createDefault(); 
        String responseContent = "";
        try {
            // 客户端执行httpGet方法，返回响应
        	response = httpClient.execute(httpGet);

            // 得到服务响应状态码
            httpStatus = response.getStatusLine().getStatusCode();
            if (httpStatus == 200) {
                entity = response.getEntity();
                responseContent = EntityUtils.toString(entity, "utf-8");
            } else {
            	responseContent = null;
            	
            }
        } catch (IOException e) {
            RedisIpList.setIPToList(ipMessage);
            System.out.println("网页下载失败！！！");
            responseContent = null;
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
	

}
