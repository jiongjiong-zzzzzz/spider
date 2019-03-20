package org.webmaic.example.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * lishunsheng
 * 27.09.2018
 */
public class MatcherUtil {

    public final static String PARENTHESES_CONTENT = "(?<=\\().*(?=\\))";
    public final static String CHINESE_CONTENT =  "[\\u4e00-\\u9fa5]";
    public final static String NUMBER_CONTENT =  "[0-9]";

    /**
     * 只取括号内JSON
     * @param str
     * @return
     */
    public static String aliJsonMatcher(String str){
        Pattern r = Pattern.compile(PARENTHESES_CONTENT);
        Matcher m = r.matcher(str);
        while(m.find()){
            System.out.println(m.group());
            str = m.group();
        }
        return str;
    }

    public static String generalMatcher(String str,String pattern,Integer groupId){
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        while(m.find()){
                str = m.group(groupId);
        }
        return str;
    }
    public static String generalMatcher(String str,String pattern){
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        while(m.find()){
                str = m.group();
        }
        return str;
    }
}
