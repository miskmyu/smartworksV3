package org.claros.intouch.webmail.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.common.util.IdUtil;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.service.IDocFileService;
import net.smartworks.server.service.IWorkService;
import net.smartworks.util.SmartUtil;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.claros.commons.auth.models.AuthProfile;
import org.claros.commons.db.DbConfigList;
import org.claros.commons.exception.NoPermissionException;
import org.claros.commons.exception.SystemException;
import org.claros.commons.mail.exception.MailboxActionException;
import org.claros.commons.mail.models.ConnectionMetaHandler;
import org.claros.commons.mail.models.ConnectionProfile;
import org.claros.commons.mail.models.Email;
import org.claros.commons.mail.parser.MessageParser;
import org.claros.commons.mail.protocols.Protocol;
import org.claros.commons.mail.protocols.ProtocolFactory;
import org.claros.intouch.common.utility.Constants;
import org.claros.intouch.common.utility.Utility;
import org.claros.intouch.webmail.factory.FolderControllerFactory;
import org.claros.intouch.webmail.models.FolderDbObject;
import org.claros.intouch.webmail.models.MsgDbObject;
import org.claros.intouch.webmail.models.MsgDbUids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jenkov.mrpersister.impl.mapping.AutoGeneratedColumnsMapper;
import com.jenkov.mrpersister.itf.IGenericDao;
import com.jenkov.mrpersister.itf.mapping.IObjectMappingKey;
import com.jenkov.mrpersister.util.JdbcUtil;
/**
 * @author Umut Gokbayrak
 */
public class DbMailControllerImpl implements MailController {
	private static Log log = LogFactory.getLog(DbMailControllerImpl.class);
	private AuthProfile auth;
	private ConnectionProfile profile;
	private ConnectionMetaHandler handler;
	private String folder;

	@SuppressWarnings("unused")
	private DbMailControllerImpl() {
		super();
	}

	public DbMailControllerImpl(AuthProfile auth, ConnectionProfile profile, ConnectionMetaHandler handler, String folder) {
		this.auth = auth;
		this.profile = profile;
		this.handler = handler;
		this.folder = folder;
	}

