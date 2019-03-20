package com.wb.task;

import static org.junit.Assume.assumeFalse;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.print.Doc;

import org.apache.commons.codec.digest.DigestUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.kevin.crop.R.string;
import com.wb.pojo.Account;
import com.wb.pojo.News;
import com.wb.util.ChangeCharset;
import com.wb.util.JsoupClient;
import com.wb.util.KafkaUtils;
import com.wb.util.TestRegex;
//猫扑论坛
public class MaoPuNewsApp {
	static int num = 3;
	static JsoupClient client = new JsoupClient();

	public static void main(String[] args) throws IOException, ParseException {
		for (int y = 1; y <= num; y++) {
			System.out.println("小米科技");
			getCatPaper("小米科技", 0, y);
		}	
	}

	public static void getCatPaper(String word, int cid, int page) throws IOException, ParseException {
		String website = "http://mtt.mop.com/---APP";
		ChangeCharset charsert = new ChangeCharset();
		String words = charsert.toutf8(word);
		try {
			String json = "http://rest3g.mop.com/search.json?keyword=" + words + "&pageNum=" + page;
			String body = client.getHtmlPageGet(json);
			System.out.println(body);
			if (TestRegex.isJson(body)) {
				JSONObject jso = JSON.parseObject(body);// json字符串转换成jsonobject对象
				System.out.println("解析:---" + jso);
				if (jso.getJSONObject("result").getInteger("totalCount") != 0) {
					JSONArray array = jso.getJSONObject("result").getJSONArray("subejcts");
					for (int i = 0; i < array.size(); i++) {
						String jsonUrl = array.getJSONObject(i).getString("jsonUrl");
						System.out.println("json"+jsonUrl);
						String body2 = client.getJsonUrl(jsonUrl);
						if (TestRegex.isJson(body2)) {
							try {
								JSONObject jso2 = JSON.parseObject(body2);
								System.out.println("---" + jso2);
								String content = "";
								String images = "";
								JSONArray arr = jso2.getJSONArray("resources");
								for (int g = 0; g < arr.size(); g++) {
									String con = arr.getJSONObject(g).getString("content");
									if (con.contains(".jpg") || con.contains(".png")) {
										images = images + con + ",";
									} else {
										content = content + con;
									}
								}
								String href = jso2.getString("url");
								String title = jso2.getString("title");
								String author = jso2.getString("username");
								String logo = jso2.getString("headurl");
								Long ctime = jso2.getLong("publishtime");
								String time = TestRegex.Millstotime(ctime);
								int userid = jso2.getInteger("articleid");
								String post = "http://staticize.mop.com/dzh/subject/v2.0/read/" + userid;
								String jsondata = "app_id=mop&imei=862609033854519&ime=862609033854519&softtype=community&softname=mopAndroid&appqid=mp_oppo180720&apptypeid=mop&ver=5.1.3&os=Android%205.1&appver=50103&deviceid=862609033854519&position=%E5%8C%97%E4%BA%AC%E5%B8%82&device=OPPO%20R9m&network=wifi&iswifi=wifi&platform=app&mtoken=&sign=c6b096f490f859a29218d5ae6f2f2517";
								String posyBody = client.getPostCat(post, jsondata);
								JSONObject sub = JSON.parseObject(posyBody);
								System.out.println(sub);
								int readNum = 0;
								int praisenum = 0;
								int recommendnum = 0;
								if (!sub.toString().contains("帖子不存在")) {
									readNum = sub.getJSONObject("data").getInteger("readnum");
									praisenum = sub.getJSONObject("data").getInteger("praisenum");
									recommendnum = sub.getJSONObject("data").getInteger("recommendnum");
								}
								System.out.println("标题:" + title);
								System.out.println("时间:" + time);
								System.out.println("内容:" + content);
								System.out.println("作者:" + author);
								System.out.println("logo:" + logo);
								System.out.println("href:" + href);
								System.out.println("图片:" + images);
								System.out.println("浏览数:" + readNum);
								System.out.println("点赞数:" + praisenum);
								System.out.println("评论数:" + recommendnum);
								if (title.contains((word)) || content.contains((word))) {
										News news = new News();
										news.setCid(cid);
										news.setRtime(time);
										news.setLink(href);
										news.setTitle(title);
										news.setImages(images);
										news.setBody(content);
										news.setReid(author);
										news.setReidsrc(logo);
										news.setPv(readNum);
										news.setWebsite(website);
										news.setConum(recommendnum);
										news.setLnum(praisenum);
										String msg = JSON.toJSONString(news);
										KafkaUtils.sendMsgToKafka(msg);
										//賬號
										getAccount(userid, time, author, logo);
									}
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static void getAccount(int userid, String time, String author, String logo)
			throws IOException, ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");// 设置日期格式
		String date = df.format(new Date());
		int did = Integer.parseInt(date);
		int tid = 10016;
		String account_id = String.valueOf(userid);
		String jsonURL = "http://rest3g.mop.com/hi/ugc/taInfo?userId=" + userid;
		String account = new JsoupClient().getHtmlPageGet(jsonURL);
		JSONObject jso_account = JSON.parseObject(account);
		String timeurl = "http://hi.mop.com/space/" + userid;
		Document a = JsoupClient.DownHtml(timeurl);
		String birth = a.select("div.hpUserInfo2 > p:nth-child(1) > span").text();
		SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd");
		Date aaaa = ss.parse(birth);
		java.sql.Date da = new java.sql.Date(aaaa.getTime());
		int fans = jso_account.getJSONObject("result").getJSONObject("user").getInteger("fansNum");
		int subjectNum = jso_account.getJSONObject("result").getJSONObject("user").getInteger("subjectNum");
		System.out.println("注册时间:" + da);
		System.out.println("粉丝:" + fans);
		System.out.println("发文数:" + subjectNum);
		Account acc = new Account();
		acc.setTid(tid);
		acc.setDid(did);
		acc.setAname(author);
		acc.setAccount_id(account_id);
		acc.setAlogo(logo);
		acc.setFans(fans);
		acc.setSnum(subjectNum);
		acc.setRtime(da);
		String msg = JSON.toJSONString(acc);
		KafkaUtils.sendMsgToKafkaAccount(msg);	
	}
}