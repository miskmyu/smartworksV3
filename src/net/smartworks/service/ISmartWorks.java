package net.smartworks.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.claros.commons.mail.models.ConnectionProfile;

import com.google.gdata.data.youtube.FormUploadToken;

import net.smartworks.model.RecordList;
import net.smartworks.model.YTMetaInfo;
import net.smartworks.model.approval.ApprovalLine;
import net.smartworks.model.approval.ApprovalLineInst;
import net.smartworks.model.calendar.CompanyCalendar;
import net.smartworks.model.calendar.CompanyEvent;
import net.smartworks.model.calendar.WorkHourPolicy;
import net.smartworks.model.community.Department;
import net.smartworks.model.community.Group;
import net.smartworks.model.community.User;
import net.smartworks.model.community.WorkSpace;
import net.smartworks.model.community.info.CommunityInfo;
import net.smartworks.model.community.info.DepartmentInfo;
import net.smartworks.model.community.info.GroupInfo;
import net.smartworks.model.community.info.CommunityInfoList;
import net.smartworks.model.community.info.GroupMemberList;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.community.info.WorkSpaceInfo;
import net.smartworks.model.company.CompanyGeneral;
import net.smartworks.model.filter.SearchFilter;
import net.smartworks.model.instance.ImageInstance;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.MailInstance;
import net.smartworks.model.instance.RunningCounts;
import net.smartworks.model.instance.TaskInstance;
import net.smartworks.model.instance.WorkInstance;
import net.smartworks.model.instance.info.AsyncMessageInstanceInfo;
import net.smartworks.model.instance.info.AsyncMessageList;
import net.smartworks.model.instance.info.BoardInstanceInfo;
import net.smartworks.model.instance.info.ChatInstanceInfo;
import net.smartworks.model.instance.info.CommentInstanceInfo;
import net.smartworks.model.instance.info.EventInstanceInfo;
import net.smartworks.model.instance.info.ImageInstanceInfo;
import net.smartworks.model.instance.info.InstanceInfo;
import net.smartworks.model.instance.info.InstanceInfoList;
import net.smartworks.model.instance.info.RequestParams;
import net.smartworks.model.instance.info.TaskInstanceInfo;
import net.smartworks.model.mail.EmailServer;
import net.smartworks.model.mail.MailAccount;
import net.smartworks.model.mail.MailFolder;
import net.smartworks.model.notice.Notice;
import net.smartworks.model.notice.NoticeBox;
import net.smartworks.model.report.Data;
import net.smartworks.model.report.Report;
import net.smartworks.model.report.ReportPane;
import net.smartworks.model.sera.Course;
import net.smartworks.model.sera.CourseAdList;
import net.smartworks.model.sera.CourseList;
import net.smartworks.model.sera.FriendInformList;
import net.smartworks.model.sera.FriendList;
import net.smartworks.model.sera.GlobalSearchList;
import net.smartworks.model.sera.MemberInformList;
import net.smartworks.model.sera.MenteeInformList;
import net.smartworks.model.sera.Mentor;
import net.smartworks.model.sera.MissionInstance;
import net.smartworks.model.sera.SeraBoardList;
import net.smartworks.model.sera.SeraUser;
import net.smartworks.model.sera.Team;
import net.smartworks.model.sera.info.CourseInfo;
import net.smartworks.model.sera.info.MissionInstanceInfo;
import net.smartworks.model.sera.info.ReviewInstanceInfo;
import net.smartworks.model.sera.info.SeraUserInfo;
import net.smartworks.model.sera.info.TeamInfo;
import net.smartworks.model.service.ExternalForm;
import net.smartworks.model.service.WSDLDetail;
import net.smartworks.model.service.WebService;
import net.smartworks.model.work.SmartForm;
import net.smartworks.model.work.Work;
import net.smartworks.model.work.info.FileCategoryInfo;
import net.smartworks.model.work.info.ImageCategoryInfo;
import net.smartworks.model.work.info.SmartWorkInfo;
import net.smartworks.model.work.info.UsedWorkInfo;
import net.smartworks.model.work.info.WorkInfo;
import net.smartworks.model.work.info.WorkInfoList;
import net.smartworks.server.engine.common.model.Property;
import net.smartworks.server.engine.docfile.exception.DocFileException;
import net.smartworks.server.engine.docfile.model.IFileModel;
import net.smartworks.server.engine.infowork.domain.model.SwdRecord;
import net.smartworks.util.LocalDate;

