package net.kernal.spiderman.worker.download;

import net.kernal.spiderman.Spiderman;
import net.kernal.spiderman.kit.Counter;
import net.kernal.spiderman.logger.Logger;
import net.kernal.spiderman.logger.Loggers;
import net.kernal.spiderman.worker.*;
import net.kernal.spiderman.worker.extract.ExtractTask;

/**
 * 下载工人驱动器
 */
public class DownloadManager extends WorkerManager {

    private final static Logger logger = Loggers.getLogger(DownloadManager.class);
    private Downloader downloader;
    private long delay;

    public DownloadManager(int nWorkers, TaskManager queueManager, Counter counter, Downloader downloader, long delay) {
        super(nWorkers, queueManager, counter);
        this.downloader = downloader;
        this.delay = delay;
    }

    /**
     * 从队列里获取任务
     */
    @Override
    protected Task takeTask() throws InterruptedException {
        return getQueueManager().getDownloadQueue().take();
    }

    /**
     * 构建工人对象
     */
    @Override
    protected Worker buildWorker() {
        return new DownloadWorker(this, downloader, delay);
    }

    /**
     * 处理结果
     */
    @Override
    protected void handleResult(WorkerResult wr) {
        final Object result = wr.getResult();
        final Task task = wr.getTask();
        if (!(result instanceof Downloader.Response)) {
            throw new Spiderman.Exception("只接受Downloader.Response类型的结果");
        }
        // 计数器加1
        long count = getCounter().plus();
        Downloader.Response response = (Downloader.Response) result;
        // 放入解析队列
        getQueueManager().append(new ExtractTask((DownloadTask) task, response));
        logger.info("下载了第" + count + "个网页: " + response.getRequest().getUrl());
    }

    @Override
    protected void clear() {
        this.downloader.close();
    }

}
