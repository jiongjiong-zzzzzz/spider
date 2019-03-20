package net.kernal.spiderman.kit;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import net.kernal.spiderman.Config;
import net.kernal.spiderman.Config.Pages;
import net.kernal.spiderman.Config.Seeds;
import net.kernal.spiderman.Spiderman;
import net.kernal.spiderman.worker.extract.ExtractTask;
import net.kernal.spiderman.worker.extract.extractor.Extractor;
import net.kernal.spiderman.worker.extract.extractor.Extractor.Callback;
import net.kernal.spiderman.worker.extract.extractor.impl.XMLExtractor;
import net.kernal.spiderman.worker.extract.schema.Field;
import net.kernal.spiderman.worker.extract.schema.Model;
import net.kernal.spiderman.worker.extract.schema.Page;
import net.kernal.spiderman.worker.extract.schema.filter.ScriptableFilter;
import net.kernal.spiderman.worker.extract.schema.rule.ContainsRule;
import net.kernal.spiderman.worker.extract.schema.rule.EndsWithRule;
import net.kernal.spiderman.worker.extract.schema.rule.EqualsRule;
import net.kernal.spiderman.worker.extract.schema.rule.RegexRule;
import net.kernal.spiderman.worker.extract.schema.rule.StartsWithRule;

/**
 * 建议深入看看哦～
 *
 * @author 赖伟威 l.weiwei@163.com 2015-12-28
 */
public class XMLConfBuilder extends DefaultConfBuilder {

    private Extractor extractor;

    public XMLConfBuilder(String file) {
        super();
        this.extractor = new XMLExtractor(file);

        // Property模型
        Model property = new Model("property")
                .set("xpath", "//property")
                .set("isAutoExtractAttrs", true);

        // Seed模型
        Model seed = new Model("seed")
                .set("xpath", "//seed")
                .set("isAutoExtractAttrs", true);
        seed.addField("text").set("xpath", "./text()");

        // Script模型
        Model script = new Model("script")
                .set("xpath", "//script")
                .set("isAutoExtractAttrs", true);
        script.addField("text").set("xpath", "./text()");

        // Extractor定义
        Model extractors = new Model("extractor")
                .set("xpath", "//extractor[@name]")
                .set("isArray", true)
                .set("isAutoExtractAttrs", true);

        // Filter定义
        Model filters = new Model("filter")
                .set("xpath", "//filter[@name]")
                .set("isArray", true)
                .set("isAutoExtractAttrs", true);

        // Page模型
        Model page = new Model("extract-page")
                .set("xpath", "//page")
                .set("isArray", true)
                .set("isAutoExtractAttrs", true);
        // URL match rule
        Field urlMatchRule = page.addField("url-match-rule")
                .set("xpath", ".//url-match-rule")
                .set("isAutoExtractAttrs", true);
        urlMatchRule.addField("text").set("xpath", "./text()");
        // models
        Field model = page.addField("model")
                .set("xpath", ".//model")
                .set("isAutoExtractAttrs", true)
                .set("isArray", true);
        // fields
        Field field = model.addField("field")
                .set("xpath", "./field")
                .set("isAutoExtractAttrs", true)
                .set("isArray", true);
        // filters
        Field filter = field.addField("filters")
                .set("xpath", "./filter[@type]")
                .set("isAutoExtractAttrs", true)
                .set("isArray", true);
        filter.addField("text").set("xpath", "./text()");
        
        // childFields
        Field childField = field.addField("field")
                .set("xpath", "./field")
                .set("isAutoExtractAttrs", true)
                .set("isArray", true);
        
        // childFilters
        Field childFilter = childField.addField("filters")
                .set("xpath", "./filter[@type]")
                .set("isAutoExtractAttrs", true)
                .set("isArray", true);
        childFilter.addField("text").set("xpath", "./text()");
        

        // 抽取器
        extractor.addModel(property);
        extractor.addModel(seed);
        extractor.addModel(script);
        extractor.addModel(extractors);
        extractor.addModel(filters);
        extractor.addModel(page);
    }

    public XMLConfBuilder addSeed(String url) {
        this.conf.addSeed(url);
        return this;
    }

    public XMLConfBuilder addSeed(String name, String url) {
        this.conf.addSeed(name, url);
        return this;
    }

    public XMLConfBuilder addPage(Page page) {
        this.conf.getPages().add(page);
        return this;
    }

    public XMLConfBuilder set(String key, Object value) {
        this.conf.getParams().put(key, value);
        return this;
    }