public interface ISmartWorks {

	public final static String CONTEXT_HOME = "sf.hm";
	public final static String CONTEXT_SMARTCASTER = "sf.sc";
	public final static String CONTEXT_DASHBOARD = "sf.db";
	public final static String CONTEXT_MYPROFILE = "sf.pf";
	public final static String CONTEXT_COMMUNITIES = "sf.cm";

	public final static String CONTEXT_USER_SPACE = "us.sp";
	public final static String CONTEXT_GROUP_SPACE = "gp.sp";
	public final static String CONTEXT_DEPARTMENT_SPACE = "dp.sp";
	public final static String CONTEXT_IWORK_SPACE = "iw.sp";
	public final static String CONTEXT_PWORK_SPACE = "pw.sp";
	public final static String CONTEXT_SWORK_SPACE = "sw.sp";
	public final static String CONTEXT_FILE_SPACE = "fl.sp";
	public final static String CONTEXT_IMAGE_SPACE = "im.sp";
	public final static String CONTEXT_EVENT_SPACE = "ev.sp";
	public final static String CONTEXT_MEMO_SPACE = "mm.sp";
	public final static String CONTEXT_BOARD_SPACE = "bd.sp";
	public final static String CONTEXT_MAIL_SPACE = "ml.sp";

	public final static String CONTEXT_PREFIX_SELF = "sf.";

	public final static int SPACE_TYPE_WORK_LIST = 1;
	public final static int SPACE_TYPE_WORK_INSTANCE = 2;
	public final static int SPACE_TYPE_TASK_INSTANCE = 3;
	public final static int SPACE_TYPE_USER = 4;
	public final static int SPACE_TYPE_GROUP = 5;
	public final static int SPACE_TYPE_DEPARTMENT = 6;
	public final static int SPACE_TYPE_AWORK_LIST = 21;
	public final static int SPACE_TYPE_IMAGE_LIST = 22;
	public final static int SPACE_TYPE_FILE_LIST = 23;
	public final static int SPACE_TYPE_EVENT_LIST = 24;
	public final static int SPACE_TYPE_MEMO_LIST = 25;
	public final static int SPACE_TYPE_BOARD_LIST = 26;	
	public final static int SPACE_TYPE_MAIL_LIST = 27;
	public final static int SPACE_TYPE_SAVED_LIST = 28;	
	public final static int SPACE_TYPE_REPORT_LIST = 29;	

	public final static int CONTEXT_PREFIX_LENGTH = 6;
	
	public final static String CONTEXT_PREFIX_HOME = "sf.hm.";
	public final static String CONTEXT_PREFIX_SMARTCASTER = "sf.sc.";
	public final static String CONTEXT_PREFIX_COMMUNITIES = "sf.cm.";
	public final static String CONTEXT_PREFIX_DASHBOARD = "sf.db.";
	public final static String CONTEXT_PREFIX_MYPROFILE = "sf.pf.";
	public final static String CONTEXT_PREFIX_USER_SPACE = "us.sp.";
	public final static String CONTEXT_PREFIX_GROUP_SPACE = "gp.sp.";
	public final static String CONTEXT_PREFIX_DEPARTMENT_SPACE = "dp.sp.";

	public final static String CONTEXT_ALL_WORKS_LIST = "aw.li";

	public final static String CONTEXT_PREFIX_IWORK_LIST = "iw.li.";
	public final static String CONTEXT_PREFIX_PWORK_LIST = "pw.li.";
	public final static String CONTEXT_PREFIX_SWORK_LIST = "sw.li.";
	public final static String CONTEXT_PREFIX_FILE_LIST = "fl.li.";
	public final static String CONTEXT_PREFIX_IMAGE_LIST = "im.li.";
	public final static String CONTEXT_PREFIX_EVENT_LIST = "ev.li.";
	public final static String CONTEXT_PREFIX_MEMO_LIST = "mm.li.";
	public final static String CONTEXT_PREFIX_BOARD_LIST = "bd.li.";
	public final static String CONTEXT_PREFIX_MAIL_LIST = "ml.li.";
	public final static String CONTEXT_PREFIX_SAVED_LIST = "sv.li.";
	public final static String CONTEXT_PREFIX_FORUM_LIST = "fr.li.";
	public final static String CONTEXT_PREFIX_CONTACTS_LIST = "ct.li.";
	public final static String CONTEXT_PREFIX_USER_LIST = "us.li.";
	public final static String CONTEXT_PREFIX_DEPARTMENT_LIST = "dp.li.";
	public final static String CONTEXT_PREFIX_GROUP_LIST = "gp.li.";
	public final static String CONTEXT_PREFIX_REPORT_LIST = "rp.li.";

