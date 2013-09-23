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
public class PopupController {
	
	@RequestMapping("/pop_user_info")
	public ModelAndView popUserInfo(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_user_info.jsp", "jsp/popup/pop_user_info.jsp");
	}

	@RequestMapping("/pop_select_user")
	public ModelAndView popSelectUser(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_select_user.jsp", "jsp/popup/pop_select_user.jsp");
	}

	@RequestMapping("/pop_select_email")
	public ModelAndView popSelectEmail(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_select_email.jsp", "jsp/popup/pop_select_email.jsp");
	}

	@RequestMapping("/pop_select_community")
	public ModelAndView popSelectCommunity(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_select_community.jsp", "jsp/popup/pop_select_community.jsp");
	}

	@RequestMapping("/pop_select_depart")
	public ModelAndView popSelectDepart(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_select_depart.jsp", "jsp/popup/pop_select_depart.jsp");
	}

	@RequestMapping("/pop_select_work")
	public ModelAndView popSelectWork(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_select_work.jsp", "jsp/popup/pop_select_work.jsp");
	}

	@RequestMapping("/pop_select_work_item")
	public ModelAndView popSelectWorkItem(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_select_work_item.jsp", "jsp/popup/pop_select_work_item.jsp");
	}

	@RequestMapping("/pop_select_approval_line")
	public ModelAndView popSelectApprovalLine(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_select_approval_line.jsp", "jsp/popup/pop_select_approval_line.jsp");
	}

	@RequestMapping("/pop_worklist_by_category")
	public ModelAndView popWorklistByCategory(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_worklist_by_category.jsp", "");
	}

	@RequestMapping("/pop_userlist_by_depart")
	public ModelAndView popUserlistByDepart(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_userlist_by_depart.jsp", "");
	}

	@RequestMapping("/pop_departlist_by_depart")
	public ModelAndView popDepartlistByDepart(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_departlist_by_depart.jsp", "");
	}

	@RequestMapping("/pop_userlist_by_group")
	public ModelAndView popUserlistByGroup(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_userlist_by_group.jsp", "");
	}

	@RequestMapping("/pop_userlist_by_contact")
	public ModelAndView popUserlistByContact(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_userlist_by_contact.jsp", "");
	}

	@RequestMapping("/pop_iwork_instance_list")
	public ModelAndView popIworkInstanceList(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_iwork_instance_list.jsp", "");
	}

	@RequestMapping("/pop_new_group")
	public ModelAndView newGroup(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_new_group.jsp", "");
	}

	@RequestMapping("/pop_new_category")
	public ModelAndView popNewCategory(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_new_category.jsp", "");
	}

	@RequestMapping("/pop_change_category_name")
	public ModelAndView popChangeCategoryName(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_change_category_name.jsp", "");
	}

	@RequestMapping("/pop_new_work_definition")
	public ModelAndView popNewWorkDefinition(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_new_work_definition.jsp", "");
	}

	@RequestMapping("/pop_move_work_definition")
	public ModelAndView popMoveWorkDefinition(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_move_work_definition.jsp", "");
	}

	@RequestMapping("/pop_download_from_appstore")
	public ModelAndView popDownloadFromAppstore(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_download_from_appstore.jsp", "");
	}

	@RequestMapping("/group_options_by_category")
	public ModelAndView groupOptionsByCategory(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/group_options_by_category.jsp", "");
	}

	@RequestMapping("/pop_new_event")
	public ModelAndView popNewEvent(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_new_event.jsp", "");
	}

	@RequestMapping("/pop_new_mail_folder")
	public ModelAndView popNewMailFolder(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_new_mail_folder.jsp", "");
	}

	@RequestMapping("/pop_invite_group_members")
	public ModelAndView popInviteGroupMembers(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_invite_group_members.jsp", "");
	}

	@RequestMapping("/pop_new_file_folder")
	public ModelAndView popNewFileFolder(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_new_file_folder.jsp", "");
	}

	@RequestMapping("/pop_new_image_folder")
	public ModelAndView popNewImageFolder(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_new_image_folder.jsp", "");
	}

	@RequestMapping("/pop_print_content")
	public ModelAndView popPrintContent(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_print_content.jsp", "");
	}

	@RequestMapping("/pop_show_picture")
	public ModelAndView popShowPicture(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_show_picture.jsp", "");
	}

	@RequestMapping("/pop_show_instance")
	public ModelAndView popShowInstance(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_show_instance.jsp", "");
	}

	@RequestMapping("/pop_reassign_performer")
	public ModelAndView popReassignPerformer(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_reassign_performer.jsp", "");
	}

	@RequestMapping("/pop_change_mail_password")
	public ModelAndView popChangeMailPassword(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_change_mail_password.jsp", "");
	}

	@RequestMapping("/pop_event_alarm")
	public ModelAndView popEventAlarm(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_event_alarm.jsp", "");
	}

	@RequestMapping("/pop_new_report_pane")
	public ModelAndView popNewReportPane(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_new_report_pane.jsp", "");
	}

	@RequestMapping("/pop_retire_member")
	public ModelAndView popRetireMember(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_retire_member.jsp", "");
	}

	@RequestMapping("/pop_abolish_department")
	public ModelAndView popAbolishDepartment(HttpServletRequest request, HttpServletResponse response) {

		return SmartUtil.returnMnv(request, "jsp/popup/pop_abolish_department.jsp", "");
	}

}
