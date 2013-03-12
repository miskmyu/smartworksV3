package net.smartworks.server.engine.publishnotice.model;

import net.smartworks.server.engine.common.model.MisObject;
import net.smartworks.server.engine.common.util.CommonUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SpaceNotice extends MisObject {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(SpaceNotice.class);

	protected static final String PREFIX = "";
	private static final String NAME = CommonUtil.toName(SpaceNotice.class, PREFIX);
	
	private String workId;
	private String workSpaceType;
	private String workSpaceId;
	private String assignee;
	private String refType;
	private String refId;
	private String taskId;
	
	public String getWorkSpaceType() {
		return workSpaceType;
	}
	public void setWorkSpaceType(String workSpaceType) {
		this.workSpaceType = workSpaceType;
	}
	public String getWorkSpaceId() {
		return workSpaceId;
	}
	public void setWorkSpaceId(String workSpaceId) {
		this.workSpaceId = workSpaceId;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public String getRefType() {
		return refType;
	}
	public void setRefType(String refType) {
		this.refType = refType;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getWorkId() {
		return workId;
	}
	public void setWorkId(String workId) {
		this.workId = workId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

}