	public final static String CONTEXT_PREFIX_IWORK_SPACE = "iw.sp.";
	public final static String CONTEXT_PREFIX_PWORK_SPACE = "pw.sp.";
	public final static String CONTEXT_PREFIX_SWORK_SPACE = "sw.sp.";
	public final static String CONTEXT_PREFIX_FILE_SPACE = "fl.sp.";
	public final static String CONTEXT_PREFIX_IMAGE_SPACE = "im.sp.";
	public final static String CONTEXT_PREFIX_EVENT_SPACE = "ev.sp.";
	public final static String CONTEXT_PREFIX_MEMO_SPACE = "mm.sp.";
	public final static String CONTEXT_PREFIX_BOARD_SPACE = "bd.sp.";
	public final static String CONTEXT_PREFIX_MAIL_SPACE = "ml.sp.";
	public final static String CONTEXT_PREFIX_REPORT_SPACE = "rp.sp.";

	public final static String CONTEXT_PREFIX_BUILDER_SPACE = "bd.sp.";

	public abstract InstanceInfo[] getMyRunningInstances(LocalDate lastInstanceDate, int requestSize, boolean assignedOnly, boolean runningOnly) throws Exception;
	
	public abstract InstanceInfo[] getMyRunningInstances(LocalDate lastInstanceDate, int requestSize, boolean assignedOnly, boolean runningOnly, RequestParams params) throws Exception;
	
	public abstract RunningCounts getMyRunningInstancesCounts() throws Exception;
	
	public abstract String[] getBroadcastingMessages() throws Exception;

	public abstract CompanyCalendar[] getCompanyCalendars(LocalDate fromDate, int days) throws Exception;

	public abstract WorkSpace getWorkSpaceById(String workSpaceId) throws Exception;

	public abstract CompanyCalendar[] getCompanyCalendars(LocalDate fromDate, LocalDate toDate) throws Exception;

	public abstract EventInstanceInfo[] getMyEventInstances(LocalDate fromDate, int days) throws Exception;

	public abstract EventInstanceInfo[] getCommunityEventInstances(LocalDate fromDate, int days, String workSpaceId) throws Exception;

	public abstract EventInstanceInfo[] getMyEventInstances(LocalDate fromDate, LocalDate toDate) throws Exception;

	public abstract BoardInstanceInfo[] getMyRecentBoardInstances() throws Exception;

	public abstract BoardInstanceInfo[] getCommunityRecentBoardInstances(String workSpaceId) throws Exception;

	public abstract CompanyCalendar getCompanyEventBox(LocalDate date) throws Exception;

	public abstract SmartWorkInfo[] getMyFavoriteWorks() throws Exception;

	public abstract CommunityInfo[] getMyFavoriteCommunities() throws Exception;

	public abstract WorkInfo[] getAllWorkCategoryByCategoryId(String categoryId) throws Exception;
	
	public abstract WorkInfo[] getMyAllWorksByCategoryId(String categoryId) throws Exception;

	public abstract WorkInfo[] getAllWorksByCategoryId(String categoryId) throws Exception;

	public abstract ImageCategoryInfo[] getImageCategoriesByType(int displayType, String spaceId) throws Exception;

	public abstract FileCategoryInfo[] getFileCategoriesByType(int displayType, String spaceId, String parentId) throws Exception;

	public abstract InstanceInfo[] getMyRecentInstances() throws Exception;

	public abstract DepartmentInfo[] getMyDepartments() throws Exception;

	public abstract DepartmentInfo[] getMyChildDepartments() throws Exception;

	public abstract Department getDepartmentById(String departId) throws Exception;

	public abstract GroupInfo[] getMyGroups() throws Exception;

	public abstract Group getGroupById(String groupId) throws Exception;

