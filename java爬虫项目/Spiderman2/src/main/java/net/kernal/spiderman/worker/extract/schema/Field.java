package net.kernal.spiderman.worker.extract.schema;

import net.kernal.spiderman.kit.Properties;
import net.kernal.spiderman.kit.Seed;
import net.kernal.spiderman.worker.download.Downloader;
import net.kernal.spiderman.worker.extract.ExtractTask;
import net.kernal.spiderman.worker.extract.extractor.Extractor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Field extends Properties {

    private static final Logger logger = Logger.getLogger(Field.class.getName());

    private static final long serialVersionUID = 7148432045433277575L;

    private String page;
    private String model;
    private String name;
    private List<ValueFilter> filters;

    private List<Field> fields;

    public Field(String page, String model, String name) {
        this.page = page;
        this.model = model;
        this.name = name;
        this.filters = new ArrayList<>();
        this.fields = new ArrayList<>();
    }

    public String getPage() {
        return this.page;
    }

    public String getModel() {
        return this.model;
    }

    public List<Field> getFields() {
        return this.fields;
    }

    public Field addField(String name) {
        Field field = new Field(page, this.name, name);
        this.fields.add(field);
        return field;
    }

    public Model toModel() {
        return new Model(page, name, fields).set("isAutoExtractAttrs", this.getBoolean("isAutoExtractAttrs"));
    }

    public String getName() {
        return this.name;
    }

    public Field set(String k, Object v) {
        this.put(k, v);
        return this;
    }

    public Field addFilter(ValueFilter ft) {
        this.filters.add(ft);
        logger.info("添加字段值过滤器: " + ft);
        return this;
    }

    public List<ValueFilter> getFilters() {
        return this.filters;
    }

    public interface ValueFilter {
        String filter(Context ctx);

        class Context extends Properties {
            private static final long serialVersionUID = -375879158096905566L;
            private String value;
            private Extractor extractor;
            private ExtractTask task;
            private Downloader.Request request;
            private Downloader.Response response;
            private Downloader.Request source;
            private Seed seed;

            public Context(Extractor e, Map<String, Properties> models, String v) {
                this.value = v;
                this.extractor = e;
                this.task = e.getTask();
                this.request = task.getRequest();
                this.response = task.getResponse();
                this.source = task.getSource() == null ? null : task.getSource().getRequest();
                this.seed = task.getSeed();
                this.put("value", v);
                this.put("extractor", e);
                this.put("task", task);
                this.put("request", request);
                this.put("response", response);
                this.put("seed", seed);
                this.put("models", models);
            }

            public String getValue() {
                return this.value;
            }

            public Extractor getExtractor() {
                return this.extractor;
            }

            public static long getSerialversionuid() {
                return serialVersionUID;
            }

            public ExtractTask getTask() {
                return task;
            }

            public Downloader.Request getRequest() {
                return request;
            }

            public Downloader.Response getResponse() {
                return response;
            }

            public Seed getSeed() {
                return seed;
            }

            public Downloader.Request getSource() {
                return source;
            }
        }
    }

    public String toString() {
        return "Field [page=" + page + ", model=" + model + ", name=" + name + "]";
    }

}
