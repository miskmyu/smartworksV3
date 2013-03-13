package net.smartworks.model.instance.info;

import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.Instance;
import net.smartworks.util.LocalDate;

public class AsyncMessageInstanceInfo extends InstanceInfo {

	private UserInfo sender;
	private UserInfo receiver;
	private String chatId;
	private UserInfo[] chatters;
	private LocalDate sendDate;
	private int msgStatus;
	private String briefMessage;
	private String message;

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
	public String getBriefMessage() {
		return briefMessage;
	}
	public void setBriefMessage(String briefMessage) {
		this.briefMessage = briefMessage;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public UserInfo getReceiver() {
		return receiver;
	}
	public void setReceiver(UserInfo receiver) {
		this.receiver = receiver;
	}
	public UserInfo[] getChatters() {
		return chatters;
	}
	public String getChatId() {
		return chatId;
	}
	public void setChatId(String chatId) {
		this.chatId = chatId;
	}
	public void setChatters(UserInfo[] chatters) {
		this.chatters = chatters;
	}
	public int getMsgStatus() {
		return msgStatus;
	}
	public void setMsgStatus(int msgStatus) {
		this.msgStatus = msgStatus;
	}
	public AsyncMessageInstanceInfo() {
		super();
		super.setType(Instance.TYPE_ASYNC_MESSAGE);
	}
	public AsyncMessageInstanceInfo(String id, UserInfo sender, LocalDate sendDate,
			String message) {
		super(id, "", Instance.TYPE_ASYNC_MESSAGE, sender, sender, sendDate);
		this.sender = sender;
		this.sendDate = sendDate;
		this.message = message;
	}

}
