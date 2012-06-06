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

	public static final String ID_ROOT 		= "0";
	
	public static final String ID_INBOX 	= "1";
	public static final String ID_JUNK 		= "2";
	public static final String ID_SENT 		= "3";
	public static final String ID_TRASH 	= "4";
	public static final String ID_DRAFTS 	= "5";
	
	public static final String ID_STRING_INBOX 	= "INBOX";
	public static final String ID_STRING_SENT 	= "Sent";
	public static final String ID_STRING_TRASH 	= "Trash";
	public static final String ID_STRING_DRAFTS = "Drafts";
	public static final String ID_STRING_JUNK 	= "Junk";
	
	public static final MailFolder[] SYSTEM_FOLDERS = {
		new MailFolder( ID_INBOX, "", TYPE_SYSTEM_INBOX),
		new MailFolder( ID_SENT, "", TYPE_SYSTEM_SENT),
		new MailFolder( ID_TRASH, "", TYPE_SYSTEM_TRASH),
		new MailFolder( ID_DRAFTS, "", TYPE_SYSTEM_DRAFTS),
		new MailFolder( ID_JUNK, "", TYPE_SYSTEM_JUNK)
	};

	private int type = TYPE_USER;
	private String desc;
	private int unreadItemCount = 0;
	private int totalItemCount = 0;
	
	public String getName(){
		switch(type){
		case 1:
			return SmartMessage.getString("mail.title.folder.inbox");
		case 2:
			return SmartMessage.getString("mail.title.folder.junk");
		case 3:
			return SmartMessage.getString("mail.title.folder.sent");
		case 4:
			return SmartMessage.getString("mail.title.folder.trash");
		case 5:
			return SmartMessage.getString("mail.title.folder.drafts");
		}
		return super.getName();
	}
	
	public String getIdString(){
		switch(type){
		case 1:
			return ID_STRING_INBOX;
		case 2:
			return ID_STRING_JUNK;
		case 3:
			return ID_STRING_SENT;
		case 4:
			return ID_STRING_TRASH;
		case 5:
			return ID_STRING_DRAFTS;
		}
		return "";
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
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
	
	public static String getFolderNameById(String folderId){
		if(SmartUtil.isBlankObject(folderId)) return null;
		
		int folderInt = Integer.parseInt(folderId);
		switch(folderInt){
		case 1:
			return SYSTEM_FOLDERS[0].getName();
		case 2:
			return SYSTEM_FOLDERS[4].getName();
		case 3:
			return SYSTEM_FOLDERS[1].getName();
		case 4:
			return SYSTEM_FOLDERS[2].getName();
		case 5:
			return SYSTEM_FOLDERS[3].getName();
		}
		return null;
	}

	public static String getFolderSIdById(String folderId){
		if(SmartUtil.isBlankObject(folderId)) return null;
		
		int folderInt = Integer.parseInt(folderId);
		switch(folderInt){
		case 1:
			return SYSTEM_FOLDERS[0].getIdString();
		case 2:
			return SYSTEM_FOLDERS[4].getIdString();
		case 3:
			return SYSTEM_FOLDERS[1].getIdString();
		case 4:
			return SYSTEM_FOLDERS[2].getIdString();
		case 5:
			return SYSTEM_FOLDERS[3].getIdString();
		}
		return null;
	}
}
