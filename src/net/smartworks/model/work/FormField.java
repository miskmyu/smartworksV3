package net.smartworks.model.work;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import net.smartworks.model.BaseObject;
import net.smartworks.util.SmartMessage;

public class FormField extends BaseObject{
	
	public static final String ID_STATUS = "status";
	public static final String ID_SUBJECT = "subject";
	public static final String ID_TASK_NAME = "taskName";
	public static final String ID_TSK_NAME = "tskName";
	public static final String ID_TSK_TITLE = "tskTitle";
	public static final String ID_TSK_ASSIGNEE = "tskAssignee";
	public static final String ID_LAST_TASK = "lastTask";
	public static final String ID_TASK_LAST_MODIFYDATE = "taskLastModifyDate";
	public static final String ID_PROCESS_TIME = "processTime";
	public static final String ID_PROCESS_TYPE = "processType";
	public static final String ID_WORK = "Work";
	public static final String ID_WORK_INSTANCE = "WorkInstance";
	public static final String ID_WORK_SPACE = "WorkSpace";
	public static final String ID_FILE_CATEGORY = "FileCategory";
	public static final String ID_FILE_TYPE = "FileType";
	public static final String ID_FILE_NAME = "FileName";
	public static final String ID_FILE_SIZE = "FileSize";

	public static final String ID_OWNER = "creator";
	public static final String ID_CREATED_DATE = "createdTime";
	public static final String ID_LAST_MODIFIER = "modifier";
	public static final String ID_LAST_MODIFIED_DATE = "modifiedTime";
	
	public static final String ID_SENDER = "from";
	public static final String ID_RECEIVERS = "to";
	public static final String ID_SEND_DATE = "date";
	public static final String ID_MAIL_SIZE = "size";

	public static final String TYPE_OBJECT_ID = "objectId"; // work
	public static final String TYPE_TEXT = "textInput"; //string
	public static final String TYPE_USER = "userField"; //user
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
	public static final String[] FORM_FIELD_TYPES_ALL = new String[] {
		TYPE_TEXT, TYPE_USER, TYPE_FILE, TYPE_OTHER_WORK, TYPE_RICHTEXT_EDITOR, TYPE_NUMBER, TYPE_CURRENCY, TYPE_PERCENT,
		TYPE_COMBO, TYPE_IMAGE, TYPE_CHECK_BOX, TYPE_RADIO_BUTTON, TYPE_EMAIL, TYPE_DATE, TYPE_TIME, TYPE_DATETIME, TYPE_DATA_GRID
	};
	public static final String[] FORM_FIELD_TYPES_VARIABLE = new String[] {
		TYPE_TEXT, TYPE_USER, TYPE_FILE, TYPE_NUMBER, TYPE_CURRENCY, TYPE_PERCENT, TYPE_COMBO, TYPE_CHECK_BOX,
		TYPE_CURRENCY, TYPE_PERCENT, TYPE_RADIO_BUTTON, TYPE_EMAIL, TYPE_DATE, TYPE_TIME, TYPE_DATETIME
	};	
	private static final String PREFIX = "field.type.";
	public static final String[] FORM_FIELD_TYPE_NAMES_VARIABLE = new String[] {
		SmartMessage.getString(PREFIX+TYPE_TEXT), SmartMessage.getString(PREFIX+TYPE_USER), SmartMessage.getString(PREFIX+TYPE_FILE),
		SmartMessage.getString(PREFIX+TYPE_NUMBER), SmartMessage.getString(PREFIX+TYPE_CURRENCY), SmartMessage.getString(PREFIX+TYPE_PERCENT),
		SmartMessage.getString(PREFIX+TYPE_COMBO), SmartMessage.getString(PREFIX+TYPE_CHECK_BOX), SmartMessage.getString(PREFIX+TYPE_CURRENCY),
		SmartMessage.getString(PREFIX+TYPE_PERCENT), SmartMessage.getString(PREFIX+TYPE_RADIO_BUTTON), SmartMessage.getString(PREFIX+TYPE_EMAIL),
		SmartMessage.getString(PREFIX+TYPE_DATE), SmartMessage.getString(PREFIX+TYPE_TIME), SmartMessage.getString(PREFIX+TYPE_DATETIME)
	};

