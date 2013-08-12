package net.smartworks.model.work;

import net.smartworks.model.BaseObject;
import net.smartworks.util.SmartMessage;

public class FormField extends BaseObject{
	
	public static final String ID_STATUS = "status";
	public static final String ID_SUBJECT = "subject";
	public static final String ID_TASK_NAME = "taskName";
	public static final String ID_TSK_NAME = "tskName";
	public static final String ID_TSK_TITLE = "tskTitle";
	public static final String ID_TSK_ASSIGNEE = "tskAssignee";
	public static final String ID_TASK_ASSIGNEE = "taskAssignee";
	public static final String ID_TASK_PERFORMER = "taskPerformer";
	public static final String ID_LAST_TASK = "lastTask";
	public static final String ID_TASK_LAST_MODIFYDATE = "taskLastModifyDate";
	public static final String ID_PROCESS_TIME = "processTime";
	public static final String ID_DELAYED_TIME = "delayedTime";
	public static final String ID_DELAYED_INSTANCE = "delayedInstance";
	public static final String ID_PROCESS_TYPE = "processType";
	public static final String ID_TASK_PROCESS_TIME = "tskProcessTime";
	public static final String ID_TASK_DELAYED_TIME = "tskDelayedTime";
	public static final String ID_DELAYED_TASK = "delayedTask";
	public static final String ID_WORK = "Work";
	public static final String ID_WORK_INSTANCE = "WorkInstance";
	public static final String ID_WORK_SPACE = "WorkSpace";
	public static final String ID_WORK_SPACE_NAME = "WorkSpaceName";
	public static final String ID_FILE_CATEGORY = "FileCategory";
	public static final String ID_FILE_TYPE = "FileType";
	public static final String ID_FILE_NAME = "FileName";
	public static final String ID_FILE_SIZE = "FileSize";
	public static final String ID_BOARD_DURATION = "boardDuration";

	public static final String ID_OWNER = "creator";
	public static final String ID_CREATED_DATE = "createdTime";
	public static final String ID_LAST_MODIFIER = "modifier";
	public static final String ID_LAST_MODIFIED_DATE = "modifiedTime";
	
	public static final String ID_INSTANCE_VIEWS = "hits";
	public static final String ID_SUB_INSTANCE_COUNT = "subInstanceCount";
	public static final String ID_MODIFIED_COUNT = "modifiedCount";
	
	public static final String ID_TARGET_WORK_TYPE = "targetWorkType";
	public static final String ID_REPORT_TYPE = "reportType";
	public static final String ID_CHART_TYPE = "chartType";
	public static final String ID_ACCESS_LEVEL = "accessLevel";
	
	public static final String ID_SENDER = "from";
	public static final String ID_RECEIVERS = "to";
	public static final String ID_SEND_DATE = "date";
	public static final String ID_MAIL_SIZE = "size";
	public static final String ID_MULTIPART = "multipart";
	
	public static final String ID_APP_WORK_NAME = "appWorkName";
	public static final String ID_APP_WORK_TYPE = "appWorkType";
	public static final String ID_APP_WORK_INDUSTRY = "appWorkIndustry";
	public static final String ID_APP_WORK_JOB = "appWorkJob";
	public static final String ID_APP_PUBLISHED_COMPANY = "appPublishedCompany";
	public static final String ID_APP_PUBLISHED_DATE = "appPublishedDate";

	public static final String ID_NAME = "name";
	public static final String ID_GROUP_IS_PUBLIC = "groupType";
	public static final String ID_GROUP_LEADER = "groupLeader";
	public static final String ID_CREATION_USER = "creationUser";
	public static final String ID_CREATION_DATE = "creationDate";
	
	public static final String ID_NUM_EVENT_START_TIME = "1";
	public static final String ID_NUM_EVENT_END_TIME = "2";
	public static final String ID_NUM_RELATED_USERS = "5";
	public static final String ID_NUM_EVENT_ALARM = "7";
	public static final String ID_NUM_REPEAT_EVENT_ID = "8";
	
