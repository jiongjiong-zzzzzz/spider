package com.combanc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
/**
 * Title:           RegexDate
 * Description:     匹配时间
 * Company:         combanc
 * Author:          shihw
 * Date:            2018/6/6
 * JDK:             1.8
 * Encoding:        UTF-8
 */
public class RegexDate {
	private static Logger logger = Logger.getLogger(RegexDate.class);
	/** 
	 * (1)能匹配的年月日类型有： 
	 *    2018年6月6日 
	 *    2018年6月6号
	 *    2018-6-6 
	 *    2018/6/6
	 *    2018.6.6
	 *     
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
	 * @return 时间
	 */  
	public static String dateRegular(String time) {
		if(time==null){
			return null;
		}
		try {
			Pattern pattern = Pattern.compile(
					"(\\d{1,4}[-|\\/|年|\\.]?\\d{1,2}[-|\\/|月|\\.]\\d{1,2}([日|号])?(\\s)*(\\d{1,2}([点|时])?((:)?\\d{1,2}(分)?((:)?\\d{1,2}(秒)?)?)?)?(\\s)*(PM|AM)?)");
			Matcher matcher = pattern.matcher(time);
			while (matcher.find()) {
				time = matcher.group();
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("时间匹配错误："+time,e);
		}
//		if(time.contains(" ")){//这个空格貌似是假的
//			time = time.replace(" ", "");
//		}
		try {
			if(time.length()==5){
				String nowDate = NowDate.createDate();
				String[] splitDate = nowDate.split(" ");
				String[] times = splitDate[0].split("-");
				String publication_date = times[0] +"-" + time;
				if(Integer.valueOf(splitDate[0].replace("-", ""))<Integer.valueOf(publication_date.replace("-", ""))){
					time = (Integer.valueOf(times[0])-1) +"-" + time + " " +splitDate[1];
				}else{
					time = publication_date + " " +splitDate[1];
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("时间错误："+time,e);
		}
		
		
		return time;
	}
	
	
	
	/**
	 * 格式化拉钩时间
	 * @param time
	 * @return yyyy-MM-dd HH:mm:ss 格式
	 */
	public static String date(String time) {
		if(time.contains("发布于拉勾网")){
			time = time.replace("发布于拉勾网", "").trim();
			if(time.contains(" ")){//这个空格貌似是假的
				time = time.replace(" ", "");
			}
			if(time.length()==3){
				if(time.contains("天前")){
					int day = Integer.valueOf(time.substring(0, 1));
					time = getTimeByDay(0-day);
				}
			}else if(time.length()==5){
				time = formatByhm(time);
			}else if(time.length()==10){
				time = formatByymd(time);
			}
		}else{
			time = dateRegular(time);
		}
		return time;
	}
	
	
    /**
     * 获取当前时间之前或之后几小时 hour
     * @param day 复数代表之前  正数代表之后
     * @return yyyy-MM-dd HH:mm:ss 格式
     */
    public static String getTimeByHour(int hour) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + hour);

        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());

    }

    /**
     * 获取当前时间之前或之后几分钟 minute
     * @param day 复数代表之前  正数代表之后
     * @return yyyy-MM-dd HH:mm:ss 格式
     */
    public static String getTimeByMinute(int minute) {

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MINUTE, minute);

        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());

    }

    /**
     * 获取当前时间之前或之后几天 day
     * @param day 复数代表之前  正数代表之后
     * @return yyyy-MM-dd HH:mm:ss 格式
     */
    public static String getTimeByDay(int day) {

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DATE, day);

        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());

    }
	
	
    /**
     * 格式化 时分  
     * @param  time 10:00 
     * @return yyyy-MM-dd HH:mm:ss 格式
     */
    public static String formatByhm(String time) {

    	Date date = new Date();
    	
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
        
        time = sdf.format(date)+" "+time+":00";     
        
        return time;

    }
	
    /**
     * 格式化 年月日  
     * @param time  2018-6-24
     * @return yyyy-MM-dd HH:mm:ss 格式
     */
    public static String formatByymd(String time) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        
        try {
			Date date = sdf.parse(time);
			time = sdf1.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("时间格式不对",e);
		}  
        
        return time;

    }
	
//  public static void main(String[] args) {

      // 获取当前时间5分钟前的时间 格式yyyy-MM-dd HH:mm:ss

//      System.out.println(getTimeByMinute(-5));

      // 获取当前时间3小时后的时间 格式yyyy-MM-dd HH:mm:ss

//      System.out.println(getTimeByHour(3));

      // 获取当前时间2天前的时间 格式yyyy-MM-dd HH:mm:ss
//	  String time = "09-20发布";
//	  System.out.println(time);
//      System.out.println(dateRegular(time));
//	  String a = "09:20 ";
//	  if(a.contains(" ")){
//		  System.out.println(1);
//	  }else if(a.contains(" ")){
//		  System.out.println(2);
//	  }else if(a.contains(" ")){
//		  System.out.println(3);
//	  }
//	  System.out.println(a.replaceAll("\\s+", ""));
//	  System.out.println("09:20 发布于拉勾网");
//	  String date = date("09:20 发布于拉勾网");
//	  System.out.println(date);
//  }
}