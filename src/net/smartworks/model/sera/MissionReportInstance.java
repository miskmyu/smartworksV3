package net.smartworks.model.sera;

import net.smartworks.model.community.User;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.WorkInstance;
import net.smartworks.model.work.Work;
import net.smartworks.util.LocalDate;

public class MissionReportInstance extends WorkInstance {
		
	private int index;
	private String content;
	private LocalDate openDate;
	private LocalDate closeDate;
	private MissionReportInstance prevMission;
	private UserInfo[] missionClearers;
	
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
	public MissionReportInstance getPrevMission() {
		return prevMission;
	}
	public void setPrevMission(MissionReportInstance prevMission) {
		this.prevMission = prevMission;
	}
	public UserInfo[] getMissionClearers() {
		return missionClearers;
	}
	public void setMissionClearers(UserInfo[] missionClearers) {
		this.missionClearers = missionClearers;
	}
	public MissionReportInstance(){
		super();
		super.setType(Instance.TYPE_MISSION);
	}

	public MissionReportInstance(String id, String subject, Work work, User owner, LocalDate lastModifiedDate){
			super(id, subject, work, owner, owner, lastModifiedDate);
			super.setType(Instance.TYPE_MISSION);
	}
}