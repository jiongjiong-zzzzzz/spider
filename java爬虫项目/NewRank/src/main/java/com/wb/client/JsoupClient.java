package com.wb.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class JsoupClient {

	/**
	 * 182.45.176.234:6666 114.226.128.120:6666 203.69.87.196:8080
	 * 203.69.87.193:8080 183.20.9.236:61234 175.8.173.81:8118 183.128.243.1:6666
	 * 113.240.226.164:8080 203.69.87.194:8080 220.163.9.74:9293
	 * 
	 * @param url
	 * @return 返回document类型page
	 */
	public Document DownHtml(String url, String cookies) {
		Document document = null;
		try {
			// 设置头文件，结构同selenium相似
			document = Jsoup.connect(url)
					.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
					.header("Accept-Encoding", "gzip, deflate, br").header("Accept-Language", "zh-CN,zh;q=0.9")
					.header("Cache-Control", "max-age=0")
					.header("User-Agent",
							"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36")
					.cookies(cookie(cookies)).timeout(5000).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}

	public Document DownHtml1(String url) {
		String[] r = getips().split(":");
		System.getProperties().setProperty("http.proxyHost", r[0]);
		System.getProperties().setProperty("http.proxyPort", r[1]);
		Document document = null;
		try {
			// 设置头文件，结构同selenium相似
			document = Jsoup.connect(url).ignoreContentType(true).header("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.89 Safari/537.36")
					.timeout(5000).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}

	public Document DownHtml2(String url, Map<String, String> cookies) {
		Document document = null;
		try {
			// 设置头文件，结构同selenium相似
			document = Jsoup.connect(url)
					.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
					.header("Accept-Encoding", "gzip, deflate, br").header("Accept-Language", "zh-CN,zh;q=0.9")
					.header("Cache-Control", "max-age=0")
					.header("User-Agent",
							"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0")
					.cookies(cookies).timeout(5000).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}

	// ip可能有失效的，建議替換
	public static String getips() {
		String[] ip = { "119.5.213.163:7684", "171.125.145.49:4278", "119.5.224.213:2361", "58.243.28.120:4676",
				"122.195.209.5:6915", "101.64.33.193:2682", "175.165.207.248:4274", "42.58.245.116:4274",
				"112.111.109.226:4293", "153.101.246.28:2589", "58.243.13.167:4227", "113.232.27.88:4675",
				"122.192.187.22:4278", "115.85.206.79:3012", "112.243.176.124:1245", "153.99.25.182:6915",
				"115.85.206.223:3012", "124.152.185.134:2132", "175.44.109.234:4254", "113.238.96.253:4268",
				"119.5.224.138:2361", "1.62.238.154:1648", "123.152.42.239:2682", "175.167.239.34:1767",
				"123.156.176.100:4281", "175.42.121.252:4261", "101.64.157.208:2682", "39.81.63.133:8878",
				"110.52.224.201:4252", "175.152.145.216:4262", "123.156.25.235:4281", "175.44.108.254:4254",
				"58.22.207.98:4293", "39.66.10.254:4274", "113.0.206.35:1648", "153.99.7.29:6915", "119.7.139.235:4212",
				"112.123.41.181:7946", "112.123.40.138:7946", "112.195.205.110:4216", "175.165.223.243:4274",
				"119.115.1.148:8943", "58.22.177.88:4254", "113.232.2.135:4675", "124.161.44.199:7684",
				"27.206.74.196:8878", "182.127.82.201:4242", "175.42.128.190:4254", "1.62.238.127:1648",
				"101.64.14.127:2682", "110.52.224.37:4252", "115.58.68.107:4242", "1.62.234.140:1648",
				"112.123.43.157:4676", "61.53.146.110:2991", "112.83.92.198:2589", "123.156.191.157:4281",
				"175.42.97.192:4261", "175.42.158.41:4254", "101.64.157.47:2682", "119.7.113.101:4262",
				"122.192.187.149:4278", "123.131.81.141:1245", "123.188.198.106:1767", "119.5.181.101:3979",
				"42.226.94.157:2991", "58.243.107.143:4249", "119.5.196.33:4262", "123.156.181.44:4281",
				"39.66.15.30:4243", "27.40.152.181:4238", "124.163.72.217:4278", "175.42.158.46:4254",
				"175.44.109.108:4254", "124.152.85.64:3012", "153.99.25.80:6915", "36.248.147.67:4261",
				"123.156.185.253:4281", "175.167.237.124:1767", "175.167.57.22:8946", "27.206.74.229:8878",
				"119.5.193.25:4262", "58.243.15.57:4227", "112.195.141.153:4212", "112.83.92.110:2589",
				"119.4.68.227:4262", "112.123.248.158:4676", "218.9.192.184:1648", "112.122.222.244:4286",
				"27.221.193.185:1245", "124.161.104.44:4212", "124.161.44.48:7684", "112.122.217.162:4286",
				"27.209.15.165:4243", "175.152.145.223:4262", "153.101.240.168:2589", "113.229.82.115:4274",
				"124.161.248.250:4262", "1.190.185.70:1648", "221.202.104.175:4274" };
		String random = "";
		int index = (int) (Math.random() * ip.length);
		random = ip[index];
		return random;
	}

	/**
	 *
	 * @param url
	 * @return 返回string 类型page
	 */
	public String DownBody(String url) {
		String body = null;

		try {
			// 设置头文件，结构同selenium相似
			body = Jsoup.connect(url).header("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36")
					.cookies(cookie()).timeout(5000).ignoreContentType(true).execute().body();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return body;
	}

	public static String getHtmlPage(String url) throws IOException {
		// Mozilla/5.0 (compatible; MSIE 10.0; Windows Phone 8.0; Trident/6.0;
		// IEMobile/10.0; ARM; Touch; NOKIA; Lumia 920)
		String[] r = getips().split(":");
		System.getProperties().setProperty("http.proxyHost", r[0]);
		System.getProperties().setProperty("http.proxyPort", r[1]);
		Connection connection = Jsoup.connect(url).ignoreContentType(true);
		connection.header("User-Agent",
				"Mozilla/5.0 (compatible; MSIE 10.0; Windows Phone 8.0; Trident/6.0; IEMobile/10.0; ARM; Touch; NOKIA; Lumia 920)")
				.header("Host", "www.douyin.com").header("Accept-Encoding", "gzip, deflate, br");
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
		String json = "touid=853241536&ftype=1&page=1&__NStokensig=cd714c47ca438a35b0328dff4632f65d657ebd6824db56c4c55cc745316419d8&token=e634e8422f0e463fac0e0701fc74bb9f-853241536&client_key=3c2cd3f3&os=android&sig=32699367f44834754608c8864b3e529d";
		String doc = null;
		try {
			Connection con = Jsoup.connect(url).ignoreContentType(true);
			// POST请求参数，from表单里的参数
			// con.data("mod", "OPPO(OPPO R9m)");
			// con.data("lon", "116.598477");
			// con.data("country_code", "cn");
			// con.data("did", "ANDROID_2d423e3849336f10");
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

	public static String getPostBusiness(String url) {
		String doc = null;
		try {
			Connection con = Jsoup.connect(url).ignoreContentType(true);
			con.data("searchword", "小米");
			// 插入cookie（头文件形式）
			con.header("Host", "app.gsxt.gov.cn").header("X-Requested-With", "XMLHttpRequest").header("User-Agent",
					"Mozilla/5.0 (Linux; Android 8.0.0; EDI-AL10 Build/HUAWEIEDISON-AL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/68.0.3440.91 Mobile Safari/537.36 Html5Plus/1.0");
			con.timeout(30000);
			doc = con.post().text();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}

	public static String getSouhu(String url) {
		String[] r = getips().split(":");
		System.getProperties().setProperty("http.proxyHost", r[0]);
		System.getProperties().setProperty("http.proxyPort", r[1]);
		String doc = null;
		try {
			Connection con = Jsoup.connect(url).ignoreContentType(true);
			// 插入cookie（头文件形式）
			con/* .header("Host", "v2.sohu.com") */.header("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
			con.timeout(30000);
			doc = con.get().text();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}

	/**
	 * 新榜
	 * cookies沒有实现自动化，手动抓取后填入相应位置即可
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public String getHtmlPage3(String url) throws IOException {
		String[] r = getips().split(":");
		System.getProperties().setProperty("http.proxyHost", r[0]);
		System.getProperties().setProperty("http.proxyPort", r[1]);
		Connection connection = Jsoup.connect(url).ignoreContentType(true);
		connection.header("Accept-Encoding", "gzip,deflate").header("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36")
				.header("cookie",
						"ticket=gQHj8DwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAycGo5TjA3a0ljbTMxT1pFRk5yMUgAAgStGulbAwQQDgAA; token=588E47D7641CDD4F71B45840BAB2FE32; tt_token=true; UM_distinctid=1645937ad774cf-0bfa2909447ec8-5e442e19-144000-1645937ad78b5e; __root_domain_v=.newrank.cn; _qddaz=QD.ereyug.mzw27e.jj3r9nca; Hm_lvt_a19fd7224d30e3c8a6558dcb38c4beed=1539587766,1539675266,1542003370; CNZZDATA1253878005=354524346-1530502204-%7C1542001311; ticket=gQHj8DwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAycGo5TjA3a0ljbTMxT1pFRk5yMUgAAgStGulbAwQQDgAA; _qdda=3-1.1; _qddab=3-s8w2c5.jodwzsaa; _qddamta_2852150610=3-0; token=588E47D7641CDD4F71B45840BAB2FE32; tt_token=true; Hm_lpvt_a19fd7224d30e3c8a6558dcb38c4beed=1542003400");
		Element body = connection.post();
		String text = body.text();
		return text;
	}

	/**
	 * 新榜
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public String getHtmlPage4(String url, String account) throws IOException {
		String[] r = getips().split(":");
		System.getProperties().setProperty("http.proxyHost", r[0]);
		System.getProperties().setProperty("http.proxyPort", r[1]);
		Connection connection = Jsoup.connect(url).ignoreContentType(true);
		connection.header("Accept-Encoding", "gzip,deflate").header("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36")
				.header("Host", "www.newrank.cn")
				.header("Referer", "https://www.newrank.cn/public/info/detail.html?account=" + account).header("cookie",
						"ticket=gQHj8DwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAycGo5TjA3a0ljbTMxT1pFRk5yMUgAAgStGulbAwQQDgAA; token=588E47D7641CDD4F71B45840BAB2FE32; tt_token=true; UM_distinctid=1645937ad774cf-0bfa2909447ec8-5e442e19-144000-1645937ad78b5e; __root_domain_v=.newrank.cn; _qddaz=QD.ereyug.mzw27e.jj3r9nca; Hm_lvt_a19fd7224d30e3c8a6558dcb38c4beed=1539587766,1539675266,1542003370; CNZZDATA1253878005=354524346-1530502204-%7C1542001311; ticket=gQHj8DwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAycGo5TjA3a0ljbTMxT1pFRk5yMUgAAgStGulbAwQQDgAA; _qdda=3-1.1; _qddab=3-s8w2c5.jodwzsaa; _qddamta_2852150610=3-0; token=588E47D7641CDD4F71B45840BAB2FE32; tt_token=true; Hm_lpvt_a19fd7224d30e3c8a6558dcb38c4beed=1542003400");
		Element body = connection.post();
		String text = body.text();
		return text;
	}

	public Map<String, String> cookie(String cookie) {
		Map<String, String> map = new HashMap<String, String>();
		// String cookies = "_T_WM=3288178faabe921622db85030726c087;
		// SCF=Aq7c9XAeQE0nrY_DmvWRqwJgafR5d__bzXA6mq59NeHhogWUyDtqRyPkvxXFk31KsIbaGLCfJvy7PEMDG4iSHiU.;
		// SUB=_2A252LZe0DeRhGeBM7loY9y7Nwz6IHXVV0Tn8rDV6PUJbkdANLWnCkW1NRM-zgE7iHcI3fZwK4CsIjDrXcC54mg-z;
		// SUHB=0M2Fcn_HmB-Fxe; SSOLoginState=1529472996";
		String[] s = cookie.split(";");
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

	public Map<String, String> cookie() {
		Map<String, String> map = new HashMap<String, String>();
		String cookies = "tt_webid=75405731274; WEATHER_CITY=%E5%8C%97%E4%BA%AC; UM_distinctid=1645937f6c777c-029ce420b01e38-5e442e19-144000-1645937f6c834e; csrftoken=d7c4c35b5cefb808fa2abb198b967fe2; login_flag=b8aa691c33f203797c4332435a3c3840; sessionid=3384af7056efe000fc612fda9440a751; uid_tt=15dc3ce00d99f403487c2fd3d155754d; sid_tt=3384af7056efe000fc612fda9440a751; sid_guard=\"3384af7056efe000fc612fda9440a751|1530609785|15552000|Sun\\054 30-Dec-2018 09:23:05 GMT\"; tt_webid=75405731274; uuid=\"w:3c62910be5874987a2b3b6d3c9ee7013\"; tt_webid=75405731274; _ga=GA1.2.1502562942.1533692870; __utma=24953151.1502562942.1533692870.1536558948.1536558948.1; __utmz=24953151.1536558948.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); ccid=564ec86ca963841f249953648de573c9; CNZZDATA1259612802=204329466-1530501134-https%253A%252F%252Fwww.newrank.cn%252F%7C1538287942; __tasessionId=gz2lpu0ih1538291863033";
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

	public static final String returnCookies(String url) {
		try {
			Connection conn = Jsoup.connect(url);
			conn.data("keyword", "%E7%BA%A2%E6%9D%89%E8%B5%84%E6%9C%AC");
			conn.data("smblog", "%E6%90%9C%E5%BE%AE%E5%8D%9A");
			conn.method(Method.GET);
			conn.followRedirects(false);
			Response response;
			response = conn.execute();
			Map<String, String> getCookies = response.cookies();
			String Cookie = getCookies.toString();
			Cookie = Cookie.substring(Cookie.indexOf("{") + 1, Cookie.lastIndexOf("}"));
			Cookie = Cookie.replaceAll(",", ";");
			return Cookie;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
