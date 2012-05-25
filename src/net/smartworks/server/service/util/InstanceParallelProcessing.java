package net.smartworks.server.service.util;

import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.info.InstanceInfo;
import net.smartworks.util.Semaphore;

public class InstanceParallelProcessing extends Thread{
	
	Thread currentThread;
	Semaphore semaphore;
	int instanceType;
	InstanceInfo[] instances;
	
	public InstanceInfo[] getInstances(){
		return instances;
	}
	
	public InstanceParallelProcessing(Semaphore semaphore, Thread currentThread, int instanceType){
		this.semaphore = semaphore;
		this.currentThread = currentThread;
		this.instanceType = instanceType;
	}

	public void run(){
		try{

			switch(instanceType){
			case Instance.TYPE_BOARD:
				break;
			case Instance.TYPE_EVENT:
				break;
			case Instance.TYPE_SERA_NOTE:
				break;
			case Instance.TYPE_SERA_MISSION_REPORT:
				break;
			}
			if(semaphore.getSemaphore() == 0){
				synchronized (currentThread){
					currentThread.notify();
				}	
			}
		}catch(Exception e){
			e.getStackTrace();
		}				
	}
}
