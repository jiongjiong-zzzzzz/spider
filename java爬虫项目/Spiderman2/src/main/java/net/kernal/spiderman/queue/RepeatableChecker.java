package net.kernal.spiderman.queue;

import java.io.File;
import java.util.List;

import net.kernal.spiderman.kit.K;
import net.kernal.spiderman.logger.Logger;
import net.kernal.spiderman.logger.Loggers;
import net.kernal.spiderman.queue.Queue.AbstractElement;
import net.kernal.spiderman.queue.Queue.Element;
import net.kernal.spiderman.store.BDbStore;
import net.kernal.spiderman.store.KVStore;

public class RepeatableChecker implements CheckableQueue.Checker {

	private final static Logger logger = Loggers.getLogger(RepeatableChecker.class);
	/** 去重需要用到存储 */
	private KVStore store;
	
	public RepeatableChecker(List<String> groups, String storePath) {
		final File file = new File(storePath);
		file.mkdirs();
		this.store = new BDbStore(file, groups.toArray(new String[]{}));
		logger.debug(RepeatableChecker.class.getName()+" 构建KVStore[names="+groups+", file="+file.getAbsolutePath()+"]存储对象, 使用BDb实现");
	}
	
	public boolean check(Element e) {
		if (e instanceof AbstractElement) {
			// 检查重复
			final AbstractElement ae = ((AbstractElement)e);
			final String group = ae.getGroup();
			final String key = ae.getKey();
			if (K.isBlank(key) || K.isBlank(group)) {
				return true;
			}
			
			if (store.contains(group, key)) {
				// key重复了
				logger.warn("队列元素重复[group="+group+", key="+key+"]");
				return false;
			}
			// 将key存储起来
			this.store.put(group, key, key.getBytes());
		}
		return true;
	}
	
	public void clear() {
		this.store.close();
	}
	
	public void removeKeys(String group) {
		this.store.removeKeys(group);
	}

}