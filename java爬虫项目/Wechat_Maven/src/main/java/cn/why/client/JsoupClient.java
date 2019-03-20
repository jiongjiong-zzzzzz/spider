package cn.why.client;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/**
 * Title:           JsoupClient
 * Description:     利用Jsoup下载htmlpage;
 * Company:         AceGear
 * Author:          henrywang
 * Date:            2018/5/17
 * JDK:             10
 * Encoding:        UTF-8
 */
public class JsoupClient {
    /**
     *
     * @param url
     * @return 返回document类型page
     */
    public Document DownHtml(String url){
        Document document = null;
        try {
            //设置头文件，结构同selenium相似
            document = Jsoup.connect(url)
                    .header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .header("Accept-Encoding","gzip, deflate, br")
                    .header("Accept-Language","zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2")
                    .header("Cache-Control","max-age=0")
                    .header("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:59.0) Gecko/20100101 Firefox/59.0")
                    .cookies(cookie())
                    .timeout(2000)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    /**
     *
     * @param url
     * @return 返回string 类型page
     */
    public String DownBody(String url){

        String body = null;
        try {
            //设置头文件，结构同selenium相似
            body = Jsoup.connect(url)
                    .header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .header("Accept-Encoding","gzip, deflate, br")
                    .header("Accept-Language","zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2")
                    .header("Cache-Control","max-age=0")
                    .header("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:59.0) Gecko/20100101 Firefox/59.0")
                    .cookies(cookie())
                    .timeout(2000)
                    .ignoreContentType(true)
                    .execute()
                    .body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }

    /**
     * 设置cookie，需要更改，要不然此cookie容易被封
     * @return
     */
    public Map<String,String> cookie(){
        Map<String,String> map = new HashMap<String, String>();
        String cookie="noticeLoginFlag=1; remember_acct=695452594%40qq.com; ua_id=vxlZSEyt01OEFdh8AAAAADZP6txrDJQBu0rF57Uhtas=; mm_lang=zh_CN; pgv_pvi=4468031488; noticeLoginFlag=1; remember_acct=18306843748%40163.com; pgv_si=s1320480768; Hm_lvt_0bd5902d44e80b78cb1cd01ca0e85f4a=1526397591,1526430013,1526470337,1526529687; uuid=800f27eed660c7123ade2210c72028df; ticket=ea361b2647dc9bfd30e8ed67c19137c4cf702676; ticket_id=gh_e7cf4c8c6b37; cert=RF1bHkwrhnlIUA1pNfC75Lh5HCbk2wsI; data_bizuin=3209453905; bizuin=3212452708; data_ticket=bUND8+GzLP4xbLeDGGWMkEaYCLO5ZDeVDL3lWvm6mNKu6MbdSYjfkDoycnKJ+vyH; slave_sid=c3k3NE9MYzFHS1hoVFVCZU84ZzJCSUx5ZVgzNTRTRVUyZkRvWHY2dXFOb2R5amM0cjc4YTJTcnRxU2FmR1ZVQ1pqMXpXVEU2eWN0RHJQQjVKTzdjOVNSVWpFQjJHTndnZ0trTmN1QjJJTThZb0diRjM1UDZYZ1JSM05zY3NLU3h1U29NNzdrUVk2ODdNV2Vp; slave_user=gh_e7cf4c8c6b37; xid=f6104e57f4d05e1f2f6fc2d872fa5808; openid2ticket_oUWFMv6qhShRZSgTN4p8LBOcYFCk=4/8hYgqwQISZsxvtUDzLlEYVrab9OoYhXNKQXiJiUzQ=; Hm_lpvt_0bd5902d44e80b78cb1cd01ca0e85f4a=1526529897";
        String[] s = cookie.split(";");
        for (String string : s) {
            String[] ss = string.split("=");
            if(ss.length>1) {
                //System.out.println(ss[0].trim()+":"+ss[1].trim());
                map.put(ss[0].trim(),ss[1].trim());
            }else {
                //System.out.println(ss[0].trim()+":"+"");
                map.put(ss[0].trim(),"");
            }
        }
        return map;
    }
}
