package wenshu;
import DATA.Analysis;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class getHtml {
    //请求网页
    public static List<String> downloadHtml(List<String> urllist) {
        String body = null;
        List<String> htmllist = new ArrayList();
        System.out.println(urllist.size());
        for(String url:urllist){

            OkHttpClient client = new OkHttpClient();
            //请求
            Request request = new Request.Builder().url(url).build();
            //发起请求
            try {
                Response response = client.newCall(request).execute();
                // body = new String(response.body().bytes());
                byte[] b = response.body().bytes();
                body = new String(b, "GB2312");
                htmllist.add(body);
            } catch (Exception e) {
                e.printStackTrace();
            }
            /*try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
        System.out.println("页面解析成功");
        return htmllist;//取得目标
    }
    //构造url，放入集合
    public  static List<String> getUrl(){
        List urllist = new ArrayList();
        int i = 0;
        String url="";
        for( i=1;i<51;i++) {
            url="http://www.law-lib.com/law/lawml.asp?bbdw=%B2%C6%D5%FE%B2%BF&pages="+i+"&tm1=&tm2=";
            urllist.add(url);
            System.out.println(url);
        }
        return urllist;
    }
    //解析得到的html，获得子URL
    public static List<String> getU(List<String> htmllist){
        List<String>  ulist = new ArrayList();
        for(String html:htmllist){
            Document doc = Jsoup.parse(html);
            Elements elements = doc.select("ul.line2").select("li");
            for (Element elmemt : elements) {
                String href = elmemt.select("a").attr("href");
                href="http://www.law-lib.com/law/"+href;
                ulist.add(href);
                System.out.println(href);
            }
           /* try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
        return ulist;
    }
    //解析子页面，得到内容
    public static List<entiy> getN(List<String> urllist){
        List<String> list=downloadHtml(urllist);
       // Map<String,Object> map1=new HashMap<String,Object>();
       // List<Map<String,Object>> datalist = new ArrayList<Map<String,Object>>();
        List<entiy> enlist = new ArrayList();
        int num = 0 ;
        for(String html:list){
            num++;

            entiy en = new entiy();
            Document doc = Jsoup.parse(html);
            String art = doc.select("div.viewcontent").text();

            en.setArt(art);
            Elements elements = doc.select("ol");
            for (Element elmemt : elements) {
                //颁布时间
                String a = elmemt.select("li:eq(0)").text();
                //标题
                String b = elmemt.select("li:eq(1)").text();
                //发文号
                String c = elmemt.select("li:eq(2)").text();
                //失效时间
                String d = elmemt.select("li:eq(3)").text();
                //颁布单位
                String e = elmemt.select("li:eq(4)").text();
                //法规来源
                String f = elmemt.select("li:eq(5)").text();
                en.setA(a);en.setB(b);en.setC(c);en.setD(d);en.setE(e);en.setF(f);
                System.out.println("第"+num+"条内容获取成功");
            }
           enlist.add(en);
          /*  try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }

        return enlist;
    }
    public void start(){
        ScheduledExecutorService es = Executors.newScheduledThreadPool(5);
        for(int i=0;i<5;i++){
            es.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("线程"
                            + Thread.currentThread().getId()
                            + "开始出发");
                    while (true){
                        List<String> list = getU(downloadHtml( getUrl()));
                        List<entiy> entiyList = getN(list);

                    }
                }
            });
        }
    }


    public static void main(String[] args) throws SQLException{

       /* System.out.println("开始爬取");
        List<String> list = getU(downloadHtml( getUrl()));
        List<entiy> entiyList = getN(list);
        try {
            Class.forName("com.mysql.jdbc.Driver");//加载数据库驱动
            System.out.println("加载数据库驱动成功");
            String url="jdbc:mysql://localhost:3306/wenshu?useUnicode=true&characterEncoding=utf8";//声明数据库test的url
        String user="root";//数据库的用户名
        String password="123";//数据库的密码
        //建立数据库连接，获得连接对象conn(抛出异常即可)
        Connection conn= DriverManager.getConnection(url, user, password);
        System.out.println("连接数据库成功");
        //生成一条mysql语句

        Statement stmt=conn.createStatement();//创建一个Statement对象
            for(entiy en:entiyList){
                PreparedStatement st2 = conn.prepareStatement("insert into wenshu values (?,?,?,?,?,?,?)");
                st2.setString(1,en.getA());
                st2.setString(2,en.getB());
                st2.setString(3,en.getC());
                st2.setString(4,en.getD());
                st2.setString(5,en.getE());
                st2.setString(6,en.getF());
                st2.setString(7,en.getArt());
               try{
                   int a = st2.executeUpdate();

               }catch (SQLException s){
                   s.printStackTrace();
               }


            }
            conn.close();
            System.out.println("关闭数据库成功");

        }
        catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }//
*/
        getHtml get = new getHtml();
        get.start();

    }
}
