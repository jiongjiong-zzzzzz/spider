package net.kernal.spiderman.logger;

import net.kernal.spiderman.kit.Properties;

public class Loggers {

	private final static Properties params = Properties.from("log.properties");
	
	public final static Logger getLogger(Class<?> clazz) {
		final byte level = params.getByte("logger.level", Logger.LEVEL_INFO);
		return new ConsoleLogger(clazz, level);
	}
	
}
