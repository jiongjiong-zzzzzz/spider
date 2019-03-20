package test;

import java.io.File;

import org.apache.commons.io.FileUtils;

import com.alibaba.fastjson.JSON;

import net.kernal.spiderman.kit.Properties;
import net.kernal.spiderman.worker.extract.extractor.Extractor;
import net.kernal.spiderman.worker.extract.extractor.impl.HtmlCleanerExtractor;
import net.kernal.spiderman.worker.extract.schema.Field;
import net.kernal.spiderman.worker.extract.schema.Model;

/**
 * @author 赖伟威 l.weiwei@163.com
 * @version V0.1.0
 */
public class TestHtmlCleanerExtractor4 {
	
    public static void main(String[] args) throws Exception {
    	final File file = new File("E:\\Work\\2018\\大投行项目\\word-parse\\附件一：IPO立项申请报告(1).htm");
        final String html = FileUtils.readFileToString(file, "UTF-8");
        
        final Extractor extractor = new HtmlCleanerExtractor(html);
        
        final Model tables = new Model("tables");
        tables.set("xpath", "//table");
        tables.set("isArray", true);
        
        Field rows = tables.addField("rows");
        rows.set("xpath", ".//tr");
        rows.set("isArray", true);
        
        Field cols = rows.addField("cols");
        cols.set("isArray", true);
        cols.set("xpath", ".//td/text()");
        
        extractor.addModel(tables);
        
        extractor.extract(new Extractor.Callback() {
            public void onModelExtracted(ModelEntry entry) {
            	String modelName = entry.getModel().getName();
            	Properties fields = entry.getFields();
                System.out.println(
            		modelName + "->\r\n" + 
    				JSON.toJSONString(
						fields, 
						true
					) + "\r\n\r\n"
				);
            }
            public void onFieldExtracted(FieldEntry entry) {
//                System.err.println(
//    				JSON.toJSONString(
//						entry, 
//						true
//					) + "\r\n\r\n"
//				);
            }
        });
    }
}
