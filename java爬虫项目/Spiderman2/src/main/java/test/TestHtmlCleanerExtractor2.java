package test;

import com.alibaba.fastjson.JSON;
import net.kernal.spiderman.worker.extract.extractor.Extractor;
import net.kernal.spiderman.worker.extract.extractor.impl.HtmlCleanerExtractor;
import net.kernal.spiderman.worker.extract.schema.Model;

/**
 * @author 赖伟威 l.weiwei@163.com
 * @version V0.1.0
 */
public class TestHtmlCleanerExtractor2 {
    public static void main(String[] args) {
        final String html = "<div><img src='other.jpg' /></div><div id='contentText'><img src='a.jpg' /><img src='b.png' /></div>";
        
        final Extractor extractor = new HtmlCleanerExtractor(html);
        
        final Model model = new Model("page");
        model.addField("imageUrls")
        	.set("xpath", "//div[@id='contentText']//img[@src]")
        	.set("attr", "src")
        	.set("isArray", true);
        extractor.addModel(model);
        
        extractor.extract(new Extractor.Callback() {
            public void onModelExtracted(ModelEntry entry) {
                System.out.println(entry.getModel().getName() + "->\r\n" + JSON.toJSONString(entry.getFields(), true) + "\r\n\r\n");
            }
            public void onFieldExtracted(FieldEntry entry) { }
        });
    }
}
