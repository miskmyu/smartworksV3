package net.smartworks.model.sera;

import net.smartworks.model.sera.info.SeraUserInfo;

public class MemberInformList{

	public static final int MAX_MEMBER_LIST = 20;
	
	public static final int TYPE_MEMBER_REQUESTERS = 1;
	public static final int TYPE_MEMBERS = 2;
	public static final int TYPE_NON_MEMBERS = 3;
	
	private int totalRequesters;
	private int totalMembers;
	private int totalNonMembers;
	SeraUserInfo[] requesters;
	SeraUserInfo[] members;
	SeraUserInfo[] nonMembers;
		
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
	public int getTotalNonMembers() {
		return totalNonMembers;
	}
	public void setTotalNonMembers(int totalNonMembers) {
		this.totalNonMembers = totalNonMembers;
	}
	public SeraUserInfo[] getRequesters() {
		return requesters;
	}
	public void setRequesters(SeraUserInfo[] requesters) {
		this.requesters = requesters;
	}
	public SeraUserInfo[] getMembers() {
		return members;
	}
	public void setMembers(SeraUserInfo[] members) {
		this.members = members;
	}
	public SeraUserInfo[] getNonMembers() {
		return nonMembers;
	}
	public void setNonMembers(SeraUserInfo[] nonMembers) {
		this.nonMembers = nonMembers;
	}

	public MemberInformList(){
		super();
	}
}