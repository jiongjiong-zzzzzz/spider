package test;

import com.alibaba.fastjson.JSON;
import net.kernal.spiderman.worker.extract.extractor.Extractor;
import net.kernal.spiderman.worker.extract.extractor.impl.HtmlCleanerExtractor;
import net.kernal.spiderman.worker.extract.schema.Field;
import net.kernal.spiderman.worker.extract.schema.Model;

/**
 * @author 赖伟威 l.weiwei@163.com
 * @version V0.1.0
 */
public class TestHtmlCleanerExtractor3 {
    public static void main(String[] args) {
        final String html = "<div class='content'>文档标签</div>"
        		+ "<div class='contentText'><img src='a.jpg' /><img src='b.png' /></div>"
        		+ "<div class='contentText'><img src='c.jpg' /><img src='d.png' /></div>";
        
        final Extractor extractor = new HtmlCleanerExtractor(html);
        
        final Model model = new Model("page");
        Field parentField = model.addField("list")
        	.set("xpath", "//div[@class='contentText']")
        	.set("isArray", true);
        parentField.addField("图片链接").set("xpath", ".//div[@class='content']/text()").set("attr", "src").set("isFromDoc", "1");
        parentField.addField("图片1").set("xpath", "//img[1]").set("attr", "src");
        parentField.addField("图片2").set("xpath", "//img[2]").set("attr", "src");
        
        model.addField("默认字段").set("value", "测试默认字段");
        
        extractor.addModel(model);
        
        extractor.extract(new Extractor.Callback() {
            public void onModelExtracted(ModelEntry entry) {
                System.out.println(entry.getModel().getName() + "->\r\n" + JSON.toJSONString(entry.getFields(), true) + "\r\n\r\n");
            }
            public void onFieldExtracted(FieldEntry entry) { }
        });
    }
}
