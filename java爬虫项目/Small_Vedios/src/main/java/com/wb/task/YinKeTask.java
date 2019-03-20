package com.wb.task;

import java.io.IOException;
import java.util.ArrayList;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wb.client.ChangeCharset;
import com.wb.client.JsoupClient;

public class YinKeTask {

	public static void main(String[] args) throws IOException {
		ChangeCharset charsert = new ChangeCharset();
		String type = "映客直播";
		String word = charsert.toutf8("大大大黑牛");
		String jsonurl = "https://service.inke.cn/api/user/search?cc=TG36014&lc=39ab43565c161c23&mtxid=0c8fffcba6a0&cpu=%5BAdreno_%28TM%29_506%5D%5BAArch64_638_Qualcomm_Technologies%2C_Inc_MSM8953%5D&devi=864080039931412&sid=20e1oDA1zZOOCq6IIi1GZknYAgEi0QEAn65EyxerbLXei2detfKh9lQi3i3&osversion=android_23&cv=IK6.0.10_Android&imei=864080039931412&proto=8&conn=wifi&ram=3770019840&ua=OPPOOPPOR9s&logid=273%2C283%2C213%2C236%2C246&uid=13819297&icc=89860069011680096028&ast=1&vv=1.0.3-201610121413.android&aid=c9164fe0b96bd38f&smid=DuKeEDZmSmwvA5csE6WI2RJi7osoEriAuRQrjLJs4FVglpVJ5bjpBYkN2u4W00OctKuW%2F01eMxyPqCk9RKrNncJw&imsi=460009271962040&mtid=08f0ad9ec1a7ae2b246ef47e96ad7843&apiv=1.0&count=10&start=0&keyword="
				+ word + "&r_c=1760984647&s_sg=vv8549e717da819bf6b982ec881d2624ea&s_sc=101&s_st=1528771009";
		String body = new JsoupClient().getHtmlPage(jsonurl);
		System.out.println(body);
		try {
			JSONObject jso = JSON.parseObject(body);// json字符串转换成jsonobject对象
			System.out.println("初始jsonObject:\n" + jso + "\n");
			JSONArray array = jso.getJSONArray("users");
			if (array.size() != 0) {
				for (int i = 0, len = 1; i < len; i++) {
					String description = array.getJSONObject(i).getJSONObject("user").getString("description");
					String nick = array.getJSONObject(i).getJSONObject("user").getString("nick");
					String portrait = array.getJSONObject(i).getJSONObject("user").getString("portrait");
					String verified_reason = array.getJSONObject(i).getJSONObject("user").getString("verified_reason");
					int level = array.getJSONObject(i).getJSONObject("user").getInteger("level");
					String id = array.getJSONObject(i).getJSONObject("user").getInteger("id").toString();
					if (nick.contains("大大大黑牛")) {
						System.out.println("昵称：" + nick);
						System.out.println("描述：" + description);
						System.out.println("头像：" + portrait);
						System.out.println("认证：" + verified_reason);
						System.out.println("等级：" + level);
						System.out.println("映客号" + id);
//						nick = nick.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*");
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}