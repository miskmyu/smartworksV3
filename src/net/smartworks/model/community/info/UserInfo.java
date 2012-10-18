package net.smartworks.model.community.info;

import net.smartworks.model.community.Department;
import net.smartworks.model.community.User;
import net.smartworks.util.SmartMessage;
import net.smartworks.util.SmartUtil;

public class UserInfo extends WorkSpaceInfo {

	private String nickName;
	private int role = User.USER_ROLE_MEMBER;
	private DepartmentInfo department;
	private DepartmentInfo[] departments;
	private String position = "";
	private String phoneNo = "";
	private String cellPhoneNo = "";
	private boolean online;
	private boolean useSignPicture;
	private String signPicture;

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
	public DepartmentInfo[] getDepartments() {
		return departments;
	}
	public void setDepartments(DepartmentInfo[] departments) {
		this.departments = departments;
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
	public boolean isUseSignPicture() {
		return useSignPicture;
	}
	public void setUseSignPicture(boolean useSignPicture) {
		this.useSignPicture = useSignPicture;
	}
	public String getSignPicture() {
		return signPicture;
	}
	public void setSignPicture(String signPicture) {
		this.signPicture = signPicture;
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

	public boolean isAdjunctUser(){
		if(SmartUtil.isBlankObject(this.departments)) return false;
		if(this.departments.length>1) return true;
		return false;
	}
	
	public String getFullDepartment(){
		if(!this.isAdjunctUser() || SmartUtil.isBlankObject(departments)) return this.department.getName();
		String fullDepartment = "";
		for(int i=0; i<departments.length; i++){
			fullDepartment = fullDepartment + departments[i].getName() + "(" + SmartMessage.getString("organization.title.adjunct") + ")" + ((i==departments.length-1) ? "" : ", ");
		}
		return fullDepartment;
	}	
	
	public User getUser(){
		User user = new User();
		user.setBigPictureName(this.getBigPictureName());
		user.setCellPhoneNo(this.getCellPhoneNo());
		user.setId(this.getId());
		user.setName(this.getName());
		user.setPosition(this.getPosition());
		user.setRole(this.getRole());
		user.setSignPicture(this.getSignPicture());
		user.setSmallPictureName(this.getSmallPictureName());
		return user;
	}
}