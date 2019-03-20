package cn.why.client;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.util.HashMap;
import java.util.Map;
/**
 * Title:           SeleniumClient
 * Description:     利用selenium下载htmlpage;运行系统：Mac os
 * Company:         AceGear
 * Author:          henrywang
 * Date:            2018/5/17
 * JDK:             10
 * Encoding:        UTF-8
 */
public class SeleniumClient {
    public String DownHtml(String url){
        //chromeDriver版本要与你电脑chrome浏览器版本相对应，本工程采用的webDriver为mac os版本，不适用于weindows，如在windows运行，需替换，并更改位置
        System.setProperty("webdriver.chrome.driver","/Users/ouhiroshisakai/Documents/ide-workspace/LaoSiJi_Maven/target/classes/com/ag/chromedriver");
        System.getProperty("user.dir");
        ChromeOptions chromeOptions = new ChromeOptions();
        //设置chrome不显示界面
        chromeOptions.addArguments("--headless");
        //设置头文件（查看浏览器头文件网址：https://httpbin.org/get?show_env=1）
        chromeOptions.addArguments("user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
        chromeOptions.addArguments("Accept=text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        chromeOptions.addArguments("Accept-Encoding=gzip, deflate, br");
        chromeOptions.addArguments("Accept-Language=zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
        chromeOptions.addArguments("Cache-Control=max-age=0");
        //设置浏览器大小，不重要
        chromeOptions.addArguments("--window-size=1920,1080");
        Map<String, Object> prefs = new HashMap<String, Object>();
        //优化加载速度与大小，设置不显示图片浏览
        prefs.put("profile.managed_default_content_settings.images", 2);
        chromeOptions.setExperimentalOption("prefs", prefs);
        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.get(url);
        try {
            //部分网址进入后需等待1秒才加载，如有加载时间更长的网站在此设置等待时间
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String html = driver.getPageSource();
        driver.quit();
        return html;
    }
}
