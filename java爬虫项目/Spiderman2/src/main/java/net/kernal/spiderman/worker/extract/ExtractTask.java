package net.kernal.spiderman.worker.extract;

import net.kernal.spiderman.worker.Task;
import net.kernal.spiderman.worker.download.DownloadTask;
import net.kernal.spiderman.worker.download.Downloader;

public class ExtractTask extends Task {

	private static final long serialVersionUID = 7914255707423166114L;
	
	private Downloader.Response response;
	
	public ExtractTask(Downloader.Response response) {
		super(null, null, null, response.getRequest());
		this.response = response;
	}
	
	public ExtractTask(DownloadTask task, Downloader.Response response) {
		super(task.getSeed(), task.getSource(), task.getGroup(), response.getRequest());
		this.response = response;
	}
	
	public Downloader.Response getResponse() {
		return this.response;
	}
	
	public String getKey() {
		return "extract_"+getSeed().getUrl()+"#"+response.getRequest().getUrl();
	}
	
}
