/*	
 * $Id$
 * created by    : hsshin
 * creation-date : 2012. 4. 22.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.sera.model;

import net.smartworks.server.engine.common.model.MisObjectCond;

public class CourseReviewCond extends MisObjectCond {

	private static final long serialVersionUID = 1L;
	private String courseId;
	private String content;
	private double startPoint;

	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public double getStartPoint() {
		return startPoint;
	}
	public void setStartPoint(double startPoint) {
		this.startPoint = startPoint;
	}

}