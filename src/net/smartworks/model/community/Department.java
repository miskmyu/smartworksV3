package net.smartworks.model.community;

import net.smartworks.model.community.info.DepartmentInfo;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.security.SpacePolicy;
import net.smartworks.service.ISmartWorks;
import net.smartworks.util.SmartUtil;

public class Department extends WorkSpace {

	public static final String  DEFAULT_DEPART_PICTURE  = "default_depart_picture";

	private String 	desc = null;
	private DepartmentInfo parent = null;
	private User	head = null;
	private UserInfo[]	members = null;
	private DepartmentInfo[] children = null;
	private String fullpathName = "";
	private SpacePolicy invitableMembers=new SpacePolicy();
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
	public UserInfo[] getMembers() {
		return members;
	}
	public void setMembers(UserInfo[] members) {
		this.members = members;
	}
	public int getNumberOfMember(){
		if(SmartUtil.isBlankObject(this.members)) return 0;
		return this.members.length;
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
	public SpacePolicy getInvitableMembers() {
		return invitableMembers;
	}
	public void setInvitableMembers(SpacePolicy invitableMembers) {
		this.invitableMembers = invitableMembers;
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
		this.members = members;
	}

	public Department(String id, String name, UserInfo[] members, User head){
		
		super(id, name);
		this.members = members;
		this.head = head;
	}

	public DepartmentInfo getDepartmentInfo(){
		DepartmentInfo departmentInfo = new DepartmentInfo(getId(), getName());
		departmentInfo.setBigPictureName(getBigPictureName());
		departmentInfo.setDesc(getDesc());
		departmentInfo.setSmallPictureName(getSmallPictureName());
		return departmentInfo;
	}
	
	public boolean amIAdministrator(User currentUser){
		if(SmartUtil.isBlankObject(currentUser)) return false;
		if(currentUser.getUserLevel()>User.USER_LEVEL_INTERNAL_USER) return true;
		if(!SmartUtil.isBlankObject(this.head) && currentUser.getId().equals(head.getId())) return true;
		return false;
	}
}
