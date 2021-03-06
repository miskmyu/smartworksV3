package net.smartworks.model.community;

import net.smartworks.model.community.info.DepartmentInfo;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.security.SpacePolicy;
import net.smartworks.service.ISmartWorks;
import net.smartworks.util.SmartUtil;

public class Department extends WorkSpace {

	public static final String  DEFAULT_DEPART_PICTURE  = "default_depart_picture";
	public static final String DEPARTMENT_ID_PREFIX = "dept_";
	public static final String DEPARTMENT_ID_ROOT = "root";

	private String 	desc = null;
	private DepartmentInfo parent = null;
	private User	head = null;
	private DepartmentInfo[] children = null;
	private String fullpathName = "";
	private SpacePolicy boardWritePolicy=new SpacePolicy();
	private SpacePolicy boardEditPolicy=new SpacePolicy();
	private SpacePolicy eventWritePolicy=new SpacePolicy();
	private SpacePolicy eventEditPolicy=new SpacePolicy();

	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public DepartmentInfo getParent() {
		return parent;
	}
	public void setParent(DepartmentInfo parent) {
		this.parent = parent;
	}
	public User getHead() {
		return head;
	}
	public void setHead(User head) {
		this.head = head;
	}
	public int getNumberOfMember(){
		if(SmartUtil.isBlankObject(getMembers())) return 0;
		return getMembers().length;
	}
	public DepartmentInfo[] getChildren() {
		return children;
	}
	public void setChildren(DepartmentInfo[] children) {
		this.children = children;
	}
	public int getNumberOfSubDepartment(){
		if(SmartUtil.isBlankObject(this.children)) return 0;
		return this.children.length;
	}
	public int getSpaceType(){
		return ISmartWorks.SPACE_TYPE_DEPARTMENT;
	}	
	public String getFullpathName() {
		return fullpathName;
	}
	public void setFullpathName(String fullpathName) {
		this.fullpathName = fullpathName;
	}
	public SpacePolicy getBoardWritePolicy() {
		return boardWritePolicy;
	}
	public void setBoardWritePolicy(SpacePolicy boardWritePolicy) {
		this.boardWritePolicy = boardWritePolicy;
	}
	public SpacePolicy getBoardEditPolicy() {
		return boardEditPolicy;
	}
	public void setBoardEditPolicy(SpacePolicy boardEditPolicy) {
		this.boardEditPolicy = boardEditPolicy;
	}
	public SpacePolicy getEventWritePolicy() {
		return eventWritePolicy;
	}
	public void setEventWritePolicy(SpacePolicy eventWritePolicy) {
		this.eventWritePolicy = eventWritePolicy;
	}
	public SpacePolicy getEventEditPolicy() {
		return eventEditPolicy;
	}
	public void setEventEditPolicy(SpacePolicy eventEditPolicy) {
		this.eventEditPolicy = eventEditPolicy;
	}
	public Department(){
		super();
	}
	
	public Department(String id, String name){
		super(id, name);
	}
	
	public Department(String id, String name, UserInfo[] members){

		super(id, name);
		setMembers(members);
	}

	public Department(String id, String name, UserInfo[] members, User head){
		
		super(id, name);
		setMembers(members);
		this.head = head;
	}

	public DepartmentInfo getDepartmentInfo(){
		DepartmentInfo departmentInfo = new DepartmentInfo(getId(), getName());
		departmentInfo.setBigPictureName(getBigPictureName());
		departmentInfo.setDesc(getDesc());
		departmentInfo.setSmallPictureName(getSmallPictureName());
		departmentInfo.setFullpathName(getFullpathName());		
		return departmentInfo;
	}
	
	public boolean amIAdministrator(User currentUser){
		if(SmartUtil.isBlankObject(currentUser)) return false;
		if(currentUser.getUserLevel()>User.USER_LEVEL_INTERNAL_USER) return true;
		if(!SmartUtil.isBlankObject(this.head) && currentUser.getId().equals(head.getId())) return true;
		return false;
	}
}
