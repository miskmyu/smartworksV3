package net.smartworks.model.instance.info;

import java.util.Map;

import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.work.info.WorkInfo;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartUtil;

public class ImageInstanceInfo extends FileInstanceInfo {

	private int views;
	private String fileId;
	private String imgSource;
	private	String originImgSource;

	private String content;
	
	public int getViews() {
		return views;
	}
	public void setViews(int views) {
		this.views = views;
	}
	public String getImgSource() {
		return imgSource;
	}
	public void setImgSource(String imgSource) {
		this.imgSource = imgSource;
	}
	public String getOriginImgSource() {
		return originImgSource;
	}
	public void setOriginImgSource(String originImgSource) {
		this.originImgSource = originImgSource;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public ImageInstanceInfo(){
		super();
		super.setType(Instance.TYPE_IMAGE);
	}

	public ImageInstanceInfo(String id, String subject, WorkInfo work, UserInfo owner, LocalDate lastModifiedDate){
		super(id, subject, work, owner, lastModifiedDate);
		super.setType(Instance.TYPE_IMAGE);
	}
	
	public String getFileName(){
		if(SmartUtil.isBlankObject(getFiles())) return "";
		
		for(int i=0; i<getFiles().size(); i++){
			Map<String, String> file = (Map<String, String>)getFiles().get(i);
			return file.get("fileName");
		}
		return "";
	}

}