	/* (non-Javadoc)
	 * @see org.claros.groupware.webmail.controllers.MailController#getEmailById(java.lang.Long)
	 */
	public Email getEmailById(Long emailId) throws Exception {
		Email email = null;
		MimeMessage msg = null;

		FolderControllerFactory fact = new FolderControllerFactory(auth, profile, handler);
		FolderController cont = fact.getFolderController();
		FolderDbObject foldObj = cont.getInboxFolder();
		if (false /*foldObj.getId().toString().equals(folder)*/) {
			// it is the INBOX
			ProtocolFactory pFact = new ProtocolFactory(profile, auth, handler);
			Protocol prot = pFact.getPop3();
			msg = (MimeMessage)prot.getMessage(emailId.intValue());
			try {
				email = MessageParser.parseMessage(msg);
				email.setMsgId(emailId);
			} catch (Exception e) {
				log.error("Message could not be parsed to an email object", e);
				throw new MailboxActionException(e);
			}
			if (email != null) {
				email.setMsgId(emailId);
			}
		} else {
			// it is fetched from the db
			MsgDbObject item = getEmailDbItemById(emailId);

			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props);
			ByteArrayInputStream bis = new ByteArrayInputStream(item.getEmail());
			msg = new MimeMessage(session, bis);
			try {
				email = MessageParser.parseMessage(msg);
				email.setMsgId(item.getId());
			} catch (Exception e) {
				log.error("Message could not be parsed to an email object", e);
				throw new MailboxActionException(e);
			} finally {
				bis.close();
			}
			if (email != null) {
				email.setMsgId(item.getId());
			}
			
			email.setReaders(item.getReader());
		}
		return email;
	}

	/**
	 * 
	 * @param auth
	 * @param emailId
	 * @return
	 * @throws Exception
	 */
	private MsgDbObject getEmailDbItemById(Long emailId) throws Exception {
		MsgDbObject email = null;
		IGenericDao dao = null;
		try {
			dao = Utility.getDbConnection();
			String username = auth.getEmailId();
			
			String sql = "SELECT * FROM MSG_DB_OBJECTS WHERE USERNAME=? AND ID = ?";
			email = (MsgDbObject)dao.read(MsgDbObject.class, sql, new Object[] {username, emailId});
		} finally {
			JdbcUtil.close(dao);
			dao = null;
		}
		if(SmartUtil.isMailFileName(email.getEmail())){
			String fileId = new String(email.getEmail());
	
			byte[] mailContent = SwManagerFactory.getInstance().getDocManager().readMailContent(fileId, email.getSentDate());
			email.setEmail(mailContent);
		}
		return email;
	}

	private MsgDbObject getEmailDbItem2ById(Long emailId) throws Exception {
		MsgDbObject email = null;
		IGenericDao dao = null;
		try {
			dao = Utility.getDbConnection();
			String username = auth.getEmailId();
			
			String sql = "SELECT * FROM MSG_DB_OBJECTS WHERE USERNAME=? AND ID = ?";
			email = (MsgDbObject)dao.read(MsgDbObject.class, sql, new Object[] {username, emailId});
		} finally {
			JdbcUtil.close(dao);
			dao = null;
		}
		return email;
	}


	/* (non-Javadoc)
	 * @see org.claros.groupware.webmail.controllers.MailController#deleteEmail(java.lang.Long)
	 */
	public void deleteEmail(Long emailId) throws Exception {
		FolderControllerFactory fact = new FolderControllerFactory(auth, profile, handler);
		FolderController cont = fact.getFolderController();
		FolderDbObject foldObj = cont.getInboxFolder();
		if (false /*foldObj.getId().toString().equals(folder)*/) {
			// it is the INBOX
			ProtocolFactory pFact = new ProtocolFactory(profile, auth, handler);
			Protocol prot = pFact.getPop3();
			prot.deleteMessages(new int[] {emailId.intValue()});
		} else {
			// it is DB
			MsgDbObject tmp = getEmailDbItem2ById(emailId);
			if (tmp != null) {
				String user = tmp.getUsername();
				if (!auth.getEmailId().equals(user)) {
					throw new NoPermissionException();
				}
			
				IGenericDao dao = null;
				try {
					dao = Utility.getDbConnection();
					dao.deleteByPrimaryKey(MsgDbObject.class, emailId);
				} catch (Exception e) {
					// do nothing sier
				} finally {
					JdbcUtil.close(dao);
					dao = null;
				}
				if(SmartUtil.isMailFileName(tmp.getEmail())){
					String fileId = new String(tmp.getEmail());
					SwManagerFactory.getInstance().getDocManager().deleteMailContent(fileId, tmp.getSentDate());
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.claros.groupware.webmail.controllers.MailController#moveEmail(java.lang.Long, java.lang.String)
	 */
	public void moveEmail(Long msgId, String destFolder) throws Exception {
		FolderControllerFactory fact = new FolderControllerFactory(auth, profile, handler);
		FolderController cont = fact.getFolderController();
		FolderDbObject foldObj = cont.getInboxFolder();

		// message can not be moved to the INBOX
		if (foldObj.getId().toString().equals(destFolder)) {
			throw new SystemException();
		} 
		
		if (false /*foldObj.getId().toString().equals(folder)*/) {
			// it is the INBOX
			ProtocolFactory pFact = new ProtocolFactory(profile, auth, handler);
			Protocol prot = pFact.getPop3();
			Message msg = prot.getMessage(msgId.intValue());
			
			try {
				if (!msg.getFolder().isOpen()) {
					msg.getFolder().open(Folder.READ_WRITE);
				}
				// create a byte array from the message content. 
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				msg.writeTo(bos);
				byte bMsg[] = bos.toByteArray();
				bos.close();
				
				// serialize the message byte array
				ObjectOutputStream os = new ObjectOutputStream(bos);
				os.writeObject(bMsg);

				// create an email db item
				MsgDbObject item = new MsgDbObject();
				item.setEmail(bos.toByteArray());
				item.setUniqueId(null);
				item.setFolderId(new Long(destFolder));
				item.setUnread(new Boolean(true));
				item.setUsername(auth.getUsername());
				item.setMsgSize(new Long(bMsg.length));

				// save the email db item.
				appendEmail(item, null);
				prot.deleteMessages(new int[] {msgId.intValue()});
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// it is db - db move
			MsgDbObject item = getEmailDbItemById(msgId);
			Long destId = new Long(destFolder);
			item.setFolderId(destId);
			updateEmail(item);
		}
	}

	/**
	 * 
	 * @param item
	 * @throws Exception
	 */
	private void updateEmail(MsgDbObject item) throws Exception {
		MsgDbObject tmp = getEmailDbItemById(item.getId());
		String user = tmp.getUsername();
		if (!auth.getEmailId().equals(user)) {
			throw new NoPermissionException();
		}

		IGenericDao dao = null;
		try {
			dao = Utility.getDbConnection();
			String sql = "UPDATE MSG_DB_OBJECTS SET FOLDER_ID = " + item.getFolderId() + " WHERE ID = " + item.getId();
			dao.executeUpdate(sql);
			//dao.update(MsgDbObject.class, item);
		} finally {
			JdbcUtil.close(dao);
			dao = null;
		}
	}

	/* (non-Javadoc)
	 * @see org.claros.groupware.webmail.controllers.MailController#appendEmail(org.claros.groupware.webmail.models.EmailDbItem)
	 */
	@SuppressWarnings("deprecation")
	public void appendEmail(MsgDbObject item, String companyId) throws Exception {
		IGenericDao dao = null;

		if(SmartUtil.isBlankObject(companyId)) companyId = SmartUtil.getCurrentUser().getCompanyId();
		byte[] mailContent = item.getEmail();
		String mailId = SwManagerFactory.getInstance().getDocManager().createMailContent(companyId, auth.getEmailId(), mailContent, item.getSentDate());
		item.setEmail(mailId.getBytes());
		try {
			dao = Utility.getDbConnection();
			IObjectMappingKey myObj = Constants.persistMan.getObjectMappingFactory().createInstance(MsgDbObject.class, new AutoGeneratedColumnsMapper(true));
			if (CommonUtil.isEmpty(item.getUid()))
				item.setUid(CommonUtil.newId());
			dao.insert(myObj, item);

			if(!auth.isDeleteAfterFetched()){
				String sql = "INSERT INTO MSG_DB_UIDS (USERNAME, UID) VALUES ('" + item.getUsername() + "', '" + item.getUid() + "')";
				dao.executeUpdate(sql);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(dao);
			dao = null;
			item.setEmail(mailContent);
		}
	}

	/* (non-Javadoc)
	 * @see org.claros.groupware.webmail.controllers.MailController#markAsRead(java.lang.Long)
	 */
	public void markAsRead(Long msgId) throws Exception {
		FolderControllerFactory fact = new FolderControllerFactory(auth, profile, handler);
		FolderController cont = fact.getFolderController();
		FolderDbObject foldObj = cont.getInboxFolder();
		if (false /*foldObj.getId().toString().equals(folder)*/) {
			// it is the INBOX
			// do nothing
		} else {
			QueryRunner run = new QueryRunner(DbConfigList.getDataSourceById("file"));
			String username = auth.getEmailId();
			try {
				String sql = "UPDATE MSG_DB_OBJECTS SET UNREAD = ? WHERE USERNAME=? AND ID=?";
				run.update(sql, new Object[] {new Integer(0), username, msgId});
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	public void appendMailReader(String userId, String messageId) throws Exception {
		if(SmartUtil.isBlankObject(userId) || SmartUtil.isBlankObject(messageId)) return;
		IGenericDao dao = null;
		FolderControllerFactory fact = new FolderControllerFactory(auth, profile, handler);
		FolderController cont = fact.getFolderController();
		FolderDbObject foldObj = cont.getInboxFolder();
		if (false /*foldObj.getId().toString().equals(folder)*/) {
			// it is the INBOX
			// do nothing
		} else {
			dao = Utility.getDbConnection();
			QueryRunner run = new QueryRunner(DbConfigList.getDataSourceById("file"));
			String username = auth.getEmailId();
			try {
				String sql = "SELECT READER FROM MSG_DB_OBJECTS WHERE USERNAME=? AND MESSAGEID = ?";
				String reader = (String)dao.read(MsgDbObject.class, sql, new Object[] {userId, messageId});

				if(SmartUtil.isBlankObject(reader))
					reader = org.claros.commons.mail.utility.Utility.userToString(SmartUtil.getCurrentUser());
				else if(reader.contains(username)) 
					return;
				else
					reader = reader + ", " + org.claros.commons.mail.utility.Utility.userToString(SmartUtil.getCurrentUser());
				
				sql = "UPDATE MSG_DB_OBJECTS SET READER = ? WHERE USERNAME=? AND MESSAGEID=?";
				run.update(sql, new Object[] {reader, username, messageId});
			} catch (SQLException e) {
				throw e;
			} finally {
				JdbcUtil.close(dao);
				dao = null;
			}
		}
	}

	/**
	 * 
	 * @param auth
	 * @param md5Header
	 * @return
	 * @throws Exception
	 */
	public boolean mailAlreadyFetched(String md5Header) throws Exception {
		IGenericDao dao = null;
		boolean result = true;
		try {
			dao = Utility.getDbConnection();
			String username = auth.getEmailId();
			
			String sql = "SELECT unique_id FROM MSG_DB_OBJECTS WHERE USERNAME=? AND UNIQUE_ID = ?";
			MsgDbObject email = (MsgDbObject)dao.read(MsgDbObject.class, sql, new Object[] {username, md5Header});
			if (email == null) {
				JdbcUtil.close(dao);
				dao = null;
				result = false;
			}
		} finally {
			JdbcUtil.close(dao);
			dao = null;
		}
		return result;
	}

	public boolean msgAlreadyFetched(String uid) throws Exception {
		IGenericDao dao = null;
		boolean result = true;
		try {
			dao = Utility.getDbConnection();
			String username = auth.getEmailId();
			
			String sql = "SELECT * FROM MSG_DB_UIDS WHERE USERNAME=? AND UID = ?";
			MsgDbUids msgUID = (MsgDbUids)dao.read(MsgDbUids.class, sql, new Object[] {username, uid});
			if (msgUID == null || !uid.equals(msgUID.getUid())) {
				JdbcUtil.close(dao);
				dao = null;
				result = false;
			}

		}catch (Exception e){
			e.printStackTrace();
		} finally {
			JdbcUtil.close(dao);
			dao = null;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.claros.groupware.webmail.controllers.MailController#deleteEmails(int)
	 */
	public void deleteEmails(int msgs[]) throws Exception {
		FolderControllerFactory fact = new FolderControllerFactory(auth, profile, handler);
		FolderController cont = fact.getFolderController();
		FolderDbObject foldObj = cont.getInboxFolder();
		if (false /*foldObj.getId().toString().equals(folder)*/) {
			// it is the INBOX
			ProtocolFactory pFact = new ProtocolFactory(profile, auth, handler);
			Protocol prot = pFact.getPop3();
			prot.deleteMessages(msgs);
		} else {
			for (int i=0; i<msgs.length; i++) {
				deleteEmail(new Long(msgs[i]));
			}
		}
	}

	/**
	 * 
	 */
	public void moveEmails(int[] msgs, String destFolder) throws Exception {
		FolderControllerFactory fact = new FolderControllerFactory(auth, profile, handler);
		FolderController cont = fact.getFolderController();
		//2012.08.24 임시저장함으로 변경
		FolderDbObject foldObj = cont.getDraftsFolder();
		//jy.bae 
		// message can not be moved to the INBOX
		if (foldObj.getId().toString().equals(destFolder)) {
			throw new SystemException();
		} 
		
		if (false /*foldObj.getId().toString().equals(folder)*/) {
			// it is the INBOX
			ProtocolFactory pFact = new ProtocolFactory(profile, auth, handler);
			Protocol prot = pFact.getPop3();
			for (int i=0;i<msgs.length;i++) {
				Message msg = prot.getMessage(msgs[i]);
				
				try {
					if (!msg.getFolder().isOpen()) {
						msg.getFolder().open(Folder.READ_WRITE);
					}
					// create a byte array from the message content. 
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					msg.writeTo(bos);
					byte bMsg[] = bos.toByteArray();
					bos.close();

					// serialize the message byte array
					ObjectOutputStream os = new ObjectOutputStream(bos);
					os.writeObject(bMsg);

					// create an email db item
					MsgDbObject item = new MsgDbObject();
					item.setEmail(bos.toByteArray());
					item.setUniqueId("");
					item.setFolderId(new Long(destFolder));
					item.setUnread(new Boolean(true));
					item.setUsername(auth.getUsername());
					item.setMsgSize(new Long(bMsg.length));

					// save the email db item.
					appendEmail(item, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			prot.deleteMessages(msgs);

		} else {
			MsgDbObject tmp = null;
			if (msgs != null) {
				for (int i=0;i<msgs.length;i++) {
					tmp = getEmailDbItemById(new Long(msgs[i]));
					Long destId = new Long(destFolder);
					tmp.setFolderId(destId);
					updateEmail(tmp);
				}
			}
		}
	}

	/**
	 * 
	 */
	public void markAsDeleted(int[] ids) throws Exception {
		deleteEmails(ids);
	}
}
