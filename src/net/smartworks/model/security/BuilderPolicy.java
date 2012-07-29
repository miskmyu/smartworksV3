package net.smartworks.model.security;

import net.smartworks.model.community.info.UserInfo;

public class BuilderPolicy {

	public final static int POLICY_TYPE_ADMINISTRATOR = 1;
	public final static int POLICY_TYPE_CUSTOM = 2;
	
	private boolean systemAdministratorChecked=true;
	private boolean customChecked;
	private UserInfo[] customs;

	public boolean isSystemAdministratorChecked() {
		return systemAdministratorChecked;
	}

	public void setSystemAdministratorChecked(boolean systemAdministratorChecked) {
		this.systemAdministratorChecked = systemAdministratorChecked;
	}

	public boolean isCustomChecked() {
		return customChecked;
	}

	public void setCustomChecked(boolean customChecked) {
		this.customChecked = customChecked;
	}

	public UserInfo[] getCustoms() {
		return customs;
	}

	public void setCustoms(UserInfo[] customs) {
		this.customs = customs;
	}

	public BuilderPolicy(){
		super();
	}
}
