package net.kernal.spiderman.worker.extract.extractor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.kernal.spiderman.kit.Properties;
import net.kernal.spiderman.worker.extract.ExtractTask;
import net.kernal.spiderman.worker.extract.schema.Field;
import net.kernal.spiderman.worker.extract.schema.Model;

/**
 * 页面提取器
 *
 * @author 赖伟威 l.weiwei@163.com 2016-01-08
 */
public abstract class Extractor {

    private ExtractTask task;
    /**
     * 所属页面名称
     */
    private String page;
    private List<Model> models;

    public Extractor(ExtractTask task, String page, Model... models) {
        this.task = task;
        this.page = page;
        this.models = new ArrayList<>(Arrays.asList(models));
    }

    public Extractor addModel(Model model) {
        this.models.add(model);
        return this;
    }

    public ExtractTask getTask() {
        return this.task;
    }

    public String getPage() {
        return this.page;
    }

    protected List<Model> getModels() {
        return this.models;
    }

    public abstract void extract(Callback callback);

    public interface Builder {
        Extractor build(ExtractTask task, String page, Model... models);
    }

    public interface Callback {

        void onModelExtracted(ModelEntry entry);

        void onFieldExtracted(FieldEntry entry);

        class ModelEntry {
        	private int idx;
            private Model model;
            private Properties fields;

            public ModelEntry(int idx, Model model, Properties fields) {
            	this.idx = idx;
                this.model = model;
                this.fields = fields;
            }
            
            public int getIdx() {
            	return this.idx;
            }

            public Model getModel() {
                return this.model;
            }

            public Properties getFields() {
                return this.fields;
            }
        }

        class FieldEntry {
            private Field field;
            private Collection<?> values;
            private Object data;

            public FieldEntry(Field field, Collection<?> values) {
                this.field = field;
                this.values = values;
            }

            public Field getField() {
                return this.field;
            }

            public Collection<?> getValues() {
                return this.values;
            }

            public void setData(Object data) {
                this.data = data;
            }

            public Object getData() {
                return this.data;
            }
        }
    }

}
