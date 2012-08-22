/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 7. 19.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.autoindex.manager.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.smartworks.server.engine.autoindex.exception.AutoIndexException;
import net.smartworks.server.engine.autoindex.manager.IIdxManager;
import net.smartworks.server.engine.autoindex.model.AutoIndexDef;
import net.smartworks.server.engine.autoindex.model.AutoIndexDefCond;
import net.smartworks.server.engine.autoindex.model.AutoIndexRule;
import net.smartworks.server.engine.autoindex.model.AutoIndexInst;
import net.smartworks.server.engine.autoindex.model.AutoIndexInstCond;
import net.smartworks.server.engine.common.manager.AbstractManager;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.pkg.model.PkgPackage;
import net.smartworks.server.engine.resource.model.IFormFieldDef;
import net.smartworks.server.engine.resource.util.XmlUtil;

import org.hibernate.Query;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IdxManagerImpl extends AbstractManager implements IIdxManager {

	@Override
	public AutoIndexDef getAutoIndexDef(String userId, String objId, String level) throws AutoIndexException {

		try {
			if (level == null)
				level = LEVEL_ALL;
			if (level.equals(LEVEL_ALL)) {
				AutoIndexDef obj = (AutoIndexDef)this.get(AutoIndexDef.class, objId);
				return obj;
			} else {
				AutoIndexDefCond cond = new AutoIndexDefCond();
				cond.setObjId(objId);
				return getAutoIndexDef(userId, cond, level);
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw new AutoIndexException(e);
		}
	}

	@Override
	public AutoIndexDef getAutoIndexDef(String userId, AutoIndexDefCond cond, String level) throws AutoIndexException {
		if (level == null)
			level = LEVEL_ALL;
		cond.setPageSize(2);
		AutoIndexDef[] objs = getAutoIndexDefs(userId, cond, level);
		if (CommonUtil.isEmpty(objs))
			return null;
		try {
			if (objs.length != 1)
				throw new AutoIndexException("More than 1 Object");
		} catch (AutoIndexException e) {
			logger.error(e, e);
			throw e;
		}
		return objs[0];
	}

	@Override
	public void setAutoIndexDef(String userId, AutoIndexDef obj, String level) throws AutoIndexException {
		try {
			fill(userId, obj);
			set(obj);
		} catch (AutoIndexException e) {
			throw e;
		} catch (Exception e) {
			throw new AutoIndexException(e);
		}
	}

	@Override
	public void removeAutoIndexDef(String userId, String objId) throws AutoIndexException {
		try {
			remove(AutoIndexDef.class, objId);
		} catch (Exception e) {
			logger.error(e, e);
			throw new AutoIndexException(e);
		}
	}

	@Override
	public void removeAutoIndexDef(String userId, AutoIndexDefCond cond) throws AutoIndexException {
		AutoIndexDef obj = getAutoIndexDef(userId, cond, null);
		if (obj == null)
			return;
		removeAutoIndexDef(userId, obj.getObjId());
	}
	private Query appendQuery(StringBuffer buf, AutoIndexDefCond cond) throws Exception {
		String objId = null;
		int version = -1;
		String formId = null;
		String fieldId = null;
		String creationUser = null;
		Date creationDate = null;
		String modificationUser = null;
		Date modificationDate = null;
		
		if (cond != null) {
			objId = cond.getObjId();
			formId = cond.getFormId();
			fieldId = cond.getFieldId();
			creationUser = cond.getCreationUser();
			creationDate = cond.getCreationDate();
			modificationUser = cond.getModificationUser();
			modificationDate = cond.getModificationDate();
		}
		buf.append(" from AutoIndexDef obj");
		buf.append(" where obj.objId is not null");
		Map filterMap = new HashMap();
		//TODO 시간 검색에 대한 확인 필요
		if (cond != null) {
			if (objId != null)
				buf.append(" and obj.objId = :objId");
			if (formId != null)
				buf.append(" and obj.formId = :formId");
			if (fieldId != null)
				buf.append(" and obj.fieldId = :fieldId");
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
			if (formId != null)
				query.setString("formId", formId);
			if (fieldId != null)
				query.setString("fieldId", fieldId);
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
	public long getAutoIndexDefSize(String userId, AutoIndexDefCond cond) throws AutoIndexException {
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
			throw new AutoIndexException(e);
		}
	}

	@Override
	public AutoIndexDef[] getAutoIndexDefs(String userId, AutoIndexDefCond cond, String level) throws AutoIndexException {
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
			AutoIndexDef[] objs = new AutoIndexDef[list.size()];
			list.toArray(objs);
			return objs;
		} catch (Exception e) {
			logger.error(e, e);
			throw new AutoIndexException(e);
		}
	}

	@Override
	public AutoIndexInst getAutoIndexInst(String userId, String objId, String level) throws AutoIndexException {
		try {
			if (level == null)
				level = LEVEL_ALL;
			if (level.equals(LEVEL_ALL)) {
				AutoIndexInst obj = (AutoIndexInst)this.get(AutoIndexInst.class, objId);
				return obj;
			} else {
				AutoIndexInstCond cond = new AutoIndexInstCond();
				cond.setObjId(objId);
				return getAutoIndexInst(userId, cond, level);
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw new AutoIndexException(e);
		}
	}

	@Override
	public AutoIndexInst getAutoIndexInst(String userId, AutoIndexInstCond cond, String level) throws AutoIndexException {
		if (level == null)
			level = LEVEL_ALL;
		cond.setPageSize(2);
		AutoIndexInst[] objs = getAutoIndexInsts(userId, cond, level);
		if (CommonUtil.isEmpty(objs))
			return null;
		try {
			if (objs.length != 1)
				throw new AutoIndexException("More than 1 Object");
		} catch (AutoIndexException e) {
			logger.error(e, e);
			throw e;
		}
		return objs[0];
	}

	@Override
	public void setAutoIndexInst(String userId, AutoIndexInst obj, String level) throws AutoIndexException {
		try {
			fill(userId, obj);
			set(obj);
		} catch (AutoIndexException e) {
			throw e;
		} catch (Exception e) {
			throw new AutoIndexException(e);
		}
	}

	@Override
	public void removeAutoIndexInst(String userId, String objId) throws AutoIndexException {
		try {
			remove(AutoIndexInst.class, objId);
		} catch (Exception e) {
			logger.error(e, e);
			throw new AutoIndexException(e);
		}
	}

	@Override
	public void removeAutoIndexInst(String userId, AutoIndexInstCond cond) throws AutoIndexException {
		AutoIndexInst obj = getAutoIndexInst(userId, cond, null);
		if (obj == null)
			return;
		removeAutoIndexDef(userId, obj.getObjId());
	}
	private Query appendQuery(StringBuffer buf, AutoIndexInstCond cond) throws Exception {
		String objId = null;
		String instanceId = null;
		String formId = null;
		String fieldId = null;
		String refType = null;
		String idType = null;
		String idValue = null;
		String seperator = null;
		int seq = -1;
		String creationUser = null;
		Date creationDate = null;
		String modificationUser = null;
		Date modificationDate = null;
		
		if (cond != null) {
			objId = cond.getObjId();
			instanceId = cond.getInstanceId();
			formId = cond.getFormId();
			fieldId = cond.getFieldId();
			refType = cond.getRefType();
			idType = cond.getIdType();
			idValue = cond.getIdValue();
			seq = cond.getSeq();
			creationUser = cond.getCreationUser();
			creationDate = cond.getCreationDate();
			modificationUser = cond.getModificationUser();
			modificationDate = cond.getModificationDate();
		}
		buf.append(" from AutoIndexInst obj");
		buf.append(" where obj.objId is not null");
		Map filterMap = new HashMap();
		//TODO 시간 검색에 대한 확인 필요
		if (cond != null) {
			if (objId != null)
				buf.append(" and obj.objId = :objId");
			if (instanceId != null)
				buf.append(" and obj.instanceId = :instanceId");
			if (formId != null)
				buf.append(" and obj.formId = :formId");
			if (fieldId != null)
				buf.append(" and obj.fieldId = :fieldId");
			if (refType != null)
				buf.append(" and obj.refType = :refType");
			if (idType != null)
				buf.append(" and obj.idType = :idType");
			if (idValue != null)
				buf.append(" and obj.idValue = :idValue");
			if (seq != -1)
				buf.append(" and obj.seq = :seq");
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
			if (instanceId != null)
				query.setString("instanceId", instanceId);
			if (formId != null)
				query.setString("formId", formId);
			if (fieldId != null)
				query.setString("fieldId", fieldId);
			if (refType != null)
				query.setString("refType", refType);
			if (idType != null)
				query.setString("idType", idType);
			if (idValue != null)
				query.setString("idValue", idValue);
			if (seq != -1)
				query.setInteger("seq", seq);
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
	public long getAutoIndexInstSize(String userId, AutoIndexInstCond cond) throws AutoIndexException {
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
			throw new AutoIndexException(e);
		}
	}

	@Override
	public AutoIndexInst[] getAutoIndexInsts(String userId, AutoIndexInstCond cond, String level) throws AutoIndexException {
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
			AutoIndexInst[] objs = new AutoIndexInst[list.size()];
			list.toArray(objs);
			return objs;
		} catch (Exception e) {
			logger.error(e, e);
			throw new AutoIndexException(e);
		}
	}

	@Override
	public void createAutoIndexDefByFormXml(String userId, String formXml) throws AutoIndexException {
		try {
			if (CommonUtil.isEmpty(formXml))
				return;
			
			Document doc = XmlUtil.parse(formXml, false, "UTF-8");
			Element root = doc.getDocumentElement();
			
			String formId = root.getAttribute("id");
			String version = root.getAttribute("version");
			String formName = root.getAttribute("name");
			String formTitle = root.getAttribute("title");
			String formSystemName = root.getAttribute("systemName");
			
			Node childrenNode = XmlUtil.getXpathNode(root, "./children");
			if (childrenNode == null)
				return;

			NodeList entityNodeList = XmlUtil.getXpathNodeList(childrenNode, "./formEntity");
			if (CommonUtil.isEmpty(entityNodeList))
				return;
			
			for(int i = 0 ; i < entityNodeList.getLength() ; i++) {
				Element entity = (Element)entityNodeList.item(i);
				String fieldId = entity.getAttribute("id");
				Element format = (Element)XmlUtil.getXpathNode(entity, "./format");

				AutoIndexDef autoIndexDef = new AutoIndexDef();
				
				if (format != null) {
					String formatType = format.getAttribute("type");
					if (!formatType.equalsIgnoreCase("autoIndex"))
						continue;
					
					Node rulesNode = XmlUtil.getXpathNode(format, "./autoIndexRules");
					if (rulesNode == null)
						continue;
					
					NodeList rulesNodeList = XmlUtil.getXpathNodeList(rulesNode, "./autoIndexRule");
					if (rulesNodeList == null || rulesNodeList.getLength() == 0)
						continue;

					autoIndexDef.setFormId(formId);
					autoIndexDef.setFieldId(fieldId);
					AutoIndexRule[] rules = new AutoIndexRule[rulesNodeList.getLength()]; 
					for(int j = 0 ; j < rulesNodeList.getLength() ; j++) {
						Element ruleEle = (Element)rulesNodeList.item(j);
						String ruleId = ruleEle.getAttribute("ruleId");
						
						AutoIndexRule rule = new AutoIndexRule();
						rule.setSeperator(ruleEle.getAttribute("seperator"));
						rule.setRuleId(ruleId);
						
						if (ruleId.equalsIgnoreCase("ruleId.code")) {
							rule.setCodeValue(ruleEle.getAttribute("codeValue"));
						} else if (ruleId.equalsIgnoreCase("ruleId.date")) {
							rule.setType(ruleEle.getAttribute("dateFormat"));
						} else if (ruleId.equalsIgnoreCase("ruleId.sequence")) {
							String increment = ruleEle.getAttribute("increment");
							rule.setIncrement(CommonUtil.isEmpty(increment) ? -1 : Integer.parseInt(increment));
							rule.setIncrementBy(ruleEle.getAttribute("incrementBy"));
							rule.setDigits(ruleEle.getAttribute("digits"));
						} else if (ruleId.equalsIgnoreCase("ruleId.list")) {
							
							Node itemsNode = XmlUtil.getXpathNode(ruleEle, "./listItems");
							if (itemsNode == null)
								continue;
							
							NodeList itemsNodeList = XmlUtil.getXpathNodeList(itemsNode, "./listItem");
							if (itemsNodeList == null || itemsNodeList.getLength() == 0)
								continue;
							
							StringBuffer itemsBuf = new StringBuffer();
							boolean isFirst = true;
							for(int k = 0 ; k < itemsNodeList.getLength() ; k++) {
								Element itemEle = (Element)itemsNodeList.item(k);
								if (!isFirst)
									itemsBuf.append("||");
								itemsBuf.append(itemEle.getTextContent());
								isFirst = false;
							}
							rule.setItems(itemsBuf.toString());
							
						}
						rules[j] = rule;
						
					}
					autoIndexDef.setRules(rules);
				} else {
					continue;
				}
				AutoIndexDefCond autoIndexDefCond = new AutoIndexDefCond();
				autoIndexDefCond.setFormId(formId);
				autoIndexDefCond.setFieldId(fieldId);
				SwManagerFactory.getInstance().getAutoIndexManager().removeAutoIndexDef(userId, autoIndexDefCond);
				SwManagerFactory.getInstance().getAutoIndexManager().setAutoIndexDef(userId, autoIndexDef, null);
			}
		} catch (Exception e) {
			throw new AutoIndexException(e);
		}
	}
}
