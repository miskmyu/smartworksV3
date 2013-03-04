package net.smartworks.model.work.info;

import net.smartworks.model.BaseObject;
import net.smartworks.model.work.FileCategory;
import net.smartworks.model.work.ImageCategory;
import net.smartworks.model.work.SmartWork;
import net.smartworks.model.work.SocialWork;
import net.smartworks.model.work.Work;
import net.smartworks.model.work.WorkCategory;
import net.smartworks.service.ISmartWorks;
import net.smartworks.util.SmartUtil;

public class WorkInfo extends BaseObject {
	
	private int type;
	private String desc;
	private int providedBy = Work.PROVIDED_BY_USER;
	
	public int getProvidedBy() {
		return providedBy;
	}
	public void setProvidedBy(int providedBy) {
		this.providedBy = providedBy;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
//	public String getIconClass(){
//		switch(getType()){
//		case SmartWork.TYPE_INFORMATION:
//		case SocialWork.TYPE_BOARD:
//		case SocialWork.TYPE_EVENT:
//		case SocialWork.TYPE_FILE:
//		case SocialWork.TYPE_IMAGE:
//		case SocialWork.TYPE_MEMO:
//		case SocialWork.TYPE_YTVIDEO:
//			if(getId().equals(SmartWork.ID_FILE_MANAGEMENT))
//				return Work.ICON_CLASS_FILE_WORKS;
//			else if(getId().equals(SmartWork.ID_EVENT_MANAGEMENT))
//				return Work.ICON_CLASS_EVENT_WORKS;
//			else if(getId().equals(SmartWork.ID_BOARD_MANAGEMENT))
//				return Work.ICON_CLASS_BOARD_WORKS;
//			else if(getId().equals(SmartWork.ID_MEMO_MANAGEMENT))
//				return Work.ICON_CLASS_MEMO_WORKS;
//			else if(getId().equals(SmartWork.ID_FORUM_MANAGEMENT))
//				return Work.ICON_CLASS_FORUM_WORKS;
//			else if(getId().equals(SmartWork.ID_CONTACTS_MANAGEMENT))
//				return Work.ICON_CLASS_CONTACTS_WORKS;
//			else if(getId().equals(SmartWork.ID_USER_MANAGEMENT))
//				return Work.ICON_CLASS_USER_WORKS;
//			else if(getId().equals(SmartWork.ID_DEPARTMENT_MANAGEMENT))
//				return Work.ICON_CLASS_DEPARTMENT_WORKS;
//			else if(getId().equals(SmartWork.ID_GROUP_MANAGEMENT))
//				return Work.ICON_CLASS_GROUP_WORKS;
//			else
//				return ((SmartWorkInfo)this).isRunning() ? Work.ICON_CLASS_IWORKS_ON : Work.ICON_CLASS_IWORKS_OFF;
//		case SmartWork.TYPE_PROCESS:
//			return ((SmartWorkInfo)this).isRunning() ? Work.ICON_CLASS_PWORKS_ON : Work.ICON_CLASS_PWORKS_OFF;
//		case SmartWork.TYPE_SCHEDULE:
//			return ((SmartWorkInfo)this).isRunning() ? Work.ICON_CLASS_SWORKS_ON : Work.ICON_CLASS_SWORKS_OFF;
//		case WorkCategory.TYPE_CATEGORY:
//			if(getId().equals(WorkCategory.ID_DEFAULT_CATEGORY))
//				return Work.ICON_CLASS_DEFAULT_CATEGORY;
//			else if(getId().equals(WorkCategory.ID_DOWNLOADED_CATEGORY))
//				return Work.ICON_CLASS_DOWNLOADED_CATEGORY;
//			return ((WorkCategoryInfo)this).isRunning() ? Work.ICON_CLASS_CWORKS_ON : Work.ICON_CLASS_CWORKS_OFF;
//		case FileCategory.TYPE_FILE_CATEGORY:
//			return "";
//		case ImageCategory.TYPE_IMAGE_CATEGORY:
//			return "";
//		}
//		return "";
//	}
	
	public String getIconClass(){
		if(this.getClass().equals(AppWorkInfo.class))
			return Work.getIconClass(getId(), ((AppWorkInfo)this).getWorkType(), true);
		else if(this.getClass().equals(WorkCategoryInfo.class))
			return Work.getIconClass(getId(), getType(), ((WorkCategoryInfo)this).isRunning());
		else if(this.getClass().equals(SmartWorkInfo.class))
			return Work.getIconClass(getId(), getType(), ((SmartWorkInfo)this).isRunning());
		return Work.getIconClass(getId(), getType(), false);
	}
//	public String getController(){
//		switch(getType()){
//		case SmartWork.TYPE_INFORMATION:
//		case SocialWork.TYPE_BOARD:
//		case SocialWork.TYPE_EVENT:
//		case SocialWork.TYPE_FILE:
//		case SocialWork.TYPE_IMAGE:
//		case SocialWork.TYPE_MEMO:
//		case SocialWork.TYPE_YTVIDEO:
//			/*if(getId().equals(SmartWork.ID_FILE_MANAGEMENT))
//				return Work.CONTROLLER_FILE_LIST;
//			else */if(getId().equals(SmartWork.ID_EVENT_MANAGEMENT))
//				return Work.CONTROLLER_EVENT_LIST;
//			else
//				return Work.CONTROLLER_IWORK_LIST;
//		case SmartWork.TYPE_PROCESS:
//			return Work.CONTROLLER_PWORK_LIST;
//		case SmartWork.TYPE_SCHEDULE:
//			return Work.CONTROLLER_SWORK_LIST;
//		case WorkCategory.TYPE_CATEGORY:
//			return "";
//		}
//		return "";
//	}
	public String getController(){
		return WorkInfo.getController(this.getId(), this.getType());
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
			/*if(getId().equals(SmartWork.ID_FILE_MANAGEMENT))
				return Work.CONTROLLER_FILE_LIST;
			else */if(workId.equals(SmartWork.ID_EVENT_MANAGEMENT))
				return Work.CONTROLLER_EVENT_LIST;
			else
				return Work.CONTROLLER_IWORK_LIST;
		case SmartWork.TYPE_PROCESS:
			return Work.CONTROLLER_PWORK_LIST;
		case SmartWork.TYPE_SCHEDULE:
			return Work.CONTROLLER_SWORK_LIST;
		case WorkCategory.TYPE_CATEGORY:
			return "";
		}
		return "";
	}

//	public String getController(String wid){
//		if(SmartUtil.isBlankObject(wid)) return getController();
//		switch(getType()){
//		case SmartWork.TYPE_INFORMATION:
//		case SocialWork.TYPE_BOARD:
//		case SocialWork.TYPE_EVENT:
//		case SocialWork.TYPE_FILE:
//		case SocialWork.TYPE_IMAGE:
//		case SocialWork.TYPE_MEMO:
//		case SocialWork.TYPE_YTVIDEO:
//			/*if(getId().equals(SmartWork.ID_FILE_MANAGEMENT))
//				return Work.CONTROLLER_FILE_LIST;
//			else*/ if(getId().equals(SmartWork.ID_EVENT_MANAGEMENT))
//				return Work.CONTROLLER_EVENT_LIST;
//			else if(getId().equals(SmartWork.ID_MEMO_MANAGEMENT))
//				return Work.CONTROLLER_MEMO_LIST;
//			else if(getId().equals(SmartWork.ID_BOARD_MANAGEMENT))
//				return Work.CONTROLLER_BOARD_LIST;
//			else
//				return Work.CONTROLLER_WORK_LIST;
//		case SmartWork.TYPE_PROCESS:
//		case SmartWork.TYPE_SCHEDULE:
//			return Work.CONTROLLER_WORK_LIST;
//		}
//		return getController();
//	}
	
