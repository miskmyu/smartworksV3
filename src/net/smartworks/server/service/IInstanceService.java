package net.smartworks.server.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.smartworks.model.approval.ApprovalLineInst;
import net.smartworks.model.instance.ImageInstance;
import net.smartworks.model.instance.Instance;
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
import net.smartworks.server.engine.common.model.Property;
import net.smartworks.server.engine.infowork.domain.model.SwdRecord;
import net.smartworks.server.engine.process.task.model.TskTask;
import net.smartworks.util.LocalDate;

public interface IInstanceService {

	public BoardInstanceInfo[] getMyRecentBoardInstances() throws Exception;

	public BoardInstanceInfo[] getCommunityRecentBoardInstances(String workSpaceId) throws Exception;

	public InstanceInfo[] getMyRecentInstances() throws Exception;

	public TaskInstanceInfo getTaskInstanceById(String taskInstId) throws Exception;

	public InstanceInfo[] getMyRunningInstances(LocalDate lastInstanceDate, int requestSize, boolean assignedOnly, boolean runningOnly) throws Exception;

	public InstanceInfo[] getMyRunningInstances(LocalDate lastInstanceDate, int requestSize, boolean assignedOnly, boolean runningOnly, RequestParams params) throws Exception;
	
	public RunningCounts getMyRunningInstancesCounts() throws Exception;

	public InstanceInfo[] searchMyRunningInstance(String key) throws Exception;

	public CommentInstanceInfo[] getRecentCommentsInWorkManual(String workId, int length) throws Exception;

	public InstanceInfo[] getRecentSubInstancesInInstance(String workId, int length) throws Exception;

	public int getSubInstanceCountInInstance(String workId) throws Exception;

	public InstanceInfo[] getSubInstancesInInstance(String workId, int length, LocalDate to) throws Exception;

	public int getSubInstancesInForwardCount(String forwardId) throws Exception;

	public TaskInstanceInfo[] getSubInstancesInForward(String forwardId, int length, LocalDate to) throws Exception;

	public InstanceInfoList getIWorkInstanceList(String workId, RequestParams params) throws Exception;

	public InstanceInfoList getIWorkInstanceListByFormId(String formId, RequestParams params) throws Exception;

	public InstanceInfoList getAllUcityPWorkInstanceList(boolean runningOnly, RequestParams params, int auditId) throws Exception;
	
	public InstanceInfoList getAllPWorkInstanceList(boolean runningOnly, RequestParams params) throws Exception;
	
	public InstanceInfoList getPWorkInstanceList(String workId, RequestParams params) throws Exception;

	public InstanceInfoList getWorkInstanceList(String workSpaceId, RequestParams params) throws Exception;

	public InstanceInfoList getSavedInstanceList(String workSpaceId, RequestParams params) throws Exception;

	public InstanceInfoList getInstanceInfoListByWorkId(String workSpaceId, RequestParams params, String workId) throws Exception;

	public InstanceInfoList getImageInstanceList(String workSpaceId, RequestParams params) throws Exception;

	public ImageInstanceInfo[] getImageInstancesByDate(int displayBy, String wid, String parentId, LocalDate lastDate, int maxCount) throws Exception;

	public InstanceInfoList getFileInstanceList(String workSpaceId, RequestParams params) throws Exception;

	public EventInstanceInfo[] getEventInstanceList(String workSpaceId, LocalDate fromDate, LocalDate toDate) throws Exception;

	public InstanceInfoList getMemoInstanceList(String workSpaceId, RequestParams params) throws Exception;

	public InstanceInfoList getBoardInstanceList(String workSpaceId, RequestParams params) throws Exception;

	public WorkInstance getWorkInstanceById(int workType, String workId, String instanceId) throws Exception;

	public WorkInstance getSavedWorkInstanceById(int workType, String workId, String instanceId) throws Exception;

	public TaskInstanceInfo[][] getTaskInstancesByWorkHours(String contextId, String spaceId, LocalDate date, int maxSize) throws Exception;

	public TaskInstanceInfo[][] getTaskInstancesByDates(String contextId, String spaceId, LocalDate fromDate, LocalDate toDate, int maxSize) throws Exception;

	public TaskInstanceInfo[][] getTaskInstancesByWeeks(String contextId, String spaceId, LocalDate month, int maxSize) throws Exception;

