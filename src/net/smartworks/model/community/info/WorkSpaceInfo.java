package net.smartworks.model.community.info;

import net.smartworks.model.community.WorkSpace;

public class WorkSpaceInfo extends CommunityInfo {

	public WorkSpaceInfo(){
		super();
	}
	public WorkSpaceInfo(String id, String name){
		super(id, name);
	}

	public WorkSpace getWorkSpace(){
		WorkSpace workSpace = new WorkSpace();
		workSpace.setBigPictureName(this.getBigPictureName());
		workSpace.setId(this.getId());
		workSpace.setName(this.getName());
		workSpace.setSmallPictureName(this.getSmallPictureName());
		return workSpace;
	}
}
