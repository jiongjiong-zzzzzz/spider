package net.kernal.spiderman.worker.download;

import net.kernal.spiderman.kit.Seed;
import net.kernal.spiderman.worker.Task;
import net.kernal.spiderman.worker.extract.ExtractTask;

public class DownloadTask extends Task {

	private static final long serialVersionUID = 6126003860229810350L;
	
	public DownloadTask(Seed seed, String group) {
		this(seed, group, new Downloader.Request(seed.getUrl()));
	}
	
	public DownloadTask(ExtractTask task, String group, Downloader.Request request) {
		super(task.getSeed(), task, group, request);
	}
	
	public DownloadTask(Seed seed, String group, Downloader.Request request) {
		super(seed, null, group, request);
	}
	
	public String getKey() {
		return "download_"+getSeed().getUrl()+"#"+getRequest().getUrl();
	}
	
}
