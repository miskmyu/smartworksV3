package org.claros.intouch.webmail.models;

/**
 * @author Umut Gokbayrak
 */
public class MsgDbObject {
	private Long id;
	private String username;
	private Long folderId;
	private String uniqueId;
	private Boolean unread;
	private Long msgSize;
	private byte[] email;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the folderId
	 */
	public Long getFolderId() {
		return folderId;
	}
	/**
	 * @param folderId the folderId to set
	 */
	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}
	/**
	 * @return the uniqueId
	 */
	public String getUniqueId() {
		return uniqueId;
	}
	/**
	 * @param uniqueId the uniqueId to set
	 */
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	/**
	 * @return the unread
	 */
	public Boolean getUnread() {
		return unread;
	}
	/**
	 * @param unread the unread to set
	 */
	public void setUnread(Boolean unread) {
		this.unread = unread;
	}
	/**
	 * @return the msgSize
	 */
	public Long getMsgSize() {
		return msgSize;
	}
	/**
	 * @param msgSize the msgSize to set
	 */
	public void setMsgSize(Long msgSize) {
		this.msgSize = msgSize;
	}
	/**
	 * @return the email
	 */
	public byte[] getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(byte[] email) {
		this.email = email;
	}
}