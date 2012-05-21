package net.smartworks.server.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import net.smartworks.model.community.Community;
import net.smartworks.model.community.Group;
import net.smartworks.model.community.User;
import net.smartworks.model.community.info.CommunityInfo;
import net.smartworks.model.community.info.DepartmentInfo;
import net.smartworks.model.community.info.GroupInfo;
import net.smartworks.model.community.info.InstanceSpaceInfo;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.community.info.WorkSpaceInfo;
import net.smartworks.model.filter.Condition;
import net.smartworks.model.filter.SearchFilter;
import net.smartworks.model.instance.CommentInstance;
import net.smartworks.model.instance.FieldData;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.SortingField;
import net.smartworks.model.instance.WorkInstance;
import net.smartworks.model.instance.info.BoardInstanceInfo;
import net.smartworks.model.instance.info.CommentInstanceInfo;
import net.smartworks.model.instance.info.EventInstanceInfo;
import net.smartworks.model.instance.info.InstanceInfo;
import net.smartworks.model.instance.info.InstanceInfoList;
import net.smartworks.model.instance.info.RequestParams;
import net.smartworks.model.instance.info.WorkInstanceInfo;
import net.smartworks.model.notice.Notice;
import net.smartworks.model.security.AccessPolicy;
import net.smartworks.model.sera.Constants;
import net.smartworks.model.sera.Course;
import net.smartworks.model.sera.CourseList;
import net.smartworks.model.sera.FriendInformList;
import net.smartworks.model.sera.FriendList;
import net.smartworks.model.sera.MemberInformList;
import net.smartworks.model.sera.MenteeInformList;
import net.smartworks.model.sera.Mentor;
import net.smartworks.model.sera.MissionInstance;
import net.smartworks.model.sera.SeraBoardList;
import net.smartworks.model.sera.SeraNotice;
import net.smartworks.model.sera.SeraUser;
import net.smartworks.model.sera.Team;
import net.smartworks.model.sera.info.CourseInfo;
import net.smartworks.model.sera.info.MissionInstanceInfo;
import net.smartworks.model.sera.info.MissionReportInstanceInfo;
import net.smartworks.model.sera.info.NoteInstanceInfo;
import net.smartworks.model.sera.info.ReviewInstanceInfo;
import net.smartworks.model.sera.info.SeraUserInfo;
import net.smartworks.model.sera.info.TeamInfo;
import net.smartworks.model.work.FormField;
import net.smartworks.model.work.SmartWork;
import net.smartworks.model.work.SocialWork;
import net.smartworks.model.work.Work;
import net.smartworks.model.work.info.SmartWorkInfo;
import net.smartworks.model.work.info.WorkCategoryInfo;
import net.smartworks.model.work.info.WorkInfo;
import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.model.Filter;
import net.smartworks.server.engine.common.model.Filters;
import net.smartworks.server.engine.common.model.Order;
import net.smartworks.server.engine.common.model.Property;
import net.smartworks.server.engine.common.model.SmartServerConstant;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.common.util.StringUtil;
import net.smartworks.server.engine.common.util.id.IDCreator;
import net.smartworks.server.engine.docfile.exception.DocFileException;
import net.smartworks.server.engine.docfile.manager.IDocFileManager;
import net.smartworks.server.engine.docfile.model.IFileModel;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.infowork.domain.manager.ISwdManager;
import net.smartworks.server.engine.infowork.domain.model.SwdDataField;
import net.smartworks.server.engine.infowork.domain.model.SwdDomain;
import net.smartworks.server.engine.infowork.domain.model.SwdDomainCond;
import net.smartworks.server.engine.infowork.domain.model.SwdField;
import net.smartworks.server.engine.infowork.domain.model.SwdFieldCond;
import net.smartworks.server.engine.infowork.domain.model.SwdRecord;
import net.smartworks.server.engine.infowork.domain.model.SwdRecordCond;
import net.smartworks.server.engine.infowork.domain.model.SwdRecordExtend;
import net.smartworks.server.engine.infowork.form.manager.ISwfManager;
import net.smartworks.server.engine.infowork.form.model.SwfField;
import net.smartworks.server.engine.infowork.form.model.SwfForm;
import net.smartworks.server.engine.infowork.form.model.SwfFormCond;
import net.smartworks.server.engine.infowork.form.model.SwfFormModel;
import net.smartworks.server.engine.opinion.model.Opinion;
import net.smartworks.server.engine.opinion.model.OpinionCond;
import net.smartworks.server.engine.organization.manager.ISwoManager;
import net.smartworks.server.engine.organization.model.SwoCompany;
import net.smartworks.server.engine.organization.model.SwoCompanyCond;
import net.smartworks.server.engine.organization.model.SwoGroup;
import net.smartworks.server.engine.organization.model.SwoGroupCond;
import net.smartworks.server.engine.organization.model.SwoGroupMember;
import net.smartworks.server.engine.organization.model.SwoUser;
import net.smartworks.server.engine.organization.model.SwoUserCond;
import net.smartworks.server.engine.organization.model.SwoUserExtend;
import net.smartworks.server.engine.process.task.model.TskTask;
import net.smartworks.server.engine.process.task.model.TskTaskCond;
import net.smartworks.server.engine.sera.manager.ISeraManager;
import net.smartworks.server.engine.sera.model.CourseDetail;
import net.smartworks.server.engine.sera.model.CourseDetailCond;
import net.smartworks.server.engine.sera.model.CourseReview;
import net.smartworks.server.engine.sera.model.CourseReviewCond;
import net.smartworks.server.engine.sera.model.CourseTeam;
import net.smartworks.server.engine.sera.model.CourseTeamCond;
import net.smartworks.server.engine.sera.model.CourseTeamUser;
import net.smartworks.server.engine.sera.model.MentorDetail;
import net.smartworks.server.engine.sera.model.SeraConstant;
import net.smartworks.server.engine.sera.model.SeraFriend;
import net.smartworks.server.engine.sera.model.SeraFriendCond;
import net.smartworks.server.engine.sera.model.SeraUserDetail;
import net.smartworks.server.engine.sera.model.SeraUserDetailCond;
import net.smartworks.server.service.ICalendarService;
import net.smartworks.server.service.ICommunityService;
import net.smartworks.server.service.IInstanceService;
import net.smartworks.server.service.ISeraService;
import net.smartworks.server.service.factory.SwServiceFactory;
import net.smartworks.server.service.util.ModelConverter;
import net.smartworks.service.ISmartWorks;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SeraServiceImpl implements ISeraService {

	private ISeraManager getSeraManager() {
		return SwManagerFactory.getInstance().getSeraManager();
	}
	private ISwoManager getSwoManager() {
		return SwManagerFactory.getInstance().getSwoManager();
	}
	private ISwfManager getSwfManager() {
		return SwManagerFactory.getInstance().getSwfManager();
	}
	private ISwdManager getSwdManager() {
		return SwManagerFactory.getInstance().getSwdManager();
	}

	private IInstanceService instanceService;
	private ICalendarService calendarService;
	private AuthenticationManager authenticationManager;

	@Autowired
	public void setInstanceService(IInstanceService instanceService) {
		this.instanceService = instanceService;
	}
	@Autowired
	public void setCalendarService(ICalendarService calendarService) {
		this.calendarService = calendarService;
	}
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

	private CourseInfo getCourseInfoById(String courseId) throws Exception {
		ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();
		ISeraManager seraMgr = SwManagerFactory.getInstance().getSeraManager();
		SwoGroup group = swoMgr.getGroup("", courseId, IManager.LEVEL_ALL);
		CourseDetail courseDetail = seraMgr.getCourseDetailById(courseId);
		CourseInfo courseInfo = this.convertSwoGroupToCourseInfo(group, courseDetail);
		return courseInfo;
	}
	private WorkSpaceInfo getWorkSpaceInfoBySwdRecord(SwdRecord record) throws Exception {
		if (record == null)
			return null;
		
		String workSpaceType = record.getWorkSpaceType();
		String workSpaceId = record.getWorkSpaceId();
		if (CommonUtil.isEmpty(workSpaceType) || CommonUtil.isEmpty(workSpaceId))
			return null;
		
		if (workSpaceType.equals(ISmartWorks.SPACE_TYPE_WORK_INSTANCE + "")) {
			MissionInstanceInfo missionInstanceInfo = getMissionInfoById(workSpaceId);
			InstanceSpaceInfo workSpaceInfo = new InstanceSpaceInfo(workSpaceId, missionInstanceInfo.getSubject());
			workSpaceInfo.setInstance(missionInstanceInfo);
			return workSpaceInfo;
		} else if (workSpaceType.equals(ISmartWorks.SPACE_TYPE_GROUP + "")) {
			return getCourseInfoById(workSpaceId);
		} else if (workSpaceType.equals(ISmartWorks.SPACE_TYPE_USER + "")) {
			return ModelConverter.getUserInfoByUserId(workSpaceId);
		} else {
			return null;
		}
	}
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
			groupInfo.setSmallPictureName(pictureId + Community.IMAGE_TYPE_THUMB + "." + extension);
			groupInfo.setBigPictureName(pictureId + Community.IMAGE_TYPE_ORIGINAL + "." + extension);
		} else {
			groupInfo.setSmallPictureName(picture);
			groupInfo.setBigPictureName(picture);
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
			group.setCreatedDate(new LocalDate(swoGroup.getCreationDate().getTime()));
			group.setPublic(swoGroup.getGroupType().equals("O") ? true : false);
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
				//groupMemberList.add(ModelConverter.getUserInfoByUserId(swoGroup.getGroupLeader()));
				for(SwoGroupMember swoGroupMember : swoGroupMembers) {
					if(swoGroupMember != null) {
						if(!swoGroupMember.getUserId().equals(swoGroup.getGroupLeader())) {
							UserInfo groupMember = ModelConverter.getUserInfoByUserId(swoGroupMember.getUserId());
							groupMemberList.add(groupMember);
						}
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
				group.setBigPictureName(pictureId + Community.IMAGE_TYPE_THUMB + "." + extension);
				group.setSmallPictureName(pictureId + Community.IMAGE_TYPE_THUMB + "." + extension);
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
		course.setLastMissionIndex(courseDetail.getLastMissionIndex());
		SwoGroupMember[] swoGroupMembers = swoGroup.getSwoGroupMembers();
		List<UserInfo> userinfoList = new ArrayList<UserInfo>();
		UserInfo[] members = null;
		if(!CommonUtil.isEmpty(swoGroupMembers)) {
			for(SwoGroupMember swoGroupMember : swoGroupMembers) {
				if(swoGroupMember != null) {
					if(!swoGroupMember.getUserId().equals(swoGroup.getGroupLeader()))
						userinfoList.add(ModelConverter.getUserInfoByUserId(swoGroupMember.getUserId()));
				}
			}
		}
		if(userinfoList.size() > 0) {
			members = new UserInfo[userinfoList.size()];
			userinfoList.toArray(members);
		}
		course.setMembers(members);

		User user = SmartUtil.getCurrentUser();
		String userId = user.getId();
		String courseId = swoGroup.getId();
		course.setMissions(getMissionInstanceList(course.getId(), null, null));

		boolean isJoinCourse = false;
		CourseList courseList = getCoursesById(userId, -1);
		if(!SmartUtil.isBlankObject(courseList)) {
			CourseInfo[] attendingCourses = courseList.getAttendingCourses();
			CourseInfo[] runningCourses = courseList.getRunningCourses();
			if(!CommonUtil.isEmpty(attendingCourses)) {
				for(CourseInfo courseInfo : attendingCourses) {
					if(courseId.equals(courseInfo.getId())) {
						isJoinCourse = true;
						break;
					}		
				}
			}
			if(!CommonUtil.isEmpty(runningCourses)) {
				if(isJoinCourse != true) {
					for(CourseInfo courseInfo : runningCourses) {
						if(courseId.equals(courseInfo.getId())) {
							isJoinCourse = true;
							break;
						}		
					}
				}
			}
		}
		course.setJoinCourse(isJoinCourse);

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
		courseInfo.setNumberOfGroupMember(swoGroup.getSwoGroupMembers() == null ? 0 : swoGroup.getSwoGroupMembers().length);
		
		if (courseDetail != null) {
			if (courseDetail.getStart() != null)
				courseInfo.setOpenDate(new LocalDate(courseDetail.getStart().getTime()));
			if (courseDetail.getEnd() != null)
				courseInfo.setCloseDate(new LocalDate(courseDetail.getEnd().getTime()));
//			courseInfo.setLastMission(lastMission);
			if (courseDetail.getCategories() != null)
				courseInfo.setCategory(courseDetail.getCategories());
		}
		
		return courseInfo;
	}
	private CourseInfo[] convertSwoGroupArrayToCourseInfoArray(SwoGroup[] swoGroups, CourseDetail[] courseDetails) throws Exception {
		if (swoGroups == null)
			return null;
		List<CourseInfo> courseInfoList = new ArrayList<CourseInfo>();
		for (int i = 0; i < swoGroups.length; i++) {
			SwoGroup swoGroup = swoGroups[i];
			if(!CommonUtil.isEmpty(swoGroup)) {
				String groupId = swoGroup.getId();
				CourseDetail courseDetail = CourseDetail.pickupCourseDetail(groupId, courseDetails);
				CourseInfo courseInfo = convertSwoGroupToCourseInfo(swoGroup, courseDetail);
				courseInfoList.add(courseInfo);
			}
		}
		CourseInfo[] result = new CourseInfo[courseInfoList.size()];
		courseInfoList.toArray(result);
		return result;
	}

	int previousPageSize = 0;
	private InstanceInfoList getIWorkInstanceList(String workId, String missionId, RequestParams params) throws Exception {
		//missionId != null 이면 해당 아이디의 미션만을 가져온다
		try {
			User user = SmartUtil.getCurrentUser();
			if(user == null)
				return null;
			String userId = user.getId();

			ISwfManager swfMgr = SwManagerFactory.getInstance().getSwfManager();
			ISwdManager swdMgr = SwManagerFactory.getInstance().getSwdManager();
			IDocFileManager docMgr = SwManagerFactory.getInstance().getDocManager();
			
			SwdDomainCond swdDomainCond = new SwdDomainCond();
			swdDomainCond.setCompanyId(user.getCompanyId());
	
			SwfFormCond swfFormCond = new SwfFormCond();
			swfFormCond.setCompanyId(user.getCompanyId());
			swfFormCond.setPackageId(workId);

			swdDomainCond.setFormId(swfMgr.getForms(userId, swfFormCond, IManager.LEVEL_LITE)[0].getId());

			SwdDomain swdDomain = swdMgr.getDomain(userId, swdDomainCond, IManager.LEVEL_LITE);

			if(swdDomain == null)
				return null;

			SwdRecordCond swdRecordCond = new SwdRecordCond();
			swdRecordCond.setCompanyId(user.getCompanyId());
			swdRecordCond.setFormId(swdDomain.getFormId());
			swdRecordCond.setDomainId(swdDomain.getObjId());

			SearchFilter searchFilter = params.getSearchFilter();
			List<Filter> filterList = new ArrayList<Filter>();
			if(searchFilter != null) {
				Condition[] conditions = searchFilter.getConditions();
				if (conditions != null) {
					for(Condition condition : conditions) {
						Filter filter = new Filter();
		
						FormField leftOperand = condition.getLeftOperand();
						String formFieldId = leftOperand.getId();
						String tableColName = formFieldId;
						if(!formFieldId.equals("workSpaceId") &&!formFieldId.equals(FormField.ID_OWNER) && !formFieldId.equals(FormField.ID_CREATED_DATE) && !formFieldId.equals(FormField.ID_LAST_MODIFIER) && !formFieldId.equals(FormField.ID_LAST_MODIFIED_DATE))
							tableColName = swdMgr.getTableColName(swdDomain.getObjId(), formFieldId);
	
						String formFieldType = leftOperand.getType();
						String operator = condition.getOperator();
						String rightOperand = (String)condition.getRightOperand();
	
						filter.setLeftOperandType(formFieldType);
						filter.setLeftOperandValue(tableColName);
						filter.setOperator(operator);
						filter.setRightOperandType(formFieldType);
						filter.setRightOperandValue(rightOperand);
						filterList.add(filter);
					}
					Filter[] filters = new Filter[filterList.size()];
					filterList.toArray(filters);
	
					swdRecordCond.setFilter(filters);
				}
			}

			String filterId = params.getFilterId();

			LocalDate priviousDate = new LocalDate(new LocalDate().getTime() - LocalDate.ONE_DAY*7);

			if(filterId != null) {
				if(filterId.equals(SearchFilter.FILTER_ALL_INSTANCES)) {
				} else if(filterId.equals(SearchFilter.FILTER_MY_INSTANCES)) {
					swdRecordCond.addFilter(new Filter("=", FormField.ID_LAST_MODIFIER, Filter.OPERANDTYPE_STRING, userId));
				} else if(filterId.equals(SearchFilter.FILTER_RECENT_INSTANCES)) {
					swdRecordCond.addFilter(new Filter(">=", FormField.ID_LAST_MODIFIED_DATE, Filter.OPERANDTYPE_DATE, priviousDate.toGMTSimpleDateString()));
				} else if(filterId.equals(SearchFilter.FILTER_MY_RECENT_INSTANCES)) {
					swdRecordCond.addFilter(new Filter("=", FormField.ID_LAST_MODIFIER, Filter.OPERANDTYPE_STRING, userId));
					swdRecordCond.addFilter(new Filter(">=", FormField.ID_LAST_MODIFIED_DATE, Filter.OPERANDTYPE_DATE, priviousDate.toGMTSimpleDateString()));
				} else {
					searchFilter = ModelConverter.getSearchFilterByFilterId(SwfFormModel.TYPE_SINGLE, workId, filterId);
					Condition[] conditions = searchFilter.getConditions();
					Filters filters = new Filters();
					filterList = new ArrayList<Filter>();
					for(Condition condition : conditions) {
						Filter filter = new Filter();
						FormField leftOperand = condition.getLeftOperand();
						String lefOperandType = leftOperand.getType();
						String operator = condition.getOperator();
						Object rightOperand = condition.getRightOperand();
						String rightOperandValue = "";
						if(rightOperand instanceof User) {
							rightOperandValue = ((User)rightOperand).getId();
						} else if(rightOperand instanceof Work) {
							rightOperandValue = ((Work)rightOperand).getId();
						} else {
							if(lefOperandType.equals(FormField.TYPE_DATETIME)) rightOperandValue = ((LocalDate)rightOperand).toGMTDateString();
							else if(lefOperandType.equals(FormField.TYPE_DATE)) rightOperandValue = ((LocalDate)rightOperand).toGMTSimpleDateString2();
							else if(lefOperandType.equals(FormField.TYPE_TIME)) rightOperandValue = ((LocalDate)rightOperand).toGMTTimeString2();
							else rightOperandValue = (String)rightOperand;
						}
						filter.setLeftOperandType(lefOperandType);
						filter.setLeftOperandValue(leftOperand.getId());
						filter.setOperator(operator);
						filter.setRightOperandType(lefOperandType);
						filter.setRightOperandValue(rightOperandValue);
						filterList.add(filter);
					}
					Filter[] searchfilters = null;
					if(filterList.size() != 0) {
						searchfilters = new Filter[filterList.size()];
						filterList.toArray(searchfilters);
						filters.setFilter(searchfilters);
					}
					swdRecordCond.addFilters(filters);
				}
			}

			String searchKey = params.getSearchKey();
			if(!CommonUtil.isEmpty(searchKey))
				swdRecordCond.setSearchKey(searchKey);

			long totalCount = swdMgr.getRecordSize(userId, swdRecordCond);

			SortingField sf = params.getSortingField();
			String columnName = "";
			boolean isAsc;

			if (sf != null) {
				columnName  = CommonUtil.toDefault(sf.getFieldId(), FormField.ID_LAST_MODIFIED_DATE);
				isAsc = sf.isAscending();
			} else {
				columnName = FormField.ID_LAST_MODIFIED_DATE;
				isAsc = false;
			}
			SortingField sortingField = new SortingField();
			sortingField.setFieldId(columnName);
			sortingField.setAscending(isAsc);

			swdRecordCond.setOrders(new Order[]{new Order(columnName, isAsc)});

			int pageSize = params.getPageSize();
			if(pageSize == 0) pageSize = 20;

			int currentPage = params.getCurrentPage();
			if(currentPage == 0) currentPage = 1;

			int totalPages = (int)totalCount % pageSize;

			if(totalPages == 0)
				totalPages = (int)totalCount / pageSize;
			else
				totalPages = (int)totalCount / pageSize + 1;

			int result = 0;

			if(params.getPagingAction() != 0) {
				if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXT10) {
					result = (((currentPage - 1) / 10) * 10) + 11;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXTEND) {
					result = totalPages;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREV10) {
					result = ((currentPage - 1) / 10) * 10;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREVEND) {
					result = 1;
				}
				currentPage = result;
			}

			if(previousPageSize != pageSize)
				currentPage = 1;

			previousPageSize = pageSize;

			if((long)((pageSize * (currentPage - 1)) + 1) > totalCount)
				currentPage = 1;

			if (currentPage > 0)
				swdRecordCond.setPageNo(currentPage-1);

			swdRecordCond.setPageSize(pageSize);
			if (missionId != null)
				swdRecordCond.setRecordId(missionId);

			SwdRecord[] swdRecords = swdMgr.getRecords(userId, swdRecordCond, IManager.LEVEL_LITE);

			SwdRecordExtend[] swdRecordExtends = swdMgr.getCtgPkg(workId);

			//SwdField[] swdFields = getSwdManager().getViewFieldList(workId, swdDomain.getFormId());

			SwfForm[] swfForms = swfMgr.getForms(userId, swfFormCond, IManager.LEVEL_ALL);
			SwfField[] swfFields = swfForms[0].getFields();

			InstanceInfoList instanceInfoList = new InstanceInfoList();

			String formId = swdDomain.getFormId();
			String formName = swdDomain.getFormName();
			String titleFieldId = swdDomain.getTitleFieldId();

			List<MissionInstanceInfo> missionInstanceInfoList = new ArrayList<MissionInstanceInfo>();
			MissionInstanceInfo[] missionInstanceInfos = null;
			if(!CommonUtil.isEmpty(swdRecords)) {
				int swdRecordsLength = swdRecords.length;
				for(int i = 0; i < swdRecordsLength; i++) {
					SwdRecord swdRecord = swdRecords[i];
					MissionInstanceInfo missionInstanceInfo = new MissionInstanceInfo();

					missionInstanceInfo.setLikers(ModelConverter.getLikersUserIdArray(userId, Instance.TYPE_SERA_MISSION, swdRecord.getRecordId()));
					
					String creationUser = swdRecord.getCreationUser();
					Date creationDate = swdRecord.getCreationDate();
					String modificationUser = swdRecord.getModificationUser();
					Date modificationDate = swdRecord.getModificationDate();
					if(creationUser == null)
						creationUser = User.USER_ID_NONE_EXISTING;
					if(creationDate == null)
						creationDate = new Date();
					UserInfo owner = ModelConverter.getUserInfoByUserId(creationUser);
					LocalDate createdDate = new LocalDate(creationDate.getTime());
					UserInfo lastModifier = modificationUser != null ? ModelConverter.getUserInfoByUserId(modificationUser) : owner;
					LocalDate lastModifiedDate = modificationDate != null ? new LocalDate(modificationDate.getTime()) : createdDate;

					missionInstanceInfo.setId(swdRecord.getRecordId());
					missionInstanceInfo.setOwner(owner);
					missionInstanceInfo.setCreatedDate(createdDate);
					missionInstanceInfo.setLastModifier(lastModifier);
					missionInstanceInfo.setLastModifiedDate(lastModifiedDate);
					missionInstanceInfo.setStatus(WorkInstance.STATUS_COMPLETED);
					String workSpaceId = swdRecord.getWorkSpaceId();
					if(CommonUtil.isEmpty(workSpaceId))
						workSpaceId = userId;

					WorkSpaceInfo workSpaceInfo = getCourseInfoById(workSpaceId);

					missionInstanceInfo.setWorkSpace(workSpaceInfo);

					
					SwdRecordCond cond = new SwdRecordCond();
					cond.setWorkSpaceId(swdRecord.getRecordId());
					cond.setFormId(SeraConstant.MISSION_REPORT_FORMID);
					SwdRecord[] records = swdMgr.getRecords(user.getId(), cond, IManager.LEVEL_LITE);
					if (records != null && records.length != 0) {
						String[] clearers = new String[records.length];
						for (int j = 0; j < records.length; j++) {
							SwdRecord record = records[j];
							clearers[j] = record.getCreationUser();
						}
						missionInstanceInfo.setMissionClearers(clearers);
					}
					
					WorkCategoryInfo groupInfo = null;
					if (!CommonUtil.isEmpty(swdRecordExtends[0].getSubCtgId()))
						groupInfo = new WorkCategoryInfo(swdRecordExtends[0].getSubCtgId(), swdRecordExtends[0].getSubCtg());
		
					WorkCategoryInfo categoryInfo = new WorkCategoryInfo(swdRecordExtends[0].getParentCtgId(), swdRecordExtends[0].getParentCtg());
		
					//WorkInfo workInfo1 = new SmartWorkInfo(formId, formName, missionInstanceInfo.getWork().getType(), groupInfo, categoryInfo);
					WorkInfo workInfo = ModelConverter.getWorkInfoByPackageId(swfForms[0].getPackageId());
					
					missionInstanceInfo.setWork(workInfo);
					//missionInstanceInfo.setViews(swdRecord.getHits());
					SwdDataField[] swdDataFields = swdRecord.getDataFields();

					if(!CommonUtil.isEmpty(swdDataFields)) {
						int swdDataFieldsLength = swdDataFields.length;
						for(int j=0; j<swdDataFieldsLength; j++) {
							SwdDataField swdDataField = swdDataFields[j];
							if (swdDataField.getId().equals(SeraConstant.MISSION_TITLEFIELDID)) {
								missionInstanceInfo.setSubject(swdDataField.getValue());
							} else if (swdDataField.getId().equals(SeraConstant.MISSION_OPENDATEFIELDID)) {
								missionInstanceInfo.setOpenDate(LocalDate.convertGMTStringToLocalDate(swdDataField.getValue()));
							} else if (swdDataField.getId().equals(SeraConstant.MISSION_CLOSEDATEFIELDID)) {
								missionInstanceInfo.setCloseDate(LocalDate.convertGMTStringToLocalDate(swdDataField.getValue()));
							} else if (swdDataField.getId().equals(SeraConstant.MISSION_PREVMISSIONFIELDID)) {
								missionInstanceInfo.setPrevMission(getMissionInfoById(swdDataField.getValue()));
							} else if (swdDataField.getId().equals(SeraConstant.MISSION_CONTENTFIELDID)) {
								missionInstanceInfo.setContent(swdDataField.getValue());
							} else if (swdDataField.getId().equals(SeraConstant.MISSION_INDEXFIELDID)) {
								missionInstanceInfo.setIndex(Integer.parseInt(swdDataField.getValue()));
							} else if (swdDataField.getId().equals(SeraConstant.MISSION_FILESFIELDID)) {
								missionInstanceInfo.setFileGroupId(swdDataField.getValue());
								List<IFileModel> docFileList = docMgr.findFileGroup(swdDataField.getValue());
								if (docFileList != null && docFileList.size() != 0) {
									List<Map<String, String>> fileList = new ArrayList<Map<String, String>>();
									for (int k = 0 ; k <docFileList.size(); k++) {
										Map<String, String> fileMap = new HashMap<String, String>();
										IFileModel docFile = docFileList.get(k);
										fileMap.put("fileId", docFile.getId());
										fileMap.put("fileName", docFile.getFileName());
										fileMap.put("fileSize", docFile.getFileSize()+"");
										fileList.add(fileMap);
									}
									missionInstanceInfo.setFiles(fileList);
								}
							}
						}
					}
					//missionInstanceInfo.setDisplayDatas(fieldDatas);

/*					boolean isAccess = false;

					if(workSpaceId.equals(userId))
						isAccess = true;
					if(!isAccess) {
						DepartmentInfo[] myDepartments = communityService.getMyDepartments();
						if(!CommonUtil.isEmpty(myDepartments)) {
							for(DepartmentInfo myDepartment : myDepartments) {
								String myDepartmentId = myDepartment.getId();
								if(workSpaceId.equals(myDepartmentId)) {
									isAccess = true;
									break;
								}
							}
						}
					}
					if(!isAccess) {
						GroupInfo[] myGroups = communityService.getMyGroups();
						if(!CommonUtil.isEmpty(myGroups)) {
							for(GroupInfo myGroup : myGroups) {
								String myGroupId = myGroup.getId();
								if(workSpaceId.equals(myGroupId)) {
									isAccess = true;
									break;
								}
							}
						}
					}*/

					String accessLevel = swdRecord.getAccessLevel();
					String accessValue = swdRecord.getAccessValue();

					//if(isAccess) { 
						if(!CommonUtil.isEmpty(accessLevel)) {
							if(Integer.parseInt(accessLevel) == AccessPolicy.LEVEL_PRIVATE) {
								if(owner.equals(userId) || modificationUser.equals(userId))
									missionInstanceInfoList.add(missionInstanceInfo);
							} else if(Integer.parseInt(accessLevel) == AccessPolicy.LEVEL_CUSTOM) {
								if(!CommonUtil.isEmpty(accessValue)) {
									String[] accessValues = accessValue.split(";");
									if(!CommonUtil.isEmpty(accessValues)) {
										for(String value : accessValues) {
											if(!owner.equals(value) && !modificationUser.equals(value)) {
												if(accessValue.equals(userId))
													missionInstanceInfoList.add(missionInstanceInfo);
											}
										}
										if(owner.equals(userId) || modificationUser.equals(userId))
											missionInstanceInfoList.add(missionInstanceInfo);
									}
								}
							} else {
								missionInstanceInfoList.add(missionInstanceInfo);
							}
						} else {
							missionInstanceInfoList.add(missionInstanceInfo);
						}
					//}
				}
				if(!CommonUtil.isEmpty(missionInstanceInfoList)) {
					missionInstanceInfos = new MissionInstanceInfo[missionInstanceInfoList.size()];
					missionInstanceInfoList.toArray(missionInstanceInfos);
				}
				instanceInfoList.setInstanceDatas(missionInstanceInfos);
			}

			instanceInfoList.setTotalSize((int)totalCount);
			instanceInfoList.setSortedField(sortingField);
			instanceInfoList.setType(InstanceInfoList.TYPE_INFORMATION_INSTANCE_LIST);
			instanceInfoList.setPageSize(pageSize);
			instanceInfoList.setTotalPages(totalPages);
			instanceInfoList.setCurrentPage(currentPage);

			return instanceInfoList;
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public String createNewMission(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		/*{
			courseId=group_27c8e4008cfe4975bdbc16f85e2f8623, 
			frmCreateMission=
				{
					txtMissionName=제목, 
					txtMissionOpenDate=2012.04.04, 
					txtMissionCloseDate=2012.04.11, 
					selPrevMission=, 
					txtFileField={groupId=fg_d299186ecf640c4407cbd69c0dc3a2c4bc30, files=[]},
					txtaMissionContent=미션 내용
				}
		}*/

		User user = SmartUtil.getCurrentUser();
		String userId = user.getId();
		String courseId = (String)requestBody.get("courseId");
		Map<String, Object> frmNewMissionProfile = (Map<String, Object>)requestBody.get("frmCreateMission");

		Set<String> keySet = frmNewMissionProfile.keySet();
		Iterator<String> itr = keySet.iterator();
		
		String txtMissionName = null;
		String txtMissionOpenDate = null;
		String txtMissionCloseDate = null;
		String selPrevMission = null;
		String txtaMissionContent = null;

		Map<String, Object> txtFileField = null;
		String fileGroupId = null;
		Map<String, List<Map<String, String>>> fileGroupMap = new HashMap<String, List<Map<String, String>>>();

		
		while (itr.hasNext()) {
			String fieldId = (String)itr.next();
			Object fieldValue = frmNewMissionProfile.get(fieldId);
			if(fieldValue instanceof String) {					
				if(fieldId.equals("txtMissionName")) {
					txtMissionName = (String)frmNewMissionProfile.get("txtMissionName");
				} else if(fieldId.equals("txtMissionOpenDate")) {
					txtMissionOpenDate = (String)frmNewMissionProfile.get("txtMissionOpenDate");
				} else if(fieldId.equals("txtMissionCloseDate")) {
					txtMissionCloseDate = (String)frmNewMissionProfile.get("txtMissionCloseDate");
				} else if(fieldId.equals("selPrevMission")) {
					selPrevMission = (String)frmNewMissionProfile.get("selPrevMission");
				} else if(fieldId.equals("txtaMissionContent")) {
					txtaMissionContent = (String)frmNewMissionProfile.get("txtaMissionContent");
				}
			} else if (fieldValue instanceof LinkedHashMap) {
				if (fieldId.equalsIgnoreCase("txtFileField")) {
					txtFileField = (Map<String, Object>)fieldValue;
					if(txtFileField != null && txtFileField.size() > 0) {
						fileGroupId = (String)txtFileField.get("groupId");
						List<Map<String, String>> files = (ArrayList<Map<String,String>>)txtFileField.get("files");
						if(!CommonUtil.isEmpty(files)) {
							fileGroupMap.put(fileGroupId, files);
						}
					}
				}
			}
		}

		SwdDomainCond swdDomainCond = new SwdDomainCond();
		swdDomainCond.setFormId(SeraConstant.MISSION_FORMID);
		SwdDomain swdDomain = SwManagerFactory.getInstance().getSwdManager().getDomain(userId, swdDomainCond, IManager.LEVEL_LITE);
		String domainId = swdDomain.getObjId();
		
		SwdFieldCond swdFieldCond = new SwdFieldCond();
		swdFieldCond.setDomainObjId(domainId);
		SwdField[] fields = SwManagerFactory.getInstance().getSwdManager().getFields(userId, swdFieldCond, IManager.LEVEL_LITE);
		if (CommonUtil.isEmpty(fields))
			return null;//TODO return null? throw new Exception??

		Map<String, SwdField> fieldInfoMap = new HashMap<String, SwdField>();
		for (SwdField field : fields) {
			fieldInfoMap.put(field.getFormFieldId(), field);
		}

		List fieldDataList = new ArrayList();
		for (SwdField field : fields) {
			String fieldId = field.getFormFieldId();
			SwdDataField fieldData = new SwdDataField();
			fieldData.setId(fieldId);
			fieldData.setName(field.getFormFieldName());
			fieldData.setRefForm(null);
			fieldData.setRefFormField(null);
			fieldData.setRefRecordId(null);
			if (fieldId.equalsIgnoreCase(SeraConstant.MISSION_TITLEFIELDID)) {
				fieldData.setValue(txtMissionName);
			} else if (fieldId.equalsIgnoreCase(SeraConstant.MISSION_OPENDATEFIELDID)) {
				if(txtMissionOpenDate.length() == FieldData.SIZE_DATETIME)
					txtMissionOpenDate = LocalDate.convertLocalDateTimeStringToLocalDate(txtMissionOpenDate).toGMTDateString();
				else if(txtMissionOpenDate.length() == FieldData.SIZE_DATE)
					txtMissionOpenDate = LocalDate.convertLocalDateStringToLocalDate(txtMissionOpenDate).toGMTDateString();
				fieldData.setValue(txtMissionOpenDate);
			} else if (fieldId.equalsIgnoreCase(SeraConstant.MISSION_CLOSEDATEFIELDID)) {
				if(txtMissionCloseDate.length() == FieldData.SIZE_DATETIME)
					txtMissionCloseDate = LocalDate.convertLocalDateTimeStringToLocalDate(txtMissionCloseDate).toGMTDateString();
				else if(txtMissionCloseDate.length() == FieldData.SIZE_DATE)
					txtMissionCloseDate = LocalDate.convertLocalDateStringToLocalDate(txtMissionCloseDate).toGMTDateString();
				fieldData.setValue(txtMissionCloseDate);
			} else if (fieldId.equalsIgnoreCase(SeraConstant.MISSION_PREVMISSIONFIELDID)) {
				fieldData.setValue(selPrevMission);
			} else if (fieldId.equalsIgnoreCase(SeraConstant.MISSION_CONTENTFIELDID)) {
				fieldData.setValue(txtaMissionContent);
			} else if (fieldId.equalsIgnoreCase(SeraConstant.MISSION_INDEXFIELDID)) {
				ISeraManager seraMgr = SwManagerFactory.getInstance().getSeraManager();
				CourseDetail courseDetail = seraMgr.getCourseDetailById(courseId);
				int lastMissionIndex = courseDetail.getLastMissionIndex();
				if (lastMissionIndex == -1) {
					lastMissionIndex = 0;
				} else {
					lastMissionIndex = lastMissionIndex + 1;
				}
				courseDetail.setLastMissionIndex(lastMissionIndex);
				seraMgr.setCourseDetail(courseDetail);
				
				fieldData.setValue(lastMissionIndex + "");
			} else if (fieldId.equalsIgnoreCase(SeraConstant.MISSION_FILESFIELDID)) {
				fieldData.setValue(fileGroupId);
			}
			fieldDataList.add(fieldData);
		}

		SwdDataField[] fieldDatas = new SwdDataField[fieldDataList.size()];
		fieldDataList.toArray(fieldDatas);
		SwdRecord obj = new SwdRecord();
		obj.setDomainId(domainId);
		obj.setFormId(SeraConstant.MISSION_FORMID);
		obj.setFormName(swdDomain.getFormName());
		obj.setFormVersion(swdDomain.getFormVersion());
		obj.setDataFields(fieldDatas);
		obj.setRecordId("dr_" + CommonUtil.newId());
		
		obj.setWorkSpaceId(courseId);
		obj.setWorkSpaceType("5");
		obj.setAccessLevel("3");
		obj.setAccessValue(null);

		SwManagerFactory.getInstance().getSwdManager().setRecord(userId, obj, IManager.LEVEL_ALL);
		
		TskTaskCond tskCond = new TskTaskCond();
		tskCond.setExtendedProperties(new Property[] {new Property("recordId", obj.getRecordId())});
		tskCond.setModificationUser(userId);
		tskCond.setOrders(new Order[]{new Order(TskTaskCond.A_CREATIONDATE, false)});
		TskTask[] tskTasks = SwManagerFactory.getInstance().getTskManager().getTasks(userId, tskCond, IManager.LEVEL_LITE);
		String taskInstId = tskTasks[0].getObjId();
		
		if(fileGroupMap.size() > 0) {
			for(Map.Entry<String, List<Map<String, String>>> entry : fileGroupMap.entrySet()) {
				String fGroupId = entry.getKey();
				List<Map<String, String>> fileGroups = entry.getValue();
				try {
					for(int i=0; i < fileGroups.subList(0, fileGroups.size()).size(); i++) {
						Map<String, String> file = fileGroups.get(i);
						String fileId = file.get("fileId");
						String fileName = file.get("fileName");
						String fileSize = file.get("fileSize");
						SwManagerFactory.getInstance().getDocManager().insertFiles("Files", taskInstId, fGroupId, fileId, fileName, fileSize);
					}
				} catch (Exception e) {
					throw new DocFileException("file upload fail...");
				}
			}
		}
		
		return courseId;
	}


	@Override
	public String setCourseProfile(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		/*{
		  	courseId=,
			frmSetCourseProfile=
				{
					txtCourseObject=asd, 
					txtaCourseDesc=asd, 
					txtCourseKeywords=222, 
					txtCourseDays=, 
					chkUserDefineDays=on, 
					txtCourseStartDate=2012.04.23, 
					txtCourseEndDate=2012.04.23, 
					chkCourseSecurity=3, 
					chkCourseUsers=unlimited, 
					txtCourseUsers=, 
					chkJoinApproval=autoApporval, 
					imgCourseProfile=
						{
							groupId=fg_0c541f664ee814406c496394b58e1b40e497, files=[]
						}
				}
		}*/
		User user = SmartUtil.getCurrentUser();
		Map<String, Object> frmSetCourseProfile = (Map<String, Object>)requestBody.get("frmSetCourseProfile");
		String courseId = (String)requestBody.get("courseId");

		Set<String> keySet = frmSetCourseProfile.keySet();
		Iterator<String> itr = keySet.iterator();
		
		String txtCourseObject = null;
		String txtaCourseDesc = null;
		String txtCourseKeywords = null;
		String txtCourseDays = null;
		String chkUserDefineDays = null;
		String txtCourseStartDate = null;
		String txtCourseEndDate = null;
		String chkCourseSecurity = null;
		String chkCourseUsers = null;
		String txtCourseUsers = null;
		String chkJoinApproval = null;
		List<Map<String, String>> imgCourseProfile = null;
		String courseFileId = null;
		String courseFileName = null;
		String selGroupProfileType = null;//공개 비공개
		
		String imgGroupProfile = null;

		while (itr.hasNext()) {
			String fieldId = (String)itr.next();
			Object fieldValue = frmSetCourseProfile.get(fieldId);
			if (fieldValue instanceof LinkedHashMap) {
				Map<String, Object> valueMap = (Map<String, Object>)fieldValue;
				if(fieldId.equals("imgCourseProfile")) {
					imgCourseProfile = (ArrayList<Map<String,String>>)valueMap.get("files");
				}
			} else if(fieldValue instanceof String) {					
				if(fieldId.equals("txtCourseObject")) {
					txtCourseObject = (String)frmSetCourseProfile.get("txtCourseObject");
				} else if(fieldId.equals("txtaCourseDesc")) {
					txtaCourseDesc = (String)frmSetCourseProfile.get("txtaCourseDesc");
				} else if(fieldId.equals("txtCourseKeywords")) {
					txtCourseKeywords = (String)frmSetCourseProfile.get("txtCourseKeywords");
				} else if(fieldId.equals("txtCourseDays")) {
					txtCourseDays = (String)frmSetCourseProfile.get("txtCourseDays");
				} else if(fieldId.equals("chkUserDefineDays")) {
					chkUserDefineDays = (String)frmSetCourseProfile.get("chkUserDefineDays");
				} else if(fieldId.equals("txtCourseStartDate")) {
					txtCourseStartDate = (String)frmSetCourseProfile.get("txtCourseStartDate");
				} else if(fieldId.equals("txtCourseEndDate")) {
					txtCourseEndDate = (String)frmSetCourseProfile.get("txtCourseEndDate");
				} else if(fieldId.equals("chkCourseSecurity")) {
					chkCourseSecurity = (String)frmSetCourseProfile.get("chkCourseSecurity");
					if (chkCourseSecurity != null) {
						if(Integer.parseInt(chkCourseSecurity) == AccessPolicy.LEVEL_PUBLIC)
							selGroupProfileType = "O";
						else
							selGroupProfileType = "C";
					}
				} else if(fieldId.equals("chkCourseUsers")) {
					chkCourseUsers = (String)frmSetCourseProfile.get("chkCourseUsers");
				} else if(fieldId.equals("txtCourseUsers")) {
					txtCourseUsers = (String)frmSetCourseProfile.get("txtCourseUsers");
				} else if(fieldId.equals("chkJoinApproval")) {
					chkJoinApproval = (String)frmSetCourseProfile.get("chkJoinApproval");
				}
			}
		}
		
		SwoGroup swoGroup = SwManagerFactory.getInstance().getSwoManager().getGroup(user.getId(), courseId, IManager.LEVEL_ALL);
		
		if (swoGroup == null)
			return null;

		if(!CommonUtil.isEmpty(imgCourseProfile)) {
			for(int i=0; i < imgCourseProfile.subList(0, imgCourseProfile.size()).size(); i++) {
				Map<String, String> fileMap = imgCourseProfile.get(i);
				courseFileId = fileMap.get("fileId");
				courseFileName = fileMap.get("fileName");
				imgGroupProfile = SwManagerFactory.getInstance().getDocManager().insertProfilesFile(courseFileId, courseFileName, swoGroup.getId());
				swoGroup.setPicture(imgGroupProfile);
			}
		}

		swoGroup.setDescription(txtaCourseDesc);
		swoGroup.setGroupType(selGroupProfileType);
		
		SwManagerFactory.getInstance().getSwoManager().setGroup(user.getId(), swoGroup, IManager.LEVEL_ALL);

		String groupId = swoGroup.getId();
		if (CommonUtil.isEmpty(groupId))
			return null;

		//코스 확장 정보 저장
		
		CourseDetail courseDetail = SwManagerFactory.getInstance().getSeraManager().getCourseDetailById(courseId);
		if (courseDetail == null)
			return courseId;
		
		courseDetail.setObject(txtCourseObject);
		courseDetail.setKeywords(txtCourseKeywords);
		
		boolean isUserDefineDays = !CommonUtil.isEmpty(chkUserDefineDays) && chkUserDefineDays.equalsIgnoreCase("on") ? true : false;
		
		courseDetail.setDuration(txtCourseDays == null || txtCourseDays == "" ? 0 : Integer.parseInt(txtCourseDays));
		/*if (isUserDefineDays) {
			Date startDate = new SimpleDateFormat("yyyy.MM.dd").parse(txtCourseStartDate);
			courseDetail.setStart(new LocalDate(startDate.getTime()));
		} else {
			LocalDate nowLocalDate = new LocalDate();
			Date startDate = new SimpleDateFormat("yyyy.MM.dd").parse(nowLocalDate.toLocalDateSimpleString());
			courseDetail.setStart(new LocalDate(startDate.getTime()));
		}
		if (isUserDefineDays) {
			Date endDate = new SimpleDateFormat("yyyy.MM.dd").parse(txtCourseEndDate);
			courseDetail.setEnd(new LocalDate(endDate.getTime()));
		} else if (!CommonUtil.isEmpty(txtCourseDays)) {
			LocalDate nowLocalDate = new LocalDate();
			Date endDate = new SimpleDateFormat("yyyy.MM.dd").parse(nowLocalDate.toLocalDateSimpleString());
			
			if (!txtCourseDays.equalsIgnoreCase("1")) {
				long endDateLong = endDate.getTime() + (Integer.parseInt(txtCourseDays) * 1000 * 60 * 60 * 24);
				endDate.setTime(endDateLong);
			}
			courseDetail.setEnd(new LocalDate(endDate.getTime()));
		}*/
		Date startDate = null;
		Date endDate = null;

		if(!isUserDefineDays) {
			startDate =  LocalDate.convertLocalDateStringToLocalDate(new LocalDate().toLocalDateSimpleString());
			endDate = new LocalDate(startDate.getTime() + LocalDate.ONE_DAY*(Integer.parseInt(txtCourseDays)-1));
		} else {
			startDate = LocalDate.convertLocalDateStringToLocalDate(txtCourseStartDate);
			endDate = LocalDate.convertLocalDateStringToLocalDate(txtCourseEndDate);
		}
		courseDetail.setStart(startDate);
		courseDetail.setEnd(endDate);
		if (chkCourseUsers != null && chkCourseUsers.equalsIgnoreCase("unlimited")) {
			courseDetail.setMaxMentees(-1);
		} else {
			courseDetail.setMaxMentees(txtCourseUsers == null || txtCourseUsers.equals("") ? -1 : Integer.parseInt(txtCourseUsers));
		}
			
		courseDetail.setAutoApproval(chkJoinApproval != null ? chkJoinApproval.equalsIgnoreCase("autoApporval") ? true : false : true);
		//courseDetail.setTeamId("teamId");
		
		ISeraManager seraMgr = SwManagerFactory.getInstance().getSeraManager();
		seraMgr.setCourseDetail(courseDetail);
		
		return groupId;
		
	}

	@Override
	public String removeCourse(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		/*{
			courseId=group_0df26ae7aebc4656bae93701060acf4c
		}*/
		User user = SmartUtil.getCurrentUser();
		String courseId = (String)requestBody.get("courseId");
		
		ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();
		SwoGroup group = swoMgr.getGroup(user.getId(), courseId, IManager.LEVEL_ALL);
		group.setStatus(SwoGroup.GROUP_STATUS_CLOSED);

		swoMgr.setGroup(user.getId(), group, IManager.LEVEL_ALL);
		
		//SwManagerFactory.getInstance().getSwoManager().removeGroup(user.getId(), courseId);
		//SwManagerFactory.getInstance().getSeraManager().removeCourseDetail(courseId);
		
		return courseId;
	}

	@Override
	public void scoreCoursePointByType(String courseId, int type, int count, boolean isAdd) throws Exception {
		try {
			CourseDetail courseDetail = getSeraManager().getCourseDetailById(courseId);
			if(courseDetail == null)
				return;
			if(type == Course.TYPE_COURSEPOINT_MEMBER) {
				if(isAdd) courseDetail.setCoursePoint(courseDetail.getCoursePoint() + Course.POINT_MEMBER * count);	
				else courseDetail.setCoursePoint(courseDetail.getCoursePoint() - Course.POINT_MEMBER * count);
			} else if(type == Course.TYPE_COURSEPOINT_CONTENT) {
				if(isAdd) courseDetail.setCoursePoint(courseDetail.getCoursePoint() + Course.POINT_CONTENT * count);	
				else courseDetail.setCoursePoint(courseDetail.getCoursePoint() - Course.POINT_CONTENT * count);
			}

			getSeraManager().setCourseDetail(courseDetail);

		} catch (Exception e) {
			e.printStackTrace();
		}
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
				chkCourseUsers=unlimited, 
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
				} else if(fieldId.equals("imgCourseProfile")) {
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
					if (chkCourseSecurity != null) {
						if(Integer.parseInt(chkCourseSecurity) == AccessPolicy.LEVEL_PUBLIC)
							selGroupProfileType = "O";
						else
							selGroupProfileType = "C";
					}
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
			Map<String, String> userMap = txtCourseMentor.get(0);
			mentorUserId = userMap.get("id");
		} else {
			mentorUserId = user.getId();
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
		swoGroup.setStatus(SwoGroup.GROUP_STATUS_OPEN);
		swoGroup.setGroupType(selGroupProfileType);
		swoGroup.setGroupLeader(mentorUserId);

		SwManagerFactory.getInstance().getSwoManager().setGroup(user.getId(), swoGroup, IManager.LEVEL_ALL);

		String groupId = swoGroup.getId();
		if (CommonUtil.isEmpty(groupId))
			return null;

		//코스 확장 정보 저장
		CourseDetail courseDetail = new CourseDetail();
		courseDetail.setLastMissionIndex(-1);
		courseDetail.setCourseId(groupId);
		courseDetail.setObject(txtCourseObject);
		courseDetail.setCategories(chkCourseCategories);
		courseDetail.setKeywords(txtCourseKeywords);
		courseDetail.setDuration(txtCourseDays == null || txtCourseDays == "" ? 0 : Integer.parseInt(txtCourseDays));
/*		if (txtCourseStartDate != null && !txtCourseStartDate.equalsIgnoreCase("")) {
			Date startDate = new SimpleDateFormat("yyyy.MM.dd").parse(txtCourseStartDate);
			courseDetail.setStart(new LocalDate(startDate.getTime()));
		} else {
			LocalDate nowLocalDate = new LocalDate();
			Date startDate = new SimpleDateFormat("yyyy.MM.dd").parse(nowLocalDate.toLocalDateSimpleString());
			courseDetail.setStart(new LocalDate(startDate.getTime()));
		}
		if (txtCourseEndDate != null && !txtCourseEndDate.equalsIgnoreCase("")) {
			Date endDate = new SimpleDateFormat("yyyy.MM.dd").parse(txtCourseEndDate);
			courseDetail.setEnd(new LocalDate(endDate.getTime()));
		} else if (!CommonUtil.isEmpty(txtCourseDays)) {
			LocalDate nowLocalDate = new LocalDate();
			Date endDate = new SimpleDateFormat("yyyy.MM.dd").parse(nowLocalDate.toLocalDateSimpleString());
			
			if (!txtCourseDays.equalsIgnoreCase("1")) {
				long endDateLong = endDate.getTime() + (Integer.parseInt(txtCourseDays) * 1000 * 60 * 60 * 24);
				endDate.setTime(endDateLong);
			}
			courseDetail.setEnd(new LocalDate(endDate.getTime()));
		}*/
		Date startDate = null;
		Date endDate = null;

		if(!CommonUtil.isEmpty(txtCourseDays)) {
			startDate =  LocalDate.convertLocalDateStringToLocalDate(new LocalDate().toLocalDateSimpleString());
			endDate = new LocalDate(startDate.getTime() + LocalDate.ONE_DAY*(Integer.parseInt(txtCourseDays)-1));
		} else {
			startDate = LocalDate.convertLocalDateStringToLocalDate(txtCourseStartDate);
			endDate = LocalDate.convertLocalDateStringToLocalDate(txtCourseEndDate);
		}
		courseDetail.setStart(startDate);
		courseDetail.setEnd(endDate);

		if (chkCourseUsers != null && chkCourseUsers.equalsIgnoreCase("unlimited")) {
			courseDetail.setMaxMentees(-1);
		} else {
			courseDetail.setMaxMentees(txtCourseUsers == null || txtCourseUsers.equals("") ? -1 : Integer.parseInt(txtCourseUsers));
		}

		courseDetail.setAutoApproval(chkJoinApproval != null ? chkJoinApproval.equalsIgnoreCase("autoApporval") ? true : false : true);
		courseDetail.setPayable(chkCourseFee != null ? chkCourseFee.equalsIgnoreCase("free") ? false : true : false);
		courseDetail.setFee(txtCourseFee == null || txtCourseFee.equals("") ? 0 : Integer.parseInt(txtCourseFee));
		//courseDetail.setTeamId("teamId");
		courseDetail.setTargetPoint(10);
		courseDetail.setAchievedPoint(10);
		courseDetail.setCreateDate(new LocalDate());
		
		ISeraManager seraMgr = SwManagerFactory.getInstance().getSeraManager();
		seraMgr.setCourseDetail(courseDetail);
		
		MentorDetail mentorDetail = new MentorDetail();
		mentorDetail.setMentorId(mentorUserId);
		mentorDetail.setBorn(null);
		mentorDetail.setHomeTown(null);
		mentorDetail.setLiving(null);
		mentorDetail.setFamily(null);
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

			SwoGroupCond runningCourseCond = new SwoGroupCond();
			runningCourseCond.setGroupLeader(userId);
			runningCourseCond.setStatus(SwoGroup.GROUP_STATUS_OPEN);
			long runningCourseCnt = swoManager.getGroupSize(userId, runningCourseCond);
			if(maxList != -1)
				runningCourseCond.setPageSize(maxList);
			runningCourseCond.setOrders(new Order[]{new Order("creationDate", false)});
			SwoGroup[] runningCourses = swoManager.getGroups(userId, runningCourseCond, IManager.LEVEL_ALL);

			SwoGroupCond attendingCourseCond = new SwoGroupCond();
			SwoGroupMember[] courseMembers = new SwoGroupMember[1];
			SwoGroupMember courseMember = new SwoGroupMember();
			courseMember.setUserId(userId);
			courseMembers[0] = courseMember;
			attendingCourseCond.setSwoGroupMembers(courseMembers);
			attendingCourseCond.setNotGroupLeader(userId);
			attendingCourseCond.setStatus(SwoGroup.GROUP_STATUS_OPEN);
			long attendingCourseCnt = swoManager.getGroupSize(userId, attendingCourseCond);
			if(maxList != -1)
				attendingCourseCond.setPageSize(maxList);
			attendingCourseCond.setOrders(new Order[]{new Order("creationDate", false)});
			SwoGroup[] attendingCourses = swoManager.getGroups(userId, attendingCourseCond, IManager.LEVEL_ALL);

			int totalCourseSize = ( runningCourses == null ? 0 : runningCourses.length ) + ( attendingCourses == null ? 0 : attendingCourses.length );
			if (totalCourseSize == 0)
				return courseList;
			
			List<String> courseIdList = new ArrayList<String>();
			if (runningCourses != null) {
				for(int i = 0; i < runningCourses.length; i++) {
					SwoGroup course = runningCourses[i];
					String courseId = course.getId();
					courseIdList.add(courseId);
				}
			}
			if (attendingCourses != null) {
				for(int i = 0; i < attendingCourses.length; i++) {
					SwoGroup course = attendingCourses[i];
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

			if(maxList != -1) {
				if (runningCoursesArray != null && runningCourseCnt > maxList) {
					CourseInfo[] tempRunningCourseInfos = new CourseInfo[runningCoursesArray.length + 1];
					for (int i = 0; i < runningCoursesArray.length; i++) {
						tempRunningCourseInfos[i] = runningCoursesArray[i];
					}
					tempRunningCourseInfos[runningCoursesArray.length] = new CourseInfo();
					runningCoursesArray = tempRunningCourseInfos;
				}
				if (attendingCoursesArray != null && attendingCourseCnt > maxList) {
					CourseInfo[] tempAttendingCourseInfos = new CourseInfo[attendingCoursesArray.length + 1];
					for (int i = 0; i < attendingCoursesArray.length; i++) {
						tempAttendingCourseInfos[i] = attendingCoursesArray[i];
					}
					tempAttendingCourseInfos[attendingCoursesArray.length] = new CourseInfo();
					attendingCoursesArray = tempAttendingCourseInfos;
				}
			}

			courseList.setRunnings((int)runningCourseCnt);
			courseList.setRunningCourses(runningCoursesArray);
			courseList.setAttendings((int)attendingCourseCnt);
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

			getStarPointAndScorePointUsers(course, courseId);

			return course;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
	}

	private Mentor getUserBySwoUserExtend(Mentor user, SwoUserExtend userExtend) throws Exception {
		if (userExtend == null)
			return null;
		if (user == null)
			user = new Mentor();

		user.setId(userExtend.getId());
		user.setName(userExtend.getName());
		user.setPassword(userExtend.getPassword());
		user.setCompanyId(userExtend.getCompanyId());
		user.setCompany(userExtend.getCompanyName());
		user.setDepartmentId(userExtend.getDepartmentId());
		user.setDepartment(userExtend.getDepartmentName());
		user.setBigPictureName(userExtend.getBigPictureName());
		user.setSmallPictureName(userExtend.getSmallPictureName());
		user.setPosition(userExtend.getPosition());
		user.setLocale(userExtend.getLocale());
		user.setCompany(userExtend.getCompanyName());
		user.setTimeZone(userExtend.getTimeZone());
		user.setUserLevel(userExtend.getAuthId().equals("EXTERNALUSER") ? User.USER_LEVEL_EXTERNAL_USER : userExtend.getAuthId().equals("USER") ? User.USER_LEVEL_INTERNAL_USER : userExtend.getAuthId().equals("ADMINISTRATOR") ? User.USER_LEVEL_AMINISTRATOR : User.USER_LEVEL_SYSMANAGER);
		user.setRole(userExtend.getRoleId().equals("DEPT LEADER") ? User.USER_ROLE_LEADER : User.USER_ROLE_MEMBER);
		user.setEmployeeId(userExtend.getEmployeeId());
		user.setPhoneNo(userExtend.getPhoneNo());
		user.setCellPhoneNo(userExtend.getCellPhoneNo());

		return user;
	}
	private Mentor getUserByUserId(String userId) throws Exception {
		if (CommonUtil.isEmpty(userId))
			return null;
		SwoUserExtend userExtend = SwManagerFactory.getInstance().getSwoManager().getUserExtend(userId, userId, false);
		return getUserBySwoUserExtend(null, userExtend);
	}
	@Override
	public Mentor getMentorById(String mentorId) throws Exception {

		try{
			if (mentorId == null)
				return null;
			Mentor mentor = getUserByUserId(mentorId);
			ISeraManager seraMgr = SwManagerFactory.getInstance().getSeraManager();
			MentorDetail mentorDetail = seraMgr.getMentorDetailById(mentorId, mentorId);
			if (mentorDetail == null)
				return mentor;
			
			mentor.setBorn(mentorDetail.getBorn());
			mentor.setHomeTown(mentorDetail.getHomeTown());
			mentor.setLiving(mentorDetail.getLiving());
			mentor.setFamily(mentorDetail.getFamily());
			mentor.setEducations(mentorDetail.getEducations());
			mentor.setWorks(mentorDetail.getWorks());
			mentor.setMentorHistory(mentorDetail.getMentorHistory());
			mentor.setMenteeHistory(mentorDetail.getMenteeHistory());
			mentor.setLectures(mentorDetail.getLectures());
			mentor.setAwards(mentorDetail.getAwards());
			mentor.setEtc(mentorDetail.getEtc());
			
			//Mentor mentor = SeraTest.getMentorById(mentorId);
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
			FriendList friendListObj = new FriendList();
			if (CommonUtil.isEmpty(userId)) {
				friendListObj.setTotalFriends(0);
				return friendListObj;
			}
			ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();
			ISeraManager seraMgr = SwManagerFactory.getInstance().getSeraManager();

			SeraFriendCond seraFriendCond = new SeraFriendCond();
			seraFriendCond.setAcceptStatus(SeraFriend.ACCEPT_STATUS_ACCEPT);
			SeraFriend[] seraFriends = seraMgr.getMyFriends(userId, seraFriendCond);

			if(CommonUtil.isEmpty(seraFriends)) {
				friendListObj.setTotalFriends(0);
				return friendListObj;
			} else {
				friendListObj.setTotalFriends(seraFriends.length);
			}
			seraFriendCond.setPageSize(maxList);
			seraFriendCond.setPageNo(0);
			seraFriends = seraMgr.getMyFriends(userId, seraFriendCond);

			String[] ids = new String[seraFriends.length];

			for(int i=0; i<seraFriends.length; i++) {
				SeraFriend seraFriend = seraFriends[i];
				String id = seraFriend.getFriendId();
				ids[i] = id;
			}

			SwoUserExtend[] userExtends = swoMgr.getUsersExtend(userId, ids);

			SwoUserExtend[] finalUserExtends = new SwoUserExtend[userExtends.length];

			for(int i=0; i<ids.length; i++) {
				String id = ids[i];
				for(int j=0; j<userExtends.length; j++) {
					SwoUserExtend swoUserExtend = userExtends[j];
					String userExtendId = swoUserExtend.getId();
					if(id.equals(userExtendId)) {
						finalUserExtends[i] = swoUserExtend;
						break;
					}
				}
			}

			User currentUser = SmartUtil.getCurrentUser();
			String currentUserId = currentUser.getId();

			seraFriendCond = new SeraFriendCond();
			seraFriendCond.setAcceptStatus(SeraFriend.ACCEPT_STATUS_ACCEPT);

			SeraFriend[] mySeraFriends = getSeraManager().getMyFriends(currentUserId, seraFriendCond);

			List userInfoList = new ArrayList();
			if(finalUserExtends != null) {
				for(SwoUserExtend swoUserExtend : finalUserExtends) {
					SeraUserInfo member = new SeraUserInfo();
					String id = swoUserExtend.getId();
					member.setId(id);
					boolean isFriend = false;
					if(!CommonUtil.isEmpty(mySeraFriends)) {
						for(SeraFriend seraFriend : mySeraFriends) {
							if(id.equals(seraFriend.getFriendId())) {
								isFriend = true;
								break;
							}
						}
					}
					member.setFriend(isFriend);
					SeraUserDetail seraUserDetail = seraMgr.getSeraUserById(currentUserId, id);
					String goal = null;
					if(seraUserDetail != null)
						goal = seraUserDetail.getGoal();
					member.setGoal(goal);
					member.setName(swoUserExtend.getName());
					member.setNickName(swoUserExtend.getNickName());
					member.setPosition(swoUserExtend.getPosition());
					member.setRole(swoUserExtend.getAuthId().equals("EXTERNALUSER") ? User.USER_LEVEL_EXTERNAL_USER : swoUserExtend.getAuthId().equals("USER") ? User.USER_LEVEL_INTERNAL_USER : swoUserExtend.getAuthId().equals("ADMINISTRATOR") ? User.USER_LEVEL_AMINISTRATOR : User.USER_LEVEL_SYSMANAGER);
					member.setSmallPictureName(swoUserExtend.getSmallPictureName());
					member.setDepartment(new DepartmentInfo(swoUserExtend.getDepartmentId(), swoUserExtend.getDepartmentName(), swoUserExtend.getDepartmentDesc()));
					userInfoList.add(member);
				}
				SeraUserInfo[] friendsUserInfo = new SeraUserInfo[userInfoList.size()];
				userInfoList.toArray(friendsUserInfo);
				friendListObj.setFriends(friendsUserInfo);
			}
			return friendListObj;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
	}
	
	@Override
	public SeraUserInfo[] getFriendsById(String userId, String lastId, int maxList, String key) throws Exception{
		try {
			ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();
			ISeraManager seraMgr = SwManagerFactory.getInstance().getSeraManager();

			SeraFriendCond seraFriendCond = new SeraFriendCond();
			if(!CommonUtil.isEmpty(lastId)) {
				SwoUser swoUser = swoMgr.getUser("", lastId, IManager.LEVEL_LITE);
				String lastFriendName = swoUser.getName();
				seraFriendCond.setLastFriendName(lastFriendName);
			}
			seraFriendCond.setAcceptStatus(SeraFriend.ACCEPT_STATUS_ACCEPT);
			if(key != null)
				seraFriendCond.setKey(key);

			SeraFriend[] seraFriends = seraMgr.getMyFriends(userId, seraFriendCond);
			int totalSize = seraFriends.length;

			seraFriendCond.setPageSize(maxList);
			seraFriendCond.setPageNo(0);
			seraFriends = seraMgr.getMyFriends(userId, seraFriendCond);

			if (CommonUtil.isEmpty(seraFriends)) 
				return null;

			String[] ids = new String[seraFriends.length];

			for(int i=0; i<seraFriends.length; i++) {
				SeraFriend seraFriend = seraFriends[i];
				String id = seraFriend.getFriendId();
				ids[i] = id;
			}

			SwoUserExtend[] userExtends = swoMgr.getUsersExtend(userId, ids);

			SwoUserExtend[] finalUserExtends = new SwoUserExtend[userExtends.length];

			for(int i=0; i<ids.length; i++) {
				String id = ids[i];
				for(int j=0; j<userExtends.length; j++) {
					SwoUserExtend swoUserExtend = userExtends[j];
					String userExtendId = swoUserExtend.getId();
					if(id.equals(userExtendId)) {
						finalUserExtends[i] = swoUserExtend;
						break;
					}
				}
			}

			User currentUser = SmartUtil.getCurrentUser();
			String currentUserId = currentUser.getId();

			SeraFriend[] mySeraFriends = getSeraManager().getMyFriends(currentUserId, seraFriendCond);
			seraFriendCond = new SeraFriendCond();
			seraFriendCond.setAcceptStatus(SeraFriend.ACCEPT_STATUS_ACCEPT);

			List userInfoList = new ArrayList();
			SeraUserInfo[] friendsUserInfo = null;
			if(finalUserExtends != null) {
				for(SwoUserExtend swoUserExtend : finalUserExtends) {
					SeraUserInfo member = new SeraUserInfo();
					String id = swoUserExtend.getId();
					member.setId(id);
					boolean isFriend = false;
					if(!CommonUtil.isEmpty(mySeraFriends)) {
						for(SeraFriend seraFriend : mySeraFriends) {
							if(id.equals(seraFriend.getFriendId())) {
								isFriend = true;
								break;
							}
						}
					}
					member.setFriend(isFriend);
					SeraUserDetail seraUserDetail = seraMgr.getSeraUserById(currentUserId, id);
					String goal = null;
					if(seraUserDetail != null)
						goal = seraUserDetail.getGoal();
					member.setGoal(goal);
					member.setName(swoUserExtend.getName());
					member.setNickName(swoUserExtend.getNickName());
					member.setPosition(swoUserExtend.getPosition());
					member.setRole(swoUserExtend.getAuthId().equals("EXTERNALUSER") ? User.USER_LEVEL_EXTERNAL_USER : swoUserExtend.getAuthId().equals("USER") ? User.USER_LEVEL_INTERNAL_USER : swoUserExtend.getAuthId().equals("ADMINISTRATOR") ? User.USER_LEVEL_AMINISTRATOR : User.USER_LEVEL_SYSMANAGER);
					member.setSmallPictureName(swoUserExtend.getSmallPictureName());
					member.setDepartment(new DepartmentInfo(swoUserExtend.getDepartmentId(), swoUserExtend.getDepartmentName(), swoUserExtend.getDepartmentDesc()));
					userInfoList.add(member);
				}
				if(totalSize > maxList)
					userInfoList.add(new SeraUserInfo());
				friendsUserInfo = new SeraUserInfo[userInfoList.size()];
				userInfoList.toArray(friendsUserInfo);
			}
			return friendsUserInfo;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
		
	}

	@Override
	public CommentInstanceInfo[] getSubInstancesByRefId(String refId, int maxSize) throws Exception {

		try {
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();
			List<CommentInstanceInfo> commentInstanceInfoList = new ArrayList<CommentInstanceInfo>();
			CommentInstanceInfo[] commentInstanceInfos = null;
			OpinionCond opinionCond = new OpinionCond();
			opinionCond.setRefId(refId);
			if(maxSize != WorkInstance.FETCH_ALL_SUB_INSTANCE)
				opinionCond.setPageSize(maxSize);
			opinionCond.setOrders(new Order[]{new Order(OpinionCond.A_CREATIONDATE, true)});
			Opinion[] opinions = SwManagerFactory.getInstance().getOpinionManager().getOpinions(userId, opinionCond, IManager.LEVEL_ALL);
			if(!CommonUtil.isEmpty(opinions)) {
				int opinionLength = opinions.length;
				for(int i=0; i<opinionLength; i++) {
					Opinion opinion = opinions[i];
					CommentInstanceInfo commentInstanceInfo = new CommentInstanceInfo();
					String modificationUser = opinion.getModificationUser() == null ? opinion.getCreationUser() : opinion.getModificationUser();
					Date modificationDate = opinion.getModificationDate() == null ? opinion.getCreationDate() : opinion.getModificationDate();
					commentInstanceInfo.setId(opinion.getObjId());
					commentInstanceInfo.setCommentType(CommentInstance.COMMENT_TYPE_ON_WORK_MANUAL);
					commentInstanceInfo.setComment(opinion.getOpinion());
					commentInstanceInfo.setCommentor(ModelConverter.getUserInfoByUserId(opinion.getCreationUser()));
					commentInstanceInfo.setLastModifiedDate(new LocalDate(modificationDate.getTime()));
					commentInstanceInfo.setType(Instance.TYPE_COMMENT);
					commentInstanceInfo.setOwner(ModelConverter.getUserInfoByUserId(opinion.getCreationUser()));
					commentInstanceInfo.setCreatedDate(new LocalDate(opinion.getCreationDate().getTime()));
					commentInstanceInfo.setLastModifier(ModelConverter.getUserInfoByUserId(modificationUser));;
					commentInstanceInfoList.add(commentInstanceInfo);
				}
			}
			if(commentInstanceInfoList.size() > 0) {
				commentInstanceInfos = new CommentInstanceInfo[commentInstanceInfoList.size()];
				commentInstanceInfoList.toArray(commentInstanceInfos);
			}
			return commentInstanceInfos;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void setSwdRecordCondBySpace(SwdRecordCond swdRecordCond, String currentUserId, String userId, String courseId, String missionId, String teamId) throws Exception {
		try {
			String workSpaceIdIns = null;
			if(!SmartUtil.isBlankObject(courseId)) {
				if(!SmartUtil.isBlankObject(userId)) {
					swdRecordCond.setCreationUser(userId);
				}
				if(!SmartUtil.isBlankObject(teamId)) {
					swdRecordCond.setWorkSpaceIdIns("('"+teamId+"')");
				} else {
					if(SmartUtil.isBlankObject(missionId)) {
						workSpaceIdIns = "(";
						MissionInstanceInfo[] missionInstanceInfos = getMissionInstanceList(courseId, null, null);
						if(!CommonUtil.isEmpty(missionInstanceInfos)) {									
							for(int j=0; j<missionInstanceInfos.length; j++) {
								MissionInstanceInfo missionInstanceInfo = missionInstanceInfos[j];
								String missionInstanceId = missionInstanceInfo.getId();
								workSpaceIdIns = workSpaceIdIns + "'" + missionInstanceId + "', ";
							}
						}
						workSpaceIdIns = workSpaceIdIns + "'" + courseId + "')";
						swdRecordCond.setWorkSpaceIdIns(workSpaceIdIns);
					} else {
						swdRecordCond.setWorkSpaceIdIns("('"+missionId+"')");
					}
				}
			} else {
				if(!SmartUtil.isBlankObject(userId)) {
					if(!userId.equals("EVENT")) {
						if(!currentUserId.equals(userId))
							swdRecordCond.setWorkSpaceIdIns("('"+userId+"')");
						else
							swdRecordCond.setCreationUser(userId);
					}
				} else {
					SwoGroupCond attendingCourseCond = new SwoGroupCond();
					SwoGroupMember[] courseMembers = new SwoGroupMember[1];
					SwoGroupMember courseMember = new SwoGroupMember();
					courseMember.setUserId(currentUserId);
					courseMembers[0] = courseMember;
					attendingCourseCond.setSwoGroupMembers(courseMembers);
					SwoGroup[] attendingCourses = SwManagerFactory.getInstance().getSwoManager().getGroups(currentUserId, attendingCourseCond, IManager.LEVEL_ALL);
					String[] courseIdIns = null;
					if(!CommonUtil.isEmpty(attendingCourses)) {
						workSpaceIdIns = "(";
						int attendingCourseLength = attendingCourses.length;
						courseIdIns = new String[attendingCourseLength];
						for(int i=0; i<attendingCourses.length; i++) {
							SwoGroup attendingCourse = attendingCourses[i];
							String attendingCourseId = attendingCourse.getId();
							courseIdIns[i] = attendingCourseId;
						}
						CourseDetailCond courseDetailCond = new CourseDetailCond();
						courseDetailCond.setCourseIdIns(courseIdIns);
						CourseDetail[] courseDetails = SwManagerFactory.getInstance().getSeraManager().getCourseDetails(currentUserId, courseDetailCond);
						Course[] myCourses = null;
						if(!CommonUtil.isEmpty(courseDetails)) {
							myCourses = convertSwoGroupArrayToCourseArray(attendingCourses, courseDetails);
							int myCourseLength = myCourses.length;
							if(!CommonUtil.isEmpty(myCourses)) {
								for(int i=0; i<myCourseLength; i++) {
									Course myCourse = myCourses[i];
									String myCourseId = myCourse.getId();
									MissionInstanceInfo[] myMissionInstanceInfos = myCourse.getMissions();
									if(!CommonUtil.isEmpty(myMissionInstanceInfos)) {
										int myMissionInstanceInfoLength = myMissionInstanceInfos.length;
										for(int j=0; j<myMissionInstanceInfoLength; j++) {
											MissionInstanceInfo myMissionInstanceInfo = myMissionInstanceInfos[j];
											String myMissionId = myMissionInstanceInfo.getId();
											workSpaceIdIns = workSpaceIdIns + "'" + myMissionId + "', ";
										}
									}
									if(i == myCourseLength - 1)								
										workSpaceIdIns = workSpaceIdIns + "'" + myCourseId + "'";
									else
										workSpaceIdIns = workSpaceIdIns + "'" + myCourseId + "', ";
								}
							}
						}
						workSpaceIdIns = workSpaceIdIns + ", '"+currentUserId+"')";
						swdRecordCond.setWorkSpaceIdIns(workSpaceIdIns);
					} else {
						swdRecordCond.setWorkSpaceIdIns("('"+currentUserId+"')");
					}
					/*if(!CommonUtil.isEmpty(attendingCourses)) {
						workSpaceIdIns = "(";
						for(int i=0; i<attendingCourses.length; i++) {
							SwoGroup attendingCourse = attendingCourses[i];
							String attendingCourseId = attendingCourse.getId();
							Course course = getCourseById(attendingCourseId);
							if(course != null) {
								MissionInstanceInfo[] missionInstanceInfos = course.getMissions();
								if(!CommonUtil.isEmpty(missionInstanceInfos)) {									
									for(int j=0; j<missionInstanceInfos.length; j++) {
										MissionInstanceInfo missionInstanceInfo = missionInstanceInfos[j];
										String missionInstanceId = missionInstanceInfo.getId();
										workSpaceIdIns = workSpaceIdIns + "'" + missionInstanceId + "', ";
									}
								}
							}
							if(i == attendingCourses.length - 1)								
								workSpaceIdIns = workSpaceIdIns + "'" + attendingCourseId + "'";
							else
								workSpaceIdIns = workSpaceIdIns + "'" + attendingCourseId + "', ";
						}
						workSpaceIdIns = workSpaceIdIns + ", '"+currentUserId+"')";
						swdRecordCond.setWorkSpaceIdIns(workSpaceIdIns);
					} else {
						swdRecordCond.setWorkSpaceIdIns("('"+currentUserId+"')");
					}*/
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private BoardInstanceInfo[] getBoardInstancesByCourseId(String userId, String courseId, String missionId, String teamId, String workSpaceId, LocalDate fromDate, int maxList) throws Exception {
		try{
			ISwdManager swdMgr = SwManagerFactory.getInstance().getSwdManager();
			ISwfManager swfMgr = SwManagerFactory.getInstance().getSwfManager();
			ICommunityService comSvc = SwServiceFactory.getInstance().getCommunityService();
			
			String workId = SmartWork.ID_BOARD_MANAGEMENT;
			User user = SmartUtil.getCurrentUser();
			SwdDomainCond swdDomainCond = new SwdDomainCond();
			swdDomainCond.setCompanyId(user.getCompanyId());
			SwfFormCond swfFormCond = new SwfFormCond();
			swfFormCond.setCompanyId(user.getCompanyId());
			swfFormCond.setPackageId(workId);
			SwfForm[] swfForms = swfMgr.getForms(user.getId(), swfFormCond, IManager.LEVEL_LITE);
	
			if(swfForms == null)
				return null;
	
			swdDomainCond.setFormId(swfForms[0].getId());
	
			SwdDomain swdDomain = swdMgr.getDomain(user.getId(), swdDomainCond, IManager.LEVEL_LITE);
	
			if(swdDomain == null)
				return  null;

			SwdRecordCond swdRecordCond = new SwdRecordCond();
			swdRecordCond.setCompanyId(user.getCompanyId());
			swdRecordCond.setFormId(swdDomain.getFormId());
			swdRecordCond.setDomainId(swdDomain.getObjId());
	
			swdRecordCond.setPageNo(0);
			swdRecordCond.setPageSize(maxList);

			swdRecordCond.setOrders(new Order[]{new Order(FormField.ID_CREATED_DATE, false)});

			if(workSpaceId == null)
				setSwdRecordCondBySpace(swdRecordCond, user.getId(), userId, courseId, missionId, teamId);
			else
				swdRecordCond.setWorkSpaceId(workSpaceId);

			if(fromDate != null)
				swdRecordCond.setFromDate(fromDate);

			SwdRecord[] swdRecords = swdMgr.getRecords(user.getId(), swdRecordCond, IManager.LEVEL_LITE);

			SwdRecordExtend[] swdRecordExtends = swdMgr.getCtgPkg(workId);
	
			BoardInstanceInfo[] boardInstanceInfos = null;

			String subCtgId = swdRecordExtends[0].getSubCtgId();
			String subCtgName = swdRecordExtends[0].getSubCtg();
			String parentCtgId = swdRecordExtends[0].getParentCtgId();
			String parentCtgName = swdRecordExtends[0].getParentCtg();
			String formId = swdDomain.getFormId();
			String formName = swdDomain.getFormName();

			if(!CommonUtil.isEmpty(swdRecords)) {
				int swdRecordsLength = swdRecords.length;
				boardInstanceInfos = new BoardInstanceInfo[swdRecordsLength];
				for(int i=0; i < swdRecordsLength; i++) {
					SwdRecord swdRecord = swdRecords[i];
					String recordId = swdRecord.getRecordId();
					BoardInstanceInfo boardInstanceInfo = new BoardInstanceInfo();
					
					boardInstanceInfo.setLikers(ModelConverter.getLikersUserIdArray(userId, Instance.TYPE_BOARD, swdRecord.getRecordId()));
					
					boardInstanceInfo.setId(recordId);
					boardInstanceInfo.setOwner(ModelConverter.getUserInfoByUserId(swdRecord.getCreationUser()));
					boardInstanceInfo.setCreatedDate(new LocalDate((swdRecord.getCreationDate()).getTime()));
					boardInstanceInfo.setStatus(WorkInstance.STATUS_COMPLETED);
					String space = swdRecord.getWorkSpaceId();
					if(space == null)
						space = user.getId();

					WorkSpaceInfo workSpaceInfo = comSvc.getWorkSpaceInfoById(space);

					boardInstanceInfo.setWorkSpace(workSpaceInfo);

					WorkCategoryInfo groupInfo = null;
					if (!CommonUtil.isEmpty(subCtgId))
						groupInfo = new WorkCategoryInfo(subCtgId, subCtgName);

					WorkCategoryInfo categoryInfo = new WorkCategoryInfo(parentCtgId, parentCtgName);

					WorkInfo workInfo = new SmartWorkInfo(formId, formName, SocialWork.TYPE_BOARD, groupInfo, categoryInfo);

					boardInstanceInfo.setWork(workInfo);
					boardInstanceInfo.setLastModifier(ModelConverter.getUserInfoByUserId(swdRecord.getModificationUser()));
					boardInstanceInfo.setLastModifiedDate(new LocalDate((swdRecord.getModificationDate()).getTime()));

					CommentInstanceInfo[] subInstanceInfos = getSubInstancesByRefId(recordId, WorkInstance.FETCH_ALL_SUB_INSTANCE);
					int subInstanceCount = 0;
					if(!CommonUtil.isEmpty(subInstanceInfos)) {
						subInstanceCount = subInstanceInfos.length;
						if(subInstanceInfos.length > WorkInstance.DEFAULT_SUB_INSTANCE_FETCH_COUNT) {
							CommentInstanceInfo[] defaultInstanceInfos = new CommentInstanceInfo[WorkInstance.DEFAULT_SUB_INSTANCE_FETCH_COUNT];
							for(int j=0; j<WorkInstance.DEFAULT_SUB_INSTANCE_FETCH_COUNT; j++) {
								CommentInstanceInfo subInstanceInfo = subInstanceInfos[j];
								defaultInstanceInfos[j] = subInstanceInfo;
							}
							boardInstanceInfo.setSubInstances(defaultInstanceInfos);
						} else {
							boardInstanceInfo.setSubInstances(subInstanceInfos);
						}
					}
					boardInstanceInfo.setSubInstanceCount(subInstanceCount);

					SwdDataField[] swdDataFields = swdRecord.getDataFields();
					if(!CommonUtil.isEmpty(swdDataFields)) {
						int swdDataFieldsLength = swdDataFields.length;
						for(int j=0; j<swdDataFieldsLength; j++) {
							SwdDataField swdDataField = swdDataFields[j];
							String value = swdDataField.getValue();
							if(swdDataField.getId().equals("0")) {
								boardInstanceInfo.setSubject(StringUtil.subString(value, 0, 24, "..."));
							} else if(swdDataField.getId().equals("1")) {
								boardInstanceInfo.setContent(value);
								boardInstanceInfo.setBriefContent(StringUtil.subString(value, 0, 120, "..."));
							}
						}
					}
					boardInstanceInfos[i] = boardInstanceInfo;
				}
			}
			return boardInstanceInfos;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required
		}
	}
	private EventInstanceInfo[] getEventInstanceInfosByWorkSpaceId(String userId, String courseId, String missionId, String teamId, LocalDate fromDate, int maxList) throws Exception {
		try{

			ISwdManager swdMgr = SwManagerFactory.getInstance().getSwdManager();
			ISwfManager swfMgr = SwManagerFactory.getInstance().getSwfManager();
			ICommunityService comSvc = SwServiceFactory.getInstance().getCommunityService();
			
			String workId = SmartWork.ID_EVENT_MANAGEMENT;
			User user = SmartUtil.getCurrentUser();

			SwdDomainCond swdDomainCond = new SwdDomainCond();
			swdDomainCond.setCompanyId(user.getCompanyId());
	
			SwfFormCond swfFormCond = new SwfFormCond();
			swfFormCond.setCompanyId(user.getCompanyId());
			swfFormCond.setPackageId(workId);
	
			SwfForm[] swfForms = swfMgr.getForms(user.getId(), swfFormCond, IManager.LEVEL_LITE);
	
			if(swfForms == null)
				return null;
			
			String formId = swfForms[0].getId();
	
			swdDomainCond.setFormId(formId);
	
			SwdDomain swdDomain = swdMgr.getDomain(user.getId(), swdDomainCond, IManager.LEVEL_LITE);
	
			SwdRecordCond swdRecordCond = new SwdRecordCond();
			swdRecordCond.setCompanyId(user.getCompanyId());
			swdRecordCond.setFormId(swdDomain.getFormId());
			swdRecordCond.setDomainId(swdDomain.getObjId());

			swdRecordCond.setPageNo(0);
			swdRecordCond.setPageSize(maxList);
			
			swdRecordCond.setOrders(new Order[]{new Order(FormField.ID_CREATED_DATE, false)});

			//if(userId == null && courseId == null && missionId == null && teamId == null)
				
			//else
				setSwdRecordCondBySpace(swdRecordCond, user.getId(), userId, courseId, missionId, teamId);

			swdRecordCond.setFromDate(fromDate);

			SwdRecord[] swdRecords = swdMgr.getRecords(user.getId(), swdRecordCond, IManager.LEVEL_ALL);

			SwdRecordExtend[] swdRecordExtends = swdMgr.getCtgPkg(workId);

			String formName = swdDomain.getFormName();

			List<EventInstanceInfo> eventInstanceInfoList = new ArrayList<EventInstanceInfo>();
			EventInstanceInfo[] eventInstanceInfos = null;
			if(swdRecords != null) {
				for(int i=0; i < swdRecords.length; i++) {
					SwdRecord swdRecord = swdRecords[i];
					EventInstanceInfo eventInstanceInfo = new EventInstanceInfo();
					eventInstanceInfo.setLikers(ModelConverter.getLikersUserIdArray(userId, Instance.TYPE_EVENT, swdRecord.getRecordId()));
					
					String recordId = swdRecord.getRecordId();
					eventInstanceInfo.setId(recordId);
					eventInstanceInfo.setOwner(ModelConverter.getUserInfoByUserId(swdRecord.getCreationUser()));
					eventInstanceInfo.setCreatedDate(new LocalDate((swdRecord.getCreationDate()).getTime()));
					eventInstanceInfo.setStatus(WorkInstance.STATUS_COMPLETED);
					eventInstanceInfo.setWorkSpace(getWorkSpaceInfoBySwdRecord(swdRecord));

					WorkCategoryInfo workGroupInfo = null;
					if (!CommonUtil.isEmpty(swdRecordExtends[0].getSubCtgId()))
						workGroupInfo = new WorkCategoryInfo(swdRecordExtends[0].getSubCtgId(), swdRecordExtends[0].getSubCtg());

					WorkCategoryInfo workCategoryInfo = new WorkCategoryInfo(swdRecordExtends[0].getParentCtgId(), swdRecordExtends[0].getParentCtg());

					WorkInfo workInfo = new SmartWorkInfo(formId, formName, SocialWork.TYPE_EVENT, workGroupInfo, workCategoryInfo);
					//WorkInfo workInfo = ModelConverter.getWorkInfoByPackageId(swfForms[0].getPackageId());
					
					eventInstanceInfo.setWork(workInfo);
					eventInstanceInfo.setLastModifier(ModelConverter.getUserInfoByUserId(swdRecord.getModificationUser()));
					eventInstanceInfo.setLastModifiedDate(new LocalDate((swdRecord.getModificationDate()).getTime()));

					CommentInstanceInfo[] subInstanceInfos = getSubInstancesByRefId(recordId, WorkInstance.FETCH_ALL_SUB_INSTANCE);
					int subInstanceCount = 0;
					if(!CommonUtil.isEmpty(subInstanceInfos)) {
						subInstanceCount = subInstanceInfos.length;
						if(subInstanceInfos.length > WorkInstance.DEFAULT_SUB_INSTANCE_FETCH_COUNT) {
							CommentInstanceInfo[] defaultInstanceInfos = new CommentInstanceInfo[WorkInstance.DEFAULT_SUB_INSTANCE_FETCH_COUNT];
							for(int j=0; j<WorkInstance.DEFAULT_SUB_INSTANCE_FETCH_COUNT; j++) {
								CommentInstanceInfo subInstanceInfo = subInstanceInfos[j];
								defaultInstanceInfos[j] = subInstanceInfo;
							}
							eventInstanceInfo.setSubInstances(defaultInstanceInfos);
						} else {
							eventInstanceInfo.setSubInstances(subInstanceInfos);
						}
					}
					eventInstanceInfo.setSubInstanceCount(subInstanceCount);

					SwdDataField[] swdDataFields = swdRecord.getDataFields();
					List<CommunityInfo> communityInfoList = new ArrayList<CommunityInfo>();
					for(SwdDataField swdDataField : swdDataFields) {
						String value = swdDataField.getValue();
						String refRecordId = swdDataField.getRefRecordId();
						if(swdDataField.getId().equals("0")) {
							eventInstanceInfo.setSubject(value);
						} else if(swdDataField.getId().equals("6")) {
							eventInstanceInfo.setContent(CommonUtil.toNotNull(value));
							eventInstanceInfo.setBriefContent(StringUtil.subString(CommonUtil.toNotNull(value), 0, 120, "..."));
						} else if(swdDataField.getId().equals("1")) {
							LocalDate start = LocalDate.convertGMTStringToLocalDate(value);
							eventInstanceInfo.setStart(start);
						} else if(swdDataField.getId().equals("2")) {
							if(value != null)
								eventInstanceInfo.setEnd(LocalDate.convertGMTStringToLocalDate(value));
						} else if(swdDataField.getId().equals("5")) {
							CommunityInfo[] relatedUsers = null;
							if(refRecordId != null) {
								String[] reltdUsers = refRecordId.split(";");
								for(String reltdUser : reltdUsers) {
									UserInfo userInfo = ModelConverter.getUserInfoByUserId(reltdUser);
									DepartmentInfo departmentInfo = ModelConverter.getDepartmentInfoByDepartmentId(reltdUser);
									GroupInfo groupInfo = ModelConverter.getGroupInfoByGroupId(reltdUser);
									if(userInfo != null) {
										communityInfoList.add(userInfo);
									} else if(departmentInfo != null) {
										communityInfoList.add(departmentInfo);
									} else if(groupInfo != null) {
										communityInfoList.add(groupInfo);
									}
								}
								if(communityInfoList.size() != 0) {
									relatedUsers = new CommunityInfo[communityInfoList.size()];
									communityInfoList.toArray(relatedUsers);
								}
							}
							eventInstanceInfo.setRelatedUsers(relatedUsers);
						} else if(swdDataField.getId().equals("4")) {
							eventInstanceInfo.setPlace(value);
						}
					}
					if(TskTask.TASKREFTYPE_EVENT.equals(userId)) {
						CommunityInfo[] relatedUsers = eventInstanceInfo.getRelatedUsers();
						boolean isParticipant = false;
						if(!CommonUtil.isEmpty(relatedUsers))
							isParticipant = calendarService.isParticipant(relatedUsers);
						if(isParticipant || swdRecord.getCreationUser().equals(user.getId()) || swdRecord.getModificationUser().equals(user.getId()))
							eventInstanceInfoList.add(eventInstanceInfo);
					} else {
						eventInstanceInfoList.add(eventInstanceInfo);
					}
				}
			}
			if(eventInstanceInfoList.size() != 0) {
				eventInstanceInfos = new EventInstanceInfo[eventInstanceInfoList.size()];
				eventInstanceInfoList.toArray(eventInstanceInfos);
			}
			return eventInstanceInfos;

		} catch(Exception e) {
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}
	@Override
	public InstanceInfo[] getCourseNotices(String courseId, LocalDate fromDate, int maxList) throws Exception{
		try{
			//공지사항(getCommunityRecentBoardInstances) + 이벤트(getEventInstanceInfosByWorkSpaceId)
			InstanceInfo[] noticeInfo = getBoardInstancesByCourseId(null, courseId, null, null, null, fromDate, maxList);
			InstanceInfo[] eventInfo = getEventInstanceInfosByWorkSpaceId(null, courseId, null, null, fromDate, maxList);
			
			Map<Long, InstanceInfo> resultMap = new HashMap<Long, InstanceInfo>();
			if (noticeInfo != null) {
				for (InstanceInfo info : noticeInfo) {
					LocalDate createDate = info.getCreatedDate();
					resultMap.put(createDate.getTime(), info);
				}
			}
			if (eventInfo != null) {
				for (InstanceInfo info : eventInfo) {
					LocalDate createDate = info.getCreatedDate();
					resultMap.put(createDate.getTime(), info);
				}
			}
			if (resultMap.size() ==  0)
				return null;

			Map<Long, InstanceInfo> sortMap = new TreeMap<Long, InstanceInfo>(Collections.reverseOrder());
			sortMap.putAll(resultMap);

			List<InstanceInfo> returnInstanceInfoList = new ArrayList<InstanceInfo>();
			Iterator<Long> itr = sortMap.keySet().iterator();
			int i = 0;
			while (itr.hasNext()) {
				if (i == maxList) 
					break;
				returnInstanceInfoList.add(sortMap.get(itr.next()));
			}
			
			InstanceInfo[] returnInstanceInfo = new InstanceInfo[returnInstanceInfoList.size()];
			returnInstanceInfoList.toArray(returnInstanceInfo);
			
		//	InstanceInfo[] notices = SeraTest.getCourseNotices(courseId, fromDate, maxList);
			return returnInstanceInfo;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	private void getStarPointAndScorePointUsers(Object object, String keyId) throws Exception {
		try {
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();

			double starPoint = 0;
			int starPointUsers = 0;

			if(object.getClass().equals(MissionInstance.class) || object.getClass().equals(MissionInstanceInfo.class)) {

				SwdRecordCond recordCond = new SwdRecordCond();
				recordCond.setFormId(SeraConstant.MISSION_REPORT_FORMID);
				recordCond.setWorkSpaceId(keyId);

				SwdRecord[] swdRecords = getSwdManager().getRecords(userId, recordCond, IManager.LEVEL_ALL);

				if(!CommonUtil.isEmpty(swdRecords)) {
					starPointUsers = swdRecords.length;
					for(SwdRecord swdRecord : swdRecords) {
						SwdDataField[] swdDataFields = swdRecord.getDataFields();
						if(!CommonUtil.isEmpty(swdDataFields)) {
							for(SwdDataField swdDataField : swdDataFields) {
								String value = swdDataField.getValue();
								String id = swdDataField.getId();
								if(id.equals(SeraConstant.MISSION_REPORT_STARPOINTFIELDID))
									starPoint = starPoint + (value != null ? Double.parseDouble(value) : 0);
							}
						}
					}
				}

				if(object.getClass().equals(MissionInstance.class)) {
					((MissionInstance)object).setStarPoint(starPoint/starPointUsers);
					((MissionInstance)object).setStarPointUsers(starPointUsers);
				} else if(object.getClass().equals(MissionInstanceInfo.class)) {
					((MissionInstanceInfo)object).setStarPoint(starPoint/starPointUsers);
					((MissionInstanceInfo)object).setStarPointUsers(starPointUsers);
				}
			} else if(object.getClass().equals(Course.class)) {
				CourseReviewCond courseReviewCond = new CourseReviewCond();
				courseReviewCond.setCourseId(keyId);
				CourseReview[] courseReviews = getSeraManager().getCourseReviews(userId, courseReviewCond);
				if(!CommonUtil.isEmpty(courseReviews)) {
					starPointUsers = courseReviews.length;
					for(CourseReview courseReview : courseReviews) {
						Double reviewStarPoint = courseReview.getStarPoint();
						starPoint = starPoint + (reviewStarPoint != null ? reviewStarPoint : 0);
					}
				}
				((Course)object).setStarPoint(starPoint/starPointUsers);
				((Course)object).setStarPointUsers(starPointUsers);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public MissionInstanceInfo[] getMissionInstanceList(String courseId, LocalDate fromDate, LocalDate toDate) throws Exception {
		try {

			//코스에 속한 미션들을 가져온다
			User user = SmartUtil.getCurrentUser();
			if(user == null)
				return null;

			SwfFormCond swfCond = new SwfFormCond();
			swfCond.setCompanyId(user.getCompanyId());
			swfCond.setId(SeraConstant.MISSION_FORMID);
	
			ISwfManager swfMgr = SwManagerFactory.getInstance().getSwfManager();
			SwfForm swfForm = swfMgr.getForms(user.getId(), swfCond, IManager.LEVEL_LITE)[0];
			
			RequestParams params = new RequestParams();
			SearchFilter searchFilter = new SearchFilter();
			FormField formField = new FormField();
			formField.setId(FormField.ID_CREATED_DATE);
			formField.setName("createdTime");
			formField.setType(FormField.TYPE_DATE);

			FormField courseIdFormField = new FormField();
			courseIdFormField.setId("workSpaceId");
			courseIdFormField.setName("workSpaceId");
			courseIdFormField.setType(FormField.TYPE_TEXT);
			
			List<Condition> conditionList = new ArrayList<Condition>();
			if (courseId != null) {
				Condition condition = new Condition(courseIdFormField, "=", courseId);
				conditionList.add(condition);
			}
			if (fromDate != null) {
				Condition condition = new Condition(formField, ">", fromDate.toLocalDateValue());
				conditionList.add(condition);
			}
			if (toDate != null) {
				Condition condition = new Condition(formField, "<", toDate.toLocalDateValue());
				conditionList.add(condition);
			}
			if (conditionList.size() != 0) {
				Condition[] conditionArray = new Condition[conditionList.size()];
				conditionList.toArray(conditionArray);
				searchFilter.setConditions(conditionArray);
			}
			params.setSearchFilter(searchFilter);
			
			InstanceInfoList infoList = getIWorkInstanceList(swfForm.getPackageId(), null, params);
			
			if (infoList == null || infoList.getInstanceDatas() == null || infoList.getInstanceDatas().length == 0)
				return null;
			
			MissionInstanceInfo[] missions = (MissionInstanceInfo[])infoList.getInstanceDatas();
			
			//MissionInstanceInfo[] missions = SeraTest.getMissionInstanceList(courseId, fromDate, toDate);
			return missions;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			throw e;
			// Exception Handling Required			
		}		
	}
	
	public MissionInstanceInfo getMissionInfoById(String missionId) throws Exception {
		try {
			if (CommonUtil.isEmpty(missionId))
				return null;
			User user = SmartUtil.getCurrentUser();
			if(user == null)
				return null;

			SwfFormCond swfCond = new SwfFormCond();
			swfCond.setCompanyId(user.getCompanyId());
			swfCond.setId(SeraConstant.MISSION_FORMID);

			ISwfManager swfMgr = SwManagerFactory.getInstance().getSwfManager();
			SwfForm swfForm = swfMgr.getForms(user.getId(), swfCond, IManager.LEVEL_LITE)[0];

			RequestParams params = new RequestParams();
			InstanceInfoList infoList = getIWorkInstanceList(swfForm.getPackageId(), missionId, params);

			if (infoList == null || infoList.getInstanceDatas() == null || infoList.getInstanceDatas().length == 0)
				return null;

			MissionInstanceInfo[] missions = (MissionInstanceInfo[])infoList.getInstanceDatas();

			MissionInstanceInfo missionInstancInfo = missions[0];
			getStarPointAndScorePointUsers(missionInstancInfo, missionInstancInfo.getId());

			return missionInstancInfo;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			throw e;
			// Exception Handling Required			
		}
	}
	@Override
	public MissionInstance getMissionById(String missionId) throws Exception {
		try{
			if (CommonUtil.isEmpty(missionId))
				return null;
			User user = SmartUtil.getCurrentUser();
			
			SwfFormCond swfCond = new SwfFormCond();
			swfCond.setCompanyId(user.getCompanyId());
			swfCond.setId(SeraConstant.MISSION_FORMID);

			IDocFileManager docMgr = SwManagerFactory.getInstance().getDocManager();
			ISwfManager swfMgr = SwManagerFactory.getInstance().getSwfManager();
			SwfForm swfForm = swfMgr.getForms(user.getId(), swfCond, IManager.LEVEL_LITE)[0];
			SwdRecordCond swdRecordCond = new SwdRecordCond();
			swdRecordCond.setCompanyId(user.getCompanyId());
			swdRecordCond.setFormId(swfForm.getId());
			swdRecordCond.setRecordId(missionId);

			ISwdManager swdMgr = SwManagerFactory.getInstance().getSwdManager();
			
			SwdRecord swdRecord = swdMgr.getRecord(user.getId(), swdRecordCond, IManager.LEVEL_LITE);
			if (swdRecord == null)
				return null;
			WorkInstance workInstance = new MissionInstance();
			ModelConverter.getWorkInstanceBySwdRecord(user.getId(), workInstance, swdRecord);

			MissionInstance missionInstance = (MissionInstance)workInstance;
			
			SwdRecordCond cond = new SwdRecordCond();
			cond.setWorkSpaceId(swdRecord.getRecordId());
			cond.setFormId(SeraConstant.MISSION_REPORT_FORMID);
			SwdRecord[] records = swdMgr.getRecords(user.getId(), cond, IManager.LEVEL_LITE);
			if (records != null && records.length != 0) {
				String[] clearers = new String[records.length];
				for (int j = 0; j < records.length; j++) {
					SwdRecord record = records[j];
					clearers[j] = record.getCreationUser();
				}
				missionInstance.setMissionClearers(clearers);
			}
			
			SwdDataField[] swdDataFields = swdRecord.getDataFields();

			if(!CommonUtil.isEmpty(swdDataFields)) {
				int swdDataFieldsLength = swdDataFields.length;
				for(int j=0; j<swdDataFieldsLength; j++) {
					SwdDataField swdDataField = swdDataFields[j];
					if (swdDataField.getId().equals(SeraConstant.MISSION_TITLEFIELDID)) {
						missionInstance.setSubject(swdDataField.getValue());
					} else if (swdDataField.getId().equals(SeraConstant.MISSION_OPENDATEFIELDID)) {
						missionInstance.setOpenDate(LocalDate.convertGMTStringToLocalDate(swdDataField.getValue()));
					} else if (swdDataField.getId().equals(SeraConstant.MISSION_CLOSEDATEFIELDID)) {
						missionInstance.setCloseDate(LocalDate.convertGMTStringToLocalDate(swdDataField.getValue()));
					} else if (swdDataField.getId().equals(SeraConstant.MISSION_PREVMISSIONFIELDID)) {
						missionInstance.setPrevMission(getMissionById(swdDataField.getValue()));
					} else if (swdDataField.getId().equals(SeraConstant.MISSION_CONTENTFIELDID)) {
						missionInstance.setContent(swdDataField.getValue());
					} else if (swdDataField.getId().equals(SeraConstant.MISSION_INDEXFIELDID)) {
						missionInstance.setIndex(Integer.parseInt(swdDataField.getValue()));
					} else if (swdDataField.getId().equals(SeraConstant.MISSION_FILESFIELDID)) {
						missionInstance.setFileGroupId(swdDataField.getValue());
						List<IFileModel> docFileList = docMgr.findFileGroup(swdDataField.getValue());
						if (docFileList != null && docFileList.size() != 0) {
							List<Map<String, String>> fileList = new ArrayList<Map<String, String>>();
							for (int k = 0 ; k <docFileList.size(); k++) {
								Map<String, String> fileMap = new HashMap<String, String>();
								IFileModel docFile = docFileList.get(k);
								fileMap.put("fileId", docFile.getId());
								fileMap.put("fileName", docFile.getFileName());
								fileMap.put("fileSize", docFile.getFileSize()+"");
								fileList.add(fileMap);
							}
							missionInstance.setFiles(fileList);
						}
					}
				}
			}
			String courseId = swdRecord.getWorkSpaceId();
			missionInstance.setWorkSpace(getCourseById(courseId));
			//MissionInstance mission = SeraTest.getMissionById(missionId);
			getStarPointAndScorePointUsers(missionInstance, missionId);
			return missionInstance;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
	}
	public SeraUserInfo[] getSeraUserInfos(String[] userIds) throws Exception {
		SwoUserExtend[] userExtends = SwManagerFactory.getInstance().getSwoManager().getUsersExtend("", userIds);
		if (userExtends == null)
			return null;
		List userInfoList = new ArrayList();
		SeraUserInfo[] userInfoArray = null;
		if(userExtends != null) {
			for(SwoUserExtend swoUserExtend : userExtends) {
				UserInfo member = new SeraUserInfo();
				member.setId(swoUserExtend.getId());
				member.setName(swoUserExtend.getName());
				member.setNickName(swoUserExtend.getNickName());
				member.setPosition(swoUserExtend.getPosition());
				member.setRole(swoUserExtend.getAuthId().equals("EXTERNALUSER") ? User.USER_LEVEL_EXTERNAL_USER : swoUserExtend.getAuthId().equals("USER") ? User.USER_LEVEL_INTERNAL_USER : swoUserExtend.getAuthId().equals("ADMINISTRATOR") ? User.USER_LEVEL_AMINISTRATOR : User.USER_LEVEL_SYSMANAGER);
				member.setSmallPictureName(swoUserExtend.getSmallPictureName());
				member.setDepartment(new DepartmentInfo(swoUserExtend.getDepartmentId(), swoUserExtend.getDepartmentName(), swoUserExtend.getDepartmentDesc()));
				userInfoList.add(member);
			}
			userInfoArray = new SeraUserInfo[userInfoList.size()];
			userInfoList.toArray(userInfoArray);
		}
		return userInfoArray;
	}
	public SeraUser[] getSeraUsers(String[] userIds) throws Exception {

		if (CommonUtil.isEmpty(userIds))
			return null;
		SwoUserExtend[] userExtend = SwManagerFactory.getInstance().getSwoManager().getUsersExtend("", userIds);
		if (userExtend == null)
			return null;
		
		SeraUser[] seraUsers = new SeraUser[userExtend.length];
		for (int i = 0; i < userExtend.length; i++) {
			seraUsers[i] = getUserBySwoUserExtend(null, userExtend[i]);
		}
		
		return seraUsers;
		
	}
	@Override
	public SeraUser getSeraUserById(String userId) throws Exception {
		try{
			User currentUser = SmartUtil.getCurrentUser();
			String currentUserId = currentUser.getId();

			boolean isFriend = false;
			SeraFriendCond seraFriendCond = new SeraFriendCond();
			seraFriendCond.setAcceptStatus(SeraFriend.ACCEPT_STATUS_ACCEPT);
			SeraFriend[] seraFriends = getSeraManager().getMyFriends(currentUserId, seraFriendCond);
			if(!CommonUtil.isEmpty(seraFriends)) {
				for(SeraFriend seraFriend : seraFriends) {
					if(userId.equals(seraFriend.getFriendId())) {
						isFriend = true;
						break;
					}
				}
			}

			User user = getUserByUserId(userId);
			ISeraManager seraMgr = SwManagerFactory.getInstance().getSeraManager();
			
			SeraUserDetail seraUserDetail = seraMgr.getSeraUserById(userId, userId);
			SeraUser seraUser = (SeraUser)user;
			seraUser.setFriend(isFriend);
			if (seraUserDetail != null) {
				seraUser.setEmail(seraUserDetail.getEmail());
				if (seraUserDetail.getBirthday() != null)
					seraUser.setBirthday(new LocalDate(seraUserDetail.getBirthday().getTime()));
				seraUser.setSex(seraUserDetail.getSex());
				seraUser.setGoal(seraUserDetail.getGoal());
				seraUser.setInterests(seraUserDetail.getInterests());
				seraUser.setEducations(seraUserDetail.getEducations());
				seraUser.setWorks(seraUserDetail.getWorks());
				seraUser.setTwUserId(seraUserDetail.getTwUserId());
				seraUser.setTwPassword(seraUserDetail.getTwPassword());
				seraUser.setFbUserId(seraUserDetail.getFbUserId());
				seraUser.setFbPassword(seraUserDetail.getFbPassword());
				seraUser.setNickName(seraUserDetail.getNickName());
			}
			return seraUser;

			//return SeraTest.getSeraUserById(userId);
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
	}

	@Override
	public String setSeraNote(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		/*{
			spaceType=4, 
			spaceId=kmyu@maninsoft.co.kr, 
			frmCreateSeraNote=
				{
					txtNoteContent=내용, 
					selAccessLevel=3, 
					txtNoteUrl=http://naver.com, 
					txtNoteFile=
						{
							groupId=fg_6bdbbeda906fb94fec983119de60a9ba85e3, 
							files=[
									{
										fileId=temp_123ae9d6828841abbc1321a38eafbef4, 
										fileName=Tulips.jpg, 
										fileSize=620888
									}, 
									{
										fileId=temp_0c3271696cd647139cbdacbf9e2cdfb2, 
										fileName=Penguins.jpg, 
										fileSize=777835
									}
							       ]
						}, 
					imgNoteImage=
						{
							groupId=fg_c2dff9c164ad1649216bbeb63fc49fcc6249, 
							files=[
									{
										fileId=temp_f52600753c954123b92f0c72e43a6439, 
										fileName=배경.jpg
									}
								]
						}, 
					ytNoteVideo=
						{
							video=
								{
									videoYTId=ixpNlrOdwQE, 
									fileName=Wildlife.wmv, 
									fileSize=26246026
								}
						}
				}
		}*/
		
		User user = SmartUtil.getCurrentUser();
		String userId = user.getId();

		String spaceType = (String)requestBody.get("spaceType");
		String spaceId = (String)requestBody.get("spaceId");
		String teamId = (String)requestBody.get("teamId");
		Map<String, Object> frmCreateSeraNoteMap = (Map<String, Object>)requestBody.get("frmCreateSeraNote");

		Set<String> keySet = frmCreateSeraNoteMap.keySet();
		Iterator<String> itr = keySet.iterator();
		
		String txtNoteContent = null;
		String selAccessLevel = null;
		String txtNoteUrl = null;
		
		Map<String, Object> txtNoteFile = null;
		String fileGroupId = null;
		Map<String, List<Map<String, String>>> fileGroupMap = new HashMap<String, List<Map<String, String>>>();
		
		Map<String, Object> imgNoteImage = null;
		String imageGroupId = null;
		String imageSrc = null;
		String imageSrcOriginal = null;
		Map<String, List<Map<String, String>>> imageGroupMap = new HashMap<String, List<Map<String, String>>>();
		
		Map<String, Object> ytNoteVideo = null;
		String videoYTId = null;
		String videoFileName = null;
		String videoFileSize = null;

		
		while (itr.hasNext()) {
			String fieldId = (String)itr.next();
			Object fieldValue = frmCreateSeraNoteMap.get(fieldId);
			if (fieldValue instanceof LinkedHashMap) {
				if (fieldId.equalsIgnoreCase("txtNoteFile")) {
					txtNoteFile = (Map<String, Object>)fieldValue;
					if(txtNoteFile != null && txtNoteFile.size() > 0) {
						fileGroupId = (String)txtNoteFile.get("groupId");
						List<Map<String, String>> files = (ArrayList<Map<String,String>>)txtNoteFile.get("files");
						if(!CommonUtil.isEmpty(files)) {
							fileGroupMap.put(fileGroupId, files);
						}
					}
				} else if (fieldId.equalsIgnoreCase("imgNoteImage")) {
					imgNoteImage = (Map<String, Object>)fieldValue;
					if(imgNoteImage != null && imgNoteImage.size() > 0) {
						imageGroupId = (String)imgNoteImage.get("groupId");

						List<Map<String, String>> files = (ArrayList<Map<String,String>>)imgNoteImage.get("files");
						if(!CommonUtil.isEmpty(files)) {
							imageGroupMap.put(imageGroupId, files);
						}
					}
				} else if (fieldId.equalsIgnoreCase("ytNoteVideo")) {
					ytNoteVideo = (Map<String, Object>)fieldValue;
					if(ytNoteVideo != null && ytNoteVideo.size() > 0) {
						Map videoMap = (Map<String, Object>)ytNoteVideo.get("video");
						if(videoMap != null && videoMap.size() > 0) {
							videoYTId = (String)videoMap.get("videoYTId");
							videoFileName = (String)videoMap.get("fileName");
							videoFileSize = (String)videoMap.get("fileSize");
						}
					}
				}
			} else if(fieldValue instanceof String) {
				if (fieldId.equals("txtNoteContent")) {
					txtNoteContent = (String)frmCreateSeraNoteMap.get("txtNoteContent");
				} else if (fieldId.equals("selAccessLevel")) {
					selAccessLevel = (String)frmCreateSeraNoteMap.get("selAccessLevel");
				} else if (fieldId.equals("txtNoteUrl")) {
					txtNoteUrl = (String)frmCreateSeraNoteMap.get("txtNoteUrl");
				}
			}
		}
		
		
		SwdDomainCond swdDomainCond = new SwdDomainCond();
		swdDomainCond.setFormId(SeraConstant.NOTE_FORMID);
		SwdDomain swdDomain = SwManagerFactory.getInstance().getSwdManager().getDomain(userId, swdDomainCond, IManager.LEVEL_LITE);
		String domainId = swdDomain.getObjId();
		
		SwdFieldCond swdFieldCond = new SwdFieldCond();
		swdFieldCond.setDomainObjId(domainId);
		SwdField[] fields = SwManagerFactory.getInstance().getSwdManager().getFields(userId, swdFieldCond, IManager.LEVEL_LITE);
		if (CommonUtil.isEmpty(fields))
			return null;//TODO return null? throw new Exception??

		Map<String, SwdField> fieldInfoMap = new HashMap<String, SwdField>();
		for (SwdField field : fields) {
			fieldInfoMap.put(field.getFormFieldId(), field);
		}

		List fieldDataList = new ArrayList();
		for (SwdField field : fields) {
			String fieldId = field.getFormFieldId();
			SwdDataField fieldData = new SwdDataField();
			fieldData.setId(fieldId);
			fieldData.setName(field.getFormFieldName());
			fieldData.setRefForm(null);
			fieldData.setRefFormField(null);
			fieldData.setRefRecordId(null);
			
			if (fieldId.equalsIgnoreCase(SeraConstant.NOTE_IMAGEGROUPIDFIELDID)) {
				fieldData.setValue(imageGroupId);
			} else if (fieldId.equalsIgnoreCase(SeraConstant.NOTE_LINKURLFIELDID)) {
				fieldData.setValue(txtNoteUrl);
			}  else if (fieldId.equalsIgnoreCase(SeraConstant.NOTE_FILEGROUPIDFIELDID)) {
				fieldData.setValue(fileGroupId);
			}  else if (fieldId.equalsIgnoreCase(SeraConstant.NOTE_CONTENTFIELDID)) {
				fieldData.setValue(txtNoteContent);
			}  else if (fieldId.equalsIgnoreCase(SeraConstant.NOTE_VIDEOYTIDFIELDID)) {
				fieldData.setValue(videoYTId);
			}  else if (fieldId.equalsIgnoreCase(SeraConstant.NOTE_VIDEOFILENAMEFIELDID)) {
				fieldData.setValue(videoFileName);
			}  else if (fieldId.equalsIgnoreCase(SeraConstant.NOTE_VIDEOFILESIZEFIELDID)) {
				fieldData.setValue(videoFileSize);
			}
			fieldDataList.add(fieldData);
		}

		SwdDataField[] fieldDatas = new SwdDataField[fieldDataList.size()];
		fieldDataList.toArray(fieldDatas);
		SwdRecord obj = new SwdRecord();
		obj.setDomainId(domainId);
		obj.setFormId(SeraConstant.NOTE_FORMID);
		obj.setFormName(swdDomain.getFormName());
		obj.setFormVersion(swdDomain.getFormVersion());
		obj.setDataFields(fieldDatas);
		obj.setRecordId("dr_" + CommonUtil.newId());

		if(!SmartUtil.isBlankObject(teamId)) obj.setWorkSpaceId(teamId);
		else obj.setWorkSpaceId(spaceId);

		obj.setWorkSpaceType(spaceType);
		obj.setAccessLevel(selAccessLevel);
		obj.setAccessValue(null);

		String recordId = SwManagerFactory.getInstance().getSwdManager().setRecord(userId, obj, IManager.LEVEL_ALL);

		if(String.valueOf(ISmartWorks.SPACE_TYPE_GROUP).equals(spaceType))
			scoreCoursePointByType(spaceId, Course.TYPE_COURSEPOINT_CONTENT, 1, true);

		TskTaskCond tskCond = new TskTaskCond();
		tskCond.setExtendedProperties(new Property[] {new Property("recordId", recordId)});
		tskCond.setModificationUser(userId);
		tskCond.setOrders(new Order[]{new Order(TskTaskCond.A_CREATIONDATE, false)});
		TskTask[] tskTasks = SwManagerFactory.getInstance().getTskManager().getTasks(userId, tskCond, IManager.LEVEL_LITE);
		String taskInstId = tskTasks[0].getObjId();
		
		if(fileGroupMap.size() > 0) {
			for(Map.Entry<String, List<Map<String, String>>> entry : fileGroupMap.entrySet()) {
				String fGroupId = entry.getKey();
				List<Map<String, String>> fileGroups = entry.getValue();
				try {
					for(int i=0; i < fileGroups.subList(0, fileGroups.size()).size(); i++) {
						Map<String, String> file = fileGroups.get(i);
						String fileId = file.get("fileId");
						String fileName = file.get("fileName");
						String fileSize = file.get("fileSize");
						SwManagerFactory.getInstance().getDocManager().insertFiles("Files", taskInstId, fGroupId, fileId, fileName, fileSize);
					}
				} catch (Exception e) {
					throw new DocFileException("file upload fail...");
				}
			}
		}if(imageGroupMap.size() > 0) {
			for(Map.Entry<String, List<Map<String, String>>> entry : imageGroupMap.entrySet()) {
				String imgGroupId = entry.getKey();
				List<Map<String, String>> imgGroups = entry.getValue();
				try {
					for(int i=0; i < imgGroups.subList(0, imgGroups.size()).size(); i++) {
						Map<String, String> file = imgGroups.get(i);
						String fileId = file.get("fileId");
						String fileName = file.get("fileName");
						//String fileSize = file.get("fileSize");
						SwManagerFactory.getInstance().getDocManager().insertFiles("Pictures", taskInstId, imgGroupId, fileId, fileName, "0");
					}
				} catch (Exception e) {
					throw new DocFileException("image upload fail...");
				}
			}
		}

		return recordId;
	}
	private NoteInstanceInfo[] getSeraNoteByMissionId(String userId, String courseId, String missionId, String teamId, LocalDate fromDate, int maxList) throws Exception {
		try{

			ISwdManager swdMgr = SwManagerFactory.getInstance().getSwdManager();
			ISwfManager swfMgr = SwManagerFactory.getInstance().getSwfManager();
			IDocFileManager docMgr = SwManagerFactory.getInstance().getDocManager();
			ICommunityService comSvc = SwServiceFactory.getInstance().getCommunityService();

			String workId = SmartWork.ID_SERA_NOTE_MANAGEMENT;
			User user = SmartUtil.getCurrentUser();
			String companyId = user.getCompanyId();

			SwdDomainCond swdDomainCond = new SwdDomainCond();
			swdDomainCond.setCompanyId(user.getCompanyId());
	
			SwfFormCond swfFormCond = new SwfFormCond();
			swfFormCond.setCompanyId(user.getCompanyId());
			swfFormCond.setPackageId(workId);
	
			SwfForm[] swfForms = swfMgr.getForms(user.getId(), swfFormCond, IManager.LEVEL_LITE);
	
			if(swfForms == null)
				return null;
			
			String formId = swfForms[0].getId();
	
			swdDomainCond.setFormId(formId);
	
			SwdDomain swdDomain = swdMgr.getDomain(user.getId(), swdDomainCond, IManager.LEVEL_LITE);
	
			SwdRecordCond swdRecordCond = new SwdRecordCond();
			swdRecordCond.setFormId(swdDomain.getFormId());
			swdRecordCond.setDomainId(swdDomain.getObjId());

			swdRecordCond.setOrders(new Order[]{new Order(FormField.ID_CREATED_DATE, false)});
	
			swdRecordCond.setPageNo(0);
			swdRecordCond.setPageSize(maxList);

			setSwdRecordCondBySpace(swdRecordCond, user.getId(), userId, courseId, missionId, teamId);

			swdRecordCond.setFromDate(fromDate);

			SwdRecord[] swdRecords = swdMgr.getRecords(user.getId(), swdRecordCond, IManager.LEVEL_ALL);

			SwdRecordExtend[] swdRecordExtends = swdMgr.getCtgPkg(workId);

			String formName = swdDomain.getFormName();

			List<NoteInstanceInfo> NoteInstanceInfoList = new ArrayList<NoteInstanceInfo>();
			NoteInstanceInfo[] noteInstanceInfos = null;
			if(swdRecords != null) {
				for(int i=0; i < swdRecords.length; i++) {
					SwdRecord swdRecord = swdRecords[i];
					NoteInstanceInfo noteInstanceInfo = new NoteInstanceInfo();

					noteInstanceInfo.setLikers(ModelConverter.getLikersUserIdArray(userId, Instance.TYPE_SERA_NOTE, swdRecord.getRecordId()));
					
					String recordId = swdRecord.getRecordId();
					noteInstanceInfo.setId(recordId);
					noteInstanceInfo.setOwner(ModelConverter.getUserInfoByUserId(swdRecord.getCreationUser()));
					noteInstanceInfo.setCreatedDate(new LocalDate((swdRecord.getCreationDate()).getTime()));
					noteInstanceInfo.setStatus(WorkInstance.STATUS_COMPLETED);
					noteInstanceInfo.setWorkSpace(getWorkSpaceInfoBySwdRecord(swdRecord));

					WorkCategoryInfo workGroupInfo = null;
					if (!CommonUtil.isEmpty(swdRecordExtends[0].getSubCtgId()))
						workGroupInfo = new WorkCategoryInfo(swdRecordExtends[0].getSubCtgId(), swdRecordExtends[0].getSubCtg());

					WorkCategoryInfo workCategoryInfo = new WorkCategoryInfo(swdRecordExtends[0].getParentCtgId(), swdRecordExtends[0].getParentCtg());

					WorkInfo workInfo = new SmartWorkInfo(formId, formName, Work.TYPE_SERA_NOTE, workGroupInfo, workCategoryInfo);
					//WorkInfo workInfo = ModelConverter.getWorkInfoByPackageId(swfForms[0].getPackageId());

					noteInstanceInfo.setWork(workInfo);
					noteInstanceInfo.setLastModifier(ModelConverter.getUserInfoByUserId(swdRecord.getModificationUser()));
					noteInstanceInfo.setLastModifiedDate(new LocalDate((swdRecord.getModificationDate()).getTime()));

					CommentInstanceInfo[] subInstanceInfos = getSubInstancesByRefId(recordId, WorkInstance.FETCH_ALL_SUB_INSTANCE);
					int subInstanceCount = 0;
					if(!CommonUtil.isEmpty(subInstanceInfos)) {
						subInstanceCount = subInstanceInfos.length;
						if(subInstanceInfos.length > WorkInstance.DEFAULT_SUB_INSTANCE_FETCH_COUNT) {
							CommentInstanceInfo[] defaultInstanceInfos = new CommentInstanceInfo[WorkInstance.DEFAULT_SUB_INSTANCE_FETCH_COUNT];
							for(int j=0; j<WorkInstance.DEFAULT_SUB_INSTANCE_FETCH_COUNT; j++) {
								CommentInstanceInfo subInstanceInfo = subInstanceInfos[j];
								defaultInstanceInfos[j] = subInstanceInfo;
							}
							noteInstanceInfo.setSubInstances(defaultInstanceInfos);
						} else {
							noteInstanceInfo.setSubInstances(subInstanceInfos);
						}
					}
					noteInstanceInfo.setSubInstanceCount(subInstanceCount);

					SwdDataField[] swdDataFields = swdRecord.getDataFields();
					List<CommunityInfo> communityInfoList = new ArrayList<CommunityInfo>();
					for(SwdDataField swdDataField : swdDataFields) {
						String value = swdDataField.getValue();
						String refRecordId = swdDataField.getRefRecordId();
						
						if(swdDataField.getId().equals(SeraConstant.NOTE_CONTENTFIELDID)) {
							noteInstanceInfo.setContent(value);
							noteInstanceInfo.setBriefContent(StringUtil.subString(value, 0, 120, "..."));
						} else if(swdDataField.getId().equals(SeraConstant.NOTE_IMAGEGROUPIDFIELDID)) {
							
							List<IFileModel> fileList = docMgr.findFileGroup(value);
							if (fileList != null && fileList.size() != 0) {
								IFileModel fileModel = fileList.get(0);
								if(fileModel != null) {
									String filePath = fileModel.getFilePath();
									String extension = filePath.lastIndexOf(".") > 1 ? filePath.substring(filePath.lastIndexOf(".")) : null;
									filePath = StringUtils.replace(filePath, "\\", "/");
									if(filePath.indexOf(companyId) != -1)
										noteInstanceInfo.setImageSrcOrigin(Community.PICTURE_PATH + filePath.substring(filePath.indexOf(companyId), filePath.length()));
									filePath = filePath.replaceAll(extension, Community.IMAGE_TYPE_THUMB + extension);
									if(filePath.indexOf(companyId) != -1)
										noteInstanceInfo.setImageSrc(Community.PICTURE_PATH + filePath.substring(filePath.indexOf(companyId), filePath.length()));
								}
							}
						} else if(swdDataField.getId().equals(SeraConstant.NOTE_VIDEOYTIDFIELDID)) {
							noteInstanceInfo.setVideoId(value);
						} else if(swdDataField.getId().equals(SeraConstant.NOTE_LINKURLFIELDID)) {
							noteInstanceInfo.setLinkUrl(value);
						} else if(swdDataField.getId().equals(SeraConstant.NOTE_FILEGROUPIDFIELDID)) {
							noteInstanceInfo.setFileGroupId(value);
							
							List<IFileModel> docFileList = docMgr.findFileGroup(value);
							
							if (docFileList != null && docFileList.size() != 0) {
								List<Map<String, String>> fileList = new ArrayList<Map<String, String>>();
								for (int j = 0 ; j <docFileList.size(); j++) {
									Map<String, String> fileMap = new HashMap<String, String>();
									IFileModel docFile = docFileList.get(j);
									fileMap.put("fileId", docFile.getId());
									fileMap.put("fileName", docFile.getFileName());
									fileMap.put("fileType", docFile.getType());
									fileMap.put("fileSize", docFile.getFileSize()+"");
									fileList.add(fileMap);
								}
								noteInstanceInfo.setFileList(fileList);
							}
						}
					}
					NoteInstanceInfoList.add(noteInstanceInfo);
				}
			}
			if(NoteInstanceInfoList.size() != 0) {
				noteInstanceInfos = new NoteInstanceInfo[NoteInstanceInfoList.size()];
				NoteInstanceInfoList.toArray(noteInstanceInfos);
			}
			return noteInstanceInfos;

		} catch(Exception e) {
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}
	private MissionReportInstanceInfo[] getSeraReportByMissionId(String userId, String courseId, String missionId, String teamId, LocalDate fromDate, int maxList) throws Exception {
		try{

			ISwdManager swdMgr = SwManagerFactory.getInstance().getSwdManager();
			ISwfManager swfMgr = SwManagerFactory.getInstance().getSwfManager();
			IDocFileManager docMgr = SwManagerFactory.getInstance().getDocManager();
			ICommunityService comSvc = SwServiceFactory.getInstance().getCommunityService();

			String workId = SmartWork.ID_SERA_MISSION_REPORT_MANAGEMENT;
			User user = SmartUtil.getCurrentUser();
			String companyId = user.getCompanyId();

			SwdDomainCond swdDomainCond = new SwdDomainCond();
			swdDomainCond.setCompanyId(user.getCompanyId());
	
			SwfFormCond swfFormCond = new SwfFormCond();
			swfFormCond.setCompanyId(user.getCompanyId());
			swfFormCond.setPackageId(workId);
	
			SwfForm[] swfForms = swfMgr.getForms(user.getId(), swfFormCond, IManager.LEVEL_LITE);
	
			if(swfForms == null)
				return null;
			
			String formId = swfForms[0].getId();
	
			swdDomainCond.setFormId(formId);
	
			SwdDomain swdDomain = swdMgr.getDomain(user.getId(), swdDomainCond, IManager.LEVEL_LITE);
	
			SwdRecordCond swdRecordCond = new SwdRecordCond();
			swdRecordCond.setCompanyId(user.getCompanyId());
			swdRecordCond.setFormId(swdDomain.getFormId());
			swdRecordCond.setDomainId(swdDomain.getObjId());

			swdRecordCond.setPageNo(0);
			swdRecordCond.setPageSize(maxList);

			swdRecordCond.setOrders(new Order[]{new Order(FormField.ID_CREATED_DATE, false)});

			setSwdRecordCondBySpace(swdRecordCond, user.getId(), userId, courseId, missionId, teamId);

			swdRecordCond.setFromDate(fromDate);

			SwdRecord[] swdRecords = swdMgr.getRecords(user.getId(), swdRecordCond, IManager.LEVEL_ALL);

			SwdRecordExtend[] swdRecordExtends = swdMgr.getCtgPkg(workId);

			String formName = swdDomain.getFormName();

			List<MissionReportInstanceInfo> missionReportInstanceInfoList = new ArrayList<MissionReportInstanceInfo>();
			MissionReportInstanceInfo[] missionReportInstanceInfos = null;
			if(swdRecords != null) {
				for(int i=0; i < swdRecords.length; i++) {
					SwdRecord swdRecord = swdRecords[i];
					MissionReportInstanceInfo missionReportInstanceInfo = new MissionReportInstanceInfo();

					missionReportInstanceInfo.setLikers(ModelConverter.getLikersUserIdArray(userId, Instance.TYPE_SERA_MISSION_REPORT, swdRecord.getRecordId()));
					
					String recordId = swdRecord.getRecordId();
					missionReportInstanceInfo.setId(recordId);
					missionReportInstanceInfo.setOwner(ModelConverter.getUserInfoByUserId(swdRecord.getCreationUser()));
					missionReportInstanceInfo.setCreatedDate(new LocalDate((swdRecord.getCreationDate()).getTime()));
					missionReportInstanceInfo.setStatus(WorkInstance.STATUS_COMPLETED);
					missionReportInstanceInfo.setWorkSpace(getWorkSpaceInfoBySwdRecord(swdRecord));

					WorkCategoryInfo workGroupInfo = null;
					if (!CommonUtil.isEmpty(swdRecordExtends[0].getSubCtgId()))
						workGroupInfo = new WorkCategoryInfo(swdRecordExtends[0].getSubCtgId(), swdRecordExtends[0].getSubCtg());

					WorkCategoryInfo workCategoryInfo = new WorkCategoryInfo(swdRecordExtends[0].getParentCtgId(), swdRecordExtends[0].getParentCtg());

					WorkInfo workInfo = new SmartWorkInfo(formId, formName, Work.TYPE_SERA_MISSION_REPORT, workGroupInfo, workCategoryInfo);
					//WorkInfo workInfo = ModelConverter.getWorkInfoByPackageId(swfForms[0].getPackageId());
					
					missionReportInstanceInfo.setWork(workInfo);
					missionReportInstanceInfo.setLastModifier(ModelConverter.getUserInfoByUserId(swdRecord.getModificationUser()));
					missionReportInstanceInfo.setLastModifiedDate(new LocalDate((swdRecord.getModificationDate()).getTime()));

					CommentInstanceInfo[] subInstanceInfos = getSubInstancesByRefId(recordId, WorkInstance.FETCH_ALL_SUB_INSTANCE);
					int subInstanceCount = 0;
					if(!CommonUtil.isEmpty(subInstanceInfos)) {
						subInstanceCount = subInstanceInfos.length;
						if(subInstanceInfos.length > WorkInstance.DEFAULT_SUB_INSTANCE_FETCH_COUNT) {
							CommentInstanceInfo[] defaultInstanceInfos = new CommentInstanceInfo[WorkInstance.DEFAULT_SUB_INSTANCE_FETCH_COUNT];
							for(int j=0; j<WorkInstance.DEFAULT_SUB_INSTANCE_FETCH_COUNT; j++) {
								CommentInstanceInfo subInstanceInfo = subInstanceInfos[j];
								defaultInstanceInfos[j] = subInstanceInfo;
							}
							missionReportInstanceInfo.setSubInstances(defaultInstanceInfos);
						} else {
							missionReportInstanceInfo.setSubInstances(subInstanceInfos);
						}
					}
					missionReportInstanceInfo.setSubInstanceCount(subInstanceCount);

					SwdDataField[] swdDataFields = swdRecord.getDataFields();
					List<CommunityInfo> communityInfoList = new ArrayList<CommunityInfo>();
					for(SwdDataField swdDataField : swdDataFields) {
						String value = swdDataField.getValue();
						String refRecordId = swdDataField.getRefRecordId();
						
						if(swdDataField.getId().equals(SeraConstant.MISSION_REPORT_CONTENTFIELDID)) {
							missionReportInstanceInfo.setContent(value);
							missionReportInstanceInfo.setBriefContent(StringUtil.subString(value, 0, 120, "..."));
						} else if(swdDataField.getId().equals(SeraConstant.MISSION_REPORT_IMAGEGROUPIDFIELDID)) {
							
							List<IFileModel> fileList = docMgr.findFileGroup(value);
							if (fileList != null && fileList.size() != 0) {
								IFileModel fileModel = fileList.get(0);
								if(fileModel != null) {
									String filePath = fileModel.getFilePath();
									String extension = filePath.lastIndexOf(".") > 1 ? filePath.substring(filePath.lastIndexOf(".")) : null;
									filePath = StringUtils.replace(filePath, "\\", "/");
									if(filePath.indexOf(companyId) != -1)
										missionReportInstanceInfo.setImageSrcOrigin(Community.PICTURE_PATH + filePath.substring(filePath.indexOf(companyId), filePath.length()));
									filePath = filePath.replaceAll(extension, Community.IMAGE_TYPE_THUMB + extension);
									if(filePath.indexOf(companyId) != -1)
										missionReportInstanceInfo.setImageSrc(Community.PICTURE_PATH + filePath.substring(filePath.indexOf(companyId), filePath.length()));
								}
							}
						} else if(swdDataField.getId().equals(SeraConstant.MISSION_REPORT_VIDEOYTIDFIELDID)) {
							missionReportInstanceInfo.setVideoId(value);
						} else if(swdDataField.getId().equals(SeraConstant.MISSION_REPORT_LINKURLFIELDID)) {
							missionReportInstanceInfo.setLinkUrl(value);
						} else if(swdDataField.getId().equals(SeraConstant.MISSION_REPORT_STARPOINTFIELDID)) {
							missionReportInstanceInfo.setStarPoint(value != null ? Double.parseDouble(value) : 0);
						} else if(swdDataField.getId().equals(SeraConstant.MISSION_REPORT_FILEGROUPIDFIELDID)) {
							missionReportInstanceInfo.setFileGroupId(value);
							
							List<IFileModel> docFileList = docMgr.findFileGroup(value);
							
							if (docFileList != null && docFileList.size() != 0) {
								List<Map<String, String>> fileList = new ArrayList<Map<String, String>>();
								for (int j = 0 ; j <docFileList.size(); j++) {
									Map<String, String> fileMap = new HashMap<String, String>();
									IFileModel docFile = docFileList.get(j);
									fileMap.put("fileId", docFile.getId());
									fileMap.put("fileName", docFile.getFileName());
									fileMap.put("fileSize", docFile.getFileSize()+"");
									fileList.add(fileMap);
								}
								missionReportInstanceInfo.setFileList(fileList);
							}
						}
					}
					missionReportInstanceInfoList.add(missionReportInstanceInfo);
				}
			}
			if(missionReportInstanceInfoList.size() != 0) {
				missionReportInstanceInfos = new MissionReportInstanceInfo[missionReportInstanceInfoList.size()];
				missionReportInstanceInfoList.toArray(missionReportInstanceInfos);
			}
			return missionReportInstanceInfos;

		} catch(Exception e) {
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}
	@Override
	public InstanceInfo[] getSeraInstances(int type, String userId, String courseId, String missionId, String teamId, LocalDate fromDate, int maxList) throws Exception{
		try{

			InstanceInfo[] boardInfo = null;
			InstanceInfo[] eventInfo = null;
			InstanceInfo[] noteInfo = null;
			InstanceInfo[] reportInfo = null;
			InstanceInfo[] messageInfo = null;

			switch (type) {
			case Instance.TYPE_BOARD:
				boardInfo = getBoardInstancesByCourseId(null, courseId, missionId, teamId, null, fromDate, maxList);
				break;
			case Instance.TYPE_EVENT:
				eventInfo = getEventInstanceInfosByWorkSpaceId(userId, courseId, missionId, teamId, fromDate, maxList);
				break;
			case Instance.TYPE_SERA_NOTE:
				noteInfo = getSeraNoteByMissionId(userId, courseId, missionId, teamId, fromDate, maxList);
				break;
			case Instance.TYPE_SERA_MISSION_REPORT:
				reportInfo = getSeraReportByMissionId(userId, courseId, missionId, teamId, fromDate, maxList);
				break;
			case Instance.TYPE_ASYNC_MESSAGE:
				messageInfo = instanceService.getMyMessageInstancesByType(type, fromDate, maxList);
				break;
			case Instance.TYPE_SENT_ASYNC_MESSAGE:
				messageInfo = instanceService.getMyMessageInstancesByType(type, fromDate, maxList);
				break;
			default:
				boardInfo = getBoardInstancesByCourseId(userId, courseId, missionId, teamId, null, fromDate, maxList);
				eventInfo = getEventInstanceInfosByWorkSpaceId(userId, courseId, missionId, teamId, fromDate, maxList);
				noteInfo = getSeraNoteByMissionId(userId, courseId, missionId, teamId, fromDate, maxList);
				reportInfo = getSeraReportByMissionId(userId, courseId, missionId, teamId, fromDate, maxList);
				break;
			}

			Map<Long, InstanceInfo> resultMap = new HashMap<Long, InstanceInfo>();
			if (boardInfo != null) {
				for (InstanceInfo info : boardInfo) {
					LocalDate createDate = info.getCreatedDate();
					resultMap.put(createDate.getTime(), info);
				}
			}
			if (eventInfo != null) {
				for (InstanceInfo info : eventInfo) {
					LocalDate createDate = info.getCreatedDate();
					resultMap.put(createDate.getTime(), info);
				}
			}
			if (noteInfo != null) {
				for (InstanceInfo info : noteInfo) {
					LocalDate createDate = info.getCreatedDate();
					resultMap.put(createDate.getTime(), info);
				}
			}
			if (reportInfo != null) {
				for (InstanceInfo info : reportInfo) {
					LocalDate createDate = info.getCreatedDate();
					resultMap.put(createDate.getTime(), info);
				}
			}
			if (messageInfo != null) {
				for (InstanceInfo info : messageInfo) {
					LocalDate createDate = info.getCreatedDate();
					resultMap.put(createDate.getTime(), info);
				}
			}
			if (resultMap.size() ==  0)
				return null;

			Map<Long, InstanceInfo> sortMap = new TreeMap<Long, InstanceInfo>(Collections.reverseOrder());
			sortMap.putAll(resultMap);

			List returnInstanceInfoList = new ArrayList();
			Iterator<Long> itr = sortMap.keySet().iterator();
			int i = 0;
			while (itr.hasNext()) {
				if (i == maxList) 
					break;
				returnInstanceInfoList.add(sortMap.get(itr.next()));
				i++;
			}

			if(sortMap.size() > maxList)
				returnInstanceInfoList.add(new WorkInstanceInfo());
			InstanceInfo[] returnInstanceInfo = new InstanceInfo[returnInstanceInfoList.size()];
			returnInstanceInfoList.toArray(returnInstanceInfo);
			//InstanceInfo[] instances = SeraTest.getSeraInstances(userId, courseId, missionId, fromDate, maxList);

			return returnInstanceInfo;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
		
	}

	@Override
	public void removeSeraInstane(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		try {

			int workType = (Integer)requestBody.get("workType");
			String workInstanceId = (String)requestBody.get("workInstanceId");
			String workId = null;

			switch (workType) {
			case SocialWork.TYPE_BOARD:
				workId = SmartWork.ID_BOARD_MANAGEMENT;
				break;
			case SocialWork.TYPE_EVENT:
				workId = SmartWork.ID_EVENT_MANAGEMENT;
				break;
			case Work.TYPE_SERA_NOTE:
				workId = SmartWork.ID_SERA_NOTE_MANAGEMENT;
				break;
			case Work.TYPE_SERA_MISSION_REPORT:
				workId = SmartWork.ID_SERA_MISSION_REPORT_MANAGEMENT;
				break;
			}

			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();

			SwfFormCond swfFormCond = new SwfFormCond();
			swfFormCond.setPackageId(workId);
			SwfForm[] swfForms = getSwfManager().getForms(userId, swfFormCond, IManager.LEVEL_LITE);

			SwfForm swfForm = null;
			if(!CommonUtil.isEmpty(swfForms)) {
				swfForm = swfForms[0];
			}

			String formId = swfForm.getId();
			SwdDomainCond swdDomainCond = new SwdDomainCond();
			swdDomainCond.setFormId(formId);
			SwdDomain swdDomain = getSwdManager().getDomain(userId, swdDomainCond, IManager.LEVEL_LITE);
			String domainId = swdDomain.getObjId();

			getSwdManager().removeRecord(userId, domainId, workInstanceId);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	//TODO
	@Override
	public String performMissionReport(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		/*{
			courseId=group_1f22476fe184438586488da824692919, 
			missionId=dr_402880eb36b59d9e0136b5b852560007, 
			frmPerformMissionReport=
				{
					txtReportContent=미션 수행 합니다., 
					txtNoteUrl=, 
					selAccessLevel=3, 
					txtNoteFile=
						{
							groupId=fg_b3f55fdfce205c4aacc9e98c573703f34125, 
							files=[]
						}, 
					imgNoteImage=
						{
							groupId=fg_d3321bb05957e5467b5b24553a269b80c50b, 
							files=[]
						},
					ytNoteVideo=
						{
						}
				}
		}*/
		User user = SmartUtil.getCurrentUser();
		String userId = user.getId();

		String missionId = (String)requestBody.get("missionId");
		String courseId = (String)requestBody.get("courseId");
		Map<String, Object> frmCreateSeraNoteMap = (Map<String, Object>)requestBody.get("frmPerformMissionReport");

		Set<String> keySet = frmCreateSeraNoteMap.keySet();
		Iterator<String> itr = keySet.iterator();
		
		String txtReportContent = null;
		String selAccessLevel = null;
		String txtNoteUrl = null;
		String startPoint = null;
		if(requestBody.get("starPoint").getClass().equals(Integer.class))
			startPoint = String.valueOf((Integer)requestBody.get("starPoint"));
		else if(requestBody.get("starPoint").getClass().equals(Double.class))
			startPoint = String.valueOf((Double)requestBody.get("starPoint"));

		Map<String, Object> txtNoteFile = null;
		String fileGroupId = null;
		Map<String, List<Map<String, String>>> fileGroupMap = new HashMap<String, List<Map<String, String>>>();
		
		Map<String, Object> imgNoteImage = null;
		String imageGroupId = null;
		String imageSrc = null;
		String imageSrcOriginal = null;
		Map<String, List<Map<String, String>>> imageGroupMap = new HashMap<String, List<Map<String, String>>>();
		
		Map<String, Object> ytNoteVideo = null;
		String videoYTId = null;
		String videoFileName = null;
		String videoFileSize = null;

		while (itr.hasNext()) {
			String fieldId = (String)itr.next();
			Object fieldValue = frmCreateSeraNoteMap.get(fieldId);
			if (fieldValue instanceof LinkedHashMap) {
				if (fieldId.equalsIgnoreCase("txtNoteFile")) {
					txtNoteFile = (Map<String, Object>)fieldValue;
					if(txtNoteFile != null && txtNoteFile.size() > 0) {
						fileGroupId = (String)txtNoteFile.get("groupId");
						List<Map<String, String>> files = (ArrayList<Map<String,String>>)txtNoteFile.get("files");
						if(!CommonUtil.isEmpty(files)) {
							fileGroupMap.put(fileGroupId, files);
						}
					}
				} else if (fieldId.equalsIgnoreCase("imgNoteImage")) {
					imgNoteImage = (Map<String, Object>)fieldValue;
					if(imgNoteImage != null && imgNoteImage.size() > 0) {
						imageGroupId = (String)imgNoteImage.get("groupId");

						List<Map<String, String>> files = (ArrayList<Map<String,String>>)imgNoteImage.get("files");
						if(!CommonUtil.isEmpty(files)) {
							imageGroupMap.put(imageGroupId, files);
						}
					}
				} else if (fieldId.equalsIgnoreCase("ytNoteVideo")) {
					ytNoteVideo = (Map<String, Object>)fieldValue;
					if(ytNoteVideo != null && ytNoteVideo.size() > 0) {
						Map videoMap = (Map<String, Object>)ytNoteVideo.get("video");
						if(videoMap != null && videoMap.size() > 0) {
							videoYTId = (String)videoMap.get("videoYTId");
							videoFileName = (String)videoMap.get("fileName");
							videoFileSize = (String)videoMap.get("fileSize");
						}
					}
				}
			} else if(fieldValue instanceof String) {
				if (fieldId.equals("txtReportContent")) {
					txtReportContent = (String)frmCreateSeraNoteMap.get("txtReportContent");
				} else if (fieldId.equals("selAccessLevel")) {
					selAccessLevel = (String)frmCreateSeraNoteMap.get("selAccessLevel");
				} else if (fieldId.equals("txtNoteUrl")) {
					txtNoteUrl = (String)frmCreateSeraNoteMap.get("txtNoteUrl");
				}
			}
		}
		
		SwdDomainCond swdDomainCond = new SwdDomainCond();
		swdDomainCond.setFormId(SeraConstant.MISSION_REPORT_FORMID);
		SwdDomain swdDomain = SwManagerFactory.getInstance().getSwdManager().getDomain(userId, swdDomainCond, IManager.LEVEL_LITE);
		String domainId = swdDomain.getObjId();
		
		SwdFieldCond swdFieldCond = new SwdFieldCond();
		swdFieldCond.setDomainObjId(domainId);
		SwdField[] fields = SwManagerFactory.getInstance().getSwdManager().getFields(userId, swdFieldCond, IManager.LEVEL_LITE);
		if (CommonUtil.isEmpty(fields))
			return null;//TODO return null? throw new Exception??

		Map<String, SwdField> fieldInfoMap = new HashMap<String, SwdField>();
		for (SwdField field : fields) {
			fieldInfoMap.put(field.getFormFieldId(), field);
		}

		List fieldDataList = new ArrayList();
		for (SwdField field : fields) {
			String fieldId = field.getFormFieldId();
			SwdDataField fieldData = new SwdDataField();
			fieldData.setId(fieldId);
			fieldData.setName(field.getFormFieldName());
			fieldData.setRefForm(null);
			fieldData.setRefFormField(null);
			fieldData.setRefRecordId(null);
			
			if (fieldId.equalsIgnoreCase(SeraConstant.MISSION_REPORT_IMAGEGROUPIDFIELDID)) {
				fieldData.setValue(imageGroupId);
			} else if (fieldId.equalsIgnoreCase(SeraConstant.MISSION_REPORT_LINKURLFIELDID)) {
				fieldData.setValue(txtNoteUrl);
			}  else if (fieldId.equalsIgnoreCase(SeraConstant.MISSION_REPORT_FILEGROUPIDFIELDID)) {
				fieldData.setValue(fileGroupId);
			}  else if (fieldId.equalsIgnoreCase(SeraConstant.MISSION_REPORT_CONTENTFIELDID)) {
				fieldData.setValue(txtReportContent);
			}  else if (fieldId.equalsIgnoreCase(SeraConstant.MISSION_REPORT_VIDEOYTIDFIELDID)) {
				fieldData.setValue(videoYTId);
			}  else if (fieldId.equalsIgnoreCase(SeraConstant.MISSION_REPORT_VIDEOFILENAMEFIELDID)) {
				fieldData.setValue(videoFileName);
			}  else if (fieldId.equalsIgnoreCase(SeraConstant.MISSION_REPORT_VIDEOFILESIZEFIELDID)) {
				fieldData.setValue(videoFileSize);
			}  else if (fieldId.equalsIgnoreCase(SeraConstant.MISSION_REPORT_STARPOINTFIELDID)) {
				fieldData.setValue(startPoint);
			}
			fieldDataList.add(fieldData);
		}

		SwdDataField[] fieldDatas = new SwdDataField[fieldDataList.size()];
		fieldDataList.toArray(fieldDatas);
		SwdRecord obj = new SwdRecord();
		obj.setDomainId(domainId);
		obj.setFormId(SeraConstant.MISSION_REPORT_FORMID);
		obj.setFormName(swdDomain.getFormName());
		obj.setFormVersion(swdDomain.getFormVersion());
		obj.setDataFields(fieldDatas);
		obj.setRecordId("dr_" + CommonUtil.newId());
		
		obj.setWorkSpaceId(missionId);
		obj.setWorkSpaceType("2");
		obj.setAccessLevel(selAccessLevel);
		obj.setAccessValue(null);

		String recordId = SwManagerFactory.getInstance().getSwdManager().setRecord(userId, obj, IManager.LEVEL_ALL);

		TskTaskCond tskCond = new TskTaskCond();
		tskCond.setExtendedProperties(new Property[] {new Property("recordId", recordId)});
		tskCond.setModificationUser(userId);
		tskCond.setOrders(new Order[]{new Order(TskTaskCond.A_CREATIONDATE, false)});
		TskTask[] tskTasks = SwManagerFactory.getInstance().getTskManager().getTasks(userId, tskCond, IManager.LEVEL_LITE);
		String taskInstId = tskTasks[0].getObjId();
		
		if(fileGroupMap.size() > 0) {
			for(Map.Entry<String, List<Map<String, String>>> entry : fileGroupMap.entrySet()) {
				String fGroupId = entry.getKey();
				List<Map<String, String>> fileGroups = entry.getValue();
				try {
					for(int i=0; i < fileGroups.subList(0, fileGroups.size()).size(); i++) {
						Map<String, String> file = fileGroups.get(i);
						String fileId = file.get("fileId");
						String fileName = file.get("fileName");
						String fileSize = file.get("fileSize");
						SwManagerFactory.getInstance().getDocManager().insertFiles("Files", taskInstId, fGroupId, fileId, fileName, fileSize);
					}
				} catch (Exception e) {
					throw new DocFileException("file upload fail...");
				}
			}
		}
		if(imageGroupMap.size() > 0) {
			for(Map.Entry<String, List<Map<String, String>>> entry : imageGroupMap.entrySet()) {
				String imgGroupId = entry.getKey();
				List<Map<String, String>> imgGroups = entry.getValue();
				try {
					for(int i=0; i < imgGroups.subList(0, imgGroups.size()).size(); i++) {
						Map<String, String> file = imgGroups.get(i);
						String fileId = file.get("fileId");
						String fileName = file.get("fileName");
						//String fileSize = file.get("fileSize");
						SwManagerFactory.getInstance().getDocManager().insertFiles("Pictures", taskInstId, imgGroupId, fileId, fileName, "0");
					}
				} catch (Exception e) {
					throw new DocFileException("image upload fail...");
				}
			}
		}

		return recordId;
	}

	@Override
	public String createNewTeam(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try {
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();
			String courseId = (String)requestBody.get("courseId");
			Map<String, Object> frmCreateTeamMap = (Map<String, Object>)requestBody.get("frmCreateTeam");

			String txtTeamName = null;
			String txaTeamDesc = null;
			String txtTeamDays = null;
			String txtTeamStartDate = null;
			String txtTeamEndDate = null;
			String chkTeamSecurity = null;
			String txtTeamUsers = null;
			List<Map<String, String>> users = null;
	
			Set<String> keySet = frmCreateTeamMap.keySet();
			Iterator<String> itr = keySet.iterator();
	
			while (itr.hasNext()) {
				String fieldId = (String)itr.next();
				Object fieldValue = frmCreateTeamMap.get(fieldId);
				if(fieldValue instanceof LinkedHashMap) {
					Map<String, Object> valueMap = (Map<String, Object>)fieldValue;
					if(fieldId.equals("txtInviteMembers")) {
						users = (ArrayList<Map<String,String>>)valueMap.get("users");
					}
				} else if(fieldValue instanceof String) {
					if(fieldId.equals("txtTeamName")) {
						txtTeamName = (String)frmCreateTeamMap.get("txtTeamName");
					} else if(fieldId.equals("txaTeamDesc")) {
						txaTeamDesc = (String)frmCreateTeamMap.get("txaTeamDesc");
					} else if(fieldId.equals("txtTeamDays")) {
						txtTeamDays = (String)frmCreateTeamMap.get("txtTeamDays");
					} else if(fieldId.equals("txtTeamStartDate")) {
						txtTeamStartDate = (String)frmCreateTeamMap.get("txtTeamStartDate");
					} else if(fieldId.equals("txtTeamEndDate")) {
						txtTeamEndDate = (String)frmCreateTeamMap.get("txtTeamEndDate");
					} else if(fieldId.equals("chkTeamSecurity")) {
						chkTeamSecurity = (String)frmCreateTeamMap.get("chkTeamSecurity");
					} else if(fieldId.equals("txtTeamUsers")) {
						txtTeamUsers = (String)frmCreateTeamMap.get("txtTeamUsers");
					}
				}
			}
	
			Date startDate = null;
			Date endDate = null;
	
			if(!CommonUtil.isEmpty(txtTeamDays)) {
				startDate =  LocalDate.convertLocalDateStringToLocalDate(new LocalDate().toLocalDateSimpleString());
				endDate = new LocalDate(startDate.getTime() + LocalDate.ONE_DAY*(Integer.parseInt(txtTeamDays)-1));
			} else {
				startDate = LocalDate.convertLocalDateStringToLocalDate(txtTeamStartDate);
				endDate = LocalDate.convertLocalDateStringToLocalDate(txtTeamEndDate);
			}

			CourseTeam courseTeam = new CourseTeam();
			courseTeam.setCourseId(courseId);
			courseTeam.setName(txtTeamName);
			courseTeam.setDescription(txaTeamDesc);
			courseTeam.setAccessPolicy(Integer.parseInt(chkTeamSecurity));
			courseTeam.setMemberSize(Integer.parseInt(txtTeamUsers));
			courseTeam.setStartDate(startDate);
			courseTeam.setEndDate(endDate);

			//멘티일 경우 - 팀생성자 포함
			//멘토일 경우 - 팀생성자 미포함
			SwoGroup swoGroup = getSwoManager().getGroup(userId, courseId, IManager.LEVEL_LITE);
			String mentorId = swoGroup.getGroupLeader();

			boolean isMentor = false;
			if(!userId.equals(mentorId)) {
				CourseTeamUser courseTeamLeader = new CourseTeamUser();
				courseTeamLeader.setUserId(userId);
				courseTeamLeader.setJoinType(CourseTeamUser.JOINTYPE_TEAMLEADER);
				courseTeamLeader.setJoinStatus(CourseTeamUser.JOINSTATUS_COMPLETE);
				courseTeam.addTeamMember(courseTeamLeader);
			}

			if(!CommonUtil.isEmpty(users)) {
				if(!isMentor) {
					if(users.size()+1 > Integer.parseInt(txtTeamUsers))
						throw new ArrayIndexOutOfBoundsException();
				} else {
					if(users.size() > Integer.parseInt(txtTeamUsers))
						throw new ArrayIndexOutOfBoundsException();
				}
				for(int i=0; i<users.size(); i++) {
					Map<String, String> userMap = users.get(i);
					String teamUserId = userMap.get("id");
					CourseTeamUser courseTeamUser = new CourseTeamUser();
					courseTeamUser.setUserId(teamUserId);
					courseTeamUser.setJoinType(CourseTeamUser.JOINTYPE_INVITE);
					courseTeamUser.setJoinStatus(CourseTeamUser.JOINSTATUS_READY); //초대에 대한 응답 구현 후 CourseTeamUser.JOINSTATUS_READY로 변경 예정
					courseTeam.addTeamMember(courseTeamUser);
				}
			}

			courseTeam = getSeraManager().setCourseTeam(userId, courseTeam);
			return courseTeam.getObjId();
			
		} catch (ArrayIndexOutOfBoundsException ae) {
			throw new ArrayIndexOutOfBoundsException("txtTeamUsers Exceed~!!");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public void modifyCourseTeam(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		try {
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();
			String teamId = (String)requestBody.get("teamId");
			Map<String, Object> frmSetTeamMap = (Map<String, Object>)requestBody.get("frmSetTeam");

			String txtaTeamDesc = null;
			String txtTeamDays = null;
			String txtTeamStartDate = null;
			String txtTeamEndDate = null;
			String chkTeamSecurity = null;
			String txtTeamUsers = null;

			Set<String> keySet = frmSetTeamMap.keySet();
			Iterator<String> itr = keySet.iterator();
	
			while (itr.hasNext()) {
				String fieldId = (String)itr.next();
				Object fieldValue = frmSetTeamMap.get(fieldId);
				if(fieldValue instanceof String) {
					if(fieldId.equals("txtaTeamDesc")) {
						txtaTeamDesc = (String)frmSetTeamMap.get("txtaTeamDesc");
					} else if(fieldId.equals("txtTeamDays")) {
						txtTeamDays = (String)frmSetTeamMap.get("txtTeamDays");
					} else if(fieldId.equals("txtTeamStartDate")) {
						txtTeamStartDate = (String)frmSetTeamMap.get("txtTeamStartDate");
					} else if(fieldId.equals("txtTeamEndDate")) {
						txtTeamEndDate = (String)frmSetTeamMap.get("txtTeamEndDate");
					} else if(fieldId.equals("chkTeamSecurity")) {
						chkTeamSecurity = (String)frmSetTeamMap.get("chkTeamSecurity");
					} else if(fieldId.equals("txtTeamUsers")) {
						txtTeamUsers = (String)frmSetTeamMap.get("txtTeamUsers");
					}
				}
			}

			Date startDate = null;
			Date endDate = null;

			if(!CommonUtil.isEmpty(txtTeamDays)) {
				startDate =  LocalDate.convertLocalDateStringToLocalDate(new LocalDate().toLocalDateSimpleString());
				endDate = new LocalDate(startDate.getTime() + LocalDate.ONE_DAY*(Integer.parseInt(txtTeamDays)-1));
			} else {
				startDate = LocalDate.convertLocalDateStringToLocalDate(txtTeamStartDate);
				endDate = LocalDate.convertLocalDateStringToLocalDate(txtTeamEndDate);
			}

			CourseTeam courseTeam = getSeraManager().getCourseTeam(userId, teamId, IManager.LEVEL_ALL);
			courseTeam.setDescription(txtaTeamDesc);
			courseTeam.setAccessPolicy(Integer.parseInt(chkTeamSecurity));
			courseTeam.setMemberSize(Integer.parseInt(txtTeamUsers));
			courseTeam.setStartDate(startDate);
			courseTeam.setEndDate(endDate);

			getSeraManager().setCourseTeam(userId, courseTeam);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("modify course team error...");
		}
	}

	@Override
	public void removeCourseTeam(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		try {
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();
			String teamId = (String)requestBody.get("teamId");
			getSeraManager().removeCourseTeam(userId, teamId);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String updateSeraProfile(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		/*{
			frmSeraProfile=
				{
					txtNickName=121, 
					txtEmail=kj@maninsoft.co.kr, 
					txtBirthYear=2012, 
					txtBirthMonth=04, 
					txtBirthDay=22, 
					selSex=2, 
					txtGoal=, 
					txtInterests=11, 
					txtEducations=, 
					txtWorks=, 
					txtPassword=1, 
					txtConfirmPassword=1, 
					imgMyProfile=
						{
							groupId=fg_80c988a2420504457e4ad704f8cc8dcae966, 
							files=[]
						}
				}
		}*/

		User user = SmartUtil.getCurrentUser();
		String userId = user.getId();
		
		Map<String, Object> frmSeraProfileMap = (Map<String, Object>)requestBody.get("frmSeraProfile");

		Set<String> keySet = frmSeraProfileMap.keySet();
		Iterator<String> itr = keySet.iterator();
		
		Map<String, Object> imgUserProfileImage = null;
		String imageGroupId = null;
		Map<String, List<Map<String, String>>> imageGroupMap = new HashMap<String, List<Map<String, String>>>();
		
		String txtNickName = null;
		String txtEmail = null;
		String txtBirthYear = null;
		String txtBirthMonth = null;
		String txtBirthDay = null;
		String selSex = null;
		String txtGoal = null;
		String txtInterests = null;
		String txtEducations = null;
		String txtWorks = null;
		String txtPassword = null;
		String txtConfirmPassword = null;
		
		while (itr.hasNext()) {
			String fieldId = (String)itr.next();
			Object fieldValue = frmSeraProfileMap.get(fieldId);
			if (fieldValue instanceof LinkedHashMap) {
				if (fieldId.equalsIgnoreCase("imgMyProfile")) {
					imgUserProfileImage = (Map<String, Object>)fieldValue;
					if(imgUserProfileImage != null && imgUserProfileImage.size() > 0) {
						imageGroupId = (String)imgUserProfileImage.get("groupId");
	
						List<Map<String, String>> files = (ArrayList<Map<String,String>>)imgUserProfileImage.get("files");
						if(!CommonUtil.isEmpty(files)) {
							imageGroupMap.put(imageGroupId, files);
						}
					}
				}
			} else if(fieldValue instanceof String) {
				if (fieldId.equals("txtNickName")) {
					txtNickName = (String)frmSeraProfileMap.get("txtNickName");
				} else if (fieldId.equals("txtEmail")) {
					txtEmail = (String)frmSeraProfileMap.get("txtEmail");
				} else if (fieldId.equals("txtBirthYear")) {
					txtBirthYear = (String)frmSeraProfileMap.get("txtBirthYear");
				} else if (fieldId.equals("txtConfirmPassword")) {
					txtBirthMonth = (String)frmSeraProfileMap.get("txtBirthMonth");
				} else if (fieldId.equals("txtBirthDay")) {
					txtBirthDay = (String)frmSeraProfileMap.get("txtBirthDay");
				} else if (fieldId.equals("selSex")) {
					selSex = (String)frmSeraProfileMap.get("selSex");
				} else if (fieldId.equals("txtGoal")) {
					txtGoal = (String)frmSeraProfileMap.get("txtGoal");
				} else if (fieldId.equals("txtInterests")) {
					txtInterests = (String)frmSeraProfileMap.get("txtInterests");
				} else if (fieldId.equals("txtEducations")) {
					txtEducations = (String)frmSeraProfileMap.get("txtEducations");
				} else if (fieldId.equals("txtWorks")) {
					txtWorks = (String)frmSeraProfileMap.get("txtWorks");
				}	else if (fieldId.equals("txtPassword")) {
					txtPassword = (String)frmSeraProfileMap.get("txtPassword");
				}	else if (fieldId.equals("txtConfirmPassword")) {
					txtConfirmPassword = (String)frmSeraProfileMap.get("txtConfirmPassword");
				}
			}
		}
		String txtUserProfilePicture = null;
		if(imageGroupMap.size() > 0) {
			for(Map.Entry<String, List<Map<String, String>>> entry : imageGroupMap.entrySet()) {
				String imgGroupId = entry.getKey();
				List<Map<String, String>> imgGroups = entry.getValue();
				try {
					for(int i=0; i < imgGroups.subList(0, imgGroups.size()).size(); i++) {
						Map<String, String> file = imgGroups.get(i);
						String fileId = file.get("fileId");
						String fileName = file.get("fileName");
						//String fileSize = file.get("fileSize");
						txtUserProfilePicture = SwManagerFactory.getInstance().getDocManager().insertProfilesFile(fileId, fileName, userId);
					}
				} catch (Exception e) {
					throw new DocFileException("image upload fail...");
				}
			}
		}
		
		ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();
		ISeraManager seraMgr = SwManagerFactory.getInstance().getSeraManager();
		
		SwoUserCond cond = new SwoUserCond();
		cond.setId(userId);
		SwoUser swoUser = swoMgr.getUser(userId, cond, IManager.LEVEL_ALL);
		if (swoUser == null)
			return null;

		swoUser.setNickName(txtNickName);
		swoUser.setEmail(txtEmail);
		swoUser.setPassword(txtPassword);
		swoUser.setPicture(txtUserProfilePicture);
		
		SeraUserDetail seraUserDetail = seraMgr.getSeraUserById(userId, userId);
		
		seraUserDetail.setNickName(txtNickName);
		seraUserDetail.setEmail(txtEmail);
		
		if (!CommonUtil.isEmpty(txtBirthYear) && !CommonUtil.isEmpty(txtBirthMonth) && !CommonUtil.isEmpty(txtBirthDay)) {
			String birthDayString = txtBirthYear + (txtBirthMonth.length() == 1 ? "0" + txtBirthMonth : txtBirthMonth) + (txtBirthDay.length() == 1 ? "0" + txtBirthDay : txtBirthDay) + "0000";
			Date birthDay = LocalDate.convertStringToDate(birthDayString);
			seraUserDetail.setBirthday(new LocalDate(birthDay.getTime()));
		}
		seraUserDetail.setSex(CommonUtil.isEmpty(selSex) ? 0 : Integer.parseInt(selSex));
		seraUserDetail.setGoal(txtGoal);
		seraUserDetail.setInterests(txtInterests);
		seraUserDetail.setEducations(txtEducations);
		seraUserDetail.setWorks(txtWorks);
		
		swoMgr.setUser(userId, swoUser, IManager.LEVEL_ALL);
		seraMgr.setSeraUser(userId, seraUserDetail);

		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(swoUser.getId(), swoUser.getPassword());
        Authentication authentication = authenticationManager.authenticate(authRequest);
        SecurityContext securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
        getSwoManager().getUserExtend(userId, userId, false);

		return userId;
	}

	@Override
	public void addReviewOnCourse(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try {
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();
			String courseId = (String)requestBody.get("courseId");
			String content = (String)requestBody.get("reviewContent");
			Double starPoint = null;
			if(requestBody.get("starPoint").getClass().equals(Integer.class))
				starPoint = Double.parseDouble(String.valueOf((Integer)requestBody.get("starPoint")));
			else if(requestBody.get("starPoint").getClass().equals(Double.class))
				starPoint = (Double)requestBody.get("starPoint");

			CourseReview courseReview = new CourseReview();
			courseReview.setCourseId(courseId);
			courseReview.setContent(content);
			courseReview.setStarPoint(starPoint);
			courseReview.setCreationUser(userId);
			courseReview.setCreationDate(new LocalDate());
			courseReview.setModificationUser(userId);
			courseReview.setModificationDate(new LocalDate());

			getSeraManager().setCourseReview(userId, courseReview);

			/*CourseDetail courseDetail = getSeraManager().getCourseDetailById(courseId);

			Double courseStarPoint = courseDetail.getStarPoint();
			int courseScorePointUsers = courseDetail.getScorePointUsers();

			courseStarPoint = (courseStarPoint + starPoint) / (courseScorePointUsers + 1);
			courseScorePointUsers = courseScorePointUsers + 1;

			courseDetail.setStarPoint(courseStarPoint);
			courseDetail.setScorePointUsers(courseScorePointUsers);

			getSeraManager().setCourseDetail(courseDetail);*/

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public ReviewInstanceInfo[] getReviewInstancesByCourse(String courseId, LocalDate fromDate, int maxList) throws Exception{
		try {
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();
			CourseReviewCond courseReviewCond = new CourseReviewCond();
			courseReviewCond.setCourseId(courseId);
			courseReviewCond.setModificationDateFrom(fromDate);
			courseReviewCond.setPageSize(maxList);
			courseReviewCond.setPageNo(0);
			courseReviewCond.setOrders(new Order[]{new Order(CourseReviewCond.A_MODIFICATIONDATE, false)});

			ReviewInstanceInfo[] reviewInstanceInfos = null;
			CourseReview[] courseReviews = getSeraManager().getCourseReviews(userId, courseReviewCond);
			if(!CommonUtil.isEmpty(courseReviews)) {
				int courseReviewsLength = courseReviews.length;
				reviewInstanceInfos = new ReviewInstanceInfo[courseReviewsLength];
				for(int i=0; i<courseReviewsLength; i++) {
					CourseReview courseReview = courseReviews[i];
					ReviewInstanceInfo reviewInstanceInfo = new ReviewInstanceInfo();
					reviewInstanceInfo.setContent(courseReview.getContent());
					reviewInstanceInfo.setStarPoint(courseReview.getStarPoint());
					String creationUser = courseReview.getCreationUser();
					Date creationDate = courseReview.getCreationDate();
					String modificationUser = courseReview.getModificationUser();
					Date modificatonDate = courseReview.getModificationDate();
					UserInfo owner = ModelConverter.getUserInfoByUserId(creationUser);
					LocalDate createdDate = new LocalDate(creationDate.getTime());
					UserInfo lastModifier = ModelConverter.getUserInfoByUserId(modificationUser);
					LocalDate lastModifiedDate = new LocalDate(modificatonDate.getTime());
					reviewInstanceInfo.setOwner(owner);
					reviewInstanceInfo.setCreatedDate(createdDate);
					reviewInstanceInfo.setLastModifier(lastModifier);
					reviewInstanceInfo.setLastModifiedDate(lastModifiedDate);
					reviewInstanceInfos[i] = reviewInstanceInfo;
				}
			}
			return reviewInstanceInfos;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}		
	}

	public CourseInfo[] getFavoriteCourses(String fromCourseId, int maxList) throws Exception {
		
		CourseDetailCond courseDetailCond = new CourseDetailCond();

		if (fromCourseId != null) {
			CourseDetail lastCourseDetailInfo = SwManagerFactory.getInstance().getSeraManager().getCourseDetailById(fromCourseId);
			if (lastCourseDetailInfo != null) {
				int coursePoint = lastCourseDetailInfo.getCoursePoint();
				if (coursePoint == 0) {
					courseDetailCond.setCoursePoint(0);
				} else {
					courseDetailCond.setCoursePointTo(lastCourseDetailInfo.getCoursePoint());
				}
				courseDetailCond.setCreateDateTo(lastCourseDetailInfo.getCreateDate());
			}
		}

		Long totalSize = SwManagerFactory.getInstance().getSeraManager().getCourseDetailSize("", courseDetailCond);
		
		//courseDetailCond.setEndFrom(new LocalDate());
		courseDetailCond.setOrders(new Order[]{new Order("coursePoint", false) , new Order("createDate", false)});
		courseDetailCond.setPageNo(0);
		courseDetailCond.setPageSize(maxList);
		
		CourseDetail[] courseDetails = SwManagerFactory.getInstance().getSeraManager().getCourseDetails("", courseDetailCond);
		if (courseDetails == null || courseDetails.length == 0) 
			return null;
	    
		String[] courseIds = new String[courseDetails.length];
		for (int i = 0; i < courseDetails.length; i++) {
			courseIds[i] = courseDetails[i].getCourseId();
		}

		SwoGroupCond groupCond = new SwoGroupCond();
		groupCond.setGroupIdIns(courseIds);
		ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();
		SwoGroup[] groups = swoMgr.getGroups("", groupCond, IManager.LEVEL_ALL);

		SwoGroup[] tempGroups = new SwoGroup[courseDetails.length];
		for (int i = 0; i < courseDetails.length; i++) {
			String courseId = courseDetails[i].getCourseId();
			for (int j = 0; j < groups.length; j++) {
				if (groups[j].getId().equalsIgnoreCase(courseId)) {
					tempGroups[i] = groups[j];
					break;
				}
			}
		}
		groups = tempGroups;
		
		CourseInfo[] courses = convertSwoGroupArrayToCourseInfoArray(groups, courseDetails);
		CourseInfo[] result = null;
		
		if (totalSize > maxList) {
			result = new CourseInfo[courses.length + 1];
			for (int i = 0; i < courses.length; i++) {
				result[i] = courses[i];
			}
			result[courses.length] = new CourseInfo();
		} else {
			result = courses;
		}
		
		return result;
	}
	@Override
	public CourseInfo[] getFavoriteCourses(int maxList) throws Exception {
		return getFavoriteCourses(null, maxList);
	}
	public CourseInfo[] getRecommendedCourses(String fromCourseId, int maxList) throws Exception {

		//추천 받은 코스중에 날짜순으로 maxList 만큼
	    CourseDetailCond courseDetailCond = new CourseDetailCond();
	    courseDetailCond.setRecommended(true);
	    
		if (fromCourseId != null) {
			CourseDetail lastCourseDetailInfo = SwManagerFactory.getInstance().getSeraManager().getCourseDetailById(fromCourseId);
			if (lastCourseDetailInfo != null)
				courseDetailCond.setCreateDateTo(lastCourseDetailInfo.getCreateDate());
		}

		Long totalSize = SwManagerFactory.getInstance().getSeraManager().getCourseDetailSize("", courseDetailCond);
	    
	    courseDetailCond.setOrders(new Order[]{new Order("createDate", false)});
	    //courseDetailCond.setEndFrom(new LocalDate());
	    courseDetailCond.setPageNo(0);
	    courseDetailCond.setPageSize(maxList);
		
	    CourseDetail[] courseDetails = SwManagerFactory.getInstance().getSeraManager().getCourseDetails("", courseDetailCond);
	    if (courseDetails == null || courseDetails.length == 0) 
	     return null;
	     
	    String[] courseIds = new String[courseDetails.length];
	    for (int i = 0; i < courseDetails.length; i++) {
	     courseIds[i] = courseDetails[i].getCourseId();
	    }
	    
	    SwoGroupCond groupCond = new SwoGroupCond();
	    groupCond.setGroupIdIns(courseIds);
	    ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();
	    SwoGroup[] groups = swoMgr.getGroups("", groupCond, IManager.LEVEL_ALL);

		SwoGroup[] tempGroups = new SwoGroup[courseDetails.length];
		for (int i = 0; i < courseDetails.length; i++) {
			String courseId = courseDetails[i].getCourseId();
			for (int j = 0; j < groups.length; j++) {
				if (groups[j].getId().equalsIgnoreCase(courseId)) {
					tempGroups[i] = groups[j];
					break;
				}
			}
		}
		groups = tempGroups;
	    
	    CourseInfo[] courses = convertSwoGroupArrayToCourseInfoArray(groups, courseDetails);
	    CourseInfo[] result = null;
		
		if (totalSize > maxList) {
			result = new CourseInfo[courses.length + 1];
			for (int i = 0; i < courses.length; i++) {
				result[i] = courses[i];
			}
			result[courses.length] = new CourseInfo();
		} else {
			result = courses;
		}
	    //CourseList courses = getCoursesById("ysjung@maninsoft.co.kr", 6);
	    return result;
	}
	@Override
	public CourseInfo[] getRecommendedCourses(int maxList) throws Exception {
		return getRecommendedCourses(null, maxList);
	}
	
	public String leaveSeraUser(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		String userId = (String)requestBody.get("userId");
		if (CommonUtil.isEmpty(userId))
			return null;
		
		ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();
		swoMgr.removeUser(userId, userId);
		
		ISeraManager seraMgr = SwManagerFactory.getInstance().getSeraManager();
		seraMgr.removeSeraUser(userId, userId);
		
		return userId;
	}
	@Override
	public String createSeraUser(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		/*{
			frmCreateSeraUser=
				{
					txtUserId=kmyu@maninsoft.co.kr,
					hdnUserName = 유광민, 
					txtNickName= 1, 
					txtPassword=1, 
					txtConfirmPassword=1, 
					txtBirthYear=1999, 
					txtBirthMonth=1, 
					txtBirthDay=1, 
					selSex=female, 
					txtChallengingTarget= 1, 
					txtInterestPart= 1, 
					imgUserProfile=
						{
							groupId=fg_f06cb045c0d61c4916ca5b4c85961299434d, 
							files=[]
						}
				}
		}*/
		Map<String, Object> frmCreateSeraUserMap = (Map<String, Object>)requestBody.get("frmCreateSeraUser");
		Set<String> keySet = frmCreateSeraUserMap.keySet();
		Iterator<String> itr = keySet.iterator();
	
		String txtUserId = null;
		String hdnUserName = null;
		String txtNickName = null;
		String txtPassword = null;
		String txtConfirmPassword = null;
		String txtBirthYear = null;
		String txtBirthMonth = null;
		String txtBirthDay = null;
		String selSex = null;
		String txtChallengingTarget = null;
		String txtInterestPart = null;

		Map<String, Object> imgUserProfileImage = null;
		String imageGroupId = null;
		Map<String, List<Map<String, String>>> imageGroupMap = new HashMap<String, List<Map<String, String>>>();
		
		while (itr.hasNext()) {
			String fieldId = (String)itr.next();
			Object fieldValue = frmCreateSeraUserMap.get(fieldId);
			if (fieldValue instanceof LinkedHashMap) {
				if (fieldId.equalsIgnoreCase("imgUserProfile")) {
					imgUserProfileImage = (Map<String, Object>)fieldValue;
					if(imgUserProfileImage != null && imgUserProfileImage.size() > 0) {
						imageGroupId = (String)imgUserProfileImage.get("groupId");
	
						List<Map<String, String>> files = (ArrayList<Map<String,String>>)imgUserProfileImage.get("files");
						if(!CommonUtil.isEmpty(files)) {
							imageGroupMap.put(imageGroupId, files);
						}
					}
				}
			} else if(fieldValue instanceof String) {
				if (fieldId.equals("txtUserId")) {
					txtUserId = (String)frmCreateSeraUserMap.get("txtUserId");
				} else if (fieldId.equals("txtNickName")) {
					txtNickName = (String)frmCreateSeraUserMap.get("txtNickName");
				} else if (fieldId.equals("txtPassword")) {
					txtPassword = (String)frmCreateSeraUserMap.get("txtPassword");
				} else if (fieldId.equals("txtConfirmPassword")) {
					txtConfirmPassword = (String)frmCreateSeraUserMap.get("txtConfirmPassword");
				} else if (fieldId.equals("txtBirthYear")) {
					txtBirthYear = (String)frmCreateSeraUserMap.get("txtBirthYear");
				} else if (fieldId.equals("txtBirthMonth")) {
					txtBirthMonth = (String)frmCreateSeraUserMap.get("txtBirthMonth");
				} else if (fieldId.equals("txtBirthDay")) {
					txtBirthDay = (String)frmCreateSeraUserMap.get("txtBirthDay");
				} else if (fieldId.equals("selSex")) {
					selSex = (String)frmCreateSeraUserMap.get("selSex");
				} else if (fieldId.equals("txtChallengingTarget")) {
					txtChallengingTarget = (String)frmCreateSeraUserMap.get("txtChallengingTarget");
				} else if (fieldId.equals("txtInterestPart")) {
					txtInterestPart = (String)frmCreateSeraUserMap.get("txtInterestPart");
				}	else if (fieldId.equals("hdnUserName")) {
					hdnUserName = (String)frmCreateSeraUserMap.get("hdnUserName");
				}
			}
		}
		String txtUserProfilePicture = null;
		if(imageGroupMap.size() > 0) {
			for(Map.Entry<String, List<Map<String, String>>> entry : imageGroupMap.entrySet()) {
				String imgGroupId = entry.getKey();
				List<Map<String, String>> imgGroups = entry.getValue();
				try {
					for(int i=0; i < imgGroups.subList(0, imgGroups.size()).size(); i++) {
						Map<String, String> file = imgGroups.get(i);
						String fileId = file.get("fileId");
						String fileName = file.get("fileName");
						//String fileSize = file.get("fileSize");
						txtUserProfilePicture = SwManagerFactory.getInstance().getDocManager().insertProfilesFile(fileId, fileName, txtUserId);
						//SwManagerFactory.getInstance().getDocManager().insertFiles("Pictures", null, imgGroupId, fileId, fileName, "0");
					}
				} catch (Exception e) {
					throw new DocFileException("image upload fail...");
				}
			}
		}
		
		ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();
		
		SwoCompanyCond cond = new SwoCompanyCond();
		SwoCompany[] company = swoMgr.getCompanys("", cond, IManager.LEVEL_LITE);
		String companyId = "Maninsoft";
		if (company != null) {
			companyId = company[0].getId();
		}
		
		SwoUser swoUser = new SwoUser();
		swoUser.setCompanyId(companyId);
		swoUser.setType("BASIC");
		swoUser.setDomainId("frm_user_SYSTEM");
		swoUser.setRetiree("N");
		
		swoUser.setId(txtUserId);
		swoUser.setName(hdnUserName);
		swoUser.setDeptId(companyId);
		swoUser.setEmail(txtUserId);
		swoUser.setPassword(txtPassword);
		swoUser.setEmpNo(null);
		swoUser.setPosition(null);
		swoUser.setRoleId("DEPT MEMBER");
		swoUser.setAuthId("USER");
		swoUser.setLocale("ko");
		swoUser.setTimeZone("Asia/Seoul");
		swoUser.setPicture(txtUserProfilePicture);
		swoUser.setNickName(txtNickName);
		
		swoMgr.setUser(txtUserId, swoUser, IManager.LEVEL_ALL);

		ISeraManager seraMgr = SwManagerFactory.getInstance().getSeraManager();
		SeraUserDetail seraUser = new SeraUserDetail();
		seraUser.setUserId(txtUserId);
		seraUser.setUserName(hdnUserName);
		seraUser.setEmail(txtUserId);
		if (!CommonUtil.isEmpty(txtBirthYear) && !CommonUtil.isEmpty(txtBirthMonth) && !CommonUtil.isEmpty(txtBirthDay)) {
			String birthDayString = txtBirthYear + (txtBirthMonth.length() == 1 ? "0" + txtBirthMonth : txtBirthMonth) + (txtBirthDay.length() == 1 ? "0" + txtBirthDay : txtBirthDay) + "0000";
			Date birthDay = LocalDate.convertStringToDate(birthDayString);
			seraUser.setBirthday(new LocalDate(birthDay.getTime()));
		}
		seraUser.setSex(CommonUtil.isEmpty(selSex) ? 0 : selSex.equalsIgnoreCase("female") ? SeraUser.SEX_FEMALE : SeraUser.SEX_MALE );
		seraUser.setNickName(txtNickName);
		seraUser.setInterests(txtInterestPart);
		//seraUser.setChallengingTarget(txtChallengingTarget);
		seraUser.setGoal(txtChallengingTarget);
		
		seraMgr.setSeraUser(txtUserId, seraUser);

		return txtUserId;
	}
	@Override
	public SeraUserInfo[] getFriendRequestsForMe(String lastId, int maxList) throws Exception {
		try {
			SeraUserInfo[] seraUserInfos = null;
			List userInfosList = new ArrayList();
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();
			SeraFriendCond seraFriendCond = new SeraFriendCond();
			seraFriendCond.setReceiveId(userId);
			seraFriendCond.setAcceptStatus(SeraFriend.ACCEPT_STATUS_YET);
			seraFriendCond.setPageNo(0);
			if(maxList != -1)
				seraFriendCond.setPageSize(maxList);
			seraFriendCond.setOrders(new Order[]{new Order("requestDate", false), new Order("requestName", true)});

			SeraFriend[] seraFriends = getSeraManager().getFriends(userId, seraFriendCond);

			if(!CommonUtil.isEmpty(seraFriends)) {
				String[] ids = new String[seraFriends.length];
				for(int j=0; j<seraFriends.length; j++) {
					SeraFriend seraFriend = seraFriends[j];
					String id = seraFriend.getRequestId();
					ids[j] = id;
				}
				SwoUserExtend[] userExtends = SwManagerFactory.getInstance().getSwoManager().getUsersExtend(userId, ids);

				SwoUserExtend[] finalUserExtends = new SwoUserExtend[userExtends.length];

				for(int i=0; i<ids.length; i++) {
					String id = ids[i];
					for(int j=0; j<userExtends.length; j++) {
						SwoUserExtend swoUserExtend = userExtends[j];
						String userExtendId = swoUserExtend.getId();
						if(id.equals(userExtendId)) {
							finalUserExtends[i] = swoUserExtend;
							break;
						}
					}
				}

				if(finalUserExtends != null) {
					for(SwoUserExtend swoUserExtend : finalUserExtends) {
						SeraUserInfo member = new SeraUserInfo();
						String id = swoUserExtend.getId();
						member.setId(id);
						SeraUserDetail seraUserDetail = getSeraManager().getSeraUserById(userId, id);
						String goal = null;
						if(seraUserDetail != null)
							goal = seraUserDetail.getGoal();
						member.setGoal(goal);
						member.setName(swoUserExtend.getName());
						member.setNickName(swoUserExtend.getNickName());
						member.setPosition(swoUserExtend.getPosition());
						member.setRole(swoUserExtend.getAuthId().equals("EXTERNALUSER") ? User.USER_LEVEL_EXTERNAL_USER : swoUserExtend.getAuthId().equals("USER") ? User.USER_LEVEL_INTERNAL_USER : swoUserExtend.getAuthId().equals("ADMINISTRATOR") ? User.USER_LEVEL_AMINISTRATOR : User.USER_LEVEL_SYSMANAGER);
						member.setSmallPictureName(swoUserExtend.getSmallPictureName());
						member.setDepartment(new DepartmentInfo(swoUserExtend.getDepartmentId(), swoUserExtend.getDepartmentName(), swoUserExtend.getDepartmentDesc()));
						userInfosList.add(member);
					}
				}
				
			}

			if(userInfosList.size() > 0) {
				seraUserInfos = new SeraUserInfo[userInfosList.size()];
				userInfosList.toArray(seraUserInfos);
			}
			return seraUserInfos;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void replyFriendRequest(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		try {
			String requestId = (String)requestBody.get("userId");
			boolean accepted = (Boolean)requestBody.get("accepted");
			User user = SmartUtil.getCurrentUser();
			String receiveId = user.getId();

			SeraFriendCond seraFriendCond = new SeraFriendCond();
			seraFriendCond.setRequestId(requestId);
			seraFriendCond.setReceiveId(receiveId);
			seraFriendCond.setAcceptStatus(SeraFriend.ACCEPT_STATUS_YET);
			SeraFriend[] seraFriends = getSeraManager().getFriends(receiveId, seraFriendCond);

			if(!CommonUtil.isEmpty(seraFriends)) {
				SeraFriend seraFriend = seraFriends[0];
				if(accepted) {
					seraFriend.setAcceptStatus(SeraFriend.ACCEPT_STATUS_ACCEPT);
					seraFriend.setReplyDate(new LocalDate());
					getSeraManager().setFriend(receiveId, seraFriend);
				} else {
					String objId = seraFriend.getObjId();
					getSeraManager().removeFriend(receiveId, objId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void friendRequest(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		try {
			String receiveId = (String)requestBody.get("userId");
			User user = SmartUtil.getCurrentUser();
			String requestId = user.getId();
			SeraFriendCond seraFriendCond = new SeraFriendCond();
			seraFriendCond.setAcceptStatus(SeraFriend.ACCEPT_STATUS_YET);
			seraFriendCond.setRequestId(requestId);
			seraFriendCond.setReceiveId(receiveId);
			SeraFriend[] requestFriends = getSeraManager().getFriends(requestId, seraFriendCond);
			if(!CommonUtil.isEmpty(requestFriends)) {
				return;
			} else {
				seraFriendCond.setRequestId(receiveId);
				seraFriendCond.setReceiveId(requestId);
				requestFriends = getSeraManager().getFriends(requestId, seraFriendCond);
				if(!CommonUtil.isEmpty(requestFriends)) {
					return;
				}
			}
			seraFriendCond = new SeraFriendCond();
			seraFriendCond.setAcceptStatus(SeraFriend.ACCEPT_STATUS_ACCEPT);
			seraFriendCond.setRequestId(requestId);
			seraFriendCond.setReceiveId(receiveId);
			requestFriends = getSeraManager().getFriends(requestId, seraFriendCond);
			if(!CommonUtil.isEmpty(requestFriends)) {
				return;
			} else {
				seraFriendCond.setRequestId(receiveId);
				seraFriendCond.setReceiveId(requestId);
				requestFriends = getSeraManager().getFriends(requestId, seraFriendCond);
				if(!CommonUtil.isEmpty(requestFriends)) {
					return;
				}
			}
			UserInfo requestUser = ModelConverter.getUserInfoByUserId(requestId);
			UserInfo receiveUser = ModelConverter.getUserInfoByUserId(receiveId);
			if(requestUser != null && receiveUser != null) {
				SeraFriend seraFriend = new SeraFriend();
				seraFriend.setRequestId(requestUser.getId());
				seraFriend.setRequestName(requestUser.getName());
				seraFriend.setReceiveId(receiveUser.getId());
				seraFriend.setReceiveName(receiveUser.getName());
				seraFriend.setAcceptStatus(SeraFriend.ACCEPT_STATUS_YET);
				seraFriend.setRequestDate(new LocalDate());	
				getSeraManager().setFriend(requestId, seraFriend);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void destroyFriendship(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		try {
			String destroyId = (String)requestBody.get("userId");
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();
			SeraFriend[] seraFriends = null;
			SeraFriendCond seraFriendCond = new SeraFriendCond();
			seraFriendCond.setRequestId(userId);
			seraFriendCond.setReceiveId(destroyId);
			seraFriendCond.setAcceptStatus(SeraFriend.ACCEPT_STATUS_ACCEPT);
			seraFriends = getSeraManager().getFriends(userId, seraFriendCond);
			if(CommonUtil.isEmpty(seraFriends)) {
				seraFriendCond.setRequestId(destroyId);
				seraFriendCond.setReceiveId(userId);
				seraFriends = getSeraManager().getFriends(userId, seraFriendCond);
			}

			if(!CommonUtil.isEmpty(seraFriends)) {
				SeraFriend seraFriend = seraFriends[0];
				String objId = seraFriend.getObjId();
				getSeraManager().removeFriend(userId, objId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CourseInfo[] getAllCourses(String fromCourseId, int maxList) throws Exception {
		
		CourseDetailCond courseDetailCond = new CourseDetailCond();
		
		if (fromCourseId != null) {
			CourseDetail lastCourseDetailInfo = SwManagerFactory.getInstance().getSeraManager().getCourseDetailById(fromCourseId);
			if (lastCourseDetailInfo != null)
				courseDetailCond.setCreateDateTo(lastCourseDetailInfo.getCreateDate());
		}
		
		Long totalSize = SwManagerFactory.getInstance().getSeraManager().getCourseDetailSize("", courseDetailCond);

		courseDetailCond.setOrders(new Order[]{new Order("createDate", false)});
		courseDetailCond.setPageNo(0);
		courseDetailCond.setPageSize(maxList);
		CourseDetail[] courseDetails = SwManagerFactory.getInstance().getSeraManager().getCourseDetails("", courseDetailCond);
		if (courseDetails == null || courseDetails.length == 0) 
			return null;
	    
		String[] courseIds = new String[courseDetails.length];
		for (int i = 0; i < courseDetails.length; i++) {
			courseIds[i] = courseDetails[i].getCourseId();
		}

		SwoGroupCond groupCond = new SwoGroupCond();
		groupCond.setGroupIdIns(courseIds);
		groupCond.setOrders(new Order[]{new Order("creationDate", false)});
		ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();
		SwoGroup[] groups = swoMgr.getGroups("", groupCond, IManager.LEVEL_ALL);
	    
		SwoGroup[] tempGroups = new SwoGroup[courseDetails.length];
		for (int i = 0; i < courseDetails.length; i++) {
			String courseId = courseDetails[i].getCourseId();
			for (int j = 0; j < groups.length; j++) {
				if (groups[j].getId().equalsIgnoreCase(courseId)) {
					tempGroups[i] = groups[j];
					break;
				}
			}
		}
		groups = tempGroups;
		
		CourseInfo[] courses = convertSwoGroupArrayToCourseInfoArray(groups, courseDetails);
		CourseInfo[] result = null;

		if (totalSize > maxList) {
			result = new CourseInfo[courses.length + 1];
			for (int i = 0; i < courses.length; i++) {
				result[i] = courses[i];
			}
			result[courses.length] = new CourseInfo();
		} else {
			result = courses;
		}
		
	    //CourseList courses = getCoursesById("ysjung@maninsoft.co.kr", 6);
		return result;
	}
	public CourseInfo[] getClosedCourses(String fromCourseId, int maxList) throws Exception {
		
		CourseDetailCond courseDetailCond = new CourseDetailCond();
		
		if (fromCourseId != null) {
			CourseDetail lastCourseDetailInfo = SwManagerFactory.getInstance().getSeraManager().getCourseDetailById(fromCourseId);
			if (lastCourseDetailInfo != null)
				courseDetailCond.setCreateDateTo(lastCourseDetailInfo.getCreateDate());
		}

		Long totalSize = SwManagerFactory.getInstance().getSeraManager().getCourseDetailSize("", courseDetailCond);
		
		courseDetailCond.setOrders(new Order[]{new Order("createDate", false)});
		courseDetailCond.setPageNo(0);
		courseDetailCond.setPageSize(maxList);
		courseDetailCond.setEndTo(new LocalDate());

		
		CourseDetail[] courseDetails = SwManagerFactory.getInstance().getSeraManager().getCourseDetails("", courseDetailCond);
		if (courseDetails == null || courseDetails.length == 0) 
			return null;
	    
		String[] courseIds = new String[courseDetails.length];
		for (int i = 0; i < courseDetails.length; i++) {
			courseIds[i] = courseDetails[i].getCourseId();
		}

		SwoGroupCond groupCond = new SwoGroupCond();
		groupCond.setGroupIdIns(courseIds);
		ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();
		SwoGroup[] groups = swoMgr.getGroups("", groupCond, IManager.LEVEL_ALL);

		SwoGroup[] tempGroups = new SwoGroup[courseDetails.length];
		for (int i = 0; i < courseDetails.length; i++) {
			String courseId = courseDetails[i].getCourseId();
			for (int j = 0; j < groups.length; j++) {
				if (groups[j].getId().equalsIgnoreCase(courseId)) {
					tempGroups[i] = groups[j];
					break;
				}
			}
		}
		groups = tempGroups;
		
		CourseInfo[] courses = convertSwoGroupArrayToCourseInfoArray(groups, courseDetails);
		CourseInfo[] result = null;
		
		if (totalSize > maxList) {
			result = new CourseInfo[courses.length + 1];
			for (int i = 0; i < courses.length; i++) {
				result[i] = courses[i];
			}
			result[courses.length] = new CourseInfo();
		} else {
			result = courses;
		}
		
	    //CourseList courses = getCoursesById("ysjung@maninsoft.co.kr", 6);
		return result;
	}
	
	@Override
	public CourseInfo[] getCoursesByType(int courseType, String lastId, int maxList) throws Exception {
		switch(courseType){
		case Course.TYPE_FAVORITE_COURSES:
			return this.getFavoriteCourses(lastId, maxList);
		case Course.TYPE_RECOMMENDED_COURSES:
			return this.getRecommendedCourses(lastId, maxList);
		case Course.TYPE_ALL_COURSES:
			return this.getAllCourses(lastId, maxList);
		case Course.TYPE_CLOSED_COURSES:
			return this.getClosedCourses(lastId, maxList);
		}
		return null;
	}
	@Override
	public CourseInfo[] getCoursesByCategory(String categoryName, String lastId, int maxList) throws Exception {
		if (CommonUtil.isEmpty(categoryName))
			return null;

		ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();
		ISeraManager seraMgr = SwManagerFactory.getInstance().getSeraManager();
		
		CourseDetailCond cond = new CourseDetailCond();
		cond.setCategories(new String[]{categoryName});
		
		if (!CommonUtil.isEmpty(lastId)) {
			CourseDetail lastCoureDetail = seraMgr.getCourseDetailById(lastId);
			if (lastCoureDetail != null)
				cond.setCreateDateTo(lastCoureDetail.getCreateDate());
		}

		Long totalSize = SwManagerFactory.getInstance().getSeraManager().getCourseDetailSize("", cond);
		
		cond.setOrders(new Order[]{new Order("createDate", false)});
		cond.setPageNo(0);
		cond.setPageSize(maxList);
		
		CourseDetail[] courseDetails = seraMgr.getCourseDetails("", cond);
		
		if (courseDetails == null)
			return null;
		
		String[] groupIdIns = new String[courseDetails.length];
		for (int i = 0; i < courseDetails.length; i++) {
			groupIdIns[i] = courseDetails[i].getCourseId();
		}
		
		SwoGroupCond groupCond = new SwoGroupCond();
		groupCond.setGroupIdIns(groupIdIns);
		groupCond.setOrders(new Order[]{new Order("creationDate", false)});
		SwoGroup[] groups = swoMgr.getGroups("", groupCond, IManager.LEVEL_ALL);
		CourseInfo[] courseInfo = this.convertSwoGroupArrayToCourseInfoArray(groups, courseDetails);
		
		CourseInfo[] result = null;
		if (totalSize > maxList) {
			result = new CourseInfo[courseInfo.length + 1];
			for (int i = 0; i < courseInfo.length; i++) {
				result[i] = courseInfo[i];
			}
			result[courseInfo.length] = new CourseInfo();
		} else {
			result = courseInfo;
		}
		return result;
	}
	private SeraUserInfo[] getJoinRequesterByCourseId(SwoGroup group, String lastId, int maxList) throws Exception {
		User user = SmartUtil.getCurrentUser();
		String userId = user.getId();

		if (group == null)
			return null;
		
		List joinRequesterIdList = new ArrayList();
		Map joinRequesterIdMap = new HashMap();
		
		SwoGroupMember[] groupMembers = group.getSwoGroupMembers();
		for (int i = 0; i < groupMembers.length; i++) {
			SwoGroupMember groupMember = groupMembers[i];
			if(groupMember != null) {
				String joinType = groupMember.getJoinType();
				String joinStatus = groupMember.getJoinStatus();
				if (joinStatus.equalsIgnoreCase(SwoGroupMember.JOINSTATUS_READY)) {
					if (joinType.equalsIgnoreCase(SwoGroupMember.JOINTYPE_REQUEST)) {
						joinRequesterIdList.add(userId);
						if (!CommonUtil.isEmpty(lastId)) {
							SwoGroupMember lastMember = group.getGroupMember(lastId);
							long lastMemberCreationDateLong = lastMember.getCreationDate().getTime();
							if (lastMemberCreationDateLong > groupMember.getCreationDate().getTime()) {
								joinRequesterIdMap.put(groupMember.getCreationDate().getTime(), groupMember);
							}
						} else {
							joinRequesterIdMap.put(groupMember.getCreationDate().getTime(), groupMember);
						}
					}
				}
			}
		}
		String[] joinRequesterIds = null;
		String[] resultJoinRequesterIds = null;
		boolean isMoreThenMaxList = false;
		Map<Long, SwoGroupMember> sortMap = new TreeMap<Long, SwoGroupMember>();
		if (joinRequesterIdList.size() != 0) {
			joinRequesterIds = new String[joinRequesterIdList.size()];
			joinRequesterIdList.toArray(joinRequesterIds);

			//Map<Long, SwoGroupMember> sortMap = new TreeMap<Long, SwoGroupMember>(Collections.reverseOrder());
			sortMap.putAll(joinRequesterIdMap);
			isMoreThenMaxList = joinRequesterIdMap.size() > maxList ? true : false;
				
			resultJoinRequesterIds = new String[sortMap.size()];
			Iterator<Long> itr = sortMap.keySet().iterator();
			int i = 0;
			while (itr.hasNext()) {
				if (i == maxList) 
					break;
				resultJoinRequesterIds[i] = ((SwoGroupMember)sortMap.get(itr.next())).getUserId();
				i++;
			}
		}
		if (CommonUtil.isEmpty(resultJoinRequesterIds))
			return null;
		
		if (isMoreThenMaxList) {
			SeraUserInfo[] userInfos = getSeraUserInfos(resultJoinRequesterIds);	
			
			SeraUserInfo[] tempUserInfos = new SeraUserInfo[userInfos.length];
			
			Map resultMap = new HashMap();
			for (SeraUserInfo userInfo : userInfos) {
				resultMap.put(userInfo.getId(), userInfos);
			}
			Iterator<Long> itr = sortMap.keySet().iterator();
			int index = 0;
			while (itr.hasNext()) {
				SwoGroupMember member = sortMap.get(itr.next());
				String id = member.getUserId();
				tempUserInfos[index] = (SeraUserInfo)resultMap.get(id);
			}
			SeraUserInfo[] resultInfo = new SeraUserInfo[tempUserInfos.length + 1];
			for (int i = 0; i < tempUserInfos.length; i++) {
				resultInfo[i] = tempUserInfos[i];
			}
			resultInfo[userInfos.length] = new SeraUserInfo();
			return resultInfo;	
		} else {
			SeraUserInfo[] userInfos = getSeraUserInfos(resultJoinRequesterIds);
			SeraUserInfo[] tempUserInfos = new SeraUserInfo[userInfos.length];
			
			Map resultMap = new HashMap();
			for (SeraUserInfo userInfo : userInfos) {
				resultMap.put(userInfo.getId(), userInfo);
			}
			Iterator<Long> itr = sortMap.keySet().iterator();
			int index = 0;
			while (itr.hasNext()) {
				SwoGroupMember member = sortMap.get(itr.next());
				String id = member.getUserId();
				tempUserInfos[index] = (SeraUserInfo)resultMap.get(id);
				index += 1;
			}
			return tempUserInfos;
		}
	}

	private SeraUserInfo[] getMenteesByCourseId(SwoGroup group, String lastId, int maxList) throws Exception {

		if (group == null)
			return null;

		User user = SmartUtil.getCurrentUser();
		String userId = user.getId();

		SwoGroupMember[] groupMembers = group.getSwoGroupMembers();

		
		List<String> idList = new ArrayList<String>();
		String[] idIns = null;
		if(!CommonUtil.isEmpty(groupMembers)) {
			for(SwoGroupMember groupMember : groupMembers) {
				if(groupMember != null) {
					String id = groupMember.getUserId();
					String joinStatus = groupMember.getJoinStatus();
					if(joinStatus.equalsIgnoreCase(SwoGroupMember.JOINSTATUS_COMPLETE))
						idList.add(id);
				}
			}
		}
		if(idList.size() > 0) {
			idIns = new String[idList.size()];
			idList.toArray(idIns);
		}

		if(CommonUtil.isEmpty(idIns))
			return null;

		String lastName = null;
		Date lastModifiedTime = null;
		if(!CommonUtil.isEmpty(lastId)) {
			SwoUser swoUser = getSwoManager().getUser(userId, lastId, IManager.LEVEL_LITE);
			lastName = swoUser.getName();
			lastModifiedTime = swoUser.getModificationDate();
		}
		SwoUserExtend[] swoUserExtends = getSwoManager().getUserExtends(idIns, lastName, lastModifiedTime, "name", true);

		if(CommonUtil.isEmpty(swoUserExtends))
			return null;

		SeraUserInfo[] seraUserInfos = null;
		List<SeraUserInfo> seraUserInfoList = new ArrayList<SeraUserInfo>();

		for(int i=0; i<swoUserExtends.length; i++) {
			if(i == maxList)
				break;
			SwoUserExtend swoUserExtend = swoUserExtends[i];
			SeraUserInfo member = new SeraUserInfo();
			member.setId(swoUserExtend.getId());
			member.setName(swoUserExtend.getName());
			member.setNickName(swoUserExtend.getNickName());
			member.setPosition(swoUserExtend.getPosition());
			member.setRole(swoUserExtend.getAuthId().equals("EXTERNALUSER") ? User.USER_LEVEL_EXTERNAL_USER : swoUserExtend.getAuthId().equals("USER") ? User.USER_LEVEL_INTERNAL_USER : swoUserExtend.getAuthId().equals("ADMINISTRATOR") ? User.USER_LEVEL_AMINISTRATOR : User.USER_LEVEL_SYSMANAGER);
			member.setSmallPictureName(swoUserExtend.getSmallPictureName());
			member.setDepartment(new DepartmentInfo(swoUserExtend.getDepartmentId(), swoUserExtend.getDepartmentName(), swoUserExtend.getDepartmentDesc()));
			seraUserInfoList.add(member);
		}

		if(swoUserExtends.length > maxList) {
			seraUserInfoList.add(new SeraUserInfo());
		}

		if(seraUserInfoList.size() > 0) {
			seraUserInfos = new SeraUserInfo[seraUserInfoList.size()];
			seraUserInfoList.toArray(seraUserInfos);
		}

		return seraUserInfos;

		/*User user = SmartUtil.getCurrentUser();
		String userId = user.getId();

		if (group == null)
			return null;
		
		List menteesIdList = new ArrayList();
		Map menteesIdMap = new HashMap();

		SwoGroupMember[] groupMembers = group.getSwoGroupMembers();
		if(!CommonUtil.isEmpty(groupMembers)) {
			for (int i = 0; i < groupMembers.length; i++) {
				SwoGroupMember groupMember = groupMembers[i];
				if(groupMember != null) {
					String joinStatus = groupMember.getJoinStatus();
					if (joinStatus.equalsIgnoreCase(SwoGroupMember.JOINSTATUS_COMPLETE)) {
						menteesIdList.add(groupMember.getUserId());
						if (!CommonUtil.isEmpty(lastId)) {
							SwoGroupMember lastMember = group.getGroupMember(lastId);
							long lastMemberJoinDateLong = lastMember.getJoinDate().getTime();
							if (lastMemberJoinDateLong > groupMember.getJoinDate().getTime()) {
								menteesIdMap.put(groupMember.getJoinDate().getTime(), groupMember);
							} 
						} else {
							menteesIdMap.put(groupMember.getJoinDate().getTime(), groupMember);
						}
					}
				}
			}
		}
		String[] menteesIds = null;
		String[] resultMenteesIds = null;
		boolean isMoreThenMaxList = false;
		Map<Long, SwoGroupMember> sortMap = new TreeMap<Long, SwoGroupMember>();
		if (menteesIdList.size() != 0) {
			menteesIds = new String[menteesIdList.size()];
			menteesIdList.toArray(menteesIds);
			
			//Map<Long, SwoGroupMember> sortMap = new TreeMap<Long, SwoGroupMember>(Collections.reverseOrder());
			sortMap.putAll(menteesIdMap);
			isMoreThenMaxList = menteesIdMap.size() > maxList ? true : false;
			resultMenteesIds = new String[sortMap.size()];
			Iterator<Long> itr = sortMap.keySet().iterator();
			int i = 0;
			while (itr.hasNext()) {
				if (i == maxList) 
					break;
				resultMenteesIds[i] = ((SwoGroupMember)sortMap.get(itr.next())).getUserId();
				i++;
			}
		}

		if (CommonUtil.isEmpty(resultMenteesIds))
			return null;
		
		if (isMoreThenMaxList) {
			SeraUserInfo[] userInfos = getSeraUserInfos(resultMenteesIds);
			SeraUserInfo[] tempUserInfos = new SeraUserInfo[userInfos.length];
			
			Map resultMap = new HashMap();
			for (SeraUserInfo userInfo : userInfos) {
				resultMap.put(userInfo.getId(), userInfo);
			}
			Iterator<Long> itr = sortMap.keySet().iterator();
			int index = 0;
			while (itr.hasNext()) {
				if (index == maxList)
					break;
				SwoGroupMember member = sortMap.get(itr.next());
				String id = member.getUserId();
				tempUserInfos[index] = (SeraUserInfo)resultMap.get(id);
				index += 1;
			}
			SeraUserInfo[] resultInfo = new SeraUserInfo[tempUserInfos.length + 1];
			for (int i = 0; i < tempUserInfos.length; i++) {
				resultInfo[i] = tempUserInfos[i];
			}
			resultInfo[userInfos.length] = new SeraUserInfo();
			return resultInfo;	
			
		} else {
			SeraUserInfo[] userInfos = getSeraUserInfos(resultMenteesIds);
			SeraUserInfo[] tempUserInfos = new SeraUserInfo[userInfos.length];
			
			Map resultMap = new HashMap();
			for (SeraUserInfo userInfo : userInfos) {
				resultMap.put(userInfo.getId(), userInfo);
			}
			Iterator<Long> itr = sortMap.keySet().iterator();
			int index = 0;
			while (itr.hasNext()) {
				if (index == maxList)
					break;
				SwoGroupMember member = sortMap.get(itr.next());
				String id = member.getUserId();
				tempUserInfos[index] = (SeraUserInfo)resultMap.get(id);
				index += 1;
			}
			return tempUserInfos;
		}*/
	}

	private SeraUserInfo[] getNotRelatedUserByCourseId(SwoGroup group, String lastId, int maxList) throws Exception {

		User user = SmartUtil.getCurrentUser();
		String userId = user.getId();
		
		ISeraManager seraMgr = SwManagerFactory.getInstance().getSeraManager();
		ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();

		if (group == null)
			return null;

		String lastUserName = null;
		Date lastModifiedTime = null;
		if(!CommonUtil.isEmpty(lastId)) {
			SwoUser lastUser = swoMgr.getUser(userId, lastId, IManager.LEVEL_LITE);
			if (lastUser != null) {
				lastUserName = lastUser.getName();
				lastModifiedTime = lastUser.getModificationDate();
			}
		}

		List courseRelatedUserIdList = new ArrayList();

		courseRelatedUserIdList.add(group.getGroupLeader());
		
		SwoGroupMember[] groupMembers = group.getSwoGroupMembers();
		for (int i = 0; i < groupMembers.length; i++) {
			SwoGroupMember groupMember = groupMembers[i];
			if(groupMember != null) {
				if (group.getGroupLeader().equalsIgnoreCase(groupMember.getUserId()))
					continue;
				courseRelatedUserIdList.add(groupMember.getUserId());
			}
		}

		String[] courseRelatedUserIds = new String[courseRelatedUserIdList.size()];
		courseRelatedUserIdList.toArray(courseRelatedUserIds);
		
		String[] courseNotRelatedUserIds = null;
		
		List courseNotRelatedUserIdList = new ArrayList();
		SeraUserDetailCond cond = new SeraUserDetailCond();
		cond.setUserIdNotIns(courseRelatedUserIds);
		SeraUserDetail[] courseNotRelatedUser = seraMgr.getSeraUserDetails(userId, cond);
		if (courseNotRelatedUser == null || courseNotRelatedUser.length == 0) {
			return null;
		} else {
			for (int i = 0; i < courseNotRelatedUser.length; i++) {
				courseNotRelatedUserIdList.add(courseNotRelatedUser[i].getUserId());
			}
			if (courseNotRelatedUserIdList.size() != 0) {
				courseNotRelatedUserIds = new String[courseNotRelatedUserIdList.size()];
				courseNotRelatedUserIdList.toArray(courseNotRelatedUserIds);
			}
		}
		
		SwoUserExtend[] userExtends = SwManagerFactory.getInstance().getSwoManager().getUsersExtend("", courseNotRelatedUserIds, lastUserName, lastModifiedTime, null);
		
		if (userExtends == null)
			return null;
		List userInfoList = new ArrayList();
		SeraUserInfo[] userInfoArray = null;
		if(userExtends != null) {
			for(int i = 0; i < userExtends.length; i++) {
				SwoUserExtend swoUserExtend = userExtends[i];
				UserInfo member = new SeraUserInfo();
				member.setId(swoUserExtend.getId());
				member.setName(swoUserExtend.getName());
				member.setNickName(swoUserExtend.getNickName());
				member.setPosition(swoUserExtend.getPosition());
				member.setRole(swoUserExtend.getAuthId().equals("EXTERNALUSER") ? User.USER_LEVEL_EXTERNAL_USER : swoUserExtend.getAuthId().equals("USER") ? User.USER_LEVEL_INTERNAL_USER : swoUserExtend.getAuthId().equals("ADMINISTRATOR") ? User.USER_LEVEL_AMINISTRATOR : User.USER_LEVEL_SYSMANAGER);
				member.setSmallPictureName(swoUserExtend.getSmallPictureName());
				member.setDepartment(new DepartmentInfo(swoUserExtend.getDepartmentId(), swoUserExtend.getDepartmentName(), swoUserExtend.getDepartmentDesc()));
				userInfoList.add(member);
				
				if (maxList > 0) {
					if (userInfoList.size() == maxList)
						break; 
				}
			}
			if(userExtends.length > maxList)
				userInfoList.add(new SeraUserInfo());
			userInfoArray = new SeraUserInfo[userInfoList.size()];
			userInfoList.toArray(userInfoArray);
		}
		return userInfoArray;
		
	}
	
	@Override
	public MenteeInformList getCourseMenteeInformations(String courseId, int maxList) throws Exception {
		if (CommonUtil.isEmpty(courseId))
			return null;
		
		ISeraManager seraMgr = SwManagerFactory.getInstance().getSeraManager();
		ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();

		User user = SmartUtil.getCurrentUser();
		String userId = user.getId();
		
		SwoGroup group = swoMgr.getGroup(userId, courseId, IManager.LEVEL_ALL);
		SwoGroupMember[] groupMembers = group.getSwoGroupMembers();
		
		int totalMentee = 0;
		int totalJoinRequester = 0;
		
		for (int i = 0; i < groupMembers.length; i++) {
			SwoGroupMember groupMember = groupMembers[i];
			if(groupMember != null) {
				String joinStatus = groupMember.getJoinStatus();
				String joinType = groupMember.getJoinType();
				if (joinStatus.equalsIgnoreCase(SwoGroupMember.JOINSTATUS_COMPLETE)) {
					totalMentee += 1;
				} else if (joinStatus.equalsIgnoreCase(SwoGroupMember.JOINSTATUS_READY) && joinType.equalsIgnoreCase(SwoGroupMember.JOINTYPE_REQUEST)) {
					totalJoinRequester += 1;
				}
			}
		}
		SeraUserDetailCond cond = new SeraUserDetailCond();
		cond.setUserIdNotIns(new String[]{userId});
		long totalSeraUserSize = seraMgr.getSeraUserSize(userId, cond);
		
		MenteeInformList menteeInformList = new MenteeInformList();
		menteeInformList.setJoinRequesters(getJoinRequesterByCourseId(group, null, maxList));
		menteeInformList.setTotalJoinRequesters(totalJoinRequester);
		menteeInformList.setMentees(getMenteesByCourseId(group, null, maxList));
		menteeInformList.setTotalMentees(totalMentee);
		menteeInformList.setNonMentees(getNotRelatedUserByCourseId(group, null, maxList));
		menteeInformList.setTotalNonMentees((int)totalSeraUserSize - (totalJoinRequester + totalMentee));
		
		return menteeInformList;
//		return SeraTest.getCourseMenteeInformations(courseId, maxList);
	}
	@Override
	public SeraUserInfo[] getCourseMenteeInformsByType(int type, String courseId, String lastId, int maxList) throws Exception {
		if (CommonUtil.isEmpty(courseId))
			return null;
		
		ISeraManager seraMgr = SwManagerFactory.getInstance().getSeraManager();
		ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();

		User user = SmartUtil.getCurrentUser();
		String userId = user.getId();
		
		SwoGroup group = swoMgr.getGroup(userId, courseId, IManager.LEVEL_ALL);
		
		switch (type) {
		case MenteeInformList.TYPE_JOIN_REQUESTERS:
			return getJoinRequesterByCourseId(group, lastId, maxList);
		case MenteeInformList.TYPE_MENTEES:
			return getMenteesByCourseId(group, lastId, maxList);
		case MenteeInformList.TYPE_NON_MENTEES:
			return getNotRelatedUserByCourseId(group, lastId, maxList);
		default:
			return null;
		}
		
		//return SeraTest.getCourseMenteeInformsByType(type, courseId, lastId, maxList);
	}

	@Override
	public String modifyMission(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		/*{
			courseId=group_c6895507218543cca385e639acbd2763, 
			frmModifyMission=
				{
					txtMissionName=123, 
					txtMissionOpenDate=2012.04.23, 
					txtMissionCloseDate=2012.04.26, 
					selPrevMission=, 
					txtFileField=
					{
						groupId=null, 
						files=[]
					}, 
					txtaMissionContent=123
				}
		}*/
		User user = SmartUtil.getCurrentUser();
		String userId = user.getId();
		String courseId = (String)requestBody.get("courseId");
		String missionId = (String)requestBody.get("missionId");
		Map<String, Object> frmModifyMission = (Map<String, Object>)requestBody.get("frmModifyMission");

		Set<String> keySet = frmModifyMission.keySet();
		Iterator<String> itr = keySet.iterator();
		
		String txtMissionName = null;
		String txtMissionOpenDate = null;
		String txtMissionCloseDate = null;
		String selPrevMission = null;
		String txtaMissionContent = null;
		Map<String, Object> txtFileField = null;
		String fileGroupId = null;
		Map<String, List<Map<String, String>>> fileGroupMap = new HashMap<String, List<Map<String, String>>>();
		String missionFileId = null;
		String missionFileName = null;
		String selGroupProfileType = null;//공개 비공개
		
		while (itr.hasNext()) {
			String fieldId = (String)itr.next();
			Object fieldValue = frmModifyMission.get(fieldId);
			if(fieldValue instanceof String) {					
				if(fieldId.equals("txtMissionName")) {
					txtMissionName = (String)frmModifyMission.get("txtMissionName");
				} else if(fieldId.equals("txtMissionOpenDate")) {
					txtMissionOpenDate = (String)frmModifyMission.get("txtMissionOpenDate");
				} else if(fieldId.equals("txtMissionCloseDate")) {
					txtMissionCloseDate = (String)frmModifyMission.get("txtMissionCloseDate");
				} else if(fieldId.equals("selPrevMission")) {
					selPrevMission = (String)frmModifyMission.get("selPrevMission");
				} else if(fieldId.equals("txtaMissionContent")) {
					txtaMissionContent = (String)frmModifyMission.get("txtaMissionContent");
				}
			} else if (fieldValue instanceof LinkedHashMap) {
				if (fieldId.equalsIgnoreCase("txtFileField")) {
					txtFileField = (Map<String, Object>)fieldValue;
					if(txtFileField != null && txtFileField.size() > 0) {
						fileGroupId = (String)txtFileField.get("groupId");
						List<Map<String, String>> files = (ArrayList<Map<String,String>>)txtFileField.get("files");
						if(!CommonUtil.isEmpty(files)) {
							fileGroupMap.put(fileGroupId, files);
						}
					}
				}
			}
		}

		/*if(!CommonUtil.isEmpty(txtFileField)) {
			for(int i=0; i < txtFileField.subList(0, txtFileField.size()).size(); i++) {
				Map<String, String> fileMap = txtFileField.get(i);
				missionFileId = fileMap.get("fileId");
				missionFileName = fileMap.get("fileName");
				selGroupProfileType = SwManagerFactory.getInstance().getDocManager().insertProfilesFile(courseFileId, courseFileName, swoGroup.getId());
				mission.setPicture(selGroupProfileType);
			}
		}*/
		
		SwdDomainCond swdDomainCond = new SwdDomainCond();
		swdDomainCond.setFormId(SeraConstant.MISSION_FORMID);
		SwdDomain swdDomain = SwManagerFactory.getInstance().getSwdManager().getDomain(userId, swdDomainCond, IManager.LEVEL_LITE);
		String domainId = swdDomain.getObjId();
		
		SwdFieldCond swdFieldCond = new SwdFieldCond();
		swdFieldCond.setDomainObjId(domainId);
		SwdField[] fields = SwManagerFactory.getInstance().getSwdManager().getFields(userId, swdFieldCond, IManager.LEVEL_LITE);
		if (CommonUtil.isEmpty(fields))
			return null;//TODO return null? throw new Exception??

		Map<String, SwdField> fieldInfoMap = new HashMap<String, SwdField>();
		for (SwdField field : fields) {
			fieldInfoMap.put(field.getFormFieldId(), field);
		}
		
		SwdRecordCond cond = new SwdRecordCond();
		cond.setRecordId(missionId);
		cond.setFormId(SeraConstant.MISSION_FORMID);
		SwdRecord obj = SwManagerFactory.getInstance().getSwdManager().getRecord(user.getId(), cond, IManager.LEVEL_ALL);

		List fieldDataList = new ArrayList();
		for (SwdField field : fields) {
			String fieldId = field.getFormFieldId();
			SwdDataField fieldData = new SwdDataField();
			fieldData.setId(fieldId);
			fieldData.setName(field.getFormFieldName());
			fieldData.setRefForm(null);
			fieldData.setRefFormField(null);
			fieldData.setRefRecordId(null);
			if (fieldId.equalsIgnoreCase(SeraConstant.MISSION_TITLEFIELDID)) {
				fieldData.setValue(txtMissionName);
			} else if (fieldId.equalsIgnoreCase(SeraConstant.MISSION_OPENDATEFIELDID)) {
				if(txtMissionOpenDate.length() == FieldData.SIZE_DATETIME)
					txtMissionOpenDate = LocalDate.convertLocalDateTimeStringToLocalDate(txtMissionOpenDate).toGMTDateString();
				else if(txtMissionOpenDate.length() == FieldData.SIZE_DATE)
					txtMissionOpenDate = LocalDate.convertLocalDateStringToLocalDate(txtMissionOpenDate).toGMTDateString();
				fieldData.setValue(txtMissionOpenDate);
			} else if (fieldId.equalsIgnoreCase(SeraConstant.MISSION_CLOSEDATEFIELDID)) {
				if(txtMissionCloseDate.length() == FieldData.SIZE_DATETIME)
					txtMissionCloseDate = LocalDate.convertLocalDateTimeStringToLocalDate(txtMissionCloseDate).toGMTDateString();
				else if(txtMissionCloseDate.length() == FieldData.SIZE_DATE)
					txtMissionCloseDate = LocalDate.convertLocalDateStringToLocalDate(txtMissionCloseDate).toGMTDateString();
				fieldData.setValue(txtMissionCloseDate);
			} else if (fieldId.equalsIgnoreCase(SeraConstant.MISSION_PREVMISSIONFIELDID)) {
				fieldData.setValue(selPrevMission);
			} else if (fieldId.equalsIgnoreCase(SeraConstant.MISSION_CONTENTFIELDID)) {
				fieldData.setValue(txtaMissionContent);
			} else if (fieldId.equalsIgnoreCase(SeraConstant.MISSION_INDEXFIELDID)) {
				String lastMissionIndex = obj.getDataFieldValue(SeraConstant.MISSION_INDEXFIELDID);
			    fieldData.setValue(lastMissionIndex);
			} else if (fieldId.equalsIgnoreCase(SeraConstant.MISSION_FILESFIELDID)) {
				fieldData.setValue(fileGroupId);
			}
			fieldDataList.add(fieldData);
		}

		SwdDataField[] fieldDatas = new SwdDataField[fieldDataList.size()];
		fieldDataList.toArray(fieldDatas);
		obj.setDataFields(fieldDatas);
		
		SwManagerFactory.getInstance().getSwdManager().setRecord(userId, obj, IManager.LEVEL_ALL);

		TskTaskCond tskCond = new TskTaskCond();
		tskCond.setExtendedProperties(new Property[] {new Property("recordId", obj.getRecordId())});
		tskCond.setModificationUser(userId);
		tskCond.setOrders(new Order[]{new Order(TskTaskCond.A_CREATIONDATE, false)});
		TskTask[] tskTasks = SwManagerFactory.getInstance().getTskManager().getTasks(userId, tskCond, IManager.LEVEL_LITE);
		String taskInstId = tskTasks[0].getObjId();

		if(fileGroupMap.size() > 0) {
			for(Map.Entry<String, List<Map<String, String>>> entry : fileGroupMap.entrySet()) {
				String fGroupId = entry.getKey();
				List<Map<String, String>> fileGroups = entry.getValue();
				try {
					for(int i=0; i < fileGroups.subList(0, fileGroups.size()).size(); i++) {
						Map<String, String> file = fileGroups.get(i);
						String fileId = file.get("fileId");
						String fileName = file.get("fileName");
						String fileSize = file.get("fileSize");
						SwManagerFactory.getInstance().getDocManager().insertFiles("Files", taskInstId, fGroupId, fileId, fileName, fileSize);
					}
				} catch (Exception e) {
					throw new DocFileException("file upload fail...");
				}
			}
		}

		return missionId;

	}

	@Override
	public String removeMission(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		/*{
			courseId=gr_402880eb36dbc3fe0136dbc3fecc0000
			missionId=dr_402880eb36dbc3fe0136dbc3fecc0000
		}*/
		
		User user = SmartUtil.getCurrentUser();
		String userId = user.getId();
		String courseId = (String)requestBody.get("courseId");
		String missionId = (String)requestBody.get("missionId");
		
		SwdDomainCond swdDomainCond = new SwdDomainCond();
		swdDomainCond.setFormId(SeraConstant.MISSION_FORMID);
		SwdDomain swdDomain = SwManagerFactory.getInstance().getSwdManager().getDomain(userId, swdDomainCond, IManager.LEVEL_LITE);
		String domainId = swdDomain.getObjId();
		
		//SwManagerFactory.getInstance().getSwdManager().removeRecord(user.getId(), domainId, missionId);
		
		return missionId;
	}
	
	private SeraUserInfo[] getSeraUserInfoByIdArrayOrderByLastNameAndMaxList(String userId, String[] ids, String lastName, Date lastModifiedTime, int maxList) throws Exception {
		
		SwoUserExtend[] userExtends = SwManagerFactory.getInstance().getSwoManager().getUsersExtend("", ids, lastName, lastModifiedTime, null);

		if (userExtends == null)
			return null;
		List userInfoList = new ArrayList();
		SeraUserInfo[] requestUsers = null;

		User currentUser = SmartUtil.getCurrentUser();
		String currentUserId = currentUser.getId();

		SeraFriendCond seraFriendCond = new SeraFriendCond();
		seraFriendCond.setAcceptStatus(SeraFriend.ACCEPT_STATUS_ACCEPT);

		SeraFriend[] mySeraFriends = getSeraManager().getMyFriends(currentUserId, seraFriendCond);

		if(userExtends != null) {
			for(int i = 0; i < userExtends.length; i++) {
				SwoUserExtend swoUserExtend = userExtends[i];
				SeraUserInfo member = new SeraUserInfo();
				String id = swoUserExtend.getId();
				member.setId(id);
				member.setName(swoUserExtend.getName());
				member.setNickName(swoUserExtend.getNickName());
				member.setPosition(swoUserExtend.getPosition());
				member.setRole(swoUserExtend.getAuthId().equals("EXTERNALUSER") ? User.USER_LEVEL_EXTERNAL_USER : swoUserExtend.getAuthId().equals("USER") ? User.USER_LEVEL_INTERNAL_USER : swoUserExtend.getAuthId().equals("ADMINISTRATOR") ? User.USER_LEVEL_AMINISTRATOR : User.USER_LEVEL_SYSMANAGER);
				member.setSmallPictureName(swoUserExtend.getSmallPictureName());
				member.setDepartment(new DepartmentInfo(swoUserExtend.getDepartmentId(), swoUserExtend.getDepartmentName(), swoUserExtend.getDepartmentDesc()));
				boolean isFriend = false;
				if(!CommonUtil.isEmpty(mySeraFriends)) {
					for(SeraFriend seraFriend : mySeraFriends) {
						if(id.equals(seraFriend.getFriendId())) {
							isFriend = true;
							break;
						}
					}
				}
				member.setFriend(isFriend);
				SeraUserDetail seraUserDetail = getSeraManager().getSeraUserById(currentUserId, id);
				String goal = null;
				if(seraUserDetail != null)
					goal = seraUserDetail.getGoal();
				member.setGoal(goal);
				userInfoList.add(member);
				
				if (maxList > 0) {
					if (userInfoList.size() == maxList)
						break; 
				}
			}
			if(userExtends.length > maxList)
				userInfoList.add(new SeraUserInfo());
			requestUsers = new SeraUserInfo[userInfoList.size()];
			userInfoList.toArray(requestUsers);
		}
		return requestUsers;
	}
	public SeraUserInfo[] getRequestFriend(String userId, String lastId, int maxList) throws Exception {
		
		//나에게 친구 요청한 사람들
		ISeraManager seraMgr = SwManagerFactory.getInstance().getSeraManager();
		ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();
		
		SeraFriendCond friendCond = new SeraFriendCond();
		friendCond.setAcceptStatus(SeraFriend.ACCEPT_STATUS_YET);
		friendCond.setReceiveId(userId);
		
		SwoUser lastSwoUser = swoMgr.getUser(userId, lastId, IManager.LEVEL_LITE);
		if (lastSwoUser != null)
			friendCond.setLastRequestName(lastSwoUser.getName());
		friendCond.setOrders(new Order[]{new Order("requestName", true)});
		
		SeraFriend[] requestFriends = seraMgr.getFriends(userId, friendCond);
		if (requestFriends == null || requestFriends.length == 0)
			return null;
		
		String[] requestFriendsIdArray = new String[requestFriends.length];
		for (int i = 0; i < requestFriends.length; i++) {
			requestFriendsIdArray[i] = requestFriends[i].getRequestId();
		}
		
		return getSeraUserInfoByIdArrayOrderByLastNameAndMaxList(userId, requestFriendsIdArray, null, null, maxList);
	}
	private SeraUserInfo[] getNotMyFrined(String userId, String lastId, int maxList, String key) throws Exception {

		ISeraManager seraMgr = SwManagerFactory.getInstance().getSeraManager();
		ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();
		
		SeraFriendCond friendCond = new SeraFriendCond();
		friendCond.setRequestIdOrReceiveId(userId);
		SeraFriend[] myFriends = seraMgr.getFriends(userId, friendCond);

		List myFriendsUserIdList = new ArrayList();
		if (myFriends != null) {
			for (int i = 0; i < myFriends.length; i++) {
				SeraFriend myFriend = myFriends[i];
				String myFriendId = null;
				if (userId.equalsIgnoreCase(myFriend.getReceiveId())) {
					myFriendId = myFriend.getRequestId();
				} else {
					myFriendId = myFriend.getReceiveId();
				}
				myFriendsUserIdList.add(myFriendId);
			}
		}
		myFriendsUserIdList.add(userId);
		
		String[] myFriendsUserIdArray = null;
		if (myFriendsUserIdList != null && myFriendsUserIdList.size() != 0) {
			myFriendsUserIdArray = new String[myFriendsUserIdList.size()];
			myFriendsUserIdList.toArray(myFriendsUserIdArray);
		}
		
		List notMyFriendsList = new ArrayList();
		String[] notMyFriendsArray = null;
		SeraUserDetailCond cond = new SeraUserDetailCond();
		cond.setUserIdNotIns(myFriendsUserIdArray);
		if(key != null)
			cond.setKey(key);

		SeraUserDetail[] notMyFrinedsUser = seraMgr.getSeraUserDetails(userId, cond);
		if (notMyFrinedsUser == null || notMyFrinedsUser.length == 0) {
			return null;
		} else {
			for (int i = 0; i < notMyFrinedsUser.length; i++) {
				notMyFriendsList.add(notMyFrinedsUser[i].getUserId());
			}
			if (notMyFriendsList.size() != 0) {
				notMyFriendsArray = new String[notMyFriendsList.size()];
				notMyFriendsList.toArray(notMyFriendsArray);
			}
		}
		
		String lastUserName = null;
		Date lastModifiedTime = null;
		SwoUser lastSwoUser = swoMgr.getUser(userId, lastId, IManager.LEVEL_LITE);
		if (lastSwoUser != null) {
			lastUserName = lastSwoUser.getName();
			lastModifiedTime = lastSwoUser.getModificationDate();
		}

		return getSeraUserInfoByIdArrayOrderByLastNameAndMaxList(userId, notMyFriendsArray, lastUserName, lastModifiedTime, maxList);
	}
	
	@Override
	public FriendInformList getMyFriendInformations(int maxList) throws Exception {
		
		ISeraManager seraMgr = SwManagerFactory.getInstance().getSeraManager();
		ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();

		User user = SmartUtil.getCurrentUser();
		String userId = user.getId();
		
		FriendInformList friendInformList = new FriendInformList();
		
		friendInformList.setFriends(getFriendsById(userId, null, maxList, null));
		friendInformList.setNonFriends(getNotMyFrined(userId, null, maxList, null));
		friendInformList.setRequesters(getRequestFriend(userId, null, maxList));

		SeraFriendCond friendCond = new SeraFriendCond();
		friendCond.setAcceptStatus(SeraFriend.ACCEPT_STATUS_ACCEPT);
		friendCond.setRequestIdOrReceiveId(userId);
		long totalFriends = seraMgr.getFriendSize(userId, friendCond);
	
		SeraFriendCond requestfriendCond = new SeraFriendCond();
		requestfriendCond.setAcceptStatus(SeraFriend.ACCEPT_STATUS_YET);
		requestfriendCond.setReceiveId(userId);
		long totalRequesters = seraMgr.getFriendSize(userId, requestfriendCond);
		
		SeraUserDetailCond seraUserCond = new SeraUserDetailCond();
		seraUserCond.setUserIdNotIns(new String[]{userId});
		long totalSeraUser = seraMgr.getSeraUserSize(userId, seraUserCond);
		
		SeraFriendCond relatedMyfriendCond = new SeraFriendCond();
		relatedMyfriendCond.setAcceptStatus(SeraFriend.ACCEPT_STATUS_YET);
		relatedMyfriendCond.setRequestIdOrReceiveId(userId);
		long relatedMyfriendSize = seraMgr.getFriendSize(userId, relatedMyfriendCond);
		
		friendInformList.setTotalRequesters((int)totalRequesters);
		friendInformList.setTotalFriends((int)totalFriends);
		friendInformList.setTotalNonFriends((int)(totalSeraUser-(relatedMyfriendSize + totalFriends)));
		
		return friendInformList;
	}
	
	@Override
	public SeraUserInfo[] getFriendInformsByType(int type, String userId, String lastId, int maxList) throws Exception {
		switch (type) {
		case FriendInformList.TYPE_FRIEND_REQUESTERS:
			return getRequestFriend(userId, lastId, maxList);
		case FriendInformList.TYPE_FRIENDS:
			return getFriendsById(userId, lastId, maxList, null);
		case FriendInformList.TYPE_NON_FRIENDS:
			return getNotMyFrined(userId, lastId, maxList, null);
		default:
			return null;
		}
	}
	@Override
	public Notice[] getSeraNoticesForMe() throws Exception {
		// TODO Auto-generated method stub
		
		String userId = null;
		
		User user = SmartUtil.getCurrentUser();
		if (user != null)
			userId = user.getId();
		
		Notice[] resultNotice = new Notice[SeraNotice.NUMBER_OF_NOTICES];
		
		Notice[] noticeNotices = SwServiceFactory.getInstance().getNoticeService().getNotices(userId, Notice.TYPE_NOTIFICATION);
		SeraNotice seraNotificationNotice = new SeraNotice(SeraNotice.TYPE_NOTIFICATION, 0);
		if (noticeNotices != null) {
			Notice notice = noticeNotices[0];
			//seraNotificationNotice.setLength(notice.getLength());
		}
		//보류
		SeraNotice seraMyCourseNotice = new SeraNotice(SeraNotice.TYPE_MY_COURSE, 0);
		
		
		SeraNotice seraFriendNotice = new SeraNotice(SeraNotice.TYPE_FRIEND, 0);
		
		
		Notice[] messageNotices = SwServiceFactory.getInstance().getNoticeService().getNotices(userId, Notice.TYPE_MESSAGE);
		SeraNotice seraMessageNotice = new SeraNotice(SeraNotice.TYPE_MESSAGE ,0);
		if (messageNotices != null) {
			Notice notice = messageNotices[0];
			//seraMessageNotice.setLength(notice.getLength());
		}
		
		//보류
		SeraNotice seraCalendarNotice = new SeraNotice(SeraNotice.TYPE_CALENDAR ,0);
		//보류
		SeraNotice seraBadgeNotice = new SeraNotice(SeraNotice.TYPE_BADGE ,0);
		//보류
		SeraNotice seraEventNotice = new SeraNotice(SeraNotice.TYPE_EVENT ,0);
		
		resultNotice[SeraNotice.INDEX_NOTIFICATION] = seraNotificationNotice;
		resultNotice[SeraNotice.INDEX_MY_COURSE] = seraMyCourseNotice;
		resultNotice[SeraNotice.INDEX_FRIEND] = seraFriendNotice;
		resultNotice[SeraNotice.INDEX_MESSAGE] = seraMessageNotice;
		resultNotice[SeraNotice.INDEX_CALENDAR] = seraCalendarNotice;
		resultNotice[SeraNotice.INDEX_BADGE] = seraBadgeNotice;
		resultNotice[SeraNotice.INDEX_EVENT] = seraEventNotice;
		
		return resultNotice;
	}
	@Override
	public SeraUserInfo[] searchSeraUserByType(int type, String userId, String key) throws Exception {
		try {
			if(CommonUtil.isEmpty(userId))
				return null;

			SeraUserInfo[] seraUserInfos = null;

			if(type == 2) {
				seraUserInfos = getFriendsById(userId, null, FriendInformList.MAX_FRIEND_LIST, key);
			} else if(type == 3) {
				seraUserInfos = getNotMyFrined(userId, null, FriendInformList.MAX_FRIEND_LIST, key);
			}

			return seraUserInfos;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public SeraUserInfo[] searchCourseMemberByType(int type, String courseId, String key) throws Exception {
		try {
			if(CommonUtil.isEmpty(courseId))
				return null;

			SeraUserInfo[] seraUserInfos = null;

			SwoGroupCond swoGroupCond = new SwoGroupCond();
			swoGroupCond.setStatus(SwoGroup.GROUP_STATUS_OPEN);
			swoGroupCond.setId(courseId);

			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();

			SwoGroup swoGroup = getSwoManager().getGroup(userId, swoGroupCond, IManager.LEVEL_ALL);

			if(SmartUtil.isBlankObject(swoGroup))
				return null;

			SwoGroupMember[] swoGroupMembers = swoGroup.getSwoGroupMembers();

			SeraUserDetailCond cond = new SeraUserDetailCond();

			List<String> memberList = new ArrayList<String>();
			String[] memberIds = null;

			if(CommonUtil.isEmpty(swoGroupMembers)) {
				if(type == 2) {
					return null;
				} else if(type == 3) {
					memberIds = new String[1];
					memberIds[0] = swoGroup.getGroupLeader();
					cond.setUserIdNotIns(memberIds);
				}
			} else {
				for(int i=0; i<swoGroupMembers.length; i++) {
					SwoGroupMember swoGroupMember =  swoGroupMembers[i];
					String memberId = swoGroupMember.getUserId();
					if(!memberId.equals(swoGroup.getGroupLeader()))
						memberList.add(memberId);
				}
				if(memberList.size() > 0) {
					if(type == 2) {
						memberIds = new String[memberList.size()];
						memberList.toArray(memberIds);
						cond.setUserIdIns(memberIds);
					} else if(type == 3) {
						memberIds = new String[memberList.size()+1];
						memberList.toArray(memberIds);
						memberIds[memberList.size()] = swoGroup.getGroupLeader();
						cond.setUserIdNotIns(memberIds);
					}
				}
			}

			if(key != null)
				cond.setKey(key);

			SeraUserDetail[] seraUserDetails = getSeraManager().getSeraUserDetails(userId, cond);

			if(CommonUtil.isEmpty(seraUserDetails))
				return null;

			memberIds = new String[seraUserDetails.length];

			for(int j=0; j<seraUserDetails.length; j++) {
				SeraUserDetail seraUserDetail = seraUserDetails[j];
				memberIds[j] = seraUserDetail.getUserId();
			}

			seraUserInfos = getSeraUserInfoByIdArrayOrderByLastNameAndMaxList(userId, memberIds, null, null, MenteeInformList.MAX_USER_LIST);

			return seraUserInfos;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	@Override
	public CourseInfo[] searchCourseByType(int type, String key) throws Exception {
		
		switch (type) {
		case Course.TYPE_FAVORITE_COURSES:
			return this.searchCourses(key);
		case Course.TYPE_RECOMMENDED_COURSES:
			return this.searchRecommendedCourses(key);
		case Course.TYPE_ALL_COURSES:
			return this.searchCourses(key);
		case Course.TYPE_CLOSED_COURSES:
			return this.searchClosedCourses(key);
		}
		return null;
	}
	public CourseInfo[] searchCourses(String key) throws Exception {

		SwoGroupCond groupCond = new SwoGroupCond();
		groupCond.setNameLike(key);
		groupCond.setStatus(SwoGroup.GROUP_STATUS_OPEN);
		ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();
		SwoGroup[] groups = swoMgr.getGroups("", groupCond, IManager.LEVEL_ALL);
		
		if (groups == null || groups.length == 0)
			return null;

		String[] courseIds = new String[groups.length];
		for (int i = 0; i < groups.length; i++) {
			courseIds[i] = groups[i].getId();
		}

		CourseDetailCond courseDetailCond = new CourseDetailCond();
		courseDetailCond.setCourseIdIns(courseIds);
		
		//courseDetailCond.setEndFrom(new LocalDate());
		
		CourseDetail[] courseDetails = SwManagerFactory.getInstance().getSeraManager().getCourseDetails("", courseDetailCond);
		
		if (courseDetails == null || courseDetails.length == 0) 
			return null;
	    
		CourseInfo[] courses = convertSwoGroupArrayToCourseInfoArray(groups, courseDetails);
		
		return courses;
	}
	public CourseInfo[] searchRecommendedCourses(String key) throws Exception {

		SwoGroupCond groupCond = new SwoGroupCond();
		groupCond.setNameLike(key);
		groupCond.setStatus(SwoGroup.GROUP_STATUS_OPEN);
		ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();
		SwoGroup[] groups = swoMgr.getGroups("", groupCond, IManager.LEVEL_ALL);
		
		if (groups == null || groups.length == 0)
			return null;

		String[] courseIds = new String[groups.length];
		for (int i = 0; i < groups.length; i++) {
			courseIds[i] = groups[i].getId();
		}
		
		CourseDetailCond courseDetailCond = new CourseDetailCond();
		courseDetailCond.setCourseIdIns(courseIds);
		courseDetailCond.setRecommended(true);
		//courseDetailCond.setEndFrom(new LocalDate());
		
		CourseDetail[] courseDetails = SwManagerFactory.getInstance().getSeraManager().getCourseDetails("", courseDetailCond);
		
		if (courseDetails == null || courseDetails.length == 0) 
			return null;
	    
		List resultGroupsList = new ArrayList();
		for (int i = 0; i < courseDetails.length; i++) {
			CourseDetail courseDetail = courseDetails[i];
			
			for (int j = 0; j < groups.length; j++) {
				SwoGroup group = groups[i];
				if (group.getId().equalsIgnoreCase(courseDetail.getCourseId()))
					resultGroupsList.add(group);
			}
		}

		SwoGroup[] resultGroups = new SwoGroup[resultGroupsList.size()];
		resultGroupsList.toArray(resultGroups);
		
		CourseInfo[] courses = convertSwoGroupArrayToCourseInfoArray(groups, courseDetails);
		
		return courses;
	}
	public CourseInfo[] searchClosedCourses(String key) throws Exception {

		SwoGroupCond groupCond = new SwoGroupCond();
		groupCond.setNameLike(key);
		groupCond.setStatus(SwoGroup.GROUP_STATUS_OPEN);
		ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();
		SwoGroup[] groups = swoMgr.getGroups("", groupCond, IManager.LEVEL_ALL);
		
		if (groups == null || groups.length == 0)
			return null;

		String[] courseIds = new String[groups.length];
		for (int i = 0; i < groups.length; i++) {
			courseIds[i] = groups[i].getId();
		}
		
		CourseDetailCond courseDetailCond = new CourseDetailCond();
		courseDetailCond.setCourseIdIns(courseIds);
		courseDetailCond.setEndTo(new LocalDate());
		
		CourseDetail[] courseDetails = SwManagerFactory.getInstance().getSeraManager().getCourseDetails("", courseDetailCond);
		
		if (courseDetails == null || courseDetails.length == 0) 
			return null;
	    
		List resultGroupsList = new ArrayList();
		for (int i = 0; i < courseDetails.length; i++) {
			CourseDetail courseDetail = courseDetails[i];
			
			for (int j = 0; j < groups.length; j++) {
				SwoGroup group = groups[i];
				if (group.getId().equalsIgnoreCase(courseDetail.getCourseId()))
					resultGroupsList.add(group);
			}
		}

		SwoGroup[] resultGroups = new SwoGroup[resultGroupsList.size()];
		resultGroupsList.toArray(resultGroups);
		
		CourseInfo[] courses = convertSwoGroupArrayToCourseInfoArray(groups, courseDetails);
		
		return courses;
	}
	@Override
	public CourseInfo[] searchCourseByCategory(String categoryName, String key) throws Exception {
		
		SwoGroupCond groupCond = new SwoGroupCond();
		groupCond.setNameLike(key);
		groupCond.setStatus(SwoGroup.GROUP_STATUS_OPEN);
		ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();
		SwoGroup[] groups = swoMgr.getGroups("", groupCond, IManager.LEVEL_ALL);
		
		if (groups == null || groups.length == 0)
			return null;

		String[] courseIds = new String[groups.length];
		for (int i = 0; i < groups.length; i++) {
			courseIds[i] = groups[i].getId();
		}
		
		CourseDetailCond courseDetailCond = new CourseDetailCond();
		courseDetailCond.setCourseIdIns(courseIds);
		courseDetailCond.setCategories(new String[]{categoryName});
		
		CourseDetail[] courseDetails = SwManagerFactory.getInstance().getSeraManager().getCourseDetails("", courseDetailCond);
		
		if (courseDetails == null || courseDetails.length == 0) 
			return null;
	    
		List resultGroupsList = new ArrayList();
		for (int i = 0; i < courseDetails.length; i++) {
			CourseDetail courseDetail = courseDetails[i];
			
			for (int j = 0; j < groups.length; j++) {
				SwoGroup group = groups[i];
				if (group.getId().equalsIgnoreCase(courseDetail.getCourseId()))
					resultGroupsList.add(group);
			}
		}

		SwoGroup[] resultGroups = new SwoGroup[resultGroupsList.size()];
		resultGroupsList.toArray(resultGroups);
		
		CourseInfo[] courses = convertSwoGroupArrayToCourseInfoArray(groups, courseDetails);
		
		return courses;
	}
	@Override
	public CourseInfo[] getCoursesByUser(String userId, int courseType, String lastId, int maxList) throws Exception {
		//
		// courseType : Course.MY_RUNNING_COURSE or Course.MY_ATTENDING_COURSE
		//
		ISwoManager swoManager = SwManagerFactory.getInstance().getSwoManager();
		
		
		if (courseType == Course.MY_RUNNING_COURSE) {
			SwoGroupCond runningCourseCond = new SwoGroupCond();
			runningCourseCond.setGroupLeader(userId);
			runningCourseCond.setStatus(SwoGroup.GROUP_STATUS_OPEN);
			if (!CommonUtil.isEmpty(lastId)) {
				SwoGroup lastGroup = swoManager.getGroup("", lastId, IManager.LEVEL_LITE);
				if (lastGroup != null) {
					Date lastDate = lastGroup.getCreationDate();
					runningCourseCond.setCreateDateTo(lastDate);
				}
			}
			long runningCourseCnt = swoManager.getGroupSize(userId, runningCourseCond);

			runningCourseCond.setOrders(new Order[]{new Order("creationDate", false)});
			runningCourseCond.setPageSize(maxList);
			
			SwoGroup[] runningCourses = swoManager.getGroups(userId, runningCourseCond, IManager.LEVEL_ALL);

			List<String> runningCourseIdList = new ArrayList<String>();
			if (runningCourses != null) {
				for(int i = 0; i < runningCourses.length; i++) {
					SwoGroup course = runningCourses[i];
					String courseId = course.getId();
					runningCourseIdList.add(courseId);
				}
			}

			String[] runningCourseIds = new String[runningCourseIdList.size()];
			runningCourseIdList.toArray(runningCourseIds);

			CourseDetailCond runningCourseDetailCond = new CourseDetailCond();
			runningCourseDetailCond.setCourseIdIns(runningCourseIds);
			CourseDetail[] runningCourseDetails = SwManagerFactory.getInstance().getSeraManager().getCourseDetails(userId, runningCourseDetailCond);

			CourseInfo[] runningCoursesArray = this.convertSwoGroupArrayToCourseInfoArray(runningCourses, runningCourseDetails);

			if (runningCoursesArray != null && runningCourseCnt > maxList) {
				CourseInfo[] tempRunningCourseInfos = new CourseInfo[runningCoursesArray.length + 1];
				for (int i = 0; i < runningCoursesArray.length; i++) {
					tempRunningCourseInfos[i] = runningCoursesArray[i];
				}
				tempRunningCourseInfos[runningCoursesArray.length] = new CourseInfo();
				runningCoursesArray = tempRunningCourseInfos;
			}
			
			return runningCoursesArray;
			
		} else if (courseType == Course.MY_ATTENDING_COURSE) {
			
			SwoGroupCond attendingCourseCond = new SwoGroupCond();
			SwoGroupMember[] courseMembers = new SwoGroupMember[1];
			SwoGroupMember courseMember = new SwoGroupMember();
			courseMember.setUserId(userId);
			courseMembers[0] = courseMember;
			attendingCourseCond.setSwoGroupMembers(courseMembers);
			attendingCourseCond.setNotGroupLeader(userId);
			attendingCourseCond.setStatus(SwoGroup.GROUP_STATUS_OPEN);
			if (!CommonUtil.isEmpty(lastId)) {
				SwoGroup lastGroup = swoManager.getGroup("", lastId, IManager.LEVEL_LITE);
				if (lastGroup != null) {
					Date lastDate = lastGroup.getCreationDate();
					attendingCourseCond.setCreateDateTo(lastDate);
				}
			}
			long attendingCourseCnt = swoManager.getGroupSize(userId, attendingCourseCond);

			attendingCourseCond.setOrders(new Order[]{new Order("creationDate", false)});
			attendingCourseCond.setPageSize(maxList);
			
			SwoGroup[] attendingCourses = swoManager.getGroups(userId, attendingCourseCond, IManager.LEVEL_ALL);

			List<String> attendingCourseIdList = new ArrayList<String>();
			if (attendingCourses != null) {
				for(int i = 0; i < attendingCourses.length; i++) {
					SwoGroup course = attendingCourses[i];
					String courseId = course.getId();
					attendingCourseIdList.add(courseId);
				}
			}
			
			String[] attendingCourseIds = new String[attendingCourseIdList.size()];
			attendingCourseIdList.toArray(attendingCourseIds);

			
			CourseDetailCond attendingCourseDetailCond = new CourseDetailCond();
			attendingCourseDetailCond.setCourseIdIns(attendingCourseIds);
			CourseDetail[] attendingCourseDetails = SwManagerFactory.getInstance().getSeraManager().getCourseDetails(userId, attendingCourseDetailCond);

			CourseInfo[] attendingCoursesArray = this.convertSwoGroupArrayToCourseInfoArray(attendingCourses, attendingCourseDetails);

			if (attendingCoursesArray != null && attendingCourseCnt > maxList) {
				CourseInfo[] tempAttendingCourseInfos = new CourseInfo[attendingCoursesArray.length + 1];
				for (int i = 0; i < attendingCoursesArray.length; i++) {
					tempAttendingCourseInfos[i] = attendingCoursesArray[i];
				}
				tempAttendingCourseInfos[attendingCoursesArray.length] = new CourseInfo();
				attendingCoursesArray = tempAttendingCourseInfos;
			}
			
			return attendingCoursesArray;
		} else {
			return null;
		}
		//return SeraTest.getCoursesById(userId, courseType, null, maxList);
	}
	public CourseTeamUser[] getCourseTeamUsers(String teamId) throws Exception {
		try {
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();

			CourseTeam courseTeam = getSeraManager().getCourseTeam(userId, teamId, IManager.LEVEL_ALL);
			CourseTeamUser[] courseTeamUsers = null;
			if(courseTeam != null)
				courseTeamUsers = courseTeam.getCourseTeamUsers();
			return courseTeamUsers;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public TeamInfo[] getTeamsByCourse(String courseId) throws Exception {
		try {
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();

			TeamInfo[] teamInfos = null;
			List<TeamInfo> teamInfoList = new ArrayList<TeamInfo>();

			CourseTeamCond courseTeamCond = new CourseTeamCond();
			courseTeamCond.setCourseId(courseId);

			CourseTeam[] courseTeams = getSeraManager().getCourseTeams(userId, courseTeamCond, IManager.LEVEL_LITE);

			if(!CommonUtil.isEmpty(courseTeams)) {
				for(CourseTeam courseTeam : courseTeams) {
					String teamId = courseTeam.getObjId();
					String teamName = courseTeam.getName();
					TeamInfo teamInfo = new TeamInfo(teamId, teamName);
					teamInfoList.add(teamInfo);
				}
			}
			if(teamInfoList.size() > 0) {
				teamInfos = new TeamInfo[teamInfoList.size()];
				teamInfoList.toArray(teamInfos);
			}
			return teamInfos;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public Team convertCourseTeamToTeam(String userId, CourseTeam courseTeam) throws Exception {
		try {
			if(courseTeam == null)
				return null;
			Team team = null;
			List<SeraUserInfo> seraUserInfoList = new ArrayList<SeraUserInfo>();
			SeraUserInfo[] seraUserInfos = null;
			if(courseTeam != null) {
				team = new Team();
				team.setId(courseTeam.getObjId());
				team.setName(courseTeam.getName());
				team.setCourseId(courseTeam.getCourseId());
				team.setDesc(courseTeam.getDescription());
				team.setStart(new LocalDate(courseTeam.getStartDate().getTime()));
				team.setEnd(new LocalDate(courseTeam.getEndDate().getTime()));
				team.setAccessPolicy(courseTeam.getAccessPolicy());
				team.setMaxMembers(courseTeam.getMemberSize());
				CourseTeamUser[] teamUsers = courseTeam.getCourseTeamUsers();
				SeraFriendCond seraFriendCond = new SeraFriendCond();
				seraFriendCond.setAcceptStatus(SeraFriend.ACCEPT_STATUS_ACCEPT);
				if(!CommonUtil.isEmpty(teamUsers)) {
					for(CourseTeamUser teamUser : teamUsers) {
						SeraUserInfo seraUserInfo = new SeraUserInfo();
						String id = teamUser.getUserId();
						SwoUserExtend swoUserExtend = getSwoManager().getUserExtend(userId, id, false);
						String name = null;
						int role = -1;
						String smallPictureName = null;
						if(swoUserExtend != null) {
							name = swoUserExtend.getName();
							role = swoUserExtend.getAuthId().equals("EXTERNALUSER") ? User.USER_LEVEL_EXTERNAL_USER : swoUserExtend.getAuthId().equals("USER") ? User.USER_LEVEL_INTERNAL_USER : swoUserExtend.getAuthId().equals("ADMINISTRATOR") ? User.USER_LEVEL_AMINISTRATOR : User.USER_LEVEL_SYSMANAGER;
							smallPictureName = swoUserExtend.getSmallPictureName();
						}
						SeraUserDetail seraUserDetail = getSeraManager().getSeraUserById(userId, id);
						String goal = null;
						boolean isFriend = false;
						if(seraUserDetail != null)
							goal = seraUserDetail.getGoal();
						SeraFriend[] mySeraFriends = getSeraManager().getMyFriends(userId, seraFriendCond);
						if(!CommonUtil.isEmpty(mySeraFriends)) {
							for(SeraFriend seraFriend : mySeraFriends) {
								if(id.equals(seraFriend.getFriendId())) {
									isFriend = true;
									break;
								}
							}
						}
						seraUserInfo.setName(name);
						seraUserInfo.setRole(role);
						seraUserInfo.setSmallPictureName(smallPictureName);
						seraUserInfo.setGoal(goal);
						seraUserInfo.setFriend(isFriend);
						seraUserInfoList.add(seraUserInfo);
					}
				}
				if(seraUserInfoList.size() > 0) {
					seraUserInfos = new SeraUserInfo[seraUserInfoList.size()];
					seraUserInfoList.toArray(seraUserInfos);
				}
				team.setMembers(seraUserInfos);
			}
			return team;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public Team getMyTeamByCourse(String courseId) throws Exception {
		try {
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();
			CourseTeamCond courseTeamCond = new CourseTeamCond();
			CourseTeamUser[] courseTeamUsers = new CourseTeamUser[1];
			CourseTeamUser courseTeamUser = new CourseTeamUser();
			courseTeamUser.setUserId(userId);
			courseTeamUsers[0] = courseTeamUser;
			courseTeamCond.setCourseTeamUsers(courseTeamUsers);
			courseTeamCond.setCourseId(courseId);

			CourseTeam courseTeam = getSeraManager().getCourseTeam(userId, courseTeamCond, IManager.LEVEL_ALL);

			return convertCourseTeamToTeam(userId, courseTeam);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public Team getTeamById(String teamId) throws Exception {
		try {
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();

			CourseTeam courseTeam = getSeraManager().getCourseTeam(userId, teamId, IManager.LEVEL_ALL);

			return convertCourseTeamToTeam(userId, courseTeam);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public SeraUserInfo[] getSeraUserInfos(List<String> seraUserIdList, int maxList, String lastId, String key) {
		try {
			SeraUserInfo[] seraUserInfos = null;
			List<SeraUserInfo> seraUserInfosList = new ArrayList<SeraUserInfo>();
			String[] seraUserIds = null;

			SwoUserExtend[] userExtends = null;
			String lastName = null;
			Date lastModifiedTime = null;
			SwoUser swoUser = null;
			if(lastId != null) {
				swoUser = getSwoManager().getUser(null, lastId, IManager.LEVEL_LITE);
				lastName = swoUser.getName();
				lastModifiedTime = swoUser.getModificationDate();
			}

			if(seraUserIdList.size() > 0) {
				seraUserIds = new String[seraUserIdList.size()];
				seraUserIdList.toArray(seraUserIds);
				userExtends = SwManagerFactory.getInstance().getSwoManager().getUsersExtend(null, seraUserIds, lastName, lastModifiedTime, key);
			}

			seraUserInfos = new SeraUserInfo[userExtends.length];

			if(maxList > 0) {
				if(userExtends.length > maxList) {
					seraUserInfos = new SeraUserInfo[maxList+1];
					seraUserInfos[maxList] = new SeraUserInfo();
				}
			}

			User currentUser = SmartUtil.getCurrentUser();
			String currentUserId = currentUser.getId();

			SeraFriendCond seraFriendCond = new SeraFriendCond();
			seraFriendCond.setAcceptStatus(SeraFriend.ACCEPT_STATUS_ACCEPT);

			SeraFriend[] mySeraFriends = getSeraManager().getMyFriends(currentUserId, seraFriendCond);

			for(int i=0; i<seraUserInfos.length; i++) {
				SwoUserExtend swoUserExtend = userExtends[i];
				SeraUserInfo member = new SeraUserInfo();
				String id = swoUserExtend.getId();
				member.setId(swoUserExtend.getId());
				member.setName(swoUserExtend.getName());
				member.setNickName(swoUserExtend.getNickName());
				member.setPosition(swoUserExtend.getPosition());
				member.setRole(swoUserExtend.getAuthId().equals("EXTERNALUSER") ? User.USER_LEVEL_EXTERNAL_USER : swoUserExtend.getAuthId().equals("USER") ? User.USER_LEVEL_INTERNAL_USER : swoUserExtend.getAuthId().equals("ADMINISTRATOR") ? User.USER_LEVEL_AMINISTRATOR : User.USER_LEVEL_SYSMANAGER);
				member.setSmallPictureName(swoUserExtend.getSmallPictureName());
				member.setDepartment(new DepartmentInfo(swoUserExtend.getDepartmentId(), swoUserExtend.getDepartmentName(), swoUserExtend.getDepartmentDesc()));
				boolean isFriend = false;
				if(!CommonUtil.isEmpty(mySeraFriends)) {
					for(SeraFriend seraFriend : mySeraFriends) {
						if(id.equals(seraFriend.getFriendId())) {
							isFriend = true;
							break;
						}
					}
				}
				member.setFriend(isFriend);
				SeraUserDetail seraUserDetail = getSeraManager().getSeraUserById(currentUserId, id);
				String goal = null;
				if(seraUserDetail != null)
					goal = seraUserDetail.getGoal();
				member.setGoal(goal);
				seraUserInfosList.add(member);
			}

			if(seraUserInfosList.size() > 0) {
				seraUserInfos = new SeraUserInfo[seraUserInfosList.size()];
				seraUserInfosList.toArray(seraUserInfos);
			}
			return seraUserInfos;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public SeraUserInfo[] getRequestersOfTeam(MemberInformList memberInformList, CourseTeamUser[] courseTeamUsers, int maxList) {
		try {
			if(CommonUtil.isEmpty(courseTeamUsers))
				return null;

			SeraUserInfo[] seraUserInfos = null;
			List<String> requesterIdList = new ArrayList<String>();
			for(CourseTeamUser courseTeamUser : courseTeamUsers) {
				String teamUserId = courseTeamUser.getUserId();
				String joinType = courseTeamUser.getJoinType();
				String joinStatus = courseTeamUser.getJoinStatus();
				if(CourseTeamUser.JOINTYPE_INVITE.equals(joinType) && CourseTeamUser.JOINSTATUS_READY.equals(joinStatus)) {
					requesterIdList.add(teamUserId);
				}
			}
			if(requesterIdList.size() > 0) {
				if(memberInformList != null)
					memberInformList.setTotalRequesters(requesterIdList.size());
				seraUserInfos = getSeraUserInfos(requesterIdList, maxList, null, null);
			}
			return seraUserInfos;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public SeraUserInfo[] getMembersOfTeam(MemberInformList memberInformList, CourseTeamUser[] courseTeamUsers, int maxList, String lastId, String key) {
		try {
			if(CommonUtil.isEmpty(courseTeamUsers))
				return null;

			SeraUserInfo[] seraUserInfos = null;
			List<String> memberIdList = new ArrayList<String>();
			for(CourseTeamUser courseTeamUser : courseTeamUsers) {
				String teamUserId = courseTeamUser.getUserId();
				String joinStatus = courseTeamUser.getJoinStatus();
				if(CourseTeamUser.JOINSTATUS_COMPLETE.equalsIgnoreCase(joinStatus)) {
					memberIdList.add(teamUserId);
				}
			}
			if(memberIdList.size() > 0) {
				if(memberInformList != null)
					memberInformList.setTotalMembers(memberIdList.size());
				seraUserInfos = getSeraUserInfos(memberIdList, maxList, lastId, key);
			}
			return seraUserInfos;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public SeraUserInfo[] getNonMembersOfTeam(MemberInformList memberInformList, CourseTeamUser[] courseTeamUsers, int maxList, String lastId, String key) {
		try {
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();

			SeraUserInfo[] seraUserInfos = null;
			SeraUserDetail[] seraUserDetails = null;
			String[] relationIds = null;
			List<String> relationIdList = new ArrayList<String>();
			if(!CommonUtil.isEmpty(courseTeamUsers)) {
				for(CourseTeamUser courseTeamUser : courseTeamUsers) {
					String teamUserId = courseTeamUser.getUserId();
					relationIdList.add(teamUserId);
				}
				if(relationIdList.size() > 0) {
					relationIds = new String[relationIdList.size()];
					relationIdList.toArray(relationIds);
				}
			}

			SeraUserDetailCond seraUserDetailCond = new SeraUserDetailCond();
			seraUserDetailCond.setUserIdNotIns(relationIds);
			seraUserDetails = getSeraManager().getSeraUserDetails(userId, seraUserDetailCond);

			List<String> nonMemberIdList = new ArrayList<String>();

			if(!CommonUtil.isEmpty(seraUserDetails)) {
				for(SeraUserDetail seraUserDetail : seraUserDetails) {
					String nonMemberId = seraUserDetail.getUserId();
					nonMemberIdList.add(nonMemberId);
				}
			}

			if(nonMemberIdList.size() > 0) {
				if(memberInformList != null)
					memberInformList.setTotalNonMembers(nonMemberIdList.size());
				seraUserInfos = getSeraUserInfos(nonMemberIdList, maxList, lastId, key);
			}
			return seraUserInfos;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public MemberInformList getTeamMemberInformations(String teamId, int maxList) throws Exception {
		try {
			MemberInformList memberInformList = new MemberInformList();

			SeraUserInfo[] requesters = null;
			SeraUserInfo[] members = null;
			SeraUserInfo[] nonMembers = null;

			CourseTeamUser[] courseTeamUsers = getCourseTeamUsers(teamId);
			requesters = getRequestersOfTeam(memberInformList, courseTeamUsers, maxList);
			members = getMembersOfTeam(memberInformList, courseTeamUsers, maxList, null, null);
			nonMembers = getNonMembersOfTeam(memberInformList, courseTeamUsers, maxList, null, null);

			memberInformList.setRequesters(requesters);
			memberInformList.setMembers(members);
			memberInformList.setNonMembers(nonMembers);

			return memberInformList;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public SeraUserInfo[] getTeamMemberInformsByType(int type, String teamId, String lastId, int maxList) throws Exception {
		try {
			CourseTeamUser[] courseTeamUsers = getCourseTeamUsers(teamId);
			switch (type) {
			case MemberInformList.TYPE_MEMBERS:
				return getMembersOfTeam(null, courseTeamUsers, maxList, lastId, null);
			case MemberInformList.TYPE_NON_MEMBERS:
				return getNonMembersOfTeam(null, courseTeamUsers, maxList, lastId, null);
			default:
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	@Override
	public SeraUserInfo[] searchTeamMemberByType(int type, String courseId, String teamId, String key) throws Exception {
		try {

			CourseTeamUser[] courseTeamUsers = getCourseTeamUsers(teamId);
			switch (type) {
			case MemberInformList.TYPE_MEMBERS:
				return getMembersOfTeam(null, courseTeamUsers, 0, null, key);
			case MemberInformList.TYPE_NON_MEMBERS:
				return getNonMembersOfTeam(null, courseTeamUsers, 0, null, key);
			default:
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public void replyTeamMemberRequest(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void teamMemberRequest(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void destroyTeamMembership(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public SeraBoardList getSeraBoards(int maxList) throws Exception {
		SeraBoardList seraBoards = new SeraBoardList();
		seraBoards.setSeraNews(getBoardInstancesByCourseId(null, null, null, null, Constants.SERA_WID_SERA_NEWS, null, maxList));
		seraBoards.setSeraTrends(getBoardInstancesByCourseId(null, null, null, null, Constants.SERA_WID_SERA_TREND, null, maxList));
		return seraBoards;
	}
	@Override
	public String setMentorProfile(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try {
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();

			String mentorId = (String)requestBody.get("mentorId");

			MentorDetail mentorDetail = getSeraManager().getMentorDetailById(userId, mentorId);
			if(mentorDetail == null)
				return null;

			Map<String, Object> frmSetCourseMentor = (Map<String, Object>)requestBody.get("frmSetCourseMentor");
			Set<String> keySet = frmSetCourseMentor.keySet();
			Iterator<String> itr = keySet.iterator();

			String txtaMentorEducations = null;
			String txtaMentorWorks = null;
			String txtaMentorHistory = null;
			String txtaMenteeHistory = null;
			String txtaMentorLectures = null;
			String txtaMentorAwards = null;
			String txtaMentorEtc = null;

			while (itr.hasNext()) {
				String fieldId = (String)itr.next();
				Object fieldValue = frmSetCourseMentor.get(fieldId);
				if(fieldValue instanceof String) {
					String strValue = (String)frmSetCourseMentor.get(fieldId);
					if(fieldId.equals("txtaMentorEducations")) {
						txtaMentorEducations = strValue;
					} else if(fieldId.equals("txtaMentorWorks")) {
						txtaMentorWorks = strValue;
					} else if(fieldId.equals("txtaMentorHistory")) {
						txtaMentorHistory = strValue;
					} else if(fieldId.equals("txtaMenteeHistory")) {
						txtaMenteeHistory = strValue;
					} else if(fieldId.equals("txtaMentorLectures")) {
						txtaMentorLectures = strValue;
					} else if(fieldId.equals("txtaMentorAwards")) {
						txtaMentorAwards = strValue;
					} else if(fieldId.equals("txtaMentorEtc")) {
						txtaMentorEtc = strValue;
					}
				}
			}

			mentorDetail.setEducations(txtaMentorEducations);
			mentorDetail.setWorks(txtaMentorWorks);
			mentorDetail.setMentorHistory(txtaMentorHistory);
			mentorDetail.setMenteeHistory(txtaMenteeHistory);
			mentorDetail.setLectures(txtaMentorLectures);
			mentorDetail.setAwards(txtaMentorAwards);
			mentorDetail.setEtc(txtaMentorEtc);

			mentorDetail = getSeraManager().setMentorDetail(userId, mentorDetail);

			return mentorDetail.getMentorId();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}