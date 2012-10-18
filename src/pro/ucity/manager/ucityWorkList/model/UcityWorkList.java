/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 9. 27.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package pro.ucity.manager.ucityWorkList.model;

import java.util.Date;

import net.smartworks.server.engine.common.model.MisObject;
import net.smartworks.server.engine.common.util.CommonUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UcityWorkList  extends MisObject{

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(UcityWorkList.class);
	
	private static final String NAME = CommonUtil.toName(UcityWorkList.class, PREFIX);
	
	private String prcInstId;
	private String packageId;
	private String status;
	private String title;
	private String runningTaskId;
	private String runningTaskName;

	private String serviceName;
	private String eventName;
	private String type;
	private String externalDisplay;
	private boolean isSms;
	private String eventPlace;
	private String eventId;
	private Date eventTime;
	
	public String getPrcInstId() {
		return prcInstId;
	}
	public void setPrcInstId(String prcInstId) {
		this.prcInstId = prcInstId;
	}
	public String getPackageId() {
		return packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getRunningTaskId() {
		return runningTaskId;
	}
	public void setRunningTaskId(String runningTaskId) {
		this.runningTaskId = runningTaskId;
	}
	public String getRunningTaskName() {
		return runningTaskName;
	}
	public void setRunningTaskName(String runningTaskName) {
		this.runningTaskName = runningTaskName;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getExternalDisplay() {
		return externalDisplay;
	}
	public void setExternalDisplay(String externalDisplay) {
		this.externalDisplay = externalDisplay;
	}
	public String getEventPlace() {
		return eventPlace;
	}
	public void setEventPlace(String eventPlace) {
		this.eventPlace = eventPlace;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public boolean isSms() {
		return isSms;
	}
	public boolean getIsSms() {
		return isSms;
	}
	public void setIsSms(boolean isSms) {
		this.isSms = isSms;
	}
	public Date getEventTime() {
		return eventTime;
	}
	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}
	
}
