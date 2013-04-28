package net.smartworks.server.service.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
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
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.smartworks.model.community.User;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.MailInstance;
import net.smartworks.model.instance.SortingField;
import net.smartworks.model.instance.info.InstanceInfoList;
import net.smartworks.model.instance.info.MailInstanceInfo;
import net.smartworks.model.instance.info.RequestParams;
import net.smartworks.model.mail.EmailServer;
import net.smartworks.model.mail.MailAccount;
import net.smartworks.model.mail.MailAttachment;
import net.smartworks.model.mail.MailFolder;
import net.smartworks.model.notice.Notice;
import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.model.Order;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.common.util.ServletUtil;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.mail.manager.IMailManager;
import net.smartworks.server.engine.mail.model.MailAccountCond;
import net.smartworks.server.engine.mail.model.MailContent;
import net.smartworks.server.engine.mail.model.MailContentCond;
import net.smartworks.server.engine.mail.model.MailServer;
import net.smartworks.server.engine.security.model.Login;
import net.smartworks.server.service.ICommunityService;
import net.smartworks.server.service.IMailService;
import net.smartworks.server.service.ISettingsService;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartMessage;
import net.smartworks.util.SmartTest;
import net.smartworks.util.SmartUtil;

import org.apache.commons.lang.StringEscapeUtils;
import org.claros.commons.auth.MailAuth;
import org.claros.commons.auth.exception.LoginInvalidException;
import org.claros.commons.auth.models.AuthProfile;
import org.claros.commons.mail.exception.ServerDownException;
import org.claros.commons.mail.models.ByteArrayDataSource;
import org.claros.commons.mail.models.ConnectionMetaHandler;
import org.claros.commons.mail.models.ConnectionProfile;
import org.claros.commons.mail.models.Email;
import org.claros.commons.mail.models.EmailHeader;
import org.claros.commons.mail.models.EmailPart;
import org.claros.commons.mail.models.EmailPriority;
import org.claros.commons.mail.parser.HTMLMessageParser;
import org.claros.commons.mail.parser.MessageParser;
import org.claros.commons.mail.protocols.Smtp;
import org.claros.commons.mail.utility.Constants;
import org.claros.commons.mail.utility.Utility;
import org.claros.commons.utility.MD5;
import org.claros.intouch.common.services.BaseService;
import org.claros.intouch.contacts.controllers.ContactsController;
import org.claros.intouch.webmail.controllers.FolderController;
import org.claros.intouch.webmail.controllers.InboxController;
import org.claros.intouch.webmail.controllers.MailController;
import org.claros.intouch.webmail.factory.FolderControllerFactory;
import org.claros.intouch.webmail.factory.InboxControllerFactory;
import org.claros.intouch.webmail.factory.MailControllerFactory;
import org.claros.intouch.webmail.models.FolderDbObject;
import org.claros.intouch.webmail.models.FolderDbObjectWrapper;
import org.claros.intouch.webmail.models.MsgDbObject;
import org.htmlcleaner.HtmlCleaner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Service
public class MailServiceImpl extends BaseService implements IMailService {

	@Autowired
	private ISettingsService settingsService;
	@Autowired
	private ICommunityService communityService;

	private static final long serialVersionUID = 1L;

	private static IMailManager getMailManager() {
		return SwManagerFactory.getInstance().getMailManager();
	}

	private ConnectionMetaHandler getConnectionMetaHandler() throws Exception{
		
	    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    HttpServletRequest request = attr.getRequest();

	    User currentUser = SmartUtil.getCurrentUser();
	    
	    ConnectionMetaHandler handler = (ConnectionMetaHandler)request.getSession().getAttribute("handler");
	    ConnectionProfile profile = (ConnectionProfile)request.getSession().getAttribute("profile");
	    AuthProfile auth = (AuthProfile)request.getSession().getAttribute("auth");
	    
	    if(!currentUser.isUseMail() || SmartUtil.isBlankObject(currentUser.getMailAccounts()))
	    	return null;
	    
	    MailAccount mailAccount = currentUser.getMailAccounts()[0];
    
	    if(handler != null && profile != null && auth != null &&  auth.getUsername().equals(mailAccount.getUserName()) && auth.getPassword().equals(mailAccount.getPassword()) )
	    	return handler;

	    ConnectionProfile[] profiles = settingsService.getMailConnectionProfiles();
	    if(SmartUtil.isBlankObject(profiles))
	    	return null;
	    profile = profiles[0];

	    MailAccount[] mailAccounts = communityService.getMyMailAccounts();
	    if(mailAccounts == null || mailAccounts.length == 0)
			throw new LoginInvalidException();

	    String emailId = mailAccounts[0].getEmailId();
		String username = mailAccounts[0].getUserName();
		String password = mailAccounts[0].getPassword();
		boolean isDeleteAfterFetched = (profile.isDeleteFetched()) ? true : mailAccounts[0].isDeleteAfterFetched();
		boolean isUseSignature = mailAccounts[0].isUseSignature();
		String signature = mailAccounts[0].getSignature();
		if (username != null && password != null) {
			auth = new AuthProfile();
			auth.setEmailId(emailId);
			auth.setUsername(username);
			auth.setPassword(password);
			auth.setDeleteAfterFetched(isDeleteAfterFetched);
			auth.setSignature(isUseSignature ? signature : "");
		}

		try {
			if(handler == null)
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
			e.printStackTrace();
			return null;
		} catch (ServerDownException e) {
			e.printStackTrace();
			return null;
		}
		return handler;
	}
	
