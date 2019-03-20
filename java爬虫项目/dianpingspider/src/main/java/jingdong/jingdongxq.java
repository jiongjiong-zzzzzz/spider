package jingdong;

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

public class jingdongxq {
    public static void main(String[] args) {
        getShopDetailedData("https://wenku.baidu.com/view/515e88c36529647d2728529b.html");
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

            return EntityUtils.toString(res.getEntity(),"GBK");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static HttpGet setHeader(HttpGet get) {

        get.setHeader("Host", "wenku.baidu.com");
        get.setHeader("Upgrade-insecure-requests", "1");
        get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
        get.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        get.setHeader("Accept-Encoding","gzip, deflate, br");
        get.setHeader("Accept-Language","zh-CN,zh;q=0.9");
        get.setHeader("Connection","keep-alive");
       get.setHeader("Cookie","Hm_lvt_59ce22710e353ee4d0f55960b28effd5=1539932486; Hm_lpvt_59ce22710e353ee4d0f55960b28effd5=1539933716; viewedPg=2bd12d7d7dd184254b35eefdc8d376eeafaa1768%3D1%7C0%26296e09a605a1b0717fd5360cba1aa81144318f8a%3D1%7C0%26515e88c36529647d2728529b%3D1%7C0; wkview_gotodaily_tip=1; BAIDUID=54231CD2C1828CC0DFF4E7AB84D7D4E8:FG=1; BIDUPSID=914A64923D5E6718AB61434AD2D26676; PSTM=1539757368; BDORZ=B490B5EBF6F3CD402E515D22BCDA1598; isJiaoyuVip=1; _click_param_pc_rec_doc_2017_testid=3; BDSFRCVID=JFLsJeCCxG3ZMeo718mP_B4yQ0CH6AQX2liC3J; H_BDCLCKID_SF=tJADoDK5tCL3fP36qR6sMJ8thmT22-usJHviQhcH0KLKsn5zXMc-jT_L-nAtLpbCWGr4bxcsbfb1MRjvhJ30WJcQ-4Lj2bcHye3Xbq5TtUJUSDnTDMRh-4_95n6yKMnitIj9-pnKBMt0hI0ljj82e5P0hxry2Dr2aI52B5r_5TrjDnCryxjdKUI8LPbO05JzBCTy_KDyaxJrECbw0j3Ty5KF0JAH2Pcmt2LE3-oJqC-2MI_l3J; delPer=0; PSINO=1; Hm_lvt_d8bfb560f8d03bbefc9bdecafc4a4bf6=1539932477,1539933774; H_PS_PSSID=1448_21087_18560_20928; Hm_lpvt_d8bfb560f8d03bbefc9bdecafc4a4bf6=1539934824");
        return get;
    }
}
