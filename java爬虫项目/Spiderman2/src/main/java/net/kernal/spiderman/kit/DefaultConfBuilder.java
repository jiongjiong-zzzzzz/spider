package net.kernal.spiderman.kit;

import net.kernal.spiderman.Config;
import net.kernal.spiderman.Spiderman;
import net.kernal.spiderman.worker.extract.schema.Page;
import net.kernal.spiderman.worker.result.handler.ResultHandler;

/**
 * 默认的配置构建器
 *
 * @author 赖伟威 l.weiwei@163.com 2015-12-01
 */
public abstract class DefaultConfBuilder implements Config.Builder {

    protected Config conf;

    public DefaultConfBuilder() {
        conf = new Config();
    }

    /**
     * 留给客户端程序去添加参数
     *
     * @param params
     */
    public abstract void configParams(Properties params);

    /**
     * 留给客户端程序去添加种子
     *
     * @param seeds
     */
    public abstract void configSeeds(Config.Seeds seeds);

    /**
     * 留给客户端程序去添加需要抽取的页面
     *
     * @param pages
     */
    public abstract void configPages(Config.Pages pages);

    public DefaultConfBuilder setResultHandler(ResultHandler resultHandler) {
        conf.setResultHandler(resultHandler);
        return this;
    }

    public DefaultConfBuilder setScript(String script) {
        conf.setScript(script);
        return this;
    }

    /**
     * 构建Spiderman.Conf对象
     */
    public Config build() {
        this.configParams(conf.getParams());
        this.configSeeds(conf.getSeeds());
        this.configPages(conf.getPages());
        for (Page page : conf.getPages().all()) {
            if (page.getExtractorBuilder() == null) {
                throw new Spiderman.Exception("页面[name=" + page.getName() + "]缺少可以构建抽取器的对象，请设置一个 models.setExtractorBuilder");
            }
        }

        return conf;
    }

}
