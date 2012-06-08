package net.smartworks.model.mail;

import org.claros.commons.mail.models.EmailPart;

import net.smartworks.model.BaseObject;
import net.smartworks.model.community.User;
import net.smartworks.model.community.WorkSpace;
import net.smartworks.model.work.Work;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartUtil;

public class MailAttachment extends BaseObject {

	public static final String MIME_TYPE_TEXT_PLAIN = "text/plain";
	public static final String MIME_TYPE_TEXT_HTML = "text/html";
	public static final String MIME_TYPE_APPLICATION = "application/octet-stream";
	
	String mimeType;
	long size;
	String fileType;
	EmailPart part;
	
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}	
	public String getFileTye() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	public MailAttachment() {
		super();
	}

	public MailAttachment(String id, String name) {
		super(id, name);
	}

	public MailAttachment(String id, String name, String mimeType, long size) {
		super(id, name);
		this.mimeType = mimeType;
		this.size = size;
	}
}
