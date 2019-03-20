package net.kernal.spiderman.worker.result;

import net.kernal.spiderman.worker.Task;
import net.kernal.spiderman.worker.Worker;
import net.kernal.spiderman.worker.WorkerManager;
import net.kernal.spiderman.worker.WorkerResult;

public class ResultWorker extends Worker {

	public ResultWorker(WorkerManager manager) {
		super(manager);
	}

	public void work(Task task) {
		getManager().done(new WorkerResult(null, (ResultTask)task, null));
	}

}
