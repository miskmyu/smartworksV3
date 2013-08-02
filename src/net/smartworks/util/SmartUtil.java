/*	
 * $Id$
 * created by    : SHIN HYUN SEONG
 * creation-date : 2011. 7. 15.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.smartworks.model.community.Group;
import net.smartworks.model.community.User;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.company.CompanyGeneral;
import net.smartworks.model.company.CompanyOption;
import net.smartworks.model.instance.info.EventInstanceInfo;
import net.smartworks.model.instance.info.WorkInstanceInfo;
import net.smartworks.model.mail.MailAccount;
import net.smartworks.model.notice.Notice;
import net.smartworks.model.notice.NoticeMessage;
import net.smartworks.model.work.SmartWork;
import net.smartworks.server.engine.common.loginuser.manager.ILoginUserManager;
import net.smartworks.server.engine.common.loginuser.model.LoginUser;
import net.smartworks.server.engine.common.loginuser.model.LoginUserCond;
import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.organization.model.SwoUser;
import net.smartworks.server.engine.publishnotice.model.PublishNotice;
import net.smartworks.server.engine.publishnotice.model.PublishNoticeCond;
import net.smartworks.server.engine.security.model.Login;
import net.smartworks.server.service.ISettingsService;
import net.smartworks.server.service.factory.SwServiceFactory;
import net.smartworks.server.service.util.ModelConverter;
import net.smartworks.service.ISmartWorks;
import net.smartworks.service.impl.SmartWorks;

import org.apache.axis.utils.StringUtils;
import org.cometd.bayeux.client.ClientSession;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.client.BayeuxClient;
import org.cometd.client.transport.ClientTransport;
import org.cometd.client.transport.LongPollingTransport;
import org.cometd.bayeux.Channel;
import org.cometd.bayeux.Message;
import org.eclipse.jetty.client.HttpClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;

public class SmartUtil {

	public SmartUtil() {
		super();
	}

	/**
	 * beanName; getBean
	 * @param beanName
	 * @param request
	 * @return
	 */
	public static Object getBean(String beanName, HttpServletRequest request) {

		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());

		return (Object) wac.getBean(beanName);
	}

	public static ModelAndView returnMnv(HttpServletRequest request, String ajaxPage, String defaultPage) {
		String getHeader = request.getHeader("X-Requested-With");
 		ISmartWorks smartworks = (ISmartWorks)SmartUtil.getBean("smartWorks", request);
		if (getHeader != null){
			SecurityContext context = (SecurityContext) request.getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
			if (SmartUtil.isBlankObject(context))
				return new ModelAndView("home.tiles", "smartWorks", smartworks);
			else
				return new ModelAndView(ajaxPage, "smartWorks", smartworks);
		}else{
			return new ModelAndView(defaultPage, "smartWorks", smartworks);
		}
	}

	public static ModelAndView returnMnvSera(HttpServletRequest request, String ajaxPage, String defaultPage) {
		String getHeader = request.getHeader("X-Requested-With");
 		ISmartWorks smartworks = (ISmartWorks)SmartUtil.getBean("smartWorks", request);
		if (getHeader != null){
			SecurityContext context = (SecurityContext) request.getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
			if (SmartUtil.isBlankObject(context))
				return new ModelAndView("myPAGE.tiles", "smartWorks", smartworks);
			else
				return new ModelAndView(ajaxPage, "smartWorks", smartworks);
		}else{
			return new ModelAndView(defaultPage, "smartWorks", smartworks);
		}
	}

	/* (non-Javadoc)
	 * @see net.smartworks.service.impl.ISmartWorks#isSameContextPrefix(java.lang.String, java.lang.String)
	 */
	public static boolean isSameContextPrefix(String contextPrefix,	String contextId) throws Exception {
		if (contextPrefix == null || contextId == null
				|| contextPrefix.length() >= contextId.length())
			return false;
		if (contextId.subSequence(0, contextPrefix.length()).equals(
				contextPrefix))
			return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see net.smartworks.service.impl.ISmartWorks#isWorkSpaceContextType(java.lang.String)
	 */
	public static boolean isWorkSpaceContextType(String contextId) throws Exception {
		if (contextId == null || contextId.length() < 6)
			return false;
		if (contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_IWORK_SPACE)
				|| contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_PWORK_SPACE)
				|| contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_SWORK_SPACE)
				|| contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_FILE_SPACE)
				|| contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_MEMO_SPACE)
				|| contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_IMAGE_SPACE)
				|| contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_EVENT_SPACE)
				|| contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_BOARD_SPACE)
				|| contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_MAIL_SPACE))
			return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see net.smartworks.service.impl.ISmartWorks#isCommunitySpaceContextType(java.lang.String)
	 */
	public static boolean isCommunitySpaceContextType(String contextId) throws Exception {
		if (contextId == null || contextId.length() < 6)
			return false;
		if (contextId.substring(0, 6).equals(SmartWorks.CONTEXT_PREFIX_DEPARTMENT_SPACE)
				|| contextId.substring(0, 6).equals(SmartWorks.CONTEXT_PREFIX_GROUP_SPACE)
				|| contextId.substring(0, 6).equals(SmartWorks.CONTEXT_PREFIX_USER_SPACE))
			return true;
		return false;
	}

	public static String getSpaceIdFromContentContext(String contextId) throws Exception {
		if (contextId == null || contextId.length() <= SmartWorks.CONTEXT_PREFIX_LENGTH)
			return null;
		return contextId.substring(SmartWorks.CONTEXT_PREFIX_LENGTH);
	}

	public static int getSpaceTypeFromContentContext(String contextId) throws Exception {
		if (contextId == null)
			return 0;
		if(contextId.length() == 5 && contextId.substring(0, 5).equals(ISmartWorks.CONTEXT_ALL_WORKS_LIST))
			return ISmartWorks.SPACE_TYPE_AWORK_LIST;
		if(contextId.length() < 6) return 0;
		if (contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_IWORK_SPACE)
				|| contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_PWORK_SPACE)
				|| contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_SWORK_SPACE)
				|| contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_FILE_SPACE)
				|| contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_IMAGE_SPACE)
				|| contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_EVENT_SPACE)
				|| contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_MEMO_SPACE)
				|| contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_BOARD_SPACE)
				|| contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_MAIL_SPACE))
			return ISmartWorks.SPACE_TYPE_WORK_INSTANCE;
		else if (contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_IWORK_LIST)
				|| contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_PWORK_LIST)
				|| contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_SWORK_LIST))
			return ISmartWorks.SPACE_TYPE_WORK_LIST;
		else if(contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_FILE_LIST))
			return ISmartWorks.SPACE_TYPE_FILE_LIST;
		else if(contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_IMAGE_LIST))
			return ISmartWorks.SPACE_TYPE_IMAGE_LIST;
		else if(contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_EVENT_LIST))
			return ISmartWorks.SPACE_TYPE_EVENT_LIST;
		else if(contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_MEMO_LIST))
			return ISmartWorks.SPACE_TYPE_MEMO_LIST;
		else if(contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_BOARD_LIST))
			return ISmartWorks.SPACE_TYPE_BOARD_LIST;
		else if(contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_MAIL_LIST))
			return ISmartWorks.SPACE_TYPE_MAIL_LIST;
		else if(contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_SAVED_LIST))
			return ISmartWorks.SPACE_TYPE_SAVED_LIST;
		else if(contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_REPORT_LIST))
			return ISmartWorks.SPACE_TYPE_REPORT_LIST;
		else if(contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_USER_SPACE))
			return ISmartWorks.SPACE_TYPE_USER;
		else if(contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_GROUP_SPACE))
			return ISmartWorks.SPACE_TYPE_GROUP;
		else if(contextId.substring(0, 6).equals(ISmartWorks.CONTEXT_PREFIX_DEPARTMENT_SPACE))
			return ISmartWorks.SPACE_TYPE_DEPARTMENT;
		return 0;
	}

	/* (non-Javadoc)
	 * @see net.smartworks.service.impl.ISmartWorks#getContextPrefixByWorkType(int, int)
	 */
	public static String getContextPrefixByWorkType(int smartWorkType, int spaceType) throws Exception {

		if (spaceType == SmartWorks.SPACE_TYPE_WORK_LIST) {
			if (smartWorkType == SmartWork.TYPE_INFORMATION)
				return SmartWorks.CONTEXT_PREFIX_IWORK_LIST;
			if (smartWorkType == SmartWork.TYPE_PROCESS)
				return SmartWorks.CONTEXT_PREFIX_PWORK_LIST;
			if (smartWorkType == SmartWork.TYPE_SCHEDULE)
				return SmartWorks.CONTEXT_PREFIX_SWORK_LIST;
		} else if(spaceType == SmartWorks.SPACE_TYPE_WORK_INSTANCE){
			if (smartWorkType == SmartWork.TYPE_INFORMATION)
				return SmartWorks.CONTEXT_PREFIX_IWORK_SPACE;
			if (smartWorkType == SmartWork.TYPE_PROCESS)
				return SmartWorks.CONTEXT_PREFIX_PWORK_SPACE;
			if (smartWorkType == SmartWork.TYPE_SCHEDULE)
				return SmartWorks.CONTEXT_PREFIX_SWORK_SPACE;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see net.smartworks.service.impl.ISmartWorks#getTargetContentByWorkType(int, int)
	 */
	public static String getTargetContentByWorkType(int smartWorkType, int spaceType) throws Exception {

		if (spaceType == SmartWorks.SPACE_TYPE_WORK_LIST) {
			if (smartWorkType == SmartWork.TYPE_INFORMATION)
				return "iwork_list.sw";
			if (smartWorkType == SmartWork.TYPE_PROCESS)
				return "pwork_list.sw";
			if (smartWorkType == SmartWork.TYPE_SCHEDULE)
				return "swork_list.sw";
		} else if(spaceType == SmartWorks.SPACE_TYPE_WORK_INSTANCE){
			if (smartWorkType == SmartWork.TYPE_INFORMATION)
				return "iwork_space.sw";
			if (smartWorkType == SmartWork.TYPE_PROCESS)
				return "pwork_space.sw";
			if (smartWorkType == SmartWork.TYPE_SCHEDULE)
				return "swork_space.sw";
		} else if(spaceType == SmartWorks.SPACE_TYPE_TASK_INSTANCE){
			if (smartWorkType == SmartWork.TYPE_INFORMATION)
				return "iwork_task.sw";
			if (smartWorkType == SmartWork.TYPE_PROCESS)
				return "pwork_task.sw";
			if (smartWorkType == SmartWork.TYPE_SCHEDULE)
				return "swork_task.sw";

		}
		return null;
	}
	
	public static String getLastHref(HttpServletRequest request) throws Exception{
		String lastLocation = (String)request.getSession().getAttribute("lastLocation");
		if(isBlankObject(lastLocation)) return "home.sw";
		
		if(		lastLocation.equals("home.sw") 
				 || lastLocation.equals("smartcaster.sw") 
				 || lastLocation.equals("dashboard.sw")){ 
			return lastLocation;
		}
		
		if(		lastLocation.equals("department_space.sw") 
				 || lastLocation.equals("group_space.sw") 
				 || lastLocation.equals("user_space.sw")){ 
			String cid = (String)request.getSession().getAttribute("cid");
			String wid = (String)request.getSession().getAttribute("wid");
			return lastLocation + "?cid=" + cid + "&wid=" + wid;
		}
			
		if(		lastLocation.equals("board_list.sw") 
				 || lastLocation.equals("memo_list.sw") 
				 || lastLocation.equals("file_list.sw") 
				 || lastLocation.equals("image_list.sw") 
				 || lastLocation.equals("event_list.sw") 
				 || lastLocation.equals("work_list.sw")){ 
			String cid = (String)request.getSession().getAttribute("cid");
			String wid = (String)request.getSession().getAttribute("wid");
			return lastLocation + "?cid=" + cid + "&wid=" + wid;
		}
			
		if(		lastLocation.equals("iwork_list.sw") 
				 || lastLocation.equals("pwork_list.sw") 
				 || lastLocation.equals("report_list.sw") 
				 || lastLocation.equals("mail_list.sw")){ 
			String cid = (String)request.getSession().getAttribute("cid");
			return lastLocation + "?cid=" + cid;
		}
			
		return "home.sw";
	}

	public static String[] getAllUserIdsFromUserArray(ArrayList<Map<String,String>> userArray) throws Exception{
		ArrayList<String> userIdList = new ArrayList<String>();
		for (int i = 0; i < userArray.size(); i++) {
			Map<String, String> userInfoMap = userArray.get(i);
			String comId = userInfoMap.get("id");
			if(SmartUtil.isEmailAddress(comId)){
				userIdList.add(comId);
			}else if(comId.startsWith(Group.GROUP_ID_PREFIX)){
				UserInfo[] groupUsers = SwServiceFactory.getInstance().getCommunityService().getGroupMembersById(comId, null, -1);
				if(!SmartUtil.isBlankObject(groupUsers)){
					for(UserInfo user : groupUsers){
						userIdList.add(user.getId());
					}
				}				
			}else{
				UserInfo[] departUsers = SwServiceFactory.getInstance().getCommunityService().getAllUsersByDepartmentId(comId);
				if(!SmartUtil.isBlankObject(departUsers)){
					for(UserInfo user : departUsers){
						userIdList.add(user.getId());
					}
				}
			}
		}
		if(SmartUtil.isBlankObject(userIdList))
			return null;
		
		String[] userIds = new String[userIdList.size()];
		for(int i=0; i<userIdList.size(); i++){
			userIds[i] = userIdList.get(i);
		}
		return userIds;
	}
		
	public static CompanyOption getCompanyOption() throws Exception{
		CompanyOption companyOption = new CompanyOption();
		// TO DO : Call the web service to get the company options registered in the Work App-Store
		return companyOption;
	}
	public static String getCurrentHref(HttpServletRequest request, String spaceId) throws Exception{
		if(isBlankObject(spaceId)) return "home.sw";
		
		String cid = (String)request.getSession().getAttribute("cid");
		String wid = (String)request.getSession().getAttribute("wid");
		return spaceId + "?cid=" + cid + "&wid=" + wid;
	}
	
	public static String combineStrings(String first, String second){
		if(SmartUtil.isBlankObject(first)) return second;
		if(SmartUtil.isBlankObject(second)) return first;
		String combined = first;
		String[] firstTokens = first.split(", ");
		String[] secondTokens = second.split(", ");
		if(!SmartUtil.isBlankObject(firstTokens) && !SmartUtil.isBlankObject(secondTokens)){
			for(int i=0; i<secondTokens.length; i++){
				boolean found = false;
				for(int j=0; j<firstTokens.length; j++){
					if(firstTokens[j].equals(secondTokens[i])){
						found = true;
						break;
					}
				}
				if(!found){
					combined = combined + ", " + secondTokens[i]; 
				}
			}
		}
		return combined;
	}

	 public static String escape(String src) {   
		 int i;   
		 char j;   
		 StringBuffer tmp = new StringBuffer();   
		 tmp.ensureCapacity(src.length() * 6);   
		 for (i = 0; i < src.length(); i++) {   
			 j = src.charAt(i);   
			 if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j))   
				 tmp.append(j);   
			 else if (j < 256) {   
				 tmp.append("%");   
				 if (j < 16)   
					 tmp.append("0");   
				 tmp.append(Integer.toString(j, 16));   
			 } else {   
				 tmp.append("%u");   
				 tmp.append(Integer.toString(j, 16));   
			 }   
		 }   
		 return tmp.toString();   
	 }  

	 public static String unescape(String src) {   
		 StringBuffer tmp = new StringBuffer();   
		 tmp.ensureCapacity(src.length());   
		 int lastPos = 0, pos = 0;   
		 char ch;   
		 while (lastPos < src.length()) {   
			 pos = src.indexOf("%", lastPos);   
			 if (pos == lastPos) {   
				 if (src.charAt(pos + 1) == 'u') {   
					 ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);   
					 tmp.append(ch);   
					 lastPos = pos + 6;   
				 } else {   
					 ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);   
					 tmp.append(ch);   
					 lastPos = pos + 3;   
				 }   
			 } else {   
				 if (pos == -1) {   
					 tmp.append(src.substring(lastPos));   
					 lastPos = src.length();   
				 } else {   
					 tmp.append(src.substring(lastPos, pos));   
					 lastPos = pos;   
				 }   
			 }   
		 }   
		 return tmp.toString();   
	 }    

	/* (non-Javadoc)
	 * @see net.smartworks.service.impl.ISmartWorks#getCurrentUser()
	 */
	public static User getCurrentUserOld() throws Exception {
		User user = new User();
		user.setId("jisook@maninsoft.co.kr");
		user.setName("김지숙");
		user.setPosition("CEO");
		user.setDepartment("경영기획실 디자인팀");
		user.setLocale("ko"); // ko, en
		user.setTimeZone(LocalDate.TIMEZONE_SEOUL); //Asia/Seoul, America/Los_Angeles
		user.setCompany("(주)맨인소프트");
		user.setUserLevel(User.USER_LEVEL_AMINISTRATOR);

		return user;
	}

	public static Login getCurrentUser(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Login user = new Login();
		SecurityContext context = (SecurityContext) request.getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
		if(context != null) {
			Authentication auth = context.getAuthentication();
			if(auth != null) {
				if(((Login)auth.getPrincipal()).getId() != null){
					user = (Login)auth.getPrincipal();
				}else{
					response.sendRedirect("logout");
				}
			} else {
				response.sendRedirect("logout");
			}
		}

		return user;
	}

	public static User getCurrentUser() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		if(authentication != null) {
			Object principal = authentication.getPrincipal();
			if(!principal.equals("anonymousUser")) {
				Login login = (Login)(principal instanceof Login ? principal : null);
				User user = new User();
				if(login != null) {
					user.setId(login.getId());
					user.setName(login.getName());
					user.setNickName(login.getNickName());
					user.setPassword(login.getPassword());
					user.setCompanyId(login.getCompanyId());
					user.setCompany(login.getCompany());
					user.setDepartmentId(login.getDepartmentId());
					user.setDepartments(login.getDepartments());
					user.setDepartment(login.getDepartment());
					user.setPosition(login.getPosition());
					user.setBigPictureName(login.getBigPictureName());
					user.setSmallPictureName(login.getSmallPictureName());
					user.setSignPicture(login.getSignPictureName());
					user.setLocale(login.getLocale());
					user.setTimeZone(login.getTimeZone());
					user.setRole(login.getRole());
					user.setUserLevel(login.getUserLevel());
					user.setPhoneNo(login.getPhoneNo());
					user.setCellPhoneNo(login.getCellPhoneNo());
					user.setEmployeeId(login.getEmpNo());
					user.setUseMail(login.isUseMail());
					user.setUseSignPicture(login.isUseSign());
					if (!CommonUtil.isEmpty(login.getMailServerId())) {
						MailAccount[] mailAccounts = new MailAccount[1];
						MailAccount mailAccount = new MailAccount();
						mailAccount.setEmailServerId(login.getMailServerId());
						mailAccount.setEmailServerName(login.getMailServerName());
						mailAccount.setPassword(login.getMailPassword());
						mailAccount.setUserName(login.getMailId() + "@" + login.getMailServerName());
						mailAccounts[0] = mailAccount;
						user.setMailAccounts(mailAccounts);
					}
					user.setHireDate(login.getHireDate());
					user.setBirthday(login.getBirthday());
					user.setLunarBirthday(login.isLunarBirthday());
					user.setHomePhoneNo(login.getHomePhoneNo());
					user.setHomeAddress(login.getHomeAddress());
				}
				return user;
			} else {
				Login login = (Login)(principal instanceof Login ? principal : null);
				if (login != null) {
					return SmartUtil.getAnonymousUser(login.getCompanyId(), login.getCompany());
				} else {
					return SmartUtil.getAnonymousUser("", "");
				}
			}
		}
		return SmartUtil.getAnonymousUser("", "");
	}
	
	public static User getSystemUser(){

		User user = new User();
		user.setId(User.USER_ID_SYSTEM);
		user.setName(SmartMessage.getString("common.title.system_user"));
		user.setPosition("");
		user.setDepartment("");
		user.setCompany("");
		user.setUserLevel(User.USER_LEVEL_AMINISTRATOR);
		return user;
	}
	
	public static User getAnonymousUser(String companyId, String company){

		User user = new User();
		user.setId(User.USER_ID_ANONYMOUS);
		user.setName("Anonymous User");
		user.setPosition("");
		user.setDepartment("");
		user.setCompanyId(companyId);
		user.setCompany(company);
		user.setUserLevel(User.USER_LEVEL_EXTERNAL_USER);
		return user;
	}
	
	public static String getUserDetailInfo(UserInfo user){
		if(SmartUtil.isBlankObject(user)) return "";
		String roleStr = (user.getRole() == User.USER_ROLE_LEADER) ? SmartMessage.getString("department.role.head") : SmartMessage.getString("department.role.member");
		String info = "<div><span class='smartp_name'>" + user.getLongName() + "</span><div>";
		if(!SmartUtil.isBlankObject(user.getDepartment())){
			info = info + "<div class='smartp_info'><span>" + SmartMessage.getString("profile.title.department") + ":</span> " + CommonUtil.toNotNull(user.getFullDepartment()) + " (" + roleStr + ")</div>";
		}
		if(!SmartUtil.isBlankObject(user.getPhoneNo())){
			info = info + "<div class='smartp_info'><span>" + SmartMessage.getString("profile.title.phone_no") + ":</span> " + user.getPhoneNo() + "</div>";
		}
		if(!SmartUtil.isBlankObject(user.getCellPhoneNo())){		
			info = info + "<div class='smartp_info'><span>" + SmartMessage.getString("profile.title.cell_phone_no") + ":</span> " + user.getCellPhoneNo() + "</div>";
		}
		info = info + "<div class='smartp_info'><span>" + SmartMessage.getString("profile.title.email") + ":</span> " + user.getId() + "</div>";
		
		return info;
	}
	
	public static String getUsersHtml(UserInfo[] users){
		if(SmartUtil.isBlankObject(users)) return "";
		String usersHtml = "";
		for(int i=0; i<users.length; i++){
			UserInfo user = users[i];
			usersHtml = usersHtml + "<a href='user_space.sw?cid=us.sp." + user.getId() + "'><span>" + user.getLongName() + ((i<users.length-1) ? ", " : "") + "</span></a>";			
		}
		return usersHtml;
	}
	
	public static String getFilesDetailInfo(List<Map<String, String>> files, String workId, String taskInstId, String recordId){
		String html = "<ul>";
		if(SmartUtil.isBlankObject(files)) return html;
		for(int i=0; i<files.size(); i++){
			Map<String, String> file = (Map<String, String>)files.get(i);
			String fileId = file.get("fileId");
			String fileName = file.get("fileName");
			String fileType = file.get("fileType");
			String fileSize = file.get("fileSize");
			long size = (SmartUtil.isBlankObject(fileSize)) ? 0 : Long.parseLong(fileSize);
			html = html + "<li><span class='vm icon_file_" + (SmartUtil.isBlankObject(fileType) ? "none" : fileType.toLowerCase()) + "'></span><a href='download_file.sw?fileId=" + fileId + 
					"&fileName=" + fileName + "&workId=" + workId + "&taskInstId=" + taskInstId + "&recordId=" + recordId + "' class='qq-upload-file'>" + fileName + "</a><span class='qq-upload-size'>" + SmartUtil.getBytesAsString(size) + "</span></li>";
		}
		return html = html + "</ul>";
	}

	private static final String[] Q = new String[]{" B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};
	public static String getBytesAsString(long bytes){
	    for (int i = 6; i > 0; i--)
	    {
	        double step = Math.pow(1024, i);
	        if (bytes > step) return String.format("%3.1f %s", bytes / step, Q[i]);
	    }
	    return Long.toString(bytes) + Q[0];
	}

	public static String getDepartNameFromFullpath(String departFullpathName){
		if(SmartUtil.isBlankObject(departFullpathName)) return "";
		String[] tokens = departFullpathName.split("▶");
		return tokens[tokens.length-1];
	}
	
	public static String getFileExtension(String filename){
		String extension = "none";
		if(SmartUtil.isBlankObject(filename)) return extension;
		int pos = filename.lastIndexOf('.');
		String[] extTypes = new String[]{"asf", "avi", "bmp", "doc", "docx", "exe", "gif", "hwp", "jpg", "mid", "mp3",
				"mpeg", "mpg", "pdf", "pds", "ppt", "pptx", "rar", "txt", "wav", "wma", "wmv", "word", "xls", "xlsx", "zip"};
		if(pos != -1) {
			String extTemp = filename.substring( pos + 1, filename.length()).toLowerCase();
			for(int i=0; i<extTypes.length; i++) {
				if(extTypes[i].equals(extTemp))
					extension = extTemp;
			}
		}
		return extension;
		
	}
	
	public static boolean isMailFileName(byte[] bytes){
		if(bytes.length<37) return false;
		byte[] tempBytes = new byte[37];
		for(int i=0; i<37; i++)
			tempBytes[i] = bytes[i];
		String fileName = new String(tempBytes);
		if(fileName.startsWith("mail_")) return true;
		return false;
	}
	
	public static String getSubjectString(String userId){
		if (CommonUtil.isEmpty(userId))
			return null;
		return userId.replace('.' , '_');
	}
	
	public static String smartEncode(String value){
		if(SmartUtil.isBlankObject(value)) return value;
		value = value.replaceAll("\"", "&quot;");
		value = value.replaceAll("\'", "&squo;");
		value = value.replaceAll("<", "&lt;");
		value = value.replaceAll(">", "&gt;");
		return value;
	}
	
	public static String smartDecode(String value){
		if(SmartUtil.isBlankObject(value)) return value;
		value = value.replaceAll("&quot;", "\"");
		value = value.replaceAll("&squo;", "\'");
		value = value.replaceAll("&lt;", "<");
		value = value.replaceAll("&gt;", ">");
		return value;
	}
	
	public static boolean isBlankObject(Object obj){
		if(obj==null) return true;
		if(obj.equals("null")) return true;
		if(obj.getClass().equals(String.class)) return StringUtils.isEmpty((String)obj);
		if(obj.getClass().isArray()) return (obj==null || Array.getLength(obj)==0) ? true : false;
		return false;
	}

	public static boolean isEmpty(String str){
		return (str == null || str.length()==0) ? true : false;
	}
	
	final static String TILES_POSTFIX = ".sw"; 
	public static boolean isTilesLocation(String str){
		if(SmartUtil.isBlankObject(str) || str.length() <= 3) return false;
		if(TILES_POSTFIX.equals(str.substring(str.length()-TILES_POSTFIX.length(), str.length())) )return true;
		return false;
	}
	
	public static boolean isEmailAddress(String str){
		
		if(SmartUtil.isBlankObject(str)) return false;
		
//		  Pattern pattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		return Pattern.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[_A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", str);
	}
	
	private static final String SUBJECT_SMARTWORKS = "/smartworks";
	private static final String SUBJECT_BROADCASTING = "broadcasting";
	private static final String SUBJECT_ONLINE = "online";
	private static final String SUBJECT_OFFLINE = "offline";
	private static final String SUBJECT_ALL = "*";
	private static final String MSG_TYPE_BROADCASTING = "BCAST";
	private static final String MSG_TYPE_NOTICE_COUNT = "NCOUNT";
	private static final String MSG_TYPE_AVAILABLE_CHATTERS = "ACHATTERS";
	private static final String MSG_TYPE_EVENT_ALARM = "EVENTALARM";
	
	private static String getMessageChannel(String channel){
		return SUBJECT_SMARTWORKS + "/" + SmartUtil.getCurrentUser().getCompanyId() + "/" + channel; 
	}

	private static String getMessageChannel(String companyId, String channel){
		return SUBJECT_SMARTWORKS + "/" + companyId + "/" + channel; 
	}
	
	public static void publishBcast(String[] messages){
		publishMessage(getMessageChannel(SUBJECT_BROADCASTING), MSG_TYPE_BROADCASTING, messages);
	}

	public static void publishCurrent(String userId, int type) throws Exception {

		Notice[] notices = SwServiceFactory.getInstance().getNoticeService().getNotices(userId, type);

		if(!CommonUtil.isEmpty(notices)) {
			Notice notice = notices[0];
			publishNoticeCount(userId, notice);
		}
	}

	public static void publishAChatters(UserInfo[] users){

		List<Map<String, Object>> userInfos = new ArrayList<Map<String,Object>>();
		if(!SmartUtil.isBlankObject(users)) {
			for(int i=0; i<users.length; i++){
				UserInfo user = users[i];
				Map<String, Object> userInfo = new HashMap<String, Object>();
				userInfo.put("userId", user.getId());
				userInfo.put("longName", user.getLongName());
				userInfo.put("nickName", user.getNickName());
				userInfo.put("minPicture", user.getMinPicture());
				userInfos.add(userInfo);
			}
		}
		Map<String, List<Map<String, Object>>> data = new HashMap<String, List<Map<String, Object>>>();
		data.put("userInfos", userInfos);
		publishMessage(getMessageChannel(SUBJECT_BROADCASTING), MSG_TYPE_AVAILABLE_CHATTERS, userInfos);		
	}
	public static void publishAChatters(String companyId, UserInfo[] users){

		List<Map<String, Object>> userInfos = new ArrayList<Map<String,Object>>();
		if(!SmartUtil.isBlankObject(users)) {
			for(int i=0; i<users.length; i++){
				UserInfo user = users[i];
				Map<String, Object> userInfo = new HashMap<String, Object>();
				userInfo.put("userId", user.getId());
				userInfo.put("longName", user.getLongName());
				userInfo.put("nickName", user.getNickName());
				userInfo.put("minPicture", user.getMinPicture());
				userInfos.add(userInfo);
			}
		}
		Map<String, List<Map<String, Object>>> data = new HashMap<String, List<Map<String, Object>>>();
		data.put("userInfos", userInfos);
		publishMessage(getMessageChannel(companyId, SUBJECT_BROADCASTING), MSG_TYPE_AVAILABLE_CHATTERS, userInfos);		
	}
	public static void removeNoticeByExecutedTaskId(String targetUserId, String taskId) throws Exception {
		
		User user = SmartUtil.getCurrentUser();
		PublishNoticeCond pubNotiCond = new PublishNoticeCond();
		pubNotiCond.setRefId(taskId);

		PublishNotice notice = SwManagerFactory.getInstance().getPublishNoticeManager().getPublishNotice(user.getId(), pubNotiCond, null);
		if (CommonUtil.isEmpty(notice))
			return;
		String assignee = notice.getAssignee();
		if (!targetUserId.equalsIgnoreCase(assignee))
			return;
		String objId = notice.getObjId();
		int noticeType = notice.getType();
		
		SwoUser targetUser = SwManagerFactory.getInstance().getSwoManager().getUser("", assignee, IManager.LEVEL_ALL);
		SwManagerFactory.getInstance().getPublishNoticeManager().removePublishNotice(user.getId(), objId);
		SmartUtil.increaseNoticeCountByNoticeType(assignee, noticeType);
	}
	public static void increaseNoticeCountByNoticeType(String targetUserId, int noticeType) throws Exception {
		if (noticeType == Notice.TYPE_INVALID)
			return;
		Notice[] notice = SwServiceFactory.getInstance().getNoticeService().getNotices(targetUserId, noticeType);
		if (CommonUtil.isEmpty(notice))
			return;
		publishNoticeCount(targetUserId, notice[0]);
	}
	public static void publishNoticeCount(String userId, Notice message){
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("type", message.getType());
		data.put("count", message.getLength());
		try{
			SwoUser targetUser = SwManagerFactory.getInstance().getSwoManager().getUser("", userId, IManager.LEVEL_ALL);
			publishMessage( getMessageChannel(targetUser.getCompanyId(), SmartUtil.getSubjectString(userId)), MSG_TYPE_NOTICE_COUNT, data);
		}catch (Exception e){}
	}
	
	public static void publishNoticeCount(String userId, String companyId, Notice message){
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("type", message.getType());
		data.put("count", message.getLength());
		publishMessage( getMessageChannel(companyId, SmartUtil.getSubjectString(userId)), MSG_TYPE_NOTICE_COUNT, data);
	}
	
	public static void pushEventAlarm(String userId, String companyId, NoticeMessage message){
		Map<String, Object> data = new HashMap<String, Object>();
		if(SmartUtil.isBlankObject(message) || message.getType() != NoticeMessage.TYPE_EVENT_ALARM || SmartUtil.isBlankObject(message.getEvent())) return;
		data.put("type", message.getType());
		data.put("id", message.getId());
		data.put("issuedDate", message.getIssuedDate());
		
		EventInstanceInfo event = message.getEvent();
				
		Map<String, Object> ownerInfo = new HashMap<String, Object>();
		ownerInfo.put("midPicture", event.getOwner().getMidPicture());
		ownerInfo.put("longName", event.getOwner().getLongName());
		ownerInfo.put("id", event.getOwner().getId());
		
		Map<String, Object> eventInfo = new HashMap<String, Object>();
		eventInfo.put("owner", ownerInfo);
		eventInfo.put("startTitle", SmartMessage.getString("common.upload.event.start_date"));
		eventInfo.put("start", event.getStart());
		eventInfo.put("subject", event.getSubject());
		eventInfo.put("controller", WorkInstanceInfo.getController(SmartWork.ID_EVENT_MANAGEMENT, SmartWork.TYPE_INFORMATION));
		eventInfo.put("contextId", WorkInstanceInfo.getContextId(SmartWork.ID_EVENT_MANAGEMENT, SmartWork.TYPE_INFORMATION, event.getId()));
		eventInfo.put("workSpaceId", event.getWorkSpaceId());
		eventInfo.put("workSpaceName", event.getWorkSpaceName());
		eventInfo.put("workId", event.getWorkId());
		data.put("event", eventInfo);
		
		publishMessage( getMessageChannel(companyId, SmartUtil.getSubjectString(userId)), MSG_TYPE_EVENT_ALARM, data);
	}
	
	public static void updateChatterStatus(boolean isOnline, String userId, String companyId){
		
		try{
			UserInfo[] userInfos = null;
			List<UserInfo> userInfoList = new ArrayList<UserInfo>();
			
			ILoginUserManager loginMgr = SwManagerFactory.getInstance().getLoginUserManager();
			
			LoginUser targetLoginUser = loginMgr.getLoginUser(userId, userId, null);
			if (CommonUtil.isEmpty(targetLoginUser)) {
				if (isOnline) {
					LoginUser newLoginUser = new LoginUser();
					newLoginUser.setUserId(userId);
					loginMgr.setLoginUser(userId, newLoginUser);
				}
			} else {
				if (!isOnline) {
					LoginUserCond cond = new LoginUserCond();
					cond.setUserId(targetLoginUser.getUserId());
					loginMgr.removeLoginUser(userId, cond);
				}
			}
			
			LoginUser[] loginUsers = loginMgr.getLoginUsers(userId, null, IManager.LEVEL_LITE);

			if(!CommonUtil.isEmpty(loginUsers)) {
				for(LoginUser loginUser : loginUsers) {
					String loginId = loginUser.getUserId();
					UserInfo userInfo = ModelConverter.getUserInfoByUserId(loginId);
					userInfo.setOnline(true);
					userInfoList.add(userInfo);
				}
			}
			if(userInfoList.size() > 0) {
				userInfos = new UserInfo[userInfoList.size()];
				userInfoList.toArray(userInfos);
			}
			//체터리스트 갱신 (publishAChatters)
			publishAChatters(companyId, userInfos);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	static Thread messageAgent = null;
	static ClientSession fayeClient = null;
	static List<MessageModel> messageQueue = new LinkedList<MessageModel>();
	
	
	private synchronized static void initializeFayeClient(){

		try{
			ISettingsService settingsService = SwServiceFactory.getInstance().getSettingsService();
			if(settingsService!=null){
				CompanyGeneral companyGeneral = settingsService.getCompanyGeneral();
				if(!companyGeneral.isUseMessagingService() && !companyGeneral.isUseChattingService()) return;				
			}
			
			HttpClient httpClient = new HttpClient();
			httpClient.start();
			Map<String, Object> options = new HashMap<String, Object>();
			ClientTransport transport = LongPollingTransport.create(options, httpClient);
			fayeClient = new BayeuxClient("http://localhost:8011/faye", transport);
			fayeClient.handshake();
		}catch (Exception e){
			e.printStackTrace();
			return;
		}
		System.out.println("Faye Client has been created successfully !!!");		
		
	}
	public synchronized static void publishMessage(String channel, String msgType, Object message){

		if(fayeClient == null){
			SmartUtil.initializeFayeClient();
		}

		if(fayeClient != null){
			if(messageAgent == null) {
				messageAgent = new Thread(new Runnable() {
					public void run() {
						try{
							MessageModel message = null;
	
							while(true) {
								try {
									message = null;								
									while(message == null) {
										try {
											message = messageQueue.remove(0);
										} catch(Exception e) {
											Thread.sleep(1000);
										}
									}
									
 									Map<String, Object> data = new HashMap<String, Object>();
									data.put("msgType", message.msgType);
									data.put("sender", "smartServer");
									data.put("body", message.message);
									fayeClient.getChannel(message.channel).publish(data);
								} catch(Exception e){
									//e.printStackTrace();
								}
							}
						}catch(Exception e){
							//e.printStackTrace();
						}
					}
				});
				messageAgent.start();
			}
	
			messageQueue.add(new MessageModel(channel, msgType, message));
		}
	}
}

class MessageModel {
	MessageModel(String channel, String msgType, Object message) {
		this.channel = channel;
		this.msgType = msgType;
		this.message = message;
	}
	
	protected String channel;
	protected String msgType;
	protected Object message;
}

