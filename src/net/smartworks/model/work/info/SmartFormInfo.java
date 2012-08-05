package net.smartworks.model.work.info;

import net.smartworks.model.BaseObject;
import net.smartworks.model.work.SmartForm;

public class SmartFormInfo extends BaseObject{

	private String description;
	private String minImageName;
	private String orgImageName;
	
	public String getDescription(){
		return this.description;
	}
	public void setDescription(String description){
		this.description = description;
	}
	public String getMinImageName() {
		if(this.minImageName != null && !this.minImageName.equals(""))
			return this.minImageName;
		return SmartForm.NO_FORM_IMAGE + "_min.jpg";
	}
	public void setMinImageName(String minImageName) {
		this.minImageName = minImageName;
	}
	public String getOrgImageName() {
		if(this.orgImageName != null && !this.orgImageName.equals(""))
			return this.orgImageName;
		return SmartForm.NO_FORM_IMAGE + ".jpg";
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
	public SmartFormInfo(){
		super();
	}
	public SmartFormInfo(String id, String name){
		super(id, name);
	}
}