	public String getController(String wid){
		return WorkInfo.getController(wid,  getId(), getType());
	}
	
	public static String getController(String wid, String workId, int workType){
		if(SmartUtil.isBlankObject(wid)) return WorkInfo.getController(workId, workType);
		switch(workType){
		case SmartWork.TYPE_INFORMATION:
		case SocialWork.TYPE_BOARD:
		case SocialWork.TYPE_EVENT:
		case SocialWork.TYPE_FILE:
		case SocialWork.TYPE_IMAGE:
		case SocialWork.TYPE_MEMO:
		case SocialWork.TYPE_YTVIDEO:
			/*if(getId().equals(SmartWork.ID_FILE_MANAGEMENT))
				return Work.CONTROLLER_FILE_LIST;
			else*/ if(workId.equals(SmartWork.ID_EVENT_MANAGEMENT))
				return Work.CONTROLLER_EVENT_LIST;
			else if(workId.equals(SmartWork.ID_MEMO_MANAGEMENT))
				return Work.CONTROLLER_MEMO_LIST;
			else if(workId.equals(SmartWork.ID_BOARD_MANAGEMENT))
				return Work.CONTROLLER_BOARD_LIST;
			else
				return Work.CONTROLLER_WORK_LIST;
		case SmartWork.TYPE_PROCESS:
		case SmartWork.TYPE_SCHEDULE:
			return Work.CONTROLLER_WORK_LIST;
		}
		return WorkInfo.getController(workId, workType);
	}
		
//	public String getContextId(){
//		switch(getType()){
//		case SmartWork.TYPE_INFORMATION:
//		case SocialWork.TYPE_BOARD:
//		case SocialWork.TYPE_EVENT:
//		case SocialWork.TYPE_FILE:
//		case SocialWork.TYPE_IMAGE:
//		case SocialWork.TYPE_MEMO:
//		case SocialWork.TYPE_YTVIDEO:
//			/*if(getId().equals(SmartWork.ID_FILE_MANAGEMENT))
//				return ISmartWorks.CONTEXT_PREFIX_FILE_LIST + getId();
//			else */if(getId().equals(SmartWork.ID_EVENT_MANAGEMENT))
//				return ISmartWorks.CONTEXT_PREFIX_EVENT_LIST + getId();
//			else if(getId().equals(SmartWork.ID_BOARD_MANAGEMENT))
//				return ISmartWorks.CONTEXT_PREFIX_BOARD_LIST + getId();
//			else if(getId().equals(SmartWork.ID_MEMO_MANAGEMENT))
//				return ISmartWorks.CONTEXT_PREFIX_MEMO_LIST + getId();
//			else if(getId().equals(SmartWork.ID_FORUM_MANAGEMENT))
//				return ISmartWorks.CONTEXT_PREFIX_FORUM_LIST + getId();
//			else if(getId().equals(SmartWork.ID_CONTACTS_MANAGEMENT))
//				return ISmartWorks.CONTEXT_PREFIX_CONTACTS_LIST + getId();
//			else if(getId().equals(SmartWork.ID_USER_MANAGEMENT))
//				return ISmartWorks.CONTEXT_PREFIX_USER_LIST + getId();
//			else if(getId().equals(SmartWork.ID_DEPARTMENT_MANAGEMENT))
//				return ISmartWorks.CONTEXT_PREFIX_DEPARTMENT_LIST + getId();
//			else if(getId().equals(SmartWork.ID_GROUP_MANAGEMENT))
//				return ISmartWorks.CONTEXT_PREFIX_GROUP_LIST + getId();
//			else
//				return ISmartWorks.CONTEXT_PREFIX_IWORK_LIST + getId();
//		case SmartWork.TYPE_PROCESS:
//			return ISmartWorks.CONTEXT_PREFIX_PWORK_LIST + getId();
//		case SmartWork.TYPE_SCHEDULE:
//			return ISmartWorks.CONTEXT_PREFIX_SWORK_LIST + getId();
//		case WorkCategory.TYPE_CATEGORY:
//			return "";
//		}
//		return "";
//	}
	
