package test;

import net.kernal.spiderman.kit.Context;
import net.kernal.spiderman.kit.K;
import net.kernal.spiderman.worker.download.Downloader;
import net.kernal.spiderman.worker.download.impl.WebDriverDownloader;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ListenerForLoginPage extends Downloader.DelayListener {

	@Override
	public void dobiz(Context ctx) {
		final WebDriverDownloader downloader = (WebDriverDownloader) ctx.getDownloader();
		final WebDriver driver = downloader.getWebDriver();
		// 此处可以delay一下，等页面加载完成再来填表单
		final Long delay = K.convertToMillis(ctx.getParams().getString("worker.download.selector.delay", "0")).longValue();
		if (delay > 0) {
			try {
				Thread.sleep(delay);
			} catch (Throwable e) {}
		}
		
		final String usernameXpath = ctx.getParams().getString("username.xpath");
		final WebElement ue = driver.findElement(By.xpath(usernameXpath));
		final String username = ctx.getParams().getString("username.value");
		ue.sendKeys(username);
		
		final String passwordXpath = ctx.getParams().getString("password.xpath");
		final WebElement pe = driver.findElement(By.xpath(passwordXpath));
		final String pwd = ctx.getParams().getString("password.value");
		pe.sendKeys(pwd);
		
		final String submitBtnXpath = ctx.getParams().getString("submit.xpath");
		final WebElement commit = driver.findElement(By.xpath(submitBtnXpath));
		commit.click();
		//done
	}


}
