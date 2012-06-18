package net.smartworks.model.mail;

public class MailAccount{

	private String emailServerId;
	private String emailServerName;
	private String userName;
	private String password;
	
	public String getEmailServerId() {
		return emailServerId;
	}

	public void setEmailServerId(String emailServerId) {
		this.emailServerId = emailServerId;
	}

	public String getEmailServerName() {
		return emailServerName;
	}

	public void setEmailServerName(String emailServerName) {
		this.emailServerName = emailServerName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public MailAccount() {
		super();
	}

	public MailAccount(String emailServer, String emailServerName, String userName, String password) {
		super();
		this.emailServerName = emailServerName;
		this.userName = userName;
		this.password = password;
	}
}
