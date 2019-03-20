package com.zxs.ssh.template.util;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Project Name:weibo-crawler
 * File Name:BlogParseUtil
 * Package Name:com.zxs.ssh.template.util
 * Date:2018/11/21
 * Author:zengxueshan
 * Description:新浪微博页面解析工具
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */


public class BlogParseUtil {

    /**
     * 表情字符正则表达式
     */
    private static final String EMOJI_REGULAR_EXPRESSION = "[\\ud83c\\udc00-\\ud83c\\udfff]|[\\ud83d\\udc00-\\ud83d\\udfff]|[\\u2600-\\u27ff]";

    /**
     * 获取文本内容、多媒体内容
     *
     * @param webElement        父节点
     * @param textCssSelector   文本内容CssSelector
     * @param mediaCssSelector  多媒体内容CssSelector
     * @return 文本内容、多媒体内容
     */
    public static String[] getContentHelp(WebElement webElement, String textCssSelector, String mediaCssSelector){
        String[] res = new String[2];
        for (int i = 0; i < res.length; i++) {
            res[i] = "";
        }
        WebElement webElement_text = null;
        try{
            if(!StringUtils.isEmpty(textCssSelector)){
                webElement_text = webElement.findElement(By.cssSelector(textCssSelector));
            }
            if(webElement_text != null){
                String text = webElement_text.getAttribute("textContent").trim().replace("\u200B","")+ " ";//替换无效字符&#8203
                text = filterEmoji(text.trim(),"");
                //获取所有子节点
                List<WebElement> webElementsTemp_2 = webElement_text.findElements(By.xpath("./*"));
                for (int j = 0; j < webElementsTemp_2.size(); j++) {
                    if(webElementsTemp_2.get(j).getTagName().equals("img")){
                        text = text + webElementsTemp_2.get(j).getAttribute("title") + " ";
                    }
                }
                res[0] = text.trim();
            }
        }catch (Exception e){

        }
        WebElement webElement_media = null;
        try{
            if(!StringUtils.isEmpty(mediaCssSelector)){
                webElement_media = webElement.findElement(By.cssSelector(mediaCssSelector));
            }
            if(webElement_media != null){
                List<WebElement> webElementsTemp_1 = webElement_media.findElements(By.tagName("img"));
                String media = "";
                for (int j = 0; j < webElementsTemp_1.size(); j++) {
                    media = media + webElementsTemp_1.get(j).getAttribute("src") + "\n";
                }
                res[1] = media.trim();
            }
        }catch (Exception e){

        }
        return res;
    }

    /**
     * 表情替换
     *
     * @param source  原字符串
     * @param slipStr emoji表情替换成的字符串
     * @return 过滤后的字符串
     */
    public static String filterEmoji(String source,String slipStr) {
        if(StringUtils.isNotBlank(source)){
            return source.replaceAll(EMOJI_REGULAR_EXPRESSION, slipStr);
        }else{
            return source;
        }
    }
}
