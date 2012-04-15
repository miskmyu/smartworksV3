package net.smartworks.model.community.info;

import net.smartworks.model.instance.info.InstanceInfo;

public class InstanceSpaceInfo extends WorkSpaceInfo {

	private InstanceInfo instance;

	public InstanceInfo getInstance() {
		return instance;
	}
	public void setInstance(InstanceInfo instance) {
		this.instance = instance;
	}
	public InstanceSpaceInfo(){
		super();
	}
	public InstanceSpaceInfo(String id, String name){
		super(id, name);
	}

}
