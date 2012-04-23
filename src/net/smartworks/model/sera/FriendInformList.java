package net.smartworks.model.sera;

import net.smartworks.model.sera.info.SeraUserInfo;

public class FriendInformList{

	public static final int MAX_BRIEF_FRIEND_LIST = 20;
	public static final int MAX_FRIEND_LIST = 20;
	
	public static final int TYPE_FRIEND_REQUESTERS = 1;
	public static final int TYPE_FRIENDS = 2;
	public static final int TYPE_NON_FRIENDS = 3;
	
	private int totalRequesters;
	private int totalFriends;
	private int totalNonFriends;
	SeraUserInfo[] requesters;
	SeraUserInfo[] friends;
	SeraUserInfo[] nonFriends;
		
	public int getTotalRequesters() {
		return totalRequesters;
	}
	public void setTotalRequesters(int totalRequesters) {
		this.totalRequesters = totalRequesters;
	}
	public int getTotalNonFriends() {
		return totalNonFriends;
	}
	public void setTotalNonFriends(int totalNonFriends) {
		this.totalNonFriends = totalNonFriends;
	}
	public SeraUserInfo[] getRequesters() {
		return requesters;
	}
	public void setRequesters(SeraUserInfo[] requesters) {
		this.requesters = requesters;
	}
	public SeraUserInfo[] getNonFriends() {
		return nonFriends;
	}
	public void setNonFriends(SeraUserInfo[] nonFriends) {
		this.nonFriends = nonFriends;
	}
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
	public FriendInformList(){
		super();
	}
}