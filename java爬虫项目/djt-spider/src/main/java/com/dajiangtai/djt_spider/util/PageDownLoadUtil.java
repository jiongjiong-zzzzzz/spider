package com.dajiangtai.djt_spider.util;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.dajiangtai.djt_spider.entity.Page;
import com.dajiangtai.djt_spider.service.impl.HttpClientDownLoadService;

/**
 * 页面下载工具类
 * @author dajiangtai
 * created by 2016-10-28
 *
 */
public class PageDownLoadUtil {

	public static String getPageContent(String url){
		HttpClientBuilder builder = HttpClients.custom();
		CloseableHttpClient client = builder.build();
		
		HttpGet request = new HttpGet(url);
		String content = null;
		try {
			CloseableHttpResponse response = client.execute(request);
			HttpEntity  entity = response.getEntity();
			content = EntityUtils.toString(entity);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;	
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String url = "http://www.youku.com/show_page/id_z9cd2277647d311e5b692.html?spm=a2htv.20005143.m13050845531.5~5~1~3~A&from=y1.3-tv-index-2640-5143.40177.1-1";
		HttpClientDownLoadService down = new HttpClientDownLoadService();
		Page page = down.download(url);
		System.out.println(page.getContent());
	}

}