	public static final FormField FIELD_STATUS = new FormField(ID_STATUS, SmartMessage.getString("common.title.status"), TYPE_COMBO);
	public static final FormField FIELD_SUBJECT = new FormField(ID_SUBJECT, SmartMessage.getString("common.title.instance_subject"), TYPE_TEXT);
	public static final FormField FIELD_TASK_NAME = new FormField(ID_TASK_NAME, SmartMessage.getString("common.title.task_name"), TYPE_TEXT);
	public static final FormField FIELD_LAST_TASK = new FormField(ID_LAST_TASK, SmartMessage.getString("common.title.last_task"), TYPE_TEXT);
	public static final FormField FIELD_PROCESS_TIME = new FormField(ID_PROCESS_TIME, SmartMessage.getString("common.title.process_time"), TYPE_TIME);
	public static final FormField FIELD_PROCESS_TYPE = new FormField(ID_PROCESS_TYPE, SmartMessage.getString("common.title.process_type"), TYPE_COMBO);
	public static final FormField FIELD_WORK = new FormField(ID_WORK, SmartMessage.getString("common.title.work_name"), TYPE_TEXT);
	public static final FormField FIELD_WORK_INSTANCE = new FormField(ID_WORK_INSTANCE, SmartMessage.getString("common.title.instance_subject"), TYPE_TEXT);
	public static final FormField FIELD_WORK_SPACE = new FormField(ID_WORK_SPACE, SmartMessage.getString("common.title.work_space_name"), TYPE_TEXT);
	public static final FormField FIELD_FILE_CATEGORY = new FormField(ID_FILE_CATEGORY, SmartMessage.getString("common.title.category_name"), TYPE_TEXT);
	public static final FormField FIELD_FILE_TYPE = new FormField(ID_FILE_TYPE, SmartMessage.getString("common.title.file_type"), TYPE_TEXT);
	public static final FormField FIELD_WORK_ID = new FormField(ID_WORK, SmartMessage.getString("common.title.work_name"), TYPE_OBJECT_ID);
	public static final FormField FIELD_WORK_SPACE_ID = new FormField(ID_WORK_SPACE, SmartMessage.getString("common.title.work_space_name"), TYPE_OBJECT_ID);
	public static final FormField FIELD_FILE_CATEGORY_ID = new FormField(ID_FILE_CATEGORY, SmartMessage.getString("common.title.category_name"), TYPE_OBJECT_ID);

	public static final FormField FIELD_OWNER = new FormField(ID_OWNER, SmartMessage.getString("common.title.owner"), TYPE_USER);
	public static final FormField FIELD_CREATED_DATE = new FormField(ID_CREATED_DATE, SmartMessage.getString("common.title.created_date"), TYPE_DATE);
	public static final FormField FIELD_LAST_MODIFIER = new FormField(ID_LAST_MODIFIER, SmartMessage.getString("common.title.last_modifier"), TYPE_USER);
	public static final FormField FIELD_LAST_MODIFIED_DATE = new FormField(ID_LAST_MODIFIED_DATE, SmartMessage.getString("common.title.last_modified_date"), TYPE_DATE);

	public static final FormField[] DEFAULT_PROCESS_FIELDS = new FormField[] {
		FIELD_STATUS, FIELD_SUBJECT, FIELD_TASK_NAME, FIELD_LAST_TASK, FIELD_PROCESS_TIME, FIELD_PROCESS_TYPE, 
		FIELD_OWNER, FIELD_CREATED_DATE, FIELD_LAST_MODIFIER, FIELD_LAST_MODIFIED_DATE
	};
	private String type;
	private int displayOrder;
	
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
	public FormField(){
		super();
	}
	public FormField(String id, String name, String type){
		super(id, name);
		this.setType(type);
	}
	
	public String getPageName(){
		if(this.type == null) return null;
		if(type.equals(TYPE_TEXT) || type.equals(TYPE_RICHTEXT_EDITOR) || type.equals(TYPE_COMBO) || type.equals(TYPE_IMAGE) || type.equals(TYPE_EMAIL) || type.equals(TYPE_RADIO_BUTTON)){
			return "string_field";
		}else if(type.equals(TYPE_USER)){
			return "user_field";
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
		}
		return null;
	}

