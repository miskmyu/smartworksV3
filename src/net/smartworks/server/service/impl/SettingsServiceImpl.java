package net.smartworks.server.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.Part;
import javax.wsdl.PortType;
import javax.xml.namespace.QName;

import net.smartworks.model.RecordList;
import net.smartworks.model.approval.Approval;
import net.smartworks.model.approval.ApprovalLine;
import net.smartworks.model.calendar.CompanyEvent;
import net.smartworks.model.calendar.WorkHour;
import net.smartworks.model.calendar.WorkHourPolicy;
import net.smartworks.model.community.Community;
import net.smartworks.model.community.User;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.company.CompanyGeneral;
import net.smartworks.model.instance.info.InstanceInfoList;
import net.smartworks.model.instance.info.RequestParams;
import net.smartworks.model.mail.EmailServer;
import net.smartworks.model.mail.MailAccount;
import net.smartworks.model.service.ExternalForm;
import net.smartworks.model.service.Variable;
import net.smartworks.model.service.WSDLDetail;
import net.smartworks.model.service.WSDLOperation;
import net.smartworks.model.service.WSDLPort;
import net.smartworks.model.service.WebService;
import net.smartworks.model.work.info.UsedWorkInfo;
import net.smartworks.server.engine.authority.model.SwaDepartment;
import net.smartworks.server.engine.authority.model.SwaDepartmentCond;
import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.model.Order;
import net.smartworks.server.engine.common.model.SmartServerConstant;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.common.util.id.IDCreator;
import net.smartworks.server.engine.config.manager.ISwcManager;
import net.smartworks.server.engine.config.model.SwcEventDay;
import net.smartworks.server.engine.config.model.SwcEventDayCond;
import net.smartworks.server.engine.config.model.SwcExternalForm;
import net.smartworks.server.engine.config.model.SwcExternalFormCond;
import net.smartworks.server.engine.config.model.SwcExternalFormParameter;
import net.smartworks.server.engine.config.model.SwcWebService;
import net.smartworks.server.engine.config.model.SwcWebServiceCond;
import net.smartworks.server.engine.config.model.SwcWebServiceParameter;
import net.smartworks.server.engine.config.model.SwcWorkHour;
import net.smartworks.server.engine.config.model.SwcWorkHourCond;
import net.smartworks.server.engine.docfile.manager.IDocFileManager;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.mail.manager.IMailManager;
import net.smartworks.server.engine.mail.model.MailServer;
import net.smartworks.server.engine.mail.model.MailServerCond;
import net.smartworks.server.engine.organization.manager.ISwoManager;
import net.smartworks.server.engine.organization.model.SwoCompany;
import net.smartworks.server.engine.organization.model.SwoConfig;
import net.smartworks.server.engine.organization.model.SwoDepartment;
import net.smartworks.server.engine.organization.model.SwoUser;
import net.smartworks.server.engine.organization.model.SwoUserCond;
import net.smartworks.server.engine.process.approval.manager.IAprManager;
import net.smartworks.server.engine.process.approval.model.AprApprovalDef;
import net.smartworks.server.engine.process.approval.model.AprApprovalLineDef;
import net.smartworks.server.engine.process.approval.model.AprApprovalLineDefCond;
import net.smartworks.server.engine.process.task.manager.impl.TskManagerMailAdvisorImpl;
import net.smartworks.server.service.ICommunityService;
import net.smartworks.server.service.ISettingsService;
import net.smartworks.server.service.util.ModelConverter;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartTest;
import net.smartworks.util.SmartUtil;

import org.claros.commons.mail.models.ConnectionProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@Service
public class SettingsServiceImpl implements ISettingsService {

	private static ISwoManager getSwoManager() {
		return SwManagerFactory.getInstance().getSwoManager();
	}
	private static ISwcManager getSwcManager() {
		return SwManagerFactory.getInstance().getSwcManager();
	}
	private static IAprManager getAprManager() {
		return SwManagerFactory.getInstance().getAprManager();
	}
	private static IDocFileManager getDocManager() {
		return SwManagerFactory.getInstance().getDocManager();
	}
	private static IMailManager getMailManager() {
		return SwManagerFactory.getInstance().getMailManager();
	}

