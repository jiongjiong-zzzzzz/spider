package test;

import java.io.FileNotFoundException;

import com.alibaba.fastjson.JSON;

import net.kernal.spiderman.worker.extract.extractor.Extractor;
import net.kernal.spiderman.worker.extract.extractor.impl.XMLExtractor;
import net.kernal.spiderman.worker.extract.schema.Field;
import net.kernal.spiderman.worker.extract.schema.Model;

/**
 * @author 赖伟威 l.weiwei@163.com
 * @author <a href='http://krbit.github.io'>Krbit</a>
 */
public class TestXMLExtractor {
    // test configure XML
    public static void main(String[] args) throws FileNotFoundException {
        // Property模型
        Model property = new Model("property")
                .set("xpath", "//property")
                .set("isAutoExtractAttrs", true);

        // Seed模型
        Model seed = new Model("seed")
                .set("xpath", "//seed")
                .set("isAutoExtractAttrs", true);
        seed.addField("text").set("xpath", "./text()");

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
        // Models
        Field model = page.addField("model")
                .set("xpath", ".//model")
                .set("isAutoExtractAttrs", true)
                .set("isArray", true);
        // Fields
        Field field = model.addField("field")
                .set("xpath", ".//field")
                .set("isAutoExtractAttrs", true)
                .set("isArray", true);
        // Field's filters
        Field filter = field.addField("filters")
                .set("xpath", ".//filter[@type]")
                .set("isAutoExtractAttrs", true)
                .set("isArray", true);
        filter.addField("text").set("xpath", "./text()");

        // 抽取器
        final Extractor extractor = new XMLExtractor("git@oschina.xml");
        extractor.addModel(property);
        extractor.addModel(seed);
        extractor.addModel(extractors);
        extractor.addModel(filters);
        extractor.addModel(page);
        extractor.extract(new Extractor.Callback() {
            public void onModelExtracted(ModelEntry entry) {
                System.out.println("[model]" + entry.getModel().getName() + "->\r\n" + JSON.toJSONString(entry.getFields(), true) + "\r\n\r\n");
            }

            public void onFieldExtracted(FieldEntry entry) {
//                System.out.println("[field]"+entry.getField().getName() + "->\n\n" + JSON.toJSONString(entry.getValues(), true) + "\r\n\r\n");
            }
        });
    }
}
