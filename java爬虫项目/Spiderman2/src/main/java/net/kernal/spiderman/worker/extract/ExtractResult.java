package net.kernal.spiderman.worker.extract;

import java.io.Serializable;

import net.kernal.spiderman.kit.Properties;

public class ExtractResult implements Serializable {
	
	private static final long serialVersionUID = 2390695820923166121L;
	
	/**
	 * 所属页面名称
	 */
	private String pageName;
	/**
	 * 模型顺序
	 */
	private int modelIdx;
	/**
	 * 结果所属模型名称
	 */
	private String modelName;
	/**
	 * 模型中的主键名
	 */
	private String keyFieldName;
	/**
	 * 字段值
	 */
	private Properties fields;
	/**
	 * 响应内容体
	 */
	private String responseBody;
	
	public ExtractResult(String pageName, String responseBody, String modelName, String keyFieldName, Properties fields) {
		this.pageName = pageName;
		this.responseBody = responseBody;
		this.modelName = modelName;
		this.keyFieldName = keyFieldName;
		this.fields = fields;
	}

	public String getPageName() {
		return this.pageName;
	}
	
	public String getResponseBody() {
		return this.responseBody;
	}
	
	public String getModelName() {
		return this.modelName;
	}
	
	public String getKeyFieldName() {
		return this.keyFieldName;
	}
	
	public Properties getFields() {
		return this.fields;
	}
	
	public void setModelIdx(int idx) {
		this.modelIdx = idx;
	}
	public int getModelIdx() {
		return this.modelIdx;
	}
	
	@Override
	public String toString() {
		return "ExtractResult [page=" + pageName + ", model=" + modelName + ", keyFieldName=" + keyFieldName + ", fields=" + fields + "]";
	}
	
}