	public static final String TYPE_OBJECT_ID = "objectId"; // work
	public static final String TYPE_TEXT = "textInput"; //string
	public static final String TYPE_USER = "userField"; //user
	public static final String TYPE_DEPARTMENT = "departmentField"; //user
	public static final String TYPE_FILE = "fileField"; //file
	public static final String TYPE_OTHER_WORK = "refFormField"; // form field of the referred work
	public static final String TYPE_RICHTEXT_EDITOR = "richEditor"; //string
	public static final String TYPE_NUMBER = "numberInput"; //number
	public static final String TYPE_CURRENCY = "currencyInput"; //number
	public static final String TYPE_PERCENT = "percentInput"; //number
	public static final String TYPE_COMBO = "comboBox"; // string
	public static final String TYPE_IMAGE = "imageBox"; // string
	public static final String TYPE_CHECK_BOX = "checkBox"; // boolean
	public static final String TYPE_RADIO_BUTTON = "radioButton"; // text
	public static final String TYPE_EMAIL = "emailIDInput"; // string
	public static final String TYPE_DATE = "dateChooser"; //date
	public static final String TYPE_TIME = "timeField"; //time
	public static final String TYPE_DATETIME = "dateTimeChooser"; //datetime
	public static final String TYPE_DATA_GRID = "dataGrid"; //
	
	public static final String TYPE_COMBO_BOOLEAN = "comboBoolean";
	public static final String TYPE_COMBO_STATUS = "comboStatus";
	
	public static final String[] FORM_FIELD_TYPES_ALL = new String[] {
		TYPE_TEXT, TYPE_USER, TYPE_DEPARTMENT, TYPE_FILE, TYPE_OTHER_WORK, TYPE_RICHTEXT_EDITOR, TYPE_NUMBER, TYPE_CURRENCY, TYPE_PERCENT,
		TYPE_COMBO, TYPE_IMAGE, TYPE_CHECK_BOX, TYPE_RADIO_BUTTON, TYPE_EMAIL, TYPE_DATE, TYPE_TIME, TYPE_DATETIME, TYPE_DATA_GRID
	};
	public static final String[] FORM_FIELD_TYPES_VARIABLE = new String[] {
		TYPE_TEXT, TYPE_USER, TYPE_DEPARTMENT, TYPE_FILE, TYPE_NUMBER, TYPE_CURRENCY, TYPE_PERCENT, TYPE_COMBO, TYPE_CHECK_BOX,
		TYPE_CURRENCY, TYPE_PERCENT, TYPE_RADIO_BUTTON, TYPE_EMAIL, TYPE_DATE, TYPE_TIME, TYPE_DATETIME
	};	
	private static final String PREFIX = "field.type.";
	public static final String[] FORM_FIELD_TYPE_NAMES_VARIABLE = new String[] {
		SmartMessage.getString(PREFIX+TYPE_TEXT), SmartMessage.getString(PREFIX+TYPE_USER), SmartMessage.getString(PREFIX+TYPE_DEPARTMENT), SmartMessage.getString(PREFIX+TYPE_FILE),
		SmartMessage.getString(PREFIX+TYPE_NUMBER), SmartMessage.getString(PREFIX+TYPE_CURRENCY), SmartMessage.getString(PREFIX+TYPE_PERCENT),
		SmartMessage.getString(PREFIX+TYPE_COMBO), SmartMessage.getString(PREFIX+TYPE_CHECK_BOX), SmartMessage.getString(PREFIX+TYPE_CURRENCY),
		SmartMessage.getString(PREFIX+TYPE_PERCENT), SmartMessage.getString(PREFIX+TYPE_RADIO_BUTTON), SmartMessage.getString(PREFIX+TYPE_EMAIL),
		SmartMessage.getString(PREFIX+TYPE_DATE), SmartMessage.getString(PREFIX+TYPE_TIME), SmartMessage.getString(PREFIX+TYPE_DATETIME)
	};
	
