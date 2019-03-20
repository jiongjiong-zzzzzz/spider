package net.kernal.spiderman.worker;

import net.kernal.spiderman.Config;
import net.kernal.spiderman.Spiderman;
import net.kernal.spiderman.kit.K;
import net.kernal.spiderman.kit.Properties;
import net.kernal.spiderman.logger.Logger;
import net.kernal.spiderman.logger.Loggers;
import net.kernal.spiderman.queue.*;
import net.kernal.spiderman.queue.Queue;
import net.kernal.spiderman.queue.Queue.Element;
import net.kernal.spiderman.worker.download.DownloadTask;
import net.kernal.spiderman.worker.extract.ExtractTask;
import net.kernal.spiderman.worker.extract.schema.Page;
import net.kernal.spiderman.worker.result.ResultTask;
import org.zbus.broker.Broker;
import org.zbus.broker.BrokerConfig;
import org.zbus.broker.ZbusBroker;
import org.zbus.mq.server.MqServerConfig;

import java.io.IOException;
import java.util.*;

/**
 * 任务管理器，负责任务的存取，此外还可以获取队列对象
 * @author 赖伟威 l.weiwei@163.com 2016-03-30
 *
 */
public class TaskManager {
	
	private final static Logger logger = Loggers.getLogger(TaskManager.class);
	
	private Map<String, Queue<Element>> queues;
	private Queue<Task> downloadQueue;
	private Queue<Task> extractQueue;
	private Queue<Task> resultQueue;
	/**
	 * ZBus broker
	 */
	private Broker broker;
	
	public TaskManager(Config conf) {
		final Properties params = conf.getParams();
		final List<Page> pages = conf.getPages().all();
		
		this.queues = new HashMap<>();
		
		// 队列构建器
		final Queue.Builder queueBuilder;
		final String storePath = params.getString("queue.store.path", "store");
		final boolean zbusEnabled = params.getBoolean("queue.zbus.enabled", false);
		if (zbusEnabled) {
			// 使用ZBus队列
			final String brokerAddr = params.getString("queue.zbus.broker");//jvm|ip:port|[ip:port]
			if (K.isBlank(brokerAddr)) {
				throw new Spiderman.Exception("缺少参数: queue.zbus.broker, 参考: conf.set(\"queue.zbus.broker\", \"127.0.0.1:155555\")");
			}
			final BrokerConfig brokerConfig = new BrokerConfig();
			brokerConfig.setBrokerAddress(brokerAddr);
			if (brokerAddr.equals("jvm")) {// 当使用本地VM模式的时候，需要用到bdb存储
				final MqServerConfig mqCfg = new MqServerConfig();
				mqCfg.setMqFilter("persist");
				mqCfg.setStorePath(storePath);
				brokerConfig.setMqServerConfig(mqCfg);
			}
		    try {
		    	broker = new ZbusBroker(brokerConfig);
		    } catch (Throwable e) {
		    	throw new Spiderman.Exception("连接ZBus服务失败", e);
		    }
		    queueBuilder = new Queue.Builder() {
				public <T extends Element> Queue<T> build(String queueName, Properties params) {
				    return new ZBusQueue<T>(broker, queueName);
				}
			};
		} else {
			// 使用默认队列
			final List<String> groups = new ArrayList<String>();
			groups.add("seeds");
		    pages.parallelStream()
		    	.filter(p -> K.isNotBlank(p.getName()))
		    	.forEach(p -> groups.add(p.getName()));
			final RepeatableChecker checker = new RepeatableChecker(groups, storePath);
			final int capacity = params.getInt("queue.capacity", 5000);
			queueBuilder = new Queue.Builder() {
				public <T extends Element> Queue<T> build(String queueName, Properties params) {
				    return new CheckableQueue<T>(new DefaultQueue<T>(capacity), checker);
				}
			};
		}
				
		// 构建队列
		final String downloadQueueName = params.getString("queue.download.name", "SPIDERMAN_DOWNLOAD_TASK");
		this.downloadQueue = queueBuilder.build(downloadQueueName, params);
		logger.debug("创建下载队列(默认)"); 
		final String extractQueueName = params.getString("queue.extract.name", "SPIDERMAN_EXTRACT_TASK");
		this.extractQueue = queueBuilder.build(extractQueueName, params);
		logger.debug("创建解析队列(默认)");
		final String resultQueueName = params.getString("queue.result.name", "SPIDERMAN_RESULT_TASK");
		this.resultQueue = queueBuilder.build(resultQueueName, params);
		logger.debug("创建结果队列(默认)");
		// 创建其他队列
		final List<String> queueNames = params.getListString("queue.other.names", "", ",");
		new HashSet<String>(queueNames).parallelStream().filter(n -> K.isNotBlank(n)).forEach(n -> {
			Queue<Element> queue = queueBuilder.build(n, params);
			queues.put(n,queue);
			logger.debug("创建其他[name="+n+"]队列(默认)");
		});
	}
	
	public void append(Task task) {
		final String source = task.getSource() == null ? null : task.getSource().getRequest().getUrl();
		if (task instanceof DownloadTask) {
			this.downloadQueue.append(task);
		} else if (task instanceof ExtractTask) {
			this.extractQueue.append(task);
		} else if (task instanceof ResultTask) {
			this.resultQueue.append(task);
		}
		logger.info("添加任务: "+ task.getKey()+", 来源->"+source);
	}
	
	public Queue<Task> getDownloadQueue() {
		return this.downloadQueue;
	}
	
	public Queue<Task> getExtractQueue() {
		return this.extractQueue;
	}
	
	public Queue<Task> getResultQueue() {
		return this.resultQueue;
	}
	
	public Queue<Element> getQueue(String name) {
		return this.queues.get(name);
	}
	
	public void register(String name, Queue<Element> queue) {
		if (this.queues.containsKey(name)) {
			throw new Spiderman.Exception("duplicate name " + name);
		}
		
		this.queues.put(name, queue);
	}
	
	public void removeKeys(String group) {
		this.downloadQueue.removeKeys(group);
		this.extractQueue.removeKeys(group);
		this.resultQueue.removeKeys(group);
		this.queues.values().parallelStream().forEach(q -> q.removeKeys(group));
	}
	
	public void shutdown() {
		if (this.downloadQueue != null) {
			this.downloadQueue.clear();
		}
		if (this.extractQueue != null) {
			this.extractQueue.clear();
		}
		if (this.resultQueue != null) {
			this.resultQueue.clear();
		}
		if (this.queues != null && !this.queues.isEmpty()) {
			this.queues.forEach((k, q) -> q.clear());
		}
		
		if (broker != null) {
			try {
				this.broker.close();
			} catch (IOException e) {
				logger.error("Failed to close ZBusBroker", e);
			}
		}
		
		logger.debug("退出...");
	}
	
}
