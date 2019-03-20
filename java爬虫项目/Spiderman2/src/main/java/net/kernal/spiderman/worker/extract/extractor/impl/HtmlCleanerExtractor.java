package net.kernal.spiderman.worker.extract.extractor.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleXmlSerializer;
import org.htmlcleaner.TagNode;

import net.kernal.spiderman.kit.K;
import net.kernal.spiderman.worker.extract.ExtractTask;
import net.kernal.spiderman.worker.extract.extractor.AbstractXPathExtractor;
import net.kernal.spiderman.worker.extract.extractor.Extractor;
import net.kernal.spiderman.worker.extract.schema.Field;
import net.kernal.spiderman.worker.extract.schema.Model;

public class HtmlCleanerExtractor extends AbstractXPathExtractor {

	public static Extractor.Builder builder() {
        return builder(null);
    }
	
    public static Extractor.Builder builder(final Map<String, Object> props) {
        return (t, p, ms) -> new HtmlCleanerExtractor(t, p, props, ms);
    }

    private HtmlCleaner htmlCleaner;
    private TagNode doc;
    
    private  void init(String html, Map<String, Object> props) {
    	// 将外部传入的map信息，反射注入到HTMLCleaner的配置对象中去
    	CleanerProperties properties = new CleanerProperties();
    	if (props != null) {
	    	for (String name : props.keySet()) {
	    		Object value = props.get(name);
	    		String prefix = name.substring(0, 1).toUpperCase();
	    		String suffix = name.substring(1, name.length());
	    		String setterName = "set" + prefix + suffix;
	    		try {
	    			Method setterMethod = null;
					Method[] methods = properties.getClass().getMethods();
					for (Method m : methods) {
						if (m.getName().equals(setterName)) {
							setterMethod = m;
							break;
						}
					}
					if (setterMethod != null) {
						setterMethod.invoke(properties, value);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
	    	}
    	}
    	// 实例化HtmlCleaner组件
        htmlCleaner = new HtmlCleaner(properties);
        // FIXME 将配置选项放到外部
        this.doc = htmlCleaner.clean(html);
    }

    public HtmlCleanerExtractor(String html, Model... models) {
    	this(html, null, models);
    }
    
    public HtmlCleanerExtractor(String html, Map<String, Object> props, Model... models) {
        super(null, null, models);
        this.init(html, props);
    }

    public HtmlCleanerExtractor(ExtractTask task, String page, Model... models) {
    	this(task, page, null, models);
    }
    
    public HtmlCleanerExtractor(ExtractTask task, String page, Map<String, Object> props, Model... models) {
        super(task, page, models);
        final String html = task.getResponse().getBodyStr();
        this.init(html, props);
    }

    protected Object getDoc() {
        return this.doc;
    }

    @Override
    protected List<Object> extractModel(Object aDoc, String modelXpath) {
        TagNode doc = (TagNode) aDoc;
        Object[] nodeArray = new TagNode[]{doc};
        if (K.isNotBlank(modelXpath)) {
            try {
            	nodeArray = doc.evaluateXPath(modelXpath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (nodeArray == null || nodeArray.length == 0) {
            return null;
        }

        return Arrays.asList(nodeArray);
    }

    protected List<Object> extractField(Object model, Field field, String defaultValue, String aXpath, 
    		String attr, boolean isFromDoc, boolean isSerialize) {
        final List<Object> values = new ArrayList<>();
        
        if (null != defaultValue) {
			values.add(defaultValue);
			return values;
		}
        
        TagNode mNode = (TagNode) model;
        Object[] nodeArray = null;
        String xpath = aXpath.replace("/text()", "");
        try {
        	if (isFromDoc) {
        		nodeArray = doc.evaluateXPath(xpath);
			}else {
				nodeArray = mNode.evaluateXPath(xpath);
			}
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (nodeArray == null || nodeArray.length == 0) {
            return null;
        }

        for (int i = 0; i < nodeArray.length; i++) {
            TagNode tagNode = (TagNode) nodeArray[i];
            if (aXpath.endsWith("/text()")) {
                values.add(tagNode.getText().toString());
                continue;
            }

            Object value;
            if (K.isNotBlank(attr)) {
                value = tagNode.getAttributeByName(attr);
            } else if (isSerialize) {
                StringWriter sw = new StringWriter();
                CleanerProperties prop = htmlCleaner.getProperties();
                prop.setOmitXmlDeclaration(true);
                SimpleXmlSerializer ser = new SimpleXmlSerializer(prop);
                String charset = null;
                ExtractTask task = getTask();
                if (task != null && task.getResponse() != null) {
                	charset = task.getResponse().getCharset();
                }
                try {
                    ser.write(tagNode, sw, charset, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                value = sw.getBuffer().toString();
            } else {
                value = tagNode;
            }
            values.add(value);
        }
        return values;
    }

    protected Map<String, String> extractAttributes(Object node) {
        Map<String, String> r = new HashMap<>();
        TagNode tagNode = (TagNode) node;
        r.putAll(tagNode.getAttributes());
        return r;
    }

}
