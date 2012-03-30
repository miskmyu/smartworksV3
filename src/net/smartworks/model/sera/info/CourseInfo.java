package net.smartworks.model.sera.info;

import net.smartworks.model.community.Community;
import net.smartworks.model.community.Department;
import net.smartworks.model.community.Group;
import net.smartworks.model.community.User;
import net.smartworks.model.community.info.DepartmentInfo;
import net.smartworks.model.community.info.GroupInfo;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.sera.Course;
import net.smartworks.server.engine.security.model.Login;
import net.smartworks.util.LocalDate;


public class CourseInfo extends GroupInfo{

	public static final String DEFAULT_COURSE_PICTURE  = "default_course_picture";

	private UserInfo owner;
	private UserInfo leader;
	private LocalDate openDate;
	private int numberOfGroupMember;
	
	private int targetPoint;
	private int achievedPoint;
	private MissionInfo lastMission;
	
	public UserInfo getOwner() {
		return owner;
	}
	public void setOwner(UserInfo owner) {
		this.owner = owner;
	}
	public UserInfo getLeader() {
		return leader;
	}
	public void setLeader(UserInfo leader) {
		this.leader = leader;
	}
	public LocalDate getOpenDate() {
		return openDate;
	}
	public void setOpenDate(LocalDate openDate) {
		this.openDate = openDate;
	}
	public int getNumberOfGroupMember() {
		return numberOfGroupMember;
	}
	public void setNumberOfGroupMember(int numberOfGroupMember) {
		this.numberOfGroupMember = numberOfGroupMember;
	}
	public MissionInfo getLastMission() {
		return lastMission;
	}
	public void setLastMission(MissionInfo lastMission) {
		this.lastMission = lastMission;
	}
	public String getBriefDesc(){
		return getDesc();
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

	public CourseInfo(){
		super();
	}
	public CourseInfo(String id, String name){
		super(id, name);
	}
}
