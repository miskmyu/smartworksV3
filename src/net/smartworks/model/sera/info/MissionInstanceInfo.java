package net.smartworks.model.sera.info;

import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.info.WorkInstanceInfo;
import net.smartworks.model.sera.MissionInstance;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartUtil;

public class MissionInstanceInfo extends WorkInstanceInfo {

	private int index;
	private String content;
	private LocalDate openDate;
	private LocalDate closeDate;
	private MissionInstanceInfo prevMission;
	private String[] missionClearers;
	private int starPoint;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
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
	public MissionInstanceInfo getPrevMission() {
		return prevMission;
	}
	public void setPrevMission(MissionInstanceInfo prevMission) {
		this.prevMission = prevMission;
	}
	public String[] getMissionClearers() {
		return missionClearers;
	}
	public void setMissionClearers(String[] missionClearers) {
		this.missionClearers = missionClearers;
	}
	public int getStarPoint() {
		return starPoint;
	}
	public void setStarPoint(int starPoint) {
		this.starPoint = starPoint;
	}
	
	public MissionInstanceInfo(){
		super();
		super.setType(Instance.TYPE_MISSION);
	}

	public MissionInstanceInfo(String id, String subject, CourseInfo course, UserInfo owner, LocalDate lastModifiedDate){
		super(id, subject,  owner, owner, lastModifiedDate);
		super.setType(Instance.TYPE_MISSION);
		super.setWorkSpace(course);
	}
	
	public boolean isClearedByMe(){
		if(SmartUtil.isBlankObject(missionClearers)) return false;
		String currentUserId = SmartUtil.getCurrentUser().getId();
		for(int i=0; i<missionClearers.length; i++)
			if(currentUserId.equals(missionClearers[i]))
				return true;
		return false;
	}
}