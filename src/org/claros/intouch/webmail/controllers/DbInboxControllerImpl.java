package org.claros.intouch.webmail.controllers;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import net.smartworks.model.mail.MailAccount;
import net.smartworks.model.mail.MailAttachment;
import net.smartworks.model.mail.MailFolder;
import net.smartworks.model.notice.Notice;
import net.smartworks.server.service.factory.SwServiceFactory;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartUtil;

import org.claros.commons.auth.models.AuthProfile;
import org.claros.commons.mail.exception.MailboxActionException;
import org.claros.commons.mail.models.ConnectionMetaHandler;
import org.claros.commons.mail.models.ConnectionProfile;
import org.claros.commons.mail.models.Email;
import org.claros.commons.mail.models.EmailHeader;
import org.claros.commons.mail.models.EmailPart;
import org.claros.commons.mail.parser.MessageParser;
import org.claros.commons.mail.protocols.Protocol;
import org.claros.commons.mail.protocols.ProtocolFactory;
import org.claros.commons.utility.MD5;
import org.claros.intouch.webmail.factory.FolderControllerFactory;
import org.claros.intouch.webmail.factory.MailControllerFactory;
import org.claros.intouch.webmail.models.MsgDbObject;

/**
 * @author Umut Gokbayrak
 */
public class DbInboxControllerImpl extends InboxControllerBase implements InboxController {
//	private static Log log = LogFactory.getLog(DbInboxControllerImpl.class);
//	private static Locale loc = new Locale("en", "US");
	/**
	 * @param profile
	 * @param auth
	 * @param handler
	 */
	public DbInboxControllerImpl(AuthProfile auth, ConnectionProfile profile, ConnectionMetaHandler handler) {
		super(auth, profile, handler);
	}

	/* (non-Javadoc)
	 * @see org.claros.groupware.webmail.controllers.InboxController#checkEmail()
	 */
	
	static List<CheckingModel> checkingQueue = new LinkedList<CheckingModel>();
	synchronized static int addChecking(String userId, String companyId){
		if(SmartUtil.isBlankObject(userId) || SmartUtil.isBlankObject(companyId)){
			System.out.println("UserId or CompanyId does not exist Error!!!!, UserId=" + userId + ", CompanyId=" + companyId);
			return -1;
		}
		if(SmartUtil.isBlankObject(checkingQueue)){
			checkingQueue.add(new CheckingModel(userId, companyId));
			return 0;
		}
		
		for(int index=0; index<checkingQueue.size(); index++){
			CheckingModel checkingModel = checkingQueue.get(index);
			if(checkingModel.getUserId().equals(userId)){
				System.out.println("CheckEmail is already Running !!!!, Model UserId=" + checkingModel.getUserId() + ", UserId=" + userId + ", Size=" + checkingQueue.size() +  ", Index=" + index);
				return -1;
			}
		}
		
		checkingQueue.add(new CheckingModel(userId, companyId));
		return checkingQueue.size() -1;
	}
	
	static void clearChecking(String userId, String companyId){
		if(SmartUtil.isBlankObject(userId) || SmartUtil.isBlankObject(companyId)){
			System.out.println("UserId or CompanyId does not exist Error!!!!, UserId=" + userId + ", CompanyId=" + companyId);
		}
		
		if(SmartUtil.isBlankObject(checkingQueue)){
			return;
		}
		
		while(true){
			boolean found = false;
			for(int index=0; index<checkingQueue.size(); index++){
				CheckingModel checkingModel = checkingQueue.get(index);
				if(checkingModel.getCompanyId().equals(companyId) && checkingModel.getUserId().equals(userId)){
					checkingQueue.remove(index);
					found = true;
					break;
				}
			}
			if(!found){
				break;
			}
		}
	}
	
	synchronized static void addThreadToChecking(int index, Thread thread){
		if( index<0 || thread==null || !(index < checkingQueue.size())) return;
		
		CheckingModel checkingModel = checkingQueue.get(index);
		checkingModel.setThread(thread);
		checkingQueue.set(index, checkingModel);
	}
	
