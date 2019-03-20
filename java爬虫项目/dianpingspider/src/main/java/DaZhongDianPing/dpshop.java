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
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class dpshop {
    public static void main(String[] args) {
        getShopDetailedData("http://www.dianping.com/beijing/food");
    }
    private static void getShopDetailedData(String url) {
        String html = downHtml(url);
        ArrayList<String> list = getMatherSubstrs(html, "\"detail_url\":\"(.+?)\"");

    }


    public static ArrayList<String> getMatherSubstrs(String destStr, String regexStr){
        Pattern pattern = Pattern.compile(regexStr);
        Matcher mather = pattern.matcher(destStr);
        ArrayList<String> res = new ArrayList<String>();
        while(mather.find()){
            res.add(mather.group(1));
        }
        return res;
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
        get.setHeader("Upgrade-insecure-requests", "1");
        get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
        get.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        get.setHeader("Accept-Encoding","gzip, deflate, br");
        get.setHeader("Accept-Language","zh-CN,zh;q=0.9");
        get.setHeader("Connection","keep-alive");
        get.setHeader("Referer","http://www.dianping.com/");
       get.setHeader("Cookie","navCtgScroll=0; showNav=javascript:; cy=2; cye=beijing; _hc.v=\"\\\"3ec663d9-9f4c-4772-aea9-1faa37da6a3f.1539766026\\\"\"; _lxsdk_cuid=166813503f7c8-0e24f5ddc74d59-3961430f-144000-166813503f8c8; _lxsdk=166813503f7c8-0e24f5ddc74d59-3961430f-144000-166813503f8c8; _tr.u=CqYVV55riMhElF6F; _lx_utm=utm_source%3DBaidu%26utm_medium%3Dorganic; s_ViewType=10; thirdtoken=DAB166985182578747B4BEE170144CB3; JSESSIONID=798E47D8614374D26D6EB8273D2DCBBC; dper=9b5ef730d3ae5d17310f5a025b814e22ba8776c2033c32ea5a5604fb06b26aa2d19e7062c289ed1d54e3261b14a02e2a95c2e61cfd2cebb0cbed0b0e9ac533f831b78ddd39d4e47b47c0999206e1aaede023eaf9607225fd271624215418ce3b; ll=7fd06e815b796be3df069dec7836c3df; ua=dpuser_92308530602; ctu=cc0ae5fa54734db0fa86305e7a2744ee8fed9ed0eb0a010d4b9a33f1c16d0157; _lxsdk_s=166d373b351-150-9de-558%7C%7C746");
        return get;
    }
}
