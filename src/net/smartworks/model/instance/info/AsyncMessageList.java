package net.smartworks.model.instance.info;

public class AsyncMessageList{

	public static final int MAX_MESSAGE_LIST = 20;
	
	private int totalSize = 0;
	private AsyncMessageInstanceInfo[] messages;

	public int getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}
	public AsyncMessageInstanceInfo[] getMessages() {
		return messages;
	}
	public void setMessages(AsyncMessageInstanceInfo[] messages) {
		this.messages = messages;
	}
	
	public AsyncMessageList(){
		super();
	}
}