	private ConnectionProfile getConnectionProfile() throws Exception{
		
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    HttpServletRequest request = attr.getRequest();

	    ConnectionMetaHandler handler = (ConnectionMetaHandler)request.getSession().getAttribute("handler");
	    ConnectionProfile profile = (ConnectionProfile)request.getSession().getAttribute("profile");
	    AuthProfile auth = (AuthProfile)request.getSession().getAttribute("auth");

	    User currentUser = SmartUtil.getCurrentUser();
	    if(!currentUser.isUseMail() || SmartUtil.isBlankObject(currentUser.getMailAccounts()))
	    	return null;
	    
	    MailAccount mailAccount = currentUser.getMailAccounts()[0];
	    if(handler != null && profile != null && auth != null &&  auth.getUsername().equals(mailAccount.getUserName()) && auth.getPassword().equals(mailAccount.getPassword()) )
	    	return profile;

	    if(profile == null) {
		    ConnectionProfile[] profiles = settingsService.getMailConnectionProfiles();
		    if(profiles == null || profiles.length == 0)
		    	return null;
		    profile = profiles[0];
	    }

	    if(auth == null) {
		    MailAccount[] mailAccounts = communityService.getMyMailAccounts();
		    if(mailAccounts == null || mailAccounts.length == 0)
				throw new LoginInvalidException();

		    String emailId = mailAccounts[0].getEmailId();
			String username = mailAccounts[0].getUserName();
			String password = mailAccounts[0].getPassword();
			boolean isDeleteAfterFetched = (profile.isDeleteFetched()) ? true : mailAccounts[0].isDeleteAfterFetched();
			boolean isUseSignature = mailAccounts[0].isUseSignature();
			String signature = mailAccounts[0].getSignature();
			if (username != null && password != null) {
				auth = new AuthProfile();
				auth.setEmailId(emailId);
				auth.setUsername(username);
				auth.setPassword(password);
				auth.setDeleteAfterFetched(isDeleteAfterFetched);
				auth.setSignature(isUseSignature ? signature : "");
			}
	    }

		try {
			if(handler == null)
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
		return profile;
	}
	
	private AuthProfile getAuthProfile() throws Exception{
		
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    HttpServletRequest request = attr.getRequest();

	    ConnectionMetaHandler handler = (ConnectionMetaHandler)request.getSession().getAttribute("handler");
	    ConnectionProfile profile = (ConnectionProfile)request.getSession().getAttribute("profile");
	    AuthProfile auth = (AuthProfile)request.getSession().getAttribute("auth");

	    User currentUser = SmartUtil.getCurrentUser();
	    if(!currentUser.isUseMail() || SmartUtil.isBlankObject(currentUser.getMailAccounts()))
	    	return null;
	    
	    MailAccount mailAccount = currentUser.getMailAccounts()[0];
	    if(handler != null && profile != null && auth != null &&  auth.getUsername().equals(mailAccount.getUserName()) && auth.getPassword().equals(mailAccount.getPassword()) )
	    	return auth;

	    if(profile == null) {
		    ConnectionProfile[] profiles = settingsService.getMailConnectionProfiles();
		    if(profiles == null || profiles.length == 0)
		    	return null;
		    profile = profiles[0];
	    }

	    if(auth == null) {
		    MailAccount[] mailAccounts = communityService.getMyMailAccounts();
		    if(mailAccounts == null || mailAccounts.length == 0)
				throw new LoginInvalidException();

		    String emailId = mailAccounts[0].getEmailId();
			String username = mailAccounts[0].getUserName();
			String password = mailAccounts[0].getPassword();
			boolean isDeleteAfterFetched = (profile.isDeleteFetched()) ? true : mailAccounts[0].isDeleteAfterFetched();
			boolean isUseSignature = mailAccounts[0].isUseSignature();
			String signature = mailAccounts[0].getSignature();
			if (username != null && password != null) {
				auth = new AuthProfile();
				auth.setEmailId(emailId);
				auth.setUsername(username);
				auth.setPassword(password);
				auth.setDeleteAfterFetched(isDeleteAfterFetched);
				auth.setSignature(isUseSignature ? signature : "");
			}
	    }

		try {
			if(handler == null)
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
	private void saveSentMail(AuthProfile auth, MimeMessage msg, EmailHeader header, HttpServletRequest request) throws Exception {
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
		item.setMessageId(msg.getHeader("Sent-Message-Id")[0]);
		System.out.println("SENT-MESSAGE-ID TO SAVE = " + item.getMessageId());
		item.setFolderId(fItem.getId());
		item.setUnread(new Boolean(false));
		item.setUsername(auth.getEmailId());
		item.setMsgSize(new Long(bMsg.length));

		item.setSender(header.getFromShown());
		item.setReceiver(header.getToShown());
		item.setCc(header.getCcShown());
		item.setBcc(header.getBccShown());
		item.setReplyTo(header.getReplyToShown());
		item.setMultipart(hasAttachment(msg));
		item.setSentDate(header.getDate());
		item.setPriority(new Integer(header.getPriority()));
		item.setSubject(header.getSubject());
		
		// save the email db item.
		MailControllerFactory mailFact = new MailControllerFactory(auth, profile, handler, fItem.getFolderName());
		MailController mailCont = mailFact.getMailController();
		mailCont.appendEmail(item, null);
	}
	
	private boolean hasAttachment(MimeMessage msg){
		if(msg==null) return false;
		MimeMessage mimeMsg = (MimeMessage)msg;
		Email email = null;
		try {
			email = MessageParser.parseMessage(mimeMsg);
		} catch (Exception e) {

		}
		if (email != null && email.getParts().size()>1) {
			List parts = email.getParts();
			for (int j=0;j<parts.size();j++) {
				EmailPart tmp = (EmailPart)parts.get(j);
				String mime = tmp.getContentType();
				mime = mime.toLowerCase(new Locale(SmartUtil.getCurrentUser().getLocale())).trim();
				if (mime.indexOf(";") > 0) {
					mime = mime.substring(0, mime.indexOf(";"));
				}
				if (mime.indexOf(" ") > 0) {
					mime = mime.substring(0, mime.indexOf(" "));
				}
				if(!mime.equals(MailAttachment.MIME_TYPE_TEXT_PLAIN) && !mime.equals(MailAttachment.MIME_TYPE_TEXT_HTML))
					return true;
			}
		}
		return false;
	}
	
	private void saveDraft(AuthProfile auth, MimeMessage msg, EmailHeader header, HttpServletRequest request) throws Exception {
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
		FolderDbObject fItem = foldCont.getDraftsFolder();

		item.setUniqueId(md5Header);
		item.setFolderId(fItem.getId());
		item.setUnread(new Boolean(false));
		item.setUsername(auth.getEmailId());
		item.setMsgSize(new Long(bMsg.length));

		item.setSender(header.getFromShown());
		item.setReceiver(header.getToShown());
		item.setCc(header.getCcShown());
		item.setBcc(header.getBccShown());
		item.setReplyTo(header.getReplyToShown());
		item.setMultipart(hasAttachment(msg));
		item.setSentDate(header.getDate());
		item.setPriority(new Integer(header.getPriority()));
		item.setSubject(header.getSubject());
		
		// save the email db item.
		MailControllerFactory mailFact = new MailControllerFactory(auth, profile, handler, fItem.getFolderName());
		MailController mailCont = mailFact.getMailController();
		mailCont.appendEmail(item, null);
	}

	@Override
	public MailFolder[] getMailFoldersById(String folderId) throws Exception {
		
		try{
			MailFolder[] mailFolders = null;
	
		    ConnectionMetaHandler handler = getConnectionMetaHandler();
		    if(handler == null) return null;
		    
			ConnectionProfile profile = getConnectionProfile();
			AuthProfile auth = getAuthProfile();
			
			FolderControllerFactory foldFact = new FolderControllerFactory(auth, profile, handler);
			FolderController folderCont = foldFact.getFolderController();
			
			List folders = folderCont.getFolders(folderId);
			if (folders != null) {
				FolderDbObjectWrapper tmp = null;
				mailFolders = new MailFolder[folders.size()];
				if(SmartUtil.isBlankObject(folderId)){
					int customCount = 0;
					for(int i=0; i<mailFolders.length; i++){
						tmp = (FolderDbObjectWrapper)folders.get(i);
						int index = 0;
						switch(tmp.getFolderType()){
						case MailFolder.TYPE_SYSTEM_INBOX:
							index = 0;
							break;
						case MailFolder.TYPE_SYSTEM_SENT:
							index = 1;
							break;
						case MailFolder.TYPE_SYSTEM_DRAFTS:
							index = 2;
							break;
						case MailFolder.TYPE_SYSTEM_TRASH:
							index = 3;
							break;
						case MailFolder.TYPE_SYSTEM_JUNK:
							index = 4;
							break;
						case MailFolder.TYPE_SYSTEM_BACKUP:
						case MailFolder.TYPE_USER:
						case MailFolder.TYPE_GROUP:
							index = 5 + customCount++;
						}
						mailFolders[index] = new MailFolder(tmp.getId().toString(), tmp.getParentId().toString(), tmp.getFolderName(), tmp.getFolderType());
						mailFolders[index].setUnreadItemCount(tmp.getUnreadItemCount().intValue());
						if(tmp.getParentId()>0){
							FolderDbObjectWrapper parent = folderCont.getFolderById(tmp.getParentId().toString());
							mailFolders[index].setParentName(parent.getFolderName());
						}
					}
				}else if((Integer.parseInt(folderId) == MailFolder.TYPE_SYSTEM_BACKUP) && folders.size()==2){
					for(int i=0; i<mailFolders.length; i++){
						tmp = (FolderDbObjectWrapper)folders.get(i);
						int index = 0;
						switch(tmp.getFolderType()){
						case MailFolder.TYPE_SYSTEM_B_INBOX:
							index = 0;
							break;
						case MailFolder.TYPE_SYSTEM_B_SENT:
							index = 1;
							break;
						}
						mailFolders[index] = new MailFolder(tmp.getId().toString(), tmp.getParentId().toString(), tmp.getFolderName(), tmp.getFolderType());
						mailFolders[index].setUnreadItemCount(tmp.getUnreadItemCount().intValue());
						if(tmp.getParentId()>0){
							FolderDbObjectWrapper parent = folderCont.getFolderById(tmp.getParentId().toString());
							mailFolders[index].setParentName(parent.getFolderName());
						}
					}
				}else{
					for(int i=0; i<mailFolders.length; i++){
						tmp = (FolderDbObjectWrapper)folders.get(i);
						mailFolders[i] = new MailFolder(tmp.getId().toString(), tmp.getParentId().toString(), tmp.getFolderName(), tmp.getFolderType());
						mailFolders[i].setUnreadItemCount(tmp.getUnreadItemCount().intValue());
						if(tmp.getParentId()>0){
							FolderDbObjectWrapper parent = folderCont.getFolderById(tmp.getParentId().toString());
							mailFolders[i].setParentName(parent.getFolderName());
						}
					}					
				}
			}
			return mailFolders;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getFolderIdByType(int folderType) throws Exception {
		
		try{
			MailFolder[] mailFolders = null;
	
		    ConnectionMetaHandler handler = getConnectionMetaHandler();
		    if(handler == null) return null;
		    
			ConnectionProfile profile = getConnectionProfile();
			AuthProfile auth = getAuthProfile();
				
			FolderControllerFactory foldFact = new FolderControllerFactory(auth, profile, handler);
			FolderController folderCont = foldFact.getFolderController();
			
			List folders = folderCont.getFolders();
			if (folders != null) {
				FolderDbObjectWrapper tmp = null;
				mailFolders = new MailFolder[folders.size()];
				for(int i=0; i<mailFolders.length; i++){
					tmp = (FolderDbObjectWrapper)folders.get(i);
					if(tmp.getFolderType() == folderType)
						return tmp.getId().toString();
				}
			}
			return null;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	int previousPageSize = 0;
	@Override
	public InstanceInfoList getMailInstanceList(String folderId, RequestParams params) throws Exception {

		try {
			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();

		    ConnectionMetaHandler handler = getConnectionMetaHandler();
			if(handler == null) return null;
			
			ConnectionProfile profile = getConnectionProfile();
			AuthProfile auth = getAuthProfile();

			InboxControllerFactory inFact = new InboxControllerFactory(auth, profile, handler);
			InboxController inCont = inFact.getInboxController();
			inCont.checkEmail();

			MailAccountCond mailAccountCond = new MailAccountCond();
			mailAccountCond.setUserId(userId);
			net.smartworks.server.engine.mail.model.MailAccount[] mailAccounts = getMailManager().getMailAccounts(userId, mailAccountCond, IManager.LEVEL_ALL);
			if(CommonUtil.isEmpty(mailAccounts))
				return null;

			net.smartworks.server.engine.mail.model.MailAccount mailAccount = mailAccounts[0];

			String username = null;
			if(mailAccount != null)
				username = mailAccount.getMailId() + "@" + mailAccount.getMailServerName();

			MailContentCond mailContentCond = new MailContentCond();
			mailContentCond.setUsername(username);
			mailContentCond.setFolderId(Long.parseLong(folderId));

			boolean unreadEmail = params.isUnreadEmail();
			int unread = -1;
			if(unreadEmail)
				unread = 1;
			mailContentCond.setUnread(unread);

			String searchKey = params.getSearchKey();
			mailContentCond.setSearchKey(CommonUtil.toNull(searchKey));

			long totalCount = getMailManager().getMailContentSize(userId, mailContentCond);

			SortingField sf = params.getSortingField();
			String columnName = "";
			boolean isAsc;

			if (sf != null) {
				columnName = CommonUtil.toDefault(sf.getFieldId(), MailContent.A_SENTDATE);
				isAsc = sf.isAscending();
			} else {
				columnName = MailContent.A_SENTDATE;
				isAsc = false;
			}
			SortingField sortingField = new SortingField();
			sortingField.setFieldId(columnName);
			sortingField.setAscending(isAsc);

			mailContentCond.setOrders(new Order[]{new Order(columnName, isAsc)});

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
				mailContentCond.setPageNo(currentPage-1);

			mailContentCond.setPageSize(pageSize);

			InstanceInfoList instanceInfoList = new InstanceInfoList();

			MailContent[] mailContents = getMailManager().getMailContents(userId, mailContentCond, IManager.LEVEL_LITE);

			List<MailInstanceInfo> mailInstanceInfoList = new ArrayList<MailInstanceInfo>();
			MailInstanceInfo[] instanceDatas = null;

			if(!CommonUtil.isEmpty(mailContents)) {
				int length = mailContents.length;
				for(int i=0; i<length; i++) {
					MailContent mailContent = mailContents[i];
					MailInstanceInfo mailInstanceInfo = new MailInstanceInfo();
					mailInstanceInfo.setId(String.valueOf(mailContent.getId()));
					String sender = mailContent.getSender();
					String senderId = null;
					if(!SmartUtil.isBlankObject(sender)){
						int start = sender.indexOf("<");
						int end = sender.indexOf(">");
						if(start == -1 || end == -1){
							senderId = sender;
						}else{
							senderId = sender.substring(start+1, end);
							sender = sender.substring(0,  start == 0 ? 0 : start-1);
						}
					}
					
					// start -- added by sjlee
					String receiversStr = mailContent.getReceiver();
					UserInfo[] receivers = null;
					if(!SmartUtil.isBlankObject(receiversStr)){
						String[] receiversArr = receiversStr.split(",");
						if(!SmartUtil.isBlankObject(receiversArr)){
							receivers = new UserInfo[receiversArr.length];
							for(int j=0; j<receiversArr.length; j++){
								String receiver = receiversArr[j];
								String receiverId = null;
								if(!SmartUtil.isBlankObject(receiver)){
									int start = receiver.lastIndexOf("<");
									int end = receiver.lastIndexOf(">");
									if(start == -1 || end == -1){
										receiverId = receiver;
									}else{
										receiverId = receiver.substring(start+1, end);
										if (start > 0)
											receiver = receiver.substring(0, start-1);
									}
								}
								receivers[j] = new UserInfo(receiverId, org.claros.intouch.common.utility.Utility.htmlCheck(org.claros.commons.utility.Utility.updateTRChars(receiver)));
							}
						}
					}					
					// end -- added by sjlee
					
					UserInfo[] readers = null;
					String  strReaders = mailContent.getReader();
					if(!SmartUtil.isBlankObject(strReaders)){
						Address[] addrReaders = Utility.stringToAddressArray(strReaders);
						if(addrReaders != null){
							readers = new UserInfo[addrReaders.length];
							for(int k=0; addrReaders!=null && k<addrReaders.length; k++){
								InternetAddress addr = (InternetAddress)addrReaders[k];
								readers[k] = new UserInfo(addr.getAddress(), addr.getPersonal());
							}
						}
					}

					String subject = org.claros.intouch.common.utility.Utility.htmlCheck(org.claros.commons.utility.Utility.updateTRChars(mailContent.getSubject()));
					if(SmartUtil.isBlankObject(subject)){
						subject = SmartMessage.getString("mail.title.no.subject");
					}else{
						subject = subject.replaceAll("\"", "\'");
					}
					mailInstanceInfo.setSubject(subject);
					mailInstanceInfo.setSender(new UserInfo(senderId, org.claros.intouch.common.utility.Utility.htmlCheck(org.claros.commons.utility.Utility.updateTRChars(sender))));
					mailInstanceInfo.setReceivers(receivers);
					mailInstanceInfo.setReaders(readers);
					if(!SmartUtil.isBlankObject(mailContent.getSentDate()))
						mailInstanceInfo.setSendDate(new LocalDate(mailContent.getSentDate().getTime()-TimeZone.getDefault().getRawOffset()));
					mailInstanceInfo.setPriority(mailContent.getPriority());
					mailInstanceInfo.setSize(mailContent.getMsgSize());
					mailInstanceInfo.setMultipart(mailContent.getMultipart() == 0 ? false : true);
					mailInstanceInfo.setUnread(mailContent.getUnread() == 0 ? false : true);
					mailInstanceInfo.setType(Instance.TYPE_MAIL);
					mailInstanceInfoList.add(mailInstanceInfo);
					/*
					private UserInfo[] ccReceivers;
					private MailFolder mailFolder;
					private MailFolder parentMailFolder;*/
				}
			}
			if(mailInstanceInfoList.size() > 0) {
				instanceDatas = new MailInstanceInfo[mailInstanceInfoList.size()];
				mailInstanceInfoList.toArray(instanceDatas);
			}
			instanceInfoList.setInstanceDatas(instanceDatas);
			instanceInfoList.setTotalSize((int)totalCount);
			instanceInfoList.setSortedField(sortingField);
			instanceInfoList.setType(InstanceInfoList.TYPE_MAIL_INSTANCE_LIST);
			instanceInfoList.setPageSize(pageSize);
			instanceInfoList.setTotalPages(totalPages);
			instanceInfoList.setCurrentPage(currentPage);

			return instanceInfoList;

		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public MailInstance getMailInstanceById(String folderId, String msgId, int sendType) throws Exception {

		try{
			MailInstance instance = null;
			
			
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		    HttpServletRequest request = attr.getRequest();
	
	
			AuthProfile auth = getAuthProfile(request);
	
			ConnectionMetaHandler handler = (ConnectionMetaHandler)request.getSession().getAttribute("handler");
			ConnectionProfile profile = (ConnectionProfile)request.getSession().getAttribute("profile");
	
			MailControllerFactory factory = new MailControllerFactory(auth, profile, handler, folderId);
			MailController mailCont = factory.getMailController();

			String sentMessageId = null;
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
				
				InternetAddress from = (InternetAddress)email.getBaseHeader().getFrom()[0];
				InternetAddress[] to = (InternetAddress[])email.getBaseHeader().getTo();
				InternetAddress[] cc = (InternetAddress[])email.getBaseHeader().getCc();
				String fromShown = (new User(from.getAddress(), from.getPersonal())).getEmailAddressShown();
				String toShown = "";
				if(to != null){
					for(int k=0; k<to.length; k++)
						toShown = toShown + (k!=0 ? "; " : "") + (new User(to[k].getAddress(), to[k].getPersonal())).getEmailAddressShown();
				}
				String ccShown = "";
				if(cc != null){
					for(int k=0; k<cc.length; k++)
						ccShown = ccShown + (k!=0 ? "; " : "") + (new User(cc[k].getAddress(), cc[k].getPersonal())).getEmailAddressShown();
				}

				String date = "";
				if (email.getBaseHeader().getDate() != null) {
					date = org.claros.intouch.common.utility.Utility.htmlCheck(email.getBaseHeader().getDate().toString());
				}
				String subject = org.claros.intouch.common.utility.Utility.htmlCheck(org.claros.commons.utility.Utility.updateTRChars(email.getBaseHeader().getSubject()));
				Boolean sendReceiptNotification = email.getBaseHeader().getRequestReceiptNotification();
				String sendReceiptNotificationEmail = email.getBaseHeader().getReceiptNotificationEmail();
				String notificationEmail = "";

//				String contentHeader = 	"</br><ul>" +
//						"<li><span>" + SmartMessage.getString("common.title.sender") + ":</span>" + org.claros.intouch.common.utility.Utility.htmlCheck(org.claros.commons.utility.Utility.updateTRChars(fromShown)) + "</li>" + 
//						"<li><span>" + SmartMessage.getString("common.title.receivers") + ":</span>" + org.claros.intouch.common.utility.Utility.htmlCheck(org.claros.commons.utility.Utility.updateTRChars(toShown)) + "</li>" +
//						"<li><span>" + SmartMessage.getString("common.title.cc_receivers") + ":</span>" + org.claros.intouch.common.utility.Utility.htmlCheck(org.claros.commons.utility.Utility.updateTRChars(ccShown)) + "</li>" +
//						"<li><span>" + SmartMessage.getString("common.title.send_date") + ":</span>" + date + "</li>" +
//						"<li><span>" + SmartMessage.getString("common.title.subject") + ":</span>" + subject + "</li>" +
//						"</ul></br></br>";
				String contentHeader = 	
						"<div><b>" + SmartMessage.getString("common.title.sender") + ":</b>" + org.claros.intouch.common.utility.Utility.htmlCheck(org.claros.commons.utility.Utility.updateTRChars(fromShown)) + "</div>" + 
						"<div><b>" + SmartMessage.getString("common.title.receivers") + ":</b>" + org.claros.intouch.common.utility.Utility.htmlCheck(org.claros.commons.utility.Utility.updateTRChars(toShown)) + "</div>" +
						"<div><b>" + SmartMessage.getString("common.title.cc_receivers") + ":</b>" + org.claros.intouch.common.utility.Utility.htmlCheck(org.claros.commons.utility.Utility.updateTRChars(ccShown)) + "</div>" +
						"<div><b>" + SmartMessage.getString("common.title.send_date") + ":</b>" + date + "</div>" +
						"<div><b>" + SmartMessage.getString("common.title.subject") + ":</b>" + subject + "</div>" +
						"</br>";
			
				if(sendReceiptNotification != null && sendReceiptNotification.booleanValue() && sendReceiptNotificationEmail !=null && email.getBaseHeader().getUnread().booleanValue()){
					notificationEmail = org.claros.commons.utility.Utility.convertTRCharsToHtmlSafe(sendReceiptNotificationEmail);
				}
				
				if (profile.getProtocol().equals(Constants.POP3)) {
					mailCont.markAsRead(new Long(msgId));
				}
				
				if (subject == null || subject.equals("")) {
					subject = SmartMessage.getString("mail.title.no.subject");
				}

				InternetAddress addrFrom = null;
				InternetAddress[] addrTo = null;	
				InternetAddress[] addrCc = null;
				InternetAddress[] addrBcc = null;
				InternetAddress currentUser = new InternetAddress(SmartUtil.getCurrentUser().getId(), SmartUtil.getCurrentUser().getLongName());
				switch(sendType){
				case MailFolder.SEND_TYPE_NONE:
				case MailFolder.SEND_TYPE_DRAFTS:
					addrFrom = (InternetAddress)email.getBaseHeader().getFrom()[0];
					addrTo = (InternetAddress[])email.getBaseHeader().getTo();			
					addrCc = (InternetAddress[])email.getBaseHeader().getCc();
					addrBcc = (InternetAddress[])email.getBaseHeader().getBcc();
					break;
				case MailFolder.SEND_TYPE_FORWARD:
					addrFrom = currentUser;
					subject = SmartMessage.getString("mail.title.prefix.forward") + subject;
					break;
				case MailFolder.SEND_TYPE_REPLY:
					addrFrom = currentUser;
					addrTo = (InternetAddress[])email.getBaseHeader().getFrom();			
					subject = SmartMessage.getString("mail.title.prefix.reply") + subject;
					break;
				case MailFolder.SEND_TYPE_REPLY_ALL:
					addrFrom = currentUser;
					addrTo = (InternetAddress[])email.getBaseHeader().getFrom();
					InternetAddress[] toTmp = (InternetAddress[])email.getBaseHeader().getTo();
					InternetAddress[] ccTmp = (InternetAddress[])email.getBaseHeader().getCc();
					int toTmpLength = (toTmp==null) ? 0 : toTmp.length;
					int ccTmpLength = (ccTmp==null) ? 0 : ccTmp.length;
					addrCc = new InternetAddress[toTmpLength+ccTmpLength];
					for(int k=0; k<toTmpLength; k++)
						addrCc[k] = toTmp[k];
					for(int j=toTmpLength; j<toTmpLength+ccTmpLength; j++)
						addrCc[j] = ccTmp[j-toTmpLength];
					addrBcc = (InternetAddress[])email.getBaseHeader().getBcc();
					subject = SmartMessage.getString("mail.title.prefix.reply") + subject;
					break;
				}

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
				
				User[] readers = null;
				String  strReaders = email.getReaders();
				if(!SmartUtil.isBlankObject(strReaders)){
					Address[] addrReaders = Utility.stringToAddressArray(strReaders);
					if(addrReaders != null){
						readers = new User[addrReaders.length];
						for(int k=0; addrReaders!=null && k<addrReaders.length; k++){
							InternetAddress addr = (InternetAddress)addrReaders[k];
							readers[k] = new User(addr.getAddress(), addr.getPersonal());
						}
					}
				}
				
//				if(!SmartUtil.isBlankObject(subject)) subject = subject.replaceAll("\"", "\'");
				Date sentDate = (SmartUtil.isBlankObject(email.getBaseHeader().getDate())) ? new LocalDate() : email.getBaseHeader().getDate();
				instance = new MailInstance(msgId, subject, sender, new LocalDate(sentDate.getTime()));
				instance.setCreatedDate(new LocalDate(sentDate.getTime()));
				instance.setReceivers(receivers);
				instance.setCcReceivers(ccReceivers);
				instance.setBccReceivers(bccReceivers);
				instance.setReaders(readers);
				
				MailAttachment[] attachments = null;
				int count = 0;
				// parts begin
				List parts = email.getParts();
				String mailContent = null;
				if (parts != null) {
					if (parts.size() > 0 || i == -1) {
						EmailPart tmp = null;
						String mime = null;
						boolean gotMailContent = false;
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
							if(!gotMailContent && (mime.equals(MailAttachment.MIME_TYPE_TEXT_PLAIN) || mime.equals(MailAttachment.MIME_TYPE_TEXT_HTML))){
								String contentPostfix = 
											(sendType == MailFolder.SEND_TYPE_FORWARD || 
											 sendType == MailFolder.SEND_TYPE_REPLY || 
											 sendType == MailFolder.SEND_TYPE_REPLY_ALL) ? "<p>" + SmartMessage.getString("mail.title.content.postfix") + "</p>" + "</br>" +  contentHeader : "";
//								if(mailContent == null &&  mime.equals(MailAttachment.MIME_TYPE_TEXT_HTML)){
								if(mime.equals(MailAttachment.MIME_TYPE_TEXT_HTML)){
									mailContent = "";
				                	Object obj = tmp.getContent();
				                	if(null!=obj) mailContent = obj.toString();
									HtmlCleaner cleaner = new HtmlCleaner(mailContent);
									cleaner.setOmitXmlDeclaration(true);
									cleaner.setOmitXmlnsAttributes(true);
									cleaner.setUseCdataForScriptAndStyle(false);
									cleaner.clean(false,false);
									mailContent = cleaner.getCompactXmlAsString();
									mailContent = HTMLMessageParser.prepareInlineHTMLContent(email, mailContent);
									mailContent = org.claros.commons.utility.Utility.updateTRChars(mailContent);
									mailContent = contentPostfix + mailContent;
									i = j;
									gotMailContent = true;
									continue;
								}else if(mailContent == null && mime.equals(MailAttachment.MIME_TYPE_TEXT_PLAIN)){
									mailContent = "";
				                	Object obj = tmp.getContent();
				                	if(null!=obj) mailContent = contentPostfix + obj.toString();
									HtmlCleaner cleaner = new HtmlCleaner(mailContent);
									cleaner.setOmitXmlDeclaration(true);
									cleaner.setOmitXmlnsAttributes(true);
									cleaner.setUseCdataForScriptAndStyle(false);
									cleaner.clean(true,false);
									mailContent = cleaner.getXmlAsString();
									mailContent = org.claros.commons.utility.Utility.updateTRChars(mailContent);
									mailContent = contentPostfix + mailContent;
									i = j;
									continue;
								}
							}	

							String fileName = org.claros.commons.utility.Utility.updateTRChars(tmp.getFilename());
							if(!fileName.equals("Text Body")){
								attachments[count] = new MailAttachment(Integer.toString(j), fileName, mime, tmp.getSize());
								attachments[count].setFileType(SmartUtil.getFileExtension(fileName));
								attachments[count].setPart(tmp);
								count++;
							}else{
//								attachments[count] = new MailAttachment(Integer.toString(j), fileName, mime, tmp.getSize());
//								attachments[count].setFileType(SmartUtil.getFileExtension(fileName));
//								attachments[count].setPart(tmp);
//								count++;								
							}
						}
					}
				}
				MailAttachment[] finalAttachments = null;
				if(count>0 && (sendType == MailFolder.SEND_TYPE_NONE || sendType == MailFolder.SEND_TYPE_DRAFTS || sendType == MailFolder.SEND_TYPE_FORWARD)){
					finalAttachments = new MailAttachment[count]; 
					for(int j=0; j<count; j++){
						finalAttachments[j] = attachments[j];
					}
				}
				instance.setMailContents(mailContent);
				instance.setAttachments(finalAttachments);
				instance.setPartId(i);
				instance.setMailFolder(getMailFolderById(folderId));
				MailContentCond mailContentCond = new MailContentCond();
				mailContentCond.setId(Long.parseLong(msgId));
				mailContentCond.setFolderId(Long.parseLong(folderId));
				String prevMsgId = String.valueOf(getMailManager().getPrevMailId(null, mailContentCond));
				String nextMsgId = String.valueOf(getMailManager().getNextMailId(null, mailContentCond));
				instance.setPrevMsgId("0".equals(prevMsgId) ? null : prevMsgId);
				instance.setNextMsgId("0".equals(nextMsgId) ? null : nextMsgId);
				
				SmartUtil.publishNoticeCount(SmartUtil.getCurrentUser().getId(), SmartUtil.getCurrentUser().getCompanyId(), new Notice(Notice.TYPE_MAILBOX, getUnreadEmails()));

				mailCont.appendMailReader(sender.getId(), email.getBaseHeader().getSentMessageId());
				
			} catch (Exception e) {
				throw e;
			}
			return instance;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
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
			if(fld.getParentId()>0){
				FolderDbObjectWrapper parent = folderCont.getFolderById(fld.getParentId().toString());
				mailFolder.setParentName(parent.getFolderName());
			}
			return mailFolder;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void sendMail(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try {
			Map<String, List<Map<String, String>>> from = (Map<String, List<Map<String, String>>>)requestBody.get("from");
			Map<String, Object> newMail = (HashMap<String, Object>)requestBody.get("frmNewMail");
			Map<String, List<Map<String, String>>> receivers = (HashMap<String, List<Map<String, String>>>)newMail.get("emailReceivers");
			Map<String, List<Map<String, String>>> ccReceivers = (HashMap<String, List<Map<String, String>>>)newMail.get("emailCcReceivers");
			Map<String, List<Map<String, String>>> bccReceivers = (HashMap<String, List<Map<String, String>>>)newMail.get("emailBccReceivers");
			Map<String, List<Map<String, String>>> attachments = (HashMap<String, List<Map<String, String>>>)newMail.get("emailAttachments");
			String subject = (String)newMail.get("emailSubject");
			String body = (String)newMail.get("emailContents");
			String requestReceiptNotification = (String)newMail.get("requestReceiptNotification");
			String priority = (String)newMail.get("emailPriority");
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
			header.setFromShown(Utility.addressArrToString(adrs));
			
			Address tos[] = Utility.stringListToAddressArray(receivers.get("users"));
			header.setTo(tos);
			header.setToShown(Utility.addressArrToString(tos));
//if (saveSentContacts != null && saveSentContacts.equals("yes")) {
//	saveContacts(auth, tos);
//}
			
			if (ccReceivers != null) {
				Address ccs[] = Utility.stringListToAddressArray(ccReceivers.get("users"));
				header.setCc(ccs);
				header.setCcShown(Utility.addressArrToString(ccs));
//if (saveSentContacts != null && saveSentContacts.equals("yes")) {
//	saveContacts(auth, ccs);
//}
			}
			if (bccReceivers != null) {
				Address bccs[] = Utility.stringListToAddressArray(bccReceivers.get("users"));
				header.setBcc(bccs);
				header.setBccShown(Utility.addressArrToString(bccs));
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
//			bodyPart.setContentType("text/html; charset=EUC-KR");
			/*
			HtmlCleaner cleaner = new HtmlCleaner(body);
			cleaner.clean(false,false);
			*/
			
			bodyPart.setContent(body);
			parts.add(0, bodyPart);
			
			ArrayList attList = Utility.stringListToEmailPartArray(attachments.get("files"));
			// attach some files...
			if (attList != null) {				
				List newLst = new ArrayList();
				EmailPart tmp = null;
				MailInstance instance = null;
				for (int i=0;i<attList.size();i++) {
					try {
						tmp = (EmailPart)attList.get(i);
						String disp = tmp.getDisposition();
						if(disp != null && !disp.equals("")){
							File f = new File(disp);
							FileInputStream fis = new FileInputStream(f);
							BufferedInputStream bis = new BufferedInputStream(fis);
							byte data[] = new byte[(int)f.length() + 2];
							bis.read(data);
							bis.close();
				
							MimeBodyPart bp = new MimeBodyPart();
							DataSource ds = new ByteArrayDataSource(data, tmp.getContentType(), tmp.getFilename());
							bp.setDataHandler(new DataHandler(ds));
							bp.setDisposition("attachment; filename=\"" + tmp.getFilename() + "\"");
							tmp.setDisposition(bp.getDisposition());
							bp.setFileName(tmp.getFilename());
							tmp.setDataSource(ds);
							tmp.setContent(bp.getContent());
						}else{
							if(instance == null)
								instance = this.getMailInstanceById(tmp.getFolderId(), tmp.getMsgId(), MailFolder.SEND_TYPE_NONE);
							if(instance!=null && instance.getAttachments()!=null){
								for(int att=0; att<instance.getAttachments().length; att++){
									MailAttachment attachment = instance.getAttachments()[att];
									if(attachment.getPart().getId() == tmp.getId()){
										tmp = attachment.getPart();
										break;
									}
								}
							}
						}
						newLst.add(tmp);
						
					} catch (Exception e) {
						e.printStackTrace();
						throw e;
					}
				}
				parts.addAll(newLst);
			}
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
			
			saveSentMail(auth, msg, header, request);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@Override
	public void saveMailAsDraft(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try {
			Map<String, List<Map<String, String>>> from = (Map<String, List<Map<String, String>>>)requestBody.get("from");
			Map<String, Object> newMail = (HashMap<String, Object>)requestBody.get("frmNewMail");
			Map<String, List<Map<String, String>>> receivers = (HashMap<String, List<Map<String, String>>>)newMail.get("emailReceivers");
			Map<String, List<Map<String, String>>> ccReceivers = (HashMap<String, List<Map<String, String>>>)newMail.get("emailCcReceivers");
			Map<String, List<Map<String, String>>> bccReceivers = (HashMap<String, List<Map<String, String>>>)newMail.get("emailBccReceivers");
			Map<String, List<Map<String, String>>> attachments = (HashMap<String, List<Map<String, String>>>)newMail.get("emailAttachments");
			String subject = (String)newMail.get("emailSubject");
			String body = (String)newMail.get("emailContents");
			String requestReceiptNotification = (String)newMail.get("requestReceiptNotification");
			String priority = (String)newMail.get("emailPriority");
			String sensitivity = (String)newMail.get("sensitivity");
			
			AuthProfile auth = getAuthProfile();
			
			// now create a new email object.
			Email email = new Email();
			EmailHeader header = new EmailHeader();

			Address adrs[] = Utility.stringListToAddressArray(from.get("users"));
			header.setFrom(adrs);
			//start 12.09.05 각종 Shown에 set 누락
			header.setFromShown(Utility.addressArrToString(adrs));
			
			Address tos[] = Utility.stringListToAddressArray(receivers.get("users"));
			header.setTo(tos);
			header.setToShown(Utility.addressArrToString(tos));
			
			if (ccReceivers != null) {
				Address ccs[] = Utility.stringListToAddressArray(ccReceivers.get("users"));
				header.setCc(ccs);
				header.setCcShown(Utility.addressArrToString(ccs));
			}
			if (bccReceivers != null) {
				Address bccs[] = Utility.stringListToAddressArray(bccReceivers.get("users"));
				header.setBcc(bccs);
				header.setBccShown(Utility.addressArrToString(bccs));
				//end jy.bae
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
//			bodyPart.setContentType("text/html; charset=UTF-8");
			bodyPart.setContentType("text/html; charset=EUC-KR");
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
			
			ArrayList attList = Utility.stringListToEmailPartArray(attachments.get("files"));
			// attach some files...
			if (attList != null) {
				List newLst = new ArrayList();
				EmailPart tmp = null;
				for (int i=0;i<attList.size();i++) {
					try {
						tmp = (EmailPart)attList.get(i);
						String disp = tmp.getDisposition();
						File f = new File(disp);
						FileInputStream fis = new FileInputStream(f);
						BufferedInputStream bis = new BufferedInputStream(fis);
						byte data[] = new byte[(int)f.length() + 2];
						bis.read(data);
						bis.close();
			
						MimeBodyPart bp = new MimeBodyPart();
						DataSource ds = new ByteArrayDataSource(data, tmp.getContentType(), tmp.getFilename());
						bp.setDataHandler(new DataHandler(ds));
						bp.setDisposition("attachment; filename=\"" + tmp.getFilename() + "\"");
						tmp.setDisposition(bp.getDisposition());
						bp.setFileName(tmp.getFilename());
						tmp.setDataSource(ds);
						tmp.setContent(bp.getContent());
						newLst.add(tmp);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				parts.addAll(newLst);
			}
			email.setParts(parts);
			
			// it is time to send the email object message
			Smtp smtp = new Smtp(getConnectionProfile(request), getAuthProfile(request));
			HashMap sendRes = smtp.send(email, true);
			MimeMessage msg = (MimeMessage)sendRes.get("msg");

			saveDraft(auth, msg, header, request);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void moveMails(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		try {

			List<String> ids = (List<String>)requestBody.get("ids");
			String srcFolder = (String)requestBody.get("source");
			String targetFolder = (String)requestBody.get("target");
			
			if (ids != null && srcFolder != null && targetFolder != null) {
				AuthProfile auth = getAuthProfile(request);
				ConnectionMetaHandler handler = (ConnectionMetaHandler)request.getSession().getAttribute("handler");
				ConnectionProfile profile = (ConnectionProfile)request.getSession().getAttribute("profile");

				MailControllerFactory factory = new MailControllerFactory(auth, profile, handler, srcFolder);
				MailController mailCont = factory.getMailController();
				
				int msgs[] = new int[ids.size()];
				for(int counter=0; counter<ids.size(); counter++) {
					msgs[counter] = Integer.parseInt(ids.get(counter));
				}

				// action time
				mailCont.moveEmails(msgs, targetFolder);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteMails(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		try {

			User user = SmartUtil.getCurrentUser();
			String userId = user.getId();
			
			List<String> ids = (List<String>)requestBody.get("ids");
			String folderId = (String)requestBody.get("folderId");
			//휴지통 비우기시에 넘어오는 파라미터
			boolean removeAll = false;
			if (requestBody.get("removeAll") != null) {
				if (requestBody.get("removeAll") instanceof Boolean)
					removeAll = (Boolean)requestBody.get("removeAll");
			}
			
			if (ids != null && folderId != null && !removeAll) {
				AuthProfile auth = getAuthProfile(request);
				ConnectionMetaHandler handler = (ConnectionMetaHandler)request.getSession().getAttribute("handler");
				ConnectionProfile profile = (ConnectionProfile)request.getSession().getAttribute("profile");

				MailControllerFactory factory = new MailControllerFactory(auth, profile, handler, folderId);
				MailController mailCont = factory.getMailController();
				
				int msgs[] = new int[ids.size()];
				for(int counter=0; counter<ids.size(); counter++) {
					msgs[counter] = Integer.parseInt(ids.get(counter));
				}

				// get the trash folder object
				FolderControllerFactory fFactory = new FolderControllerFactory(auth, profile, handler);
				FolderController foldCont = fFactory.getFolderController();
				FolderDbObject fItem = foldCont.getTrashFolder();

				// action time
				if (profile.getProtocol().equals(Constants.POP3)) {
					if (Long.toString(fItem.getId()).equals(folderId)) {
						mailCont.deleteEmails(msgs);
					} else {
						mailCont.moveEmails(msgs, "" + fItem.getId());
					}
				} else {
					if (fItem.getFolderName().equals(folderId)) {
						mailCont.markAsDeleted(msgs);
					} else {
						mailCont.moveEmails(msgs, fItem.getFolderName());
					}
				}
			} else if (ids == null && !CommonUtil.isEmpty(folderId) && removeAll) {

				AuthProfile auth = getAuthProfile(request);
				ConnectionMetaHandler handler = (ConnectionMetaHandler)request.getSession().getAttribute("handler");
				ConnectionProfile profile = (ConnectionProfile)request.getSession().getAttribute("profile");

				MailControllerFactory factory = new MailControllerFactory(auth, profile, handler, folderId);
				MailController mailCont = factory.getMailController();
				
				//휴지통비우
				MailContentCond mailCond = new MailContentCond();
				mailCond.setFolderId(Long.parseLong(folderId));
				mailCond.setUsername(userId);
				mailCond.setUnread(-1);
				MailContent[] mailContents = getMailManager().getMailContents(userId, mailCond, IManager.LEVEL_ALL);
				if (!CommonUtil.isEmpty(mailContents)) {
					int msgs[] = new int[mailContents.length];
					for (int j = 0; j < mailContents.length; j++) {
						MailContent mailContent = mailContents[j];
						msgs[j] = (int)mailContent.getId();
					}
					mailCont.deleteEmails(msgs);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void newMailFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		String parentId = (String)requestBody.get("parentId");
		String sFolderType = (String)requestBody.get("folderType");
		int folderType = (SmartUtil.isBlankObject(sFolderType)) ? 0 : Integer.parseInt(sFolderType);
		String folderName = (String)requestBody.get("folderName");
		String folderDesc = (String)requestBody.get("folderDesc");
		
		if (folderName != null) {
			// character corrections. This is important for turkish users. 
			folderName = org.claros.commons.utility.Utility.replaceAllOccurances(folderName.trim(), ".", "_");
			folderName = org.claros.commons.utility.Utility.replaceAllOccurances(folderName, "\u0131", "i");
			folderName = org.claros.commons.utility.Utility.replaceAllOccurances(folderName, "\u0130", "I");
			folderName = org.claros.commons.utility.Utility.replaceAllOccurances(folderName, "\u015E", "S");
			folderName = org.claros.commons.utility.Utility.replaceAllOccurances(folderName, "\u015F", "s");
			folderName = org.claros.commons.utility.Utility.replaceAllOccurances(folderName, "\u00E7", "c");
			folderName = org.claros.commons.utility.Utility.replaceAllOccurances(folderName, "\u00C7", "C");
			folderName = org.claros.commons.utility.Utility.replaceAllOccurances(folderName, "\u00FC", "u");
			folderName = org.claros.commons.utility.Utility.replaceAllOccurances(folderName, "\u00DC", "U");
			folderName = org.claros.commons.utility.Utility.replaceAllOccurances(folderName, "\u00F6", "o");
			folderName = org.claros.commons.utility.Utility.replaceAllOccurances(folderName, "\u00D6", "O");
			folderName = org.claros.commons.utility.Utility.replaceAllOccurances(folderName, "\u011F", "g");
			folderName = org.claros.commons.utility.Utility.replaceAllOccurances(folderName, "\u011E", "G");
			folderName = org.claros.commons.utility.Utility.replaceAllOccurances(folderName, "\"", "_");

			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		    request = attr.getRequest();
			AuthProfile auth = getAuthProfile(request);
			ConnectionMetaHandler handler = (ConnectionMetaHandler)request.getSession().getAttribute("handler");
			ConnectionProfile profile = (ConnectionProfile)request.getSession().getAttribute("profile");
			FolderControllerFactory factory = new FolderControllerFactory(auth, profile, handler);
			FolderController foldCont = factory.getFolderController();
			
			FolderDbObject tmp = null;
			try{
				tmp = foldCont.getFolderByName(parentId, folderName);
			}catch(Exception e){	
			}
			if(tmp != null){
				throw new Exception("Duplicated Folder Name Error!");
			}
			
			FolderDbObject folder = new FolderDbObject();
			folder.setFolderType(folderType);
			folder.setFolderName(folderName);
			folder.setUsername(auth.getEmailId());
			Long lParent = (SmartUtil.isBlankObject(parentId)) ? 0 : new Long(parentId);
			folder.setParentId(lParent);
			
			foldCont.createFolder(folder);
		}

	}

	@Override
	public void setMailFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		try {

			String parentId = (String)requestBody.get("parentId");
			String folderId = (String)requestBody.get("folderId");
			String folderName = (String)requestBody.get("folderName");
			String folderDesc = (String)requestBody.get("folderDesc");
			
			if (folderName != null) {
				// character corrections. This is important for turkish users. 
				folderName = org.claros.commons.utility.Utility.replaceAllOccurances(folderName.trim(), ".", "_");
				folderName = org.claros.commons.utility.Utility.replaceAllOccurances(folderName, "\u0131", "i");
				folderName = org.claros.commons.utility.Utility.replaceAllOccurances(folderName, "\u0130", "I");
				folderName = org.claros.commons.utility.Utility.replaceAllOccurances(folderName, "\u015E", "S");
				folderName = org.claros.commons.utility.Utility.replaceAllOccurances(folderName, "\u015F", "s");
				folderName = org.claros.commons.utility.Utility.replaceAllOccurances(folderName, "\u00E7", "c");
				folderName = org.claros.commons.utility.Utility.replaceAllOccurances(folderName, "\u00C7", "C");
				folderName = org.claros.commons.utility.Utility.replaceAllOccurances(folderName, "\u00FC", "u");
				folderName = org.claros.commons.utility.Utility.replaceAllOccurances(folderName, "\u00DC", "U");
				folderName = org.claros.commons.utility.Utility.replaceAllOccurances(folderName, "\u00F6", "o");
				folderName = org.claros.commons.utility.Utility.replaceAllOccurances(folderName, "\u00D6", "O");
				folderName = org.claros.commons.utility.Utility.replaceAllOccurances(folderName, "\u011F", "g");
				folderName = org.claros.commons.utility.Utility.replaceAllOccurances(folderName, "\u011E", "G");
				folderName = org.claros.commons.utility.Utility.replaceAllOccurances(folderName, "\"", "_");

				AuthProfile auth = getAuthProfile(request);
				ConnectionMetaHandler handler = (ConnectionMetaHandler)request.getSession().getAttribute("handler");
				ConnectionProfile profile = (ConnectionProfile)request.getSession().getAttribute("profile");
				FolderControllerFactory factory = new FolderControllerFactory(auth, profile, handler);
				FolderController foldCont = factory.getFolderController();
				
				FolderDbObject itm = foldCont.getFolderById(folderId);
				foldCont.renameFolder(parentId, itm.getFolderName(), folderName);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteMailFolder(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		try {

			String folderId = (String)requestBody.get("folderId");
			
			if (folderId != null) {

				AuthProfile auth = getAuthProfile(request);
				ConnectionMetaHandler handler = (ConnectionMetaHandler)request.getSession().getAttribute("handler");
				ConnectionProfile profile = (ConnectionProfile)request.getSession().getAttribute("profile");

				FolderControllerFactory factory = new FolderControllerFactory(auth, profile, handler);
				FolderController foldCont = factory.getFolderController();
				
				FolderDbObject[] folders = null;
				FolderDbObject folder = foldCont.getFolderById(folderId);

				if(folder.getFolderType() == MailFolder.TYPE_GROUP){
					List lFolders = foldCont.getFolders(folderId);
					if(!SmartUtil.isBlankObject(lFolders)){
						folders = new FolderDbObject[lFolders.size()+1];
						int index;
						for(index=0; index<lFolders.size(); index++)
							folders[index] = (FolderDbObject)lFolders.get(index);
						folders[index] = folder;
					}else{
						folders = new FolderDbObject[]{folder};
					}
				}else{
					folders = new FolderDbObject[]{folder};
				}
				
				for(int i=0; i<folders.length; i++){
					folderId = folders[i].getId().toString();
					List mails = foldCont.getMailsByFolder(folderId);
					if(!SmartUtil.isBlankObject(mails)){
						int[] msgs = new int[mails.size()];
						for(int j=0; j<mails.size(); j++){
							msgs[j] = ((MsgDbObject)mails.get(j)).getId().intValue();
						}
						MailControllerFactory mailFactory = new MailControllerFactory(auth, profile, handler, folderId);
						MailController mailCont = mailFactory.getMailController();
						mailCont.moveEmails(msgs, foldCont.getTrashFolder().getId().toString());
					}
					try{
						foldCont.deleteFolder(folderId);
					}catch (Exception e){
						e.printStackTrace();						
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void checkEmail() throws Exception {
		if(!SmartUtil.getCurrentUser().isUseMail()) return;
		try {
		    ConnectionMetaHandler handler = getConnectionMetaHandler();
			if(handler == null){
				System.out.println("========= ConnectionMetaHanlder is null error !!!! ===========");
				return;
			}
			
			ConnectionProfile profile = getConnectionProfile();
			AuthProfile auth = getAuthProfile();
			InboxControllerFactory inFact = new InboxControllerFactory(auth, profile, handler);
			InboxController inCont = inFact.getInboxController();
			inCont.checkEmail();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int getUnreadEmails() throws Exception {
		try {
		    ConnectionMetaHandler handler = getConnectionMetaHandler();
			if(handler == null)  return 0;
			
			ConnectionProfile profile = getConnectionProfile();
			AuthProfile auth = getAuthProfile();
			
			FolderControllerFactory fFactory = new FolderControllerFactory(auth, profile, handler);
			FolderController foldCont = fFactory.getFolderController();
			return foldCont.countUnreadMessages(foldCont.getInboxFolder().getId().toString());
		} catch (Exception e) {
		}
		return 0;
	}

	@Override
	public boolean authenticateEmailAccount(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		EmailServer[] emailServers = settingsService.getEmailServers();
		String mailServerId = (String)requestBody.get("mailServerId");
		String username = (String)requestBody.get("username");
		String password = (String)requestBody.get("password");
	    
		ConnectionMetaHandler handler = null;
		ConnectionProfile profile = null;
		if(SmartUtil.isBlankObject(mailServerId) || SmartUtil.isBlankObject(emailServers))
			return false;

		for(int i=0; i<emailServers.length; i++){
			if(emailServers[i].getId().equals(mailServerId)){
				profile = emailServers[i].getConnectionProfile();
				break;
			}
		}
		
		AuthProfile auth = new AuthProfile();
		auth.setUsername(username);
		auth.setPassword(password);
		try {
			handler = MailAuth.authenticate(profile, auth, handler);
			if (handler != null) {
			    request.getSession().removeAttribute("handler");
			    request.getSession().removeAttribute("profile");
			    request.getSession().removeAttribute("auth");
				return true;
			}
		} catch (LoginInvalidException e) {
		} catch (ServerDownException e) {
		}
		return false;
	}

	@Override
	public MailFolder[] getMailFolders() throws Exception {
		try{
			MailFolder[] mailFolders = null;
	
		    ConnectionMetaHandler handler = getConnectionMetaHandler();
		    if(handler == null) return null;
		    
			ConnectionProfile profile = getConnectionProfile();
			AuthProfile auth = getAuthProfile();
			
			FolderControllerFactory foldFact = new FolderControllerFactory(auth, profile, handler);
			FolderController folderCont = foldFact.getFolderController();
			
			List folders = folderCont.getFolders();
			if (folders != null) {
				FolderDbObjectWrapper tmp = null;
				mailFolders = new MailFolder[folders.size()];
				for(int i=0; i<mailFolders.length; i++){
					tmp = (FolderDbObjectWrapper)folders.get(i);
					mailFolders[i] = new MailFolder(tmp.getId().toString(), tmp.getParentId().toString(), tmp.getFolderName(), tmp.getFolderType());
					mailFolders[i].setUnreadItemCount(tmp.getUnreadItemCount().intValue());
					if(tmp.getParentId()>0){
						try{
							FolderDbObject parent = folderCont.getFolderById(tmp.getParentId().toString());
							if(parent!=null)
								mailFolders[i].setParentName(parent.getFolderName());
						}catch (Exception e){
							e.printStackTrace();
						}
					}
				}
			}
			return mailFolders;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	//(주)원진 에서 사용하는 포스티안 메일 연동에 대한 결과값을 파싱한다
	//원진 회사에 관련된 메소드 이기 때문에 다른 프로젝트에서 영향을 주지 않도록 코딩해야 하며
	//평상시에는 주석처리한다 
	private boolean parsingResultXml(String resultXml) throws Exception {
		
		/* 호출성공시 리턴받게되는 Xml
		<?xml version='1.0' encoding='utf-8' ?>
		<data>
		   <returnCode>0</returnCode>
		   <returnMsg>Success to modify a mail account</returnMsg>
		</data>
		*/
		//xml 파싱
		
		String returnCode = null;
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		Document doc = builder.parse(new ByteArrayInputStream(resultXml.getBytes()));
		
		Element root = doc.getDocumentElement();
		
		System.out.println(root.getNodeName());

		NodeList nodeList = root.getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node returnCodeNode = nodeList.item(i);
			if (returnCodeNode.getNodeType() != Node.ELEMENT_NODE || !returnCodeNode.getNodeName().equals("returnCode"))
				continue;
			
			System.out.println(returnCodeNode.getNodeName());
			NodeList textNodeList = returnCodeNode.getChildNodes();
			for (int j = 0; j < textNodeList.getLength(); j++) {
				Node textNode = textNodeList.item(j);
				returnCode = textNode.getNodeValue();
			}
		}
		System.out.println("result : " + returnCode);
		if (returnCode.equalsIgnoreCase("0")) {
			return true;
		} else {
			return false;
		}
	}
	
	
	@Override
	public void changeMailPasswordRequest(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

	    User currentUser = SmartUtil.getCurrentUser();
	    String userId = currentUser.getId();
	    
		String requestUrl = (String)requestBody.get("requestData");
		String userName = (String)requestBody.get("userName");
		String oldPassword = (String)requestBody.get("oldPassword");
		String newPassword = (String)requestBody.get("newPassword");
		
		//원진 소스
//		if (userId.indexOf("@modineonegene.com") != -1) {
//			requestUrl = StringUtils.replace(requestUrl, "domain=onegene.com", "domain=modineonegene.com");
//		}
		
		String result = ServletUtil.request(requestUrl);
		
		if (!parsingResultXml(result)) {
			throw new Exception("Change Mail Password Fail!! " + result);
		} else {
			System.out.println("############## Change Mail Password Success!! ##############");
		}
		//mailAccount 의 계정비밀번호도 바꾸어 준다

		MailAccountCond mailAccountCond = new MailAccountCond();
		mailAccountCond.setUserId(userId);
		net.smartworks.server.engine.mail.model.MailAccount mailAccount = getMailManager().getMailAccount(userId, mailAccountCond, IManager.LEVEL_ALL);
		mailAccount.setMailPassword(newPassword);
		getMailManager().setMailAccount(userId, mailAccount, IManager.LEVEL_ALL);
		
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		if(authentication != null) {
			Object principal = authentication.getPrincipal();
			if(!principal.equals("anonymousUser")) {
				Login login = (Login)(principal instanceof Login ? principal : null);
				login.setMailPassword(newPassword);
			}
		}
	}

	@Override
	public void addJunk(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		
		if (requestBody.get("senderDomains") == null && requestBody.get("senderIds") == null)
			return;
		
		List junkList = null;
		boolean isJunkMailAddress = true;
		StringBuffer junkAddressBuff = new StringBuffer();
		
		if (requestBody.get("senderDomains") != null) {
			isJunkMailAddress = false;
			junkList = (ArrayList)requestBody.get("senderDomains");
			if (junkList == null || junkList.size() == 0)
				return;
		} else {
			junkList = (ArrayList)requestBody.get("senderIds");
			if (junkList == null || junkList.size() == 0)
				return;
		}
		
	    User currentUser = SmartUtil.getCurrentUser();
	    String userId = currentUser.getId();
	    
		//로그인 사용자의 mailAccount 를 가져온다
	    MailAccountCond mailAccountCond = new MailAccountCond();
	    mailAccountCond.setUserId(userId);
	    
	    net.smartworks.server.engine.mail.model.MailAccount mailAccount = SwManagerFactory.getInstance().getMailManager().getMailAccount(userId, mailAccountCond, IManager.LEVEL_ALL);
	    
	    String junks = mailAccount.getJunks();
	    //가져온 mailAccount 에서 junkMailIds를 가져와 새로 추가된 junk가 이미 등록되어 있는것이지를 검사한다
	    if (!CommonUtil.isEmpty(junks)) {
	    	//mailId 는 preFix 로 M_, domain은 D_ 로시작하도록 등록하여 이후 조회시 아이디와 도메인을 구별한다
	    	for (int i = 0; i < junkList.size(); i++) {
	    		if (isJunkMailAddress) {
	    			if (junks.indexOf(";M_" + junkList.get(i)) == -1) {
	    				junkAddressBuff.append(";M_").append(junkList.get(i));
	    			}
	    		} else {
	    			if (junks.indexOf(";D_" + junkList.get(i)) == -1) {
	    				junkAddressBuff.append(";D_").append(junkList.get(i));
	    			}
	    		}
			}
	    	mailAccount.setJunks(junks + junkAddressBuff.toString());
	    } else {
	    	//없다면 새로 등록한다
	    	for (int i = 0; i < junkList.size(); i++) {
	    		if (isJunkMailAddress) {
	    			junkAddressBuff.append(";M_").append(junkList.get(i));
	    		} else {
	    			junkAddressBuff.append(";D_").append(junkList.get(i));
	    		}
			}
	    	mailAccount.setJunks(junkAddressBuff.toString());
	    }
		//저장
	    SwManagerFactory.getInstance().getMailManager().setMailAccount(userId, mailAccount, IManager.LEVEL_ALL);
		
	}

	@Override
	public void removeJunk(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {

		if (requestBody.get("senderDomains") == null && requestBody.get("senderIds") == null)
			return;
		
		List junkList = null;
		boolean isJunkMailAddress = true;
		
		if (requestBody.get("senderDomains") != null) {
			isJunkMailAddress = false;
			junkList = (ArrayList)requestBody.get("senderDomains");
			if (junkList == null || junkList.size() == 0)
				return;
		} else {
			junkList = (ArrayList)requestBody.get("senderIds");
			if (junkList == null || junkList.size() == 0)
				return;
		}
		User currentUser = SmartUtil.getCurrentUser();
	    String userId = currentUser.getId();
	    
		//로그인 사용자의 mailAccount 를 가져온다
	    MailAccountCond mailAccountCond = new MailAccountCond();
	    mailAccountCond.setUserId(userId);
	    
	    net.smartworks.server.engine.mail.model.MailAccount mailAccount = SwManagerFactory.getInstance().getMailManager().getMailAccount(userId, mailAccountCond, IManager.LEVEL_ALL);
	    
	    String junks = mailAccount.getJunks();
	
	    if (!CommonUtil.isEmpty(junks)) {
	    	//mailId 는 preFix 로 M_, domain은 D_ 로시작하도록 등록하여 이후 조회시 아이디와 도메인을 구별한다
	    	for (int i = 0; i < junkList.size(); i++) {
	    		if (isJunkMailAddress) {
	    			if (junks.indexOf(";M_" + junkList.get(i)) != -1) {
	    				junks = StringUtils.replace(junks, ";M_" + junkList.get(i), "");
	    			}
	    		} else {
	    			if (junks.indexOf(";D_" + junkList.get(i)) != -1) {
	    				junks = StringUtils.replace(junks, ";D_" + junkList.get(i), "");
	    			}
	    		}
			}
	    	mailAccount.setJunks(junks);
	    }
		//저장
	    SwManagerFactory.getInstance().getMailManager().setMailAccount(userId, mailAccount, IManager.LEVEL_ALL);
	}

	@Override
	public String[][] getJunkIds(String userId) throws Exception {
		if (CommonUtil.isEmpty(userId)) {
			User currentUser = SmartUtil.getCurrentUser();
			userId = currentUser.getId();
		}
			
		//로그인 사용자의 mailAccount 를 가져온다
	    MailAccountCond mailAccountCond = new MailAccountCond();
	    mailAccountCond.setUserId(userId);
	    
	    net.smartworks.server.engine.mail.model.MailAccount mailAccount = SwManagerFactory.getInstance().getMailManager().getMailAccount(userId, mailAccountCond, IManager.LEVEL_ALL);
	    
	    String junks = mailAccount.getJunks();
	    if (CommonUtil.isEmpty(junks))
	    	return null;
		
	    String[] junksArray = StringUtils.tokenizeToStringArray(junks, ";");
	    
	    List junkMailIdList = new ArrayList();
	    List junkDomainIdList = new ArrayList();
	    for (int i = 0; i < junksArray.length; i++) {
	    	String junkTempIds = junksArray[i];
	    	if (junkTempIds.indexOf("M_") == 0) {
	    		junkMailIdList.add(junkTempIds.substring(2, junkTempIds.length()));
	    	} else {
	    		junkDomainIdList.add(junkTempIds.substring(2, junkTempIds.length()));
	    	}
		}
	    
	    String[] resultMailIdArray = new String[junkMailIdList.size()];
	    junkMailIdList.toArray(resultMailIdArray);
	    String[] resultDomainIdArray = new String[junkDomainIdList.size()];
	    junkDomainIdList.toArray(resultDomainIdArray);
	    
	    return new String[][]{resultMailIdArray, resultDomainIdArray};
		
		//return SmartTest.getJunkIds();
	}
	
	@Override
	public String[][] getJunkIds() throws Exception {
	   return getJunkIds(null);
	}
}
