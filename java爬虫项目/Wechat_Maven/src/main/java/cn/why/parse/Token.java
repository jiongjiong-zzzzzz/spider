package cn.why.parse;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.util.List;
/**
 * Title:           Token
 * Description:     解析token，可用1900037076替换
 * Company:         AceGear
 * Author:          henrywang
 * Date:            2018/5/17
 * JDK:             10
 * Encoding:        UTF-8
 */
public class Token {
    public String getToken(Document document){
        List<String> list = new ArrayList<String>();
        Element element = document.select("#header > div > div > div > div.weui-desktop-layout__extra > div > a").get(0);
        String token = element.attr("href").substring(element.attr("href").indexOf("token=")+6);
        System.out.println("token: "+token);
        return token;
    }
}
