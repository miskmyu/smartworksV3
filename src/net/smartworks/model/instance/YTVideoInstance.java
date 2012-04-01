package net.smartworks.model.instance;

import net.smartworks.model.community.User;
import net.smartworks.model.work.Work;
import net.smartworks.util.LocalDate;

public class YTVideoInstance extends WorkInstance {

	private String fileName;
	private String category;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public YTVideoInstance(){
		super();
		super.setType(Instance.TYPE_YTVIDEO);
	}

	public YTVideoInstance(String id, String subject, Work work, User owner, LocalDate lastModifiedDate){
			super(id, subject, work, owner, owner, lastModifiedDate);
			super.setType(Instance.TYPE_YTVIDEO);
	}

	
}
