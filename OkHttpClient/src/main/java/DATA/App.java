package DATA;



/* 本爬虫利用Jsoup爬取中国大学排血排行榜前100名
 * 并将爬取后的结果存入到MongoDB数据库中
 */

import java.util.List;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.net.*;
import java.io.*;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class App {

    public static void main(String[] args) {
        String url = "http://gaokao.xdf.cn/201702/10612921.html";
        insertIntoMongoDB(url);
    }

    // insertIntoMongoDB()函数：将爬取的表格数据插入到MongoDB中
    public static void insertIntoMongoDB(String url) {
        try{
            // 连接到本地的 mongodb 服务
            MongoClient mongoClient = new MongoClient("localhost", 27017);

            // 连接到university数据库，不过该数据库不存在，则创建university数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase("daxue");
            System.out.println("Connect to database successfully");

            // 创建集合，该集合事先不存在
            mongoDatabase.createCollection("rank");
            System.out.println("集合创建成功");

            // 将爬取的表格数据作为文档分条插入到新建的集合中
            MongoCollection<org.bson.Document> collection = mongoDatabase.getCollection("rank");
            System.out.println("集合 test 选择成功");

            List<String> content = getContent(url);

            for(int i=2; i<content.size(); i++) {
                String[] record = content.get(i).split(" ");

                org.bson.Document document = new org.bson.Document("rank", record[0]).
                        append("name", record[1]).
                        append("province", record[2]).
                        append("local_rank",record[3]).
                        append("score", record[4]).
                        append("type", record[5]).
                        append("stars", record[6]).
                        append("level", record[7]);
                List<org.bson.Document> documents = new ArrayList<org.bson.Document>();
                documents.add(document);
                collection.insertMany(documents);
                System.out.println("第"+i+"条文档插入成功");

            }

            // 关闭mongodb连接
            mongoClient.close();
            System.out.println("MongoDB连接已关闭");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    // getContent()函数,返回爬取的表格数据
    public static List<String> getContent(String url){
        List<String> content = new ArrayList<String>();

        // 利用URL解析网址
        URL urlObj = null;
        try{
            urlObj = new URL(url);

        }
        catch(MalformedURLException e){
            System.out.println("The url was malformed!");
            return content;
        }

        // URL连接
        URLConnection urlCon = null;
        try{
            // 打开URL连接
            urlCon = urlObj.openConnection();
            // 将HTML内容解析成UTF-8格式
            Document doc = Jsoup.parse(urlCon.getInputStream(), "utf-8", url);
            // 刷选需要的网页内容
            Elements elems = doc.getElementsByTag("tbody").first().children();
            // 提取每个字段的文字部分
            content = elems.eachText();

            return content;
        }
        catch(IOException e){
            System.out.println("There was an error connecting to the URL");
            return content;
        }

    }
}


