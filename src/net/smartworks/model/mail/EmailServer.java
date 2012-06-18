package net.smartworks.model.mail;

import net.smartworks.model.BaseObject;
import net.smartworks.util.SmartMessage;
import net.smartworks.util.SmartUtil;

public class EmailServer extends BaseObject {

	public static final String PROTOCOL_POP3 	= "pop3";
	public static final String PROTOCOL_IMAP 	= "imap";
	public static final String PROTOCOL_SMTP 	= "smtp";

	private String fetchServer;
	private int fetchServerPort;
	private String fetchProtocol;
	private boolean fetchSsl;

	private String smtpServer;
	private int smtpServerPort;
	private boolean smtpAuthenticated;
	private boolean smtpSsl;
	
	public String getFetchServer() {
		return fetchServer;
	}
	public void setFetchServer(String fetchServer) {
		this.fetchServer = fetchServer;
	}
	public int getFetchServerPort() {
		return fetchServerPort;
	}
	public void setFetchServerPort(int fetchServerPort) {
		this.fetchServerPort = fetchServerPort;
	}
	public String getFetchProtocol() {
		return fetchProtocol;
	}
	public void setFetchProtocol(String fetchProtocol) {
		this.fetchProtocol = fetchProtocol;
	}
	public boolean isFetchSsl() {
		return fetchSsl;
	}
	public void setFetchSsl(boolean fetchSsl) {
		this.fetchSsl = fetchSsl;
	}
	public String getSmtpServer() {
		return smtpServer;
	}
	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}
	public int getSmtpServerPort() {
		return smtpServerPort;
	}
	public void setSmtpServerPort(int smtpServerPort) {
		this.smtpServerPort = smtpServerPort;
	}
	public boolean isSmtpAuthenticated() {
		return smtpAuthenticated;
	}
	public void setSmtpAuthenticated(boolean smtpAuthenticated) {
		this.smtpAuthenticated = smtpAuthenticated;
	}
	public boolean isSmtpSsl() {
		return smtpSsl;
	}
	public void setSmtpSsl(boolean smtpSsl) {
		this.smtpSsl = smtpSsl;
	}

	public EmailServer() {
		super();
	}

	public EmailServer(String id, String name) {
		super(id, name);
	}
}
