package net.smartworks.server.service.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import net.smartworks.model.community.User;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.MailInstance;
import net.smartworks.model.instance.SortingField;
import net.smartworks.model.instance.info.BoardInstanceInfo;
import net.smartworks.model.instance.info.EventInstanceInfo;
import net.smartworks.model.instance.info.InstanceInfoList;
import net.smartworks.model.instance.info.MailInstanceInfo;
import net.smartworks.model.instance.info.RequestParams;
import net.smartworks.model.mail.MailAttachment;
import net.smartworks.model.mail.MailFolder;
import net.smartworks.server.service.IMailService;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartTest;
import net.smartworks.util.SmartUtil;
import org.claros.commons.auth.MailAuth;
import org.claros.commons.auth.exception.LoginInvalidException;
import org.claros.commons.auth.models.AuthProfile;
import org.claros.commons.cache.Cache;
import org.claros.commons.cache.CacheManager;
import org.claros.commons.configuration.Paths;
import org.claros.commons.configuration.PropertyFile;
import org.claros.commons.exception.NoPermissionException;
import org.claros.commons.mail.comparator.ComparatorDate;
import org.claros.commons.mail.comparator.ComparatorFrom;
import org.claros.commons.mail.comparator.ComparatorSize;
import org.claros.commons.mail.comparator.ComparatorSubject;
import org.claros.commons.mail.comparator.ComparatorTo;
import org.claros.commons.mail.exception.ProtocolNotAvailableException;
import org.claros.commons.mail.exception.ServerDownException;
import org.claros.commons.mail.models.ByteArrayDataSource;
import org.claros.commons.mail.models.ConnectionMetaHandler;
import org.claros.commons.mail.models.ConnectionProfile;
import org.claros.commons.mail.models.Email;
import org.claros.commons.mail.models.EmailHeader;
import org.claros.commons.mail.models.EmailPart;
import org.claros.commons.mail.models.EmailPriority;
import org.claros.commons.mail.protocols.Protocol;
import org.claros.commons.mail.protocols.ProtocolFactory;
import org.claros.commons.mail.protocols.Smtp;
import org.claros.commons.mail.utility.Constants;
import org.claros.commons.mail.utility.Utility;
import org.claros.commons.utility.MD5;
import org.claros.intouch.common.services.BaseService;
import org.claros.intouch.contacts.controllers.ContactsController;
import org.claros.intouch.preferences.controllers.UserPrefsController;
import org.claros.intouch.webmail.controllers.FolderController;
import org.claros.intouch.webmail.controllers.InboxController;
import org.claros.intouch.webmail.controllers.MailController;
import org.claros.intouch.webmail.factory.FolderControllerFactory;
import org.claros.intouch.webmail.factory.InboxControllerFactory;
import org.claros.intouch.webmail.factory.MailControllerFactory;
import org.claros.intouch.webmail.models.FolderDbObject;
import org.claros.intouch.webmail.models.FolderDbObjectWrapper;
import org.claros.intouch.webmail.models.MsgDbObject;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class MailServiceImpl extends BaseService implements IMailService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public MailFolder[] getMailFoldersById(String folderId) throws Exception {
		
		try{
			MailFolder[] mailFolders = null;
	
		    ConnectionMetaHandler handler = getConnectionMetaHandler();
		    if(handler == null) return null;
		    
			ConnectionProfile profile = getConnectionProfile();
			AuthProfile auth = getAuthProfile();
			
			String sFolder = folderId;
			if (sFolder == null || sFolder.equals("")) {
				sFolder = Constants.FOLDER_INBOX(profile);
			}
	
			FolderControllerFactory foldFact = new FolderControllerFactory(auth, profile, handler);
			FolderController folderCont = foldFact.getFolderController();
			if (profile.getProtocol().equals(Constants.POP3)) {
				if (sFolder == null || sFolder.equals("INBOX")) {
					FolderDbObject foldObj = folderCont.getInboxFolder();
					sFolder = foldObj.getId().toString();
				}
			}
			
			List folders = folderCont.getFolders();
			if (folders != null) {
				FolderDbObjectWrapper tmp = null;
				mailFolders = new MailFolder[folders.size()];
				for(int i=0; i<mailFolders.length; i++){
					tmp = (FolderDbObjectWrapper)folders.get(i);
					mailFolders[i] = new MailFolder(tmp.getId().toString(), tmp.getFolderName(), tmp.getFolderType());
					mailFolders[i].setUnreadItemCount(tmp.getUnreadItemCount().intValue());
				}
			}
			return mailFolders;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	@Override
	public InstanceInfoList getMailInstanceList(String folderId, RequestParams params) throws Exception {

		try{
			InstanceInfoList instanceInfoList = new InstanceInfoList();
	
		    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		    HttpServletRequest request = attr.getRequest();

		    ConnectionMetaHandler handler = getConnectionMetaHandler();
			if(handler == null)  return null;
			
			ConnectionProfile profile = getConnectionProfile();
			AuthProfile auth = getAuthProfile();
			
			
			// get folder and set it into sesssion
			String sFolder = MailFolder.getFolderSIdById(folderId);
	
			// prepare variables
			List headers = null;
	
			FolderControllerFactory foldFact = null;
			FolderController folderCont = null;
			String currFolder = Constants.FOLDER_INBOX(profile);
	
			try {
				if (auth == null) {
					throw new NoPermissionException();
				}
				// if folder name is empty or it is inbox then do mail filtering. It is done by inbox controller
				if (sFolder == null || sFolder.equals("") || sFolder.equals(currFolder)) {
					try {
						InboxControllerFactory inFact = new InboxControllerFactory(auth, profile, handler);
						InboxController inCont = inFact.getInboxController();
						handler = inCont.checkEmail();
						request.getSession().setAttribute("handler", handler);
						foldFact = new FolderControllerFactory(auth, profile, handler);
						folderCont = foldFact.getFolderController();
					} catch (Exception e) {
					}
	
					// get the id(pop3) or the mail folder name (imap)
					if (profile.getProtocol().equals(Constants.POP3)) {
						currFolder = folderCont.getInboxFolder().getId().toString();
					} else {
						currFolder = folderCont.getInboxFolder().getFolderName();
					}
				} else {
					currFolder = sFolder;
					handler = (ConnectionMetaHandler)request.getSession().getAttribute("handler");
					foldFact = new FolderControllerFactory(auth, profile, handler);
					folderCont = foldFact.getFolderController();
				}
				request.getSession().setAttribute("folder", currFolder);
	
				// get info about the current folder
				FolderDbObject myFolder = folderCont.getFolder(currFolder);
	
				// time to fetch the headers
				if (profile.getProtocol().equals(Constants.POP3) && myFolder.getFolderType().equals(org.claros.intouch.common.utility.Constants.FOLDER_TYPE_INBOX)) {
					currFolder = Constants.FOLDER_INBOX(null);
				}
	
				// get and set sort parameters
				String mailSort = params.getSortingField().getFieldId();
				if(null==mailSort) mailSort = "date";
				String mailSortDirection = params.getSortingField().isAscending() ? "asc" : "desc";
				request.getSession().setAttribute("mailSort", mailSort);
				request.getSession().setAttribute("mailSortDirection", mailSortDirection);
	
				// first check to see if the server supports server side sorting or not. 
				// if it supports server side sorting it is a big performance enhancement.
				ArrayList sortedHeaders = null;
				boolean supportsServerSorting = false;
				try {
					// the user agrees on the performance enhancement imap sort offers. So go on.
					// let's give a try if the user supports it or not.
					ProtocolFactory pFact = new ProtocolFactory(profile, auth, handler);
					Protocol protocol = pFact.getProtocol(currFolder);
					
					sortedHeaders = protocol.getHeadersSortedList(mailSort, mailSortDirection);
					
					// profile has the boolean variable of it supports server side sorting or not!!!
					// so set it to the session for future references. 
					profile = protocol.getProfile();
					request.getSession().setAttribute("profile", profile);
					
					supportsServerSorting = true;
				} catch (ProtocolNotAvailableException p) {
					sortedHeaders = null;
					supportsServerSorting = false;
				}
				
				// it is pop3 mode or the imap server doesn't support server side sorting. 
				if (!supportsServerSorting) {
					headers = folderCont.getHeadersByFolder(currFolder);
				}
				// if server side sorting is supported, do not fetch the messages yet. they will be fetched
				// when paging variables are set below. 
	
				
				// get and set pageNo
				int pageNo = params.getCurrentPage();
				switch(params.getPagingAction()){
				case RequestParams.PAGING_ACTION_NEXT10:
					pageNo = (((pageNo - 1) / 10) * 10) + 11;
					break;
				case RequestParams.PAGING_ACTION_NEXTEND:
					pageNo = -1;
					break;
				case RequestParams.PAGING_ACTION_PREV10:
					pageNo = ((pageNo - 1) / 10) * 10;
					break;
				case RequestParams.PAGING_ACTION_PREVEND:
					pageNo = 1;
					break;
				}
				
				boolean isAscending = false;
				if (mailSortDirection != null && mailSortDirection.equals("asc")) {
					isAscending = true;
				}
				
				// organize em
				String fromSort = "";
				String dateSort = "";
				String sizeSort = "";
				String subjectSort = "";
				
				if (mailSort == null || mailSort.equals("date")) {
					if (isAscending) dateSort = "asc"; else dateSort = "desc";
				} else if (mailSort.equals("from")) {
					if (isAscending) fromSort = "asc"; else fromSort = "desc";
				} else if (mailSort.equals("subject")) {
					if (isAscending) subjectSort = "asc"; else subjectSort = "desc";
				} else if (mailSort.equals("to")) {
					if (isAscending) fromSort = "asc"; else fromSort = "desc";
				} else if (mailSort.equals("size")) {
					if (isAscending) sizeSort = "asc"; else sizeSort = "desc";
				}
				
				if (myFolder.getFolderType().equals(org.claros.intouch.common.utility.Constants.FOLDER_TYPE_SENT)) {
					  if (mailSort != null && mailSort.equals("from")) {
						  mailSort = "to";
					  }
				} else {
					  if (mailSort != null && mailSort.equals("to")) {
						  mailSort = "from";
					  }
				}
				
				if (!supportsServerSorting) {
					// sort the headers
					Locale loc = new Locale(SmartUtil.getCurrentUser().getLocale());
					if (mailSort == null || mailSort.equals("date")) {
						Collections.sort(headers, new ComparatorDate(isAscending));
					} else if (mailSort.equals("from")) {
						Collections.sort(headers, new ComparatorFrom(isAscending, loc));
					} else if (mailSort.equals("subject")) {
						Collections.sort(headers, new ComparatorSubject(isAscending, loc));
					} else if (mailSort.equals("to")) {
						Collections.sort(headers, new ComparatorTo(isAscending, loc));
					} else if (mailSort.equals("size")) {
						Collections.sort(headers, new ComparatorSize(isAscending));
					}
				}
				
				// organize and generate XML from the headers.
				if (headers != null || supportsServerSorting) {
					EmailHeader tmp = null;
					int pageSize = params.getPageSize();
					
					// determine the message count. the method varies if server side or client side 
					// sorting is used. 
					int messageCount = -1;
					if (!supportsServerSorting) {
						messageCount = headers.size();
					} else {
						messageCount = sortedHeaders.size();
					}
					int pageCount = messageCount/pageSize;
					if((messageCount%pageSize)>0) pageCount++;
					if(pageNo > pageCount || pageNo == -1) pageNo = pageCount;
					int startIdx = (pageNo-1)*pageSize;
					if (startIdx < 0) startIdx = 0;
					int endIdx = startIdx+pageSize;
					if(endIdx > messageCount) endIdx = messageCount;
					
					instanceInfoList.setPageSize(pageSize);
					instanceInfoList.setCurrentPage(pageNo);
					instanceInfoList.setTotalPages(pageCount);
					instanceInfoList.setSortedField(new SortingField(mailSort, isAscending));
					
					MailInstanceInfo[] instanceInfos = null;
					// If server side sorting is supported, it is time to fetch the message headers. 
					// not all of the headers are fetched.
					if (supportsServerSorting) {
						int msgs[] = new int[endIdx - startIdx];
						int counter = 0;
						for (int y=startIdx;y<endIdx;y++) {
							msgs[counter] = ((Integer)sortedHeaders.get(y)).intValue();
							counter++;
						}
						headers = folderCont.getHeadersByFolder(currFolder, msgs);
						
						instanceInfos = new MailInstanceInfo[headers.size()];
						// we only have the headers to be displayed in the headers arraylist. 
						for (int i=0;i<headers.size();i++) {
							tmp = (EmailHeader)headers.get(i);
							InternetAddress from = (InternetAddress)tmp.getFrom()[0];
							MailInstanceInfo mailInstance = new MailInstanceInfo(Integer.toString(tmp.getMessageId()),
									tmp.getSubject(), new UserInfo(from.getAddress(), from.getPersonal()), new LocalDate(tmp.getDate().getTime()-TimeZone.getDefault().getRawOffset()));						
							mailInstance.setSize(tmp.getSize());
							mailInstance.setUnread(tmp.getUnread());
							mailInstance.setPriority(tmp.getPriority());
							mailInstance.setMultipart(tmp.isMultipart());
							instanceInfos[i] = mailInstance;
						}
					} else {
	
						instanceInfos = new MailInstanceInfo[endIdx-startIdx];
						// with the client side sorting method the headers array has all the message headers
						// so with a for statement display them. 
						for (int i=startIdx;i<endIdx;i++) {
							tmp = (EmailHeader)headers.get(i);
							InternetAddress from = (InternetAddress)tmp.getFrom()[0];
							MailInstanceInfo mailInstance = new MailInstanceInfo(Integer.toString(tmp.getMessageId()),
									tmp.getSubject(), new UserInfo(from.getAddress(), from.getPersonal()), new LocalDate(tmp.getDate().getTime()-TimeZone.getDefault().getRawOffset()));						
							mailInstance.setSize(tmp.getSize());
							mailInstance.setUnread(tmp.getUnread());
							mailInstance.setPriority(tmp.getPriority());
							mailInstance.setMultipart(tmp.isMultipart());
							instanceInfos[i-startIdx] = mailInstance;
							
						}
					}
					instanceInfoList.setInstanceDatas(instanceInfos);
				}
			} catch (Exception e) {
			}
			
			return instanceInfoList;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}
	
	private ConnectionMetaHandler getConnectionMetaHandler() throws Exception{
		
	    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    HttpServletRequest request = attr.getRequest();
	    
	    ConnectionMetaHandler handler = (ConnectionMetaHandler)request.getSession().getAttribute("handler");
	    ConnectionProfile profile = (ConnectionProfile)request.getSession().getAttribute("profile");
	    AuthProfile auth = (AuthProfile)request.getSession().getAttribute("auth");
	    
	    if(handler != null && profile != null && auth != null)
	    	return handler;
	    
	    ConnectionProfile[] profiles = SmartTest.getMailConnectionProfiles();
	    if(profiles == null || profiles.length == 0)
	    	return null;
	    
	    profile = profiles[0];
	    
		String username = SmartUtil.getCurrentUser().getId();
		String password = SmartUtil.getCurrentUser().getPassword();

		if (username != null && password != null) {
			auth = new AuthProfile();
			auth.setUsername(username);
			auth.setPassword(password);

			try {
				handler = MailAuth.authenticate(profile, auth, handler);
				if (handler != null) {
					
					request.getSession().setAttribute("handler", handler);
					request.getSession().setAttribute("auth", auth);
					request.getSession().setAttribute("profile", profile);

					// create default mailboxes if not exists
					FolderControllerFactory factory = new FolderControllerFactory(auth, profile, handler);
					FolderController foldCont = factory.getFolderController();
					foldCont.createDefaultFolders();
				}
			} catch (LoginInvalidException e) {
				return null;
			} catch (ServerDownException e) {
				return null;
			}
		} else {
			throw new LoginInvalidException();
		}
		return handler;
	}
	
	private ConnectionProfile getConnectionProfile() throws Exception{
		
	    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    HttpServletRequest request = attr.getRequest();
	    
	    ConnectionMetaHandler handler = (ConnectionMetaHandler)request.getSession().getAttribute("handler");
	    ConnectionProfile profile = (ConnectionProfile)request.getSession().getAttribute("profile");
	    AuthProfile auth = (AuthProfile)request.getSession().getAttribute("auth");
	    
	    if(handler != null && profile != null && auth != null)
	    	return profile;
	    
	    ConnectionProfile[] profiles = SmartTest.getMailConnectionProfiles();
	    if(profiles == null || profiles.length == 0)
	    	return null;
	    
	    profile = profiles[0];
	    
		String username = SmartUtil.getCurrentUser().getId();
		String password = SmartUtil.getCurrentUser().getPassword();

		if (username != null && password != null) {
			auth = new AuthProfile();
			auth.setUsername(username);
			auth.setPassword(password);

			try {
				handler = MailAuth.authenticate(profile, auth, handler);
				if (handler != null) {
					
					request.getSession().setAttribute("handler", handler);
					request.getSession().setAttribute("auth", auth);
					request.getSession().setAttribute("profile", profile);

					// create default mailboxes if not exists
					FolderControllerFactory factory = new FolderControllerFactory(auth, profile, handler);
					FolderController foldCont = factory.getFolderController();
					foldCont.createDefaultFolders();
				}
			} catch (LoginInvalidException e) {
				return null;
			} catch (ServerDownException e) {
				return null;
			}
		} else {
			throw new LoginInvalidException();
		}
		return profile;
	}
	
	private AuthProfile getAuthProfile() throws Exception{
		
	    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    HttpServletRequest request = attr.getRequest();
	    
	    ConnectionMetaHandler handler = (ConnectionMetaHandler)request.getSession().getAttribute("handler");
	    ConnectionProfile profile = (ConnectionProfile)request.getSession().getAttribute("profile");
	    AuthProfile auth = (AuthProfile)request.getSession().getAttribute("auth");
	    
	    if(handler != null && profile != null && auth != null)
	    	return auth;
	    
	    ConnectionProfile[] profiles = SmartTest.getMailConnectionProfiles();
	    if(profiles == null || profiles.length == 0)
	    	return null;
	    
	    profile = profiles[0];
	    
		String username = SmartUtil.getCurrentUser().getId();
		String password = SmartUtil.getCurrentUser().getPassword();

		if (username != null && password != null) {
			auth = new AuthProfile();
			auth.setUsername(username);
			auth.setPassword(password);

			try {
				handler = MailAuth.authenticate(profile, auth, handler);
				if (handler != null) {
					
					request.getSession().setAttribute("handler", handler);
					request.getSession().setAttribute("auth", auth);
					request.getSession().setAttribute("profile", profile);

					// create default mailboxes if not exists
					FolderControllerFactory factory = new FolderControllerFactory(auth, profile, handler);
					FolderController foldCont = factory.getFolderController();
					foldCont.createDefaultFolders();
				}
			} catch (LoginInvalidException e) {
				return null;
			} catch (ServerDownException e) {
				return null;
			}
		} else {
			throw new LoginInvalidException();
		}
		return auth;
	}
	
	private int findHtmlBody(ArrayList parts) {
		for (int i=0;i<parts.size();i++) {
			EmailPart body = (EmailPart)parts.get(i);
			String cType = body.getContentType();
			if (cType.toLowerCase().startsWith("text/html")) {
				return i;
			}
		}
		return -1;
	}

	private int findTextBody(ArrayList parts) {
		for (int i=0;i<parts.size();i++) {
			EmailPart body = (EmailPart)parts.get(i);
			String cType = body.getContentType();
			if (cType.toLowerCase().startsWith("text/plain")) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public MailInstance getMailInstanceById(String folderId, String msgId) throws Exception {

		try{
			MailInstance instance = null;
			
			
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		    HttpServletRequest request = attr.getRequest();
	
	
			AuthProfile auth = getAuthProfile(request);
	
			ConnectionMetaHandler handler = (ConnectionMetaHandler)request.getSession().getAttribute("handler");
			ConnectionProfile profile = (ConnectionProfile)request.getSession().getAttribute("profile");
	
			MailControllerFactory factory = new MailControllerFactory(auth, profile, handler, folderId);
			MailController mailCont = factory.getMailController();
			
			try {
				Email email = mailCont.getEmailById(new Long(msgId));
				request.getSession().setAttribute("email", email);
	
				String format = "html";
				int i = -1;
				if (format == null || format.equals("html")) {
					i = findHtmlBody(email.getParts());
				}
				if (i == -1) {
					i = findTextBody(email.getParts());
				}
	
				String from = org.claros.intouch.common.utility.Utility.htmlCheck(org.claros.commons.utility.Utility.updateTRChars(email.getBaseHeader().getFromShown()));
				String to = org.claros.intouch.common.utility.Utility.htmlCheck(org.claros.commons.utility.Utility.updateTRChars(email.getBaseHeader().getToShown()));
				String cc = org.claros.intouch.common.utility.Utility.htmlCheck(org.claros.commons.utility.Utility.updateTRChars(email.getBaseHeader().getCcShown()));
				String date = org.claros.intouch.common.utility.Utility.htmlCheck(email.getBaseHeader().getDateShown());
				String subject = org.claros.intouch.common.utility.Utility.htmlCheck(org.claros.commons.utility.Utility.updateTRChars(email.getBaseHeader().getSubject()));
				Boolean sendReceiptNotification = email.getBaseHeader().getRequestReceiptNotification();
				String sendReceiptNotificationEmail = email.getBaseHeader().getReceiptNotificationEmail();
				String notificationEmail = "";
			
				if(sendReceiptNotification != null && sendReceiptNotification.booleanValue() && sendReceiptNotificationEmail !=null && email.getBaseHeader().getUnread().booleanValue()){
					notificationEmail = org.claros.commons.utility.Utility.convertTRCharsToHtmlSafe(sendReceiptNotificationEmail);
				}
				
				if (profile.getProtocol().equals(Constants.POP3)) {
					mailCont.markAsRead(new Long(msgId));
				}
				
				if (from == null || from.equals("")) {
					from = getText(request, "unknown.sender");
				}
				if (subject == null || subject.equals("")) {
					subject = getText(request, "no.subject");
				}
				
				InternetAddress addrFrom = (InternetAddress)email.getBaseHeader().getFrom()[0];
				InternetAddress[] addrTo = (InternetAddress[])email.getBaseHeader().getTo();			
				InternetAddress[] addrCc = (InternetAddress[])email.getBaseHeader().getCc();
				InternetAddress[] addrBcc = (InternetAddress[])email.getBaseHeader().getBcc();
				User sender = new User(addrFrom.getAddress(), addrFrom.getPersonal());
				User[] receivers = null;
				if(addrTo != null){
					receivers = new User[addrTo.length];
					for(int k=0; k<addrTo.length; k++)
						receivers[k] = new User(addrTo[k].getAddress(), addrTo[k].getPersonal());
				}
				User[] ccReceivers = null;
				if(addrCc != null){
					ccReceivers = new User[addrCc.length];
					for(int k=0; k<addrCc.length; k++)
						ccReceivers[k] = new User(addrCc[k].getAddress(), addrCc[k].getPersonal());
				}
				User[] bccReceivers = null;
				if(addrBcc != null){
					bccReceivers = new User[addrBcc.length];
					for(int k=0; addrBcc!=null && k<addrBcc.length; k++)
						bccReceivers[k] = new User(addrBcc[k].getAddress(), addrBcc[k].getPersonal());
				}
				
				instance = new MailInstance(msgId, subject, sender, new LocalDate(email.getBaseHeader().getDate().getTime()));
				instance.setCreatedDate(new LocalDate(email.getBaseHeader().getDate().getTime()));
				instance.setReceivers(receivers);
				instance.setCcReceivers(ccReceivers);
				instance.setBcccReceivers(bccReceivers);
				
				MailAttachment[] attachments = null;
				int count = 0;
				// parts begin
				List parts = email.getParts();
				if (parts != null) {
					if (parts.size() > 1 || i == -1) {
						EmailPart tmp = null;
						String mime = null;
						attachments = new MailAttachment[parts.size()];
						for (int j=0;j<parts.size();j++) {
							tmp = (EmailPart)parts.get(j);
	
							mime = tmp.getContentType();
							mime = mime.toLowerCase(new Locale(SmartUtil.getCurrentUser().getLocale())).trim();
							if (mime.indexOf(";") > 0) {
								mime = mime.substring(0, mime.indexOf(";"));
							}
							if (mime.indexOf(" ") > 0) {
								mime = mime.substring(0, mime.indexOf(" "));
							}
							if(mime.equals(MailAttachment.MIME_TYPE_TEXT_PLAIN) || mime.equals(MailAttachment.MIME_TYPE_TEXT_HTML))
								continue;
							String fileName = org.claros.commons.utility.Utility.updateTRChars(tmp.getFilename());
							attachments[count] = new MailAttachment(Integer.toString(j), fileName, mime, tmp.getSize());
							attachments[count].setFileType(SmartUtil.getFileExtension(fileName));
							count++;
						}
					}
				}
				MailAttachment[] finalAttachments = null;
				if(count>0){
					finalAttachments = new MailAttachment[count]; 
					for(int j=0; j<count; j++){
						finalAttachments[j] = attachments[j];
					}
				}
				instance.setAttachments(finalAttachments);
				instance.setPartId(i);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return instance;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	@Override
	public MailFolder getMailFolderById(String folderId) throws Exception {
		try{
			MailFolder mailFolder = null;
	
		    ConnectionMetaHandler handler = getConnectionMetaHandler();
		    if(handler == null) return null;
		    
			ConnectionProfile profile = getConnectionProfile();
			AuthProfile auth = getAuthProfile();
			
			String sFolder = folderId;
			if (sFolder == null || sFolder.equals("")) {
				sFolder = Constants.FOLDER_INBOX(profile);
			}
	
			FolderControllerFactory foldFact = new FolderControllerFactory(auth, profile, handler);
			FolderController folderCont = foldFact.getFolderController();
			if (profile.getProtocol().equals(Constants.POP3)) {
				if (sFolder == null || sFolder.equals("INBOX")) {
					FolderDbObject foldObj = folderCont.getInboxFolder();
					sFolder = foldObj.getId().toString();
				}
			}
			
			FolderDbObjectWrapper fld = folderCont.getFolderById(folderId);
			mailFolder = new MailFolder(fld.getId().toString(), fld.getFolderName(), fld.getFolderType());
			mailFolder.setUnreadItemCount(fld.getUnreadItemCount().intValue());
			mailFolder.setTotalItemCount(fld.getTotalItemCount().intValue());
			return mailFolder;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	@Override
	public void sendMail(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try {
			Map<String, List<Map<String, String>>> from = (Map<String, List<Map<String, String>>>)requestBody.get("from");
			Map<String, Object> newMail = (HashMap<String, Object>)requestBody.get("frmNewMail");
			Map<String, List<Map<String, String>>> receivers = (HashMap<String, List<Map<String, String>>>)newMail.get("receivers");
			Map<String, List<Map<String, String>>> ccReceivers = (HashMap<String, List<Map<String, String>>>)newMail.get("ccReceivers");
			Map<String, List<Map<String, String>>> bccReceivers = (HashMap<String, List<Map<String, String>>>)newMail.get("bccReceivers");
			String subject = (String)newMail.get("subject");
			String body = (String)newMail.get("contents");
			String requestReceiptNotification = (String)newMail.get("requestReceiptNotification");
			String priority = (String)newMail.get("priority");
			String sensitivity = (String)newMail.get("sensitivity");
			
			AuthProfile auth = getAuthProfile();

//String saveSentContacts = UserPrefsController.getUserSetting(auth, "saveSentContacts");
//if (saveSentContacts == null) {
//	saveSentContacts = "yes";
//}
			
			// now create a new email object.
			Email email = new Email();
			EmailHeader header = new EmailHeader();
			
			Address adrs[] = Utility.stringListToAddressArray(from.get("users"));
			header.setFrom(adrs);
			
			Address tos[] = Utility.stringListToAddressArray(receivers.get("users"));
			header.setTo(tos);
//if (saveSentContacts != null && saveSentContacts.equals("yes")) {
//	saveContacts(auth, tos);
//}
			
			if (ccReceivers != null) {
				Address ccs[] = Utility.stringListToAddressArray(ccReceivers.get("users"));
				header.setCc(ccs);
//if (saveSentContacts != null && saveSentContacts.equals("yes")) {
//	saveContacts(auth, ccs);
//}
			}
			if (bccReceivers != null) {
				Address bccs[] = Utility.stringListToAddressArray(bccReceivers.get("users"));
				header.setBcc(bccs);
//if (saveSentContacts != null && saveSentContacts.equals("yes")) {
//	saveContacts(auth, bccs);
//}
			}
			header.setSubject(subject);
			header.setDate(new Date());

//String replyTo = UserPrefsController.getUserSetting(auth, "replyTo");
//if (replyTo != null && replyTo.trim().length() != 0) {
//	header.setReplyTo(new Address[] {new InternetAddress(replyTo)});
//}
			
			if (requestReceiptNotification!=null && requestReceiptNotification.equals("1")) {
				header.setRequestReceiptNotification(Boolean.valueOf(true));
			}
			
			if (priority!=null && priority.equals("on")) {
				header.setPriority((short)EmailPriority.HIGH);
			}

			if (sensitivity!=null) {
				header.setSensitivity(Short.valueOf(sensitivity).shortValue());
			}
			
			email.setBaseHeader(header);

			ArrayList parts = new ArrayList();
			EmailPart bodyPart = new EmailPart();
			bodyPart.setContentType("text/html; charset=UTF-8");
			/*
			HtmlCleaner cleaner = new HtmlCleaner(body);
			cleaner.clean(false,false);
			*/
			
//String appendSignature = PropertyFile.getConfiguration("/config/config.xml").getString("common-params.append-signature");
//String sign = "";
//if (appendSignature != null && appendSignature.toLowerCase().equals("true")) {
//	Cache cache = CacheManager.getContent("server.signature");
//	if (cache == null) {
//		BufferedInputStream is = new BufferedInputStream(new FileInputStream(Paths.getCfgFolder() + "/server_signature.txt"));
//		int byte_;
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		while ((byte_ = is.read ()) != -1) {
//			bos.write (byte_);
//		}
//		is.close();
//		sign = new String(bos.toByteArray());
//		bos.close();
//		
//		cache = new Cache();
//		CacheManager.putContent("server.signature", sign, Integer.MAX_VALUE);
//	} else {
//		sign = (String)cache.getValue();
//	}
//}
//body = body + sign;
			bodyPart.setContent(body);
			parts.add(0, bodyPart);
			
			// attach some files...
//ArrayList attachments = (ArrayList)request.getSession().getAttribute("attachments");
//if (attachments != null) {
//	List newLst = new ArrayList();
//	EmailPart tmp = null;
//	for (int i=0;i<attachments.size();i++) {
//		try {
//			tmp = (EmailPart)attachments.get(i);
//			String disp = tmp.getDisposition();
//			File f = new File(disp);
//			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
//			byte data[] = new byte[(int)f.length() + 2];
//			bis.read(data);
//			bis.close();
//
//			MimeBodyPart bp = new MimeBodyPart();
//			DataSource ds = new ByteArrayDataSource(data, tmp.getContentType(), tmp.getFilename());
//			bp.setDataHandler(new DataHandler(ds));
//			bp.setDisposition("attachment; filename=\"" + tmp.getFilename() + "\"");
//			tmp.setDisposition(bp.getDisposition());
//			bp.setFileName(tmp.getFilename());
//			tmp.setDataSource(ds);
//			tmp.setContent(bp.getContent());
//			newLst.add(tmp);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	parts.addAll(newLst);
//}
			email.setParts(parts);
			
			// it is time to send the email object message
			Smtp smtp = new Smtp(getConnectionProfile(request), getAuthProfile(request));
			HashMap sendRes = smtp.send(email, false);
			MimeMessage msg = (MimeMessage)sendRes.get("msg");
			
			// if we fail to send the message to any of the recepients
			// we should make a report about it to the user. 
			Address[] sent = (Address[])sendRes.get("sent");
//			Address[] fail = (Address[])sendRes.get("fail");
//			Address[] invalid = (Address[])sendRes.get("invalid");
			
//if (sent == null || sent.length == 0) {
//} else {
//	// if save to sent items enabled, save the sent mail.
//	String saveEnabled = UserPrefsController.getUserSetting(auth, "saveSent");
//	if (saveEnabled == null) {
//		saveEnabled = "yes";
//	}
//	if (saveEnabled == null || saveEnabled.equals("yes")) {
//		saveSentMail(auth, msg, request);
//	}
//}
		} catch (Exception e) {
		}
	}

	private void saveContacts(AuthProfile auth, Address[] adrs) {
		try {
			if (adrs != null) {
				InternetAddress adr = null;
				for (int i=0;i<adrs.length;i++) {
					adr = (InternetAddress)adrs[i];
					ContactsController.saveSenderFromAddr(auth, adr);
				}
			}
		} catch (Exception e) {
		}
		
	}

	/**
	 * 
	 * @param auth
	 * @param msg
	 * @param request
	 * @throws Exception
	 */
	private void saveSentMail(AuthProfile auth, MimeMessage msg, HttpServletRequest request) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		msg.writeTo(bos);
		byte bMsg[] = bos.toByteArray();
					
		// serialize the message byte array
		ObjectOutputStream os = new ObjectOutputStream(bos);
		os.writeObject(bMsg);

		// create an email db item
		MsgDbObject item = new MsgDbObject();
		item.setEmail(bMsg);
		String md5Header = new String(MD5.getHashString(bMsg)).toUpperCase(new Locale("en", "US"));

		ConnectionMetaHandler handler = getConnectionHandler(request);
		ConnectionProfile profile = getConnectionProfile(request);

		FolderControllerFactory factory = new FolderControllerFactory(auth, profile, handler);
		FolderController foldCont = factory.getFolderController();
		FolderDbObject fItem = foldCont.getSentItems();

		item.setUniqueId(md5Header);
		item.setFolderId(fItem.getId());
		item.setUnread(new Boolean(false));
		item.setUsername(auth.getUsername());
		item.setMsgSize(new Long(bMsg.length));

		// save the email db item.
		MailControllerFactory mailFact = new MailControllerFactory(auth, profile, handler, fItem.getFolderName());
		MailController mailCont = mailFact.getMailController();
		mailCont.appendEmail(item);
	}

}