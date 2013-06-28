/*	
 * $Id$
 * created by    : hsshin
 * creation-date : 2011. 11. 14.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.docfile.manager.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.Clob;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.smartworks.model.community.Community;
import net.smartworks.model.community.User;
import net.smartworks.model.company.CompanyGeneral;
import net.smartworks.model.work.FileCategory;
import net.smartworks.model.work.FormField;
import net.smartworks.model.work.InformationWork;
import net.smartworks.model.work.Work;
import net.smartworks.server.engine.common.manager.AbstractManager;
import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.model.Filter;
import net.smartworks.server.engine.common.model.Filters;
import net.smartworks.server.engine.common.model.SmartServerConstant;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.common.util.FileUtil;
import net.smartworks.server.engine.common.util.id.IDCreator;
import net.smartworks.server.engine.docfile.exception.DocFileException;
import net.smartworks.server.engine.docfile.manager.IDocFileManager;
import net.smartworks.server.engine.docfile.model.FileDownloadHistory;
import net.smartworks.server.engine.docfile.model.FileDownloadHistoryCond;
import net.smartworks.server.engine.docfile.model.FileWork;
import net.smartworks.server.engine.docfile.model.FileWorkCond;
import net.smartworks.server.engine.docfile.model.HbDocumentModel;
import net.smartworks.server.engine.docfile.model.HbFileModel;
import net.smartworks.server.engine.docfile.model.IDocumentModel;
import net.smartworks.server.engine.docfile.model.IFileModel;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.infowork.domain.model.SwdDomain;
import net.smartworks.server.engine.infowork.domain.model.SwdDomainCond;
import net.smartworks.server.engine.infowork.domain.model.SwdField;
import net.smartworks.server.engine.infowork.domain.model.SwdRecord;
import net.smartworks.server.engine.infowork.domain.model.SwdRecordCond;
import net.smartworks.server.engine.infowork.form.model.SwfField;
import net.smartworks.server.engine.infowork.form.model.SwfFieldRef;
import net.smartworks.server.engine.infowork.form.model.SwfForm;
import net.smartworks.server.engine.infowork.form.model.SwfFormCond;
import net.smartworks.server.engine.infowork.form.model.SwfFormRef;
import net.smartworks.server.engine.infowork.form.model.SwfFormat;
import net.smartworks.server.engine.organization.exception.SwoException;
import net.smartworks.server.engine.organization.manager.ISwoManager;
import net.smartworks.server.engine.organization.model.SwoCompany;
import net.smartworks.server.engine.organization.model.SwoCompanyCond;
import net.smartworks.server.engine.process.process.exception.PrcException;
import net.smartworks.server.engine.process.task.model.TskTask;
import net.smartworks.server.service.ICommunityService;
import net.smartworks.server.service.IInstanceService;
import net.smartworks.server.service.IWorkService;
import net.smartworks.util.LocalDate;
import net.smartworks.util.OSValidator;
import net.smartworks.util.SmartConfUtil;
import net.smartworks.util.SmartUtil;
import net.smartworks.util.Thumbnail;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.engine.SessionFactoryImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.data.media.mediarss.MediaCategory;
import com.google.gdata.data.media.mediarss.MediaDescription;
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.data.media.mediarss.MediaTitle;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.YouTubeMediaGroup;
import com.google.gdata.data.youtube.YouTubeNamespace;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class DocFileManagerImpl extends AbstractManager implements IDocFileManager {

	@Autowired
	private IWorkService workService;
	@Autowired
	private IInstanceService instanceService;
	@Autowired
	private ICommunityService communityService;

	public DocFileManagerImpl() {
		super();
		if (logger.isInfoEnabled())
			logger.info(this.getClass().getName() + " created");
	}

	public static final String FILE_DIVISION_PROFILES = "Profiles";
	public static final String FILE_DIVISION_TEMPS = "Temps";
	public static final String FILE_DIVISION_WORKIMAGES = "WorkImages";
	public static final String FILE_DIVISION_MAILS = "Mails";

	private String dbType;

	/**
	 * 파일 저장 위치
	 */
	private String fileDirectory;

	/**
	 * @return the fileDirectory
	 */
	public String getFileDirectory() {
		return fileDirectory;
	}

	/**
	 * @param fileDirectory
	 *            the fileDirectory to set
	 */
	public void setFileDirectory(String fileDirectory) {
		this.fileDirectory = fileDirectory;
	}

	/**
	 * 파일 저장 디렉토리 - 기본 파일 저장 디렉토리에 현재 년, 현재 월로 카테고라이즈 하여 저장한다.
	 * 
	 * @return
	 */
	private File getFileRepository(String companyId, String fileDivision, Date date, String emailId) throws DocFileException {

		if (this.fileDirectory == null)
			throw new DocFileException("Attachment directory is not specified!");
		// 파일 홈 디렉토리 선택
		String storageDir = this.fileDirectory + File.separator + "SmartFiles";
		File storage = new File(storageDir);

		// 없다면 생성한다.
		if (!storage.exists())
			storage.mkdir();

		// 사용자의 회사아이디의 디렉토리 선택
		storageDir =  storageDir + File.separator + companyId;
		storage = new File(storageDir);

		if (!storage.exists())
			storage.mkdir();

		// 파일 형태 구분에 따른 디렉토리 선택
		storageDir = storageDir + File.separator + fileDivision;
		storage = new File(storageDir);

		// 없다면 생성한다.
		if (!storage.exists())
			storage.mkdir();

		if(fileDivision.equals(FILE_DIVISION_MAILS)){
			// 현재사용자의 이메일서버이름으로 파일 디렉토리를 설정한다.
			storageDir = storageDir + File.separator + emailId; 
			storage = new File(storageDir);
				
			// 없다면 생성한다.
			if (!storage.exists())
				storage.mkdir();

			// 메일 받은날짜의 년, 월 정보를 얻는다.
			Calendar calendar = Calendar.getInstance();
			if(SmartUtil.isBlankObject(date)) date = new Date();
			calendar.setTime(date);
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
	
			// 기본 파일 저장 디렉토리와 받은날짜 년 정보로 파일 디렉토리를 설정한다.
			storageDir = storageDir + File.separator + "Y" + year;
			storage = new File(storageDir);
	
			// 없다면 생성한다.
			if (!storage.exists())
				storage.mkdir();

			// 기본 파일 저장 디렉토리와 받은날짜 월 정보로 파일 디렉토리를 설정한다.
			storageDir = storageDir + File.separator + "M" + month;
	
			// 만일 디렉토리가 없다면 생성한다.
			storage = new File(storageDir);
	
			if (!storage.exists())
				storage.mkdir();
		}else if(!fileDivision.equals(FILE_DIVISION_TEMPS) && !fileDivision.equals(FILE_DIVISION_PROFILES) && !fileDivision.equals(FILE_DIVISION_WORKIMAGES)) {

			// 현재 년, 월 정보를 얻는다.
			Calendar currentDate = Calendar.getInstance();
			int year = currentDate.get(Calendar.YEAR);
			int month = currentDate.get(Calendar.MONTH) + 1;
	
			// 기본 파일 저장 디렉토리와 현재 년 정보로 파일 디렉토리를 설정한다.
			storageDir = storageDir + File.separator + "Y" + year;
			storage = new File(storageDir);
	
			// 없다면 생성한다.
			if (!storage.exists())
				storage.mkdir();

			// 기본 파일 저장 디렉토리와 현재 월 정보로 파일 디렉토리를 설정한다.
			storageDir = storageDir + File.separator + "M" + month;
	
			// 만일 디렉토리가 없다면 생성한다.
			storage = new File(storageDir);
	
			if (!storage.exists())
				storage.mkdir();
		}

		return storage;
	}

	private String getFileFullPath(String companyId, String fileDivision, Date date, String fileName) throws DocFileException {

		if (this.fileDirectory == null)
			throw new DocFileException("Attachment directory is not specified!");
		// 파일 홈 디렉토리 선택
		String storageDir = this.fileDirectory + File.separator + "SmartFiles";

		// 사용자의 회사아이디의 디렉토리 선택
		storageDir =  storageDir + File.separator + companyId;

		// 파일 형태 구분에 따른 디렉토리 선택
		storageDir = storageDir + File.separator + fileDivision;

		if(fileDivision.equals(FILE_DIVISION_MAILS)){
			// 현재사용자의 이메일서버이름으로 파일 디렉토리를 설정한다.
			storageDir = storageDir + File.separator + SmartUtil.getCurrentUser().getEmailId(); 
				
			// 메일 받은날짜의 년, 월 정보를 얻는다.
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
	
			// 기본 파일 저장 디렉토리와 받은날짜 년 정보로 파일 디렉토리를 설정한다.
			storageDir = storageDir + File.separator + "Y" + year;
	
			// 기본 파일 저장 디렉토리와 받은날짜 월 정보로 파일 디렉토리를 설정한다.
			storageDir = storageDir + File.separator + "M" + month;
	
		}else if(!fileDivision.equals(FILE_DIVISION_TEMPS) && !fileDivision.equals(FILE_DIVISION_PROFILES) && !fileDivision.equals(FILE_DIVISION_WORKIMAGES)) {

			// 현재 년, 월 정보를 얻는다.
			Calendar currentDate = Calendar.getInstance();
			int year = currentDate.get(Calendar.YEAR);
			int month = currentDate.get(Calendar.MONTH) + 1;
	
			// 기본 파일 저장 디렉토리와 현재 년 정보로 파일 디렉토리를 설정한다.
			storageDir = storageDir + File.separator + "Y" + year;
	
			// 기본 파일 저장 디렉토리와 현재 월 정보로 파일 디렉토리를 설정한다.
			storageDir = storageDir + File.separator + "M" + month;
	
		}

		return storageDir + File.separator + fileName;
	}

	/**
	 * 파일을 저장한다.
	 * 
	 * @param filePath
	 * @param fileItem
	 * @throws Exception
	 */
	private void writeFile(String filePath, MultipartFile multipartFile) throws DocFileException {
		if (multipartFile.getSize() > 0) {
			try {
				File uploadedFile = new File(filePath);
				FileOutputStream os = new FileOutputStream(uploadedFile);
				InputStream is = multipartFile.getInputStream();
				
				byte[] buf = new byte[1024];

				int len;

				while ((len = is.read(buf)) > 0) {
					os.write(buf, 0, len);
				}
				is.close();
				os.close();
			} catch (Exception e) {
				throw new DocFileException("Failed to write file [" + filePath + "]!");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.maninsoft.smart.server.dao.IDocumentDao#createFile(java.lang.String,
	 * java.lang.String, com.maninsoft.smart.server.model.IFileModel)
	 */
	public String createFile(String userId, String groupId, IFileModel file, HttpServletRequest request) throws DocFileException {

		String fileId = request.getParameter("fileId");
		file.setId(fileId);
		file.setWrittenTime(new Date(new LocalDate().getGMTDate()));
		this.setFileDirectory(SmartConfUtil.getInstance().getImageServer());

		//File repository = this.getFileRepository();
		MultipartFile multipartFile = file.getMultipartFile();
		String filePath = null;
		if (file != null) {
			String fileName = request.getParameter("fileName");
			if (fileName.indexOf(File.separator) > 1)
				fileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1);

			String extension = fileName.lastIndexOf(".") > 1 ? fileName.substring(fileName.lastIndexOf(".") + 1) : null;
			//filePath = repository.getAbsolutePath() + File.separator + (String) fileId;

			if (extension != null) {
				filePath = filePath + "." + extension;
				file.setType(extension);
			}

			file.setFilePath(filePath);
			file.setFileSize(multipartFile.getSize());
			file.setFileName(fileName);

		}

		this.getHibernateTemplate().save(file);
		this.writeFile(file.getFilePath(), file.getMultipartFile());
		// String path = file.getFilePath();

		// 그룹 아이디가 넘어 오지 않았다면 그룹아이디 설정
		if (groupId == null)
			// 그룹아이디를 생성하여 문서 아이디와 매핑
			groupId = IDCreator.createId(SmartServerConstant.DOCUMENT_GROUP_ABBR);

		// 그룹아이디, 문서 아이디 쌍 저장
		Query query = this.getSession().createSQLQuery("insert into SWDocGroup(groupId, docId) values ('" + groupId + "', '" + fileId + "')");
		query.executeUpdate();
		return groupId;
	}

	public String createFileList(String userId, String groupId, List<IFileModel> fileList, HttpServletRequest request) throws DocFileException {

		if (fileList == null)
			return null;

		if (groupId == null)
			groupId = IDCreator.createId(SmartServerConstant.DOCUMENT_GROUP_ABBR);

		for (IFileModel file : fileList) {
			if(!(file.getMultipartFile().getOriginalFilename().equals(""))) {
				if(request.getParameter("fileId").startsWith("temp_"))
					this.createFile(userId, groupId, file, request);
			}
		}
		return groupId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.maninsoft.smart.server.dao.IDocumentDao#deleteFile(java.lang.String)
	 */
	public void deleteFile(String fileId) throws DocFileException {

		// 파일 그룹에서 삭제
		Query query = this.getSession().createSQLQuery("delete from SWDocGroup where docId = '" + fileId + "'");
		query.executeUpdate();

		// 파일 모델 및 파일 삭제
		IFileModel fileModel = this.retrieveFile(fileId);
		String filePath = fileModel.getFilePath();
		File file = new File(filePath);
		if(file.exists())
			file.delete();
		this.getHibernateTemplate().delete(fileModel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.maninsoft.smart.server.dao.IDocumentDao#deleteFileGroup(java.lang
	 * .String)
	 */
	public void deleteFileGroup(String groupId) throws DocFileException {

		if (groupId == null)
			return;

		String sql = "select docId from SWDocGroup where groupId = '" + groupId + "'";
		Query query = this.getSession().createSQLQuery(sql);
		List fileIdList = query.list();

		if (fileIdList == null || fileIdList.isEmpty())
			return;

		HibernateTemplate ht = this.getHibernateTemplate();

		// 파일 및 문서 모델 삭제
		for (Object fileId : fileIdList) {
			IFileModel fileModel = this.retrieveFile((String) fileId);
			String filePath = fileModel.getFilePath();
			File file = new File(filePath);
			file.delete();
			ht.delete(fileModel);
		}

		// 문서 그룹 삭제
		query = this.getSession().createSQLQuery("delete from SWDocGroup where groupId = '" + groupId + "'");
		query.executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.maninsoft.smart.server.dao.IDocumentDao#findFileGroup(java.lang.String
	 * )
	 */
	public List<IFileModel> findFileGroup(String groupId) throws DocFileException {

		if (groupId == null)
			return new ArrayList<IFileModel>();

		String sql = "select docId from SWDocGroup where groupId = '" + groupId + "'";
		Query query = this.getSession().createSQLQuery(sql);
		List fileIdList = query.list();

		if (fileIdList == null || fileIdList.isEmpty())
			return new ArrayList<IFileModel>();

		String hql = "from HbFileModel where id in (";
		int index = 0;

		for (Object fileId : fileIdList) {
			if (index > 0)
				hql += ",";

			hql += "'" + fileId + "'";
			index++;
		}

		hql += ")";
		query = this.getSession().createQuery(hql);
		List<IFileModel> docList = query.list();
		return docList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.maninsoft.smart.server.dao.IDocumentDao#retrieveFile(java.lang.String
	 * )
	 */
	public IFileModel retrieveFile(String fileId) throws DocFileException {

		return (IFileModel) this.getHibernateTemplate().get(HbFileModel.class, fileId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.maninsoft.smart.server.dao.IDocumentDao#updateFile(java.lang.String,
	 * com.maninsoft.smart.server.model.IFileModel)
	 */
	public void updateFile(String userId, IFileModel file) throws DocFileException {

		try {
			StringBuffer buf = new StringBuffer();
			buf.append("update SwFile set ");
			buf.append(" deleteAction = :deleteAction ");
			buf.append(" where id = :id");
			Query query = this.getSession().createSQLQuery(buf.toString());
			query.setBoolean("deleteAction", file.isDeleteAction());
			query.setString("id", file.getId());
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocFileException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.maninsoft.smart.server.dao.IDocumentDao#findFileIdListByGroup(java
	 * .lang.String)
	 */
	public List<String> findFileIdListByGroup(String groupId) throws DocFileException {

		Query query = this.getSession().createSQLQuery("select docId from SWDocGroup where groupId = '" + groupId + "'");
		return query.list();
	}

	public String createMailContent(String companyId, String emailId, byte[] content, Date receivedDate) throws DocFileException {

		
		String fileId = IDCreator.createId(SmartServerConstant.MAIL_ABBR);
		this.setFileDirectory(OSValidator.getImageDirectory());

		File repository = this.getFileRepository(companyId, FILE_DIVISION_MAILS, receivedDate, emailId);
		String filePath = repository.getAbsolutePath() + File.separator + (String) fileId;

		try{
			FileUtil.write(filePath, content, false);
		}catch (Exception e){
			e.printStackTrace();
			throw new DocFileException("Create Mail Content Failed Error !");
		}
		return fileId;
	}

	public byte[] readMailContent(String fileId, Date receivedDate) throws DocFileException {

		this.setFileDirectory(OSValidator.getImageDirectory());
		String filePath = getFileFullPath(SmartUtil.getCurrentUser().getCompanyId(),  FILE_DIVISION_MAILS, receivedDate, fileId);
		File file = new File(filePath);
		try{
			if(file.exists())
				return FileUtil.readBytes(filePath);
		}catch (Exception e){
			e.printStackTrace();
			throw new DocFileException("Read Mail Content Failed Error !");
		}
		return null;
	}

	public void deleteMailContent(String fileId, Date receivedDate) throws DocFileException {

		this.setFileDirectory(OSValidator.getImageDirectory());
		String filePath = getFileFullPath(SmartUtil.getCurrentUser().getCompanyId(),  FILE_DIVISION_MAILS, receivedDate, fileId);
		File file = new File(filePath);
		try{
			if(file.exists())
				FileUtil.delete(file);
		}catch (Exception e){
			e.printStackTrace();
			throw new DocFileException("Delete Mail Content Failed Error !");
		}
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.maninsoft.smart.server.dao.IDocumentDao#deleteDocument(java.lang.
	 * String)
	 */
	public void deleteDocument(String documentId) throws DocFileException {

		// 문서 조회
		IDocumentModel document = this.retrieveDocument(documentId);
		List<IFileModel> fileList = this.findFileGroup(document.getFileGroupId());

		// 문서 그룹에서 삭제
		Query query = this.getSession().createSQLQuery("delete from SWDocGroup where groupId = '" + document.getFileGroupId() + "'");
		query.executeUpdate();

		// 문서 모델 및 파일 삭제
		this.getHibernateTemplate().deleteAll(fileList);
		this.getHibernateTemplate().delete(document);

		// 파일 삭제
		for (IFileModel fileModel : fileList) {
			String filePath = fileModel.getFilePath();
			File file = new File(filePath);
			file.delete();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.maninsoft.smart.server.dao.IDocumentDao#findDocIdByGroupId(java.lang
	 * .String)
	 */
	public List<String> findDocIdByGroupId(String fileGroupId) throws DocFileException {

		Query query = this.getSession().createSQLQuery("select docId from SWDocGroup where groupId = '" + fileGroupId + "'");
		return query.list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.maninsoft.smart.server.dao.IDocumentDao#retrieveDocument(java.lang
	 * .String)
	 */
	public IDocumentModel retrieveDocument(String documentId) throws DocFileException {

		return (IDocumentModel) this.getHibernateTemplate().get(HbDocumentModel.class, documentId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.maninsoft.smart.server.dao.IDocumentDao#retrieveDocumentByGroupId
	 * (java.lang.String)
	 */
	public IDocumentModel retrieveDocumentByGroupId(String fileGroupId) throws DocFileException {

		Query query = this.getSession().createQuery("from HbDocumentModel where fileGroupId = '" + fileGroupId + "'");
		IDocumentModel doc = (IDocumentModel) query.uniqueResult();
		return doc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.maninsoft.smart.server.dao.IDocumentDao#updateDocument(java.lang.
	 * String, com.maninsoft.smart.server.model.IDocumentModel)
	 */
	public void updateDocument(String userId, IDocumentModel document) throws DocFileException {

		this.getHibernateTemplate().update(document);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.maninsoft.smart.server.dao.IDocumentDao#retrieveDocumentByRef(int,
	 * java.lang.String)
	 */
	public IDocumentModel retrieveDocumentByRef(int refType, String refId) throws DocFileException {

		Query query = this.getSession().createQuery("from HbDocumentModel where refType = " + refType + " and refId = '" + refId + "'");
		IDocumentModel doc = (IDocumentModel) query.uniqueResult();
		return doc;
	}

	private void writeAjaxFile(HttpServletRequest request, HttpServletResponse response, IFileModel formFile) throws DocFileException {

		PrintWriter writer = null;
        InputStream is = null;
        FileOutputStream fos = null;

        try {
            writer = response.getWriter();
        } catch (IOException ex) {
            throw new DocFileException(ex.getMessage());
        }
        String agentInfo = request.getHeader("User-Agent");
        try {
    		if(agentInfo.indexOf("MSIE") > 0) { //IE
    			MultipartFile multipartFile = formFile.getMultipartFile();
    			is = multipartFile.getInputStream();
    		} else {
                is = request.getInputStream();
    		}
            fos = new FileOutputStream(new File(formFile.getFilePath()));
            IOUtils.copy(is, fos);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setHeader("Content-Type", "text/html");
            /*JSONObject jsonObject = new JSONObject();
            jsonObject.put("success", true);
            jsonObject.put("fileId", formFile.getId());
            jsonObject.put("fullPathName", formFile.getImageServerPath());
            jsonObject.put("fileSize", formFile.getFileSize());
            writer.print(jsonObject.toString());*/
            
            String encodingFilePath = StringUtils.replace(formFile.getFilePath(), "\\", "[R_S]");
            
            
            writer.print("{success: \"" + true + "\", fileId: \"" + formFile.getId() + "\", fullPathName: \"" + formFile.getImageServerPath() + "\", fileSize: \"" + formFile.getFileSize() +"\", localFilePath: \"" + encodingFilePath + "\"}");
        } catch (FileNotFoundException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writer.print("{success: false}");
            throw new DocFileException(ex.getMessage());
        } catch (IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writer.print("{success: false}");
            throw new DocFileException(ex.getMessage());
        } finally {
            try {
                fos.close();
                is.close();
            } catch (IOException ignored) {
                throw new DocFileException(ignored.getMessage());
            }
        }

        writer.flush();
        writer.close();
    }
	
	private static String YOUTUBE_CLIENT_ID = "SmartWorks.net";
	private static String YOUTUBE_DEVELOPER_KEY = "AI39si5ITgaYnxRo9xpWzW-BDmhg127Rtlj2M5jB0OZ7Yz7hWlc7S0iu8opQ6LEhLKoS0e4Jp9_UproHtKftR3-I_CVMQW5ibQ";
	private static String YOUTUBE_SMARTWORKS_USERID = "smartworksnet@gmail.com";
	private static String YOUTUBE_SMARTWORKS_PASSWORD = "smartworks.net";
	private static String YOUTUBE_YSJUNG_USERID = "ysjung@maninsoft.co.kr";
	private static String YOUTUBE_YSJUNG_PASSWORD = "ysjung5775";

	private void uploadAjaxYTVideo(HttpServletRequest request, HttpServletResponse response, IFileModel formFile) throws DocFileException {

		PrintWriter writer = null;
        InputStream is = null;
        FileOutputStream fos = null;
		String videoSubject = request.getParameter("videoSubject");
		String videoContent = request.getParameter("videoContent");
		String ytUserId = request.getParameter("ytUserId");
		String ytPassword = request.getParameter("ytPassword");

		//MultipartRequest multipartRequest = (MultipartRequest)request;
        try {
            writer = response.getWriter();
        } catch (IOException ex) {
            throw new DocFileException(ex.getMessage());
        }

        try {
            is = request.getInputStream();
            fos = new FileOutputStream(new File(formFile.getFilePath()));
            IOUtils.copy(is, fos);
        } catch (FileNotFoundException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writer.print("{success: false}");
            throw new DocFileException(ex.getMessage());
        } catch (IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writer.print("{success: false}");
            throw new DocFileException(ex.getMessage());
        } finally {
            try {
                fos.close();
                is.close();
            } catch (IOException ignored) {
                throw new DocFileException(ignored.getMessage());
            }
				
			YouTubeService service = new YouTubeService(YOUTUBE_CLIENT_ID, YOUTUBE_DEVELOPER_KEY);
			if(SmartUtil.isBlankObject(ytUserId) || SmartUtil.isBlankObject(ytPassword)){
				ytUserId = YOUTUBE_SMARTWORKS_USERID;
				ytPassword = YOUTUBE_SMARTWORKS_PASSWORD;
			}
			
			try{
				service.setUserCredentials(ytUserId,  ytPassword);
		    }catch (AuthenticationException e) {
		        System.out.println("Invalid login credentials.");
		        e.printStackTrace();
		        return;
		    }
			
			VideoEntry newEntry = new VideoEntry();

			YouTubeMediaGroup mg = newEntry.getOrCreateMediaGroup();
			mg.setTitle(new MediaTitle());
			mg.getTitle().setPlainTextContent((SmartUtil.isBlankObject(videoSubject)) ? formFile.getFileName() : videoSubject);
		    mg.addCategory(new MediaCategory(YouTubeNamespace.CATEGORY_SCHEME, "Tech"));
			mg.setKeywords(new MediaKeywords());
			mg.getKeywords().addKeyword("SmartWorks");
			mg.setDescription(new MediaDescription());
			if(!SmartUtil.isBlankObject(videoContent))
				mg.getDescription().setPlainTextContent(SmartUtil.isBlankObject(videoContent) ? formFile.getFileName() : videoContent);
			mg.setPrivate(false);

			MediaFileSource ms = new MediaFileSource(new File(formFile.getFilePath()), "video/quicktime");
			//MediaFileSource ms = new MediaFileSource(multipartRequest.getFile("file"), "video/quicktime");
			newEntry.setMediaSource(ms);	
			String uploadUrl = "http://uploads.gdata.youtube.com/feeds/api/users/default/uploads";
			try{
				VideoEntry createdEntry = service.insert(new URL(uploadUrl), newEntry);
	            response.setStatus(HttpServletResponse.SC_OK);
	            String[] idSplited = createdEntry.getId().split(":");
 	            writer.print("{success: true, videoYTId: \"" + idSplited[idSplited.length-1] + "\", fileName: \"" + formFile.getFileName() + "\", fileSize: \"" + formFile.getFileSize() + "\"}");
			}catch (ServiceException se){
	            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	            writer.print("{success: false}");
	            throw new DocFileException(se.getMessage());				
			}catch (IOException ex){
	            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	            writer.print("{success: false}");
	            throw new DocFileException(ex.getMessage());								
			}
        }

        writer.flush();
        writer.close();
    }

	@Override
	public void ajaxUploadFile(HttpServletRequest request, HttpServletResponse response) throws DocFileException {

		IFileModel formFile = new HbFileModel();
		String fileId = IDCreator.createId(SmartServerConstant.FILE_ABBR);
		
		formFile.setId(fileId);
		formFile.setWrittenTime(new Date(new LocalDate().getGMTDate()));
		this.setFileDirectory(SmartConfUtil.getInstance().getImageServer());

		String companyId = SmartUtil.getCurrentUser().getCompanyId();

		String fileDivision = "Files";

		File repository = this.getFileRepository(companyId, fileDivision, null, null);
		String filePath = "";
		if (formFile != null) {
			String fileName = "";
			try {
				fileName = URLDecoder.decode(request.getHeader("X-File-Name"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if (fileName.indexOf(File.separator) > 1)
				fileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1);

			String extension = fileName.lastIndexOf(".") > 1 ? fileName.substring(fileName.lastIndexOf(".") + 1) : null;
			filePath = repository.getAbsolutePath() + File.separator + (String) fileId;

			if (extension != null) {
				filePath = filePath + "." + extension;
				formFile.setType(extension);
			}

			formFile.setFilePath(filePath);
			formFile.setFileSize(Long.parseLong(request.getHeader("Content-Length")));
			formFile.setFileName(fileName);

		}

		this.getHibernateTemplate().save(formFile);
		this.writeAjaxFile(request, response, formFile);
		// String path = file.getFilePath();

		String groupId = request.getParameter("groupId");
		// 그룹 아이디가 넘어 오지 않았다면 그룹아이디 설정
		if (groupId == null)
			// 그룹아이디를 생성하여 문서 아이디와 매핑
			groupId = IDCreator.createId(SmartServerConstant.DOCUMENT_GROUP_ABBR);

		// 그룹아이디, 문서 아이디 쌍 저장
		Query query = this.getSession().createSQLQuery("insert into SWDocGroup(groupId, docId) values ('" + groupId + "', '" + fileId + "')");
		query.executeUpdate();
	}


	public void ajaxUploadTempFile(HttpServletRequest request, HttpServletResponse response) throws DocFileException {

		IFileModel formFile = new HbFileModel();
		String fileId = IDCreator.createId(SmartServerConstant.TEMP_ABBR);
		formFile.setId(fileId);
		//this.setFileDirectory(SmartConfUtil.getInstance().getFileDirectory());
		this.setFileDirectory(System.getenv("SMARTWORKS_FILE_HOME") == null ? System.getProperty("user.home") : System.getenv("SMARTWORKS_FILE_HOME"));

		String companyId = SmartUtil.getCurrentUser().getCompanyId();

		String fileDivision = FILE_DIVISION_TEMPS;

		File repository = this.getFileRepository(companyId, fileDivision, null, null);
		String filePath = "";
		String extension = "";
		if (formFile != null) {
			String fileName = "";
			try {
				fileName = URLDecoder.decode(request.getHeader("X-File-Name"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if (fileName.indexOf(File.separator) > 1)
				fileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1);

			extension = fileName.lastIndexOf(".") > 1 ? fileName.substring(fileName.lastIndexOf(".") + 1) : null;
			filePath = repository.getAbsolutePath() + File.separator + (String) fileId;

			if (extension != null) {
				filePath = filePath + "." + extension;
			}

			formFile.setFilePath(filePath);

		}

		this.writeAjaxFile(request, response, formFile);

	}

	@Override
	public void uploadTempFile(HttpServletRequest request, HttpServletResponse response) throws DocFileException {
		IFileModel formFile = new HbFileModel();

		//this.setFileDirectory(System.getenv("SMARTWORKS_FILE_DIRECTORY") == null ? System.getProperty("user.home") : System.getenv("SMARTWORKS_FILE_DIRECTORY"));
		this.setFileDirectory(OSValidator.getImageDirectory());
		User user = SmartUtil.getCurrentUser();
		String companyId = null;
		SwoCompany[] swoCompanies = null;
		if(user == null) {
			try {
				swoCompanies = SwManagerFactory.getInstance().getSwoManager().getCompanys("", null, IManager.LEVEL_LITE);
			} catch (SwoException e) {
				e.printStackTrace();
			} finally {
				if(!CommonUtil.isEmpty(swoCompanies)) {
					SwoCompany swoCompany = swoCompanies[0];
					companyId = swoCompany.getId();
				}
			}
		} else {
			companyId = user.getCompanyId();
		}

		String fileId = IDCreator.createId(SmartServerConstant.TEMP_ABBR);
		String fileDivision = FILE_DIVISION_TEMPS;
		File repository = this.getFileRepository(companyId, fileDivision, null, null);
		String filePath = "";
		String imagerServerPath = "";
		String extension = "";
		String fileName = "";
		String agentInfo = request.getHeader("User-Agent");
		if(agentInfo.indexOf("MSIE") > 0) { //IE
			List<IFileModel> docList = new ArrayList<IFileModel>();
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			Map<String, MultipartFile> filesMap = multipartRequest.getFileMap();
			for(String key : filesMap.keySet()) {
	        	MultipartFile mf = filesMap.get(key);
	        	IFileModel doc = new HbFileModel();
	        	doc.setMultipartFile(mf);
	        	docList.add(doc);
			}
			formFile = docList.get(0);
			MultipartFile multipartFile = formFile.getMultipartFile();
			fileName = multipartFile.getOriginalFilename();
			formFile.setFileSize(multipartFile.getSize());
			formFile.setMultipartFile(multipartFile);
		} else {
			try {
				fileName = URLDecoder.decode(request.getHeader("X-File-Name"), "UTF-8");
				formFile.setFileSize(Long.parseLong(request.getHeader("Content-Length")));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		if (fileName.indexOf(File.separator) > 1)
			fileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1);
		formFile.setFileName(fileName);
		extension = fileName.lastIndexOf(".") > 1 ? fileName.substring(fileName.lastIndexOf(".") + 1) : null;
		filePath = repository.getAbsolutePath() + File.separator + (String) fileId;

		imagerServerPath = SmartConfUtil.getInstance().getImageServer() + companyId + "/" + FILE_DIVISION_TEMPS + "/" + fileId + "." + extension;
		formFile.setImageServerPath(imagerServerPath);

		if (extension != null) {
			filePath = filePath + "." + extension;
		}

		formFile.setFilePath(filePath);
		formFile.setId(fileId);

		this.writeAjaxFile(request, response, formFile);

	}

	@Override
	public int uploadExcelToWork(Map<String, Object> requestBody, HttpServletRequest request) throws DocFileException {
		
		User cUser = SmartUtil.getCurrentUser();
		
		String workId = (String)requestBody.get("workId");
		InformationWork work = (InformationWork)request.getSession().getAttribute("smartWork");
		try{
			if(SmartUtil.isBlankObject(work) || !work.getId().equals(workId))
				work = (InformationWork)workService.getWorkById(workId);
		}catch (Exception e){
			e.printStackTrace();
		}
		Map<String, Object> smartFormInfoMap = (Map<String, Object>)requestBody.get("frmImportFromExcel");
		Map<String, Object> importFile = (Map<String, Object>)smartFormInfoMap.get("txtImportFile");
		List<Map<String, String>> files = (List<Map<String, String>>)importFile.get("files");
		String groupId = (String)importFile.get("groupId");
		String fileId=null;
		String fileName=null;
		String fileSize=null;
		String localFilePath=null;
		if(files != null && files.size() == 1) {
			Map<String, String> file = files.get(0);
			fileId = file.get("fileId");
			fileName = file.get("fileName");
			fileSize = file.get("fileSize");
			localFilePath = file.get("localFilePath");
		}
		if(localFilePath==null){
			throw new DocFileException("File Information is incorrect...");
		}
		int count = 0;
		try {

			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(localFilePath));
			XSSFSheet sheet = wb.getSheetAt(0);
			int rows = sheet.getPhysicalNumberOfRows();
			Map<String, Object> requestMap = new HashMap<String, Object>();
			requestMap.put("workId", workId);
			requestMap.put("formId", work.getForm().getId());
			requestMap.put("formName", work.getForm().getName());
			requestMap.put("frmAccessSpace", requestBody.get("frmAccessSpace"));
			FormField[] importFields = work.getForm().getImportFields();
			
			for(int r=1; r<rows; r++){
				XSSFRow row = sheet.getRow(r);
				if (row == null) {
					continue;
				}
				int cells = row.getPhysicalNumberOfCells();
				Map<String, Object> smartForm = new HashMap<String, Object>();
				if((cells-1) != importFields.length) return 0;
				for (int c = 1; c < cells; c++) {
					XSSFCell cell = row.getCell(c);
					FormField field = importFields[c-1];

					String sValue = null;
					Date dValue = null;
					boolean bValue;
					double douValue;
					String value = null;
					User user = null;
					switch (cell.getCellType()) {
		                case Cell.CELL_TYPE_STRING:
		                	sValue = cell.getRichStringCellValue().getString();
		                    break;
		                case Cell.CELL_TYPE_NUMERIC:
		                    if (DateUtil.isCellDateFormatted(cell)) {
		                    	dValue = cell.getDateCellValue();
		                    } else {
		                    	sValue = "" + cell.getNumericCellValue();
		                    }
		                    break;
		                case Cell.CELL_TYPE_BOOLEAN:
		                	sValue = (cell.getBooleanCellValue()) ? "true" : "false";
		                    break;
		                case Cell.CELL_TYPE_FORMULA:
		                	sValue = cell.getCellFormula();
		                    break;
		                default:				
					}
					
					if(field.getType().equals(FormField.TYPE_DATE) && dValue!=null){
						value = (new SimpleDateFormat("yyyy.MM.dd")).format(dValue);
					}else if(field.getType().equals(FormField.TYPE_DATETIME) && dValue!=null){
						value = (new SimpleDateFormat("yyyy.MM.dd HH:mm")).format(dValue);
					}else if(field.getType().equals(FormField.TYPE_TIME) && dValue!=null){
						value = (new SimpleDateFormat("HH:mm")).format(dValue);
					}else if(field.getType().equals(FormField.TYPE_USER) && sValue!=null){
						user = communityService.getUserById(sValue);
					}else{
						value = sValue;
					}
					
					if(field.isMandatory() && (value==null && user==null)){
						smartForm.clear();
						break;
					}
					if(value!=null){
						if (field.getType().equalsIgnoreCase("refFormField")) {
							if (value.indexOf(".0") != -1)
								value = StringUtils.replace(value, ".0", "");
							SwfFormCond formCond = new SwfFormCond();
							formCond.setId(work.getForm().getId());
							SwfForm[] swForms = SwManagerFactory.getInstance().getSwfManager().getForms(cUser.getId(), formCond, IManager.LEVEL_ALL);
							
							if (swForms == null || swForms.length == 0)
								continue;
							
							SwfField[] swFields = swForms[0].getFields();
							if (swFields == null)
								continue;
							SwfField targetField = null;
							for (int i = 0; i < swFields.length; i++) {
								SwfField swField = swFields[i];
								if (field.getId().equalsIgnoreCase(swField.getId())) {
									targetField = swField;
									break;
								}
							}
							if (targetField == null)
								continue;
							SwfFormat swFormat = targetField.getFormat();
							if (swFormat == null)
								continue;
							SwfFormRef formRef = swFormat.getRefForm();
							if (formRef == null)
								continue;
							SwfFieldRef fieldRef = formRef.getField();
							if (fieldRef == null)
								continue;
							
							String targetFormId = formRef.getId();
							String targetFieldId = fieldRef.getId();

							SwdDomainCond domainCond = new SwdDomainCond();
							domainCond.setFormId(targetFormId);
							SwdDomain domain = SwManagerFactory.getInstance().getSwdManager().getDomain(cUser.getId(), domainCond, IManager.LEVEL_ALL);
							
							SwdField[] swdField = domain.getFields();
							SwdField targetSwdField = null;
							for (int i = 0; i < swdField.length; i++) {
								SwdField tempField = swdField[i];
								if (tempField.getFormFieldId().equalsIgnoreCase(targetFieldId)) {
									targetSwdField = tempField;
									break;
								}
							}
							if (targetSwdField == null)
								continue;

							SwdRecordCond recordCond = new SwdRecordCond();
							recordCond.setFormId(targetFormId);
							recordCond.setFilter(new Filter[]{new Filter("=", targetSwdField.getTableColumnName(), value)});
							SwdRecord[] records = SwManagerFactory.getInstance().getSwdManager().getRecords(cUser.getId(), recordCond, IManager.LEVEL_LITE);
							if (records == null || records.length != 1) //같은게 하나이상이면 입력을 포기한다
								continue;
							
							//다른업무 참조
							   /*0   =   {
							      refForm=frm_board_SYSTEM,
							      refFormField=2,
							      refRecordId=dr_402880ea396b54a001396b54a09d0000,
							      value=게시판에서 글을 남겨요
							   },*/
							Map refDataMap = new LinkedHashMap();
							refDataMap.put("refForm", targetFormId);
							refDataMap.put("refFormField", targetFieldId);
							refDataMap.put("refRecordId", records[0].getRecordId());
							refDataMap.put("value", value);
							smartForm.put(field.getId(), refDataMap);
							
						} else {
							smartForm.put(field.getId(), value);
						}
					}else if(user!=null){
						Map<String, Object> usersMap = new LinkedHashMap<String, Object>();
						List<Map<String, Object>> userArrayMap = new ArrayList<Map<String, Object>>();
						Map<String, Object> userMap = new HashMap<String, Object>();
						userMap.put("id", user.getId());
						userMap.put("name", user.getLongName());
						userArrayMap.add(userMap);
						usersMap.put("users", userArrayMap);
						smartForm.put(field.getId(), usersMap);
					}
					
				}
				
				if(smartForm.isEmpty()) continue;
				
				requestMap.put("frmSmartForm", smartForm);
				String instanceId = null;
				try{
					instanceId = instanceService.setInformationWorkInstance(requestMap, request);
				}catch (Exception e){
					e.printStackTrace();
				}
				if(instanceId!=null) count++;
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		} finally {
		}
		return count;
	}
	
	@Override
	public void uploadYTVideo(HttpServletRequest request, HttpServletResponse response) throws DocFileException {
		IFileModel formFile = new HbFileModel();
		String fileId = IDCreator.createId(SmartServerConstant.TEMP_ABBR);
		formFile.setId(fileId);

		//this.setFileDirectory(System.getenv("SMARTWORKS_FILE_DIRECTORY") == null ? System.getProperty("user.home") : System.getenv("SMARTWORKS_FILE_DIRECTORY"));
		this.setFileDirectory(OSValidator.getImageDirectory());
		String companyId = SmartUtil.getCurrentUser().getCompanyId();

		String fileDivision = FILE_DIVISION_TEMPS;
		File repository = this.getFileRepository(companyId, fileDivision, null, null);
		String filePath = "";
		String imagerServerPath = "";
		String extension = "";
		if (formFile != null) {
			String fileName = "";
			try {
				fileName = URLDecoder.decode(request.getHeader("X-File-Name"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if (fileName.indexOf(File.separator) > 1)
				fileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1);

			formFile.setFileName(fileName);
			
			extension = fileName.lastIndexOf(".") > 1 ? fileName.substring(fileName.lastIndexOf(".") + 1) : null;
			filePath = repository.getAbsolutePath() + File.separator + (String) fileId;

			imagerServerPath = SmartConfUtil.getInstance().getImageServer() + companyId + "/" + FILE_DIVISION_TEMPS + "/" + fileId + "." + extension;
			formFile.setImageServerPath(imagerServerPath);

			if (extension != null) {
				filePath = filePath + "." + extension;
			}
			formFile.setFileSize(Long.parseLong(request.getHeader("Content-Length")));

			formFile.setFilePath(filePath);

		}

		this.uploadAjaxYTVideo(request, response, formFile);

	}
	public String insertWorkManualFile(String workId, String fileId, String fileName) throws DocFileException {
		try {
			if (fileName.indexOf(File.separator) > 1)
				fileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1);

			String extension = fileName.lastIndexOf(".") > 1 ? fileName.substring(fileName.lastIndexOf(".") + 1) : null;
			if(!CommonUtil.isEmpty(extension))
				extension = extension.toLowerCase();

			User user = SmartUtil.getCurrentUser();
			String companyId = "Maninsoft";
			if (user != null) {
				companyId = user.getCompanyId();
			} else {
				ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();
				
				SwoCompanyCond cond = new SwoCompanyCond();
				SwoCompany[] companys = swoMgr.getCompanys("", cond, IManager.LEVEL_LITE);
				if (companys != null) {
					companyId = companys[0].getId();
				}				
			}

			String tempFilePath = this.getFileDirectory() + "/SmartFiles/" + companyId + "/" + FILE_DIVISION_TEMPS + "/" + fileId + "." + extension;

			//String realFile = OSValidator.getImageDirectory() + "/" + originId + "." + extension;
			StringBuffer realFilePath = new StringBuffer(OSValidator.getImageDirectory()).append("/SmartFiles/").append(SmartUtil.getCurrentUser().getCompanyId()).append("/").append(Work.WORKDEF_IMG_DIR).append("/");
			realFilePath.append("/").append(workId).append("/").append(fileName);
			
			File tempFile = new File(tempFilePath);
			File targetFile = new File(realFilePath.toString());
			
			File dir = targetFile.getParentFile();
			if(dir != null && !dir.exists())
				dir.mkdirs();
			
			FileInputStream is = new FileInputStream(tempFile);
			FileOutputStream os = new FileOutputStream(targetFile);
			IOUtils.copy(is, os);
			is.close();
			os.close();

			return fileName;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public String insertProfilesFile(String fileId, String fileName, String communityId) throws DocFileException {

		try {
			//this.setFileDirectory(SmartConfUtil.getInstance().getImageServerDirectory());
			//this.setFileDirectory(System.getenv("SMARTWORKS_FILE_DIRECTORY") == null ? System.getProperty("user.home") : System.getenv("SMARTWORKS_FILE_DIRECTORY"));
			this.setFileDirectory(OSValidator.getImageDirectory());

			if (fileName.indexOf(File.separator) > 1)
				fileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1);

			String extension = fileName.lastIndexOf(".") > 1 ? fileName.substring(fileName.lastIndexOf(".") + 1) : null;
			if(!CommonUtil.isEmpty(extension))
				extension = extension.toLowerCase();

			User user = SmartUtil.getCurrentUser();
			String companyId = "Maninsoft";
			if (user != null) {
				companyId = user.getCompanyId();
			} else {
				ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();
				
				SwoCompanyCond cond = new SwoCompanyCond();
				SwoCompany[] companys = swoMgr.getCompanys("", cond, IManager.LEVEL_LITE);
				if (companys != null) {
					companyId = companys[0].getId();
				}				
			}

			File repository = this.getFileRepository(companyId, FILE_DIVISION_PROFILES, null, null);

			String communityPictureId = communityId + "." + extension;
			//String bigId = null;
			String thumbId = null;
			String originId = null;
			//String realFile1 = null;
			String realFile2 = null;
			String tempFile = this.getFileDirectory() + "/SmartFiles/" + companyId + "/" + FILE_DIVISION_TEMPS + "/" + fileId + "." + extension;

			if(communityId.equals(companyId + CompanyGeneral.IMAGE_TYPE_LOGO) || communityId.equals(companyId + CompanyGeneral.IMAGE_TYPE_LOGINIMAGE)) {
				originId = communityId;
			} else {
				//bigId = communityId + "_big";
				thumbId = communityId + Community.IMAGE_TYPE_THUMB;
				originId = communityId + Community.IMAGE_TYPE_ORIGINAL;
				//realFile1 = repository.getAbsolutePath() + "/" + bigId + "." + extension;
				realFile2 = repository.getAbsolutePath() + "/" + thumbId + "." + extension;
				//Thumbnail.createImage(tempFile, realFile1, "big", extension);
				Thumbnail.createImage(tempFile, realFile2, "thumb", extension);
			}
			String realFile = repository.getAbsolutePath() + "/" + originId + "." + extension;

			FileInputStream is = new FileInputStream(new File(tempFile));
			FileOutputStream os = new FileOutputStream(new File(realFile));
			IOUtils.copy(is, os);
			is.close();
			os.close();

			return communityPictureId;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public void insertFiles(String workType, String taskInstId, String groupId, String tempFileId, String fileName, String fileSize) throws DocFileException {

		try {
			//this.setFileDirectory(System.getenv("SMARTWORKS_FILE_DIRECTORY") == null ? System.getProperty("user.home") : System.getenv("SMARTWORKS_FILE_DIRECTORY"));
			this.setFileDirectory(OSValidator.getImageDirectory());

			if (fileName.indexOf(File.separator) > 1)
				fileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1);

			String extension = fileName.lastIndexOf(".") > 1 ? fileName.substring(fileName.lastIndexOf(".") + 1) : null;

			User user = SmartUtil.getCurrentUser();
			String companyId = "Maninsoft";
			if (user != null) {
				companyId = user.getCompanyId();
			} else {
				ISwoManager swoMgr = SwManagerFactory.getInstance().getSwoManager();
				
				SwoCompanyCond cond = new SwoCompanyCond();
				SwoCompany[] companys = swoMgr.getCompanys("", cond, IManager.LEVEL_LITE);
				if (companys != null) {
					companyId = companys[0].getId();
				}
			}
			File repository = this.getFileRepository(companyId, workType, null, null);
			String fileId = tempFileId.split("temp_")[tempFileId.split("temp_").length-1];
			if(workType.equals("Pictures")) fileId = "pic_" + fileId;
			else fileId = "file_" + fileId;

			String tempFile = this.getFileDirectory() + "/SmartFiles/" + companyId + "/" + FILE_DIVISION_TEMPS + "/" + tempFileId + "." + extension;
			File file = new File(tempFile);
			if(!file.exists())
				return;
			String realFile = repository.getAbsolutePath() + File.separator + fileId + "." + extension;
			if(workType.equals("Pictures")) {
				String thumbFile = repository.getAbsolutePath() + File.separator + fileId + Community.IMAGE_TYPE_THUMB + "." + extension;
				Thumbnail.createImage(tempFile, thumbFile, "thumb", extension);
			}

			try {
				FileInputStream is = new FileInputStream(new File(tempFile));
				FileOutputStream os = new FileOutputStream(new File(realFile));
				IOUtils.copy(is, os);
				is.close();
				os.close();
			} catch (Exception e) {
				throw new DocFileException("Failed to copy file [" + tempFile + "]!");
			}

			// 그룹 아이디가 넘어 오지 않았다면 그룹아이디 설정
			if (groupId == null)
				groupId = IDCreator.createId(SmartServerConstant.DOCUMENT_GROUP_ABBR);

			// 그룹아이디, 파일 아이디 쌍 저장
			Query query = this.getSession().createSQLQuery("insert into SWDocGroup(tskInstanceId, groupId, docId) values ('" + taskInstId + "', '" + groupId + "', '" + fileId + "')");
			query.executeUpdate();

			// 파일 정보 저장
			IFileModel formFile = new HbFileModel();
			formFile.setId(fileId);
			formFile.setFileName(fileName);
			formFile.setWrittenTime(new Date(new LocalDate().getGMTDate()));
			formFile.setFilePath(realFile);
			formFile.setFileSize(Long.parseLong(fileSize));
			formFile.setType(extension);
			formFile.setDeleteAction(false);
			this.getHibernateTemplate().save(formFile);

			File deleteFile = new File(tempFile);
			if(deleteFile.exists())
				deleteFile.delete();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

	@Override
	public String deleteTempFile() throws DocFileException {

		String returnValue = "";
		//this.setFileDirectory(System.getenv("SMARTWORKS_FILE_DIRECTORY") == null ? System.getProperty("user.home") : System.getenv("SMARTWORKS_FILE_DIRECTORY"));
		this.setFileDirectory(OSValidator.getImageDirectory());

		User user = SmartUtil.getCurrentUser();
		String tempFilePath = this.getFileDirectory() + "/SmartFiles/" + user.getCompanyId() + "/" + FILE_DIVISION_TEMPS + "/";

		try {
			File tempFileDir = new File(tempFilePath);
			String[] tempFiles = tempFileDir.list();
			for(int i=0; i<tempFiles.length; i++) {
				File tempFile = new File(tempFiles[i]);
				if(tempFile.exists()) {
					returnValue += tempFile.getName();
					tempFile.delete();
				} else {
					returnValue = "tempFile not exists...";
				}
			}
			returnValue += " : delete tempFile success...";
		} catch (Exception e) {
			returnValue = "delete tempFile fail... : " + e.getMessage();
			return returnValue;
		}

		return returnValue;
	}

	public IFileModel[] getFilesByTaskInstId(String taskInstId) throws DocFileException {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("select doc.tskinstanceid, docfile.id, docfile.type, docfile.fileName, docfile.filePath, docfile.fileSize, docfile.writtentime");
		stringBuffer.append("  from swdocgroup doc, swfile docfile");
		stringBuffer.append(" where doc.docid = docfile.id");
		stringBuffer.append("   and doc.tskinstanceid = :taskInstId");
		stringBuffer.append(" order by docfile.writtenTime desc");

		Query query = this.getSession().createSQLQuery(stringBuffer.toString());
		if (!CommonUtil.isEmpty(taskInstId))
			query.setString("taskInstId", taskInstId);

		List list = query.list();

		if (CommonUtil.isEmpty(list))
			return null;
		List<IFileModel> objList = new ArrayList<IFileModel>();
		for (Iterator itr = list.iterator(); itr.hasNext();) {
			Object[] fields = (Object[]) itr.next();
			IFileModel obj = new HbFileModel();
			int j = 0;
			obj.setTskInstanceId((String)fields[j++]);
			obj.setId((String)fields[j++]);
			obj.setType((String)fields[j++]);
			obj.setFileName((String)fields[j++]);
			obj.setFilePath((String)fields[j++]);
			obj.setFileSize(Long.parseLong(String.valueOf(fields[j++])));
			obj.setWrittenTime((Timestamp)fields[j++]);
			objList.add(obj);
		}
		IFileModel[] fileModels = new HbFileModel[objList.size()];
		objList.toArray(fileModels);

		return fileModels;
	}
	public FileDownloadHistory getFileDownloadHistoryInfoByFileId(String fileId) throws DocFileException {
		if (CommonUtil.isEmpty(fileId))
			return null;
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(" select f.id, f.filename, g.tskinstanceid, t.tskname, t.tskprcinstid, p.prctitle, p.prcPackageId, pkg.name as pkgName ");
		stringBuffer.append(" from swfile f, swdocgroup g, tsktask t, prcprcinst p, swpackage pkg ");
		stringBuffer.append(" where f.id='").append(fileId).append("' ");
		stringBuffer.append(" and f.id = g.docid ");
		stringBuffer.append(" and t.tskobjid = g.tskinstanceid ");
		stringBuffer.append(" and p.prcobjid = t.tskprcinstid ");
		stringBuffer.append(" and p.prcPackageid = pkg.packageid ");
		
		Query query = this.getSession().createSQLQuery(stringBuffer.toString());

		List list = query.list();
		if (CommonUtil.isEmpty(list))
			return null;

		List<FileDownloadHistory> objList = new ArrayList<FileDownloadHistory>();
		for (Iterator itr = list.iterator(); itr.hasNext();) {
			Object[] fields = (Object[]) itr.next();
			FileDownloadHistory obj = new FileDownloadHistory();
			int j = 0;
			obj.setFileId((String)fields[j++]);
			obj.setFileName((String)fields[j++]);
			obj.setRefTaskId((String)fields[j++]);
			obj.setRefTaskName((String)fields[j++]);
			obj.setRefPrcInstId((String)fields[j++]);
			obj.setRefPrcInstName((String)fields[j++]);
			obj.setRefPackageId((String)fields[j++]);
			obj.setRefPackageName((String)fields[j++]);
			objList.add(obj);
		}
		FileDownloadHistory[] fileModels = new FileDownloadHistory[objList.size()];
		objList.toArray(fileModels);

		return fileModels[0];
		
	}
	
	public IFileModel getFileById(String fileId) throws DocFileException {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("select id, type, fileName, filePath, fileSize, writtenTime");
		stringBuffer.append("  from SwFile");
		stringBuffer.append(" where id = :fileId");

		Query query = this.getSession().createSQLQuery(stringBuffer.toString());
		if (!CommonUtil.isEmpty(fileId))
			query.setString("fileId", fileId);

		List list = query.list();
		if (CommonUtil.isEmpty(list))
			return null;

		List<IFileModel> objList = new ArrayList<IFileModel>();
		for (Iterator itr = list.iterator(); itr.hasNext();) {
			Object[] fields = (Object[]) itr.next();
			IFileModel obj = new HbFileModel();
			int j = 0;
			obj.setId((String)fields[j++]);
			obj.setType((String)fields[j++]);
			obj.setFileName((String)fields[j++]);
			obj.setFilePath((String)fields[j++]);
			Object object = fields[j++];
			obj.setFileSize(object == null ? 0 : Long.parseLong(String.valueOf(object)));
			obj.setWrittenTime((Timestamp)fields[j++]);
			objList.add(obj);
		}
		IFileModel[] fileModels = new HbFileModel[objList.size()];
		objList.toArray(fileModels);

		return fileModels[0];
	}

	private Query appendQuery(String user, StringBuffer queryBuffer, FileWorkCond cond) throws Exception {

		String fileId = cond.getFileId();
		String tskAssignee = cond.getTskAssignee();
		String tskAssigneeOrTskSpaceId = cond.getTskAssigneeOrSpaceId();
		//assingnedOnly 값이 true 라면 실행중인(11) 태스크만 조회를 한다.
		String tskStatus =  cond.getTskStatus();
		String prcStatus = cond.getPrcStatus();
		Date lastInstanceDate = cond.getLastInstanceDate();
		String tskRefType = cond.getTskRefType();
		String folderId = cond.getFolderId();
		String writtenTimeMonthString = cond.getWrittenTimeMonthString();
		String packageId = cond.getPackageId();
		String packageStatus = cond.getPackageStatus();
		String fileType = cond.getFileType();
		int pageNo = cond.getPageNo();
		int pageSize = cond.getPageSize();

		String searchKey = cond.getSearchKey();

		String worksSpaceId = cond.getTskWorkSpaceId();
		Date executionDateFrom = cond.getTskExecuteDateFrom();
		Date executionDateTo = cond.getTskExecuteDateTo();
		Filters[] filtersArray = cond.getFilters();

		String[] workSpaceIdNotIns = cond.getWorkSpaceIdNotIns();
		String[] likeAccessValues = cond.getLikeAccessValues();

		queryBuffer.append("from ");
		queryBuffer.append("( ");
		queryBuffer.append("  select docfile.id as fileId ");
		queryBuffer.append("	    , docfile.type as fileType ");
		queryBuffer.append("	    , docfile.fileName ");
		queryBuffer.append("	    , docfile.filePath ");
		queryBuffer.append("	    , docfile.fileSize ");
		queryBuffer.append("	    , docfile.writtenTime ");
		queryBuffer.append("	    , docgroup.groupId ");
		queryBuffer.append("	    , folder.id as folderId ");
		queryBuffer.append("	    , folder.name as folderName ");
		queryBuffer.append("	    , task.tskobjId ");
		queryBuffer.append("		, task.tsktitle ");
		queryBuffer.append("		, task.tskDoc ");
		queryBuffer.append("		, task.tsktype ");
		queryBuffer.append("		, task.tskReftype ");
		queryBuffer.append("		, task.tskstatus ");
		queryBuffer.append("		, task.tskCreateUser ");
		queryBuffer.append("		, task.tskassignee ");
		queryBuffer.append("		, case when task.tskstatus='11' then task.tskassigndate else task.tskexecuteDate end as taskLastModifyDate ");
		queryBuffer.append("		, task.tskcreatedate ");
		queryBuffer.append("		, task.tskname ");
		queryBuffer.append("		, task.tskprcinstid ");
		queryBuffer.append("		, task.tskform ");
		queryBuffer.append("		, task.isStartActivity ");
		queryBuffer.append("		, task.tskWorkSpaceId ");
		queryBuffer.append("		, task.tskWorkSpaceType ");
		queryBuffer.append("		, task.tskAccessLevel ");
		queryBuffer.append("		, task.tskAccessValue ");
		queryBuffer.append("		, task.tskDef ");
		queryBuffer.append("		, form.packageId ");
		queryBuffer.append("		, pkg.name as packageName ");
		queryBuffer.append("		, pkg.status as packageStatus ");
		queryBuffer.append("		, ctg.id as childCtgId ");
		queryBuffer.append("		, ctg.name as childCtgName ");
		queryBuffer.append("		, case when ctg.parentId = '_PKG_ROOT_' then null else ctg2.id end as parentCtgId ");
		queryBuffer.append("		, case when ctg.parentId = '_PKG_ROOT_' then null else ctg2.name end as parentCtgName ");
		queryBuffer.append("	from tsktask task ");
		queryBuffer.append("		right outer join ");
		queryBuffer.append("		swdocgroup docgroup ");
		queryBuffer.append("		on task.tskobjid = docgroup.tskinstanceId ");
		queryBuffer.append("		left outer join ");
		queryBuffer.append("		swfile docfile ");
		queryBuffer.append("		on docgroup.docId = docfile.id ");
		queryBuffer.append("		left outer join ");
		queryBuffer.append("		swfolderfile folderfile ");
		queryBuffer.append("		on folderfile.fileid = docfile.id ");
		queryBuffer.append("		left outer join ");
		queryBuffer.append("		swfolder folder ");
		queryBuffer.append("		on folder.id = folderfile.folderid ");
		queryBuffer.append("		, swform form ");
		queryBuffer.append("		left outer join ");
		queryBuffer.append("		swpackage pkg ");
		queryBuffer.append("		on form.packageId = pkg.packageId ");
		queryBuffer.append("		left outer join ");
		queryBuffer.append("		swcategory ctg ");
		queryBuffer.append("		on ctg.id = pkg.categoryId ");
		queryBuffer.append("		left outer join ");
		queryBuffer.append("		swcategory ctg2 ");
		queryBuffer.append("		on ctg.parentId = ctg2.id ");
		queryBuffer.append("	where tsktype not in ('and','route','SUBFLOW','xor') ");
		queryBuffer.append("	and task.tskform = form.formid ");
		if (!CommonUtil.isEmpty(packageStatus))
			queryBuffer.append("	and pkg.status = :packageStatus ");
		/*if (!CommonUtil.isEmpty(filtersArray)) {
			for(Filters filters : filtersArray) {
				Filter[] filterArray = filters.getFilter();
				if (!CommonUtil.isEmpty(filterArray)) {
					for(Filter filter : filterArray) {
						String leftOperandValue = filter.getLeftOperandValue();
						String operator = filter.getOperator();
						String rightOperandValue = filter.getRightOperandValue();
						if(rightOperandValue.equals(FileCategory.ID_UNCATEGORIZED)) {
							queryBuffer.append("and ").append(leftOperandValue).append(" is null ");
						} else {
							if(!leftOperandValue.equals("writtenTime"))
								queryBuffer.append("and ").append(leftOperandValue).append(" ").append(operator).append(" :").append(leftOperandValue).append(" ");
						}
					}
				}
			}
		}*/
		if (!CommonUtil.isEmpty(folderId)) {
			if(folderId.equals(FileCategory.ID_UNCATEGORIZED)) queryBuffer.append("	and folder.id is null ");
			else queryBuffer.append("	and folder.id = :folderId ");
		}
		if (!CommonUtil.isEmpty(packageId)) {
			queryBuffer.append("	and form.packageId = :packageId ");
		}
		if (!CommonUtil.isEmpty(writtenTimeMonthString)) {
			if(this.getDbType().equalsIgnoreCase("sqlserver"))
				queryBuffer.append("	and datename(yy, docfile.writtenTime) + '.' + datename(mm, docfile.writtenTime) = :writtenTimeMonthString ");
			else if(this.getDbType().equalsIgnoreCase("oracle"))
				queryBuffer.append("	and to_char(docfile.writtenTime, 'YYYY') || '.' || to_char(docfile.writtenTime, 'MM') = :writtenTimeMonthString ");
		}
		if (!CommonUtil.isEmpty(fileType))
			queryBuffer.append("	and docfile.type = :fileType ");
		if (!CommonUtil.isEmpty(tskAssignee))
			queryBuffer.append("	and task.tskassignee = :tskAssignee ");
		if (!CommonUtil.isEmpty(tskAssigneeOrTskSpaceId))
			queryBuffer.append("	and (task.tskassignee = :tskAssigneeOrTskSpaceId or task.tskWorkSpaceId = :tskAssigneeOrTskSpaceId) ");
		if (!CommonUtil.isEmpty(tskStatus))
			queryBuffer.append("	and task.tskstatus = :tskStatus ");
		if (!CommonUtil.isEmpty(worksSpaceId))
			queryBuffer.append("	and task.tskWorkSpaceId = :worksSpaceId ");
		if (executionDateFrom != null)
			queryBuffer.append("	and task.tskExecuteDate > :executionDateFrom ");
		if (executionDateTo != null)
			queryBuffer.append("	and task.tskExecuteDate < :executionDateTo ");
		if (searchKey != null)
			queryBuffer.append("	and (docfile.fileName like :searchKey or task.tskTitle like :searchKey )");
		
		queryBuffer.append(") taskInfo ");
		//queryBuffer.append("left outer join ");
		queryBuffer.append("join ");
		queryBuffer.append("( ");
		queryBuffer.append("	select ");
		queryBuffer.append("		 prcInst.prcObjId ");
		queryBuffer.append("		, prcInst.prcTitle ");
		queryBuffer.append("		, prcInst.prcType ");
		queryBuffer.append("		, prcInst.prcStatus ");
		queryBuffer.append("		, prcInst.prcCreateUser ");
		queryBuffer.append("		, prcInst.prcDid ");
		queryBuffer.append("		, prcInst.prcPrcId ");
		queryBuffer.append("		, prcInst.prcCreateDate ");
		queryBuffer.append("		, prcInst.prcWorkSpaceId ");
		queryBuffer.append("		, prcInst.prcWorkSpaceType ");
		queryBuffer.append("		, prcInst.prcAccessLevel ");
		queryBuffer.append("		, prcInst.prcAccessValue ");
		queryBuffer.append("		, prcInstInfo.lastTask_tskobjid ");
		queryBuffer.append("		, prcInstInfo.lastTask_tskname ");
		queryBuffer.append("		, prcInstInfo.lastTask_tskcreateuser ");
		queryBuffer.append("		, prcInstInfo.lastTask_tskcreateDate ");
		queryBuffer.append("		, prcInstInfo.lastTask_tskstatus ");
		queryBuffer.append("		, prcInstInfo.lastTask_tsktype ");
		queryBuffer.append("		, prcInstInfo.lastTask_tsktitle ");
		queryBuffer.append("		, prcInstInfo.lastTask_tskassignee ");
		queryBuffer.append("		, prcInstInfo.lastTask_tskexecuteDate ");
		queryBuffer.append("		, prcInstInfo.lastTask_tskduedate ");
		queryBuffer.append("		, prcInstInfo.lastTask_tskform ");
		queryBuffer.append("		, prcInstInfo.lastTask_tskWorkSpaceId ");
		queryBuffer.append("		, prcInstInfo.lastTask_tskWorkSpaceType ");
		queryBuffer.append("		, (select count(*) from tsktask where tskstatus='11' and tsktype='common' and tskprcInstId = prcInst.prcObjid) as lastTaskCount ");
		queryBuffer.append("	from  ");
		queryBuffer.append("		prcprcinst prcInst,  ");
		queryBuffer.append("		( ");
		queryBuffer.append("			select a.tskprcinstid as lastTask_tskprcinstid ");
		queryBuffer.append("					, task.tskobjid as lastTask_tskobjid ");
		queryBuffer.append("					, task.tskname as lastTask_tskname ");
		queryBuffer.append("					, task.tskcreateuser as lastTask_tskcreateuser ");
		queryBuffer.append("					, task.tskcreateDate as lastTask_tskcreateDate ");
		queryBuffer.append("					, task.tskstatus as lastTask_tskstatus ");
		queryBuffer.append("					, task.tsktype as lastTask_tsktype ");
		queryBuffer.append("					, task.tsktitle as lastTask_tsktitle ");
		queryBuffer.append("					, task.tskassignee as lastTask_tskassignee ");
		queryBuffer.append("					, task.tskexecuteDate as lastTask_tskexecuteDate ");
		queryBuffer.append("					, task.tskduedate as lastTask_tskduedate ");
		queryBuffer.append("					, task.tskform as lastTask_tskform ");
		queryBuffer.append("					, task.tskWorkSpaceId as lastTask_tskWorkSpaceId ");
		queryBuffer.append("					, task.tskWorkSpaceType as lastTask_tskWorkSpaceType ");
		queryBuffer.append("			from ( ");
		queryBuffer.append("					select tskprcinstId , max(tskCreatedate) as createDate  ");
		queryBuffer.append("					from tsktask  ");
		queryBuffer.append("					where tsktype not in ('and','route','SUBFLOW','xor') ");
		queryBuffer.append("					group by tskprcinstid ");
		queryBuffer.append("				  ) a,	 ");
		queryBuffer.append("				  TskTask task		 ");
		queryBuffer.append("			where  ");
		queryBuffer.append("				a.createDate = task.tskcreatedate ");
		queryBuffer.append("		) prcInstInfo	 ");
		queryBuffer.append("	where ");
		queryBuffer.append("		prcInst.prcobjid=prcInstInfo.lastTask_tskprcinstid ");
		if (!CommonUtil.isEmpty(prcStatus))
			queryBuffer.append("		and prcInst.prcStatus = :prcStatus ");
		queryBuffer.append(") prcInstInfo ");
		queryBuffer.append("on taskInfo.tskPrcInstId = prcInstInfo.prcObjId ");
		queryBuffer.append("where 1=1 ");
		if (lastInstanceDate != null)
			queryBuffer.append("and taskInfo.tskCreateDate < :lastInstanceDate ");
		if (tskRefType != null) {
			if(tskRefType.equals(TskTask.TASKREFTYPE_NOTHING))
				queryBuffer.append("and taskInfo.tskReftype is null ");
			else 
				queryBuffer.append("and taskInfo.tskReftype = :tskRefType ");
		}

		if (workSpaceIdNotIns != null) {
			queryBuffer.append(" and");
			queryBuffer.append(" tskWorkSpaceId not in (");
			for (int j=0; j<workSpaceIdNotIns.length; j++) {
				if (j != 0)
					queryBuffer.append(", ");
				queryBuffer.append(":workSpaceIdNotIn").append(j);
			}
			queryBuffer.append(")");
		}
		String likeAccessValuesQuery = "tskAccessValue like '%%'";
		StringBuffer likeAccessValuesBuffer = new StringBuffer();
		String divisionUL = "";
		if(this.getDbType().equals("sqlserver"))
			divisionUL = "collate Korean_Wansung_CS_AS ";
		if(likeAccessValues != null) {
			for (int j=0; j<likeAccessValues.length; j++) {
				if(j==0)
					likeAccessValuesBuffer.append("tskAccessValue ").append(divisionUL).append("like :likeAccessValue").append(j);
				else
					likeAccessValuesBuffer.append(" or tskAccessValue ").append(divisionUL).append("like :likeAccessValue").append(j);
			}
			likeAccessValuesQuery = likeAccessValuesBuffer.toString();
		}
		queryBuffer.append(" and (tskAccessLevel is null or tskAccessLevel = '3' or (tskAccessLevel = '1' and tskCreateUser = '" + user + "') or (tskAccessLevel = '2' and (").append(likeAccessValuesQuery).append(" or tskCreateUser = '" + user + "'))) ");

		this.appendOrderQuery(queryBuffer, "taskInfo", cond);
		//queryBuffer.append("order by taskInfo.tskCreatedate desc ");

		Query query = this.getSession().createSQLQuery(queryBuffer.toString());

		if (pageSize > 0 || pageNo >= 0) {
			query.setFirstResult(pageNo * pageSize);
			query.setMaxResults(pageSize);
		}

		/*if (!CommonUtil.isEmpty(filtersArray)) {
			for(Filters filters : filtersArray) {
				Filter[] filterArray = filters.getFilter();
				if (!CommonUtil.isEmpty(filterArray)) {
					for(Filter filter : filterArray) {
						String leftOperandValue = filter.getLeftOperandValue();
						String rightOperandValue = filter.getRightOperandValue();
						if(!rightOperandValue.equals(FileCategory.ID_UNCATEGORIZED)) {
							if(!leftOperandValue.equals("writtenTime"))
								query.setString(leftOperandValue, rightOperandValue);
						}
					}
				}
			}
		}*/
		if (!CommonUtil.isEmpty(folderId) && !folderId.equals(FileCategory.ID_UNCATEGORIZED))
			query.setString("folderId", folderId);
		if (!CommonUtil.isEmpty(packageId))
			query.setString("packageId", packageId);
		if (!CommonUtil.isEmpty(writtenTimeMonthString))
			query.setString("writtenTimeMonthString", writtenTimeMonthString);
		if (!CommonUtil.isEmpty(fileType))
			query.setString("fileType", fileType);
		if (!CommonUtil.isEmpty(tskAssignee))
			query.setString("tskAssignee", tskAssignee);
		if (!CommonUtil.isEmpty(tskAssigneeOrTskSpaceId))
			query.setString("tskAssigneeOrTskSpaceId", tskAssigneeOrTskSpaceId);
		if (!CommonUtil.isEmpty(tskStatus))
			query.setString("tskStatus", tskStatus);
		if (lastInstanceDate != null)
			query.setTimestamp("lastInstanceDate", lastInstanceDate);
		if (!CommonUtil.isEmpty(worksSpaceId))
			query.setString("worksSpaceId", worksSpaceId);
		if (executionDateFrom != null)
			query.setTimestamp("executionDateFrom", executionDateFrom);
		if (executionDateTo != null)
			query.setTimestamp("executionDateTo", executionDateTo);
		if (!CommonUtil.isEmpty(prcStatus)) 
			query.setString("prcStatus", prcStatus);
		if (!CommonUtil.isEmpty(tskRefType) && !tskRefType.equals(TskTask.TASKREFTYPE_NOTHING)) 
			query.setString("tskRefType", tskRefType);
		if (!CommonUtil.isEmpty(searchKey)) 
			query.setString("searchKey", CommonUtil.toLikeString(searchKey));
		if (workSpaceIdNotIns != null) {
			for (int j=0; j<workSpaceIdNotIns.length; j++) {
				query.setString("workSpaceIdNotIn"+j, workSpaceIdNotIns[j]);
			}
		}
		if (likeAccessValues != null) {
			for (int j=0; j<likeAccessValues.length; j++) {
				query.setString("likeAccessValue"+j, CommonUtil.toLikeString(likeAccessValues[j]));
			}
		}
		if (!CommonUtil.isEmpty(packageStatus))
			query.setString("packageStatus", packageStatus);

		return query;
	}

	@Override
	public long getFileWorkListSize(String user, FileWorkCond cond) throws Exception {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			buf.append(" count(*) ");
			Query query = this.appendQuery(user, buf, cond);
			List list = query.list();
			
			long count =((Integer)list.get(0)).longValue();
			return count;
		} catch (PrcException e) {
			throw e;
		} catch (Exception e) {
			throw new PrcException(e);
		}
	}

	@Override
	public FileWork[] getFileWorkList(String user, FileWorkCond cond) throws Exception {
		try {
			StringBuffer queryBuffer = new StringBuffer();
			queryBuffer.append(" select taskInfo.*, ");
			queryBuffer.append(" prcInstInfo.* ");
			
			Query query = this.appendQuery(user, queryBuffer, cond);

			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			List objList = new ArrayList();
			for (Iterator itr = list.iterator(); itr.hasNext();) {
				Object[] fields = (Object[]) itr.next();
				FileWork obj = new FileWork();
				int j = 0;
				obj.setFileId((String)fields[j++]);
				obj.setFileType((String)fields[j++]);
				obj.setFileName((String)fields[j++]);
				obj.setFilePath((String)fields[j++]);
				Object object = fields[j++];
				obj.setFileSize(object == null ? 0 : Long.parseLong(String.valueOf(object)));
				obj.setWrittenTime((Timestamp)fields[j++]);
				obj.setGroupId((String)fields[j++]);
				obj.setFolderId((String)fields[j++]);
				obj.setFolderName((String)fields[j++]);
				obj.setTskObjId((String)fields[j++]);
				obj.setTskTitle((String)fields[j++]);
				String tempCountStr = null;
				if(this.getDbType().equals("sqlserver")) {
					Clob varData = (Clob)fields[j++];
					if (varData != null) {
						long length = varData.length();
						tempCountStr = varData.getSubString(1, (int)length);
					}
				} else {
					tempCountStr = (String)fields[j++];
				}
				obj.setTskDoc(tempCountStr);
				obj.setTskType((String)fields[j++]);     
				obj.setTskRefType((String)fields[j++]);     
				obj.setTskStatus((String)fields[j++]);   
				obj.setTskCreateUser((String)fields[j++]);
				obj.setTskAssignee((String)fields[j++]);
				obj.setTaskLastModifyDate((Timestamp)fields[j++]);
				obj.setTskCreateDate((Timestamp)fields[j++]);
				obj.setTskName((String)fields[j++]);
				obj.setTskPrcInstId((String)fields[j++]);
				obj.setTskForm((String)fields[j++]);
				obj.setIsStartActivity((String)fields[j++]);
				obj.setTskWorkSpaceId((String)fields[j++]);
				obj.setTskWorkSpaceType((String)fields[j++]);
				obj.setTskAccessLevel((String)fields[j++]);
				obj.setTskAccessValue((String)fields[j++]);
				obj.setTskDef((String)fields[j++]);
				obj.setPackageId((String)fields[j++]);
				obj.setPackageName((String)fields[j++]);
				obj.setPackageStatus((String)fields[j++]);
				obj.setChildCtgId((String)fields[j++]);
				obj.setChildCtgName((String)fields[j++]);
				obj.setParentCtgId((String)fields[j++]);
				obj.setParentCtgName((String)fields[j++]);
				obj.setPrcObjId((String)fields[j++]);
				obj.setPrcTitle((String)fields[j++]);
				obj.setPrcType((String)fields[j++]);
				obj.setPrcStatus((String)fields[j++]);
				obj.setPrcCreateUser((String)fields[j++]);
				obj.setPrcDid((String)fields[j++]);
				obj.setPrcPrcId((String)fields[j++]);
				obj.setPrcCreateDate((Timestamp)fields[j++]);
				obj.setPrcWorkSpaceId((String)fields[j++]);
				obj.setPrcWorkSpaceType((String)fields[j++]);
				obj.setPrcAccessLevel((String)fields[j++]);
				obj.setPrcAccessValue((String)fields[j++]);
				obj.setLastTskObjId((String)fields[j++]);                       
				obj.setLastTskName((String)fields[j++]);                        
				obj.setLastTskCreateUser((String)fields[j++]);                  
				obj.setLastTskCreateDate((Timestamp)fields[j++]);                  
				obj.setLastTskStatus((String)fields[j++]);                      
				obj.setLastTskType((String)fields[j++]);                        
				obj.setLastTskTitle((String)fields[j++]);                       
				obj.setLastTskAssignee((String)fields[j++]);                    
				obj.setLastTskExecuteDate((Timestamp)fields[j++]);                 
				obj.setLastTskDueDate((Timestamp)fields[j++]); 
				obj.setLastTskForm((String)fields[j++]);    
				obj.setLastTskWorkSpaceId((String)fields[j++]);
				obj.setLastTskWorkSpaceType((String)fields[j++]);
				Object lastTaskCountObj = fields[j];
				int lastTaskCount = 0;
				if(lastTaskCountObj == null) {
					lastTaskCount = -1;
				} else {
					if (lastTaskCountObj instanceof BigInteger) {
						lastTaskCount = ((BigInteger)lastTaskCountObj).intValue();
					} else if (lastTaskCountObj instanceof Long) {
						lastTaskCount = ((Long)lastTaskCountObj).intValue();
					} else {
						lastTaskCount = Integer.parseInt(lastTaskCountObj.toString());
					}
				}
				obj.setLastTskCount(lastTaskCount == 0 ? 1 : lastTaskCount);
				objList.add(obj);
			}
			list = objList;
			FileWork[] objs = new FileWork[list.size()];
			list.toArray(objs);
			return objs;
				
		} catch (Exception e) {
			throw new DocFileException(e);
		}
	}
	public String getDbType() {
		if (dbType == null) {
			SessionFactory sf = getSessionFactory();
			SessionFactoryImplementor sfi = (SessionFactoryImplementor)sf;
			Dialect dialect = sfi.getDialect();
			if (dialect instanceof PostgreSQLDialect) {
				dbType = "postgresql";
			} else if (dialect instanceof SQLServerDialect) {
				dbType = "sqlserver";
			} else {
				dbType = "oracle";
			}
		}
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	@Override
	public FileDownloadHistory getFileDownloadHistory(String user, String id, String level) throws DocFileException {
		try {
			if (level == null)
				level = LEVEL_ALL;
			if (level.equals(LEVEL_ALL)) {
				FileDownloadHistory obj = (FileDownloadHistory)this.get(FileDownloadHistory.class, id);
				return obj;
			} else {
				FileDownloadHistoryCond cond = new FileDownloadHistoryCond();
				cond.setObjId(id);
				return getFileDownloadHistory(user, cond, level);
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw new DocFileException(e);
		}
	}
	@Override
	public FileDownloadHistory getFileDownloadHistory(String user, FileDownloadHistoryCond cond, String level) throws DocFileException {
		if (level == null)
			level = LEVEL_ALL;
		cond.setPageSize(2);
		FileDownloadHistory[] objs = getFileDownloadHistorys(user, cond, level);
		if (CommonUtil.isEmpty(objs))
			return null;
		try {
			if (objs.length != 1)
				throw new DocFileException("More than 1 Object");
		} catch (DocFileException e) {
			logger.error(e, e);
			throw e;
		}
		return objs[0];
	}
	@Override
	public FileDownloadHistory setFileDownloadHistory(String user, FileDownloadHistory obj, String level) throws DocFileException {
		try {
			fill(user, obj);
			set(obj);
			return obj;
		} catch (DocFileException e) {
			throw e;
		} catch (Exception e) {
			throw new DocFileException(e);
		}
	}
	@Override
	public FileDownloadHistory createFileDownloadHistory(String user, FileDownloadHistory obj) throws DocFileException {
		try {
			fill(user, obj);
			create(obj);
			return obj;
		} catch (Exception e) {
			logger.error(e, e);
			throw new DocFileException(e);
		}
	}
	@Override
	public void removeFileDownloadHistory(String user, String id) throws DocFileException {
		try {
			remove(FileDownloadHistory.class, id);
		} catch (Exception e) {
			logger.error(e, e);
			throw new DocFileException(e);
		}
	}
	@Override
	public void removeFileDownloadHistory(String user, FileDownloadHistoryCond cond) throws DocFileException {
		FileDownloadHistory obj = getFileDownloadHistory(user, cond, null);
		if (obj == null)
			return;
		removeFileDownloadHistory(user, obj.getObjId());
	}
	private Query appendQuery(StringBuffer buf, FileDownloadHistoryCond cond) throws Exception {
		String objId = null;
		String fileId = null;
		String fileName = null;
		String downloadUserId = null;
		String refPackageId = null;
		String refPackageName = null;
		String refPrcInstId = null;
		String refPrcInstName = null;
		String refTaskId = null;
		String refTaskName = null;
		String creationUser = null;
		Date creationDate = null;
		String modificationUser = null;
		Date modificationDate = null;
		
		if (cond != null) {
			objId = cond.getObjId();
			fileId = cond.getFileId();
			fileName = cond.getFileName();
			downloadUserId = cond.getDownloadUserId();
			refPackageId = cond.getRefPackageId();
			refPackageName = cond.getRefPackageName();
			refPrcInstId = cond.getRefPrcInstId();
			refPrcInstName = cond.getRefPrcInstName();
			refTaskId = cond.getRefTaskId();
			refTaskName = cond.getRefTaskName();
			creationUser = cond.getCreationUser();
			creationDate = cond.getCreationDate();
			modificationUser = cond.getModificationUser();
			modificationDate = cond.getModificationDate();
		}
		buf.append(" from FileDownloadHistory obj");
		buf.append(" where obj.objId is not null");
		Map filterMap = new HashMap();
		//TODO 시간 검색에 대한 확인 필요
		if (cond != null) {
			if (objId != null)
				buf.append(" and obj.objId = :objId");
			if (fileId != null)
				buf.append(" and obj.fileId = :fileId");
			if (fileName != null)
				buf.append(" and obj.fileName = :fileName");
			if (downloadUserId != null)
				buf.append(" and obj.downloadUserId = :downloadUserId");
			if (refPackageId != null)
				buf.append(" and obj.refPackageId = :refPackageId");
			if (refPackageName != null)
				buf.append(" and obj.refPackageName = :refPackageName");
			if (refPrcInstId != null)
				buf.append(" and obj.refPrcInstId = :refPrcInstId");
			if (refPrcInstName != null)
				buf.append(" and obj.refPrcInstName = :refPrcInstName");
			if (refTaskId != null)
				buf.append(" and obj.refTaskId = :refTaskId");
			if (refTaskName != null)
				buf.append(" and obj.refTaskName = :refTaskName");
			if (creationUser != null)
				buf.append(" and obj.creationUser = :creationUser");
			if (creationDate != null)
				buf.append(" and obj.creationDate = :creationDate");
			if (modificationUser != null)
				buf.append(" and obj.modificationUser = :modificationUser");
			if (modificationDate != null)
				buf.append(" and obj.modificationDate = :modificationDate");
		}
		this.appendOrderQuery(buf, "obj", cond);
		
		Query query = this.createQuery(buf.toString(), cond);
		if (cond != null) {
			if (objId != null)
				query.setString("objId", objId);
			if (fileId != null)
				query.setString("fileId", fileId);
			if (fileName != null)
				query.setString("fileName", fileName);
			if (downloadUserId != null)
				query.setString("downloadUserId", downloadUserId);
			if (refPackageId != null)
				query.setString("refPackageId", refPackageId);
			if (refPackageName != null)
				query.setString("refPackageName", refPackageName);
			if (refPrcInstId != null)
				query.setString("refPrcInstId", refPrcInstId);
			if (refPrcInstName != null)
				query.setString("refPrcInstName", refPrcInstName);
			if (refTaskId != null)
				query.setString("refTaskId", refTaskId);
			if (refTaskName != null)
				query.setString("refTaskName", refTaskName);
			if (creationUser != null)
				query.setString("creationUser", creationUser);
			if (creationDate != null)
				query.setTimestamp("creationDate", creationDate);
			if (modificationUser != null)
				query.setString("modificationUser", modificationUser);
			if (modificationDate != null)
				query.setTimestamp("modificationDate", modificationDate);
		}
		return query;
	}
	
	@Override
	public long getFileDownloadHistorySize(String user, FileDownloadHistoryCond cond) throws DocFileException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			buf.append(" count(obj)");
			Query query = this.appendQuery(buf,cond);
			List list = query.list();
			long count = ((Long)list.get(0)).longValue();
			return count;
		} catch (Exception e) {
			logger.error(e, e);
			throw new DocFileException(e);
		}
	}
	@Override
	public FileDownloadHistory[] getFileDownloadHistorys(String user, FileDownloadHistoryCond cond, String level) throws DocFileException {
		try {
			if (level == null)
				level = LEVEL_ALL;
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			buf.append(" obj");
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			FileDownloadHistory[] objs = new FileDownloadHistory[list.size()];
			list.toArray(objs);
			return objs;
		} catch (Exception e) {
			logger.error(e, e);
			throw new DocFileException(e);
		}
	}
}
