package com.wb.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Arrays;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.kevin.crop.R.string;

public class JavaExecScriptDemo {

	public static void main(String[] args) {
		/*
		 * ScriptEngineManager m = new ScriptEngineManager(); // 获取JavaScript执行引擎
		 * ScriptEngine engine = m.getEngineByName("JavaScript"); // 执行JavaScript代码 try
		 * { engine.
		 * eval("function getAdd(a,b){return a+b;} print('我爱你中国！' + getAdd(1,2));"); }
		 * catch (ScriptException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		String l = TestRegex.Millstotime(System.currentTimeMillis());
		try {
			RunJavaScript(l);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(jsObjFunc());
		// System.out.println(Arrays.toString(getArray()));
		// System.out.println(jsCalculate("a=1+2+3+(2*2)"));
		// jsFunction();
		// jsVariables();
	}

	/**
	 * 执行md5.js文件中的MD5函数
	 * 
	 * @param code
	 *            原始字符串
	 * @return 进行加密操作后的字符串
	 */
	public static String Special(String code) throws Exception {
		ScriptEngineManager manager = new ScriptEngineManager();
		String newCode = "";
		InputStreamReader inputStreamReader = null;
		ScriptEngine engine = manager.getEngineByName("javascript");
		try {
			URL url = new URL("https://s3a.pstatp.com/toutiao/resource/ntoutiao_web/page/home/whome/home_e14d2a0.js");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Content-Type", "text/html");
			inputStreamReader = getInputContent("GET", null, conn);
		} catch (ConnectException ce) {
			ce.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		engine.eval(inputStreamReader);
		if (engine instanceof Invocable) {
			Invocable invoke = (Invocable) engine;
			newCode = (String) invoke.invokeFunction("sign", code);// 调用md5方法，并传入1个参数

		}
		inputStreamReader.close();
		return newCode;

	}

	/**
	 *
	 * 通过流获取返回内容
	 */
	private static InputStreamReader getInputContent(String requestMethod, String outputStr, HttpURLConnection conn)
			throws ProtocolException, IOException, UnsupportedEncodingException { // （封装的http请求方法） 需要调用的方法
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		// 设置请求方式（GET/POST）
		conn.setRequestMethod(requestMethod);
		// 当outputStr不为null时向输出流写数据
		if (null != outputStr) {
			OutputStream outputStream = conn.getOutputStream();
			// 注意编码格式
			outputStream.write(outputStr.getBytes("UTF-8"));
			outputStream.close();
		}
		// 从输入流读取返回内容
		InputStream inputStream = conn.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
		return inputStreamReader;
	}

	/**
	 * 运行JS对象中的函数
	 * 
	 * @return
	 */
	public static Object jsObjFunc() {
		String script = "var obj={run:function(){return 'run method : return:\"abc'+this.next('test')+'\"';},next:function(str){return ' 我来至next function '+str+')'}}";
		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine scriptEngine = sem.getEngineByName("js");
		try {
			scriptEngine.eval(script);
			Object object = scriptEngine.get("obj");
			Invocable inv2 = (Invocable) scriptEngine;
			return (String) inv2.invokeMethod(object, "run");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取js对象数字类型属性
	 * 
	 * @return
	 */
	public static Object[] getArray() {
		ScriptEngineManager sem = new ScriptEngineManager();
		String script = "var obj={array:['test',true,1,1.0,2.11111]}";

		ScriptEngine scriptEngine = sem.getEngineByName("js");
		try {
			scriptEngine.eval(script);
			Object object2 = scriptEngine.eval("obj.array");
			Class clazz = object2.getClass();
			Field denseField = clazz.getDeclaredField("dense");
			denseField.setAccessible(true);
			return (Object[]) denseField.get(object2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * JS计算
	 * 
	 * @param script
	 * @return
	 */
	public static Object jsCalculate(String script) {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("javascript");
		try {
			return (Object) engine.eval(script);
		} catch (ScriptException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 运行JS基本函数
	 */
	public static void jsFunction() {
		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine se = sem.getEngineByName("javascript");
		try {
			String script = "function say(name){ return 'hello,'+name; }";
			se.eval(script);
			Invocable inv2 = (Invocable) se;
			String res = (String) inv2.invokeFunction("say", "test");
			System.out.println(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * JS中变量使用
	 */
	public static void jsVariables() {
		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine engine = sem.getEngineByName("javascript");
		File file = new File("/data/js.txt");
		engine.put("file", file);
		try {
			engine.eval("println('path:'+file.getAbsoluteFile())");
		} catch (ScriptException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 執行js文件中代碼
	 * 
	 * @return 返回一個規定串
	 */
	public static String RunJavaScript(String code) {
		ScriptEngineManager m = new ScriptEngineManager();
		// 获取JavaScript执行引擎
		ScriptEngine engine = m.getEngineByName("JavaScript");
		// 使用管道流，将输出流转为输入流
		PipedReader prd = new PipedReader();
		PipedWriter pwt = null;
		try {
			pwt = new PipedWriter(prd);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// 设置执行结果内容的输出流
		engine.getContext().setWriter(pwt);
		// js文件的路径
		String strFile = "src/main/resources/asd.js";
		Reader reader = null;
		BufferedReader br = null;
		try {
			reader = new FileReader(new File(strFile));
			engine.eval(reader);
			Invocable invocable = (Invocable) engine;
			String result = (String) invocable.invokeFunction("r", new Object[] { code,"",""});
			br = new BufferedReader(prd);
			String str = null;
			while ((str = br.readLine()) != null && str.length() > 0) {
				System.out.println(str);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
				pwt.close();
				prd.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 开始读执行结果数据
		return null;
	}
}
