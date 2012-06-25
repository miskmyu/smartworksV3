/*	
 * $Id: ContentController.java,v 1.1 2011/10/29 00:34:23 ysjung Exp $
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

import net.smartworks.service.ISmartWorks;
import net.smartworks.service.impl.SmartWorks;
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
public class BuilderController {

	ISmartWorks smartworks;

	@Autowired
	public void setSmartworks(ISmartWorks smartworks) {
		this.smartworks = smartworks;
	}

	@RequestMapping("/builder_home")
	public ModelAndView builderHome(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/builder/home.jsp", "builder_home.tiles");
	}

	@RequestMapping("/tab_workbench")
	public ModelAndView tabWorkbench(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/builder/tab_workbench.jsp", "tab_workbench.tiles");
	}

	@RequestMapping("/tab_work_settings")
	public ModelAndView tabWorkSettings(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/builder/tab_work_settings.jsp", "tab_work_settings.tiles");
	}

	@RequestMapping("/tab_work_sharing")
	public ModelAndView tabWorkSharing(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/builder/tab_work_sharing.jsp", "tab_work_sharing.tiles");
	}

	@RequestMapping("/worklist_by_category")
	public ModelAndView worklistByCategory(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/nav/worklist_by_category.jsp", "");
	}

	@RequestMapping("/start_work_service")
	public ModelAndView startWorkService(HttpServletRequest request, HttpServletResponse response) {
		String workId = request.getParameter("workId");
		try{
			smartworks.startWorkService(workId);
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}
		String cid = ISmartWorks.CONTEXT_PREFIX_BUILDER_SPACE + workId;
		return SmartUtil.returnMnv(request, "jsp/content/builder/tab_workbench.jsp?cid=" + cid, "");
	}

	@RequestMapping("/stop_work_service")
	public ModelAndView stopWorkService(HttpServletRequest request, HttpServletResponse response) {
		String workId = request.getParameter("workId");
		try{
			smartworks.stopWorkService(workId);
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}
		String cid = ISmartWorks.CONTEXT_PREFIX_BUILDER_SPACE + workId;
		return SmartUtil.returnMnv(request, "jsp/content/builder/tab_workbench.jsp?cid=" + cid, "");
	}

	@RequestMapping("/start_work_editing")
	public ModelAndView startWorkEditing(HttpServletRequest request, HttpServletResponse response) {
		String workId = request.getParameter("workId");
		try{
			smartworks.startWorkEditing(workId);
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}
		String cid = ISmartWorks.CONTEXT_PREFIX_BUILDER_SPACE + workId;
		return SmartUtil.returnMnv(request, "jsp/content/builder/tab_workbench.jsp?cid=" + cid, "");
	}

	@RequestMapping("/stop_work_editing")
	public ModelAndView stopWorkEditing(HttpServletRequest request, HttpServletResponse response) {
		String workId = request.getParameter("workId");
		try{
			smartworks.stopWorkEditing(workId);
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}
		String cid = ISmartWorks.CONTEXT_PREFIX_BUILDER_SPACE + workId;
		return SmartUtil.returnMnv(request, "jsp/content/builder/tab_workbench.jsp?cid=" + cid, "");
	}

	@RequestMapping(value = "/set_work_settings", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void setWorkSettings(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.setWorkSettings(requestBody, request);
	}
	
	@RequestMapping(value = "/publish_work_to_store", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void publishWorkToStore(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.publishWorkToStore(requestBody, request);
	}
	
	@RequestMapping(value = "/create_new_category", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody void createNewCategory(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.createNewCategory(requestBody, request);
	}
	
	@RequestMapping(value = "/set_category", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void setCategory(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.setCategory(requestBody, request);
	}
	
	@RequestMapping(value = "/remove_category", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void removeCategory(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.removeCategory(requestBody, request);
	}
	
	@RequestMapping(value = "/create_new_work_definition", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public @ResponseBody void createNewWorkDefinition(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.createNewWorkDefinition(requestBody, request);
	}
	
	@RequestMapping(value = "/set_work_definition", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void setWorkDefinition(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.setWorkDefinition(requestBody, request);
	}
	
	@RequestMapping(value = "/remove_work_definition", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void removeWorkDefinition(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.removeWorkDefinition(requestBody, request);
	}
	
	@RequestMapping(value = "/copy_work_definition", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Map<String, Object> copyWorkDefinition(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String workId = smartworks.copyWorkDefinition(requestBody, request);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("href", "builder_home.sw#tab_workbench.sw?cid=" + SmartWorks.CONTEXT_PREFIX_BUILDER_SPACE + workId);
		return map;
	}
	
	@RequestMapping(value = "/move_work_definition", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Map<String, Object> moveWorkDefinition(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String workId = smartworks.moveWorkDefinition(requestBody, request);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("href", "builder_home.sw#tab_workbench.sw?cid=" + SmartWorks.CONTEXT_PREFIX_BUILDER_SPACE + workId);
		return map;
	}
	
}
