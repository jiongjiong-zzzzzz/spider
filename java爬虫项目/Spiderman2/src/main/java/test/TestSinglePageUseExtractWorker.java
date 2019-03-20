package test;

import java.util.Arrays;

import com.alibaba.fastjson.JSON;

import net.kernal.spiderman.worker.download.DownloadWorker;
import net.kernal.spiderman.worker.download.Downloader;
import net.kernal.spiderman.worker.download.impl.HttpClientDownloader;
import net.kernal.spiderman.worker.extract.ExtractTask;
import net.kernal.spiderman.worker.extract.ExtractWorker;
import net.kernal.spiderman.worker.extract.extractor.Extractor;
import net.kernal.spiderman.worker.extract.extractor.impl.HtmlCleanerExtractor;
import net.kernal.spiderman.worker.extract.schema.Model;
import net.kernal.spiderman.worker.extract.schema.Page;

/**
 * 测试单篇文章分页, 单篇文章内容分页之后是需要严格恢复它原来的顺序的，因此使用线程内递归抽取的模式，效率会慢许多
 * @author 赖伟威 l.weiwei@163.com 2016-01-26
 *
 */
public class TestSinglePageUseExtractWorker {

	public static void main(String[] args) {
		// 先下载第一页
		final DownloadWorker downloadWorker = new DownloadWorker(new HttpClientDownloader());
		final String url = "http://travel.163.com/14/0313/17/9N8002ON00063KE8.html";
		final Downloader.Request request = new Downloader.Request(url);
		final Downloader.Response response = downloadWorker.download(request);
		// 从这里开始计算耗时，前面的是第一页下载不纳入到递归分页耗时计算
		final long start = System.currentTimeMillis();//使用微秒即可
		// 解析, 使用HtmlClean解析器
		final Extractor.Builder builder = HtmlCleanerExtractor.builder();
		final ExtractTask task = new ExtractTask(response);
		// 页面抽取配置
		final Page page = new Page("单篇文章内容分页", builder) {
			public void config(UrlMatchRules rules, Models models) {
				rules.addRegexRule(".*");
				Model model = models.addModel("")
						// 这两个参数非常重要，全靠它们来递归抽取下一页内容了
						.set("fieldNameForNextPageUrl", "next")
						.set("fieldNameForNextPageContent", "content");
				model.addField("title")// 标题
					.set("xpath", "//title/text()");
				model.addField("next")// 下一页
					.set("xpath", "//div[@class='ep-pages']//a[@class='ep-pages-ctrl']")
					.set("attr", "href");
				model.addField("intro")// 文章简介
					.set("xpath", "//p[@class='ep-summary']/text()");
				model.addField("content")// 文章内容
					.set("xpath", "(//div[@id='endText'])[1]/text()");
			}
		};
		
		// 执行解析
		final ExtractWorker worker = new ExtractWorker(Arrays.asList(page), downloadWorker);
		worker.work(task, (p, result) -> {
			System.out.println("耗时："+(System.currentTimeMillis() - start)+"ms");
			System.out.println(JSON.toJSONString(result, true));
		});
	}
		
}
