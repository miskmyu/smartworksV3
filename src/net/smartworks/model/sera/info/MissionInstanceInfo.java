package net.smartworks.model.sera.info;

import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.info.WorkInstanceInfo;
import net.smartworks.model.sera.MissionInstance;
import net.smartworks.util.LocalDate;

public class MissionInstanceInfo extends WorkInstanceInfo {

	private String content;
	private LocalDate plannedStartDate;
	private MissionInstanceInfo prevMission;	
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public LocalDate getPlannedStartDate() {
		return plannedStartDate;
	}
	public void setPlannedStartDate(LocalDate plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}
	public MissionInstanceInfo getPrevMission() {
		return prevMission;
	}
	public void setPrevMission(MissionInstanceInfo prevMission) {
		this.prevMission = prevMission;
	}

	public MissionInstanceInfo(){
		super();
		super.setType(Instance.TYPE_MISSION);
	}

	public MissionInstanceInfo(String id, String subject, UserInfo owner, LocalDate lastModifiedDate){
		super(id, subject,  owner, owner, lastModifiedDate);
		super.setType(Instance.TYPE_MISSION);
	}
}