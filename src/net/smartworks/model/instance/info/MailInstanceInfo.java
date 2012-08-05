package net.smartworks.model.instance.info;

import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.mail.MailFolder;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartMessage;
import net.smartworks.util.SmartUtil;

public class MailInstanceInfo extends InstanceInfo {

	private UserInfo sender;
	private LocalDate sendDate;
	private UserInfo[] receivers;
	private UserInfo[] ccReceivers;
	private int priority;
	private long size;
	private boolean multipart;
	private boolean unread;
	private MailFolder mailFolder;
	private MailFolder parentMailFolder;

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public MailFolder getMailFolder() {
		return mailFolder;
	}

	public void setMailFolder(MailFolder mailFolder) {
		this.mailFolder = mailFolder;
	}

	public MailFolder getParentMailFolder() {
		return parentMailFolder;
	}

	public void setParentMailFolder(MailFolder parentMailFolder) {
		this.parentMailFolder = parentMailFolder;
	}

	public UserInfo getSender() {
		return sender;
	}

	public void setSender(UserInfo sender) {
		this.sender = sender;
	}

	public LocalDate getSendDate() {
		return sendDate;
	}

	public void setSendDate(LocalDate sendDate) {
		this.sendDate = sendDate;
	}

	public UserInfo[] getReceivers() {
		return receivers;
	}

	public void setReceivers(UserInfo[] receivers) {
		this.receivers = receivers;
	}

	public UserInfo[] getCcReceivers() {
		return ccReceivers;
	}

	public void setCcReceivers(UserInfo[] ccReceivers) {
		this.ccReceivers = ccReceivers;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean isMultipart() {
		return multipart;
	}

	public void setMultipart(boolean multipart) {
		this.multipart = multipart;
	}

	public boolean isUnread() {
		return unread;
	}

	public void setUnread(boolean unread) {
		this.unread = unread;
	}

	public MailInstanceInfo() {
		super();
		super.setType(Instance.TYPE_MAIL);
	}

	public MailInstanceInfo(String id, String subject, UserInfo sender,
			LocalDate sendDate) {
		super(id, subject, Instance.TYPE_MAIL, sender, sender, sendDate);
		this.sender = sender;
		this.sendDate = sendDate;
	}

	public String getReceiversShown(){
		if(SmartUtil.isBlankObject(this.receivers)) return "";
		String shown = receivers[0].getEmailAddressShown();
		if(this.receivers.length==1){
			return receivers[0].getEmailAddressShown();
		}else{
			shown = receivers[0].getEmailAddressShown() + SmartMessage.getString("content.sentence.with_other_users", new Object[]{this.receivers.length-1});
		}
		return shown;
	}
	

}
