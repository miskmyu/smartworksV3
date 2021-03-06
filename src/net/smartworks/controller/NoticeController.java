/*	
 * $Id: CommunityController.java,v 1.1 2011/10/29 00:34:23 ysjung Exp $
 * created by    : SHIN HYUN SEONG
 * creation-date : 2011. 10. 15.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.smartworks.service.ISmartWorks;
import net.smartworks.util.SmartUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class NoticeController {

	private ISmartWorks smartworks;

	@Autowired
	public void setSmartworks(ISmartWorks smartworks) {
		this.smartworks = smartworks;
	}
	
	@RequestMapping("/notice_message_box")
	public ModelAndView noticeMessageBox(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return SmartUtil.returnMnv(request, "jsp/notice/notice_message_box.jsp", "notice_message_box.tiles");
	}

	@RequestMapping("/notification_list_box")
	public ModelAndView notificationListBox(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return SmartUtil.returnMnv(request, "jsp/notice/notification_list_box.jsp", "notification_list_box.tiles");
	}

	@RequestMapping("/message_list_box")
	public ModelAndView messageListBox(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return SmartUtil.returnMnv(request, "jsp/notice/message_list_box.jsp", "message_list_box.tiles");
	}

	@RequestMapping("/comment_list_box")
	public ModelAndView commentsListBox(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return SmartUtil.returnMnv(request, "jsp/notice/comment_list_box.jsp", "comments_list_box.tiles");
	}

	@RequestMapping("/assigned_list_box")
	public ModelAndView assignedListBox(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return SmartUtil.returnMnv(request, "jsp/notice/assigned_list_box.jsp", "assigned_list_box.tiles");
	}

	@RequestMapping("/mailbox_list_box")
	public ModelAndView mailboxListBox(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return SmartUtil.returnMnv(request, "jsp/notice/mailbox_list_box.jsp", "mailbox_list_box.tiles");
	}

	@RequestMapping("/savedbox_list_box")
	public ModelAndView savedboxListBox(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return SmartUtil.returnMnv(request, "jsp/notice/savedbox_list_box.jsp", "savedbox_list_box.tiles");
	}

	@RequestMapping("/remove_notice_instance")
	public ModelAndView remove_notice_instance(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String removeNoticeId = request.getParameter("removeNoticeId");
		
		smartworks.removeNoticeInstance(removeNoticeId);
		
		return null;
	}
}
