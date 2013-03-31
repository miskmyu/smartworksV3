package net.smartworks.model.community.info;

import net.smartworks.model.community.User;
import net.smartworks.util.LocalDate;


public class GroupInfo extends WorkSpaceInfo {

	private String desc;
	private boolean	isPublic = false;
	private UserInfo leader = null;
	private UserInfo owner = null;
	private LocalDate openDate = null;
	private int numberOfGroupMember;
	private String type;
	private LocalDate createdDate;
	
	public boolean isPublic() {
		return isPublic;
	}
	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public UserInfo getLeader() {
		return leader;
	}
	public void setLeader(UserInfo leader) {
		this.leader = leader;
	}
	public UserInfo getOwner() {
		return owner;
	}
	public void setOwner(UserInfo owner) {
		this.owner = owner;
	}
	public LocalDate getOpenDate() {
		return openDate;
	}
	public void setOpenDate(LocalDate openDate) {
		this.openDate = openDate;
	}
	public int getNumberOfGroupMember() {
		return numberOfGroupMember;
	}
	public void setNumberOfGroupMember(int numberOfGroupMember) {
		this.numberOfGroupMember = numberOfGroupMember;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public LocalDate getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}
	public GroupInfo(){
		super();
	}
	public GroupInfo(String id, String name){
		super(id, name);
	}

}