	public String getMSOFormat(){
		if(this.type == null) return "";
		if(type.equals(TYPE_TEXT) || type.equals(TYPE_RICHTEXT_EDITOR) || type.equals(TYPE_COMBO) || type.equals(TYPE_IMAGE) || type.equals(TYPE_EMAIL) || type.equals(TYPE_RADIO_BUTTON)){
			return "";
		}else if(type.equals(TYPE_USER)){
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
	
	public String getDataFormat(String data, String viewingType, int fieldSize) {
//
//		mso-number-format:"0"                       
//	        NO Decimals 
//	mso-number-format:"0\.000"                       
//	        3 Decimals 
//	mso-number-format:"\#\,\#\#0\.000"                       
//	        Comma with 3 dec 
//	mso-number-format:"mm\/dd\/yy"                       
//	        Date7 
//	mso-number-format:"mmmm\ d\,\ yyyy"                       
//	        Date9 
//	mso-number-format:"m\/d\/yy\ h\:mm\ AM\/PM"                       
//	        D -T AMPM 
//	mso-number-format:"Short Date"                       
//	        01/03/1998 
//	mso-number-format:"Medium Date"                       
//	        01-mar-98 
//	mso-number-format:"d\-mmm\-yyyy"                       
//	        01-mar-1998 
//	mso-number-format:"Short Time"                      
//	        5:16 
//	mso-number-format:"Medium Time"                       
//	        5:16 am 
//	mso-number-format:"Long Time"                       
//	        5:16:21:00 
//	mso-number-format:"Percent"                       
//	        Percent - two decimals 
//	mso-number-format:"0%"                         
//	        Percent - no decimals 
//	mso-number-format:"0\.E+00"                       
//	        Scientific Notation 
//	mso-number-format:"\@"                         
//	        Text 
//	mso-number-format:"\#\ ???\/???"                       
//	        Fractions - up to 3 digits (312/943) 
//	mso-number-format:"\0022￡\0022\#\,\#\#0\.00"                       
//	        ￡12.76 
//	mso-number-format:"\#\,\#\#0\.00_ \;\[Red\]\-\#\,\#\#0\.00\"                       
//	        2 decimals, negative numbers in red and signed(1.56   -1.56)
//	 
//	 
//	한 셀 안에서 줄바꿈
//	<style>   
//	.xl24   {mso-number-format:"\@";}   
//	br      {mso-data-placement:same-cell;}   
//	</style>
//	이 게시물을...
//		
//		
//		
		
		//StringBuffer str = new StringBuffer();
		DecimalFormat df = new java.text.DecimalFormat("###,###,###,###,###,###");
		String tdAlign = "left";
		//날짜
		if( viewingType.equalsIgnoreCase("dateChooser") ) {
			if (data.length() > 10) {
				data = data.substring(0, 10);
			}
			tdAlign="center";
		// 시간
		} else if( viewingType.equalsIgnoreCase("timeField") ) {
			if (data.length() == 21) {
				data = data.substring(11, 16);
			} else if(data.length() == 8) {
				data = data.substring(0, 5);
			}
			tdAlign="center";
		// 숫자
		} else if( viewingType.equalsIgnoreCase("numberInput") ) {
			try{      
				double dataDouble = new Double(data).doubleValue();
				data = df.format(dataDouble);
			} catch(Exception e){      

			} finally {      
				tdAlign="right";
			}
		// 숫자
		} else if( viewingType.equalsIgnoreCase("numericStepper") ) {
			try{      
				double dataDouble = new Double(data).doubleValue();
				data = df.format(dataDouble);
			} catch(Exception e){      

			} finally {      
				tdAlign="right";
			}
		// 통화
		} else if( viewingType.startsWith("currencyInput") ) {
			try{      
				double dataDouble = new Double(data).doubleValue();
				data = df.format(dataDouble);
				if(data.length() > 0) {
					data = viewingType.substring(14,viewingType.length()) + data.toString();
				}
			} catch(Exception e){      
				if(data.length() > 0) {
					data = viewingType.substring(14,viewingType.length()) + data.toString();
				}
			} finally {      
				tdAlign="right";
			}
		} else if( viewingType.equalsIgnoreCase("percentInput") ) {
			try{      
				double dataDouble = new Double(data).doubleValue();
				data = df.format(dataDouble);
				if(data.length() > 0) {
					data = data+"%";
				}
			} catch(Exception e){      
				if(data.length() > 0) {
					data = data+"%";
				}
			} finally {      
				tdAlign="right";
			}
			tdAlign="right";
		//첨부파일
		} else if( viewingType.equalsIgnoreCase("fileField") ) {
			tdAlign="center";
		}
		return "<td style='text-align:" + tdAlign + ";'>" + data + "</td>";
	}
	
}
