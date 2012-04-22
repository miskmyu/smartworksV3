package net.smartworks.model.sera.info;

import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.info.WorkInstanceInfo;
import net.smartworks.model.work.info.WorkInfo;
import net.smartworks.util.LocalDate;

public class ReviewInstanceInfo extends WorkInstanceInfo {

	private String briefContent;
	private String content;
	private double starPoint;
	
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
	public double getStarPoint() {
		return starPoint;
	}
	public void setStarPoint(double starPoint) {
		this.starPoint = starPoint;
	}
	
	public ReviewInstanceInfo(){
		super();
		super.setType(Instance.TYPE_SERA_REVIEW);
	}

	public ReviewInstanceInfo(String id, String subject, WorkInfo work, UserInfo owner, LocalDate lastModifiedDate){
		super(id, subject, owner, owner, lastModifiedDate);
		super.setType(Instance.TYPE_SERA_REVIEW);
	}

}
