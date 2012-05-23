package org.claros.intouch.webmail.controllers_org;

import org.claros.commons.mail.models.ConnectionMetaHandler;

/**
 * @author Umut Gokbayrak
 */
public interface InboxController {
	public ConnectionMetaHandler checkEmail() throws Exception;

}
