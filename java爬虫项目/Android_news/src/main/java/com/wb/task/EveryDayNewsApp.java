package com.wb.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.kevin.crop.R.string;
import com.wb.pojo.News;
import com.wb.util.ChangeCharset;
import com.wb.util.JsoupClient;
import com.wb.util.KafkaUtils;
import com.wb.util.TestRegex;
//天天快報
public class EveryDayNewsApp {
	static int num = 2;
	static JsoupClient client = new JsoupClient();

	public static void main(String[] args) throws IOException, InterruptedException {
		String type = "天天快报APP";
		String key = "小米科技";
		String website = "https://kuaibao.qq.com/---app";
		ChangeCharset charsert = new ChangeCharset();
		long waitLoadBaseTime = 2000;
		int waitLoadRandomTime = 3000;
		Random random = new Random(System.currentTimeMillis());
		long t1 = System.currentTimeMillis();
		long t2 = System.currentTimeMillis();
		long t3 = System.currentTimeMillis();
			String word = charsert.toutf8(key);
			for (int y = 1; y <= num; y++) {
				String jsonurl = "https://r.cnews.qq.com/searchByType";
				// String json = getJson(word, y, t1, t2, t3);
				String json = "REQBuildTime=" + t1
						+ "&adcode=110105&ssid=HUAWEI-9XGJ3R_5G&omgid=6f89832bb9588e4ac9195c5117287bc22e820010211708&source=&REQExecTime="
						+ t2
						+ "&qqnetwork=wifi&commonsid=914d87dd31994fcb847ca346a563a2e6&curTab=kuaibao&kingCardType=0&picSizeMode=0&commonGray=1_3%7C2_0%7C18_2%7C12_1%7C38_3%7C22_1%7C14_0%7C17_0%7C35_0%7C30_0%7C19_1&currentTab=kuaibao&is_wap=0&lastCheckCardType=1&omgbizid=da30c51576a46442dee8a547ad4d60c3273a0080211918&type=pictext&page="
						+ y + "&imsi=460011610105207&commonIsFirstLaunch=0&bssid=0c%3A8f%3Aff%3Acb%3Aa6%3Aa0&query="
						+ word + "&muid=49392155873350634&activefrom=icon&curChannel=daily_timeline&unixtimesign=" + t3
						+ "&qimei=862609033854519&Cookie=%26access_token%3D11_OlkqxwCH0pOOpaieSL5_JIIQe_9hvCvnRutAfsUeOoTlJhpVVnckK6_W-VwKFRQBr70i0OhBqQRirVTmY25NW3167B7OG8sEYYhmH5AAQNg%26openid%3Doijc7uOFia-h6a_gFOqft2tNHals%26refresh_token%3D11_OlkqxwCH0pOOpaieSL5_JLtr2NGZEeJH8cURNi_yV3sdeUvBv5-rsGEbW7rCnEEP2a5nVsiRfv1FAhemMv-cr01DIYyJKy4qtKOl3GICz3s%26appid%3Dwxe90c9765ad00e2cd%26logintype%3D1&chlid=&rawQuery=&imsi_history=460011610105207%2C0%2C460011610105207%2C0%2C460011610105207%2C0%2C460011610105207&qn-sig=1b40f6d66e3e1030b680e103d7252f7b&qn-rid=44df5c97-b3e3-4fe3-83fe-00eb8a2bba12&hw_fp=OPPO%2FR9m%2FR9%3A5.1%2FLMY47I%2F1515760704%3Auser%2Frelease-keys&mid=e6b8a0943d47c1244a8199e54199509f18fcb5b4&devid=862609033854519&store=60009&screen_height=1920&apptype=android&origin_imei=862609033854519&hw=OPPO_OPPOR9m&appversion=4.8.40&appver=22_areading_4.8.40&uid=2d423e3849336f10&screen_width=1080&sceneid=&android_id=2d423e3849336f10";
				String body = new JsoupClient().getPostEveryDay(jsonurl, word, json);
				System.out.println(body);
				try {
					JSONObject jso = JSON.parseObject(body);
					System.out.println(jso);
					parseJson(jso, word, 0, website);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
	}

//	public static String getJson(String word, int y, long t1, long t2, long t3) {
//		String array[] = { "REQBuildTime=" + t1
//				+ "&adcode=110105&ssid=HUAWEI-9XGJ3R_5G&omgid=6f89832bb9588e4ac9195c5117287bc22e820010211708&source=&REQExecTime="
//				+ t2
//				+ "&qqnetwork=wifi&commonsid=914d87dd31994fcb847ca346a563a2e6&curTab=kuaibao&kingCardType=0&picSizeMode=0&commonGray=1_3%7C2_0%7C18_2%7C12_1%7C38_3%7C22_1%7C14_0%7C17_0%7C35_0%7C30_0%7C19_1&currentTab=kuaibao&is_wap=0&lastCheckCardType=1&omgbizid=da30c51576a46442dee8a547ad4d60c3273a0080211918&type=pictext&page="
//				+ y + "&imsi=460011610105207&commonIsFirstLaunch=0&bssid=0c%3A8f%3Aff%3Acb%3Aa6%3Aa0&query=" + word
//				+ "&muid=49392155873350634&activefrom=icon&curChannel=daily_timeline&unixtimesign=" + t3
//				+ "&qimei=862609033854519&Cookie=%26access_token%3D11_OlkqxwCH0pOOpaieSL5_JIIQe_9hvCvnRutAfsUeOoTlJhpVVnckK6_W-VwKFRQBr70i0OhBqQRirVTmY25NW3167B7OG8sEYYhmH5AAQNg%26openid%3Doijc7uOFia-h6a_gFOqft2tNHals%26refresh_token%3D11_OlkqxwCH0pOOpaieSL5_JLtr2NGZEeJH8cURNi_yV3sdeUvBv5-rsGEbW7rCnEEP2a5nVsiRfv1FAhemMv-cr01DIYyJKy4qtKOl3GICz3s%26appid%3Dwxe90c9765ad00e2cd%26logintype%3D1&chlid=&rawQuery=&imsi_history=460011610105207%2C0%2C460011610105207%2C0%2C460011610105207%2C0%2C460011610105207&qn-sig=1b40f6d66e3e1030b680e103d7252f7b&qn-rid=44df5c97-b3e3-4fe3-83fe-00eb8a2bba12&hw_fp=OPPO%2FR9m%2FR9%3A5.1%2FLMY47I%2F1515760704%3Auser%2Frelease-keys&mid=e6b8a0943d47c1244a8199e54199509f18fcb5b4&devid=862609033854519&store=60009&screen_height=1920&apptype=android&origin_imei=862609033854519&hw=OPPO_OPPOR9m&appversion=4.8.40&appver=22_areading_4.8.40&uid=2d423e3849336f10&screen_width=1080&sceneid=&android_id=2d423e3849336f10" };
//		String random = "";
//		int index = (int) (Math.random() * array.length);
//		random = array[index];
//		return random;
//	}

	private static void parseJson(JSONObject obj, String word, int cid, String website) {
		int secCount = obj.getJSONObject("new_list").getInteger("secCount");
		num = (int) Math.ceil(secCount / 10.0);
		JSONArray array = obj.getJSONObject("new_list").getJSONArray("data");
		for (int i = 0, len = array.size(); i < len; i++) {
			String title = array.getJSONObject(i).getJSONObject("article").getString("title");
			System.out.println("标题:" + title);
			String time = array.getJSONObject(i).getJSONObject("article").getString("time");
			System.out.println("时间:" + time);
			String source = array.getJSONObject(i).getJSONObject("article").getString("source");
			System.out.println("来源:" + source);
			String logo = array.getJSONObject(i).getJSONObject("article").getString("chlicon");
			System.out.println("头像链接:" + logo);
			String url = array.getJSONObject(i).getJSONObject("article").getString("url");
			System.out.println("网址链接:" + url);
			Document doc = client.DownHtml(url);
			String content = doc.select("div.content-box").text();
			System.out.println("正文:" + content);
			Elements select = doc.select("div.content-box > p> img");
			String imgurl = "";
			for (Element element : select) {
				imgurl = imgurl + element.attr("src") + ",";
			}
			System.out.println("imgurl:" + imgurl);
			if (title.contains(word) || content.contains(word)) {
					News news = new News();
					news.setCid(cid);
					news.setRtime(time);
					news.setLink(url);
					news.setTitle(title);
					news.setReid(source);
					news.setReidsrc(logo);
					news.setImages(imgurl);
					news.setBody(content);
					news.setWebsite(website);
					String msg = JSON.toJSONString(news);
					System.out.println(msg);
//					KafkaUtils.sendMsgToKafka(msg);
				}
			}
	}
}