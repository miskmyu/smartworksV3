package net.smartworks.model.sera;

import net.smartworks.model.community.User;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartUtil;

public class SeraUser extends User {

	public static final int SEX_MALE = 1;
	public static final int SEX_FEMALE = 2;
	
	private String email;
	private LocalDate birthday;
	private int sex;
	private String goal;
	private String interests;
	private String educations;
	private String works;
	private String twUserId;
	private String twPassword;
	private String fbUserId;
	private String fbPassword;

	public String getEmail() {
		if(SmartUtil.isBlankObject(email)) return getId();
		return this.email ;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public LocalDate getBirthday() {
		return birthday;
	}
	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getGoal() {
		return goal;
	}
	public void setGoal(String goal) {
		this.goal = goal;
	}
	public String getInterests() {
		return interests;
	}
	public void setInterests(String interests) {
		this.interests = interests;
	}
	public String getEducations() {
		return educations;
	}
	public void setEducations(String educations) {
		this.educations = educations;
	}
	public String getWorks() {
		return works;
	}
	public void setWorks(String works) {
		this.works = works;
	}
	public String getTwUserId() {
		return twUserId;
	}
	public void setTwUserId(String twUserId) {
		this.twUserId = twUserId;
	}
	public String getTwPassword() {
		return twPassword;
	}
	public void setTwPassword(String twPassword) {
		this.twPassword = twPassword;
	}
	public String getFbUserId() {
		return fbUserId;
	}
	public void setFbUserId(String fbUserId) {
		this.fbUserId = fbUserId;
	}
	public String getFbPassword() {
		return fbPassword;
	}
	public void setFbPassword(String fbPassword) {
		this.fbPassword = fbPassword;
	}

	public SeraUser(){
		super();
	}
	public SeraUser(String id, String name){
		super(id, name);
	}
}
