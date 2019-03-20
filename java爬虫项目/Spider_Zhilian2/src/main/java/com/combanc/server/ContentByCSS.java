package com.combanc.server;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.combanc.mongodb.MongoDBConn;
import com.combanc.pojo.Content;
import com.combanc.pojo.Field_Rules;
import com.combanc.util.NowDate;
import com.combanc.util.RegexDate;
import com.combanc.util.ReplaceContent;

/**
 * Title: 			ContentByCSS 
 * Description:     用css解析文章内容页，所有规则均为css规则 
 * Company:			combanc 
 * Author:			shihw
 * Date: 			2018/5/24 
 * JDK: 			1.8 
 * Encoding: 		UTF-8
 */
public class ContentByCSS {
	private static Logger logger = Logger.getLogger(ContentByCSS.class);
	/**
	 * 解析内容页
	 * 
	 * @param document		网页源码
	 * @param url			内容页url
	 * @param field_Rules	解析规则
	 * @return boolean 		true 表示解析成功
	 * 						false表示解析失败
	 */
	public static boolean getContent(Document document, String url , String job_category,Field_Rules field_Rules) {
		try {

			// JD的ID
			String id = ReplaceContent.replaceUrlGetId(url,field_Rules.getId_rule(),field_Rules.getResource_rule());
			// JD标题
			String title = ReplaceContent.replaceSymbol(document.select(field_Rules.getTitle_rule()).text());
			
			if (title == null) {
				logger.warn("该url：" + url + "内容不完整！");
				return false;
			}
			
			// 公司名称
			String company_name = ReplaceContent.replaceSymbol(parseByCss(field_Rules.getCompany_name_rule(),document));
			
			if(null == company_name){
				logger.warn("该url：" + url + "内容不完整！");
				return false;
			}
			// 福利待遇
			String welfare = null;
			if(parseListByCss(field_Rules.getWelfare_rule(),document) != null){
				welfare = ReplaceContent.replaceSymbol(String.join(" ", parseListByCss(field_Rules.getWelfare_rule(),document)));
			}
			
			// 职位描述
			String job_description = ReplaceContent.replaceSymbol(String.join("$", parseListByCss(field_Rules.getJob_description_rule(),document)));

			// 公司介绍
			String company_description = ReplaceContent.replaceSymbol(parseByCss(field_Rules.getCompany_description_rule(),document));
			// 入库时间
			String create_time = NowDate.createDate();
			// 月薪
			String salary = ReplaceContent.replaceSymbol(parseByCss(field_Rules.getSalary_rule(),document));
			if(null == salary){
				logger.warn("该url：" + url + "内容不完整！");
				return false;
			}
			
			// 工作地点
			String city = ReplaceContent.replaceSymbol(parseByCss(field_Rules.getCity_rule(),document));
			// 发布日期
			String publication_date =  RegexDate.dateRegular(ReplaceContent.replaceSymbol(parseByCss(field_Rules.getPublication_date_rule(),document)));
			// 工作性质(全职、兼职等)
			String job_property = ReplaceContent.replaceSymbol(parseByCss(field_Rules.getJob_property_rule(),document));
			// 工作经验(年限)
			String years = ReplaceContent.replaceSymbol(parseByCss(field_Rules.getYears_rule(),document));
			// 学历
			String education = ReplaceContent.replaceSymbol(parseByCss(field_Rules.getEducation_rule(),document));
			// 招聘人数
			String recruitment_number = ReplaceContent.replaceSymbol(parseByCss(field_Rules.getRecruitment_number_rule(),document));
			// 职位类别
			if (job_category == null || job_category.equals("")) {
				job_category = ReplaceContent.replaceSymbol(parseByCss(field_Rules.getJob_category_rule(),document));
			}
			
			
			String company_size = null;
			String company_type = null;
			String company_industry = null;
			String company_address = null;
			String company_web = null;
			
			ArrayList<String> company = parseListByCss(field_Rules.getCompany_size_rule(),document);
			
			if(company.size()==4){
				// 公司规模
				company_size = ReplaceContent.replaceSymbol(company.get(0));
				// 公司性质
				company_type = ReplaceContent.replaceSymbol(company.get(1));
				// 公司行业
				company_industry = ReplaceContent.replaceSymbol(company.get(2));
				// 公司地址
				company_address = ReplaceContent.replaceSymbol(company.get(3));
			}else{
				// 公司规模
				company_size = ReplaceContent.replaceSymbol(company.get(0));
				// 公司性质
				company_type = ReplaceContent.replaceSymbol(parseByCss(field_Rules.getCompany_type_rule(),document));
				// 公司行业
				company_industry = ReplaceContent.replaceSymbol(parseByCss(field_Rules.getCompany_industry_rule(),document));
				// 公司地址
				company_address = ReplaceContent.replaceSymbol(parseByCss(field_Rules.getCompany_address_rule(),document));
				// 公司主页
				company_web = ReplaceContent.replaceSymbol(parseByCss(field_Rules.getCompany_web_rule(),document));
			}
			
			
			String resource = field_Rules.getResource_rule();
			//保存到 实体类
			Content content = new Content(id, title, company_name, welfare, salary, city, publication_date, job_property, years, education, recruitment_number, job_category, job_description, company_description, company_size, company_type, company_industry, company_web, company_address, create_time,resource);
			//写入到MongoDB
			System.out.println(content.toString());
		} catch (Exception e) {
			logger.error("网站异常:"+url,e);
		}
		return false;

	}
	
	
	/**
	 * 解析内容
	 * @param rule		规则
	 * @param document	doc
	 * @return			内容
	 */
	public static String parseByCss(String rule, Document document) {
		String content = null;
		if (rule.equals("null")) {
			return null;
		}else if (rule.equals("")) {
			logger.warn("规则为空,请输入规则！");
			return null;
		}else {
			content = document.select(rule).text();
			if (content.equals("")) {
				content = null;
			}
		}
		return content;
	}
	/**
	 * 解析内容list
	 * @param rule		规则
	 * @param document	doc
	 * @return			内容
	 */
	public static ArrayList<String> parseListByCss(String rule, Document document) {
		ArrayList<String> list = new ArrayList<>();
		if (rule.equals("null")) {
			return null;
		}else if (rule.equals("")) {
			logger.warn("规则为空,请输入规则！");
			return null;
		}else {
			Elements elements = document.select(rule);
			
			if(elements.size()==0){
				logger.warn("规则错误,解析的内容为null,错误规则为：" + rule);
				return null;
			}
			for (Element element : elements) {
				list.add(element.text());
			}
		}
		return list;
	}
}
