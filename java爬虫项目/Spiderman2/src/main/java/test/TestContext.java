package test;

import net.kernal.spiderman.Config;

import javax.script.*;

/**
 * @author 赖伟威 l.weiwei@163.com
 * @author <a href='http://krbit.github.io'>Krbit</a>
 * @version V0.1.0
 */
public class TestContext {
    public static void main(String[] args) throws ScriptException {
        ScriptEngine e = new ScriptEngineManager().getEngineByName("nashorn");
        Bindings bind = new SimpleBindings();
        Config conf = new Config();
        bind.put("$seeds", conf.getSeeds());
        final String script = "var kws = Java.type('net.kernal.spiderman.kit.K').readLine('keywords.txt'); for(var i=0; i<kws.length; i++) { $seeds.add(kws[i]); }";
        Object v = e.eval(script, bind);
        System.out.println("v->" + v);
        System.out.println("seeds->" + conf.getSeeds().all());
    }
}
