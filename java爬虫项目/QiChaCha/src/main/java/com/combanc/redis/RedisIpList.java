package com.combanc.redis;

import java.util.List;

import com.combanc.pojo.IPMessage;

import redis.clients.jedis.Jedis;
/**
 * @Title:           RedisIpList
 * @Description:     代理ip池
 * @Company:         combanc
 * @Author:          shihw
 * @Date:            2018/9/18
 * @JDK:             1.8
 * @Encoding:        UTF-8
 */
public class RedisIpList {
	private static String proxyIplist = "ip-proxy-pool";
	/**
	 * 将单个ip信息保存在Redis列表中
	 * @param listName   列表名称
	 * @param ipMessage	 ip		
	 */
    public static void setIPToList(IPMessage ipMessage) {
    	
    	Jedis jedis = null;
		try {
			jedis =  RedisPool.getWriteJedisObject();
			jedis.select(2);
			jedis.rpush(proxyIplist, ipMessage.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally { 
			RedisPool.returnJedisOjbect(jedis);
		}
    	
    	
    	
    }

    /**
     * 将多个ip信息保存在Redis列表中
     * @param listName   列表名称
     * @param ipMessages 多个ip
     */
    public static void setIPToList(List<IPMessage> ipMessages) {
    	
    	Jedis jedis = null;
		try {
			jedis =  RedisPool.getWriteJedisObject();
			jedis.select(2);
			for (IPMessage ipMessage : ipMessages) {
				jedis.rpush(proxyIplist, ipMessage.toString());
	        }
		} catch (Exception e) {
			e.printStackTrace();
		} finally { 
			RedisPool.returnJedisOjbect(jedis);
		}
        
    }

    // 
    /**
     * 从Redis里取出ip
     * @param listName	   列表名称
     * @return IPMessage ip
     */
    public static IPMessage getIPByList(String listName) {
    	Jedis jedis = null;
    	IPMessage ipMessage = new IPMessage();
		try {
			jedis =  RedisPool.getWriteJedisObject();
			jedis.select(2);
			String proxy = jedis.lpop(listName);
			ipMessage.setIPAddress(proxy.split(":")[0]);
			ipMessage.setIPPort(proxy.split(":")[1]);
		} catch (Exception e) {
			e.printStackTrace();
		} finally { 
			RedisPool.returnJedisOjbect(jedis);
		}
    	
        return ipMessage;
    }

    /**
     * 判断IP代理池是否为空
     * @param listName	列表名称
     * @return	boolean 
     */
    public static boolean isEmpty(String listName) {
    	
    	Jedis jedis = null;
    	Long flag = (long) 0;
		try {
			jedis =  RedisPool.getWriteJedisObject();
			jedis.select(2);
			flag = jedis.llen(listName);
		} catch (Exception e) {
			e.printStackTrace();
		} finally { 
			RedisPool.returnJedisOjbect(jedis);
		}
    	
        return flag <= 0;
    }

}
