package com.wb.client;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

public class HttpHelper {

	// 客户端。CloseableHttpClient可以自己维护一个请求池，自行调配资源
	private static CloseableHttpClient httpClient = null;

	static {
		// 获取一个socket的连接工厂，可以设置一些参数。
		ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
		LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", plainsf).register("https", sslsf).build();

		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);

		cm.setMaxTotal(1000); // 这个是最大连接数。如果并发量大于这个数，就会发生排队。
		cm.setDefaultMaxPerRoute(1000); // 设置每个路由的最大值。 默认只有一个路由。可以忽视这个设置。
		// 此处解释下MaxtTotal和DefaultMaxPerRoute的区别：
		// 1、MaxtTotal是整个池子的大小；
		// 2、DefaultMaxPerRoute是根据连接到的主机对MaxTotal的一个细分；比如：
		// MaxtTotal=400 DefaultMaxPerRoute=200
		// 而我只连接到http://sishuok.com时，到这个主机的并发最多只有200；而不是400；
		// 而我连接到http://sishuok.com 和
		// http://qq.com时，到每个主机的并发最多只有200；即加起来是400（但不能超过400）；所以起作用的设置是DefaultMaxPerRoute。

		// 根据上面的一堆配置，生成一个client对象。 这个对象是整个应用程序公用的。是唯一的对象。
		httpClient = HttpClients.custom().setConnectionManager(cm).build();
	}

	/**
	 * post请求
	 * 
	 * @param url
	 *            请求地址
	 * @param requestBody
	 *            请求体
	 * @return 返回内容
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String post(String url, String requestBody, Map<String, String> header)
			throws ClientProtocolException, IOException {
		// 创建一个post请求对象
		HttpPost postRequest = new HttpPost(url);

		for (Entry<String, String> entry : header.entrySet()) {
			postRequest.addHeader(entry.getKey(), entry.getValue());
		}

		StringEntity input = new StringEntity(requestBody, Charset.forName("utf-8"));
		input.setContentType("application/json");
		postRequest.setEntity(input);
		CloseableHttpResponse response = httpClient.execute(postRequest);
		String result = "";
		if (response != null && response.getStatusLine().getStatusCode() == 200) {

			result = EntityUtils.toString(response.getEntity());
			// String str = null;
			// HttpEntity entity = response.getEntity();
			// if (entity != null) {
			// InputStream instreams = entity.getContent();
			// str = inputStream2String(instreams);
			// }
			System.out.println(response.getStatusLine());
			System.out.println("str" + result);
		}
		response.close();
		return result;
	}

	/**
	 * get请求
	 * 
	 * @param url
	 *            请求地址
	 * @return 返回内容
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String get(String url, Map<String, String> header) throws ClientProtocolException, IOException {
		// 创建一个get请求对象
		HttpGet getRequest = new HttpGet(url);

		for (Entry<String, String> entry : header.entrySet()) {
			getRequest.addHeader(entry.getKey(), entry.getValue());
		}

		HttpResponse response = httpClient.execute(getRequest);

		String str = null;

		HttpEntity entity = response.getEntity();
		if (entity != null) {
			InputStream instreams = entity.getContent();
			str = inputStream2String(instreams);
		}
		return str;
	}

	/**
	 * 从网络输入流中获取数据
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static String inputStream2String(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		in.close();
		return out.toString();
	}

	public static String getResult(String itemUrl) throws HttpException, IOException {

		HttpClient httpClient = new HttpClient(); // 创建httpClient实例
		GetMethod get = new GetMethod(itemUrl); // 创建httpget实例
		get.setRequestHeader("Accept",
				"textml,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");

		get.setRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36");
		get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		get.setRequestHeader("Connection", "keep-alive");
		get.setRequestHeader("Cache-Control", "max-age=0");
		get.setRequestHeader("Upgrade-Insecure-Requests", "1");
		get.getParams().setContentCharset("UTF-8");
		get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		int status = httpClient.executeMethod(get);
		if (status != HttpStatus.SC_OK) {
			System.out.println("Crawl failed, error: " + get.getStatusLine());
		}
		String response = get.getResponseBodyAsString();
		int charsetPos = get.getResponseBodyAsString().indexOf("charset");
		byte[] dataResponseBody = get.getResponseBody();
		String result = get.getResponseBodyAsString();

		if (charsetPos != -1) {
			String charset = get.getResponseBodyAsString().substring(charsetPos, charsetPos + 30);
			String rType = getCharSet(charset);
			if (rType == "GBK") {
				result = new String(dataResponseBody, Charset.forName("gb2312"));
			}
		}
		return result;

	}

	public static String getips() {
		String[] ip = { "125.86.167.221:4697" };
		String random = "";
		int index = (int) (Math.random() * ip.length);
		random = ip[index];
		return random;
	}

	public static String getInvestor(String url) {
		// 创建httpClient实例
		CloseableHttpClient httpClient = HttpClients.createDefault();
		// 创建httpGet实例
		HttpGet httpGet = new HttpGet(url);
		// 设置代理IP，设置连接超时时间 、 设置 请求读取数据的超时时间 、 设置从connect Manager获取Connection超时时间、
		// 设置代理IP、端口、协议（请分别替换）
		String[] r = getips().split(":");
		HttpHost proxy = new HttpHost(r[0], Integer.parseInt(r[1]));
		RequestConfig requestConfig = RequestConfig.custom().setProxy(proxy).setConnectTimeout(10000)
				.setSocketTimeout(10000).setConnectionRequestTimeout(3000).build();
		httpGet.setConfig(requestConfig);
		httpGet.setHeader("Accept",
				"textml,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");

		httpGet.setHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36");
		httpGet.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		httpGet.setHeader("Connection", "keep-alive");
		httpGet.setHeader("Cache-Control", "max-age=0");
		httpGet.setHeader("Upgrade-Insecure-Requests", "1");
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
		} catch (ClientProtocolException e) {
			System.out.println("客户端连接池出错" + e);
		} catch (IOException e) {
			System.out.println("IO出错" + e);
		}
		String content = "";
		if (response != null) {
			HttpEntity entity = response.getEntity(); // 获取返回实体
			if (entity != null) {
				try {
					content = EntityUtils.toString(entity, "utf-8");
					// System.out.println("网页内容为:" + content);
					// saveHtml(content,"D://html.html");
				} catch (ParseException e) {
					System.out.println("解析出错" + e);
				} catch (IOException e) {
					System.out.println("IO出错" + e);
				}
			}
		}
		if (response != null) {
			try {
				response.close();
			} catch (IOException e) {
				System.out.println("IO出错" + e);
			}
		}
		return content;
	}

	public static String getResultgsxx(String itemUrl) throws HttpException, IOException {
		HttpClient httpClient = new HttpClient(); // 创建httpClient实例
		GetMethod get = new GetMethod(itemUrl); // 创建httpget实例
		get.setRequestHeader("Accept", "application/json");
		get.setRequestHeader("User-Agent",
				"Mozilla/5.0 (Linux; Android 8.0.0; EDI-AL10 Build/HUAWEIEDISON-AL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/68.0.3440.91 Mobile Safari/537.36 Html5Plus/1.0");
		get.setRequestHeader("Cookie",
				"JSESSIONID=74E8623D615C02DAF59953FA0A34B101; SECTOKEN=7067265633630292608; __jsluid=12aed9d3ac79e9597d22b9ce51da1816; tlb_cookie=172.16.12.1108080");
		get.setRequestHeader("Host", "app.gsxt.gov.cn");
		get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		get.setRequestHeader("Connection", "keep-alive");
		get.setRequestHeader("X-Requested-With", "XMLHttpRequest");
		get.getParams().setContentCharset("UTF-8");
		get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		int status = httpClient.executeMethod(get);
		if (status != HttpStatus.SC_OK) {
			System.out.println("Crawl failed, error: " + get.getStatusLine());
		}
		String response = get.getResponseBodyAsString();
		int charsetPos = get.getResponseBodyAsString().indexOf("charset");
		byte[] dataResponseBody = get.getResponseBody();
		String result = get.getResponseBodyAsString();

		if (charsetPos != -1) {
			String charset = get.getResponseBodyAsString().substring(charsetPos, charsetPos + 30);
			String rType = getCharSet(charset);
			if (rType == "GBK") {
				result = new String(dataResponseBody, Charset.forName("gb2312"));
			}
		}
		return result;

	}

	private static String getCharSet(String content) {
		String regex = ".*charset=\"?(utf-)|(UTF-)8";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		if (matcher.find())
			return "UTF-8";
		else {
			regex = ".*charset=\"?(gb2312)|(gbk)|(GB2312)|(GBK)";
			pattern = Pattern.compile(regex);
			matcher = pattern.matcher(content);
			if (matcher.find()) {
				return "GBK";
			} else {
				return null;
			}
		}
	}
}