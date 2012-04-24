package net.smartworks.model.instance;

import net.smartworks.model.community.User;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.util.LocalDate;

public class AsyncMessageInstance extends Instance {

	public static final int MESSAGE_STATUS_UNREAD = 1;
	public static final int MESSAGE_STATUS_READ = 2;
	public static final int MESSAGE_STATUS_OFFLINE_CHAT = 3;
	
	private User sender;
	private User receiver;
	private UserInfo[] chatters;
	private LocalDate sendDate;
	private int msgStatus;
	private String message;

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
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}	
	public User getReceiver() {
		return receiver;
	}
	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}
	public UserInfo[] getChatters() {
		return chatters;
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
	public AsyncMessageInstance() {
		super();
		super.setType(Instance.TYPE_ASYNC_MESSAGE);
	}
	public AsyncMessageInstance(String id, User sender, User receiver, LocalDate sendDate,
			String message) {
		super(id, "", Instance.TYPE_ASYNC_MESSAGE, sender, sender, sendDate);
		this.sender = sender;
		this.receiver = receiver;
		this.sendDate = sendDate;
		this.message = message;
	}

}
