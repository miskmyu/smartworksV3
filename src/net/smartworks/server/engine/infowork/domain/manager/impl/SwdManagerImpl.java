/*	
 * $Id$
 * created by    : maninsoft
 * creation-date : 2011. 11. 7.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.infowork.domain.manager.impl;

import java.io.Reader;
import java.math.BigInteger;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.tagext.TryCatchFinally;

import net.smartworks.model.instance.FieldData;
import net.smartworks.model.security.AccessPolicy;
import net.smartworks.server.engine.common.manager.AbstractManager;
import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.model.Cond;
import net.smartworks.server.engine.common.model.Filter;
import net.smartworks.server.engine.common.model.Filters;
import net.smartworks.server.engine.common.model.InstanceVariable;
import net.smartworks.server.engine.common.model.InstanceVariables;
import net.smartworks.server.engine.common.model.Order;
import net.smartworks.server.engine.common.model.Property;
import net.smartworks.server.engine.common.script.exception.SctException;
import net.smartworks.server.engine.common.script.manager.ISctManager;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.common.util.ConnectionUtil;
import net.smartworks.server.engine.common.util.DateUtil;
import net.smartworks.server.engine.common.util.DbUtil;
import net.smartworks.server.engine.common.util.MisUtil;
import net.smartworks.server.engine.common.util.Wrapper;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.infowork.domain.exception.SwdException;
import net.smartworks.server.engine.infowork.domain.manager.ISwdManager;
import net.smartworks.server.engine.infowork.domain.model.SwdDataField;
import net.smartworks.server.engine.infowork.domain.model.SwdDataRef;
import net.smartworks.server.engine.infowork.domain.model.SwdDataRefCond;
import net.smartworks.server.engine.infowork.domain.model.SwdDomain;
import net.smartworks.server.engine.infowork.domain.model.SwdDomainCond;
import net.smartworks.server.engine.infowork.domain.model.SwdDomainFieldView;
import net.smartworks.server.engine.infowork.domain.model.SwdField;
import net.smartworks.server.engine.infowork.domain.model.SwdFieldCond;
import net.smartworks.server.engine.infowork.domain.model.SwdRecord;
import net.smartworks.server.engine.infowork.domain.model.SwdRecordCond;
import net.smartworks.server.engine.infowork.domain.model.SwdRecordExtend;
import net.smartworks.server.engine.infowork.form.exception.SwfException;
import net.smartworks.server.engine.infowork.form.manager.ISwfManager;
import net.smartworks.server.engine.infowork.form.model.SwfCondition;
import net.smartworks.server.engine.infowork.form.model.SwfConditions;
import net.smartworks.server.engine.infowork.form.model.SwfField;
import net.smartworks.server.engine.infowork.form.model.SwfFieldRef;
import net.smartworks.server.engine.infowork.form.model.SwfForm;
import net.smartworks.server.engine.infowork.form.model.SwfFormFieldDef;
import net.smartworks.server.engine.infowork.form.model.SwfFormLink;
import net.smartworks.server.engine.infowork.form.model.SwfFormRef;
import net.smartworks.server.engine.infowork.form.model.SwfFormat;
import net.smartworks.server.engine.infowork.form.model.SwfMapping;
import net.smartworks.server.engine.infowork.form.model.SwfMappings;
import net.smartworks.server.engine.infowork.form.model.SwfOperand;
import net.smartworks.server.engine.organization.model.SwoUser;
import net.smartworks.server.engine.process.process.manager.IPrcManager;
import net.smartworks.server.engine.process.process.model.PrcProcessInst;
import net.smartworks.server.engine.process.task.model.TskTask;
import net.smartworks.server.engine.process.task.model.TskTaskCond;
import net.smartworks.util.LocalDate;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.jdbc.object.SqlQuery;
import org.springframework.util.StringUtils;

public class SwdManagerImpl extends AbstractManager implements ISwdManager {

	private ISwfManager swfManager;
	private ISctManager sctManager;
	private String dbType;

	public SwdManagerImpl() {
		super();
		if (logger.isInfoEnabled())
			logger.info(this.getClass().getName() + " created");
	}
	public List executeSqlQuery(String qStr, Property[] params, Cond cond) throws SwdException {
		try {
			Query query = createSqlQuery(qStr, cond);
			if (!CommonUtil.isEmpty(params)) {
				for (Property param : params)
					query.setParameter(param.getName(), param.getValue());
			}
			List list = query.list();
			return list;
		} catch (Exception e) {
			throw new SwdException(e);
		}
	}
	public SwdDomain getDomain(String user, String objId, String level) throws SwdException {
		if (level == null)
			level = LEVEL_ALL;
		if (level.equals(LEVEL_ALL)) {
			try {
				SwdDomain obj = (SwdDomain)get(SwdDomain.class, objId);
				return obj;
			} catch (Exception e) {
				throw new SwdException(e);
			}
		} else {
			SwdDomainCond cond = new SwdDomainCond();
			cond.setObjId(objId);
			SwdDomain[] objs = this.getDomains(user, cond, level);
			if (CommonUtil.isEmpty(objs))
				return null;
			return objs[0];
		}
	}
	public SwdDomain getDomain(String user, SwdDomainCond cond, String level) throws SwdException {
		if (cond == null)
			return null;
		if (level == null)
			level = LEVEL_ALL;
		cond.setPageSize(2);
		SwdDomain[] domains = getDomains(user, cond, level);
		if (CommonUtil.isEmpty(domains))
			return null;
		if (domains.length > 1)
			throw new SwdException("More than 1 domain.");
		return domains[0];
	}
	public void setDomain(String user, SwdDomain obj, String level) throws SwdException {
		try {
			set(obj);
		} catch (Exception e) {
			throw new SwdException(e);
		}
	}
	public void mergeDomain(String user, SwdDomain obj, String level) throws SwdException {
		try {
			merge(obj);
		} catch (Exception e) {
			throw new SwdException(e);
		}
	}
	public void removeDomain(String user, String objId) throws SwdException {
		try {
			remove(SwdDomain.class, objId);
		} catch (Exception e) {
			throw new SwdException(e);
		}
	}
	private Query appendQuery(StringBuffer buf, SwdDomainCond cond) throws Exception {
		String objId = null;
		String formId = null;
		String formName = null;
		String formNameLike = null;
		String tableName = null;
		String keyColumn = null;
		String titleFieldId = null;
		String publishMode = null;
		Boolean isSystemDomain = false;
		String companyId = null;

		if (cond != null) {
			objId = cond.getObjId();
			formId = cond.getFormId();
			formName = cond.getFormName();
			formNameLike = cond.getFormNameLike();
			tableName = cond.getTableName();
			keyColumn = cond.getKeyColumn();
			titleFieldId = cond.getTitleFieldId();
			publishMode = cond.getPublishMode();
			isSystemDomain = cond.isSystemDomain();
			companyId = cond.getCompanyId();
		}
		buf.append(" from SwdDomain obj");
//		if (extProps != null && extProps.length != 0) {
//			for (int i=0; i<extProps.length; i++) {
//				buf.append(" left join obj.extendedProperties as extProp").append(i);
//			}
//		}
		buf.append(" where obj.objId is not null");
		if (cond != null) {
			if (objId != null)
				buf.append(" and obj.objId = :objId");
			if (formId != null)
				buf.append(" and obj.formId = :formId");
			if (formName != null)
				buf.append(" and obj.formName = :formName");
			if (formNameLike != null)
				buf.append(" and obj.formName like :formNameLike");
			if (tableName != null)
				buf.append(" and obj.tableName = :tableName");
			if (keyColumn != null)
				buf.append(" and obj.keyColumn = :keyColumn");
			if (titleFieldId != null)
				buf.append(" and obj.titleFieldId = :titleFieldId");
			if (publishMode != null)
				buf.append(" and obj.publishMode = :publishMode");
			if (isSystemDomain != null) {
				if (isSystemDomain) {
					buf.append(" and obj.systemDomainYn = 'Y'");
				} else {
					buf.append(" and (obj.systemDomainYn is null or obj.systemDomainYn = 'N')");
				}
			}
			if (companyId != null)
				buf.append(" and obj.companyId = :companyId");
		}
		this.appendOrderQuery(buf, "obj", cond);
		
		Query query = this.createQuery(buf.toString(), cond);
		if (cond != null) {
			if (objId != null)
				query.setString("objId", objId);
			if (formId != null)
				query.setString("formId", formId);
			if (formName != null)
				query.setString("formName", formName);
			if (formNameLike != null)
				query.setString("formNameLike", CommonUtil.toLikeString(formNameLike));
			if (tableName != null)
				query.setString("tableName", tableName);
			if (keyColumn != null)
				query.setString("keyColumn", keyColumn);
			if (titleFieldId != null)
				query.setString("titleFieldId", titleFieldId);
			if (publishMode != null)
				query.setString("publishMode", publishMode);
			if (companyId != null)
				query.setString("companyId", companyId);
		}
		return query;
	}
	public long getDomainSize(String user, SwdDomainCond cond) throws SwdException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			buf.append(" count(obj)");
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			long count =((Long)list.get(0)).longValue();
			return count;
		} catch (Exception e) {
			throw new SwdException(e);
		}
	}
	public SwdDomain[] getDomains(String user, SwdDomainCond cond, String level) throws SwdException {
		try {
			if (level == null)
				level = LEVEL_LITE;
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			if (level.equals(LEVEL_ALL)) {
				buf.append(" obj");
			} else {
				buf.append(" obj.objId, obj.formId, obj.formVersion, obj.formName");
				buf.append(", obj.tableName, obj.keyColumn, obj.titleFieldId, obj.keyDuplicable");
				buf.append(", obj.systemDomainYn, obj.publishMode, obj.companyId");
			}
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			if (!level.equals(LEVEL_ALL)) {
				List<SwdDomain> objList = new ArrayList<SwdDomain>();
				for (Iterator itr = list.iterator(); itr.hasNext();) {
					Object[] fields = (Object[]) itr.next();
					SwdDomain obj = new SwdDomain();
					int j = 0;
					obj.setObjId((String)fields[j++]);
					obj.setFormId((String)fields[j++]);
					obj.setFormVersion(CommonUtil.toInt(fields[j++]));
					obj.setFormName((String)fields[j++]);
					obj.setTableName((String)fields[j++]);
					obj.setKeyColumn((String)fields[j++]);
					obj.setTitleFieldId((String)fields[j++]);
					obj.setKeyDuplicable(CommonUtil.toBoolean(fields[j++]));
					obj.setSystemDomain(CommonUtil.toBoolean(fields[j++]));
					obj.setPublishMode((String)fields[j++]);
					obj.setCompanyId((String)fields[j++]);
					objList.add(obj);
				}
				list = objList;
			}
			SwdDomain[] objs = new SwdDomain[list.size()];
			list.toArray(objs);
			return objs;
		} catch (Exception e) {
			throw new SwdException(e);
		}
	}

	private Query appendQuery(StringBuffer buf, SwdFieldCond cond) throws Exception {
		String objId = null;
		String domainObjId = null;
		String formId = null;
		String formFieldId = null;
		if (cond != null) {
			objId = cond.getObjId();
			domainObjId = cond.getDomainObjId();
			formId = cond.getFormId();
			formFieldId = cond.getFormFieldId();
		}
		buf.append(" from SwdField obj");
//		if (extProps != null && extProps.length != 0) {
//			for (int i=0; i<extProps.length; i++) {
//				buf.append(" left join obj.extendedProperties as extProp").append(i);
//			}
//		}
		buf.append(" where obj.objId is not null");
		if (cond != null) {
			if (objId != null)
				buf.append(" and obj.objId = :objId");
			if (domainObjId != null)
				buf.append(" and obj.domain.objId = :domainObjId");
			if (formId != null)
				buf.append(" and obj.domain.formId = :formId");
			if (formFieldId != null)
				buf.append(" and obj.formFieldId = :formFieldId");
		}
		this.appendOrderQuery(buf, "obj", cond);
		
		Query query = this.createQuery(buf.toString(), cond);
		if (cond != null) {
			if (objId != null)
				query.setString("objId", objId);
			if (domainObjId != null)
				query.setString("domainObjId", domainObjId);
			if (formId != null)
				query.setString("formId", formId);
			if (formFieldId != null)
				query.setString("formFieldId", formFieldId);
		}
		return query;
	}
	public long getFieldSize(String user, SwdFieldCond cond) throws SwdException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			buf.append(" count(obj)");
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			long count =((Long)list.get(0)).longValue();
			return count;
		} catch (Exception e) {
			throw new SwdException(e);
		}
	}
	public SwdField[] getFields(String user, SwdFieldCond cond, String level) throws SwdException {
		try {
			if (level == null)
				level = LEVEL_LITE;
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			if (level.equals(LEVEL_ALL)) {
				buf.append(" obj");
			} else {
				buf.append(" obj.objId, obj.formFieldId, obj.formFieldPath, obj.formFieldName, obj.formFieldType");
				buf.append(", obj.tableColumnName, obj.arrayYn, obj.systemFieldYn");
				buf.append(", obj.displayOrder, obj.tableWidth");
			}
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			if (!level.equals(LEVEL_ALL)) {
				List<SwdField> objList = new ArrayList<SwdField>();
				for (Iterator itr = list.iterator(); itr.hasNext();) {
					Object[] fields = (Object[]) itr.next();
					SwdField obj = new SwdField();
					int j = 0;
					obj.setObjId((String)fields[j++]);
					obj.setFormFieldId((String)fields[j++]);
					obj.setFormFieldPath((String)fields[j++]);
					obj.setFormFieldName((String)fields[j++]);
					obj.setFormFieldType((String)fields[j++]);
					obj.setTableColumnName((String)fields[j++]);
					obj.setArray(CommonUtil.toBoolean((String)fields[j++]));
					obj.setSystemField(CommonUtil.toBoolean((String)fields[j++]));
					obj.setDisplayOrder(CommonUtil.toInt(fields[j++]));
					obj.setTableWidth(CommonUtil.toFloat(fields[j++]));
					objList.add(obj);
				}
				list = objList;
			}
			SwdField[] objs = new SwdField[list.size()];
			list.toArray(objs);
			return objs;
		} catch (Exception e) {
			throw new SwdException(e);
		}
	}

	public SwdRecord getRecord(String user, String domainId, String recordId, String level) throws SwdException {
		if (domainId == null || recordId == null)
			return null;
		if (level == null)
			level = LEVEL_ALL;
		SwdRecordCond cond = new SwdRecordCond();
		cond.setDomainId(domainId);
		cond.setRecordId(recordId);

		SwdRecord obj = getRecord(user, cond, level);
		return obj;
	}
	public SwdRecord getRecord(String user, SwdRecordCond cond, String level) throws SwdException {
		if (cond == null)
			return null;
		if (level == null)
			level = LEVEL_ALL;
		cond.setPageSize(2);
		SwdRecord[] objs = this.getRecords(user, cond, level);
		if (CommonUtil.isEmpty(objs))
			return null;
		if (objs.length != 1) {
			String formId = cond.getFormId();
			String domainId = cond.getDomainId();
			StringBuffer buf = new StringBuffer("More than 1 record.");
			if (!CommonUtil.isEmpty(formId))
				buf.append(" formId:").append(formId);
			if (!CommonUtil.isEmpty(domainId))
				buf.append(" domainId: ").append(domainId);
			buf.append(" recordIds: ").append(objs[0].getRecordId()).append(", ").append(objs[1].getRecordId());
			throw new SwdException(buf.toString());
		}
		return objs[0];
	}
	public String setRecord(String user, SwdRecord obj, String level) throws SwdException {
		// 테이블을 알기 위해 도메인을 조회합니다.
		String domainId = obj.getDomainId();
		String formId = obj.getFormId();
		SwdDomain domain = null;
		if (formId == null) {
			if (domainId == null)
				throw new SwdException("domainId and formId are null.");
			domain = getDomain(user, domainId, LEVEL_ALL);
			if (domain == null)
				throw new SwdException("Domain is not exist domainId:" + domainId);
			formId = domain.getFormId();
			obj.setFormId(formId);
		} else {
			SwdDomainCond domainCond = new SwdDomainCond();
			domainCond.setFormId(formId);
			domain = getDomain(user, domainCond, LEVEL_ALL);
			if (domain == null)
				throw new SwdException("Domain is not exist formId:" + formId);
			domainId = domain.getObjId();
			obj.setDomainId(domainId);
		}
		String tableName = domain.getTableName();
		
		Map<String, SwdField> fieldMap = new HashMap<String, SwdField>();
		SwdField[] fields = domain.getFields();
		if (!CommonUtil.isEmpty(fields)) {
			for (SwdField field : fields)
				fieldMap.put(field.getFormFieldId(), field);
		}
		// 기존 레코드 여부를 파악합니다.
		String recordId = obj.getRecordId();
		boolean exist = false;
		if (recordId != null) {
			if (getRecord(user, domainId, recordId, LEVEL_LITE) != null) {
				exist = true;
			}
		} else {
			recordId = "dr_" + CommonUtil.newId();
			obj.setRecordId(recordId);
		}

		try {
			StringBuffer buf = new StringBuffer();
			Query query;
			SwdDataField[] dataFields = obj.getDataFields();
			String fieldId;
			SwdField field;
			Map<String, SwdDataField> paramMap = new HashMap<String, SwdDataField>();
			if (exist) {
				//userSetAccessLevel
				buf.append("update ").append(tableName).append(" set modifier = :modifier, modifiedTime = :modifiedTime, workSpaceId = :workSpaceId, workSpaceType = :workSpaceType, isUserSetAccessLevel = :isUserSetAccessLevel, accessLevel = :accessLevel, accessValue = :accessValue");
				//buf.append("update ").append(tableName).append(" set modifier = :modifier, modifiedTime = :modifiedTime, workSpaceId = :workSpaceId, workSpaceType = :workSpaceType, accessLevel = :accessLevel, accessValue = :accessValue");
				if (!CommonUtil.isEmpty(dataFields)) {
					int i = 0;
					String param;
					for (SwdDataField dataField : dataFields) {
						fieldId = dataField.getId();
						if (!fieldMap.containsKey(fieldId))
							continue;
						param = "a" + i++;
						paramMap.put(param, dataField);
						field = fieldMap.get(fieldId);
						dataField.setType(field.getFormFieldType());
						buf.append(", ").append(field.getTableColumnName()).append(" = :").append(param);
					}
				}
				buf.append(" where id = :id");
				query = createSqlQuery(buf.toString(), null);
				query.setString("modifier", obj.getModificationUser());
				query.setTimestamp("modifiedTime", obj.getModificationDate());
				query.setString("workSpaceId", obj.getWorkSpaceId());
				query.setString("workSpaceType", obj.getWorkSpaceType());
				//userSetAccessLevel
				query.setString("isUserSetAccessLevel", obj.getIsUserSetAccessLevel());
				query.setString("accessLevel", obj.getAccessLevel());
				query.setString("accessValue", obj.getAccessValue());
				this.setQueryParameters(query, paramMap);
				query.setString("id", obj.getRecordId());
			} else {
				buf.append("insert into ").append(tableName).append(" (");
				//userSetAccessLevel
				buf.append("id, domainId, creator, createdTime, modifier, modifiedTime, workSpaceId, workSpaceType, isUserSetAccessLevel, accessLevel, accessValue, hits");
				//buf.append("id, domainId, creator, createdTime, modifier, modifiedTime, workSpaceId, workSpaceType, accessLevel, accessValue, hits");
				if (!CommonUtil.isEmpty(dataFields)) {
					for (SwdDataField dataField : dataFields) {
						fieldId = dataField.getId();
						if (!fieldMap.containsKey(fieldId))
							continue;
						field = fieldMap.get(fieldId);
						String colName = field.getTableColumnName();
						if (colName.equalsIgnoreCase("id") || 
								colName.equalsIgnoreCase("domainId") || 
								colName.equalsIgnoreCase("creator") || 
								colName.equalsIgnoreCase("createdTime") || 
								colName.equalsIgnoreCase("modifier") || 
								colName.equalsIgnoreCase("modifiedTime") ||
								colName.equalsIgnoreCase("workSpaceId") ||
								colName.equalsIgnoreCase("workSpaceType") ||
								//userSetAccessLevel
								colName.equalsIgnoreCase("isUserSetAccessLevel") ||
								colName.equalsIgnoreCase("accessLevel") ||
								colName.equalsIgnoreCase("accessValue") ||
								colName.equalsIgnoreCase("hits")) {
							fieldMap.remove(fieldId);
							continue;
						}
						dataField.setType(field.getFormFieldType());
						buf.append(", ").append(colName);
					}
				}
				//userSetAccessLevel
				buf.append(") values (:id, :domainId, :creator, :createdTime, :modifier, :modifiedTime, :workSpaceId, :workSpaceType, :isUserSetAccessLevel, :accessLevel, :accessValue, :hits");
				//buf.append(") values (:id, :domainId, :creator, :createdTime, :modifier, :modifiedTime, :workSpaceId, :workSpaceType, :accessLevel, :accessValue, :hits");
				if (!CommonUtil.isEmpty(dataFields)) {
					int i = 0;
					String param;
					for (SwdDataField dataField : dataFields) {
						fieldId = dataField.getId();
						if (!fieldMap.containsKey(fieldId))
							continue;
						param = "a" + i++;
						paramMap.put(param, dataField);
						buf.append(", :").append(param);
					}
				}
				buf.append(")");
				query = createSqlQuery(buf.toString(), null);
				query.setString("id", obj.getRecordId());
				query.setString("domainId", obj.getDomainId());
				query.setString("creator", obj.getCreationUser());
				query.setTimestamp("createdTime", obj.getCreationDate());
				query.setString("modifier", obj.getModificationUser());
				query.setTimestamp("modifiedTime", obj.getModificationDate());
				query.setString("workSpaceId", obj.getWorkSpaceId());
				query.setString("workSpaceType", obj.getWorkSpaceType());
				//userSetAccessLevel
				query.setString("isUserSetAccessLevel", obj.getIsUserSetAccessLevel());
				query.setString("accessLevel", obj.getAccessLevel());
				query.setString("accessValue", obj.getAccessValue());
				query.setInteger("hits", obj.getHits());
				this.setQueryParameters(query, paramMap);
			}

			query.executeUpdate();

			return obj.getRecordId();

		} catch (SQLGrammarException e) {
			if(e.getSQLState().equals("42S22") || e.getSQLState().equals("42703") || e.getSQLState().equals("42000")) {
				this.addTableColumn("", domain.getTableName(), "workspaceId", "varchar(100)");
				this.addTableColumn("", domain.getTableName(), "workspaceType", "varchar(50)");
				//userSetAccessLevel
				this.addTableColumn("", domain.getTableName(), "isUserSetAccessLevel", "varchar(10)");
				this.addTableColumn("", domain.getTableName(), "accessLevel", "varchar(50)");
				this.addTableColumn("", domain.getTableName(), "accessValue", "varchar(4000)");
				this.addTableColumn("", domain.getTableName(), "hits", "int");
				return setRecord(user, obj, level);
			}
			
			throw new SwdException(e);
		} catch (Exception e) {
			throw new SwdException(e);
		}
	}
	private void setQueryParameters(Query query, Map<String, SwdDataField> paramMap) throws Exception {
		if (CommonUtil.isEmpty(paramMap))
			return;
		SwdDataField dataField;
		for (String param : paramMap.keySet()) {
			dataField = paramMap.get(param);
			String type  = dataField.getType();
			String value = null;
			if (type != null && type.equalsIgnoreCase("complex")) {
				//TODO COMPLEX
				//value = dataField.toString();
				value = dataField.getValue();
			} else {
				value = dataField.getValue();
			}
			if (type == null || type.equalsIgnoreCase("string") || type.equalsIgnoreCase("text") || type.equalsIgnoreCase("complex")) {
				query.setString(param, value);
			} else if (type.equalsIgnoreCase("number")) {
				if (CommonUtil.isEmpty(value)) {
					query.setString(param, null);
				} else {
					if (value.indexOf("$") != -1)
						value = value.replace("$", "");
					query.setDouble(param, Double.parseDouble(value));
				}
			} else if (type.equalsIgnoreCase("boolean")) {
				query.setBoolean(param, CommonUtil.toBoolean(value));
			} else if (type.equalsIgnoreCase("datetime")) {
				//TODO type이 datetime 으로 들어오는경우 날짜 포멧에 대한 정의 필요 - 임시로 if 문에서 ":"문자 포함여부로 데이터 포멧을 결정한다 변경필요

				//프로세스 datetime type 변환
				
				if(type.equals("datetime")) {
					if (value != null) {
						if(value.length() == FieldData.SIZE_DATETIME) {
							value = LocalDate.convertLocalDateTimeStringToLocalDate(value).toGMTDateString();								
						} else if(value.length() == FieldData.SIZE_DATE) {
							value = LocalDate.convertLocalDateStringToLocalDate(value).toGMTDateString();
						}
					}
				} else if(type.equals("time")) {
					value = LocalDate.convertLocalTimeStringToLocalDate(value).toGMTTimeString2();
				}
				if (value != null) {
					if (value.indexOf(":") != -1) {
						query.setTimestamp(param, DateUtil.toDate(value, "yyyy.MM.dd HH:mm:ss"));
					} else {
						query.setTimestamp(param, DateUtil.toDate(value, "yyyy.MM.dd"));
					}
				} else {
					query.setTimestamp(param, null);
				}
			} else if (type.equalsIgnoreCase("date")) {
				query.setTimestamp(param, DateUtil.toDate(value));
			} else if (type.equalsIgnoreCase("time")) {
				if (CommonUtil.isEmpty(value)) {
					query.setParameter(param, null);
				} else {
					String timeStr = null;
					String minuteStr = null;
					int timeInt = 0;
					int minuteInt = 0;
					
					if(value.startsWith("오전")) {
						value = value.substring(value.indexOf(" ") + 1);
						timeStr = value.substring(0, 2);
						minuteStr = value.substring(3);
						timeInt = Integer.parseInt(timeStr.startsWith("0") ? timeStr.substring(1) : timeStr);
						minuteInt = Integer.parseInt(minuteStr.startsWith("0") ? minuteStr.substring(1) : minuteStr);
					} else if(value.startsWith("오후")) {
						value = value.substring(value.indexOf(" ") + 1);
						timeStr = value.substring(0, 2);
						minuteStr = value.substring(3);
						timeInt = Integer.parseInt(timeStr.startsWith("0") ? timeStr.substring(1) : timeStr);
						timeInt = timeInt + 12;
						minuteInt = Integer.parseInt(minuteStr.startsWith("0") ? minuteStr.substring(1) : minuteStr);
					} else {
						timeStr = value.substring(0, 2);
						minuteStr = value.substring(3);
						if (minuteStr.length() >= 3)
							minuteStr = minuteStr.substring(0, 2);
						timeInt = Integer.parseInt(timeStr.startsWith("0") ? timeStr.substring(1) : timeStr);
						minuteInt = Integer.parseInt(minuteStr.startsWith("0") ? minuteStr.substring(1) : minuteStr);			
					}
					
					Time time = new Time(timeInt, minuteInt, 0);
					query.setTime(param, time);
				}
			} else {
				query.setParameter(param, value);
			}
		}
	}
	public void removeRecord(String user, String domainId, String recordId) throws SwdException {
		SwdDomain domain = this.getDomain(user, domainId, LEVEL_LITE);
		if (domain == null)
			return;
		String tableName = domain.getTableName();
		
		try {
			remove(tableName, new Property[] {new Property("id", recordId)});
		} catch (Exception e) {
			throw new SwdException(e);
		}
	}
	public void removeRecord(String user, SwdRecordCond cond) throws SwdException {
		// 삭제할 레코드 조회
		SwdRecord record = this.getRecord(user, cond, LEVEL_LITE);
		if (record == null)
			return;
		
		// 삭제할 도메인 아이디 조회
		String domainId = record.getDomainId();
		if (domainId == null) {
			SwdDomainCond domainCond = new SwdDomainCond();
			domainCond.setFormId(record.getFormId());
			SwdDomain domain = this.getDomain(user, domainCond, LEVEL_LITE);
			domainId = domain.getObjId();
		}
		
		// 삭제
		removeRecord(user, record.getDomainId(), record.getRecordId());
	}
	public long getRecordSize(String user, SwdRecordCond cond) throws SwdException {
		try {
			Query query = this.appendQuery(user, cond, null, null, null, "select count(*) from (", ") qry");
			List list = query.list();
			Object sizeObj = list.get(0);
			long size = 0;
			if (sizeObj instanceof BigInteger) {
				size = ((BigInteger)sizeObj).longValue();
			} else if (sizeObj instanceof Long) {
				size = ((Long)sizeObj).longValue();
			} else {
				size = Long.parseLong(sizeObj.toString());
			} 
			return size;
		} catch (SQLGrammarException e) {
			System.out.println("########## SQL STATUS : " + e.getSQLState());
			if(e.getSQLState().equals("42S22") || e.getSQLState().equals("42703") || e.getSQLState().equals("42000")) {
				SwdDomain domain = getDomain(user, cond);
				this.addTableColumn("", domain.getTableName(), "workspaceId", "varchar(100)");
				this.addTableColumn("", domain.getTableName(), "workspaceType", "varchar(50)");
				//userSetAccessLevel
				this.addTableColumn("", domain.getTableName(), "isUserSetAccessLevel", "varchar(10)");
				this.addTableColumn("", domain.getTableName(), "accessLevel", "varchar(50)");
				this.addTableColumn("", domain.getTableName(), "accessValue", "varchar(4000)");
				this.addTableColumn("", domain.getTableName(), "hits", "int");
				return getRecordSize(user, cond);
			}
			throw new SwdException(e);
		} catch (Exception e) {
			throw new SwdException(e);
		}
	}
	public double getRecordValue(String user, String field, String func, SwdRecordCond cond) throws SwdException {
		try {
			StringBuffer preQ = new StringBuffer("select ").append(func).append("(");
			if (!func.equalsIgnoreCase("count")) {
				preQ.append("cast(").append(field).append(" as real)");
			} else {
				preQ.append(field);
			}
			preQ.append(") from (");
			Query query = this.appendQuery(user, cond, null, null, field, preQ.toString(), ") qry");
			List list = query.list();
			Object valueObj = list.get(0);
			double size = 0;
			if (valueObj == null)
				return 0;
			if (valueObj instanceof Double) {
				size = ((Double)valueObj).doubleValue();
			} else if (valueObj instanceof Float) {
				size = ((Float)valueObj).floatValue();
			} else if (valueObj instanceof BigInteger) {
				size = ((BigInteger)valueObj).longValue();
			} else if (valueObj instanceof Long) {
				size = ((Long)valueObj).longValue();
			} else {
				size = Double.parseDouble(valueObj.toString());
			} 
			return size;
		} catch (SQLGrammarException e) {
			System.out.println("########## SQL STATUS : " + e.getSQLState());
			if(e.getSQLState().equals("42S22") || e.getSQLState().equals("42703") || e.getSQLState().equals("42000")) {
				SwdDomain domain = getDomain(user, cond);
				this.addTableColumn("", domain.getTableName(), "workspaceId", "varchar(100)");
				this.addTableColumn("", domain.getTableName(), "workspaceType", "varchar(50)");
				//userSetAccessLevel
				this.addTableColumn("", domain.getTableName(), "isUserSetAccessLevel", "varchar(10)");
				this.addTableColumn("", domain.getTableName(), "accessLevel", "varchar(50)");
				this.addTableColumn("", domain.getTableName(), "accessValue", "varchar(4000)");
				this.addTableColumn("", domain.getTableName(), "hits", "int");
				return getRecordValue(user, field, func, cond);
			}
			throw new SwdException(e);
		} catch (Exception e) {
			throw new SwdException(e);
		}
	}
	private SwdDomain getDomain(String user, SwdRecordCond cond) throws SwdException {
		String domainId = cond.getDomainId();
		String formId = cond.getFormId();
		if (domainId == null && formId == null)
			throw new SwdException("domainId and formId is null.");
		SwdDomainCond domainCond = new SwdDomainCond();
		if (formId == null) {
			domainCond.setObjId(domainId);
		} else {
			domainCond.setFormId(formId);
		}
		SwdDomain[] domains = getDomains(user, domainCond, LEVEL_ALL);
		if (CommonUtil.isEmpty(domains)) {
			StringBuffer excBuf = new StringBuffer();
			excBuf.append("No domain found");
			if (domainId != null)
				excBuf.append(" domainId:").append(domainId);
			if (formId != null)
				excBuf.append(" formId:").append(formId);
			throw new SwdException(excBuf.toString());
		}
		return domains[0];
	}
	public SwdRecord[] getRecords(String user, SwdRecordCond cond, String level) throws SwdException {
		Query query = null;
		SwdDomain domain = null;
		try {
			List<SwdField> selectedFieldList = new ArrayList<SwdField>();
			if (level == null)
				level = LEVEL_LITE;
			domain = getDomain(user, cond);
			query = this.appendQuery(user, cond, domain, selectedFieldList, null, null, null);
			
			List list = query.list();
			
			if (list == null || list.isEmpty())
				return null;
			List<SwdRecord> objList = new ArrayList<SwdRecord>();
			for (Iterator itr = list.iterator(); itr.hasNext();) {
				Object[] fields = (Object[]) itr.next();
				SwdRecord obj = new SwdRecord();
				int j = 0;
				obj.setRecordId((String)fields[j++]);
				j++;
				obj.setDomainId(domain.getObjId());
				//obj.setFormId((String)fields[j++]);
				//obj.setFormName((String)fields[j++]);
				obj.setCreationUser((String)fields[j++]);
				obj.setCreationDate((Timestamp)fields[j++]);
				obj.setModificationUser((String)fields[j++]);
				obj.setModificationDate((Timestamp)fields[j++]);
				obj.setWorkSpaceId((String)fields[j++]);
				obj.setWorkSpaceType((String)fields[j++]);
				//userSetAccessLevel
				obj.setIsUserSetAccessLevel((String)fields[j++]);
				obj.setAccessLevel((String)fields[j++]);
				obj.setAccessValue((String)fields[j++]);
				obj.setHits(CommonUtil.toInt(fields[j++]));
				objList.add(obj);
				if (CommonUtil.isEmpty(selectedFieldList))
					continue;
				String colName;
				SwdDataField dataField;
				String dataType;
				for (SwdField field : selectedFieldList) {
					colName = field.getTableColumnName();
					dataType = field.getFormFieldType();
					dataField = new SwdDataField();
					obj.addDataField(dataField);
					dataField.setId(field.getFormFieldId());
					dataField.setName(field.getFormFieldName());
					dataField.setType(dataType);
					dataField.setDisplayOrder(field.getDisplayOrder());
					dataType = field.getFormFieldType();
					if (colName != null && colName.equalsIgnoreCase("id")) {
						dataField.setValue((String)fields[0]);
						continue;
					}
					Object fieldValue = fields[j++];
					if (fieldValue == null)
						continue;
					if(fieldValue instanceof Clob) {
						
						Clob clob = (Clob)fieldValue;
						try {
							int clobLength = (int)clob.length();
							fieldValue = clob.getSubString(1, clobLength);
							if (dataType.equalsIgnoreCase("complex")) {
								//TODO COMPLEX
								//SwdDataField tmpField = (SwdDataField) SwdDataField.toObject(fieldValue.toString());
								//dataField.setDataFields(tmpField.getDataFields());
								dataField.setValue((String)fieldValue);
							} else {
								dataField.setValue((String)fieldValue);
							}
						} catch (SQLException e) {
							throw new SwdException(e);
						}
					} else if (dataType == null) {
						dataField.setValue(fieldValue.toString());
					} else if (fieldValue instanceof String) {
						if (dataType.equalsIgnoreCase("complex")) {
							//TODO COMPLEX
							//SwdDataField tmpField = (SwdDataField) SwdDataField.toObject(fieldValue.toString());
							//dataField.setDataFields(tmpField.getDataFields());
							dataField.setValue((String)fieldValue);
						} else {
							dataField.setValue((String)fieldValue);
						}
					} else if (dataType.equalsIgnoreCase("time")) {
						if (fieldValue instanceof Timestamp) {
							dataField.setValue(DateUtil.toString((Timestamp)fieldValue, "HH:mm:ss"));
						} else if (fieldValue instanceof Time) {
							dataField.setValue(DateUtil.toString((Time)fieldValue, "HH:mm:ss"));
						}
					} else if (fieldValue instanceof Timestamp || fieldValue instanceof Time) {
						String value = fieldValue.toString();
						if (value != null && value.startsWith("1900-")) {
							if (fieldValue instanceof Timestamp) {
								dataField.setValue(DateUtil.toString((Timestamp)fieldValue, "HH:mm:ss"));
							} else if (fieldValue instanceof Time) {
								dataField.setValue(DateUtil.toString((Time)fieldValue, "HH:mm:ss"));
							}
						} else {
							dataField.setValue(value);
						}
					} else {
						dataField.setValue(fieldValue.toString());
					}
				}
			}
			SwdRecord[] objs = new SwdRecord[list.size()];
			objList.toArray(objs);
			return objs;
		} catch (SQLGrammarException e) {
			System.out.println("########## SQL STATUS : " + e.getSQLState());
			if(e.getSQLState().equals("42S22") || e.getSQLState().equals("42703") || e.getSQLState().equals("42000")) {
				this.addTableColumn("", domain.getTableName(), "workspaceId", "varchar(100)");
				this.addTableColumn("", domain.getTableName(), "workspaceType", "varchar(50)");
				//userSetAccessLevel
				this.addTableColumn("", domain.getTableName(), "isUserSetAccessLevel", "varchar(10)");
				this.addTableColumn("", domain.getTableName(), "accessLevel", "varchar(50)");
				this.addTableColumn("", domain.getTableName(), "accessValue", "varchar(4000)");
				this.addTableColumn("", domain.getTableName(), "hits", "int");
				return this.getRecords(user, cond, level);
			}
			if (query != null)
				logger.error("Query: " + query.getQueryString());
			throw new SwdException(e);
		} catch (Exception e) {
			throw new SwdException(e);
		}
	}
	
	private String makeAuthTable(SwdRecordCond cond, SwdDomain domain, String resourceId) throws Exception {
		
		String tableName = domain.getTableName();
		SwdField[] fields = domain.getFields();
		boolean display = cond.isDisplayableDataFieldsOnly();
		
		// 쿼리 생성
		StringBuffer buf = new StringBuffer();
		buf.append("( select obj1.id, obj1.domainId, obj1.creator, obj1.createdTime");
		buf.append(", obj1.modifier, obj1.modifiedTime, obj1.workspaceId, obj1.workspaceType, obj1.isUserSetAccessLevel");
		
		buf.append(", case when obj1.isUserSetAccessLevel is null then (select accessLevel from swauthproxy where resourceid='").append(resourceId).append("') when obj1.isUserSetAccessLevel ='false' then (select accessLevel from swauthproxy where resourceid='").append(resourceId).append("') when obj1.isUserSetAccessLevel ='true' then (obj1.accessLevel) end as accessLevel");
		buf.append(", case when obj1.isUserSetAccessLevel is null then (select accessValue from swauthproxy where resourceid='").append(resourceId).append("') when obj1.isUserSetAccessLevel ='false' then (select accessValue from swauthproxy where resourceid='").append(resourceId).append("') when obj1.isUserSetAccessLevel ='true' then (obj1.accessValue) end as accessValue");
		
		buf.append(", obj1.hits");
		String columnName;
		if (!CommonUtil.isEmpty(fields)) {
			int order = 0;
			for (SwdField field : fields) {
				order = field.getDisplayOrder();
				if (display && order < 0)
					continue;
				columnName = field.getTableColumnName();
				if (columnName.equalsIgnoreCase("id"))
					continue;
				buf.append(", obj1.").append(columnName);
			}
		}
		// from
		buf.append(" from ").append(tableName).append(" obj1 )");
		return buf.toString();
	}
	
	public Query getRecordQuery(String user, SwdRecordCond cond) throws Exception {
		return appendQuery(user, cond, null, null, null, null, null);
	}
	
	private Query appendQuery(String user, SwdRecordCond cond, SwdDomain domain, List<SwdField> selectedFieldList, String funcField,
			String preQuery, String postQuery) throws Exception {

		if (cond == null)
			throw new SwdException("Cond object is null.");
		Filter[] fs = cond.getFilter();
		if (!CommonUtil.isEmpty(fs)) {
			String operator;
			for (Filter f : fs) {
				operator = f.getOperator();
				if (operator == null || (!operator.equalsIgnoreCase("datein") && !operator.equalsIgnoreCase("datenotin")))
					continue;
				if (operator.equalsIgnoreCase("datein")) {
					f.setOperator(">=");
					f.setRightOperandType(Filter.OPERANDTYPE_DATE);
					Date rightValue = DateUtil.toDate(f.getRightOperandValue());
					f.setRightOperandValue(DateUtil.toDateString(rightValue));
					Filter f2 = new Filter("<=", f.getLeftOperandValue(), Filter.OPERANDTYPE_DATE, DateUtil.toXsdDateTimeString(DateUtil.toToDate(rightValue, DateUtil.CYCLE_DAY)));
					cond.addFilter(f2);
				} else if (operator.equalsIgnoreCase("datenotin")) {
					Filters newFs = new Filters();
					newFs.setOperator("or");
					newFs.addFilter(new Filter("<", f.getLeftOperandValue(), Filter.OPERANDTYPE_DATE, DateUtil.toXsdDateTimeString(DateUtil.toFromDate(DateUtil.toDate(f.getRightOperandValue()), DateUtil.CYCLE_DAY))));
					newFs.addFilter(new Filter(">", f.getLeftOperandValue(), Filter.OPERANDTYPE_DATE, DateUtil.toXsdDateTimeString(DateUtil.toToDate(DateUtil.toDate(f.getRightOperandValue()), DateUtil.CYCLE_DAY))));
					cond.addFilters(newFs);
					cond.setFilter(Filter.remove(cond.getFilter(), f));
					continue;
				} else {
					continue;
				}
			}
		}
		
		// 도메인 조회
		if (domain == null)
			domain = getDomain(user, cond);
		String tableName = domain.getTableName();
		SwdField[] fields = domain.getFields();
		String refFormId = cond.getReferencedFormId();
		String refRecordId = cond.getReferencedRecordId();
		boolean display = cond.isDisplayableDataFieldsOnly();
		String logicalOperator = CommonUtil.toDefault(cond.getOperator(), "and");
		
		Map<String, String> fieldColumnMap = new HashMap<String, String>();
		Map<String, String> fieldTypeMap = new HashMap<String, String>();
		
		// 쿼리 생성
		StringBuffer buf = new StringBuffer();
		// select
		//buf.append("select obj.id, obj.domainId, domain.formId, domain.formName, obj.creator, obj.createdTime");
		buf.append("select obj.id, obj.domainId, obj.creator, obj.createdTime");
		buf.append(", obj.modifier, obj.modifiedTime, obj.workspaceId, obj.workspaceType");
		//userSetAccessLevel
		buf.append(", obj.isUserSetAccessLevel");
		buf.append(", obj.accessLevel");
		buf.append(", obj.accessValue");
		buf.append(", obj.hits");
		String columnName;
		if (!CommonUtil.isEmpty(fields)) {
			int order = 0;
			for (SwdField field : fields) {
				fieldColumnMap.put(field.getFormFieldId(), field.getTableColumnName());
				fieldTypeMap.put(field.getTableColumnName(), field.getFormFieldType());
				order = field.getDisplayOrder();
				if (display && order < 0)
					continue;
				if (selectedFieldList != null)
					selectedFieldList.add(field);
				columnName = field.getTableColumnName();
				if (columnName.equalsIgnoreCase("id"))
					continue;
				buf.append(", obj.").append(columnName);
			}
		}
		// from
		//authProxy
		//buf.append(" from ").append(tableName).append(" obj");
		//userSetAccessLevel
		buf.append(" from ").append(makeAuthTable(cond, domain, domain.getFormId())).append(" obj");
		
		//buf.append(", swdomain domain");
		if (refFormId != null || refRecordId != null) {
			buf.append(", swdataref");
		}
		
		// where
		String[] workSpaceIdIns = cond.getWorkSpaceIdIns();
		String[] likeAccessValues = cond.getLikeAccessValues();
		String[] workSpaceIdNotIns = cond.getWorkSpaceIdNotIns();
		boolean first = true;
		if (refFormId != null || refRecordId != null) {
			first = false;
			buf.append(" where obj.id = swdataref.myrecordid");
			if (refFormId != null)
				buf.append(" and swdataref.refformid = :refFormId");
			if (refRecordId != null)
				buf.append(" and swdataref.refrecordid = :refRecordId");
			//buf.append(" and obj.domainId = domain.id");
		//} else {
			//buf.append(" where obj.domainId = domain.id");
			//first = false;
		}
		String recordId = cond.getRecordId();
		String creationUser = cond.getCreationUser();
		Date creationDateFrom = cond.getCreationDateFrom();
		Date creationDateTo = cond.getCreationDateTo();
		String modificationUser = cond.getModificationUser();
		Date modificationDateFrom = cond.getModificationDateFrom();
		Date modificationDateTo = cond.getModificationDateTo();
		String searchKey = cond.getSearchKey();
		String workSpaceId = cond.getWorkSpaceId();
		String workSpaceType = cond.getWorkSpaceType();
		//userSetAccessLevel
		String isUserSetAccessLevel = cond.getIsUserSetAccessLevel();
		String accessLevel = cond.getAccessLevel();
		String accessValue = cond.getAccessValue();
		String creatorOrSpaceId = cond.getCreatorOrSpaceId();
		int hits = cond.getHits();
		Date fromDate = cond.getFromDate();

		if (recordId != null)
			cond.addFilter(new Filter("=", "obj.id", recordId));
		if (!CommonUtil.isEmpty(creationUser))
			cond.addFilter(new Filter("=", "obj.creator", creationUser));
		if (creationDateFrom != null)
			cond.addFilter(new Filter(">=", "obj.createdTime", Filter.OPERANDTYPE_DATE, DateUtil.toXsdDateTimeString(creationDateFrom)));
		if (creationDateTo != null)
			cond.addFilter(new Filter("<=", "obj.createdTime", Filter.OPERANDTYPE_DATE, DateUtil.toXsdDateTimeString(creationDateTo)));
		if (!CommonUtil.isEmpty(modificationUser))
			cond.addFilter(new Filter("=", "obj.modifier", modificationUser));
		if (modificationDateFrom != null)
			cond.addFilter(new Filter(">=", "obj.modifiedTime", Filter.OPERANDTYPE_DATE, DateUtil.toXsdDateTimeString(modificationDateFrom)));
		if (modificationDateTo != null)
			cond.addFilter(new Filter("<=", "obj.modifiedTime", Filter.OPERANDTYPE_DATE, DateUtil.toXsdDateTimeString(modificationDateTo)));
		if (!CommonUtil.isEmpty(searchKey) && !CommonUtil.isEmpty(fields)) {
			searchKey = CommonUtil.toLikeString(searchKey);
			String fieldType;
			for (SwdField field : fields) {
				fieldType = field.getFormFieldType();
				if (fieldType != null && !fieldType.equalsIgnoreCase("string"))
					continue;
				cond.addFilter(new Filter("like", "searchKey", field.getTableColumnName(), "", searchKey));
			}
			cond.addFilter(new Filter("like", "searchKey", "modifier", "", searchKey));
		}
		if (workSpaceId != null)
			cond.addFilter(new Filter("=", "obj.workSpaceId", workSpaceId));
		if (workSpaceType != null)
			cond.addFilter(new Filter("=", "obj.workSpaceType", workSpaceType));
		//userSetAccessLevel
		if (isUserSetAccessLevel != null)
			cond.addFilter(new Filter("=", "obj.isUserSetAccessLevel", isUserSetAccessLevel));
		if (accessLevel != null)
			cond.addFilter(new Filter("=", "obj.accessLevel", accessLevel));
		if (accessValue != null) {
			cond.addFilter(new Filter("=", "obj.accessValue", accessValue));
		}
			
		if (hits > 0)
			cond.addFilter(new Filter("=", "obj.hits", String.valueOf(hits)));
		if (fromDate != null)
			cond.addFilter(new Filter("<", "obj.createdTime", Filter.OPERANDTYPE_DATE, DateUtil.toXsdDateTimeString(fromDate)));

		Map<String, Filter> filterMap = new HashMap<String, Filter>();
		Map<String, String> paramTypeMap = new HashMap<String, String>();
		Wrapper iWrap = new Wrapper();
		Integer i = 0;
		iWrap.setObj(i);
		first = appendFilterConditions(cond.getFilter(), logicalOperator, first, true, iWrap, fieldColumnMap, fieldTypeMap, filterMap, paramTypeMap, buf, domain);

		i = (Integer)iWrap.getObj();

		Filters[] filterses = cond.getFilters();
		if (!CommonUtil.isEmpty(filterses)) {
			for (Filters filters : filterses) {
				Filter[] filter = filters.getFilter();
				if (CommonUtil.isEmpty(filter))
					continue;
				String logOper = CommonUtil.toDefault(filters.getOperator(), "and");
				if (first) {
					buf.append(" where (");
					first = false;
				} else {
					buf.append(CommonUtil.SPACE).append(logicalOperator).append(" (");
				}
				boolean fFirst = true;
				fFirst = appendFilterConditions(filter, logOper, fFirst, false, iWrap,  fieldColumnMap, fieldTypeMap, filterMap, paramTypeMap, buf, domain);
				buf.append(")");
			}
		}
		if(creatorOrSpaceId != null) {
			if(first) {
				buf.append(" where ");
				first = false;
			} else {
				buf.append(" and ");
			}
			buf.append("(obj.creator = '" + creatorOrSpaceId + "' or obj.workSpaceId = '" + creatorOrSpaceId + "')");
		}
		/*if (workSpaceIdIns != null) {
			if(first) {
				buf.append(" where ((obj.workSpaceType = 6 and obj.workSpaceId in " + workSpaceIdIns + ") or (obj.workSpaceType = 5 and obj.workSpaceId in " + workSpaceIdIns + ") or obj.workSpaceType = 4 or obj.workSpaceType = 2)");
				//buf.append(" where obj.workSpaceId in " + workSpaceIdIns);
				first = false;
			} else {
				buf.append(" and ((obj.workSpaceType = 6 and obj.workSpaceId in " + workSpaceIdIns + ") or (obj.workSpaceType = 5 and obj.workSpaceId in " + workSpaceIdIns + ") or obj.workSpaceType = 4 or obj.workSpaceType = 2)");
				//buf.append(" and obj.workSpaceId in " + workSpaceIdIns);
			}
		}*/
		
		if (!domain.getObjId().equalsIgnoreCase("frm_company_SYSTEM") && !domain.getObjId().equalsIgnoreCase("frm_dept_SYSTEM") && 
				!domain.getObjId().equalsIgnoreCase("frm_role_SYSTEM") && !domain.getObjId().equalsIgnoreCase("frm_user_SYSTEM") && CommonUtil.isEmpty(cond.getRecordId())) {
			
			if (workSpaceIdIns != null) {
				if(first) {
					first = false;
					buf.append(" where");
				} else {
					buf.append(" and");
				}
				buf.append(" (obj.creator = '" + user + "' or ((obj.workSpaceType = '6' and obj.workSpaceId in (");
				for (int j=0; j<workSpaceIdIns.length; j++) {
					if (j != 0)
						buf.append(", ");
					buf.append(":workSpaceIdIn").append(j);
				}
				buf.append(")) or (obj.workSpaceType = '5' and obj.workSpaceId in (");
				for (int j=0; j<workSpaceIdIns.length; j++) {
					if (j != 0)
						buf.append(", ");
					buf.append(":workSpaceIdIn").append(j);
				}
				buf.append(")) or (obj.workSpaceType = '4' and obj.workSpaceId = '" + user + "') or obj.workSpaceType = '2' or obj.workSpaceType is null))");
	
				//buf.append(" where ((obj.workSpaceType = 6 and obj.workSpaceId in " + workSpaceIdIns + ") or (obj.workSpaceType = 5 and obj.workSpaceId in " + workSpaceIdIns + ") or obj.workSpaceType = 4 or obj.workSpaceType = 2)");
				//buf.append(" where obj.workSpaceId in " + workSpaceIdIns);
			}
			if (workSpaceIdNotIns != null) {
				if(first) {
					buf.append(" where");
					first = false;
				} else {
					buf.append(" and");
				}
				buf.append(" (workspaceId is null or obj.workSpaceId not in (");
				for (int j=0; j<workSpaceIdNotIns.length; j++) {
					if (j != 0)
						buf.append(", ");
					buf.append(":workSpaceIdNotIn").append(j);
				}
				buf.append("))");
				
	//				buf.append(" where obj.workSpaceId not in " + workSpaceIdNotIns);
	//			} else {
	//				buf.append(" and obj.workSpaceId not in " + workSpaceIdNotIns);
	//			}
			}
			if(first) {
				buf.append(" where ");
				first = false;
			} else {
				buf.append(" and ");
			}
			
			String likeAccessValuesQuery = "obj.accessValue like '%%'";
			
			StringBuffer likeAccessValuesBuffer = new StringBuffer();
			String divisionUL = "";
			if(this.getDbType().equals("sqlserver"))
				divisionUL = "collate Korean_Wansung_CS_AS ";
			if(likeAccessValues != null) {
				for (int j=0; j<likeAccessValues.length; j++) {
					if(j==0) {
						likeAccessValuesBuffer.append("obj.accessValue ").append(divisionUL).append("like :likeAccessValue").append(j);
					} else {
						likeAccessValuesBuffer.append(" or obj.accessValue ").append(divisionUL).append("like :likeAccessValue").append(j);
					}
				}
				likeAccessValuesQuery = likeAccessValuesBuffer.toString();
			}
			buf.append("(obj.accessLevel is null or obj.accessLevel = '3' or (obj.accessLevel = '1' and obj.creator = '" + user + "') or (obj.accessLevel = '2' and (").append(likeAccessValuesQuery).append(" or obj.creator = '" + user + "'))) ");
		} else if (domain.getObjId().equalsIgnoreCase("frm_dept_SYSTEM")) {
			if(first) {
				buf.append(" where");
				first = false;
			} else {
				buf.append(" and");
			}
			buf.append(" obj.id not in ('root', 'ROOT') ");
		}
		
		// post query
		if (postQuery != null)
			buf.append(postQuery);
		// order by
		Order[] orders = cond.getOrders();
		if (!CommonUtil.isEmpty(orders)) {
			String orderField;
			for (Order order : orders) {
				orderField = order.getField();
				order.setField(CommonUtil.toDefault(fieldColumnMap.get(orderField), orderField));
			}
			appendOrderQuery(buf, "obj", cond);
		}
		
		// pre query
		if (preQuery != null) {
			if (!CommonUtil.isEmpty(funcField)) {
				String dbField = CommonUtil.toDefault(fieldColumnMap.get(funcField), funcField);
				if (!funcField.equalsIgnoreCase(dbField)) {
					preQuery = StringUtils.replace(preQuery, "("+funcField+")", "("+dbField+")");
				}
			}
			buf = new StringBuffer(preQuery).append(buf);
		}
		Query query = this.createSqlQuery(buf.toString(), cond);
		
		if (refFormId != null)
			query.setString("refFormId", refFormId);
		if (refRecordId != null)
			query.setString("refRecordId", refRecordId);

		if (!domain.getObjId().equalsIgnoreCase("frm_company_SYSTEM") && !domain.getObjId().equalsIgnoreCase("frm_dept_SYSTEM") && 
				!domain.getObjId().equalsIgnoreCase("frm_role_SYSTEM") && !domain.getObjId().equalsIgnoreCase("frm_user_SYSTEM") && CommonUtil.isEmpty(cond.getRecordId())) {
			if (workSpaceIdIns != null) {
				for (int j=0; j<workSpaceIdIns.length; j++) {
					query.setString("workSpaceIdIn"+j, workSpaceIdIns[j]);
				}
			}
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
		}

		if (!CommonUtil.isEmpty(filterMap)) {
			Filter f;
			String operType;
			String operValue;
			String operator;
			for (String param : filterMap.keySet()) {
				f = (Filter)filterMap.get(param);
				operType = f.getRightOperandType();
				operator = f.getOperator();
				if (operator.equalsIgnoreCase("like")) {
					operValue = CommonUtil.toLikeString(f.getRightOperandValue());
				} else {
					operValue = f.getRightOperandValue();
					if ((operType == null || operType.equalsIgnoreCase(Filter.OPERANDTYPE_STRING)) && paramTypeMap.containsKey(param))
						operType = paramTypeMap.get(param);
				}
				if (operType == null || operType.equalsIgnoreCase(Filter.OPERANDTYPE_STRING)) {
					query.setString(param, operValue);
				} else if (operType.equalsIgnoreCase(Filter.OPERANDTYPE_INT)) {
					query.setInteger(param, CommonUtil.toInt(operValue));
//				} else if (operType.equalsIgnoreCase(Filter.OPERANDTYPE_LONG)) {
//					query.setLong(param, CommonUtil.toLong(operValue));
				} else if (operType.equalsIgnoreCase(Filter.OPERANDTYPE_FLOAT)) {
					query.setFloat(param, CommonUtil.toFloat(operValue));
//				} else if (operType.equalsIgnoreCase(Filter.OPERANDTYPE_DOUBLE)) {
//					query.setDouble(param, CommonUtil.toDouble(operValue));
				} else if (operType.equalsIgnoreCase(Filter.OPERANDTYPE_DATE)) {
					query.setTimestamp(param, DateUtil.toDate(operValue));
				} else if (operType.equalsIgnoreCase(Filter.OPERANDTYPE_DATETIME)) {
					query.setTimestamp(param, DateUtil.toDate(operValue));
				} else if (operType.equalsIgnoreCase("number")) {
					query.setDouble(param, Double.parseDouble(operValue));
				} else if (operType.equalsIgnoreCase("boolean")) {
					query.setBoolean(param, CommonUtil.toBoolean(operValue));
				} else {
					query.setParameter(param, operValue);
				}
			}
		}
		
		return query;
	}
	private static boolean appendFilterConditions(Filter[] filter, String logicalOperator, boolean first, boolean where, Wrapper iWrap, 
			Map<String, String> fieldColumnMap, Map<String, String> fieldTypeMap, 
			Map<String, Filter> filterMap, Map<String, String> paramTypeMap, StringBuffer buf, SwdDomain domain) throws Exception {
		if (CommonUtil.isEmpty(filter))
			return first;

		int searchKeyCount = 0;
		for (Filter f : filter) {
			if(f.getLeftOperandType().equals("searchKey"))
				searchKeyCount++;
		}

		int forSearchKeyCount = 0;

		String operator;
		String left;
		String leftType;
		String right;
		String rightType;
		Integer i = (Integer)iWrap.getObj();
		boolean firstCnt = true;
		for (Filter f : filter) {
			operator = f.getOperator();
			left = f.getLeftOperandValue();
			leftType = f.getLeftOperandType();
			right = f.getRightOperandValue();
			rightType = f.getRightOperandType();

			String formFieldId = null;
			Iterator itr = fieldColumnMap.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				String value = fieldColumnMap.get(key);
				if (value.equalsIgnoreCase(left)) {
					formFieldId = key;
					break;
				}
			}
			
			if (leftType.equalsIgnoreCase("userField") && !CommonUtil.isEmpty(formFieldId)) {
				
				String formId = domain.getFormId();
				String userOperator = "in";
				if (operator.equalsIgnoreCase("=") || operator.equalsIgnoreCase("like")) {
					userOperator = "in";
				} else if (operator.equalsIgnoreCase("!=") || operator.equalsIgnoreCase("not like")){
					userOperator = "not in";
				}
				if (first) {
					if (where)
						buf.append(" where");
					first = false;
				} else {
					buf.append(CommonUtil.SPACE).append(logicalOperator);
				}
				buf.append(CommonUtil.SPACE);
				buf.append("( obj.id ").append(userOperator).append(" (select myrecordId from swdataref where myFormFieldId = '").append(formFieldId).append("' and ");
				buf.append(" myformid = '").append(formId).append("' and refRecordId = '").append(right).append("'))");
				
			} else {
				if (left == null)
					throw new SwdException("left operand of filter condition is null.");
				if (operator == null) {
					operator = "=";
				} else {
					operator = operator.trim();
				}
				if (left.equalsIgnoreCase("creationUser")) {
					left = "obj.creator";
				} else if (left.equalsIgnoreCase("creationDate")) {
					left = "obj.createdTime";
				} else if (left.equalsIgnoreCase("modificationUser")) {
					left = "obj.modifier";
				} else if (left.equalsIgnoreCase("modificationDate")) {
					left = "obj.modifiedTime";
				} else {
					left = CommonUtil.toDefault(fieldColumnMap.get(left), left);
				}
				if(leftType.equals("searchKey")) {
					forSearchKeyCount++;
					logicalOperator = "or";
				} else {
					logicalOperator = "and";
				}
				if (first) {
					if (where)
						buf.append(" where");
					first = false;
				} else {
						buf.append(CommonUtil.SPACE).append(logicalOperator);
				}
				String fieldType = (String)fieldTypeMap.get(left);
				buf.append(CommonUtil.SPACE);
				boolean isLikeCast = false;
				if (operator.equalsIgnoreCase("like")) {
					if (fieldType != null && !fieldType.equalsIgnoreCase("string"))
						isLikeCast = true;
				}

				if (isLikeCast)
					buf.append(" cast(");
				if (left.indexOf(".") == -1) {
					if(leftType.equals("searchKey")) {
						if(firstCnt) {
							buf.append("(obj.");
							firstCnt = false;
						} else {
							buf.append("obj.");
						}
					} else {
						buf.append("obj.");
					}
				}
				buf.append(left);
				if (isLikeCast)
					buf.append(" as varchar)");
				if (right == null) {
					if (operator.equals("!=") || 
							(operator.indexOf("=") == -1 && !operator.equalsIgnoreCase("is"))) {
						buf.append(" is not null");
					} else {
						buf.append(" is null");
					}
				} else {
					if (rightType == null || !rightType.equalsIgnoreCase(Filter.OPERANDTYPE_FIELD)) {
						right = "a" + i++;
						filterMap.put(right, f);
						if (!isLikeCast)
							paramTypeMap.put(right, fieldType);
					}
					buf.append(CommonUtil.SPACE).append(operator);
					buf.append(CommonUtil.SPACE).append(CommonUtil.COLON).append(right);
				}
				if(leftType.equals("searchKey")) {
					if(searchKeyCount > 0 && searchKeyCount == forSearchKeyCount)
						buf.append(")");
				}
			}
		}
		iWrap.setObj(i);
		return first;
	}
	
	public SwdDataRef getDataRef(String user, String objId, String level) throws SwdException {
		try {
			if (level == null)
				level = LEVEL_ALL;
			if (level.equals(LEVEL_ALL)) {
				SwdDataRef obj = (SwdDataRef)get(SwdDataRef.class, objId);
				return obj;
			} else {
				SwdDataRefCond cond = new SwdDataRefCond();
				cond.setObjId(objId);
				SwdDataRef[] objs = this.getDataRefs(user, cond, level);
				if (CommonUtil.isEmpty(objs))
					return null;
				return objs[0];
			}
		} catch (Exception e) {
			throw new SwdException(e);
		}
	}
	public void setDataRef(String user, SwdDataRef obj, String level) throws SwdException {
		try {
			set(obj);
		} catch (Exception e) {
			throw new SwdException(e);
		}
	}
	public void removeDataRef(String user, String objId) throws SwdException {
		try {
			remove(SwdDataRef.class, objId);
		} catch (Exception e) {
			throw new SwdException(e);
		}
	}
	private Query appendQuery(StringBuffer buf, SwdDataRefCond cond) throws Exception {
		String objId = null;
		String myFormId = null;
		String myFormFieldId = null;
		String myRecordId = null;
		String refFormId = null;
		String refFormFieldId = null;
		String refRecordId = null;
		if (cond != null) {
			objId = cond.getObjId();
			myFormId = cond.getMyFormId();
			myFormFieldId = cond.getMyFormFieldId();
			myRecordId = cond.getMyRecordId();
			refFormId = cond.getRefFormId();
			refFormFieldId = cond.getRefFormFieldId();
			refRecordId = cond.getRefRecordId();
		}
		buf.append(" from SwdDataRef obj");
		buf.append(" where obj.objId is not null");
		if (cond != null) {
			if (objId != null)
				buf.append(" and obj.objId = :objId");
			if (myFormId != null)
				buf.append(" and obj.myFormId = :myFormId");
			if (myFormFieldId != null)
				buf.append(" and obj.myFormFieldId = :myFormFieldId");
			if (myRecordId != null)
				buf.append(" and obj.myRecordId = :myRecordId");
			if (refFormId != null)
				buf.append(" and obj.refFormId = :refFormId");
			if (refFormFieldId != null)
				buf.append(" and obj.refFormFieldId = :refFormFieldId");
			if (refRecordId != null)
				buf.append(" and obj.refRecordId = :refRecordId");
		}
		this.appendOrderQuery(buf, "obj", cond);
		
		Query query = this.createQuery(buf.toString(), cond);
		if (cond != null) {
			if (objId != null)
				query.setString("objId", objId);
			if (myFormId != null)
				query.setString("myFormId", myFormId);
			if (myFormFieldId != null)
				query.setString("myFormFieldId", myFormFieldId);
			if (myRecordId != null)
				query.setString("myRecordId", myRecordId);
			if (refFormId != null)
				query.setString("refFormId", refFormId);
			if (refFormFieldId != null)
				query.setString("refFormFieldId", refFormFieldId);
			if (refRecordId != null)
				query.setString("refRecordId", refRecordId);
		}
		return query;
	}
	public long getDataRefSize(String user, SwdDataRefCond cond) throws SwdException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			buf.append(" count(obj)");
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			long count =((Long)list.get(0)).longValue();
			return count;
		} catch (Exception e) {
			throw new SwdException(e);
		}
	}
	public SwdDataRef[] getDataRefs(String user, SwdDataRefCond cond, String level) throws SwdException {
		try {
			if (level == null)
				level = LEVEL_LITE;
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			if (level.equals(LEVEL_ALL)) {
				buf.append(" obj");
			} else {
				buf.append(" obj.objId, obj.myFormId, obj.myFormFieldId, obj.myRecordId");
				buf.append(", obj.refFormId, obj.refFormFieldId, obj.refRecordId");
			}
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			if (!level.equals(LEVEL_ALL)) {
				List<SwdDataRef> objList = new ArrayList<SwdDataRef>();
				for (Iterator itr = list.iterator(); itr.hasNext();) {
					Object[] fields = (Object[]) itr.next();
					SwdDataRef obj = new SwdDataRef();
					int j = 0;
					obj.setObjId((String)fields[j++]);
					obj.setMyFormId((String)fields[j++]);
					obj.setMyFormFieldId((String)fields[j++]);
					obj.setMyRecordId((String)fields[j++]);
					obj.setRefFormId((String)fields[j++]);
					obj.setRefFormFieldId((String)fields[j++]);
					obj.setRefRecordId((String)fields[j++]);
					objList.add(obj);
				}
				list = objList;
			}
			SwdDataRef[] objs = new SwdDataRef[list.size()];
			list.toArray(objs);
			return objs;
		} catch (Exception e) {
			throw new SwdException(e);
		}
	}

	public void preFieldMapping(String user, SwfForm form, SwdRecord record, Map context) throws SwdException {
		if (record == null)
			return;
		preFieldMapping(user, form, new SwdRecord[]{record}, context);
	}
	public void preFieldMapping(String user, SwfForm form, SwdRecord[] records, Map context) throws SwdException {
		if (form == null || CommonUtil.isEmpty(records))
			return;
		List<SwfField> fieldList = getMappingFieldList(form, "pre");
		if (CommonUtil.isEmpty(fieldList))
			return;
		SwfField[] fields = new SwfField[fieldList.size()];
		fieldList.toArray(fields);
		try {
			for (SwdRecord record : records)
				preFieldMapping(user, record, fields, form.getMappingForms(), context);
		} catch (Exception e) {
			throw new SwdException(e);
		}
	}
	public void postFieldMapping(String user, SwfForm form, SwdRecord record, Map context) throws SwdException {
		if (form == null || record == null)
			return;
		List<SwfField> fieldList = getMappingFieldList(form, "post");
		if (CommonUtil.isEmpty(fieldList))
			return;
		SwfField[] fields = new SwfField[fieldList.size()];
		fieldList.toArray(fields);
		try {
			postFieldMapping(user, record, fields, form.getMappingForms(), context);
		} catch (Exception e) {
			throw new SwdException(e);
		}
	}
	private List<SwfField> getMappingFieldList(SwfForm form, String type) {
		if (form == null)
			return null;
		SwfField[] fields = form.getFields();
		if (CommonUtil.isEmpty(fields))
			return null;
		List<SwfField> list = new ArrayList<SwfField>();
		SwfMappings maps;
		SwfMapping[] mappings;
		for (SwfField field : fields) {
			maps = field.getMappings();
			if (maps == null)
				continue;
			if (type.equalsIgnoreCase("pre")) {
				mappings = maps.getPreMappings();
				if (CommonUtil.isEmpty(mappings))
					continue;
				for (SwfMapping mapping : mappings) {
					// TODO 매번 매핑 여부
				}
			} else if (type.equalsIgnoreCase("post")) {
				if (CommonUtil.isEmpty(maps.getPostMappings()))
					continue;
			}
			list.add(field);
		}
		return list;
	}
	private void preFieldMapping(String user, SwdRecord record, SwfField[] fields, SwfFormLink[] mappingForms, Map context) throws Exception {
		if (record == null || CommonUtil.isEmpty(fields))
			return;
		
		Map<String, SwfFormLink> mappingFormMap = toMap(mappingForms);
		
		if (context == null)
			context = new HashMap();
		if (user != null)
			context.put("user", user);
		context.put("self", record);
		context.put("mappingFormMap", mappingFormMap);
		
		Map refFieldMap = new HashMap();
		
		for (SwfField field : fields) {
			SwfMappings mappings = field.getMappings();
			if (mappings == null)
				continue;
			SwfMapping[] maps = mappings.getPreMappings();
			if (CommonUtil.isEmpty(maps))
				continue;
			String fieldId = field.getId();
			SwdDataField dataField = record.getDataField(fieldId);
			if (dataField == null) {
				dataField = new SwdDataField();
				dataField.setId(fieldId);
				dataField.setName(field.getName());
				dataField.setType(field.getSystemType());
				record.addDataField(dataField);
			}
			SwfFormat format = field.getFormat();
			if (format != null) {
				String formatType = format.getType();
				if (formatType != null && formatType.equalsIgnoreCase("refFormField")) {
					SwfFormRef formRef = format.getRefForm();
					if (formRef != null) {
						dataField.setRefForm(formRef.getId());
						SwfFieldRef fieldRef = formRef.getField();
						if (fieldRef != null) {
							dataField.setRefFormField(fieldRef.getId());
						}
					}
				}
			}
			String mappingType;
			Object sctResult;
			String mappingFormId;
			String mappingFormType;
			SwdRecord mappingRecord = null;
			String mappingFieldId;
			SwdDataField mappingDataField;
			Map<String, SwfMapping> formIdFieldMappingMap = new HashMap<String, SwfMapping>();
			for (SwfMapping fieldMapping : maps) {
				// TODO 매번 매핑 여부
				mappingType = fieldMapping.getType();
				if (mappingType == null || mappingType.equalsIgnoreCase("expression")) {
					sctResult = getSctManager().execute(fieldMapping.getExpression(), context);
					if (sctResult == null)
						continue;
					if (sctResult instanceof SwdDataField) {
						dataField.setValue(((SwdDataField)sctResult).getValue());
					} else {
						dataField.setValue(sctResult.toString());
					}
				} else if (mappingType.equalsIgnoreCase("mapping_form")) {
					mappingFormType = fieldMapping.getMappingFormType();
					if (!CommonUtil.isEmpty(mappingFormType) && (mappingFormType.equalsIgnoreCase("process_form"))) {
						mappingFormId = fieldMapping.getMappingFormId();
						if (CommonUtil.isEmpty(mappingFormId))
							continue;
						if (mappingFormId.equals("processParam") || mappingFormId.equals("subParameter")|| mappingFormId.equals("serviceParam")) {
							TskTask exeTask = (TskTask)context.get("exeTask");
							String instanceId = exeTask.getProcessInstId();
							if (instanceId == null)
								continue;
							IPrcManager prcManager = SwManagerFactory.getInstance().getPrcManager();
							PrcProcessInst prcInst = prcManager.getProcessInst(user, instanceId, null);
							if (prcInst.getInstVariable() == null || prcInst.getInstVariable().length() == 0)
								continue;
							InstanceVariables instVariables = (InstanceVariables)InstanceVariables.toObject(prcInst.getInstVariable());
							if (instVariables == null)
								continue;
							InstanceVariable instVariable = instVariables.getInstanceVariableById(fieldMapping.getFieldId());

							if (field.getFormat().getType().equalsIgnoreCase("userField")) {
								
								dataField.setRefRecordId(instVariable.getVariableValue());
								dataField.setRefForm("frm_user_SYSTEM");
								dataField.setRefFormField("4");
								SwoUser userObj = SwManagerFactory.getInstance().getSwoManager().getUser(user, instVariable.getVariableValue(), IManager.LEVEL_LITE);
								if (userObj != null) {
									dataField.setValue(userObj.getPosition() + " " + userObj.getName());
								} else {
									dataField.setValue(instVariable.getVariableValue());
								}
							} else  {
								dataField.setValue(instVariable.getVariableValue());
								dataField.setRefRecordId("");
							}
							
							
							populateRefDataField(user, dataField);
						}
						formIdFieldMappingMap.put(mappingFormId, fieldMapping);
						continue;
					}
					mappingRecord = getMappingRecord(user, fieldMapping, context);
					if (mappingRecord == null)
						continue;
					mappingFieldId = fieldMapping.getFieldId();
					if (CommonUtil.isEmpty(mappingFieldId))
						continue;
					mappingDataField = mappingRecord.getDataField(mappingFieldId);
					if (mappingDataField == null)
						continue;
					dataField.setValue(mappingDataField.getValue());
					dataField.setRefRecordId(mappingDataField.getRefRecordId());
					populateRefDataField(user, dataField);
				}
			}
			if (CommonUtil.isEmpty(formIdFieldMappingMap))
				continue;
			
			TskTask exeTask = (TskTask)context.get("exeTask");
			if (exeTask == null)
				continue;
			TskTask mappingTask = getMappingTask(exeTask, formIdFieldMappingMap, context);
			if (mappingTask == null)
				continue;
			String mappingTaskDoc = mappingTask.getDocument();
			if (mappingTaskDoc == null)
				continue;
			mappingTaskDoc = mappingTaskDoc.trim();
			if (!mappingTaskDoc.startsWith("<") || !mappingTaskDoc.endsWith(">"))
				continue;
			mappingRecord = (SwdRecord)SwdRecord.toObject(mappingTaskDoc);
			
			SwfMapping fieldMapping = formIdFieldMappingMap.get(mappingTask.getForm());
			mappingFieldId = fieldMapping.getFieldId();
			if (CommonUtil.isEmpty(mappingFieldId))
				continue;
			mappingDataField = mappingRecord.getDataField(mappingFieldId);
			if (mappingDataField == null)
				continue;
			dataField.setValue(mappingDataField.getValue());
			dataField.setRefRecordId(mappingDataField.getRefRecordId());
			populateRefDataField(user, dataField);
		}
	}
	private void populateRefDataField(String user, SwdDataField dataField) throws Exception {
		if (CommonUtil.toNull(dataField.getRefRecordId()) != null ||
				CommonUtil.toNull(dataField.getRefForm()) == null || 
				CommonUtil.toNull(dataField.getRefFormField()) == null || 
				CommonUtil.toNull(dataField.getValue()) == null)
			return;
		
		SwdRecordCond recCond = new SwdRecordCond();
		recCond.setFormId(dataField.getRefForm());
		recCond.addFilter(new Filter("=", dataField.getRefFormField(), dataField.getValue()));
		recCond.setOrders(new Order[] {new Order("createdTime", false)});
		recCond.setPageSize(1);
		SwdRecord[] rec = getRecords(user, recCond, LEVEL_ALL);
		if (CommonUtil.isEmpty(rec))
			return;
		
		dataField.setRefRecordId(rec[0].getRecordId());
	}
	private TskTask getMappingTask(TskTask exeTask, Map<String, SwfMapping> formIdFieldMappingMap, Map context) throws Exception {
		if (CommonUtil.isEmpty(formIdFieldMappingMap))
			return null;
		String exeTaskForm = exeTask.getForm();
		if (exeTaskForm != null && formIdFieldMappingMap.containsKey(exeTaskForm))
			return exeTask;

		TskTask task = null;
		TskTask nowTask;
		Date exeDate;
		long exeDateLong = 0;
		for (String formId : formIdFieldMappingMap.keySet()) {
			if (context.containsKey(formId+"_Task")) {
				nowTask = (TskTask)context.get(formId+"_Task");
				if (task == null)
					task = nowTask;
				exeDate = nowTask.getExecutionDate();
				if (exeDate == null)
					continue;
				if (exeDateLong < exeDate.getTime()) {
					task = nowTask;
					exeDateLong = exeDate.getTime();
				}
				continue;
			}
			
			TskTaskCond cond = new TskTaskCond();
			cond.setPageSize(1);
			cond.setProcessInstId(exeTask.getProcessInstId());
			cond.setStatus(CommonUtil.toDefault((String) MisUtil.taskStatusMap().get("executed"), "executed"));
			//cond.setTypeNotIns(new String[]{"xor", "or", "and", "route", "reference", "approval"});
			cond.setTypeNotIns(new String[]{"xor", "or", "and", "route", "REFERENCE", "APPROVAL"});
			cond.setFormIns(CommonUtil.toStringArray(formIdFieldMappingMap.keySet()));
			cond.setOrders(new Order[] {new Order("executionDate", false)});
			TskTask[] tasks = SwManagerFactory.getInstance().getTskManager().getTasks("smartAdvisor", cond, null);
			if (CommonUtil.isEmpty(tasks))
				continue;
			nowTask = tasks[0];
			context.put(nowTask.getForm()+"_Task", nowTask);
			exeDate = nowTask.getExecutionDate();
			if (exeDate == null)
				continue;
			if (exeDateLong < exeDate.getTime()) {
				task = nowTask;
				exeDateLong = exeDate.getTime();
			}
		}
		
		return task;
	}
	private boolean isExecutablePostMappingCondition(SwdRecord record, SwfField field) throws Exception {
		SwfMappings mappings = field.getMappings();
		//내보내기 조건식
		SwfConditions swfConds = mappings.getConds();
		if (swfConds == null)
			return true;
		
		String condsOperator = swfConds.getOperator();//OR, AND
		
		SwfCondition[] conds = swfConds.getCond();
		if (CommonUtil.isEmpty(conds))
			return true;
		for (int i = 0; i < conds.length; i++) {
			boolean result = false;
			
			String operator = conds[i].getOperator();
			SwfOperand first = conds[i].getFirst();
			SwfOperand second = conds[i].getSecond();
			
			String selfFieldId = first.getFieldId();
			String selfFieldIdValue = CommonUtil.toNotNull(record.getDataFieldValue(selfFieldId));
			
			String secondType = second.getType();
			if (secondType.equalsIgnoreCase("expression")) {
				String secondValue = CommonUtil.toNotNull(second.getExpression());
				if (operator.equalsIgnoreCase("=")) {
					result = selfFieldIdValue.equalsIgnoreCase(secondValue);
				} else if (operator.equalsIgnoreCase("!=")) {
					result = !selfFieldIdValue.equalsIgnoreCase(secondValue);
				} else if (operator.equalsIgnoreCase("like")) {
					result = selfFieldIdValue.indexOf(secondValue) != -1;
				} else if (operator.equalsIgnoreCase(">=")) {
					int tempSelf = 0;
					int tempSecond = 0;
					try {
						tempSelf = Integer.parseInt(selfFieldIdValue);
						tempSecond = Integer.parseInt(secondValue);
					} catch (Exception e) {
						result = false;
					}
					result = tempSelf >= tempSecond;
				} else if (operator.equalsIgnoreCase("&lt;=") || operator.equalsIgnoreCase("<=")) {
					int tempSelf = 0;
					int tempSecond = 0;
					try {
						tempSelf = Integer.parseInt(selfFieldIdValue);
						tempSecond = Integer.parseInt(secondValue);
					} catch (Exception e) {
						result = false;
					}
					result = tempSelf <= tempSecond;
				} else if (operator.equalsIgnoreCase(">")) {
					int tempSelf = 0;
					int tempSecond = 0;
					try {
						tempSelf = Integer.parseInt(selfFieldIdValue);
						tempSecond = Integer.parseInt(secondValue);
					} catch (Exception e) {
						result = false;
					}
					result = tempSelf > tempSecond;
				} else if (operator.equalsIgnoreCase("&lt;") || operator.equalsIgnoreCase("<")) {
					int tempSelf = 0;
					int tempSecond = 0;
					try {
						tempSelf = Integer.parseInt(selfFieldIdValue);
						tempSecond = Integer.parseInt(secondValue);
					} catch (Exception e) {
						result = false;
					}
					result = tempSelf < tempSecond;
				}
			} else {
				String secondFieldId = second.getFieldId();
				String secondFieldIdValue = CommonUtil.toNotNull(record.getDataFieldValue(secondFieldId));
				if (operator.equalsIgnoreCase("=")) {
					result = selfFieldIdValue.equalsIgnoreCase(secondFieldIdValue);
				} else if (operator.equalsIgnoreCase("!=")) {
					result = !selfFieldIdValue.equalsIgnoreCase(secondFieldIdValue);
				} else if (operator.equalsIgnoreCase("like")) {
					result = selfFieldIdValue.indexOf(secondFieldIdValue) != -1;
				} else if (operator.equalsIgnoreCase(">=")) {
					int tempSelf = 0;
					int tempSecond = 0;
					try {
						tempSelf = Integer.parseInt(selfFieldIdValue);
						tempSecond = Integer.parseInt(secondFieldIdValue);
					} catch (Exception e) {
						result = false;
					}
					result = tempSelf >= tempSecond;
				} else if (operator.equalsIgnoreCase("&lt;=") || operator.equalsIgnoreCase("<=")) {
					int tempSelf = 0;
					int tempSecond = 0;
					try {
						tempSelf = Integer.parseInt(selfFieldIdValue);
						tempSecond = Integer.parseInt(secondFieldIdValue);
					} catch (Exception e) {
						result = false;
					}
					result = tempSelf <= tempSecond;
				} else if (operator.equalsIgnoreCase(">")) {
					int tempSelf = 0;
					int tempSecond = 0;
					try {
						tempSelf = Integer.parseInt(selfFieldIdValue);
						tempSecond = Integer.parseInt(secondFieldIdValue);
					} catch (Exception e) {
						result = false;
					}
					result = tempSelf > tempSecond;
				} else if (operator.equalsIgnoreCase("&lt;") || operator.equalsIgnoreCase("<")) {
					int tempSelf = 0;
					int tempSecond = 0;
					try {
						tempSelf = Integer.parseInt(selfFieldIdValue);
						tempSecond = Integer.parseInt(secondFieldIdValue);
					} catch (Exception e) {
						result = false;
					}
					result = tempSelf < tempSecond;
				}
			}
			if (condsOperator.equalsIgnoreCase("or")) {
				if (result)
					return result;
			} else {
				if (!result)
					return result;
			}
		}
		return condsOperator.equalsIgnoreCase("or") ? false : true;
	}
	private void postFieldMapping(String user, SwdRecord record, SwfField[] fields, SwfFormLink[] mappingForms, Map context) throws Exception {
		if (record == null || CommonUtil.isEmpty(fields))
			return;
		
		Map<String, SwdRecord> mappingRecordMap  = new HashMap<String, SwdRecord>();
		Map<String, SwfFormLink> mappingFormMap = toMap(mappingForms);
		
		for (SwfField field : fields) {
			SwfMappings mappings = field.getMappings();
			if (mappings == null)
				continue;
			
			SwfMapping[] maps = mappings.getPostMappings();
			if (CommonUtil.isEmpty(maps))
				continue;
			
			//내보내기 조건식을 비교하고 결과가 TURE 라면 내보내기 진행을 한다
			if (!isExecutablePostMappingCondition(record, field)) 
				continue;
			
			if (context == null)
				context = new HashMap();
			if (user != null)
				context.put("user", user);
			context.put("self", record);
			context.put("mappingFormMap", mappingFormMap);
			
			String type;
			SwdRecord mappingRecord = null;
			SwfFormLink mappingForm;
			String mappingFormId;
			String mappingFieldId;
			String mappingFieldName;
			SwdDataField mappingDataField;
			String fieldId;
			SwdDataField dataField;
			String dataFieldValue = null;
			String dataFieldRefRecordId = null;
			String dataFieldRefFormId = null;
			String dataFieldRefFormFieldId = null;
			for (SwfMapping map : maps) {
				type = map.getType();
				if (type == null || type.equalsIgnoreCase("expression")) {
					// TODO
					Object expRes = getSctManager().execute(map.getExpression(), context);
				} else if (type.equalsIgnoreCase("mapping_form")) {
					if (map.getMappingFormId().equalsIgnoreCase("processParam")) {
						TskTask exeTsk = (TskTask)context.get("task");
						if (exeTsk == null)
							continue;
						String processInstId = exeTsk.getProcessInstId();
						IPrcManager prcMgr = SwManagerFactory.getInstance().getPrcManager();
						
						PrcProcessInst prcInst = prcMgr.getProcessInst(user, processInstId, IManager.LEVEL_ALL);
						if (prcInst == null)
							continue;
						
						String instVariablesStr = prcInst.getInstVariable();

						SwdDataField dField = record.getDataField(field.getId());
						if (CommonUtil.isEmpty(instVariablesStr) && dField != null) {
							InstanceVariable instVariable = new InstanceVariable();
							instVariable.setId(map.getFieldId());
							instVariable.setVariableName(map.getFieldName());
							instVariable.setInstType("process");
							instVariable.setInstId(processInstId);
							instVariable.setVariableType(field.getFormat().getType());
							instVariable.setVariableMode("INOUT");
							if (field.getFormat().getType().equalsIgnoreCase("userField")) {
								instVariable.setVariableValue(dField.getRefRecordId());
							} else {
								instVariable.setVariableValue(dField.getValue());
							}
							
							InstanceVariables instVariables = new InstanceVariables();
							instVariables.addInstanceVariable(instVariable);
							prcInst.setInstVariable(instVariables.toString());
							prcMgr.setProcessInst(user, prcInst, IManager.LEVEL_ALL);
							
						} else if (dField != null){
							
							InstanceVariables instVariables = (InstanceVariables)InstanceVariables.toObject(instVariablesStr);
							InstanceVariable instVar = instVariables.getInstanceVariableById(map.getFieldId());
							if (instVar == null) {
								InstanceVariable instVariable = new InstanceVariable();
								instVariable.setId(map.getFieldId());
								instVariable.setVariableName(map.getFieldName());
								instVariable.setInstType("process");
								instVariable.setInstId(processInstId);
								instVariable.setVariableType(field.getFormat().getType());
								instVariable.setVariableMode("INOUT");
								if (field.getFormat().getType().equalsIgnoreCase("userField")) {
									instVariable.setVariableValue(dField.getRefRecordId());
								} else {
									instVariable.setVariableValue(dField.getValue());
								}
								
								instVariables.addInstanceVariable(instVariable);
							} else {
								if (field.getFormat().getType().equalsIgnoreCase("userField")) {
									instVar.setVariableValue(dField.getRefRecordId());
								} else {
									instVar.setVariableValue(dField.getValue());
								}
							}

							prcInst.setInstVariable(instVariables.toString());
							prcMgr.setProcessInst(user, prcInst, IManager.LEVEL_ALL);
						}
						continue;
					}
					mappingFormId = map.getMappingFormId();
					mappingFieldId = map.getFieldId();
					mappingFieldName = map.getFieldName();
					if (CommonUtil.isEmpty(mappingFieldId))
						continue;
					if (!mappingFormMap.containsKey(mappingFormId))
						continue;
					
					mappingRecord = getMappingRecord(user, map, context);
					fieldId = field.getId();
					dataField = record.getDataField(fieldId);
					if (dataField != null) {
						dataFieldValue = dataField.getValue();
						dataFieldRefRecordId = dataField.getRefRecordId();
						dataFieldRefFormFieldId = dataField.getRefFormField();
						dataFieldRefFormId = dataField.getRefForm();
					}

					if (mappingFieldId.equalsIgnoreCase("id")) {
//						if (mappingRecordMap.containsKey(record.getRecordId()))
//							mappingRecordMap.remove(record.getRecordId());
						if (mappingRecordMap.containsKey(mappingRecord.getRecordId()))
							mappingRecordMap.remove(mappingRecord.getRecordId());
						mappingRecord.setRecordId(dataFieldValue);
					} else {
						mappingDataField = mappingRecord.getDataField(mappingFieldId);
						if (mappingDataField == null) {
							mappingDataField = new SwdDataField();
							mappingDataField.setId(mappingFieldId);
							mappingDataField.setName(mappingFieldName);
							mappingDataField.setType(field.getSystemType());
							mappingRecord.addDataField(mappingDataField);
						}
						mappingDataField.setValue(dataFieldValue);
						mappingDataField.setRefRecordId(dataFieldRefRecordId);
						mappingDataField.setRefForm(dataFieldRefFormId);
						mappingDataField.setRefFormField(dataFieldRefFormFieldId);
					}
					if (!mappingRecordMap.containsKey(mappingRecord.getRecordId()))
						mappingRecordMap.put(mappingRecord.getRecordId(), mappingRecord);
				}
			}
		}
		if (!CommonUtil.isEmpty(mappingRecordMap)) {
			for (SwdRecord mappingRecord : mappingRecordMap.values()) {
				populateRecord(user, mappingRecord);
				//데이터 내보내기에서 연결되는 데이터가 없을경우 데이터를 새로 만든다
				//만들때 accessLevel createUser createDate 등 기본적으로 들어가야 할데이터를 셋팅한다
				defaultSetRecord(user, mappingRecord);
				
				//내보내기 할시에는 어드민 계정으로 내보내기를 한다
				mappingRecord.setCreationUser("admin");
				mappingRecord.setWorkSpaceId("admin");
				mappingRecord.setWorkSpaceType("4");
				SwManagerFactory.getInstance().getSwdManager().setRecord("admin", mappingRecord, null);
				
				setDataRefByRecord(user, mappingRecord);
				
				if (!context.containsKey("task"))
					continue;
				TskTask task = (TskTask)context.get("task");
				if (task == null)
					continue;
				String[] recIds = task.getExtendedPropertyValues("mappingRecordId");
				if (CommonUtil.contains(recIds, mappingRecord.getRecordId()))
					continue;
				task.addExtendedProperty(new Property("mappingRecordId", mappingRecord.getRecordId()));
			}
		}
	}
	private void defaultSetRecord(String user, SwdRecord obj) throws Exception {
		if (obj == null)
			return;
		if (obj.getCreationUser() == null)
			obj.setCreationUser(user);
		if (obj.getCreationDate() == null)
			obj.setCreationDate(new LocalDate());
		obj.setModificationUser(user);
		obj.setModificationDate(new LocalDate());
		
		if (obj.getAccessLevel() == null)
			obj.setAccessLevel(AccessPolicy.LEVEL_DEFAULT + "");
		if (obj.getWorkSpaceId() == null && obj.getWorkSpaceType() == null) {
			obj.setWorkSpaceId(user);
			obj.setWorkSpaceType("4");//사용자 공간
		}
	}
	private void populateRecord(String user, SwdRecord obj) throws Exception {
		if (obj == null)
			return;
		
		String formId = obj.getFormId();
		String recordId = obj.getRecordId();
		if (recordId == null)
			return;
		
		SwdDataRefCond cond = new SwdDataRefCond();
		cond.setMyFormId(formId);
		cond.setMyRecordId(recordId);
		SwdDataRef[] dataRefs = this.getDataRefs(user, cond, null);
		if (CommonUtil.isEmpty(dataRefs))
			return;
		
		String fieldId;
		SwdDataField dataField;
		for (SwdDataRef dataRef : dataRefs) {
			fieldId = dataRef.getMyFormFieldId();
			dataField = obj.getDataField(fieldId);
			if (dataField == null)
				continue;
			dataField.setRefForm(dataRef.getRefFormId());
			dataField.setRefFormField(dataRef.getRefFormFieldId());
			dataField.setRefRecordId(dataRef.getRefRecordId());
		}
	}
	//일반적으로 setRecord를 수행하면 swdAdvisor의 postSetRecord에서 DataRef를 생성한다 하지만 내보내기를 통한 데이터생성은
	//swdAdvisor를 타지 않음으로 내보내기 dataRef 성성을 위해 swdAdvisor 해당 소스를 편집하여 아래에 복사해 사용한다
	private void setDataRefByRecord(String user, SwdRecord obj) throws Exception {
		// 도메인 조회
		String formId = obj.getFormId();
		if ("DEFAULTFORM".equalsIgnoreCase(formId)) {
			
		} else {
			String domainId = null;
			String recordId = null;
			String name = null;
			String title = null;
			domainId = obj.getDomainId();
			SwdDomain domain = null;
			if (CommonUtil.isEmpty(formId)) {
				if (CommonUtil.isEmpty(domainId))
					return;
				domain = getDomain(user, domainId, null);
				formId = domain.getFormId();
				obj.setFormId(formId);
			} else {
				SwdDomainCond domainCond = new SwdDomainCond();
				domainCond.setFormId(formId);
				domain = getDomain(user, domainCond, null);
				domainId = domain.getObjId();
				obj.setDomainId(domainId);
			}
			if (domain == null)
				return;
			
			SwdField[] fields = domain.getFields();
			if (CommonUtil.isEmpty(fields))
				return;
			
			// 데이터 참조 삭제
			recordId = obj.getRecordId();
			this.removeDataRefsByRecordId(user, domain.getFormId(), recordId);
			
			String dataFieldId;
			SwdDataField dataField;
			int refFormFieldPathIndex;
			String refFormId;
			String refFormFieldId;
			String refRecordId;
			SwdDataRef dataRef;
			for (SwdField field : fields) {
				dataFieldId = field.getFormFieldId();
				dataField = obj.getDataField(dataFieldId);
				if (dataField == null)
					continue;
				String refFormFieldPath = field.getFormFieldPath();
				if (CommonUtil.isEmpty(refFormFieldPath)) {
					refFormId = dataField.getRefForm();
					refFormFieldId = dataField.getRefFormField();
					refRecordId = dataField.getRefRecordId();
					if (CommonUtil.isEmpty(refFormId) || CommonUtil.isEmpty(refFormFieldId) || CommonUtil.isEmpty(refRecordId))
						continue;
					dataRef = this.newDataRef(obj.getRecordId(), domain.getFormId(), dataFieldId, refRecordId, refFormId, refFormFieldId);
					setDataRef(user, dataRef, null);
				} else {
					refFormFieldPathIndex = refFormFieldPath.indexOf(".");
					if (refFormFieldPathIndex == -1)
						continue;
					refFormId = refFormFieldPath.substring(0, refFormFieldPathIndex);
					refFormFieldId = refFormFieldPath.substring(refFormFieldPathIndex + 1);
					refRecordId = dataField.getRefRecordId();
					if (CommonUtil.isEmpty(refRecordId))
						continue;
					dataRef = this.newDataRef(obj.getRecordId(), domain.getFormId(), dataFieldId, refRecordId, refFormId, refFormFieldId);
					setDataRef(user, dataRef, null);
				}
			}
		}
	}
	private void removeDataRefsByRecordId(String user, String formId, String recordId) throws Exception {
		SwdDataRefCond dataRefCond = new SwdDataRefCond();
		dataRefCond.setMyFormId(formId);
		dataRefCond.setMyRecordId(recordId);
		SwdDataRef[] dataRefs = getDataRefs(user, dataRefCond, IManager.LEVEL_LITE);
		if (!CommonUtil.isEmpty(dataRefs)) {
			for (SwdDataRef dataRef : dataRefs)
				removeDataRef(user, dataRef.getObjId());
		}
	}
	private SwdDataRef newDataRef(String myRecordId, String myFormId, String myFormFieldId, String refRecordId, String refFormId, String refFormFieldId) {
		SwdDataRef dataRef = new SwdDataRef();
		dataRef.setMyFormId(myFormId);
		dataRef.setMyFormFieldId(myFormFieldId);
		dataRef.setMyRecordId(myRecordId);
		dataRef.setRefFormId(refFormId);
		dataRef.setRefFormFieldId(refFormFieldId);
		dataRef.setRefRecordId(refRecordId);
		return dataRef;
	}
	
