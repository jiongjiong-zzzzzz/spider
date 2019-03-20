package com.wb.util;


 

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;



public class RedisHash {
	Jedis	 jedis =new RedisConnection().jedis;
	ShardedJedis shardedJedis =new RedisConnection().shardedJedis;
	public void addHash(String hashslist,String key){
		String value =String.valueOf(System.currentTimeMillis());
		boolean falx = operateHash(hashslist,key);
	    if(falx == true){
	       System.out.println(key+"已存在");
	    }else{
	    	shardedJedis.hset(hashslist, key,value);
	    	System.out.println("新增"+key+"→"+hashslist);
	    }	    
	}
	public boolean operateHash(String listName,String key){
		  boolean flax =  shardedJedis.hexists(listName,key);
			return flax;    
	}
	public Long listlen(){
		Long count =  shardedJedis.hlen("hashlist");
		return count;
	}
	public void closeRedis() {
		jedis.close();
	}
	
}
