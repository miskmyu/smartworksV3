package net.smartworks.model.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.smartworks.model.community.info.CommunityInfo;
import net.smartworks.model.community.info.DepartmentInfo;
import net.smartworks.model.community.info.GroupInfo;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.service.ICommunityService;
import net.smartworks.util.SmartUtil;

@Component
public class EditPolicy {

	private static ICommunityService communityService;

	@Autowired(required=true)
	public void setCommunityService(ICommunityService communityService) {
		EditPolicy.communityService = communityService;
	}

	public final static int LEVEL_PRIVATE = 1;
	public final static int LEVEL_CUSTOM = 2;
	public final static int LEVEL_PUBLIC = 3;
	public final static int LEVEL_DEFAULT = LEVEL_PRIVATE;
	
	private int level = LEVEL_DEFAULT;
	private CommunityInfo[] communitiesToEdit;
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}	
	public CommunityInfo[] getCommunitiesToEdit() {
		return communitiesToEdit;
	}
	public void setCommunitiesToEdit(CommunityInfo[] communitiesToEdit) {
		this.communitiesToEdit = communitiesToEdit;
	}
	public EditPolicy(){
		super();
	}	
	public EditPolicy(int level){
		super();
		this.level = level;
	}

	public boolean isEditableForMe(String ownerId) throws Exception{

		if(!CommonUtil.isEmpty(ownerId)) {
			if(ownerId.equals(SmartUtil.getCurrentUser().getId()))
				return true;
		}
		if(this.level == EditPolicy.LEVEL_PUBLIC) {
			return true;
		} else if(this.level == EditPolicy.LEVEL_CUSTOM) {
			if(SmartUtil.isBlankObject(communitiesToEdit)) return false;
			DepartmentInfo[] myDepartments = null;
			GroupInfo[] myGroups = null;
			myDepartments = communityService.getMyDepartments();
			myGroups = communityService.getMyGroups();

			for(CommunityInfo community : communitiesToEdit) {
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
