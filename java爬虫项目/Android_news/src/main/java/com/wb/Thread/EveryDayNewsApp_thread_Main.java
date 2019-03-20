package com.wb.Thread;

import static org.junit.Assume.assumeFalse;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.print.Doc;

import org.apache.commons.codec.digest.DigestUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.kevin.crop.R.string;
import com.wb.util.ChangeCharset;
import com.wb.util.JsoupClient;
import com.wb.util.RedisHash;
import com.wb.util.TestRegex;

public class EveryDayNewsApp_thread_Main {

	public static void main(String[] args) throws InterruptedException {
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10);
		for (int y = 1; y <= 10; y++) {
			System.out.println("**************" + Thread.currentThread().getName() + "**************");
			long start = System.currentTimeMillis();
			System.out.println("开始爬虫.........................................");
			ExecutorService pool = null;
			// pool = Executors.newCachedThreadPool();
			pool = Executors.newFixedThreadPool(10);
			// pool = Executors.newSingleThreadExecutor();
			pool.execute(new ProducerTask("诺亚财富", 0, y, queue));
			pool.execute(new ConsumerTask(queue));
			// 释放资源
			pool.shutdown();
			while (true) {
				if (pool.isTerminated()) {
					long end = System.currentTimeMillis();
					System.out.println("爬虫结束.........................................");
					System.out.println("总共耗时" + (end - start) / 1000 + "秒");
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("-----------------------------------------------------------");
		}
	}
}