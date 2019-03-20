package DATA;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.Map;

public class StoreData{
    public static void main( String args[] ){
    }

    public static void adds(Map<String,Object> dataMap){
        try{
          /* /// 连接到 mongodb 服务String siteRank
            MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase("sit_rank");
            MongoCollection<Document> collection = mongoDatabase.getCollection("information");
            // 连接到本地的 mongodb 服务*/

            MongoClient mongoClient = new MongoClient("localhost", 27017);

            // 连接到university数据库，不过该数据库不存在，则创建university数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase("sit_rank");
            System.out.println("Connect to database successfully");

            // 创建集合，该集合事先不存在
            mongoDatabase.createCollection("information");
            System.out.println("集合创建成功");
            MongoCollection<org.bson.Document> collection = mongoDatabase.getCollection("rank");
            System.out.println("集合 test 选择成功");





            //插入文档
            /**
             * 1. 创建文档 org.bson.Document 参数为key-value的格式
             * 2. 创建文档集合List<Document>
             * 3. 将文档集合插入数据库集合中 mongoCollection.insertMany(List<Document>) 插入单个文档可以用 mongoCollection.insertOne(Document)
             * */
            String siteName=null;String domainName=null;String AlexaRank=null;String Synopsis=null;
            String score=null;String siteRank=null;String webSite=null;String RecordInformation=null;
            JSONObject josn = JSONObject.parseObject(dataMap.toString());
            Document document = new Document(josn);
            document.put("_id",siteName);
            document.append("domainName", domainName);
            document.append("AlexaRank",AlexaRank);
            document.append("Synopsis",Synopsis);
            document.append("score",score);
            document.append("siteRank",siteRank);
            document.append("webSite",webSite);
            document.append("RecordInformation",RecordInformation);
            collection.insertOne(document);
            System.out.println("文档插入成功");

            //关闭mongodb连接
            mongoClient.close();
            System.out.println("MongoDB连接已关闭");
        }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

}
