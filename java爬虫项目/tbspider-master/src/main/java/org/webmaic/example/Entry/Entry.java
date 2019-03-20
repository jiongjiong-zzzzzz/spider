package org.webmaic.example.Entry;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;
import org.webmaic.example.util.MatcherUtil;
import java.util.ArrayList;
import java.util.List;

public class Entry implements PageProcessor {

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me()
            .setRetryTimes(3)
            .setSleepTime(1000)
            .addHeader("Accept","application/json, text/javascript, */*; q=0.01")
            .addHeader("Accept-Encoding","gzip, deflate, br")
            .addHeader("Accept-Language","zh-CN,zh;q=0.9")
            .addHeader("Connection","keep-alive")
            //.addHeader("Content-Length","23")
            .addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8")
            .addHeader("Cookie","miid=9154837681527149408; t=435feb4105d4864537c6adbec97bd7e2; cna=J/nvE8snqVcCAXQYFfXqygBE; hng=CN%7Czh-CN%7CCNY%7C156; thw=cn; tracknick=%5Cu5168tm%5Cu88AB%5Cu7528%5Cu4E86; lgc=%5Cu5168tm%5Cu88AB%5Cu7528%5Cu4E86; tg=0; enc=40yrzq%2Bf7plOSmerDi%2FrcAfCOWjGLHNAvKitkYi8lwxI8kt%2BvOPgkPEpvq3klcaV60fFf4UiDuneVKkgH9mMog%3D%3D; x=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0%26__ll%3D-1%26_ato%3D0; birthday_displayed=1; uc3=vt3=F8dByRuXKYfcdVRBrbY%3D&id2=VWhcdAvge9Xz&nk2=q%2FJ2n3kD18t9nA%3D%3D&lg2=VFC%2FuZ9ayeYq2g%3D%3D; _cc_=UIHiLt3xSw%3D%3D; mt=ci=-1_0; _m_h5_tk=7ea9f77b54d383a871de284fc7f893bf_1537878845097; _m_h5_tk_enc=838066d31c5f2970ceca65e97a994270; tk_trace=oTRxOWSBNwn9dPyorMJE%2FoPdY8zfvmw%2Fq5hiWXd0Ms7t0wsPNNLfqShjcNZu96B5e5AaYu0Rbm2X8u1%2BciZG3pDteUcK5%2FQ%2F%2Fu7DYv3mlyuu7hEjYvxLH98TPKk9FyMGjk7uEPNr5bMe87ApuMGRTwHp2D2eMUwxZHT8xDWsFXB4npjk%2BiY8GQvJquBEIi2xoz5J3ia3AgG2pF2%2BfxKR3rAdkis8yspxe7n5D6hDzNhgdGc3QAv6p8yYZA5L%2BjjRmlZpwlecDzkAPFuofUW4knGCWNljZg%3D%3D; v=0; cookie2=3098a46f21dbb20e00591be6f0e0f204; _tb_token_=55d5e61363d39; alitrackid=www.taobao.com; lastalitrackid=www.taobao.com; swfstore=135312; JSESSIONID=9A41CA106CCE0B3BB7C0F931A8B6AE5F; uc1=cookie14=UoTfLJy2X6uUGw%3D%3D; isg=BCgohqjq52AkfMsjcsBz6yxb-Rb6-YzgImQe3eJZdKOWPcinimFc6757MZVoFkQz")
            //   .addHeader("Host","www.lagou.com")
            // .addHeader("Origin","https://www.lagou.com")
            .addHeader("Referer","https://s.taobao.com/search?q=%E6%8A%95%E5%BD%B1%E4%BB%AA&imgfile=&commend=all&ssid=s5-e&search_type=item&sourceId=tb.index&spm=a21bo.2017.201856-taobao-item.1&ie=utf8&initiative_id=tbindexz_20170306")
            .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.84 Safari/537.36")
            //   .addHeader("X-Anit-Forge-Code","0")
            //    .addHeader("X-Anit-Forge-Token","None")
            .addHeader("X-Requested-With","XMLHttpRequest");


