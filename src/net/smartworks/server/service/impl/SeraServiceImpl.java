package net.smartworks.server.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.smartworks.model.community.Group;
import net.smartworks.model.community.User;
import net.smartworks.model.community.info.GroupInfo;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.info.InstanceInfo;
import net.smartworks.model.security.AccessPolicy;
import net.smartworks.model.sera.Course;
import net.smartworks.model.sera.CourseList;
import net.smartworks.model.sera.FriendList;
import net.smartworks.model.sera.Mentor;
import net.smartworks.model.sera.MissionInstance;
import net.smartworks.model.sera.info.CourseInfo;
import net.smartworks.model.sera.info.MissionInstanceInfo;
import net.smartworks.model.sera.info.ReviewInstanceInfo;
import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.model.SmartServerConstant;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.common.util.id.IDCreator;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.organization.manager.ISwoManager;
import net.smartworks.server.engine.organization.model.SwoGroup;
import net.smartworks.server.engine.organization.model.SwoGroupCond;
import net.smartworks.server.engine.organization.model.SwoGroupMember;
import net.smartworks.server.engine.sera.manager.ISeraManager;
import net.smartworks.server.engine.sera.model.CourseDetail;
import net.smartworks.server.engine.sera.model.CourseDetailCond;
import net.smartworks.server.engine.sera.model.MentorDetail;
import net.smartworks.server.service.ISeraService;
import net.smartworks.server.service.util.ModelConverter;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SeraTest;
import net.smartworks.util.SmartUtil;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SeraServiceImpl implements ISeraService {

	private static GroupInfo getGroupInfoBySwoGroup(GroupInfo groupInfo, SwoGroup swoGroup) throws Exception {
		if (swoGroup == null)
			return null;
		if (groupInfo == null) 
			groupInfo = new CourseInfo();

		groupInfo.setId(swoGroup.getId());
		groupInfo.setName(swoGroup.getName());
		groupInfo.setDesc(swoGroup.getDescription());

		String picture = CommonUtil.toNotNull(swoGroup.getPicture());
		if(!picture.equals("")) {
			String extension = picture.lastIndexOf(".") > 0 ? picture.substring(picture.lastIndexOf(".") + 1) : null;
			String pictureId = picture.substring(0, (picture.length() - extension.length())-1);
			groupInfo.setSmallPictureName(pictureId + "_thumb" + "." + extension);
		} else {
			groupInfo.setSmallPictureName(picture);
		}

		return groupInfo;
	}
	private static Group getGroupBySwoGroup(Group group, SwoGroup swoGroup) throws Exception {
		try {
			if (swoGroup == null)
				return null;
			if (group == null)
				group = new Course();
	
			group.setId(swoGroup.getId());
			group.setName(swoGroup.getName());
			group.setDesc(swoGroup.getDescription());
			group.setPublic(swoGroup.equals("O") ? true : false);
			//group.setContinue(swoGroup.getStatus().equals("C") ? true : false);
			User leader = ModelConverter.getUserByUserId(swoGroup.getGroupLeader());
			if(leader != null)
				group.setLeader(leader);
	
			User owner = ModelConverter.getUserByUserId(swoGroup.getCreationUser());
			if(owner != null)
				group.setOwner(owner);

			LocalDate openDate = new LocalDate(swoGroup.getCreationDate().getTime());
			group.setOpenDate(openDate);

			List<UserInfo> groupMemberList = new ArrayList<UserInfo>();
			SwoGroupMember[] swoGroupMembers = swoGroup.getSwoGroupMembers();
			if(!CommonUtil.isEmpty(swoGroupMembers)) {
				groupMemberList.add(ModelConverter.getUserInfoByUserId(swoGroup.getGroupLeader()));
				for(SwoGroupMember swoGroupMember : swoGroupMembers) {
					if(!swoGroupMember.getUserId().equals(swoGroup.getGroupLeader())) {
						UserInfo groupMember = ModelConverter.getUserInfoByUserId(swoGroupMember.getUserId());
						groupMemberList.add(groupMember);
					}
				}
				UserInfo[] groupMembers = new UserInfo[groupMemberList.size()];
				groupMemberList.toArray(groupMembers);
				group.setMembers(groupMembers);
				group.setNumberOfGroupMember(groupMembers.length);
			}

			String picture = CommonUtil.toNotNull(swoGroup.getPicture());
			if(!picture.equals("")) {
				String extension = picture.lastIndexOf(".") > 0 ? picture.substring(picture.lastIndexOf(".") + 1) : null;
				String pictureId = picture.substring(0, (picture.length() - extension.length())-1);
				group.setBigPictureName(pictureId + "_thumb" + "." + extension);
				group.setSmallPictureName(pictureId + "_thumb" + "." + extension);
			} else {
				group.setBigPictureName(picture);
				group.setSmallPictureName(picture);
			}

			return group;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	private Course convertSwoGroupToCourse(SwoGroup swoGroup, CourseDetail courseDetail) throws Exception {
		Group group = getGroupBySwoGroup(null, swoGroup);
		if (group == null)
			return null;
		if (courseDetail == null)
			return (Course)group;
		
		Course course = (Course)group;
		course.setObject(courseDetail.getObject());
		String[] categoryArray = null;
		String categories = courseDetail.getCategories();
		if (categories != null) {
			categoryArray = StringUtils.tokenizeToStringArray(categories, ",");
		}
		course.setCategories(categoryArray);
		String[] keywordsArray = null;
		String keywords = courseDetail.getKeywords();
		if (keywords != null) {
			keywordsArray = StringUtils.tokenizeToStringArray(keywords, ",");
		}
		course.setKeywords(keywordsArray);
		course.setDuration(courseDetail.getDuration());
		if (courseDetail.getStart() != null)
			course.setOpenDate(new LocalDate(courseDetail.getStart().getTime()));
		if (courseDetail.getEnd() != null)
			course.setCloseDate(new LocalDate(courseDetail.getEnd().getTime()));
		course.setMaxMentees(courseDetail.getMaxMentees());
		course.setAutoApproval(courseDetail.isAutoApproval());
		course.setPayable(course.isPayable());
		course.setFee(courseDetail.getFee());
		//course.setTeam("TEAM");
		course.setTargetPoint(courseDetail.getTargetPoint());
		course.setAchievedPoint(courseDetail.getAchievedPoint());
		
		return course;
	}
	private Course[] convertSwoGroupArrayToCourseArray(SwoGroup[] swoGroups, CourseDetail[] courseDetails) throws Exception {
		if (swoGroups == null)
			return null;
		List<Course> courseList = new ArrayList<Course>();
		for (int i = 0; i < swoGroups.length; i++) {
			SwoGroup swoGroup = swoGroups[i];
			String groupId = swoGroup.getId();
			CourseDetail courseDetail = CourseDetail.pickupCourseDetail(groupId, courseDetails);
			Course course = convertSwoGroupToCourse(swoGroup, courseDetail);
			courseList.add(course);
		}
		Course[] result = new Course[courseList.size()];
		courseList.toArray(result);
		return result;
	}
	private CourseInfo convertSwoGroupToCourseInfo(SwoGroup swoGroup, CourseDetail courseDetail) throws Exception {
		if (swoGroup == null)
			return null;
		GroupInfo group = getGroupInfoBySwoGroup(null, swoGroup);
		
		CourseInfo courseInfo = (CourseInfo)group;
		courseInfo.setOwner(ModelConverter.getUserInfoByUserId(swoGroup.getCreationUser()));
		courseInfo.setLeader(ModelConverter.getUserInfoByUserId(swoGroup.getGroupLeader()));
		courseInfo.setOpenDate(new LocalDate(swoGroup.getCreationDate().getTime()));
		courseInfo.setNumberOfGroupMember(swoGroup.getSwoGroupMembers() == null ? 0 : swoGroup.getSwoGroupMembers().length);
		
		if (courseDetail != null) {
			courseInfo.setTargetPoint(courseDetail.getTargetPoint());
			courseInfo.setAchievedPoint(courseDetail.getAchievedPoint());
//			courseInfo.setLastMission(lastMission);
		}
		
		return courseInfo;
	}
	private CourseInfo[] convertSwoGroupArrayToCourseInfoArray(SwoGroup[] swoGroups, CourseDetail[] courseDetails) throws Exception {
		if (swoGroups == null)
			return null;
		List<CourseInfo> courseInfoList = new ArrayList<CourseInfo>();
		for (int i = 0; i < swoGroups.length; i++) {
			SwoGroup swoGroup = swoGroups[i];
			String groupId = swoGroup.getId();
			CourseDetail courseDetail = CourseDetail.pickupCourseDetail(groupId, courseDetails);
			CourseInfo courseInfo = convertSwoGroupToCourseInfo(swoGroup, courseDetail);
			courseInfoList.add(courseInfo);
		}
		CourseInfo[] result = new CourseInfo[courseInfoList.size()];
		courseInfoList.toArray(result);
		return result;
	}

	@Override
	public String createNewCourse(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		/*
		 {
			frmCreateCourse=
			{
				txtCourseName=제목, 
				txtCourseObject=목적, 
				txtaCourseDesc=설명, 
			*	chkCourseCategories=[예술, 엔터테인먼트, 스타일, 생활], 
				txtCourseKeywords=키워드,키워드2, 
				txtCourseDays=10, 
				txtCourseStartDate=, 
				txtCourseEndDate=, 
				chkCourseSecurity=1, 
				chkCourseUsers=userInput, 
				txtCourseUsers=3, 
				chkJoinApproval=autoApporval, 
				chkCourseFee=free, 
				txtCourseFee=, 
				txtaMentorEducations=학력, 
				txtaMentorWorks=경력, 
				txtaMentorHistory=세라 활동 멘토활동, 
				txtaMenteeHistory=세라활동 멘티활동, 
				txtaMentorLectures=강의 활동, 
				txtaMentorAwards=수상경력, 
				txtaMentorEtc=기타활동, 
				txtCourseMentor={users=[{id=kmyu@maninsoft.co.kr, name=1 유광민}]}, 
				imgCourseProfile={groupId=fg_06737343eb606e45dde9396eb84ef78cb8d7, files=[]}
			}
		}
		 */
		
		User user = SmartUtil.getCurrentUser();
		Map<String, Object> frmNewCourseProfile = (Map<String, Object>)requestBody.get("frmCreateCourse");

		Set<String> keySet = frmNewCourseProfile.keySet();
		Iterator<String> itr = keySet.iterator();
		
		String txtCourseName = null;
		String txtCourseObject = null;
		String txtaCourseDesc = null;
		String chkCourseCategories = null;
		String txtCourseKeywords = null;
		String txtCourseDays = null;
		String chkUserDefineDays = null;
		String txtCourseStartDate = null;
		String txtCourseEndDate = null;
		String chkCourseSecurity = null;
		String chkCourseUsers = null;
		String txtCourseUsers = null;
		String chkJoinApproval = null;
		String chkCourseFee = null;
		String txtCourseFee = null;
		String txtaMentorEducations = null;
		String txtaMentorWorks = null;
		String txtaMentorHistory = null;
		String txtaMenteeHistory = null;
		String txtaMentorLectures = null;
		String txtaMentorAwards = null;
		String txtaMentorEtc = null;
		List<Map<String, String>> txtCourseMentor = null;
		String mentorUserId = null;
		List<Map<String, String>> imgCourseProfile = null;
		String courseFileId = null;
		String courseFileName = null;
		String selGroupProfileType = null;//공개 비공개
		
		String imgGroupProfile = null;

		while (itr.hasNext()) {
			String fieldId = (String)itr.next();
			Object fieldValue = frmNewCourseProfile.get(fieldId);
			if (fieldValue instanceof LinkedHashMap) {
				Map<String, Object> valueMap = (Map<String, Object>)fieldValue;
				if(fieldId.equals("txtCourseMentor")) {
					txtCourseMentor = (ArrayList<Map<String,String>>)valueMap.get("users");
				} else if(fieldId.equals("imgGroupProfile")) {
					imgCourseProfile = (ArrayList<Map<String,String>>)valueMap.get("files");
				}
			} else if(fieldValue instanceof String) {					
				if(fieldId.equals("txtCourseName")) {
					txtCourseName = (String)frmNewCourseProfile.get("txtCourseName");
				} else if(fieldId.equals("txtCourseObject")) {
					txtCourseObject = (String)frmNewCourseProfile.get("txtCourseObject");
				} else if(fieldId.equals("txtaCourseDesc")) {
					txtaCourseDesc = (String)frmNewCourseProfile.get("txtaCourseDesc");
				} else if(fieldId.equals("chkCourseCategories")) {
					chkCourseCategories = (String)frmNewCourseProfile.get("chkCourseCategories");
				} else if(fieldId.equals("txtCourseKeywords")) {
					txtCourseKeywords = (String)frmNewCourseProfile.get("txtCourseKeywords");
				} else if(fieldId.equals("txtCourseDays")) {
					txtCourseDays = (String)frmNewCourseProfile.get("txtCourseDays");
				} else if(fieldId.equals("txtCourseStartDate")) {
					txtCourseStartDate = (String)frmNewCourseProfile.get("txtCourseStartDate");
				} else if(fieldId.equals("txtCourseEndDate")) {
					txtCourseEndDate = (String)frmNewCourseProfile.get("txtCourseEndDate");
				} else if(fieldId.equals("chkCourseSecurity")) {
					chkCourseSecurity = (String)frmNewCourseProfile.get("chkCourseSecurity");
					if(chkCourseSecurity.equals(AccessPolicy.LEVEL_PUBLIC))
						selGroupProfileType = "O";
					else
						selGroupProfileType = "C";
				} else if(fieldId.equals("chkCourseUsers")) {
					chkCourseUsers = (String)frmNewCourseProfile.get("chkCourseUsers");
				} else if(fieldId.equals("txtCourseUsers")) {
					txtCourseUsers = (String)frmNewCourseProfile.get("txtCourseUsers");
				} else if(fieldId.equals("chkJoinApproval")) {
					chkJoinApproval = (String)frmNewCourseProfile.get("chkJoinApproval");
				} else if(fieldId.equals("chkCourseFee")) {
					chkCourseFee = (String)frmNewCourseProfile.get("chkCourseFee");
				} else if(fieldId.equals("txtCourseFee")) {
					txtCourseFee = (String)frmNewCourseProfile.get("txtCourseFee");
				} else if(fieldId.equals("txtaMentorEducations")) {
					txtaMentorEducations = (String)frmNewCourseProfile.get("txtaMentorEducations");
				} else if(fieldId.equals("txtaMentorWorks")) {
					txtaMentorWorks = (String)frmNewCourseProfile.get("txtaMentorWorks");
				} else if(fieldId.equals("txtaMentorHistory")) {
					txtaMentorHistory = (String)frmNewCourseProfile.get("txtaMentorHistory");
				} else if(fieldId.equals("txtaMenteeHistory")) {
					txtaMenteeHistory = (String)frmNewCourseProfile.get("txtaMenteeHistory");
				} else if(fieldId.equals("txtaMentorLectures")) {
					txtaMentorLectures = (String)frmNewCourseProfile.get("txtaMentorLectures");
				} else if(fieldId.equals("txtaMentorAwards")) {
					txtaMentorAwards = (String)frmNewCourseProfile.get("txtaMentorAwards");
				} else if(fieldId.equals("txtaMentorEtc")) {
					txtaMentorEtc = (String)frmNewCourseProfile.get("txtaMentorEtc");
				}
			}
		}
		SwoGroup swoGroup = new SwoGroup();
		swoGroup.setId(IDCreator.createId(SmartServerConstant.GROUP_APPR));

		if(!CommonUtil.isEmpty(txtCourseMentor)) {
			for(int i=0; i < txtCourseMentor.subList(0, txtCourseMentor.size()).size(); i++) {
				Map<String, String> userMap = txtCourseMentor.get(i);
				mentorUserId = userMap.get("id");
			}
		}
		
		SwoGroupMember swoGroupMember = new SwoGroupMember();
		swoGroupMember.setUserId(mentorUserId);
		swoGroupMember.setJoinType("I");
		swoGroupMember.setJoinStatus("P");
		swoGroupMember.setJoinDate(new LocalDate());
		if(!CommonUtil.isEmpty(txtCourseMentor)) {
			for(int i=0; i < txtCourseMentor.subList(0, txtCourseMentor.size()).size(); i++) {
				Map<String, String> userMap = txtCourseMentor.get(i);
				swoGroupMember = new SwoGroupMember();
				swoGroupMember.setUserId(userMap.get("id"));
				swoGroupMember.setJoinType("I");
				swoGroupMember.setJoinStatus("P");
				swoGroupMember.setJoinDate(new LocalDate());
				swoGroup.addGroupMember(swoGroupMember);
			}
		}
		
		if(!CommonUtil.isEmpty(imgCourseProfile)) {
			for(int i=0; i < imgCourseProfile.subList(0, imgCourseProfile.size()).size(); i++) {
				Map<String, String> fileMap = imgCourseProfile.get(i);
				courseFileId = fileMap.get("fileId");
				courseFileName = fileMap.get("fileName");
				imgGroupProfile = SwManagerFactory.getInstance().getDocManager().insertProfilesFile(courseFileId, courseFileName, swoGroup.getId());
				swoGroup.setPicture(imgGroupProfile);
			}
		}

		swoGroup.setCompanyId(user.getCompanyId());
		swoGroup.setName(txtCourseName);
		swoGroup.setDescription(txtaCourseDesc);
		swoGroup.setStatus("C");
		swoGroup.setGroupType(selGroupProfileType);
		swoGroup.setGroupLeader(mentorUserId);

		SwManagerFactory.getInstance().getSwoManager().setGroup(user.getId(), swoGroup, IManager.LEVEL_ALL);

		String groupId = swoGroup.getId();
		if (CommonUtil.isEmpty(groupId))
			return null;

		//코스 확장 정보 저장
		CourseDetail courseDetail = new CourseDetail();
		courseDetail.setCourseId(groupId);
		courseDetail.setObject(txtCourseObject);
		courseDetail.setCategories(chkCourseCategories);
		courseDetail.setKeywords(txtCourseKeywords);
		courseDetail.setDuration(txtCourseDays == null || txtCourseDays == "" ? 0 : Integer.parseInt(txtCourseDays));
		if (txtCourseStartDate != null && !txtCourseStartDate.equalsIgnoreCase("")) {
			Date startDate = new SimpleDateFormat("yyyy.MM.dd").parse(txtCourseStartDate);
			courseDetail.setStart(new LocalDate(startDate.getTime()));
		} else {
			courseDetail.setStart(new LocalDate());
		}
		if (txtCourseEndDate != null && !txtCourseEndDate.equalsIgnoreCase("")) {
			Date endDate = new SimpleDateFormat("yyyy.MM.dd").parse(txtCourseEndDate);
			courseDetail.setEnd(new LocalDate(endDate.getTime()));
		}
		courseDetail.setMaxMentees(txtCourseUsers == null || txtCourseUsers.equals("") ? 0 : Integer.parseInt(txtCourseUsers));
		courseDetail.setAutoApproval(chkJoinApproval != null ? chkJoinApproval.equalsIgnoreCase("autoApporval") ? true : false : true);
		courseDetail.setPayable(chkCourseFee != null ? chkCourseFee.equalsIgnoreCase("free") ? false : true : false);
		courseDetail.setFee(txtCourseFee == null || txtCourseFee.equals("") ? 0 : Integer.parseInt(txtCourseFee));
		//courseDetail.setTeamId("teamId");
		courseDetail.setTargetPoint(10);
		courseDetail.setAchievedPoint(10);
		
		ISeraManager seraMgr = SwManagerFactory.getInstance().getSeraManager();
		seraMgr.setCourseDetail(courseDetail);
		
		//TODO 맨토 정보 저장
		
		MentorDetail mentorDetail = new MentorDetail();
		mentorDetail.setMentorId(mentorUserId);
		mentorDetail.setBorn("born");
		mentorDetail.setHomeTown("homeTown");
		mentorDetail.setLiving("living");
		mentorDetail.setFamily("family");
		mentorDetail.setEducations(txtaMentorEducations);
		mentorDetail.setWorks(txtaMentorWorks);
		mentorDetail.setMentorHistory(txtaMentorHistory);
		mentorDetail.setMenteeHistory(txtaMenteeHistory);
		mentorDetail.setLectures(txtaMentorLectures);
		mentorDetail.setAwards(txtaMentorAwards);
		mentorDetail.setEtc(txtaMentorEtc);
		
		seraMgr.setMentorDetail(mentorUserId, mentorDetail);
		
		return groupId;
	}
	
	@Override
	public CourseList getCoursesById(String userId, int maxList) throws Exception {
		try{
			CourseList courseList = new CourseList();
			if (userId == null || userId.length() == 0)
				return courseList;
			ISwoManager swoManager = SwManagerFactory.getInstance().getSwoManager();
			SwoGroupCond attendingCourseCond = new SwoGroupCond();
			SwoGroupMember[] courseMembers = new SwoGroupMember[1];
			SwoGroupMember courseMember = new SwoGroupMember();
			courseMember.setUserId(userId);
			courseMembers[0] = courseMember;
			attendingCourseCond.setSwoGroupMembers(courseMembers);
			attendingCourseCond.setPageSize(maxList);
			SwoGroup[] attendingCourses = swoManager.getGroups(userId, attendingCourseCond, IManager.LEVEL_ALL);
			
			SwoGroupCond runningCourseCond = new SwoGroupCond();
			runningCourseCond.setGroupLeader(userId);
			runningCourseCond.setPageSize(maxList);
			SwoGroup[] runningCourses = swoManager.getGroups(userId, runningCourseCond, IManager.LEVEL_ALL);
			
			int totalCourseSize = ( attendingCourses == null ? 0 : attendingCourses.length ) + ( runningCourses == null ? 0 : runningCourses.length );
			if (totalCourseSize == 0)
				return courseList;
			
			List<String> courseIdList = new ArrayList<String>();
			if (attendingCourses != null) {
				for(int i = 0; i < attendingCourses.length; i++) {
					SwoGroup course = attendingCourses[i];
					String courseId = course.getId();
					courseIdList.add(courseId);
				}
			}
			if (runningCourses != null) {
				for(int i = 0; i < runningCourses.length; i++) {
					SwoGroup course = runningCourses[i];
					String courseId = course.getId();
					courseIdList.add(courseId);
				}
			}
			
			String[] courseIds = new String[totalCourseSize];
			courseIdList.toArray(courseIds);
			
			CourseDetailCond courseDetailCond = new CourseDetailCond();
			courseDetailCond.setCourseIdIns(courseIds);
			CourseDetail[] courseDetails = SwManagerFactory.getInstance().getSeraManager().getCourseDetails(userId, courseDetailCond);
			
			CourseInfo[] runningCoursesArray = this.convertSwoGroupArrayToCourseInfoArray(runningCourses, courseDetails);
			CourseInfo[] attendingCoursesArray = this.convertSwoGroupArrayToCourseInfoArray(attendingCourses, courseDetails);
			
			courseList.setRunnings(runningCoursesArray == null ? 0 : runningCoursesArray.length);
			courseList.setRunningCourses(runningCoursesArray);
			courseList.setAttendings(attendingCoursesArray == null ? 0 : attendingCoursesArray.length);
			courseList.setAttendingCourses(attendingCoursesArray);
			
			//CourseList courses = SeraTest.getCoursesById(userId, maxList);
			return courseList;
			
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
	}

	@Override
	public CourseInfo[] getCoursesById(String userId, int courseType, LocalDate fromDate, int maxList) throws Exception {
		try{
			ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();
			ISeraManager seraMgr = SwManagerFactory.getInstance().getSeraManager();
			String[] courseIds = seraMgr.getCourseIdArrayByCondition(courseType, userId, fromDate, maxList, 0);
			
			SwoGroupCond groupCond = new SwoGroupCond();
			groupCond.setGroupIdIns(courseIds);
			
			SwoGroup[] groups = swoMgr.getGroups(userId, groupCond, IManager.LEVEL_ALL);
			
			CourseDetailCond courseDetailCond = new CourseDetailCond();
			courseDetailCond.setCourseIdIns(courseIds);
			CourseDetail[] courseDetails = seraMgr.getCourseDetails(userId, courseDetailCond);
			
			CourseInfo[] courses = this.convertSwoGroupArrayToCourseInfoArray(groups, courseDetails);
			
			//CourseInfo[] courses = SeraTest.getCoursesById(userId, courseType, FromDate, maxList);
			return courses;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
	}

	@Override
	public Course getCourseById(String courseId) throws Exception {
		try{
			ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();
			ISeraManager seraMgr = SwManagerFactory.getInstance().getSeraManager();

			SwoGroup group = swoMgr.getGroup("", courseId, IManager.LEVEL_ALL);
			CourseDetail courseDetail = seraMgr.getCourseDetailById(courseId);

			Course course = this.convertSwoGroupToCourse(group, courseDetail);
			//Course course = SeraTest.getCourseById(courseId);
			return course;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
	}

	@Override
	public Mentor getMentorById(String mentorId) throws Exception {

		try{
			Mentor mentor = SeraTest.getMentorById(mentorId);
			return mentor;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
	}

	@Override
	public FriendList getFriendsById(String userId, int maxList) throws Exception{
		try{
			FriendList friendList = SeraTest.getFriendsById(userId, maxList);
			return friendList;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
	}
	
	@Override
	public UserInfo[] getFriendsById(String userId, String lastId, int maxList) throws Exception{
		try{
			UserInfo[] friends = SeraTest.getFriendsById(userId, lastId, maxList);
			return friends;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
		
	}
	
	@Override
	public InstanceInfo[] getCourseNotices(String courseId, LocalDate fromDate, int maxList) throws Exception{
		try{
			InstanceInfo[] notices = SeraTest.getCourseNotices(courseId, fromDate, maxList);
			return notices;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
	}
	@Override
	public InstanceInfo[] getSeraInstances(String userId, String courseId, String missionId, LocalDate fromDate, int maxList) throws Exception{
		try{
			InstanceInfo[] instances = SeraTest.getSeraInstances(userId, courseId, missionId, fromDate, maxList);
			return instances;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
		
	}
	
	@Override
	public ReviewInstanceInfo[] getReviewInstancesByCourse(String courseId, LocalDate fromDate, int maxList) throws Exception{
		try{
			ReviewInstanceInfo[] instances = SeraTest.getReviewInstancesByCourse(courseId, fromDate, maxList);
			return instances;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
		
	}

	@Override
	public MissionInstanceInfo[] getMissionInstanceList(String courseId, LocalDate fromDate, LocalDate toDate) throws Exception {
		try{
			MissionInstanceInfo[] missions = SeraTest.getMissionInstanceList(courseId, fromDate, toDate);
			return missions;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
	}

	@Override
	public MissionInstance getMissionById(String missionId) throws Exception {
		try{
			MissionInstance mission = SeraTest.getMissionById(missionId);
			return mission;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
	}
}
