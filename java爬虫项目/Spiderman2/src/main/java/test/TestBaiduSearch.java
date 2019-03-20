package test;

import net.kernal.spiderman.Bootstrap;

/**
 * 以动态关键词在百度搜索作为入口的采集例子 
 */
public class TestBaiduSearch {

	public static void main(String[] args) {
		Bootstrap.main(new String[]{"-conf", "baidu-search-example.xml"});
	}
	
}
