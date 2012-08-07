package net.smartworks.model.instance;

import net.smartworks.model.community.User;
import net.smartworks.model.instance.info.InstanceInfo;
import net.smartworks.model.work.Work;
import net.smartworks.util.LocalDate;

public class ImageInstance extends WorkInstance {

	private int views;
	private String groupId;
	private String imgSource;
	private	String originImgSource;
	private String content;	
	private String fileName;
	private String prevInstId;
	private String nextInstId;
	private int subInstanceCount;
	private InstanceInfo[] subInstances;
	
	public int getViews() {
		return views;
	}
	public void setViews(int views) {
		this.views = views;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
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
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getPrevInstId() {
		return prevInstId;
	}
	public void setPrevInstId(String prevInstId) {
		this.prevInstId = prevInstId;
	}
	public String getNextInstId() {
		return nextInstId;
	}
	public void setNextInstId(String nextInstId) {
		this.nextInstId = nextInstId;
	}
	public int getSubInstanceCount() {
		return subInstanceCount;
	}
	public void setSubInstanceCount(int subInstanceCount) {
		this.subInstanceCount = subInstanceCount;
	}
	public InstanceInfo[] getSubInstances() {
		return subInstances;
	}
	public void setSubInstances(InstanceInfo[] subInstances) {
		this.subInstances = subInstances;
	}
	public ImageInstance(){
		super();
		super.setType(Instance.TYPE_IMAGE);
	}

	public ImageInstance(String id, String subject, Work work, User owner, LocalDate lastModifiedDate){
			super(id, subject, work, owner, owner, lastModifiedDate);
			super.setType(Instance.TYPE_IMAGE);
	}

	
}
