package net.smartworks.model.sera;

import net.smartworks.model.sera.info.SeraUserInfo;

public class MenteeInformList{

	public static final int MAX_ALL_USER_LIST = -1;
	public static final int MAX_USER_LIST = 20;
	public static final int TYPE_JOIN_REQUESTERS = 1;
	public static final int TYPE_MENTEES = 2;
	public static final int TYPE_NON_MENTEES = 3;
		
	private int totalJoinRequesters;
	private int totalMentees;
	private int totalNonMentees;
	
	private SeraUserInfo[] joinRequesters;
	private SeraUserInfo[] mentees;
	private SeraUserInfo[] nonMentees;

	public int getTotalJoinRequesters() {
		return totalJoinRequesters;
	}
	public void setTotalJoinRequesters(int totalJoinRequesters) {
		this.totalJoinRequesters = totalJoinRequesters;
	}
	public int getTotalMentees() {
		return totalMentees;
	}
	public void setTotalMentees(int totalMentees) {
		this.totalMentees = totalMentees;
	}
	public int getTotalNonMentees() {
		return totalNonMentees;
	}
	public void setTotalNonMentees(int totalNonMentees) {
		this.totalNonMentees = totalNonMentees;
	}
	public SeraUserInfo[] getJoinRequesters() {
		return joinRequesters;
	}
	public void setJoinRequesters(SeraUserInfo[] joinRequesters) {
		this.joinRequesters = joinRequesters;
	}
	public SeraUserInfo[] getMentees() {
		return mentees;
	}
	public void setMentees(SeraUserInfo[] mentees) {
		this.mentees = mentees;
	}
	public SeraUserInfo[] getNonMentees() {
		return nonMentees;
	}
	public void setNonMentees(SeraUserInfo[] nonMentees) {
		this.nonMentees = nonMentees;
	}

	public MenteeInformList(){
		super();
	}
}