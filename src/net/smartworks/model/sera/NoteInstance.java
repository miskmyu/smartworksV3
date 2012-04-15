package net.smartworks.model.sera;

import java.util.List;
import java.util.Map;

import net.smartworks.model.community.User;
import net.smartworks.model.instance.FileInstance;
import net.smartworks.model.instance.ImageInstance;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.LinkInstance;
import net.smartworks.model.instance.WorkInstance;
import net.smartworks.model.instance.YTVideoInstance;
import net.smartworks.model.work.Work;
import net.smartworks.util.LocalDate;

public class NoteInstance extends WorkInstance {

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
	public NoteInstance(){
		super();
		super.setType(Instance.TYPE_SERA_NOTE);
	}

	public NoteInstance(String id, String subject, User owner, LocalDate lastModifiedDate){
			super(id, subject, null, owner, owner, lastModifiedDate);
			super.setType(Instance.TYPE_SERA_NOTE);
	}

	
}
