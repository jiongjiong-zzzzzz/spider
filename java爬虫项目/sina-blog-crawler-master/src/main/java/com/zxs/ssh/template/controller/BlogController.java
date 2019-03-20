package com.zxs.ssh.template.controller;

import com.zxs.ssh.template.util.BlogParseUtil;
import com.zxs.ssh.template.util.ConversionUtil;
import com.zxs.ssh.template.util.RequestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Project Name:weibo-crawler
 * File Name:BlogController
 * Package Name:com.zxs.ssh.template.controller
 * Date:2018/11/20
 * Author:zengxueshan
 * Description: 爬取用户博客内容及其转发数、评论数、点赞数(新增获取每条博客地址)
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */

@RestController("blogController")
public class BlogController {

    private static final Logger logger = LoggerFactory.getLogger(BlogController.class);

    /**
     * 爬取用户博客内容及其转发数、评论数、点赞数
     *
     * @return 爬取用户博客及其转发数、评论数、点赞数
     */
    @RequestMapping("crawlerBlog")
    public String crawlerBlog(){
        String res = "ok";
        long startTime = System.currentTimeMillis();
        String driverPath = "D:\\ZXSWork\\微博爬虫\\chromedriver.exe";
        String url = "https://weibo.com/hejiong?count=45";
        //String url = "https://weibo.com/xiena?is_all=1";
        //String url = "https://weibo.com/5941149147/profile?topnav=1&wvr=6&is_all=1";
        WebDriver webDriver = RequestUtil.buildDriver(driverPath,url);
        long endTime = System.currentTimeMillis();
        System.out.println("请求花费时间：" + (endTime - startTime) + "ms");
        getBlogContent(webDriver);
        System.out.println("解析花费时间：" + (System.currentTimeMillis() - endTime) + "ms");
        return res;
    }

    /**
     * 获取博客详细地址
     *
     * @param webElement 父节点
     * @return 博客地址
     */
    private String getSingleBlogUrl(WebElement webElement) {
        String res = "";
        try{
            String ouid = webElement.getAttribute("tbinfo").replace("ouid=","");
            if(ouid.contains("&")){
                ouid = ouid.split("&")[0];
            }
            String linkId = encode(webElement.getAttribute("mid"));
            res = "https://weibo.com/"+ouid+"/"+linkId;
        }catch (Exception e){
            logger.info("博客详细地址获取失败");
        }
        return res;

    }

    /**
     * 将mid转换为微博的链接地址标识
     *
     * @param mid 每条微博的mid属性值
     * @return 微博的链接地址标识
     */
    private static String encode(String mid) {
        String res = "";
        if(mid.length() != 16){
            return res;
        }
        //将mid分解为3部分
        long[] midTemp = new long[3];
        try{
            midTemp[0] = Long.parseLong(mid.substring(0,2));
            midTemp[1] = Long.parseLong(mid.substring(2,9));
            midTemp[2] = Long.parseLong(mid.substring(9,16));
        }catch (Exception e){
            logger.info("转换错误",e);
        }

        for (int i = 0; i < midTemp.length; i++) {
            if(i == 0){
                res = res + ConversionUtil.encode(midTemp[i],1);
            }else{
                res = res + ConversionUtil.encode(midTemp[i],4);
            }
        }
        return res;
    }
    /**
     * 获取博客内容,包括创建时间、文本内容、多媒体内容、转发数、评论数、点赞数
     *
     * @param webDriver 驱动
     */
    private void getBlogContent(WebDriver webDriver) {
        List<WebElement> webElements = new ArrayList<>();
        try{
            webElements = webDriver.findElements(By.cssSelector("div[action-type=feed_list_item]"));
        }catch (Exception e){
            logger.info("提取节点错误");
        }
        System.out.println("当前页博客总数："+webElements.size());
        for (int i = 0; i < webElements.size(); i++) {
            System.out.println("****************第"+(i+1)+"条博客******************");
            try{
                System.out.println("博客创建时间："+webElements.get(i).findElement(By.cssSelector("a[class='S_txt2'][node-type='feed_list_item_date']")).getAttribute("title"));
            }catch (Exception e){
                logger.info("博客创建时间获取失败");
            }
            String[] strTemp = BlogParseUtil.getContentHelp(webElements.get(i), "div[node-type='feed_list_content']", "div[node-type='feed_list_media_prev']");
            System.out.println("博客文本内容："+strTemp[0]);
            System.out.println("博客多媒体内容："+strTemp[1].trim());
            try{
                System.out.println("博客转发数："+webElements.get(i).findElement(By.cssSelector("a[action-type='fl_forward']")).findElements(By.cssSelector("em")).get(1).getAttribute("innerHTML"));
            }catch (Exception e){
                logger.info("博客转发数获取失败");
            }
            try{
                System.out.println("博客评论数："+webElements.get(i).findElement(By.cssSelector("a[action-type='fl_comment']")).findElements(By.cssSelector("em")).get(1).getAttribute("innerHTML"));
            }catch (Exception e){
                logger.info("博客评论数获取失败");
            }
            try{
                System.out.println("博客点赞数："+webElements.get(i).findElement(By.cssSelector("a[action-type='fl_like']")).findElements(By.cssSelector("em")).get(1).getAttribute("innerHTML"));
            }catch (Exception e){
                logger.info("博客点赞数获取失败");
            }
            System.out.println("博客详细地址："+getSingleBlogUrl(webElements.get(i)));
        }
    }
}
