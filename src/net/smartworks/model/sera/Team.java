package net.smartworks.model.sera;

import net.smartworks.model.BaseObject;
import net.smartworks.model.sera.info.SeraUserInfo;
import net.smartworks.util.LocalDate;

public class Team extends BaseObject {

	private String courseId;
	private String desc;
	private LocalDate start;
	private LocalDate end;
	private int accessPolicy;
	private int maxMembers;
	private SeraUserInfo[] members;
	
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
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
	public int getAccessPolicy() {
		return accessPolicy;
	}
	public void setAccessPolicy(int accessPolicy) {
		this.accessPolicy = accessPolicy;
	}
	public int getMaxMembers() {
		return maxMembers;
	}
	public void setMaxMembers(int maxMembers) {
		this.maxMembers = maxMembers;
	}
	public SeraUserInfo[] getMembers() {
		return members;
	}
	public void setMembers(SeraUserInfo[] members) {
		this.members = members;
	}
	public Team(){
		super();
	}
	public Team(String id, String name){
		super(id, name);
	}

}