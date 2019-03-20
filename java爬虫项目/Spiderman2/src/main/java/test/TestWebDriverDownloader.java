package test;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * @author 赖伟威 l.weiwei@163.com 2017-07-04
 * @version V0.1.0
 */
public class TestWebDriverDownloader {
	
    public static void main(String[] args) throws Exception {
    	final String[] filePaths = {"c:\\testfile.txt", "e:\\tmp\\hello.txt"};
    	final String username = "laiweiwei";
    	final String chromedriver = "E:/tmp/chromedriver.exe";
        final String loginUrl = "http://sso.cmschina.com.cn/Login.aspx";
        final String homeUrl = "http://home.cmschina.com.cn/UserDesktop/Default.aspx";
        final String writeMailUrl = "http://mail.cmschina.com.cn/mail/MailItemNew.aspx?Username="+username;
        
    	System.getProperties().setProperty("webdriver.chrome.driver", chromedriver);
        final WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        
        // try to visit home
        driver.navigate().to(homeUrl);
        // redirect to login
        Thread.sleep(1000);
        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.equals(loginUrl)) {
        	WebElement une = driver.findElement(By.id("txtUserName"));
        	une.sendKeys(username);
        	
        	promise(()->{
        		String _currentUrl = driver.getCurrentUrl();
        		if (!_currentUrl.equals(homeUrl)) {
        			System.out.println("请登录");
        			throw new RuntimeException("need login");
        		}
        	});
        }
        
        currentUrl = driver.getCurrentUrl();
        System.out.println("登录成功");
        
        // 2. 进入写邮件页面
        driver.navigate().to(writeMailUrl);
        
        // 3. 打开文件选择窗口
        promise(new Runnable() {
			public void run() {
				WebElement uploadAttachBtn = driver.findElement(By.xpath("//div[@id='div_attach']/div/a[2]"));
		        uploadAttachBtn.click();
			}
		});
	        
        
        // 4. 进入iframe
        driver.switchTo().defaultContent();
        List<WebElement> iframe = driver.findElements(By.tagName("iframe"));
        driver.switchTo().frame(iframe.get(0));
        
        // 4. 选择文件
        for (String filePath : filePaths) {
        	promise(new Runnable() {
    			public void run() {
    				List<WebElement> uploadAttachBtn2 = driver.findElements(By.xpath("//div[@class='dhx_file_uploader_button button_browse']"));
    				uploadAttachBtn2.get(0).click();
    			}
    		});
        	
	        promise(new Runnable() {
				public void run() {
					StringSelection stringSelection = new StringSelection(filePath);            
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);  
				}
			});
	        
	        final Robot robot = new Robot();
	        promise(new Runnable() {
				public void run() {
			        robot.keyPress(KeyEvent.VK_CONTROL);  
			        robot.keyPress(KeyEvent.VK_V);  
			        robot.keyRelease(KeyEvent.VK_V);  
			        robot.keyRelease(KeyEvent.VK_CONTROL);
				}
			});
	        
	    	robot.keyPress(KeyEvent.VK_ENTER);
	    	robot.keyRelease(KeyEvent.VK_ENTER);
	    	
	    	Thread.sleep(1000);
        }
    	promise(new Runnable() {
			public void run() {
	    		// 100%
	    		List<WebElement> progressDiv = driver.findElements(By.xpath("//div[@class='dhx_file_param dhx_file_progress']"));
	    		for (int i = 0; i < progressDiv.size(); i++) {
	    			WebElement pd = progressDiv.get(i);
	    			String progress = pd.getText();
	    			if (!"100%".equals(progress)) {
	    				System.out.println("progress["+i+"] => " + progress);
	    				throw new RuntimeException("not fninish!");
	    			}
	    		}
	    	}
		});
    	
    	// last step submit
    	WebElement btnSubmit = driver.findElement(By.id("btnSubmit"));
		btnSubmit.click();
    }
    
    public static void promise(Runnable then) {
		while (true) {
			try {
				Thread.sleep(1000);
				then.run();
				break;
			} catch (Throwable e) {
//				e.printStackTrace();
			}
		}
	}
    
}
