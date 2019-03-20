package com.wb.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.util.TextUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONObject;
import com.sun.jna.platform.win32.WinDef.LONG;

public class TestRegex {

	/**
	 * (1)能匹配的年月日类型有： 2014年4月19日 2014年4月19号 2014-4-19 2014/4/19 2014.4.19
	 * (2)能匹配的时分秒类型有： 15:28:21 15:28 5:28 pm 15点28分21秒 15点28分 15点 (3)能匹配的年月日时分秒类型有：
	 * (1)和(2)的任意组合，二者中间可有任意多个空格 如果dateStr中有多个时间串存在，只会匹配第一个串，其他的串忽略
	 * 
	 * @param text
	 * @return
	 */
	public static String dataRegular(String time) {
		Matcher matcher = null;
		Pattern pattern = null;
		pattern = Pattern.compile(
				"(\\d{1,4}[-|\\/|年|\\.]\\d{1,2}[-|\\/|月|\\.]\\d{1,2}([日|号])?(\\s)*(\\d{1,2}([点|时])?((:)?\\d{1,2}(分)?((:)?\\d{1,2}(秒)?)?)?)?(\\s)*(PM|AM)?)");
		matcher = pattern.matcher(time);

		while (matcher.find()) {
			time = matcher.group();
		}
		return time;
	}

	public static String dataRegular2(String time) throws ParseException {
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		SimpleDateFormat sim2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date l = sim.parse(time);
		String format = sim2.format(l);
		return format;
	}

