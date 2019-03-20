package com.combanc.redis;

import java.util.Set;

import org.apache.log4j.Logger;

import com.combanc.util.NowDate;

import redis.clients.jedis.Jedis;
/**
 * Title:           RedisHash
 * Description:     Url去重
 * Company:         combanc
 * Author:          shihw
 * Date:            2018/10/26
 * JDK:             1.8
 * Encoding:        UTF-8
 */
public class RedisHash {
	private static Logger logger = Logger.getLogger(RedisHash.class);
	/**
	 * 增加 
	 * @param hashsList list名称
	 * @param href		url  -- key
	 * 					时间戳   -- value
	 * @return			true  该url已存在
	 * 					false 该url不存在
	 */
	public static void addHash(String hashsList, String href) {
		String value = String.valueOf(System.currentTimeMillis());
		Jedis jedis = null;
		try {
			String date = NowDate.createDateM();
			hashsList = date + " " +hashsList;
			jedis = RedisPool.getWriteJedisObject();
			jedis.select(3);
			jedis.hset(hashsList, href, value);
			logger.info("新增" + href + "→" + hashsList);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisPool.returnJedisOjbect(jedis);
		}
	}
	
	/**
	 * 查看某个key的剩余生存时间,单位【秒】.永久生存返回-1 不存在返回 -2
	 * @param hashsList hash名称
	 * @return 剩余生存时间
	 */
	public static Long ttlHash(String hashsList){
		Jedis jedis = null;
		Long time = null;
		try {
			jedis =  RedisPool.getWriteJedisObject();
			jedis.select(3);
			time = jedis.ttl(hashsList);
			//System.out.println("查看"+hashsList+"的剩余生存时间："+time);
		} catch (Exception e) {
			e.printStackTrace();
		} finally { 
			RedisPool.returnJedisOjbect(jedis);
		}
		return time;
	}
	   
	/**
	 * 设置 hashsList的过期时间
	 * @param hashsList   hash名称
	 * @param expireTime  过期时间（秒）
	 */
	public static void expireHash(String hashsList,int expireTime){
		Jedis jedis = null;
		try {
			jedis = RedisPool.getWriteJedisObject();
			jedis.select(3);
			jedis.expire(hashsList, expireTime);
			//System.out.println("设置"+hashsList+"的过期时间为："+expireTime+"秒");
		} catch (Exception e) {
			e.printStackTrace();
		} finally { 
			RedisPool.returnJedisOjbect(jedis);
		}
	}
   
	
	
	/**
	 *  判断key是否存在
	 * @param listName  hash名称
	 * @param key		键
	 * @return			存在返回 true
	 * 					不存在返回false
	 */
	public static boolean operateHash(String listName, String key) {
		Jedis jedis = null;
		boolean flax = false;
		try {
			jedis = RedisPool.getReadJedisObject();
			jedis.select(3);
			Set<String> keys = keys(listName);
			
			for (String hash : keys) {
				flax = jedis.hexists(hash, key);
				if(flax){
					return flax;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally { 
			RedisPool.returnJedisOjbect(jedis);
		}
		return flax;
	}

	
	/**
	 * 匹配包含 关键词 的 keys
	 * @param pattern 关键词
	 * @return
	 */
	public static Set<String> keys(String pattern) {
		Jedis jedis = null;
		Set<String> keys = null;
		try {
			jedis = RedisPool.getReadJedisObject();
			jedis.select(3);
			keys = jedis.keys("*" + pattern);
		} catch (Exception e) {
			e.printStackTrace();
		} finally { 
			RedisPool.returnJedisOjbect(jedis);
		}
		
		return keys;
	}

	

//	public static void main(String[] agrs) {
//		
//		Set<String> keys = keys("测试");
//		
//		for (String string : keys) {
//			System.out.println(string);
//		}
//		
//	}
}
