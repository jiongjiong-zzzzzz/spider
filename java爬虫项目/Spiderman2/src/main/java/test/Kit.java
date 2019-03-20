package test;

import java.util.Set;

import org.openqa.selenium.WebDriver;

public class Kit {
	
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
	
	public static void stayAt(String windowName, WebDriver driver) {
		Kit.promise(()->{
			// 干掉弹窗
			Set<String> wins = driver.getWindowHandles();
			for (String w : wins) {
				if (w.equals(windowName)) {
					continue;
				}
				driver.switchTo().window(w);
				driver.close();
			}
			// 回到windowName
			driver.switchTo().window(windowName);
		});
	}
	
	public static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	} 
	
	public static void closeAll(WebDriver driver) {
		try {
			// 干掉弹窗
			Set<String> wins = driver.getWindowHandles();
			for (String w : wins) {
				driver.switchTo().window(w);
				driver.close();
			}
		} finally {
			driver.quit();
		}
	}
	
}
