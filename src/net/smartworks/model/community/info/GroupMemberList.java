package net.smartworks.model.community.info;

import net.smartworks.model.sera.info.SeraUserInfo;

public class GroupMemberList{

	public static final int MAX_MEMBER_LIST = 20;

	public static final int TYPE_MEMBER_REQUESTERS = 1;
	public static final int TYPE_MEBERS = 2;
	
	private int totalRequesters;
	private int totalMembers;
	UserInfo[] requesters;
	UserInfo[] members;
		
	public int getTotalRequesters() {
		return totalRequesters;
	}
	public void setTotalRequesters(int totalRequesters) {
		this.totalRequesters = totalRequesters;
	}
	public int getTotalMembers() {
		return totalMembers;
	}
	public void setTotalMembers(int totalMembers) {
		this.totalMembers = totalMembers;
	}
	public UserInfo[] getRequesters() {
		return requesters;
	}
	public void setRequesters(UserInfo[] requesters) {
		this.requesters = requesters;
	}
	public UserInfo[] getMembers() {
		return members;
	}
	public void setMembers(UserInfo[] members) {
		this.members = members;
	}
	public GroupMemberList(){
		super();
	}
}