package com.wb.task;

import static org.junit.Assume.assumeFalse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.print.Doc;

import org.apache.commons.codec.digest.DigestUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.wb.util.ChangeCharset;
import com.wb.util.JsoupClient;
import com.wb.util.RedisHash;
import com.wb.util.TestRegex;
//百度新聞
public class BaiDuNewsApp {
	static RedisHash hash = new RedisHash();
	static JsoupClient client = new JsoupClient();

	public static void main(String[] args) throws IOException {
		String key = "小米科技";
		Date date = new Date();
		ChangeCharset charsert = new ChangeCharset();
		String word = charsert.toutf8(key);
		String json = "https://news.baidu.com/sn/api/search?pd=newsplus&os=android&sv=7.1.3.0&from=app&_uid=l82d8l8tS800a-au0uHCu_8f2alcOHiQ08vHfjaRviiqOv8fgaBz8_u6-i_ua2tDA&_ua=_a-qi4uq-igBNE6lI5me6NNy2I_UC2NegI2ctA-qC&_ut=r93jO6kEB64_asiDyavGCr9H3O-VC&_from=1014720z&_cfrom=1014720z&_network=1_0&cen=uid_ua_ut&pn=0&wf=1&s="
				+ word + "&rn=20&cuid=DC46FED5B08BA0CD4D4FC9C0AF099361|915458330906268";
		try {
			String body = new JsoupClient().getHtmlPage(json);
			if (TestRegex.isJson(body) == true) {
				JSONObject jso = JSON.parseObject(body);// json字符串转换成jsonobject对象
				System.out.println("解析:---" + jso);
				//文章
				getBaiDuContent(jso, key, 0);
				//账号
				if (jso.containsKey("forum")) {
					getBaiDuAccount(jso, key, 0);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public static void getBaiDuContent(JSONObject obj, String key, int cid) {
		JSONArray array_c = obj.getJSONObject("data").getJSONArray("news");
		for (int i = 0; i < array_c.size(); i++) {
			String title = array_c.getJSONObject(i).getString("title");
			String url = array_c.getJSONObject(i).getString("url");
			String source = array_c.getJSONObject(i).getString("site");
			String time = array_c.getJSONObject(i).getString("ts");
			time = TestRegex.Millstotime(Long.parseLong(time));
			System.out.println("标题" + title);
			System.out.println("来源" + source);
			System.out.println("时间" + time);
			System.out.println("链接" + url);
		}
	}

	public static void getBaiDuAccount(JSONObject obj, String key, int cid) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");// 设置日期格式
		String date = df.format(new Date());
		JSONArray array = (JSONArray) obj.getJSONObject("data").getJSONArray("forum").get(0);
		String keys = "百度APP_account" + date;
		int tid = 10006;
		int did = Integer.parseInt(date);
		for (int i = 0; i < array.size(); i++) {
			String forum_name = array.getJSONObject(i).getString("forum_name");
			String forum_id = array.getJSONObject(i).getString("forum_id");
			String forum_icon = array.getJSONObject(i).getString("forum_icon");
			String forum_abstract = array.getJSONObject(i).getString("forum_abstract");
			int follow_count = array.getJSONObject(i).getInteger("follow_count");
			System.out.println("账号名称" + forum_name);
			System.out.println("账号id" + forum_id);
			System.out.println("账号头像" + forum_icon);
			System.out.println("账号简介" + forum_abstract);
			System.out.println("粉丝数量" + follow_count);
		}
	}
}