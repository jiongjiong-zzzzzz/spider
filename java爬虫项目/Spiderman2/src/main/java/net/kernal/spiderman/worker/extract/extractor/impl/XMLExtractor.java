package net.kernal.spiderman.worker.extract.extractor.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.kernal.spiderman.Spiderman;
import net.kernal.spiderman.kit.K;
import net.kernal.spiderman.worker.extract.ExtractTask;
import net.kernal.spiderman.worker.extract.extractor.AbstractXPathExtractor;
import net.kernal.spiderman.worker.extract.extractor.Extractor;
import net.kernal.spiderman.worker.extract.schema.Field;
import net.kernal.spiderman.worker.extract.schema.Model;

public class XMLExtractor extends AbstractXPathExtractor {

    public static Extractor.Builder builder() {
        return (t, p, ms) -> new XMLExtractor(t, p, ms);
    }

    private XPath xpath;
    private Document doc;
    private Transformer transformer;

    public XMLExtractor(ExtractTask task, Model... models) {
        this(new ByteArrayInputStream(task.getResponse().getBody()), task, null, models);
    }

    public XMLExtractor(String file, Model... models) {
        this(K.asStream(file), null, null, models);
    }

    public XMLExtractor(ExtractTask task, String page, Model... models) {
        this(new ByteArrayInputStream(task.getResponse().getBody()), task, page, models);
    }

    public XMLExtractor(String file, String page, Model... models) {
        this(K.asStream(file), null, page, models);
    }

    public XMLExtractor(InputStream is, ExtractTask task, String page, Model... models) {
        super(task, page, models);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            this.doc = db.parse(is);
            XPathFactory xf = XPathFactory.newInstance();
            this.xpath = xf.newXPath();
            this.transformer = TransformerFactory.newInstance().newTransformer();
        } catch (Throwable e) {
            throw new Spiderman.Exception("初始化XML解析器失败", e);
        }
    }

    protected Object getDoc() {
        return this.doc;
    }

    /**
     * @param doc
     * @param modelXpath
     * @return 匹配的节点
     */
    @Override
    protected List<Object> extractModel(Object doc, String modelXpath) {
        NodeList nodeList = null;
        try {
            nodeList = (NodeList) this.xpath.compile(modelXpath).evaluate(doc, XPathConstants.NODESET);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (nodeList == null || nodeList.getLength() == 0) {
            return null;
        }

        List<Object> mNodes = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            mNodes.add(node);
        }
        return mNodes;
    }

    /**
     * @param model
     * @param field
     * @param defaultValue
     * @param aXpath
     * @param attr
     * @param isFromDoc
     * @param isSerialize
     * @return 匹配的字段
     */
    @Override
    protected List<Object> extractField(Object model, Field field, String defaultValue, String aXpath, 
    		String attr, boolean isFromDoc, boolean isSerialize) {
        final List<Object> values = new ArrayList<>();
        if (null != defaultValue) {
			values.add(defaultValue);
			return values;
		}
        
        NodeList nodeList = null;
        String xpath = aXpath.replace("/text()", "");
        try {
        	if (isFromDoc) {
        		nodeList = (NodeList) this.xpath.compile(xpath).evaluate(doc, XPathConstants.NODESET);
			}else {
				nodeList = (NodeList) this.xpath.compile(xpath).evaluate(model, XPathConstants.NODESET);
			}
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (nodeList == null || nodeList.getLength() == 0) {
            return null;
        }

        for (int j = 0; j < nodeList.getLength(); j++) {
            Node node = nodeList.item(j);
            if (aXpath.endsWith("/text()")) {
                values.add(node.getTextContent());
                continue;
            }

            Object value = null;
            if (K.isNotBlank(attr)) {
                Element e = (Element) node;
                value = e.getAttribute(attr);
            } else if (isSerialize) {
                StringWriter writer = new StringWriter();
                try {
                    transformer.transform(new DOMSource(node), new StreamResult(writer));
                    String str = writer.getBuffer().toString();
                    value = new String(str.substring(str.indexOf("?>") + 2));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            } else {
                value = node;
            }
            values.add(value);
        }

        return values;
    }

    /**
     * @param node
     * @return 匹配的属性
     */
    @Override
    protected Map<String, String> extractAttributes(Object node) {
        Map<String, String> r = new HashMap<String, String>();
        Element e = (Element) node;
        NamedNodeMap attrs = e.getAttributes();
        if (attrs.getLength() > 0) {
            for (int i = 0; i < attrs.getLength(); i++) {
                Node n = attrs.item(i);
                if (Node.ATTRIBUTE_NODE == n.getNodeType()) {
                    r.put(n.getNodeName(), n.getNodeValue());
                }
            }
        }
        return r;
    }

}
