package net.smartworks.model.sera;

import net.smartworks.model.sera.info.SeraUserInfo;

public class SeraUserList{

	public static final int MAX_USER_LIST = 20;
	
	private int totalUsers;
	SeraUserInfo[] users;
		
	public int getTotalUsers() {
		return totalUsers;
	}

	public void setTotalUsers(int totalUsers) {
		this.totalUsers = totalUsers;
	}

	public SeraUserInfo[] getUsers() {
		return users;
	}

	public void setUsers(SeraUserInfo[] users) {
		this.users = users;
	}

	public SeraUserList(){
		super();
	}
}