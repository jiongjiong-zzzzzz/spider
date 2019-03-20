package DATA;
import java.util.List;
import java.util.Map;

public class ExecuteTask {
    public static  void main(String[]args) throws Exception {
        //调用downloadHtml下载网页
        CrawlData crawlData =new CrawlData();
        String url =null;
        url="http://top.chinaz.com/all/index.html";
        System.out.println("开始爬取,请等待.");
        String htmlBody = crawlData.downloadHtml(url);
        System.out.println("爬取成功");
        //将下载的数据进行分析
        List<Map<String,Object>> dataList = Analysis.analysisData(htmlBody);
        System.out.println("数据解析成功");
        for(Map<String,Object> data : dataList){
            StoreData.adds(data);
            System.out.println("存储成功");
        }
    }
}





