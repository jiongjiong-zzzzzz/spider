package net.kernal.spiderman.worker.extract.schema.rule;

import net.kernal.spiderman.worker.download.Downloader;

public class AlwaysTrueRule extends UrlMatchRule {

	public boolean doMatches(Downloader.Request request) {
		return true;
	}
	
}