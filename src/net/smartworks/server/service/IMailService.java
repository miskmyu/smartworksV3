package net.smartworks.server.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.smartworks.model.instance.MailInstance;
import net.smartworks.model.instance.info.InstanceInfoList;
import net.smartworks.model.instance.info.RequestParams;
import net.smartworks.model.mail.MailFolder;

public interface IMailService {

	public InstanceInfoList getMailInstanceList(String folderId, RequestParams params) throws Exception;

	public MailInstance getMailInstanceById(String folderId, String msgId) throws Exception;

	public MailFolder[] getMailFoldersById(String folderId) throws Exception;
	
	public MailFolder getMailFolderById(String folderId) throws Exception;	

	public void sendMail(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public void saveMailAsDraft(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
	public void moveMails(Map<String, Object> requestBody, HttpServletRequest request) throws Exception;
	
}
