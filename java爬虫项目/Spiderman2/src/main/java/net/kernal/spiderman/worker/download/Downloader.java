package net.kernal.spiderman.worker.download;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import net.kernal.spiderman.kit.Context;
import net.kernal.spiderman.kit.K;
import net.kernal.spiderman.kit.Properties;
import net.kernal.spiderman.kit.Seed;

/**
 * 下载器抽象
 * @author 赖伟威 l.weiwei@163.com 2016-01-16
 *
 */
public abstract class Downloader {

	protected Listener listener = new DelayListener(){public void dobiz(Context ctx){}};
	
	public abstract static class DelayListener implements Listener {
		public abstract void dobiz(Context ctx);
		public void init(Context context) {
			final Downloader downloader = context.getDownloader();
			final Seed seed = context.getSeeds().all().get(0);
			final Downloader.Request request = new Downloader.Request(seed.getUrl());
			downloader.download(request);
			this.dobiz(context);
			final Long delay = K.convertToMillis(context.getParams().getString("worker.download.listener.delay", "0")).longValue();
			if (delay > 0) {
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
				}
			}
		}
		public void beforeDownload(Downloader downloader, Request request) {}
		public void afterDownload(Downloader downloader, Response response) {}
	}
	public void setListener(Listener listener) {
		if (listener != null) {
			this.listener = listener;
		}
	}
	
	public Listener getListener() {
		return this.listener;
	}
	
	/**
	 * 不允许覆盖，不允许包外访问此方法
	 */
	final Response doDownload(Request request) {
		this.listener.beforeDownload(this, request);
		final Response response = this.download(request);
		this.listener.afterDownload(this, response);
		return response;
	}
	
	public abstract Response download(Request request);
	
	public abstract Downloader keepHeader(Downloader.Header header);

	public abstract Downloader keepCookie(Downloader.Cookie cookie);
	
	public abstract void close();
	
	/**
	 * 监听器，提供给开发者个性化处理能力
	 * @author 赖伟威 l.weiwei@163.com 2016-04-01
	 */
	public static interface Listener {
		
		public void init(Context context);
		public void beforeDownload(Downloader downloader, Downloader.Request request);
		public void afterDownload(Downloader downloader, Downloader.Response response);
		
	}
	
	/**
	 * HTTP请求
	 * @author 赖伟威 l.weiwei@163.com 2015-12-10
	 *
	 */
	public static class Request extends Properties implements Serializable {
		
		private static final long serialVersionUID = -5271854259417049190L;

		public Request(String url) {
			this(url, "GET");
		}
		
		/**
		 * 实例化
		 * @param url 请求链接地址
		 * @param httpMethod 请求方法 GET,POST,PUT,DELETE,HEAD等等
		 */
		public Request(String url, String httpMethod) {
			this.url = url;
			this.method = httpMethod;
			try {
				final URL u = new URL(url);
				StringBuilder sb = new StringBuilder(u.getProtocol());
				sb.append("://").append(u.getHost());
				if (u.getPort() > 0 && u.getPort() != 80) {
					sb.append(":").append(u.getPort()+"");
				}
				baseUrl = sb.append("/").toString();
			} catch (MalformedURLException e) {
			}
		}
		
		/**
		 * 请求链接地址
		 */
		private String url;
		
		private String baseUrl;
		
		/**
		 * 请求方法GET,POST,PUT,DELETE,HEAD等等
		 */
		private String method;
		
		/**
		 * 请求头参数
		 */
		private List<Header> headers;
		/**
		 * 请求Cookie参数
		 */
		private List<Cookie> cookies;
		
		public String getUrl() {
			return url;
		}
		
		public String getBaseUrl() {
			return this.baseUrl;
		}
		
		public String getMethod() {
			return method;
		}
		
		public List<Header> getHeaders() {
			return headers;
		}
		public List<Cookie> getCookies() {
			return cookies;
		}

		@Override
		public String toString() {
			return "Request [url=" + url + ", method=" + method + ", headers=" + headers + ", cookies=" + cookies + "]";
		}
		
	}
	
	/**
	 * HTTP响应对象
	 * @author 赖伟威 l.weiwei@163.com 2015-12-01
	 *
	 */
	public static class Response implements Serializable {
		
		private static final long serialVersionUID = -9068800067277456934L;

		public Response() {}
		
		public Response(Request request) {
			this.request = request;
		}
		
		public Response(Request request, int statusCode, String statusDesc) {
			this.request = request;
			this.statusCode = statusCode;
			this.statusDesc = statusDesc;
		}
		
		private Request request;
		private int statusCode;
		private String statusDesc;
		//----- 几个常用的header -----
		private String mimeType;
		private String charset;
		private String location;
		//------------------------
		private byte[] body;
		private String bodyStr;// 默认是null
		//------------------------
		private Throwable exception;
		
		public Request getRequest() {
			return request;
		}
		public void setRequest(Request request) {
			this.request = request;
		}
		public int getStatusCode() {
			return statusCode;
		}
		public void setStatusCode(int statusCode) {
			this.statusCode = statusCode;
		}
		public String getStatusDesc() {
			return statusDesc;
		}
		public void setStatusDesc(String statusDesc) {
			this.statusDesc = statusDesc;
		}
		public String getMimeType() {
			return mimeType;
		}
		public void setMimeType(String mimeType) {
			this.mimeType = mimeType;
		}
		public String getCharset() {
			return charset;
		}
		public void setCharset(String charset) {
			this.charset = charset;
		}
		public byte[] getBody() {
			return body;
		}
		public void setBody(byte[] body) {
			this.body = body;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}

		public String getBodyStr() {
			if (bodyStr == null && body != null) {
				bodyStr = K.byteToStringForHtml(body, charset);
			}
			return bodyStr;
		}

		public void setBodyStr(String bodyStr) {
			this.bodyStr = bodyStr;
		}

		public Throwable getException() {
			return exception;
		}

		public void setException(Throwable exception) {
			this.exception = exception;
		}

		@Override
		public String toString() {
			return "Response [request=" + request + ", statusCode=" + statusCode + ", statusDesc=" + statusDesc
					+ ", mimeType=" + mimeType + ", charset=" + charset + ", location=" + location + "]";
		}
	}
	
	public static class Header {
		
		private String name;
		private String value;
		
		public Header(String name, String value) {
			this.name = name;
			this.value = value;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}
	
	public static class Cookie extends Header {
		public Cookie(String name, String value) {
			super(name, value);
		}
		public Cookie(String name, String value, String path) {
			super(name, value);
			this.path = path;
		}
		
		public Cookie(String name, String value, String domain, String path, Date expiryDate, boolean secure) {
			super(name, value);
			this.domain = domain;
			this.path = path;
			this.expiryDate = expiryDate;
			this.secure = secure;
		}
		
		public String getPath() {
			return path;
		}
		public void setPath(String path) {
			this.path = path;
		}
		public Date getExpiryDate() {
			return expiryDate;
		}
		public void setExpiryDate(Date expiryDate) {
			this.expiryDate = expiryDate;
		}
		public boolean isSecure() {
			return secure;
		}
		public void setSecure(boolean secure) {
			this.secure = secure;
		}
		public void setDomain(String domain) {
			this.domain = domain;
		}
		public String getDomain() {
			return this.domain;
		}
		private String domain;
		private String path;
		private Date expiryDate;
		private boolean secure;
	}
	
}
