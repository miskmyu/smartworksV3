package net.smartworks.model.work;

import net.smartworks.model.BaseObject;

public class SmartForm extends BaseObject{

	public static final String  NO_FORM_IMAGE  = "no_form_image";

	public final static String ID_MEMO_MANAGEMENT = "frm_memo_SYSTEM";
	public final static String NAME_MEMO_MANAGEMENT = "메모";
	public final static String ID_EVENT_MANAGEMENT = "frm_event_SYSTEM";
	//start added by jybae, 2012-8-5
	//이벤트 > 일정으로 수정
	public final static String NAME_EVENT_MANAGEMENT = "일정";
	//end added by jybae, 2012-8-5

	private String description;
	private String minImageName;
	private String orgImageName;
	private FormField[] fields;

	public FormField[] getFields() {
		return fields;
	}
	public void setFields(FormField[] fields) {
		this.fields = fields;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMinImageName() {
		if(this.minImageName != null && !this.minImageName.equals(""))
			return this.minImageName;
		return 		NO_FORM_IMAGE + "_min.jpg";
	}
	public void setMinImageName(String minImageName) {
		this.minImageName = minImageName;
	}
	public String getOrgImageName() {
		if(this.orgImageName != null && !this.orgImageName.equals(""))
			return this.orgImageName;
		return 		NO_FORM_IMAGE + ".jpg";
	}
	public void setOrgImageName(String orgImageName) {
		this.orgImageName = orgImageName;
	}
	public String getOrgImage(){
		return getOrgImageName();
	}
	public String getMinImage(){
		return getMinImageName();
	}
	public SmartForm(){
		super();
	}
	public SmartForm(String id, String name){
		super(id, name);
	}
}
