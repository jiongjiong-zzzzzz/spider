package test;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import net.kernal.spiderman.worker.extract.extractor.Extractor;
import net.kernal.spiderman.worker.extract.extractor.impl.HtmlCleanerExtractor;
import net.kernal.spiderman.worker.extract.schema.Model;

/**
 * @author 赖伟威 l.weiwei@163.com
 * @version V0.1.0
 */
public class TestHtmlCleanerExtractor {
    public static void main(String[] args) {
        String html = "<html><title>Hello</title><targets>dfd<target name='vivi' /><target name='linda' /></targets></html>";
        html="<div><font face=\"华文细黑\" style=\"font-size: 16px; line-height: 19.200000762939453px;\" class=\" xh-highlight\">4,591.64</font></div>";
        
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("treatDeprecatedTagsAsContent", false);
        Extractor extractor = new HtmlCleanerExtractor(html, props);
        Model page = new Model("page");
        page.addField("money1").set("xpath", "//div/font/text()");
        page.addField("money2").set("xpath", "//div/text()");
        
//        page.addField("title").set("xpath", "//title/text()");
//        page.addField("target").set("xpath", "//target").set("isAutoExtractAttrs", true).set("isArray", true);
        extractor.addModel(page);
        extractor.extract(new Extractor.Callback() {
            public void onModelExtracted(ModelEntry entry) {
                System.out.println(entry.getModel().getName() + "->\r\n" + JSON.toJSONString(entry.getFields(), true) + "\r\n\r\n");
            }

            public void onFieldExtracted(FieldEntry entry) {
            }
        });
    }
}
