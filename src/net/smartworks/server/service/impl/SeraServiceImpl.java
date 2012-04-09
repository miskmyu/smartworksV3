package net.smartworks.server.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import net.smartworks.model.community.info.WorkSpaceInfo;
import net.smartworks.model.filter.Condition;
import net.smartworks.model.filter.SearchFilter;
import net.smartworks.model.instance.FieldData;
import net.smartworks.model.instance.SortingField;
import net.smartworks.model.instance.WorkInstance;
import net.smartworks.model.instance.info.InstanceInfo;
import net.smartworks.model.instance.info.InstanceInfoList;
import net.smartworks.model.instance.info.RequestParams;
import net.smartworks.model.security.AccessPolicy;
import net.smartworks.model.sera.Course;
import net.smartworks.model.sera.CourseList;
import net.smartworks.model.sera.FriendList;
import net.smartworks.model.sera.Mentor;
import net.smartworks.model.sera.MissionInstance;
import net.smartworks.model.sera.SeraUser;
import net.smartworks.model.sera.info.CourseInfo;
import net.smartworks.model.sera.info.MissionInstanceInfo;
import net.smartworks.model.sera.info.ReviewInstanceInfo;
import net.smartworks.model.work.FormField;
import net.smartworks.model.work.SmartWork;
import net.smartworks.model.work.Work;
import net.smartworks.model.work.info.SmartWorkInfo;
import net.smartworks.model.work.info.WorkCategoryInfo;
import net.smartworks.model.work.info.WorkInfo;
import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.model.Filter;
import net.smartworks.server.engine.common.model.Filters;
import net.smartworks.server.engine.common.model.Order;
import net.smartworks.server.engine.common.model.SmartServerConstant;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.common.util.id.IDCreator;
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
import net.smartworks.server.engine.organization.manager.ISwoManager;
import net.smartworks.server.engine.organization.model.SwoGroup;
import net.smartworks.server.engine.organization.model.SwoGroupCond;
import net.smartworks.server.engine.organization.model.SwoGroupMember;
import net.smartworks.server.engine.organization.model.SwoUserExtend;
import net.smartworks.server.engine.sera.manager.ISeraManager;
import net.smartworks.server.engine.sera.model.CourseDetail;
import net.smartworks.server.engine.sera.model.CourseDetailCond;
import net.smartworks.server.engine.sera.model.MentorDetail;
import net.smartworks.server.engine.sera.model.SeraConstant;
import net.smartworks.server.service.ISeraService;
import net.smartworks.server.service.factory.SwServiceFactory;
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
		course.setLastMissionIndex(courseDetail.getLastMissionIndex());
		//course.setTeam("TEAM");
		course.setTargetPoint(courseDetail.getTargetPoint());
		course.setAchievedPoint(courseDetail.getAchievedPoint());
		
		course.setMissions(getMissionInstanceList(course.getId(), null, null));
		
		
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
						if(!formFieldId.equals(FormField.ID_OWNER) && !formFieldId.equals(FormField.ID_CREATED_DATE) && !formFieldId.equals(FormField.ID_LAST_MODIFIER) && !formFieldId.equals(FormField.ID_LAST_MODIFIED_DATE))
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
					MissionInstanceInfo missionInstanceInfo = new MissionInstanceInfo();
					SwdRecord swdRecord = swdRecords[i];
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
					int type = WorkInstance.TYPE_INFORMATION;
					missionInstanceInfo.setType(type);
					missionInstanceInfo.setStatus(WorkInstance.STATUS_COMPLETED);
					String workSpaceId = swdRecord.getWorkSpaceId();
					if(CommonUtil.isEmpty(workSpaceId))
						workSpaceId = userId;

					WorkSpaceInfo workSpaceInfo = SwServiceFactory.getInstance().getCommunityService().getWorkSpaceInfoById(workSpaceId);

					missionInstanceInfo.setWorkSpace(workSpaceInfo);

					WorkCategoryInfo groupInfo = null;
					if (!CommonUtil.isEmpty(swdRecordExtends[0].getSubCtgId()))
						groupInfo = new WorkCategoryInfo(swdRecordExtends[0].getSubCtgId(), swdRecordExtends[0].getSubCtg());
		
					WorkCategoryInfo categoryInfo = new WorkCategoryInfo(swdRecordExtends[0].getParentCtgId(), swdRecordExtends[0].getParentCtg());
		
					WorkInfo workInfo = new SmartWorkInfo(formId, formName, SmartWork.TYPE_INFORMATION, groupInfo, categoryInfo);
	
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
		
		return courseId;
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
		courseDetail.setLastMissionIndex(-1);
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
		SwoUserExtend userExtend = SwManagerFactory.getInstance().getSwoManager().getUserExtend(userId, userId, true);
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
			List<Condition> conditionList = new ArrayList<Condition>();
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
			
			return missions[0];
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
					}
				}
			}
			String courseId = swdRecord.getWorkSpaceId();
			missionInstance.setWorkSpace(getCourseById(courseId));
			//MissionInstance mission = SeraTest.getMissionById(missionId);
			return missionInstance;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
	}

	@Override
	public SeraUser getSeraUserById(String userId) throws Exception {
		try{
			return SeraTest.getSeraUserById(userId);
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
	}
}
