/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 8. 19.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.docfile.model;

import net.smartworks.server.engine.common.model.MisObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileDownloadHistory extends MisObject {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(FileDownloadHistory.class);
	
	private String fileId;
	private String fileName;
	private String downloadUserId;
	private String refPackageId;
	private String refPackageName;
	private String refPrcInstId;
	private String refPrcInstName;
	private String refTaskId;
	private String refTaskName;
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getDownloadUserId() {
		return downloadUserId;
	}
	public void setDownloadUserId(String downloadUserId) {
		this.downloadUserId = downloadUserId;
	}
	public String getRefPackageId() {
		return refPackageId;
	}
	public void setRefPackageId(String refPackageId) {
		this.refPackageId = refPackageId;
	}
	public String getRefPackageName() {
		return refPackageName;
	}
	public void setRefPackageName(String refPackageName) {
		this.refPackageName = refPackageName;
	}
	public String getRefPrcInstId() {
		return refPrcInstId;
	}
	public void setRefPrcInstId(String refPrcInstId) {
		this.refPrcInstId = refPrcInstId;
	}
	public String getRefPrcInstName() {
		return refPrcInstName;
	}
	public void setRefPrcInstName(String refPrcInstName) {
		this.refPrcInstName = refPrcInstName;
	}
	public String getRefTaskId() {
		return refTaskId;
	}
	public void setRefTaskId(String refTaskId) {
		this.refTaskId = refTaskId;
	}
	public String getRefTaskName() {
		return refTaskName;
	}
	public void setRefTaskName(String refTaskName) {
		this.refTaskName = refTaskName;
	}

}
