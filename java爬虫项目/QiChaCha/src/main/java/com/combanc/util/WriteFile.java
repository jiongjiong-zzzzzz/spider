package com.combanc.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;
/**
 * Title:           WriteFile
 * Description:     写入文件
 * Company:         combanc
 * Author:          shihw
 * Date:            2018/5/25
 * JDK:             1.8
 * Encoding:        UTF-8
 */
public class WriteFile {
	private static Logger logger = Logger.getLogger(WriteFile.class);
	/**
	 * 
	 * @param path    文件路径
	 * @param content 写入的内容
	 */
	public static void writerTXT(String path, String content) {
		FileWriter writer = null;
		try {
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			writer = new FileWriter(file, true);
			writer.write(content + "\n");

		} catch (Exception ex) {
			ex.printStackTrace();
			ex.getMessage();
			logger.error("文件写入失败！",ex);

		} finally {// 不管成不成功都关闭
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
//	public static void main(String[] args) {
//		
//		WriteFile file = new WriteFile();
//		file.writerTXT("this is a test!");
//		
//	}
}
