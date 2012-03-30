package net.smartworks.model.sera;

import net.smartworks.model.community.Community;
import net.smartworks.model.community.Group;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.sera.info.MissionInfo;
import net.smartworks.util.LocalDate;


public class Course extends Group {

	public static final String DEFAULT_COURSE_PICTURE  = "default_course_picture";
	public static final int MY_RUNNING_COURSE = 1;
	public static final int MY_ATTENDING_COURSE = 2;	

	private String object;
	private String[] categories;
	private String[] keywords;
	private int duration;
	private LocalDate start;
	private LocalDate end;
	private int maxMentees;
	private boolean autoApproval;
	private boolean payable;
	private int fee;
	private Team team;
	private MissionInfo[] missions;
	private int targetPoint;
	private int achievedPoint;
	
	public MissionInfo[] getMissions() {
		return missions;
	}
	public void setMissions(MissionInfo[] missions) {
		this.missions = missions;
	}
	public int getTargetPoint() {
		return targetPoint;
	}
	public void setTargetPoint(int targetPoint) {
		this.targetPoint = targetPoint;
	}
	public int getAchievedPoint() {
		return achievedPoint;
	}
	public void setAchievedPoint(int achievedPoint) {
		this.achievedPoint = achievedPoint;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public String[] getCategories() {
		return categories;
	}
	public void setCategories(String[] categories) {
		this.categories = categories;
	}
	public String[] getKeywords() {
		return keywords;
	}
	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public LocalDate getStart() {
		return start;
	}
	public void setStart(LocalDate start) {
		this.start = start;
	}
	public LocalDate getEnd() {
		return end;
	}
	public void setEnd(LocalDate end) {
		this.end = end;
	}
	public int getMaxMentees() {
		return maxMentees;
	}
	public void setMaxMentees(int maxMentees) {
		this.maxMentees = maxMentees;
	}
	public boolean isAutoApproval() {
		return autoApproval;
	}
	public void setAutoApproval(boolean autoApproval) {
		this.autoApproval = autoApproval;
	}
	public boolean isPayable() {
		return payable;
	}
	public void setPayable(boolean payable) {
		this.payable = payable;
	}
	public int getFee() {
		return fee;
	}
	public void setFee(int fee) {
		this.fee = fee;
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public String getOrgPicture() {
		if(this.getBigPictureName() == null || this.getBigPictureName().equals("")) {
			return Community.NO_PICTURE_PATH + Course.DEFAULT_COURSE_PICTURE + ".gif";
		}
		return getPath() + this.getBigPictureName();
	}
	public String getMidPicture() {
		if(this.getSmallPictureName() == null || this.getSmallPictureName().equals("")) {
			return Community.NO_PICTURE_PATH + Course.DEFAULT_COURSE_PICTURE + "_mid.gif";
		}
		return getPath() + this.getSmallPictureName();
	}
	public String getMinPicture() {
		if(this.getSmallPictureName() == null || this.getSmallPictureName().equals("")) {
			return Community.NO_PICTURE_PATH + Course.DEFAULT_COURSE_PICTURE + "_min.gif";
		}
		return getPath() + this.getSmallPictureName();
	}

	public Course(){
		super();
	}
	public Course(String id, String name){
		super(id, name);
	}
	public Course(String id, String name, UserInfo[] mentees){
		
		super(id, name, mentees);
	}
	public Course(String id, String name, UserInfo[] mentees, Mentor mentor){
		
		super(id, name, mentees, mentor);
	}
}
