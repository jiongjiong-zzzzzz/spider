package com.combanc.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Title:           RegexChar
 * Description:     匹配字符串数字
 * Company:         combanc
 * Author:          shihw
 * Date:            2018/7/13
 * JDK:             1.8
 * Encoding:        UTF-8
 */
public class RegexChar {

	public static String numRegular(String text) {

		Pattern pattern = Pattern.compile("(\\d+)");
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			text = matcher.group();
		}
		
		return text;
	}	
	
	
	
	public static void main(String[] args) {
		String text = "zlzp.PopupDiv.allIns['buttonSelJobType'].fnPopupChildren(this,['4010200','销售业务'])";
		System.out.println(numRegular(text));
	}
	
}