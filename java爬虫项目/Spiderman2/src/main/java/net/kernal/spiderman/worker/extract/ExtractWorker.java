package net.kernal.spiderman.worker.extract;

import net.kernal.spiderman.Spiderman;
import net.kernal.spiderman.kit.K;
import net.kernal.spiderman.kit.Properties;
import net.kernal.spiderman.worker.Task;
import net.kernal.spiderman.worker.Worker;
import net.kernal.spiderman.worker.WorkerManager;
import net.kernal.spiderman.worker.WorkerResult;
import net.kernal.spiderman.worker.download.DownloadTask;
import net.kernal.spiderman.worker.download.DownloadWorker;
import net.kernal.spiderman.worker.download.Downloader;
import net.kernal.spiderman.worker.extract.extractor.Extractor;
import net.kernal.spiderman.worker.extract.extractor.Extractor.Callback.ModelEntry;
import net.kernal.spiderman.worker.extract.schema.Field;
import net.kernal.spiderman.worker.extract.schema.Field.ValueFilter;
import net.kernal.spiderman.worker.extract.schema.Model;
import net.kernal.spiderman.worker.extract.schema.Page;
import net.kernal.spiderman.worker.extract.schema.Page.Models;
import net.kernal.spiderman.worker.extract.schema.filter.TrimFilter;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 页面抽取工人
 *
 * @author 赖伟威 l.weiwei@163.com 2016-01-02
 */
public class ExtractWorker extends Worker {

    private List<Page> pages;
    private DownloadWorker downloadWorker;

    public ExtractWorker(Page... pages) {
    	this(Arrays.asList(pages), (Downloader)null);
    }
    
    public ExtractWorker(List<Page> pages, Downloader downloader) {
        this(pages, null, downloader);
    }

    public ExtractWorker(List<Page> pages, DownloadWorker downloadWorker) {
        this(pages, null, downloadWorker);
    }

    public ExtractWorker(List<Page> pages, WorkerManager manager, Downloader downloader) {
        this(pages, manager, new DownloadWorker(downloader, 0));
    }

    public ExtractWorker(List<Page> pages, WorkerManager manager, DownloadWorker downloadWorker) {
        super(manager);
        this.downloadWorker = downloadWorker;
        this.pages = pages;
    }

    @Override
    public void work(Task t) {
        final WorkerManager manager = getManager();
        this.work(t, (page, result) -> {
            if (manager != null) {
                //将工作结果呈报给经理
                final WorkerResult extractResult = new WorkerResult(page, t, result);
                manager.done(extractResult);
            }
        });
    }

