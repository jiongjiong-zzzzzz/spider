package com.combanc.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Title:           ReplaceContent
 * Description:     初步清洗
 * Company:         combanc
 * Author:          shihw
 * Date:            2018/6/6
 * JDK:             1.8
 * Encoding:        UTF-8
 */
public class ReplaceContent {
	/**
	 * 对内容进行清洗
	 * @param content 内容
	 * @return 清洗后的内容
	 */
	public static String replaceSymbol(String content){
		
		
		if(content!=null && !content.equals("")){
			content = content.trim().replaceAll("\t", " ").replaceAll("\n", " ").replaceAll("\\s+", " ");
			if(content.contains("//www.lagou.com/gon")){
				return null;
			}
			if(content.contains("该公司其他职位")){
				content = content.replace("该公司其他职位", "");
			}
			if(content.contains("查看公司地图")){
				content = content.replace("查看公司地图", "");
			}
			if(content.contains("查看职位地图")){
				content = content.replace("查看职位地图", "");
			}
			if(content.contains("查看地图")){
				content = content.replace("查看地图", "");
			}
			if(content.contains("查看地图")){
				content = content.replace("查看地图", "");
			}
			if(content.contains("查看地图")){
				content = content.replace("查看地图", "");
			}
			if(content.contains("拉勾未认证企业")){
				content = content.replace("拉勾未认证企业", "");
			}
			if(content.contains("拉勾认证企业")){
				content = content.replace("拉勾认证企业", "");
			}
			if(content.contains("领域")){
				content = content.replace("领域", "");
			}
			if(content.contains("公司主页")){
				content = content.replace("公司主页", "");
			}
			if(content.contains("规模")){
				content = content.replace("规模", "");
			}
			if(content.startsWith("/") || content.endsWith("/")){
				content = content.replace("/", "");
			}
		}else{
			return null;
		}
		
		return content.trim();
	}
	
	/**
	 * 处理拉勾公司行业
	 * @param content
	 * @return
	 */
	public static String replaceindustry(String content){
		content = content.trim();
		if(content.contains("领域")){
			content = content.replace("领域", "");
		}
		if(content.contains(",")){
			content = content.replace(",", " ");
		}
		if(content.contains("、")){
			content = content.replace("、", " ");
		}
		if(content.contains("　")){
			content = content.replace("　", " ");
		}
		return content.split(" ")[0];
		
	}
	
	
	
	/**
	 * 是否包含内容页url
	 * @param url 内容页url
	 * @param resource 网址来源
	 * @return boolean true包含 false不包含
	 */
	public static boolean replaceContentUrl(String url,String resource){
		boolean falx = false;
		if(resource.equals("智联招聘")){
			if (url.contains("https://jobs.zhaopin.com")) {
				falx = true;
			}
		}else if(resource.equals("51Job")){
			if (url.contains("https://jobs.51job.com")) {
				falx = true;
			}
		}else if(resource.equals("拉勾")){
			if (url.contains("https://www.lagou.com/jobs")) {
				falx = true;
			}
		}
		return falx;
	}
	
	/**
	 * 从内容页网址获取JD id
	 * @param url 		内容页网址
	 * @param id_rule   主网址
	 * @param resource	来源网站
	 * @return JD id
	 */
	public static String replaceUrlGetId(String url, String id_rule, String resource) {
		String id = null;
		if (resource.equals("智联招聘")) {
			url = url.replace(id_rule, "");

			id = url.substring(0, url.indexOf(".htm")).replace("\t", " ");
		} else if (resource.equals("Boss直聘")) {
			url = url.replace(id_rule, "");

			id = url.substring(0, url.indexOf(".html")).replace("\t", " ");
		} else if (resource.equals("51Job")) {
			Pattern pattern = Pattern.compile("[0-9]+");
			url = url.replace(id_rule, "");

			id = url.substring(0, url.indexOf(".html")).replace("\t", " ");
			Matcher matcher = pattern.matcher(id);
			while (matcher.find()) {
				id = matcher.group();
			}
		}else if (resource.equals("拉勾")) {
			url = url.replace(id_rule, "");

			id = url.substring(0, url.indexOf(".html")).replace("\t", " ");
		}
		return id;
	}
	
	public static String replaceUrlList(String url){
		
		if(url.contains("href=\"")){
			url = url.replace("href=\"", "").replace("\"", "");
		}
		
		return url;
	}
	
	
	/**
	 * 智联匹配 职位福利
	 * @param content
	 * @return
	 */
	public static String welfareRegular(String content,String rule) {

		Pattern pattern = Pattern.compile(rule);
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			return matcher.group(1);
		}
		
		return null;
	}
	
	
}