	public String getContextId(){
		return WorkInfo.getContextId(getId(), getType());
	}
	
	public static String getContextId(String workId, int workType){
		if(SmartUtil.isBlankObject(workId)) return "";
		switch(workType){
		case SmartWork.TYPE_INFORMATION:
		case SocialWork.TYPE_BOARD:
		case SocialWork.TYPE_EVENT:
		case SocialWork.TYPE_FILE:
		case SocialWork.TYPE_IMAGE:
		case SocialWork.TYPE_MEMO:
		case SocialWork.TYPE_YTVIDEO:
			/*if(getId().equals(SmartWork.ID_FILE_MANAGEMENT))
				return ISmartWorks.CONTEXT_PREFIX_FILE_LIST + getId();
			else */if(workId.equals(SmartWork.ID_EVENT_MANAGEMENT))
				return ISmartWorks.CONTEXT_PREFIX_EVENT_LIST + workId;
			else if(workId.equals(SmartWork.ID_BOARD_MANAGEMENT))
				return ISmartWorks.CONTEXT_PREFIX_BOARD_LIST + workId;
			else if(workId.equals(SmartWork.ID_MEMO_MANAGEMENT))
				return ISmartWorks.CONTEXT_PREFIX_MEMO_LIST + workId;
			else if(workId.equals(SmartWork.ID_FORUM_MANAGEMENT))
				return ISmartWorks.CONTEXT_PREFIX_FORUM_LIST + workId;
			else if(workId.equals(SmartWork.ID_CONTACTS_MANAGEMENT))
				return ISmartWorks.CONTEXT_PREFIX_CONTACTS_LIST + workId;
			else if(workId.equals(SmartWork.ID_USER_MANAGEMENT))
				return ISmartWorks.CONTEXT_PREFIX_USER_LIST + workId;
			else if(workId.equals(SmartWork.ID_DEPARTMENT_MANAGEMENT))
				return ISmartWorks.CONTEXT_PREFIX_DEPARTMENT_LIST + workId;
			else if(workId.equals(SmartWork.ID_GROUP_MANAGEMENT))
				return ISmartWorks.CONTEXT_PREFIX_GROUP_LIST + workId;
			else
				return ISmartWorks.CONTEXT_PREFIX_IWORK_LIST + workId;
		case SmartWork.TYPE_PROCESS:
			return ISmartWorks.CONTEXT_PREFIX_PWORK_LIST + workId;
		case SmartWork.TYPE_SCHEDULE:
			return ISmartWorks.CONTEXT_PREFIX_SWORK_LIST + workId;
		case WorkCategory.TYPE_CATEGORY:
			return "";
		}
		return "";
	}
	
	public WorkInfo(){
		super();
	}

	public WorkInfo(String id, String name, int type){
		super(id, name);
		this.type = type;
	}
	
	public Work getWork(){
		Work work = new Work();
		work.setDesc(this.getDesc());
		work.setId(this.getId());
		work.setName(this.getName());
		work.setProvidedBy(this.getProvidedBy());
		work.setType(this.getType());
		return work;
	}
}
