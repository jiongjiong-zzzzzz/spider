package net.kernal.spiderman.worker.result.handler.impl;

import com.alibaba.fastjson.JSON;

import net.kernal.spiderman.kit.Counter;
import net.kernal.spiderman.worker.extract.ExtractResult;
import net.kernal.spiderman.worker.result.ResultTask;
import net.kernal.spiderman.worker.result.handler.ResultHandler;

public class ConsoleResultHandler implements ResultHandler {
	public void handle(ResultTask task, Counter c) {
		final ExtractResult er = task.getResult();
		final String url = task.getRequest().getUrl();
		final String json = JSON.toJSONString(er.getFields(), true);
		final String fmt = "\r\n获取第%s个[page=%s, model=%s, url=%s]结果：\r\n%s\r\n";
		System.err.println(String.format(fmt, c, er.getPageName(), er.getModelName(), url, json));
	}
};