	public static final FormField FIELD_STATUS = new FormField(ID_STATUS, SmartMessage.getString("common.title.status"), TYPE_COMBO_STATUS);
	public static final FormField FIELD_SUBJECT = new FormField(ID_SUBJECT, SmartMessage.getString("common.title.instance_subject"), TYPE_TEXT);
	public static final FormField FIELD_TASK_NAME = new FormField(ID_TASK_NAME, SmartMessage.getString("common.title.task_name"), TYPE_TEXT);
	public static final FormField FIELD_TASK_ASSIGNEE = new FormField(ID_TASK_ASSIGNEE, SmartMessage.getString("common.title.task_assignee"), TYPE_USER);
	public static final FormField FIELD_TASK_PERFORMER = new FormField(ID_TASK_PERFORMER, SmartMessage.getString("common.title.task_performer"), TYPE_USER);
	public static final FormField FIELD_LAST_TASK = new FormField(ID_LAST_TASK, SmartMessage.getString("common.title.last_task"), TYPE_TEXT);
	public static final FormField FIELD_PROCESS_TIME = new FormField(ID_PROCESS_TIME, SmartMessage.getString("common.title.process_time"), TYPE_TIME);
	public static final FormField FIELD_DELAYED_TIME = new FormField(ID_DELAYED_TIME, SmartMessage.getString("common.title.delayed_time"), TYPE_TIME);
	public static final FormField FIELD_DELAYED_INSTANCE = new FormField(ID_DELAYED_INSTANCE, SmartMessage.getString("common.title.delayed_instance"), TYPE_TEXT);
	public static final FormField FIELD_TASK_PROCESS_TIME = new FormField(ID_TASK_PROCESS_TIME, SmartMessage.getString("common.title.task_process_time"), TYPE_TIME);
	public static final FormField FIELD_TASK_DELAYED_TIME = new FormField(ID_TASK_DELAYED_TIME, SmartMessage.getString("common.title.task_delayed_time"), TYPE_TIME);
	public static final FormField FIELD_DELAYED_TASK = new FormField(ID_DELAYED_TASK, SmartMessage.getString("common.title.delayed_task"), TYPE_TEXT);
	public static final FormField FIELD_WORK = new FormField(ID_WORK, SmartMessage.getString("common.title.work_name"), TYPE_TEXT);
	public static final FormField FIELD_WORK_INSTANCE = new FormField(ID_WORK_INSTANCE, SmartMessage.getString("common.title.instance_subject"), TYPE_TEXT);
	public static final FormField FIELD_WORK_SPACE_NAME = new FormField(ID_WORK_SPACE_NAME, SmartMessage.getString("common.title.work_space_name"), TYPE_TEXT);
	public static final FormField FIELD_FILE_CATEGORY = new FormField(ID_FILE_CATEGORY, SmartMessage.getString("common.title.category_name"), TYPE_TEXT);
	public static final FormField FIELD_FILE_TYPE = new FormField(ID_FILE_TYPE, SmartMessage.getString("common.title.file_type"), TYPE_TEXT);
	public static final FormField FIELD_WORK_ID = new FormField(ID_WORK, SmartMessage.getString("common.title.work_name"), TYPE_OBJECT_ID);
	public static final FormField FIELD_FILE_CATEGORY_ID = new FormField(ID_FILE_CATEGORY, SmartMessage.getString("common.title.category_name"), TYPE_OBJECT_ID);
	public static final FormField FIELD_BOARD_DURATION = new FormField(ID_BOARD_DURATION, SmartMessage.getString("common.upload.board.duration"), TYPE_DATE);

