package com.combanc.proxy;

import com.combanc.pojo.IPMessage;
import com.combanc.proxy.util.ProxyCheck;
import com.combanc.redis.RedisIpList;
/**
 * @Title:           GetProxy
 * @Description:     从Redis里获取ip
 * @Company:         combanc
 * @Author:          shihw
 * @Date:            2018/9/19
 * @JDK:             1.8
 * @Encoding:        UTF-8
 */
public class GetProxy {
	private final static Object lock = new Object();    // 用于与 ip-proxy-pool 进行协作的锁
	
	/**
	 * 从Redis里获取ip
	 */
	public static IPMessage getPorxy(){
			
			IPMessage ipMessage = null;
			boolean flag = true;
			
			while (true) {
				if (flag) {
					synchronized (lock) {
						while (RedisIpList.isEmpty("ip-proxy-pool")) {
							try {
								System.out.println("当前线程：" + Thread.currentThread().getName() + ", "
										+ "发现ip-proxy-pool已空, 开始进行等待... ...");
								lock.wait(10000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
	
						ipMessage = RedisIpList.getIPByList("ip-proxy-pool");
						//测试ip
						boolean check = ProxyCheck.checkProxy(ipMessage.getIPAddress(), ipMessage.getIPPort());
						
						if(check){
							flag = false;
						}
					}
				}
				
				if(!flag){
					break;
				}
			}
			return ipMessage;
			
	
	}
}
