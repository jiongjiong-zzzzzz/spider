package com.dajiangtai.djt_spider.service.impl;

import com.dajiangtai.djt_spider.entity.Page;
import com.dajiangtai.djt_spider.service.IStoreService;
/**
 * 数据存储实现类Console
 * @author dajiangtai
 *
 */
public class ConsoleStoreService implements IStoreService {

	public void store(Page page) {
		// TODO Auto-generated method stub
		System.out.println("tvId="+page.getTvId());
		System.out.println("allnumber="+page.getAllnumber());
		System.out.println("commentnumber="+page.getCommentnumber());
		System.out.println("supportnumber="+page.getSupportnumber());
		System.out.println("daynumber="+page.getDaynumber());
		System.out.println("againstnumber="+page.getAgainstnumber());
		System.out.println("collectnumber="+page.getCollectnumber());
	}

}
