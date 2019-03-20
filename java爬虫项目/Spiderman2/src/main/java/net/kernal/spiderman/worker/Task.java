package net.kernal.spiderman.worker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import net.kernal.spiderman.kit.Seed;
import net.kernal.spiderman.queue.Queue;
import net.kernal.spiderman.worker.download.Downloader;

public abstract class Task extends Queue.AbstractElement {

	private static final long serialVersionUID = 2506296221733528670L;

	/** 请求 */
	private Downloader.Request request;
	/** 种子 */
	private Seed seed;
	/** 来源 */
	private Task source;
	/** 当前任务新发现的URL列表 */
	private Collection<String> links;
	
	protected Task(Seed seed, Task source, String group, Downloader.Request request) {
		super(group);
		this.seed = seed;
		this.source = source;
		this.request = request;
		this.links = new HashSet<>();
	}
	
	public Task getSource() {
		return this.source;
	}
	
	public String getSourceUrl() {
		return this.source == null ? null : this.source.getRequest().getUrl();
	}
	
	public Collection<String> getLinks() {
		return this.links;
	}
	public Collection<String> getSourceUrlsAndLinks() {
		Collection<String> links = this.getSourceLinks();
		List<String> urls = this.getSourceUrls();
		if (urls != null) {
			links.addAll(urls);
		}
		return links;
	}
	public Collection<String> getSourceLinks() {
		Task source = this.getSource();
		final Collection<String> list = new HashSet<>();
		while (source != null) {
			list.addAll(source.getLinks());
			source = source.getSource();
		}
		
		return list;
	}
	public List<String> getSourceUrls() {
		return this.getSources()
			.parallelStream()
			.map(src -> src.getRequest().getUrl())
			.collect(Collectors.toList());
	}
	public List<Task> getSources() {
		Task source = this.getSource();
		final List<Task> list = new ArrayList<>();
		while (source != null) {
			list.add(source);
			source = source.getSource();
		}
		
		return list;
	}
	
	public Seed getSeed() {
		return this.seed;
	}
	
	public Downloader.Request getRequest() {
		return this.request;
	}
	
}
