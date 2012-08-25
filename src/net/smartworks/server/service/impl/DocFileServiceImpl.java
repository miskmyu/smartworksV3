/*	
 * $Id$
 * created by    : hsshin
 * creation-date : 2011. 11. 15.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.service.impl;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.smartworks.model.community.User;
import net.smartworks.server.engine.common.manager.AbstractManager;
import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.model.Property;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.docfile.exception.DocFileException;
import net.smartworks.server.engine.docfile.manager.IDocFileManager;
import net.smartworks.server.engine.docfile.model.FileDownloadHistory;
import net.smartworks.server.engine.docfile.model.HbFileModel;
import net.smartworks.server.engine.docfile.model.IFileModel;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.process.task.model.TskTask;
import net.smartworks.server.engine.process.task.model.TskTaskCond;
import net.smartworks.server.engine.worklist.model.TaskWork;
import net.smartworks.server.engine.worklist.model.TaskWorkCond;
import net.smartworks.server.service.IDocFileService;
import net.smartworks.server.service.factory.SwServiceFactory;
import net.smartworks.server.service.util.ModelConverter;
import net.smartworks.util.OSValidator;
import net.smartworks.util.SmartConfUtil;
import net.smartworks.util.SmartUtil;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
public class DocFileServiceImpl extends AbstractManager implements IDocFileService {

	public DocFileServiceImpl() {
		super();
		if (logger.isInfoEnabled())
			logger.info(this.getClass().getName() + " created");
	}
	
	private static IDocFileManager getDocManager() {
		return SwManagerFactory.getInstance().getDocManager();
	}

	public void uploadFile(HttpServletRequest request) throws Exception {

		try{
			String userId = CommonUtil.toNotNull(request.getParameter("userId"));
			String groupId = CommonUtil.toNotNull(request.getParameter("groupId"));
	
			List<IFileModel> docList = new ArrayList<IFileModel>();
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	        Map<String, MultipartFile> files = multipartRequest.getFileMap();
	
	        for(String fileName : files.keySet()) {
	        	MultipartFile mf = files.get(fileName);
	        	IFileModel doc = new HbFileModel();
	        	doc.setMultipartFile(mf);
	        	docList.add(doc);
	        }
	
			getDocManager().createFileList(userId, (groupId.equals("") ? null : groupId), docList, request);
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}
	}

	public void uploadFiles(HttpServletRequest request) throws Exception {

		try{
			List<IFileModel> docList = new ArrayList<IFileModel>();
			Map<String, String> files = new HashMap<String, String>();
			for(String fileId : files.keySet()) {
				String fileName = files.get(fileId);
				IFileModel doc = new HbFileModel();
				doc.setId(fileId);
				doc.setFileName(fileName);
				docList.add(doc);
			}
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}
	}

	@Override
	public void ajaxUploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try{
			getDocManager().ajaxUploadTempFile(request, response);
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}
	}

	public List<IFileModel> findFileGroup(HttpServletRequest request) throws Exception {

		try{
			String groupId = CommonUtil.toNotNull(request.getParameter("groupId"));
	
			List<IFileModel> fileList = new ArrayList<IFileModel>(); 
	
			fileList = getDocManager().findFileGroup(groupId);
	
			return fileList;
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	public void deleteFile(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try{
			String fileId = CommonUtil.toNotNull(request.getParameter("fileId"));
			String fileName = CommonUtil.toNotNull(request.getParameter("fileName"));
			String filePath = "";
			User user = SmartUtil.getCurrentUser();
	
			if(fileId.startsWith("temp_")) {
				String extension = fileName.lastIndexOf(".") > 1 ? fileName.substring(fileName.lastIndexOf(".") + 1) : null;
				filePath = SmartConfUtil.getInstance().getImageServer() + user.getCompanyId() + "\\"+ "Temps" + "\\" + fileId + "." + extension;
				File f = new File(filePath);
				f.delete();
			} else {
				IFileModel doc = getDocManager().retrieveFile(fileId);
				doc.setDeleteAction(true);
				getDocManager().updateFile(user.getId(), doc);
				//filePath = doc.getFilePath();
				//getDocManager().deleteFile(fileId);
			}
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}
	}

	@Override
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try{
			DataInputStream in = null;
	    	ServletOutputStream op = null;
	        FileChannel inChannel = null;
	        FileChannel outChannel = null;
	
	    	try{
	
	    		String fileId = request.getParameter("fileId");
	    		String fileName = request.getParameter("fileName");
	    		
	    		String packageId = CommonUtil.toNull(request.getParameter("workId"));
	    		String taskInstId = CommonUtil.toNull(request.getParameter("taskInstId"));
	    		String recordId = CommonUtil.toNull(request.getParameter("recordId"));
	    		
	    		User user = SmartUtil.getCurrentUser();
	
	    		String sourceFile = "";
	    		String file_name = "";
	
	    		String extension = fileName.lastIndexOf(".") > 1 ? fileName.substring(fileName.lastIndexOf(".") + 1) : null;
	    		IFileModel doc = null;
	    		if(fileId.startsWith("temp_")) {
	    			file_name = fileName;
	    			sourceFile = OSValidator.getImageDirectory() + "/SmartFiles/" + user.getCompanyId() + "/"+ "Temps" + "/" + fileId + "." + extension;
	    			//sourceFile = System.getenv("SMARTWORKS_FILE_HOME") == null ? System.getProperty("user.home") : System.getenv("SMARTWORKS_FILE_HOME") + File.separator + user.getCompanyId() + File.separator + "Temps" + File.separator + fileId + "." + extension;
	    		} else {
	    			doc = getDocManager().retrieveFile(fileId);
		    		//파일명, UniqValue
		    		file_name = doc.getFileName(); 
	    			sourceFile = doc.getFilePath();
	    		}
	
	    		File file = new File(sourceFile);
	    		int length = 0;
	    		op = response.getOutputStream();
	
	    		response.setContentType("application/octet-stream" );
	    		response.setContentLength((int)file.length());
	    		file_name = new String(file_name.getBytes(), response.getCharacterEncoding());
	    		response.setHeader( "Content-Disposition", "attachment; filename=\"" + file_name + "\"" );
	
	    		byte[] bbuf = new byte[4096];
	    		in = new DataInputStream(new FileInputStream(file));
	    		
	    		while ((in != null) && ((length = in.read(bbuf)) != -1))
	    		{
	    			op.write(bbuf,0,length);
	    		}
	    		
	    		//파일 다운로드 이력을 남긴다
	    		saveFileDownloadHistory(user.getId(), doc, packageId, taskInstId, recordId);
	    		
	    	}catch(Throwable t){
	    		t.printStackTrace();
	    		try{
	    			if(in != null & op != null){
	    				in.close();
	    				op.flush();
	    				op.close();
	    			}			
	    		}catch(Throwable th){
	    		}
	        } finally {
	            if (inChannel != null)
	                inChannel.close();
	            if (outChannel != null)
	                outChannel.close();
				op.flush();
	        }
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}
	}
	private void saveFileDownloadHistory(String userId, IFileModel file, String packageId, String taskInstId, String recordId) throws Exception {
		
		FileDownloadHistory obj = getDocManager().getFileDownloadHistoryInfoByFileId(file.getId());
		if (obj == null) {
			obj = new FileDownloadHistory();
		}
		obj.setFileId(file.getId());
		obj.setFileName(file.getFileName());
		obj.setDownloadUserId(userId);
		
		if (!CommonUtil.isEmpty(packageId) && !obj.getRefPackageId().equalsIgnoreCase(packageId)) {

			if (CommonUtil.isEmpty(taskInstId)) {
				TskTaskCond tskTaskCond = new TskTaskCond();
				tskTaskCond.setExtendedProperties(new Property[]{new Property("recordId", recordId)});
				TskTask[] tskTasks = SwManagerFactory.getInstance().getTskManager().getTasks(userId, tskTaskCond, IManager.LEVEL_LITE);
				if (tskTasks != null && tskTasks.length != 0) {
					taskInstId = tskTasks[0].getObjId();
				}
			}
			TaskWorkCond taskCond = new TaskWorkCond();
			taskCond.setTskObjIdIns(new String[]{taskInstId});
			TaskWork[] taskWork = SwManagerFactory.getInstance().getWorkListManager().getTaskWorkList(userId, taskCond);
			if (taskWork != null && taskWork.length != 0) {
				TaskWork task = taskWork[0];
				String taskId = task.getTskObjId();
				String taskName = task.getTskName();
				String prcInstId = task.getPrcObjId();
				String prcInstName = task.getPrcTitle();
				String pkgId = task.getPackageId();
				String pkgName = task.getPackageName();
				
				obj.setRefTaskId(taskId);
				obj.setRefTaskName(taskName);
				obj.setRefPrcInstId(prcInstId);
				obj.setRefPrcInstName(prcInstName);
				obj.setRefPackageId(pkgId);
				obj.setRefPackageName(pkgName);
			}
		}
		
		getDocManager().createFileDownloadHistory(userId, obj);
		
		if (logger.isInfoEnabled()) {
			StringBuffer strBuf = new StringBuffer();
			strBuf.append("FILE DOWNLOAD [ ").append(obj.getFileName()).append(" (historyId : ").append(obj.getObjId()).append(") By ").append(userId).append("]");
			logger.info(strBuf.toString());
		}
	}
	
	@Override
	public void uploadTempFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		getDocManager().uploadTempFile(request, response);
	}

	@Override
	public void uploadYTVideo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		getDocManager().uploadYTVideo(request, response);
	}

	@Override
	public int uploadExcelToWork(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		return getDocManager().uploadExcelToWork(requestBody, request);
	}

	@Override
	public String createMailContent(String companyId, String emailId, byte[] content, Date receivedDate) throws DocFileException {
		return getDocManager().createMailContent(companyId, emailId, content, receivedDate);
	}

	@Override
	public byte[] readMailContent(String fileId, Date receivedDate) throws DocFileException {
		return getDocManager().readMailContent(fileId, receivedDate);
	}

	@Override
	public void deleteMailContent(String fileId, Date receivedDate) throws DocFileException {
		getDocManager().deleteMailContent(fileId, receivedDate);
	}

/*	@Override
	public String createFile(String userId, String groupId, IFileModel file) throws Exception {
		return getDocManager().createFile(userId, groupId, file);
	}

	@Override
	public String createFileList(String userId, String groupId, List<IFileModel> fileList) throws Exception {
		return getDocManager().createFileList(userId, groupId, fileList);
	}

	@Override
	public IFileModel retrieveFile(String fileId) throws Exception {
		 return getDocManager().retrieveFile(fileId);
	}

	@Override
	public void updateFile(String userId, IFileModel file) throws Exception {
		getDocManager().updateFile(userId, file);
	}

	@Override
	public void deleteFile(String fileId) throws Exception {
		getDocManager().deleteFile(fileId);
	}

	@Override
	public void deleteFileGroup(String groupId) throws Exception {
		getDocManager().deleteFileGroup(groupId);
	}

	@Override
	public List<IFileModel> findFileGroup(String groupId) throws Exception {
		return getDocManager().findFileGroup(groupId);
	}

	@Override
	public List<String> findFileIdListByGroup(String groupId) throws Exception {
		return getDocManager().findFileIdListByGroup(groupId);
	}

	@Override
	public String createDocument(String userId, String groupId, IDocumentModel document, List<FileItem> fileList) throws Exception {
		return getDocManager().createDocument(userId, groupId, document, fileList);
	}

	@Override
	public void updateDocument(String userId, IDocumentModel document) throws Exception {
		getDocManager().updateDocument(userId, document);
	}

	@Override
	public IDocumentModel retrieveDocument(String documentId) throws Exception {
		return getDocManager().retrieveDocument(documentId);
	}

	@Override
	public IDocumentModel retrieveDocumentByGroupId(String fileGroupId) throws Exception {
		return getDocManager().retrieveDocumentByGroupId(fileGroupId);
	}

	@Override
	public List<String> findDocIdByGroupId(String fileGroupId) throws Exception {
		return getDocManager().findDocIdByGroupId(fileGroupId);
	}

	@Override
	public void deleteDocument(String documentId) throws Exception {
		getDocManager().deleteDocument(documentId);
	}

	@Override
	public IDocumentModel retrieveDocumentByRef(int refType, String refId) throws Exception {
		return getDocManager().retrieveDocumentByRef(refType, refId);
	}*/

}
