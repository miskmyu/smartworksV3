/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 4. 13.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.sera.model;

import java.util.Date;

public class SeraUserDetail {
	
	private String userId;
	private String email;
	private Date birthday;
	private int sex = 0;
	private String goal;
	private String interests;
	private String educations;
	private String works;
	private String twUserId;
	private String twPassword;
	private String fbUserId;
	private String fbPassword;
	private String nickName;
	private String challengingTarget;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
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
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getChallengingTarget() {
		return challengingTarget;
	}
	public void setChallengingTarget(String challengingTarget) {
		this.challengingTarget = challengingTarget;
	}
	
}
