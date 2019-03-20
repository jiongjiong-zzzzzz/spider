package com.wb.client;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsoupClient {
	public String getHtmlPage(String url) throws IOException {
		Connection connection = Jsoup.connect(url).ignoreContentType(true);
		connection.header("User-Agent", "Aweme/1.8.5 (iPhone; iOS 11.3.1; Scale/3.00)").header("Host", "www.douyin.com")
				.header("Accept-Encoding", "gzip, deflate, br");
		Element body = connection.post();
		String text = body.text();
		return text;
	}

	public static String getHtmlPageSetIp(String ip, String url) {
		String[] r = ip.split(":");
		System.getProperties().setProperty("http.proxyHost", r[0]);
		System.getProperties().setProperty("http.proxyPort", r[1]);
		String text ="";
		try {
			Connection connection = Jsoup.connect(url).ignoreContentType(true);
			connection.header("User-Agent", "Aweme/1.8.5 (iPhone; iOS 11.3.1; Scale/3.00)").header("Host", "www.douyin.com")
					.header("Accept-Encoding", "gzip, deflate, br");
			Element body = connection.post();
			text= body.text();
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
//			con.data(fromdatakuaishou());
			// POST请求参数，from表单里的参数
//			con.data("mod", "OPPO(OPPO R9m)");
//			con.data("lon", "116.598477");
//			con.data("country_code", "cn");
//			con.data("did", "ANDROID_2d423e3849336f10");
//			con.data("app", "0");
//			con.data("net", "WIFI");
//			con.data("oc", "OPPO");
//			con.data("ud", "374374061");
//			con.data("sys", "ANDROID_5.1");
//			con.data("appver", "5.7.4.6223");
//			con.data("language", "zh-cn");
//			con.data("iuid",
//					"DugqkZiziLtvdETI+llr3oYjoncxrExteWlxuhms4yCPUwzM+V+qxcUpACmlMIdI17mUhqKO56VRGbJBaAsnwQ1w");
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

	public static String getDocPosHuajiaot(String url) {
		String json = "keyword=%E7%BA%A2%E6%9D%89%E8%B5%84%E6%9C%AC";
		String doc = null;
		try {
			Connection con = Jsoup.connect(url).ignoreContentType(true);
			// POST请求参数，from表单里的参数
			con.data(fromdata());
			// POST请求携带的参数,抓包可以抓到，此处传递了一个Json串
			con.requestBody(json);
			// 插入cookie（头文件形式）
			con.header("Host", "live.huajiao.com").header("Content-Type", "application/x-www-form-urlencoded")
					.header("Accept-Encoding", "gzip").header("User-Agent",
							"Mozilla/5.0 (Linux; U; Android 5.1; zh-cn; OPPO R9m Build/LMY47I) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
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

	public static Map<String, String> fromdata() {
		Map<String, String> map = new HashMap<String, String>();
		String cookies = "userid=178326318&deviceid=267228e5b0b8fcb0092f14cb9832cd34&platform=android&network=wifi&version=6.3.1.1017&rand=0.7199617603851425&netspeed=1024&time=1528696624&channel=oppo&dui=267228e5b0b8fcb0092f14cb9832cd34&imei=862609033854519&guid=6aecf57adfc935ddedae6a8dc452a0db&device=R9&devicebrand=OPPO&devicemanufacturer=OPPO&model=oppo+r9m&androidversion=5.1&androidversioncode=22&lng=116.598526&lat=39.91073227&province=%E5%8C%97%E4%BA%AC%E5%B8%82&city=%E5%8C%97%E4%BA%AC%E5%B8%82&town=%E6%9C%9D%E9%98%B3%E5%8C%BA&smid=20170316160440f840867ebe2db26ff190b0aa6dcf08821d29dc33b94fa059";
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
}
