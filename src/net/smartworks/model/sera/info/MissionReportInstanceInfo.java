package net.smartworks.model.sera.info;

import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.info.WorkInstanceInfo;
import net.smartworks.model.sera.MissionInstance;
import net.smartworks.util.LocalDate;

public class MissionReportInstanceInfo extends WorkInstanceInfo {

	private MissionInstanceInfo mission;
	private String content;
	private int starPoint;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
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
		super.setType(Instance.TYPE_MISSION);
	}

	public MissionReportInstanceInfo(String id, String subject, UserInfo owner, LocalDate lastModifiedDate){
		super(id, subject,  owner, owner, lastModifiedDate);
		super.setType(Instance.TYPE_MISSION);
	}
}