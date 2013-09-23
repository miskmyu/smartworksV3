package net.smartworks.server.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.claros.commons.mail.models.ConnectionProfile;

import net.smartworks.model.RecordList;
import net.smartworks.model.approval.ApprovalLine;
import net.smartworks.model.calendar.CompanyEvent;
import net.smartworks.model.calendar.WorkHourPolicy;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.company.CompanyGeneral;
import net.smartworks.model.instance.info.RequestParams;
import net.smartworks.model.mail.EmailServer;
import net.smartworks.model.service.ExternalForm;
import net.smartworks.model.service.WSDLDetail;
import net.smartworks.model.service.WebService;
import net.smartworks.model.work.info.UsedWorkInfo;

public interface ISettingsService {

	public abstract CompanyGeneral getCompanyGeneral() throws Exception;

	public abstract void setCompanyGeneral(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract RecordList getWorkHourPolicyList(RequestParams params) throws Exception;
	
	public abstract WorkHourPolicy getWorkHourPolicyById(String id) throws Exception;
	
	public abstract void setWorkHourPolicy(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void removeWorkHourPolicy(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract RecordList getCompanyEventList(RequestParams params) throws Exception;
	
	public abstract CompanyEvent getCompanyEventById(String id) throws Exception;
	
	public abstract void setCompanyEvent(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void removeCompanyEvent(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract RecordList getApprovalLineList(RequestParams params) throws Exception;
	
	public abstract ApprovalLine getApprovalLineById(String id) throws Exception;
	
	public abstract void setApprovalLine(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void removeApprovalLine(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract RecordList getWebServiceList(RequestParams params) throws Exception;
	
	public abstract WebService getWebServiceById(String id) throws Exception;
	
	public abstract void setWebService(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void removeWebService(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract WSDLDetail getWsdlDetailFromUri(String wsdlUri) throws Exception;

	public abstract RecordList getExternalFormList(RequestParams params) throws Exception;
	
	public abstract ExternalForm getExternalFormById(String id) throws Exception;
	
	public abstract void setExternalForm(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void removeExternalForm(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void setMember(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void removeMember(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void setDepartment(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void removeDepartment(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;

	public abstract void checkIdDuplication(HttpServletRequest request) throws Exception;
	
	public abstract RecordList getEmailServerList(RequestParams params) throws Exception;
	
	public abstract EmailServer getEmailServerById(String id) throws Exception;
	
	public abstract void setEmailServer(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void removeEmailServer(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract EmailServer[] getEmailServers() throws Exception;
	
	public abstract ConnectionProfile[] getMailConnectionProfiles() throws Exception;
	
	public abstract void addAdjunctMember(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract UsedWorkInfo[] getUsedWorkListByCommunityId(String communityId) throws Exception;
	
	public abstract UserInfo getHeadByUserId(String userId) throws Exception;
	
	public abstract void executeRetireMember(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public abstract void executeWorkTransfer(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
}