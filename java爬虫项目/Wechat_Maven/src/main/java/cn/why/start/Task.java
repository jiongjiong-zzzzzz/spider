package cn.why.start;

import cn.why.client.JsoupClient;
import cn.why.client.SeleniumClient;
import cn.why.parse.ContentByCSS;
import cn.why.parse.FakeID;
import cn.why.parse.Token;
import cn.why.parse.URLlistByCss;
import cn.why.pojo.urlList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.List;
/**
 * Title:           Task
 * Description:     用于书写爬虫逻辑
 * Company:         AceGear
 * Author:          henrywang
 * Date:            2018/5/17
 * JDK:             10
 * Encoding:        UTF-8
 */
public class Task {
    public void weChat(){
        //主入口
        String url = "https://mp.weixin.qq.com";
        Document document = new JsoupClient().DownHtml(url);
        String token = new Token().getToken(document);
        //采集汽车之家公众号
        String weChatID = "汽车之家";
        String fakeID = new FakeID().getFakeID(token,weChatID);
        List<urlList> list = new URLlistByCss().getURl(token,fakeID);
        System.out.println(list.size());
        ContentByCSS contentByCSS = new ContentByCSS();
        for(urlList ulist : list){
            String contentURl = ulist.getLink();
            Document document1 = Jsoup.parse(new SeleniumClient().DownHtml(contentURl));
            contentByCSS.getContent(document1);
        }
    }
}
