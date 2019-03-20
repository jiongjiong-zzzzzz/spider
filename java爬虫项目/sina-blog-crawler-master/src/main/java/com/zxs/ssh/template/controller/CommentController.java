package com.zxs.ssh.template.controller;

import com.zxs.ssh.template.util.BlogParseUtil;
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
 * File Name:CommentController
 * Package Name:com.zxs.ssh.template.controller
 * Date:2018/11/20
 * Author:zengxueshan
 * Description:爬取博客评论者信息和评论内容(新增评论回复信息)
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */

@RestController("commentController")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(BlogController.class);

    /**
     * 爬取博客评论者信息和评论内容
     *
     * @return 博客评论者信息和评论内容
     */
    @RequestMapping("crawlerComment")
    public String crawlerComment() {
        String res = "ok";
        long startTime = System.currentTimeMillis();
        String driverPath = "D:\\ZXSWork\\微博爬虫\\chromedriver.exe";
        String url = "https://weibo.com/1195230310/H0cjyhF4U";
        //String url = "https://weibo.com/xiena?is_all=1";
        //String url = "https://weibo.com/5941149147/profile?topnav=1&wvr=6&is_all=1";
        WebDriver webDriver = RequestUtil.buildDriver(driverPath, url);
        long endTime = System.currentTimeMillis();
        System.out.println("请求花费时间：" + (endTime - startTime) + "ms");
        getComment(webDriver);
        System.out.println("解析花费时间：" + (System.currentTimeMillis() - endTime) + "ms");
        return res;
    }

    /**
     * 获取博客评论者信息和评论内容
     *
     * @param webDriver 驱动
     */
    private void getComment(WebDriver webDriver) {
        WebElement webElement = null;
        try {
            webElement = webDriver.findElement(By.cssSelector("div[node-type=comment_list]"));
        } catch (Exception e) {
            logger.info("提取节点错误");
        }
        if (webElement != null) {
            List<WebElement> webElements = new ArrayList<>();
            try {
                webElements = webElement.findElements(By.cssSelector("div[node-type=root_comment]"));
            } catch (Exception e) {
                logger.info("提取节点错误");
            }
            System.out.println("博客当前页评论总数：" + webElements.size());
            for (int i = 0; i < webElements.size(); i++) {
                System.out.println("*********************第" + (i + 1) + "条评论********************");
                getCommentContent(webElements.get(i));
            }
        }
    }

    /**
     * 获取评论内容，包括评论者昵称、评论者头像、评论者id、文本内容、多媒体内容、创建时间、点赞数
     *
     * @param webElement 单条评论节点
     */
    private void getCommentContent(WebElement webElement) {
        try{
            System.out.println("评论者昵称：" + webElement.findElement(By.cssSelector("img")).getAttribute("alt"));
        }catch (Exception e){
            logger.info("评论者昵称获取失败");
        }
        try{
            System.out.println("评论者头像：" + webElement.findElement(By.cssSelector("img")).getAttribute("src"));
        }catch (Exception e){
            logger.info("评论者头像获取失败");
        }
        try{
            System.out.println("评论者id：" + webElement.findElement(By.cssSelector("img")).getAttribute("usercard").replace("id=", ""));
        }catch (Exception e){
            logger.info("评论者id获取失败");
        }
        String[] strTemp = BlogParseUtil.getContentHelp(webElement, "div[class='WB_text']", "div[class='media_box']");
        System.out.println("评论文本内容：" + strTemp[0]);
        System.out.println("评论多媒体内容：" + strTemp[1]);
        try{
            System.out.println("评论创建时间：" + webElement.findElement(By.cssSelector(".WB_from.S_txt2")).getAttribute("innerHTML"));

        }catch (Exception e){
            logger.info("评论创建时间获取失败");
        }
        try{
            System.out.println("评论点赞数：" + webElement.findElement(By.cssSelector("span[node-type='like_status']"))
                    .findElements(By.cssSelector("em")).get(1).getAttribute("innerHTML"));
        }catch (Exception e){
            logger.info("评论点赞数获取失败");
        }

    }
}
