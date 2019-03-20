package DATA;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.*;
public class Analysis {
    /**
     * 解析数据
     * @param htmlBody
     * @return
     * @throws IOException
     */
    public static List<Map<String,Object>> analysisData(String htmlBody) throws Exception {
        // 获取目标HTML代码
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        Document doc = Jsoup.parse(htmlBody);
        Elements elements = doc.select("ul.listCentent").select("li");
        System.out.println(" 数据集合大小=====" + elements.size());
//foreach循环
        for (Element elmemt : elements) {
            Map<String,Object> map1=new HashMap<String,Object>();
            //获取公司名
            String siteName = elmemt.select("div.CentTxt > h3.rightTxtHead > a").text();
            System.out.println("siteName=====" + siteName);
            //获取域名
            String domainName = elmemt.select("div.CentTxt > h3.rightTxtHead > span").text();
            System.out.println("domainName=====" + domainName);
            //获取AlexaRank排名
            String AlexaRank = elmemt.select("li.clearfix >div.CentTxt > div.RtCPart >p").text();
            System.out.println("AlexaRank=====" + AlexaRank);
            //获取公司简介
            String Synopsis = elmemt.select("div.CentTxt> p").text();
            System.out.println("公司简介====" + Synopsis);
            //获取得分
            String score = elmemt.select("div.RtCRateCent>span").text();
            System.out.println(score);
            //获取排名
            String siteRank = elmemt.select("div.RtCRateCent> strong").text();
            System.out.println("排名:" + siteRank);
            //获取网址
            String webSite = "http://top.chinaz.com"+elmemt.select("a").first().attr("href");
            System.out.println("网址:" + webSite);
            //获取备案信息
            String stringecordInformation = getGecordInformation(webSite);
            System.out.println("备案信息"+stringecordInformation);
            System.out.println("\t");
//            StoreData.add(siteName,domainName, AlexaRank , Synopsis, score, siteRank, webSite ,RecordInformation);
            map1.put("siteName",siteName);
            map1.put("domainName",domainName);
            map1.put("AlexaRank",AlexaRank);
            map1.put("公司简介",Synopsis);
            map1.put("排名",siteRank);
            map1.put("网址",webSite);
            map1.put("备案信息",stringecordInformation);
            list.add(map1);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
    /**
     * 获取备案信息
     * @param url
     * @return
     * @throws Exception
     */
    private static String getGecordInformation(String url) throws Exception{
        String htmlBody = CrawlData.downloadHtml(url);
        if(htmlBody != null){
            Document doc = Jsoup.parse(htmlBody);
            String stringecordInformation = doc.select("li.TMain06List-Left>p").text();
            return stringecordInformation;
        }
        return null;
    }
}
