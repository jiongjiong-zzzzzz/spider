package net.kernal.spiderman.worker.download.impl;

import org.apache.http.HttpStatus;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import net.kernal.spiderman.kit.K;
import net.kernal.spiderman.kit.Properties;
import net.kernal.spiderman.worker.download.Downloader;

public class HtmlUnitDownloader extends Downloader {

	private WebClient client;
	
	public HtmlUnitDownloader() {
		this(new Properties());
	}
	
	public HtmlUnitDownloader(Properties props) {
		final String proxyHost = props.getString("downloader.proxy.host");
		final int proxyPort = props.getInt("downloader.proxy.port");
		if (K.isNotBlank(proxyHost)) {
			this.client = new WebClient(BrowserVersion.BEST_SUPPORTED, proxyHost, proxyPort);
		} else {
			this.client = new WebClient(BrowserVersion.BEST_SUPPORTED);
		}
	}
	
	@Override
	public Response download(Request request) {
		final Downloader.Response response = new Downloader.Response(request);
		try {
			HtmlPage page = this.client.getPage(request.getUrl());
			// get status
			WebResponse resp = page.getWebResponse();
			int statusCode = resp.getStatusCode();
			String statusDesc = resp.getStatusMessage();
			response.setStatusCode(statusCode);
			response.setStatusDesc(statusDesc);
			// get redirect location
			final String location = resp.getResponseHeaderValue("Location");
			if (K.isNotBlank(location) && (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY)) 
				response.setLocation(location);
			
			// content type and charset
			response.setCharset(resp.getContentCharset());
			response.setMimeType(resp.getContentType());
			// body
			byte[] body = K.toByteArray(resp.getContentAsStream());
			response.setBody(body);
			resp = null;
		} catch (Throwable e) {
			response.setException(e);
		} finally {
			
		}
		
		return response;
	}

	@Override
	public Downloader keepHeader(Header header) {
		this.client.addRequestHeader(header.getName(), header.getValue());
		return this;
	}

	@Override
	public Downloader keepCookie(Cookie cookie) {
		//设置Cookie
        this.client.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie(cookie.getDomain(), cookie.getName(), cookie.getValue(), cookie.getPath(), cookie.getExpiryDate(), cookie.isSecure()));
		return this;
	}
	
	@Override
	public void close() {
		this.client.close();
	}
	


}
