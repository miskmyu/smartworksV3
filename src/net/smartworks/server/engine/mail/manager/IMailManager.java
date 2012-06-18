/*	
 * $Id$
 * created by    : hsshin
 * creation-date : 2012. 6. 12.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.mail.manager;

import net.smartworks.server.engine.mail.exception.MailException;
import net.smartworks.server.engine.mail.model.MailAccount;
import net.smartworks.server.engine.mail.model.MailAccountCond;
import net.smartworks.server.engine.mail.model.MailContent;
import net.smartworks.server.engine.mail.model.MailContentCond;
import net.smartworks.server.engine.mail.model.MailServer;
import net.smartworks.server.engine.mail.model.MailServerCond;

public interface IMailManager {

	public MailContent getMailContent(String user, String objId, String level) throws MailException;
	public MailContent getMailContent(String user, MailContentCond cond, String level) throws MailException;
	public void setMailContent(String user, MailContent obj, String level) throws MailException;
	public void createMailContent(String user, MailContent obj) throws MailException;
	public void removeMailContent(String user, String objId) throws MailException;
	public void removeMailContent(String user, MailContentCond cond) throws MailException;
	public long getMailContentSize(String user, MailContentCond cond) throws MailException;
	public MailContent[] getMailContents(String user, MailContentCond cond, String level) throws MailException;

	public MailServer getMailServer(String user, String objId, String level) throws MailException;
	public MailServer getMailServer(String user, MailServerCond cond, String level) throws MailException;
	public void setMailServer(String user, MailServer obj, String level) throws MailException;
	public void createMailServer(String user, MailServer obj) throws MailException;
	public void removeMailServer(String user, String objId) throws MailException;
	public void removeMailServer(String user, MailServerCond cond) throws MailException;
	public long getMailServerSize(String user, MailServerCond cond) throws MailException;
	public MailServer[] getMailServers(String user, MailServerCond cond, String level)throws MailException;

	public MailAccount getMailAccount(String user, String objId, String level) throws MailException;
	public MailAccount getMailAccount(String user, MailAccountCond cond, String level) throws MailException;
	public void setMailAccount(String user, MailAccount obj, String level) throws MailException;
	public void createMailAccount(String user, MailAccount obj) throws MailException;
	public void removeMailAccount(String user, String objId) throws MailException;
	public void removeMailAccount(String user, MailAccountCond cond) throws MailException;
	public long getMailAccountSize(String user, MailAccountCond cond) throws MailException;
	public MailAccount[] getMailAccounts(String user, MailAccountCond cond, String level)throws MailException;

}