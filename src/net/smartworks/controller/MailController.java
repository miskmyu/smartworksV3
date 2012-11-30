/*	
 * $Id: WorkListController.java,v 1.1 2011/10/29 00:34:23 ysjung Exp $
 * created by    : SHIN HYUN SEONG
 * creation-date : 2011. 10. 15.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.smartworks.model.mail.MailFolder;
import net.smartworks.service.ISmartWorks;
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
public class MailController extends ExceptionInterceptor {

	ISmartWorks smartworks;

	@Autowired
	public void setSmartworks(ISmartWorks smartworks) {
		this.smartworks = smartworks;
	}

	@RequestMapping("/new_mail")
	public ModelAndView newMail(HttpServletRequest request, HttpServletResponse response) throws Exception {

		return SmartUtil.returnMnv(request, "jsp/content/work/start/new_mail.jsp", "");
	}

	@RequestMapping(value = "/new_mail_post", method = RequestMethod.POST)
	public ModelAndView sendMailPost(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String mailContents = (String)requestBody.get("contents");
		request.setAttribute("mailContents", mailContents);
		return SmartUtil.returnMnv(request, "jsp/content/work/start/new_mail.jsp?sendType=" + MailFolder.SEND_TYPE_WORK_CONTENT, "");
	}

	@RequestMapping("/my_all_mail_folders")
	public ModelAndView myAllMailFolders(HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.checkEmail();
		return SmartUtil.returnMnv(request, "jsp/nav/my_all_mail_folders.jsp", "");
	}

	@RequestMapping(value = "/send_mail", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void sendMail(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.sendMail(requestBody, request);
	}

	@RequestMapping(value = "/save_mail", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void saveMail(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.saveMailAsDraft(requestBody, request);
	}

	@RequestMapping(value = "/move_mails", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void moveMailS(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.moveMails(requestBody, request);
	}

	@RequestMapping(value = "/delete_mails", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void deleteMails(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.deleteMails(requestBody, request);
	}
	
	@RequestMapping(value = "/new_mail_folder", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void newMailFolder(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.newMailFolder(requestBody, request);
	}

	@RequestMapping(value = "/set_mail_folder", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void setMailFolder(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.setMailFolder(requestBody, request);
	}

	@RequestMapping(value = "/delete_mail_folder", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void deleteMailFolder(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.deleteMailFolder(requestBody, request);
	}
	
	@RequestMapping(value = "/authenticate_email_account", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody String authenticateEmailAccount(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		boolean result = smartworks.authenticateEmailAccount(requestBody, request);
		return result ? "true" : "false";
		
	}

	@RequestMapping(value = "/change_mail_password_request", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody void changeMailPasswordRequest(@RequestBody Map<String, Object> requestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		smartworks.changeMailPasswordRequest(requestBody, request);
	}

}