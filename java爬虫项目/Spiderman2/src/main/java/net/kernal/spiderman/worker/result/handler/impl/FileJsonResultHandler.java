package net.kernal.spiderman.worker.result.handler.impl;

import com.alibaba.fastjson.JSON;
import net.kernal.spiderman.kit.Context;
import net.kernal.spiderman.kit.Counter;
import net.kernal.spiderman.kit.K;
import net.kernal.spiderman.worker.extract.ExtractResult;
import net.kernal.spiderman.worker.result.ResultTask;
import net.kernal.spiderman.worker.result.handler.ResultHandler;

import java.io.File;
import java.io.IOException;

public class FileJsonResultHandler implements ResultHandler {

    public void handle(ResultTask task, Counter c) {
        final ExtractResult er = task.getResult();
        final String url = task.getRequest().getUrl();
        final String json = JSON.toJSONString(er.getFields(), true);
        final Context ctx = context.get();
        final String validName = url.replace("\\", "_").replace("/", "_").replace(":", "_").replace("*", "_")
                .replace("?", "_").replace("\"", "_").replace("<", "_").replace(">", "_").replace("|", "_");
        final String fileName = String.format("/json_result_%s_%s_%s_%s.json", c.get(), er.getPageName(),
                er.getModelName(), validName);
        final String path = ctx.getParams().getString("worker.result.store", "store/result") + fileName;
        File file = new File(path);
        try {
            K.writeFile(file, json);
            final String fmt = "\r\n保存第%s个[page=%s, model=%s, url=%s]结果[%s]";
            System.err.println(String.format(fmt, c, er.getPageName(), er.getModelName(), url, file.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}