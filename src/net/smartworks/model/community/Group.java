package net.smartworks.model.community;

import net.smartworks.model.community.info.GroupInfo;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.security.EditPolicy;
import net.smartworks.model.security.WritePolicy;
import net.smartworks.service.ISmartWorks;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartUtil;


public class Group extends WorkSpace {

	public static final String DEFAULT_GROUP_PICTURE  = "default_group_picture";
	public static final String GROUP_TYPE_OPEN = "Open";
	public static final String GROUP_TYPE_CLOSED = "Closed";
	public static final String GROUP_TYPE_DEFAULT = GROUP_TYPE_OPEN;
	public static final int MAX_MEMBERS_UNLIMITED = -1;
	//public static final String GROUP_STATUS_CONTINUE = "Continue";
	//public static final String GROUP_STATUS_SUSPEND = "Suspend";
	//public static final String GROUP_STATUS_DEFAULT = GROUP_STATUS_CONTINUE;

	private String	desc = null;
	private boolean	isPublic = false;
	//private boolean isContinue = false;
	private User	leader = null;
	private UserInfo[] 	members = null;
	private User	owner = null;
	private LocalDate openDate = null;
	private int numberOfGroupMember = 0;
	private String type = GROUP_TYPE_DEFAULT;
	private boolean autoApproval;
	private int maxMembers=MAX_MEMBERS_UNLIMITED;
	private UserInfo[] invitableMembers;
	private WritePolicy boardWritePolicy;
	private EditPolicy boardEditPolicy;
	private WritePolicy eventWritePolicy;
	private EditPolicy eventEditPolicy;
	private LocalDate createdDate;
	private UserInfo[] joinRequesters;

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public boolean isPublic() {
		return isPublic;
	}
	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
	public User getLeader() {
		return leader;
	}
	public void setLeader(User leader) {
		this.leader = leader;
	}
	public UserInfo[] getMembers() {
		return members;
	}
	public void setMembers(UserInfo[] members) {
		this.members = members;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
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
	public boolean isAutoApproval() {
		return autoApproval;
	}
	public void setAutoApproval(boolean autoApproval) {
		this.autoApproval = autoApproval;
	}

	public LocalDate getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}
	public int getSpaceType(){
		return ISmartWorks.SPACE_TYPE_GROUP;
	}
	
	public int getMaxMembers() {
		return maxMembers;
	}
	public void setMaxMembers(int maxMembers) {
		this.maxMembers = maxMembers;
	}
	public UserInfo[] getInvitableMembers() {
		return invitableMembers;
	}
	public void setInvitableMembers(UserInfo[] invitableMembers) {
		this.invitableMembers = invitableMembers;
	}
	public WritePolicy getBoardWritePolicy() {
		return boardWritePolicy;
	}
	public void setBoardWritePolicy(WritePolicy boardWritePolicy) {
		this.boardWritePolicy = boardWritePolicy;
	}
	public EditPolicy getBoardEditPolicy() {
		return boardEditPolicy;
	}
	public void setBoardEditPolicy(EditPolicy boardEditPolicy) {
		this.boardEditPolicy = boardEditPolicy;
	}
	public WritePolicy getEventWritePolicy() {
		return eventWritePolicy;
	}
	public void setEventWritePolicy(WritePolicy eventWritePolicy) {
		this.eventWritePolicy = eventWritePolicy;
	}
	public EditPolicy getEventEditPolicy() {
		return eventEditPolicy;
	}
	public void setEventEditPolicy(EditPolicy eventEditPolicy) {
		this.eventEditPolicy = eventEditPolicy;
	}
	public UserInfo[] getJoinRequesters() {
		return joinRequesters;
	}
	public void setJoinRequesters(UserInfo[] joinRequesters) {
		joinRequesters = joinRequesters;
	}
	public Group(){
		super();
	}
	public Group(String id, String name){
		super(id, name);
	}
	public Group(String id, String name, UserInfo[] members){
		
		super(id, name);
		this.members = members;
	}
	public Group(String id, String name, UserInfo[] members, User leader){
		
		super(id, name);
		this.members = members;
		this.leader = leader;
	}

	public GroupInfo getGroupInfo(){
		GroupInfo groupInfo = new GroupInfo(getId(), getName());
		groupInfo.setBigPictureName(getBigPictureName());
		groupInfo.setDesc(getDesc());
		groupInfo.setSmallPictureName(getSmallPictureName());
		return groupInfo;
	}
	
	public boolean amIInvitableMember(){
		if(SmartUtil.isBlankObject(invitableMembers)) return false;
		for(UserInfo member : invitableMembers){
			if(member.getId().equals(SmartUtil.getCurrentUser().getId()))
				return true;
		}
		return false;
	}
	
	public boolean amIMember(){
		if(SmartUtil.isBlankObject(members)) return false;
		for(UserInfo member : members){
			if(member.getId().equals(SmartUtil.getCurrentUser().getId()))
				return true;
		}
		return false;		
	}
	
	public boolean amIJoinRequester(){
		if(SmartUtil.isBlankObject(joinRequesters)) return false;
		for(UserInfo member : joinRequesters){
			if(member.getId().equals(SmartUtil.getCurrentUser().getId()))
				return true;
		}
		return false;				
	}
	
}
