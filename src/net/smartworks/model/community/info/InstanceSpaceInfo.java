package net.smartworks.model.community.info;

import net.smartworks.model.work.info.WorkInfo;


public class InstanceSpaceInfo extends WorkSpaceInfo {

	private WorkInfo work;
	
	public WorkInfo getWork() {
		return work;
	}
	public void setWork(WorkInfo work) {
		this.work = work;
	}
	public InstanceSpaceInfo(){
		super();
	}
	public InstanceSpaceInfo(String id, String name){
		super(id, name);
	}

}
