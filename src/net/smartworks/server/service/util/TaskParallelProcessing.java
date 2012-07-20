package net.smartworks.server.service.util;

import net.smartworks.model.community.User;
import net.smartworks.model.instance.info.TaskInstanceInfo;
import net.smartworks.server.engine.worklist.model.TaskWork;
import net.smartworks.util.Semaphore;

public class TaskParallelProcessing extends ParallelProcessing {
	private User currentUser;
	private TaskWork task;

	public TaskParallelProcessing(Semaphore semaphore, Thread currentThread, User currentUser, TaskWork task){
		super(semaphore, currentThread);
		this.currentUser = currentUser;
		this.task = task;
	}

	@Override
	public void doRun() throws Exception {

		TaskInstanceInfo taskInstanceInfo = null;
		taskInstanceInfo = ModelConverter.getTaskInstanceInfo(currentUser, task);		
		setResult(taskInstanceInfo);
	}
	
}
