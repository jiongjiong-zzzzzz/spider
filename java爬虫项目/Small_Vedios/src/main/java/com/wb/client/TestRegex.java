package  com.wb.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRegex {

	/** 
	 * (1)能匹配的年月日类型有： 
	 *    2014年4月19日 
	 *    2014年4月19号 
	 *    2014-4-19 
	 *    2014/4/19 
	 *    2014.4.19 
	 * (2)能匹配的时分秒类型有： 
	 *    15:28:21 
	 *    15:28 
	 *    5:28 pm 
	 *    15点28分21秒 
	 *    15点28分 
	 *    15点 
	 * (3)能匹配的年月日时分秒类型有： 
	 *    (1)和(2)的任意组合，二者中间可有任意多个空格 
	 * 如果dateStr中有多个时间串存在，只会匹配第一个串，其他的串忽略 
	 * @param text 
	 * @return 
	 */  
	public static String  dataRegular(String time) {
		Matcher matcher = null;
		Pattern pattern = null;
		pattern = Pattern
				.compile("(\\d{1,4}[-|\\/|年|\\.]\\d{1,2}[-|\\/|月|\\.]\\d{1,2}([日|号])?(\\s)*(\\d{1,2}([点|时])?((:)?\\d{1,2}(分)?((:)?\\d{1,2}(秒)?)?)?)?(\\s)*(PM|AM)?)");
		matcher = pattern.matcher(time);
		
		while (matcher.find()) {
			time = matcher.group();
		}
		return time;
	}
	public static String replaceData(String text){
		if(text.contains("'")){
			text = text.replace("\'", "\"");
		}else if(text.contains("&nbsp;")){
			text = text.replace("&nbsp;", " ");
		}
		text = replaceBlank(text);
		return text;
	}
	public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest.trim();
	}
	public static String sendNumberReg(String text) {
		String regEx="[^0-9]";  
		Pattern p = Pattern.compile(regEx);  
		Matcher m = p.matcher(text);  
		return m.replaceAll("").trim();
	}
	public static void main(String[] args) {
		String time="2018年6月4日 星期 一";
		time=TestRegex.dataRegular(time).replaceAll("年", "-").replaceAll("月", "-").replaceAll("日", "")+"00:00:00";
		System.out.println(time);
	}
}