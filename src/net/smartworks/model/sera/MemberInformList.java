package net.smartworks.model.sera;

import net.smartworks.model.sera.info.SeraUserInfo;

public class MemberInformList{

	public static final int MAX_MEMBER_LIST = 20;
	
	public static final int TYPE_MEMBERS = 1;
	public static final int TYPE_NON_MEMBERS = 2;
	public static final int TYPE_INVITED_MEMBERS = 3;
	
	private int totalMembers;
	private int totalInvitedMembers;
	private int totalNonMembers;
	SeraUserInfo[] members;
	SeraUserInfo[] invitedMembers;
	SeraUserInfo[] nonMembers;
		
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
	public int getTotalInvitedMembers() {
		return totalInvitedMembers;
	}
	public void setTotalInvitedMembers(int totalInvitedMembers) {
		this.totalInvitedMembers = totalInvitedMembers;
	}
	public SeraUserInfo[] getInvitedMembers() {
		return invitedMembers;
	}
	public void setInvitedMembers(SeraUserInfo[] invitedMembers) {
		this.invitedMembers = invitedMembers;
	}
	public MemberInformList(){
		super();
	}
}