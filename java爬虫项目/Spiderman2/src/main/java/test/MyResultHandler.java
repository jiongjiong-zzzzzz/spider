package test;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sleepycat.je.utilint.Timestamp;

import net.kernal.spiderman.kit.Context;
import net.kernal.spiderman.kit.Counter;
import net.kernal.spiderman.kit.K;
import net.kernal.spiderman.kit.Properties;
import net.kernal.spiderman.logger.Logger;
import net.kernal.spiderman.logger.Loggers;
import net.kernal.spiderman.queue.Queue;
import net.kernal.spiderman.queue.Queue.Element;
import net.kernal.spiderman.worker.extract.ExtractResult;
import net.kernal.spiderman.worker.result.ResultTask;
import net.kernal.spiderman.worker.result.handler.ResultHandler;

/**
 * 结果处理器
 * @author 赖伟威 l.weiwei@163.com 2016-01-19
 *
 */
public class MyResultHandler implements ResultHandler {

	private final static Logger logger = Loggers.getLogger(MyResultHandler.class);
	/** 结果计数器 */
	private final AtomicLong counter = new AtomicLong(0);
	
	public void handle(ResultTask task, Counter c) {
		// context对象利用了接口默认实现方法初始化获得
		final Context ctx = context.get();
		// 获取队列对象，我们要往该队列放结果
		final Queue<Element> queue = ctx.getTaskManager().getQueue("SPIDERMAN_JSON_RESULT");
		final String key = task.getKey();
		final String url = task.getRequest().getUrl();
		final ExtractResult result = task.getResult();
		final String pageName = result.getPageName();
		final String modelName = result.getModelName();
		final Properties fields = result.getFields();
		final String title;
		final String content;
		if ("百度知道内容".equals(pageName)) {
			title = fields.getString("title");
			// merge field
			final List<String> lines = fields.getListString("answers");
			lines.add(fields.getString("question"));
			lines.add(fields.getString("bestAnswer"));
			content = K.join(lines, "\r\n\r\n");
		} else if ("网页内容".equals(pageName)) {
			final String text = fields.getString("text");
			if ("*推测您提供的网页为非主题型网页，目前暂不处理！:-)".equals(text)) {
				return;
			}
			title = fields.getString("title");
			content = text;
		} else {
			return;
		}
		
		final String md5Key = K.md5(key);
		final JSONObject map = new JSONObject();
		map.put("title", title);
		map.put("content", content);
		map.put("url", url);
		map.put("key", key);
		map.put("md5key", md5Key);
		map.put("create_at", new Timestamp(new Date().getTime()));
		map.put("update_at", map.get("create_at"));
		
		final byte[] body = map.toJSONString().getBytes();
		// 往队列放最终结果，提供给其他消费者使用
		queue.append(body);
		final long count = counter.incrementAndGet();
		final String info = String.format("发布第%s个结果[page=%s, model=%s, url=%s]:\r\n %s", count, pageName, modelName, url, JSON.toJSONString(map, true));
		logger.warn(info);
	} 

}
