package TaoBao;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class taobaoSpider {
    public static void main(String[] args) {
        getShopDetailedData("https://www.taobao.com/tbhome/page/market-list?spm=a21bo.2017.1997563209.1.5af911d9x6jCW0");
    }
    private static void getShopDetailedData(String url) {
        String html = downHtml(url);
        //System.out.println(html);
        getShopTel(html);
    }
    private static String getShopTel(String html){
        Document  doc=Jsoup.parse(html);
        System.out.println(doc);
        Elements elem= doc.select("div.category-items a.category-name");
        for(Element Element:elem){
            String variety = Element.select("a").get(0).text();
            System.out.println(variety);

        }
        return null;
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
    private static HttpGet setHeader(HttpGet get) {
        get.setHeader("Referer", "https://www.taobao.com/");
        get.setHeader("Upgrade-Insecure-Requests", "1");
        get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
        get.setHeader("X-DevTools-Emulate-Network-Conditions-Client-Id", "D916A24157E0B0466C7E62E9DEC33CD1");
        return get;
    }
}
