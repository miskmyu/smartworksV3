package net.smartworks.model.sera;

import java.text.DecimalFormat;

import net.smartworks.model.community.Group;
import net.smartworks.model.community.User;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.sera.info.MissionInstanceInfo;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartUtil;


public class Course extends Group {

	public static final String NO_PICTURE_PATH = "sera/images/";
	public static final String DEFAULT_COURSE_PICTURE  = "default_course_picture";
	public static final int MY_ALL_COURSES = 0;
	public static final int MY_RUNNING_COURSE = 1;
	public static final int MY_ATTENDING_COURSE = 2;
	public static final int TYPE_ALL_COURSES = -1;
	public static final int TYPE_FAVORITE_COURSES = 11;
	public static final int TYPE_RECOMMENDED_COURSES = 12;
	public static final int TYPE_CLOSED_COURSES = 13;
	public static final int TYPE_CATEGORIES = 14;

	public static final int TYPE_COURSEPOINT_MEMBER = 1;
	public static final int TYPE_COURSEPOINT_CONTENT = 2;

	public static final int POINT_MEMBER = 5;
	public static final int POINT_CONTENT = 1;

	public static final int LIST_PAGE_SIZE = 20;

	private String object;
	private String[] categories;
	private String[] keywords;
	private int duration;
	private LocalDate closeDate;
	private int maxMentees;
	private boolean payable;
	private int fee;
	private Team team;
	private MissionInstanceInfo[] missions;
	private int lastMissionIndex=-1;
	private double starPoint;
	private int starPointUsers;
	private int likes;
	private boolean isJoinCourse;

	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public double getStarPoint() {
		return starPoint;
	}
	public void setStarPoint(double starPoint) {
		this.starPoint = starPoint;
	}
	public int getStarPointUsers() {
		return starPointUsers;
	}
	public void setStarPointUsers(int starPointUsers) {
		this.starPointUsers = starPointUsers;
	}
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
		if(SmartUtil.isBlankObject(super.getOpenDate()) || SmartUtil.isBlankObject(closeDate) || closeDate.getTime()<super.getOpenDate().getTime()) return 0;
		int point = (int)LocalDate.getDiffDate(super.getOpenDate(), closeDate)+1;
		return (point<0) ? 0 : point;
	}
	public int getAchievedPoint() {
		if(SmartUtil.isBlankObject(super.getOpenDate()) || SmartUtil.isBlankObject(closeDate) || closeDate.getTime()<super.getOpenDate().getTime() || super.getOpenDate().getTime()>(new LocalDate()).getTime()) return 0;
		int point = (int)LocalDate.getDiffDate(super.getOpenDate(), new LocalDate())+1;
		return (point<0) ? getTargetPoint() : point;
	}
	public double getAchievedRatio(){
		if(getTargetPoint()==0 || getAchievedPoint()==0) return 0;
		return (getAchievedPoint() * 100 / getTargetPoint());
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
	public boolean isJoinCourse() {
		return isJoinCourse;
	}
	public void setJoinCourse(boolean isJoinCourse) {
		this.isJoinCourse = isJoinCourse;
	}
	public boolean isMyRunningCourse(){
		return this.getLeader().getId().equals(SmartUtil.getCurrentUser().getId());
	}
	public boolean isMyAttendingCourse(){
		UserInfo[] members = this.getMembers();
		User currentUser = SmartUtil.getCurrentUser();
		if(SmartUtil.isBlankObject(members) || SmartUtil.isBlankObject(currentUser)) return false;
		for(int i=0; i<members.length; i++)
			if(members[i].getId().equals(currentUser.getId()))
				return true;
		return false;
	}
	public String getOrgPicture() {
		if(this.getBigPictureName() == null || this.getBigPictureName().equals("")) {
			return Course.NO_PICTURE_PATH + Course.DEFAULT_COURSE_PICTURE + ".gif";
		}
		return getPath() + this.getBigPictureName();
	}
	public String getMidPicture() {
		if(this.getSmallPictureName() == null || this.getSmallPictureName().equals("")) {
			return Course.NO_PICTURE_PATH + Course.DEFAULT_COURSE_PICTURE + ".gif";
		}
		return getPath() + this.getSmallPictureName();
	}
	public String getMinPicture() {
		if(this.getSmallPictureName() == null || this.getSmallPictureName().equals("")) {
			return Course.NO_PICTURE_PATH + Course.DEFAULT_COURSE_PICTURE + ".gif";
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
	
	public String getKeywordsAsCommaString(){
		String str = "";
		if(SmartUtil.isBlankObject(this.getKeywords())) return str;
		for(int i=0; i<this.getKeywords().length; i++)
			str = str + this.getKeywords()[i] + ((i==this.getKeywords().length-1) ? "" : ", ");
		return str;
	}
	
	public String getStarPointString(){
		DecimalFormat df = new DecimalFormat("#.#");
		return df.format(getStarPoint());
	}
}
