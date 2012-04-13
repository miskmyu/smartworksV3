package net.smartworks.model.security;

import net.smartworks.model.community.info.CommunityInfo;
import net.smartworks.model.community.info.DepartmentInfo;
import net.smartworks.model.community.info.GroupInfo;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.service.ICommunityService;
import net.smartworks.util.SmartUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccessPolicy {

	private static ICommunityService communityService;

	@Autowired(required=true)
	public void setCommunityService(ICommunityService communityService) {
		AccessPolicy.communityService = communityService;
	}

	public final static int LEVEL_PRIVATE = 1;
	public final static int LEVEL_CUSTOM = 2;
	public final static int LEVEL_PUBLIC = 3;
	public final static int LEVEL_DEFAULT = LEVEL_PUBLIC;

	public final static int TYPE_WORK = 4;
	public final static int TYPE_INSTANCE = 5;

	private int level = LEVEL_DEFAULT;
	private CommunityInfo[] communitiesToOpen;
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}	
	public CommunityInfo[] getCommunitiesToOpen() {
		return communitiesToOpen;
	}
	public void setCommunitiesToOpen(CommunityInfo[] communitiesToOpen) {
		this.communitiesToOpen = communitiesToOpen;
	}
	public AccessPolicy(){
		super();
	}
	
	public AccessPolicy(int level){
		super();
		this.level = level;
	}
	
	public boolean isAccessableForMe(String ownerId, String modifierId) throws Exception {

		if(this.level == AccessPolicy.LEVEL_PRIVATE) {
			if(!CommonUtil.isEmpty(ownerId)) {
				if(ownerId.equals(SmartUtil.getCurrentUser().getId()))
					return true;
			}
			if(!CommonUtil.isEmpty(modifierId)) {
				if(modifierId.equals(SmartUtil.getCurrentUser().getId()))
					return true;
			}
		} else if(this.level == AccessPolicy.LEVEL_PUBLIC) {
			return true;
		} else if(this.level == AccessPolicy.LEVEL_CUSTOM) {
			if(SmartUtil.isBlankObject(communitiesToOpen)) return false;
			DepartmentInfo[] myDepartments = null;
			GroupInfo[] myGroups = null;
			myDepartments = communityService.getMyDepartments();
			myGroups = communityService.getMyGroups();

			for(CommunityInfo community : communitiesToOpen) {
				if(community.getClass().equals(UserInfo.class) && community.getId().equals(SmartUtil.getCurrentUser().getId())) {
					return true;
				} else if(community.getClass().equals(DepartmentInfo.class) && !SmartUtil.isBlankObject(myDepartments)) {
					for(DepartmentInfo department : myDepartments)
						if(department.getId().equals(community.getId())) return true;
				} else if(community.getClass().equals(GroupInfo.class) && !SmartUtil.isBlankObject(myGroups)) {
					for(GroupInfo group : myGroups)
						if(group.getId().equals(community.getId())) return true;
				}
			}
		}
		return false;
	}
}
