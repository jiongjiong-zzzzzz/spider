package com.zxs.ssh.template.controller;

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
 * File Name:IpProxyController
 * Package Name:com.zxs.ssh.template.controller
 * Date:2018/11/21
 * Author:zengxueshan
 * Description:爬取网页，获取IP代理池
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */

@RestController("ipProxyController")
public class IpProxyController {

    private static final Logger logger = LoggerFactory.getLogger(IpProxyController.class);

    /**
     * 爬取网页，获取IP代理池
     *
     * @return IP代理池
     */
    @RequestMapping("crawlerIp")
    public String crawlerIp(){
        String res = "ok";
        long startTime = System.currentTimeMillis();
        String driverPath = "D:\\ZXSWork\\微博爬虫\\chromedriver.exe";
        String url = "http://www.xicidaili.com/nn/";
        WebDriver webDriver = RequestUtil.buildDriver(driverPath,url);
        long endTime = System.currentTimeMillis();
        System.out.println("请求花费时间：" + (endTime - startTime) + "ms");
        getIp(webDriver);
        System.out.println("解析花费时间：" + (System.currentTimeMillis() - endTime) + "ms");
        return res;
    }

    /**
     * 获取ip代理池
     *
     * @param webDriver 驱动
     */
    private void getIp(WebDriver webDriver) {
        WebElement webElement = null;
        try{
            webElement = webDriver.findElement(By.cssSelector("table[id='ip_list']"));
            if(webElement != null){
                List<WebElement> webElements = new ArrayList<>();
                webElements = webElement.findElements(By.cssSelector("tr"));
                System.out.println("当前页共有IP数："+(webElements.size()-1));
                for (int i = 1; i < webElements.size(); i++) {
                    System.out.println("*******************第"+i+"个IP**************");
                    String ip = webElements.get(i).findElements(By.cssSelector("td")).get(1).getAttribute("innerHTML") + ":"
                            + webElements.get(i).findElements(By.cssSelector("td")).get(2).getAttribute("innerHTML");
                    System.out.println("IP: "+ip);
                }
            }
        }catch (Exception e){

        }
    }

}