	@Autowired
	private ICommunityService communityService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.smartworks.service.impl.ISmartWorks#getMyDepartments(java.lang.String
	 * )
	 */
	@Override
	public CompanyGeneral getCompanyGeneral() throws Exception {

		try {
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			String companyId = cUser.getCompanyId();
			if(CommonUtil.isEmpty(companyId)) {
				SwoCompany[] swoCompanies = getSwoManager().getCompanys(null, null, null);
				if(!CommonUtil.isEmpty(swoCompanies))
					companyId = swoCompanies[0].getId();
			}
			SwoCompany swoCompany = getSwoManager().getCompany(userId, companyId, IManager.LEVEL_ALL);
			SwoConfig swoConfig = getSwoManager().getConfig(userId, companyId, IManager.LEVEL_ALL);
			String id = "";
			String name = "";
			String logoName = "";
			String loginImageName = "";
			String sendMailHost = "";
			String sendMailAccount = "";
			String sendMailPassword = "";
			boolean sendMailNotification = false;
			boolean useMessagingService = true;
			boolean useChattingService = false;
			boolean userReturnFunction = true;
			LocalDate startDate = null;
			if(swoCompany != null) {
				id = swoCompany.getId();
				name = swoCompany.getName();
			}
			logoName = CommonUtil.toNotNull(getSwoManager().getLogo(userId, companyId));
			loginImageName = CommonUtil.toNotNull(getSwoManager().getLoginImage(userId, companyId));
			if(swoConfig != null) {
				sendMailHost = swoConfig.getSmtpAddress();
				sendMailAccount = swoConfig.getUserId();
				sendMailPassword = swoConfig.getPassword();
				sendMailNotification = swoConfig.isActivity();
				useMessagingService = swoConfig.isUseMessagingService();
				useChattingService = swoConfig.isUseChattingService();
				userReturnFunction = swoConfig.isUserReturnFunction();
				if(swoConfig.getSetupCompanyDate()!=null){
					startDate = new LocalDate(swoConfig.getSetupCompanyDate().getTime());
				}
			}

			CompanyGeneral companyGeneral = new CompanyGeneral();
			companyGeneral.setId(id);
			companyGeneral.setName(name);
			companyGeneral.setLogoName(logoName);
			companyGeneral.setLoginImageName(loginImageName);
			companyGeneral.setSendMailHost(sendMailHost);
			companyGeneral.setSendMailAccount(sendMailAccount);
			companyGeneral.setSendMailPassword(sendMailPassword);
			companyGeneral.setSendMailNotification(sendMailNotification);
			companyGeneral.setTestAfterSaving(true);
			companyGeneral.setUseMessagingService(useMessagingService);
			companyGeneral.setUseChattingService(useChattingService);
			companyGeneral.setUseReturnFunction(userReturnFunction);
			companyGeneral.setStartDate(startDate);
			return companyGeneral;
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}		
	}

	@Override
	public void setCompanyGeneral(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try {
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			String companyId = cUser.getCompanyId();
			String companyName = cUser.getCompany();

			Map<String, Object> frmCompanyGeneral = (Map<String, Object>)requestBody.get("frmCompanyGeneral");
			
			Set<String> keySet = frmCompanyGeneral.keySet();
			Iterator<String> itr = keySet.iterator();
			List<Map<String, String>> logoFiles = null;
			List<Map<String, String>> loginImageFiles = null;
			String txtMailHost = null;
			String txtMailAccount = null;
			String pasMailPassword = null;
			boolean isActivity = false;
			boolean useMessagingService = false;
			boolean useChattingService = false;
			boolean chkUseReturnFunction = false;
			String imgCompanyLogo = null;
			String imgCompanyLoginImage = null;
			String companyFileId = null;
			String companyFileName = null;
			String startYear = null;
			String startMonth = null;
			String startDay = null;
			
			while(itr.hasNext()) {
				String fieldId = (String)itr.next();
				Object fieldValue = frmCompanyGeneral.get(fieldId);
				if(fieldValue instanceof LinkedHashMap) {
					Map<String, Object> valueMap = (Map<String, Object>)fieldValue;
					if(fieldId.equals("imgCompanyLogo")) {
						logoFiles = (ArrayList<Map<String,String>>)valueMap.get("files");
					} else if(fieldId.equals("imgCompanyLoginImage")) {
						loginImageFiles = (ArrayList<Map<String,String>>)valueMap.get("files");
					}
				} else if(fieldValue instanceof String) {					
					if(fieldId.equals("txtMailHost")) {
						txtMailHost = (String)frmCompanyGeneral.get("txtMailHost");
					} else if(fieldId.equals("txtMailAccount")) {
						txtMailAccount = (String)frmCompanyGeneral.get("txtMailAccount");
					} else if(fieldId.equals("pasMailPassword")) {
						pasMailPassword = (String)frmCompanyGeneral.get("pasMailPassword");
					} else if(fieldId.equals("chkMailNotification")) {
						isActivity = true;
					} else if(fieldId.equals("chkUseMessagingService")) {
						useMessagingService = true;
					} else if(fieldId.equals("chkUseChattingService")){
						useChattingService = true;
					} else if (fieldId.equals("chkUseReturnFunction")) {
						chkUseReturnFunction = true;
					} else if (fieldId.equals("txtCompanyStartYear")) {
						startYear = (String)frmCompanyGeneral.get("txtCompanyStartYear");
					} else if (fieldId.equals("txtCompanyStartMonth")) {
						startMonth = (String)frmCompanyGeneral.get("txtCompanyStartMonth");
					} else if (fieldId.equals("txtCompanyStartDay")) {
						startDay = (String)frmCompanyGeneral.get("txtCompanyStartDay");
					}
				}
			}

			if(!logoFiles.isEmpty()) {
				for(int i=0; i < logoFiles.subList(0, logoFiles.size()).size(); i++) {
					Map<String, String> fileMap = logoFiles.get(i);
					companyFileId = fileMap.get("fileId");
					companyFileName = fileMap.get("fileName");
					imgCompanyLogo = getDocManager().insertProfilesFile(companyFileId, companyFileName, companyId + CompanyGeneral.IMAGE_TYPE_LOGO);
					getSwoManager().setLogo(userId, companyId, imgCompanyLogo);
				}
			}
			if(!loginImageFiles.isEmpty()) {
				for(int i=0; i < loginImageFiles.subList(0, loginImageFiles.size()).size(); i++) {
					Map<String, String> fileMap = loginImageFiles.get(i);
					companyFileId = fileMap.get("fileId");
					companyFileName = fileMap.get("fileName");
					imgCompanyLoginImage = getDocManager().insertProfilesFile(companyFileId, companyFileName, companyId + CompanyGeneral.IMAGE_TYPE_LOGINIMAGE);
					getSwoManager().setLoginImage(userId, companyId, imgCompanyLoginImage);
				}
			}

			SwoConfig swoConfig = getSwoManager().getConfig(userId, companyId, IManager.LEVEL_ALL);

			if(swoConfig == null)
				swoConfig = new SwoConfig();

			swoConfig.setId(companyId);
			swoConfig.setName(companyName);
			swoConfig.setCompanyId(companyId);
			swoConfig.setSmtpAddress(txtMailHost);
			swoConfig.setUserId(txtMailAccount);
			swoConfig.setPassword(pasMailPassword);
			swoConfig.setActivity(isActivity);
			swoConfig.setUseMessagingService(useMessagingService);
			swoConfig.setUseChattingService(useChattingService);
			swoConfig.setUserReturnFunction(chkUseReturnFunction);
			if(startYear!=null && startYear.length()==4 && startMonth!=null && startDay!=null){
				if(startMonth.length()==1) startMonth="0"+startMonth;
				if(startDay.length()==1) startDay="0"+startDay;
				try{
					swoConfig.setsetupCompanyDate(new Date(LocalDate.convertLocalDateStringToLocalDate1(startYear+startMonth+startDay).getTime()));
				}catch (Exception e){
					swoConfig.setsetupCompanyDate(null);
				}				
			}else{
				swoConfig.setsetupCompanyDate(null);				
			}
			getSwoManager().setConfig(userId, swoConfig, IManager.LEVEL_ALL);
			
			String chkTestAfterSaving = (String)frmCompanyGeneral.get("chkTestAfterSaving");
			if (!CommonUtil.isEmpty(chkTestAfterSaving) && chkTestAfterSaving.equalsIgnoreCase("on")) {
				WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
				TskManagerMailAdvisorImpl mailAdvisor = (TskManagerMailAdvisorImpl) wac.getBean("tskManagerMailAdvisor");
				mailAdvisor.sendMailByUserInfo(userId, userId, userId, "[SMARTWORKS]COMPANY MAIL SYSTEM CHECK", "[" + cUser.getCompany() + "] COMPANY MAIL SYSTEM CHECK RESULT : OK!");
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();			
		}
	}

	int previousPageSize = 0;
	@Override
	public RecordList getWorkHourPolicyList(RequestParams params) throws Exception {

		try {
			RecordList recordList = new RecordList();
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			String companyId = cUser.getCompanyId();

			SwcWorkHourCond swcWorkHourCond = new SwcWorkHourCond();
			swcWorkHourCond.setCompanyId(companyId);

			long totalCount = getSwcManager().getWorkhourSize(userId, swcWorkHourCond);

			int pageSize = params.getPageSize();
			if(pageSize == 0) pageSize = 20;

			int currentPage = params.getCurrentPage();
			if(currentPage == 0) currentPage = 1;

			int totalPages = (int)totalCount % pageSize;

			if(totalPages == 0)
				totalPages = (int)totalCount / pageSize;
			else
				totalPages = (int)totalCount / pageSize + 1;

			int result = 0;

			if(params.getPagingAction() != 0) {
				if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXT10) {
					result = (((currentPage - 1) / 10) * 10) + 11;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXTEND) {
					result = totalPages;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREV10) {
					result = ((currentPage - 1) / 10) * 10;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREVEND) {
					result = 1;
				}
				currentPage = result;
			}

			if(previousPageSize != pageSize)
				currentPage = 1;

			previousPageSize = pageSize;

			if((long)((pageSize * (currentPage - 1)) + 1) > totalCount)
				currentPage = 1;

			if (currentPage > 0)
				swcWorkHourCond.setPageNo(currentPage-1);

			swcWorkHourCond.setPageSize(pageSize);

			swcWorkHourCond.setOrders(new Order[]{new Order("modificationDate", false)});
			SwcWorkHour[] swcWorkHours = getSwcManager().getWorkhours(userId, swcWorkHourCond, IManager.LEVEL_ALL); 
			List<WorkHourPolicy> workHourPolicyList = new ArrayList<WorkHourPolicy>();

			WorkHourPolicy[] workHourPolicies = null;
			if(swcWorkHours != null) {
				for(int i=0; i<swcWorkHours.length; i++) {
					SwcWorkHour swcWorkHour = swcWorkHours[i];
					int firstDayOfWeek = Integer.parseInt(swcWorkHour.getStartDayOfWeek()); 
					int workingDays = swcWorkHour.getWorkingDays();

					WorkHourPolicy workHourPolicy = new WorkHourPolicy();
					workHourPolicy.setId(swcWorkHour.getObjId());
					workHourPolicy.setValidFrom(new LocalDate(swcWorkHour.getValidFromDate().getTime()));
					if(swcWorkHour.getValidToDate() != null)
						workHourPolicy.setValidTo(new LocalDate(swcWorkHour.getValidToDate().getTime()));
					workHourPolicy.setFirstDayOfWeek(firstDayOfWeek);
					workHourPolicy.setWorkingDays(workingDays);

					WorkHour[] workHours = new WorkHour[7];
					for(int j=firstDayOfWeek; j<firstDayOfWeek+workingDays; j++) {

						int start;
						int end;
						int workTime;
		
						Calendar startCalendar = Calendar.getInstance();
						Calendar endCalendar = Calendar.getInstance();
						int dayOfWeek = j;
		
						if(j > 7)
							dayOfWeek = j % 7;

						switch (dayOfWeek) {
						case Calendar.SUNDAY:
							startCalendar.setTime(swcWorkHour.getSunStartTime());
							endCalendar.setTime(swcWorkHour.getSunEndTime());
							break;
						case Calendar.MONDAY:
							startCalendar.setTime(swcWorkHour.getMonStartTime());
							endCalendar.setTime(swcWorkHour.getMonEndTime());
							break;
						case Calendar.TUESDAY:
							startCalendar.setTime(swcWorkHour.getTueStartTime());
							endCalendar.setTime(swcWorkHour.getTueEndTime());
							break;
						case Calendar.WEDNESDAY:
							startCalendar.setTime(swcWorkHour.getWedStartTime());
							endCalendar.setTime(swcWorkHour.getWedEndTime());
							break;
						case Calendar.THURSDAY:
							startCalendar.setTime(swcWorkHour.getThuStartTime());
							endCalendar.setTime(swcWorkHour.getThuEndTime());
							break;
						case Calendar.FRIDAY:
							startCalendar.setTime(swcWorkHour.getFriStartTime());
							endCalendar.setTime(swcWorkHour.getFriEndTime());
							break;
						case Calendar.SATURDAY:
							startCalendar.setTime(swcWorkHour.getSatStartTime());
							endCalendar.setTime(swcWorkHour.getSatEndTime());
							break;
						default: 
							break;
						}
						start = startCalendar.get(Calendar.HOUR_OF_DAY) * LocalDate.ONE_HOUR + startCalendar.get(Calendar.MINUTE) * LocalDate.ONE_MINUTE;
						end = endCalendar.get(Calendar.HOUR_OF_DAY) * LocalDate.ONE_HOUR + endCalendar.get(Calendar.MINUTE) * LocalDate.ONE_MINUTE;
						workTime = end - start;

						workHours[dayOfWeek-1] = new WorkHour(start, end, workTime);
					}
					workHourPolicy.setWorkHours(workHours);
					workHourPolicyList.add(workHourPolicy);
				}
				if(workHourPolicyList.size() != 0) {
					workHourPolicies = new WorkHourPolicy[workHourPolicyList.size()];
					workHourPolicyList.toArray(workHourPolicies);
				}
			}

			recordList.setRecords(workHourPolicies);
			recordList.setPageSize(pageSize);
			recordList.setTotalPages(totalPages);
			recordList.setCurrentPage(currentPage);
			recordList.setType(InstanceInfoList.TYPE_INFORMATION_INSTANCE_LIST);

			return recordList;
		 } catch(Exception e) {
			e.printStackTrace();
			return null;			
		}		

	}

	@Override
	public WorkHourPolicy getWorkHourPolicyById(String id) throws Exception {

		try {
			User cUser = SmartUtil.getCurrentUser();
			SwcWorkHour swcWorkHour = getSwcManager().getWorkhour(cUser.getId(), id, IManager.LEVEL_ALL);
			WorkHourPolicy workHourPolicy = new WorkHourPolicy();

			if(swcWorkHour != null) {
				int firstDayOfWeek = Integer.parseInt(swcWorkHour.getStartDayOfWeek()); 
				int workingDays = swcWorkHour.getWorkingDays();

				workHourPolicy.setId(id);
				workHourPolicy.setValidFrom(new LocalDate(swcWorkHour.getValidFromDate().getTime()));
				if(swcWorkHour.getValidToDate() != null)
					workHourPolicy.setValidTo(new LocalDate(swcWorkHour.getValidToDate().getTime()));
				workHourPolicy.setFirstDayOfWeek(firstDayOfWeek);
				workHourPolicy.setWorkingDays(workingDays);
	
				WorkHour[] workHours = new WorkHour[7];
				for(int j=firstDayOfWeek; j<firstDayOfWeek+workingDays; j++) {

					int start;
					int end;
					int workTime;
	
					Calendar startCalendar = Calendar.getInstance();
					Calendar endCalendar = Calendar.getInstance();
					int dayOfWeek = j;
	
					if(j > 7)
						dayOfWeek = j % 7;
	
					switch (dayOfWeek) {
					case Calendar.SUNDAY:
						startCalendar.setTime(swcWorkHour.getSunStartTime());
						endCalendar.setTime(swcWorkHour.getSunEndTime());
						break;
					case Calendar.MONDAY:
						startCalendar.setTime(swcWorkHour.getMonStartTime());
						endCalendar.setTime(swcWorkHour.getMonEndTime());
						break;
					case Calendar.TUESDAY:
						startCalendar.setTime(swcWorkHour.getTueStartTime());
						endCalendar.setTime(swcWorkHour.getTueEndTime());
						break;
					case Calendar.WEDNESDAY:
						startCalendar.setTime(swcWorkHour.getWedStartTime());
						endCalendar.setTime(swcWorkHour.getWedEndTime());
						break;
					case Calendar.THURSDAY:
						startCalendar.setTime(swcWorkHour.getThuStartTime());
						endCalendar.setTime(swcWorkHour.getThuEndTime());
						break;
					case Calendar.FRIDAY:
						startCalendar.setTime(swcWorkHour.getFriStartTime());
						endCalendar.setTime(swcWorkHour.getFriEndTime());
						break;
					case Calendar.SATURDAY:
						startCalendar.setTime(swcWorkHour.getSatStartTime());
						endCalendar.setTime(swcWorkHour.getSatEndTime());
						break;
					default:
						break;
					}
					start = startCalendar.get(Calendar.HOUR_OF_DAY) * LocalDate.ONE_HOUR + startCalendar.get(Calendar.MINUTE) * LocalDate.ONE_MINUTE;
					end = endCalendar.get(Calendar.HOUR_OF_DAY) * LocalDate.ONE_HOUR + endCalendar.get(Calendar.MINUTE) * LocalDate.ONE_MINUTE;
					workTime = end - start;
	
					workHours[dayOfWeek-1] = new WorkHour(start, end, workTime);
				}
				workHourPolicy.setWorkHours(workHours);
			} else {
				workHourPolicy = new WorkHourPolicy();
			}
			return workHourPolicy;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}		
	}

	@Override
	public void setWorkHourPolicy(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try {
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			String companyId = cUser.getCompanyId();

			Map<String, Object> frmEditWorkHour = (Map<String, Object>)requestBody.get("frmEditWorkHour");

			String policyId = (String)requestBody.get("policyId");

			Set<String> keySet = frmEditWorkHour.keySet();
			Iterator<String> itr = keySet.iterator();

			Date datValidFrom = null;
			String selFirstDayOfWeek = null;
			int selWorkingDays = 0;
			List<String> timWorkStart = null;
			List<String> timWorkEnd = null;
			Date startTime = null;
			Date endTime = null;

			SwcWorkHour swcWorkHour = null;
			if(!policyId.equals("")) {
				swcWorkHour = getSwcManager().getWorkhour(userId, policyId, IManager.LEVEL_ALL);
			} else {
				swcWorkHour = new SwcWorkHour();
				swcWorkHour.setCompanyId(companyId);
				swcWorkHour.setType("0");
			}
			while(itr.hasNext()) {
				String fieldId = (String)itr.next();
				Object fieldValue = frmEditWorkHour.get(fieldId);
				if(fieldValue instanceof String) {					
					if(fieldId.equals("datValidFrom")) {
						datValidFrom = new Date(LocalDate.convertLocalDateStringToLocalDate((String)frmEditWorkHour.get("datValidFrom")).getTime());
						swcWorkHour.setValidFromDate(datValidFrom);
					} else if(fieldId.equals("selFirstDayOfWeek")) {
						selFirstDayOfWeek = (String)frmEditWorkHour.get("selFirstDayOfWeek");
						swcWorkHour.setStartDayOfWeek(selFirstDayOfWeek);
					} else if(fieldId.equals("selWorkingDays")) {
						//selWorkingDays = Integer.parseInt((String)frmEditWorkHour.get("selWorkingDays"));
						//swcWorkHour.setWorkingDays(selWorkingDays);
					}
				} else if(fieldValue instanceof ArrayList) {
					if(fieldId.equals("timWorkStart")) {
						timWorkStart = (ArrayList<String>)frmEditWorkHour.get("timWorkStart");
						selWorkingDays = 0;
						for(int i=0; i<timWorkStart.size(); i++) {
							if(!timWorkStart.get(i).equals("00:00"))
								selWorkingDays++;
							startTime = LocalDate.convertTimeStringToDate(timWorkStart.get(i));
							if(i==0)swcWorkHour.setSunStartTime(startTime);
							else if(i==1)swcWorkHour.setMonStartTime(startTime);
							else if(i==2)swcWorkHour.setTueStartTime(startTime);
							else if(i==3)swcWorkHour.setWedStartTime(startTime);
							else if(i==4)swcWorkHour.setThuStartTime(startTime);
							else if(i==5)swcWorkHour.setFriStartTime(startTime);
							else if(i==6)swcWorkHour.setSatStartTime(startTime);
						}
						swcWorkHour.setWorkingDays(selWorkingDays);
					} else if(fieldId.equals("timWorkEnd")) {
						timWorkEnd = (ArrayList<String>)frmEditWorkHour.get("timWorkEnd");
						for(int i=0; i<timWorkEnd.size(); i++) {
							endTime = LocalDate.convertTimeStringToDate(timWorkEnd.get(i));
							if(i==0)swcWorkHour.setSunEndTime(endTime);
							else if(i==1)swcWorkHour.setMonEndTime(endTime);
							else if(i==2)swcWorkHour.setTueEndTime(endTime);
							else if(i==3)swcWorkHour.setWedEndTime(endTime);
							else if(i==4)swcWorkHour.setThuEndTime(endTime);
							else if(i==5)swcWorkHour.setFriEndTime(endTime);
							else if(i==6)swcWorkHour.setSatEndTime(endTime);
						}
					}
				}
			}
			getSwcManager().setWorkhour(userId, swcWorkHour, IManager.LEVEL_ALL);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void removeWorkHourPolicy(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try {
			User cUser = SmartUtil.getCurrentUser();
			String policyId = (String)requestBody.get("policyId");
			if(policyId.equals(""))
				return;
			getSwcManager().removeWorkhour(cUser.getId(), policyId);
		} catch(Exception e) {
			e.printStackTrace();			
		}
	}

	@Override
	public RecordList getCompanyEventList(RequestParams params) throws Exception {

		try {
			RecordList recordList = new RecordList();
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			String companyId = cUser.getCompanyId();

			SwcEventDayCond swcEventDayCond = new SwcEventDayCond();
			swcEventDayCond.setCompanyId(companyId);

			long totalCount = getSwcManager().getEventdaySize(userId, swcEventDayCond);

			int pageSize = params.getPageSize();
			if(pageSize == 0) pageSize = 20;

			int currentPage = params.getCurrentPage();
			if(currentPage == 0) currentPage = 1;

			int totalPages = (int)totalCount % pageSize;

			if(totalPages == 0)
				totalPages = (int)totalCount / pageSize;
			else
				totalPages = (int)totalCount / pageSize + 1;

			int result = 0;

			if(params.getPagingAction() != 0) {
				if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXT10) {
					result = (((currentPage - 1) / 10) * 10) + 11;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXTEND) {
					result = totalPages;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREV10) {
					result = ((currentPage - 1) / 10) * 10;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREVEND) {
					result = 1;
				}
				currentPage = result;
			}

			if(previousPageSize != pageSize)
				currentPage = 1;

			previousPageSize = pageSize;

			if((long)((pageSize * (currentPage - 1)) + 1) > totalCount)
				currentPage = 1;

			if (currentPage > 0)
				swcEventDayCond.setPageNo(currentPage-1);

			swcEventDayCond.setPageSize(pageSize);

			swcEventDayCond.setOrders(new Order[]{new Order("startDay", false)});
			SwcEventDay[] swcEventDays = getSwcManager().getEventdays(userId, swcEventDayCond, IManager.LEVEL_ALL);

			CompanyEvent[] companyEvents = null;
			if(swcEventDays != null) {
				List<CompanyEvent> companyEventList = new ArrayList<CompanyEvent>();
				for(SwcEventDay swcEventDay : swcEventDays) {
					CompanyEvent companyEvent = new CompanyEvent();
					boolean isHoliDay = swcEventDay.getType().equals(CompanyEvent.EVENT_TYPE_HOLIDAY) ? true : false;
					LocalDate plannedStart = new LocalDate(swcEventDay.getStartDay().getTime());
					LocalDate plannedEnd = new LocalDate(swcEventDay.getEndDay().getTime());
					String id = swcEventDay.getObjId();
					String name = swcEventDay.getName();
					if(swcEventDay.getReltdPerson() != null) {
						List<Community> userList = new ArrayList<Community>();
						String[] reltdUsers = swcEventDay.getReltdPerson().split(";");
						if(reltdUsers != null && reltdUsers.length > 0) {
							for(String reltdUser : reltdUsers) {
								Object obj = communityService.getWorkSpaceById(reltdUser);
								userList.add((Community)obj);
							}
						}
						Community[] relatedUsers = new Community[userList.size()];
						userList.toArray(relatedUsers);
						companyEvent.setRelatedUsers(relatedUsers);
					}
					companyEvent.setId(id);
					companyEvent.setName(name);
					companyEvent.setHoliday(isHoliDay);
					companyEvent.setPlannedStart(plannedStart);
					companyEvent.setPlannedEnd(plannedEnd);
					companyEventList.add(companyEvent);
				}
				if(companyEventList.size() != 0) {
					companyEvents = new CompanyEvent[companyEventList.size()];
					companyEventList.toArray(companyEvents);
				}
			}
			recordList.setRecords(companyEvents);
			recordList.setPageSize(pageSize);
			recordList.setTotalPages(totalPages);
			recordList.setCurrentPage(currentPage);
			recordList.setType(InstanceInfoList.TYPE_INFORMATION_INSTANCE_LIST);

			return recordList;
		} catch(Exception e) {
			e.printStackTrace();
			return null;			
		}
	}

	@Override
	public CompanyEvent getCompanyEventById(String id) throws Exception {

		try {
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			SwcEventDay swcEventDay = getSwcManager().getEventday(userId, id, IManager.LEVEL_ALL);

			Community[] relatedUsers = null;
			String name = null;
			boolean isHoliday = false;
			LocalDate plannedStart = null;
			LocalDate plannedEnd = null;
			CompanyEvent companyEvent = new CompanyEvent();
			if(swcEventDay != null) {
				isHoliday = swcEventDay.getType().equals(CompanyEvent.EVENT_TYPE_HOLIDAY) ? true : false;
				plannedStart = new LocalDate(swcEventDay.getStartDay().getTime());
				plannedEnd = new LocalDate(swcEventDay.getEndDay().getTime());
				name = swcEventDay.getName();
				if(swcEventDay.getReltdPerson() != null) {
					List<Community> userList = new ArrayList<Community>();
					String[] reltdUsers = swcEventDay.getReltdPerson().split(";");
					if(reltdUsers != null && reltdUsers.length > 0) {
						for(String reltdUser : reltdUsers) {
							Object obj = communityService.getWorkSpaceById(reltdUser);
							userList.add((Community)obj);
						}
					}
					if(userList.size() != 0) {
						relatedUsers = new Community[userList.size()];
						userList.toArray(relatedUsers);
					}
					companyEvent.setRelatedUsers(relatedUsers);
				}
			}
			companyEvent.setId(id);
			companyEvent.setName(name);
			companyEvent.setHoliday(isHoliday);
			companyEvent.setPlannedStart(plannedStart);
			companyEvent.setPlannedEnd(plannedEnd);
			return companyEvent;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void setCompanyEvent(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try{
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			String companyId = cUser.getCompanyId();

			Map<String, Object> frmEditCompanyEvent = (Map<String, Object>)requestBody.get("frmEditCompanyEvent");
			String eventId = (String)requestBody.get("eventId");

			Set<String> keySet = frmEditCompanyEvent.keySet();
			Iterator<String> itr = keySet.iterator();

			String txtEventName = null;
			Date datStartDate = null;
			Date datEndDate = null;
			List<Map<String, String>> users = null;

			SwcEventDay swcEventDay = null;
			if(!eventId.equals("")) {
				swcEventDay = getSwcManager().getEventday(userId, eventId, IManager.LEVEL_ALL);
			} else {
				swcEventDay = new SwcEventDay();
				swcEventDay.setType(CompanyEvent.EVENT_TYPE_EVENTDAY);
				swcEventDay.setCompanyId(companyId);
			}

			while(itr.hasNext()) {
				String fieldId = (String)itr.next();
				Object fieldValue = frmEditCompanyEvent.get(fieldId);
				if(fieldValue instanceof LinkedHashMap) {
					Map<String, Object> valueMap = (Map<String, Object>)fieldValue;
					if(fieldId.equals("usrRelatedUsers"))
						users = (ArrayList<Map<String,String>>)valueMap.get("users");

					if(!CommonUtil.isEmpty(users)) {
						String relatedId = "";
						String symbol = ";";
						for(int i=0; i < users.subList(0, users.size()).size(); i++) {
							Map<String, String> user = users.get(i);
							relatedId += user.get("id") + symbol;
						}
						swcEventDay.setReltdPerson(relatedId);
					}
				} else if(fieldValue instanceof String) {	
					if(fieldId.equals("txtEventName")) {
						txtEventName = (String)frmEditCompanyEvent.get("txtEventName");
						swcEventDay.setName(txtEventName);
					} else if(fieldId.equals("chkIsHoliday")) {
						swcEventDay.setType(CompanyEvent.EVENT_TYPE_HOLIDAY);
					} else if(fieldId.equals("datStartDate")) {
						datStartDate = new LocalDate(LocalDate.convertLocalDateStringToLocalDate((String)frmEditCompanyEvent.get("datStartDate")).getTime());
						swcEventDay.setStartDay(datStartDate);
					} else if(fieldId.equals("datEndDate")) {
						datEndDate = new LocalDate(LocalDate.convertLocalDateStringToLocalDate((String)frmEditCompanyEvent.get("datEndDate")).getTime());
						swcEventDay.setEndDay(datEndDate);
					}
				}
			}
			getSwcManager().setEventday(userId, swcEventDay, IManager.LEVEL_ALL);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void removeCompanyEvent(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try {
			User cUser = SmartUtil.getCurrentUser();
			String eventId = (String)requestBody.get("eventId");
			if(eventId.equals(""))
				return;
			getSwcManager().removeEventday(cUser.getId(), eventId);
		} catch(Exception e) {
			e.printStackTrace();			
		}
	}
	
	@Override
	public RecordList getApprovalLineList(RequestParams params) throws Exception {

		try {
			RecordList recordList = new RecordList();
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			String companyId = cUser.getCompanyId();

			AprApprovalLineDefCond approvalLineDefCond = new AprApprovalLineDefCond();
			approvalLineDefCond.setCompanyId(companyId);

			long totalCount = getAprManager().getApprovalLineDefSize(userId, approvalLineDefCond);

			totalCount += 2;
			
			int pageSize = params.getPageSize();
			if(pageSize == 0) pageSize = 20;

			int currentPage = params.getCurrentPage();
			if(currentPage == 0) currentPage = 1;

			int totalPages = (int)totalCount % pageSize;

			if(totalPages == 0)
				totalPages = (int)totalCount / pageSize;
			else
				totalPages = (int)totalCount / pageSize + 1;

			int result = 0;

			if(params.getPagingAction() != 0) {
				if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXT10) {
					result = (((currentPage - 1) / 10) * 10) + 11;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXTEND) {
					result = totalPages;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREV10) {
					result = ((currentPage - 1) / 10) * 10;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREVEND) {
					result = 1;
				}
				currentPage = result;
			}

			if(previousPageSize != pageSize)
				currentPage = 1;

			previousPageSize = pageSize;

			if((long)((pageSize * (currentPage - 1)) + 1) > totalCount)
				currentPage = 1;

			if (currentPage > 0)
				approvalLineDefCond.setPageNo(currentPage-1);

			approvalLineDefCond.setPageSize(pageSize);

			approvalLineDefCond.setOrders(new Order[]{new Order("modificationDate", false)});
			AprApprovalLineDef[] approvalLineDefs = getAprManager().getApprovalLineDefs(userId, approvalLineDefCond, IManager.LEVEL_ALL);

			ApprovalLine[] approvalLines = null;
			List<ApprovalLine> approvalLineList = new ArrayList<ApprovalLine>();
			for (int i = 0; i < ApprovalLine.SYSTEM_APPROVAL_LINES.length; i++) {
				approvalLineList.add(ApprovalLine.SYSTEM_APPROVAL_LINES[i]);
			}
			if(approvalLineDefs != null) {
				for(AprApprovalLineDef approvalLineDef : approvalLineDefs) {
					ApprovalLine approvalLine = new ApprovalLine();
					String id = approvalLineDef.getObjId();
					String name = approvalLineDef.getAprLineName();
					String desc = CommonUtil.toNotNull(approvalLineDef.getDescription());
					int approvalLevel = Integer.parseInt(approvalLineDef.getAprLevel());
					approvalLine.setId(id);
					approvalLine.setName(name);
					approvalLine.setDesc(desc);
					approvalLine.setApprovalLevel(approvalLevel);
					AprApprovalDef[] approvalDefs = approvalLineDef.getApprovalDefs();
					if(!CommonUtil.isEmpty(approvalDefs)) {
						List<Approval> approvalList = new ArrayList<Approval>();
						for(AprApprovalDef approvalDef : approvalDefs) {
							Approval approval = new Approval();
							approval.setName(approvalDef.getAprName());
							approval.setApproverType(Integer.parseInt(approvalDef.getType()));
							approval.setApprover(ModelConverter.getUserByUserId(approvalDef.getAprPerson()));
							String dueDate = CommonUtil.toNotNull(approvalDef.getDueDate());
							int meanTimeDays = 0;
							int meanTimeHours = 0;
							int meanTimeMinutes = 30;
							int daysToMinutes = 60 * 24;
							int hoursToMinutes = 60;
							if(!dueDate.equals("")) {
								int meanTime = Integer.parseInt(dueDate);
								meanTimeDays = meanTime / daysToMinutes;
								meanTime = meanTime % daysToMinutes;
								meanTimeHours = meanTime / hoursToMinutes;
								meanTimeMinutes = meanTime % hoursToMinutes;
							}
							approval.setMeanTimeDays(meanTimeDays);
							approval.setMeanTimeHours(meanTimeHours);
							approval.setMeanTimeMinutes(meanTimeMinutes);
							approvalList.add(approval);
						}
						Approval[] approvals = new Approval[approvalList.size()];
						approvalList.toArray(approvals);
						approvalLine.setApprovals(approvals);
					}
					approvalLineList.add(approvalLine);
				}
				if(approvalLineList.size() != 0) {
					approvalLines = new ApprovalLine[approvalLineList.size()];
					approvalLineList.toArray(approvalLines);
				}
			}
			recordList.setRecords(approvalLines);
			recordList.setPageSize(pageSize);
			recordList.setTotalPages(totalPages);
			recordList.setCurrentPage(currentPage);
			recordList.setType(InstanceInfoList.TYPE_INFORMATION_INSTANCE_LIST);

			return recordList;
		} catch(Exception e) {
			e.printStackTrace();
			return null;			
		}
	}

	@Override
	public ApprovalLine getApprovalLineById(String id) throws Exception {

		try {

			Approval[] approvals = null;
			ApprovalLine approvalLine = new ApprovalLine();
			if(SmartUtil.isBlankObject(id))
				return ApprovalLine.DEFAULT_APPROVAL_LINE_3_LEVEL;
			
			for (int i = 0; i < ApprovalLine.SYSTEM_APPROVAL_LINES.length; i++) {
				if (id.equalsIgnoreCase(ApprovalLine.SYSTEM_APPROVAL_LINES[i].getId())) {
					return ApprovalLine.SYSTEM_APPROVAL_LINES[i];
				}
			}
			
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			AprApprovalLineDef approvalLineDef = getAprManager().getApprovalLineDef(userId, id, IManager.LEVEL_ALL);
			if(approvalLineDef != null) {
				String name = approvalLineDef.getAprLineName();
				String desc = CommonUtil.toNotNull(approvalLineDef.getAprDescription());
				int approvalLevel = Integer.parseInt(approvalLineDef.getAprLevel());
				approvalLine.setId(id);
				approvalLine.setName(name);
				approvalLine.setDesc(desc);
				approvalLine.setApprovalLevel(approvalLevel);
				AprApprovalDef[] approvalDefs = approvalLineDef.getApprovalDefs();
				if(approvalDefs != null) {
					List<Approval> approvalList = new ArrayList<Approval>();
					for(AprApprovalDef approvalDef : approvalDefs) {
						Approval approval = new Approval();
						approval.setName(approvalDef.getAprName());
						approval.setApproverType(Integer.parseInt(approvalDef.getType()));
						approval.setApprover(ModelConverter.getUserByUserId(approvalDef.getAprPerson()));
						String dueDate = approvalDef.getDueDate();
						int meanTimeDays = 0;
						int meanTimeHours = 0;
						int meanTimeMinutes = 30;
						int daysToMinutes = 60 * 24;
						int hoursToMinutes = 60;
						if(dueDate != null) {
							int meanTime = Integer.parseInt(dueDate);
							meanTimeDays = meanTime / daysToMinutes;
							meanTime = meanTime % daysToMinutes;
							meanTimeHours = meanTime / hoursToMinutes;
							meanTimeMinutes = meanTime % hoursToMinutes;
						}
						approval.setMeanTimeDays(meanTimeDays);
						approval.setMeanTimeHours(meanTimeHours);
						approval.setMeanTimeMinutes(meanTimeMinutes);
						approvalList.add(approval);
					}
					if(approvalList.size() != 0) {
						approvals = new Approval[approvalList.size()];
						approvalList.toArray(approvals);
					}
					approvalLine.setApprovals(approvals);
				}
			}
			return approvalLine;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void setApprovalLine(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try{
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			String companyId = cUser.getCompanyId();

			Map<String, Object> frmEditApprovalLine = (Map<String, Object>)requestBody.get("frmEditApprovalLine");
			String approvalLineId = (String)requestBody.get("approvalLineId");

			Set<String> keySet = frmEditApprovalLine.keySet();
			Iterator<String> itr = keySet.iterator();

			String txtApprovalLineName = null;
			String txtaApprovalLineDesc = null;
			String selApprovalLineLevel = null;
			String txtLevelName = null;
			String selLevelApproverType = null;
			String txtMeanTimeDays = null;
			String txtMeanTimeHours = null;
			String txtMeanTimeMinutes = null;
			String dueDateString = null;
			String usrLevelApprover = null;
			List<Map<String, String>> users = null;

			AprApprovalLineDef approvalLineDef = null;
			if(!approvalLineId.equals("")) {
				approvalLineDef = getAprManager().getApprovalLineDef(userId, approvalLineId, IManager.LEVEL_ALL);
			} else {
				approvalLineDef = new AprApprovalLineDef();
				approvalLineDef.setCompanyId(companyId);
			}

			int count = 0;
			while(itr.hasNext()) {
				String fieldId = (String)itr.next();
				Object fieldValue = frmEditApprovalLine.get(fieldId);
				if(fieldValue instanceof String) {
					if(fieldId.startsWith("txtLevelName")) {
						if(!((String)fieldValue).equals("")) {
							count++;
						}
					}
					if(fieldId.equals("txtApprovalLineName")) {
						txtApprovalLineName = (String)frmEditApprovalLine.get("txtApprovalLineName");
						approvalLineDef.setAprLineName(txtApprovalLineName);
					} else if(fieldId.equals("txtaApprovalLineDesc")) {
						txtaApprovalLineDesc = (String)frmEditApprovalLine.get("txtaApprovalLineDesc");
						approvalLineDef.setAprDescription(txtaApprovalLineDesc);
					} else if(fieldId.equals("selApprovalLineLevel")) {
						selApprovalLineLevel = (String)frmEditApprovalLine.get("selApprovalLineLevel");
						approvalLineDef.setAprLevel(selApprovalLineLevel);
					}
				}
			}

			List<AprApprovalDef> approvalDefList = new ArrayList<AprApprovalDef>();
			if(count != 0) {
				for(int i=1; i<count+1; i++) {
					AprApprovalDef approvalDef = new AprApprovalDef();
					txtLevelName = (String)frmEditApprovalLine.get("txtLevelName"+i);
					selLevelApproverType = (String)frmEditApprovalLine.get("selLevelApproverType"+i);
					txtMeanTimeDays = (String)frmEditApprovalLine.get("txtMeanTimeDays"+i);
					txtMeanTimeHours = (String)frmEditApprovalLine.get("txtMeanTimeHours"+i);
					txtMeanTimeMinutes = (String)frmEditApprovalLine.get("txtMeanTimeMinutes"+i);
					int dueDate = (Integer.parseInt(txtMeanTimeDays) * 24 * 60) + (Integer.parseInt(txtMeanTimeHours) * 60) + Integer.parseInt(txtMeanTimeMinutes);
					dueDateString = dueDate + "";
					Map<String, Object> valueMap = (Map<String, Object>)frmEditApprovalLine.get("usrLevelApprover"+i);
					users = (ArrayList<Map<String,String>>)valueMap.get("users");
					if(users.size() != 0) {
						Map<String, String> userMap = users.get(0);
						usrLevelApprover = userMap.get("id");
						approvalDef.setAprPerson(usrLevelApprover);
					}
					approvalDef.setAprName(txtLevelName);
					approvalDef.setType(selLevelApproverType);
					approvalDef.setDueDate(dueDateString);
					approvalDefList.add(approvalDef);
				}
			}
			AprApprovalDef[] approvalDefs = null;
			if(approvalDefList.size() != 0) {
				approvalDefs = new AprApprovalDef[approvalDefList.size()];
				approvalDefList.toArray(approvalDefs);
			}
			approvalLineDef.setApprovalDefs(approvalDefs);

			getAprManager().setApprovalLineDef(userId, approvalLineDef, IManager.LEVEL_ALL);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void removeApprovalLine(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try {
			User cUser = SmartUtil.getCurrentUser();
			String lineId = (String)requestBody.get("lineId");
			if(lineId.equals(""))
				return;
			getAprManager().removeApprovalLineDef(cUser.getId(), lineId);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public RecordList getWebServiceList(RequestParams params) throws Exception {

		try {
			RecordList recordList = new RecordList();
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			String companyId = cUser.getCompanyId();

			SwcWebServiceCond swcWebServiceCond = new SwcWebServiceCond();
			swcWebServiceCond.setCompanyId(companyId);

			long totalCount = getSwcManager().getWebServiceSize(userId, swcWebServiceCond);

			int pageSize = params.getPageSize();
			if(pageSize == 0) pageSize = 20;

			int currentPage = params.getCurrentPage();
			if(currentPage == 0) currentPage = 1;

			int totalPages = (int)totalCount % pageSize;

			if(totalPages == 0)
				totalPages = (int)totalCount / pageSize;
			else
				totalPages = (int)totalCount / pageSize + 1;

			int result = 0;

			if(params.getPagingAction() != 0) {
				if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXT10) {
					result = (((currentPage - 1) / 10) * 10) + 11;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXTEND) {
					result = totalPages;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREV10) {
					result = ((currentPage - 1) / 10) * 10;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREVEND) {
					result = 1;
				}
				currentPage = result;
			}

			if(previousPageSize != pageSize)
				currentPage = 1;

			previousPageSize = pageSize;

			if((long)((pageSize * (currentPage - 1)) + 1) > totalCount)
				currentPage = 1;

			if (currentPage > 0)
				swcWebServiceCond.setPageNo(currentPage-1);

			swcWebServiceCond.setPageSize(pageSize);

			//swcWebServiceCond.setOrders(new Order[]{new Order("webServiceName", true)});
			SwcWebService[] swcWebServices = getSwcManager().getWebServices(userId, swcWebServiceCond, IManager.LEVEL_ALL);

			WebService[] webServices = null;
			if(swcWebServices != null) {
				List<WebService> webServiceList = new ArrayList<WebService>();
				for(SwcWebService swcWebService : swcWebServices) {
					WebService webService = new WebService();
					String id = swcWebService.getObjId();
					String name = swcWebService.getWebServiceName();
					String desc = swcWebService.getDescription();
					String wsdlUri = swcWebService.getWsdlAddress();
					String port = swcWebService.getPortName();
					String operation = swcWebService.getOperationName();
					webService.setId(id);
					webService.setName(name);
					webService.setDesc(desc);
					webService.setWsdlUri(wsdlUri);
					webService.setPort(port);
					webService.setOperation(operation);
					SwcWebServiceParameter[] swcWebServiceParameters = swcWebService.getSwcWebServiceParameters();
					if(swcWebServiceParameters != null) {
						List<Variable> inputVariableList = new ArrayList<Variable>();
						List<Variable> returnVariableList = new ArrayList<Variable>();
						for(SwcWebServiceParameter swcWebServiceParameter : swcWebServiceParameters) {
							Variable variable = new Variable();
							String type = swcWebServiceParameter.getType();
							if(type.equals("I")) {
								variable.setName(swcWebServiceParameter.getVariableName());
								variable.setElementName(swcWebServiceParameter.getParameterName());
								variable.setElementType(swcWebServiceParameter.getParameterType());
								inputVariableList.add(variable);
							} else if(type.equals("O")) {
								variable.setName(swcWebServiceParameter.getVariableName());
								variable.setElementName(swcWebServiceParameter.getParameterName());
								variable.setElementType(swcWebServiceParameter.getParameterType());
								returnVariableList.add(variable);
							}
						}
						Variable[] inputVariables = new Variable[inputVariableList.size()];
						inputVariableList.toArray(inputVariables);
						webService.setInputVariables(inputVariables);
						Variable[] returnVariables = new Variable[returnVariableList.size()];
						returnVariableList.toArray(returnVariables);
						webService.setReturnVariables(returnVariables);
					}
					webServiceList.add(webService);
				}
				if(webServiceList.size() != 0) {
					webServices = new WebService[webServiceList.size()];
					webServiceList.toArray(webServices);
				}
			}
			recordList.setRecords(webServices);
			recordList.setPageSize(pageSize);
			recordList.setTotalPages(totalPages);
			recordList.setCurrentPage(currentPage);
			recordList.setType(InstanceInfoList.TYPE_INFORMATION_INSTANCE_LIST);

			return recordList;
		} catch(Exception e) {
			e.printStackTrace();
			return null;			
		}
	}

	@Override
	public WebService getWebServiceById(String id) throws Exception {

		try {
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			SwcWebService swcWebService = getSwcManager().getWebService(userId, id, IManager.LEVEL_ALL);

			Variable[] inputVariables = null;
			Variable[] returnVariables = null;
			WebService webService = new WebService();
			if(swcWebService != null) {
				String name = swcWebService.getWebServiceName();
				String desc = swcWebService.getDescription();
				String wsdlUri = swcWebService.getWsdlAddress();
				String port = swcWebService.getPortName();
				String operation = swcWebService.getOperationName();
				webService.setId(id);
				webService.setName(name);
				webService.setDesc(desc);
				webService.setWsdlUri(wsdlUri);
				webService.setPort(port);
				webService.setOperation(operation);
				SwcWebServiceParameter[] swcWebServiceParameters = swcWebService.getSwcWebServiceParameters();
				if(swcWebServiceParameters != null) {
					List<Variable> inputVariableList = new ArrayList<Variable>();
					List<Variable> returnVariableList = new ArrayList<Variable>();
					for(SwcWebServiceParameter swcWebServiceParameter : swcWebServiceParameters) {
						Variable variable = new Variable();
						String type = swcWebServiceParameter.getType();
						if(type.equals("I")) {
							variable.setName(swcWebServiceParameter.getVariableName());
							variable.setElementName(swcWebServiceParameter.getParameterName());
							variable.setElementType(swcWebServiceParameter.getParameterType());
							inputVariableList.add(variable);
						} else if(type.equals("O")) {
							variable.setName(swcWebServiceParameter.getVariableName());
							variable.setElementName(swcWebServiceParameter.getParameterName());
							variable.setElementType(swcWebServiceParameter.getParameterType());
							returnVariableList.add(variable);
						}
					}
					if(inputVariableList.size() != 0) {
						inputVariables = new Variable[inputVariableList.size()];
						inputVariableList.toArray(inputVariables);
					}
					webService.setInputVariables(inputVariables);
					if(returnVariableList.size() != 0) {
						returnVariables = new Variable[returnVariableList.size()];
						returnVariableList.toArray(returnVariables);
					}
					webService.setReturnVariables(returnVariables);
				}
			}
			return webService;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void setWebService(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try {
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			String companyId = cUser.getCompanyId();

			Map<String, Object> frmEditWebService = (Map<String, Object>)requestBody.get("frmEditWebService");
			String serviceId = (String)requestBody.get("serviceId");

			Set<String> keySet = frmEditWebService.keySet();
			Iterator<String> itr = keySet.iterator();

			String txtWebServiceName = null;
			String txtWebServiceDesc = null;
			String txtWebServiceWSDL = null;
			String txtWebServiceAddress = null;
			String selWebServicePort = null;
			String selWebServiceOperation = null;
			String variableName = null;
			String elementName = null;
			String elementType = null;

			SwcWebService swcWebService = null;
			if(!serviceId.equals("")) {
				swcWebService = getSwcManager().getWebService(userId, serviceId, IManager.LEVEL_ALL);
			} else {
				swcWebService = new SwcWebService();
				swcWebService.setCompanyId(companyId);
			}

			int inputCount = 0;
			int returnCount = 0;
			while(itr.hasNext()) {
				String fieldId = (String)itr.next();
				Object fieldValue = frmEditWebService.get(fieldId);
				if(fieldValue instanceof String) {
					if(fieldId.startsWith("txtInputVariableName")) {
						if(!((String)fieldValue).equals("")) {
							inputCount++;
						}
					}
					if(fieldId.startsWith("txtReturnVariableName")) {
						if(!((String)fieldValue).equals("")) {
							returnCount++;
						}
					}
					if(fieldId.equals("txtWebServiceName")) {
						txtWebServiceName = (String)frmEditWebService.get("txtWebServiceName");
						swcWebService.setWebServiceName(txtWebServiceName);
					} else if(fieldId.equals("txtaWebServiceDesc")) {
						txtWebServiceDesc = (String)frmEditWebService.get("txtaWebServiceDesc");
						swcWebService.setDescription(txtWebServiceDesc);
					} else if(fieldId.equals("txtWebServiceWSDL")) {
						txtWebServiceWSDL = (String)frmEditWebService.get("txtWebServiceWSDL");
						txtWebServiceAddress = txtWebServiceWSDL.replaceAll("\\?wsdl", "");
						swcWebService.setWsdlAddress(txtWebServiceWSDL);
						swcWebService.setWebServiceAddress(txtWebServiceAddress);
					} else if(fieldId.equals("selWebServicePort")) {
						selWebServicePort = (String)frmEditWebService.get("selWebServicePort");
						swcWebService.setPortName(selWebServicePort);
					} else if(fieldId.equals("selWebServiceOperation")) {
						selWebServiceOperation = (String)frmEditWebService.get("selWebServiceOperation");
						swcWebService.setOperationName(selWebServiceOperation);
					}
				}
			}

			List<SwcWebServiceParameter> webServiceParameterList = new ArrayList<SwcWebServiceParameter>();
			if(inputCount != 0) {
				for(int i=1; i<inputCount+1; i++) {
					SwcWebServiceParameter webServiceParameter = new SwcWebServiceParameter();
					variableName = (String)frmEditWebService.get("txtInputVariableName"+i);
					elementName = (String)frmEditWebService.get("txtInputElementName"+i);
					elementType = (String)frmEditWebService.get("txtInputElementType"+i);
					webServiceParameter.setVariableName(variableName);
					webServiceParameter.setParameterName(elementName);
					webServiceParameter.setParameterType(elementType);
					webServiceParameter.setType("I");
					webServiceParameterList.add(webServiceParameter);
				}
			}
			if(returnCount != 0) {
				for(int i=1; i<returnCount+1; i++) {
					SwcWebServiceParameter webServiceParameter = new SwcWebServiceParameter();
					variableName = (String)frmEditWebService.get("txtReturnVariableName"+i);
					elementName = (String)frmEditWebService.get("txtReturnElementName"+i);
					elementType = (String)frmEditWebService.get("txtReturnElementType"+i);
					webServiceParameter.setVariableName(variableName);
					webServiceParameter.setParameterName(elementName);
					webServiceParameter.setParameterType(elementType);
					webServiceParameter.setType("O");
					webServiceParameterList.add(webServiceParameter);
				}
			}
			SwcWebServiceParameter[] webServiceParameters = null;
			if(webServiceParameterList.size() != 0) {
				webServiceParameters = new SwcWebServiceParameter[webServiceParameterList.size()];
				webServiceParameterList.toArray(webServiceParameters);
			}
			swcWebService.setSwcWebServiceParameters(webServiceParameters);

			getSwcManager().setWebService(userId, swcWebService, IManager.LEVEL_ALL);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void removeWebService(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try {
			User cUser = SmartUtil.getCurrentUser();
			String serviceId = (String)requestBody.get("serviceId");
			if(serviceId.equals(""))
				return;
			getSwcManager().removeWebService(cUser.getId(), serviceId);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public WSDLDetail getWsdlDetailFromUri(String wsdlUri) throws Exception {

		try {
			WSDLDetail wsdlDetail = new WSDLDetail();
			wsdlDetail.setWsdlUri(wsdlUri);
			List<WSDLPort> wsdlPortList = new ArrayList<WSDLPort>();
			PortType[] portTypes = getSwcManager().getPortTypes(wsdlUri);
			if(portTypes != null) {
				for(PortType portType : portTypes) {
					WSDLPort wsdlPort = new WSDLPort();
					String port = portType.getQName().getLocalPart();
					wsdlPort.setPort(port);
					List<Operation> operationList = portType.getOperations();
					List<WSDLOperation> wsdlOperationList = new ArrayList<WSDLOperation>();
					if(operationList != null && operationList.size() != 0) {
						for(int i=0; i<operationList.size(); i++) {
							Operation operation = operationList.get(i);
							if(operation != null) {
								WSDLOperation wsdlOperation = new WSDLOperation();
								String operationName = operation.getName();
								wsdlOperation.setOperation(operationName);
								List<String> parameterOrdering = operation.getParameterOrdering();
								Input input = operation.getInput();
								Message inputMessage = input.getMessage();
								List<Variable> inputVariableList = new ArrayList<Variable>();
								if(inputMessage != null) {
									Map inputPartsMap = inputMessage.getParts();
									if(inputPartsMap != null) {
										Set<String> inputSet = inputPartsMap.keySet();
									    List<String> inputKeyList = new ArrayList<String>();
									    for(String str : inputSet){
									    	inputKeyList.add(str);
									    }
									    if(parameterOrdering != null) {
											for(String parameter : parameterOrdering){
										    	for(String key : inputKeyList) {
													Variable inputVariable = new Variable();
									    			Part part = (Part)inputPartsMap.get(key);
											    	String inputElementName = part.getName();
											    	String inputElemnetType = "";
											    	QName qName = part.getTypeName();
											    	if(parameter.equals(inputElementName)) {
												    	inputElemnetType = qName.getLocalPart();
												    	inputVariable.setElementName(inputElementName);
												    	inputVariable.setElementType(inputElemnetType);
												    	inputVariableList.add(inputVariable);
											    	}
										    	}
									    	}
									    }
										Variable[] inputVariables = new Variable[inputVariableList.size()];
										inputVariableList.toArray(inputVariables);
										wsdlOperation.setInputVariables(inputVariables);
									}
								}
								Output output = operation.getOutput();
								Message outputMessage = output.getMessage();
								List<Variable> outputVariableList = new ArrayList<Variable>();
								if(outputMessage != null) {
									Map outputPartsMap = outputMessage.getParts();
									if(outputPartsMap != null) {
										Set<String> outputSet = outputPartsMap.keySet();
									    List<String> outputKeyList = new ArrayList<String>();
									    for(String str : outputSet){
									    	outputKeyList.add(str);
									    }
								    	for(String key : outputKeyList) {
											Variable outputVariable = new Variable();
							    			Part part = (Part)outputPartsMap.get(key);
									    	String outputElementName = part.getName();
									    	String outputElemnetType = part.getTypeName().getLocalPart();
									    	outputVariable.setElementName(outputElementName);
									    	outputVariable.setElementType(outputElemnetType);
									    	outputVariableList.add(outputVariable);
								    	}
										Variable[] outputVariables = new Variable[outputVariableList.size()];
										outputVariableList.toArray(outputVariables);
										wsdlOperation.setReturnVariables(outputVariables);
									}
								}
								wsdlOperationList.add(wsdlOperation);
							}
							WSDLOperation[] wsdlOperations = new WSDLOperation[wsdlOperationList.size()];
							wsdlOperationList.toArray(wsdlOperations);
							wsdlPort.setOperations(wsdlOperations);
						}
					}
					wsdlPortList.add(wsdlPort);
				}
				WSDLPort[] wsdlPorts = new WSDLPort[wsdlPortList.size()];
				wsdlPortList.toArray(wsdlPorts);
				wsdlDetail.setPorts(wsdlPorts);
			}
			return wsdlDetail;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public RecordList getExternalFormList(RequestParams params) throws Exception {

		try {
			RecordList recordList = new RecordList();
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			String companyId = cUser.getCompanyId();

			SwcExternalFormCond swcExternalFormCond = new SwcExternalFormCond();
			swcExternalFormCond.setCompanyId(companyId);

			long totalCount = getSwcManager().getExternalFormSize(userId, swcExternalFormCond);

			int pageSize = params.getPageSize();
			if(pageSize == 0) pageSize = 20;

			int currentPage = params.getCurrentPage();
			if(currentPage == 0) currentPage = 1;

			int totalPages = (int)totalCount % pageSize;

			if(totalPages == 0)
				totalPages = (int)totalCount / pageSize;
			else
				totalPages = (int)totalCount / pageSize + 1;

			int result = 0;

			if(params.getPagingAction() != 0) {
				if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXT10) {
					result = (((currentPage - 1) / 10) * 10) + 11;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXTEND) {
					result = totalPages;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREV10) {
					result = ((currentPage - 1) / 10) * 10;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREVEND) {
					result = 1;
				}
				currentPage = result;
			}

			if(previousPageSize != pageSize)
				currentPage = 1;

			previousPageSize = pageSize;

			if((long)((pageSize * (currentPage - 1)) + 1) > totalCount)
				currentPage = 1;

			if (currentPage > 0)
				swcExternalFormCond.setPageNo(currentPage-1);

			swcExternalFormCond.setPageSize(pageSize);

			//swcExternalFormCond.setOrders(new Order[]{new Order("webAppServiceName", true)});
			SwcExternalForm[] swcExternalForms = getSwcManager().getExternalForms(userId, swcExternalFormCond, IManager.LEVEL_ALL);

			ExternalForm[] externalForms = null;
			if(swcExternalForms != null) {
				List<ExternalForm> externalFormList = new ArrayList<ExternalForm>();
				for(SwcExternalForm swcExternalForm : swcExternalForms) {
					ExternalForm externalForm = new ExternalForm();
					String id = swcExternalForm.getObjId();
					String name = swcExternalForm.getWebAppServiceName();
					String desc = swcExternalForm.getDescription();
					String url = swcExternalForm.getWebAppServiceUrl();
					String editMethod = swcExternalForm.getModifyMethod();
					String viewMethod = swcExternalForm.getViewMethod();
					externalForm.setId(id);
					externalForm.setName(name);
					externalForm.setDesc(desc);
					externalForm.setUrl(url);
					externalForm.setEditMethod(editMethod);
					externalForm.setViewMethod(viewMethod);
					SwcExternalFormParameter[] swcExternalFormParameters = swcExternalForm.getSwcExternalFormParameters();
					if(swcExternalFormParameters != null) {
						List<Variable> editVariableList = new ArrayList<Variable>();
						List<Variable> viewVariableList = new ArrayList<Variable>();
						List<Variable> returnVariableList = new ArrayList<Variable>();
						for(SwcExternalFormParameter swcExternalFormParameter : swcExternalFormParameters) {
							Variable variable = new Variable();
							String type = swcExternalFormParameter.getType();
							if(type.equals("M")) {
								variable.setName(swcExternalFormParameter.getVariableName());
								variable.setElementName(swcExternalFormParameter.getParameterName());
								variable.setElementType(swcExternalFormParameter.getParameterType());
								editVariableList.add(variable);
							} else if(type.equals("V")) {
								variable.setName(swcExternalFormParameter.getVariableName());
								variable.setElementName(swcExternalFormParameter.getParameterName());
								variable.setElementType(swcExternalFormParameter.getParameterType());
								viewVariableList.add(variable);
							} else if(type.equals("R")) {
								variable.setName(swcExternalFormParameter.getVariableName());
								variable.setElementName(swcExternalFormParameter.getParameterName());
								variable.setElementType(swcExternalFormParameter.getParameterType());
								returnVariableList.add(variable);
							}
						}
						Variable[] editVariables = new Variable[editVariableList.size()];
						editVariableList.toArray(editVariables);
						externalForm.setEditVariables(editVariables);
						Variable[] viewVariables = new Variable[viewVariableList.size()];
						viewVariableList.toArray(viewVariables);
						externalForm.setViewVariables(viewVariables);
						Variable[] returnVariables = new Variable[returnVariableList.size()];
						returnVariableList.toArray(returnVariables);
						externalForm.setReturnVariables(returnVariables);
					}
					externalFormList.add(externalForm);
				}
				if(externalFormList.size() != 0) {
					externalForms = new ExternalForm[externalFormList.size()];
					externalFormList.toArray(externalForms);
				}
			}
			recordList.setRecords(externalForms);
			recordList.setPageSize(pageSize);
			recordList.setTotalPages(totalPages);
			recordList.setCurrentPage(currentPage);
			recordList.setType(InstanceInfoList.TYPE_INFORMATION_INSTANCE_LIST);

			return recordList;
		} catch(Exception e) {
			e.printStackTrace();
			return null;			
		}
	}

	@Override
	public ExternalForm getExternalFormById(String id) throws Exception {

		try {
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			SwcExternalForm swcExternalForm = getSwcManager().getExternalForm(userId, id, IManager.LEVEL_ALL);

			Variable[] editVariables = null;
			Variable[] viewVariables = null;
			Variable[] returnVariables = null;
			ExternalForm externalForm = new ExternalForm();
			if(swcExternalForm != null) {
				String name = swcExternalForm.getWebAppServiceName();
				String desc = swcExternalForm.getDescription();
				String url = swcExternalForm.getWebAppServiceUrl();
				String editMethod = swcExternalForm.getModifyMethod();
				String viewMethod = swcExternalForm.getViewMethod();
				externalForm.setId(id);
				externalForm.setName(name);
				externalForm.setDesc(desc);
				externalForm.setUrl(url);
				externalForm.setEditMethod(editMethod);
				externalForm.setViewMethod(viewMethod);
				SwcExternalFormParameter[] swcExternalFormParameters = swcExternalForm.getSwcExternalFormParameters();
				if(swcExternalFormParameters != null) {
					List<Variable> editVariableList = new ArrayList<Variable>();
					List<Variable> viewVariableList = new ArrayList<Variable>();
					List<Variable> returnVariableList = new ArrayList<Variable>();
					for(SwcExternalFormParameter swcExternalFormParameter : swcExternalFormParameters) {
						Variable variable = new Variable();
						String type = swcExternalFormParameter.getType();
						if(type.equals("M")) {
							variable.setName(swcExternalFormParameter.getVariableName());
							variable.setElementName(swcExternalFormParameter.getParameterName());
							variable.setElementType(swcExternalFormParameter.getParameterType());
							editVariableList.add(variable);
						} else if(type.equals("V")) {
							variable.setName(swcExternalFormParameter.getVariableName());
							variable.setElementName(swcExternalFormParameter.getParameterName());
							variable.setElementType(swcExternalFormParameter.getParameterType());
							viewVariableList.add(variable);
						} else if(type.equals("R")) {
							variable.setName(swcExternalFormParameter.getVariableName());
							variable.setElementName(swcExternalFormParameter.getParameterName());
							variable.setElementType(swcExternalFormParameter.getParameterType());
							returnVariableList.add(variable);
						}
					}
					if(editVariableList.size() != 0) {
						editVariables = new Variable[editVariableList.size()];
						editVariableList.toArray(editVariables);
					}
					externalForm.setEditVariables(editVariables);
					if(viewVariableList.size() != 0) {
						viewVariables = new Variable[viewVariableList.size()];
						viewVariableList.toArray(viewVariables);
					}
					externalForm.setViewVariables(viewVariables);
					if(returnVariableList.size() != 0) {
						returnVariables = new Variable[returnVariableList.size()];
						returnVariableList.toArray(returnVariables);
					}
					externalForm.setReturnVariables(returnVariables);
				}
			}
			return externalForm;
		} catch(Exception e) {
			e.printStackTrace();
			return null;			
		}		
	}

	@Override
	public void setExternalForm(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try {
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			String companyId = cUser.getCompanyId();

			Map<String, Object> frmEditExternalForm = (Map<String, Object>)requestBody.get("frmEditExternalForm");
			String formId = (String)requestBody.get("formId");

			Set<String> keySet = frmEditExternalForm.keySet();
			Iterator<String> itr = keySet.iterator();

			String txtExternalFormName = null;
			String txtExternalFormDesc = null;
			String txtExternalFormURL = null;
			String txtEditMethod = null;
			String txtViewMethod = null;

			String variableName = null;
			String elementName = null;
			String elementType = null;

			SwcExternalForm swcExternalForm = null;
			if(!formId.equals("")) {
				swcExternalForm = getSwcManager().getExternalForm(userId, formId, IManager.LEVEL_ALL);
			} else {
				swcExternalForm = new SwcExternalForm();
				swcExternalForm.setCompanyId(companyId);
			}

			int editCount = 0;
			int viewCount = 0;
			int returnCount = 0;
			Map<Integer, String> editFieldIdMap = new HashMap<Integer, String>();
			Map<Integer, String> viewFieldIdMap = new HashMap<Integer, String>();
			Map<Integer, String> returnFieldIdMap = new HashMap<Integer, String>();
			while(itr.hasNext()) {
				String fieldId = (String)itr.next();
				Object fieldValue = frmEditExternalForm.get(fieldId);
				if(fieldValue instanceof String) {
					if(fieldId.startsWith("txtEditVariableName")) {
						if(!((String)fieldValue).equals("")) {
							editCount++;
							editFieldIdMap.put(editCount, fieldId);
						}
					}
					if(fieldId.startsWith("txtViewVariableName")) {
						if(!((String)fieldValue).equals("")) {
							viewCount++;
							viewFieldIdMap.put(viewCount, fieldId);
						}
					}
					if(fieldId.startsWith("txtReturnVariableName")) {
						if(!((String)fieldValue).equals("")) {
							returnCount++;
							returnFieldIdMap.put(returnCount, fieldId);
						}
					}
					if(fieldId.equals("txtExternalFormName")) {
						txtExternalFormName = (String)frmEditExternalForm.get("txtExternalFormName");
						swcExternalForm.setWebAppServiceName(txtExternalFormName);
					} else if(fieldId.equals("txtExternalFormDesc")) {
						txtExternalFormDesc = (String)frmEditExternalForm.get("txtExternalFormDesc");
						swcExternalForm.setDescription(txtExternalFormDesc);
					} else if(fieldId.equals("txtExternalFormURL")) {
						txtExternalFormURL = (String)frmEditExternalForm.get("txtExternalFormURL");
						swcExternalForm.setWebAppServiceUrl(txtExternalFormURL);
					} else if(fieldId.equals("txtEditMethod")) {
						txtEditMethod = (String)frmEditExternalForm.get("txtEditMethod");
						swcExternalForm.setModifyMethod(txtEditMethod);
					} else if(fieldId.equals("txtViewMethod")) {
						txtViewMethod = (String)frmEditExternalForm.get("txtViewMethod");
						swcExternalForm.setViewMethod(txtViewMethod);
					}
				}
			}

			List<SwcExternalFormParameter> externalFormParameterList = new ArrayList<SwcExternalFormParameter>();
			if(editCount != 0) {
				for(int i=1; i<editCount+1; i++) {
					SwcExternalFormParameter externalFormParameter = new SwcExternalFormParameter();
					String fieldId = editFieldIdMap.get(i);
					int count = Integer.parseInt(fieldId.replaceAll("txtEditVariableName", ""));
					variableName = (String)frmEditExternalForm.get("txtEditVariableName"+count);
					elementName = (String)frmEditExternalForm.get("txtEditElementName"+count);
					elementType = (String)frmEditExternalForm.get("selEditElementType"+count);
					externalFormParameter.setVariableName(variableName);
					externalFormParameter.setParameterName(elementName);
					externalFormParameter.setParameterType(elementType);
					externalFormParameter.setType("M");
					externalFormParameterList.add(externalFormParameter);
				}
			}
			if(viewCount != 0) {
				for(int i=1; i<viewCount+1; i++) {
					SwcExternalFormParameter externalFormParameter = new SwcExternalFormParameter();
					String fieldId = viewFieldIdMap.get(i);
					int count = Integer.parseInt(fieldId.replaceAll("txtViewVariableName", ""));
					variableName = (String)frmEditExternalForm.get("txtViewVariableName"+count);
					elementName = (String)frmEditExternalForm.get("txtViewElementName"+count);
					elementType = (String)frmEditExternalForm.get("selViewElementType"+count);
					externalFormParameter.setVariableName(variableName);
					externalFormParameter.setParameterName(elementName);
					externalFormParameter.setParameterType(elementType);
					externalFormParameter.setType("V");
					externalFormParameterList.add(externalFormParameter);
				}
			}
			if(returnCount != 0) {
				for(int i=1; i<returnCount+1; i++) {
					SwcExternalFormParameter externalFormParameter = new SwcExternalFormParameter();
					String fieldId = returnFieldIdMap.get(i);
					int count = Integer.parseInt(fieldId.replaceAll("txtReturnVariableName", ""));
					variableName = (String)frmEditExternalForm.get("txtReturnVariableName"+count);
					elementName = (String)frmEditExternalForm.get("txtReturnElementName"+count);
					elementType = (String)frmEditExternalForm.get("selReturnElementType"+count);
					externalFormParameter.setVariableName(variableName);
					externalFormParameter.setParameterName(elementName);
					externalFormParameter.setParameterType(elementType);
					externalFormParameter.setType("R");
					externalFormParameterList.add(externalFormParameter);
				}
			}
			SwcExternalFormParameter[] externalFormParameters = null;
			if(externalFormParameterList.size() != 0) {
				externalFormParameters = new SwcExternalFormParameter[externalFormParameterList.size()];
				externalFormParameterList.toArray(externalFormParameters);
			}
			swcExternalForm.setSwcExternalFormParameters(externalFormParameters);

			getSwcManager().setExternalForm(userId, swcExternalForm, IManager.LEVEL_ALL);
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void removeExternalForm(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try {
			User cUser = SmartUtil.getCurrentUser();
			String formId = (String)requestBody.get("formId");
			if(formId.equals(""))
				return;
			getSwcManager().removeExternalForm(cUser.getId(), formId);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setMember(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try{
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			String companyId = cUser.getCompanyId();
			Map<String, Object> frmEditMember = (Map<String, Object>)requestBody.get("frmEditMember");

			String setUserId = (String)requestBody.get("userId");

			Set<String> keySet = frmEditMember.keySet();
			Iterator<String> itr = keySet.iterator();

			String txtMemberName = null;
			String txtMemberId = null;
			String pasMemberPassword = null;
			String txtMemberEmployeeId = null;
			String txtMemberPosition = null;
			int selMemberRole;
			int selMemberUserLevel;
			String selMemberLocale = null;
			String selMemberTimeZone = null;
			String txtMemberPhoneNo = null;
			String txtMemberCellPhoneNo = null;

			String birthYear = null, birthMonth = null, birthDay = null;
			String hireYear = null, hireMonth = null, hireDay = null;
			boolean isLunarBirthday = false;
			
			SwoUser swoUser = null;
			if(!setUserId.equals("") && !setUserId.equalsIgnoreCase("null") && setUserId != null) {
				swoUser = getSwoManager().getUser(userId, setUserId, IManager.LEVEL_ALL);
				if(swoUser.getDomainId() == null || swoUser.getDomainId() == "")
					swoUser.setDomainId("frm_user_SYSTEM");
				if(swoUser.getRetiree() == null || swoUser.getRetiree() == "")
					swoUser.setRetiree("N");
			} else {
				swoUser = new SwoUser();
				swoUser.setCompanyId(companyId);
				swoUser.setType("BASIC");
				swoUser.setDomainId("frm_user_SYSTEM");
				swoUser.setRetiree("N");
			}

			while(itr.hasNext()) {
				String fieldId = (String)itr.next();
				Object fieldValue = frmEditMember.get(fieldId);
				if(fieldValue instanceof String) {
					if(fieldId.equals("txtMemberName")) {
						txtMemberName = (String)frmEditMember.get("txtMemberName");
						swoUser.setName(txtMemberName);
					} else if(fieldId.equals("txtMemberId")) {
						txtMemberId = (String)frmEditMember.get("txtMemberId");
						swoUser.setId(txtMemberId);
						swoUser.setEmail(txtMemberId);
					} else if(fieldId.equals("pasMemberPassword")) {
						pasMemberPassword = (String)frmEditMember.get("pasMemberPassword");
						//pasMemberPassword = DigestUtils.md5Hex(pasMemberPassword);
						swoUser.setPassword(pasMemberPassword);
					} else if(fieldId.equals("txtMemberEmployeeId")) {
						txtMemberEmployeeId = (String)frmEditMember.get("txtMemberEmployeeId");
						swoUser.setEmpNo(txtMemberEmployeeId);
					} else if(fieldId.equals("txtMemberPosition")) {
						txtMemberPosition = (String)frmEditMember.get("txtMemberPosition");
						swoUser.setPosition(txtMemberPosition);
					} else if(fieldId.equals("selMemberRole")) {
						selMemberRole = Integer.parseInt((String)frmEditMember.get("selMemberRole"));
						swoUser.setRoleId(selMemberRole == User.USER_ROLE_LEADER ? "DEPT LEADER" : "DEPT MEMBER");
					} else if(fieldId.equals("selMemberUserLevel")) {
						selMemberUserLevel = Integer.parseInt((String)frmEditMember.get("selMemberUserLevel"));
						swoUser.setAuthId(selMemberUserLevel == User.USER_LEVEL_EXTERNAL_USER ? "EXTERNALUSER" : selMemberUserLevel == User.USER_LEVEL_INTERNAL_USER ? "USER" : selMemberUserLevel == User.USER_LEVEL_AMINISTRATOR ? "ADMINISTRATOR" : "OPERATOR");
					} else if(fieldId.equals("selMemberLocale")) {
						selMemberLocale = (String)frmEditMember.get("selMemberLocale");
						swoUser.setLocale(selMemberLocale);
					} else if(fieldId.equals("selMemberTimeZone")) {
						selMemberTimeZone = (String)frmEditMember.get("selMemberTimeZone");
						swoUser.setTimeZone(selMemberTimeZone);
					} else if(fieldId.equals("txtMemberPhoneNo")) {
						txtMemberPhoneNo = (String)frmEditMember.get("txtMemberPhoneNo");
						swoUser.setExtensionNo(txtMemberPhoneNo);
					} else if(fieldId.equals("txtMemberCellPhoneNo")) {
						txtMemberCellPhoneNo = (String)frmEditMember.get("txtMemberCellPhoneNo");
						swoUser.setMobileNo(txtMemberCellPhoneNo);
					} else if(fieldId.equals("txtMemberHomePhoneNo")) {
						swoUser.setHomePhoneNo((String)frmEditMember.get("txtMemberHomePhoneNo"));
					} else if(fieldId.equals("txtMemberHomeAddress")) {
						swoUser.setHomeAddress((String)frmEditMember.get("txtMemberHomeAddress"));
					} else if(fieldId.equals("txtMemberHireYear")) {
						hireYear = (String)frmEditMember.get("txtMemberHireYear");
					} else if(fieldId.equals("txtMemberHireMonth")) {
						hireMonth = (String)frmEditMember.get("txtMemberHireMonth");
					} else if(fieldId.equals("txtMemberHireDay")) {
						hireDay = (String)frmEditMember.get("txtMemberHireDay");
					} else if(fieldId.equals("txtMemberBirthYear")) {
						birthYear = (String)frmEditMember.get("txtMemberBirthYear");
					} else if(fieldId.equals("txtMemberBirthMonth")) {
						birthMonth = (String)frmEditMember.get("txtMemberBirthMonth");
					} else if(fieldId.equals("txtMemberBirthDay")) {
						birthDay = (String)frmEditMember.get("txtMemberBirthDay");
					} else if(fieldId.equals("selMemberLunarBirthday")) {
						isLunarBirthday = ((String)frmEditMember.get("selMemberLunarBirthday")).equalsIgnoreCase("true");
					}
				} else {
					if(fieldId.equals("memberDepartment")) {
						Map<String, Object> memberDepartment = (Map<String, Object>)frmEditMember.get("memberDepartment");
						
						//신규 사용자추가 
						String oldDepartmentId = (String)requestBody.get("departmentId");
						//기존 사용자변경 
						String oldParentId = (String)requestBody.get("parentId");
						
						if (memberDepartment != null && memberDepartment.size() != 0) {
							//Map<String, Object> department = (Map<String, Object>)memberDepartment.get("department");
							
							List<Map<String, Object>> departmentList = (ArrayList<Map<String, Object>>)memberDepartment.get("departments");
							if (departmentList.size() != 0) {
								Map<String, Object> departmentMap = (Map<String, Object>)departmentList.get(0);
								
								String newDepartmentId = (String)departmentMap.get("id");
								String newDepartmentName = (String)departmentMap.get("name");
								
								//겸직을 검사하여 겸직에서 이동하는것인지를 판단한다
								String userDeptId = swoUser.getDeptId();
								if (userDeptId != null && userDeptId.equalsIgnoreCase(oldParentId)) {
									swoUser.setDeptId(newDepartmentId);
									String userAdjunctDeptIds = swoUser.getAdjunctDeptIds();
									if (userAdjunctDeptIds != null && userAdjunctDeptIds.indexOf(oldParentId + "|") != -1) {
										userAdjunctDeptIds = StringUtils.replace(userAdjunctDeptIds, oldParentId + "|LEADER;", "");
										swoUser.setAdjunctDeptIds(userAdjunctDeptIds);
									}
								} else {
									if (userDeptId == null) {
										swoUser.setDeptId(newDepartmentId);
									} else {
										if (userDeptId.equalsIgnoreCase(newDepartmentId)) {
											String userAdjunctDeptIds = swoUser.getAdjunctDeptIds();
											if (userAdjunctDeptIds != null && userAdjunctDeptIds.indexOf(oldParentId + "|") != -1) {
												userAdjunctDeptIds = StringUtils.replace(userAdjunctDeptIds, oldParentId + "|LEADER;", "");
												swoUser.setAdjunctDeptIds(userAdjunctDeptIds);
											}
										} else {
											String userAdjunctDeptIds = swoUser.getAdjunctDeptIds();
											if (userAdjunctDeptIds != null && userAdjunctDeptIds.indexOf(oldParentId + "|") != -1) {
												userAdjunctDeptIds = StringUtils.replace(userAdjunctDeptIds, oldParentId + "|", newDepartmentId + "|");
												swoUser.setAdjunctDeptIds(userAdjunctDeptIds);
											}
										}
									}
								}
							}
						}
					}
				}
			}

			if(hireYear!=null && hireYear.length()==4 && hireMonth!=null && hireDay!=null){
				if(hireMonth.length()==1) hireMonth="0"+hireMonth;
				if(hireDay.length()==1) hireDay="0"+hireDay;
				try{
					swoUser.setHireDate(new Date(LocalDate.convertLocalDateStringToLocalDate1(hireYear+hireMonth+hireDay).getTime()));
				}catch (Exception e){
					swoUser.setHireDate(null);
				}				
			}else{
				swoUser.setHireDate(null);
			}
			
			if(birthYear!=null && birthYear.length()==4 && birthMonth!=null && birthDay!=null){
				if(birthMonth.length()==1) birthMonth="0"+birthMonth;
				if(birthDay.length()==1) birthDay="0"+birthDay;
				try{
					swoUser.setBirthDay(new Date(LocalDate.convertLocalDateStringToLocalDate1(birthYear+birthMonth+birthDay).getTime()));
					
				}catch (Exception e){
					swoUser.setBirthDay(null);
				}			
			}else{
				swoUser.setBirthDay(null);
			}
			swoUser.setLunarBirthday(isLunarBirthday);
			
			getSwoManager().setUser(userId, swoUser, IManager.LEVEL_ALL);
			if(!setUserId.equals(""))
				getSwoManager().getUserExtend(userId, swoUser.getId(), false);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void removeMember(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		try {
			String userId = (String)requestBody.get("userId");
			if(userId.equals(""))
				return;

			SwoUser user = SwManagerFactory.getInstance().getSwoManager().getUser(userId, userId, null);
			
			String userDepartmentId = user.getDeptId();
			String removeDepartmentId = (String)requestBody.get("departmentId");
			if (userDepartmentId.equalsIgnoreCase(removeDepartmentId)) {
				String adjunctDeptIdStr = user.getAdjunctDeptIds();
				if (CommonUtil.isEmpty(adjunctDeptIdStr)) {
					getSwoManager().removeUser(user.getId(), userId);
				} else {
					StringBuffer adjunctDeptIdsBuf = new StringBuffer();
					String[] adjunctDeptIds = StringUtils.tokenizeToStringArray(adjunctDeptIdStr , ";");
					
					if (adjunctDeptIds != null && adjunctDeptIds.length != 0) {
						String[] deptIdAndRole = StringUtils.tokenizeToStringArray(adjunctDeptIds[0], "|");
						user.setDeptId(deptIdAndRole[0]);
						adjunctDeptIdStr = StringUtils.replace(adjunctDeptIdStr, adjunctDeptIds[0]+";", "");
						user.setAdjunctDeptIds(CommonUtil.toNull(adjunctDeptIdStr));
						SwManagerFactory.getInstance().getSwoManager().setUser(userId, user, null);
						
					}
				}
			} else {
				String adjunctDeptIdStr = user.getAdjunctDeptIds();
				StringBuffer adjunctDeptIdsBuf = new StringBuffer();
				String[] adjunctDeptIds = StringUtils.tokenizeToStringArray(adjunctDeptIdStr , ";");
				for (int i = 0; i < adjunctDeptIds.length; i++) {
					if (CommonUtil.isEmpty(adjunctDeptIds))
						continue;
					String[] deptIdAndRole = StringUtils.tokenizeToStringArray(adjunctDeptIds[i], "|");
					if (!deptIdAndRole[0].equalsIgnoreCase(removeDepartmentId)) {
						adjunctDeptIdsBuf.append(adjunctDeptIds[i]).append(";");
					}
				}
				user.setAdjunctDeptIds(adjunctDeptIdsBuf.toString());
				SwManagerFactory.getInstance().getSwoManager().setUser(userId, user, null);
			}
		} catch(Exception e) {
			e.printStackTrace();			
		}
	}

	@Override
	public void setDepartment(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try{
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			String companyId = cUser.getCompanyId();
			Map<String, Object> frmEditDepartment = (Map<String, Object>)requestBody.get("frmEditDepartment");

			String departmentId = (String)requestBody.get("departmentId");

			Set<String> keySet = frmEditDepartment.keySet();
			Iterator<String> itr = keySet.iterator();

			String hdnParentId = null;
			String txtDepartmentName = null;

			SwoDepartment swoDepartment = null;
			if(!departmentId.equals("") && !departmentId.equalsIgnoreCase("null") && departmentId != null) {
				swoDepartment = getSwoManager().getDepartment(userId, departmentId, IManager.LEVEL_ALL);
			} else {
				swoDepartment = new SwoDepartment();
				//start 2012.08.21 부서 추가 시 dept_ 가 안붙어서, smartUtil.newid(); 대신 추가
				swoDepartment.setId(IDCreator.createId(SmartServerConstant.DEPT_ABBR));
				//end jybae
				swoDepartment.setCompanyId(companyId);
				swoDepartment.setType("BASIC");
				swoDepartment.setDomainId("frm_dept_SYSTEM");
			}

			while (itr.hasNext()) {
				String fieldId = (String)itr.next();
				Object fieldValue = frmEditDepartment.get(fieldId);
				if(fieldValue instanceof String) {
//					if(fieldId.equals("hdnParentId")) {
//						hdnParentId = (String)frmEditDepartment.get("hdnParentId");
//						swoDepartment.setParentId(hdnParentId);
//					} else 
					if(fieldId.equals("txtDepartmentName")) {
						txtDepartmentName = (String)frmEditDepartment.get("txtDepartmentName");
						swoDepartment.setName(txtDepartmentName);
					}
				} else {
					if(fieldId.equals("parentDepartment")) {
//						txtDepartmentName = (String)frmEditDepartment.get("txtDepartmentName");
//						swoDepartment.setName(txtDepartmentName);
						
						Map<String, Object> parentDepartment = (Map<String, Object>)frmEditDepartment.get("parentDepartment");

						if (parentDepartment != null && parentDepartment.size() != 0) {
							//Map<String, Object> department = (Map<String, Object>)parentDepartment.get("department");
							List<Map<String, Object>> departmentList = (ArrayList<Map<String, Object>>)parentDepartment.get("departments");
							
							if (departmentList.size() != 0) {
								Map<String, Object> departmentMap = (Map<String, Object>)departmentList.get(0);
								
								String newParentDepartmentId = (String)departmentMap.get("id");
								String newParentDepartmentName = (String)departmentMap.get("name");

								swoDepartment.setParentId(newParentDepartmentId);
							
							}
						}
					}
				}
			}

			getSwoManager().setDepartment(userId, swoDepartment, IManager.LEVEL_ALL);
			
			if(!departmentId.equals("") && !departmentId.equalsIgnoreCase("null") && departmentId != null)
				getSwoManager().getDepartmentExtend(userId, departmentId, false);
			
			SwaDepartmentCond deptAuthCond = new SwaDepartmentCond();
			deptAuthCond.setDeptId(swoDepartment.getId());
			long deptAuthSize = SwManagerFactory.getInstance().getSwaManager().getAuthDepartmentSize(cUser.getId(), deptAuthCond);
			if (deptAuthSize == 0 ) {
				SwaDepartment boardWrite = new SwaDepartment();
				boardWrite.setDeptAuthType(SwaDepartment.DEPT_AUTHTYPE_BOARD_WRITE);
				boardWrite.setDeptId(swoDepartment.getId());
				boardWrite.setRoleKey(SwaDepartment.DEPT_ROLEKYE_ADMIN + ";" + SwaDepartment.DEPT_ROLEKYE_LEADER);

				SwaDepartment boardEdit = new SwaDepartment();
				boardEdit.setDeptAuthType(SwaDepartment.DEPT_AUTHTYPE_BOARD_EDIT);
				boardEdit.setDeptId(swoDepartment.getId());
				boardEdit.setRoleKey(SwaDepartment.DEPT_ROLEKYE_OWNER + ";" + SwaDepartment.DEPT_ROLEKYE_ADMIN + ";" + SwaDepartment.DEPT_ROLEKYE_LEADER);

				SwaDepartment eventWrite = new SwaDepartment();
				eventWrite.setDeptAuthType(SwaDepartment.DEPT_AUTHTYPE_EVENT_WRITE);
				eventWrite.setDeptId(swoDepartment.getId());
				eventWrite.setRoleKey(SwaDepartment.DEPT_ROLEKYE_ADMIN + ";" + SwaDepartment.DEPT_ROLEKYE_LEADER);
				
				SwaDepartment eventEdit = new SwaDepartment();
				eventEdit.setDeptAuthType(SwaDepartment.DEPT_AUTHTYPE_EVENT_EDIT);
				eventEdit.setDeptId(swoDepartment.getId());
				eventEdit.setRoleKey(SwaDepartment.DEPT_ROLEKYE_OWNER + ";" + SwaDepartment.DEPT_ROLEKYE_ADMIN + ";" + SwaDepartment.DEPT_ROLEKYE_LEADER);
				
				SwManagerFactory.getInstance().getSwaManager().setAuthDepartment(cUser.getId(), boardWrite, null);
				SwManagerFactory.getInstance().getSwaManager().setAuthDepartment(cUser.getId(), boardEdit, null);
				SwManagerFactory.getInstance().getSwaManager().setAuthDepartment(cUser.getId(), eventWrite, null);
				SwManagerFactory.getInstance().getSwaManager().setAuthDepartment(cUser.getId(), eventEdit, null);
			}
			
		} catch(Exception e) {
			e.printStackTrace();			
		}
	}

	@Override
	public void removeDepartment(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try {
			User cUser = SmartUtil.getCurrentUser();
			String departmentId = (String)requestBody.get("departmentId");
			if(departmentId.equals(""))
				return;
			getSwoManager().removeDepartment(cUser.getId(), departmentId);
		} catch(Exception e) {
			e.printStackTrace();			
		}
	}

	@Override
	public void checkIdDuplication(HttpServletRequest request) throws Exception {

		try {
			boolean isExistId = getSwoManager().isExistId(request.getParameter("userId"));
			if(isExistId) throw new DuplicateKeyException("duplicateKeyException");
		} catch(Exception e) {
			throw new DuplicateKeyException("duplicateKeyException");
		}
	}
	
	@Override
	public RecordList getEmailServerList(RequestParams params) throws Exception {

		try {
			RecordList recordList = new RecordList();
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			String companyId = cUser.getCompanyId();

			MailServerCond mailServerCond = new MailServerCond();
			mailServerCond.setCompanyId(companyId);

			long totalCount = getMailManager().getMailServerSize(userId, mailServerCond);

			int pageSize = params.getPageSize();
			if(pageSize == 0) pageSize = 20;

			int currentPage = params.getCurrentPage();
			if(currentPage == 0) currentPage = 1;

			int totalPages = (int)totalCount % pageSize;

			if(totalPages == 0)
				totalPages = (int)totalCount / pageSize;
			else
				totalPages = (int)totalCount / pageSize + 1;

			int result = 0;

			if(params.getPagingAction() != 0) {
				if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXT10) {
					result = (((currentPage - 1) / 10) * 10) + 11;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_NEXTEND) {
					result = totalPages;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREV10) {
					result = ((currentPage - 1) / 10) * 10;
				} else if(params.getPagingAction() == RequestParams.PAGING_ACTION_PREVEND) {
					result = 1;
				}
				currentPage = result;
			}

			if(previousPageSize != pageSize)
				currentPage = 1;

			previousPageSize = pageSize;

			if((long)((pageSize * (currentPage - 1)) + 1) > totalCount)
				currentPage = 1;

			if (currentPage > 0)
				mailServerCond.setPageNo(currentPage-1);

			mailServerCond.setPageSize(pageSize);

			mailServerCond.setOrders(new Order[]{new Order(MailServerCond.A_CREATIONDATE, false)});
			MailServer[] mailServers = getMailManager().getMailServers(userId, mailServerCond, IManager.LEVEL_ALL);

			EmailServer[] emailServers = null;
			List<EmailServer> emailServerList = new ArrayList<EmailServer>();
			if(mailServers != null) {
				for(MailServer mailServer : mailServers) {
					EmailServer emailServer = new EmailServer();
					emailServer.setId(mailServer.getObjId());
					emailServer.setName(mailServer.getName());
					emailServer.setFetchServer(mailServer.getFetchServer());
					emailServer.setFetchServerPort(mailServer.getFetchServerPort());
					emailServer.setFetchProtocol(mailServer.getFetchProtocol());
					emailServer.setFetchSsl(mailServer.isFetchSsl());
					emailServer.setSmtpServer(mailServer.getSmtpServer());
					emailServer.setSmtpServerPort(mailServer.getSmtpServerPort());
					emailServer.setSmtpAuthenticated(mailServer.isSmtpAuthenticated());
					emailServer.setSmtpSsl(mailServer.isSmtpSsl());
					emailServerList.add(emailServer);
				}
			}
			if(emailServerList.size() > 0) {
				emailServers = new EmailServer[emailServerList.size()];
				emailServerList.toArray(emailServers);
			}

			recordList.setRecords(emailServers);
			recordList.setPageSize(pageSize);
			recordList.setTotalPages(totalPages);
			recordList.setCurrentPage(currentPage);
			recordList.setType(InstanceInfoList.TYPE_INFORMATION_INSTANCE_LIST);

			return recordList;
		} catch(Exception e) {
			e.printStackTrace();
			return null;			
		}

	}
	
	@Override
	public EmailServer getEmailServerById(String id) throws Exception {
		try {
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();

			MailServer mailServer = getMailManager().getMailServer(userId, id, IManager.LEVEL_ALL);

			EmailServer emailServer = null;
			if(!SmartUtil.isBlankObject(mailServer)) {
				emailServer = new EmailServer();
				emailServer.setId(mailServer.getObjId());
				emailServer.setName(mailServer.getName());
				emailServer.setFetchServer(mailServer.getFetchServer());
				emailServer.setFetchServerPort(mailServer.getFetchServerPort());
				emailServer.setFetchProtocol(mailServer.getFetchProtocol());
				emailServer.setFetchSsl(mailServer.isFetchSsl());
				emailServer.setSmtpServer(mailServer.getSmtpServer());
				emailServer.setSmtpServerPort(mailServer.getSmtpServerPort());
				emailServer.setSmtpAuthenticated(mailServer.isSmtpAuthenticated());
				emailServer.setSmtpSsl(mailServer.isSmtpSsl());
				emailServer.setPwChangeAPI(mailServer.getPwChangeAPI());
				emailServer.setPwChangeDefaultData(mailServer.getPwChangeDefaultData());
				emailServer.setPwChangeParamId(mailServer.getPwChangeParamId());
				emailServer.setPwChangeParamOldPW(mailServer.getPwChangeParamOldPW());
				emailServer.setPwChangeParamNewPW(mailServer.getPwChangeParamNewPW());
			}
			return emailServer;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void setEmailServer(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		try {
			/*{
				   txtEmailServerName=hanmail.net,
				   txtEmailServerFetchServer=pop.daum.net,
				   txtEmailServerFetchPort=995,
				   selEmailServerFetchProtocol=pop3,
				   txtEmailServerFetchSsl=on,
				   txtEmailServerSmtpServer=smtp.daum.net,
				   txtEmailServerSmtpPort=465,
				   txtEmailServerSmtpAuthenticated=on,
				   txtEmailServerSmtpSsl=on,
				  
				   txtPWChangeAPI=http:   //localhost:8080   /smartworksV3/emailApi.jsp,
				   txtPWChangeDefaultData=defaultValue,
				   txtPWChangeParamId=idValue,
				   txtPWChangeParamOldPW=oldPassValue,
				   txtPWChangeParamNewPW=newPassValue
				}*/
			
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			String companyId = cUser.getCompanyId();

			String emailServerId = (String)requestBody.get("emailServerId");
			Map<String, Object> frmEditEmailServer = (Map<String, Object>)requestBody.get("frmEditEmailServer");

			Set<String> keySet = frmEditEmailServer.keySet();
			Iterator<String> itr = keySet.iterator();

			String txtEmailServerName = null;
			String txtEmailServerFetchServer = null;
			String txtEmailServerFetchPort = null;
			String selEmailServerFetchProtocol = null;
			boolean txtEmailServerFetchSsl = false;
			String txtEmailServerSmtpServer = null;
			String txtEmailServerSmtpPort = null;
			boolean txtEmailServerSmtpAuthenticated = false;
			boolean txtEmailServerSmtpSsl = false;

			String txtPWChangeAPI = null;
			String txtPWChangeDefaultData = null;
			String txtPWChangeParamId = null;
			String txtPWChangeParamOldPW = null;
			String txtPWChangeParamNewPW = null;

			while(itr.hasNext()) {
				String fieldId = (String)itr.next();
				Object fieldValue = frmEditEmailServer.get(fieldId);
				if(fieldValue instanceof String) {
					String valueString = (String)fieldValue;
					if(fieldId.equals("txtEmailServerName")) {
						txtEmailServerName = valueString;
					} else if(fieldId.equals("txtEmailServerFetchServer")) {
						txtEmailServerFetchServer = valueString;
					} else if(fieldId.equals("txtEmailServerFetchPort")) {
						txtEmailServerFetchPort = valueString;
					} else if(fieldId.equals("selEmailServerFetchProtocol")) {
						selEmailServerFetchProtocol = valueString;
					} else if(fieldId.equals("txtEmailServerFetchSsl")) {
						txtEmailServerFetchSsl = true;
					} else if(fieldId.equals("txtEmailServerSmtpServer")) {
						txtEmailServerSmtpServer = valueString;
					} else if(fieldId.equals("txtEmailServerSmtpPort")) {
						txtEmailServerSmtpPort = valueString;
					} else if(fieldId.equals("txtEmailServerSmtpAuthenticated")) {
						txtEmailServerSmtpAuthenticated = true;
					} else if(fieldId.equals("txtEmailServerSmtpSsl")) {
						txtEmailServerSmtpSsl = true;
					} else if(fieldId.equals("txtPWChangeAPI")) {
						txtPWChangeAPI = valueString;
					} else if(fieldId.equals("txtPWChangeDefaultData")) {
						txtPWChangeDefaultData = valueString;
					} else if(fieldId.equals("txtPWChangeParamId")) {
						txtPWChangeParamId = valueString;
					} else if(fieldId.equals("txtPWChangeParamOldPW")) {
						txtPWChangeParamOldPW = valueString;
					} else if(fieldId.equals("txtPWChangeParamNewPW")) {
						txtPWChangeParamNewPW = valueString;
					}
				}
			} 

			MailServer mailServer = null;
			if(!CommonUtil.isEmpty(emailServerId))
				mailServer = getMailManager().getMailServer(userId, emailServerId, IManager.LEVEL_ALL);
			else
				mailServer = new MailServer();

			mailServer.setName(txtEmailServerName);
			mailServer.setCompanyId(companyId);
			mailServer.setFetchServer(txtEmailServerFetchServer);
			mailServer.setFetchServerPort(Integer.parseInt(txtEmailServerFetchPort));
			mailServer.setFetchProtocol(selEmailServerFetchProtocol);
			mailServer.setFetchSsl(txtEmailServerFetchSsl);
			mailServer.setSmtpServer(txtEmailServerSmtpServer);
			mailServer.setSmtpServerPort(Integer.parseInt(txtEmailServerSmtpPort));
			mailServer.setSmtpAuthenticated(txtEmailServerSmtpAuthenticated);
			mailServer.setSmtpSsl(txtEmailServerSmtpSsl);
			mailServer.setPwChangeAPI(txtPWChangeAPI);
			mailServer.setPwChangeDefaultData(txtPWChangeDefaultData);
			mailServer.setPwChangeParamId(txtPWChangeParamId);
			mailServer.setPwChangeParamOldPW(txtPWChangeParamOldPW);
			mailServer.setPwChangeParamNewPW(txtPWChangeParamNewPW);
			
			getMailManager().setMailServer(userId, mailServer, IManager.LEVEL_ALL);

		} catch(Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	
	@Override
	public void removeEmailServer(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		try {
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			String emailServerId = (String)requestBody.get("emailServerId");
			getMailManager().removeMailServer(userId, emailServerId);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	@Override
	public EmailServer[] getEmailServers() throws Exception {

		User cUser = SmartUtil.getCurrentUser();
		String userId = cUser.getId();
		String companyId = cUser.getCompanyId();

		MailServerCond mailServerCond = new MailServerCond();
		mailServerCond.setCompanyId(companyId);
		mailServerCond.setOrders(new Order[]{new Order(MailServerCond.A_CREATIONDATE, false)});

		MailServer[] mailServers = getMailManager().getMailServers(userId, mailServerCond, IManager.LEVEL_ALL);

		EmailServer[] emailServers = null;
		List<EmailServer> emailServerList = new ArrayList<EmailServer>();
		if(mailServers != null) {
			for(MailServer mailServer : mailServers) {
				EmailServer emailServer = new EmailServer();
				emailServer.setId(mailServer.getObjId());
				emailServer.setName(mailServer.getName());
				emailServer.setFetchServer(mailServer.getFetchServer());
				emailServer.setFetchServerPort(mailServer.getFetchServerPort());
				emailServer.setFetchProtocol(mailServer.getFetchProtocol());
				emailServer.setFetchSsl(mailServer.isFetchSsl());
				emailServer.setSmtpServer(mailServer.getSmtpServer());
				emailServer.setSmtpServerPort(mailServer.getSmtpServerPort());
				emailServer.setSmtpAuthenticated(mailServer.isSmtpAuthenticated());
				emailServer.setSmtpSsl(mailServer.isSmtpSsl());
				emailServer.setPwChangeAPI(mailServer.getPwChangeAPI());
				emailServer.setPwChangeDefaultData(mailServer.getPwChangeDefaultData());
				emailServer.setPwChangeParamId(mailServer.getPwChangeParamId());
				emailServer.setPwChangeParamOldPW(mailServer.getPwChangeParamOldPW());
				emailServer.setPwChangeParamNewPW(mailServer.getPwChangeParamNewPW());
				emailServerList.add(emailServer);
			}
		}
		if(emailServerList.size() > 0) {
			emailServers = new EmailServer[emailServerList.size()];
			emailServerList.toArray(emailServers);
		}
		return emailServers;
	}

	@Override
	public ConnectionProfile[] getMailConnectionProfiles() throws Exception {
		MailAccount[] mailAccounts = communityService.getMyMailAccounts();
		if(SmartUtil.isBlankObject(mailAccounts)) return null;
		ConnectionProfile[] connectionProfiles = new ConnectionProfile[mailAccounts.length];		
		for(int i=0; i<mailAccounts.length; i++){
			MailAccount mailAccount = mailAccounts[i];
			EmailServer server = getEmailServerById(mailAccount.getEmailServerId());
			connectionProfiles[i] = server.getConnectionProfile();
		}
		return connectionProfiles;
	}
	@Override
	public void addAdjunctMember(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		/*{
			   frmAdjunctMember=   {
			      hdnDepartmentId=dept_302bcb7c7db04a7ba03a7bc43da23a07,
			      txtAdjunctUser=      {
			         users=         [
			            {
			               id=ktsoo@maninsoft.co.kr,
			               name=김태수
			            }
			         ]
			      }
			   }
			}*/
		User user = SmartUtil.getCurrentUser();
		String userId = user.getId();
		
		Map<String, Object> frmAdjunctMember = (Map<String, Object>)requestBody.get("frmAdjunctMember");
		String departmentId = (String)frmAdjunctMember.get("hdnDepartmentId");
		Map<String, Object> txtAdjunctUser = (Map<String, Object>)frmAdjunctMember.get("txtAdjunctUser");
		List<Map<String,String>> users = (ArrayList<Map<String,String>>)txtAdjunctUser.get("users");
		
		SwoUserCond userCond = new SwoUserCond();
		userCond.setDeptId(departmentId);
		SwoUser[] deptMemberUsers = SwManagerFactory.getInstance().getSwoManager().getUsers(userId, userCond, IManager.LEVEL_LITE);
		List deptMemberIdList = new ArrayList();
		if (deptMemberUsers != null && deptMemberUsers.length != 0) {
			for (int i = 0; i < deptMemberUsers.length; i++) {
				deptMemberIdList.add(deptMemberUsers[i].getId());
			}
		}
		SwoUserCond userAjtCond = new SwoUserCond();
		userAjtCond.setAdjunctDeptIdsLike(departmentId);
		SwoUser[] deptAjtMemberUsers = SwManagerFactory.getInstance().getSwoManager().getUsers(userId, userCond, IManager.LEVEL_LITE);
		List deptAjtMemberIdList = new ArrayList();
		if (deptAjtMemberUsers != null && deptAjtMemberUsers.length != 0) {
			for (int i = 0; i < deptAjtMemberUsers.length; i++) {
				deptAjtMemberIdList.add(deptAjtMemberUsers[i].getId());
			}
		}
		
		if(users != null && users.size() != 0) {
			for (int i = 0; i < users.size(); i++) {
				Map<String, String> userMap = users.get(i);
				String addUserId = userMap.get("id");
				
				if (deptMemberIdList.contains(addUserId))
					continue;
				SwoUser swoUser = SwManagerFactory.getInstance().getSwoManager().getUser(userId, addUserId, IManager.LEVEL_ALL);
				String adjunctMemberStr = CommonUtil.toNotNull(swoUser.getAdjunctDeptIds());
				adjunctMemberStr = adjunctMemberStr + departmentId + "|LEADER" + ";";
				swoUser.setAdjunctDeptIds(adjunctMemberStr);
				SwManagerFactory.getInstance().getSwoManager().setUser(userId, swoUser, IManager.LEVEL_ALL);
				SwManagerFactory.getInstance().getSwoManager().getUserExtend(userId, swoUser.getId(), false);
			}
		}
	}
	@Override
	public UsedWorkInfo[] getUsedWorkListByCommunityId(String communityId) throws Exception {
		return SmartTest.getUsedWorkListByUserId(communityId);
	}
	@Override
	public UserInfo getHeadByUserId(String userId) throws Exception {
		// TODO Auto-generated method stub
		return SmartTest.getHeadByUserId(userId);
	}
	@Override
	public void executeRetireMember(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void executeWorkTransfer(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