	synchronized static CheckingModel getChecking(Thread thread){
		if(thread==null || SmartUtil.isBlankObject(checkingQueue))
			return null;
		
		for(int index=0; index<checkingQueue.size(); index++){
			CheckingModel checkingModel = checkingQueue.get(index);
			if(checkingModel.getThread() == thread){
				checkingQueue.remove(index);
				return checkingModel;
			}
		}
		return null;

	}
	
	static CheckingModel getModel(Thread thread){
		if(thread==null || SmartUtil.isBlankObject(checkingQueue))
			return null;
		
		for(int index=0; index<checkingQueue.size(); index++){
			CheckingModel checkingModel = checkingQueue.get(index);
			if(checkingModel.getThread() == thread)
				return checkingModel;
		}
		return null;

	}
	
	public void checkEmail() throws Exception {
		
		int index = -1;
		if((index = addChecking(SmartUtil.getCurrentUser().getId(), SmartUtil.getCurrentUser().getCompanyId())) == -1){
			System.out.println("CheckEmail already running, please try it again!! index=" + index + ", userId=" + SmartUtil.getCurrentUser().getId() + ", companyId=" + SmartUtil.getCurrentUser().getCompanyId());
			return;		
		}
		Thread checkingEmail = new Thread(new Runnable() {
			public void run() {
				System.out.println( auth.getEmailId() + " Start Checking Email : " + (new Date()));
				int newMessages = -1;
				
				CheckingModel thisModel = getModel(Thread.currentThread());
				System.out.println("UserId=" + thisModel.getUserId() + ", CompanyId=" + thisModel.getCompanyId() + ", Thread=" + thisModel.getThread() + " just started !!!");
				ProtocolFactory factory = new ProtocolFactory(profile, auth, handler);
				Protocol protocol = factory.getProtocol(null);

				try {

					// fetch all messages from the remote pop3 server
					protocol.disconnect();
					handler = protocol.connect(org.claros.commons.mail.utility.Constants.CONNECTION_READ_WRITE);
					Message[] msgs = protocol.fetchAllMessagesWithUid();
					ArrayList toBeDeleted = new ArrayList();
					if (msgs != null) {
						EmailHeader header = null;
						String[][] junkIds = SwServiceFactory.getInstance().getMailService().getJunkIds(thisModel.getUserId());

						for (int i=0;i<msgs.length && toBeDeleted.size()<MailAccount.MAX_MESSAGES_PER_FETCH;i++) {
							Message msg = msgs[i];
							int msgId = i+1;
							try {
								
								String uid = protocol.getMessageUID(msg);
								
								MailControllerFactory mailFact = new MailControllerFactory(auth, profile, handler, null);
								MailController mailCont = mailFact.getMailController();
								DbMailControllerImpl dbMailCont = (DbMailControllerImpl)mailCont;
								if (!SmartUtil.isBlankObject(uid) && !dbMailCont.msgAlreadyFetched(uid)) {
									header = protocol.fetchHeader(msg, msgId);
									msg = protocol.getMessage(msgId);
									if (!msg.getFolder().isOpen()) {
										msg.getFolder().open(Folder.READ_ONLY);
									}
							
									// find the destionation folderId for the message
									String folderId = findDestinationFolderId(msg);
							
									ByteArrayOutputStream bos = new ByteArrayOutputStream();
									ObjectOutputStream os = new ObjectOutputStream(bos);
									os.writeObject(header);
									byte bHeader[] = bos.toByteArray();
									String md5Header = MD5.getHashString(bHeader);
									// if message should be directly deleted it shouldn't be 
									// stored in DB.
									if (folderId != null) {
										// create a byte array from the message content. 
										bos = new ByteArrayOutputStream();
										msg.writeTo(bos);
										byte bMsg[] = bos.toByteArray();
							
										// serialize the message byte array
										os = new ObjectOutputStream(bos);
										os.writeObject(bMsg);

										// create an email db item
										MsgDbObject item = new MsgDbObject();
										item.setEmail(bos.toByteArray());
										item.setUniqueId(md5Header);
										item.setUid(uid);
										item.setFolderId(new Long(folderId));
										if(!SmartUtil.isBlankObject(junkIds) && !SmartUtil.isBlankObject(header.getFrom())){
											String senderId = ((InternetAddress)header.getFrom()[0]).getAddress();
											for(int j=0; j<junkIds[0].length; j++){
												if(junkIds[0][j].equals(senderId)){
													FolderControllerFactory folderFactory = new FolderControllerFactory(auth, profile, handler);
													FolderController fc = folderFactory.getFolderController();
													item.setFolderId(new Long(fc.getJunkFolder().getId()));
													break;
												}
											}
											for(int j=0; j<junkIds[1].length; j++){
												if(senderId.contains("@" + junkIds[1][j])){
													FolderControllerFactory folderFactory = new FolderControllerFactory(auth, profile, handler);
													FolderController fc = folderFactory.getFolderController();
													item.setFolderId(new Long(fc.getJunkFolder().getId()));
													break;
												}
											}
										}
										item.setUnread(new Boolean(true));
										item.setUsername(auth.getEmailId());
										item.setMsgSize(new Long(bMsg.length));
										
										item.setSender(header.getFromShown());
										item.setReceiver(header.getToShown());
										item.setCc(header.getCcShown());
										item.setBcc(header.getBccShown());
										item.setReplyTo(header.getReplyToShown());
										item.setMultipart(hasAttachment((MimeMessage)msg));
										if(SmartUtil.isBlankObject(header.getDate()))
											item.setSentDate(new LocalDate());
										else
											item.setSentDate(header.getDate());
										item.setPriority(new Integer(header.getPriority()));
										item.setSubject(header.getSubject());

										mailCont.appendEmail(item, thisModel.getCompanyId());
										msg = null;
										bMsg = null;
										item = null;
									}
									toBeDeleted.add(new Integer(msgId));
								}
							} catch (Exception e) {
								//toBeDeleted.add(new Integer(msgId));
								e.printStackTrace();
							}
						}
					}
				
					// fetched messages are deleted if the user requested so.					
					String deleteFetched = (auth.isDeleteAfterFetched()) ? "yes" : "no";
					if (deleteFetched != null && deleteFetched.equals("yes")) {
						if (toBeDeleted.size() > 0) {
							int ids[] = new int[toBeDeleted.size()];
							for (int i=0;i<toBeDeleted.size();i++) {
								Integer id = (Integer)toBeDeleted.get(i);
								ids[i] = id.intValue();
							}
							protocol.deleteMessages(ids);
						}
					}
					newMessages = toBeDeleted.size();
				}catch(Exception e){
				} finally {
				}
				protocol.disconnect();
				System.out.println(auth.getEmailId() + " End Checking Email : " + (new Date()));
				if(newMessages != -1)
					System.out.println(auth.getEmailId() + " " + newMessages +  " 개의 새로운 메시지 도착!!!");
				FolderControllerFactory fFactory = new FolderControllerFactory(auth, profile, handler);
				FolderController foldCont = fFactory.getFolderController();
				try{
					CheckingModel checkingEmail = getChecking(Thread.currentThread());
					int unreadMails = foldCont.countUnreadMessages(foldCont.getInboxFolder().getId().toString());
					SmartUtil.publishNoticeCount(checkingEmail.getUserId(), checkingEmail.getCompanyId(), new Notice(Notice.TYPE_MAILBOX, unreadMails));
					System.out.println(auth.getEmailId() + " Mailbox Notice Published [MAILBOX = " + unreadMails + " ]");					
				}catch(Exception e){
				}
			}
		});
		addThreadToChecking(index, checkingEmail);
		checkingEmail.start();
		System.out.println("New Thread=" + checkingEmail + " just started !!!");
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
}

class CheckingModel {
	CheckingModel(String userId, String companyId) {
		this.userId = userId;
		this.companyId = companyId;
		this.deleteAfterFetched = false;
	}
	
	protected Thread thread=null;
	protected String userId=null;
	protected String companyId=null;
	protected boolean deleteAfterFetched=false;
	public Thread getThread() {
		return thread;
	}
	public void setThread(Thread thread) {
		this.thread = thread;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public boolean isDeleteAfterFetched() {
		return deleteAfterFetched;
	}
	public void setDeleteAfterFetched(boolean deleteAfterFetched) {
		this.deleteAfterFetched = deleteAfterFetched;
	}	
}

