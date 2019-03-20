package net.kernal.spiderman.worker.extract.schema.filter;

import net.kernal.spiderman.worker.extract.schema.Field.ValueFilter;

public class StrToTagFilter implements ValueFilter {

	public String filter(Context ctx) {
		String oldStr = ctx.getValue();
		String newStr = oldStr
			.replace("&amp;", "&")
			.replace("&nbsp;", " ")
			.replace("\u00A0", " ")
			.replace("\u0020", " ")
			.replace("&lt;", "<")
			.replace("&gt;", ">")
			.replace("<br />", "\t\n")
			.replace("&quot;", "\"")
			.replace("&apos;", "'");
		

        return newStr;
	}
	
}
