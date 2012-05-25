/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 4. 2.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.sera.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.smartworks.model.sera.Course;
import net.smartworks.server.engine.common.manager.AbstractManager;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.sera.exception.SeraException;
import net.smartworks.server.engine.sera.manager.ISeraManager;
import net.smartworks.server.engine.sera.model.CourseDetail;
import net.smartworks.server.engine.sera.model.CourseDetailCond;
import net.smartworks.server.engine.sera.model.CourseReview;
import net.smartworks.server.engine.sera.model.CourseReviewCond;
import net.smartworks.server.engine.sera.model.CourseTeam;
import net.smartworks.server.engine.sera.model.CourseTeamCond;
import net.smartworks.server.engine.sera.model.CourseTeamUser;
import net.smartworks.server.engine.sera.model.MentorDetail;
import net.smartworks.server.engine.sera.model.MentorDetailCond;
import net.smartworks.server.engine.sera.model.SeraFriend;
import net.smartworks.server.engine.sera.model.SeraFriendCond;
import net.smartworks.server.engine.sera.model.SeraUserDetail;
import net.smartworks.server.engine.sera.model.SeraUserDetailCond;

import org.hibernate.Query;

public class SeraManagerImpl extends AbstractManager implements ISeraManager {

	public MentorDetail getMentorDetailById(String userId, String mentorId) throws SeraException {
		try {
			if (CommonUtil.isEmpty(mentorId)) 
				return null;
			MentorDetail mentor = (MentorDetail)this.get(MentorDetail.class, mentorId);
			return mentor;
		} catch (SeraException e) {
			throw e;
		} catch (Exception e) {
			throw new SeraException(e);
		}
	}
	public MentorDetail setMentorDetail(String userId, MentorDetail mentorDetail) throws SeraException {
		try {
			if (mentorDetail == null)
				return null;
			this.set(mentorDetail);
			return mentorDetail;
		} catch (SeraException e) {
			throw e;
		} catch (Exception e) {
			throw new SeraException(e);
		}
	}
	public void removeMentorDetail(String user, String mentorId) throws SeraException {
		try {
			MentorDetail obj = this.getMentorDetailById(user, mentorId);
			this.getHibernateTemplate().delete(obj);
			this.getHibernateTemplate().flush();
		} catch (SeraException e) {
			throw e;
		} catch (Exception e) {
			throw new SeraException(e);
		}
	}
	private Query appendQuery(StringBuffer buf, MentorDetailCond cond) throws Exception {
		String mentorId = null;
		String[] mentorIdIns = null;
		if (cond != null) {
			mentorId = cond.getMentorId();
			mentorIdIns = cond.getMentorIdIns();
		}
		buf.append(" from MentorDetail obj");
		buf.append(" where obj.mentorId is not null");
		if (cond != null) {
			if (mentorId != null)
				buf.append(" and obj.mentorId = :mentorId");

			if (mentorIdIns != null && mentorIdIns.length != 0) {
				buf.append(" and obj.mentorId in (");
				for (int i=0; i<mentorIdIns.length; i++) {
					if (i != 0)
						buf.append(", ");
					buf.append(":mentorIdIn").append(i);
				}
				buf.append(")");
			}
		}
		this.appendOrderQuery(buf, "obj", cond);
		Query query = this.createQuery(buf.toString(), cond);
		if (cond != null) {
			if (mentorId != null)
				query.setString("mentorId", mentorId);

			if (mentorIdIns != null && mentorIdIns.length != 0) {
				for (int i=0; i<mentorIdIns.length; i++) {
					query.setString("mentorIdIn"+i, mentorIdIns[i]);
				}
			}
		}
		return query;
	}

