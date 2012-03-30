package net.smartworks.model.sera;

import net.smartworks.model.community.User;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.WorkInstance;
import net.smartworks.model.work.Work;
import net.smartworks.util.LocalDate;

public class MissionInstance extends WorkInstance {
		
	private String content;
	private LocalDate plannedStartDate;
	private MissionInstance prevMission;
	
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
	public MissionInstance getPrevMission() {
		return prevMission;
	}
	public void setPrevMission(MissionInstance prevMission) {
		this.prevMission = prevMission;
	}

	public MissionInstance(){
		super();
		super.setType(Instance.TYPE_MISSION);
	}

	public MissionInstance(String id, String subject, Work work, User owner, LocalDate lastModifiedDate){
			super(id, subject, work, owner, owner, lastModifiedDate);
			super.setType(Instance.TYPE_MISSION);
	}
}