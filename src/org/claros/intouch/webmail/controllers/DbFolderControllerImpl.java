package org.claros.intouch.webmail.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.mail.Session;

import net.smartworks.model.mail.MailFolder;
import net.smartworks.util.SmartUtil;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.claros.commons.auth.models.AuthProfile;
import org.claros.commons.db.DbConfigList;
import org.claros.commons.exception.NoPermissionException;
import org.claros.commons.exception.SystemException;
import org.claros.commons.mail.models.ConnectionMetaHandler;
import org.claros.commons.mail.models.ConnectionProfile;
import org.claros.commons.mail.models.EmailHeader;
import org.claros.commons.mail.protocols.Protocol;
import org.claros.commons.mail.protocols.ProtocolFactory;
import org.claros.commons.utility.Formatter;
import org.claros.intouch.common.utility.Constants;
import org.claros.intouch.common.utility.Utility;
import org.claros.intouch.webmail.models.MsgDbObject;
import org.claros.intouch.webmail.models.FolderDbObject;
import org.claros.intouch.webmail.models.FolderDbObjectWrapper;

import com.jenkov.mrpersister.impl.mapping.AutoGeneratedColumnsMapper;
import com.jenkov.mrpersister.itf.IGenericDao;
import com.jenkov.mrpersister.itf.mapping.IObjectMappingKey;
import com.jenkov.mrpersister.util.JdbcUtil;

/**
 * @author Umut Gokbayrak
 */
public class DbFolderControllerImpl implements FolderController {
	private static Log log = LogFactory.getLog(DbFolderControllerImpl.class);
	private AuthProfile auth;
	private ConnectionProfile profile;
	private ConnectionMetaHandler handler;
	
	/**
	 * @param auth
	 */
	public DbFolderControllerImpl(AuthProfile auth, ConnectionProfile profile, ConnectionMetaHandler handler) {
		this.auth = auth;
		this.profile = profile;
		this.handler = handler;
	}

	/**
	 * used to disable it.
	 *
	 */
	@SuppressWarnings("unused")
	private DbFolderControllerImpl() {
		super();
	}

	/**
	 * only meaningful for inbox, sent, junk, trash
	 * @param auth
	 * @return
	 */
	private FolderDbObject getSpecialFolderByType(Integer folderType) throws Exception {
		IGenericDao dao = null;
		FolderDbObject folder = null;
		try {
			dao = Utility.getDbConnection();
			String username = auth.getEmailId();
			
			String sql = "SELECT * FROM FOLDER_DB_OBJECTS WHERE USERNAME=? AND FOLDER_TYPE = ?"; 
			folder = (FolderDbObject)dao.read(FolderDbObject.class, sql, new Object[] {username, folderType});
		}catch (Exception e){
			e.printStackTrace();
		} finally { 
			JdbcUtil.close(dao);
			dao = null;
		}
		return folder;
	}

	/**
	 * @param auth
	 * @return
	 */
	public FolderDbObject getJunkFolder() throws Exception {
		return getSpecialFolderByType(Constants.FOLDER_TYPE_JUNK);
	}

	/**
	 * @param auth
	 * @return
	 */
	public FolderDbObject getInboxFolder() throws Exception {
		return getSpecialFolderByType(Constants.FOLDER_TYPE_INBOX);
	}

	/**
	 * @param auth
	 * @return
	 */
	public FolderDbObject getSentItems() throws Exception {
		return getSpecialFolderByType(Constants.FOLDER_TYPE_SENT);
	}

	/**
	 * @param auth
	 * @return
	 */
	public FolderDbObject getTrashFolder() throws Exception {
		return getSpecialFolderByType(Constants.FOLDER_TYPE_TRASH);
	}

	/**
	 * @param auth
	 * @return
	 */
	public FolderDbObject getDraftsFolder() throws Exception {
		return getSpecialFolderByType(Constants.FOLDER_TYPE_DRAFTS);
	}

