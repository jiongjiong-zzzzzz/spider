package net.kernal.spiderman.worker.result;

import net.kernal.spiderman.kit.Counter;
import net.kernal.spiderman.logger.Logger;
import net.kernal.spiderman.logger.Loggers;
import net.kernal.spiderman.worker.*;
import net.kernal.spiderman.worker.extract.ExtractResult;
import net.kernal.spiderman.worker.result.handler.ResultHandler;

/**
 * 结果管理器
 */
public class ResultManager extends WorkerManager {

    private Logger logger = Loggers.getLogger(ResultManager.class);
    private ResultHandler handler;

    public ResultManager(int nWorkers, TaskManager queueManager, Counter counter, ResultHandler handler) {
        super(nWorkers, queueManager, counter);
        this.handler = handler;
    }

    @Override
    protected void handleResult(WorkerResult wr) {
        // 计数器加1
        final Counter counter = getCounter();
        final long count = counter.plus();
        final ResultTask rtask = (ResultTask) wr.getTask();
        final ExtractResult result = rtask.getResult();
        logger.info("消费了第" + count + "个结果[seed=" + rtask.getSeed().getName() + ",page=" + result.getPageName() + ", model=" + result.getModelName() + ", url=" + rtask.getRequest().getUrl() + ", source=" + rtask.getSourceUrl() + "]");
        if (this.handler != null) {
            this.handler.handle(rtask, counter);
        }
    }

    @Override
    protected Task takeTask() throws InterruptedException {
        return getQueueManager().getResultQueue().take();
    }

    @Override
    protected Worker buildWorker() {
        return new ResultWorker(this);
    }

    @Override
    protected void clear() {
    }

}
