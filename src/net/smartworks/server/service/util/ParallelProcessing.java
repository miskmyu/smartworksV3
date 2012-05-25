package net.smartworks.server.service.util;

import org.springframework.stereotype.Component;

import net.smartworks.util.Semaphore;

@Component
public class ParallelProcessing extends Thread implements IParallelProcessing{

	Thread currentThread = null;
	Semaphore semaphore = null;
	Object result = null;
	Object[] arrayResult = null;

	public ParallelProcessing() {
	}

	public ParallelProcessing(Semaphore semaphore, Thread currentThread){
		this.semaphore = semaphore;
		this.currentThread = currentThread;
	}

	public void run(){
		try{

			doRun();
			if(semaphore.getSemaphore() == 0){
				synchronized (currentThread){
					currentThread.notifyAll();
				}	
			}
		}catch(Exception e){
			e.printStackTrace();
			try {
				if(semaphore.getSemaphore() == 0){
					synchronized (currentThread){
						currentThread.notifyAll();
					}	
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}				
	}

	@Override
	public Object getResult() {
		return this.result;
	}

	@Override
	public Object[] getArrayResult() {
		return this.arrayResult;
	}

	@Override
	public void doRun() throws Exception {
		// TODO Auto-generated method stub		
	}

	@Override
	public void setResult(Object result) {
		this.result = result;
	}

	@Override
	public void setArrayResult(Object[] arrayResult) {
		this.arrayResult = arrayResult;
	}
}
