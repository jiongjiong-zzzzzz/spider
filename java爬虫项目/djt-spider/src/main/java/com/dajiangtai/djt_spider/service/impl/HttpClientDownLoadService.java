package com.dajiangtai.djt_spider.service.impl;

import com.dajiangtai.djt_spider.entity.Page;
import com.dajiangtai.djt_spider.service.IDownLoadService;
import com.dajiangtai.djt_spider.util.PageDownLoadUtil;
/**
 * HttpClient页面下载实现类
 * @author dajiangtai
 * created by 2016-10-28
 */
public class HttpClientDownLoadService implements IDownLoadService {

	public Page download(String url) {
		// TODO Auto-generated method stub
		Page page = new Page();
		page.setContent(PageDownLoadUtil.getPageContent(url));
		page.setUrl(url);
		return page;
	}

}
