package net.smartworks.model.instance.info;

import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.Instance;
import net.smartworks.util.LocalDate;

public class YTVideoInstanceInfo extends WorkInstanceInfo {

	private String fileName;
	private String category;
	private String briefContent;
	private String content;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getBriefContent() {
		return briefContent;
	}
	public void setBriefContent(String briefContent) {
		this.briefContent = briefContent;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public YTVideoInstanceInfo(){
		super();
		super.setType(Instance.TYPE_YTVIDEO);
	}
	
	public YTVideoInstanceInfo(String id, String subject, UserInfo owner, LocalDate lastModifiedDate){
		super(id, subject, owner, owner, lastModifiedDate);
		super.setType(Instance.TYPE_YTVIDEO);
	}
}
