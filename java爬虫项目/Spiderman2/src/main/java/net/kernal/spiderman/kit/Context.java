package net.kernal.spiderman.kit;

import net.kernal.spiderman.Config;
import net.kernal.spiderman.Config.Seeds;
import net.kernal.spiderman.Spiderman;
import net.kernal.spiderman.logger.Logger;
import net.kernal.spiderman.logger.Loggers;
import net.kernal.spiderman.worker.TaskManager;
import net.kernal.spiderman.worker.WorkerManager;
import net.kernal.spiderman.worker.download.DownloadManager;
import net.kernal.spiderman.worker.download.Downloader;
import net.kernal.spiderman.worker.extract.ExtractManager;
import net.kernal.spiderman.worker.extract.schema.Page;
import net.kernal.spiderman.worker.extract.schema.filter.ScriptableFilter;
import net.kernal.spiderman.worker.result.ResultManager;
import net.kernal.spiderman.worker.result.handler.ResultHandler;

import javax.script.*;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Context {
	
	private final static Logger logger = Loggers.getLogger(Context.class);
	
	private Config conf;
	private TaskManager taskManager;
	private List<WorkerManager> workerManagers;
	private ScriptEngine scriptEngine;
	private Downloader downloader;
	
	public Context(Config conf) {
		final List<Page> pages = conf.getPages().all();
		final Properties params = conf.getParams();
		if (pages.isEmpty()) 
			throw new Spiderman.Exception("少年,请添加一个页面来让蜘蛛侠行动起来!参考：conf.addPage");
		final String engineName = params.getString("scriptEngine", "nashorn");
		scriptEngine = new ScriptEngineManager().getEngineByName(engineName);
		if (scriptEngine == null) 
			throw new Spiderman.Exception("无法获取脚本引擎对象[name="+engineName+"]");
		this.conf = conf;
		// process script
		this.processScript();
		
		// build manager
		this.workerManagers = new ArrayList<>();
		// 构建任务管理器
		taskManager = new TaskManager(conf);
		final int limitOfResult = params.getInt("worker.result.limit", 0);
		
		// 构建下载管理器
		final String downloaderClassName = params.getString("worker.download.class", "net.kernal.spiderman.worker.download.impl.HttpClientDownloader");
		final Class<Downloader> downloaderClass = K.loadClass(downloaderClassName);
		try {
			final Constructor<Downloader> ct = downloaderClass.getConstructor(Properties.class);
			if (ct != null) {
				downloader = ct.newInstance(params);
			} else {
				downloader = downloaderClass.newInstance();
			}
			// 设置Header给Downloader
			conf.getHeaders().all().parallelStream().forEach(h -> downloader.keepHeader(h));
			// 设置Cookie给Downloader
			conf.getCookies().all().parallelStream().forEach(c -> downloader.keepCookie(c));
			
			// 处理Download监听器
			final Downloader.Listener downloadListener;
			final String downloadListenerClassName = params.getString("worker.download.listener");
			if (K.isNotBlank(downloadListenerClassName)) {
				final Class<Downloader.Listener> downloadListenerClass = K.loadClass(downloadListenerClassName);
				try {
					downloadListener = downloadListenerClass.newInstance();
				} catch (Throwable e) {
					throw new Spiderman.Exception("下载监听器[worker.download.listener="+downloadListenerClassName+"]实例化失败", e);
				}
			} else {
				downloadListener = conf.getDownloadListener();
			}
			downloader.setListener(downloadListener);
			if (downloader.getListener() != null) {
				// 通知监听器初始化
				// 另外开启一个线程去做这个事情，等待监听器完成它的工作后才继续
				Thread t = new Thread(()->downloader.getListener().init(Context.this));
				t.start();
				t.join();
			}
		} catch (Throwable e) {
			throw new Spiderman.Exception("下载器[worker.download.class="+downloaderClassName+"]实例化失败", e);
		}
		final boolean enabled1 = params.getBoolean("worker.download.enabled", true);
		if (enabled1) {
			final int limit = params.getInt("worker.download.result.limit", limitOfResult);
			final Counter counter = new Counter(limit, 0);
			final int size = params.getInt("worker.download.size", 1);
			final long delay = K.convertToMillis(params.getString("worker.download.delay", "0")).longValue();
			final DownloadManager downloadManager = new DownloadManager(size, taskManager, counter, downloader, delay);
			logger.debug("构建下载管理器");
			this.addWorkerManager(downloadManager);
		}
		
		// 构建解析管理器
		final boolean enabled2 = params.getBoolean("worker.extract.enabled", true);
		if (enabled2) {
			final int limit = params.getInt("worker.extract.result.limit", limitOfResult);
			final Counter counter = new Counter(limit, 0);
			final int size = params.getInt("worker.extract.size", 1);
			final ExtractManager extractManager = new ExtractManager(size, taskManager, counter, pages, downloader);
			logger.debug("构建解析管理器");
			this.addWorkerManager(extractManager);
		}
		
		// 构建结果处理管理器
		final boolean enabled3 = params.getBoolean("worker.result.enabled", true);
		if (enabled3) {
			ResultHandler handler = conf.getResultHandler();
			if (handler == null) {
				final String resultHandlerClassName = params.getString("worker.result.handler");
				if (K.isNotBlank(resultHandlerClassName)) {
					Class<ResultHandler> resultHandlerClass = K.loadClass(resultHandlerClassName);
					if (resultHandlerClass == null) {
						throw new Spiderman.Exception("ResultHandler[class="+resultHandlerClassName+"]不存在");
					}
					try {
						handler = resultHandlerClass.newInstance();
					} catch (InstantiationException | IllegalAccessException e) {
						throw new Spiderman.Exception("实例化ResultHandler[class="+resultHandlerClassName+"]失败", e);
					}
				}
			}
			
			if (handler != null) {
				handler.init(this);
			}
			final Counter counter = new Counter(limitOfResult, 0);
			final int size = params.getInt("worker.result.size", 1);
			final ResultManager resultManager = new ResultManager(size, taskManager, counter, handler);
			logger.debug("构建结果管理器");
			this.addWorkerManager(resultManager);
		}
	}
	
	public Collection<WorkerManager> getManagers() {
		return Collections.unmodifiableCollection(this.workerManagers);
	}
	
	public Context addWorkerManager(WorkerManager manager) {
		this.workerManagers.add(manager);
		return this;
	}
	
	public Config getConf() {
		return this.conf;
	}
	
	public Properties getParams() {
		return this.conf.getParams();
	}
	
	public Seeds getSeeds() {
		return this.conf.getSeeds();
	}
	
	public TaskManager getTaskManager() {
		return this.taskManager;
	}
	
	public void shutdown() {
		this.taskManager.shutdown();
		logger.debug("退出...");
	}
	
	private void processScript() {
		// 若配置了script脚本，执行它
		final Bindings bindings = new SimpleBindings();
		final Config.ScriptBindings sb = conf.getScriptBindings();
		final String script = conf.getScript();
		if (K.isNotBlank(script)) {
			try {
				sb.config(bindings, this);
				scriptEngine.eval(script, bindings);
			} catch (ScriptException e) {
				throw new Spiderman.Exception("执行脚本错误", e);
			}
		}
		// 设置脚本引擎
		conf.getPages().all().forEach(page -> {
			page.getModels().all().forEach(p -> {
				p.getFields().forEach(f -> {
					f.getFilters().stream()
						.filter(ft -> ft instanceof ScriptableFilter)
						.map(ft -> (ScriptableFilter)ft)
						.forEach(ft -> {
							ft.setBindings(bindings);
							ft.setScriptEngine(scriptEngine);
						});
				});
			});
		});
	}
	
	public Downloader getDownloader() {
		return this.downloader;
	}

}
