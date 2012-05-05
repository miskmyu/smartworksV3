package net.smartworks.model.sera;

import net.smartworks.model.community.User;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.WorkInstance;
import net.smartworks.model.work.Work;
import net.smartworks.util.LocalDate;

public class ReviewInstance extends WorkInstance {
		
	private String content;
	private double starPoint;
	
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
	
	public ReviewInstance(){
		super();
		super.setType(Instance.TYPE_SERA_REVIEW);
	}

	public ReviewInstance(String id, String subject, Work work, User owner, LocalDate lastModifiedDate){
			super(id, subject, work, owner, owner, lastModifiedDate);
			super.setType(Instance.TYPE_SERA_REVIEW);
	}
}