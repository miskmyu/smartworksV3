/*	
 * $Id$
 * created by    : maninsoft
 * creation-date : 2011. 11. 2.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.sera.model;

import net.smartworks.server.engine.common.model.Cond;

public class MentorDetailCond extends Cond{

	public MentorDetailCond(){
		super();
	}
	private String mentorId;
	private String[] mentorIdIns;
	private String born;
	private String homeTown;
	private String living;
	private String family;
	private String educations;
	private String works;
	private String mentorHistory;
	private String menteeHistory;
	private String lectures;
	private String awards;
	private String etc;

	public String getMentorId() {
		return mentorId;
	}
	public void setMentorId(String mentorId) {
		this.mentorId = mentorId;
	}
	public String[] getMentorIdIns() {
		return mentorIdIns;
	}
	public void setMentorIdIns(String[] mentorIdIns) {
		this.mentorIdIns = mentorIdIns;
	}
	public String getBorn() {
		return born;
	}
	public void setBorn(String born) {
		this.born = born;
	}
	public String getHomeTown() {
		return homeTown;
	}
	public void setHomeTown(String homeTown) {
		this.homeTown = homeTown;
	}
	public String getFamily() {
		return family;
	}
	public void setFamily(String family) {
		this.family = family;
	}
	public String getLiving() {
		return living;
	}
	public void setLiving(String living) {
		this.living = living;
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
	public String getMentorHistory() {
		return mentorHistory;
	}
	public void setMentorHistory(String mentorHistory) {
		this.mentorHistory = mentorHistory;
	}
	public String getMenteeHistory() {
		return menteeHistory;
	}
	public void setMenteeHistory(String menteeHistory) {
		this.menteeHistory = menteeHistory;
	}
	public String getLectures() {
		return lectures;
	}
	public void setLectures(String lectures) {
		this.lectures = lectures;
	}
	public String getAwards() {
		return awards;
	}
	public void setAwards(String awards) {
		this.awards = awards;
	}
	public String getEtc() {
		return etc;
	}
	public void setEtc(String etc) {
		this.etc = etc;
	}
}