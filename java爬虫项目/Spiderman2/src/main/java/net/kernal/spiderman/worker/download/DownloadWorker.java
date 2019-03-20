package net.kernal.spiderman.worker.download;

import net.kernal.spiderman.Spiderman;
import net.kernal.spiderman.kit.K;
import net.kernal.spiderman.worker.Task;
import net.kernal.spiderman.worker.Worker;
import net.kernal.spiderman.worker.WorkerManager;
import net.kernal.spiderman.worker.WorkerResult;

import java.util.HashSet;
import java.util.Set;

public class DownloadWorker extends Worker {

	private long delay;
	private Downloader downloader;
	/** 保存已经重定向过的URL地址 */
	private Set<String> redirectedLocations;
	public DownloadWorker(Downloader downloader) {
		this(downloader, 0);
	}
	public DownloadWorker(Downloader downloader, long delay) {
		this(null, downloader, delay);
	}
	public DownloadWorker(WorkerManager manager, Downloader downloader, long delay) {
		super(manager);
		this.downloader = downloader;
		this.redirectedLocations = new HashSet<>();
		this.delay = delay;
	}
	
	public Downloader.Response download(Downloader.Request request) {
		if (delay > 0) {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
			}
		}
		final Downloader.Response response = this.downloader.doDownload(request);
		if (response == null) {
			return null;
		}
		// 处理异常
		if (response.getException() != null) {
			throw new Spiderman.Exception("下载["+request.getUrl()+"]失败", response.getException());
		}
		// 处理重定向
		final int statusCode = response.getStatusCode();
		final String location = response.getLocation();
		if (K.isNotBlank(location) && K.isIn(statusCode, 301, 302)) {
			// 递归下载
			if (!redirectedLocations.contains(location)) {
				redirectedLocations.add(location);
				final Downloader.Request newRequest = new Downloader.Request(location);
				return this.download(newRequest);
			}
		}
		if (response.getBody() == null || response.getBody().length == 0) {
			return null;
		}
		// 处理响应体文本编码问题
		String charsetName = K.getCharsetName(response.getCharset());
		if (K.isBlank(charsetName)) {
			// 获取HTML里面的charset
			charsetName = getCharsetFromBodyStr(new String(response.getBody()));
		}
		if (K.isNotBlank(charsetName)) {
			response.setCharset(charsetName);
		}
		// 获取响应体文本内容
		final String bodyStr = K.byteToString(response.getBody(), charsetName); 
		// 若内容为空，结束任务
		if (K.isBlank(bodyStr)) {
			return null;
		}
		response.setBodyStr(bodyStr);
		return response;
	}

	@Override
	public void work(Task t) {
		if (this.downloader == null) {
			throw new Spiderman.Exception("缺少下载器");
		}
		if (t == null) {
			throw new Spiderman.Exception("缺少任务对象");
		}
		final DownloadTask task = (DownloadTask)t;
		final Downloader.Request request = task.getRequest();
		final Downloader.Response response = this.download(request);
		if (response == null) {
			return;
		}
		// 告诉经理完成任务，并将结果传递过去
		this.getManager().done(new WorkerResult(null, task, response));
	}
	
	private String getCharsetFromBodyStr(final String bodyStr) {
		if (K.isBlank(bodyStr)) {
			return null;
		}
		
		String html = bodyStr.trim().toLowerCase();
		String s1 = K.findOneByRegex(html, "(?=<meta ).*charset=.[^/]*");
		if (K.isBlank(s1)) {
			return null;
		}
		
		String s2 = K.findOneByRegex(s1, "(?=charset\\=).[^;/\"']*");
		if (K.isBlank(s2)) {
			return null;
		}
		String charsetName = s2.replace("charset=", "");
		return K.getCharsetName(charsetName);
	}
	
}