    public Config build() {
        super.build();
        // 开始解析
        final AtomicReference<String> defaultExtractorNames = new AtomicReference<>();
        extractor.extract(new Callback() {
            public void onModelExtracted(ModelEntry entry) {
                final Properties fields = entry.getFields();
                switch (entry.getModel().getName()) {
                    case "property":
                        conf.set(fields.getString("key"), fields.getString("value", fields.getString("text")));
                        break;
                    case "seed":
                        conf.addSeed(fields.getString("name"), fields.getString("url", fields.getString("text")));
                        break;
                    case "script":
                        final String bindingsClassName = fields.getString("bindings");
                        if (K.isNotBlank(bindingsClassName)) {
                            Class<Config.ScriptBindings> bindingsClass = K.loadClass(bindingsClassName);
                            if (bindingsClass != null) {
                                Config.ScriptBindings bindings;
                                try {
                                    bindings = bindingsClass.newInstance();
                                } catch (InstantiationException | IllegalAccessException e) {
                                    throw new Spiderman.Exception("实例化" + bindingsClassName + "失败", e);
                                }
                                conf.setScriptBindings(bindings);
                            }
                        }
                        conf.setScript(fields.getString("value", fields.getString("text")));
                        break;
                    case "extractor":
                        final String name = fields.getString("name");
                        final String className = fields.getString("class");
                        final boolean isDefault = fields.getBoolean("isDefault", false);
                        if (K.isBlank(name) || K.isBlank(className)) {
                            break;
                        }
                        if (K.isBlank(defaultExtractorNames.get()) && isDefault) {
                            defaultExtractorNames.set(name);
                        }
                        final Class<Extractor> cls = K.loadClass(className);
                        conf.registerExtractor(name, cls);
                        break;
                    case "filter":
                        final String name2 = fields.getString("name");
                        final String className2 = fields.getString("class");
                        if (K.isBlank(name2) || K.isBlank(className2)) {
                            break;
                        }
                        final Class<Field.ValueFilter> ftCls = K.loadClass(className2);
                        Field.ValueFilter filter;
                        try {
                            filter = ftCls.newInstance();
                        } catch (Exception e) {
                            throw new Spiderman.Exception("过滤器[class=" + ftCls.getName() + "]实例化失败", e);
                        }
                        conf.registerFilter(name2, filter);
                        break;
                    case "extract-page":
                        final String pageName = fields.getString("name");
                        final Page page = new Page(pageName) {
                            public void config(UrlMatchRules rules, Models models) {
                            }
                        };
                        final boolean isPersisted = fields.getBoolean("isPersisted", false);
                        page.setIsPersisted(isPersisted);
                        // 处理extractor
                        final String extractorName = fields.getString("extractor", defaultExtractorNames.get());
                        handleExtractor(page, extractorName);

                        // 处理<page filter="">
                        final String filterName = fields.getString("filter");
                        if (K.isNotBlank(filterName)) {
                            Field.ValueFilter ft = conf.getFilters().all().get(filterName);
                            if (ft == null) {
                                throw new Spiderman.Exception("页面[name=" + pageName + "]指定的的filter[name=" + filterName + "]不存在");
                            }
                            // 设置页面内的全局过滤器，每个Model和Field都要执行
                            page.setFilter(ft);
                        }

                        // handle url match rule
                        final Properties rule = fields.getProperties("url-match-rule");
                        if (rule == null) {
                            throw new Spiderman.Exception("页面[name=" + pageName + "]缺少URL匹配规则的配置");
                        }
                        String type = rule.getString("type", "");
                        final boolean isNegativeEnabled = type.startsWith("!");
                        if (isNegativeEnabled) {
                            type = type.substring(1);
                        }

                        final String value = rule.getString("value", rule.getString("text"));
                        switch (type) {
                            case "equals":
                                page.getRules().add(new EqualsRule(value).setNegativeEnabled(isNegativeEnabled));
                                break;
                            case "regex":
                                page.getRules().add(new RegexRule(value).setNegativeEnabled(isNegativeEnabled));
                                break;
                            case "startsWith":
                                page.getRules().add(new StartsWithRule(value).setNegativeEnabled(isNegativeEnabled));
                                break;
                            case "endsWith":
                                page.getRules().add(new EndsWithRule(value).setNegativeEnabled(isNegativeEnabled));
                                break;
                            case "contains":
                                page.getRules().add(new ContainsRule(value).setNegativeEnabled(isNegativeEnabled));
                                break;
                        }
                        // handle model
                        List<Properties> models = fields.getListProperties("model");
                        if (K.isNotEmpty(models)) {
                            models.forEach(mdl -> {
                                final String modelName = mdl.getString("name");
                                final Model model = page.getModels().addModel(modelName);
                                model.putAll(mdl);
                                final List<Properties> _fields = mdl.getListProperties("field");
                                extractField(model, pageName, modelName, _fields);
                            });
                        }
                        // add page
                        conf.addPage(page);
                        break;
                }
            }

            private void extractField(Model model, String pageName, String modelName ,List<Properties> fields ) {
            	if (K.isNotEmpty(fields)) {
            		fields.forEach(f -> {
                        final String fieldName = f.getString("name");
                        final Field field = model.addField(fieldName);
                        field.putAll(f);
                        // 处理Filters
                        String ftName = f.getString("filter");
                        if (K.isNotBlank(ftName)) {
                            Field.ValueFilter rft = conf.getFilters().all().get(ftName);
                            if (rft == null) {
                                throw new Spiderman.Exception("页面[name=" + pageName + "].模型[name=" + modelName + "].Field[name=" + fieldName + "]配置的属性[filter=" + ftName + "]不存在");
                            }
                            field.addFilter(rft);
                        }
                        final List<Properties> filters = f.getListProperties("filters");
                        if (K.isNotEmpty(filters)) {
                            filters.forEach(ft -> {
                                switch (ft.getString("type")) {
                                    case "script":
                                        field.addFilter(new ScriptableFilter(ft.getString("value", ft.getString("text"))));
                                        break;
                                }
                            });
                        }
                        final List<Properties> newFields = f.getListProperties("field");
                        if (K.isNotEmpty(newFields)) {
                        	extractField(field, pageName, modelName, newFields);
                        }
                    });
                }
            }
            
            private void extractField(Field parentField, String pageName, String modelName ,List<Properties> fields ) {
            	if (K.isNotEmpty(fields)) {
            		fields.forEach(f -> {
                        final String fieldName = f.getString("name");
                        final Field field = parentField.addField(fieldName);
                        field.putAll(f);
                        // 处理Filters
                        String ftName = f.getString("filter");
                        if (K.isNotBlank(ftName)) {
                            Field.ValueFilter rft = conf.getFilters().all().get(ftName);
                            if (rft == null) {
                                throw new Spiderman.Exception("页面[name=" + pageName + "].模型[name=" + modelName + "].Field[name=" + fieldName + "]配置的属性[filter=" + ftName + "]不存在");
                            }
                            field.addFilter(rft);
                        }
                        final List<Properties> filters = f.getListProperties("filters");
                        if (K.isNotEmpty(filters)) {
                            filters.forEach(ft -> {
                                switch (ft.getString("type")) {
                                    case "script":
                                        field.addFilter(new ScriptableFilter(ft.getString("value", ft.getString("text"))));
                                        break;
                                }
                            });
                        }
                    });
                }
            }
            
            public void onFieldExtracted(FieldEntry entry) {
            }
        });
        return conf;
    }

