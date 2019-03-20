package net.kernal.spiderman.worker.extract.schema.filter;

import net.kernal.spiderman.worker.extract.schema.Field.ValueFilter;
import net.kernal.spiderman.worker.extract.util.URLKit;

public class URLNormalizer implements ValueFilter {

	public String filter(Context ctx) {
		final String url = ctx.getValue();
		return URLKit.normalize(ctx.getRequest().getUrl(), url);
	}

}
