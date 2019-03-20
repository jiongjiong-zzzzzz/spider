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
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class taobaoxq {
    public static void main(String[] args) {
        getShopDetailedData("https://detail.tmall.com/item.htm?spm=a230r.1.14.6.62996dbcMEIT9K&id=563010710042&cm_id=140105335569ed55e27b&abbucket=19");
    }
    private static void getShopDetailedData(String url) {
        String html = downHtml(url);
        getShopTel(html);
    }

    private static String getShopTel(String html){
        Document  doc=Jsoup.parse(html);
        Elements elem= doc.select("detail_url");
        for(Element Element:elem){
            String href = Element.text();
            System.out.println(href);
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
        get.setHeader(":authority","detail.tmall.com");
        get.setHeader(":method","GET");
        get.setHeader(":scheme","https");
        get.setHeader(":path","/item.htm?spm=a230r.1.14.6.62996dbcMEIT9K&id=563010710042&cm_id=140105335569ed55e27b&abbucket=19");
        get.setHeader("referer", "https://s.taobao.com/search?q=%E7%BE%BD%E7%BB%92%E6%9C%8D&imgfile=&commend=all&ssid=s5-e&search_type=item&sourceId=tb.index&spm=a21bo.2017.201856-taobao-item.1&ie=utf8&initiative_id=tbindexz_20170306");
        get.setHeader("upgrade-insecure-requests", "1");
        get.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
        get.setHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        get.setHeader("accept-encoding","gzip, deflate, br");
        get.setHeader("accept-language","zh-CN,zh;q=0.9");
        get.setHeader("cookie","thw=cn; t=0ece6493fd0b94b6d4480b194f274d39; cna=/cZNFOeZtjQCAWonlMe4QoEC; tracknick=%5Cu8584%5Cu83775598; lgc=%5Cu8584%5Cu83775598; tg=0; enc=i7iqafxYvQ%2FaWvtgqM2s2zHr6zhrKgwz5txL%2BsnWtP8NlyBSlGmiHFtjWseFFKJskfLTpZjJ%2BsPdhkOT7%2BopiQ%3D%3D; hng=CN%7Czh-CN%7CCNY%7C156; miid=1014007544183312682; _m_h5_tk=a26842dfe778727066d3a7aae3cb116f_1539780561218; _m_h5_tk_enc=d6912ff7af181f8b84936a89a013abf4; v=0; cookie2=1552d78f22a4daf95ef3a814a47a0790; _tb_token_=e4ebe76473307; swfstore=243299; alitrackid=www.taobao.com; lastalitrackid=www.taobao.com; unb=2210484786; sg=866; _l_g_=Ug%3D%3D; skt=3e28a4cc1486c6e7; cookie1=B0TxbKswfUaChk408bRsYaZPjHHtnaZgmcCKLoHna%2FE%3D; csg=77131d15; uc3=vt3=F8dByRmr3%2FzdvOYYt3g%3D&id2=UUpgRsBU%2BJwSpw%3D%3D&nk2=0uR9WCHwUcY%3D&lg2=WqG3DMC9VAQiUQ%3D%3D; existShop=MTUzOTg0MjYzNg%3D%3D; _cc_=U%2BGCWk%2F7og%3D%3D; dnk=%5Cu8584%5Cu83775598; _nk_=%5Cu8584%5Cu83775598; cookie17=UUpgRsBU%2BJwSpw%3D%3D; mt=ci=4_1&np=; uc1=cookie16=WqG3DMC9UpAPBHGz5QBErFxlCA%3D%3D&cookie21=V32FPkk%2FgihF%2FS5nr3O5&cookie15=URm48syIIVrSKA%3D%3D&existShop=false&pas=0&cookie14=UoTfItjXJSIbrQ%3D%3D&tag=8&lng=zh_CN; x=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0%26__ll%3D-1%26_ato%3D0; JSESSIONID=F572F95F5995DFF61361F5A228AA851D; isg=BE5OFtttmXF2Yy1bcP7guvDxnyTQZxPpN3paRXiXutEM2-414F9i2fSRF0cSQwrh; whl=-1%260%260%261539843223203");
        return get;
    }
}
