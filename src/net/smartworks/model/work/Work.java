package net.smartworks.model.work;

import net.smartworks.model.BaseObject;
import net.smartworks.model.KeyMap;
import net.smartworks.model.work.info.SmartWorkInfo;
import net.smartworks.model.work.info.WorkCategoryInfo;
import net.smartworks.model.work.info.WorkInfo;
import net.smartworks.service.ISmartWorks;
import net.smartworks.util.SmartConfUtil;
import net.smartworks.util.SmartMessage;
import net.smartworks.util.SmartUtil;

public class Work extends BaseObject{

	public static final String PICTURE_PATH = SmartConfUtil.getInstance().getImageServer();
	public static final String WORKDEF_IMG_DIR = "workDef";
	
	public final static int TYPE_NONE = -1;
	public final static int	TYPE_ASYNC_MESSAGE	= 1;
	public final static int	TYPE_NOTIFICAITON	= 2;
	public final static int	TYPE_COMMENTS		= 3;
	public final static int	TYPE_MAIL_MESSAGE	= 4;
	
	public final static int TYPE_SERA_MISSION = 101;
	public final static int TYPE_SERA_MISSION_REPORT = 102;
	public final static int TYPE_SERA_REVIEW = 103;
	public final static int TYPE_SERA_NOTE = 104;

	public static final int PROVIDED_BY_USER = 0;
	public static final int PROVIDED_BY_APPSTORE = 1;
	public static final int PROVIDED_BY_SYSTEM = 2;

	public static final int SEARCH_TYPE_LIST_WORK = 1;
	public static final int SEARCH_TYPE_START_WORK = 2;

	public final static String ICON_CLASS_IWORKS_ON = "icon_iworks";
	public final static String ICON_CLASS_PWORKS_ON = "icon_pworks";
	public final static String ICON_CLASS_SWORKS_ON = "icon_sworks";
	public final static String ICON_CLASS_CWORKS_ON = "icon_cworks";
	public final static String ICON_CLASS_DEFAULT_CATEGORY = "icon_dworks";
	public final static String ICON_CLASS_DOWNLOADED_CATEGORY = "icon_aworks";
	public final static String ICON_CLASS_IWORKS_OFF = "icon_iworks_off";
	public final static String ICON_CLASS_PWORKS_OFF = "icon_pworks_off";
	public final static String ICON_CLASS_SWORKS_OFF = "icon_sworks_off";
	public final static String ICON_CLASS_CWORKS_OFF = "icon_cworks_off";
	public final static String ICON_CLASS_ALL_WORKS = "icon_all_works";
	public final static String ICON_CLASS_FILE_WORKS = "icon_file_works";
	public final static String ICON_CLASS_IMAGE_WORKS = "icon_image_works";
	public final static String ICON_CLASS_EVENT_WORKS = "icon_event_works";
	public final static String ICON_CLASS_MEMO_WORKS = "icon_memo_works";
	public final static String ICON_CLASS_BOARD_WORKS = "icon_board_works";
	public final static String ICON_CLASS_FORUM_WORKS = "icon_forum_works";
	public final static String ICON_CLASS_CONTACTS_WORKS = "icon_contacts_works";
	public final static String ICON_CLASS_USER_WORKS = "icon_user_works";
	public final static String ICON_CLASS_DEPARTMENT_WORKS = "icon_depart_works";
	public final static String ICON_CLASS_GROUP_WORKS = "icon_group_works";

	public static final String CONTROLLER_IWORK_LIST = "iwork_list.sw";
	public static final String CONTROLLER_PWORK_LIST = "pwork_list.sw";
	public static final String CONTROLLER_SWORK_LIST = "swork_list.sw";
	public static final String CONTROLLER_EVENT_LIST = "event_list.sw";
	public static final String CONTROLLER_FILE_LIST = "file_list.sw";
	public static final String CONTROLLER_WORK_LIST = "work_list.sw";
	public static final String CONTROLLER_IMAGE_LIST = "image_list.sw";
	public static final String CONTROLLER_BOARD_LIST = "board_list.sw";
	public static final String CONTROLLER_MEMO_LIST = "memo_list.sw";
	
