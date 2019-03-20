package net.kernal.spiderman.kit;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 计数器
 *
 * @author 赖伟威 l.weiwei@163.com 2016-01-16
 */
public class Counter {

    /**
     * 计数
     */
    private AtomicLong count;
    /**
     * 计数最大限制
     */
    private int limit;
    /**
     * 停止信号计数器
     */
    private CountDownLatch countDown;
    /**
     * 等待超时时间,配合停止信号计数器
     */
    private long timeout;
    /**
     * 计数开始时间
     */
    private long start;
    /**
     * 计数结束时间
     */
    private long end;

    public Counter(int limit, long timeout) {
        this.limit = limit;
        this.countDown = new CountDownLatch(limit > 0 ? limit : 1);
        this.count = new AtomicLong();
        this.timeout = timeout;
        this.start = System.currentTimeMillis();
    }

    /**
     * 计数器加1
     */
    public long plus() {
        if (this.limit > 0) {
            this.countDown.countDown();
        }
        return this.count.addAndGet(1);
    }

    /**
     * 供外界主动调用
     */
    public void stop() {
        final int c = limit > 0 ? limit : 1;
        for (int i = 0; i < c; i++) {
            this.countDown.countDown();
        }
        this.end = System.currentTimeMillis();
    }

    public void await() {
        try {
            if (timeout > 0) {
                this.countDown.await(timeout, TimeUnit.MILLISECONDS);
            } else {
                this.countDown.await();
            }
        } catch (InterruptedException e) {
            //ignore
        }
        this.end = System.currentTimeMillis();
    }

    public int getLimit() {
        return this.limit;
    }

    public boolean isTimeout() {
        return this.getCost() >= this.timeout;
    }

    public long get() {
        return this.count.get();
    }

    public long getCost() {
        final long cost = this.end - this.start;
        return cost;
    }

    public String toString() {
        if (limit > 0) {
            return get() + "/" + limit;
        }
        return get() + "";
    }

}
