package net.smartworks.model.instance.info;

import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.work.info.WorkInfo;
import net.smartworks.util.LocalDate;

public class LinkInstanceInfo extends FileInstanceInfo {

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
	public LinkInstanceInfo(){
		super();
		super.setType(Instance.TYPE_LINK);
	}

	public LinkInstanceInfo(String id, String subject, WorkInfo work, UserInfo owner, LocalDate lastModifiedDate){
		super(id, subject, work, owner, lastModifiedDate);
		super.setType(Instance.TYPE_LINK);
	}

}
