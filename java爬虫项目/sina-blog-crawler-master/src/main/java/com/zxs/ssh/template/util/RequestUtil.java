package com.zxs.ssh.template.util;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * Project Name:weibo-crawler
 * File Name:RequestUtil
 * Package Name:com.zxs.ssh.template.util
 * Date:2018/11/20
 * Author:zengxueshan
 * Description: 请求链接工具
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */


public class RequestUtil {

    private static final Logger logger = LoggerFactory.getLogger(RequestUtil.class);

    /**
     * 请求链接，返回包含html页面数据的WebDriver
     *
     * @param driverPath 谷歌浏览器驱动地址
     * @param url        请求链接地址
     * @return 包含html页面数据的WebDriver
     */
    public static WebDriver buildDriver(String driverPath, String url) {
        //等待数据加载的时间
        //为了防止服务器封锁，这里的时间要模拟人的行为，随机且不能太短
        long waitLoadBaseTime = 10000;
        int waitLoadRandomTime = 3000;
        Random random = new Random(System.currentTimeMillis());
        // 设置 chrome 的路径
        System.setProperty("webdriver.chrome.driver", driverPath);
        // 创建一个 ChromeDriver 的接口，用于连接 Chrome
        ChromeOptions opt = new ChromeOptions();
        opt.addArguments("headless");
/*        String ip = "118.190.94.224:9001";
        opt.addArguments("--proxy-server=http://" + ip);*/
        // 创建一个 Chrome 的浏览器实例
        WebDriver driver = new ChromeDriver(opt);
        // 让浏览器访问微博主页
        driver.get(url);
        //等待页面动态加载完毕
        try {
            Thread.sleep(waitLoadBaseTime + random.nextInt(waitLoadRandomTime));
        } catch (Exception e) {
            logger.info("等待页面动态加载完毕失败");
        }
        //模拟自动翻页
        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight)");
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            logger.info("等待页面动态加载完毕失败");
        }
        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight)");
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            logger.info("等待页面动态加载完毕失败");
        }
        return driver;
    }
}
