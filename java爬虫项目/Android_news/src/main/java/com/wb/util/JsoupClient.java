package com.wb.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsoupClient {
	public static String getips() {
		String[] ip = { "221.210.120.153:54402", "180.118.243.247:53128", "183.129.207.80:32231", "139.199.66.222:808",
				"121.225.25.19:3128", "125.70.13.77:8080", "59.127.38.117:8080", "42.55.253.93:1133",
				"183.129.207.73:15823" };
		String random = "";
		int index = (int) (Math.random() * ip.length);
		random = ip[index];
		return random;
	}

	public static Document DownHtml(String url) {
		// String[] r = getips().split(":");
		// System.getProperties().setProperty("http.proxyHost", r[0]);
		// System.getProperties().setProperty("http.proxyPort", r[1]);
		Document document = null;
		try {
			// 设置头文件，结构同selenium相似
			document = Jsoup.connect(url)
					.header("Accept",
							"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
					.header("Accept-Encoding", "gzip, deflate, br").header("Accept-Language", "zh-CN,zh;q=0.9")
					.header("Cache-Control", "max-age=0")
					.header("User-Agent",
							"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.89 Safari/537.36")
					.timeout(10000).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}

	public static Document DownHtmlUC(String url) {
		String[] r = "103.103.52.40:8080".split(":");
		System.getProperties().setProperty("http.proxyHost", r[0]);
		System.getProperties().setProperty("http.proxyPort", r[1]);
		Document document = null;
		try {
			// 设置头文件，结构同selenium相似
			document = Jsoup.connect(url).ignoreContentType(true)
					.header("Accept",
							"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
					.header("Accept-Encoding", "gzip, deflate, br").header("Accept-Language", "zh-CN,zh;q=0.9")
					.header("Cache-Control", "max-age=0")
					.header("Cookie",
							"_cpcl=5b5eae56ec47e; _cpl=zh-cn; _cp_agentid=online; _ccst=1; gr_user_id=4fa624b3-b613-4ecc-bdfb-e3c26aecd7b2; _ga=GA1.2.497536352.1532931713; _gid=GA1.2.1304955142.1532931713; Hm_lvt_61d823871fcd65e2140d28838503aace=1532931712; Hm_lpvt_61d823871fcd65e2140d28838503aace=1532932398")
					.header("Host", "www.camcard.com")
					.header("User-Agent",
							"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.89 Safari/537.36")
					.timeout(10000).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}

	public Document DownHtml2(String url) {
		Document document = null;
		try {
			// 设置头文件，结构同selenium相似
			document = Jsoup.connect(url).ignoreContentType(true)
					.header("Accept",
							"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
					.header("Accept-Encoding", "gzip, deflate, br").header("Accept-Language", "zh-CN,zh;q=0.9")
					.header("Cache-Control", "max-age=0").header("Host", "weibo.com")
					// .header("Cookie",
					// "SINAGLOBAL=8624022936778.275.1516006533280; UOR=,,military.cnr.cn;
					// SUB=_2AkMsFPDKf8NxqwJRmPEWyW_ka4l0zA7EieKaSAERJRMxHRl-yj9jqhQetRB6B5TeJWObTTro5YHWbalNqmoTqd3ZCak4;
					// SUBP=0033WrSXqPxfM72-Ws9jqgMF55529P9D9WWQ0pKbZ5h0ADgdZ9KGJ8YQ;
					// TC-Page-G0=2b304d86df6cbca200a4b69b18c732c4;
					// TC-V5-G0=04dc502e635144031713f186989293c7; _s_tentry=-;
					// Apache=5666185039591.487.1531552855740;
					// ULV=1531552855800:5:1:1:5666185039591.487.1531552855740:1529657756586;
					// WBStorage=201807141540|undefined")
					.header("User-Agent",
							"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.89 Safari/537.36")
					.timeout(10000).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}

	public String getHtmlPage(String url) throws IOException {
		String[] r = getips().split(":");
		System.getProperties().setProperty("http.proxyHost", r[0]);
		System.getProperties().setProperty("http.proxyPort", r[1]);
		Connection connection = Jsoup.connect(url).ignoreContentType(true);
		connection.header("Accept-Encoding", "gzip,deflate").header("User-Agent",
				"Mozilla/5.0 (Linux; Android 5.1; OPPO R9m Build/LMY47I; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.121 Mobile Safari/537.36 JsKit/1.0 (Android)");
		;
		connection.timeout(10000);
		Element body = connection.post();
		String text = body.text();
		return text;
	}

	public String getHtmlPageGet(String url) throws IOException {
		String[] r = getips().split(":");
		System.getProperties().setProperty("http.proxyHost", r[0]);
		System.getProperties().setProperty("http.proxyPort", r[1]);
		Connection connection = Jsoup.connect(url).ignoreContentType(true);
		connection.header("Accept-Encoding", "gzip,deflate").header("User-Agent",
				"Mozilla/5.0 (Linux; U; Android 7.0; zh-CN; SM-G9550 Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/40.0.2214.89 UCBrowser/11.7.0.953 Mobile Safari/537.36");
		connection.timeout(50000);
		Element body = connection.get();
		String text = body.text();
		return text;
	}

	public String getHtmlPageUC(String url) throws IOException {
		String[] r = getips().split(":");
		System.getProperties().setProperty("http.proxyHost", r[0]);
		System.getProperties().setProperty("http.proxyPort", r[1]);
		Connection connection = Jsoup.connect(url).ignoreContentType(true);
		connection.header("Accept-Encoding", "gzip,deflate").header("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.89 Safari/537.36");
		connection.timeout(50000);
		Element body = connection.get();
		Elements t = body.select("a");
		String text = "";
		for (Element element : t) {
			text = text + element.attr("href") + ",";
		}
		return text;
	}

	public String getHtmlPageSina(String url) throws IOException {
		String[] r = "103.103.52.40:8080".split(":");
		System.getProperties().setProperty("http.proxyHost", r[0]);
		System.getProperties().setProperty("http.proxyPort", r[1]);
		Connection connection = Jsoup.connect(url).ignoreContentType(true);
		connection.header("Accept-Encoding", "gzip").header("Host", "newsapi.sina.cn")
				.header("User-Agent", "OPPO-OPPO R9m__sinanews__6.9.7__android__5.1");
		connection.timeout(30000);
		Element body = connection.post();
		String text = body.text();
		return text;
	}

	public String getHtmlPageWangYi(String url) throws IOException {
		String[] r = "103.103.52.40:8080".split(":");
		System.getProperties().setProperty("http.proxyHost", r[0]);
		System.getProperties().setProperty("http.proxyPort", r[1]);
		Connection connection = Jsoup.connect(url).ignoreContentType(true);
		connection.header("Accept-Encoding", "gzip, deflate").header("Host", "c.m.163.com")
				.header("Accept-Language", "zh-CN,zh;q=0.9").header("Cache-Control", "max-age=0")
				.header("User-Agent",
						"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.89 Safari/537.36");
		connection.timeout(30000);
		Element body = connection.get();
		String text = body.text();
		return text;
	}

	public static String getHtmlPageSetIp(String ip, String url) {
		String[] r = ip.split(":");
		System.getProperties().setProperty("http.proxyHost", r[0]);
		System.getProperties().setProperty("http.proxyPort", r[1]);
		String text = "";
		try {
			Connection connection = Jsoup.connect(url).ignoreContentType(true);
			connection.header("User-Agent", "Aweme/1.8.5 (iPhone; iOS 11.3.1; Scale/3.00)")
					.header("Host", "www.douyin.com").header("Accept-Encoding", "gzip, deflate, br");
			connection.timeout(30000);
			Element body = connection.post();
			text = body.text();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}

	//
	public static String getDocPost(String url) {
		String json = "touid=18611117&ftype=1&page=1&__NStokensig=40a03f17bb44cdf01c96a54f006a56980be0f5f428efece4907b60d613d56dee&token=3a60ce7f2c3b4924965f05dd046b1883-18611117&client_key=3c2cd3f3&os=android&sig=9324d691a312b38c8ffe1b34765cf2b5";
		String doc = null;
		try {
			Connection con = Jsoup.connect(url).ignoreContentType(true);
			// con.data(fromdatakuaishou());
			// POST请求参数，from表单里的参数
			// con.data("mod", "OPPO(OPPO R9m)");
			// con.data("lon", "116.598477");
			// con.data("country_code", "cn");
			// con.data("did", "ANDROID_2d423e3849336f10");
			// con.data("app", "0");
			// con.data("net", "WIFI");
			// con.data("oc", "OPPO");
			// con.data("ud", "374374061");
			// con.data("sys", "ANDROID_5.1");
			// con.data("appver", "5.7.4.6223");
			// con.data("language", "zh-cn");
			// con.data("iuid",
			// "DugqkZiziLtvdETI+llr3oYjoncxrExteWlxuhms4yCPUwzM+V+qxcUpACmlMIdI17mUhqKO56VRGbJBaAsnwQ1w");
			// POST请求携带的参数,抓包可以抓到，此处传递了一个Json串
			con.requestBody(json);
			// 插入cookie（头文件形式）
			con.header("Host", "api.ksapisrv.com").header("Content-Type", "application/x-www-form-urlencoded")
					.header("Accept-Encoding", "gzip").header("Accept-Language", "zh-cn")
					.header("User-Agent", "kwai-android");
			con.timeout(30000);
			doc = con.post().text();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}

	public static String getPostEveryDay(String url, String word, String json) {
		String doc = null;
		String[] r = "183.246.84.229:8060".split(":");
		System.getProperties().setProperty("http.proxyHost", r[0]);
		System.getProperties().setProperty("http.proxyPort", r[1]);
		try {
			Connection con = Jsoup.connect(url).ignoreContentType(true);
			con.data(formdata(json));
			con.requestBody(json);
			// 插入cookie（头文件形式）
			con.referrer("http://cnews.qq.com/cnews/android/").header("Host", "r.cnews.qq.com")
					.header("Accept-Encoding", "gzip,deflate")
					.header("User-Agent", "%E5%A4%A9%E5%A4%A9%E5%BF%AB%E6%8A%A54840(android)");
			con.timeout(30000);
			doc = con.post().text();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}

	public static String getPostCat(String url, String json) {
		String doc = null;
		String[] r = "183.246.84.229:8060".split(":");
		System.getProperties().setProperty("http.proxyHost", r[0]);
		System.getProperties().setProperty("http.proxyPort", r[1]);
		try {
			Connection con = Jsoup.connect(url).ignoreContentType(true);
			con.data(formdata(json));
			con.requestBody(json);
			// 插入cookie（头文件形式）
			con.referrer("http://cnews.qq.com/cnews/android/").header("Host", "r.cnews.qq.com")
					.header("Accept-Encoding", "gzip,deflate")
					.header("User-Agent", "%E5%A4%A9%E5%A4%A9%E5%BF%AB%E6%8A%A54840(android)");
			con.timeout(30000);
			doc = con.post().text();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}

	public static String getPostCshuqi(String url, String json) {
		String doc = null;
		String[] r = "183.246.84.229:8060".split(":");
		System.getProperties().setProperty("http.proxyHost", r[0]);
		System.getProperties().setProperty("http.proxyPort", r[1]);
		try {
			Connection con = Jsoup.connect(url).ignoreContentType(true);
			con.data(formdata(json));
			con.requestBody(json);
			// 插入cookie（头文件形式）
			con.header("User-C", "6K6i6ZiF").header("Add-To-Queue-Millis", "1531722537")
					.header("User-D", "JyH4qmC8TJ8zr9a3a/").header("httpDNSIP", "59.111.160.220")
					.header("User-L", "GmM8cbVMDunx").header("httpDNSIP", "GmM8cbVMDunx")
					.header("User-LC", "yCldziPlrx").header("User-N", "ELwR6f4rhTcUqICF")
					.header("data4-Sent-Millis", "1531722537490")
					.header("User-Agent", " NewsApp/39.1 Android/8.1.0 (xiaomi/Redmi Note 5)")
					.header("X-NR-Trace-Id", "1531722537491_76902487")
					.header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
					.header("Connection", "Keep-Alive").header("Accept-Encoding", "gzip").header("Host", "c.m.163.com");

			con.timeout(30000);
			doc = con.post().text();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}

	public static String getDocPosHuajiaot(String url) {
		String json = "keyword=%E7%BA%A2%E6%9D%89%E8%B5%84%E6%9C%AC";
		String doc = null;
		try {
			Connection con = Jsoup.connect(url).ignoreContentType(true);
			// POST请求参数，from表单里的参数
			con.data(formdata(
					"userid=178326318&deviceid=267228e5b0b8fcb0092f14cb9832cd34&platform=android&network=wifi&version=6.3.1.1017&rand=0.7199617603851425&netspeed=1024&time=1528696624&channel=oppo&dui=267228e5b0b8fcb0092f14cb9832cd34&imei=862609033854519&guid=6aecf57adfc935ddedae6a8dc452a0db&device=R9&devicebrand=OPPO&devicemanufacturer=OPPO&model=oppo+r9m&androidversion=5.1&androidversioncode=22&lng=116.598526&lat=39.91073227&province=%E5%8C%97%E4%BA%AC%E5%B8%82&city=%E5%8C%97%E4%BA%AC%E5%B8%82&town=%E6%9C%9D%E9%98%B3%E5%8C%BA&smid=20170316160440f840867ebe2db26ff190b0aa6dcf08821d29dc33b94fa059"));
			// POST请求携带的参数,抓包可以抓到，此处传递了一个Json串
			con.requestBody(json);
			// 插入cookie（头文件形式）
			con.header("Host", "live.huajiao.com").header("Content-Type", "application/x-www-form-urlencoded")
					.header("Accept-Encoding", "gzip,deflate").header("User-Agent",
							"Mozilla/5.0 (Linux; Android 5.1; OPPO R9m Build/LMY47I; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.121 Mobile Safari/537.36 JsKit/1.0 (Android)");
			con.timeout(30000);
			doc = con.post().text();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}

	public Map<String, String> cookie() {
		Map<String, String> map = new HashMap<String, String>();
		String cookies = "token=ZWUKoQsuWydJlfbiww--JUmIfjgZIpJv99ff";
		String[] s = cookies.split(";");
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

	public static Map<String, String> formdata(String formdata) {
		Map<String, String> map = new HashMap<String, String>();
		// String cookies =
		// "userid=178326318&deviceid=267228e5b0b8fcb0092f14cb9832cd34&platform=android&network=wifi&version=6.3.1.1017&rand=0.7199617603851425&netspeed=1024&time=1528696624&channel=oppo&dui=267228e5b0b8fcb0092f14cb9832cd34&imei=862609033854519&guid=6aecf57adfc935ddedae6a8dc452a0db&device=R9&devicebrand=OPPO&devicemanufacturer=OPPO&model=oppo+r9m&androidversion=5.1&androidversioncode=22&lng=116.598526&lat=39.91073227&province=%E5%8C%97%E4%BA%AC%E5%B8%82&city=%E5%8C%97%E4%BA%AC%E5%B8%82&town=%E6%9C%9D%E9%98%B3%E5%8C%BA&smid=20170316160440f840867ebe2db26ff190b0aa6dcf08821d29dc33b94fa059";
		String[] s = formdata.split("&");
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

	public static Map<String, String> fromdatakuaishou() {
		Map<String, String> map = new HashMap<String, String>();
		String cookies = "mod=OPPO%28OPPO%20R9m%29&lon=116.598557&country_code=cn&did=ANDROID_2d423e3849336f10&app=0&net=WIFI&oc=OPPO&ud=18611117&c=OPPO&sys=ANDROID_5.1&appver=5.7.4.6223&ftt=K-T-T&language=zh-cn&iuid=DugqkZiziLtvdETI%2Bllr3oYjoncxrExteWlxuhms4yCPUwzM%2BV%2BqxcUpACmlMIdI17mUhqKO56VRGbJBaAsnwQ1w&lat=39.910936&did_gt=1528861211881&ver=5.7&max_memory=256";
		String[] s = cookies.split("&");
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

	public static String getJsonUrl(String url) {
		// 查询Ip信息的接口，返回json
		// String url = "http://dc.mop.com/subject/a/151112092633000982351.json";
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		// 得到的json数据
		return result;
	}

	/**
	 * post请求（用于请求json格式的参数）
	 *
	 * @param url
	 *            请求网址
	 * @param params
	 *            requestBody
	 * @return
	 */
	public static String doPost(String url, String params) throws Exception {
		CloseableHttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);// 创建httpPost
		// 设置请求头
		httpPost.setHeader("User-C", "6K6i6ZiF");
		httpPost.setHeader("Add-To-Queue-Millis", "1531722537");
		httpPost.setHeader("User-D", "JyH4qmC8TJ8zr9a3a/");
		httpPost.setHeader("httpDNSIP", "59.111.160.220");
		httpPost.setHeader("User-L", "GmM8cbVMDunx");
		httpPost.setHeader("httpDNSIP", "GmM8cbVMDunx");
		httpPost.setHeader("User-LC", "yCldziPlrx");
		httpPost.setHeader("User-N", "ELwR6f4rhTcUqICF");
		httpPost.setHeader("data4-Sent-Millis", "1531722537490");
		httpPost.setHeader("User-Agent", " NewsApp/39.1 Android/8.1.0 (xiaomi/Redmi Note 5)");
		httpPost.setHeader("X-NR-Trace-Id", "1531722537491_76902487");
		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		httpPost.setHeader("Connection", "Keep-Alive");
		httpPost.setHeader("Accept-Encoding", "gzip");
		httpPost.setHeader("Host", "c.m.163.com");
		String charSet = "UTF-8";
		StringEntity entity = new StringEntity(params, charSet);
		httpPost.setEntity(entity);
		CloseableHttpResponse response = null;
		// System.out.println(httpPost.toString());
		try {

			response = httpclient.execute(httpPost);
			StatusLine status = response.getStatusLine();
			int state = status.getStatusCode();
			if (state == HttpStatus.SC_OK) {
				HttpEntity responseEntity = response.getEntity();
				String jsonString = EntityUtils.toString(responseEntity);
				return jsonString;
			} else {
				System.out.println("");
			}
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
