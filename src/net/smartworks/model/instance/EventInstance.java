package net.smartworks.model.instance;

import java.util.Date;

import net.smartworks.model.community.Community;
import net.smartworks.model.community.User;
import net.smartworks.model.work.Work;
import net.smartworks.util.LocalDate;

public class EventInstance extends WorkInstance {

	public final static int EVENT_STATUS_NORMAL			= 0;
	public final static int EVENT_STATUS_ALARM_START		= 1;
	public final static int EVENT_STATUS_ALARM_CONFIRMED	= 2;
	
	public final static int ALARM_NONE				= 0;
	public final static int ALARM_ON_TIME			= 1;
	public final static int ALARM_ON_ALARM_TIME		= 2;
	public final static int ALARM_ON_BOTH_TIME		= 3;

	private int 			views;
	private String			content;
	private Community[]		relatedUsers;
	private LocalDate		start;
	private LocalDate		end;
	private int				alarmOption=-1; 	// 시작시간에 한번만, 미리알림시간에 한번만, 미리알리시간과 시작시간에 한번씩, 미리알미리알미리알림없음 
	private Date			alarmTime;

	public int getViews() {
		return views;
	}
	public void setViews(int views) {
		this.views = views;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Community[] getRelatedUsers() {
		return relatedUsers;
	}
	public void setRelatedUsers(Community[] relatedUsers) {
		this.relatedUsers = relatedUsers;
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
	public int getAlarmOption() {
		return alarmOption;
	}
	public void setAlarmOption(int alarmOption) {
		this.alarmOption = alarmOption;
	}
	public Date getAlarmTime() {
		return alarmTime;
	}
	public void setAlarmTime(Date alarmTime) {
		this.alarmTime = alarmTime;
	}
	public EventInstance(){
		super();
		super.setType(Instance.TYPE_EVENT);
	}

	public EventInstance(String id, String subject, Work work, User owner, LocalDate lastModifiedDate){
			super(id, subject, work, owner, owner, lastModifiedDate);
			super.setType(Instance.TYPE_EVENT);
	}
}