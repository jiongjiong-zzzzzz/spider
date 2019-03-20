package com.wb.client;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * Title: SeleniumClient Description: 利用selenium下载htmlpage;运行系统：Mac os Company:
 * AceGear Author: henrywang Date: 2018/5/17 JDK: 10 Encoding: UTF-8
 */
public class SeleniumClient {
	public static String getips() {
		String[] ip = { "140.250.152.235:29062", "49.89.86.227:43001" };
		String random = "";
		int index = (int) (Math.random() * ip.length);
		random = ip[index];
		return random;
	}

	public String DownHtml(String url) {
		long waitLoadBaseTime = 3000;
		int waitLoadRandomTime = 5000;
		Random random = new Random(System.currentTimeMillis());
		// chromeDriver版本要与你电脑chrome浏览器版本相对应，本工程采用的webDriver为mac
		// os版本，不适用于weindows，如在windows运行，需替换，并更改位置
		String path = System.getProperty("user.dir");
		 String driverPath = "/usr/bin" + "/chromedriver";
//		String driverPath = path + "/chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", driverPath);
		System.getProperty("user.dir");
		String proxyIpAndPort = getips();
		ChromeOptions chromeOptions = new ChromeOptions();
		// 设置chrome不显示界面
		// chromeOptions.addArguments("proxy-server=" + proxyIpAndPort);
		chromeOptions.addArguments("--headless");
		// 设置头文件（查看浏览器头文件网址：https://httpbin.org/get?show_env=1）
		chromeOptions.addArguments(
				"user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0");
		chromeOptions.addArguments("Accept=text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		chromeOptions.addArguments("Accept-Encoding=gzip, deflate, br");
		chromeOptions.addArguments("Accept-Language=zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
		chromeOptions.addArguments("Host=www.toutiao.com");
		// 设置浏览器大小，不重要
		chromeOptions.addArguments("--window-size=1920,1080");
		Map<String, Object> prefs = new HashMap<String, Object>();
		// 优化加载速度与大小，设置不显示图片浏览
		prefs.put("profile.managed_default_content_settings.images", 2);
		chromeOptions.setExperimentalOption("prefs", prefs);
		WebDriver driver = new ChromeDriver(chromeOptions);
		driver.get(url);
		try {
			Thread.sleep(waitLoadBaseTime + random.nextInt(waitLoadRandomTime));
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		String html = driver.getPageSource();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.quit();
		return html;
	}

	public String DownHtmlXB(String url) {
		long waitLoadBaseTime = 3000;
		int waitLoadRandomTime = 5000;
		Random random = new Random(System.currentTimeMillis());
		// chromeDriver版本要与你电脑chrome浏览器版本相对应，本工程采用的webDriver为mac
		// os版本，不适用于weindows，如在windows运行，需替换，并更改位置
		String path = System.getProperty("user.dir");
		// String driverPath = "/usr/bin" + "/chromedriver";
		String driverPath = path + "/chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", driverPath);
		System.getProperty("user.dir");
		String proxyIpAndPort = getips();
		ChromeOptions chromeOptions = new ChromeOptions();
		// 设置chrome不显示界面
		// chromeOptions.addArguments("proxy-server=" + proxyIpAndPort);
		// chromeOptions.addArguments("--headless");
		// 设置头文件（查看浏览器头文件网址：https://httpbin.org/get?show_env=1）
		chromeOptions.addArguments(
				"user-agent=Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.360");
		// chromeOptions.addArguments("Accept=text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		// chromeOptions.addArguments("Accept-Encoding=gzip, deflate, br");
		// chromeOptions.addArguments("Accept-Language=zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
		// chromeOptions.addArguments("Host=www.toutiao.com");
		// 设置浏览器大小，不重要
		// chromeOptions.addArguments("--window-size=1920,1080");
		// Map<String, Object> prefs = new HashMap<String, Object>();
		// 优化加载速度与大小，设置不显示图片浏览
		// prefs.put("profile.managed_default_content_settings.images", 2);
		// chromeOptions.setExperimentalOption("prefs", prefs);
		WebDriver driver = new ChromeDriver(chromeOptions);
		// String cookies = "Hm_lvt_a19fd7224d30e3c8a6558dcb38c4beed=1530505178;
		// UM_distinctid=1645937ad774cf-0bfa2909447ec8-5e442e19-144000-1645937ad78b5e;
		// CNZZDATA1253878005=354524346-1530502204-%7C1530502204;
		// ticket=gQFG8DwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyTHBlSjFja0ljbTMxYkhSVmhyMWsAAgTbpzlbAwQQDgAA;
		// __root_domain_v=.newrank.cn; _qddaz=QD.ereyug.mzw27e.jj3r9nca; _qdda=3-1.1;
		// _qddab=3-4z7n7q.jj3r9nfi; _qddamta_2852150610=3-0;
		// token=28B7BC08533F56337CD1D65845B132C4; tt_token=true;
		// Hm_lpvt_a19fd7224d30e3c8a6558dcb38c4beed=1530506147";
		// String[] s = cookies.split(";");
		// for (String string1 : s) {
		// String[] ss = string1.split("=");
		// System.out.println(ss[0].trim() + ":" + ss[1].trim());
		// Cookie c1 = new Cookie(ss[0].trim(), ss[1].trim());
		// driver.manage().addCookie(c1);
		// }
		driver.get(url);
		try {
			Thread.sleep(waitLoadBaseTime + random.nextInt(waitLoadRandomTime));
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		String html = driver.getPageSource();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.quit();
		return html;
	}

	public WebDriver getCookies(String path, WebDriver driver) {
		File file = new File(path + "broswer.data");
		try { // delete file if exists
			file.delete();
			file.createNewFile();
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			for (Cookie ck : driver.manage().getCookies()) {
				bw.write(ck.getName() + ";" + ck.getValue() + ";" + ck.getDomain() + ";" + ck.getPath() + ";"
						+ ck.getExpiry() + ";" + ck.isSecure());
				bw.newLine();
			}
			bw.flush();
			bw.close();
			fw.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("cookie write to file");
		}
		try {
			File file1 = new File(path + "broswer.data");
			FileReader fr = new FileReader(file1);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				StringTokenizer str = new StringTokenizer(line, ";");
				while (str.hasMoreTokens()) {
					String name = str.nextToken();
					String value = str.nextToken();
					String domain = str.nextToken();
					String path1 = str.nextToken();
					Date expiry = null;
					String dt;
					if (!(dt = str.nextToken()).equals(null)) {
						expiry = new Date(dt);
						System.out.println();
					}
					boolean isSecure = new Boolean(str.nextToken()).booleanValue();
					Cookie ck = new Cookie(name, value, domain, path1, expiry, isSecure);
					System.out.println(ck.getName() + "\t" + ck.getValue());
					driver.manage().addCookie(ck);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return driver;
	}
}