	public TaskInstanceInfo[] getTaskInstancesByDate(String contextId, String spaceId, LocalDate fromDate, LocalDate toDate, int maxSize) throws Exception;

	public TaskInstanceInfo[] getTaskInstancesByTimeline(String contextId, String spaceId, LocalDate fromDate, int maxSize) throws Exception;

	public TaskInstanceInfo[] getCastTaskInstancesByDate(LocalDate fromDate, int maxSize) throws Exception;

	public TaskInstanceInfo[] getInstanceTaskHistoriesById(String instId) throws Exception;

	public InstanceInfoList[] getInstanceRelatedWorksById(String instId) throws Exception;

	public InstanceInfo[] getSpaceInstancesByDate(String spaceId, LocalDate fromDate, int maxSize) throws Exception;
	
	public String setInformationWorkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public void removeInformationWorkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public void forwardIworkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public void approvalIworkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public void forwardPworkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public void approvalPworkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public String startProcessWorkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public void removeProcessWorkInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public String setFileInstance(HttpServletRequest request) throws Exception;

	public String setEventInstance(HttpServletRequest request) throws Exception;
	
	public String setMemoInstance(HttpServletRequest request) throws Exception;
	
	public SwdRecord refreshDataFields(SwdRecord record) throws Exception;

	public SwdRecord refreshDataFields(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public String setBoardInstance(HttpServletRequest request) throws Exception;
	
	public String createTaskInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public String performTaskInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public String returnTaskInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public String reassignTaskInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public String abendTaskInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public String tempSaveTaskInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public String addCommentOnWork(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public void updateCommentOnWork(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public void removeCommentFromWork(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public String addCommentOnInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public void updateCommentOnInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public void removeCommentFromInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public void addLikeToInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public void removeLikeFromInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public AsyncMessageList getMyMessageInstancesByType(int type, int maxSize) throws Exception;

	public AsyncMessageInstanceInfo[] getMyMessageInstancesByType(AsyncMessageList asyncMessageList, int type, LocalDate fromDate, int maxSize) throws Exception;

	public void createAsyncMessage(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public void removeAsyncMessage(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public void setAsyncMessage(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public ChatInstanceInfo[] fetchAsyncMessagesByChatid(HttpServletRequest request, HttpServletResponse response) throws Exception;

	public void commentOnTaskForward(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public void commentOnTaskApproval(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public ApprovalLineInst getApprovalLineInstById(String instId) throws Exception;
	
	public EventInstanceInfo[] getCommingEventInstances(String spaceId, int maxLength) throws Exception;
	
	public BoardInstanceInfo[] getRecentBoardInstances(String spaceId, int maxLength) throws Exception;
	
	public void createNewFileFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public void setFileFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public void removeFileFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public void createNewImageFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public void setImageFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public void removeImageFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public void removeImageInstance(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public void moveFileInstances(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public void moveImageInstances(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public ImageInstance getImageInstanceById(String instId) throws Exception;
	
	public InstanceInfoList getUpdateHistoryList(String instanceId, RequestParams params) throws Exception;

	public InstanceInfoList getForwardHistoryList(String instanceId, RequestParams params) throws Exception;

	public InstanceInfoList getDownloadHistoryList(String instanceId, String taskInstanceId, RequestParams params) throws Exception;
	
	public InstanceInfoList getRelatedWorkList(String instanceId, RequestParams params) throws Exception;
	
	public InstanceInfoList getForwardTasksById(String forwardId, RequestParams params) throws Exception;
	
	public TaskInstance getTaskInstanceById(String workId, String taskInstId) throws Exception;
	
	//외부화면실행기
	
	public String initiateProcessByExternalFormInfo(String user,String title, String processId, Property[] props) throws Exception;
	
	public TskTask executeTaskByExternalFormInfo(String user, String action, String taskId, Property[] props) throws Exception;
	
	public TskTask executeInstanceByUserIdAndExternalFormInfo(String user, String action, String instanceId, Property[] props) throws Exception;
		
	
	public int[][] getUcityAuditTaskCounts(boolean runningOnly) throws Exception;
	
	public String getUcityChartXml(String categoryName, String periodName, String serviceName, String eventName) throws Exception;

	public void getUcityChartExcel(String categoryName, String periodName, String serviceName, String eventName, HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	public Property[] getUcityExtendedPropertyByTaskInstId(String taskInstId) throws Exception;
	
	
	
	
	
	
	
}
