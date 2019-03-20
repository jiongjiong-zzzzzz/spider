package com.dajiangtai.djt_spider.service.impl;

import java.io.IOException;

import com.dajiangtai.djt_spider.entity.Page;
import com.dajiangtai.djt_spider.service.IStoreService;
import com.dajiangtai.djt_spider.util.HbaseUtil;
/**
 * 数据存储实现类HBase
 * @author dajiangtai
 *
 */
public class HBaseStoreService implements IStoreService {
	HbaseUtil hbaseUtil = new HbaseUtil();
	public void store(Page page) {
		// TODO Auto-generated method stub
		String tvId = page.getTvId();
		try {
			hbaseUtil.put(HbaseUtil.TABLE_NAME, tvId, HbaseUtil.COLUMNFAMILY_1, HbaseUtil.COLUMNFAMILY_1_URL, page.getUrl());
			hbaseUtil.put(HbaseUtil.TABLE_NAME, tvId, HbaseUtil.COLUMNFAMILY_1, HbaseUtil.COLUMNFAMILY_1_ALLNUMBER, page.getAllnumber());
			hbaseUtil.put(HbaseUtil.TABLE_NAME, tvId, HbaseUtil.COLUMNFAMILY_1, HbaseUtil.COLUMNFAMILY_1_COMMENTNUMBER, page.getCommentnumber());
			hbaseUtil.put(HbaseUtil.TABLE_NAME, tvId, HbaseUtil.COLUMNFAMILY_1, HbaseUtil.COLUMNFAMILY_1_SUPPORTNUMBER, page.getSupportnumber());
			hbaseUtil.put(HbaseUtil.TABLE_NAME, tvId, HbaseUtil.COLUMNFAMILY_1, HbaseUtil.COLUMNFAMILY_1_AGAINSTNUMBER, page.getAgainstnumber());
			hbaseUtil.put(HbaseUtil.TABLE_NAME, tvId, HbaseUtil.COLUMNFAMILY_1, HbaseUtil.COLUMNFAMILY_1_DAYNUMBER, page.getDaynumber());
			hbaseUtil.put(HbaseUtil.TABLE_NAME, tvId, HbaseUtil.COLUMNFAMILY_1, HbaseUtil.COLUMNFAMILY_1_COLLECTNUMBER, page.getCollectnumber());		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
