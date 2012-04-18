package net.smartworks.model.sera.info;

import net.smartworks.model.community.info.UserInfo;

public class SeraUserInfo extends UserInfo{

	private boolean isFriend;
	private String goal;
	
	public boolean isFriend() {
		return isFriend;
	}
	public void setFriend(boolean isFriend) {
		this.isFriend = isFriend;
	}
	public String getGoal() {
		return goal;
	}
	public void setGoal(String goal) {
		this.goal = goal;
	}
	
	public SeraUserInfo(){
		super();
	}
	public SeraUserInfo(String id, String name){
		super(id, name);
	}
}
