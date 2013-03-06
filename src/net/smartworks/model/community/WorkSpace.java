package net.smartworks.model.community;

import net.smartworks.model.community.info.InstanceSpaceInfo;
import net.smartworks.model.instance.WorkInstance;
import net.smartworks.model.work.SmartWork;
import net.smartworks.model.work.Work;
import net.smartworks.model.work.info.SmartWorkInfo;
import net.smartworks.model.work.info.WorkInfo;
import net.smartworks.service.ISmartWorks;
import net.smartworks.util.SmartUtil;

public class WorkSpace extends Community {
	
	private int spaceType;
	
	private WorkInfo instanceWork;
	private String instanceWorkSpaceId;

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
	public WorkSpace(){
		super();
	}
	public WorkSpace(String id, String name){
		super(id, name);
	}
	
	public String getIconClass(){
		if(this.getClass().equals(WorkSpace.class)){
			switch(this.getSpaceType()){
				case ISmartWorks.SPACE_TYPE_GROUP:
					return Community.ICON_CLASS_GROUP;
				case ISmartWorks.SPACE_TYPE_DEPARTMENT:
					return Community.ICON_CLASS_DEPARTMENT;
			}
		}

		if(spaceType != ISmartWorks.SPACE_TYPE_WORK_INSTANCE || SmartUtil.isBlankObject(instanceWork)) return super.getIconClass();
		return Work.getIconClass(instanceWork.getId(), instanceWork.getType(), instanceWork.isRunning());
	}

	public String getSpaceController(){
		switch(this.getSpaceType()){
		case ISmartWorks.SPACE_TYPE_USER:
			return Community.CONTROLLER_USER_SPACE;
		case ISmartWorks.SPACE_TYPE_DEPARTMENT:
			return Community.CONTROLLER_DEPARTMENT_SPACE;
		case ISmartWorks.SPACE_TYPE_GROUP:
			return Community.CONTROLLER_GROUP_SPACE;
		case ISmartWorks.SPACE_TYPE_WORK_INSTANCE:
			if(this.getInstanceWork()!=null && this.getInstanceWork().getWork()!=null){
				switch(this.getInstanceWork().getWork().getType()){
				case SmartWork.TYPE_INFORMATION:
					return Community.CONTROLLER_IWORK_SPACE;
				case SmartWork.TYPE_PROCESS:
					return Community.CONTROLLER_PWORK_SPACE;
				case SmartWork.TYPE_SCHEDULE:
					return Community.CONTROLLER_SWORK_SPACE;
				}
			}
		}
		return "";
	}
	public String getSpaceContextId(){
		switch(this.getSpaceType()){
		case ISmartWorks.SPACE_TYPE_USER:
			return ISmartWorks.CONTEXT_PREFIX_USER_SPACE + getId();
		case ISmartWorks.SPACE_TYPE_DEPARTMENT:
			return ISmartWorks.CONTEXT_PREFIX_DEPARTMENT_SPACE + getId();
		case ISmartWorks.SPACE_TYPE_GROUP:
			return ISmartWorks.CONTEXT_PREFIX_GROUP_SPACE + getId();
		case ISmartWorks.SPACE_TYPE_WORK_INSTANCE:
			if(this.getInstanceWork()!=null && this.getInstanceWork().getWork()!=null){
				switch(this.getInstanceWork().getWork().getType()){
				case SmartWork.TYPE_INFORMATION:
					return ISmartWorks.CONTEXT_PREFIX_IWORK_SPACE + getId();
				case SmartWork.TYPE_PROCESS:
					return ISmartWorks.CONTEXT_PREFIX_PWORK_SPACE + getId();
				case SmartWork.TYPE_SCHEDULE:
					return ISmartWorks.CONTEXT_PREFIX_SWORK_SPACE + getId();
				}
			}
		}
		return "";
	}
}
