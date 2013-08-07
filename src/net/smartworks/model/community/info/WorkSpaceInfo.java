package net.smartworks.model.community.info;

import net.smartworks.model.community.Community;
import net.smartworks.model.community.WorkSpace;
import net.smartworks.model.work.SmartWork;
import net.smartworks.model.work.Work;
import net.smartworks.model.work.info.WorkInfo;
import net.smartworks.service.ISmartWorks;
import net.smartworks.util.SmartUtil;

public class WorkSpaceInfo extends CommunityInfo {

	private int spaceType;
	private WorkInfo instanceWork;
	private String instanceWorkSpaceId;

	public String getLongName() {
		return this.getName();
	}
	
	public int getSpaceType() {
		return spaceType;
	}
	public void setSpaceType(int spaceType) {
		this.spaceType = spaceType;
	}
	public WorkInfo getInstanceWork() {
		return instanceWork;
	}
	public void setInstanceWork(WorkInfo instanceWork) {
		this.instanceWork = instanceWork;
	}
	public String getInstanceWorkSpaceId() {
		return instanceWorkSpaceId;
	}
	public void setInstanceWorkSpaceId(String instanceWorkSpaceId) {
		this.instanceWorkSpaceId = instanceWorkSpaceId;
	}
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
		workSpace.setInstanceWork(this.getInstanceWork());
		workSpace.setInstanceWorkSpaceId(this.getInstanceWorkSpaceId());
		workSpace.setSpaceType(this.getSpaceType());
		return workSpace;
	}
	
	public static String getIconClass(int spaceType, WorkInfo instanceWork){
		if(spaceType != ISmartWorks.SPACE_TYPE_WORK_INSTANCE || SmartUtil.isBlankObject(instanceWork)) return CommunityInfo.getIconClass(spaceType);
		return Work.getIconClass(instanceWork.getId(), instanceWork.getType(), instanceWork.isRunning());
	}
	
	public static String getSpaceController(int spaceType, WorkInfo instanceWork){
		if(spaceType != ISmartWorks.SPACE_TYPE_WORK_INSTANCE || SmartUtil.isBlankObject(instanceWork)) return CommunityInfo.getSpaceController(spaceType);
		switch(instanceWork.getType()){
		case SmartWork.TYPE_INFORMATION:
			return Community.CONTROLLER_IWORK_SPACE;
		case SmartWork.TYPE_PROCESS:
			return Community.CONTROLLER_PWORK_SPACE;
		case SmartWork.TYPE_SCHEDULE:
			return Community.CONTROLLER_SWORK_SPACE;
		}
		return "";
	}
	
	public static String getSpaceContextId(int spaceType, String spaceId, WorkInfo instanceWork){
		if(spaceType != ISmartWorks.SPACE_TYPE_WORK_INSTANCE || SmartUtil.isBlankObject(instanceWork)) return CommunityInfo.getSpaceContextId(spaceType, spaceId);
		switch(instanceWork.getType()){
		case SmartWork.TYPE_INFORMATION:
			return ISmartWorks.CONTEXT_PREFIX_IWORK_SPACE + spaceId;
		case SmartWork.TYPE_PROCESS:
			return ISmartWorks.CONTEXT_PREFIX_PWORK_SPACE + spaceId;
		case SmartWork.TYPE_SCHEDULE:
			return ISmartWorks.CONTEXT_PREFIX_SWORK_SPACE + spaceId;
		}
		return "";
	}
}