	public static String replaceData(String text) {
		if (text.contains("'")) {
			text = text.replace("\'", "\"");
		} else if (text.contains("&nbsp;")) {
			text = text.replace("&nbsp;", " ");
		}
		text = replaceBlank(text);
		return text;
	}

	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest.trim();
	}

	public static String sendNumberReg(String text) {
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(text);
		return m.replaceAll("").trim();
	}

	/**
	 * 判断json格式是否正确
	 * 
	 * @param content
	 * @return
	 */
	public static boolean isJson(String content) {
		try {
			JSONObject jsonStr = JSONObject.parseObject(content);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 生成token
	 * 
	 * @return
	 */
	public static String GetGUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * java生成随机数字和字母组合
	 * 
	 * @param length[生成随机数的长度]
	 * 
	 */
	public static String getCharAndNumr(int length) {
		String val = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			// 输出字母还是数字
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			// 字符串
			if ("char".equalsIgnoreCase(charOrNum)) {
				// 取得大写字母还是小写字母
				// int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
				int choice = 97;
				val += (char) (choice + random.nextInt(26));
			} else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val;
	}

	/**
	 * 生成指定字符串的hash值 A hashing method that changes a string (like a URL) into a hash
	 * suitable for using as a disk filename.
	 */
	public static String hashKeyForDisk(String key) {
		String cacheKey;
		try {
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(key.getBytes());
			cacheKey = bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(key.hashCode());
		}
		return cacheKey;
	}

	private static String bytesToHexString(byte[] bytes) {
		// http://stackoverflow.com/questions/332079
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	/**
	 * Java将Unix时间戳转换成指定格式日期字符串
	 * 
	 * @param timestampString
	 *            时间戳 如："1473048265";
	 * @param formats
	 *            要格式化的格式 默认："yyyy-MM-dd HH:mm:ss";
	 *
	 * @return 返回结果 如："2016-09-05 16:06:42";
	 */
	public static String TimeStamp2Date(String timestampString) {
		String formats = "";
		if (TextUtils.isEmpty(formats))
			formats = "yyyy-MM-dd HH:mm:ss";
		Long timestamp = Long.parseLong(timestampString) * 1000;
		String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
		return date;
	}

	public static Date StringToDate(String timestampString) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = sdf.parse(timestampString);
		return date;
	}

	// 输入毫秒数，转化为日期，用simpleDateFormat + Date 方法；
	/**
	 * 直接用SimpleDateFormat格式化 Date对象，即可得到相应格式的日期 字符串。
	 */
	public static String Millstotime(long birth) {

		// SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd
		// HH:mm:ss");
		// long a = time + 5000 * 1000;
		// Date date = new Date();
		// date.setTime(a);
		// return simpleDateFormat.format(simpleDateFormat.format(date));

		// Calendar calendar2 = Calendar.getInstance();
		// calendar2.setTimeInMillis(time);
		// int year = calendar2.get(Calendar.YEAR);
		// int month = calendar2.get(Calendar.MONTH);
		// int day = calendar2.get(Calendar.DAY_OF_MONTH);
		// int hour = calendar2.get(Calendar.HOUR_OF_DAY);// 24小时制
		// // int hour = calendar2.get(Calendar.HOUR);//12小时制
		// int minute = calendar2.get(Calendar.MINUTE);
		// int second = calendar2.get(Calendar.SECOND);
		// return year + "-" + (month + 1) + "-" + day + " " + hour + ":" + minute + ":"
		// + second;
		SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(birth);
		Date date = c.getTime();
		return ss.format(date);
	}

	public static Long DateToUnixtime() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 1361325960
		String time = df.format(new Date());
		long epoch = 0;
		try {
			epoch = df.parse(time).getTime();
			System.out.println("should be 1361325960 ：" + epoch);
			Date d = new Date();
			String t = df.format(d);
			epoch = df.parse(t).getTime() / 1000;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return epoch;
	}

	/**
	 * 字符串--base64
	 * 
	 * @return
	 */
	public static String editBase64(String text) {
		final Base64.Encoder encoder = Base64.getEncoder();
		byte[] textByte = null;
		try {
			textByte = text.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 编码
		final String encodedText = encoder.encodeToString(textByte);
		System.out.println(encodedText);
		return encodedText;
	}

	public static String editBase642(String str, Map<String, String> map) {
		String text = "";
		for (Map.Entry<String, String> entry : map.entrySet()) {
			text = text + entry.getValue() + "/";
		}
		text = str + "/" + text;
		final Base64.Encoder encoder = Base64.getEncoder();
		byte[] textByte = null;
		try {
			textByte = text.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 编码
		final String encodedText = encoder.encodeToString(textByte);
		System.out.println(encodedText);
		return encodedText;
	}

	/**
	 * base64--字符串
	 * 
	 * @return
	 */
	public static String parseBase64(String encodedText) {
		final Base64.Decoder decoder = Base64.getDecoder();
		String parse = null;
		try {
			parse = new String(decoder.decode(encodedText), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return parse;
	}

	// 加密后解密
	public static String JM(String inStr) {
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String k = new String(a);
		return k;
	}

	public static String KL(String inStr) {
		// String s = new String(inStr);
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String s = new String(a);
		return s;
	}

	/**
	 * 生成随即密码
	 * 
	 * @param pwd_len
	 *            生成的密码的总长度
	 * @return 密码的字符串
	 */
	public static String genRandomNum(int pwd_len) {
		// 35是因为数组是从0开始的，26个字母+10个数字
		final int maxNum = 36;
		int i; // 生成的随机数
		int count = 0; // 生成的密码的长度
		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
				't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < pwd_len) {
			// 生成随机数，取绝对值，防止生成负数，
			i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1
			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}
		return pwd.toString();
	}

	public static String spiltRule(String rule, Document doc) {
		String rules[] = rule.split("%");
		String content = "";
		for (String string : rules) {
			if (content.equals("")) {
				content = doc.select(string).text();
			}
		}
		return content;
	}
	public static String spiltRuleimg(String rule, Document doc) {
		String rules[] = rule.split("%");
		Elements select = null;
		String imgs = "";
		if (select == null) {
			for (String string : rules) {
				select = doc.select(string);
				for (Element element : select) {
					if (!element.attr("src").contains("http")) {
						imgs = imgs + "http:" + element.attr("src") + ",";
					} else {
						imgs = imgs + element.attr("src") + ",";
					}
				}
			}
		}
		return imgs;
	}
	public static String emjoy(String content) {
		// ȥemoji
		String text = content.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*");
		return text;
	}

	public static void main(String[] args) throws ParseException, UnsupportedEncodingException {
		// String time = "2018年6月4日 星期 一";
		// time = TestRegex.dataRegular(time).replaceAll("年", "-").replaceAll("月",
		// "-").replaceAll("日", "") + "00:00:00";
		// System.out.println(JM(KL("xnMcjucGRxumvEwXJ9vcEvRlLuyJ0ynL4DKpT7GmHfF48ErR02zJ6/KXOnxX046I")));
		System.out.println(editBase64(("百度")));
		System.out.println(parseBase64("55m+5bqm"));
		// Date date = new Date();
		// System.out.println(DigestUtils.md5Hex(("57qi5p2J6LWE5pys")) +
		// DigestUtils.md5Hex(("17 Jul 2018 13:28:54 GMT")));
		// System.out.println(date.toGMTString());
		System.out.println(genRandomNum(10));
	}
}