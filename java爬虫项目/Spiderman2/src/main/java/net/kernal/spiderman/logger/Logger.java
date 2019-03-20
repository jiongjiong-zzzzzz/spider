package net.kernal.spiderman.logger;

public interface Logger {
	
	public final static byte LEVEL_INFO		= 1;
	public final static byte LEVEL_DEBUG	= 2;
	public final static byte LEVEL_WARN		= 3;
	public final static byte LEVEL_ERROR	= 4;
	public final static byte LEVEL_OFF		= 5;
	
	public static Byte getLevel(String l) {
		Byte level = null;
		switch(l) {
		case "OFF":
			level = Logger.LEVEL_OFF;
			break;
		case "DEBUG":
			level = Logger.LEVEL_DEBUG;
			break;
		case "WARN":
			level = Logger.LEVEL_WARN;
			break;
		case "ERROR":
			level = Logger.LEVEL_ERROR;
			break;
		default:
			level = Logger.LEVEL_INFO;
			break;
		}
		return level;
	}
	
	public void info(String msg);
	public void debug(String msg);
	public void warn(String msg);
	public void error(String err, Throwable e);
	
}