//	private void populateRecord(String user, SwdRecord record) throws Exception {
//		String formId = record.getFormId();
//		if (CommonUtil.isEmpty(formId))
//			return;
//		SwfForm form = getSwfManager().getForm(user, formId);
//		if (form == null)
//			return;
//		SwfField[] fields = form.getFields();
//		if (CommonUtil.isEmpty(fields))
//			return;
//		String fieldId;
//		SwdDataField dataField;
//		SwfFormat format;
//		String formatType;
//		for (SwfField field : fields) {
//			fieldId = field.getId();
//			dataField = record.getDataField(fieldId);
//			if (dataField == null)
//				continue;
//			if (!CommonUtil.isEmpty(dataField.getRefForm()) && 
//					!CommonUtil.isEmpty(dataField.getRefFormField()))
//				continue;
//			format = field.getFormat();
//			if (format == null)
//				continue;
//			formatType = format.getType();
//			if (CommonUtil.isEmpty(formatType))
//				continue;
//			if (formatType.equalsIgnoreCase("userField")) {
//				dataField.setRefForm("frm_user_SYSTEM");
//				dataField.setRefFormField("4");
//			}
//		}
//	}
	private SwdRecord getMappingRecord(String user, SwfMapping map, Map context) throws Exception {
		String formType = map.getMappingFormType();
		if (formType != null && !formType.equalsIgnoreCase("info_form"))
			return null;
		String mappingFormId = map.getMappingFormId();
		String fieldId = map.getFieldId();
		String valueFunc = map.getValueFunc();
		return getRecordValue(user, mappingFormId, fieldId, valueFunc, context);
	}
	private SwdRecord getRecordValue(String user, String mappingFormId, String fieldId, String valueFunc, Map context) throws SwdException {
		if (CommonUtil.isEmpty(mappingFormId))
			return null;
		boolean isValueFunc = !CommonUtil.isEmpty(valueFunc) && !valueFunc.equalsIgnoreCase("value");
		if (isValueFunc)
			mappingFormId += ";" + valueFunc;
		//ykm instanceof
		if (context.containsKey(mappingFormId) && context.get(mappingFormId) instanceof SwdRecord) {
			return (SwdRecord)context.get(mappingFormId);
		} else {
			if (!context.containsKey("mappingFormMap"))
				return null;
			Map<String, SwfFormLink> mappingFormMap = (Map<String, SwfFormLink>)context.get("mappingFormMap");
			SwdRecord record = null;
			if (isValueFunc) {
				double value;
				try {
					value = getMappingRecordValue(user, mappingFormMap.get(mappingFormId), fieldId, valueFunc, context);
				} catch (Exception e) {
					throw new SwdException(e);
				}
				record = new SwdRecord();
				record.setDataFieldValue(fieldId, null, value+"");
			} else {
				try {
					record = getMappingRecord(user, mappingFormMap.get(mappingFormId), context);
				} catch (Exception e) {
					throw new SwdException(e);
				}
			}
			if (record == null) {
				record = new SwdRecord();
				record.setRecordId("dr_"+CommonUtil.newId());
				SwfFormLink mappingForm = mappingFormMap.get(mappingFormId);
				record.setFormId(mappingForm.getTargetFormId());
			}
			context.put(mappingFormId, record);
			return record;
		}
	}
	public SwdRecord getRecordByMappingForm(String user, SwdRecord self, SwfFormLink mappingForm) throws SwdException {
		Map context = new HashMap();
		if (user != null)
			context.put("user", user);
		if (self != null)
			context.put("self", self);
		try {
			return this.getMappingRecord(user, mappingForm, context);
		} catch (Exception e) {
			throw new SwdException(e);
		}
	}
	public double getRecordValueByMappingForm(String user, SwdRecord self, SwfFormLink mappingForm, String field, String func) throws SwdException {
		Map context = new HashMap();
		if (user != null)
			context.put("user", user);
		if (self != null)
			context.put("self", self);
		try {
			return this.getMappingRecordValue(user, mappingForm, field, func, context);
		} catch (Exception e) {
			throw new SwdException(e);
		}
	}
	public SwdRecord[] getRecordsByMappingForm(String user, SwdRecord self,
			SwfFormLink mappingForm) throws SwdException {
		Map context = new HashMap();
		if (user != null)
			context.put("user", user);
		if (self != null)
			context.put("self", self);
		try {
			return this.getMappingRecords(user, mappingForm, context);
		} catch (Exception e) {
			throw new SwdException(e);
		}
	}
	public String executeExpression(String user, String expression, SwdRecord self, SwfFormLink[] mappingForms) throws SwdException {
		if (!CommonUtil.isEmpty(expression))
			expression = StringUtils.replace(expression, "/", " div ");
		Map context = new HashMap();
		if (user != null)
			context.put("user", user);
		if (self != null)
			context.put("self", self);
		if (!CommonUtil.isEmpty(mappingForms))
			context.put("mappingFormMap", toMap(mappingForms));
		Object sctResult;
		try {
			sctResult = getSctManager().execute(expression, context);
		} catch (SctException e) {
			throw new SwdException(e);
		}
		if (sctResult == null)
			return null;
		if (sctResult instanceof SwdDataField) {
			return ((SwdDataField)sctResult).getValue();
		} else {
			return sctResult.toString();
		}
	}
	private double getMappingRecordValue(String user, SwfFormLink mappingForm, String funcField, String func, Map context) throws Exception {
		if (mappingForm == null)
			return 0;
		
		String formId = mappingForm.getTargetFormId();
		if (formId == null)
			return 0;
		
		SwfConditions conditions = mappingForm.getConds();
		if (conditions == null)
			return 0;
		SwfCondition[] conds = conditions.getCond();
		if (CommonUtil.isEmpty(conds))
			return 0;
		
		SwdFieldCond cond = new SwdFieldCond();
		cond.setFormId(formId);
		cond.setFormFieldId(funcField);
		SwdField[] fields = getFields(user, cond, null);
		if (!CommonUtil.isEmpty(fields))
			funcField = fields[0].getTableColumnName();

		SwdRecordCond recordCond = toRecordCond(formId, conds, context);
		return getRecordValue(user, funcField, func, recordCond);
	}
	private SwdRecord getMappingRecord(String user, SwfFormLink mappingForm, Map context) throws Exception {
		if (mappingForm == null)
			return null;
		
		String formId = mappingForm.getTargetFormId();
		if (formId == null)
			return null;
		
		SwfConditions conditions = mappingForm.getConds();
		if (conditions == null)
			return null;
		SwfCondition[] conds = conditions.getCond();
		if (CommonUtil.isEmpty(conds))
			return null;
		
		SwdRecordCond recordCond = toRecordCond(formId, conds, context);
		if (recordCond == null || CommonUtil.isEmpty(recordCond.getFilter()))
			return null;
		return getRecord(user, recordCond, IManager.LEVEL_ALL);
	}
	private SwdRecord[] getMappingRecords(String user, SwfFormLink mappingForm, Map context) throws Exception {
		if (mappingForm == null)
			return null;
		
		String formId = mappingForm.getTargetFormId();
		if (formId == null)
			return null;
		SwfCondition[] conds = null;
		SwfConditions conditions = mappingForm.getConds();
		if (conditions != null)
			conds = conditions.getCond();
		
		SwdRecordCond recordCond = toRecordCond(formId, conds, context);
		return getRecords(user, recordCond, IManager.LEVEL_ALL);
	}
	private SwdRecordCond toRecordCond(String formId, SwfCondition[] conds, Map context) throws Exception {
		SwdRecordCond recordCond = new SwdRecordCond();
		recordCond.setFormId(formId);
		
		if (conds == null) {
			recordCond.setPageSize(100);
			return recordCond;
		}
		
		String operator;
		SwfOperand first;
		SwfOperand second;
		for (SwfCondition cond : conds) {
			operator = cond.getOperator();
			first = cond.getFirst();
			second = cond.getSecond();
			if (CommonUtil.isEmpty(operator) || first == null || second == null)
				continue;
			String firstRes = preFieldMappingOperand(first, context);
			String secondRes = preFieldMappingOperand(second, context);
			recordCond.addFilter(new Filter(operator, firstRes, secondRes));
		}
		return recordCond;
	}
	private String preFieldMappingOperand(SwfOperand operand, Map context) throws Exception {
		String type = operand.getType();
		String fieldId = operand.getFieldId();
		if (type.equalsIgnoreCase("self")) {
			SwdRecord self = (SwdRecord)context.get("self");
			SwdDataField dataField = self.getDataField(fieldId);
			if (dataField == null)
				return null;
			return dataField.getValue();
		} else if (type.equalsIgnoreCase("other")) {
			return fieldId;
		} else if (type.equalsIgnoreCase("expression")) {
			Object obj = getSctManager().execute(operand.getExpression(), context);
			if (obj == null)
				return null;
			return obj.toString();
		}
		return null;
	}
	private Map<String, SwfFormLink> toMap(SwfFormLink[] mappingForms) {
		if (CommonUtil.isEmpty(mappingForms))
			return null;
		Map<String, SwfFormLink> map = new HashMap<String, SwfFormLink>();
		String id;
		for (SwfFormLink mappingForm : mappingForms) {
			id = mappingForm.getId();
			if (CommonUtil.isEmpty(id))
				continue;
			map.put(id, mappingForm);
		}
		return map;
	}
	
	public List<String> getDomainTableColumnNameList(String user, String table) throws SwdException {
		Connection con = null;
		try {
			con = getSession().connection();
			return DbUtil.getFieldNameList(table, "id", con);
		} catch (Exception e) {
			throw new SwdException(e);
		} finally {
			ConnectionUtil.close(con);
		}
	}
	@Override
	public void addTableColumn(String user, String table, String column, String type) throws SwdException {
		try {
			if(table == null || column == null || type == null)
				return;
			if(this.getDbType().equalsIgnoreCase("sqlserver"))
				type = StringUtils.replace(type, "timestamp", "datetime");
	
			StringBuffer buf = new StringBuffer("select ").append(column).append(" from ").append(table);
			Query query = this.createSqlQuery(buf.toString(), null);
			query.list();
		} catch (Exception e) {
			StringBuffer buf = new StringBuffer("alter table ").append(table);
			buf.append(" add ").append(column).append(CommonUtil.SPACE).append(type);
			Query query = this.getSession().createSQLQuery(buf.toString());
			query.executeUpdate();
			return;
		}
	}
	public ISwfManager getSwfManager() {
		if (swfManager == null)
			swfManager = SwManagerFactory.getInstance().getSwfManager();
		return swfManager;
	}
	public void setSwfManager(ISwfManager swfManager) {
		this.swfManager = swfManager;
	}
	public ISctManager getSctManager() {
		if (sctManager == null)
			sctManager = SwManagerFactory.getInstance().getSctManager();
		return sctManager;
	}
	public void setSctManager(ISctManager sctManager) {
		this.sctManager = sctManager;
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

	public List<SwdDomainFieldView> findDomainFieldViewList(String formId) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("from SwdDomainFieldView");
		stringBuffer.append(" where domainId = (select id from SwdDomain where masterId is null and formId = '" + formId + "' and formVersion = (select max(formVersion) from SwdDomain where formId = '" + formId + "'))");
		stringBuffer.append(" order by dispOrder asc");
		Query query = this.getSession().createQuery(stringBuffer.toString());
		List<SwdDomainFieldView> fieldViewList = query.list();
		return fieldViewList;
	}

	public SwdField[] getViewFieldList(String packageId, String formId) throws SwdException {
		List<SwfFormFieldDef> formFieldDefList = null;
		try {
			formFieldDefList = getSwfManager().findFormFieldByForm(formId, true);
		} catch (SwfException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("select domainfield.formFieldId, domainfield.formFieldName, domainfield.formFieldType, domainfield.tableColumnName, domainfield.displayOrder");
		stringBuffer.append("  from PkgPackage pkg, SwfForm form, SwdDomain domain, SwdField domainfield");
		stringBuffer.append(" where pkg.packageId = form.packageId");
		stringBuffer.append("   and form.id = domain.formId");
		stringBuffer.append("   and domain.objId = domainfield.domain");
		stringBuffer.append("   and pkg.packageId = '" + packageId + "' ");
		stringBuffer.append(" order by domainfield.displayOrder asc");

		Query query = this.getSession().createQuery(stringBuffer.toString());

		List<SwdField> list = query.list();

		if (list == null || list.isEmpty())
			return null;
		List<SwdField> objList = new ArrayList<SwdField>();

		for (Iterator itr = list.iterator(); itr.hasNext();) {
			Object[] fields = (Object[]) itr.next();
			SwdField obj = new SwdField();
			int j = 0;
			obj.setFormFieldId((String)fields[j++]);
			obj.setFormFieldName((String)fields[j++]);
			obj.setFormFieldType((String)fields[j++]);
			obj.setTableColumnName((String)fields[j++]);
			obj.setDisplayOrder((Integer)fields[j++]);
			for(int i=0; i<formFieldDefList.size(); i++) {
				SwfFormFieldDef swfFormFieldDef = formFieldDefList.get(i);
				String formFieldId = swfFormFieldDef.getId();
				String viewingType = swfFormFieldDef.getViewingType();
				if(formFieldId.equals(obj.getFormFieldId()) && !viewingType.equals("richEditor") && !viewingType.equals("textArea") && !viewingType.equals("dataGrid")) {
					objList.add(obj);
				}
			}
		}
		SwdField[] swdFields = new SwdField[objList.size()];
		objList.toArray(swdFields);

		return swdFields;
	}

	public SwdRecordExtend[] getCtgPkg(String packageId) throws SwdException {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("select ctg.objId as subCtgId, ctg.name as subCtgName, parentCtg.objId as parentCtgId, parentCtg.name as parentCtgName");
		stringBuffer.append("  from SwfForm form, SwdDomain domain, PkgPackage pkg, CtgCategory ctg, CtgCategory parentCtg");
		stringBuffer.append(" where form.packageId = pkg.packageId");
		stringBuffer.append("   and domain.formId = form.id");
		stringBuffer.append("   and pkg.categoryId = ctg.objId");
		stringBuffer.append("   and ctg.parentId = parentCtg.objId");
		stringBuffer.append("   and pkg.packageId = '" + packageId + "'");

		Query query = this.getSession().createQuery(stringBuffer.toString());

		List<SwdField> list = query.list();

		if (list == null || list.isEmpty())
			return null;
		List<SwdRecordExtend> objList = new ArrayList<SwdRecordExtend>();
		for (Iterator itr = list.iterator(); itr.hasNext();) {
			Object[] fields = (Object[]) itr.next();
			SwdRecordExtend obj = new SwdRecordExtend();
			int j = 0;
			obj.setSubCtgId((String)fields[j++]);
			obj.setSubCtg((String)fields[j++]);
			obj.setParentCtgId((String)fields[j++]);
			obj.setParentCtg((String)fields[j++]);
			objList.add(obj);
		}
		SwdRecordExtend[] swdRecordExtends = new SwdRecordExtend[objList.size()];
		objList.toArray(swdRecordExtends);

		return swdRecordExtends;
	}

	@Override
	public String getTableColName(String domainId, String formFieldId) throws SwdException {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("select tableColName");
		stringBuffer.append(" from SwdDomainFieldView");
		stringBuffer.append(" where domainId = '" + domainId + "' and formFieldId = '" + formFieldId + "'");

		Query query = this.getSession().createQuery(stringBuffer.toString());

		String tableColName = (String)query.uniqueResult();

		if(tableColName == null)
			return null;

		return tableColName;

	}

	@Override
	public int getTableRowCount(String tableName) throws SwdException {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("select count(*) from ");
		stringBuffer.append(tableName);
		Query query = this.getSession().createSQLQuery(stringBuffer.toString());
		int count = (Integer)query.uniqueResult();
		return count;
	}

	@Override
	public void addHits(String tableName, String recordId) throws SwdException {
		try {
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("select hits from ").append(tableName).append(" where id= '"+recordId+"' ");
			Query query = this.getSession().createSQLQuery(stringBuffer.toString());
			int hits = CommonUtil.toInt(query.uniqueResult());
			stringBuffer = new StringBuffer();
			stringBuffer.append("update ").append(tableName).append(" set hits=").append(hits+1).append(" where id= '"+recordId+"' ");
			query = this.getSession().createSQLQuery(stringBuffer.toString());
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new SwdException();
		}
	}
	@Override
	public long getObjectsCountByFormFieldId(String domainId, String formFieldId, String tableName, String instanceId, String fieldValue) throws SwdException {
		try {
			String tableColName = getTableColName(domainId, formFieldId);
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("select count(*)").append(" from ").append(tableName).append(" where id != :instanceId and ").append(tableColName).append(" = :fieldValue");
			Query query = this.getSession().createSQLQuery(stringBuffer.toString());
			query.setString("instanceId", instanceId);
			query.setString("fieldValue", fieldValue);
			List list = query.list();
			Object sizeObj = list.get(0);
			return Long.parseLong(sizeObj.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

}

                                                                                                                                                                                                                                                                                                                                                                                                                   