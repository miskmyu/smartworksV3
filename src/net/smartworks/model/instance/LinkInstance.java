package net.smartworks.model.instance;

import net.smartworks.model.community.User;
import net.smartworks.model.work.Work;
import net.smartworks.util.LocalDate;

public class LinkInstance extends WorkInstance {

	private int views;
	private String url;

	private String content;
	
	
	public int getViews() {
		return views;
	}
	public void setViews(int views) {
		this.views = views;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public LinkInstance(){
		super();
		super.setType(Instance.TYPE_LINK);
	}

	public LinkInstance(String id, String subject, Work work, User owner, LocalDate lastModifiedDate){
			super(id, subject, work, owner, owner, lastModifiedDate);
			super.setType(Instance.TYPE_LINK);
	}

	
}
