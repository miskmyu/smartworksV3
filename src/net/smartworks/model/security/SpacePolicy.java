package net.smartworks.model.security;

import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.community.info.WorkSpaceInfo;

public class SpacePolicy {

	public final static int POLICY_TYPE_OWNER = 1;
	public final static int POLICY_TYPE_LEADER = 2;
	public final static int POLICY_TYPE_ADMINISTRATOR = 3;
	public final static int POLICY_TYPE_MEMBERS = 4;
	public final static int POLICY_TYPE_CUSTOM = 5;
	
	private boolean ownerChecked;
	private boolean leaderChecked;
	private boolean systemAdministratorChecked;
	private boolean membersChecked;
	private boolean customChecked;
	private WorkSpaceInfo[] customs;

	public boolean isOwnerChecked() {
		return ownerChecked;
	}

	public void setOwnerChecked(boolean ownerChecked) {
		this.ownerChecked = ownerChecked;
	}

	public boolean isLeaderChecked() {
		return leaderChecked;
	}

	public void setLeaderChecked(boolean leaderChecked) {
		this.leaderChecked = leaderChecked;
	}

	public boolean isSystemAdministratorChecked() {
		return systemAdministratorChecked;
	}

	public void setSystemAdministratorChecked(boolean systemAdministratorChecked) {
		this.systemAdministratorChecked = systemAdministratorChecked;
	}

	public boolean isMembersChecked() {
		return membersChecked;
	}

	public void setMembersChecked(boolean membersChecked) {
		this.membersChecked = membersChecked;
	}

	public boolean isCustomChecked() {
		return customChecked;
	}

	public void setCustomChecked(boolean customChecked) {
		this.customChecked = customChecked;
	}

	public WorkSpaceInfo[] getCustoms() {
		return customs;
	}

	public void setCustoms(WorkSpaceInfo[] customs) {
		this.customs = customs;
	}

	public SpacePolicy(){
		super();
	}
}
