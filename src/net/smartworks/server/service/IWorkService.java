package net.smartworks.server.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.smartworks.model.filter.SearchFilter;
import net.smartworks.model.instance.info.RequestParams;
import net.smartworks.model.work.SmartForm;
import net.smartworks.model.work.Work;
import net.smartworks.model.work.info.FileCategoryInfo;
import net.smartworks.model.work.info.ImageCategoryInfo;
import net.smartworks.model.work.info.SmartWorkInfo;
import net.smartworks.model.work.info.WorkInfo;
import net.smartworks.model.work.info.WorkInfoList;
import net.smartworks.server.engine.infowork.domain.model.SwdRecord;
import net.smartworks.server.engine.infowork.form.exception.SwfException;
import net.smartworks.server.engine.infowork.form.model.SwfFormFieldDef;

public interface IWorkService {

	public WorkInfo[] getAllWorkCategoryByCategoryId(String categoryId) throws Exception;
	
	public WorkInfo[] getMyAllWorksByCategoryId(String categoryId) throws Exception;

	public WorkInfo[] getAllWorksByCategoryId(String categoryId) throws Exception;

	public ImageCategoryInfo[] getImageCategoriesByType(int displayType, String spaceId) throws Exception;

	public FileCategoryInfo[] getFileCategoriesByType(int displayType, String spaceId, String parentId) throws Exception;

	public SmartWorkInfo[] getMyFavoriteWorks() throws Exception;

	public SmartWorkInfo[] searchWork(String key, int searchType) throws Exception;

	public String getWorkIdByFormId(String formId) throws Exception;

	public Work getWorkById(String workId) throws Exception;

	public SearchFilter getSearchFilterById(String workType, String workId, String filterId) throws Exception;

	public List<SwfFormFieldDef> findFormFieldByForm(String formId, boolean deployedCondition) throws SwfException, Exception;

	public String getFormXml(String formId, String workId) throws Exception;

	public SmartForm getFormById(String formId, String workId) throws Exception;

	public SwdRecord getRecord(HttpServletRequest request) throws Exception;

	public SwdRecord getRecord(String workId, String recordId, String taskInstId) throws Exception;

	public SwdRecord getRecordByKeyValue(String workId, String keyValue) throws Exception;

	public String setWorkSearchFilter(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public void removeWorkSearchFilter(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public void setMyProfile(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public RequestParams setInstanceListParams(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public void addAFavoriteWork(String workId) throws Exception;

	public void removeAFavoriteWork(String workId) throws Exception;

	public void setIWorkManual(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public void setPWorkManual(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public WorkInfoList getAppWorkList(RequestParams params) throws Exception;

}