package net.smartworks.model.community.info;

import net.smartworks.model.community.User;
import net.smartworks.util.SmartUtil;

public class UserInfo extends WorkSpaceInfo {

	private String nickName;
	private int role = User.USER_ROLE_MEMBER;
	private DepartmentInfo department;
	private String position = "";
	private String phoneNo = "";
	private String cellPhoneNo = "";
	private boolean online;

	public String getNickName() {
		if(SmartUtil.isBlankObject(nickName)) return getName();
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public DepartmentInfo getDepartment() {
		return department;
	}
	public void setDepartment(DepartmentInfo department) {
		this.department = department;
	}
	public String getLongName(){
		return (SmartUtil.isBlankObject(position)) ? getNickName() : position + " " + getNickName(); 
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getCellPhoneNo() {
		return cellPhoneNo;
	}
	public void setCellPhoneNo(String cellPhoneNo) {
		this.cellPhoneNo = cellPhoneNo;
	}
	public boolean isOnline() {
		return online;
	}
	public void setOnline(boolean online) {
		this.online = online;
	}
	
	public UserInfo(){
		super();
	}
	public UserInfo(String id, String name){
		super(id, name);
	}

	public String getEmailAddressShown(){
		if(this.getLongName() == null) return super.getId();
		return this.getLongName() + "&lt;" + super.getId() + "&gt;";
	}

}