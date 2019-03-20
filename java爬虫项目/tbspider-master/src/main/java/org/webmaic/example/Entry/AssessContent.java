package org.webmaic.example.Entry;

import org.webmaic.example.model.DataSource;
import org.webmaic.example.model.ExtractAssessDetail;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;
import org.webmaic.example.util.DateUtil;
import org.webmaic.example.util.MatcherUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AssessContent implements PageProcessor {
    public static String goodsId = new String();

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
            //   .addHeader("Host","www.taobao.com")
            // .addHeader("Origin","https://www.taobao.com")
            .addHeader("Referer","https://s.taobao.com/search?q=%E6%8A%95%E5%BD%B1%E4%BB%AA&imgfile=&commend=all&ssid=s5-e&search_type=item&sourceId=tb.index&spm=a21bo.2017.201856-taobao-item.1&ie=utf8&initiative_id=tbindexz_20170306")
            .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.84 Safari/537.36")
            //   .addHeader("X-Anit-Forge-Code","0")
            //    .addHeader("X-Anit-Forge-Token","None")
            .addHeader("X-Requested-With","XMLHttpRequest");


    @Override
    public void process(Page page) {
        String  handleRawText = MatcherUtil.aliJsonMatcher( page.getRawText());
        //评论Id
        List<String> assessIdList = new JsonPathSelector("$.rateDetail.rateList[*].id").selectList(handleRawText);
        //商品ID
        goodsId = goodsId;
        //评论内容
        List<String> rateContentList = new JsonPathSelector("$.rateDetail.rateList[*].rateContent").selectList(handleRawText);
        //评论时间
        List<String> assessTimeList = new JsonPathSelector("$.rateDetail.rateList[*].rateDate").selectList(handleRawText);
        //评论SKU
        List<String> assessSkuList = new JsonPathSelector("$.rateDetail.rateList[*].auctionSku").selectList(handleRawText);
        //图片
        List<String> assessImageList = new JsonPathSelector("$.rateDetail.rateList[*].pics").selectList(handleRawText);
        List<String> haveImageList = new ArrayList<String>(21);
        for (String s:assessImageList){

        }
        //来源
        List<String> dataSourceList = new JsonPathSelector("$.rateDetail.rateList[*].cmsSource").selectList(handleRawText);
        //采集时间
        Date date = new Date();
        List<ExtractAssessDetail> assessList = new ArrayList<ExtractAssessDetail>(21);
        for (int i=0;i<assessIdList.size();i++){
            ExtractAssessDetail ead = new ExtractAssessDetail();
            ead.setAssessId(Long.parseLong(assessIdList.get(i)));
            ead.setGoodsId(Long.parseLong(goodsId));
            ead.setAssessContent(rateContentList.get(i));
            ead.setAssessSku(assessSkuList.get(i));
            ead.setAssessTime(DateUtil.getDateByStrTime(assessTimeList.get(i),"yyyy-MM-dd HH:mm:ss"));
            ead.setHaveImage("");
            ead.setDataSource(DataSource.TMall.getDesc());
            ead.setCollectTime(date);
            System.out.println(ead.toString());
        }

    }


    @Override
    public Site getSite() {
        return site;
    }

    /**
     *
     * @param currentPage 当前页码
     * @param itemId 商品Id
     * @param sellerId 卖家Id
     */
    public void extract(Integer currentPage,String itemId,String sellerId){
      itemId="565845090710";
        this.goodsId =itemId;
                Spider.create(new AssessContent())
                .addUrl("https://rate.tmall.com/list_detail_rate.htm?" +
                        "itemId=565845090710" +
                        "&spuId=939063316" +
                        "&sellerId=2587461980" +
                        "&order=3" +
                        "&currentPage=2" +
                        "&append=0" +
                        "&content=1" +
                        "&tagId=" +
                        "&posi=" +
                        "&picture=" +
                        "&groupId=" +
                        "&ua=098%23E1hvDQv7vcUvUvCkvvvvvjiPPsLy0jtnPL5y0j3mPmPO0j3nPscygjE2RLsZ1jD8RphvCvvvvvmrvpvEvvB49FQG227NdphvHmQh%2Bg8SdQmFUmeSULELKXhH6LItRphvCvvvvvmrvpvEvvLurYjBvjpL3QhvCvvhvvvCvpvVvmvvvhCvKphv8vvvvvCvpvvvvvmm86CvCUpvvUUdphvWvvvv9krvpv3Fvvmm86CvmVRivpvUvvmvW1Hu3R0EvpvVmvvC9jXRmphvLv9ldpvjOdea64VNe5aV0RFOtCQ4fCuYiXVvVE6Fp%2B0x9WQaRoxBlwet9b8rwkM61bmxdX9aUWoQiNp4VzHH%2BneYiLUpwhKn3w0xhCIPvpvhvv2MMsyCvvpvvvvviQhvCvvv9U8rvpvEvCoiUrcxvbsM9phv2nGvCepj7rMNz05wz86CvvyvCHIm29gvbOArvpvEvvsv9yxHvnlK9phv2nM52cQO7rMNzsavz2yCvvpvvvvvdphvmpvhxgy%2BbvABvOhCvCLNYGZ%2FGldNzMwshS1aeYGKzMFwQv%3D%3D" +
                        "&needFold=0" +
                        "&_ksTS=1538965540295_2784" +
                        "&callback=jsonp2785")
                .run();
    }
}