    public void work(Task t, Callback callback) {
        final WorkerManager manager = getManager();
        final ExtractTask task = (ExtractTask) t;
        final Collection<String> links = task.getSourceUrlsAndLinks();
        final Downloader.Response response = task.getResponse();
        final String responseBody = response.getBodyStr();
        final Downloader.Request request = response.getRequest();
        this.pages.parallelStream()//多线程来做
                .filter(page -> page.matches(request))//过滤，只要能匹配request的page
                .forEach(page -> {
                    final Extractor.Builder builder = page.getExtractorBuilder();
                    if (builder == null) {
                        throw new Spiderman.Exception("页面[name=" + page.getName() + "]缺少可以构建抽取器的对象，请设置一个 models.setExtractorBuilder");
                    }
                    final String pageName = page.getName();
                    final Models models = page.getModels();
                    final Extractor extractor = builder.build(task, pageName, models.all().toArray(new Model[]{}));

                    final Map<String, Properties> modelsCtx = new HashMap<>();
                    // 执行抽取
                    extractor.extract(new Extractor.Callback() {
                        public void onModelExtracted(ModelEntry entry) {
                        	final int modelIdx = entry.getIdx();
                            String modelName = entry.getModel().getName();
                            if (K.isBlank(modelName)) {
                                modelName = "no-name";
                            }
                            final Properties fields = entry.getFields();
                            modelsCtx.put(modelName, fields);
                            final String key = entry.getModel().getString("key");
                            final ExtractResult result = new ExtractResult(pageName, responseBody, modelName, key, fields);
                            result.setModelIdx(modelIdx);
                            // 处理详细页分页的情况[不是列表页，是详情页，这种分页是把详细内容分成了好几页，所以采集的时候要求要保持顺序才能合并成完整的内容]
                            final Field fieldForNextPageUrl = entry.getModel().getFieldForNextPageUrl();
                            final Field fieldForNextPageContent = entry.getModel().getFieldForNextPageContent();
                            if (fieldForNextPageUrl != null) {
                                final List<String> contents = new ArrayList<String>();
                                // 解析下一页
                                extractNextPage(page, entry, contents, task, builder, this);
                                if (K.isNotEmpty(contents) && fieldForNextPageContent != null) {
                                    result.getFields().put(fieldForNextPageContent.getName(), contents);
                                }
                            }
                            callback.onResultExtracted(page, result);
                        }

                        public void onFieldExtracted(FieldEntry entry) {
                            final Field field = entry.getField();
                            final boolean isArray = field.getBoolean("isArray", false);
                            final boolean isForNewTask = field.getBoolean("isForNewTask", false);
                            final boolean isDistinct = field.getBoolean("isDistinct", false);
                            final boolean isSorted = field.getBoolean("isSorted", false);
                            final Collection<?> values = entry.getValues();

                            // 处理过滤器
                            final List<Field.ValueFilter> filters = new ArrayList<Field.ValueFilter>();
                            if (page.getFilter() != null) {
                                filters.add(page.getFilter());// 设置全局过滤器
                            }
                            filters.add(new TrimFilter());// 添加一个trim过滤器
                            filters.addAll(field.getFilters());// 添加多个子元素配置的过滤器

                            // 执行过滤, 得到过滤后的新值
                            final Collection<String> newValues = new ArrayList<String>(values.size());
                            values.stream()// 此处要保证顺序，因此不能用多线程
                                    .filter(v -> v instanceof String).map(v -> (String) v)// 保证值是String类型
                                    .forEach(v -> {
                                        AtomicReference<String> v2 = new AtomicReference<String>(v);
                                        filters.forEach(ft -> {
                                        	try {
                                        		String nv = ft.filter(new ValueFilter.Context(extractor, modelsCtx, v2.get()));
                                        		v2.set(nv);// 将上一个结果作为下一个参数过滤
                                        	} catch (Throwable e) {
                                        		manager.getLogger().error("value["+v2.get()+"] execute filter["+ft.getClass()+"] failed!", e);
                                        	}
                                        });
                                        final String nv = v2.get();
                                        if (K.isNotBlank(nv)) {
                                            if ((isForNewTask || isDistinct) && newValues.contains(nv)) {
                                                // 不能重复
                                                return;
                                            }

                                            newValues.add(nv);
                                        }
                                    });

                            // 新URL入队列
                            if (isForNewTask) {
                                final Collection<String> newLinks = new HashSet<String>();
                                newValues.parallelStream()
                                        .filter(url -> !request.getUrl().equals(url))// 排除自己
                                        .filter(url -> { // 排除sources和links
                                            final AtomicBoolean repeat = new AtomicBoolean(false);
                                            if (!repeat.get() && K.isNotEmpty(links)) {
                                                for (String link : links) {
                                                    if (link.equals(url)) {
                                                        repeat.set(true);
                                                        break;
                                                    }
                                                }
                                            }
                                            return !repeat.get();
                                        })
                                        .forEach(url -> {
                                            // 收集links
                                            newLinks.add(url);
                                        });
                                if (K.isNotEmpty(newLinks)) {
                                    task.getLinks().addAll(newLinks);
                                    if (manager != null) {
                                        newLinks.parallelStream()
                                                .map(link -> new Downloader.Request(link))
                                                .forEach(req -> manager.done(new WorkerResult(page, task, req)));
                                    }
                                    newValues.clear();
                                    newValues.addAll(newLinks);
                                }
                            }
                            // 设置结果
                            List<String> finalList = new ArrayList<String>(newValues);
                            if (isSorted) {
                                Collections.sort(finalList);
                            }
                            final Object data = isArray ? finalList : finalList.stream().findFirst().orElse(null);
                            entry.setData(data);
                        }
                    });
                });
    }

    private void extractNextPage(final Page page, final ModelEntry entry, final List<String> appender, ExtractTask task, Extractor.Builder builder, Extractor.Callback callback) {
        final Properties fields = entry.getFields();
        final Model model = entry.getModel();
        final Field fieldForNextPageUrl = model.getFieldForNextPageUrl();
        final Field fieldForNextPageContent = model.getFieldForNextPageContent();
        // 将当前页的字段值追加到appender里
        if (fieldForNextPageContent != null) {
            appender.add(fields.getString(fieldForNextPageContent.getName()));
        }

        final String nextPageUrl = fields.getString(fieldForNextPageUrl.getName());
        if (K.isBlank(nextPageUrl)) {
            return;
        }
        // 下载
        final Downloader.Request nextRequest = new Downloader.Request(nextPageUrl);
        final Downloader.Response nextResponse = downloadWorker.download(nextRequest);
        if (nextResponse == null) {
            return;
        }
        final DownloadTask downloadTask = new DownloadTask(task, page.getName(), nextRequest);
        final ExtractTask nextTask = new ExtractTask(downloadTask, nextResponse);
        final Model nextModel = new Model(model.getPage(), model.getName(), Arrays.asList(fieldForNextPageUrl, fieldForNextPageContent));
        nextModel.putAll(model);
        final Extractor nextExtractor = builder.build(nextTask, model.getPage(), nextModel);
        // 解析
        nextExtractor.extract(new Extractor.Callback() {
            public void onModelExtracted(ModelEntry entry) {
                // 递归
                extractNextPage(page, entry, appender, task, builder, callback);
            }

            public void onFieldExtracted(FieldEntry entry) {
                callback.onFieldExtracted(entry);
            }
        });
    }

    public static interface Callback {
        public void onResultExtracted(Page page, ExtractResult result);
    }

}
