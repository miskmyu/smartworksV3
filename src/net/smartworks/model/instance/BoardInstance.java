package net.smartworks.model.instance;

import java.util.List;
import java.util.Map;

import net.smartworks.model.community.User;
import net.smartworks.model.work.SmartWork;
import net.smartworks.model.work.Work;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartUtil;

public class BoardInstance extends WorkInstance {
		
	private int views;
	private String content;
	private String fileGroupId;
	private List<Map<String, String>> files;

	public int getViews() {
		return views;
	}
	public void setViews(int views) {
		this.views = views;
	}
	public String getFileGroupId() {
		return fileGroupId;
	}
	public void setFileGroupId(String fileGroupId) {
		this.fileGroupId = fileGroupId;
	}
	public List<Map<String, String>> getFiles() {
		return files;
	}
	public void setFiles(List<Map<String, String>> files) {
		this.files = files;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public BoardInstance(){
		super();
		super.setType(Instance.TYPE_BOARD);
	}

	public BoardInstance(String id, String subject, Work work, User owner, LocalDate lastModifiedDate){
			super(id, subject, work, owner, owner, lastModifiedDate);
			super.setType(Instance.TYPE_BOARD);
	}
	
	public String getFilesHtml(){
		if(SmartUtil.isBlankObject(this.fileGroupId) || SmartUtil.isBlankObject(this.files)) return "";
		return SmartUtil.getFilesDetailInfo(this.files, SmartWork.ID_BOARD_MANAGEMENT, null, this.getId());
	}	
}