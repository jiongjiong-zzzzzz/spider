package com.wb.task;

import static org.junit.Assume.assumeFalse;

import java.io.IOException;
import java.util.ArrayList;

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

//新浪新聞
public class SinaNewsApp {
	static int num = 2;
	static int page = 1;
	static JsoupClient client = new JsoupClient();

	public static void main(String[] args) throws IOException {
		int rand = (int) ((Math.random() * 9 + 1) * 100);
		String website = "https://weibo.com/";
		ChangeCharset charsert = new ChangeCharset();
		String word = charsert.toutf8("小米科技");
		for (int y = 1; y <= num; y++) {

			// 可替换参数 page keyword rand
			String json = "http://newsapi.sina.cn/?resource=hbpage&newsId=HB-1-snhs/index-search&lq=1&page=1&newpage=0&keyword=%E8%85%BE%E8%AE%AF&lDid=1ed7fa45-4078-49b0-b596-dfa08a034803&oldChwm=14170_0001&city=WMXX2972&loginType=1&authToken=ecb21c998b1d508f22693fa6af1fa85e&link=&authGuid=6415860192466189701&ua=OPPO-OPPO+R9m__sinanews__6.9.8__android__5.1&deviceId=1f984fdbff91238c&connectionType=2&resolution=1080x1920&mac=c0%3A9f%3A05%3A48%3A06%3A11&weiboUid=3114128891&osVersion=5.1&chwm=14170_0001&weiboSuid=29963141bf&andId=2d423e3849336f10&from=6069895012&sn=5PW8KFSO99999999&aId=01AjTIBIz0RYamzzD-_tWl-l-SfYObkgLW0f8JqQarr7Sbgd0.&deviceIdV1=1f984fdbff91238c&osSdk=22&abver=1532097391698&accessToken=2.002LZk5De3vYNC87d99904b4lhWpWC&seId=005e6aa3c0&imei=862609033854519&deviceModel=OPPO__OPPO__OPPO+R9m&location=39.910987%2C116.598464&authUid=6422374486829839649&urlSign=67f2b22854&rand=96";
			String body = new JsoupClient().getHtmlPageSina(json);
			System.out.println(body);
			JSONObject jso = JSON.parseObject(body);// json字符串转换成jsonobject对象
			System.out.println("解析:" + jso);
			JSONArray array = jso.getJSONObject("data").getJSONObject("list").getJSONArray("feed");
			for (int i = 0, len = array.size(); i < len; i++) {
				String title = array.getJSONObject(i).getString("title");
				System.out.println("标题:" + title);
				String url = array.getJSONObject(i).getString("url");
				System.out.println("网址链接:" + url);
				//解析内容页，具体静态页面直接解析即可，新浪的页面比较复杂，规则多样，所以没来的及写
				Document doc = client.DownHtml2(url);
			}
			JSONArray array2 = jso.getJSONObject("data").getJSONArray("kandian");
			for (int j = 0, len2 = array.size(); j < len2; j++) {
				String title = array2.getJSONObject(j).getString("title");
				System.out.println("标题:" + title);
				String url = array2.getJSONObject(j).getString("url");
				System.out.println("网址链接:" + url);
				//解析内容页，具体静态页面直接解析即可，新浪的页面比较复杂，规则多样，所以没来的及写
				Document doc = client.DownHtml2(url);
			}
		}
	}
}