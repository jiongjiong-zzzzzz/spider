package com.combanc.test;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.combanc.mongodb.MongoDBConn;
import com.combanc.pojo.Company_Url;
/**
 * @Title:           UrlByXpath
 * @Description:     用Xpath解析url列表
 * @Company:         combanc
 * @Author:          shihw
 * @Date:            2018/10/24
 * @JDK:             1.8
 * @Encoding:        UTF-8
 */
public class UrlByXpath {
	
	
	/**企查查
	 * 解析URL链接 存入mongo
	 * @param body
	 * @return
	 */
	public static  boolean ParseUrlByXpath(String body) {
		try {	
            HtmlCleaner hCleaner = new HtmlCleaner();
    		TagNode tagNode = hCleaner.clean(body);
			Document dom = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
			XPath xPath = XPathFactory.newInstance().newXPath();
			//获取URL列表
			NodeList urls = (NodeList) xPath.evaluate("//tbody/tr/td[2]/a[@class='ma_h1']/@href",dom , XPathConstants.NODESET);
			
			//获取URL对应公司名
			NodeList company_names = (NodeList) xPath.evaluate("//tbody/tr/td[2]/a[@class='ma_h1']",dom , XPathConstants.NODESET);
			
			for(int i = 0;i<urls.getLength();i++){
				Company_Url company_Url = new Company_Url();
				//提取url链接
				String href = urls.item(i).getTextContent();
				//提取公司名称
				String company_name = company_names.item(i).getTextContent();
				String url = "https://www.qichacha.com"+href;
				String id = href.replace("/firm_", "").replace(".html", "");
				company_Url.setCompany_name(company_name);
				company_Url.setCompany_url(url);
				company_Url.setId(id);
				
				//添加到mongo数据库里
				int success = MongoDBConn.AddUrl(company_Url);
				if (success>0) {
					return true;
				}
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return false; 	   
 	}

}
