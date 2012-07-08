/*	
 * $Id: ContentController.java,v 1.1 2011/10/29 00:34:23 ysjung Exp $
 * created by    : SHIN HYUN SEONG
 * creation-date : 2011. 10. 15.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.smartworks.util.SmartUtil;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ContentController {
	
	@RequestMapping("/smart")
	public ModelAndView layouts(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/layouts.jsp", "smart.tiles");
	}

	@RequestMapping("/home")
	public ModelAndView home(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/home.jsp", "home.tiles");
	}

	@RequestMapping("/smartcaster")
	public ModelAndView smartcaster(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/smartcaster.jsp", "smartcaster.tiles");
	}

	@RequestMapping("/dashboard")
	public ModelAndView dashboard(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/dashboard.jsp", "dashboard.tiles");
	}

	@RequestMapping("/my_profile")
	public ModelAndView myProfile(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/my_profile.jsp", "my_profile.tiles");
	}

	@RequestMapping("/more_instance_list")
	public ModelAndView moreInstanceList(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/today/more_instance_list.jsp", "");
	}
	@RequestMapping("/start_work")
	public ModelAndView startWork(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/upload/start_work.jsp", "");
	}

	@RequestMapping("/upload_buttons")
	public ModelAndView uploadButtons(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/upload/upload_buttons.jsp", "");
	}

	@RequestMapping("/content")
	public ModelAndView content(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/content.jsp", "");
	}
	@RequestMapping("/localdate_string")
	public ModelAndView localdate_string(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/content/today/localdate_string.jsp", "");
	}
}