	/* (non-Javadoc)
	 * @see org.claros.groupware.webmail.controllers.FolderController#getFolders(org.claros.commons.models.AuthProfile)
	 */
	public List getFolders() throws Exception {
		IGenericDao dao = null;
		ArrayList myList = null;
		try {
			dao = Utility.getDbConnection();
			String username = auth.getEmailId();
		
			String sql = "SELECT * FROM FOLDER_DB_OBJECTS WHERE USERNAME=? ORDER BY PARENT_ID ASC, FOLDER_TYPE ASC, FOLDER_NAME ASC";
			List folders = dao.readList(FolderDbObject.class, sql, new Object[] {username});
		
			myList = new ArrayList();
			if (folders != null) {
				FolderDbObject tmp = null;
				for (int i=0;i<folders.size();i++) {
					tmp = (FolderDbObject)folders.get(i);
					FolderDbObjectWrapper fd = new FolderDbObjectWrapper(tmp);
					try {
						fd.setUnreadItemCount(countUnreadMessages(tmp.getId().toString()));
						fd.setTotalItemCount(countTotalMessages(tmp.getId().toString()));
					} catch (Exception f) {
						log.debug("unable to fetch unread/total count for folder");
					}
					myList.add(fd);
				}
			}
		} finally {
			JdbcUtil.close(dao);
			dao = null;
		}
		return myList;
	}

