package net.smartworks.server.service.impl;

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
import net.smartworks.model.sera.Course;
import net.smartworks.model.sera.CourseList;
import net.smartworks.model.sera.FriendList;
import net.smartworks.model.sera.Mentor;
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
		course.setStart(courseDetail.getStart());
		course.setEnd(courseDetail.getEnd());
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
	public Course createNewCourse(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		User user = SmartUtil.getCurrentUser();
		Map<String, Object> frmNewGroupProfile = (Map<String, Object>)requestBody.get("frmNewGroupProfile");

		Set<String> keySet = frmNewGroupProfile.keySet();
		Iterator<String> itr = keySet.iterator();

		List<Map<String, String>> users = null;
		List<Map<String, String>> files = null;
		String groupId = null;
		String txtGroupName = null;
		String txtaGroupDesc = null;
		String selGroupProfileType = null;
		String txtGroupLeader = null;
		String imgGroupProfile = null;
		String groupUserId = null;
		String groupFileId = null;
		String groupFileName = null;

		while (itr.hasNext()) {
			String fieldId = (String)itr.next();
			Object fieldValue = frmNewGroupProfile.get(fieldId);
			if (fieldValue instanceof LinkedHashMap) {
				Map<String, Object> valueMap = (Map<String, Object>)fieldValue;
				if(fieldId.equals("txtGroupMembers")) {
					users = (ArrayList<Map<String,String>>)valueMap.get("users");
				} else if(fieldId.equals("imgGroupProfile")) {
					files = (ArrayList<Map<String,String>>)valueMap.get("files");
				}
			} else if(fieldValue instanceof String) {					
				if(fieldId.equals("groupId")) {
					groupId = (String)frmNewGroupProfile.get("groupId");
				} else if(fieldId.equals("txtGroupName")) {
					txtGroupName = (String)frmNewGroupProfile.get("txtGroupName");
				} else if(fieldId.equals("txtaGroupDesc")) {
					txtaGroupDesc = (String)frmNewGroupProfile.get("txtaGroupDesc");
				} else if(fieldId.equals("selGroupProfileType")) {
					selGroupProfileType = (String)frmNewGroupProfile.get("selGroupProfileType");
					if(selGroupProfileType.equals(Group.GROUP_TYPE_OPEN))
						selGroupProfileType = "O";
					else
						selGroupProfileType = "C";
				} else if(fieldId.equals("txtGroupLeader")) {
					txtGroupLeader = (String)frmNewGroupProfile.get("txtGroupLeader");
				}
				
				//Sera Extends Field Value - Course;
				/*private String courseId;
				private String object;
				private String categories;
				private String keywords;
				private int duration;
				private LocalDate start;
				private LocalDate end;
				private int maxMentees;
				private boolean autoApproval;
				private boolean payable;
				private int fee;
				private String teamId;
				private int targetPoint;
				private int achievedPoint;*/
				
			}
		}

		SwoGroup swoGroup = null;

		if(groupId != null) {
			swoGroup = SwManagerFactory.getInstance().getSwoManager().getGroup(user.getId(), groupId, IManager.LEVEL_ALL);
		} else {
			swoGroup = new SwoGroup();
			swoGroup.setId(IDCreator.createId(SmartServerConstant.GROUP_APPR));
		}

		SwoGroupMember swoGroupMember = new SwoGroupMember();
		swoGroupMember.setUserId(txtGroupLeader);
		swoGroupMember.setJoinType("I");
		swoGroupMember.setJoinStatus("P");
		swoGroupMember.setJoinDate(new LocalDate());
		swoGroup.addGroupMember(swoGroupMember);
		if(!CommonUtil.isEmpty(users)) {
			for(int i=0; i < users.subList(0, users.size()).size(); i++) {
				Map<String, String> userMap = users.get(i);
				groupUserId = userMap.get("id");
				if(!txtGroupLeader.equals(groupUserId)) {
					swoGroupMember = new SwoGroupMember();
					swoGroupMember.setUserId(groupUserId);
					swoGroupMember.setJoinType("I");
					swoGroupMember.setJoinStatus("P");
					swoGroupMember.setJoinDate(new LocalDate());
					swoGroup.addGroupMember(swoGroupMember);
				}
			}
		}

		if(!files.isEmpty()) {
			for(int i=0; i < files.subList(0, files.size()).size(); i++) {
				Map<String, String> fileMap = files.get(i);
				groupFileId = fileMap.get("fileId");
				groupFileName = fileMap.get("fileName");
				imgGroupProfile = SwManagerFactory.getInstance().getDocManager().insertProfilesFile(groupFileId, groupFileName, swoGroup.getId());
				swoGroup.setPicture(imgGroupProfile);
			}
		}

		swoGroup.setCompanyId(user.getCompanyId());
		swoGroup.setName(txtGroupName);
		swoGroup.setDescription(txtaGroupDesc);
		swoGroup.setStatus("C");
		swoGroup.setGroupType(selGroupProfileType);
		swoGroup.setGroupLeader(txtGroupLeader);

		SwManagerFactory.getInstance().getSwoManager().setGroup(user.getId(), swoGroup, IManager.LEVEL_ALL);

		groupId = swoGroup.getId();

		if (CommonUtil.isEmpty(groupId))
			return null;
		
		
		
		
		
		
		
		
		
		
		
		return null;
	}
	
	@Override
	public CourseList getCoursesById(String userId, int maxList) throws Exception {
		try{
			if (userId == null || userId.length() == 0)
				return null;
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
				return null;
			
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
			
			CourseList courseList = new CourseList();
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
	public InstanceInfo[] getSeraInstancesByUser(String userId, LocalDate fromDate, int maxList) throws Exception{
		try{
			InstanceInfo[] instances = SeraTest.getSeraInstancesByUser(userId, fromDate, maxList);
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
}
