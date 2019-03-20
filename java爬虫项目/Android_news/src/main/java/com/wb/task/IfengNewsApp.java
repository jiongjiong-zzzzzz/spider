package com.wb.task;

import static org.junit.Assume.assumeFalse;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.codec.digest.DigestUtils;
import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.kevin.crop.R.string;
import com.wb.pojo.News;
import com.wb.util.ChangeCharset;
import com.wb.util.JsoupClient;
import com.wb.util.KafkaUtils;
import com.wb.util.RedisHash;
import com.wb.util.TestRegex;

//鳳凰新聞app
public class IfengNewsApp {
	static int page = 1;
	static JsoupClient client = new JsoupClient();

	public static void main(String[] args) throws IOException {
		String key = "小米科技";
		ChangeCharset charsert = new ChangeCharset();
		String word = charsert.toutf8(key);
		long t1 = System.currentTimeMillis();
		// 爬取5頁
		// 接口token加密 破解後發現是對IFENG和當前毫秒級時間拼接字符串MD5加密
		for (int y = 1; y <= 5; y++) {
			String jsonurl = "https://api.3g.ifeng.com/ClientSearchList?k=" + word + "&page=" + y
					+ "&gv=6.1.9&av=6.1.9&uid=862609033854519&n=10&deviceid=862609033854519&proid=ifengnews&os=android_22&df=androidphone&vt=5&screen=720x1280&publishid=6101&nw=wifi&token="
					+ DigestUtils.md5Hex("IFENG" + t1) + "&date=" + t1 + "&callback=1&callback=window.jsonp.cb0";
			System.out.println("接口" + jsonurl);
			parseContent(jsonurl, key, 0);
		}
	}

	public static void parseContent(String jsonurl, String key, int cid) throws IOException {
		try {
			String body = new JsoupClient().getHtmlPage(jsonurl).replace("window.jsonp.cb0(", "").replace(")", "");
			System.out.println(body);
			//判断json格式是否正确
			if (TestRegex.isJson(body) == true) {
				JSONObject jso = JSON.parseObject(body);
				System.out.println("解析---" + jso);
				System.out.println(jso.getString("msg"));
				//请求成功后msg值为ok
				if (jso.getString("msg").equals("OK")) {
					JSONArray array = jso.getJSONArray("items");
					for (int i = 0, len = array.size(); i < len; i++) {
						String types = array.getJSONObject(i).getString("type");
						//过滤视频
						if (!types.equals("phvideo")) {
							//拼接詳情頁jsonurl
							String contentUrl = array.getJSONObject(i).getString("id");
							//請求接口
							String contentbody = new JsoupClient().getHtmlPage(contentUrl);
							if (TestRegex.isJson(contentbody) == true) {
								JSONObject jsoBody = JSON.parseObject(contentbody);
								System.out.println();
								String title = jsoBody.getJSONObject("body").getString("title")
										.replaceAll("</?[a-zA-Z]+[^><]*>", "");
								System.out.println("标题:" + title);
								String source = jsoBody.getJSONObject("body").getString("source");
								System.out.println("来源:" + source);
								String publishtime = jsoBody.getJSONObject("body").getString("cTime");
								publishtime = publishtime.replace("/", "-");
								System.out.println("发布时间:" + publishtime);
								String content = jsoBody.getJSONObject("body").getString("text")
										.replaceAll("</?[a-zA-Z]+[^><]*>", "");
								System.out.println("内容:" + content);
								String weburl = jsoBody.getJSONObject("body").getString("shareurl");
								System.out.println("内容地址:" + weburl);
								JSONArray imgarray = jsoBody.getJSONObject("body").getJSONArray("img");
								String imgurl = "";
								for (int j = 0, lenimg = imgarray.size(); j < lenimg; j++) {
									imgurl = imgurl + imgarray.getJSONObject(j).getString("url") + ",";
								}
								String logo = jsoBody.getJSONObject("body").getJSONObject("subscribe")
										.getString("logo");
								System.out.println("发布作者头像:" + logo);
								if (title.contains(key) || content.contains(key)) {
									News news = new News();
									news.setCid(cid);
									news.setRtime(publishtime);
									news.setLink(weburl);
									news.setTitle(title);
									news.setCf(source);
									news.setBody(content);
									news.setImages(imgurl);
									news.setReid(source);
									news.setReidsrc(logo);
									// 转换成json串发送到kafka
									// String msg = JSON.toJSONString(news);
									// KafkaUtils.sendMsgToKafka(msg);
								}
							}
						}
					}
				}
			}
		} catch (NullPointerException e) {
			// TODO: handle exception
		}

	}

}