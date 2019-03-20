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

import java.util.List;

/**
 * Project Name:weibo-crawler
 * File Name:InfoController
 * Package Name:com.zxs.ssh.template.controller
 * Date:2018/11/16
 * Author:zengxueshan
 * Description:爬取用户信息
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */

@RestController("infoController")
public class InfoController {

    private static final Logger logger = LoggerFactory.getLogger(InfoController.class);

    /**
     * 爬取用户信息
     *
     * @return 用户信息
     */
    @RequestMapping("crawlerInfo")
    public String crawlerInfo() {
        String res = "";
        long startTime = System.currentTimeMillis();
        String driverPath = "D:\\ZXSWork\\微博爬虫\\chromedriver.exe";
        String url = "https://weibo.com/hejiong?is_hot=1";
        //String url = "https://weibo.com/xiena?is_all=1";
        //String url = "https://weibo.com/5941149147/profile?topnav=1&wvr=6&is_all=1";
        WebDriver driver = RequestUtil.buildDriver(driverPath, url);
        long endTime = System.currentTimeMillis();
        System.out.println("请求花费时间：" + (endTime - startTime) + "ms");
        getUserInfo(driver);
        System.out.println("解析花费时间：" + (System.currentTimeMillis() - endTime) + "ms");
        return res;
    }

    /**
     * 获取用户信息，包括用户名、头像、介绍信息、微博等级、关注数、粉丝数、微博数
     *
     * @param driver 驱动
     */
    private void getUserInfo(WebDriver driver) {
        try{
            System.out.println("用户名：" + driver.findElement(By.className("username")).getAttribute("innerHTML"));
        }catch (Exception e){
            logger.info("用户名获取失败");
        }
        try{
            System.out.println("头像：" + driver.findElement(By.className("photo")).getAttribute("src"));
        }catch (Exception e){
            logger.info("头像获取失败");
        }
        try{
            System.out.println("介绍信息：" + driver.findElement(By.className("pf_intro")).getAttribute("innerHTML").trim());
        }catch (Exception e){
            logger.info("介绍信息获取失败");
        }

        try{
            System.out.println("微博等级：" + driver.findElement(By.cssSelector("div[id='Pl_Core_UserInfo__6']"))
                    .findElement(By.className("PCD_person_info"))
                    .findElement(By.cssSelector("a[target='_black']"))
                    .getAttribute("textContent").replace("Lv.", ""));
        }catch (Exception e){
            logger.info("微博等级获取失败");
        }

        try{
            System.out.println("关注数："+driver.findElement(By.cssSelector("table[class='tb_counter']"))
                    .findElements(By.cssSelector("strong")).get(0).getAttribute("innerHTML"));
        }catch (Exception e){
            logger.info("关注数获取失败");
        }
        try{
            System.out.println("粉丝数："+driver.findElement(By.cssSelector("table[class='tb_counter']"))
                    .findElements(By.cssSelector("strong")).get(1).getAttribute("innerHTML"));
        }catch (Exception e){
            logger.info("粉丝数获取失败");
        }
        try{
            System.out.println("微博数："+driver.findElement(By.cssSelector("table[class='tb_counter']"))
                    .findElements(By.cssSelector("strong")).get(2).getAttribute("innerHTML"));
        }catch (Exception e){
            logger.info("微博数获取失败");
        }
    }
}
