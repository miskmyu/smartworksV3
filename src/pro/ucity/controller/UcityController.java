/*	
 * $Id: ContentController.java,v 1.1 2011/10/29 00:34:23 ysjung Exp $
 * created by    : SHIN HYUN SEONG
 * creation-date : 2011. 10. 15.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package pro.ucity.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.smartworks.controller.ExceptionInterceptor;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.info.InstanceInfo;
import net.smartworks.model.sera.Team;
import net.smartworks.model.sera.info.MissionInstanceInfo;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.service.ISmartWorks;
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
public class UcityController extends ExceptionInterceptor {
	
	ISmartWorks smartworks;

	@Autowired
	public void setSmartworks(ISmartWorks smartworks) {
		this.smartworks = smartworks;
	}

	@RequestMapping("/situationMonitoring")
	public ModelAndView situationMonitoring(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "u-city/jsp/situation_monitoring.jsp", "situationMonitoring.tiles");
	}

	@RequestMapping("/loginc")
	public ModelAndView loginc(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mnv = new ModelAndView();
		String type = CommonUtil.toNotNull(request.getParameter("type"));
		mnv.addObject("type", type);
		mnv.setViewName("u-city/jsp/login.jsp");
		return mnv;
	}

	@RequestMapping("/logoutc")
	public ModelAndView logoutc(HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.logout(request, response);
		ModelAndView mnv = new ModelAndView();
		mnv.addObject("href", "logout");
		mnv.setViewName("jsp/movePage.jsp");
		return mnv;
	}

	@RequestMapping("/indexc")
	public ModelAndView indexc(HttpServletRequest request, HttpServletResponse response) {		
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("u-city/jsp/index.jsp");
		return mnv;
	}

	@RequestMapping("/situationDetail")
	public ModelAndView situationDetail(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "u-city/jsp/content/situation_space.jsp", "situationDetail.tiles");
	}

}
