package net.kernal.spiderman.worker.extract.extractor.impl;

import java.util.Collection;

import net.kernal.spiderman.kit.K;
import net.kernal.spiderman.kit.Properties;
import net.kernal.spiderman.worker.download.Downloader;
import net.kernal.spiderman.worker.extract.ExtractTask;
import net.kernal.spiderman.worker.extract.extractor.Extractor;
import net.kernal.spiderman.worker.extract.extractor.Extractor.Callback.FieldEntry;
import net.kernal.spiderman.worker.extract.extractor.Extractor.Callback.ModelEntry;
import net.kernal.spiderman.worker.extract.schema.Field;
import net.kernal.spiderman.worker.extract.schema.Model;
import net.kernal.spiderman.worker.extract.util.URLKit;

/**
 * 链接抽取器
 * @author 赖伟威 l.weiwei@163.com 2016-01-21
 *
 */
public class LinksExtractor extends Extractor {

	public LinksExtractor(ExtractTask task, String page, Model... models) {
		super(task, page, models);
	}

	public void extract(Callback callback) {
		final Downloader.Response response = getTask().getResponse();
		final Collection<?> urls = URLKit.links(response);
		if (K.isNotEmpty(urls)) {
			final Model model = new Model(getPage(), "");
			Field field = new Field(getPage(), null, "urls");
			field.set("isForNewTask", true);
			field.set("isArray", true);
			field.set("isDistinct", true);
			final FieldEntry fieldEntry = new FieldEntry(field, urls);
			callback.onFieldExtracted(fieldEntry);
			
			final Properties fields = new Properties();
			fields.put("links", fieldEntry.getData());
			fields.put("url", response.getRequest().getUrl());
			final ModelEntry modelEntry = new ModelEntry(0, model, fields);
			callback.onModelExtracted(modelEntry);
		}
	}

}