	public List getFolders(String parentId) throws Exception {
		IGenericDao dao = null;
		ArrayList myList = null;
		try {
			dao = Utility.getDbConnection();
			String username = auth.getEmailId();
			Long lParent = (SmartUtil.isBlankObject(parentId)) ? new Long(0) : new Long(parentId);
		
			String sql = "SELECT * FROM FOLDER_DB_OBJECTS WHERE USERNAME=? AND PARENT_ID = ? ORDER BY FOLDER_NAME ASC";
			List folders = dao.readList(FolderDbObject.class, sql, new Object[] {username, lParent});
		
			myList = new ArrayList();
			if (folders != null) {
				FolderDbObject tmp = null;
				for (int i=0;i<folders.size();i++) {
					tmp = (FolderDbObject)folders.get(i);
					FolderDbObjectWrapper fd = new FolderDbObjectWrapper(tmp);
					try {
						fd.setUnreadItemCount(countUnreadMessages(tmp.getId().toString()));
						fd.setTotalItemCount(countTotalMessages(tmp.getId().toString()));
					} catch (Exception f) {
						log.debug("unable to fetch unread/total count for folder");
					}
					myList.add(fd);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		} finally {
			JdbcUtil.close(dao);
			dao = null;
		}
		return myList;
	}

	/* (non-Javadoc)
	 * @see org.claros.groupware.webmail.controllers.FolderController#getFolder(org.claros.commons.models.AuthProfile, java.lang.String)
	 */
	public FolderDbObject getFolder(String folder) throws Exception {
		IGenericDao dao = null;
		FolderDbObject fld = null;
		try {
			Long lFolder = new Long(folder);
			
			dao = Utility.getDbConnection();
			String username = auth.getEmailId();
			
			String sql = "SELECT * FROM FOLDER_DB_OBJECTS WHERE USERNAME=? AND ID = ?";
			fld = (FolderDbObject)dao.read(FolderDbObject.class, sql, new Object[] {username, lFolder});
		}catch (Exception e){
			e.printStackTrace();
		} finally {
			JdbcUtil.close(dao);
			dao = null;
		}
		return fld;
	}

	public FolderDbObject getFolderByName(String parentId, String folderName) throws Exception {
		IGenericDao dao = null;
		FolderDbObject fld = null;
		
		Long lFolder = (SmartUtil.isBlankObject(parentId)) ? 0 : new Long(parentId);		
		try {			
			dao = Utility.getDbConnection();
			String username = auth.getEmailId();
			
			String sql = "SELECT * FROM FOLDER_DB_OBJECTS WHERE USERNAME=? AND PARENT_ID = ? AND FOLDER_NAME = ?";
			fld = (FolderDbObject)dao.read(FolderDbObject.class, sql, new Object[] {username, lFolder, folderName});
		}catch (Exception e){
			e.printStackTrace();
		} finally {
			JdbcUtil.close(dao);
			dao = null;
		}
		return fld;
	}

	/* (non-Javadoc)
	 * @see org.claros.groupware.webmail.controllers.FolderController#getFolder(org.claros.commons.models.AuthProfile, java.lang.String)
	 */
	public FolderDbObjectWrapper getFolderById(String folderId) throws Exception {
		IGenericDao dao = null;
		FolderDbObject fld = null;
		FolderDbObjectWrapper fd = null;
		try {
			Long lFolder = new Long(folderId);
			
			dao = Utility.getDbConnection();
			String username = auth.getEmailId();
			
			String sql = "SELECT * FROM FOLDER_DB_OBJECTS WHERE USERNAME=? AND ID = ?";
			fld = (FolderDbObject)dao.read(FolderDbObject.class, sql, new Object[] {username, lFolder});
			fd = new FolderDbObjectWrapper(fld);
			try {
				fd.setUnreadItemCount(countUnreadMessages(fld.getId().toString()));
				fd.setTotalItemCount(countTotalMessages(fld.getId().toString()));
			} catch (Exception f) {
				log.debug("unable to fetch unread/total count for folder");
			}
		} finally {
			JdbcUtil.close(dao);
			dao = null;
		}
		return fd;
	}

	/* (non-Javadoc)
	 * @see org.claros.groupware.webmail.controllers.FolderController#getMailsByFolder(org.claros.commons.models.AuthProfile, java.lang.String)
	 */
	public List getMailsByFolder(String folder) throws Exception {
		IGenericDao dao = null;
		List msgs = null;
		try {
			Long folderId = new Long(folder);
			dao = Utility.getDbConnection();
			String username = auth.getEmailId();
			
			String sql = "SELECT * FROM MSG_DB_OBJECTS WHERE USERNAME=? AND FOLDER_ID = ?";
			msgs = dao.readList(MsgDbObject.class, sql, new Object[] {username, folderId});
		} finally {
			JdbcUtil.close(dao);
			dao = null;
		}
		return msgs;
	}

	/* (non-Javadoc)
	 * @see org.claros.groupware.webmail.controllers.FolderController#createFolder(org.claros.groupware.webmail.models.FolderDbItem)
	 */
	@SuppressWarnings("deprecation")
	public void createFolder(FolderDbObject item) throws Exception {
		IGenericDao dao = null;
		try {
			dao = Utility.getDbConnection();
			IObjectMappingKey myObj = org.claros.intouch.common.utility.Constants.persistMan.getObjectMappingFactory().createInstance(FolderDbObject.class, new AutoGeneratedColumnsMapper(true));
			dao.insert(myObj, item);
		} finally {
			JdbcUtil.close(dao);
			dao = null;
		}
	}

	/* (non-Javadoc)
	 * @see org.claros.groupware.webmail.controllers.FolderController#countUnreadMessages(org.claros.commons.models.AuthProfile, java.lang.Long)
	 */
	public Integer countUnreadMessages(String folder) throws Exception {
		FolderDbObject foldObj = getInboxFolder();
		if (false /*foldObj.getId().toString().equals(folder)*/) {
			// it is the INBOX
			ProtocolFactory pFact = new ProtocolFactory(profile, auth, handler);
			Protocol prot = pFact.getPop3();
			return new Integer(prot.getTotalMessageCount());
		} else {
				
			QueryRunner run = new QueryRunner(DbConfigList.getDataSourceById("file"));
			HashMap result = null;
			String username = auth.getEmailId();
			try {
				Long folderId = new Long(folder);
				String sql = "SELECT COUNT(*) AS NUMBER FROM MSG_DB_OBJECTS WHERE USERNAME=? AND FOLDER_ID = ? AND UNREAD = ?";
				result = (HashMap)run.query(sql, new Object[] {username, folderId, 1}, new MapHandler());
			} catch (SQLException e) {
				return new Integer(0);
			}
			if (result != null) {
				return new Integer(result.get("number").toString());
			}
		}
		return new Integer(0);
	}

	/* (non-Javadoc)
	 * @see org.claros.groupware.webmail.controllers.FolderController#countTotalMessages(org.claros.commons.models.AuthProfile, java.lang.String)
	 */
	public Integer countTotalMessages(String folder) throws Exception {
		FolderDbObject foldObj = getInboxFolder();
		if (false /*foldObj.getId().toString().equals(folder)*/) {
			// it is the INBOX
			ProtocolFactory pFact = new ProtocolFactory(profile, auth, handler);
			Protocol prot = pFact.getPop3();
			return new Integer(prot.getTotalMessageCount());
		} else {
			// it is a database folder
			QueryRunner run = new QueryRunner(DbConfigList.getDataSourceById("file"));
			HashMap result = null;
			String username = auth.getEmailId();
			try {
				Long folderId = new Long(folder);
				String sql = "SELECT COUNT(*) AS NUMBER FROM MSG_DB_OBJECTS WHERE USERNAME=? AND FOLDER_ID = ? ";
				result = (HashMap)run.query(sql, new Object[] {username, folderId}, new MapHandler());
			} catch (SQLException e) {
				return new Integer(0);
			}
			if (result != null) {
				return new Integer(result.get("number").toString());
			}
		}
		return new Integer(0);
	}

	/* (non-Javadoc)
	 * @see org.claros.groupware.webmail.controllers.FolderController#emptyFolder(org.claros.commons.models.AuthProfile, java.lang.String)
	 */
	public void emptyFolder(String folder) throws Exception {
		FolderDbObject foldObj = getInboxFolder();
		if (false /*foldObj.getId().toString().equals(folder)*/) {
			// it is the INBOX
			ProtocolFactory pFact = new ProtocolFactory(profile, auth, handler);
			Protocol prot = pFact.getPop3();
			prot.emptyFolder();
		} else {
			// it is a database folder
			QueryRunner run = new QueryRunner(DbConfigList.getDataSourceById("file"));
			String username = auth.getEmailId();
			try {
				Long folderId = new Long(folder);
				String sql = "DELETE FROM MSG_DB_OBJECTS WHERE USERNAME=? AND FOLDER_ID=?";
				run.update(sql, new Object[] {username, folderId});
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.claros.groupware.webmail.controllers.FolderController#deleteFolder(org.claros.commons.models.AuthProfile, java.lang.String)
	 */
	public void deleteFolder(String folder) throws Exception {
		FolderDbObject foldObj = getInboxFolder();
		if (foldObj.getId().toString().equals(folder)) {
			// it is inbox and you can not delete INBOXin pop3 mode
			throw new SystemException();
		}
		
		IGenericDao dao = null;
		try {
			dao = org.claros.intouch.common.utility.Utility.getTxnDbConnection();
			String username = auth.getEmailId();
			
			Long folderId = new Long(folder);
			FolderDbObject fld = getFolder(folder);
			if (!fld.getUsername().equals(auth.getEmailId())) {
				throw new NoPermissionException();
			}

			String sql = "DELETE FROM MSG_DB_OBJECTS WHERE USERNAME=? AND FOLDER_ID = ?";
			try{
				// delete the emails under folder
				dao.executeUpdate(sql, new Object[] {username, folderId});
			}catch(Exception e){
				e.printStackTrace();
			}

			try{
				sql = "DELETE FROM MSG_RULES WHERE USERNAME=? AND DESTINATION = ?";
				// delete the filters targeting the deleted folder
				dao.executeUpdate(sql, new Object[] {username, folderId});
			}catch(Exception e){
				e.printStackTrace();
			}

			// delete the folder
			dao.deleteByPrimaryKey(FolderDbObject.class, folderId);
			dao.commit();

		} catch (Exception e) {
//			dao.rollback();
			throw e;
		} finally {
			JdbcUtil.close(dao);
			dao = null;
		}
	}

	/* (non-Javadoc)
	 * @see org.claros.groupware.webmail.controllers.FolderController#getHeadersByFolder(org.claros.commons.models.AuthProfile, java.lang.String)
	 */
	public ArrayList getHeadersByFolder(String folder) throws Exception {
		ArrayList headers = new ArrayList();
		FolderDbObject foldObj = getInboxFolder();
		List mails = null;
		
		if (false /*org.claros.commons.mail.utility.Constants.FOLDER_INBOX(null).equals(folder)*/) {
			// it is inbox so we fetch e-mail on the fly
			ProtocolFactory factory = new ProtocolFactory(profile, auth, handler);
			Protocol protocol = factory.getProtocol(folder);
			// protocol.disconnect();
			// this.handler = protocol.connect(org.claros.commons.mail.utility.Constants.CONNECTION_READ_WRITE);

			mails = protocol.fetchAllHeaders();
			if (mails != null) {
				EmailHeader item = null;
				EmailHeader header = null;

				for (int i=0;i<mails.size();i++) {
					item = (EmailHeader)mails.get(i);
					
					try {
						header = new EmailHeader();

						header.setMultipart((item.isMultipart()) ? true : false);
						header.setMessageId(item.getMessageId());
						header.setFrom(item.getFrom());
						header.setTo(item.getTo());
						header.setCc(item.getCc());
						header.setBcc(item.getBcc());
						header.setDate(item.getDate());
						header.setReplyTo(item.getReplyTo());
						header.setSize(item.getSize());
						header.setSubject(org.claros.commons.utility.Utility.updateTRChars(item.getSubject()));
						header.setUnread(Boolean.valueOf(false));

						// now set the human readables.
						header.setDateShown(Formatter.formatDate(header.getDate(), "dd.MM.yyyy HH:mm"));
						header.setFromShown(org.claros.commons.utility.Utility.updateTRChars(org.claros.commons.mail.utility.Utility.addressArrToString(header.getFrom())));
						header.setToShown(org.claros.commons.mail.utility.Utility.addressArrToString(header.getTo()));
						header.setCcShown(org.claros.commons.mail.utility.Utility.addressArrToString(header.getCc()));
						header.setSizeShown(org.claros.commons.mail.utility.Utility.sizeToHumanReadable(header.getSize()));
	                
						// it is time to add it to the arraylist
						headers.add(header);
					} catch (Exception e1) {
						log.error("Could not parse headers of e-mail. Message might be defuncted or illegal formatted.", e1);
					}
				}
			}
		} else {
			if(folder == null || folder.equals("") || folder.equals(MailFolder.ID_STRING_INBOX))
				folder = foldObj.getId().toString();
			// it is a database folder
			IGenericDao dao = null;
			try {
				Long folderId = new Long(folder);
				dao = Utility.getDbConnection();
				String username = auth.getEmailId();
				
				String sql = "SELECT id, folder_id, unique_id, sender, receiver, cc, bcc, replyTo, subject, multipart, priority, sentdate, unread, msg_size FROM MSG_DB_OBJECTS WHERE USERNAME=? AND FOLDER_ID = ?";
				mails = dao.readList(MsgDbObject.class, sql, new Object[] {username, folderId});
			} catch (Exception e) {
				e.printStackTrace();
			}
			JdbcUtil.close(dao);
			dao = null;
			//mails = getMailsByFolder(folder);
			if (mails != null) {
				MsgDbObject item = null;
				byte bEmail[] = null;
				Properties props = new Properties();
				Session session = Session.getDefaultInstance(props);
				EmailHeader header = null;
	
				for (int i=0;i<mails.size();i++) {
					item = (MsgDbObject)mails.get(i);
	
					try {
						header = new EmailHeader();
						header.setMultipart(item.getMultipart());
						header.setMessageId(item.getId().intValue());
						header.setFrom(org.claros.commons.mail.utility.Utility.stringToAddressArray(item.getSender()));
						header.setTo(org.claros.commons.mail.utility.Utility.stringToAddressArray(item.getReceiver()));
						header.setCc(org.claros.commons.mail.utility.Utility.stringToAddressArray(item.getCc()));
						header.setBcc(org.claros.commons.mail.utility.Utility.stringToAddressArray(item.getBcc()));
						header.setDate(item.getSentDate());
						header.setReplyTo(org.claros.commons.mail.utility.Utility.stringToAddressArray(item.getReplyTo()));
						header.setSize(item.getMsgSize());
						header.setSubject(org.claros.commons.utility.Utility.updateTRChars(item.getSubject()));
						header.setUnread(item.getUnread());
	
						// now set the human readables.
						header.setDateShown(Formatter.formatDate(header.getDate(), "dd.MM.yyyy HH:mm"));
						header.setFromShown(org.claros.commons.utility.Utility.updateTRChars(org.claros.commons.mail.utility.Utility.addressArrToString(header.getFrom())));
						header.setToShown(org.claros.commons.mail.utility.Utility.addressArrToString(header.getTo()));
						header.setCcShown(org.claros.commons.mail.utility.Utility.addressArrToString(header.getReplyTo()));
						header.setReplyToShown(org.claros.commons.mail.utility.Utility.addressArrToString(header.getTo()));
						header.setBccShown(org.claros.commons.mail.utility.Utility.addressArrToString(header.getBcc()));
						header.setSizeShown(org.claros.commons.mail.utility.Utility.sizeToHumanReadable(header.getSize()));
	
						// it is time to add it to the arraylist
						headers.add(header);
					} catch (Exception e) {
						log.error("Could not parse headers of e-mail. Message might be defuncted or illegal formatted.", e);
					}
				}
			}
		}
		return headers;
	}

	/* (non-Javadoc)
	 * @see org.claros.groupware.webmail.controllers.FolderController#createDefaultFolders(org.claros.commons.models.AuthProfile)
	 */
	public void createDefaultFolders() throws Exception {
		if (getInboxFolder() == null) {
			createFolder(new FolderDbObject(null, new Long(0), auth.getEmailId(), org.claros.commons.mail.utility.Constants.FOLDER_INBOX(profile), Constants.FOLDER_TYPE_INBOX));
		}
		if (getJunkFolder() == null) {
			createFolder(new FolderDbObject(null, new Long(0), auth.getEmailId(), org.claros.commons.mail.utility.Constants.FOLDER_JUNK(profile), Constants.FOLDER_TYPE_JUNK));
		}
		if (getSentItems() == null) {
			createFolder(new FolderDbObject(null, new Long(0), auth.getEmailId(), org.claros.commons.mail.utility.Constants.FOLDER_SENT(profile), Constants.FOLDER_TYPE_SENT));
		}
		if (getTrashFolder() == null) {
			createFolder(new FolderDbObject(null, new Long(0), auth.getEmailId(), org.claros.commons.mail.utility.Constants.FOLDER_TRASH(profile), Constants.FOLDER_TYPE_TRASH));
		}
		if (getDraftsFolder() == null) {
			createFolder(new FolderDbObject(null, new Long(0), auth.getEmailId(), org.claros.commons.mail.utility.Constants.FOLDER_DRAFTS(profile), Constants.FOLDER_TYPE_DRAFTS));
		}
		
//		if (getBackupFolder() == null && profile.isAutoBackup()) {
//			createFolder(new FolderDbObject(null, new Long(0), auth.getEmailId(), org.claros.commons.mail.utility.Constants.FOLDER_BACKUP(profile), Constants.FOLDER_TYPE_BACKUP));
//		}
//		if (getBackupInboxFolder() == null && profile.isAutoBackup()) {
//			createFolder(new FolderDbObject(null, getBackupFolder().getId(), auth.getEmailId(), org.claros.commons.mail.utility.Constants.FOLDER_INBOX(profile), Constants.FOLDER_TYPE_B_INBOX));
//		}
//		if (getBackupSentFolder() == null && profile.isAutoBackup()) {
//			createFolder(new FolderDbObject(null, getBackupFolder().getId(), auth.getEmailId(), org.claros.commons.mail.utility.Constants.FOLDER_SENT(profile), Constants.FOLDER_TYPE_B_SENT));
//		}
	}

	/* (non-Javadoc)
	 * @see org.claros.groupware.webmail.controllers.FolderController#renameFolder(java.lang.String, java.lang.String)
	 */
	public void renameFolder(String parentId, String oldName, String newName) throws Exception {
		if (oldName.equals(org.claros.commons.mail.utility.Constants.FOLDER_INBOX(profile))) {
			throw new SystemException();
		}
		List folders = getFolders();
		FolderDbObjectWrapper tmp = null;
		String n = null;
		for (int i=0; i<folders.size(); i++) {
			tmp = (FolderDbObjectWrapper)folders.get(i);
			n = tmp.getFolderName();
			if (n.equals(oldName)) {

				IGenericDao dao = null;
				try {
					dao = org.claros.intouch.common.utility.Utility.getDbConnection();
					
					FolderDbObject item = getFolder(tmp.getId().toString());
					item.setFolderName(newName);
					String newParentId = (SmartUtil.isBlankObject(parentId)) ? "0" : parentId;
					//dao.update(item);
					String sql = "update folder_db_objects set folder_name = '" +newName+ "', parent_id='" + newParentId + "' where id = '"+tmp.getId()+"'";
					dao.executeUpdate(sql);
					
				} finally {
					JdbcUtil.close(dao);
					dao = null;
				}
				return;
			}
		}
		throw new NoPermissionException();
	}

	/**
	 * 
	 */
	public List getHeadersByFolder(String folder, int[] msgs) throws Exception {
		ArrayList result = new ArrayList();
		List headers = getHeadersByFolder(folder);
		EmailHeader tmp = null;
		for (int i=0;i<headers.size();i++) {
			tmp = (EmailHeader)headers.get(i);
			for (int j=0;j<msgs.length;j++) {
				if (tmp.getMessageId() == msgs[j]) {
					result.add(tmp);
					break;
				}
			}
		}
		return result;
	}

	@Override
	public FolderDbObject getBackupFolder() throws Exception {
		return getSpecialFolderByType(Constants.FOLDER_TYPE_BACKUP);
	}

	@Override
	public FolderDbObject getBackupInboxFolder() throws Exception {
		return getSpecialFolderByType(Constants.FOLDER_TYPE_B_INBOX);
	}

	@Override
	public FolderDbObject getBackupSentFolder() throws Exception {
		return getSpecialFolderByType(Constants.FOLDER_TYPE_B_SENT);
	}
}
