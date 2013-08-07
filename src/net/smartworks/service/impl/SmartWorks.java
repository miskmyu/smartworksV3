
package net.smartworks.service.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import net.smartworks.model.work.info.WorkInfo;
import net.smartworks.model.work.info.WorkInfoList;
import net.smartworks.server.engine.common.model.Property;
import net.smartworks.server.engine.docfile.exception.DocFileException;
import net.smartworks.server.engine.docfile.model.IFileModel;
import net.smartworks.server.engine.infowork.domain.model.SwdRecord;
import net.smartworks.server.service.IBuilderService;
import net.smartworks.server.service.ICalendarService;
import net.smartworks.server.service.ICommunityService;
import net.smartworks.server.service.IDocFileService;
import net.smartworks.server.service.IInstanceService;
import net.smartworks.server.service.ILoginService;
import net.smartworks.server.service.IMailService;
import net.smartworks.server.service.INoticeService;
import net.smartworks.server.service.IReportService;
import net.smartworks.server.service.ISeraService;
import net.smartworks.server.service.ISettingsService;
import net.smartworks.server.service.IWorkService;
import net.smartworks.server.service.IYouTubeService;
import net.smartworks.service.ISmartWorks;
import net.smartworks.util.LocalDate;

import org.claros.commons.mail.models.ConnectionProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gdata.data.youtube.FormUploadToken;

@Service
public class SmartWorks implements ISmartWorks {

	ICommunityService communityService;
	ILoginService loginService;
	INoticeService noticeService;
	ICalendarService calendarService;
	IInstanceService instanceService;
	IWorkService workService;
	IReportService reportService;
	IMailService mailService;
	IDocFileService docFileService;
	ISettingsService settingsService;
	IBuilderService builderService;
	IYouTubeService youTubeService;
	ISeraService seraService;

	@Autowired
	public void setReportService(IReportService reportService) {
		this.reportService = reportService;
	}
	@Autowired
	public void setCommunityService(ICommunityService communityService) {
		this.communityService = communityService;
	}

	@Autowired
	public void setLoginService(ILoginService loginService) {
		this.loginService = loginService;
	}

	@Autowired
	public void setNoticeService(INoticeService noticeService) {
		this.noticeService = noticeService;
	}

	@Autowired
	public void setCalendarService(ICalendarService calendarService) {
		this.calendarService = calendarService;
	}

	@Autowired
	public void setInstanceService(IInstanceService instanceService) {
		this.instanceService = instanceService;
	}

	@Autowired
	public void setWorkService(IWorkService workService) {
		this.workService = workService;
	}

	@Autowired
	public void setMailService(IMailService mailService) {
		this.mailService = mailService;
	}

	@Autowired
	public void setDocFileService(IDocFileService docFileService) {
		this.docFileService = docFileService;
	}

	@Autowired
	public void setSettingsService(ISettingsService settingsService) {
		this.settingsService = settingsService;
	}

	@Autowired
	public void setBuilderService(IBuilderService builderService) {
		this.builderService = builderService;
	}

	@Autowired
	public void setYouTubeService(IYouTubeService youTubeService) {
		this.youTubeService = youTubeService;
	}

	@Autowired
	public void setSeraService(ISeraService seraService) {
		this.seraService = seraService;
	}

	@Override
	public DepartmentInfo[] getMyDepartments() throws Exception {
		return communityService.getMyDepartments();
	}

	@Override
	public DepartmentInfo[] getMyChildDepartments() throws Exception {
		return communityService.getMyChildDepartments();
	}

	@Override
	public Department getDepartmentById(String departId) throws Exception {
		return communityService.getDepartmentById(departId);
	}

	@Override
	public GroupInfo[] getMyGroups() throws Exception {
		return communityService.getMyGroups();
	}

	@Override
	public Group getGroupById(String groupId) throws Exception {
		return communityService.getGroupById(groupId);
	}

	@Override
	public String setGroup(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return communityService.setGroup(requestBody, request);
	}

	@Override
	public User getUserById(String userId) throws Exception {
		return communityService.getUserById(userId);
	}

	@Override
	public WorkSpaceInfo[] searchCommunity(String key, HttpServletRequest request) throws Exception {
		return communityService.searchCommunity(key, request);
	}

	@Override
	public UserInfo[] searchCommunityMember(String communityId, String key) throws Exception {
		return communityService.searchCommunityMember(communityId, key);
	}

	@Override
	public SmartWorkInfo[] searchWork(String key, int searchType) throws Exception {
		return workService.searchWork(key, searchType);
	}

	@Override
	public UserInfo[] searchUser(String key) throws Exception {
		return communityService.searchUser(key);
	}

	@Override
	public InstanceInfo[] searchMyRunningInstance(String key) throws Exception {
		return instanceService.searchMyRunningInstance(key);
	}

	@Override
	public WorkSpace getWorkSpaceById(String workSpaceId) throws Exception {
		return communityService.getWorkSpaceById(workSpaceId);
	}

	@Override
	public UserInfo[] getAvailableChatter(HttpServletRequest request) throws Exception {
		return communityService.getAvailableChatter(request);
	}

	@Override
	public UserInfo[] searchAvailableChatter(String key) throws Exception {
		return communityService.searchAvailableChatter(key);
	}

	@Override
	public String[] getBroadcastingMessages() throws Exception {
		return noticeService.getBroadcastingMessages();
	}

	@Override
	public Notice[] getNoticesForMe() throws Exception {
		return noticeService.getNoticesForMe();
	}

	@Override
	public void removeNoticeInstance(String noticeId) throws Exception {
		noticeService.removeNoticeInstance(noticeId);
	}

	@Override
	public NoticeBox getNoticeBoxForMe10(int noticeType, String lastNoticeId) throws Exception {
		return noticeService.getNoticeBoxForMe10(noticeType, lastNoticeId);
	}

	@Override
	public CompanyCalendar[] getCompanyCalendars(LocalDate fromDate, int days) throws Exception {
		return calendarService.getCompanyCalendars(fromDate, days);
	}

	@Override
	public CompanyCalendar[] getCompanyCalendars(LocalDate fromDate, LocalDate toDate) throws Exception {
		return calendarService.getCompanyCalendars(fromDate, toDate);
	}

	@Override
	public EventInstanceInfo[] getMyEventInstances(LocalDate fromDate, int days) throws Exception {
		return calendarService.getMyEventInstances(fromDate, days);
	}

	@Override
	public EventInstanceInfo[] getCommunityEventInstances(LocalDate fromDate, int days, String workSpaceId) throws Exception {
		return calendarService.getCommunityEventInstances(fromDate, days, workSpaceId);
	}

