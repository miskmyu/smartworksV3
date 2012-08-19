package net.smartworks.model.mail;

import org.claros.commons.mail.models.ConnectionProfile;

import net.smartworks.model.BaseObject;

public class EmailServer extends BaseObject {

	public static final String PROTOCOL_POP3 	= "pop3";
	public static final String PROTOCOL_IMAP 	= "imap";
	public static final String PROTOCOL_SMTP 	= "smtp";
	
	public static final int DEFAULT_MAIL_KEEPING_MONTHS = 6;

	private String fetchServer;
	private int fetchServerPort;
	private String fetchProtocol;
	private boolean fetchSsl;

	private String smtpServer;
	private int smtpServerPort;
	private boolean smtpAuthenticated;
	private boolean smtpSsl;
	
	private boolean deleteFetched;
	private boolean autoBackup;
	private int mailKeepingMonths=DEFAULT_MAIL_KEEPING_MONTHS;
	
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
	public boolean isDeleteFetched() {
		return deleteFetched;
	}
	public void setDeleteFetched(boolean deleteFetched) {
		this.deleteFetched = deleteFetched;
	}
	public boolean isAutoBackup() {
		return autoBackup;
	}
	public void setAutoBackup(boolean autoBackup) {
		this.autoBackup = autoBackup;
	}
	public int getMailKeepingMonths() {
		return mailKeepingMonths;
	}
	public void setMailKeepingMonths(int mailKeepingMonths) {
		this.mailKeepingMonths = mailKeepingMonths;
	}
	public EmailServer() {
		super();
	}

	public EmailServer(String id, String name) {
		super(id, name);
	}
	
	public ConnectionProfile getConnectionProfile(){
		ConnectionProfile profile = new ConnectionProfile();
		profile.setShortName(this.getName());
		profile.setFetchServer(this.getFetchServer());
		profile.setFetchPort(String.valueOf(this.getFetchServerPort()));
		profile.setFetchSSL(String.valueOf(this.isFetchSsl()));
		profile.setProtocol(this.getFetchProtocol());
		profile.setSmtpServer(this.getSmtpServer());
		profile.setSmtpPort(String.valueOf(this.getSmtpServerPort()));
		profile.setSmtpSSL(String.valueOf(this.isSmtpSsl()));
		profile.setSmtpAuthenticated(String.valueOf(this.isSmtpAuthenticated()));
		profile.setDeleteFetched(this.deleteFetched);
		profile.setAutoBackup(this.autoBackup);
		profile.setMailKeepingMonths(this.mailKeepingMonths);
		return profile;
	}
}
