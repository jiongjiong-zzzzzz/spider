package com.combanc.server;

import java.util.ArrayList;

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

import com.combanc.pojo.Content;
import com.combanc.pojo.Field_Rules;
import com.combanc.util.NowDate;
import com.combanc.util.ReplaceContent;

/**
 * @Title: 		 ContentByXpath_zhilian 
 * @Description: 用Xpath解析文章内容页，所有规则均为Xpath规则 
 * @Company:	 combanc 
 * @Author: 	 shihw 
 * @Date: 		 2018/10/17
 * @JDK: 		 1.8 
 * @Encoding: 	 UTF-8
 */
public class ContentByXpath {
	private static Logger logger = Logger.getLogger(ContentByXpath.class);

	/**
	 * 解析内容页
	 * 
	 * @param body	网页源码
	 * @param url	内容页网址
	 * @param job_category	行业
	 * @param inputPath	输出路径
	 * @param field_Rules	解析规则
	 *            
	 */
	public static boolean getContentByXpath(String body, String url, String job_property,Field_Rules field_Rules) {
		try {
			HtmlCleaner hCleaner = new HtmlCleaner();
			TagNode tagNode = hCleaner.clean(body);
			Document dom = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
			XPath xPath = XPathFactory.newInstance().newXPath();
			// jd的ID
			String id = ReplaceContent.replaceUrlGetId(url, "https://jobs.zhaopin.com/", "智联招聘");

			// JD标题
			String title = ReplaceContent.replaceSymbol(parseByXpath("//h1[@class='l info-h3']", xPath, dom));
			
			if (title == null) {
				logger.warn("该url：" + url + "内容不完整！");
				return false;
			}
			
			// 公司名称
			String company_name = ReplaceContent.replaceSymbol(parseByXpath("//div[@class='company l']/a", xPath, dom));
			
			if(null == company_name){
				logger.warn("该url：" + url + "内容不完整！");
				return false;
			}
			
			// 福利待遇 多个待遇 以" "分隔
			String welfare = ReplaceContent.welfareRegular(body,"JobWelfareTab\\s=\\s'(.*?)'");

			
			// 月薪
			String salary = ReplaceContent.replaceSymbol(parseByXpath("//div[@class='l info-money']/strong", xPath, dom));
			
			if(null == salary){
				logger.warn("该url：" + url + "内容不完整！");
				return false;
			}
			// 工作地点
			String city = ReplaceContent.replaceSymbol(parseByXpath("//div[@class='info-three l']/span[1]", xPath, dom));

			// 发布日期
			String publication_date = ReplaceContent.replaceSymbol(parseByXpath("//div[@class='company l']/span", xPath, dom));
			// 工作性质(全职、兼职等)
//			String job_property = "全职";
			// 工作经验(年限)
			String years = ReplaceContent
					.replaceSymbol(parseByXpath("//div[@class='info-three l']/span[2]", xPath, dom));

			// 学历
			String education = ReplaceContent.replaceSymbol(parseByXpath("//div[@class='info-three l']/span[3]", xPath, dom));

			// 招聘人数
			String recruitment_number = ReplaceContent.replaceSymbol(parseByXpath("//div[@class='info-three l']/span[4]", xPath, dom));
			// 职位类别
			String job_category = ReplaceContent
					.replaceSymbol(parseByXpath("//div[@class='pos-info-tit']/p/span[1]", xPath, dom));
			// 职位描述
			String job_description = ReplaceContent
					.replaceSymbol(String.join("$", parseByXpathList("//div[@class='pos-ul']/p", xPath, dom)));

			// 公司介绍
			String company_description = ReplaceContent
					.replaceSymbol(parseByXpath("//div[@class='intro-content']", xPath, dom));

			// 入库时间
			String create_time = NowDate.createDate();


			// 公司行业
			String	company_industry = ReplaceContent
					.replaceSymbol(parseByXpath("//ul[@class='promulgator-ul cl']/li[1]/strong", xPath, dom));
			// 公司性质
			String	company_type = ReplaceContent
					.replaceSymbol(parseByXpath("//ul[@class='promulgator-ul cl']/li[2]/strong", xPath, dom));

			// 公司规模
			String	company_size = ReplaceContent
					.replaceSymbol(parseByXpath("//ul[@class='promulgator-ul cl']/li[3]/strong", xPath, dom));
			// 公司主页
			String	company_web = ReplaceContent
					.replaceSymbol(parseByXpath("//ul[@class='promulgator-ul cl']/li[4]/strong", xPath, dom));
			// 公司地址
			String	company_address = ReplaceContent
					.replaceSymbol(parseByXpath("//ul[@class='promulgator-ul cl']/li[5]/strong", xPath, dom));

			// JD来源
			String resource = field_Rules.getResource_rule();

			Content content = new Content(id, title, company_name, welfare, salary, city, publication_date,
					job_property, years, education, recruitment_number, job_category, job_description,
					company_description, company_size, company_type, company_industry, company_web, company_address,
					create_time, resource);
			
			//写入到MongoDB
			System.out.println(content.toString());
			return true;
		} catch (Exception e) {
			logger.error("网站异常:" + url, e);
		}
		return false;

	}

	/**
	 * 解析内容list
	 * 
	 * @param rule
	 *            规则
	 * @param xPath
	 * @param dom
	 *            doc
	 * @return 内容
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
					// logger.warn("规则错误,解析的内容为null,错误规则为：" + rule);
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
	 * 
	 * @param rule	规则
	 * @param xPath
	 * @param dom	doc
	 * @return 内容
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
					// logger.warn("规则错误,解析的内容为null,错误规则为：" + rule);
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
