package net.kernal.spiderman.worker.result.handler;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

import net.kernal.spiderman.kit.Context;
import net.kernal.spiderman.kit.Counter;
import net.kernal.spiderman.worker.result.ResultTask;

public interface ResultHandler {
	
	final AtomicReference<Context> context = new AtomicReference<Context>();

	public default void init(Context ctx) {
		context.set(ctx);
		final String savePath = ctx.getParams().getString("worker.result.store", "store/result");
		final File dir = new File(savePath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	public void handle(ResultTask task, Counter c);
	
}