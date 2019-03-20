package com.dajiangtai.djt_spider.start;

import com.dajiangtai.djt_spider.entity.Page;
import com.dajiangtai.djt_spider.service.IDownLoadService;
import com.dajiangtai.djt_spider.service.IProcessService;
import com.dajiangtai.djt_spider.service.IStoreService;
import com.dajiangtai.djt_spider.service.impl.ConsoleStoreService;
import com.dajiangtai.djt_spider.service.impl.HBaseStoreService;
import com.dajiangtai.djt_spider.service.impl.HttpClientDownLoadService;
import com.dajiangtai.djt_spider.service.impl.YOUKUProcessService;

/**
 * 电视剧爬虫执行入口类
 * @author dajiangtai
 * created by 2016-10-28
 *
 */
public class StartDSJCount {
	private IDownLoadService downLoadSerivce ;
	private IProcessService processService;
	
	private IStoreService storeService;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		StartDSJCount dsj = new StartDSJCount();
		dsj.setDownLoadSerivce(new HttpClientDownLoadService());
		dsj.setProcessService(new YOUKUProcessService());
		//dsj.setStoreService(new ConsoleStoreService());
		dsj.setStoreService(new HBaseStoreService());
		
		String url = "http://www.youku.com/show_page/id_z9cd2277647d311e5b692.html?spm=a2htv.20005143.m13050845531.5~5~1~3~A&from=y1.3-tv-index-2640-5143.40177.1-1";
		//下载页面
		Page page = dsj.downloadPage(url);
		//解析页面
		dsj.processPage(page);
		//System.out.println(page.getContent());
		
		//存储页面信息
		dsj.storePageInfo(page);
	}

	/**
	 * 下载页面
	 * @param url
	 * @return
	 */
	public Page downloadPage(String url){
		return this.downLoadSerivce.download(url);		
	}
	
	/**
	 * 页面解析
	 * @param page
	 */
	public void processPage(Page page){
		this.processService.process(page);
	}
	
	/**
	 * 存储页面信息
	 * @return
	 */
	public void storePageInfo(Page page){
		this.storeService.store(page);
	}
	public IDownLoadService getDownLoadSerivce() {
		return downLoadSerivce;
	}

	public void setDownLoadSerivce(IDownLoadService downLoadSerivce) {
		this.downLoadSerivce = downLoadSerivce;
	}

	public IProcessService getProcessService() {
		return processService;
	}

	public void setProcessService(IProcessService processService) {
		this.processService = processService;
	}

	public IStoreService getStoreService() {
		return storeService;
	}

	public void setStoreService(IStoreService storeService) {
		this.storeService = storeService;
	}

	
}
