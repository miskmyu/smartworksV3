package net.smartworks.model.work;

import net.smartworks.model.BaseObject;
import net.smartworks.model.KeyMap;
import net.smartworks.service.ISmartWorks;
import net.smartworks.util.SmartMessage;
import net.smartworks.util.SmartUtil;

public class Work extends BaseObject{

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

	public static final KeyMap[] CATEGORIES_BY_INDUSTRY = new KeyMap[]{
		new KeyMap("CBI01", SmartMessage.getString("category.industry.1")),
		new KeyMap("CBI02", SmartMessage.getString("category.industry.2")),
		new KeyMap("CBI03", SmartMessage.getString("category.industry.3")),
		new KeyMap("CBI04", SmartMessage.getString("category.industry.4")),
		new KeyMap("CBI05", SmartMessage.getString("category.industry.5")),
		new KeyMap("CBI06", SmartMessage.getString("category.industry.6")),
		new KeyMap("CBI07", SmartMessage.getString("category.industry.7")),
		new KeyMap("CBI08", SmartMessage.getString("category.industry.8")),
		new KeyMap("CBI09", SmartMessage.getString("category.industry.9")),
		new KeyMap("CBI10", SmartMessage.getString("category.industry.10")),
		new KeyMap("CBI11", SmartMessage.getString("category.industry.11")),
		new KeyMap("CBI99", SmartMessage.getString("category.industry.99"))
	};
	public static final KeyMap[] CATEGORIES_BY_JOB = new KeyMap[]{
		new KeyMap("CBJ01", SmartMessage.getString("category.job.1")),
		new KeyMap("CBJ02", SmartMessage.getString("category.job.2")),
		new KeyMap("CBJ03", SmartMessage.getString("category.job.3")),
		new KeyMap("CBJ04", SmartMessage.getString("category.job.4")),
		new KeyMap("CBJ05", SmartMessage.getString("category.job.5")),
		new KeyMap("CBJ06", SmartMessage.getString("category.job.6")),
		new KeyMap("CBJ07", SmartMessage.getString("category.job.7")),
		new KeyMap("CBJ08", SmartMessage.getString("category.job.8")),
		new KeyMap("CBJ09", SmartMessage.getString("category.job.9")),
		new KeyMap("CBJ10", SmartMessage.getString("category.job.10")),
		new KeyMap("CBJ99", SmartMessage.getString("category.job.99"))
	};

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
		switch(getType()){
		case SmartWork.TYPE_INFORMATION:
			if(getId().equals(SmartWork.ID_FILE_MANAGEMENT))
				return Work.ICON_CLASS_FILE_WORKS;
			else if(getId().equals(SmartWork.ID_EVENT_MANAGEMENT))
				return Work.ICON_CLASS_EVENT_WORKS;
			else if(getId().equals(SmartWork.ID_BOARD_MANAGEMENT))
				return Work.ICON_CLASS_BOARD_WORKS;
			else if(getId().equals(SmartWork.ID_MEMO_MANAGEMENT))
				return Work.ICON_CLASS_MEMO_WORKS;
			else if(getId().equals(SmartWork.ID_FORUM_MANAGEMENT))
				return Work.ICON_CLASS_FORUM_WORKS;
			else if(getId().equals(SmartWork.ID_CONTACTS_MANAGEMENT))
				return Work.ICON_CLASS_CONTACTS_WORKS;
			else if(getId().equals(SmartWork.ID_USER_MANAGEMENT))
				return Work.ICON_CLASS_USER_WORKS;
			else if(getId().equals(SmartWork.ID_DEPARTMENT_MANAGEMENT))
				return Work.ICON_CLASS_DEPARTMENT_WORKS;
			else if(getId().equals(SmartWork.ID_GROUP_MANAGEMENT))
				return Work.ICON_CLASS_GROUP_WORKS;
			else
				return ((SmartWork)this).isRunning() ? Work.ICON_CLASS_IWORKS_ON : Work.ICON_CLASS_IWORKS_OFF;
		case SmartWork.TYPE_PROCESS:
			return ((SmartWork)this).isRunning() ? Work.ICON_CLASS_PWORKS_ON : Work.ICON_CLASS_PWORKS_OFF;
		case SmartWork.TYPE_SCHEDULE:
			return ((SmartWork)this).isRunning() ? Work.ICON_CLASS_SWORKS_ON : Work.ICON_CLASS_SWORKS_OFF;
		case WorkCategory.TYPE_CATEGORY:
			if(getId().equals(WorkCategory.ID_DEFAULT_CATEGORY))
				return Work.ICON_CLASS_DEFAULT_CATEGORY;
			else if(getId().equals(WorkCategory.ID_DOWNLOADED_CATEGORY))
				return Work.ICON_CLASS_DOWNLOADED_CATEGORY;
			return ((WorkCategory)this).isRunning() ? Work.ICON_CLASS_CWORKS_ON : Work.ICON_CLASS_CWORKS_OFF;
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
