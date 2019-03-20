package net.kernal.spiderman.worker.extract.schema.filter;

import net.kernal.spiderman.worker.extract.schema.Field.ValueFilter;

public class TagToStrFilter implements ValueFilter {

	public String filter(Context ctx) {
		String oldStr = ctx.getValue();
		String newStr = oldStr
			.replace("&", "&amp;")
			.replace(" ", "&nbsp;")
			.replace("<", "&lt;")
			.replace(">", "&gt;")
			.replace("\t\n", "<br />")
			.replace("\r\n", "<br />")
			.replace("\"", "&quot;")
			.replace("'", "&apos;");

        return newStr;
	}
	
}
