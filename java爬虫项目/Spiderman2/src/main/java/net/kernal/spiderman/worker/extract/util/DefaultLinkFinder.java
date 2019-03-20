package net.kernal.spiderman.worker.extract.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultLinkFinder implements LinksFinder {

	private final String content;

	public DefaultLinkFinder(final String content) {
		if (content == null || "".equals(content.trim())) {
			throw new IllegalArgumentException("content cannot be null");
		}
		this.content = content;
	}

	public List<String> getLinks() {
		Pattern pattern = Pattern.compile("(?i)(?s)<\\s*?a.*?href\\s*?=\\s*?[\",'](.*?)[\",'].*?>");
		Matcher matcher = pattern.matcher(content);

		List<String> list = new ArrayList<String>();
		while (matcher.find()) {
			final String link = matcher.group(1);
			if (link.startsWith("javascript:")) {
				continue;
			}
			list.add(link);
		}

		return list;
	}

}
