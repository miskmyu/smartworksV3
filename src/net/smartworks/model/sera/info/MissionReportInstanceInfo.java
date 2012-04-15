package net.smartworks.model.sera.info;

import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.info.WorkInstanceInfo;
import net.smartworks.model.sera.MissionInstance;
import net.smartworks.util.LocalDate;

public class MissionReportInstanceInfo extends NoteInstanceInfo {

	private MissionInstanceInfo mission;
	private int starPoint;
	
	public MissionInstanceInfo getMission() {
		return mission;
	}
	public void setMission(MissionInstanceInfo mission) {
		this.mission = mission;
	}
	public int getStarPoint() {
		return starPoint;
	}
	public void setStarPoint(int starPoint) {
		this.starPoint = starPoint;
	}
	
	public MissionReportInstanceInfo(){
		super();
		super.setType(Instance.TYPE_SERA_MISSION_REPORT);
	}

	public MissionReportInstanceInfo(String id, String subject, UserInfo owner, LocalDate lastModifiedDate){
		super(id, subject, owner, lastModifiedDate);
		super.setType(Instance.TYPE_SERA_MISSION_REPORT);
	}
}