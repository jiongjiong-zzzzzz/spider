package com.wb.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Test {

	static String json ="{\r\n" + 
			"    \"data\": [\r\n" + 
			"        {\r\n" + 
			"            \"ratingagency\": \"辉立证券\",\r\n" + 
			"            \"rating\": \"孖展认购\",\r\n" + 
			"            \"score\": 70,\r\n" + 
			"            \"ratingurl\": \"http://research.cyberquote.com.hk/page/htm/kc/share_recommend/pdf/1806.pdf\"\r\n" + 
			"        },\r\n" + 
			"        {\r\n" + 
			"            \"ratingagency\": \"英皇证券\",\r\n" + 
			"            \"rating\": \"认购\",\r\n" + 
			"            \"score\": 70,\r\n" + 
			"            \"ratingurl\": \"http://www.emperorcapital.com/filemanager/ipoinformation/tc/upload/545/2018-06-01%20%E5%8C%AF%E4%BB%98%E5%A4%A9%E4%B8%8B(1806.HK).pdf\"\r\n" + 
			"        },\r\n" + 
			"        {\r\n" + 
			"            \"ratingagency\": \"致富证券\",\r\n" + 
			"            \"rating\": \"认购\",\r\n" + 
			"            \"score\": 60,\r\n" + 
			"            \"ratingurl\": \"https://www.chiefgroup.com.hk/hk/financial/stock-research?researchID=359\"\r\n" + 
			"        }\r\n" + 
			"    ],\r\n" + 
			"    \"result\": 1,\r\n" + 
			"    \"cache\": true,\r\n" + 
			"    \"durationSeconds\": 1800,\r\n" + 
			"    \"Version\": \"5.7.180607.30463\",\r\n" + 
			"    \"ElapsedMilliseconds\": 0,\r\n" + 
			"    \"serverIp\": \"10.0.11.22\",\r\n" + 
			"    \"requestIp\": \"1.119.192.130\",\r\n" + 
			"    \"clientIp\": \"1.119.192.130\"\r\n" + 
			"}" ;

	public static void main(String[] args) {

		JSONObject jso = JSON.parseObject(json);// json字符串转换成jsonobject对象
		System.out.println("初始jsonObject:\n" + jso + "\n");
		JSONArray aaa= jso.getJSONArray("data");
		for (int i = 0,len=aaa.size(); i <len; i++) {
			String ratingagency=aaa.getJSONObject(i).getString("ratingagency");
			String rating=aaa.getJSONObject(i).getString("rating");
			String score=aaa.getJSONObject(i).getString("score");
			String ratingurl=aaa.getJSONObject(i).getString("ratingurl");
			System.out.println("ratingagency:"+ratingagency);
			System.out.println("rating:"+rating);
			System.out.println("score:"+score);
			System.out.println("ratingurl:"+ratingurl);
			System.out.println("----------------");
		}
		 
		 
	}
	 
}