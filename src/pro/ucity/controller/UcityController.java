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
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.service.ISmartWorks;
import net.smartworks.util.SmartUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
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

	@RequestMapping("/loginc")
	public ModelAndView loginc(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mnv = new ModelAndView();
		String type = CommonUtil.toNotNull(request.getParameter("type"));
		String referer = CommonUtil.toNotNull(request.getParameter("referer"));
		mnv.addObject("type", type);
		mnv.addObject("_to", referer);
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

	@RequestMapping("/situationMonitoring")
	public ModelAndView situationMonitoring(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "u-city/jsp/situation_monitoring.jsp", "situationMonitoring.tiles");
	}

	@RequestMapping("/situationDetail")
	public ModelAndView situationDetail(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "u-city/jsp/content/situation_space.jsp", "situationDetail.tiles");
	}

	@RequestMapping("/situationManual")
	public ModelAndView situationManual(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "u-city/jsp/content/situation_manual.jsp", "situationManual.tiles");
	}

	@RequestMapping("/situationAudit")
	public ModelAndView situationAudit(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "u-city/jsp/content/situation_audit_list.jsp", "situationAudit.tiles");
	}

	@RequestMapping("/situationManualEnvOccurrence")
	public ModelAndView situationManualEnvOccurrence(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "u-city/jsp/content/situation_manual_env_occurrence.jsp", "situationManualEnvOccurrence.tiles");
	}

	@RequestMapping("/searchFilter")
	public ModelAndView searchFilter(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "u-city/jsp/content/search_filter.jsp", "");
	}

	@RequestMapping("/combo_u_status_field")
	public ModelAndView comboUStatusField(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "u-city/jsp/content/combo_u_status_field.jsp", "");
	}

	@RequestMapping("/combo_u_service_field")
	public ModelAndView comboUServiceField(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "u-city/jsp/content/combo_u_service_field.jsp", "");
	}

	@RequestMapping("/combo_u_event_field")
	public ModelAndView comboUEventField(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "u-city/jsp/content/combo_u_event_field.jsp", "");
	}

	@RequestMapping("/combo_u_type_field")
	public ModelAndView comboUTypeField(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "u-city/jsp/content/combo_u_type_field.jsp", "");
	}

	@RequestMapping("/combo_u_issms_field")
	public ModelAndView comboUIsSmsField(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "u-city/jsp/content/combo_u_issms_field.jsp", "");
	}

	@RequestMapping("/situationReportView")
	public ModelAndView situationReportView(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnvSera(request, "u-city/jsp/content/situation_report_view.jsp", "");
	}

	@RequestMapping(value = "/ucity_get_chart_xml", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody Map<String, Object>  ucityGetChartXml(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String categoryName = request.getParameter("categoryName");
		String periodName = request.getParameter("periodName");
		String serviceName = request.getParameter("serviceName");
		String eventName = request.getParameter("eventName");
		
		String chartXml = smartworks.getUcityChartXml(categoryName, periodName, serviceName, eventName);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("record", chartXml);
		return map;
	}	


}
