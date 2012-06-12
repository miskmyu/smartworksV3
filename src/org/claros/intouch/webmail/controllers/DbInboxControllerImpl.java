package org.claros.intouch.webmail.controllers;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.Folder;
import javax.mail.Message;

import net.smartworks.model.notice.Notice;
import net.smartworks.util.SmartUtil;

import org.claros.commons.auth.models.AuthProfile;
import org.claros.commons.mail.models.ConnectionMetaHandler;
import org.claros.commons.mail.models.ConnectionProfile;
import org.claros.commons.mail.models.EmailHeader;
import org.claros.commons.mail.protocols.Protocol;
import org.claros.commons.mail.protocols.ProtocolFactory;
import org.claros.commons.mail.utility.Constants;
import org.claros.commons.utility.MD5;
import org.claros.intouch.webmail.factory.FolderControllerFactory;
import org.claros.intouch.webmail.factory.MailControllerFactory;
import org.claros.intouch.webmail.models.MsgDbObject;
import org.cometd.bayeux.client.ClientSession;
import org.cometd.client.BayeuxClient;
import org.cometd.client.transport.ClientTransport;
import org.cometd.client.transport.LongPollingTransport;
import org.eclipse.jetty.client.HttpClient;

import com.sun.mail.pop3.POP3Folder;

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
	
	static Thread checkingEmail = null;
	static String currentUserId = null;
	public void checkEmailWithHeader() throws Exception {
		
		currentUserId = SmartUtil.getCurrentUser().getId();
		checkingEmail = new Thread(new Runnable() {
			public void run() {
				System.out.println(" Start Checking Email : " + (new Date()));
				int newMessages = -1;
				
				ProtocolFactory factory = new ProtocolFactory(profile, auth, handler);
				Protocol protocol = factory.getProtocol(null);
				try {
					// fetch all messages from the remote pop3 server
					protocol.disconnect();
					handler = protocol.connect(org.claros.commons.mail.utility.Constants.CONNECTION_READ_WRITE);
					
					ArrayList headers = protocol.fetchAllHeaders();
					ArrayList toBeDeleted = new ArrayList();
					if (headers != null) {
						EmailHeader header = null;
						for (int i=0;i<headers.size();i++) {
							header = (EmailHeader)headers.get(i);
							int msgId = header.getMessageId();
							try {
								ByteArrayOutputStream bos = new ByteArrayOutputStream();
								ObjectOutputStream os = new ObjectOutputStream(bos);
								os.writeObject(header);
								byte bHeader[] = bos.toByteArray();
								String md5Header = MD5.getHashString(bHeader);

								MailControllerFactory mailFact = new MailControllerFactory(auth, profile, handler, null);
								MailController mailCont = mailFact.getMailController();
								DbMailControllerImpl dbMailCont = (DbMailControllerImpl)mailCont;
								if (!dbMailCont.mailAlreadyFetched(md5Header)) {
									Message msg = protocol.getMessage(msgId);
									if (!msg.getFolder().isOpen()) {
										msg.getFolder().open(Folder.READ_ONLY);
									}
							
									// find the destionation folderId for the message
									String folderId = findDestinationFolderId(msg);
							
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
										item.setFolderId(new Long(folderId));
										item.setUnread(new Boolean(true));
										item.setUsername(auth.getUsername());
										item.setMsgSize(new Long(bMsg.length));
										
										item.setSender(header.getFromShown());
										item.setReceiver(header.getToShown());
										item.setCc(header.getCcShown());
										item.setBcc(header.getBccShown());
										item.setReplyTo(header.getReplyToShown());
//										item.setMultipart(header.isMultipart());
										item.setSentDate(header.getDate());
//										item.setPriority(new Integer(header.getPriority()));
										item.setSubject(header.getSubject());

										// save the email db item.
										mailCont.appendEmail(item);
									}
									toBeDeleted.add(new Integer(msgId));
								}else{
									Message msg = protocol.getMessage(msgId);									
								}
							} catch (Exception e) {
								toBeDeleted.add(new Integer(msgId));
							}
						}
					}
				
					// fetched messages are deleted if the user requested so.
//					String deleteFetched = UserPrefsController.getUserSetting(auth, UserPrefConstants.deleteFetched);
					String deleteFetched = "no";
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
					checkingEmail = null;
				}catch(Exception e){
				} finally {
				}
				protocol.disconnect();
				System.out.println(" End Checking Email : " + (new Date()));
				if(newMessages != -1)
					System.out.println("" + newMessages +  " 개의 새로운 메시지 도착!!!");
				FolderControllerFactory fFactory = new FolderControllerFactory(auth, profile, handler);
				FolderController foldCont = fFactory.getFolderController();
				try{
					int unreadMails = foldCont.countUnreadMessages(foldCont.getInboxFolder().getId().toString());
					SmartUtil.publishNoticeCount(currentUserId, new Notice(Notice.TYPE_MAILBOX, unreadMails));
					System.out.println(" Mailbox Notice Published [MAILBOX = " + unreadMails + " ]");					
				}catch(Exception e){
				}
				currentUserId = null;
				checkingEmail = null;
			}
		});
		checkingEmail.start();		
	}
	public void checkEmail() throws Exception {
		
		currentUserId = SmartUtil.getCurrentUser().getId();
		checkingEmail = new Thread(new Runnable() {
			public void run() {
				System.out.println(" Start Checking Email : " + (new Date()));
				int newMessages = -1;
				
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
						for (int i=0;i<msgs.length;i++) {
							Message msg = msgs[i];
							int msgId = i+1;
							try {
								
								String uid = protocol.getMessageUID(msg);
								
								MailControllerFactory mailFact = new MailControllerFactory(auth, profile, handler, null);
								MailController mailCont = mailFact.getMailController();
								DbMailControllerImpl dbMailCont = (DbMailControllerImpl)mailCont;
								if (!dbMailCont.msgAlreadyFetched(uid)) {
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
										item.setUnread(new Boolean(true));
										item.setUsername(auth.getUsername());
										item.setMsgSize(new Long(bMsg.length));
										
										item.setSender(header.getFromShown());
										item.setReceiver(header.getToShown());
										item.setCc(header.getCcShown());
										item.setBcc(header.getBccShown());
										item.setReplyTo(header.getReplyToShown());
										item.setMultipart(header.isMultipart());
										item.setSentDate(header.getDate());
										item.setPriority(new Integer(header.getPriority()));
										item.setSubject(header.getSubject());

										// save the email db item.
										mailCont.appendEmail(item);
									}
									toBeDeleted.add(new Integer(msgId));
								}
							} catch (Exception e) {
								toBeDeleted.add(new Integer(msgId));
							}
						}
					}
				
					// fetched messages are deleted if the user requested so.
//					String deleteFetched = UserPrefsController.getUserSetting(auth, UserPrefConstants.deleteFetched);
					String deleteFetched = "no";
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
					checkingEmail = null;
				}catch(Exception e){
				} finally {
				}
				protocol.disconnect();
				System.out.println(" End Checking Email : " + (new Date()));
				if(newMessages != -1)
					System.out.println("" + newMessages +  " 개의 새로운 메시지 도착!!!");
				FolderControllerFactory fFactory = new FolderControllerFactory(auth, profile, handler);
				FolderController foldCont = fFactory.getFolderController();
				try{
					int unreadMails = foldCont.countUnreadMessages(foldCont.getInboxFolder().getId().toString());
					SmartUtil.publishNoticeCount(currentUserId, new Notice(Notice.TYPE_MAILBOX, unreadMails));
					System.out.println(" Mailbox Notice Published [MAILBOX = " + unreadMails + " ]");					
				}catch(Exception e){
				}
				currentUserId = null;
				checkingEmail = null;
			}
		});
		checkingEmail.start();		
	}

}
