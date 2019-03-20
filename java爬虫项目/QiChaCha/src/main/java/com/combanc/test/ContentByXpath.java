package com.combanc.test;

import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.combanc.mongodb.MongoDBConn;
import com.combanc.pojo.Company_Info;
/**
 * @Title: 			ContentByXpath 
 * @Description:    用Xpath解析文章内容页，所有规则均为Xpath规则 
 * @Company:		combanc 
 * @Author:			shihw
 * @Date: 			2018/10/25
 * @JDK: 			1.8 
 * @Encoding: 		UTF-8
 */
public class ContentByXpath {
	private static Logger logger = Logger.getLogger(ContentByXpath.class);
	/**
	 * 解析内容页
	 * @param body			网页源码
	 * @param url			内容页网址
	 * @param job_category	行业
	 * @param inputPath		输出路径
	 * @param field_Rules	解析规则
	 */
	public static void getContentByXpath(String html,String id,String url,String company_name) {
		try {
			HtmlCleaner hCleaner = new HtmlCleaner();
			TagNode tagNode = hCleaner.clean(html);
			Document dom = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
			XPath xPath = XPathFactory.newInstance().newXPath();
			
			
			String register_capital = parseByXpath("//table[@class='ntable'][2]/tbody/tr[1]/td[2]", xPath, dom);
			String contributed_capital = parseByXpath("//table[@class='ntable'][2]/tbody/tr[1]/td[4]", xPath, dom);
			String operating_state = parseByXpath("//table[@class='ntable'][2]/tbody/tr[2]/td[2]", xPath, dom);
			String establish_data = parseByXpath("//table[@class='ntable'][2]/tbody/tr[2]/td[4]", xPath, dom);
			String credit_Code = parseByXpath("//table[@class='ntable'][2]/tbody/tr[3]/td[2]", xPath, dom);
			String taxpayer_code = parseByXpath("//table[@class='ntable'][2]/tbody/tr[3]/td[4]", xPath, dom);
			String register_code = parseByXpath("//table[@class='ntable'][2]/tbody/tr[4]/td[2]", xPath, dom);
			String organization_code = parseByXpath("//table[@class='ntable'][2]/tbody/tr[4]/td[4]", xPath, dom);
			String company_type = parseByXpath("//table[@class='ntable'][2]/tbody/tr[5]/td[2]", xPath, dom);
			String company_industry = parseByXpath("//table[@class='ntable'][2]/tbody/tr[5]/td[4]", xPath, dom);
			String approval_date = parseByXpath("//table[@class='ntable'][2]/tbody/tr[6]/td[2]", xPath, dom);
			String register_office = parseByXpath("//table[@class='ntable'][2]/tbody/tr[6]/td[4]", xPath, dom);
			String area = parseByXpath("//table[@class='ntable'][2]/tbody/tr[7]/td[2]", xPath, dom);
			String english_name = parseByXpath("//table[@class='ntable'][2]/tbody/tr[7]/td[4]", xPath, dom);
			String before_name = parseByXpath("//table[@class='ntable'][2]/tbody/tr[8]/td[2]", xPath, dom);
			String insured_num = parseByXpath("//table[@class='ntable'][2]/tbody/tr[8]/td[4]", xPath, dom);
			String company_size = parseByXpath("//table[@class='ntable'][2]/tbody/tr[9]/td[2]", xPath, dom);
			String business_term = parseByXpath("//table[@class='ntable'][2]/tbody/tr[9]/td[4]", xPath, dom);
			String company_address = parseByXpath("//table[@class='ntable'][2]/tbody/tr[10]/td[2]", xPath, dom).replace("查看地图", "").replace("附近公司", "").trim();
			String business_scope = parseByXpath("//table[@class='ntable'][2]/tbody/tr[11]/td[2]", xPath, dom);
			Company_Info content = new Company_Info(id,company_name,
							register_capital, 
							contributed_capital, 
							operating_state, 
							establish_data, 
							credit_Code, 
							taxpayer_code, 
							register_code, 
							organization_code, 
							company_type, 
							company_industry,
							approval_date, 
							register_office, 
							area, 
							english_name, 
							before_name, 
							insured_num, 
							company_size, 
							business_term, 
							company_address, 
							business_scope);
			
			
			
//			System.out.println(content.toString());
			MongoDBConn.AddContent(content);
		}  catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("网站异常:"+url,e);
		}
		
	}
	/**
	 * 解析内容list
	 * @param rule	规则	
	 * @param xPath 
	 * @param dom	doc
	 * @return		内容
	 */
	public static ArrayList<String> parseByXpathList(String rule, XPath xPath, Document dom) {
		ArrayList<String> list = new ArrayList<>();
		NodeList blockList = null;
		try {

			if (null == rule) {
				return null;
			} else if (rule.equals("")) {
				logger.warn("规则为空,请输入规则！");
				return null;
			} else {
				// 将多个规则以&&分割
				String[] rules = rule.split("&&");
				// 循环
				for (int i = 0; i < rules.length; i++) {
					blockList = (NodeList) xPath.evaluate(rules[i], dom, XPathConstants.NODESET);
					if (blockList.getLength() > 0) {
						for (int j = 0; j < blockList.getLength(); j++) {
							list.add(blockList.item(j).getTextContent());
						}
						break;
					}
				}
				if (list.size() == 0) {
					//logger.warn("规则错误,解析的内容为null,错误规则为：" + rule);
				}

			}

		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("规则错误!,错误规则为：" + rule, e);
		}
		return list;
	}
	
	
	/**
	 * 解析内容
	 * @param rule	规则	
	 * @param xPath 
	 * @param dom	doc
	 * @return		内容
	 */
	public static String parseByXpath(String rule, XPath xPath, Document dom) {

		String content = null;
		try {
			if (null == rule) {
				return null;
			} else if (rule.equals("")) {
				logger.warn("规则为空,请输入规则！");
				content = null;
			} else {
				// 将多个规则以&&分割
				String[] rules = rule.split("&&");
				// 循环
				for (int i = 0; i < rules.length; i++) {
					// 解析
					content = xPath.evaluate(rules[i], dom, XPathConstants.STRING).toString();
					// 如果不为 ""和null 结束循环
					if (!content.equals("") && content != null) {
						break;
					}
				}
				if (content.equals("")) {
					//logger.warn("规则错误,解析的内容为null,错误规则为：" + rule);
					content = null;
				}
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("规则错误!,错误规则为：" + rule, e);
		}
		return content;
	}
}
