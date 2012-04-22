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
import net.smartworks.server.engine.sera.model.CourseReview;
import net.smartworks.server.engine.sera.model.CourseReviewCond;
import net.smartworks.server.engine.sera.model.MentorDetail;
import net.smartworks.server.engine.sera.model.MentorDetailCond;
import net.smartworks.server.engine.sera.model.SeraFriend;
import net.smartworks.server.engine.sera.model.SeraFriendCond;
import net.smartworks.server.engine.sera.model.SeraUserDetail;

public interface ISeraManager extends IManager{

	public MentorDetail getMentorDetailById(String userId, String mentorId) throws SeraException;
	public MentorDetail setMentorDetail(String userId, MentorDetail mentor) throws SeraException;
	public void removeMentorDetail(String userId, String mentorId) throws SeraException;
	public long getMentorDetailSize(String user, MentorDetailCond cond) throws SeraException;
	public MentorDetail[] getMentorDetails(String user, MentorDetailCond cond) throws SeraException;
	
	public CourseDetail getCourseDetailById(String courseId) throws SeraException;
	public CourseDetail setCourseDetail(CourseDetail courseDetail) throws SeraException;
	public void removeCourseDetail(String courseId) throws SeraException;
	public long getCourseDetailSize(String user, CourseDetailCond cond) throws SeraException;
	public CourseDetail[] getCourseDetails(String user, CourseDetailCond cond) throws SeraException;
	public String[] getCourseIdArrayByCondition(int type, String userId, Date fromDate, int pageSize, int pageNo) throws SeraException;
	
	public SeraFriend getFriendById(String userId, String objId) throws SeraException;
	public SeraFriend setFriend(String userId, SeraFriend friend) throws SeraException;
	public void removeFriend(String userId, String objId) throws SeraException;
	public long getFriendSize(String userId, SeraFriendCond friendCond) throws SeraException;
	public SeraFriend[] getFriends(String userId, SeraFriendCond friendCond) throws SeraException;
	public SeraFriend[] getMyFriends(String userId, SeraFriendCond cond) throws SeraException;

	public SeraUserDetail getSeraUserById(String userId, String objId) throws SeraException;
	public SeraUserDetail setSeraUser(String userId, SeraUserDetail seraUser) throws SeraException;
	public void removeSeraUser(String userId, String objId) throws SeraException;

	public CourseReview getCourseReviewById(String userId, String objId) throws SeraException;
	public CourseReview setCourseReview(String userId, CourseReview courseReview) throws SeraException;
	public void removeCourseReview(String userId, String objId) throws SeraException;
	public long getCourseReviewSize(String userId, CourseReviewCond courseReviewCond) throws SeraException;
	public CourseReview[] getCourseReviews(String userId, CourseReviewCond courseReviewCond) throws SeraException;

}