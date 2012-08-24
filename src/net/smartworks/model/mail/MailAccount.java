package net.smartworks.model.mail;

import net.smartworks.util.SmartUtil;

public class MailAccount{

	public static final int MAX_MESSAGES_PER_FETCH = 100;
	
	private String emailServerId;
	private String emailServerName;
	private String emailId;
	private String userName;
	private String password;
	private boolean deleteAfterFetched;
	private boolean useSignature;
	private String signature;
	
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

	public String getEmailId() {
		if(SmartUtil.isBlankObject(this.emailId)) return this.userName;
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getUserName() {
		if(SmartUtil.isBlankObject(this.emailId) && !SmartUtil.isBlankObject(this.userName))
			return userName.split("@")[0];
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

	public boolean isDeleteAfterFetched() {
		return deleteAfterFetched;
	}

	public void setDeleteAfterFetched(boolean deleteAfterFetched) {
		this.deleteAfterFetched = deleteAfterFetched;
	}

	public boolean isUseSignature() {
		return useSignature;
	}

	public void setUseSignature(boolean useSignature) {
		this.useSignature = useSignature;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
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

	public MailAccount(String emailServer, String emailServerName, String emailId, String userName, String password, boolean deleteAfterFetched) {
		super();
		this.emailServerName = emailServerName;
		this.emailId = emailId;
		this.userName = userName;
		this.password = password;
		this.deleteAfterFetched = deleteAfterFetched;
	}
}