    private void handleExtractor(Page page, String extractorName) {
        final String pageName = page.getName();
        if (K.isBlank(extractorName)) {
            throw new Spiderman.Exception("页面[name=" + pageName + "]必须指定解析器");
        }
        final Map<String, Class<Extractor>> extractors = conf.getExtractors().all();
        final Class<? extends Extractor> extractorClass = extractors.get(extractorName);
        if (extractorClass == null) {
            throw new Spiderman.Exception("页面[name=" + pageName + "]指定的extractor[name=" + extractorName + "]不存在");
        }
        final Constructor<? extends Extractor> ct;
        try {
            ct = extractorClass.getConstructor(ExtractTask.class, String.class, Model[].class);
        } catch (Exception e) {
            throw new Spiderman.Exception("页面[name=" + pageName + "]指定的extractor[class=" + extractorClass.getName() + "]的构造器不满足要求", e);
        }
        if (ct == null) {
            throw new Spiderman.Exception("页面[name=" + pageName + "]指定的extractor[class=" + extractorClass.getName() + "]的构造器不满足要求");
        }
        page.setExtractorBuilder((t, p, mds) -> {
            try {
                return ct.newInstance(t, p, mds);
            } catch (Exception e) {
                throw new Spiderman.Exception("页面[name=" + pageName + "]指定的extractor[class=" + extractorClass.getName() + "]实例化失败", e);
            }
        });
    }

    public void configParams(Properties params) {
    }

    public void configSeeds(Seeds seeds) {
    }

    public void configPages(Pages pages) {
    }

    public void configBindings(Map<String, Object> bindings) {
    }

}
