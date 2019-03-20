package DaZhongDianPing;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 大众点评数据采集器
 * 采集数据： 店铺名，地址，评分，联系方式，店铺ID。
 * 单页抓取2-4s，采用单线程处理，多线程可自己优化。
 * @author 十字
 * @date 2018-07-28 13:15
 */
public class spider {
    public static void main(String[] args) {
        //{page}为需要替换的页码
        String basicUrl="http://www.dianping.com/changzhou/ch0/p{page}";
        List<Map> results=new ArrayList<>();
        for(int i=1;i<11;i++) {
            String targetUrl=basicUrl.replace("{page}",String.valueOf(i));
            String html = downHtml(targetUrl);
            List<Map> result=parseHtml(html);
            if(result!=null){
                results.addAll(result);
            }else{
                System.out.println(targetUrl+"未获取到数据");
            }
        }
        System.out.println(JSON.toJSONString(results));
    }

    private static String downHtml(String url) {
        HttpClient http = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        //设置头
        get = setHeader(get);
        try {
            HttpResponse res = http.execute(get);
            return EntityUtils.toString(res.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<Map> parseHtml(String html) {
        Element doc = Jsoup.parse(html).body();
        Element allShop = doc.getElementById("shop-all-list");
        Elements shops = allShop.getElementsByTag("li");
        if (shops != null && shops.size() > 0) {
            List<Map> results = new ArrayList<>();
            for (Element shop : shops) {
                try {
                    Map<String,Object> shopData = new HashMap<>();
                    Element tit = shop.getElementsByClass("tit").get(0);
                    Element a = tit.getElementsByTag("a").get(0);
                    String shopId = a.attr("data-shopid");
                    String title = a.attr("title");
                    String href = a.attr("href");
                    shopData.put("shopId", shopId);
                    shopData.put("title", title);
                    shopData.put("href", href);
                    Object detailed = getShopDetailedData(href);
                    shopData.put("detailed", detailed);
                    results.add(shopData);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            return results;
        }
        return null;
    }


    private static Map getShopDetailedData(String url) {
        String html = downHtml(url);
        Map shopDetailedData = parseShopHtml(html);
        return shopDetailedData;
    }

    private static Map<String, String> parseShopHtml(String html) {
        Map<String, String> shopDetailedData = new HashMap();
        try {
            //目前解析只有两种格式的页面，有新的可以在此处追加
            if (html.contains("basic-info")) {
                shopDetailedData = parseBasicInfo(html);
                shopDetailedData.put("code", "200");
                shopDetailedData.put("status", "basic-info解析完成");
            } else if (html.contains("J_boxYouhui")) {
                shopDetailedData = parseJBoxYouhui(html);
                shopDetailedData.put("code", "200");
                shopDetailedData.put("status", "J_boxYouhui解析完成");
            } else {
                shopDetailedData.put("code", "404");
                shopDetailedData.put("status", "没有可用解析器");
            }
        } catch (Exception e) {
            shopDetailedData.put("code", "500");
            shopDetailedData.put("status", "解析异常:" + e.getMessage());
        }
        return shopDetailedData;
    }

    private static Map<String, String> parseBasicInfo(String html) {
        Map<String, String> data = new HashMap<>();
        Element doc = Jsoup.parse(html).body();
        Element basicInfo = doc.getElementById("basic-info");
        try {
            Element shopNameElement = basicInfo.getElementsByClass("shop-name").get(0);
            String shopName = shopNameElement.html().replaceAll("<.*>","").replace("手机买单&nbsp;积分抵现","").trim();
            data.put("shopName", shopName);
        } catch (Exception e) {
            data.put("shopName", "获取失败:>" + e.getMessage());
        }
        try {
            Element briefInfoElement = basicInfo.getElementsByClass("brief-info").get(0);
            String briefInfo = briefInfoElement.children().get(0).attr("title");
            data.put("rating", briefInfo);
        } catch (Exception e) {
            data.put("rating", "获取失败:>" + e.getMessage());
        }
        try {
            String address = basicInfo.getElementById("address").html().trim().replaceAll("<.*?>","");
            data.put("address", address);
        } catch (Exception e) {
            data.put("address", "获取失败:>" + e.getMessage());
        }
        try {
            Element telElement = basicInfo.getElementsByAttributeValue("class", "expand-info tel").get(0);
            String tel = telElement.html().replaceAll("<.*?>","").replaceAll("电话：|添加","").trim();
            data.put("tel", tel);
        } catch (Exception e) {
            data.put("tel", "获取失败:>" + e.getMessage());
        }
        return data;
    }

    private static Map parseJBoxYouhui(String html) {
        Map<String, String> data = new HashMap<>();
        Element doc = Jsoup.parse(html).body();
        Element jBoxYouhui = doc.getElementById("J_boxYouhui");
        try {
        Element shopTitleElement = jBoxYouhui.getElementsByClass("shop-title").get(0);
        String shopName=shopTitleElement.html().trim();
        data.put("shopName",shopName);
        } catch (Exception e) {
            data.put("shopName", "获取失败:>" + e.getMessage());
        }
        try {
        Element commentRstElement = jBoxYouhui.getElementsByClass("comment-rst").get(0);
        String rating= commentRstElement.children().get(1).attr("title");
        data.put("rating",rating);
        } catch (Exception e) {
            data.put("rating", "获取失败:>" + e.getMessage());
        }
        try {
        Element shopAddrElement = jBoxYouhui.getElementsByClass("shop-addr").get(0);
        String shopAddr=shopAddrElement.children().get(0).children().get(0).attr("title");
        data.put("address",shopAddr);
        } catch (Exception e) {
            data.put("address", "获取失败:>" + e.getMessage());
        }
        try {
        Element iconPhoneElement = jBoxYouhui.getElementsByClass("icon-phone").get(0);
        String tel=iconPhoneElement.html().trim();
        data.put("tel",tel);
        } catch (Exception e) {
            data.put("tel", "获取失败:>" + e.getMessage());
        }
        return data;
    }

    private static HttpGet setHeader(HttpGet get) {
        get.setHeader("Host", "www.dianping.com");
        get.setHeader("Connection", "keep-alive");
        get.setHeader("Upgrade-Insecure-Requests", "1");
        get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
        get.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        get.setHeader("Accept-Encoding", "gzip, deflate");
        get.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
        return get;
    }
}
