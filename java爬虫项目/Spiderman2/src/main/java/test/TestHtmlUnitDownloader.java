package test;

import net.kernal.spiderman.worker.download.Downloader;
import net.kernal.spiderman.worker.download.impl.HtmlUnitDownloader;

/**
 * @author 赖伟威 l.weiwei@163.com
 * @author <a href='http://krbit.github.io'>Krbit</a>
 * @version V0.1.0
 */
public class TestHtmlUnitDownloader {

    public static void main(String[] args) {
        final String url = "http://www.tianyancha.com/search?base=%E5%B9%BF%E4%B8%9C";
        Downloader downloader = new HtmlUnitDownloader();
        Downloader.Request req = new Downloader.Request(url);
        Downloader.Response resp = downloader.download(req);
        final Throwable err = resp.getException();
        if (err != null) {
        	err.printStackTrace();
        	return;
        }
        final String body = resp.getBodyStr();
        System.out.println(body);
    }
}
