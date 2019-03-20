package com.wb.parse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Set;

import javax.xml.transform.TransformerException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wb.client.JsoupClient;
import com.wb.util.NewRankUtil;
import com.wb.util.TestRegex;

public class Spider_xinbang_paper {
	static int num = 2;
	static JsoupClient client = new JsoupClient();

	public static void main(String[] args) throws IOException, InterruptedException, TransformerException {
		System.out.println("**************" + Thread.currentThread().getName() + "**************");
		long start = System.currentTimeMillis();
		System.out.println("开始爬虫.........................................");
		ExecutorService pool = null;
		String url = NewRankUtil.getURL("腾讯");
		Thread.sleep(1000);
		// String url = NewRankUtil.getURL("联想");
		try {
			Thread.sleep(1000);
			pool = Executors.newCachedThreadPool();
			pool = Executors.newFixedThreadPool(30);
			//如程序不能正常执行，可能是反扒，可替换cookies并更换新的ip
			String body = client.getHtmlPage3(url);
			JSONObject jso = JSON.parseObject(body);// json字符串转换成jsonobject对象
			pool.execute(getData(jso, "腾讯"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		// 释放资源
		pool.shutdown();
		while (true) {
			if (pool.isTerminated()) {

				long end = System.currentTimeMillis();
				System.out.println("爬虫结束.........................................");
				System.out.println("总共耗时" + (end - start) / 1000 + "秒");
				break;
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static Runnable getData(JSONObject jso, String key) throws IOException {
		// int tid = 10017;
		// SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");// 设置日期格式
		// String date = df.format(new Date());
		// int did = Integer.parseInt(date);
		JSONArray obj = jso.getJSONObject("value").getJSONArray("result");
		for (int i = 0; i < obj.size(); i++) {
			System.out.println(key);
			JSONObject data = obj.getJSONObject(i);
			String name = data.getString("name").replace("@font", "").replace("#font", "");
			System.out.println("微信名称:" + name);
			String account = data.getString("accountLower");
			if (account == null || account.equals("")) {
				account = "";
			}
			System.out.println("微信号:" + account);
			String description = data.getString("description").replace("@font", "").replace("#font", "");
			if (description == null || description.equals("")) {
				description = "";
			}
			System.out.println("功能介绍:" + description);
			String uuid = data.getString("uuid");
			if (name.contains(key) || description.contains(key)) {
				String jsonPaperJson = NewRankUtil.getURL2(account, uuid);
				String paperjson = client.getHtmlPage4(jsonPaperJson, account);
				JSONObject jso2 = JSON.parseObject(paperjson);// json字符串转换成jsonobject对象
				JSONArray array = jso2.getJSONObject("value").getJSONArray("lastestArticle");
				for (int j = 0; j < array.size(); j++) {
					String title = array.getJSONObject(j).getString("title");
					System.out.println("标题" + title);
					String publishtime = array.getJSONObject(j).getString("publicTime");
					System.out.println("时间" + publishtime);
					String clicksCount = array.getJSONObject(j).getString("clicksCount");
					System.out.println("阅读数" + clicksCount);
					String likeCount = array.getJSONObject(j).getString("likeCount");
					System.out.println("点赞数" + likeCount);
					String url = array.getJSONObject(j).getString("url");
					System.out.println("文章连接" + url);
					parseContent(url, title, publishtime, clicksCount, likeCount);
				}
				JSONArray array2 = jso2.getJSONObject("value").getJSONArray("topArticle");
				for (int j = 0; j < array2.size(); j++) {
					String title = array2.getJSONObject(j).getString("title");
					System.out.println("标题" + title);
					String publishtime = array2.getJSONObject(j).getString("publicTime");
					System.out.println("时间" + publishtime);
					String clicksCount = array2.getJSONObject(j).getString("clicksCount");
					System.out.println("阅读数" + clicksCount);
					String likeCount = array2.getJSONObject(j).getString("likeCount");
					System.out.println("点赞数" + likeCount);
					String url = array2.getJSONObject(j).getString("url");
					System.out.println("文章连接" + url);
					parseContent(url, title, publishtime, clicksCount, likeCount);
				}
			}
		}
		return null;
	}

	public static void parseContent(String url, String title, String publishtime, String clicksCount,
			String likeCount) {
		System.out.println("==================================================================");
		int pv = Integer.parseInt(clicksCount.trim());
		int like = Integer.parseInt(likeCount.trim());
		String website = "https://mp.weixin.qq.com/---PC";
		String html = new JsoupClient().DownBody(url);
		Document doc = Jsoup.parse(html);
		String obj = "";
		for (Element e : doc.select("script")) {
			if (e.toString().contains("publish_time") && e.toString().contains("nickname")) {
				obj = e.html();
				break;
			}
		}
		Elements imgsEle = doc.select("#js_content > p > img");
		String imgs = "";
		for (Element element : imgsEle) {
			imgs = imgs + element.attr("src") + ",";
		}
		if (imgs.equals("")) {
			imgs = obj.substring(obj.lastIndexOf("msg_cdn_url =") + 15, obj.lastIndexOf("var msg_link"))
					.replace("\";", "").trim();
		}
		System.out.println("img:" + imgs);
		String nickname = obj.substring(obj.lastIndexOf("nickname =") + 12, obj.lastIndexOf("var appmsg_type"))
				.replace("\";", "").trim();
		System.out.println("nickname:" + nickname);
		String content = doc.select("#js_content").text();
		if (content.indexOf("'") != -1) {
			content = TestRegex.emjoy(content.replace("'", "\"").replace("（点击文末“阅读原文”，一键预约理财师）", ""));
		}
		System.out.println("内容:" + content);
		System.out.println("==================================================================");
	}
}
