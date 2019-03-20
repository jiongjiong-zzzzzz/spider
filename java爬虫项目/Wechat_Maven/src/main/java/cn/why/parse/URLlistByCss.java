package cn.why.parse;

import cn.why.client.JsoupClient;
import cn.why.pojo.urlList;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
/**
 * Title:           SeleniumClient
 * Description:     url列表页，此处逻辑可以更改，现有逻辑是要把url及标题放入数据库，此时for循环的a<=2改为a<=num，则不断解析此公众号所有列表页标题及网址
 * Company:         AceGear
 * Author:          henrywang
 * Date:            2018/5/17
 * JDK:             10
 * Encoding:        UTF-8
 */
public class URLlistByCss {
    public List<urlList> getURl(String token, String fakeid){
        List<urlList> list = new ArrayList<urlList>();
        int num = 1;
        //此处改为a<=num，则会不断解析url列表，直到最后一页最后一条
        for(int a = 1; a <= 2;a++){
            //微信公众号网址拼接规则，不可更改。
            String url = "https://mp.weixin.qq.com/cgi-bin/appmsg?token="+token+"&lang=zh_CN&f=json&ajax=1&action=list_ex&begin="+(a-1)*5+"&count=5&query=&fakeid="+fakeid+"&type=9";
            JSONObject jso = JSON.parseObject(new JsoupClient().DownBody(url));
            JSONArray jarr=jso.getJSONArray("app_msg_list");
            if(num==1) {
                Integer c = jso.getInteger("app_msg_cnt");
                num = (int) Math.ceil(c/5.0);
            }
            for(int i=0,len=jarr.size();i<len;i++){
                JSONObject temp=  jarr.getJSONObject(i);
                System.out.println("第 "+a+" 页"+"\t"+"第 "+(i+1)+" 条");
                System.out.println("title： "+temp.getString("title")+"\r\n"+"link: "+temp.getString("link")+"\r\n");
                urlList urlList = new urlList();
                urlList.setTitle(temp.getString("title"));
                urlList.setLink(temp.getString("link"));
                list.add(urlList);
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
