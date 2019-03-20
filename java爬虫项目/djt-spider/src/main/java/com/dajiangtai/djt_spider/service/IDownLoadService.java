package com.dajiangtai.djt_spider.service;

import com.dajiangtai.djt_spider.entity.Page;

/**
 * 页面下载接口
 * @author dajiangtai
 * created by 2016-10-28
 */
public interface IDownLoadService {
	public Page download(String url);
}
