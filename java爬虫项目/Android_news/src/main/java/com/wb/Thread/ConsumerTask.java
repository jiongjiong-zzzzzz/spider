package com.wb.Thread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.wb.util.JsoupClient;
import com.wb.util.RedisHash;

public class ConsumerTask implements Runnable {
	private static Logger logger = Logger.getLogger(ConsumerTask.class);
	static JsoupClient client = new JsoupClient();
	static String website = "https://kuaibao.qq.com/---app";
	BlockingQueue<String> queue;
	private static final int DEFAULT_RANGE_FOR_SLEEP = 1000;

	public ConsumerTask(BlockingQueue<String> queue) {
		this.queue = queue;
	}

	public ConsumerTask() {
		// TODO Auto-generated constructor stub
	}

	public void run() {
		try {
			getUrl();
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.gc();
		}
	}

	public synchronized void getUrl() {
		System.out.println("启动消费者线程！");
		Random r = new Random();
		try {
			System.out.println("正从队列获取数据...");
			String data = queue.poll(2, TimeUnit.SECONDS);// 有数据时直接从队列的队首取走，无数据时阻塞，在2s内有数据，取走，超过2s还没数据，返回失败
			if (null != data) {
				System.out.println("拿到数据：" + data);
				System.out.println("正在消费数据：" + data);
				String[] datas = data.split(",");
				ConsumerTask task = new ConsumerTask();
				task.getContent(datas[0], datas[1], Integer.parseInt(datas[2]));
				Thread.sleep(r.nextInt(DEFAULT_RANGE_FOR_SLEEP));
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			System.out.println("退出消费者线程！");
		}

	}

	public void getContent(String href, String word, int cid) throws Exception {
		String link = href + "?refer=kb_news&omgid=e7cc81abfd5dbb30c1794025244697be";
		Document doc = client.DownHtml(link);
		String title = doc.select("#content > p").text();
		System.out.println("标题:" + title);
		String author = doc.select("#content > div.artinfo > span.author").text();
		System.out.println("作者:" + author);
		String logo = doc.select("div.cardBoxWrap > div > a > img").attr("src");
		System.out.println("logo:" + logo);
		String time = doc.select("#content > div.artinfo > span.time").text();
		System.out.println("时间:" + time);
		String content = doc.select("#content > div.content-box").text();
		if (content.equals("")) {
			content = doc.select("#content").text();
		}
		System.out.println("内容:" + content);
		Elements select = doc.select("p img");
		String imgs = "";
		for (Element element : select) {
			imgs = imgs + element.attr("src") + ",";
		}
		System.out.println("图片:" + imgs);

	}
}