	@Override
	public EventInstanceInfo[] getMyEventInstances(LocalDate fromDate, LocalDate toDate) throws Exception {
		return calendarService.getMyEventInstances(fromDate, toDate);
	}

	@Override
	public CompanyCalendar getCompanyEventBox(LocalDate date) throws Exception {
		return calendarService.getCompanyEventBox(date);
	}

	@Override
	public EventInstanceInfo[] getCompanyEventsByDate(LocalDate date, int maxEvents) throws Exception {
		return calendarService.getCompanyEventsByDate(date, maxEvents);
	}

	@Override
	public EventInstanceInfo[] getMyEventsByDate(LocalDate date, int maxEvents) throws Exception {
		return calendarService.getMyEventsByDate(date, maxEvents);
	}

	@Override
	public WorkHourPolicy getCompanyWorkHourPolicy() throws Exception {
		return calendarService.getCompanyWorkHourPolicy();
	}

	@Override
	public BoardInstanceInfo[] getMyRecentBoardInstances() throws Exception {
		return instanceService.getMyRecentBoardInstances();
	}

	@Override
	public BoardInstanceInfo[] getCommunityRecentBoardInstances(String workSpaceId) throws Exception {
		return instanceService.getCommunityRecentBoardInstances(workSpaceId);
	}

	@Override
	public InstanceInfo[] getMyRecentInstances() throws Exception {
		return instanceService.getMyRecentInstances();
	}

	@Override
	public SmartWorkInfo[] getMyFavoriteWorks() throws Exception {
		return workService.getMyFavoriteWorks();
	}

	@Override
	public CommunityInfo[] getMyFavoriteCommunities() throws Exception {
		return communityService.getMyFavoriteCommunities();
	}

	@Override
	public WorkInfo[] getAllWorkCategoryByCategoryId(String categoryId) throws Exception {
		return workService.getAllWorkCategoryByCategoryId(categoryId);
	}
	
	@Override
	public WorkInfo[] getMyAllWorksByCategoryId(String categoryId) throws Exception {
		return workService.getMyAllWorksByCategoryId(categoryId);
	}

	@Override
	public WorkInfo[] getAllWorksByCategoryId(String categoryId) throws Exception {
		return workService.getAllWorksByCategoryId(categoryId);
	}

	@Override
	public ImageCategoryInfo[] getImageCategoriesByType(int displayType, String spaceId) throws Exception {
		return workService.getImageCategoriesByType(displayType, spaceId);
	}

	@Override
	public FileCategoryInfo[] getFileCategoriesByType(int displayType, String spaceId, String parentId) throws Exception {
		return workService.getFileCategoriesByType(displayType, spaceId, parentId);
	}

	@Override
	public InstanceInfo[] getMyRunningInstances(LocalDate lastInstanceDate, int requestSize, boolean assignedOnly, boolean runningOnly) throws Exception {
		return instanceService.getMyRunningInstances(lastInstanceDate, requestSize, assignedOnly, runningOnly);
	}
	@Override
	public InstanceInfo[] getMyRunningInstances(LocalDate lastInstanceDate, int requestSize, boolean assignedOnly, boolean runningOnly, RequestParams params) throws Exception {
		return instanceService.getMyRunningInstances(lastInstanceDate, requestSize, assignedOnly, runningOnly, params);
	}

	@Override
	public RunningCounts getMyRunningInstancesCounts() throws Exception {
		return instanceService.getMyRunningInstancesCounts();
	}

	@Override
	public String getWorkIdByFormId(String formId) throws Exception {
		return workService.getWorkIdByFormId(formId);
	}

	@Override
	public Work getWorkById(String workId) throws Exception {
		return workService.getWorkById(workId);
	}

	@Override
	public TaskInstanceInfo getTaskInstanceById(String taskInstId) throws Exception {
		return instanceService.getTaskInstanceById(taskInstId);
	}

	@Override
	public CommunityInfo[] getMyCommunities() throws Exception {
		return communityService.getMyCommunities();
	}

	@Override
	public CommunityInfo[] getMyCommunitiesForUpload(String workId) throws Exception {
		return communityService.getMyCommunitiesForUpload(workId);
	}

	@Override
	public CommentInstanceInfo[] getRecentCommentsInWorkManual(String workId, int length) throws Exception {
		return instanceService.getRecentCommentsInWorkManual(workId, length);
	}

	@Override
	public InstanceInfo[] getRecentSubInstancesInInstance(String workId, int length) throws Exception {
		return instanceService.getRecentSubInstancesInInstance(workId, length);
	}

	@Override
	public int getSubInstancesInInstanceCount(String workId) throws Exception {
		return instanceService.getSubInstanceCountInInstance(workId);
	}

	@Override
	public InstanceInfo[] getSubInstancesInInstance(String workId, int length, LocalDate to) throws Exception {
		return instanceService.getSubInstancesInInstance(workId, length, to);
	}

