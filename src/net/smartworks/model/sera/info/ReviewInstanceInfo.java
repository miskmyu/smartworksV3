package net.smartworks.model.sera.info;

import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.info.WorkInstanceInfo;
import net.smartworks.model.work.info.WorkInfo;
import net.smartworks.util.LocalDate;

public class ReviewInstanceInfo extends WorkInstanceInfo {

	private String content;
	private int startPoint;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getStartPoint() {
		return startPoint;
	}
	public void setStartPoint(int startPoint) {
		this.startPoint = startPoint;
	}
	
	public ReviewInstanceInfo(){
		super();
		super.setType(Instance.TYPE_REVIEW);
	}

	public ReviewInstanceInfo(String id, String subject, WorkInfo work, UserInfo owner, LocalDate lastModifiedDate){
		super(id, subject, owner, owner, lastModifiedDate);
		super.setType(Instance.TYPE_REVIEW);
	}

}