	public static final String ID_EMPTY_WORK = "EmptyWorkId";
	public static final String ID_GROUP_WORK = "GroupWorkId";
	public static final String ID_DEPARTMENT_WORK = "DepartmentWorkId";
	public static final String ID_USER_WORK = "UserWorkId";

	private int 	type=-1; 
	private int 	providedBy;
	private String 	desc;
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getProvidedBy() {
		return providedBy;
	}
	public void setProvidedBy(int providedBy) {
		this.providedBy = providedBy;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getIconClass(){
		if(this.getClass().equals(WorkCategory.class))
			return Work.getIconClass(getId(), getType(), ((WorkCategory)this).isRunning());
		else if(this.getClass().equals(InformationWork.class) || this.getClass().equals(ProcessWork.class) || this.getClass().equals(ScheduleWork.class))
			return Work.getIconClass(getId(), getType(), ((SmartWork)this).isRunning());
		return Work.getIconClass(getId(), getType(), false);
	}

	public static String getIconClass(String workId, int workType, boolean isWorkRunning){
		if(SmartUtil.isBlankObject(workId)) return "";
		switch(workType){
		case SmartWork.TYPE_INFORMATION:
		case SocialWork.TYPE_BOARD:
		case SocialWork.TYPE_EVENT:
		case SocialWork.TYPE_FILE:
		case SocialWork.TYPE_IMAGE:
		case SocialWork.TYPE_MEMO:
		case SocialWork.TYPE_YTVIDEO:
			if(workId.equals(SmartWork.ID_FILE_MANAGEMENT))
				return Work.ICON_CLASS_FILE_WORKS;
			else if(workId.equals(SmartWork.ID_EVENT_MANAGEMENT))
				return Work.ICON_CLASS_EVENT_WORKS;
			else if(workId.equals(SmartWork.ID_BOARD_MANAGEMENT))
				return Work.ICON_CLASS_BOARD_WORKS;
			else if(workId.equals(SmartWork.ID_MEMO_MANAGEMENT))
				return Work.ICON_CLASS_MEMO_WORKS;
			else if(workId.equals(SmartWork.ID_FORUM_MANAGEMENT))
				return Work.ICON_CLASS_FORUM_WORKS;
			else if(workId.equals(SmartWork.ID_CONTACTS_MANAGEMENT))
				return Work.ICON_CLASS_CONTACTS_WORKS;
			else if(workId.equals(SmartWork.ID_USER_MANAGEMENT))
				return Work.ICON_CLASS_USER_WORKS;
			else if(workId.equals(SmartWork.ID_DEPARTMENT_MANAGEMENT))
				return Work.ICON_CLASS_DEPARTMENT_WORKS;
			else if(workId.equals(SmartWork.ID_GROUP_MANAGEMENT))
				return Work.ICON_CLASS_GROUP_WORKS;
			else
				return isWorkRunning ? Work.ICON_CLASS_IWORKS_ON : Work.ICON_CLASS_IWORKS_OFF;
		case SmartWork.TYPE_PROCESS:
			return isWorkRunning ? Work.ICON_CLASS_PWORKS_ON : Work.ICON_CLASS_PWORKS_OFF;
		case SmartWork.TYPE_SCHEDULE:
			return isWorkRunning ? Work.ICON_CLASS_SWORKS_ON : Work.ICON_CLASS_SWORKS_OFF;
		case WorkCategory.TYPE_CATEGORY:
			if(workId.equals(WorkCategory.ID_DEFAULT_CATEGORY))
				return Work.ICON_CLASS_DEFAULT_CATEGORY;
			else if(workId.equals(WorkCategory.ID_DOWNLOADED_CATEGORY))
				return Work.ICON_CLASS_DOWNLOADED_CATEGORY;
			return isWorkRunning ? Work.ICON_CLASS_CWORKS_ON : Work.ICON_CLASS_CWORKS_OFF;
		case FileCategory.TYPE_FILE_CATEGORY:
			return "";
		case ImageCategory.TYPE_IMAGE_CATEGORY:
			return "";
		}
		return "";
	}
	
	public String getController(){
		switch(getType()){
		case SmartWork.TYPE_INFORMATION:
			if(getId().equals(SmartWork.ID_FILE_MANAGEMENT))
				return Work.CONTROLLER_FILE_LIST;
			else if(getId().equals(SmartWork.ID_EVENT_MANAGEMENT))
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
	
	public String getController(String wid){
		if(SmartUtil.isBlankObject(wid)) return getController();
		switch(getType()){
		case SmartWork.TYPE_INFORMATION:
			if(getId().equals(SmartWork.ID_FILE_MANAGEMENT))
				return Work.CONTROLLER_FILE_LIST;
			else if(getId().equals(SmartWork.ID_EVENT_MANAGEMENT))
				return Work.CONTROLLER_EVENT_LIST;
			else if(getId().equals(SmartWork.ID_MEMO_MANAGEMENT))
				return Work.CONTROLLER_MEMO_LIST;
			else if(getId().equals(SmartWork.ID_BOARD_MANAGEMENT))
				return Work.CONTROLLER_BOARD_LIST;
			else
				return Work.CONTROLLER_WORK_LIST;
		case SmartWork.TYPE_PROCESS:
		case SmartWork.TYPE_SCHEDULE:
			return Work.CONTROLLER_WORK_LIST;
		}
		return getController();
	}
	
	public String getContextId(){
		switch(getType()){
		case SmartWork.TYPE_INFORMATION:
		case SocialWork.TYPE_BOARD:
		case SocialWork.TYPE_EVENT:
		case SocialWork.TYPE_FILE:
		case SocialWork.TYPE_IMAGE:
		case SocialWork.TYPE_MEMO:
		case SocialWork.TYPE_YTVIDEO:
			if(getId().equals(SmartWork.ID_FILE_MANAGEMENT))
				return ISmartWorks.CONTEXT_PREFIX_FILE_LIST + getId();
			else if(getId().equals(SmartWork.ID_EVENT_MANAGEMENT))
				return ISmartWorks.CONTEXT_PREFIX_EVENT_LIST + getId();
			else if(getId().equals(SmartWork.ID_BOARD_MANAGEMENT))
				return ISmartWorks.CONTEXT_PREFIX_BOARD_LIST + getId();
			else if(getId().equals(SmartWork.ID_MEMO_MANAGEMENT))
				return ISmartWorks.CONTEXT_PREFIX_MEMO_LIST + getId();
			else if(getId().equals(SmartWork.ID_FORUM_MANAGEMENT))
				return ISmartWorks.CONTEXT_PREFIX_FORUM_LIST + getId();
			else if(getId().equals(SmartWork.ID_CONTACTS_MANAGEMENT))
				return ISmartWorks.CONTEXT_PREFIX_CONTACTS_LIST + getId();
			else if(getId().equals(SmartWork.ID_USER_MANAGEMENT))
				return ISmartWorks.CONTEXT_PREFIX_USER_LIST + getId();
			else if(getId().equals(SmartWork.ID_DEPARTMENT_MANAGEMENT))
				return ISmartWorks.CONTEXT_PREFIX_DEPARTMENT_LIST + getId();
			else if(getId().equals(SmartWork.ID_GROUP_MANAGEMENT))
				return ISmartWorks.CONTEXT_PREFIX_GROUP_LIST + getId();
			else
				return ISmartWorks.CONTEXT_PREFIX_IWORK_LIST + getId();
		case SmartWork.TYPE_PROCESS:
			return ISmartWorks.CONTEXT_PREFIX_PWORK_LIST + getId();
		case SmartWork.TYPE_SCHEDULE:
			return ISmartWorks.CONTEXT_PREFIX_SWORK_LIST + getId();
		case WorkCategory.TYPE_CATEGORY:
			return "";
		}
		return "";
	}
	
	public Work(){
		super();
	}
	public Work(String id, String name){
		super(id, name);
	}	
	public Work(String id, String name, int type, String desc){
		super(id, name);
		this.type = type;
		this.desc = desc;
	}
}
