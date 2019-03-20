package com.wb.parse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import com.wb.client.SeleniumClient;
import com.wb.pojo.Account;
import com.wb.pojo.KeyWord;
import com.wb.util.ChangeCharset;
import com.wb.util.KafkaUtils;
import com.wb.util.MysqlConnect;
import com.wb.util.MysqlOpertion;
import com.wb.util.NewRankUtil;
import com.wb.util.TestRegex;

public class Spider_xinbang_account {
	static int num = 2;

	public static void main(String[] args) throws IOException, InterruptedException, TransformerException {
		String url = NewRankUtil.getURL("腾讯");
		Thread.sleep(1000);
		try {
			Thread.sleep(3000);
			String body = new JsoupClient().getHtmlPage3(url);
			System.out.println(body);
			JSONObject jso = JSON.parseObject(body);// json字符串转换成jsonobject对象
			getData(jso, "腾讯");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void getData(JSONObject jso, String key) {
		// int tid = 10017;
		// SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");// 设置日期格式
		// String date = df.format(new Date());
		// int did = Integer.parseInt(date);
		JSONArray obj = jso.getJSONObject("value").getJSONArray("result");
		for (int i = 0; i < obj.size(); i++) {
			JSONObject data = obj.getJSONObject(i);
			String name = data.getString("name").replace("@font", "").replace("#font", "");
			System.out.println("微信名称:" + name);
			String certifiedText = data.getString("certifiedText");
			if (certifiedText == null || certifiedText.equals("")) {
				certifiedText = "";
			} else {
				certifiedText = certifiedText.split("：")[1];
			}
			System.out.println("微信认证:" + certifiedText);
			String headImageUrl = data.getString("headImageUrl");
			if (headImageUrl == null || headImageUrl.equals("")) {
				headImageUrl = "";
			}
			System.out.println("头像:" + headImageUrl);
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
		}
	}
}
