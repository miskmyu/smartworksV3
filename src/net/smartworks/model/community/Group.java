package net.smartworks.model.community;

import net.smartworks.model.community.info.DepartmentInfo;
import net.smartworks.model.community.info.GroupInfo;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.community.info.WorkSpaceInfo;
import net.smartworks.model.security.SpacePolicy;
import net.smartworks.server.service.factory.SwServiceFactory;
import net.smartworks.service.ISmartWorks;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartUtil;


public class Group extends WorkSpace {

	public static final String DEFAULT_GROUP_PICTURE  = "default_group_picture";
	public static final String GROUP_ID_PREFIX = "group_";

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
	private User	owner = null;
	private LocalDate openDate = null;
	private int numberOfGroupMember = 0;
	private String type = GROUP_TYPE_DEFAULT;
	private boolean autoApproval=true;
	private int maxMembers=MAX_MEMBERS_UNLIMITED;
	private SpacePolicy invitableMembers=new SpacePolicy();
	private SpacePolicy boardWritePolicy=new SpacePolicy();
	private SpacePolicy boardEditPolicy=new SpacePolicy();
	private SpacePolicy eventWritePolicy=new SpacePolicy();
	private SpacePolicy eventEditPolicy=new SpacePolicy();
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
	public UserInfo[] getJoinRequesters() {
		return joinRequesters;
	}
	public void setJoinRequesters(UserInfo[] joinRequesters) {
		this.joinRequesters = joinRequesters;
	}
	public Group(){
		super();
	}
	public Group(String id, String name){
		super(id, name);
	}
	public Group(String id, String name, UserInfo[] members){
		
		super(id, name);
		setMembers(members);
	}
	public Group(String id, String name, UserInfo[] members, User leader){
		
		super(id, name);
		setMembers(members);
		this.leader = leader;
	}

	public GroupInfo getGroupInfo(){
		GroupInfo groupInfo = new GroupInfo(getId(), getName());
		groupInfo.setBigPictureName(getBigPictureName());
		groupInfo.setDesc(getDesc());
		groupInfo.setSmallPictureName(getSmallPictureName());
		return groupInfo;
	}
	
	public boolean amIInvitableMember() throws Exception{
		User currentUser = SmartUtil.getCurrentUser();
		if(amIGroupLeader(currentUser)) return true;
		if(SmartUtil.isBlankObject(invitableMembers)) return false;
		if(invitableMembers.isLeaderChecked() && amIGroupLeader(currentUser)) return true;
		else if(invitableMembers.isMembersChecked() && this.amIMember()) return true;
		else if(invitableMembers.isCustomChecked() && !SmartUtil.isBlankObject(invitableMembers.getCustoms())){
			GroupInfo[] myGroup = SwServiceFactory.getInstance().getCommunityService().getMyGroups();
			for(WorkSpaceInfo custom : invitableMembers.getCustoms()) {
				if (custom instanceof UserInfo) {
					if(custom.getId().equals(currentUser.getId()))
						return true;
				} else if (custom instanceof DepartmentInfo) {
					if(custom.getId().equals(currentUser.getDepartmentId()))
						return true;
				} else if (custom instanceof GroupInfo) {
					for (int i = 0; i < myGroup.length; i++) {
						if (myGroup[i].getId().equals(custom.getId()))
							return true;
					}
				}
			}
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
	
	public boolean amIAdministrator(User currentUser){
		if(SmartUtil.isBlankObject(currentUser)) return false;
		if(currentUser.getUserLevel()>User.USER_LEVEL_INTERNAL_USER) return true;
		if(!SmartUtil.isBlankObject(this.leader) && currentUser.getId().equals(leader.getId())) return true;
		return false;
	}
	
	public boolean amIGroupLeader(User currentUser){
		if(SmartUtil.isBlankObject(leader)) return false;
		if(leader.getId().equals(currentUser.getId())) return true;
		return false;
	}

}
