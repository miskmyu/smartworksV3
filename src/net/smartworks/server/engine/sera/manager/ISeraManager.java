/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 4. 2.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.sera.manager;

import java.util.Date;

import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.sera.exception.SeraException;
import net.smartworks.server.engine.sera.model.CourseDetail;
import net.smartworks.server.engine.sera.model.CourseDetailCond;
import net.smartworks.server.engine.sera.model.MentorDetail;
import net.smartworks.server.engine.sera.model.MentorDetailCond;

public interface ISeraManager extends IManager{

	public abstract MentorDetail getMentorDetailById(String userId, String mentorId) throws SeraException;
	public abstract MentorDetail setMentorDetail(String userId, MentorDetail mentor) throws SeraException;
	public abstract void removeMentorDetail(String userId, String mentorId) throws SeraException;
	public long getMentorDetailSize(String user, MentorDetailCond cond) throws SeraException;
	public MentorDetail[] getMentorDetails(String user, MentorDetailCond cond) throws SeraException;
	
	public abstract CourseDetail getCourseDetailById(String courseId) throws SeraException;
	public abstract CourseDetail setCourseDetail(CourseDetail courseDetail) throws SeraException;
	public abstract void removeCourseDetail(String courseId) throws SeraException;
	public long getCourseDetailSize(String user, CourseDetailCond cond) throws SeraException;
	public CourseDetail[] getCourseDetails(String user, CourseDetailCond cond) throws SeraException;
	public String[] getCourseIdArrayByCondition(int type, String userId, Date fromDate, int pageSize, int pageNo) throws SeraException;
}
