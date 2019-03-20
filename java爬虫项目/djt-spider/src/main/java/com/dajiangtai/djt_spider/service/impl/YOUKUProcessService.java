package com.dajiangtai.djt_spider.service.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import com.dajiangtai.djt_spider.entity.Page;
import com.dajiangtai.djt_spider.service.IProcessService;
import com.dajiangtai.djt_spider.util.HtmlUtil;
import com.dajiangtai.djt_spider.util.LoadPropertyUtil;
import com.dajiangtai.djt_spider.util.RegexUtil;
/**
 * 优酷页面解析实现类
 * @author dajiangtai
 * created by 2016-10-28
 */
public class YOUKUProcessService implements IProcessService {
	public void process(Page page) {
		// TODO Auto-generated method stub
		String content = page.getContent();
		
		HtmlCleaner htmlCleaner = new HtmlCleaner();
		 TagNode rootNode = htmlCleaner.clean(content);
		 ////*[@id="showInfo"]/div/div[1]/ul[2]/li[1]/span[1]
		 //获取总播放量
		
		 String allnumber = HtmlUtil.getFieldByRegex(rootNode, LoadPropertyUtil.getYOUKU("parseAllNumber"), LoadPropertyUtil.getYOUKU("allnumberRegex"));
		 page.setAllnumber(allnumber);
		 //System.out.println(" 总播放数:"+allnumber);


		 
		 //获取评论数
		 String commentnumber = HtmlUtil.getFieldByRegex(rootNode, LoadPropertyUtil.getYOUKU("parseCommentNumber"), LoadPropertyUtil.getYOUKU("commentnumberRegex"));
		 page.setCommentnumber(commentnumber);
		 //System.out.println(" 评论数:"+commentnumber);
		 
		 //获取赞
		 String supportnumber = HtmlUtil.getFieldByRegex(rootNode, LoadPropertyUtil.getYOUKU("parseSupportNumber"), LoadPropertyUtil.getYOUKU("supportnumberRegex"));
		 page.setSupportnumber(supportnumber);
		 //System.out.println(" 赞:"+supportnumber);
		 
		 page.setDaynumber("0");
		 page.setAgainstnumber("0");
		 page.setCollectnumber("0");
		 
		 //获取优酷电视剧id
		 Pattern pattern = Pattern.compile(LoadPropertyUtil.getYOUKU("idRegex"), Pattern.DOTALL);
		 page.setTvId("youku_"+RegexUtil.getPageInfoByRegex(page.getUrl(), pattern, 1));
	}

}