	public static final FormField FIELD_OWNER = new FormField(ID_OWNER, SmartMessage.getString("common.title.owner"), TYPE_USER);
	public static final FormField FIELD_CREATED_DATE = new FormField(ID_CREATED_DATE, SmartMessage.getString("common.title.created_date"), TYPE_DATE);
	public static final FormField FIELD_LAST_MODIFIER = new FormField(ID_LAST_MODIFIER, SmartMessage.getString("common.title.last_modifier"), TYPE_USER);
	public static final FormField FIELD_LAST_MODIFIED_DATE = new FormField(ID_LAST_MODIFIED_DATE, SmartMessage.getString("common.title.last_modified_date"), TYPE_DATE);
	public static final FormField FIELD_INSTANCE_VIEWS = new FormField(ID_INSTANCE_VIEWS, SmartMessage.getString("common.title.instance_views"), TYPE_NUMBER);
	public static final FormField FIELD_SUB_INSTANCE_COUNT = new FormField(ID_SUB_INSTANCE_COUNT, SmartMessage.getString("common.title.sub_instance_count"), TYPE_NUMBER);
	public static final FormField FIELD_MODIFIED_COUNT = new FormField(ID_MODIFIED_COUNT, SmartMessage.getString("common.title.modified_count"), TYPE_NUMBER);

	
	public static final Map<String, FormField> FORM_FIELD_MAP = new HashMap<String, FormField>() {
		{
			put(ID_STATUS,FIELD_STATUS);
			put(ID_SUBJECT,FIELD_SUBJECT);
			put(ID_TASK_NAME,FIELD_TASK_NAME);
			put(ID_TASK_ASSIGNEE,FIELD_TASK_ASSIGNEE);
			put(ID_TASK_PERFORMER,FIELD_TASK_PERFORMER);
			put(ID_LAST_TASK,FIELD_LAST_TASK);
			put(ID_PROCESS_TIME,FIELD_PROCESS_TIME);
			put(ID_DELAYED_TIME,FIELD_DELAYED_TIME);
			put(ID_DELAYED_INSTANCE,FIELD_DELAYED_INSTANCE);
			put(ID_TASK_PROCESS_TIME,FIELD_TASK_PROCESS_TIME);
			put(ID_TASK_DELAYED_TIME,FIELD_TASK_DELAYED_TIME);
			put(ID_DELAYED_TASK,FIELD_DELAYED_TASK);
			put(ID_WORK,FIELD_WORK);
			put(ID_WORK_INSTANCE,FIELD_WORK_INSTANCE);
			put(ID_WORK_SPACE_NAME,FIELD_WORK_SPACE_NAME);
			put(ID_FILE_CATEGORY,FIELD_FILE_CATEGORY_ID);
			put(ID_FILE_TYPE,FIELD_FILE_TYPE);
			//put(ID_WORK,FIELD_WORK_ID);
			//put(ID_FILE_CATEGORY,FIELD_FILE_CATEGORY_ID);
			put(ID_BOARD_DURATION,FIELD_BOARD_DURATION);
			put(ID_OWNER,FIELD_OWNER);
			put(ID_CREATED_DATE,FIELD_CREATED_DATE);
			put(ID_LAST_MODIFIER,FIELD_LAST_MODIFIER);
			put(ID_LAST_MODIFIED_DATE,FIELD_LAST_MODIFIED_DATE);
			put(ID_INSTANCE_VIEWS,FIELD_INSTANCE_VIEWS);
			put(ID_SUB_INSTANCE_COUNT,FIELD_SUB_INSTANCE_COUNT);
			put(ID_MODIFIED_COUNT,FIELD_MODIFIED_COUNT);
		}
	};
	
	public static final FormField[] DEFAULT_INFORMATION_FIELDS = new FormField[] {
		FIELD_INSTANCE_VIEWS, FIELD_SUB_INSTANCE_COUNT, FIELD_WORK_SPACE_NAME, FIELD_OWNER, FIELD_CREATED_DATE, FIELD_LAST_MODIFIER, FIELD_LAST_MODIFIED_DATE		
	};
	
	public static final FormField[] DEFAULT_PROCESS_FIELDS = new FormField[] {
		FIELD_STATUS, FIELD_SUBJECT, FIELD_LAST_TASK, FIELD_TASK_NAME, FIELD_TASK_ASSIGNEE, FIELD_TASK_PERFORMER, FIELD_TASK_PROCESS_TIME, FIELD_TASK_DELAYED_TIME, FIELD_DELAYED_TASK,
		FIELD_PROCESS_TIME, FIELD_DELAYED_TIME, FIELD_DELAYED_INSTANCE, FIELD_SUB_INSTANCE_COUNT, FIELD_WORK_SPACE_NAME, FIELD_OWNER, FIELD_CREATED_DATE, FIELD_LAST_MODIFIER, FIELD_LAST_MODIFIED_DATE
	};
	
	private String type;
	private int displayOrder;
	private boolean mandatory;
	
	public int getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isMandatory() {
		return mandatory;
	}
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	public FormField(){
		super();
	}
	public FormField(String id, String name, String type){
		super(id, name);
		this.setType(type);
	}
	
