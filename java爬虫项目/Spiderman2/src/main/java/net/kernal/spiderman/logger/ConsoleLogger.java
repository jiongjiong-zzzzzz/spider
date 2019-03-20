package net.kernal.spiderman.logger;

import net.kernal.spiderman.kit.K;

public class ConsoleLogger implements Logger {

	private byte level;
	private String name;
	public ConsoleLogger(Class<?> clazz, byte level) {
		this.name = clazz.getName();
		this.level = level;
	}
	
	public void info(String msg) {
		if (level > Logger.LEVEL_INFO) {
			return;
		}
		System.out.println("[Spiderman][消息]["+name+"]"+K.formatNow()+"\r\n"+msg+"\r\n");
	}
	
	public void debug(String msg) {
		if (level > Logger.LEVEL_DEBUG) {
			return;
		}
		System.out.println("[Spiderman][调试]["+name+"]"+K.formatNow()+"\r\n"+msg+"\r\n");
	}
	
	public void warn(String msg) {
		if (level > Logger.LEVEL_WARN) {
			return;
		}
		System.err.println("[Spiderman][警告]["+name+"]"+K.formatNow()+"\r\n"+msg+"\r\n");
	}
	
	public void error(String err, Throwable e) {
		if (level > Logger.LEVEL_ERROR) {
			return;
		}
		
		System.err.println("[Spiderman][错误]["+name+"]"+K.formatNow()+"\r\n"+err+"\r\n");
		e.printStackTrace();
	}

}
