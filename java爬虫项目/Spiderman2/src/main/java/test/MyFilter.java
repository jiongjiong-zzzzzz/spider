package test;

import net.kernal.spiderman.kit.K;
import net.kernal.spiderman.worker.extract.schema.Field;

/**
 * 字段值过滤器
 * @author 赖伟威(l.weiwei@163.com) 2016-05-13 下午15:19
 *
 */
public class MyFilter implements Field.ValueFilter {

	public String filter(Context ctx) {
		final String v = ctx.getValue();//获取字段当前值
		final String pn = K.findOneByRegex(v, "&pn\\=\\d+");//找出里面的分页页码
		if ("&pn=0".equals(pn) || K.isBlank(pn))
			return null;
		
		return ctx.getSeed().getUrl()+pn;//构建新的值
	}

}
