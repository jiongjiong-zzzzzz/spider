package com.wb.util;

import static org.hamcrest.CoreMatchers.containsString;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.bcel.generic.NEW;
import org.apache.commons.codec.digest.DigestUtils;
import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kevin.crop.R.string;

import net.sourceforge.htmlunit.cyberneko.HTMLEventInfo.SynthesizedItem;

public class Test {
	public static String buildSign() {
		String sign = "";
		try {
			Class<?> signUtils = Class.forName("com.netease.freecrad.util.SignUtils");
			Object obj = signUtils.newInstance();
			String param1 = "MIIBSwIBADCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoEFgIUC1EHpnRseNKw/EePxkCL4S4J8HM=";
			TreeMap<String, String> localTreeMap = new TreeMap<String, String>();
			localTreeMap.put("channelId", "VDEzNDg2NDc5MDkxMDc=");
			localTreeMap.put("msgId", UUID.randomUUID().toString());
			localTreeMap.put("userId", "");
			localTreeMap.put("openType", "1");
			localTreeMap.put("message", "");
			localTreeMap.put("expandParams", "");
			Method buildSignF = signUtils.getDeclaredMethod("buildSign", String.class, Map.class);
			sign = buildSignF.invoke(obj, param1, localTreeMap).toString();
			System.out.println(sign);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sign;
	}

	public static String buildSign2() {
		String href = "https://c.m.163.com/news/a/D18J6BQ6002580S6.html";
		String dicid = href.substring(href.indexOf("a/") + 2, href.indexOf(".html"));
		return dicid;

	}

	public static void main(String[] args) throws Exception {

		// ChangeCharset charsert = new ChangeCharset();
		// String BASE64 = TestRegex.editBase64("腾讯");
		// String word = charsert.togb2312(BASE64);
		// String json = "https://c.m.163.com/search/comp2/Kg%3D%3D/20/" + word
		// +
		// ".html?deviceId=a3ITxH4%2Fwa8V5h8hViBMvQ%3D%3D&version=newsclient.39.1.android&channel=VDEzNDg2NDc5MDkxMDc%3D&canal=b3Bwb19zdG9yZTIwMTRfbmV3cw%3D%3D&dtype=0&tabname=wangyihao&position=5pCc57Si5Y6G5Y%2By&ts="
		// + TestRegex.DateToUnixtime() + "&sign=" + charsert.toutf8(buildSign2()) +
		// "&spever=FALSE";
		// String body = new JsoupClient().getHtmlPageWangYi(json);
		// System.out.println(buildSign2());
		// String time="2018-05-22T05:12:15.271+0800";
		// SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		// SimpleDateFormat sim2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// Date l=sim.parse(time);
		// String format = sim2.format(l);
		// System.out.println(format);
		String url="http://api.k.sohu.com/api/paper/view.do?m=news&newsId=232792037&sid=2";
		Pattern pattern = Pattern.compile("\\d{8,9}");
		Matcher matcher = pattern.matcher(url);
		String newsId="";
		while (matcher.find()) {
			System.out.println(matcher.group(0));
			newsId=matcher.group(0);
		}
		System.out.println(newsId);

	}

	/*
	 * public static void main(String[] args) { String[] arr = { "辉立证券:4.5",
	 * "耀才证券:1.0", "海通证券:0.63", "光大新鸿基:0.6", "信诚证券:0.3", "时富金融:0.22", "富途证券:0.2",
	 * "敦沛金融:0.15", "国泰君安:0.13", "尊嘉证券:0.4" }; List<String> list = new ArrayList();
	 * list.add("辉立证券:3.0"); list.add("耀才证券:0.8"); list.add("海通证券:0.63");
	 * list.add("光大新鸿基:0.6"); list.add("信诚证券:0.3"); list.add("宝盛证券:0.01");
	 * List<String> list2 = new ArrayList(); for (int j = 0; j < arr.length; j++) {
	 * list2.add(arr[j]); } System.out.println(unionSet(list, list2)); Set set =
	 * unionSet(list, list2); Map<String, Double> map = new HashMap<String,
	 * Double>(); for (Object object : set) { String key =
	 * object.toString().split(":")[0]; Double value =
	 * Double.parseDouble(object.toString().split(":")[1]); if
	 * (map.containsKey(key)) { if (map.get(key) > value) { map.put(key,
	 * map.get(key)); } else { map.put(key, value); } } else { map.put(key, value);
	 * } } }
	 * 
	 * public static Set unionSet(List setA, List setB) { Set unionSet = new
	 * HashSet(); Iterator iterA = setA.iterator(); unionSet.addAll(setB); while
	 * (iterA.hasNext()) { Object tempInner = iterA.next(); if
	 * (!setB.contains(tempInner)) { unionSet.add(tempInner); } } return unionSet; }
	 */
}
