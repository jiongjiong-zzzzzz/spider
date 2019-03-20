package net.kernal.spiderman.worker.extract.schema;

import net.kernal.spiderman.kit.Properties;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Model extends Properties {

    private static final Logger logger = Logger.getLogger(Model.class.getName());

    private static final long serialVersionUID = 17497943540212548L;

    private String page;
    private String name;
    private List<Field> fields;

    public Model(String page, String name, List<Field> fields) {
        this.page = page;
        this.name = name;
        this.fields = fields;
    }

    public Model(String name) {
        this(null, name, new ArrayList<>());
        logger.info("创建模型[" + name + "]");
    }

    public Model(String page, String name) {
        this(page, name, new ArrayList<>());
    }

    public String getPage() {
        return this.page;
    }

    public String getName() {
        return this.name;
    }

    public Model set(String key, Object value) {
        this.put(key, value);
        return this;
    }

    public Field addField(String name) {
        if (name == null) {
            throw new NullPointerException("addField parameter[name] is null");
        }
        Field field = new Field(page, this.name, name);
        this.fields.add(field);
        logger.info("添加字段配置: " + field);
        return field;
    }

    public List<Field> getFields() {
        return this.fields;
    }

    public Field getFieldForNextPageUrl() {
        return this.fields.parallelStream()
                .filter(field -> field.getName().equals(getString("fieldNameForNextPageUrl")))
                .findFirst().orElse(null);
    }

    public Field getFieldForNextPageContent() {
        return this.fields.parallelStream()
                .filter(field -> field.getName().equals(getString("fieldNameForNextPageContent")))
                .findFirst().orElse(null);
    }

    public String toString() {
        return "Model [page=" + page + ", name=" + name + ", fields=" + fields + "]";
    }

}
