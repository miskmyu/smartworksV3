package net.smartworks.server.service.util;

import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.info.InstanceInfo;
import net.smartworks.util.Semaphore;

public class InstanceParallelProcessing extends ParallelProcessing{
	private int instanceType;
	
	public int getInstanceType() {
		return instanceType;
	}
	public void setInstanceType(int instanceType) {
		this.instanceType = instanceType;
	}

	public InstanceParallelProcessing(Semaphore semaphore, Thread currentThread, int instanceType){
		super(semaphore, currentThread);
		this.instanceType = instanceType;
	}

	@Override
	public void doRun() throws Exception {
		
		InstanceInfo[] instances = null;
		switch(this.instanceType){
		case Instance.TYPE_EVENT:			
			break;
		case Instance.TYPE_BOARD:
			break;
		case Instance.TYPE_SERA_NOTE:
			break;
		case Instance.TYPE_SERA_MISSION_REPORT:
			break;
		}		
		setArrayResult(instances);
	}
	
}
