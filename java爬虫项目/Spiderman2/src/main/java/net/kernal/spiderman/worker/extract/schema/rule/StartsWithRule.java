package net.kernal.spiderman.worker.extract.schema.rule;

import net.kernal.spiderman.worker.download.Downloader;

public class StartsWithRule extends UrlMatchRule {
	
	private String prefix;

	public StartsWithRule(String prefix) {
		if (prefix == null) {
			throw new RuntimeException("regex can not be null");
		}
		this.prefix = prefix;
	}

	protected boolean doMatches(Downloader.Request request) {
		return request.getUrl().trim().startsWith(prefix);
	}
	
}