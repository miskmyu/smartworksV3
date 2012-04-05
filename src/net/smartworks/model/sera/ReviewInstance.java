package net.smartworks.model.sera;

import net.smartworks.model.community.User;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.WorkInstance;
import net.smartworks.model.work.Work;
import net.smartworks.util.LocalDate;

public class ReviewInstance extends WorkInstance {
		
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
	
	public ReviewInstance(){
		super();
		super.setType(Instance.TYPE_REVIEW);
	}

	public ReviewInstance(String id, String subject, Work work, User owner, LocalDate lastModifiedDate){
			super(id, subject, work, owner, owner, lastModifiedDate);
			super.setType(Instance.TYPE_REVIEW);
	}
}