	public String getPageName(){
		if(this.type == null) return null;
		if(type.equals(TYPE_TEXT) || type.equals(TYPE_RICHTEXT_EDITOR) || type.equals(TYPE_IMAGE) || type.equals(TYPE_EMAIL) || type.equals(TYPE_RADIO_BUTTON)){
			return "string_field";
		}else if(type.equals(TYPE_USER)){
			return "user_field";
		}else if(type.equals(TYPE_DEPARTMENT)){
			return "department_field";
		}else if(type.equals(TYPE_FILE)){
			return "file_field";
		}else if(type.equals(TYPE_OTHER_WORK)){
			return "work_field";
		}else if(type.equals(TYPE_NUMBER) || type.equals(TYPE_CURRENCY) || type.equals(TYPE_PERCENT)){
			return "number_field";
		}else if(type.equals(TYPE_CHECK_BOX)){
			return "boolean_field";
		}else if(type.equals(TYPE_DATE)){
			return "date_field";
		}else if(type.equals(TYPE_TIME)){
			return "time_field";
		}else if(type.equals(TYPE_DATETIME)){
			return "datetime_field";
		}else if(type.equals(TYPE_COMBO_BOOLEAN)){
			return "combo_boolean_field";
		}else if(type.equals(TYPE_COMBO_STATUS)){
			return "combo_status_field";
		}
		return null;
	}

	public String getMSOFormat(){
		if(this.type == null) return "";
		if(type.equals(TYPE_TEXT) || type.equals(TYPE_RICHTEXT_EDITOR) || type.equals(TYPE_COMBO) || type.equals(TYPE_IMAGE) || type.equals(TYPE_EMAIL) || type.equals(TYPE_RADIO_BUTTON)){
			return "";
		}else if(type.equals(TYPE_USER)){
			return "";
		}else if(type.equals(TYPE_DEPARTMENT)){
			return "";
		}else if(type.equals(TYPE_FILE)){
			return "";
		}else if(type.equals(TYPE_OTHER_WORK)){
			return "";
		}else if(type.equals(TYPE_NUMBER)){
			return "mso-number-format:#,##0.#";
		}else if(type.equals(TYPE_CURRENCY)){
			return "mso-number-format:#,##0.#";			
		}else if(type.equals(TYPE_PERCENT)){
			return "mso-number-format:#,##0.#%";
		}else if(type.equals(TYPE_CHECK_BOX)){
			return "";
		}else if(type.equals(TYPE_DATE)){
			return "mso-number-format:YYYY-MM-DD";
		}else if(type.equals(TYPE_TIME)){
			return "mso-number-format:h:mm";
		}else if(type.equals(TYPE_DATETIME)){
			return "mso-number-format:YYYY-MM-DD h:mm";
		}
		return null;
	}
	
	public String getStyle() {
		if(this.type == null) return "";
		if(type.equals(TYPE_TEXT)){
			return "";
		}else if(type.equals(TYPE_RICHTEXT_EDITOR)){
			return "";
		}else if(type.equals(TYPE_COMBO)){
			return "";
		}else if(type.equals(TYPE_IMAGE)){
			return "";
		}else if(type.equals(TYPE_EMAIL)){
			return "";
		}else if(type.equals(TYPE_RADIO_BUTTON)){
			return "";
		}else if(type.equals(TYPE_USER)){
			return "";
		}else if(type.equals(TYPE_DEPARTMENT)){
			return "";
		}else if(type.equals(TYPE_FILE)){
			return "";
		}else if(type.equals(TYPE_OTHER_WORK)){
			return "";
		}else if(type.equals(TYPE_NUMBER)){
			return "text-align:right";
		}else if(type.equals(TYPE_CURRENCY)){
			return "text-align:right";
		}else if(type.equals(TYPE_PERCENT)){
			return "text-align:right";
		}else if(type.equals(TYPE_CHECK_BOX)){
			return "";
		}else if(type.equals(TYPE_DATE)){
			return "";
		}else if(type.equals(TYPE_TIME)){
			return "";
		}else if(type.equals(TYPE_DATETIME)){
			return "";
		}
		return "";
	}
	public boolean isImportableField() {
		if(this.type == null) return false;
		//if(	type.equals(TYPE_FILE) || type.equals(TYPE_OTHER_WORK) || type.equals(TYPE_IMAGE) || type.equals(TYPE_DATA_GRID))
		//if(	type.equals(TYPE_FILE) || type.equals(TYPE_IMAGE) || type.equals(TYPE_DATA_GRID))
		if(type.equals(TYPE_IMAGE) || type.equals(TYPE_DATA_GRID))
			return false;
		return true;
	}
	
}
