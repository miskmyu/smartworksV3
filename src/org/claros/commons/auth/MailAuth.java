package org.claros.commons.auth;

import net.smartworks.util.SmartUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.claros.commons.auth.exception.LoginInvalidException;
import org.claros.commons.auth.models.AuthProfile;
import org.claros.commons.exception.SystemException;
import org.claros.commons.mail.exception.ConnectionException;
import org.claros.commons.mail.exception.ServerDownException;
import org.claros.commons.mail.models.ConnectionMetaHandler;
import org.claros.commons.mail.models.ConnectionProfile;
import org.claros.commons.mail.protocols.Protocol;
import org.claros.commons.mail.protocols.ProtocolFactory;
import org.claros.commons.mail.utility.Constants;

/**
 * 
 * @author Umut Gokbayrak
 *
 */
public class MailAuth {

	public static ConnectionMetaHandler authenticate(ConnectionProfile profile, AuthProfile auth, ConnectionMetaHandler handler) throws SystemException, LoginInvalidException, ServerDownException {
		try {
			
			ProtocolFactory factory = new ProtocolFactory(profile, auth, handler);
			Protocol protocol = factory.getProtocol(null);
			handler = protocol.connect(Constants.CONNECTION_READ_WRITE);
			if (handler == null || !handler.getStore().isConnected()) {
				throw new ConnectionException();
			}
			return handler;
		} catch (SystemException e) {
			System.out.println("System Exception while authenticating user: " + ((auth == null) ? null : auth.getUsername()));
			throw e;
		} catch (ConnectionException e) {
			System.out.println("Login Failed of user: " + ((auth == null) ? null : auth.getUsername()));
			throw new LoginInvalidException(e);
		}
	}
}
