package net.kernal.spiderman.worker.extract.extractor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.kernal.spiderman.Spiderman;
import net.kernal.spiderman.kit.K;
import net.kernal.spiderman.kit.Properties;
import net.kernal.spiderman.worker.extract.ExtractTask;
import net.kernal.spiderman.worker.extract.extractor.Extractor.Callback.FieldEntry;
import net.kernal.spiderman.worker.extract.extractor.Extractor.Callback.ModelEntry;
import net.kernal.spiderman.worker.extract.extractor.impl.HtmlCleanerExtractor;
import net.kernal.spiderman.worker.extract.schema.Field;
import net.kernal.spiderman.worker.extract.schema.Model;

/**
 * 抽象XPath解析 , 所有支持XPath的解析 都要继承此类
 *
 * @author 赖伟  l.weiwei@163.com 2016-01-12
 */
public abstract class AbstractXPathExtractor extends Extractor {

    protected AbstractXPathExtractor(ExtractTask task, String page, Model... models) {
        super(task, page, models);
    }

    protected abstract Object getDoc();

    protected abstract List<Object> extractModel(Object doc, String modelXpath);

    protected abstract List<Object> extractField(Object model, Field field, String defaultValue, 
    		String xpath, String attr, boolean isFromDoc, boolean isSerialize);

    protected abstract Map<String, String> extractAttributes(Object node);

    public void extract(Callback callback) {
        List<Model> models = getModels();
        if (K.isEmpty(models)) {
            throw new Spiderman.Exception("请添加抽取模型配置");
        }

        models.forEach(model -> {
            final String modelXpath = model.getString("xpath");
            final Object doc = getDoc();
            List<Object> mNodes = new ArrayList<>();
            if (K.isNotBlank(modelXpath)) {
                // 抽取模型
                List<Object> mds = this.extractModel(doc, modelXpath);
                if (mds != null) {
                    mNodes.addAll(mds);
                }
            } else {
                mNodes.add(doc);
            }
            for (int i = 0; i < mNodes.size(); i++) {
            	Object mNode = mNodes.get(i);
                final Properties fields = this.extractModel(mNode, model, callback);
                // 通知回调
                callback.onModelExtracted(new ModelEntry(i, model, fields));
            }
        });
    }

    private Properties extractModel(Object mNode, Model model, Callback callback) {
        final Properties fields = new Properties();
        if (model.getBoolean("isAutoExtractAttrs", false)) {
            // extract all attributes
            Map<String, String> attrs = this.extractAttributes(mNode);
            if (attrs != null) {
                attrs.forEach((k, v) -> {
                    fields.put(k, v);
                });
            }
        }
        model.getFields().forEach(field -> {
        	final String defaultValue = field.getString("value", null);
            final String xpath = field.getString("xpath", ".");
            final String attr = field.getString("attribute", field.getString("attr"));
            final boolean isSerialize = field.getBoolean("isSerialize", false);
            final boolean isFromDoc = field.getBoolean("isFromDoc", false);
            final boolean isAutoExtractAttrs = field.getBoolean("isAutoExtractAttrs", false);
           
            // 抽取字段
            List<Object> values = this.extractField(mNode, field, defaultValue, xpath, attr, isFromDoc, isSerialize);
            
            if (K.isEmpty(values)) {
                return;
            }
            
            // 若字段包含子字段，则进入递归抽取
            if (isAutoExtractAttrs || K.isNotEmpty(field.getFields())) {
                List<Object> subValues = new ArrayList<>();
                values.forEach(node -> {
                    Properties _fields = this.extractModel(node, field.toModel(), callback);
                    subValues.add(_fields);
                });
                values = subValues;
            }
            
            final boolean isArray = field.getBoolean("isArray", false);
            final FieldEntry entry = new FieldEntry(field, values);
            entry.setData(isArray ? values : values.get(0));
            if (K.isEmpty(field.getFields()) || !(this instanceof HtmlCleanerExtractor)) {
            	callback.onFieldExtracted(entry); //进行字段过滤和新URL入队列
            }
            fields.put(field.getName(), entry.getData());
        });
        return fields;
    }

}
