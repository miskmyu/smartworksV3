package net.smartworks.model.sera.info;

import java.util.List;
import java.util.Map;

import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.info.WorkInstanceInfo;
import net.smartworks.model.sera.MissionInstance;
import net.smartworks.util.LocalDate;

public class NoteInstanceInfo extends WorkInstanceInfo {

	private String content;
	private String imageSrc;
	private String imageSrcOrigin;
	private String videoId;
	private String linkUrl;
	private String fileGroupId;
	private List<Map<String, String>> fileList;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}	
	public String getImageSrc() {
		return imageSrc;
	}
	public void setImageSrc(String imageSrc) {
		this.imageSrc = imageSrc;
	}
	public String getImageSrcOrigin() {
		return imageSrcOrigin;
	}
	public void setImageSrcOrigin(String imageSrcOrigin) {
		this.imageSrcOrigin = imageSrcOrigin;
	}
	public String getVideoId() {
		return videoId;
	}
	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public String getFileGroupId() {
		return fileGroupId;
	}
	public void setFileGroupId(String fileGroupId) {
		this.fileGroupId = fileGroupId;
	}
	public List<Map<String, String>> getFileList() {
		return fileList;
	}
	public void setFileList(List<Map<String, String>> fileList) {
		this.fileList = fileList;
	}
	public NoteInstanceInfo(){
		super();
		super.setType(Instance.TYPE_MISSION);
	}

	public NoteInstanceInfo(String id, String subject, UserInfo owner, LocalDate lastModifiedDate){
		super(id, subject,  owner, owner, lastModifiedDate);
		super.setType(Instance.TYPE_MISSION);
	}
}