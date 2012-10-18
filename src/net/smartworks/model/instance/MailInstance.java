package net.smartworks.model.instance;

import net.smartworks.model.community.User;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.mail.MailAttachment;
import net.smartworks.model.mail.MailFolder;
import net.smartworks.model.work.Work;
import net.smartworks.model.work.WorkCategory;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartMessage;
import net.smartworks.util.SmartUtil;

public class MailInstance extends Instance {
	
	public final static int BRIEF_EMAILADDRESS_COUNT = 3;

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
	private String prevMsgId;
	private String nextMsgId;

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

	public String getPrevMsgId() {
		return prevMsgId;
	}

	public void setPrevMsgId(String prevMsgId) {
		this.prevMsgId = prevMsgId;
	}

	public String getNextMsgId() {
		return nextMsgId;
	}

	public void setNextMsgId(String nextMsgId) {
		this.nextMsgId = nextMsgId;
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
	
	public String getReceiversShownBrief(){
		if(SmartUtil.isBlankObject(this.receivers)) return "";
		String shown = receivers[0].getEmailAddressShown();
		for(int i=1; i<this.receivers.length; i++){
			if(i==BRIEF_EMAILADDRESS_COUNT){
				//Start 2012.09.10 받는사람 클릭시 사라지는 버그 수정 잘못된 메소드 호출
				String usersShown = this.getReceiversShown().replaceAll("<", "&lt;");
				//End jybae 
				usersShown = usersShown.replaceAll(">", "&gt;");
				usersShown = usersShown.replaceAll("\"", "&quot;");
				usersShown = usersShown.replaceAll("\'", "&#39;");
				shown = shown + " <a href='' class='js_show_all_users_shown' usersShown=\"" + usersShown + "\">" 
						+ SmartMessage.getString("content.sentence.with_other_users", new Object[]{this.receivers.length-BRIEF_EMAILADDRESS_COUNT})
						+ "</a>";
				break;
			}
			shown = shown + ", " +  receivers[i].getEmailAddressShown();
		}
		return shown;
	}
	
	public String getReceiversHtml(){
		if(SmartUtil.isBlankObject(this.receivers)) return "";
		String userField = "";
		for(int i=0; i<this.receivers.length; i++){
			String name = (SmartUtil.isBlankObject(receivers[i].getLongName())) ? receivers[i].getId() : receivers[i].getLongName();
			userField = userField + "<span class='js_community_item user_select' comId='" + receivers[i].getId() + "'>" + name + "<a class='js_remove_community' href=''>&nbsp;x</a></span>";
		}
		return userField;
	}
	
	public String getCcReceiversShown(){
		if(SmartUtil.isBlankObject(this.ccReceivers)) return "";
		String shown = ccReceivers[0].getEmailAddressShown();
		for(int i=1; i<this.ccReceivers.length; i++){
			shown = shown + ", " +  ccReceivers[i].getEmailAddressShown();
		}
		return shown;		
	}
	
	public String getCcReceiversShownBrief(){
		if(SmartUtil.isBlankObject(this.ccReceivers)) return "";
		String shown = ccReceivers[0].getEmailAddressShown();
		for(int i=1; i<this.ccReceivers.length; i++){
			if(i==BRIEF_EMAILADDRESS_COUNT){
				String usersShown = this.getCcReceiversShown().replaceAll("<", "&lt;");
				usersShown = usersShown.replaceAll(">", "&gt;");
				usersShown = usersShown.replaceAll("\"", "&quot;");
				usersShown = usersShown.replaceAll("\'", "&#39;");
				shown = shown + " <a href='' class='js_show_all_users_shown' usersShown=\"" + usersShown + "\">" 
						+ SmartMessage.getString("content.sentence.with_other_users", new Object[]{this.ccReceivers.length-BRIEF_EMAILADDRESS_COUNT})
						+ "</a>";
				break;
			}
			shown = shown + ", " +  ccReceivers[i].getEmailAddressShown();
		}
		return shown;		
	}
	
	public String getCcReceiversHtml(){
		if(SmartUtil.isBlankObject(this.ccReceivers)) return "";
		String userField = "";
		for(int i=0; i<this.ccReceivers.length; i++){
			String name = (SmartUtil.isBlankObject(ccReceivers[i].getLongName())) ? ccReceivers[i].getId() : ccReceivers[i].getLongName();
			userField = userField + "<span class='js_community_item user_select' comId='" + ccReceivers[i].getId() + "'>" + name + "<a class='js_remove_community' href=''>&nbsp;x</a></span>";
		}
		return userField;
	}
	
	public String getBccReceiversShown(){
		if(SmartUtil.isBlankObject(this.bccReceivers)) return "";
		String shown = bccReceivers[0].getEmailAddressShown();
		for(int i=1; i<this.bccReceivers.length; i++){
			shown = shown + ", " +  bccReceivers[i].getEmailAddressShown();
		}
		return shown;		
	}

	public String getBccReceiversShownBrief(){
		if(SmartUtil.isBlankObject(this.bccReceivers)) return "";
		String shown = bccReceivers[0].getEmailAddressShown();
		for(int i=1; i<this.bccReceivers.length; i++){
			if(i==BRIEF_EMAILADDRESS_COUNT){
				String usersShown = this.getBccReceiversShown().replaceAll("<", "&lt;");
				usersShown = usersShown.replaceAll(">", "&gt;");
				usersShown = usersShown.replaceAll("\"", "&quot;");
				usersShown = usersShown.replaceAll("\'", "&#39;");
				shown = shown + " <a href='' class='js_show_all_users_shown' usersShown=\"" + usersShown  + "\">" 
						+ SmartMessage.getString("content.sentence.with_other_users", new Object[]{this.bccReceivers.length-BRIEF_EMAILADDRESS_COUNT})
						+ "</a>";
				break;
			}
			shown = shown + ", " +  bccReceivers[i].getEmailAddressShown();
		}
		return shown;		
	}

	public String getBccReceiversHtml(){
		if(SmartUtil.isBlankObject(this.bccReceivers)) return "";
		String userField = "";
		for(int i=0; i<this.bccReceivers.length; i++){
			String name = (SmartUtil.isBlankObject(bccReceivers[i].getLongName())) ? bccReceivers[i].getId() : bccReceivers[i].getLongName();
			userField = userField + "<span class='js_community_item user_select' comId='" + bccReceivers[i].getId() + "'>" + name + "<a class='js_remove_community' href=''>&nbsp;x</a></span>";
		}
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
	
	public static String getEmailAddressesShow(User[] users){
		if(SmartUtil.isBlankObject(users)) return "";
		String shown = users[0].getEmailAddressShown();
		for(int i=1; i<users.length; i++){
			shown = shown + ", " +  users[i].getEmailAddressShown();
		}
		return shown;		
		
	}
}
