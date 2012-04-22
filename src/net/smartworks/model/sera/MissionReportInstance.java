package net.smartworks.model.sera;

import net.smartworks.model.community.User;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.WorkInstance;
import net.smartworks.model.work.Work;
import net.smartworks.util.LocalDate;

public class MissionReportInstance extends NoteInstance {
		
	private MissionInstance mission;
	private double starPoint;
	
	public MissionInstance getMission() {
		return mission;
	}
	public void setMission(MissionInstance mission) {
		this.mission = mission;
	}
	public double getStarPoint() {
		return starPoint;
	}
	public void setStarPoint(double starPoint) {
		this.starPoint = starPoint;
	}

	public MissionReportInstance(){
		super();
		super.setType(Instance.TYPE_SERA_MISSION_REPORT);
	}

	public MissionReportInstance(String id, String subject, User owner, LocalDate lastModifiedDate){
			super(id, subject, owner, lastModifiedDate);
			super.setType(Instance.TYPE_SERA_MISSION_REPORT);
	}
}