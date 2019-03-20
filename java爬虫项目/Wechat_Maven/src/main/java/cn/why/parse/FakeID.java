package cn.why.parse;

import cn.why.client.JsoupClient;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Title:           FakeID
 * Description:     解析公众号的数字id，用来拼接网址
 * Company:         AceGear
 * Author:          henrywang
 * Date:            2018/5/17
 * JDK:             10
 * Encoding:        UTF-8
 */
public class FakeID {
    public String getFakeID(String token,String weChatID){
        //拼接网址，不可更改
        String url = "https://mp.weixin.qq.com/cgi-bin/searchbiz?action=search_biz&token="+token+"&lang=zh_CN&f=json&ajax=1&query="+weChatID;
        JSONObject jso = JSON.parseObject(new JsoupClient().DownBody(url));
        JSONArray jarr = jso.getJSONArray("list");
        JSONObject temp = jarr.getJSONObject(0);
        String fakeid = temp.getString("fakeid");
        String nickname = temp.getString("nickname");
        //System.out.println("fakeid: "+fakeid+"\n\r"+"nickname: "+nickname);
        return fakeid;
    }
}
