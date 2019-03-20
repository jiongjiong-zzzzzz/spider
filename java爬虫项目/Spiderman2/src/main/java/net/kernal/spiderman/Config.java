package net.kernal.spiderman;

import net.kernal.spiderman.kit.Context;
import net.kernal.spiderman.kit.Properties;
import net.kernal.spiderman.kit.Seed;
import net.kernal.spiderman.worker.download.Downloader;
import net.kernal.spiderman.worker.extract.extractor.Extractor;
import net.kernal.spiderman.worker.extract.schema.Field;
import net.kernal.spiderman.worker.extract.schema.Page;
import net.kernal.spiderman.worker.result.handler.ResultHandler;

import javax.script.Bindings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Config {

    private static final Logger logger = Logger.getLogger(Config.class.getName());

    public Config() {
        seeds = new Seeds();
        cookies = new Cookies();
        extractors = new Extractors();
        filters = new Filters();
        pages = new Pages();
        params = new Properties();
        headers = new Headers();
    }

    private Seeds seeds;
    private Cookies cookies;
    private Headers headers;
    private Extractors extractors;
    private Filters filters;
    private Pages pages;
    private Properties params;
    private String script;
    private ScriptBindings bindings;
    private ResultHandler resultHandler;
    private Downloader.Listener downloadListener;

    public interface ScriptBindings {
        void config(Bindings bindings, Context ctx);
    }

    public static class Seeds {
        private List<Seed> seeds;

        public Seeds() {
            this.seeds = new ArrayList<>();
        }

        public Seeds add(Seed seed) {
            this.seeds.add(seed);
            logger.info("添加种子: " + seed);
            return this;
        }

        public Seeds add(String name, String url) {
            return this.add(new Seed(name, url));
        }

        public Seeds add(String url) {
            return this.add(new Seed(url));
        }

        public List<Seed> all() {
            return this.seeds;
        }
    }

    public static class Cookies {
        private List<Downloader.Cookie> cookies;

        public Cookies() {
            this.cookies = new ArrayList<>();
        }

        public Cookies add(Downloader.Cookie cookie) {
            this.cookies.add(cookie);
            logger.info("添加Cookie: " + cookie);
            return this;
        }

        public Cookies add(String name, String value) {
            return this.add(new Downloader.Cookie(name, value));
        }

        public List<Downloader.Cookie> all() {
            return this.cookies;
        }
    }
    
    public static class Headers {
        private List<Downloader.Header> headers;

        public Headers() {
            this.headers = new ArrayList<>();
        }

        public Headers add(Downloader.Header header) {
            this.headers.add(header);
            logger.info("添加Header: " + header);
            return this;
        }

        public Headers add(String name, String value) {
            return this.add(new Downloader.Header(name, value));
        }

        public List<Downloader.Header> all() {
            return this.headers;
        }
    }

    public static class Extractors {
        private Map<String, Class<Extractor>> extractors;

        public Extractors() {
            extractors = new HashMap<>();
        }

        public Extractors register(String name, Class<Extractor> extractor) {
            this.extractors.put(name, extractor);
            logger.info("注册解析器类: " + name + ", " + extractor.getName());
            return this;
        }

        public Map<String, Class<Extractor>> all() {
            return this.extractors;
        }
    }

    public static class Filters {
        private Map<String, Field.ValueFilter> filters;

        public Filters() {
            filters = new HashMap<>();
        }

        public Filters register(String name, Field.ValueFilter ft) {
            this.filters.put(name, ft);
            logger.info("注册过滤器: " + name + ", " + ft);
            return this;
        }

        public Map<String, Field.ValueFilter> all() {
            return this.filters;
        }
    }

    public static class Pages {
        private List<Page> pages;

        public Pages() {
            this.pages = new ArrayList<>();
        }

        public Pages add(Page page) {
            this.pages.add(page);
            logger.info("添加页面配置: " + page);
            return this;
        }

        public List<Page> all() {
            return this.pages;
        }
    }

    public interface Builder {
        Config build() throws Exception;
    }

    public Config addSeed(String url) {
        return this.addSeed(new Seed(url));
    }

    public Config addSeed(String name, String url) {
        return this.addSeed(new Seed(name, url));
    }

    public Config addSeed(Seed seed) {
        seeds.add(seed);
        return this;
    }

    public Config addCookie(String name, String value) {
        cookies.add(name, value);
        return this;
    }

    public Config addCookie(Downloader.Cookie cookie) {
        cookies.add(cookie);
        return this;
    }
    
    public Config addHeader(String name, String value) {
        headers.add(name, value);
        return this;
    }

    public Config addHeader(Downloader.Header header) {
        headers.add(header);
        return this;
    }

    public Config registerExtractor(String name, Class<Extractor> extractor) {
        extractors.register(name, extractor);
        return this;
    }

    public Config registerFilter(String name, Field.ValueFilter filter) {
        filters.register(name, filter);
        return this;
    }

    public ScriptBindings getScriptBindings() {
        return this.bindings;
    }

    public Config addPage(Page page) {
        pages.add(page);
        return this;
    }

    public Config set(String paramName, Object value) {
        this.params.put(paramName, value);
        return this;
    }

    public Seeds getSeeds() {
        return seeds;
    }

    public Cookies getCookies() {
        return cookies;
    }
    
    public Headers getHeaders() {
    	return headers;
    }

    public Extractors getExtractors() {
        return extractors;
    }

    public Filters getFilters() {
        return filters;
    }

    public Pages getPages() {
        return pages;
    }

    public Properties getParams() {
        return params;
    }

    public Config setScriptBindings(ScriptBindings bindings) {
        this.bindings = bindings;
        return this;
    }

    public Config setScript(String script) {
        this.script = script;
        return this;
    }

    public String getScript() {
        return this.script;
    }

    public Config setResultHandler(ResultHandler resultHandler) {
        this.resultHandler = resultHandler;
        return this;
    }

    public ResultHandler getResultHandler() {
        return this.resultHandler;
    }

    public Config setDownloadListener(Downloader.Listener downloadListener) {
        this.downloadListener = downloadListener;
        return this;
    }

    public Downloader.Listener getDownloadListener() {
        return this.downloadListener;
    }
}
