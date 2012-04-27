package net.smartworks.model.instance.info;

import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.Instance;

public class ChatInstanceInfo extends InstanceInfo {

	private String chatId;
	private String senderId;
	private UserInfo[] senderInfo;
	private String chatMessage;

	public String getChatId() {
		return chatId;
	}

	public void setChatId(String chatId) {
		this.chatId = chatId;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public UserInfo[] getSenderInfo() {
		return senderInfo;
	}

	public void setSenderInfo(UserInfo[] senderInfo) {
		this.senderInfo = senderInfo;
	}

	public String getChatMessage() {
		return chatMessage;
	}

	public void setChatMessage(String chatMessage) {
		this.chatMessage = chatMessage;
	}

	public ChatInstanceInfo() {
		super();
		super.setType(Instance.TYPE_CHAT_MESSAGE);
	}
}
