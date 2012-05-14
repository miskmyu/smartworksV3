/*	
 * $Id$
 * created by    : hsshin
 * creation-date : 2012. 4. 23.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.sera.model;

import net.smartworks.server.engine.common.model.MisObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CourseTeamUser extends MisObject {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(CourseTeamUser.class);

	public static final String JOINTYPE_INVITE = "I";
	public static final String JOINTYPE_REQUEST = "R";

	public static final String JOINSTATUS_COMPLETE = "C";
	public static final String JOINSTATUS_READY = "R";

	private String userId;
	private String joinType;
	private String joinStatus;

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getJoinType() {
		return joinType;
	}
	public void setJoinType(String joinType) {
		this.joinType = joinType;
	}
	public String getJoinStatus() {
		return joinStatus;
	}
	public void setJoinStatus(String joinStatus) {
		this.joinStatus = joinStatus;
	}

	public static CourseTeamUser[] add(CourseTeamUser[] objs, CourseTeamUser obj) {
		if (obj == null)
			return objs;
		int size = 0;
		if (objs != null)
			size = objs.length;
		CourseTeamUser[] newObjs = new CourseTeamUser[size+1];
		int i;
		for (i=0; i<size; i++)
			newObjs[i] = objs[i];
		newObjs[i] = obj;
		return newObjs;
	}
	public static CourseTeamUser[] remove(CourseTeamUser[] objs, CourseTeamUser obj) {
		if (obj == null)
			return objs;
		int size = 0;
		if (objs != null)
			size = objs.length;
		if (size == 0)
			return objs;
		CourseTeamUser[] newObjs = new CourseTeamUser[size-1];
		int i;
		int j = 0;
		for (i=0; i<size; i++) {
			if (objs[i].equals(obj))
				continue;
			newObjs[j++] = objs[i];
		}
		return newObjs;
	}
	public static CourseTeamUser[] left(CourseTeamUser[] objs, CourseTeamUser obj) {
		if (objs == null || objs.length == 0 || obj == null)
			return objs;
		int idx = -1;
		for (int i=0; i<objs.length; i++) {
			if (!objs[i].equals(obj))
				continue;
			idx = i;
			break;
		}
		if (idx < 1)
			return objs;
		CourseTeamUser[] newObjs = new CourseTeamUser[objs.length];
		for (int i=0; i<objs.length; i++) {
			if (i == idx) {
				newObjs[i] = objs[idx-1];
				continue;
			} else if (i == idx-1) {
				newObjs[i] = objs[idx];
				continue;
			}
			newObjs[i] = objs[i];
		}
		return newObjs;
	}
	public static CourseTeamUser[] right(CourseTeamUser[] objs, CourseTeamUser obj) {
		if (objs == null || objs.length == 0 || obj == null)
			return objs;
		int idx = -1;
		for (int i=0; i<objs.length; i++) {
			if (!objs[i].equals(obj))
				continue;
			idx = i;
			break;
		}
		if (idx == -1 || idx+1 == objs.length)
			return objs;
		CourseTeamUser[] newObjs = new CourseTeamUser[objs.length];
		for (int i=0; i<objs.length; i++) {
			if (i == idx) {
				newObjs[i] = objs[idx+1];
				continue;
			} else if (i == idx+1) {
				newObjs[i] = objs[idx];
				continue;
			}
			newObjs[i] = objs[i];
		}
		return newObjs;
	}
	public Object clone() throws CloneNotSupportedException {
		try {
			return toObject(this.toString());
		} catch (Exception e) {
			logger.warn(e, e);
			return null;
		}
	}

}