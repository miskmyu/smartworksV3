package net.smartworks.model.sera;

import net.smartworks.model.sera.info.SeraUserInfo;

public class FriendList{

	public static final int MAX_BRIEF_FRIEND_LIST = 20;
	public static final int MAX_FRIEND_LIST = 20;
	
	private int totalFriends;
	SeraUserInfo[] friends;
		
	public int getTotalFriends() {
		return totalFriends;
	}
	public void setTotalFriends(int totalFriends) {
		this.totalFriends = totalFriends;
	}
	public SeraUserInfo[] getFriends() {
		return friends;
	}
	public void setFriends(SeraUserInfo[] friends) {
		this.friends = friends;
	}
	public FriendList(){
		super();
	}
}