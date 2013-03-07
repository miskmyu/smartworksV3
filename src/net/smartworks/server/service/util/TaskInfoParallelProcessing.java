package net.smartworks.server.service.util;

import java.util.Date;

import net.smartworks.model.community.User;
import net.smartworks.model.instance.info.TaskInstanceInfo;
import net.smartworks.server.engine.worklist.model.TaskWork;
import net.smartworks.server.service.impl.InstanceServiceImpl;
import net.smartworks.util.Semaphore;

public class TaskInfoParallelProcessing extends ParallelProcessing {
	private User currentUser;
	private String contextId;
	private String spaceId;
	private Date fromDate;
	private Date toDate;
	private int maxSize;
	
	public TaskInfoParallelProcessing(Semaphore semaphore, Thread currentThread, String contextId, String spaceId, User currentUser, Date fromDate, Date toDate, int maxSize){
		super(semaphore, currentThread);
		this.currentUser = currentUser;
		this.contextId = contextId;
		this.spaceId = spaceId;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.maxSize = maxSize;
	}

	@Override
	public void doRun() throws Exception {

		TaskInstanceInfo[] taskInstanceInfos = null;
		taskInstanceInfos = ModelConverter.getTaskInstanceInfoArrayByTaskWorkArray(currentUser.getId(), InstanceServiceImpl.getTaskWorkByFromToDate(contextId, spaceId, currentUser, fromDate, toDate, maxSize), maxSize);		
		setResult(taskInstanceInfos);
	}
	
}
