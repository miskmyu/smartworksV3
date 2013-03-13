package net.smartworks.model.instance.info;

import net.smartworks.model.community.WorkSpace;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.WorkInstance;
import net.smartworks.model.work.SmartWork;
import net.smartworks.model.work.SocialWork;
import net.smartworks.model.work.Work;
import net.smartworks.model.work.WorkCategory;
import net.smartworks.service.ISmartWorks;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartMessage;
import net.smartworks.util.SmartUtil;

public class WorkInstanceInfo extends InstanceInfo {

	private TaskInstanceInfo lastTask;
	private int lastTaskCount = -1;
	private int subInstanceCount;
	private InstanceInfo[] subInstances;
	private String[] likers;

	public String[] getLikers() {
		return likers;
	}
	public void setLikers(String[] likers) {
		this.likers = likers;
	}
	public TaskInstanceInfo getLastTask() {
		return lastTask;
	}
	public void setLastTask(TaskInstanceInfo lastTask) {
		this.lastTask = lastTask;
	}
	public int getLastTaskCount() {
		return lastTaskCount;
	}
	public void setLastTaskCount(int lastTaskCount) {
		this.lastTaskCount = lastTaskCount;
	}
	public int getSubInstanceCount() {
		return subInstanceCount;
	}
	public void setSubInstanceCount(int subInstanceCount) {
		this.subInstanceCount = subInstanceCount;
	}
	public InstanceInfo[] getSubInstances() {
		return subInstances;
	}
	public void setSubInstances(InstanceInfo[] subInstances) {
		this.subInstances = subInstances;
	}
//	public String getController(){
//		if(getWork()==null) return "";
//		switch(getWork().getType()){
//		case SmartWork.TYPE_INFORMATION:
//		case SocialWork.TYPE_BOARD:
//		case SocialWork.TYPE_EVENT:
//		case SocialWork.TYPE_FILE:
//		case SocialWork.TYPE_IMAGE:
//		case SocialWork.TYPE_MEMO:
//		case SocialWork.TYPE_YTVIDEO:
//			if(SmartWork.ID_FILE_MANAGEMENT.equals(getWork().getId()))
//				return WorkInstance.CONTROLLER_IWORK_SPACE;
//			else
//				return WorkInstance.CONTROLLER_IWORK_SPACE;
//		case SmartWork.TYPE_PROCESS:
//			return WorkInstance.CONTROLLER_PWORK_SPACE;
//		case SmartWork.TYPE_SCHEDULE:
//			return WorkInstance.CONTROLLER_SWORK_SPACE;
//		case WorkCategory.TYPE_CATEGORY:
//			return "";
//		}
//		return "";
//	}
	public String getController(){
		if(SmartUtil.isBlankObject(getWorkId())) return "";
		return WorkInstanceInfo.getController(getWorkId(), getWorkType());
	}
	
	public static String getController(String workId, int workType){
		if(SmartUtil.isBlankObject(workId)) return "";
		switch(workType){
		case SmartWork.TYPE_INFORMATION:
		case SocialWork.TYPE_BOARD:
		case SocialWork.TYPE_EVENT:
		case SocialWork.TYPE_FILE:
		case SocialWork.TYPE_IMAGE:
		case SocialWork.TYPE_MEMO:
		case SocialWork.TYPE_YTVIDEO:
			if(SmartWork.ID_FILE_MANAGEMENT.equals(workId))
				return WorkInstance.CONTROLLER_IWORK_SPACE;
			else
				return WorkInstance.CONTROLLER_IWORK_SPACE;
		case SmartWork.TYPE_PROCESS:
			return WorkInstance.CONTROLLER_PWORK_SPACE;
		case SmartWork.TYPE_SCHEDULE:
			return WorkInstance.CONTROLLER_SWORK_SPACE;
		case WorkCategory.TYPE_CATEGORY:
			return "";
		}
		return "";
		
	}
	
//	public String getContextId(){
//		if(getWork()==null) return "";
//		switch(getWork().getType()){
//		case SmartWork.TYPE_INFORMATION:
//		case SocialWork.TYPE_BOARD:
//		case SocialWork.TYPE_EVENT:
//		case SocialWork.TYPE_FILE:
//		case SocialWork.TYPE_IMAGE:
//		case SocialWork.TYPE_MEMO:
//		case SocialWork.TYPE_YTVIDEO:
//			if(SmartWork.ID_FILE_MANAGEMENT.equals(getWork().getId()))
//				return ISmartWorks.CONTEXT_PREFIX_IWORK_SPACE + getId();
//			else if(SmartWork.ID_DEPARTMENT_MANAGEMENT.equals(getWork().getId()))
//				return ISmartWorks.CONTEXT_PREFIX_DEPARTMENT_SPACE + getId();
//			else if(SmartWork.ID_GROUP_MANAGEMENT.equals(getWork().getId()))
//				return ISmartWorks.CONTEXT_PREFIX_GROUP_SPACE + getId();
//			else
//				return ISmartWorks.CONTEXT_PREFIX_IWORK_SPACE + getId();
//		case SmartWork.TYPE_PROCESS:
//			return ISmartWorks.CONTEXT_PREFIX_PWORK_SPACE + getId();
//		case SmartWork.TYPE_SCHEDULE:
//			return ISmartWorks.CONTEXT_PREFIX_SWORK_SPACE + getId();
//		case WorkCategory.TYPE_CATEGORY:
//			return "";
//		}
//		return "";
//	}
	public String getContextId(){
		if(SmartUtil.isBlankObject(getWorkId())) return "";
		return WorkInstanceInfo.getContextId(getWorkId(), getWorkType(), getId());
	}

