package test;

import net.kernal.spiderman.Config;
import net.kernal.spiderman.Config.Pages;
import net.kernal.spiderman.Config.Seeds;
import net.kernal.spiderman.Spiderman;
import net.kernal.spiderman.kit.DefaultConfBuilder;
import net.kernal.spiderman.kit.Properties;
import net.kernal.spiderman.kit.Seed;
import net.kernal.spiderman.worker.extract.extractor.impl.HtmlCleanerExtractor;
import net.kernal.spiderman.worker.extract.schema.Model;
import net.kernal.spiderman.worker.extract.schema.Page;
import net.kernal.spiderman.worker.result.handler.impl.ConsoleResultHandler;

/**
 * 这个测试代码完全使用Java代码的方式来配置抽取规则，可以看到配置躲起来之后代码不太好看，至少是比较繁杂的。
 * 另外一个TestXML例子就使用大部分配置通过XML文件加载，小部分用Java代码处理，看起来会好很多。
 */
public class TestQFang {
	
	public static void main(String[] args) {
		final Config conf = new DefaultConfBuilder() {
			public void configPages(Pages pages) {
				pages.add(new Page("详情") { 
					public void config(UrlMatchRules rules, Models models) {
						this.setExtractorBuilder(HtmlCleanerExtractor.builder());
						rules.addEqualsRule("http://hangzhou.qfang.com/garden/desc/1758");
						Model model = models.addModel("demo");
						model.addField("xml")
							.set("isSerialize", true)
							.set("xpath", "//div[@class='head-info-list']");
						model.addField("all")
							.set("isArray", true)
							.set("xpath", "//div[@class='head-info-list']//li/p/text()");
						model.addField("标题")
							.set("xpath", "//title/text");
						model.addField("建筑年代")
							.set("xpath", "//div[@class='head-info-list']//li[1]/p/text()");
						model.addField("停车位")
							.set("xpath", "//div[@class='head-info-list']//li[2]/p/text()");
						model.addField("停车费用")
							.set("xpath", "//div[@class='head-info-list']//li[3]/p/text()");
						model.addField("容积率")
							.set("xpath", "//div[@class='head-info-list']//li[4]/p/text()");
						model.addField("绿化率")
							.set("xpath", "//div[@class='head-info-list']//li[5]/p/text()");
						model.addField("物业费")
							.set("xpath", "//div[@class='head-info-list']//li[6]/p/text()");
						model.addField("物业公司")
							.set("xpath", "//div[@class='head-info-list']//li[7]/p/text()");
						model.addField("开发商")
							.set("xpath", "//div[@class='head-info-list']//li[8]/p/text()");
					}
				});
			}
			public void configSeeds(Seeds seeds) {
				seeds.add(new Seed("QFang", "http://hangzhou.qfang.com/garden/desc/1758"));
			}
			public void configParams(Properties params) {
				params.put("logger.level", "DEBUG");
				params.put("downloader.proxy", "172.20.0.2:8086");// 代理，如果你所在公司需要代理才能上网记得要设置成你们公司的代理IP哦
//				params.put("duration", "10s");// 运行时间
//				params.put("scheduler.period", "1m");// 调度间隔时间，每隔固定时间重新将种子任务放入队列，并清除一些不需要持久化的消息key
				params.put("worker.result.limit", 2);
				params.put("worker.download.size", 1);// 下载线程数
				params.put("worker.download.delay", "100");// 下载延迟时间，降低频率，免得被封
				params.put("worker.extract.size", 1);// 解析线程数
				params.put("worker.result.size", 1);// 结果处理线程数
				params.put("queue.store.path", "store");
				params.put("queue.zbus.enabled", false);// 分布式采集时要开启
				params.put("queue.zbus.broker", "jvm");// 1.jvm(进程内模式) 2.ip:port(单机模式) 3.[ip:port,ip:port](高可用多机模式)
			}
		}
		.setResultHandler(new ConsoleResultHandler())
		.build();
		
		new Spiderman(conf).go();//别忘记看控制台信息哦，结束之后会有统计信息的
	}
	
}
