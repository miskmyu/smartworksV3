/*	
 * $Id$
 * created by    : hsshin
 * creation-date : 2012. 6. 12.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.mail.manager;

import net.smartworks.server.engine.mail.exception.MailException;
import net.smartworks.server.engine.mail.model.MailContent;
import net.smartworks.server.engine.mail.model.MailContentCond;

public interface IMailManager {

	public MailContent getMailContent(String user, String objId, String level) throws MailException;
	public MailContent getMailContent(String user, MailContentCond cond, String level) throws MailException;
	public void setMailContent(String user, MailContent obj, String level) throws MailException;
	public void createMailContent(String user, MailContent obj) throws MailException;
	public void removeMailContent(String user, String objId) throws MailException;
	public void removeMailContent(String user, MailContentCond cond) throws MailException;
	public long getMailContentSize(String user, MailContentCond cond) throws MailException;
	public MailContent[] getMailContents(String user, MailContentCond cond, String level) throws MailException;

}