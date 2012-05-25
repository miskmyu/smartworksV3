package net.smartworks.model.mail;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.claros.commons.mail.models.ConnectionProfile;

/**
 * @author Umut Gokbayrak
 */
public class ConnectionProfileList_old {
	public static HashMap conList = new HashMap();

	/**
	 * 
	 */
	public ConnectionProfileList_old() {
		super();
	}
	
	public void addConnectionProfile(ConnectionProfile con) {
		if (con == null) return;
		conList.put(con.getShortName(), con);
	}

	public static HashMap getConList() {
		return conList;
	}
	
	public static ConnectionProfile getProfileByShortName(String shortName) {
		ConnectionProfile con = (ConnectionProfile)conList.get(shortName);
		if (con == null) {
			System.out.println("The Shortname searched at the ConnectionProfileList does not correspond to a ConnectionProfile");
			return null;
		}
		return con;
	}
}
