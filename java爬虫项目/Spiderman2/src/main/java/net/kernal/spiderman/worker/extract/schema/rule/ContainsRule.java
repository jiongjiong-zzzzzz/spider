package net.kernal.spiderman.worker.extract.schema.rule;

import net.kernal.spiderman.worker.download.Downloader;

public class ContainsRule extends UrlMatchRule {
	
	private String chars;

	public ContainsRule(String chars) {
		if (chars == null) {
			throw new RuntimeException("chars can not be null");
		}
		this.chars = chars.trim();
	}

	public boolean doMatches(Downloader.Request request) {
		return request.getUrl().trim().contains(chars);
	}
	
}