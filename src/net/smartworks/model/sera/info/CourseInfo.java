package net.smartworks.model.sera.info;

import net.smartworks.model.community.info.GroupInfo;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.sera.Course;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartUtil;


public class CourseInfo extends GroupInfo{

	public static final String DEFAULT_COURSE_PICTURE  = "default_course_picture";

	private UserInfo owner;
	private UserInfo leader;
	private LocalDate openDate;
	private LocalDate closeDate;
	private int numberOfGroupMember;
	
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
	public LocalDate getCloseDate() {
		return closeDate;
	}
	public void setCloseDate(LocalDate closeDate) {
		this.closeDate = closeDate;
	}
	public int getNumberOfGroupMember() {
		return numberOfGroupMember;
	}
	public void setNumberOfGroupMember(int numberOfGroupMember) {
		this.numberOfGroupMember = numberOfGroupMember;
	}
	public String getBriefDesc(){
		return getDesc();
	}	
	public int getTargetPoint() {
		if(SmartUtil.isBlankObject(openDate) || SmartUtil.isBlankObject(closeDate) || closeDate.getTime()<openDate.getTime()) return -1;
		return (int)LocalDate.getDiffDate(openDate, closeDate)+1;
	}
	public int getAchievedPoint() {
		if(SmartUtil.isBlankObject(openDate) || SmartUtil.isBlankObject(closeDate) || closeDate.getTime()<openDate.getTime() || openDate.getTime()>(new LocalDate()).getTime()) return 0;
		return (int)LocalDate.getDiffDate(openDate, new LocalDate());
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

	public CourseInfo(){
		super();
	}
	public CourseInfo(String id, String name){
		super(id, name);
	}
}