	public long getMentorDetailSize(String user, MentorDetailCond cond) throws SeraException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			buf.append(" count(obj)");
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			long count = ((Long)list.get(0)).longValue();
			return count;
		} catch (Exception e) {
			logger.error(e, e);
			throw new SeraException(e);
		}
	}
	public MentorDetail[] getMentorDetails(String user, MentorDetailCond cond) throws SeraException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			buf.append(" obj");
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			MentorDetail[] objs = new MentorDetail[list.size()];
			list.toArray(objs);
			return objs;
		} catch (Exception e) {
			logger.error(e, e);
			throw new SeraException(e);
		}
	}
	public CourseDetail getCourseDetailById(String courseId) throws SeraException {
		try {
			if (CommonUtil.isEmpty(courseId)) 
				return null;
			CourseDetail courseDetail = (CourseDetail)this.get(CourseDetail.class, courseId);
			return courseDetail;
		} catch (SeraException e) {
			throw e;
		} catch (Exception e) {
			throw new SeraException(e);
		}
	}
	public CourseDetail setCourseDetail(CourseDetail courseDetail) throws SeraException {
		try {
			if (courseDetail == null)
				return null;
			this.set(courseDetail);
			return courseDetail;
		} catch (SeraException e) {
			throw e;
		} catch (Exception e) {
			throw new SeraException(e);
		}
	}
	public void removeCourseDetail(String courseId) throws SeraException {
		try {
			CourseDetail obj = this.getCourseDetailById(courseId);
			this.getHibernateTemplate().delete(obj);
			this.getHibernateTemplate().flush();
		} catch (SeraException e) {
			throw e;
		} catch (Exception e) {
			throw new SeraException(e);
		}
	}
	private Query appendQuery(StringBuffer buf, CourseDetailCond cond) throws Exception {
		String courseId = null;
		String[] courseIdIns = null;
		String status = null;
		Date fromDate = null;
		Date startTo = null;
		Date endDateTo = null;
		Date endDateFrom = null;
		boolean recommended = false;
		Date createDate = null;
		Date createDateFrom = null;
		Date createDateTo = null;
		int coursePointFrom = -1;
		int coursePointTo = -1;
		int coursePoint =  -1;
		String[] categorys = null;
		if (cond != null) {
			courseId = cond.getCourseId();
			courseIdIns = cond.getCourseIdIns();
			status = cond.getStatus();
			startTo = cond.getStartTo();
			fromDate = cond.getStart();
			endDateFrom = cond.getEndFrom();
			endDateTo = cond.getEndTo();
			recommended = cond.isRecommended();
			createDate = cond.getCreateDate();
			createDateFrom = cond.getCreateDateFrom();
			createDateTo = cond.getCreateDateTo();
			coursePoint = cond.getCoursePoint();
			coursePointFrom = cond.getCoursePointFrom();
			coursePointTo = cond.getCoursePointTo();
			categorys = cond.getCategories();
		}
		buf.append(" from CourseDetail obj");
		buf.append(" where obj.courseId is not null");
		if (cond != null) {
			if (courseId != null)
				buf.append(" and obj.courseId = :courseId");

			if (courseIdIns != null && courseIdIns.length != 0) {
				buf.append(" and obj.courseId in (");
				for (int i=0; i<courseIdIns.length; i++) {
					if (i != 0)
						buf.append(", ");
					buf.append(":courseIdIn").append(i);
				}
				buf.append(")");
			}
			if (status != null) 
				buf.append(" and obj.status = :status");
			if (startTo != null) 
				buf.append(" and obj.start < :startTo");
			if (fromDate != null) 
				buf.append(" and obj.start > :fromDate");
			if (endDateFrom != null) 
				buf.append(" and obj.end > :endDateFrom");
			if (endDateTo != null) 
				buf.append(" and obj.end < :endDateTo");
			if (recommended)
				buf.append(" and obj.recommended = :recommended");
			if (createDate != null)
				buf.append(" and obj.createDate = :createDate");
			if (createDateFrom != null)
				buf.append(" and obj.createDate > :createDateFrom");
			if (createDateTo != null)
				buf.append(" and obj.createDate < :createDateTo");
			if (coursePoint != -1)
				buf.append(" and obj.coursePoint = :coursePoint");
			if (coursePointFrom != -1)
				buf.append(" and obj.coursePoint > :coursePointFrom");
			if (coursePointTo != -1)
				buf.append(" and obj.coursePoint < :coursePointTo");
			if (categorys != null && categorys.length != 0) {
				buf.append(" and obj.categories in (");
				for (int i=0; i<categorys.length; i++) {
					if (i != 0)
						buf.append(", ");
					buf.append(":categoryIdIns").append(i);
				}
				buf.append(")");
			}
		}
		this.appendOrderQuery(buf, "obj", cond);
		Query query = this.createQuery(buf.toString(), cond);
		if (cond != null) {
			if (courseId != null)
				query.setString("courseId", courseId);

			if (courseIdIns != null && courseIdIns.length != 0) {
				for (int i=0; i<courseIdIns.length; i++) {
					query.setString("courseIdIn"+i, courseIdIns[i]);
				}
			}
			if (status != null)
				query.setString("status", status);
			if (startTo != null)
				query.setTimestamp("startTo", startTo);
			if (fromDate != null)
				query.setTimestamp("fromDate", fromDate);
			if (endDateFrom != null) 
				query.setTimestamp("endDateFrom", endDateFrom);
			if (endDateTo != null) 
				query.setTimestamp("endDateTo", endDateTo);
			if (recommended)
				query.setBoolean("recommended", recommended);
			if (createDate != null)
				query.setTimestamp("createDate", createDate);
			if (createDateFrom != null)
				query.setTimestamp("createDateFrom", createDateFrom);
			if (createDateTo != null)
				query.setTimestamp("createDateTo", createDateTo);
			if (coursePoint != -1)
				query.setInteger("coursePoint", coursePoint);
			if (coursePointFrom != -1)
				query.setInteger("coursePointFrom", coursePointFrom);
			if (coursePointTo != -1)
				query.setInteger("coursePointTo", coursePointTo);
			if (categorys != null && categorys.length != 0) {
				for (int i=0; i<categorys.length; i++) {
					query.setString("categoryIdIns"+i, categorys[i]);
				}
			}
		}
		return query;
	}
	public long getCourseDetailSize(String user, CourseDetailCond cond) throws SeraException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			buf.append(" count(obj)");
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			long count = ((Long)list.get(0)).longValue();
			return count;
		} catch (Exception e) {
			logger.error(e, e);
			throw new SeraException(e);
		}
	}
	public CourseDetail[] getCourseDetails(String user, CourseDetailCond cond) throws SeraException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			buf.append(" obj");
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			CourseDetail[] objs = new CourseDetail[list.size()];
			list.toArray(objs);
			return objs;
		} catch (Exception e) {
			logger.error(e, e);
			throw new SeraException(e);
		}
	}
	public String[] getCourseIdArrayByCondition(int type, String userId, Date fromDate, int pageSize, int pageNo) throws SeraException {
		
		StringBuffer buf = new StringBuffer();
		buf.append("select distinct grp.id, courseDetail.startdate ");
		buf.append("from ");
		buf.append("	sworggroup grp, ");
		buf.append("	coursedetail courseDetail, ");
		buf.append("	sworggroupmember grpMem ");
		buf.append("where  ");
		buf.append("	grp.status = 'O' ");
		buf.append("	grp.id = courseDetail.courseId ");
		buf.append("	grp.id = grpMem.groupId ");
		if (fromDate != null)
			buf.append("	and courseDetail.startdate > :fromDate ");
		
		if (type == Course.MY_ALL_COURSES) {
			buf.append("	and (grp.groupLeader = :userId or grpMem.userId = :userId) ");		
		} else if (type == Course.MY_ATTENDING_COURSE) {
			buf.append("	and grpMem.userId = :userId ");
		} else if (type == Course.MY_RUNNING_COURSE) {
			buf.append("	and grp.groupLeader = :userId ");
		}
		buf.append("order by courseDetail.startdate desc ");

		Query query = this.getSession().createSQLQuery(buf.toString());

		if (fromDate != null)
			query.setTimestamp("fromDate", fromDate);
		
		if (pageSize > 0|| pageNo >= 0) {
			query.setFirstResult(pageNo * pageSize);
			query.setMaxResults(pageSize);
		}
		List list = query.list();
		if (list == null || list.isEmpty())
			return null;
		List objList = new ArrayList();
		for (Iterator itr = list.iterator(); itr.hasNext();) {
			Object[] fields = (Object[]) itr.next();
			int j = 0;
			String courseId = (String)fields[j++];    
			
			objList.add(courseId);
		}
		list = objList;
		String[] objs = new String[list.size()];
		list.toArray(objs);
		return objs;
	}
	@Override
	public SeraFriend getFriendById(String userId, String objId) throws SeraException {
		try {
			if (CommonUtil.isEmpty(objId)) 
				return null;
			SeraFriend seraFriend = (SeraFriend)this.get(SeraFriend.class, objId);
			return seraFriend;
		} catch (SeraException e) {
			throw e;
		} catch (Exception e) {
			throw new SeraException(e);
		}
	}
	@Override
	public SeraFriend setFriend(String userId, SeraFriend friend) throws SeraException {
		try {
			if (friend == null)
				return null;
			this.set(friend);
			return friend;
		} catch (SeraException e) {
			throw e;
		} catch (Exception e) {
			throw new SeraException(e);
		}
	}
	@Override
	public void removeFriend(String userId, String objId) throws SeraException {
		try {
			SeraFriend obj = this.getFriendById(userId, objId);
			this.getHibernateTemplate().delete(obj);
			this.getHibernateTemplate().flush();
		} catch (SeraException e) {
			throw e;
		} catch (Exception e) {
			throw new SeraException(e);
		}
	}
	private Query appendQuery(StringBuffer buf, SeraFriendCond cond) throws Exception {
		String objId = null;
		String requestId = null;
		String requestName = null;
		String receiveId = null;
		String receiveName = null;
		int acceptStatus = -1;
		String requestIdOrReceiveId = null;
		String lastRequestName = null;

		if (cond != null) {
			objId = cond.getObjId();
			requestId = cond.getRequestId();
			requestName = cond.getRequestName();
			receiveId = cond.getReceiveId();
			receiveName = cond.getReceiveName();
			acceptStatus = cond.getAcceptStatus();
			requestIdOrReceiveId = cond.getRequestIdOrReceiveId();
			lastRequestName = cond.getLastRequestName();
		}
		buf.append(" from SeraFriend obj");
		buf.append(" where obj.objId is not null");
		if (cond != null) {
			if (objId != null)
				buf.append(" and obj.objId = :objId");
			if (requestId != null)
				buf.append(" and obj.requestId = :requestId");
			if (requestName != null)
				buf.append(" and obj.requestName = :requestName");
			if (receiveId != null)
				buf.append(" and obj.receiveId = :receiveId");
			if (receiveName != null)
				buf.append(" and obj.receiveName = :receiveName");
			if (acceptStatus != -1)
				buf.append(" and obj.acceptStatus = :acceptStatus");
			if (requestIdOrReceiveId != null)
				buf.append(" and (obj.requestId = :requestIdOrReceiveId or obj.receiveId = :requestIdOrReceiveId)");
			if (lastRequestName != null)
				buf.append(" and obj.requestName > :lastRequestName");
		}
		this.appendOrderQuery(buf, "obj", cond);
		Query query = this.createQuery(buf.toString(), cond);
		if (cond != null) {
			if (objId != null)
				query.setString("objId", objId);
			if (requestId != null)
				query.setString("requestId", requestId);
			if (requestName != null)
				query.setString("requestName", requestName);
			if (receiveId != null)
				query.setString("receiveId", receiveId);
			if (receiveName != null)
				query.setString("receiveName", receiveName);
			if (acceptStatus != -1)
				query.setInteger("acceptStatus", acceptStatus);
			if (requestIdOrReceiveId != null)
				query.setString("requestIdOrReceiveId", requestIdOrReceiveId);
			if (lastRequestName != null)
				query.setString("lastRequestName", lastRequestName);
		}
		return query;
	}

	@Override
	public long getFriendSize(String userId, SeraFriendCond friendCond) throws SeraException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			buf.append(" count(obj)");
			Query query = this.appendQuery(buf, friendCond);
			List list = query.list();
			long count = ((Long)list.get(0)).longValue();
			return count;
		} catch (Exception e) {
			logger.error(e, e);
			throw new SeraException(e);
		}
	}
	@Override
	public SeraFriend[] getFriends(String userId, SeraFriendCond friendCond) throws SeraException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			buf.append(" obj");
			Query query = this.appendQuery(buf, friendCond);
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			SeraFriend[] objs = new SeraFriend[list.size()];
			list.toArray(objs);
			return objs;
		} catch (Exception e) {
			logger.error(e, e);
			throw new SeraException(e);
		}
	}

	@Override
	public SeraFriend[] getMyFriends(String userId, SeraFriendCond cond) throws SeraException {

		String friendId = userId;
		String lastFriendName = null;
		String key = null;
		int acceptStatus = -1;
		int pageSize = -1;
		int pageNo = -1;

		if(cond != null) {
			lastFriendName = cond.getLastFriendName();
			acceptStatus = cond.getAcceptStatus();
			key = cond.getKey();
			pageSize = cond.getPageSize();
			pageNo = cond.getPageNo();
		}
		StringBuffer queryBuffer = new StringBuffer();

		queryBuffer.append(" select *  ");
		queryBuffer.append(" from  ");
		queryBuffer.append(" ( ");
		queryBuffer.append(" 	select fri.requestId as friendId ");
		queryBuffer.append(" 		, fri.requestName as friendName ");
		queryBuffer.append(" 		, fri.acceptStatus ");
		queryBuffer.append("		, usr.nickName ");
		queryBuffer.append("	  from friends fri, sworguser usr ");
		queryBuffer.append(" 	 where fri.requestId = usr.id ");
		queryBuffer.append(" 	   and receiveId =:friendId ");
		queryBuffer.append(" 	union ");
		queryBuffer.append(" 	select fri.receiveId as friendId ");
		queryBuffer.append(" 		, fri.receiveName as friendName ");
		queryBuffer.append(" 		, fri.acceptStatus ");
		queryBuffer.append("		, usr.nickName ");
		queryBuffer.append("	  from friends fri, sworguser usr ");
		queryBuffer.append(" 	 where fri.receiveId = usr.id ");
		queryBuffer.append(" 	   and requestId =:friendId ");
		queryBuffer.append(" ) friendInfo ");
		queryBuffer.append(" where 1=1 ");
		if(acceptStatus != -1)
			queryBuffer.append(" and friendInfo.acceptStatus =:acceptStatus ");
		if(lastFriendName != null)
			queryBuffer.append(" and friendInfo.friendName >:lastFriendName ");
		if(key != null)
			queryBuffer.append(" and (friendInfo.friendId like :key or friendInfo.friendName like :key or friendInfo.nickName like :key)");
		queryBuffer.append(" order by friendInfo.friendName asc ");

		Query query = this.getSession().createSQLQuery(queryBuffer.toString());
		if (pageSize > 0 || pageNo >= 0) {
			query.setFirstResult(pageNo * pageSize);
			query.setMaxResults(pageSize);
		}

		query.setString("friendId", friendId);
		if(acceptStatus != -1)
			query.setInteger("acceptStatus", acceptStatus);
		if(lastFriendName != null)
			query.setString("lastFriendName", lastFriendName);
		if (key != null)
			query.setString("key", CommonUtil.toLikeString(key));
		List list = query.list();
		if (list == null || list.isEmpty())
			return null;
		List objList = new ArrayList();
		for (Iterator itr = list.iterator(); itr.hasNext();) {
			Object[] fields = (Object[]) itr.next();
			SeraFriend obj = new SeraFriend();
			int j = 0;
			obj.setFriendId((String)fields[j++]);    
			obj.setFriendName((String)fields[j++]);
			objList.add(obj);
		}
		list = objList;
		SeraFriend[] objs = new SeraFriend[list.size()];
		list.toArray(objs);
		return objs;
	}

	@Override
	public SeraUserDetail getSeraUserById(String userId, String objId) throws SeraException {
		try {
			if (CommonUtil.isEmpty(objId)) 
				return null;
			SeraUserDetail seraUser = (SeraUserDetail)this.get(SeraUserDetail.class, objId);
			return seraUser;
		} catch (SeraException e) {
			throw e;
		} catch (Exception e) {
			throw new SeraException(e);
		}
	}

	private Query appendQuery(StringBuffer buf, SeraUserDetailCond cond) throws Exception {
		String userId = null;
		String email = null;
		String[] userIdIns = null;
		String[] userIdNotIns = null;
		String key = null;
		if (cond != null) {
			userId = cond.getUserId();
			email = cond.getEmail();
			userIdIns = cond.getUserIdIns();
			userIdNotIns = cond.getUserIdNotIns();
			key = cond.getKey();
		}
		buf.append(" from SeraUserDetail obj");
		buf.append(" where obj.userId is not null");
		if (cond != null) {
			if (userId != null)
				buf.append(" and obj.userId = :userId");
			if (email != null)
				buf.append(" and obj.email = :email");
			if (userIdIns != null && userIdIns.length != 0) {
				buf.append(" and obj.userId in (");
				for (int i=0; i<userIdIns.length; i++) {
					if (i != 0)
						buf.append(", ");
					buf.append(":userIdIn").append(i);
				}
				buf.append(")");
			}
			if (userIdNotIns != null && userIdNotIns.length != 0) {
				buf.append(" and obj.userId not in (");
				for (int i=0; i<userIdNotIns.length; i++) {
					if (i != 0)
						buf.append(", ");
					buf.append(":userIdNotIn").append(i);
				}
				buf.append(")");
			}
			if(key != null)
				buf.append(" and (obj.userId like :key or obj.userName like :key or obj.nickName like :key)");
		}
		this.appendOrderQuery(buf, "obj", cond);
		Query query = this.createQuery(buf.toString(), cond);
		if (cond != null) {
			if (userId != null)
				query.setString("userId", userId);
			if (email != null)
				query.setString("email", email);

			if (userIdIns != null && userIdIns.length != 0) {
				for (int i=0; i<userIdIns.length; i++) {
					query.setString("userIdIn"+i, userIdIns[i]);
				}
			}
			if (userIdNotIns != null && userIdNotIns.length != 0) {
				for (int i=0; i<userIdNotIns.length; i++) {
					query.setString("userIdNotIn"+i, userIdNotIns[i]);
				}
			}
			if (key != null)
				query.setString("key", CommonUtil.toLikeString(key));
				
		}
		return query;
	}

	@Override
	public SeraUserDetail[] getSeraUserDetails(String userId, SeraUserDetailCond cond) throws SeraException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			buf.append(" obj");
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			SeraUserDetail[] objs = new SeraUserDetail[list.size()];
			list.toArray(objs);
			return objs;
		} catch (Exception e) {
			logger.error(e, e);
			throw new SeraException(e);
		}
	}
	@Override
	public SeraUserDetail setSeraUser(String userId, SeraUserDetail seraUser) throws SeraException {
		try {
			if (seraUser == null)
				return null;
			this.set(seraUser);
			return seraUser;
		} catch (SeraException e) {
			throw e;
		} catch (Exception e) {
			throw new SeraException(e);
		}
	}
	@Override
	public void removeSeraUser(String userId, String objId) throws SeraException {
		try {
			SeraUserDetail obj = this.getSeraUserById(userId, objId);
			if (obj == null)
				return;
			this.getHibernateTemplate().delete(obj);
			this.getHibernateTemplate().flush();
		} catch (SeraException e) {
			throw e;
		} catch (Exception e) {
			throw new SeraException(e);
		}
	}
	@Override
	public long getSeraUserSize(String userId, SeraUserDetailCond cond) throws SeraException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			buf.append(" count(obj)");
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			long count = ((Long)list.get(0)).longValue();
			return count;
		} catch (Exception e) {
			logger.error(e, e);
			throw new SeraException(e);
		}
	}
	@Override
	public CourseReview getCourseReviewById(String userId, String objId) throws SeraException {
		try {
			if(CommonUtil.isEmpty(objId)) 
				return null;
			CourseReview courseReview = (CourseReview)this.get(CourseReview.class, objId);
			return courseReview;
		} catch(SeraException e) {
			throw e;
		} catch(Exception e) {
			throw new SeraException(e);
		}
	}
	@Override
	public CourseReview setCourseReview(String userId, CourseReview courseReview) throws SeraException {
		try {
			if(courseReview == null)
				return null;
			this.set(courseReview);
			return courseReview;
		} catch (SeraException e) {
			throw e;
		} catch (Exception e) {
			throw new SeraException(e);
		}
	}
	@Override
	public void removeCourseReview(String userId, String objId) throws SeraException {
		try {
			CourseReview obj = this.getCourseReviewById(userId, objId);
			this.getHibernateTemplate().delete(obj);
			this.getHibernateTemplate().flush();
		} catch (SeraException e) {
			throw e;
		} catch (Exception e) {
			throw new SeraException(e);
		}
	}
	private Query appendQuery(StringBuffer buf, CourseReviewCond cond) throws Exception {
		String objId = null;
		String courseId = null;
		String creationUser = null;
		String modificationUser = null;
		Date modificationDateFrom = null;
		if (cond != null) {
			objId = cond.getObjId();
			courseId = cond.getCourseId();
			creationUser = cond.getCreationUser();
			modificationUser = cond.getModificationUser();
			modificationDateFrom = cond.getModificationDateFrom();
		}
		buf.append(" from CourseReview obj");
		buf.append(" where obj.objId is not null");
		if (cond != null) {
			if (objId != null)
				buf.append(" and obj.objId = :objId");
			if (courseId != null)
				buf.append(" and obj.courseId = :courseId");
			if (creationUser != null)
				buf.append(" and obj.creationUser = :creationUser");
			if (modificationUser != null)
				buf.append(" and obj.modificationUser = :modificationUser");
			if (modificationDateFrom != null)
				buf.append(" and obj.modificationDate < :modificationDateFrom");
		}
		this.appendOrderQuery(buf, "obj", cond);
		Query query = this.createQuery(buf.toString(), cond);
		if (cond != null) {
			if (objId != null)
				query.setString("objId", objId);
			if (courseId != null)
				query.setString("courseId", courseId);
			if (creationUser != null)
				query.setString("creationUser", creationUser);
			if (modificationUser != null)
				query.setString("modificationUser", modificationUser);
			if (modificationDateFrom != null)
				query.setTimestamp("modificationDateFrom", modificationDateFrom);
		}
		return query;
	}
	@Override
	public long getCourseReviewSize(String userId, CourseReviewCond courseReviewCond) throws SeraException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			buf.append(" count(obj)");
			Query query = this.appendQuery(buf, courseReviewCond);
			List list = query.list();
			long count = ((Long)list.get(0)).longValue();
			return count;
		} catch (Exception e) {
			logger.error(e, e);
			throw new SeraException(e);
		}
	}
	@Override
	public CourseReview[] getCourseReviews(String userId, CourseReviewCond courseReviewCond) throws SeraException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			buf.append(" obj");
			Query query = this.appendQuery(buf, courseReviewCond);
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			CourseReview[] objs = new CourseReview[list.size()];
			list.toArray(objs);
			return objs;
		} catch (Exception e) {
			logger.error(e, e);
			throw new SeraException(e);
		}
	}
	@Override
	public CourseTeam getCourseTeam(String user, String id, String level) throws SeraException {
		try {
			if (level == null)
				level = LEVEL_ALL;
			if (level.equals(LEVEL_ALL)) {
				CourseTeam obj = (CourseTeam)this.get(CourseTeam.class, id);
				return obj;
			} else {
				CourseTeamCond cond = new CourseTeamCond();
				cond.setObjId(id);
				CourseTeam[] objs = this.getCourseTeams(user, cond, level);
				if (objs == null || objs.length == 0)
					return null;
				return objs[0];
			}
		} catch (Exception e) {
			return null;
		}
	}
	@Override
	public CourseTeam getCourseTeam(String user, CourseTeamCond cond, String level) throws SeraException {
		if (level == null)
			level = LEVEL_ALL;
		CourseTeam[] objs = getCourseTeams(user, cond, level);
		if (CommonUtil.isEmpty(objs))
			return null;
		try {
			if (objs.length != 1)
				throw new SeraException("More than 1 Object");
		} catch (SeraException e) {
			logger.error(e, e);
			throw e;
		}
		return objs[0];
	}
	@Override
	public CourseTeam setCourseTeam(String user, CourseTeam courseTeam) throws SeraException {
		try {
			if(courseTeam == null)
				return null;
			this.set(courseTeam);
			return courseTeam;
		} catch (SeraException e) {
			throw e;
		} catch (Exception e) {
			throw new SeraException(e);
		}
	}
	@Override
	public CourseTeam createCourseTeam(String user, CourseTeam courseTeam) throws SeraException {
		try {
			if(courseTeam == null)
				return null;
			this.create(courseTeam);
			return courseTeam;
		} catch (SeraException e) {
			throw e;
		} catch (Exception e) {
			throw new SeraException(e);
		}
	}
	@Override
	public void removeCourseTeam(String user, String id) throws SeraException {
		try {
			remove(CourseTeam.class, id);
		} catch (Exception e) {
			logger.error(e, e);
			throw new SeraException(e);
		}
	}
	@Override
	public void removeCourseTeam(String user, CourseTeamCond cond) throws SeraException {
		CourseTeam obj = getCourseTeam(user, cond, null);
		if (obj == null)
			return;
		removeCourseTeam(user, obj.getObjId());
	}
	private Query appendQuery(StringBuffer buf, CourseTeamCond cond) throws Exception {
		String objId = null;
		String courseId = null;
		CourseTeamUser[] courseTeamUsers = null;

		if (cond != null) {
			objId = cond.getObjId();
			courseId = cond.getCourseId();
			courseTeamUsers = cond.getCourseTeamUsers();
		}
		buf.append(" from CourseTeam obj");
		if (courseTeamUsers != null && courseTeamUsers.length != 0) {
			for (int i=0; i<courseTeamUsers.length; i++) {
				buf.append(" left join obj.courseTeamUsers as teamUser").append(i);
			}
		}
		buf.append(" where obj.objId is not null");
		//TODO 시간 검색에 대한 확인 필요
		if (cond != null) {
			if (objId != null)	
				buf.append(" and obj.objId = :objId");
			if (courseId != null)
				buf.append(" and obj.courseId = :courseId");
			if (courseTeamUsers != null && courseTeamUsers.length != 0) {
				for (int i=0; i<courseTeamUsers.length; i++) {
					CourseTeamUser courseTeamUser = courseTeamUsers[i];
					String teamId = courseTeamUser.getObjId();
					String userId = courseTeamUser.getUserId();
					String joinType = courseTeamUser.getJoinType();
					String joinStatus = courseTeamUser.getJoinStatus();
					if (teamId != null)
						buf.append(" and teamUser").append(i).append(".objId = :teamId").append(i);
					if (userId != null)
						buf.append(" and teamUser").append(i).append(".userId = :userId").append(i);
					if (joinType != null)
						buf.append(" and teamUser").append(i).append(".joinType = :joinType").append(i);
					if (joinStatus != null)
						buf.append(" and teamUser").append(i).append(".joinStatus = :joinStatus").append(i);
				}
			}
		}

		this.appendOrderQuery(buf, "obj", cond);

		Query query = this.createQuery(buf.toString(), cond);
		if (cond != null) {
			if (objId != null)
				query.setString("objId", objId);
			if (courseId != null)
				query.setString("courseId", courseId);

			if (courseTeamUsers != null && courseTeamUsers.length != 0) {
				for (int i=0; i<courseTeamUsers.length; i++) {
					CourseTeamUser courseTeamUser = courseTeamUsers[i];
					String teamId = courseTeamUser.getObjId();
					String userId = courseTeamUser.getUserId();
					String joinType = courseTeamUser.getJoinType();
					String joinStatus = courseTeamUser.getJoinStatus();
					if (teamId != null)
						query.setString("teamId"+i, teamId);
					if (userId != null)
						query.setString("userId"+i, userId);
					if (joinType != null)
						query.setString("joinType"+i, joinType);
					if (joinStatus != null)
						query.setString("joinStatus"+i, joinStatus);
				}
			}
		}
		return query;
	}
	@Override
	public long getCourseTeamSize(String user, CourseTeamCond cond) throws SeraException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			buf.append(" count(obj)");
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			long count = ((Long)list.get(0)).longValue();
			return count;
		} catch (Exception e) {
			logger.error(e, e);
			throw new SeraException(e);
		}
	}
	@Override
	public CourseTeam[] getCourseTeams(String user, CourseTeamCond cond, String level) throws SeraException {
		try {
			if (level == null)
				level = LEVEL_LITE;
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			if (level.equals(LEVEL_ALL)) {
				buf.append(" obj");
			} else {
				buf.append(" obj.objId, obj.name, obj.courseId, obj.description, obj.accessPolicy, obj.memberSize, obj.startDate, obj.endDate");
			}
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			if (!level.equals(LEVEL_ALL)) {
				List objList = new ArrayList();
				for (Iterator itr = list.iterator(); itr.hasNext();) {
					Object[] fields = (Object[]) itr.next();
					CourseTeam obj = new CourseTeam();
					int j = 0;
					obj.setObjId((String)fields[j++]);
					obj.setName((String)fields[j++]);
					obj.setCourseId((String)fields[j++]);
					obj.setDescription((String)fields[j++]);
					obj.setAccessPolicy((Integer)fields[j++]);
					obj.setMemberSize((Integer)fields[j++]);
					obj.setStartDate((Timestamp)fields[j++]);
					obj.setEndDate((Timestamp)fields[j++]);
					objList.add(obj);
				}
				list = objList;
			}
			CourseTeam[] objs = new CourseTeam[list.size()];
			list.toArray(objs);
			return objs;
		} catch (Exception e) {
			logger.error(e, e);
			throw new SeraException(e);
		}
	}

}