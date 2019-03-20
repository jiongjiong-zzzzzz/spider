package com.wb.task;

import java.io.IOException;
import java.util.ArrayList;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParseContext;
import com.wb.client.ChangeCharset;
import com.wb.client.JsoupClient;

public class XiongMaoTask {

	public static void main(String[] args) throws IOException {
		ChangeCharset i = new ChangeCharset();
		String word = i.toutf8("托马斯");
		String jsonurl = "https://api.m.panda.tv/ajax_search_host?keyword=" + word
				+ "&pageno=1&pagenum=20&__plat=android&__version=4.0.6.6999&__channel=oppo";
		String body = new JsoupClient().getHtmlPage(jsonurl);
		System.out.println(body);
		JSONObject jso = JSON.parseObject(body);// json字符串转换成jsonobject对象
		System.out.println("初始jsonObject:\n" + jso + "\n");
		JSONObject jarr = jso.getJSONObject("data");
		JSONArray array = jarr.getJSONArray("items");
		String type = "熊猫直播";
		try {
			if (array.size() != 0) {
				for (int j = 0, lens = 1; j < lens; j++) {
					parse(array, j);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void parse(JSONArray array, int index) {
		String nickname = array.getJSONObject(index).getJSONObject("userinfo").getString("nickName");
		System.out.println("昵称" + nickname);
		String avatar = array.getJSONObject(index).getJSONObject("userinfo").getString("avatar");
		System.out.println("头像" + avatar);
		String cname = array.getJSONObject(index).getJSONObject("classification").getString("cname");
		System.out.println("类别" + cname);
		String id = array.getJSONObject(index).getString("id");
		System.out.println("房间号" + id);
		int fans = Integer.parseInt(array.getJSONObject(index).getString("fans"));
		System.out.println("粉丝数" + fans);
		int person_num = Integer.parseInt(array.getJSONObject(index).getString("person_num"));
		System.out.println("人气" + person_num);
	}
}