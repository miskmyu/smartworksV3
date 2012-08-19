package net.smartworks.model.mail;

import net.smartworks.model.BaseObject;
import net.smartworks.util.SmartMessage;
import net.smartworks.util.SmartUtil;

public class MailFolder extends BaseObject {

	public static final int TYPE_SYSTEM_INBOX 	= 1;
	public static final int TYPE_SYSTEM_DRAFTS 	= 2;
	public static final int TYPE_SYSTEM_SENT 	= 3;
	public static final int TYPE_SYSTEM_TRASH 	= 4;
	public static final int TYPE_SYSTEM_JUNK 	= 5;
	public static final int TYPE_USER 			= 6;
	public static final int TYPE_SYSTEM_BACKUP 	= 7;
	public static final int TYPE_SYSTEM_B_INBOX = 71;
	public static final int TYPE_SYSTEM_B_SENT 	= 73;
	
	
	public static final int SEND_TYPE_NONE		= 0;
	public static final int SEND_TYPE_REPLY		= 1;
	public static final int SEND_TYPE_REPLY_ALL	= 2;
	public static final int SEND_TYPE_FORWARD	= 3;
	public static final int SEND_TYPE_DRAFTS	= 4;
	public static final int SEND_TYPE_WORK_CONTENT = 5;

	public static final String ID_ROOT 		= "0";
		
	public static final String ID_STRING_INBOX 	= "INBOX";
	public static final String ID_STRING_JUNK 	= "Junk";
	public static final String ID_STRING_SENT 	= "Sent";
	public static final String ID_STRING_TRASH 	= "Trash";
	public static final String ID_STRING_DRAFTS = "Drafts";
	public static final String ID_STRING_USER 	= "User";
	public static final String ID_STRING_BACKUP = "Backup";
	
	private int type = TYPE_USER;
	private String parentId;
	private String desc;
	private int unreadItemCount = 0;
	private int totalItemCount = 0;
	
	public String getName(){
		switch(type){
		case 1:
			return SmartMessage.getString("mail.title.folder.inbox");
		case 2:
			return SmartMessage.getString("mail.title.folder.drafts");
		case 3:
			return SmartMessage.getString("mail.title.folder.sent");
		case 4:
			return SmartMessage.getString("mail.title.folder.trash");
		case 5:
			return SmartMessage.getString("mail.title.folder.junk");
		case 7:
			return SmartMessage.getString("mail.title.folder.backup");
		case 71:
			return SmartMessage.getString("mail.title.folder.inbox");
		case 73:
			return SmartMessage.getString("mail.title.folder.sent");
		}
		return super.getName();
	}
	
	public String getIdString(){
		switch(type){
		case 1:
			return ID_STRING_INBOX;
		case 2:
			return ID_STRING_DRAFTS;
		case 3:
			return ID_STRING_SENT;
		case 4:
			return ID_STRING_TRASH;
		case 5:
			return ID_STRING_JUNK;
		case 6:
			return ID_STRING_USER;
		case 7:
			return ID_STRING_BACKUP;
		case 71:
			return ID_STRING_INBOX;
		case 73:
			return ID_STRING_SENT;
		}
		return "";
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getUnreadItemCount() {
		return unreadItemCount;
	}
	public void setUnreadItemCount(int unreadItemCount) {
		this.unreadItemCount = unreadItemCount;
	}

	public int getTotalItemCount() {
		return totalItemCount;
	}
	public void setTotalItemCount(int totalItemCount) {
		this.totalItemCount = totalItemCount;
	}

	public MailFolder() {
		super();
	}

	public MailFolder(String id, String name) {
		super(id, name);
	}

	public MailFolder(String id, String name, int type) {
		super(id, name);
		this.type = type;
	}
	
	public MailFolder(String id, String parentId, String name, int type) {
		super(id, name);
		this.parentId = parentId;
		this.type = type;
	}	
}