    @Override
    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
//        //处理商品列表JSON
//        String  handleRawText = MatcherUtil.aliJsonMatcher( page.getRawText());
//
//        //商品标题
//        List<String> titleList = new JsonPathSelector("$..itemlist.auctions[*].raw_title").selectList(handleRawText);
//        //商品ID
//        List<String> idList = new JsonPathSelector("$..itemlist.auctions[*].nid").selectList(handleRawText);
//        //商品价格
//        List<String> priceList = new JsonPathSelector("$..itemlist.auctions[*].view_price").selectList(handleRawText);
//        //商品图片
//        List<String> picList = new JsonPathSelector("$..itemlist.auctions[*].pic_url").selectList(handleRawText);
//        //店铺Id
//        List<String> shopIdList = new JsonPathSelector("$..itemlist.auctions[*].shopLink").selectList(handleRawText);
//        for(String s:shopIdList){
//            s = MatcherUtil.numberMatcher(s);
//        }
//        List<ExtractGoods> goodsList = new ArrayList<ExtractGoods>(idList.size());

        //   page.putField("goodsName", page.getHtml().xpath("//*[@id=\"mainsrp-itemlist\"]/div/div/div[1]"));

        //搜索列表JSON
        String  handleRawText = MatcherUtil.generalMatcher(page.getRawText(),"(\\\"itemlist\\\":)(.*)(,\\\"bottomsearch\\\")",2);

        //商品标题
        List<String> titleList = new JsonPathSelector("$.data.auctions[*].raw_title").selectList(handleRawText);
        //商品ID
        List<String> idList = new JsonPathSelector("$.data.auctions[*].nid").selectList(handleRawText);
        //商品价格
        List<String> priceList = new JsonPathSelector("$.data.auctions[*].view_price").selectList(handleRawText);
        //商品图片
        List<String> picList = new JsonPathSelector("$.data.auctions[*].pic_url").selectList(handleRawText);
        //店铺Id
        List<String> shopIdList = new JsonPathSelector("$.data.auctions[*].shopLink").selectList(handleRawText);
        for (String s:shopIdList){
            MatcherUtil.generalMatcher(s,"[0-9]");
        }
        //商品url
        List<String> urlList = new JsonPathSelector("$.data.auctions[*].detail_url").selectList(handleRawText);
        List<String> completeUrlList = new ArrayList<String>(urlList.size());
        for (String s:urlList){
            if (s.indexOf("http")==-1){//如果链接包含http说明是广告商品
                String url = MatcherUtil.generalMatcher(s,"(.*)\\?",1);
                String param =MatcherUtil.generalMatcher(s,"\\?(.*)",1);
                s = "https:"+url+"?spm=a230r.1.14.201.753f3936h1PuLg&"+param;
                completeUrlList.add(s);
            }
        }
        //遍历商品
        for (String s:completeUrlList){

        }

        // 部分三：从页面发现后续的url地址来抓取
        //    page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/[\\w\\-]+/[\\w\\-]+)").all());

    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        Spider.create(new Entry())
                //开始抓
                //       .addUrl("https://s.taobao.com/api?_ksTS=1537948377593_281&callback=jsonp282&ajax=true&m=customized&sourceId=tb.index&q=%E6%8A%95%E5%BD%B1%E4%BB%AA&spm=a21bo.2017.201856-taobao-item.1&s=36&imgfile=&initiative_id=tbindexz_20170306&bcoffset=-1&commend=all&ie=utf8&rn=1a33bdcdcb97078f4ca12ae1aeb38d4e&ssid=s5-e&search_type=item")
                .addUrl("https://s.taobao.com/search?q=投影仪&imgfile=&js=1&stats_click=search_radio_all%3A1&initiative_id=staobaoz_20180927&ie=utf8")
                //开启5个线程抓取
                //    .thread(5)
                //启动爬虫
                .run();
    }
}