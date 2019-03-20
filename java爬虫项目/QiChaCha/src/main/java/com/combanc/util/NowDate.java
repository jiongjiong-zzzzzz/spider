package com.combanc.util;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Title:           NowDate
 * Description:     获取当前时间
 * Company:         combanc
 * Author:          shihw
 * Date:            2018/5/25
 * JDK:             1.8
 * Encoding:        UTF-8
 */
public class NowDate {
		
	/**
	 * 获取当前时间
	 * @return yyyy-MM-dd HH:mm:ss 格式
	 */
	public static String createDate(){
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}
	
	/**
	 * 获取当前时间
	 * @return yyyy-MM-dd 格式
	 */
	public static String createDateY(){
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}
	
	/**
	 * 获取当前时间
	 * @return yyyy-MM 格式
	 */
	public static String createDateM(){
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		return format.format(date);
	}
	
	public static void main(String[] args) {
		Double num =Math.ceil(6/3);
		System.out.println(num.intValue());
	}
}
