/*	
 * $Id$
 * created by    : hsshin
 * creation-date : 2012. 3. 13.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.docfile.model;

import java.util.Date;

import net.smartworks.server.engine.worklist.model.TaskWorkCond;

public class FileWorkCond extends TaskWorkCond {

	private static final long serialVersionUID = 1L;

	private String fileId;
	private String fileType;
	private String fileName;
	private String filePath;
	private long fileSize;
	private Date writtenTime;
	private String writtenTimeMonthString;
	private String groupId;
	private String folderId;
	private String folderName;

	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	public Date getWrittenTime() {
		return writtenTime;
	}
	public void setWrittenTime(Date writtenTime) {
		this.writtenTime = writtenTime;
	}
	public String getWrittenTimeMonthString() {
		return writtenTimeMonthString;
	}
	public void setWrittenTimeMonthString(String writtenTimeMonthString) {
		this.writtenTimeMonthString = writtenTimeMonthString;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getFolderId() {
		return folderId;
	}
	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}
	public String getFolderName() {
		return folderName;
	}
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

}