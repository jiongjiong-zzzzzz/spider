package com.combanc.redis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
 
/**
 * Title:           RedisPool
 * Description:     redis连接池
 * Company:         combanc
 * Author:          shihw
 * Date:            2018/8/2
 * JDK:             1.8
 * Encoding:        UTF-8
 */
public class RedisPool {
 
    private final static Logger logger = LoggerFactory.getLogger(RedisPool.class);
 
    private static JedisPool readPool = null;
    private static JedisPool writePool = null;
    private static int time = 0;
    //静态代码初始化池配置
    static {
        try{
            Properties props = new Properties();
            InputStream in = RedisPool.class.getResourceAsStream("/conf" + File.separator + "redis-connection.properties");
            props.load(in);
 
            //创建jedis池配置实例
            JedisPoolConfig config = new JedisPoolConfig();
            //设置池配置项值
            config.setMaxTotal(Integer.valueOf(props.getProperty("MaxTotal")));
            config.setMaxIdle(Integer.valueOf(props.getProperty("MaxIdle")));
            config.setMaxWaitMillis(Long.valueOf(props.getProperty("MaxWaitMillis")));
            config.setTestOnBorrow(Boolean.valueOf(props.getProperty("TestOnBorrow")));
//            config.setTestOnReturn(Boolean.valueOf(props.getProperty("TestOnReturn")));
            time = Integer.valueOf(props.getProperty("ExpireTime"));
            //根据配置实例化jedis池
            readPool = new JedisPool(config, props.getProperty("readIp"), Integer.valueOf(props.getProperty("readPort")));
            writePool = new JedisPool(config, props.getProperty("writeIp"), Integer.valueOf(props.getProperty("writePort")));
        }catch (IOException e) {
            logger.info("redis连接池异常",e);
        }
    }
 
 
    /**获得jedis对象*/
    public static Jedis getReadJedisObject(){
    	
    	
        return readPool.getResource();
    }
    /**获得jedis对象*/
    public static Jedis getWriteJedisObject(){
        return writePool.getResource();
    }
 
    /**归还jedis对象*/
    public static void returnJedisOjbect(Jedis jedis){
        if (jedis != null) {
            jedis.close();
        }
    }
	public static int getTime() {
		return time;
	}
	public static void setTime(int time) {
		RedisPool.time = time;
	}
 
}