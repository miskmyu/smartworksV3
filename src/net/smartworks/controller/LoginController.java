/*	
 * $Id$
 * created by    : hsshin
 * creation-date : 2011. 11. 14.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.service.ISmartWorks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

	ISmartWorks smartworks;

	@Autowired
	public void setSmartworks(ISmartWorks smartworks) {
		this.smartworks = smartworks;
	}

	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mnv = new ModelAndView();
		String type = CommonUtil.toNotNull(request.getParameter("type"));
		mnv.addObject("type", type);
		mnv.setViewName("jsp/login.jsp");
		return mnv;
	}

	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("jsp/index.jsp");
		return mnv;
	}

	@RequestMapping("/logout")
	public ModelAndView logouts(HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.logout(request, response);
		ModelAndView mnv = new ModelAndView();
		mnv.addObject("href", "logout");
		mnv.setViewName("jsp/movePage.jsp");
		return mnv;
	}

}