package net.smartworks.model.sera;

import net.smartworks.model.community.Community;
import net.smartworks.model.community.Group;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.sera.info.MissionInstanceInfo;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartUtil;


public class Course extends Group {

	public static final String DEFAULT_COURSE_PICTURE  = "default_course_picture";
	public static final int MY_ALL_COURSES = 0;
	public static final int MY_RUNNING_COURSE = 1;
	public static final int MY_ATTENDING_COURSE = 2;

	private String object;
	private String[] categories;
	private String[] keywords;
	private int duration;
	private LocalDate openDate;
	private LocalDate closeDate;
	private int maxMentees;
	private boolean payable;
	private int fee;
	private Team team;
	private MissionInstanceInfo[] missions;
	private int lastMissionIndex=-1;
	
	public int getLastMissionIndex() {
		return lastMissionIndex;
	}
	public void setLastMissionIndex(int lastMissionIndex) {
		this.lastMissionIndex = lastMissionIndex;
	}
	public MissionInstanceInfo[] getMissions() {
		return missions;
	}
	public void setMissions(MissionInstanceInfo[] missions) {
		this.missions = missions;
	}
	public int getTargetPoint() {
		if(SmartUtil.isBlankObject(openDate) || SmartUtil.isBlankObject(closeDate) || closeDate.getTime()<openDate.getTime()) return -1;
		return (int)LocalDate.getDiffDate(openDate, closeDate)+1;
	}
	public int getAchievedPoint() {
		if(SmartUtil.isBlankObject(openDate) || SmartUtil.isBlankObject(closeDate) || closeDate.getTime()<openDate.getTime() || openDate.getTime()>(new LocalDate()).getTime()) return 0;
		return (int)LocalDate.getDiffDate(openDate, new LocalDate());
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
	public LocalDate getOpenDate() {
		return openDate;
	}
	public void setOpenDate(LocalDate openDate) {
		this.openDate = openDate;
	}
	public LocalDate getCloseDate() {
		return closeDate;
	}
	public void setCloseDate(LocalDate closeDate) {
		this.closeDate = closeDate;
	}
	public int getMaxMentees() {
		return maxMentees;
	}
	public void setMaxMentees(int maxMentees) {
		this.maxMentees = maxMentees;
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
