package cn.why.parse;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * Title:           ContentByCSS
 * Description:     用css解析文章内容页，所有规则均为css规则
 * Company:         AceGear
 * Author:          henrywang
 * Date:            2018/5/17
 * JDK:             10
 * Encoding:        UTF-8
 */
public class ContentByCSS {
    public void getContent(Document document){
        Elements elements = document.select("div#js_content p");
        String title = (document.select("#img-content h2").text()).replaceAll("\r\n","").replaceAll("\r","").replaceAll("\t","").replaceAll(" ","");
        String time = document.select("em#publish_time").text();
        String author = document.select("#meta_content p").text();
        System.out.println("标题: "+title);
        System.out.println("时间: "+time);
        System.out.println("作者: "+author);
        for(Element element : elements){
            System.out.println("==========内容+图片网址==========");
            String text = element.select("span").text().trim();
            System.out.println(text);
            String pic = element.select("img").attr("data-src").toString();
            System.out.println(pic);
            System.out.println("====================");
        }
    }
}
