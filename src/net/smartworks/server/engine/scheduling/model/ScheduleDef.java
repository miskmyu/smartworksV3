/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 10. 24.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.scheduling.model;

import net.smartworks.server.engine.common.model.MisObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ScheduleDef extends MisObject {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(ScheduleDef.class);
	
	private String objId;
	private String targetPackageType;
	private String targetPackageId;
	private String targetFormId;
	private String targetFieldId;
	private String status;
	private String name;
	private String groupName;
	private String jobName;
	private String triggerName;
	private String targetClass;
	private String cronExpression;
	private String isAutoStart;
	
	public String getObjId() {
		return objId;
	}
	public void setObjId(String objId) {
		this.objId = objId;
	}
	public String getTargetPackageType() {
		return targetPackageType;
	}
	public void setTargetPackageType(String targetPackageType) {
		this.targetPackageType = targetPackageType;
	}
	public String getTargetPackageId() {
		return targetPackageId;
	}
	public void setTargetPackageId(String targetPackageId) {
		this.targetPackageId = targetPackageId;
	}
	public String getTargetFormId() {
		return targetFormId;
	}
	public void setTargetFormId(String targetFormId) {
		this.targetFormId = targetFormId;
	}
	public String getTargetFieldId() {
		return targetFieldId;
	}
	public void setTargetFieldId(String targetFieldId) {
		this.targetFieldId = targetFieldId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getTriggerName() {
		return triggerName;
	}
	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}
	public String getTargetClass() {
		return targetClass;
	}
	public void setTargetClass(String targetClass) {
		this.targetClass = targetClass;
	}
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public String getIsAutoStart() {
		return isAutoStart;
	}
	public void setIsAutoStart(String isAutoStart) {
		this.isAutoStart = isAutoStart;
	}
}
