package com.wb.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONObject;

public class TestRegex {

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

	/**
	 * 配置正则
	 * 
	 * @param urlPattern
	 * @param url
	 * @return
	 */
	public static boolean CatchRegEx(String urlPattern, String url) {
		Pattern itemPattern = Pattern.compile(urlPattern);
		if (itemPattern.matcher(url).matches()) {
			return true;
		}

		return false;
	}

	// Unicode转中文
	public static String decodeUnicode(final String dataStr) {
		int start = 0;
		int end = 0;
		final StringBuffer buffer = new StringBuffer();
		while (start > -1) {
			end = dataStr.indexOf("\\u", start + 2);
			String charStr = "";
			if (end == -1) {
				charStr = dataStr.substring(start + 2, dataStr.length());
			} else {
				charStr = dataStr.substring(start + 2, end);
			}
			char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
			buffer.append(new Character(letter).toString());
			start = end;
		}
		return buffer.toString();
	}
	//获取字符串中数字
	public static String sendNumberReg(String text) {
		// String a="cdf-capital月发文&nbsp;5&nbsp;篇";
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(text);
		return m.replaceAll("").trim();
	}

	public static void main(String[] args) {
	}
//去除文本中emjoy表情
	public static String emjoy(String content) {
		if (StringUtils.isNotBlank(content)) {
			return content.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*").replaceAll("'", "’");
		} else {
			return content.replaceAll("'", "’");
		}
		// String text =
		// content.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]",
		// "*").replaceAll("'", "*");
		// return text;
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
	public static String TimeStamp2Date(String timestampString, String formats) {
		if (TextUtils.isEmpty(formats))
			formats = "yyyy-MM-dd HH:mm:ss";
		Long timestamp = Long.parseLong(timestampString) * 1000;
		String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
		return date;
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

	public static String spiltRule(String rule, Document doc) {
		String rules[] = rule.split("%");
		String content = "";
		if (content.equals("")) {
			for (String string : rules) {
				content = doc.select(string).text();
			}
		}
		return content;
	}
//毫秒时间转时间戳
	public static String Millstotime(long birth) {
		SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(birth);
		Date date = c.getTime();
		return ss.format(date);
	}
//判断json格式是否正确
	public static boolean isJson(String content) {
		try {
			JSONObject jsonStr = JSONObject.parseObject(content);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}