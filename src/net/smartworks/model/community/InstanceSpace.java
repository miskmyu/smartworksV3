package net.smartworks.model.community;

import net.smartworks.model.community.info.InstanceSpaceInfo;
import net.smartworks.model.instance.Instance;

public class InstanceSpace extends WorkSpace {

	private Instance instance;

	public Instance getInstance() {
		return instance;
	}
	public void setInstance(Instance instance) {
		this.instance = instance;
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
