package DaZhongDianPing;

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

public class DianpingTel {
    public static void main(String[] args) {
        getShopDetailedData("http://www.dianping.com/shop/23099429");
    }
    private static void getShopDetailedData(String url) {
        String html = downHtml(url);
        getShopTel(html);

    }
    private static String getShopTel(String html){
        Document  doc=Jsoup.parse(html);
        Element tel= doc.getElementsByClass("tel").get(0);
        String numStr=tel.toString();
        numStr=numStr.replace("<span class=\"rc-fodK\"></span>","8");
        numStr=numStr.replace("<span class=\"pt-Q48P\"></span>","2");
        numStr=numStr.replace("<span class=\"fn-9HQC\"></span>","3");
        numStr=numStr.replace("<span class=\"rc-pOiZ\"></span>","4");
        numStr=numStr.replace("<span class=\"rc-T8SL\"></span>","5");
        numStr=numStr.replace("<span class=\"rc-nkYb\"></span>","6");
        numStr=numStr.replace("<span class=\"fn-YkOs\"></span>","7");
        numStr=numStr.replace("<span class=\"pt-OX0e\"></span>","8");
        numStr=numStr.replace("<span class=\"rc-j6dt\"></span>","9");
        numStr=numStr.replaceAll("<.*?>","").replace(" ","").replace("&nbsp;"," ");
        System.out.println(numStr);
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
        get.setHeader("Host", "www.dianping.com");
        get.setHeader("Connection", "keep-alive");
        get.setHeader("Upgrade-Insecure-Requests", "1");
        get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
        get.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        get.setHeader("Accept-Encoding", "gzip, deflate");
        get.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
        get.setHeader("Cookie","_hc.v=\"\\\"3ec663d9-9f4c-4772-aea9-1faa37da6a3f.1539766026\\\"\"; _lxsdk_cuid=166813503f7c8-0e24f5ddc74d59-3961430f-144000-166813503f8c8; _lxsdk=166813503f7c8-0e24f5ddc74d59-3961430f-144000-166813503f8c8; _tr.u=CqYVV55riMhElF6F; s_ViewType=10; dper=9b5ef730d3ae5d17310f5a025b814e22ba8776c2033c32ea5a5604fb06b26aa2d19e7062c289ed1d54e3261b14a02e2a95c2e61cfd2cebb0cbed0b0e9ac533f831b78ddd39d4e47b47c0999206e1aaede023eaf9607225fd271624215418ce3b; ua=dpuser_92308530602; ctu=cc0ae5fa54734db0fa86305e7a2744ee8fed9ed0eb0a010d4b9a33f1c16d0157; __utma=1.1043762305.1541165346.1541165346.1541165346.1; __utmz=1.1541165346.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); cy=1; cye=shanghai; ll=7fd06e815b796be3df069dec7836c3df; _lx_utm=utm_source%3DBaidu%26utm_medium%3Dorganic; _lxsdk_s=166e2b4291c-610-fd8-b1d%7C%7C63");

        return get;
    }
}
