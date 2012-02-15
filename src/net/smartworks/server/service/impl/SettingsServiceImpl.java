package net.smartworks.server.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.smartworks.model.RecordList;
import net.smartworks.model.approval.ApprovalLine;
import net.smartworks.model.calendar.CompanyEvent;
import net.smartworks.model.calendar.WorkHour;
import net.smartworks.model.calendar.WorkHourPolicy;
import net.smartworks.model.community.Community;
import net.smartworks.model.community.User;
import net.smartworks.model.company.CompanyGeneral;
import net.smartworks.model.instance.info.InstanceInfoList;
import net.smartworks.model.instance.info.RequestParams;
import net.smartworks.model.service.ExternalForm;
import net.smartworks.model.service.WebService;
import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.model.Order;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.config.manager.ISwcManager;
import net.smartworks.server.engine.config.model.SwcEventDay;
import net.smartworks.server.engine.config.model.SwcEventDayCond;
import net.smartworks.server.engine.config.model.SwcWorkHour;
import net.smartworks.server.engine.config.model.SwcWorkHourCond;
import net.smartworks.server.engine.config.webservice.manager.IWebServiceManager;
import net.smartworks.server.engine.config.webservice.model.WebServiceCond;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.organization.manager.ISwoManager;
import net.smartworks.server.engine.organization.model.SwoCompany;
import net.smartworks.server.engine.organization.model.SwoConfig;
import net.smartworks.server.engine.organization.model.SwoDepartment;
import net.smartworks.server.engine.organization.model.SwoUser;
import net.smartworks.server.service.ICommunityService;
import net.smartworks.server.service.ISettingsService;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingsServiceImpl implements ISettingsService {

	private ISwoManager getSwoManager() {
		return SwManagerFactory.getInstance().getSwoManager();
	}
	private ISwcManager getSwcManager() {
		return SwManagerFactory.getInstance().getSwcManager();
	}
	private IWebServiceManager getWebServiceManager() {
		return SwManagerFactory.getInstance().getWebServiceManager();
	}

	ICommunityService communityService;
	
	@Autowired
	public void setCommunityService(ICommunityService communityService) {
		this.communityService = communityService;
	}

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
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();
			String companyId = user.getCompanyId();
			SwoCompany swoCompany = getSwoManager().getCompany(userId, companyId, IManager.LEVEL_ALL);
			SwoConfig swoConfig = getSwoManager().getConfig(userId, companyId, IManager.LEVEL_ALL);
			String id = swoCompany.getId();
			String name = swoCompany.getName();
			String logoName = getSwoManager().getLogo(userId, companyId);
			String sendMailHost = swoConfig.getSmtpAddress();
			String sendMailAccount = swoConfig.getUserId();
			String sendMailPassword = swoConfig.getPassword();
			boolean sendMailNotification = swoConfig.isActivity();
			CompanyGeneral companyGeneral = new CompanyGeneral();
			companyGeneral.setId(id);
			companyGeneral.setName(name);
			companyGeneral.setLogoName(logoName);
			companyGeneral.setSendMailHost(sendMailHost);
			companyGeneral.setSendMailAccount(sendMailAccount);
			companyGeneral.setSendMailPassword(sendMailPassword);
			companyGeneral.setSendMailNotification(sendMailNotification);
			companyGeneral.setTestAfterSaving(true);
			return companyGeneral;
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}		
	}

	@Override
	public void setCompanyGeneral(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try {
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();
			String companyId = user.getCompanyId();
			String companyName = user.getCompany();

			Map<String, Object> frmCompanyGeneral = (Map<String, Object>)requestBody.get("frmCompanyGeneral");
	
			Set<String> keySet = frmCompanyGeneral.keySet();
			Iterator<String> itr = keySet.iterator();
			List<Map<String, String>> files = null;
			String txtMailHost = null;
			String txtMailAccount = null;
			String pasMailPassword = null;
			boolean isActivity = false;
			String imgCompanyLogo = null;
			String companyFileId = null;
			String companyFileName = null;

			while (itr.hasNext()) {
				String fieldId = (String)itr.next();
				Object fieldValue = frmCompanyGeneral.get(fieldId);
				if (fieldValue instanceof LinkedHashMap) {
					Map<String, Object> valueMap = (Map<String, Object>)fieldValue;
					if(fieldId.equals("imgCompanyLogo")) {
						files = (ArrayList<Map<String,String>>)valueMap.get("files");
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
					}
				}
			}

			if(!files.isEmpty()) {
				for(int i=0; i < files.subList(0, files.size()).size(); i++) {
					Map<String, String> fileMap = files.get(i);
					companyFileId = fileMap.get("fileId");
					companyFileName = fileMap.get("fileName");
					imgCompanyLogo = SwManagerFactory.getInstance().getDocManager().insertProfilesFile(companyFileId, companyFileName, user.getCompanyId());
					getSwoManager().setLogo(user.getId(), user.getCompanyId(), imgCompanyLogo);
				}
			}

			SwoConfig swoConfig = getSwoManager().getConfig(userId, companyId, IManager.LEVEL_ALL);

			swoConfig.setId(companyId);
			swoConfig.setName(companyName);
			swoConfig.setCompanyId(companyId);
			swoConfig.setSmtpAddress(txtMailHost);
			swoConfig.setUserId(txtMailAccount);
			swoConfig.setPassword(pasMailPassword);
			swoConfig.setActivity(isActivity);
			getSwoManager().setConfig(userId, swoConfig, IManager.LEVEL_ALL);
		} catch(Exception e) {
			e.printStackTrace();			
		}
	}
	
	@Override
	public RecordList getWorkHourPolicyList(RequestParams params) throws Exception {

		try{
			RecordList recordList = new RecordList();
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();
			String companyId = user.getCompanyId();

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

			if (currentPage > 0)
				swcWorkHourCond.setPageNo(currentPage-1);

			if((long)((pageSize * (currentPage - 1)) + 1) > totalCount)
				currentPage = 1;

			swcWorkHourCond.setPageSize(pageSize);

			swcWorkHourCond.setOrders(new Order[]{new Order("modificationDate", false)});
			SwcWorkHour[] swcWorkHours = getSwcManager().getWorkhours(userId, swcWorkHourCond, IManager.LEVEL_ALL); 
			List<WorkHourPolicy> workHourPolicyList = new ArrayList<WorkHourPolicy>();

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
				WorkHourPolicy[] workHourPolicies = new WorkHourPolicy[workHourPolicyList.size()];
				workHourPolicyList.toArray(workHourPolicies);

				recordList.setRecords(workHourPolicies);
				recordList.setPageSize(pageSize);
				recordList.setTotalPages(totalPages);
				recordList.setCurrentPage(currentPage);
				recordList.setType(InstanceInfoList.TYPE_INFORMATION_INSTANCE_LIST);

				return recordList;
			} else {
				return null;
			}
		 } catch(Exception e) {
			e.printStackTrace();
			return null;			
		}		

	}

	@Override
	public WorkHourPolicy getWorkHourPolicyById(String id) throws Exception {

		try {
			User user = SmartUtil.getCurrentUser();
			SwcWorkHour swcWorkHour = getSwcManager().getWorkhour(user.getId(), id, IManager.LEVEL_ALL);
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
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();
			String companyId = user.getCompanyId();

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
			while (itr.hasNext()) {
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
			User user = SmartUtil.getCurrentUser();
			String policyId = (String)requestBody.get("policyId");
			if(policyId.equals(""))
				return;
			getSwcManager().removeWorkhour(user.getId(), policyId);
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

			if (currentPage > 0)
				swcEventDayCond.setPageNo(currentPage-1);

			if((long)((pageSize * (currentPage - 1)) + 1) > totalCount)
				currentPage = 1;

			swcEventDayCond.setPageSize(pageSize);

			swcEventDayCond.setOrders(new Order[]{new Order("startDay", false)});
			SwcEventDay[] swcEventDays = getSwcManager().getEventdays(userId, swcEventDayCond, IManager.LEVEL_ALL);

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
				CompanyEvent[] companyEvents = new CompanyEvent[companyEventList.size()];
				companyEventList.toArray(companyEvents);

				recordList.setRecords(companyEvents);
				recordList.setPageSize(pageSize);
				recordList.setTotalPages(totalPages);
				recordList.setCurrentPage(currentPage);
				recordList.setType(InstanceInfoList.TYPE_INFORMATION_INSTANCE_LIST);

				return recordList;
			} else {
				return null;
			}
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

			if(swcEventDay != null) {
				CompanyEvent companyEvent = new CompanyEvent();
				boolean isHoliDay = swcEventDay.getType().equals(CompanyEvent.EVENT_TYPE_HOLIDAY) ? true : false;
				LocalDate plannedStart = new LocalDate(swcEventDay.getStartDay().getTime());
				LocalDate plannedEnd = new LocalDate(swcEventDay.getEndDay().getTime());
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
				return companyEvent;
			} else {
				return null;
			}
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

			while (itr.hasNext()) {
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
			User user = SmartUtil.getCurrentUser();
			String eventId = (String)requestBody.get("eventId");
			if(eventId.equals(""))
				return;
			getSwcManager().removeEventday(user.getId(), eventId);
		} catch(Exception e) {
			e.printStackTrace();			
		}
	}
	
	@Override
	public RecordList getApprovalLineList(RequestParams params) throws Exception {

		try{
			RecordList recordList = new RecordList();
			recordList.setRecords(new ApprovalLine[]{});
			return recordList;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
	}

	@Override
	public ApprovalLine getApprovalLineById(String id) throws Exception {

		try{
			return new ApprovalLine();
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
	}

	@Override
	public void setApprovalLine(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try{
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}
	}
	
	@Override
	public void removeApprovalLine(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try{
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}
	}
	
	@Override
	public RecordList getWebServiceList(RequestParams params) throws Exception {
		return null;
		/*try {
			RecordList recordList = new RecordList();
			User cUser = SmartUtil.getCurrentUser();
			String userId = cUser.getId();
			String companyId = cUser.getCompanyId();

			WebServiceCond webServiceCond = new WebServiceCond();
			webServiceCond.setCompanyId(companyId);

			long totalCount = getWebServiceManager().getWebServiceSize(userId, webServiceCond);

			int pageSize = params.getPageSize();
			if(pageSize == 0) pageSize = 20;

			int currentPage = params.getCurrentPage();
			if(currentPage == 0) currentPage = 1;

			int totalPages = (int)totalCount % pageSize;

			if(totalPages == 0)
				totalPages = (int)totalCount / pageSize;
			else
				totalPages = (int)totalCount / pageSize + 1;

			if (currentPage > 0)
				swcEventDayCond.setPageNo(currentPage-1);

			if((long)((pageSize * (currentPage - 1)) + 1) > totalCount)
				currentPage = 1;

			swcEventDayCond.setPageSize(pageSize);

			swcEventDayCond.setOrders(new Order[]{new Order("startDay", false)});
			SwcEventDay[] swcEventDays = getSwcManager().getEventdays(userId, swcEventDayCond, IManager.LEVEL_ALL);

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
				CompanyEvent[] companyEvents = new CompanyEvent[companyEventList.size()];
				companyEventList.toArray(companyEvents);

				recordList.setRecords(companyEvents);
				recordList.setPageSize(pageSize);
				recordList.setTotalPages(totalPages);
				recordList.setCurrentPage(currentPage);
				recordList.setType(InstanceInfoList.TYPE_INFORMATION_INSTANCE_LIST);

				return recordList;
			} else {
				return null;
			}
		} catch(Exception e) {
			e.printStackTrace();
			return null;			
		}*/
	}

	@Override
	public WebService getWebServiceById(String id) throws Exception {

		try{
			return new WebService();
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
	}

	@Override
	public void setWebService(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try{
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}
	}
	
	@Override
	public void removeWebService(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try{
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}
	}
	
	@Override
	public RecordList getExternalFormList(RequestParams params) throws Exception {

		try{
			RecordList recordList = new RecordList();
			recordList.setRecords(new ExternalForm[]{});
			return recordList;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
	}

	@Override
	public ExternalForm getExternalFormById(String id) throws Exception {

		try{
			return new ExternalForm();
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
	}

	@Override
	public void setExternalForm(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try{
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}
	}
	
	@Override
	public void removeExternalForm(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try{
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
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

			String hdnDepartmentId = null;
			String txtMemberName = null;
			String txtMemberId = null;
			String pasMemberPassword = null;
			String txtMemberEmployeeId = null;
			String txtMemberPosition = null;
			String selMemberRole = null;
			String selMemberUserLevel = null;
			String selMemberLocale = null;
			String selMemberTimeZone = null;
			String txtMemberPhoneNo = null;
			String txtMemberCellPhoneNo = null;

			SwoUser swoUser = null;
			if(!setUserId.equals("")) {
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

			while (itr.hasNext()) {
				String fieldId = (String)itr.next();
				Object fieldValue = frmEditMember.get(fieldId);
				if(fieldValue instanceof String) {
					if(fieldId.equals("hdnDepartmentId")) {
						hdnDepartmentId = (String)frmEditMember.get("hdnDepartmentId");
						swoUser.setDeptId(hdnDepartmentId);
					} else if(fieldId.equals("txtMemberName")) {
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
						selMemberRole = (String)frmEditMember.get("selMemberRole");
						swoUser.setRoleId(selMemberRole.equals(User.USER_ROLE_LEADER) ? "DEPT LEADER" : "DEPT MEMBER");
					} else if(fieldId.equals("selMemberUserLevel")) {
						selMemberUserLevel = (String)frmEditMember.get("selMemberUserLevel");
						swoUser.setAuthId(selMemberUserLevel.equals(User.USER_LEVEL_EXTERNAL_USER) ? "EXTERNALUSER" : selMemberUserLevel.equals(User.USER_LEVEL_INTERNAL_USER) ? "USER" : selMemberUserLevel.equals(User.USER_LEVEL_AMINISTRATOR) ? "ADMINISTRATOR" : "OPERATOR");
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
					}
				}
			}

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
			User user = SmartUtil.getCurrentUser();
			String userId = (String)requestBody.get("userId");
			if(userId.equals(""))
				return;
			getSwoManager().removeUser(user.getId(), userId);
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
			if(!departmentId.equals("")) {
				swoDepartment = getSwoManager().getDepartment(userId, departmentId, IManager.LEVEL_ALL);
			} else {
				swoDepartment = new SwoDepartment();
				swoDepartment.setCompanyId(companyId);
				swoDepartment.setType("BASIC");
				swoDepartment.setDomainId("frm_dept_SYSTEM");
			}

			while (itr.hasNext()) {
				String fieldId = (String)itr.next();
				Object fieldValue = frmEditDepartment.get(fieldId);
				if(fieldValue instanceof String) {
					if(fieldId.equals("hdnParentId")) {
						hdnParentId = (String)frmEditDepartment.get("hdnParentId");
						swoDepartment.setParentId(hdnParentId);
					} else if(fieldId.equals("txtDepartmentName")) {
						txtDepartmentName = (String)frmEditDepartment.get("txtDepartmentName");
						swoDepartment.setName(txtDepartmentName);
					}
				}
			}

			getSwoManager().setDepartment(userId, swoDepartment, IManager.LEVEL_ALL);
			if(!departmentId.equals(""))
				getSwoManager().getDepartmentExtend(userId, departmentId, false);
		} catch(Exception e) {
			e.printStackTrace();			
		}
	}

	@Override
	public void removeDepartment(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try {
			User user = SmartUtil.getCurrentUser();
			String departmentId = (String)requestBody.get("departmentId");
			if(departmentId.equals(""))
				return;
			getSwoManager().removeDepartment(user.getId(), departmentId);
		} catch(Exception e) {
			e.printStackTrace();			
		}
	}
	
}
