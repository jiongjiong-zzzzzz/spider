package com.wb.task;

import java.io.IOException;
import java.util.ArrayList;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.wb.client.ChangeCharset;
import com.wb.client.JsoupClient;

public class DouYuTask {
	public static void main(String[] args) throws IOException {

		String type = "斗鱼直播";
		ChangeCharset charsert = new ChangeCharset();
		try {
			String word = charsert.toutf8("冯提莫");
			String jsonurl = "https://capi.douyucdn.cn/api/v1/mobileSearch/1/1?sk=" + word
					+ "&offset=0&limit=20&client_sys=android";
			String body = new JsoupClient().getHtmlPage(jsonurl);
			System.out.println(body);
			JSONObject jso = JSON.parseObject(body);// json字符串转换成jsonobject对象
			System.out.println("初始jsonObject:\n" + jso + "\n");
			JSONObject jro = jso.getJSONObject("data");
			JSONArray array = jro.getJSONArray("anchor");
			if (array.size() != 0) {
				for (int i = 0, len = 1; i < len; i++) {
					String nickname = array.getJSONObject(i).getString("noRed");
					String cateName = array.getJSONObject(i).getString("cateName");
					int follow = array.getJSONObject(i).getInteger("follow");
					int hn = array.getJSONObject(i).getInteger("hn");
					String room_id = array.getJSONObject(i).getInteger("room_id").toString();
					String avatar = array.getJSONObject(i).getString("avatar");
					if (nickname.contains("冯提莫")) {
						System.out.println("分类:" + cateName);
						System.out.println("昵称:" + nickname);
						System.out.println("头像:" + avatar);
						System.out.println("房间号:" + room_id);
						System.out.println("关注:" + follow);
						System.out.println("热度:" + hn);
					}
				}
			}
		} catch (JSONException e) {
			// TODO: handle exception
		}
	}
}