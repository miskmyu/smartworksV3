/*	
 * $Id: WorkInstanceController.java,v 1.1 2011/10/29 00:34:23 ysjung Exp $
 * created by    : SHIN HYUN SEONG
 * creation-date : 2011. 10. 15.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.smartworks.model.filter.SearchFilter;
import net.smartworks.model.instance.TaskInstance;
import net.smartworks.model.instance.info.AsyncMessageInstanceInfo;
import net.smartworks.model.instance.info.ChatInstanceInfo;
import net.smartworks.model.instance.info.EventInstanceInfo;
import net.smartworks.model.instance.info.RequestParams;
import net.smartworks.model.security.AccessPolicy;
import net.smartworks.model.work.FileCategory;
import net.smartworks.model.work.SmartWork;
import net.smartworks.model.work.info.FileCategoryInfo;
import net.smartworks.model.work.info.ImageCategoryInfo;
import net.smartworks.server.service.factory.SwServiceFactory;
import net.smartworks.service.ISmartWorks;
import net.smartworks.service.impl.SmartWorks;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WorkInstanceController extends ExceptionInterceptor {

	ISmartWorks smartworks;

	@Autowired
	public void setSmartworks(ISmartWorks smartworks) {
		this.smartworks = smartworks;
	}

	@RequestMapping("/iwork_space")
	public ModelAndView iworkSpace(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/work/space/iwork_space.jsp", "iwork_space.tiles");
	}

	@RequestMapping("/pwork_space")
	public ModelAndView pworkSpace(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/work/space/pwork_space.jsp", "pwork_space.tiles");
	}

	@RequestMapping("/swork_space")
	public ModelAndView sworkSpace(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/work/space/swork_space.jsp", "swork_space.tiles");
	}

	@RequestMapping("/board_space")
	public ModelAndView boardSpace(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/work/space/board_space.jsp", "board_space.tiles");
	}

	@RequestMapping("/event_space")
	public ModelAndView eventSpace(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/work/space/event_space.jsp", "event_space.tiles");
	}

	@RequestMapping("/file_space")
	public ModelAndView fileSpace(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/work/space/file_space.jsp", "file_space.tiles");
	}

	@RequestMapping("/image_space")
	public ModelAndView imageSpace(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/work/space/image_space.jsp", "image_space.tiles");
	}

	@RequestMapping("/memo_space")
	public ModelAndView memoSpace(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/work/space/memo_space.jsp", "memo_space.tiles");
	}

	@RequestMapping("/mail_space")
	public ModelAndView mailSpace(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/work/space/mail_space.jsp", "mail_space.tiles");
	}

	@RequestMapping("/new_file")
	public ModelAndView newFile(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/work/start/new_file.jsp", "");
	}

	@RequestMapping("/new_picture")
	public ModelAndView newPicture(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/work/start/new_picture.jsp", "");
	}

	@RequestMapping("/new_event")
	public ModelAndView newEvent(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/work/start/new_event.jsp", "");
	}

	@RequestMapping("/new_memo")
	public ModelAndView newMemo(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/work/start/new_memo.jsp", "");
	}

	@RequestMapping("/new_board")
	public ModelAndView newBoard(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/work/start/new_board.jsp", "");
	}

	@RequestMapping("/new_comment")
	public ModelAndView newComment(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/work/start/new_comment.jsp", "");
	}

	@RequestMapping("/more_space_sub_instances")
	public ModelAndView moreSpaceSubInstances(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/work/space/more_space_sub_instances.jsp", "");
	}

	@RequestMapping("/image_instance_list")
	public ModelAndView imageInstanceList(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/work/list/image_instance_list.jsp", "");
	}
	@RequestMapping("/more_image_instance_list")
	public ModelAndView moreImageInstanceList(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/work/list/more_image_instance_list.jsp", "");
	}

	@RequestMapping("/categories_by_type")
	public ModelAndView categoriesByType(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/work/list/categories_by_type.jsp", "");
	}

	@RequestMapping("/sub_instances_in_instance")
	public ModelAndView subInstancesInInstance(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().setAttribute("subComments", null);
		return SmartUtil.returnMnv(request, "jsp/content/work/list/sub_instances_in_instance.jsp", "");
	}

	@RequestMapping("/update_histories")
	public ModelAndView updateHistories(HttpServletRequest request, HttpServletResponse response) {
		return SmartUtil.returnMnv(request, "jsp/content/work/space/update_histories.jsp", "");
	}

	@RequestMapping("/forward_histories")
	public ModelAndView forwardHistories(HttpServletRequest request, HttpServletResponse response) {
		return SmartUtil.returnMnv(request, "jsp/content/work/space/forward_histories.jsp", "");
	}

	@RequestMapping("/download_histories")
	public ModelAndView downloadHistories(HttpServletRequest request, HttpServletResponse response) {
		return SmartUtil.returnMnv(request, "jsp/content/work/space/download_histories.jsp", "");
	}

	@RequestMapping("/related_instances")
	public ModelAndView relatedInstances(HttpServletRequest request, HttpServletResponse response) {
		return SmartUtil.returnMnv(request, "jsp/content/work/space/related_instances.jsp", "");
	}

	@RequestMapping("/space_instance_list")
	public ModelAndView spaceInstanceList(HttpServletRequest request, HttpServletResponse response) {
		return SmartUtil.returnMnv(request, "jsp/content/work/space/space_instance_list.jsp", "");
	}

	@RequestMapping(value = "/set_file_instance_list", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public ModelAndView setIworkSearchFilter(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String filterId = smartworks.setWorkSearchFilter(requestBody, request);
		String workId = (String)requestBody.get("workId");
		ISmartWorks smartworks = (ISmartWorks)SmartUtil.getBean("smartWorks", request);
		ModelAndView mnv = new ModelAndView();
		mnv.addObject(smartworks);
		mnv.setViewName("jsp/content/work/list/file_instance_list.jsp?displayType=" + workId + "&filterId=" + filterId);
		return mnv;
	}

//	@RequestMapping(value = "/refresh_data_fields", method = RequestMethod.POST)
//	@ResponseStatus(HttpStatus.CREATED)
//	public @ResponseBody Map<String, Object> refreshDataFields(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
//		String instanceId = smartworks.refreshDataFields(requestBody);
//		// TO DO : Exception handler
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("href", "iwork_space.sw?cid=" + SmartWorks.CONTEXT_PREFIX_IWORK_SPACE + instanceId + "&wid=" + request.getParameter("selWorkSpace"));
//		return map;
//	}
	
	@RequestMapping(value = "/create_new_iwork", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Map<String, Object> createNewIwork(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String instanceId = smartworks.setInformationWorkInstance(requestBody, request);
		String workId = (String)requestBody.get("workId");
		String workSpaceId = (String)((Map<String, Object>)requestBody.get("frmAccessSpace")).get("selWorkSpace");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("href", "iwork_list.sw?cid=" + SmartWorks.CONTEXT_PREFIX_IWORK_LIST + workId);
		map.put("instanceId", instanceId);
		return map;
	}

	@RequestMapping(value = "/upload_new_picture", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Map<String, Object> uploadNewPicture(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String instanceId = smartworks.setInformationWorkInstance(requestBody, request);
		// TO DO : Exception handler
		String workSpaceId = (String)((Map<String, Object>)requestBody.get("frmAccessSpace")).get("selWorkSpace");
		String workId = (String)requestBody.get("workId");
		Map<String, Object> map = new HashMap<String, Object>();
		//map.put("href", "image_list.sw?cid=" + SmartWorks.CONTEXT_PREFIX_IMAGE_LIST + SmartWork.ID_FILE_MANAGEMENT + "&wid=" + workSpaceId);
		map.put("href", "image_list.sw?cid=" + SmartWorks.CONTEXT_PREFIX_IMAGE_LIST + SmartWork.ID_FILE_MANAGEMENT);
		map.put("instanceId", instanceId);
		return map;
	}

	@RequestMapping(value = "/start_new_pwork", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Map<String, Object> startNewPwork(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String instanceId = smartworks.startProcessWorkInstance(requestBody, request);
		// TO DO : Exception handler
		String workSpaceId = (String)((Map<String, Object>)requestBody.get("frmAccessSpace")).get("selWorkSpace");
		String workId = (String)requestBody.get("workId");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("href", "pwork_list.sw?cid=" + SmartWorks.CONTEXT_PREFIX_PWORK_LIST + workId);
		map.put("instanceId", instanceId);
		return map;
	}

	@RequestMapping(value = "/upload_new_file", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Map<String, Object> uploadNewFile(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String instanceId = smartworks.setInformationWorkInstance(requestBody, request);
		// TO DO : Exception handler
		String workSpaceId = (String)((Map<String, Object>)requestBody.get("frmAccessSpace")).get("selWorkSpace");
		String workId = (String)requestBody.get("workId");
		Map<String, Object> map = new HashMap<String, Object>();
		//map.put("href", "file_list.sw?cid=" + SmartWorks.CONTEXT_PREFIX_FILE_LIST + SmartWork.ID_FILE_MANAGEMENT + "&wid=" + workSpaceId);
		map.put("href", "file_list.sw?cid=" + SmartWorks.CONTEXT_PREFIX_FILE_LIST + SmartWork.ID_FILE_MANAGEMENT);
		map.put("instanceId", instanceId);
		return map;
	}

	@RequestMapping(value = "/create_new_event", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Map<String, Object> createNewEvent(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String instanceId = smartworks.setInformationWorkInstance(requestBody, request);
		String workSpaceId = (String)((Map<String, Object>)requestBody.get("frmAccessSpace")).get("selWorkSpace");
		String workId = (String)requestBody.get("workId");
		Map<String, Object> map = new HashMap<String, Object>();
		//map.put("href", "event_list.sw?cid=" + SmartWorks.CONTEXT_PREFIX_EVENT_LIST + SmartWork.ID_EVENT_MANAGEMENT + "&wid=" + workSpaceId);
		map.put("href", "event_list.sw?cid=" + SmartWorks.CONTEXT_PREFIX_EVENT_LIST + SmartWork.ID_EVENT_MANAGEMENT);
		map.put("instanceId", instanceId);
		return map;
	}

	@RequestMapping(value = "/create_new_memo", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Map<String, Object> createNewMemo(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String instanceId = smartworks.setInformationWorkInstance(requestBody, request);
		// TO DO : Exception handler
		String workSpaceId = (String)((Map<String, Object>)requestBody.get("frmAccessSpace")).get("selWorkSpace");
		String workId = (String)requestBody.get("workId");
		Map<String, Object> map = new HashMap<String, Object>();
		//map.put("href", "memo_list.sw?cid=" + SmartWorks.CONTEXT_PREFIX_MEMO_LIST + SmartWork.ID_MEMO_MANAGEMENT + "&wid=" + workSpaceId);
		map.put("href", "memo_list.sw?cid=" + SmartWorks.CONTEXT_PREFIX_MEMO_LIST + SmartWork.ID_MEMO_MANAGEMENT);
		map.put("instanceId", instanceId);
		return map;
	}

	@RequestMapping(value = "/create_new_board", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Map<String, Object> createNewBoard(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String instanceId = smartworks.setInformationWorkInstance(requestBody, request);
		// TO DO : Exception handler
		String workSpaceId = (String)((Map<String, Object>)requestBody.get("frmAccessSpace")).get("selWorkSpace");
		String workId = (String)requestBody.get("workId");
		Map<String, Object> map = new HashMap<String, Object>();
		//map.put("href", "board_list.sw?cid=" + SmartWorks.CONTEXT_PREFIX_BOARD_LIST + SmartWork.ID_BOARD_MANAGEMENT + "&wid=" + workSpaceId);
		map.put("href", "board_list.sw?cid=" + SmartWorks.CONTEXT_PREFIX_BOARD_LIST + SmartWork.ID_BOARD_MANAGEMENT);
		map.put("instanceId", instanceId);
		return map;
	}

	@RequestMapping(value = "/set_iwork_instance", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody void setIworkInstance(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.setInformationWorkInstance(requestBody, request);
	}

	@RequestMapping(value = "/remove_iwork_instance", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody void removeIworkInstance(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.removeInformationWorkInstance(requestBody, request);
	}

	@RequestMapping(value = "/forward_iwork_instance", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Map<String, Object> forwardIworkInstance(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.forwardIworkInstance(requestBody, request);
		return null;
	}

	@RequestMapping(value = "/approval_iwork_instance", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Map<String, Object> approvalIworkInstance(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.approvalIworkInstance(requestBody, request);
		return null;
	}

	@RequestMapping(value = "/forward_pwork_instance", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Map<String, Object> forwardPworkInstance(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.forwardPworkInstance(requestBody, request);
		return null;
	}

	@RequestMapping(value = "/approval_pwork_instance", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody Map<String, Object> approvalPworkInstance(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.approvalPworkInstance(requestBody, request);
		return null;
	}

	@RequestMapping(value = "/remove_pwork_instance", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody void removePworkInstance(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.removeProcessWorkInstance(requestBody, request);
	}

	@RequestMapping(value = "/update_my_profile", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Map<String, Object> updateMyProfile(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.setMyProfile(requestBody, request);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("href", "my_profile.sw");
		return map;
	}

	@RequestMapping(value = "/set_instance_list_params", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public ModelAndView setInstanceListParams(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		RequestParams requestParams = smartworks.setInstanceListParams(requestBody, request);
		String href = (String)requestBody.get("href");
		ISmartWorks smartworks = (ISmartWorks)SmartUtil.getBean("smartWorks", request);
		ModelAndView mnv = new ModelAndView();
		mnv.addObject(smartworks);
		mnv.addObject("requestParams", requestParams);
		mnv.setViewName(href);
		return mnv;

	}

	@RequestMapping(value = "/set_file_list_params", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public ModelAndView setFileListParams(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		RequestParams requestParams = smartworks.setInstanceListParams(requestBody, request);
		int displayType = Integer.parseInt((String)requestBody.get("displayType"));
		String categoryId = (String)requestBody.get("categoryId");
		SearchFilter searchFilter = null;
		switch(displayType){
		case FileCategory.DISPLAY_BY_CATEGORY:
			searchFilter = SearchFilter.getByFileCategoryIdFilter(categoryId);
			break;
		case FileCategory.DISPLAY_BY_WORK:
			searchFilter = SearchFilter.getByWorkIdFilter(categoryId);
			break;
		case FileCategory.DISPLAY_BY_YEAR:
			searchFilter = SearchFilter.getByCreatedDateFilter(categoryId);
			break;
		case FileCategory.DISPLAY_BY_OWNER:
			searchFilter = SearchFilter.getByOwnerFilter(categoryId);
			break;
		case FileCategory.DISPLAY_BY_FILE_TYPE:
			searchFilter = SearchFilter.getByFileTypeFilter(categoryId);
			break;
		}
		requestParams.setSearchFilter(searchFilter);
		String href = (String)requestBody.get("href");
		ISmartWorks smartworks = (ISmartWorks)SmartUtil.getBean("smartWorks", request);
		ModelAndView mnv = new ModelAndView();
		mnv.addObject(smartworks);
		mnv.addObject("requestParams", requestParams);
		mnv.setViewName(href);
		return mnv;

	}

	@RequestMapping("/get_file_category_list_page")
	public @ResponseBody Map<String, Object> getFileCategoryListPage(HttpServletRequest request, HttpServletResponse response) {
		
		int displayType = Integer.parseInt(request.getParameter("displayType"));
		String wid = request.getParameter("wid");
		String parentId = request.getParameter("parentId");
		String listPage = "";
		try{
			FileCategoryInfo[] categories = smartworks.getFileCategoriesByType(displayType, wid, parentId);
			for(int i=0; i<categories.length; i++){
				FileCategoryInfo category = categories[i];
				listPage = listPage + "<option value='" + category.getId() + "'>" + category.getName() + "</option>";
			}
		}catch(Exception e){
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("listPage", listPage);
		return map;		
	}
	
	@RequestMapping("/get_image_category_list_page")
	public @ResponseBody Map<String, Object> getImageCategoryListPage(HttpServletRequest request, HttpServletResponse response) {
		
		int displayType = Integer.parseInt(request.getParameter("displayType"));
		String wid = request.getParameter("wid");
		String listPage = "";
		try{
			ImageCategoryInfo[] categories = smartworks.getImageCategoriesByType(displayType, wid);
			for(int i=0; i<categories.length; i++){
				ImageCategoryInfo category = categories[i];
				listPage = listPage + "<option value='" + category.getId() + "'>" + category.getName() + "</option>";
			}
		}catch(Exception e){
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("listPage", listPage);
		return map;		
	}
	
	@RequestMapping(value = "/add_comment_on_work", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Map<String, Object> addCommentOnWork(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String commentId = smartworks.addCommentOnWork(requestBody, request);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("commentId", commentId);
		return map;		
	}

	@RequestMapping(value = "/update_comment_on_work", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void updateCommentOnWork(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.updateCommentOnWork(requestBody, request);
	}

	@RequestMapping(value = "/remove_comment_from_work", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void removeCommentFromWork(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.removeCommentFromWork(requestBody, request);
	}

	@RequestMapping(value = "/add_comment_on_instance", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Map<String, Object> addCommentOnInstance(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String commentId = smartworks.addCommentOnInstance(requestBody, request);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("commentId", commentId);
		return map;
	}

	@RequestMapping(value = "/update_comment_on_instance", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void updateCommentOnInstance(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.updateCommentOnInstance(requestBody, request);
	}

	@RequestMapping(value = "/remove_comment_from_instance", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void removeCommentFromInstance(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.removeCommentFromInstance(requestBody, request);
	}

	@RequestMapping(value = "/get_events_by_dates", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Map<String, Object> getEventsByDates(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String workSpaceId = request.getParameter("workSpaceId");
		LocalDate fromDate = LocalDate.convertLocalDateStringToLocalDate(request.getParameter("fromDate"));
		LocalDate toDate = LocalDate.convertLocalDateStringToLocalDate(request.getParameter("toDate"));
		
		EventInstanceInfo[] eventInstances = smartworks.getEventInstanceList(workSpaceId, fromDate, toDate);
		
		class EventInfo{
			String id;
			String name;
			String start;
			String end;
			String ownerId;
			String ownerName;
			String ownerPicture;
			String workSpaceId;
			String newInstance;
			int subInstanceCount;
			
			public String getId() {
				return id;
			}
			public String getName() {
				return name;
			}
			public String getStart() {
				return start;
			}
			public String getEnd() {
				return end;
			}
			public String getOwnerId() {
				return ownerId;
			}
			public String getOwnerName() {
				return ownerName;
			}
			public String getOwnerPicture() {
				return ownerPicture;
			}
			public String getWorkSpaceId() {
				return workSpaceId;
			}
			public String getWorkId() {
				return SmartWork.ID_EVENT_MANAGEMENT;
			}
			public String getNewInstance() {
				return newInstance;
			}
			public int getSubInstanceCount() {
				return subInstanceCount;
			}
			public EventInfo(){
			}
		}

		EventInfo[] events = new EventInfo[eventInstances.length];
		for(int i=0; i<eventInstances.length; i++){
			EventInstanceInfo eventInstance = eventInstances[i];
			EventInfo event = new EventInfo();
			event.id = eventInstance.getId();
			event.name = eventInstance.getSubject();
			event.start = eventInstance.getStart().toDateValue();
			event.end = eventInstance.getEnd() != null ? eventInstance.getEnd().toDateValue() : null;
			event.ownerId = eventInstance.getOwner().getId();
			event.ownerName = eventInstance.getOwner().getLongName();
			event.ownerPicture = eventInstance.getOwner().getMinPicture();
			event.workSpaceId = eventInstance.getWorkSpaceId();
			event.subInstanceCount = eventInstance.getSubInstanceCount();
			event.newInstance = "" + eventInstance.isNew();
			events[i] = event;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("events", events);
		return map;
	}	

	@RequestMapping(value = "/add_like_to_instance", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void addLikeToInstance(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.addLikeToInstance(requestBody, request);
	}

	@RequestMapping(value = "/remove_like_from_instance", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void removeLikeFromInstance(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.removeLikeFromInstance(requestBody, request);
	}

	@RequestMapping(value = "/create_async_message", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void createAsyncMessage(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.createAsyncMessage(requestBody, request);
	}

	@RequestMapping(value = "/remove_async_message", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void removeAsyncMessage(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.removeAsyncMessage(requestBody, request);
	}

	@RequestMapping(value = "/set_async_message", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void setAsyncMessage(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.setAsyncMessage(requestBody, request);
	}

	@RequestMapping(value = "/fetch_async_messages_by_chatid", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Map<String, Object> fetchAsyncMessagesByChatid(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ChatInstanceInfo[] messages = smartworks.fetchAsyncMessagesByChatid(request, response);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("messages", messages);
		return map;
	}

	@RequestMapping(value = "/create_new_file_folder", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody void createNewFileFolder(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.createNewFileFolder(requestBody, request);
	}
	
	@RequestMapping(value = "/set_file_folder", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void setFileFolder(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.setFileFolder(requestBody, request);
	}
	
	@RequestMapping(value = "/remove_file_folder", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void removeFileFolder(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.removeFileFolder(requestBody, request);
	}
	
	@RequestMapping(value = "/create_new_image_folder", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody void createNewImageFolder(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.createNewImageFolder(requestBody, request);
	}
	
	@RequestMapping(value = "/set_image_folder", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void setImageFolder(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.setImageFolder(requestBody, request);
	}
	
	@RequestMapping(value = "/remove_image_folder", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void removeImageFolder(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.removeImageFolder(requestBody, request);
	}
	
	@RequestMapping(value = "/remove_image_instance", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void removeImageInstance(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.removeImageInstance(requestBody, request);
	}
	
	@RequestMapping(value = "/move_file_instances", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void moveFileInstances(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.moveFileInstances(requestBody, request);
	}
	
	@RequestMapping(value = "/move_image_instances", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void moveImageInstances(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.moveImageInstances(requestBody, request);
	}
	
	@RequestMapping(value = "/abend_process_instance", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void abendProcessInstance(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> accessData = new HashMap<String, Object>();	
		accessData.put("selWorkSpace", SmartUtil.getSystemUser().getId());
		accessData.put("selWorkSpaceType", ISmartWorks.SPACE_TYPE_USER);
		accessData.put("selAccessLevel", AccessPolicy.LEVEL_PUBLIC);
		requestBody.put("frmAccessSpace", accessData);
		smartworks.abendTaskInstance(requestBody, request);
	}
	
}
