package test;

import net.kernal.spiderman.Config;
import net.kernal.spiderman.Spiderman;
import net.kernal.spiderman.kit.XMLConfBuilder;

/**
 * 测试单篇文章分页, 单篇文章内容分页之后是需要严格恢复它原来的顺序的，因此使用线程内递归抽取的模式，效率会慢许多
 * @author 赖伟威 l.weiwei@163.com 2016-01-26
 *
 */
public class TestSinglePageUseXML {

	public static void main(String[] args) {
		final String xml = "single-page-example.xml";
		final Config conf = new XMLConfBuilder(xml).build();// 通过XMLBuilder构建CONF对象
		new Spiderman(conf).go();//启动，别忘记看控制台信息哦，结束之后会有统计信息的
	}
		
}