	public static String getContextId(String workId, int workType, String workInstanceId){
		if(SmartUtil.isBlankObject(workId) || SmartUtil.isBlankObject(workInstanceId)) return "";
		switch(workType){
		case SmartWork.TYPE_INFORMATION:
		case SocialWork.TYPE_BOARD:
		case SocialWork.TYPE_EVENT:
		case SocialWork.TYPE_FILE:
		case SocialWork.TYPE_IMAGE:
		case SocialWork.TYPE_MEMO:
		case SocialWork.TYPE_YTVIDEO:
			if(SmartWork.ID_FILE_MANAGEMENT.equals(workId))
				return ISmartWorks.CONTEXT_PREFIX_IWORK_SPACE + workInstanceId;
			else if(SmartWork.ID_DEPARTMENT_MANAGEMENT.equals(workId))
				return ISmartWorks.CONTEXT_PREFIX_DEPARTMENT_SPACE + workInstanceId;
			else if(SmartWork.ID_GROUP_MANAGEMENT.equals(workId))
				return ISmartWorks.CONTEXT_PREFIX_GROUP_SPACE + workInstanceId;
			else
				return ISmartWorks.CONTEXT_PREFIX_IWORK_SPACE + workInstanceId;
		case SmartWork.TYPE_PROCESS:
			return ISmartWorks.CONTEXT_PREFIX_PWORK_SPACE + workInstanceId;
		case SmartWork.TYPE_SCHEDULE:
			return ISmartWorks.CONTEXT_PREFIX_SWORK_SPACE + workInstanceId;
		case WorkCategory.TYPE_CATEGORY:
			return "";
		}
		return "";
	}

	public WorkInstanceInfo(){
		super();
		super.setType(Instance.TYPE_WORK);
	}	
	public WorkInstanceInfo(String id, String subject, UserInfo owner, UserInfo lastModifier, LocalDate lastModifiedDate) {
		super(id, subject, Instance.TYPE_WORK, owner, lastModifier, lastModifiedDate);
	}
	
	public boolean doesCurrentUserLike(){
		if(SmartUtil.isBlankObject(this.likers)) return false;
		String cUserId = SmartUtil.getCurrentUser().getId();
		for(int i=0; i<likers.length; i++)
			if(cUserId.equals(likers[i]))
				return true;
		return false;
	}
	
//	public WorkInstance getInstance(){
//		WorkInstance workInstance = new WorkInstance();
//		workInstance.setCreatedDate(this.getCreatedDate());
//		workInstance.setId(this.getId());
//		workInstance.setLastModifiedDate(this.getLastModifiedDate());
//		workInstance.setLastModifier((this.getLastModifier()!=null) ? this.getLastModifier().getUser():null);
//		workInstance.setOwner((this.getOwner()!=null) ? this.getOwner().getUser() : null);
//		workInstance.setStatus(this.getStatus());
//		workInstance.setSubject(this.getSubject());
//		workInstance.setType(this.getType());
//		workInstance.setWork((this.getWork()!=null) ? this.getWork().getWork():null);
//		workInstance.setWorkSpace((this.getWorkSpace()!=null) ? this.getWorkSpace().getWorkSpace() : null);
//		return workInstance;
//	}	
	public WorkInstance getInstance(){
		WorkInstance workInstance = new WorkInstance();
		workInstance.setCreatedDate(this.getCreatedDate());
		workInstance.setId(this.getId());
		workInstance.setLastModifiedDate(this.getLastModifiedDate());
		workInstance.setLastModifier((this.getLastModifier()!=null) ? this.getLastModifier().getUserModel():null);
		workInstance.setOwner((this.getOwner()!=null) ? this.getOwner().getUserModel() : null);
		workInstance.setStatus(this.getStatus());
		workInstance.setSubject(this.getSubject());
		workInstance.setType(this.getType());
		Work work = SmartUtil.isBlankObject(getWorkId()) ? null : new Work(getWorkId(), getWorkName(), getWorkType(), "");
		workInstance.setWork(work);
		WorkSpace workSpace = SmartUtil.isBlankObject(getWorkSpaceId()) ? null : new WorkSpace(getWorkSpaceId(), getWorkSpaceName());
		workInstance.setWorkSpace(workSpace);
		return workInstance;
	}
	
}
