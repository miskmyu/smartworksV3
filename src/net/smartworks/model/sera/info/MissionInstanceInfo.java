package net.smartworks.model.sera.info;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.info.WorkInstanceInfo;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartUtil;

public class MissionInstanceInfo extends WorkInstanceInfo {

	private int index;
	private String briefContent;
	private String content;
	private String fileGroupId;
	private List<Map<String, String>> files;
	private LocalDate openDate;
	private LocalDate closeDate;
	private MissionInstanceInfo prevMission;
	private String[] missionClearers;
	private double starPoint;
	private int starPointUsers;

	public String getBriefContent() {
		return briefContent;
	}
	public void setBriefContent(String briefContent) {
		this.briefContent = briefContent;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFileGroupId() {
		return fileGroupId;
	}
	public void setFileGroupId(String fileGroupId) {
		this.fileGroupId = fileGroupId;
	}
	public List<Map<String, String>> getFiles() {
		return files;
	}
	public void setFiles(List<Map<String, String>> files) {
		this.files = files;
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
	public LocalDate getOpenLocalDate() {
		if(SmartUtil.isBlankObject(openDate)) return null;
		return new LocalDate(openDate.getLocalDate());
	}
	public void setOpenDate(LocalDate openDate) {
		this.openDate = openDate;
	}
	public LocalDate getCloseDate() {
		return closeDate;
	}
	public LocalDate getCloseLocalDate() {
		if(SmartUtil.isBlankObject(closeDate)) return null;
		return new LocalDate(closeDate.getLocalDate());
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
	public double getStarPoint() {
		return starPoint;
	}
	public void setStarPoint(double starPoint) {
		this.starPoint = starPoint;
	}
	public int getStarPointUsers() {
		return starPointUsers;
	}
	public void setStarPointUsers(int starPointUsers) {
		this.starPointUsers = starPointUsers;
	}
	public MissionInstanceInfo(){
		super();
		super.setType(Instance.TYPE_SERA_MISSION);
	}

	public MissionInstanceInfo(String id, String subject, CourseInfo course, UserInfo owner, LocalDate lastModifiedDate){
		super(id, subject,  owner, owner, lastModifiedDate);
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
	
	public String getStarPointString(){
		DecimalFormat df = new DecimalFormat("#.#");
		return df.format(getStarPoint());
	}	
}