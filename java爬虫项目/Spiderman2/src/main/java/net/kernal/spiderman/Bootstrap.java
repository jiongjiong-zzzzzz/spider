package net.kernal.spiderman;

import net.kernal.spiderman.kit.Context;
import net.kernal.spiderman.kit.Properties;
import net.kernal.spiderman.kit.XMLConfBuilder;

/**
 * 启动类
 *
 * @author 赖伟威 l.weiwei@163.com 2016-01-20
 */
public class Bootstrap {

    /**
     * 以XML文件方式来构建配置对象，这样的好处是可以将那些不需要代码编写的配置规则放到XML去，减少代码处理。
     */
    public static void main(String[] args) {
        final Properties params = Properties.from(args);// 将参数里的 -k1 v1 -k2 v2 转成 map
        final String xml = params.getString("-conf", "tianyancha.xml");// 获得XML配置文件路径
        final Config conf = new XMLConfBuilder(xml).build();// 通过XMLBuilder构建CONF对象
        new Spiderman(conf).go();//启动，别忘记看控制台信息哦，结束之后会有统计信息的

    }

    public static class Bindings implements Config.ScriptBindings {
        public void config(javax.script.Bindings bindings, Context ctx) {
            bindings.put("$ctx", ctx);// 上下文
            bindings.put("$conf", ctx.getConf());// 配置对象
            bindings.put("$seeds", ctx.getConf().getSeeds());// 种子列表
        }
    }

}
