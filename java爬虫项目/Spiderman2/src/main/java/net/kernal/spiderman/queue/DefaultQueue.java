package net.kernal.spiderman.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

import net.kernal.spiderman.logger.Logger;
import net.kernal.spiderman.logger.Loggers;

/**
 * 默认的任务队列实现
 * @author 赖伟威 l.weiwei@163.com 2015-12-10
 * @param <E>
 *
 */
public class DefaultQueue<E> implements Queue<E> {
	
	private final static Logger logger = Loggers.getLogger(DefaultQueue.class);
	private BlockingQueue<E> queue;
	
	public DefaultQueue(int capacity) {
		if (capacity <= 0) {
			queue = new LinkedTransferQueue<E>();
			logger.debug(getClass().getName()+" 使用无边界LinkedEransferQueue");
		} else {
			queue = new ArrayBlockingQueue<E>(capacity);
			logger.debug(getClass().getName()+" 使用有边界ArrayBlockingQueue");
		}
	}
	
	public E take() {
		try {
			return this.queue.take();
		} catch (InterruptedException e) {
		}	
		return null;
	}
	
	public void append(E e) {
		try {
			this.queue.put(e);
		} catch (InterruptedException ex) {
		}
	}

	public void clear() {
		logger.debug(getClass().getName()+" 队列元素剩余数:"+queue.size());
		queue.clear();
	}

	public void removeKeys(String group) {
		
	}

	@Deprecated
	public void append(byte[] data) {
		// reject
	}

}