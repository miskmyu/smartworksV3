package net.smartworks.model.community;

import net.smartworks.model.community.info.InstanceSpaceInfo;
import net.smartworks.model.work.Work;


public class InstanceSpace extends WorkSpace {

	private Work work;
	
	public Work getWork() {
		return work;
	}
	public void setWork(Work work) {
		this.work = work;
	}
	public InstanceSpace(){
		super();
	}
	public InstanceSpace(String id, String name){
		super(id, name);
	}
	public InstanceSpaceInfo getInstanceSpaceInfo(){
		InstanceSpaceInfo instanceSpaceInfo = new InstanceSpaceInfo(getId(), getName());
		return instanceSpaceInfo;
	}
}
