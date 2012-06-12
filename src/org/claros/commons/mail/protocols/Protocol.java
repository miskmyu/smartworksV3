package org.claros.commons.mail.protocols;

import java.util.ArrayList;

import javax.mail.Message;

import org.claros.commons.exception.SystemException;
import org.claros.commons.mail.exception.ConnectionException;
import org.claros.commons.mail.exception.MailboxActionException;
import org.claros.commons.mail.exception.ProtocolNotAvailableException;
import org.claros.commons.mail.exception.ServerDownException;
import org.claros.commons.mail.models.ConnectionMetaHandler;
import org.claros.commons.mail.models.ConnectionProfile;
import org.claros.commons.mail.models.EmailHeader;

/**
 * @author Umut Gokbayrak
 */
public interface Protocol {
	public ConnectionMetaHandler connect(int connectType) throws SystemException, ConnectionException, ServerDownException;
	public void disconnect();
	public EmailHeader fetchHeader(Message msg, int msgId) throws SystemException, ConnectionException;
	public ArrayList fetchAllHeaders() throws SystemException, ConnectionException;
	public Message[] fetchAllMessagesWithUid() throws SystemException, ConnectionException;
	public ArrayList fetchAllHeadersAsMessages() throws SystemException, ConnectionException;
	public ConnectionMetaHandler deleteMessages(int messageIds[]) throws MailboxActionException, SystemException, ConnectionException;
	public Message getMessage(int messageId) throws MailboxActionException, SystemException, ConnectionException, Exception;
	public String getMessageUID(Message message) throws MailboxActionException, SystemException, ConnectionException, Exception;
	public void emptyFolder() throws Exception;
	public int getTotalMessageCount() throws Exception;
	public int getUnreadMessageCount() throws Exception;
	public void flagAsDeleted(int[] ids) throws Exception;
	public ArrayList getHeadersSortedList(String sortCriteriaRaw, String sortDirectionRaw) throws ProtocolNotAvailableException;
	public ConnectionProfile getProfile();
	public ArrayList fetchHeaders(int[] msgs) throws SystemException, ConnectionException;
}
