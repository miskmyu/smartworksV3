package net.smartworks.model.community.info;

import net.smartworks.util.LocalDate;

public class DepartmentInfo extends WorkSpaceInfo {

	private String desc;
	private String fullpathName = "";
	private UserInfo head = null;
	private int numberOfMembers;
	private int numberOfDescendents;
	private UserInfo lastModificationUser;
	private LocalDate lastModificationDate;
		
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getFullpathName() {
		return fullpathName;
	}
	public void setFullpathName(String fullpathName) {
		this.fullpathName = fullpathName;
	}
	public UserInfo getHead() {
		return head;
	}
	public void setHead(UserInfo head) {
		this.head = head;
	}
	public int getNumberOfMembers() {
		return numberOfMembers;
	}
	public void setNumberOfMembers(int numberOfMembers) {
		this.numberOfMembers = numberOfMembers;
	}
	public int getNumberOfDescendents() {
		return numberOfDescendents;
	}
	public void setNumberOfDescendents(int numberOfDescendents) {
		this.numberOfDescendents = numberOfDescendents;
	}
	public UserInfo getLastModificationUser() {
		return lastModificationUser;
	}
	public void setLastModificationUser(UserInfo lastModificationUser) {
		this.lastModificationUser = lastModificationUser;
	}
	public LocalDate getLastModificationDate() {
		return lastModificationDate;
	}
	public void setLastModificationDate(LocalDate lastModificationDate) {
		this.lastModificationDate = lastModificationDate;
	}
	public DepartmentInfo(){
		super();
	}
	public DepartmentInfo(String id, String name){
		super(id, name);
	}
	public DepartmentInfo(String id, String name, String desc){
		super(id, name);
		this.desc = desc;
	}

}