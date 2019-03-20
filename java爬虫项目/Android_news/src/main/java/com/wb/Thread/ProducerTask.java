package com.wb.Thread;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.wb.util.ChangeCharset;
import com.wb.util.JsoupClient;

/**
 * Created by wb on 18-7-31.
 *
 * 我们只保证在给数据库中写入URL，还有改变线程线程计数器的值的时候，是需要同步的。
 *
 * 线程计数器threads是所有线程共享的。
 */

public class ProducerTask extends Thread {
	// 线程计数器需要对所有线程可见，是共享变量
	private String keyword;
	// 公司id
	private int cid;
	// 爬取页数
	private int page;
	static int num = 1;
	static JsoupClient client = new JsoupClient();
	private static Logger logger = Logger.getLogger(ProducerTask.class);
	private static final int DEFAULT_RANGE_FOR_SLEEP = 1000; // 缺省睡眠时间
	private BlockingQueue<String> queue;

	public ProducerTask() {
	}

	public ProducerTask(String keyword, int cid, int page, BlockingQueue<String> queue) {
		super();
		this.keyword = keyword;
		this.cid = cid;
		this.page = page;
		this.queue = queue;
	}

	public synchronized void parseToVisitUrltoRedis() throws Exception {
		// 阻塞队列
		logger.info(keyword + "：  开始采集...");
		long startTime = System.currentTimeMillis();
		Random r = new Random();
		String data = null;
		String key = "site%3Akuaibao.qq.com%20" + keyword;
		String json = "https://m.sm.cn/s?q=" + key + "&page=" + page;
		System.out.println(Thread.currentThread().getName() + "----" + json);
		String doc[] = client.getHtmlPageUC(json).split(",");
		for (String string : doc) {
			if (string.contains("http")) {
				Thread.sleep(r.nextInt(DEFAULT_RANGE_FOR_SLEEP));// 取0~DEFAULT_RANGE_FOR_SLEEP值的一个随机数
				data = string + "," + keyword + "," + cid;// 以原子方式将count当前值加1
				System.out.println(Thread.currentThread().getName() + "将数据：" + data + "放入队列...");
				if (!queue.offer(data, 2, TimeUnit.SECONDS)) {// 设定的等待时间为2s，如果超过2s还没加进去返回true
					System.out.println("放入数据失败：" + data);
					logger.debug("放入数据失败：" + data);
				}
			}
		}
		long stopTime = System.currentTimeMillis();
		System.out.println("爬虫结束------" + "共用时间" + (stopTime - startTime) / 1000L + "秒");
	}

	public void run() {
		try {
			parseToVisitUrltoRedis();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.gc();
		}
	}

}
