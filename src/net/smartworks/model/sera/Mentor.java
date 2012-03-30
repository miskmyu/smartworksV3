package net.smartworks.model.sera;

import net.smartworks.model.community.User;
import net.smartworks.util.SmartUtil;

public class Mentor extends User {

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
	public Mentor(){
		super();
	}
	public Mentor(String id, String name){
		super(id, name);
	}
}
