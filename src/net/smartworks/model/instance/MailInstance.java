package net.smartworks.model.instance;

import net.smartworks.model.community.User;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.mail.MailAttachment;
import net.smartworks.model.mail.MailFolder;
import net.smartworks.model.work.Work;
import net.smartworks.model.work.WorkCategory;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartUtil;

public class MailInstance extends Instance {

	private User sender;
	private LocalDate sendDate;
	private User[] receivers;
	private User[] ccReceivers;
	private User[] bccReceivers;
	private int priority;
	private String mailContents;
	private long size;
	private int partId;
	private MailAttachment[] attachments;
	private boolean unread;
	private MailFolder mailFolder;
	private MailFolder parentMailFolder;

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public LocalDate getSendDate() {
		return sendDate;
	}

	public void setSendDate(LocalDate sendDate) {
		this.sendDate = sendDate;
	}

	public User[] getReceivers() {
		return receivers;
	}

	public void setReceivers(User[] receivers) {
		this.receivers = receivers;
	}

	public User[] getCcReceivers() {
		return ccReceivers;
	}

	public void setCcReceivers(User[] ccReceivers) {
		this.ccReceivers = ccReceivers;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getPartId() {
		return partId;
	}

	public void setPartId(int partId) {
		this.partId = partId;
	}

	public MailAttachment[] getAttachments() {
		return attachments;
	}

	public void setAttachments(MailAttachment[] attachments) {
		this.attachments = attachments;
	}

	public User[] getBccReceivers() {
		return bccReceivers;
	}

	public void setBccReceivers(User[] bccReceivers) {
		this.bccReceivers = bccReceivers;
	}

	public String getMailContents() {
		return mailContents;
	}

	public void setMailContents(String mailContents) {
		this.mailContents = mailContents;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public boolean isUnread() {
		return unread;
	}

	public void setUnread(boolean unread) {
		this.unread = unread;
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

	public MailInstance() {
		super();
		super.setType(Instance.TYPE_MAIL);
	}

	public MailInstance(String id, String subject, User sender,
			LocalDate sendDate) {
		super(id, subject, Instance.TYPE_MAIL, sender, sender, sendDate);
		this.sender = sender;
		this.sendDate = sendDate;
	}
	
	public String getReceiversShown(){
		if(SmartUtil.isBlankObject(this.receivers)) return "";
		String shown = receivers[0].getEmailAddressShown();
		for(int i=1; i<this.receivers.length; i++)
			shown = shown + ", " +  receivers[i].getEmailAddressShown();
		return shown;
	}
	
	public String getReceiversHtml(){
		if(SmartUtil.isBlankObject(this.receivers)) return "";
		String userField = "";
		for(int i=0; i<this.receivers.length; i++)
			userField = userField + "<span class='js_community_item user_select' comId='" + receivers[i].getId() + "'>" + receivers[i].getLongName() + "<a class='js_remove_community' href=''>&nbsp;x</a></span>";
		return userField;
	}
	
	public String getCcReceiversShown(){
		if(SmartUtil.isBlankObject(this.ccReceivers)) return "";
		String shown = ccReceivers[0].getEmailAddressShown();
		for(int i=1; i<this.ccReceivers.length; i++)
			shown = shown + ", " +  ccReceivers[i].getEmailAddressShown();
		return shown;		
	}
	
	public String getCcReceiversHtml(){
		if(SmartUtil.isBlankObject(this.ccReceivers)) return "";
		String userField = "";
		for(int i=0; i<this.ccReceivers.length; i++)
			userField = userField + "<span class='js_community_item user_select' comId='" + ccReceivers[i].getId() + "'>" + ccReceivers[i].getLongName() + "<a class='js_remove_community' href=''>&nbsp;x</a></span>";
		return userField;
	}
	
	public String getBccReceiversShown(){
		if(SmartUtil.isBlankObject(this.bccReceivers)) return "";
		String shown = bccReceivers[0].getEmailAddressShown();
		for(int i=1; i<this.bccReceivers.length; i++)
			shown = shown + ", " +  bccReceivers[i].getEmailAddressShown();
		return shown;		
	}

	public String getBccReceiversHtml(){
		if(SmartUtil.isBlankObject(this.bccReceivers)) return "";
		String userField = "";
		for(int i=0; i<this.bccReceivers.length; i++)
			userField = userField + "<span class='js_community_item user_select' comId='" + bccReceivers[i].getId() + "'>" + bccReceivers[i].getLongName() + "<a class='js_remove_community' href=''>&nbsp;x</a></span>";
		return userField;
	}
	
	public String getAttachmentsHtml(){
		String html = "";
		if(SmartUtil.isBlankObject(attachments)) return html;
		
		for(int i=0; i<attachments.length; i++){
			MailAttachment attachment = attachments[i];
			html = html + "<li class='qq-upload-success' folderId='" + mailFolder.getId() + "' msgId='" + this.getId() + "' partId='" + attachment.getPart().getId() + "'>" +
								"<span class='vm icon_file_" + attachment.getFileType() + "'></span>" + 
								"<a href='' partId='" + attachment.getId() + "' class='qq-upload-file js_download_mail_attachment'>" + attachment.getName() + "</a>" + 
								"<span class='qq-upload-size'>" + SmartUtil.getBytesAsString(attachment.getSize()) + "</span>" +
								"<a href='' class='qq-delete-text'>X</a></li>";
		}
		return html;
	}
}
