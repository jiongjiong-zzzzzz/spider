package com.wb.task;

import java.io.IOException;
import java.util.ArrayList;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.wb.pojo.News;
import com.wb.util.ChangeCharset;
import com.wb.util.JsoupClient;
import com.wb.util.KafkaUtils;
import com.wb.util.TestRegex;
//搜狐新闻
public class SouHuNewsApp {

	public static void main(String[] args) throws IOException {

		String key = "阿里巴巴";
		ChangeCharset charsert = new ChangeCharset();
		for (int y = 1; y <= 5; y++) {
			String word = charsert.toutf8(key);
			long t1 = System.currentTimeMillis();
			String jsonurl = "https://api.k.sohu.com/api/search/v5/search.go?rt=json&pageNo=" + y + "&words=" + word
					+ "&keyword=" + word
					+ "&p1=NjQxNTg2MDUwMDY1NTQxOTUxNw%3D%3D&pageSize=20&type=0&pid=&token=&gid=&apiVersion=42&sid=10&u=1&bid=&keyfrom=input&autoCorrection=&refertype=1&versionName=6.1.1&os=android&picScale=16&h=1157&_="
					+ t1;
			try {
				String body = new JsoupClient().getHtmlPage(jsonurl);
				JSONObject jso2 = null;
				JSONObject jso = JSON.parseObject(body);// json字符串转换成jsonobject对象
				System.out.println(jso);
				if (!jso.keySet().contains("msg")) {
					System.out.println("初始jsonObject:\n" + jso + "\n");
					JSONArray array = jso.getJSONArray("resultList");
					for (int i = 0, len = array.size(); i < len; i++) {
						int newsId = array.getJSONObject(i).getInteger("newsId");
						System.out.println(newsId);
						String link = "http://3g.k.sohu.com/t/n" + newsId + "?showType=";
						String contentUrl = "https://api.k.sohu.com/api/news/v5/article.go?channelId=&apiVersion=42&gid=-1&imgTag=1&newsId="
								+ newsId
								+ "&openType=&u=1&p1=NjQxNTg2MDUwMDY1NTQxOTUxNw%3D%3D&pid=-1&recommendNum=3&refer=130&rt=json&showSdkAd=1&moreCount=8&articleDebug=0&_="
								+ t1;
						String body3 = new JsoupClient().getHtmlPage(contentUrl);
						if (TestRegex.isJson(body3) == true) {
							jso2 = JSON.parseObject(body3);// json字符串转换成jsonobject对象
							String title = jso2.getString("title");
							System.out.println("标题：" + title);
							String time = jso2.getString("time");
							time = time.replaceAll("/", "-") + ":00";
							System.out.println("時間：" + time);
							String author = "";
							String logo = "";
							if (jso2.containsKey("subInfo")) {
								author = jso2.getJSONObject("subInfo").getString("subName");
								logo = jso2.getJSONObject("subInfo").getString("subIcon");
							} else {
								author = "";
							}
							System.out.println("作者：" + author);
							System.out.println("作者logo：" + logo);
							String content = "";
							if (jso2.keySet().contains("content")) {
								content = jso2.getString("content").replaceAll("</?[a-zA-Z]+[^><]*>", "");
								System.out.println("内容：" + content);
							}
							String imgs = "";
							if (jso2.containsKey("photos")) {
								JSONArray imgsarray = jso2.getJSONArray("photos");
								for (int j = 0; j < imgsarray.size(); j++) {
									imgs = imgs + imgsarray.getJSONObject(i).getString("pic") + ",";
								}
							}
							System.out.println("imgs" + imgs);
							System.out.println("link" + link);
							if (title.contains(key )
									|| content.contains(key )) {
								if (!title.equals("") && !content.equals("") && !time.equals("")) {
									News news = new News();
									news.setRtime(time);
									news.setLink(link);
									news.setTitle(title);
									news.setReid(author);
									news.setReidsrc(logo);
									news.setBody(content);
									news.setImages(imgs);
									// String msg = JSON.toJSONString(news);
									// KafkaUtils.sendMsgToKafka(msg);
								}
							}
						}
					}
				} else {
					System.out.println("该条json格式错误");
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}
}