	@Override
	public String setInformationWorkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return instanceService.setInformationWorkInstance(requestBody, request);
	}

	@Override
	public void removeInformationWorkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		instanceService.removeInformationWorkInstance(requestBody, request);
	}

	@Override
	public void forwardIworkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		instanceService.forwardIworkInstance(requestBody, request);
	}
	
	@Override
	public void approvalIworkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		instanceService.approvalIworkInstance(requestBody, request);
	}
	
	@Override
	public String startProcessWorkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return instanceService.startProcessWorkInstance(requestBody, request);

	}

	@Override
	public String setFileInstance(HttpServletRequest request) throws Exception {
		return instanceService.setFileInstance(request);

	}

	@Override
	public String setEventInstance(HttpServletRequest request) throws Exception {
		return instanceService.setEventInstance(request);

	}

	@Override
	public String setMemoInstance(HttpServletRequest request) throws Exception {
		return instanceService.setMemoInstance(request);
	}

	@Override
	public String setBoardInstance(HttpServletRequest request) throws Exception {
		return instanceService.setBoardInstance(request);

	}

	@Override
	public InstanceInfoList getIWorkInstanceList(String workId, RequestParams params) throws Exception {
		return instanceService.getIWorkInstanceList(workId, params);
	}
	@Override
	public InstanceInfoList getAllPWorkInstanceList(boolean runningOnly, RequestParams params) throws Exception {
		return instanceService.getAllPWorkInstanceList(runningOnly, params);
	}
	@Override
	public InstanceInfoList getPWorkInstanceList(String workId, RequestParams params) throws Exception {
		return instanceService.getPWorkInstanceList(workId, params);
	}

	@Override
	public InstanceInfoList getWorkInstanceList(String workSpaceId, RequestParams params) throws Exception {
		return instanceService.getWorkInstanceList(workSpaceId, params);
	}

	@Override
	public InstanceInfoList getSavedInstanceList(String workSpaceId, RequestParams params) throws Exception {
		return instanceService.getSavedInstanceList(workSpaceId, params);
	}

	@Override
	public InstanceInfoList getImageInstanceList(String workSpaceId, RequestParams params) throws Exception {
		return instanceService.getImageInstanceList(workSpaceId, params);
	}

	@Override
	public ImageInstanceInfo[] getImageInstancesByDate(int displayBy, String wid, String parentId, LocalDate lastDate, int maxCount) throws Exception{
		return instanceService.getImageInstancesByDate(displayBy, wid, parentId, lastDate, maxCount);
	}
	
	@Override
	public InstanceInfoList getFileInstanceList(String workSpaceId, RequestParams params) throws Exception {
		return instanceService.getFileInstanceList(workSpaceId, params);
	}

	@Override
	public EventInstanceInfo[] getEventInstanceList(String workSpaceId, LocalDate fromDate, LocalDate toDate) throws Exception {
		return instanceService.getEventInstanceList(workSpaceId, fromDate, toDate);
	}

	@Override
	public InstanceInfoList getMemoInstanceList(String workSpaceId, RequestParams params) throws Exception {
		return instanceService.getMemoInstanceList(workSpaceId, params);
	}

	@Override
	public InstanceInfoList getBoardInstanceList(String workSpaceId, RequestParams params) throws Exception {
		return instanceService.getBoardInstanceList(workSpaceId, params);
	}

	@Override
	public WorkInstance getWorkInstanceById(int workType, String workId, String instanceId) throws Exception {
		return instanceService.getWorkInstanceById(workType, workId, instanceId);
	}

	@Override
	public WorkInstance getSavedWorkInstanceById(int workType, String workId, String instanceId) throws Exception {
		return instanceService.getSavedWorkInstanceById(workType, workId, instanceId);
	}
	
	@Override
	public TaskInstanceInfo[][] getTaskInstancesByWorkHours(String contextId, String spaceId, LocalDate date, int maxSize) throws Exception {
		// TODO Auto-generated method stub
		return instanceService.getTaskInstancesByWorkHours(contextId, spaceId, date, maxSize);
	}
	@Override
	public TaskInstanceInfo[][] getTaskInstancesByDates(String contextId, String spaceId, LocalDate fromDate, LocalDate toDate, int maxSize) throws Exception {
		// TODO Auto-generated method stub
		return instanceService.getTaskInstancesByDates(contextId, spaceId, fromDate, toDate, maxSize);
	}
	@Override
	public TaskInstanceInfo[][] getTaskInstancesByWeeks(String contextId, String spaceId, LocalDate month, int maxSize) throws Exception {
		// TODO Auto-generated method stub
		return instanceService.getTaskInstancesByWeeks(contextId, spaceId, month, maxSize);
	}
	@Override
	public TaskInstanceInfo[] getTaskInstancesByDate(String contextId, String spaceId, LocalDate fromDate, LocalDate toDate, int maxSize) throws Exception {
		// TODO Auto-generated method stub
		return instanceService.getTaskInstancesByDate(contextId, spaceId, fromDate, toDate, maxSize);
	}	

	@Override
	public TaskInstanceInfo[] getTaskInstancesByTimeline(String contextId, String spaceId, LocalDate fromDate, int maxSize) throws Exception {
		// TODO Auto-generated method stub
		return instanceService.getTaskInstancesByTimeline(contextId, spaceId, fromDate, maxSize);
	}	

	@Override
	public TaskInstanceInfo[] getCastTaskInstancesByDate(LocalDate fromDate, int maxSize) throws Exception {
		// TODO Auto-generated method stub
		return instanceService.getCastTaskInstancesByDate(fromDate, maxSize);
	}	

	@Override
	public TaskInstanceInfo[] getInstanceTaskHistoriesById(String instId) throws Exception {
		return instanceService.getInstanceTaskHistoriesById(instId);
	}

	@Override
	public InstanceInfoList[] getInstanceRelatedWorksById(String instId) throws Exception {
		return instanceService.getInstanceRelatedWorksById(instId);
	}

	@Override
	public InstanceInfo[] getSpaceInstancesByDate(String spaceId, LocalDate fromDate, int maxSize) throws Exception {
		return instanceService.getSpaceInstancesByDate(spaceId, fromDate, maxSize);
	}
	
	/*
	 * @Override >>>>>>> branch 'master' of
	 * git@github.com:maninsoft/smartworksV3.git public String createFile(String
	 * userId, String groupId, IFileModel file) throws Exception { return
	 * docFileService.createFile(userId, groupId, file); }
	 * 
	 * @Override public String createFileList(String userId, String groupId,
	 * List<IFileModel> fileList) throws Exception { return
	 * docFileService.createFileList(userId, groupId, fileList); }
	 * 
	 * @Override public IFileModel retrieveFile(String fileId) throws Exception
	 * { return docFileService.retrieveFile(fileId); }
	 * 
	 * @Override public void updateFile(String userId, IFileModel file) throws
	 * Exception { docFileService.updateFile(userId, file); }
	 * 
	 * @Override public void deleteFile(String fileId) throws Exception {
	 * docFileService.deleteFile(fileId); }
	 * 
	 * @Override public void deleteFileGroup(String groupId) throws Exception {
	 * docFileService.deleteFileGroup(groupId); }
	 * 
	 * @Override public List<IFileModel> findFileGroup(String groupId) throws
	 * Exception { return docFileService.findFileGroup(groupId); }
	 * 
	 * @Override public List<String> findFileIdListByGroup(String groupId)
	 * throws Exception { return docFileService.findFileIdListByGroup(groupId);
	 * }
	 * 
	 * @Override public String createDocument(String userId, String groupId,
	 * IDocumentModel document, List<FileItem> fileList) throws Exception {
	 * return docFileService.createDocument(userId, groupId, document,
	 * fileList); }
	 * 
	 * @Override public void updateDocument(String userId, IDocumentModel
	 * document) throws Exception { docFileService.updateDocument(userId,
	 * document); }
	 * 
	 * @Override public IDocumentModel retrieveDocument(String documentId)
	 * throws Exception { return docFileService.retrieveDocument(documentId); }
	 * 
	 * @Override public IDocumentModel retrieveDocumentByGroupId(String
	 * fileGroupId) throws Exception { return
	 * docFileService.retrieveDocumentByGroupId(fileGroupId); }
	 * 
	 * @Override public List<String> findDocIdByGroupId(String fileGroupId)
	 * throws Exception { return docFileService.findDocIdByGroupId(fileGroupId);
	 * }
	 * 
	 * @Override public void deleteDocument(String documentId) throws Exception
	 * { docFileService.deleteDocument(documentId); }
	 * 
	 * @Override public IDocumentModel retrieveDocumentByRef(int refType, String
	 * refId) throws Exception { return
	 * docFileService.retrieveDocumentByRef(refType, refId); }
	 */

