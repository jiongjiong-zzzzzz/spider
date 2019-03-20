package com.combanc.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Title: ReadFile 
 * Description: 读取文件 
 * Company: combanc 
 * Author: shihw 
 * Date:2018/5/25
 * JDK: 1.8 
 * Encoding: UTF-8
 */
public class ReadFile {
	private static Logger logger = Logger.getLogger(ReadFile.class);

	/**
	 * 读取txt文件的内容 一行一行读
	 * 
	 * @param file
	 *            想要读取的文件对象
	 * @return 返回文件内容
	 */
	public static List<String> readkeywords(InputStream is) {
		List<String> list = new ArrayList<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(is));// 构造一个BufferedReader类来读取文件
			String s = null;
			while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
				list.add(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("文件读取失败！", e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return list;
	}

	/**
	 * 读取txt文件的内容 一行一行读 以\t分割取第一位
	 * 
	 * @param is
	 *            想要读取的文件输入流
	 * @return 返回文件内容
	 */
	public static List<String> readkeywordsOne(InputStream is) {
		List<String> list = new ArrayList<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(is));// 构造一个BufferedReader类来读取文件
			String s = null;
			while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
				String[] split = s.split("\t");
				// 拿 第一位
				list.add(split[0]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("文件读取失败！", e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return list;
	}

	/**
	 * 读取txt文件的内容 以\t分割取第二位
	 * 
	 * @param is
	 *            想要读取的文件输入流
	 * @return 返回文件内容
	 */
	public static List<String> readkeywordsTwo(InputStream is) {
		List<String> list = new ArrayList<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(is));// 构造一个BufferedReader类来读取文件
			String s = null;
			while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
				String[] split = s.split("\t");
				// 拿 第二位
				list.add(split[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("文件读取失败！", e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return list;
	}

	/**
	 * 使用BufferedReader类读文本文件
	 */
	public static List<String> readFile(String path) {
		List<String> list = new ArrayList<>();
		String line = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(path));
			while ((line = in.readLine()) != null) {
				list.add(line);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	// public static void main(String[] args){
	// String path = System.getProperty("user.dir");
	// File file = new File(path+"/keywords/Cities.txt");
	// System.out.println(txt2String(file).toString());
	// }
}
