package net.kernal.spiderman.worker.result;

import net.kernal.spiderman.worker.Task;
import net.kernal.spiderman.worker.extract.ExtractResult;
import net.kernal.spiderman.worker.extract.ExtractTask;

public class ResultTask extends Task {

	private static final long serialVersionUID = -7531379852428467887L;
	
	private ExtractResult result;
	
	public ResultTask(ExtractTask task, ExtractResult result) {
		super(task.getSeed(), task.getSource(), task.getGroup(), task.getRequest());
		this.result = result;
	}
	
	public ExtractResult getResult() {
		return result;
	}
	
	public String getKey() {
		final String keyValue = result.getFields().getString(result.getKeyFieldName());
		return "result_"+result.getPageName()+"#"+result.getModelName()+"#"+keyValue+"#"+getSeed().getUrl()+"#"+getRequest().getUrl();
	}

}
