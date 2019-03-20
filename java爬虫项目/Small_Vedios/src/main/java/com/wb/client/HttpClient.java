package com.wb.client;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpClient {
	static CloseableHttpClient closeableHttpClient = null;
	static CloseableHttpResponse closeableHttpResponse = null;
	static HttpEntity httpEntity = null;

	public String DownHtml(String httpUrl, String ip) {
		String[] ips = ip.split(":");
		int port = Integer.parseInt(ips[1]);
		RequestConfig requestConfig = RequestConfig.custom().setProxy(new HttpHost(ips[0], port))
				.setSocketTimeout(15000).setConnectTimeout(15000).setConnectionRequestTimeout(15000).build();
		closeableHttpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(httpUrl);
		// HttpHost proxy = new HttpHost("39.137.69.10",80);
		httpGet.setHeader("User-Agent", "Mozilla/5.0(Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");
		httpGet.setConfig(requestConfig);
		String responseContent = "";
		try {
			closeableHttpResponse = closeableHttpClient.execute(httpGet);
			httpEntity = closeableHttpResponse.getEntity();
			responseContent = EntityUtils.toString(httpEntity, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseContent;
	}
}