	public abstract String setGroup(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract User getUserById(String userId) throws Exception;

	public abstract SmartWorkInfo[] searchWork(String key, int searchType) throws Exception;

	public abstract WorkSpaceInfo[] searchCommunity(String key, HttpServletRequest request) throws Exception;

	public abstract DepartmentInfo[] searchDepartment(String key, HttpServletRequest request) throws Exception;

	public abstract UserInfo[] searchCommunityMember(String communityId, String key) throws Exception;

	public abstract UserInfo[] searchCommunityNonMember(String communityId, String key) throws Exception;
	/*
	 * 현재 스마트웍스에 접속되어 있는 유저들에 대한 정보를 리턴한다 (채팅가능 유져)
	 * 채팅가능 유저목록의 갱신을 위하여 서버에서는 세션정보를 리스닝하고 있으면서 세션의 접속과 끊김 이벤트가
	 * 발생하였을경우 SmartUtil의 publishAChatters 메소드를 호출하여 채팅 가능 유저 목록을 갱신시켜준다
	 * (세션의 접속,끊김 이벤트를 받을수 있나?, publishAChatters 을 이용할때 세션정보에서 사용자의 회사아이디를 가져올수 있나?)
	 */
	public abstract UserInfo[] getAvailableChatter(HttpServletRequest request) throws Exception;

	public abstract UserInfo[] searchAvailableChatter(String key) throws Exception;

	public abstract UserInfo[] searchUser(String key) throws Exception;

	public abstract EventInstanceInfo[] getCompanyEventsByDate(LocalDate date, int maxEvents) throws Exception;

	public abstract EventInstanceInfo[] getMyEventsByDate(LocalDate date, int maxEvents) throws Exception;

	public abstract WorkHourPolicy getCompanyWorkHourPolicy() throws Exception;

	public abstract Notice[] getNoticesForMe() throws Exception;

	public abstract void removeNoticeInstance(String noticeId) throws Exception;

	public abstract NoticeBox getNoticeBoxForMe10(int noticeType, String lastNoticeId) throws Exception;

	public String getWorkIdByFormId(String formId) throws Exception;

	public abstract Work getWorkById(String workId) throws Exception;

	public abstract TaskInstanceInfo getTaskInstanceById(String taskInstId) throws Exception;
	
	public abstract InstanceInfo[] searchMyRunningInstance(String key) throws Exception;

	public abstract CommunityInfo[] getMyCommunities() throws Exception;

	public abstract CommunityInfo[] getMyCommunitiesForUpload(String workId) throws Exception;
	
	public abstract CommentInstanceInfo[] getRecentCommentsInWorkManual(String workId, int length) throws Exception;
	
	public abstract InstanceInfo[] getRecentSubInstancesInInstance(String workId, int length) throws Exception;
	
	public abstract int getSubInstancesInInstanceCount(String workId) throws Exception;
	
	public abstract InstanceInfo[] getSubInstancesInInstance(String workId, int length, LocalDate to) throws Exception;
	
	public abstract int getSubInstancesInForwardCount(String forwardId) throws Exception;

	public abstract TaskInstanceInfo[] getSubInstancesInForward(String forwardId, int length, LocalDate to) throws Exception;

	public abstract InstanceInfoList getIWorkInstanceList(String workId, RequestParams params) throws Exception;
	
	public InstanceInfoList getAllPWorkInstanceList(boolean runningOnly, RequestParams params) throws Exception;
	
	public abstract InstanceInfoList getPWorkInstanceList(String workId, RequestParams params) throws Exception;
	
	public abstract InstanceInfoList getWorkInstanceList(String workSpaceId, RequestParams params) throws Exception;
	
	public abstract InstanceInfoList getSavedInstanceList(String workSpaceId, RequestParams params) throws Exception;
	
	public abstract InstanceInfoList getImageInstanceList(String workSpaceId, RequestParams params) throws Exception;
	
	public abstract ImageInstanceInfo[] getImageInstancesByDate(int displayBy, String wid, String parentId, LocalDate lastDate, int maxCount) throws Exception;

	public abstract InstanceInfoList getFileInstanceList(String workSpaceId, RequestParams params) throws Exception;
	
	public abstract EventInstanceInfo[] getEventInstanceList(String workSpaceId, LocalDate fromDate, LocalDate toDate) throws Exception;
	
	public abstract InstanceInfoList getMemoInstanceList(String workSpaceId, RequestParams params) throws Exception;
	
	public abstract InstanceInfoList getBoardInstanceList(String workSpaceId, RequestParams params) throws Exception;
	
	public abstract InstanceInfoList getMailInstanceList(String folderId, RequestParams params) throws Exception;

	public abstract CommunityInfoList getCommunityInstanceList(int type, RequestParams params) throws Exception;
	
	public abstract WorkInstance getWorkInstanceById(int workType, String workId, String instanceId) throws Exception; // hsshin
	
	public abstract WorkInstance getSavedWorkInstanceById(int workType, String workId, String instanceId) throws Exception;

	public abstract TaskInstanceInfo[][] getTaskInstancesByWorkHours(String contextId, String spaceId, LocalDate date, int maxSize) throws Exception;

	public abstract TaskInstanceInfo[][] getTaskInstancesByDates(String contextId, String spaceId, LocalDate fromDate, LocalDate toDate, int maxSize) throws Exception;

	public abstract TaskInstanceInfo[][] getTaskInstancesByWeeks(String contextId, String spaceId, LocalDate month, int maxSize) throws Exception;

	public abstract TaskInstanceInfo[] getTaskInstancesByDate(String contextId, String spaceId, LocalDate fromDate, LocalDate toDate, int maxSize) throws Exception;

	public abstract TaskInstanceInfo[] getTaskInstancesByTimeline(String contextId, String spaceId, LocalDate fromDate, int maxSize) throws Exception;

	public abstract TaskInstanceInfo[] getCastTaskInstancesByDate(LocalDate fromDate, int maxSize) throws Exception;

	public abstract TaskInstanceInfo[] getInstanceTaskHistoriesById(String instId) throws Exception;

	public abstract InstanceInfoList[] getInstanceRelatedWorksById(String instId) throws Exception;

	public abstract InstanceInfo[] getSpaceInstancesByDate(String spaceId, LocalDate fromDate, int maxSize) throws Exception;
	
	public abstract Report getReportById(String reportId) throws Exception;

	public abstract SearchFilter getSearchFilterById(String workType, String workId, String filterId) throws Exception;
	
	public abstract Data getReportData(HttpServletRequest request) throws Exception;
	
	public abstract Data getReportDataByDef(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract String setInformationWorkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void removeInformationWorkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void forwardIworkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void approvalIworkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void forwardPworkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void approvalPworkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract String startProcessWorkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void removeProcessWorkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	/**
	 * @deprecated
	 */
	public abstract String setFileInstance(HttpServletRequest request) throws Exception;
	/**
	 * @deprecated
	 */
	public abstract String setEventInstance(HttpServletRequest request) throws Exception;
	/**
	 * @deprecated
	 */
	public abstract String setMemoInstance(HttpServletRequest request) throws Exception;
	/**
	 * @deprecated
	 */
	public abstract String setBoardInstance(HttpServletRequest request) throws Exception;

	public abstract CommunityInfo[] getAllComsByDepartmentId(String departmentId, boolean departmentOnly) throws Exception;

	public abstract UserInfo[] getAllUsersByDepartmentId(String departmentId) throws Exception;

	public abstract CommunityInfo[] getAllComsByGroupId(String groupId) throws Exception;

	public abstract MailFolder[] getMailFoldersById(String folderId) throws Exception;

	public abstract MailFolder[] getMailFolders() throws Exception;

	public abstract MailInstance getMailInstanceById(String folderId, String msgId, int sendType) throws Exception;

	public abstract void setMyProfile(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void ajaxUploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception;

	public abstract void uploadTempFile(HttpServletRequest request, HttpServletResponse response) throws Exception;

	public abstract void uploadYTVideo(HttpServletRequest request, HttpServletResponse response) throws Exception;

	public abstract List<IFileModel> findFileGroup(HttpServletRequest request) throws Exception;

	public abstract void deleteFile(HttpServletRequest request, HttpServletResponse response) throws Exception;

	public abstract String getFormXml(HttpServletRequest request, HttpServletResponse response) throws Exception;

	public abstract SwdRecord getRecord(HttpServletRequest request) throws Exception;

	public abstract SwdRecord getRecord(String workId, String recordId, String taskInstId) throws Exception;

	public abstract SwdRecord refreshDataFields(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract String setWorkSearchFilter(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void removeWorkSearchFilter(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception;

	public abstract RequestParams setInstanceListParams(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void addAFavoriteWork(HttpServletRequest request) throws Exception;

	public abstract void removeAFavoriteWork(HttpServletRequest request) throws Exception;

	public abstract void addAFavoriteCommunity(HttpServletRequest request) throws Exception;

	public abstract void removeAFavoriteCommunity(HttpServletRequest request) throws Exception;

	public abstract CompanyGeneral getCompanyGeneral() throws Exception;

	public abstract void setCompanyGeneral(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract RecordList getWorkHourPolicyList(RequestParams params) throws Exception;
	
	public abstract WorkHourPolicy getWorkHourPolicyById(String id) throws Exception;
	
	public abstract void setWorkHourPolicy(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void removeWorkHourPolicy(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract RecordList getCompanyEventList(RequestParams params) throws Exception;
	
	public abstract CompanyEvent getCompanyEventById(String id) throws Exception;
	
	public abstract void setCompanyEvent(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void removeCompanyEvent(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract RecordList getApprovalLineList(RequestParams params) throws Exception;
	
	public abstract ApprovalLine getApprovalLineById(String id) throws Exception;
	
	public abstract void setApprovalLine(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void removeApprovalLine(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract RecordList getExternalFormList(RequestParams params) throws Exception;
	
	public abstract ExternalForm getExternalFormById(String id) throws Exception;
	
	public abstract void setExternalForm(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void removeExternalForm(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void setMember(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void removeMember(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void setDepartment(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void removeDepartment(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void checkIdDuplication(HttpServletRequest request) throws Exception;

	public abstract RecordList getWebServiceList(RequestParams params) throws Exception;
	
	public abstract WebService getWebServiceById(String id) throws Exception;
	
	public abstract void setWebService(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void removeWebService(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract WSDLDetail getWsdlDetailFromUri(String wsdlUri) throws Exception;

	public abstract String performTaskInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract String createTaskInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract String returnTaskInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract String reassignTaskInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract String abendTaskInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract String tempSaveTaskInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void startWorkService(String workId) throws Exception;

	public abstract void stopWorkService(String workId) throws Exception;

	public abstract void startWorkEditing(String workId) throws Exception;

	public abstract void stopWorkEditing(String workId) throws Exception;

	public abstract void setWorkSettings(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void publishWorkToStore(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void createNewCategory(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void setCategory(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void removeCategory(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void createNewWorkDefinition(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void setWorkDefinition(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract String addCommentOnWork(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void updateCommentOnWork(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void removeCommentFromWork(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract String addCommentOnInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void updateCommentOnInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void removeCommentFromInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void addLikeToInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void removeLikeFromInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void createAsyncMessage(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void removeAsyncMessage(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void setAsyncMessage(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract ChatInstanceInfo[] fetchAsyncMessagesByChatid(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	public abstract String createNewMission(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract String modifyMission(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract String removeMission(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract String performMissionReport(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract String setSeraNote(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract Team createNewTeam(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void modifyCourseTeam(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void removeCourseTeam(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract String updateSeraProfile(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract String createNewCourse(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract String setCourseProfile(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract String removeCourse(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract CourseList getCoursesById(String userId, int maxList) throws Exception;

	public abstract CourseInfo[] getCoursesById(String userId, int courseType, LocalDate fromDate, int maxList) throws Exception;

	public abstract Course getCourseById(String courseId) throws Exception;

	public abstract Mentor getMentorById(String mentorId) throws Exception;

	public abstract FriendList getFriendsById(String userId, int maxList) throws Exception;

	public abstract SeraUserInfo[] getFriendsById(String userId, String lastId, int maxList, String key) throws Exception;

	public abstract SeraUserInfo[] getFriendRequestsByUserId(String userId, String lastId, int maxList) throws Exception;

	public abstract void replyFriendRequest(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void friendRequest(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void destroyFriendship(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void removeSeraInstane(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract InstanceInfo[] getCourseNotices(String courseId, LocalDate fromDate, int maxList) throws Exception;

	public abstract FormUploadToken getUploadToken(YTMetaInfo metaInfo, String ytUserId, String ytPassword) throws Exception;

	public abstract InstanceInfo[] getSeraInstances(int type, String userId, String courseId, String missionId, String teamId, LocalDate fromDate, int maxList) throws Exception;

	public abstract void joinGroupRequest(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract ReviewInstanceInfo[] getReviewInstancesByCourse(String courseId, LocalDate fromDate, int maxList) throws Exception;

	public abstract void addReviewOnCourse(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void inviteGroupMembers(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void approvalJoinGroup(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void pushoutGroupMember(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void leaveGroup(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void updateGroupSetting(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract MissionInstanceInfo[] getMissionInstanceList(String courseId, LocalDate fromDate, LocalDate toDate) throws Exception;
	
	public abstract MissionInstance getMissionById(String missionId) throws Exception;

	public abstract SeraUser getSeraUserById(String userId) throws Exception;

	public abstract CourseInfo[] getCoursesByType(int courseType, String lastId, int maxList) throws Exception;

	public abstract CourseInfo[] getCoursesByCategory(String categoryName, String lastId, int maxList) throws Exception;

	public abstract CommentInstanceInfo[] getSubInstancesByRefId(String refId, int maxSize) throws Exception;
	
	public abstract String createSeraUser(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract String leaveSeraUser(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract MenteeInformList getCoursesMenteeInformations(String courseId, int maxList) throws Exception;

	public abstract SeraUserInfo[] getCourseMenteeInformsByType(int type, String courseId, String lastId, int maxList) throws Exception;
	
	public abstract FriendInformList getMyFriendInformations(int maxList) throws Exception;

	public abstract SeraUserInfo[] getFriendInformsByType(int type, String userId, String lastId, int maxList) throws Exception;
	
	public abstract AsyncMessageList getMyMessageInstancesByType(int type, int maxSize) throws Exception;
	
	public abstract AsyncMessageInstanceInfo[] getMyMessageInstancesByType(AsyncMessageList asyncMessageList, int type, LocalDate fromDate, int maxSize) throws Exception;
	
	public abstract Notice[] getSeraNoticesForMe() throws Exception;

	public abstract SeraUserInfo[] searchSeraUserByType(int type, String userId, String key) throws Exception;

	public abstract SeraUserInfo[] searchCourseMemberByType(int type, String courseId, String key) throws Exception;

	public abstract CourseInfo[] searchCourseByType(int type, String key) throws Exception;

	public abstract CourseInfo[] searchCourseByCategory(String categoryName, String key) throws Exception;

	public abstract CourseInfo[] getCoursesByUser(String userId, int courseType, String lastId, int maxList) throws Exception;

	public abstract TeamInfo[] getTeamsByCourse(String courseId) throws Exception;
	
	public abstract Team getMyTeamByCourse(String courseId) throws Exception;
	
	public abstract Team getTeamById(String teamId) throws Exception;

	public abstract MemberInformList getTeamMemberInformations(String teamId, int maxList) throws Exception;

	public abstract SeraUserInfo[] getTeamMemberInformsByType(int type, String teamId, String lastId, int maxList) throws Exception;

	public abstract SeraUserInfo[] searchTeamMemberByType(int type, String courseId, String teamId, String key) throws Exception;

	public abstract SeraBoardList getSeraBoards(int maxList) throws Exception;
	
	public abstract String setMentorProfile(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract MailFolder getMailFolderById(String folderId) throws Exception;
	
	public abstract Team getJoinRequestTeamByCourseId(String courseId) throws Exception;

	public abstract void replyTeamJoinRequest(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void teamJoinRequest(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void leaveTeam(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void destroyMembership(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract BoardInstanceInfo[] getSeraTrends(int maxList) throws Exception; 
	
	public abstract CourseAdList getCourseAds(int maxList) throws Exception;

	public abstract GlobalSearchList searchGlobal(String key, int maxCourseList, int maxUserList) throws Exception;

	public abstract CourseInfo[] searchCourses(String key, String lastId, int maxList) throws Exception;

	public abstract SeraUserInfo[] searchSeraUsers(String key, String lastId, int maxList) throws Exception;

	public abstract void logout(HttpServletRequest request, HttpServletResponse response) throws Exception;

	public abstract void sendMail(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract UserInfo[] searchEmailAddress(String key) throws Exception;
	
	public abstract CommunityInfo[] getAllComsByCategoryId(String categoryId) throws Exception;

	public abstract void saveMailAsDraft(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void moveMails(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void addJunk(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void removeJunk(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract String[][] getJunkIds() throws Exception;

	public abstract void deleteMails(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void newMailFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void setMailFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void deleteMailFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract String getFolderIdByType(int folderType) throws Exception;

	public abstract void checkEmail() throws Exception;
	
	public abstract int getUnreadEmails() throws Exception;

	public abstract void commentOnTaskForward(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void commentOnTaskApproval(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract ApprovalLineInst getApprovalLineInstById(String instId) throws Exception;

	public abstract RecordList getEmailServerList(RequestParams params) throws Exception;
	
	public abstract EmailServer getEmailServerById(String id) throws Exception;
	
	public abstract void setEmailServer(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void removeEmailServer(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract EmailServer[] getEmailServers() throws Exception;

	public abstract ConnectionProfile[] getMailConnectionProfiles() throws Exception;

	public abstract MailAccount[] getMyMailAccounts() throws Exception;

	public abstract void removeWorkDefinition(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract String copyWorkDefinition(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract String moveWorkDefinition(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract EventInstanceInfo[] getCommingEventInstances(String spaceId, int maxLength) throws Exception;
	
	public abstract BoardInstanceInfo[] getRecentBoardInstances(String spaceId, int maxLength) throws Exception;

	public abstract GroupMemberList getGroupMemberInformList(String groupId) throws Exception;

	public abstract UserInfo[] getGroupMembersById(String groupId, String lastId, int maxSize) throws Exception;

	public abstract void updateDepartmentSetting(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract boolean canIUploadToWorkSpace(String workSpaceId, String workId) throws Exception;

	public abstract void createNewFileFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void setFileFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void removeFileFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void createNewImageFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void setImageFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void removeImageFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void removeImageInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void moveFileInstances(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void moveImageInstances(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void setIWorkManual(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void setPWorkManual(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
		
	public abstract ImageInstance getImageInstanceById(String instId) throws Exception;

	public abstract boolean authenticateEmailAccount(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract int uploadExcelToWork(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void addAdjunctMember(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract String createMailContent(String companyId, String emailId, byte[] content, Date receivedDate) throws DocFileException;
	
	public abstract byte[] readMailContent(String fileId, Date receivedDate) throws DocFileException;
	
	public abstract void deleteMailContent(String fileId, Date receivedDate) throws DocFileException;	

	public abstract InstanceInfoList getUpdateHistoryList(String instanceId, RequestParams params) throws Exception;
	
	public abstract InstanceInfoList getForwardHistoryList(String instanceId, RequestParams params) throws Exception;
	
	public abstract InstanceInfoList getDownloadHistoryList(String instanceId, String taskInstanceId, RequestParams params) throws Exception;
	
	public abstract InstanceInfoList getRelatedWorkList(String instanceId, RequestParams params) throws Exception;
	
	public abstract InstanceInfoList getForwardTasksById(String forwardId, RequestParams params) throws Exception;

	public abstract SwdRecord getRecordByKeyValue(String workId, String keyValue) throws Exception;

	public abstract TaskInstance getTaskInstanceById(String workId, String taskInstId) throws Exception;

	public abstract SmartForm getFormById(String formId, String workId) throws Exception;

	public abstract InstanceInfoList getAllUcityPWorkInstanceList(boolean runningOnly, RequestParams params, int auditId) throws Exception;
	
	public abstract String getUcityChartXml(String categoryName, String periodName, String serviceName, String eventName) throws Exception;
	
	public abstract void getUcityChartExcel(String categoryName, String periodName, String serviceName, String eventName, HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	public abstract int[][] getUcityAuditTaskCounts(boolean runningOnly) throws Exception;
	
	public abstract Property[] getUcityExtendedPropertyByTaskInstId(String taskInstId) throws Exception;
	
	public abstract void changeMailPasswordRequest(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract WorkInfoList getAppWorkList(RequestParams params) throws Exception;

	public abstract void downloadAppWork(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract String setWorkReport(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract String removeWorkReport(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract String setWorkReportPane(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract String removeWorkReportPane(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract int getUserReportCount(String targetWorkId) throws Exception;

	public abstract InstanceInfoList getReportInstanceList(String targetWorkId, int targetWorkType, String producedBy, RequestParams params) throws Exception;

	public abstract ReportPane[] getMyDashboard() throws Exception;
	
	public abstract UsedWorkInfo[] getUsedWorkListByUserId(String userId) throws Exception;
	
	public abstract UserInfo getHeadByUserId(String userId) throws Exception;

	public abstract void executeRetireMember(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
}
