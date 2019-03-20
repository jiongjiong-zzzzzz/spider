package test;

import net.kernal.spiderman.Config;
import net.kernal.spiderman.Spiderman;
import net.kernal.spiderman.kit.XMLConfBuilder;

public class Tianyancha {

	public static void main(String[] args) throws ClassNotFoundException {
		final String xml = "tianyancha.xml";
		final Config conf = new XMLConfBuilder(xml).build();// 通过XMLBuilder构建CONF对象
		new Spiderman(conf).go();//启动，别忘记看控制台信息哦，结束之后会有统计信息的
	}
	
}
