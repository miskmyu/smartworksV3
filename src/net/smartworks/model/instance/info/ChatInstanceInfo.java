package net.smartworks.model.instance.info;

import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.Instance;

public class ChatInstanceInfo extends InstanceInfo {

	private String chatId;
	private String senderId;
	private String senderLongName;
	private String senderNickName;
	private String senderMinPicture;
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

	public String getSenderLongName() {
		return senderLongName;
	}

	public void setSenderLongName(String senderLongName) {
		this.senderLongName = senderLongName;
	}

	public String getSenderNickName() {
		return senderNickName;
	}

	public void setSenderNickName(String senderNickName) {
		this.senderNickName = senderNickName;
	}

	public String getSenderMinPicture() {
		return senderMinPicture;
	}

	public void setSenderMinPicture(String senderMinPicture) {
		this.senderMinPicture = senderMinPicture;
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
