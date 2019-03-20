package net.kernal.spiderman.worker.extract.schema.rule;

import net.kernal.spiderman.worker.download.Downloader;

public class RegexRule extends UrlMatchRule {
	
	private String regex;

	public RegexRule(String regex) {
		if (regex == null) {
			throw new RuntimeException("regex can not be null");
		}
		this.regex = regex.trim();
	}

	protected boolean doMatches(Downloader.Request request) {
		return request.getUrl().trim().matches(this.regex);
	}
	
}