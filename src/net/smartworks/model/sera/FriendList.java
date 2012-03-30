package net.smartworks.model.sera;

import net.smartworks.model.community.info.UserInfo;

public class FriendList{

	public static final int MAX_BRIEF_FRIEND_LIST = 20;
	public static final int MAX_FRIEND_LIST = 20;
	
	private int totalFriends;
	UserInfo[] friends;
		
	public int getTotalFriends() {
		return totalFriends;
	}
	public void setTotalFriends(int totalFriends) {
		this.totalFriends = totalFriends;
	}
	public UserInfo[] getFriends() {
		return friends;
	}
	public void setFriends(UserInfo[] friends) {
		this.friends = friends;
	}

	public FriendList(){
		super();
	}
}