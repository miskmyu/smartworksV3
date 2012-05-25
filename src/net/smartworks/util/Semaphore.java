package net.smartworks.util;

public class Semaphore{
	private int semaphoreCount = 0;
	public synchronized int getSemaphore() throws InterruptedException{
		if(this.semaphoreCount<=0) return 0;
		this.semaphoreCount--;
		return this.semaphoreCount;
	}
	public Semaphore(int semaphoreCount){
		this.semaphoreCount = semaphoreCount;
	}
	
}
