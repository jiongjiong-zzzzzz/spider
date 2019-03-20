package net.kernal.spiderman.queue;

import java.io.IOException;

import org.zbus.broker.Broker;
import org.zbus.mq.Consumer;
import org.zbus.mq.MqConfig;
import org.zbus.mq.Producer;
import org.zbus.net.Sync.ResultCallback;
import org.zbus.net.http.Message;

import net.kernal.spiderman.Spiderman;
import net.kernal.spiderman.kit.K;
import net.kernal.spiderman.logger.Logger;
import net.kernal.spiderman.logger.Loggers;
import net.kernal.spiderman.queue.Queue.Element;

/**
 * PS:由于ZBus支持队列元素的重复检查，所以此类不需要继承CheckableQueue
 * @author 赖伟威 l.weiwei@163.com 2016-01-19
 *
 */
public class ZBusQueue<E extends Element> implements Queue<E> {

	private static final Logger logger = Loggers.getLogger(ZBusQueue.class);
	private final MqConfig cfg;
	private Producer producer;
	private Consumer consumer;
//	private boolean isConsumerAlive;
//	private boolean isBeingRepaired;
	
	public ZBusQueue(Broker broker, String mq) {
	    this.cfg = new MqConfig(); 
	    this.cfg.setBroker(broker);
	    this.cfg.setMq(mq);
	    
	    this.producer = new Producer(cfg);
		try {
			this.producer.createMQ();
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}
		
		this.consumer = new Consumer(cfg);
	}
	
	@SuppressWarnings("unchecked")
	public E take() {
		Message msg = null;
		try {
			msg = consumer.take();
		} catch (InterruptedException e) {
			return null;
		} catch (IOException e) {
//			// 若没有其他线程正在处理，则当前线程负责挂上“Consumer不可用”的牌子
//			if (!this.isBeingRepaired) {
//				// 告诉其他线程，当前已经有一个线程正在修复consumer
//				this.isConsumerAlive = false;
//			} else {
//				// 若已经有线程正在处理，那么排队等待就行了
//			}
//			// 大家排好队等待第一个进去的线程负责把consumer修复好
//			synchronized (this) {
//				// 排队进来后，首先要看看consumer是否已经被修复好了
//				if (!this.isConsumerAlive) {
//					this.isBeingRepaired = true;
//					try {
//						consumer.close();
//						consumer = null;
//						consumer = new Consumer(cfg); 
//						this.isConsumerAlive = true;
//					} catch (IOException e1) {
//					} finally {
//						this.isBeingRepaired = false;
//					}
//				}
//			}
			
			throw new Spiderman.Exception("zbus consumer recv error", e);
		}
		final byte[] data = msg.getBody();
		return (E)K.deserialize(data);
	}

	public void append(Element e) {
		byte[] data = K.serialize(e);
		Message msg = new Message();
		if (e instanceof AbstractElement) {
			AbstractElement ae = ((AbstractElement)e);
			final String key = ae.getKey();
			final String group = ae.getGroup();
			msg.setKey(key);
			msg.setKeyGroup(group);
		}
		msg.setBody(data);
		try {
			producer.sendAsync(msg, new ResultCallback<Message>() {
				public void onReturn(Message result) {
					if (result.isStatus406()) {
						logger.warn("队列消息重复[group="+msg.getKeyGroup()+", key="+msg.getKey()+"]");
					} else if (!result.isStatus200()) {
						logger.warn("队列消息发送失败[group="+msg.getKeyGroup()+", key="+msg.getKey()+"]:"+result.getBodyString());
					}
				}
			});
		} catch (IOException ex) {
			throw new Spiderman.Exception("zbus producer invoke error", ex);
		}
	}

	public void clear() {
		try {
			String mq = this.consumer.getMq();
			this.consumer.close();
			logger.debug("ZBus Queue["+mq+"] 停止");
		} catch (Throwable e) {
			throw new Spiderman.Exception("zbus client close error", e);
		}
	}
	
	public void removeKeys(String group) {
		try {
			this.producer.removeGroup(group);
			logger.warn("删除Keys成功[mq="+this.producer.getMq()+", group="+group+"]");
		} catch (Throwable e) {
			logger.error("删除Keys失败[[mq="+this.producer.getMq()+", group="+group+"]", e);
		}
	}


	@Override
	public void append(byte[] data) {
		Message msg = new Message();
		msg.setBody(data);
		try {
			producer.sendAsync(msg, new ResultCallback<Message>() {
				public void onReturn(Message result) {
					if (!result.isStatus200()) {
						logger.warn("队列消息发送失败[group="+msg.getKeyGroup()+", key="+msg.getKey()+"]:"+result.getBodyString());
					}
				}
			});
		} catch (IOException ex) {
			throw new Spiderman.Exception("zbus producer invoke error", ex);
		}
	}

}
