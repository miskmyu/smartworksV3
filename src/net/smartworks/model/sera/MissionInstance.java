package net.smartworks.model.sera;

import net.smartworks.model.community.User;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.WorkInstance;
import net.smartworks.model.work.Work;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartUtil;

public class MissionInstance extends WorkInstance {
		
	private int index;
	private String content;
	private LocalDate openDate;
	private LocalDate closeDate;
	private MissionInstance prevMission;
	private String[] missionClearers;
	private int starPoint;
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public MissionInstance getPrevMission() {
		return prevMission;
	}
	public void setPrevMission(MissionInstance prevMission) {
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
	
	public MissionInstance(){
		super();
		super.setType(Instance.TYPE_SERA_MISSION);
	}

	public MissionInstance(String id, String subject, Course course, User owner, LocalDate lastModifiedDate){
			super(id, subject, null, owner, owner, lastModifiedDate);
			super.setType(Instance.TYPE_SERA_MISSION);
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
