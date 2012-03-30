package net.smartworks.model.instance.info;

import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.work.info.WorkInfo;
import net.smartworks.util.LocalDate;

public class UVideoInstanceInfo extends FileInstanceInfo {

	private String content;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public UVideoInstanceInfo(){
		super();
		super.setType(Instance.TYPE_UVIDEO);
	}

	public UVideoInstanceInfo(String id, String subject, WorkInfo work, UserInfo owner, LocalDate lastModifiedDate){
		super();
		super.setType(Instance.TYPE_UVIDEO);
	}

}