//	public String uploadFile(HttpServletRequest request) throws Exception {
//		return docFileService.uploadFile(request);
//	}

	public void ajaxUploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		docFileService.ajaxUploadFile(request, response);
	}

	public List<IFileModel> findFileGroup(HttpServletRequest request) throws Exception {
		return docFileService.findFileGroup(request);
	}

	public void deleteFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		docFileService.deleteFile(request, response);
	}

	@Override
	public Report getReportById(String reportId) throws Exception {
		// TODO Auto-generated method stub
		return reportService.getReportById(reportId);
	}

	@Override
	public SearchFilter getSearchFilterById(String workType, String workId, String filterId) throws Exception {
		// TODO Auto-generated method stub
		return workService.getSearchFilterById(workType, workId, filterId);
	}

	@Override
	public void setMyProfile(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		workService.setMyProfile(requestBody, request);
	}

	@Override
	public Data getReportData(HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return reportService.getReportData(request);
	}

	@Override
	public Data getReportDataByDef(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return reportService.getReportDataByDef(requestBody, request);
	}

	@Override
	public String getFormXml(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return workService.getFormXml(request.getParameter("formId"), request.getParameter("workId"));
	}

	@Override
	public SwdRecord getRecord(HttpServletRequest request) throws Exception {
		return workService.getRecord(request);
	}

	@Override
	public SwdRecord refreshDataFields(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return instanceService.refreshDataFields(requestBody, request);
	}

	@Override
	public String setWorkSearchFilter(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return workService.setWorkSearchFilter(requestBody, request);
	}

	@Override
	public void removeWorkSearchFilter(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		workService.removeWorkSearchFilter(requestBody, request);
	}

	@Override
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		docFileService.downloadFile(request, response);

	}

	@Override
	public void uploadTempFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		docFileService.uploadTempFile(request, response);
	}

	@Override
	public void uploadYTVideo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		docFileService.uploadYTVideo(request, response);
	}

	@Override
	public MailFolder[] getMailFoldersById(String folderId) throws Exception {
		return mailService.getMailFoldersById(folderId);
	}

	@Override
	public InstanceInfoList getMailInstanceList(String folderId, RequestParams params) throws Exception {
		return mailService.getMailInstanceList(folderId, params);
	}

	@Override
	public MailInstance getMailInstanceById(String folderId, String msgId, int sendType) throws Exception {
		return mailService.getMailInstanceById(folderId, msgId, sendType);
	}

	@Override
	public RequestParams setInstanceListParams(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return workService.setInstanceListParams(requestBody, request);
	}

	@Override
	public void addAFavoriteWork(HttpServletRequest request) throws Exception {
		workService.addAFavoriteWork(request.getParameter("workId"));
	}

	@Override
	public void removeAFavoriteWork(HttpServletRequest request) throws Exception {
		workService.removeAFavoriteWork(request.getParameter("workId"));		
	}

	@Override
	public void addAFavoriteCommunity(HttpServletRequest request) throws Exception {
		communityService.addAFavoriteCommunity(request.getParameter("comId"));
	}

	@Override
	public void removeAFavoriteCommunity(HttpServletRequest request) throws Exception {
		communityService.removeAFavoriteCommunity(request.getParameter("comId"));		
	}

	@Override
	public CompanyGeneral getCompanyGeneral() throws Exception {
		return settingsService.getCompanyGeneral();
	}

	@Override
	public void setCompanyGeneral(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		settingsService.setCompanyGeneral(requestBody, request);
	}
	
	@Override
	public RecordList getWorkHourPolicyList(RequestParams params) throws Exception {
		return settingsService.getWorkHourPolicyList(params);
	}
	
	@Override
	public WorkHourPolicy getWorkHourPolicyById(String id) throws Exception {
		return settingsService.getWorkHourPolicyById(id);
	}
	
	@Override
	public void setWorkHourPolicy(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		settingsService.setWorkHourPolicy(requestBody, request);
	}
	
	@Override
	public void removeWorkHourPolicy(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		settingsService.removeWorkHourPolicy(requestBody, request);
	}
	
	@Override
	public RecordList getCompanyEventList(RequestParams params) throws Exception {
		return settingsService.getCompanyEventList(params);
	}
	
	@Override
	public CompanyEvent getCompanyEventById(String id) throws Exception {
		return settingsService.getCompanyEventById(id);
	}
	
	@Override
	public void setCompanyEvent(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		settingsService.setCompanyEvent(requestBody, request);
	}
		
	@Override
	public void removeCompanyEvent(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		settingsService.removeCompanyEvent(requestBody, request);
	}
		
	@Override
	public RecordList getApprovalLineList(RequestParams params) throws Exception {
		return settingsService.getApprovalLineList(params);
	}
	
	@Override
	public ApprovalLine getApprovalLineById(String id) throws Exception {
		return settingsService.getApprovalLineById(id);
	}
	
	@Override
	public void setApprovalLine(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		settingsService.setApprovalLine(requestBody, request);
	}
		
	@Override
	public void removeApprovalLine(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		settingsService.removeApprovalLine(requestBody, request);
	}
		
	@Override
	public RecordList getWebServiceList(RequestParams params) throws Exception {
		return settingsService.getWebServiceList(params);
	}
	
	@Override
	public WebService getWebServiceById(String id) throws Exception {
		return settingsService.getWebServiceById(id);
	}
	
	@Override
	public void setWebService(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		settingsService.setWebService(requestBody, request);
	}
		
	@Override
	public void removeWebService(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		settingsService.removeWebService(requestBody, request);
	}
	
	@Override
	public WSDLDetail getWsdlDetailFromUri(String wsdlUri) throws Exception {
		return settingsService.getWsdlDetailFromUri(wsdlUri);
	}
	@Override
	public RecordList getExternalFormList(RequestParams params) throws Exception {
		return settingsService.getExternalFormList(params);
	}
	
	@Override
	public ExternalForm getExternalFormById(String id) throws Exception {
		return settingsService.getExternalFormById(id);
	}
	
	@Override
	public void setExternalForm(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		settingsService.setExternalForm(requestBody, request);
	}
		
	@Override
	public void removeExternalForm(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		settingsService.removeExternalForm(requestBody, request);
	}
		
	@Override
	public void setMember(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		settingsService.setMember(requestBody, request);
	}
		
	@Override
	public void removeMember(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		settingsService.removeMember(requestBody, request);
	}
		
	@Override
	public void setDepartment(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		settingsService.setDepartment(requestBody, request);
	}
		
	@Override
	public void removeDepartment(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		settingsService.removeDepartment(requestBody, request);
	}

	@Override
	public void checkIdDuplication(HttpServletRequest request) throws Exception {
		settingsService.checkIdDuplication(request);
	}

	@Override
	public String performTaskInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		String instanceId = instanceService.performTaskInstance(requestBody, request);
		return instanceId;
	}

	@Override
	public String returnTaskInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		String instanceId = instanceService.returnTaskInstance(requestBody, request);
		return instanceId;
	}

	@Override
	public String reassignTaskInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		String instanceId = instanceService.reassignTaskInstance(requestBody, request);
		return instanceId;
	}

	@Override
	public String tempSaveTaskInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		String instanceId = instanceService.tempSaveTaskInstance(requestBody, request);
		return instanceId;
	}

	@Override
	public void startWorkService(String workId) throws Exception {
		builderService.startWorkService(workId);
	}
		
	@Override
	public void stopWorkService(String workId) throws Exception {
		builderService.stopWorkService(workId);
	}
		
	@Override
	public void startWorkEditing(String workId) throws Exception {
		builderService.startWorkEditing(workId);
	}
		
	@Override
	public void stopWorkEditing(String workId) throws Exception {
		builderService.stopWorkEditing(workId);
	}
	
	@Override
	public void setWorkSettings(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		builderService.setWorkSettings(requestBody, request);
	}
	
	@Override
	public void publishWorkToStore(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		builderService.publishWorkToStore(requestBody, request);
	}

	@Override
	public void createNewCategory(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		builderService.createNewCategory(requestBody, request);
	}
	
	@Override
	public void setCategory(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		builderService.setCategory(requestBody, request);
	}

	@Override
	public void removeCategory(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		builderService.removeCategory(requestBody, request);
	}

	@Override
	public void createNewWorkDefinition(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		builderService.createNewWorkDefinition(requestBody, request);
	}

	@Override
	public void setWorkDefinition(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		builderService.setWorkDefinition(requestBody, request);
	}

	@Override
	public String addCommentOnWork(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return instanceService.addCommentOnWork(requestBody, request);
	}

	@Override
	public void updateCommentOnWork(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		instanceService.updateCommentOnWork(requestBody, request);
	}

	@Override
	public void removeCommentFromWork(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		instanceService.removeCommentFromWork(requestBody, request);
	}

	@Override
	public String addCommentOnInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return instanceService.addCommentOnInstance(requestBody, request);
	}

	@Override
	public void updateCommentOnInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		instanceService.updateCommentOnInstance(requestBody, request);
	}

	@Override
	public void removeCommentFromInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		instanceService.removeCommentFromInstance(requestBody, request);
	}
	@Override
	public String removeMission(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return seraService.removeMission(requestBody, request);
	}
	@Override
	public String modifyMission(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return seraService.modifyMission(requestBody, request);
	}
	@Override
	public String createNewMission(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return seraService.createNewMission(requestBody, request);
	}
	@Override
	public String createNewCourse(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return seraService.createNewCourse(requestBody, request);
	}
	@Override
	public String setCourseProfile(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return seraService.setCourseProfile(requestBody, request);
	}
	@Override
	public String removeCourse(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return seraService.removeCourse(requestBody, request);
	}
	@Override
	public String performMissionReport(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return seraService.performMissionReport(requestBody, request);
	}
	@Override
	public String setSeraNote(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return seraService.setSeraNote(requestBody, request);
	}
	@Override
	public Team createNewTeam(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return seraService.createNewTeam(requestBody, request);
	}
	@Override
	public void modifyCourseTeam(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		seraService.modifyCourseTeam(requestBody, request);
	}
	@Override
	public void removeCourseTeam(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		seraService.removeCourseTeam(requestBody, request);
	}
	@Override
	public String updateSeraProfile(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return seraService.updateSeraProfile(requestBody, request);
	}
	
	@Override
	public CourseList getCoursesById(String userId, int maxList) throws Exception {
		return seraService.getCoursesById(userId, maxList);
	}
	
	@Override
	public CourseInfo[] getCoursesById(String userId, int courseType, LocalDate fromDate, int maxList) throws Exception {
		return seraService.getCoursesById(userId, courseType, fromDate, maxList);
	}
	
	@Override
	public Course getCourseById(String courseId) throws Exception {
		return seraService.getCourseById(courseId);
	}
	
	@Override
	public Mentor getMentorById(String mentorId) throws Exception {
		return seraService.getMentorById(mentorId);
	}

	@Override
	public FriendList getFriendsById(String userId, int maxList) throws Exception {
		return seraService.getFriendsById(userId, maxList);
	}
	
	@Override
	public SeraUserInfo[] getFriendsById(String userId, String lastId, int maxList, String key) throws Exception {
		return seraService.getFriendsById(userId, lastId, maxList, key);
	}

	@Override
	public SeraUserInfo[] getFriendRequestsByUserId(String userId, String lastId, int maxList) throws Exception {
		return seraService.getFriendRequestsByUserId(userId, lastId, maxList);
	}
	
	@Override
	public InstanceInfo[] getCourseNotices(String courseId, LocalDate fromDate, int maxList) throws Exception {
		return seraService.getCourseNotices(courseId, fromDate, maxList);
	}

	@Override
	public FormUploadToken getUploadToken(YTMetaInfo metaInfo, String ytUserId, String ytPassword) throws Exception{
		return youTubeService.getUploadToken(metaInfo, ytUserId, ytPassword);
	}
	
	@Override
	public InstanceInfo[] getSeraInstances(int type, String userId, String courseId, String missionId, String teamId, LocalDate fromDate, int maxList) throws Exception {
		return seraService.getSeraInstances(type, userId, courseId, missionId, teamId, fromDate, maxList);
	}

	@Override
	public void addReviewOnCourse(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		seraService.addReviewOnCourse(requestBody, request);
	}

	@Override
	public ReviewInstanceInfo[] getReviewInstancesByCourse(String courseId, LocalDate fromDate, int maxList) throws Exception {
		return seraService.getReviewInstancesByCourse(courseId, fromDate, maxList);
	}

	@Override
	public void joinGroupRequest(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		communityService.joinGroupRequest(requestBody, request);
	}

	@Override
	public void inviteGroupMembers(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		communityService.inviteGroupMembers(requestBody, request);		
	}

	@Override
	public void approvalJoinGroup(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		communityService.approvalJoinGroup(requestBody, request);		
	}

	@Override
	public void pushoutGroupMember(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		communityService.pushoutGroupMember(requestBody, request);		
	}

	@Override
	public void leaveGroup(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		communityService.leaveGroup(requestBody, request);		
	}

	@Override
	public MissionInstanceInfo[] getMissionInstanceList(String courseId, LocalDate fromDate, LocalDate toDate) throws Exception {
		return seraService.getMissionInstanceList(courseId, fromDate, toDate);
	}

	@Override
	public MissionInstance getMissionById(String missionId) throws Exception {
		return seraService.getMissionById(missionId);
	}

	@Override
	public SeraUser getSeraUserById(String userId) throws Exception {
		return seraService.getSeraUserById(userId);
	}

	@Override
	public void replyFriendRequest(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		seraService.replyFriendRequest(requestBody, request);
	}

	@Override
	public void friendRequest(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		seraService.friendRequest(requestBody, request);
	}

	@Override
	public void destroyFriendship(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		seraService.destroyFriendship(requestBody, request);
		
	}

	@Override
	public CourseInfo[] getCoursesByType(int courseType, String lastId, int maxList) throws Exception {
		return seraService.getCoursesByType(courseType, lastId, maxList);
	}

	@Override
	public void addLikeToInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		instanceService.addLikeToInstance(requestBody, request);
	}

	@Override
	public void removeLikeFromInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		instanceService.removeLikeFromInstance(requestBody, request);
	}

	@Override
	public void createAsyncMessage(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		instanceService.createAsyncMessage(requestBody, request);
	}
	
	@Override
	public void removeAsyncMessage(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		instanceService.removeAsyncMessage(requestBody, request);
	}

	@Override
	public void setAsyncMessage(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		instanceService.setAsyncMessage(requestBody, request);
	}
	
	public CommentInstanceInfo[] getSubInstancesByRefId(String refId, int maxSize) throws Exception {
		return seraService.getSubInstancesByRefId(refId, maxSize);
	}

	@Override
	public String createSeraUser(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return seraService.createSeraUser(requestBody, request);
	}
	@Override
	public String leaveSeraUser(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return seraService.leaveSeraUser(requestBody, request);
	}

	@Override
	public CourseInfo[] getCoursesByCategory(String categoryName, String lastId, int maxList) throws Exception {
		return seraService.getCoursesByCategory(categoryName, lastId, maxList);
	}

	@Override
	public MenteeInformList getCoursesMenteeInformations(String courseId, int maxList) throws Exception {
		return seraService.getCourseMenteeInformations(courseId, maxList);
	}

	@Override
	public SeraUserInfo[] getCourseMenteeInformsByType(int type, String courseId, String lastId, int maxList) throws Exception {
		return seraService.getCourseMenteeInformsByType(type, courseId, lastId, maxList);
	}

	@Override
	public FriendInformList getMyFriendInformations(int maxList) throws Exception {
		return seraService.getMyFriendInformations(maxList);
	}

	@Override
	public SeraUserInfo[] getFriendInformsByType(int type, String userId, String lastId, int maxList) throws Exception {
		return seraService.getFriendInformsByType(type, userId, lastId, maxList);
	}

	@Override
	public AsyncMessageList getMyMessageInstancesByType(int type, int maxSize) throws Exception {
		return instanceService.getMyMessageInstancesByType(type, maxSize);
	}

	@Override
	public AsyncMessageInstanceInfo[] getMyMessageInstancesByType(AsyncMessageList asyncMessageList, int type, LocalDate fromDate, int maxSize) throws Exception {
		return instanceService.getMyMessageInstancesByType(null, type, fromDate, maxSize);
	}

	@Override
	public void removeSeraInstane(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		int workType = (Integer)requestBody.get("workType");
		switch (workType) {
		case Work.TYPE_ASYNC_MESSAGE:
			instanceService.removeAsyncMessage(requestBody, request);
			break;
		default:
			seraService.removeSeraInstane(requestBody, request);
			break;
		}
	}

	@Override
	public Notice[] getSeraNoticesForMe() throws Exception {
		return seraService.getSeraNoticesForMe();
	}

	@Override
	public ChatInstanceInfo[] fetchAsyncMessagesByChatid(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return instanceService.fetchAsyncMessagesByChatid(request, response);
	}

	@Override
	public SeraUserInfo[] searchSeraUserByType(int type, String userId, String key) throws Exception {
		return seraService.searchSeraUserByType(type, userId, key);
	}

	@Override
	public SeraUserInfo[] searchCourseMemberByType(int type, String courseId, String key) throws Exception {
		return seraService.searchCourseMemberByType(type, courseId, key);
	}

	@Override
	public CourseInfo[] searchCourseByType(int type, String key) throws Exception {
		return seraService.searchCourseByType(type, key);
	}

	@Override
	public CourseInfo[] searchCourseByCategory(String categoryName, String key) throws Exception {
		return seraService.searchCourseByCategory(categoryName, key);
	}

	@Override
	public CourseInfo[] getCoursesByUser(String userId, int courseType, String lastId, int maxList) throws Exception {
		return seraService.getCoursesByUser(userId, courseType, lastId, maxList);
	}

	@Override
	public TeamInfo[] getTeamsByCourse(String courseId) throws Exception {
		return seraService.getTeamsByCourse(courseId);
	}

	@Override
	public Team getMyTeamByCourse(String courseId) throws Exception {
		return seraService.getMyTeamByCourse(courseId);
	}

	@Override
	public Team getTeamById(String teamId) throws Exception {
		return seraService.getTeamById(teamId);
	}

	@Override
	public MemberInformList getTeamMemberInformations(String teamId, int maxList) throws Exception {
		return seraService.getTeamMemberInformations(teamId, maxList);
	}

	@Override
	public SeraUserInfo[] getTeamMemberInformsByType(int type, String teamId, String lastId, int maxList) throws Exception {
		return seraService.getTeamMemberInformsByType(type, teamId, lastId, maxList);
	}

	@Override
	public SeraUserInfo[] searchTeamMemberByType(int type, String courseId, String teamId, String key) throws Exception {
		return seraService.searchTeamMemberByType(type, courseId, teamId, key);
	}

	@Override
	public SeraBoardList getSeraBoards(int maxList) throws Exception {
		return seraService.getSeraBoards(maxList);
	}

	@Override
	public String setMentorProfile(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return seraService.setMentorProfile(requestBody, request);
	}

	@Override
	public MailFolder getMailFolderById(String folderId) throws Exception {
		return mailService.getMailFolderById(folderId);
	}

	@Override
	public Team getJoinRequestTeamByCourseId(String courseId) throws Exception {
		return seraService.getJoinRequestTeamByCourseId(courseId);
	}

	@Override
	public void replyTeamJoinRequest(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		seraService.replyTeamJoinRequest(requestBody, request);
	}

	@Override
	public void teamJoinRequest(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		seraService.teamJoinRequest(requestBody, request);
	}

	@Override
	public void leaveTeam(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		seraService.leaveTeam(requestBody, request);
	}

	@Override
	public void destroyMembership(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		seraService.destroyMembership(requestBody, request);
	}

	@Override
	public BoardInstanceInfo[] getSeraTrends(int maxList) throws Exception {
		return seraService.getSeraTrends(maxList);
	}

	@Override
	public CourseAdList getCourseAds(int maxList) throws Exception {
		return seraService.getCourseAds(maxList);
	}

	@Override
	public GlobalSearchList searchGlobal(String key, int maxCourseList, int maxUserList) throws Exception {
		return seraService.searchGlobal(key, maxCourseList, maxUserList);
	}

	@Override
	public CourseInfo[] searchCourses(String key, String lastId, int maxList) throws Exception {
		return seraService.searchCourses(null, key, lastId, maxList);
	}

	@Override
	public SeraUserInfo[] searchSeraUsers(String key, String lastId, int maxList) throws Exception {
		return seraService.searchSeraUsers(null, key, lastId, maxList);
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		loginService.logout(request, response);
	}

	@Override
	public void sendMail(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		mailService.sendMail(requestBody, request);
	}

	@Override
	public UserInfo[] searchEmailAddress(String key) throws Exception {
		return communityService.searchEmailAddress(key);
	}

	@Override
	public CommunityInfo[] getAllComsByDepartmentId(String departmentId, boolean departmentOnly) throws Exception {
		return communityService.getAllComsByDepartmentId(departmentId, departmentOnly);
	}

	@Override
	public CommunityInfo[] getAllComsByGroupId(String groupId) throws Exception {
		return communityService.getAllComsByGroupId(groupId);
	}

	@Override
	public CommunityInfo[] getAllComsByCategoryId(String categoryId) throws Exception {
		return communityService.getAllComsByCategoryId(categoryId);
	}

	@Override
	public void saveMailAsDraft(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		mailService.saveMailAsDraft(requestBody, request);
	}

	@Override
	public void moveMails(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		mailService.moveMails(requestBody, request);
	}

	@Override
	public void deleteMails(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		mailService.deleteMails(requestBody, request);
	}

	@Override
	public void newMailFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		mailService.newMailFolder(requestBody, request);
	}

	@Override
	public void setMailFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		mailService.setMailFolder(requestBody, request);
	}

	@Override
	public void deleteMailFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		mailService.deleteMailFolder(requestBody, request);
	}

	@Override
	public String getFolderIdByType(int folderType) throws Exception {
		return mailService.getFolderIdByType(folderType);
	}

	@Override
	public void checkEmail() throws Exception {
		mailService.checkEmail();
	}

	@Override
	public int getUnreadEmails() throws Exception {
		return mailService.getUnreadEmails();
	}

	@Override
	public void commentOnTaskForward(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		instanceService.commentOnTaskForward(requestBody, request);
	}

	@Override
	public ApprovalLineInst getApprovalLineInstById(String instId) throws Exception {
		return instanceService.getApprovalLineInstById(instId);
	}

	@Override
	public void commentOnTaskApproval(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		instanceService.commentOnTaskApproval(requestBody, request);
	}

	@Override
	public RecordList getEmailServerList(RequestParams params) throws Exception {
		return settingsService.getEmailServerList(params);
	}

	@Override
	public EmailServer getEmailServerById(String id) throws Exception {
		return settingsService.getEmailServerById(id);
	}

	@Override
	public void setEmailServer(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		settingsService.setEmailServer(requestBody, request);
	}

	@Override
	public void removeEmailServer(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		settingsService.removeEmailServer(requestBody, request);
	}

	@Override
	public EmailServer[] getEmailServers() throws Exception {
		return settingsService.getEmailServers();
	}

	@Override
	public ConnectionProfile[] getMailConnectionProfiles() throws Exception {
		return settingsService.getMailConnectionProfiles();
	}

	@Override
	public MailAccount[] getMyMailAccounts() throws Exception {
		return communityService.getMyMailAccounts();
	}

	@Override
	public void removeWorkDefinition(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		builderService.removeWorkDefinition(requestBody, request);
	}
	@Override
	public String copyWorkDefinition(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return builderService.copyWorkDefinition(requestBody, request);
	}

	@Override
	public String moveWorkDefinition(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return builderService.moveWorkDefinition(requestBody, request);
	}

	@Override
	public void forwardPworkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		instanceService.forwardPworkInstance(requestBody, request);
	}

	@Override
	public void approvalPworkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		instanceService.approvalPworkInstance(requestBody, request);
	}

	@Override
	public EventInstanceInfo[] getCommingEventInstances(String spaceId, int maxLength) throws Exception {
		return instanceService.getCommingEventInstances(spaceId, maxLength);
	}

	@Override
	public BoardInstanceInfo[] getRecentBoardInstances(String spaceId, int maxLength) throws Exception {
		return instanceService.getRecentBoardInstances(spaceId, maxLength);
	}

	@Override
	public UserInfo[] searchCommunityNonMember(String communityId, String key) throws Exception {
		return communityService.searchCommunityNonMember(communityId, key);
	}

	@Override
	public void updateGroupSetting(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		communityService.updateGroupSetting(requestBody, request);
	}

	@Override
	public GroupMemberList getGroupMemberInformList(String groupId) throws Exception {
		return communityService.getGroupMemberInformList(groupId);
	}

	@Override
	public UserInfo[] getGroupMembersById(String groupId, String lastId, int maxSize) throws Exception {
		return communityService.getGroupMembersById(groupId, lastId, maxSize);
	}

	@Override
	public void updateDepartmentSetting(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		communityService.updateDepartmentSetting(requestBody, request);
	}

	@Override
	public boolean canIUploadToWorkSpace(String workSpaceId, String workId) throws Exception {
		return communityService.canIUploadToWorkSpace(workSpaceId, workId);
	}

	@Override
	public void createNewFileFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		instanceService.createNewFileFolder(requestBody, request);
	}

	@Override
	public void setFileFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		instanceService.setFileFolder(requestBody, request);
	}

	@Override
	public void removeFileFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		instanceService.removeFileFolder(requestBody, request);
	}

	@Override
	public void createNewImageFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		instanceService.createNewImageFolder(requestBody, request);
	}

	@Override
	public void setImageFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		instanceService.setImageFolder(requestBody, request);
	}

	@Override
	public void removeImageFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		instanceService.removeImageFolder(requestBody, request);
	}

	@Override
	public void removeImageInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		instanceService.removeImageInstance(requestBody, request);
	}

	@Override
	public void moveFileInstances(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		instanceService.moveFileInstances(requestBody, request);
	}

	@Override
	public void moveImageInstances(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		instanceService.moveImageInstances(requestBody, request);
	}

	@Override
	public void setIWorkManual(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		workService.setIWorkManual(requestBody, request);
	}

	@Override
	public void setPWorkManual(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		workService.setPWorkManual(requestBody, request);
	}
		
	public ImageInstance getImageInstanceById(String instId) throws Exception {
		return instanceService.getImageInstanceById(instId);
	}

	@Override
	public boolean authenticateEmailAccount(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return mailService.authenticateEmailAccount(requestBody, request);
	}

	@Override
	public int uploadExcelToWork(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return docFileService.uploadExcelToWork(requestBody, request);
	}

	@Override
	public void addAdjunctMember(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		settingsService.addAdjunctMember(requestBody, request);
	}

	@Override
	public UserInfo[] getAllUsersByDepartmentId(String departmentId) throws Exception {
		return communityService.getAllUsersByDepartmentId(departmentId);
	}

	@Override
	public String createMailContent(String companyId, String emailId, byte[] content, Date receivedDate) throws DocFileException {
		return docFileService.createMailContent(companyId, emailId, content, receivedDate);
	}

	@Override
	public byte[] readMailContent(String fileId, Date receivedDate) throws DocFileException {
		return docFileService.readMailContent(fileId, receivedDate);
	}

	@Override
	public void deleteMailContent(String fileId, Date receivedDate) throws DocFileException {
		docFileService.deleteMailContent(fileId, receivedDate);
	}

	@Override
	public InstanceInfoList getUpdateHistoryList(String instanceId, RequestParams params) throws Exception {
		return instanceService.getUpdateHistoryList(instanceId, params);
	}

	@Override
	public InstanceInfoList getDownloadHistoryList(String instanceId, String taskInstanceId, RequestParams params) throws Exception {
		return instanceService.getDownloadHistoryList(instanceId, taskInstanceId, params);
	}

	@Override
	public InstanceInfoList getRelatedWorkList(String instanceId, RequestParams params) throws Exception {
		return instanceService.getRelatedWorkList(instanceId, params);
	}

	@Override
	public InstanceInfoList getForwardHistoryList(String instanceId, RequestParams params) throws Exception {
		return instanceService.getForwardHistoryList(instanceId, params);
	}

	@Override
	public InstanceInfoList getForwardTasksById(String forwardId, RequestParams params) throws Exception {
		return instanceService.getForwardTasksById(forwardId, params);
	}

	@Override
	public MailFolder[] getMailFolders() throws Exception {
		return mailService.getMailFolders();
	}

	@Override
	public SwdRecord getRecordByKeyValue(String workId, String keyValue) throws Exception {
		return workService.getRecordByKeyValue(workId, keyValue);
	}

	@Override
	public TaskInstance getTaskInstanceById(String workId, String taskInstId) throws Exception {
		return instanceService.getTaskInstanceById(workId, taskInstId);
	}

	@Override
	public SwdRecord getRecord(String workId, String recordId, String taskInstId) throws Exception {
		return workService.getRecord(workId, recordId, taskInstId);
	}

	@Override
	public SmartForm getFormById(String formId, String workId) throws Exception {
		return workService.getFormById(formId, workId);
	}

	@Override
	public String abendTaskInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return instanceService.abendTaskInstance(requestBody, request);
	}

	@Override
	public InstanceInfoList getAllUcityPWorkInstanceList(boolean runningOnly, RequestParams params, int auditId) throws Exception {
		return instanceService.getAllUcityPWorkInstanceList(runningOnly, params, auditId);
	}

	@Override
	public String createTaskInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return instanceService.createTaskInstance(requestBody, request);
	}

	@Override
	public String getUcityChartXml(String categoryName, String periodName, String serviceName, String eventName) throws Exception {
		return instanceService.getUcityChartXml(categoryName, periodName, serviceName, eventName);
	}
	
	@Override
	public void getUcityChartExcel(String categoryName, String periodName, String serviceName, String eventName, HttpServletRequest request, HttpServletResponse response) throws Exception {
		instanceService.getUcityChartExcel(categoryName, periodName, serviceName, eventName, request, response);
	}

	@Override
	public int[][] getUcityAuditTaskCounts(boolean runningOnly) throws Exception {
		return instanceService.getUcityAuditTaskCounts(runningOnly);
	}

	@Override
	public Property[] getUcityExtendedPropertyByTaskInstId(String taskInstId) throws Exception {
		return instanceService.getUcityExtendedPropertyByTaskInstId(taskInstId);
	}

	@Override
	public void changeMailPasswordRequest(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		mailService.changeMailPasswordRequest(requestBody, request);
	}

	@Override
	public DepartmentInfo[] searchDepartment(String key, HttpServletRequest request) throws Exception {
		return communityService.searchDepartment(key, request);
	}

	@Override
	public void removeProcessWorkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		instanceService.removeProcessWorkInstance(requestBody, request);		
	}

	@Override
	public void addJunk(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		mailService.addJunk(requestBody, request);
	}

	@Override
	public void removeJunk(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		mailService.removeJunk(requestBody, request);
	}

	@Override
	public String[][] getJunkIds() throws Exception {
		return mailService.getJunkIds();
	}

	@Override
	public WorkInfoList getAppWorkList(RequestParams params) throws Exception {
		return workService.getAppWorkList(params);
	}

	@Override
	public void downloadAppWork(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		builderService.downloadAppWork(requestBody, request);
	}

	@Override
	public int getSubInstancesInForwardCount(String forwardId) throws Exception {
		return instanceService.getSubInstancesInForwardCount(forwardId);
	}

	@Override
	public TaskInstanceInfo[] getSubInstancesInForward(String forwardId, int length, LocalDate to) throws Exception {
		return instanceService.getSubInstancesInForward(forwardId, length, to);
	}

	@Override
	public CommunityInfoList getCommunityInstanceList(int type, RequestParams params) throws Exception {
		return communityService.getCommunityInstanceList(type, params);
	}

	@Override
	public String setWorkReport(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return reportService.setWorkReport(requestBody, request);
	}

	@Override
	public String setWorkReportPane(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return reportService.setWorkReportPane(requestBody, request);
	}

	@Override
	public int getUserReportCount(String targetWorkId) throws Exception {
		return reportService.getUserReportCount(targetWorkId);
	}
	@Override
	public InstanceInfoList getReportInstanceList(String targetWorkId, int targetWorkType, String producedBy, RequestParams params) throws Exception {
		return reportService.getReportInstanceList(targetWorkId, targetWorkType, producedBy, params);
	}
	@Override
	public ReportPane[] getMyDashboard() throws Exception {
		return reportService.getMyDashboard();
	}
}
