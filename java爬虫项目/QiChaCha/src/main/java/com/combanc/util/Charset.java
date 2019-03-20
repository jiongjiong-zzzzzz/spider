package com.combanc.util;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import cpdetector.io.ASCIIDetector;
import cpdetector.io.ByteOrderMarkDetector;
import cpdetector.io.CodepageDetectorProxy;
import cpdetector.io.JChardetFacade;
import cpdetector.io.ParsingDetector;
import cpdetector.io.UnicodeDetector;

/**
 * Title:           Charset
 * Description:     获取网页编码
 * Company:         combanc
 * Author:          shihw
 * Date:            2018/6/7
 * JDK:             1.8
 * Encoding:        UTF-8
 */
public class Charset {
	 /*
     * detector是探测器，它把探测任务交给具体的探测实现类的实例完成。
     * cpDetector内置了一些常用的探测实现类，这些探测实现类的实例可以通过add方法 加进来，如ParsingDetector、
     * JChardetFacade、ASCIIDetector、UnicodeDetector。
     * detector按照“谁最先返回非空的探测结果，就以该结果为准”的原则返回探测到的
     * 字符集编码。使用需要用到三个第三方JAR包：antlr.jar、chardet.jar和jargs-1.0.jar
     * cpDetector是基于统计学原理的，不保证完全正确。
     */
	public static String getFileEncoding(String htmlurl) {
		URL url = null;
		java.nio.charset.Charset charset = null;
		try {
			url = new URL(htmlurl);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		CodepageDetectorProxy codepageDetectorProxy = CodepageDetectorProxy.getInstance();
		// JChardetFacade封装了由Mozilla组织提供的JChardet，它可以完成大多数文件的编码
		codepageDetectorProxy.add(JChardetFacade.getInstance());
		// ASCIIDetector用于ASCII编码测定
		codepageDetectorProxy.add(ASCIIDetector.getInstance());
		// UnicodeDetector用于Unicode家族编码的测定
		codepageDetectorProxy.add(UnicodeDetector.getInstance());
		codepageDetectorProxy.add(new ParsingDetector(false));
		codepageDetectorProxy.add(new ByteOrderMarkDetector());

		try {
			charset = codepageDetectorProxy.detectCodepage(url);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			return htmlurl;
		}
		return charset.name();
	}
	
	
	
	/**
	 * 把汉字转换为 url编码
	 * @param keyWord 关键词
	 * @return		      编码后的关键词
	 */
	public static String getCodeByURLEncoder(String keyWord){
		String encode = null;
		try {
			// UTF-8编码
			encode = URLEncoder.encode(keyWord, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encode;
	}
	
	
	/**
	 * 把url解码为汉字
	 * @param keyWord 关键词
	 * @return		      编码后的关键词
	 */
	public static String getCodeByURLDecoder(String keyWord){
		String decode = null;
		try {
			// UTF-8解码
			decode = URLDecoder.decode(keyWord, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return decode;
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String fileEncoding = getFileEncoding("https://jobs.51job.com/urumqi/103276733.html?s=01&t=0");
		System.out.println(fileEncoding);
		
	//	getCodeByURLEncoder("https://sou.zhaopin.com/jobs/searchresult.ashx?sj=233&in=301100&jl=俄罗斯联邦&p=1&isadv=0");
	}
}
