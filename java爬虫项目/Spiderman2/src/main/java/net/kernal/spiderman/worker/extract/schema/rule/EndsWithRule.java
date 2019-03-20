package net.kernal.spiderman.worker.extract.schema.rule;

import net.kernal.spiderman.worker.download.Downloader;

public class EndsWithRule extends UrlMatchRule {
	
	private String suffix;

	public EndsWithRule(String suffix) {
		if (suffix == null) {
			throw new RuntimeException("suffix can not be null");
		}
		this.suffix = suffix;
	}

	protected boolean doMatches(Downloader.Request request) {
		return request.getUrl().trim().endsWith(suffix);
	}
	
}