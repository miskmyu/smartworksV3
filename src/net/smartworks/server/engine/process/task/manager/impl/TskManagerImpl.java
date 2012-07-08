package net.smartworks.server.engine.process.task.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.smartworks.server.engine.common.manager.AbstractManager;
import net.smartworks.server.engine.common.model.ClassObject;
import net.smartworks.server.engine.common.model.Filter;
import net.smartworks.server.engine.common.model.MisObject;
import net.smartworks.server.engine.common.model.Property;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.common.util.DateUtil;
import net.smartworks.server.engine.process.task.exception.TskException;
import net.smartworks.server.engine.process.task.manager.ITskManager;
import net.smartworks.server.engine.process.task.model.TskTask;
import net.smartworks.server.engine.process.task.model.TskTaskCond;
import net.smartworks.server.engine.process.task.model.TskTaskDef;
import net.smartworks.server.engine.process.task.model.TskTaskDefCond;
import net.smartworks.server.engine.process.task.model.TskTaskExtend;
import net.smartworks.util.LocalDate;

import org.hibernate.Query;

public class TskManagerImpl extends AbstractManager implements ITskManager{
	public TskManagerImpl() {
		super();
		if (logger.isInfoEnabled())
			logger.info(this.getClass().getName() + " created");
	}
	
	public TskTask getTask(String user, String id, String level) throws TskException {
		try {
			if (level == null)
				level = LEVEL_ALL;
			if (level.equals(LEVEL_ALL)) {
				TskTask obj = (TskTask)this.get(TskTask.class, id);
				return obj;
			} else {
				TskTaskCond cond = new TskTaskCond();
				cond.setObjId(id);
				TskTask[] objs = this.getTasks(user, cond, level);
				if (objs == null || objs.length == 0)
					return null;
				return objs[0];
			}
		} catch (TskException e) {
			throw e;
		} catch (Exception e) {
			throw new TskException(e);
		}
	}
	
	public TskTask setTask(String user, TskTask obj, String level) throws TskException {
		if (level == null)
			level = LEVEL_ALL;
		try {
			if (level.equals(LEVEL_ALL)) {
				fill(user, obj);
				set(obj);
			} else {
				StringBuffer buf = new StringBuffer();
				buf.append("update TskTask set");
				buf.append(" name=:name, creationUser=:creationUser, creationDate=:creationDate");
				buf.append(", modificationUser=:modificationUser, modificationDate=:modificationDate");
				buf.append(", description=:description");
				buf.append(", status=:status, correlation=:correlation, type=:type, refType=:refType");
				buf.append(", processInstId=:processInstId, title=:title, priority=:priority");
				buf.append(", document=:document, assigner=:assigner, assignee=:assignee, performer=:performer");
				buf.append(", startDate=:startDate, assignmentDate=:assignmentDate, executionDate=:executionDate, dueDate=:dueDate");
				buf.append(", expectStartDate=:expectStartDate, expectEndDate=:expectEndDate");
				buf.append(", realStartDate=:realStartDate, realEndDate=:realEndDate");
				buf.append(", def=:def, form=:form");
				buf.append(", multiInstId=:multiInstId, multiInstOrdering=:multiInstOrdering");
				buf.append(", multiInstFlowCondition=:multiInstFlowCondition, loopCounterInteger=:loopCounterInteger");
				buf.append(", stepInteger=:stepInteger, instVariable=:instVariable, isStartActivity=:isStartActivity");
				buf.append(", fromRefType=:fromRefType, fromRefId=:fromRefId, approvalId=:approvalId, forwardId=:forwardId, isApprovalSourceTask=:isApprovalSourceTask,targetApprovalStatus=:targetApprovalStatus,  workSpaceId=:workSpaceId, workSpaceType=:workSpaceType");
				buf.append(", accessLevel=:accessLevel, accessValue=:accessValue where objId=:objId");
				Query query = this.getSession().createQuery(buf.toString());
				query.setString(MisObject.A_NAME, obj.getName());
				query.setString(MisObject.A_CREATIONUSER, obj.getCreationUser());
				query.setTimestamp(MisObject.A_CREATIONDATE, obj.getCreationDate());
				query.setString(MisObject.A_MODIFICATIONUSER, obj.getModificationUser());
				query.setTimestamp(MisObject.A_MODIFICATIONDATE, obj.getModificationDate());
				query.setString(MisObject.A_DESCRIPTION, obj.getDescription());
				query.setString(MisObject.A_STATUS, obj.getStatus());
				query.setString(TskTask.A_CORRELATION, obj.getCorrelation());
				query.setString(TskTask.A_TYPE, obj.getType());
				query.setString(TskTask.A_REFTYPE, obj.getRefType());
				query.setString(TskTask.A_PROCESSINSTID, obj.getProcessInstId());
				query.setString(TskTask.A_TITLE, obj.getTitle());
				query.setString(TskTask.A_PRIORITY, obj.getPriority());
				query.setString(TskTask.A_DOCUMENT, obj.getDocument());
				query.setString(TskTask.A_ASSIGNER, obj.getAssigner());
				query.setString(TskTask.A_ASSIGNEE, obj.getAssignee());
				query.setString("performer", obj.getPerformer());
				query.setTimestamp(TskTask.A_STARTDATE, obj.getStartDate());
				query.setTimestamp(TskTask.A_ASSIGNMENTDATE, obj.getAssignmentDate());
				query.setTimestamp(TskTask.A_EXECUTIONDATE, obj.getExecutionDate());
				query.setTimestamp(TskTask.A_DUEDATE, obj.getDueDate());
				query.setTimestamp(TskTask.A_EXPECTSTARTDATE, obj.getExpectStartDate());
				query.setTimestamp(TskTask.A_EXPECTENDDATE, obj.getExpectEndDate());
				query.setTimestamp(TskTask.A_REALSTARTDATE, obj.getRealStartDate());
				query.setTimestamp(TskTask.A_REALENDDATE, obj.getRealEndDate());
				query.setString(TskTask.A_DEF, obj.getDef());
				query.setString(TskTask.A_FORM, obj.getForm());
				query.setString(TskTask.A_MULTIINSTID, obj.getMultiInstId());
				query.setString(TskTask.A_MULTIINSTORDERING, obj.getMultiInstOrdering());
				query.setString(TskTask.A_MULTIINSTFLOWCONDITION, obj.getMultiInstFlowCondition());
				query.setInteger("loopCounterInteger", CommonUtil.toInt(obj.getLoopCounterInteger()));
				query.setInteger("stepInteger", CommonUtil.toInt(obj.getStepInteger()));
				query.setString("instVariable", obj.getInstVariable());
				query.setString("isStartActivity", obj.getIsStartActivity());
				query.setString("fromRefType", obj.getFromRefType());
				query.setString("fromRefId", obj.getFromRefId());
				query.setString("approvalId", obj.getApprovalId());
				query.setString("forwardId", obj.getForwardId());
				query.setString("isApprovalSourceTask", obj.getIsApprovalSourceTask());
				query.setString("targetApprovalStatus", obj.getTargetApprovalStatus());
				query.setString(TskTask.A_WORKSPACEID, obj.getWorkSpaceId());
				query.setString(TskTask.A_WORKSPACETYPE, obj.getWorkSpaceType());
				query.setString(TskTask.A_ACCESSLEVEL, obj.getAccessLevel());
				query.setString(TskTask.A_ACCESSVALUE, obj.getAccessValue());
				query.setString(ClassObject.A_OBJID, obj.getObjId());
			}
			return obj;
		} catch (TskException e) {
			throw e;
		} catch (Exception e) {
			throw new TskException(e);
		}
	}
	
	public void removeTask(String user, String id) throws TskException {
		try {
			TskTask obj = this.getTask(user, id, LEVEL_ALL);
			this.getHibernateTemplate().delete(obj);
			this.getHibernateTemplate().flush();
		} catch (TskException e) {
			throw e;
		} catch (Exception e) {
			throw new TskException(e);
		}
	}
	private Query appendQuery(StringBuffer buf, TskTaskCond cond) throws Exception{
		String objId = null;
		String creationUser = null;
		Date creationDateFrom = null;
		Date creationDateTo = null;
		String modificationUser = null;
		Date modificationDateFrom = null;
		Date modificationDateTo = null;
		Date assignmentDateFrom = null;
		Date assignmentDateTo = null;
		Date executionDateFrom = null;
		Date executionDateTo = null;
		Date dueDateFrom = null;
		Date dueDateTo = null;
		String assignee = null;
		String assigner = null;
		String status = null;
		String correlation = null;
		String type = null;
		String refType = null;
		String processInstId = null;
		String name = null;
		String title = null;
		String titleLike = null;
		String description = null;
		String descriptionLike = null;
		String priority = null;
		String def = null;
		String form = null;
		String multiInstId = null;
		String multiInstOrdering = null;
		String multiInstFlowCondition = null;
		String isStartActivity = null;
		String fromRefType = null;
		String fromRefId = null;
		String approvalId = null;
		String forwardId = null;
		String workSpaceId = null;
		String workSpaceType = null;
		String accessLevel = null;
		String accessValue = null;
		int loopCounter = -1;
		int step = -1;
		Property[] extProps = null;
		String[] statusIns = null;
		String[] statusNotIns = null;
		String[] typeIns = null;
		String[] typeNotIns = null;
		String[] priorityIns = null;
		String[] priorityNotIns = null;
		String[] formIns = null;
		String searchKey = null;
		Filter[] filters = null;
		String logicalOperator = null;
		if (cond != null) {
			objId = cond.getObjId();
			creationUser = cond.getCreationUser();
			creationDateFrom = cond.getCreationDateFrom();
			creationDateTo = cond.getCreationDateTo();
			modificationUser = cond.getModificationUser();
			modificationDateFrom = cond.getModificationDateFrom();
			modificationDateTo = cond.getModificationDateTo();
			assignmentDateFrom = cond.getAssignmentDateFrom();
			assignmentDateTo = cond.getAssignmentDateTo();
			executionDateFrom = cond.getExecutionDateFrom();
			executionDateTo = cond.getExecutionDateTo();
			assignee = cond.getAssignee();
			assigner = cond.getAssigner();
			correlation = cond.getCorrelation();
			status = cond.getStatus();
			type = cond.getType();
			refType = cond.getRefType();
			processInstId = cond.getProcessInstId();
			name = cond.getName();
			title = cond.getTitle();
			titleLike = cond.getTitleLike();
			description = cond.getDescription();
			descriptionLike = cond.getDescriptionLike();
			priority = cond.getPriority();
			def = cond.getDef();
			form = cond.getForm();
			extProps = cond.getExtendedProperties();
			statusIns = cond.getStatusIns();
			statusNotIns = cond.getStatusNotIns();
			typeIns = cond.getTypeIns();
			typeNotIns = cond.getTypeNotIns();
			priorityIns = cond.getPriorityIns();
			priorityNotIns = cond.getPriorityNotIns();
			formIns = cond.getFormIns();
			searchKey = cond.getSearchKey();
			multiInstId = cond.getMultiInstId();
			multiInstOrdering = cond.getMultiInstOrdering();
			multiInstFlowCondition = cond.getMultiInstFlowCondition();
			isStartActivity = cond.getIsStartActivity();
			fromRefType = cond.getFromRefType();
			fromRefId = cond.getFromRefId();
			approvalId = cond.getApprovalId();
			forwardId = cond.getForwardId();
			workSpaceId = cond.getWorkSpaceId();
			workSpaceType = cond.getWorkSpaceType();
			accessLevel = cond.getAccessLevel();
			accessValue = cond.getAccessValue();
			loopCounter = cond.getLoopCounter();
			step = cond.getStep();
			filters = cond.getFilter();
			logicalOperator = cond.getOperator();
		}

		buf.append(" from TskTask obj");
		if (extProps != null && extProps.length != 0) {
			for (int i=0; i<extProps.length; i++) {
				buf.append(" left join obj.extendedProperties as extProp").append(i);
			}
		}
		buf.append(" where obj.objId is not null");
		Map filterMap = new HashMap();
		if (cond != null) {
			if (objId != null)
				buf.append(" and obj.objId = :objId");
			if (creationUser != null)
				buf.append(" and obj.creationUser = :creationUser");
			if (creationDateFrom != null)
				buf.append(" and obj.creationDate >= :creationDateFrom");
			if (creationDateTo != null)
				buf.append(" and obj.creationDate <= :creationDateTo");
			if (modificationUser != null)
				buf.append(" and obj.modificationUser = :modificationUser");
			if (modificationDateFrom != null)
				buf.append(" and obj.modificationDate >= :modificationDateFrom");
			if (modificationDateTo != null)
				buf.append(" and obj.modificationDate <= :modificationDateTo");
			if (assignmentDateFrom != null)
				buf.append(" and obj.assignmentDate >= :assignmentDateFrom");
			if (assignmentDateTo != null)
				buf.append(" and obj.assignmentDate <= :assignmentDateTo");
			if (executionDateFrom != null)
				buf.append(" and obj.executionDate >= :executionDateFrom");
			if (executionDateTo != null)
				buf.append(" and obj.creationDate <= :executionDateTo");
			if (dueDateFrom != null)
				buf.append(" and obj.dueDate >= :dueDateFrom");
			if (dueDateTo != null)
				buf.append(" and obj.dueDate <= :dueDateTo");
			if (assignee != null)
				buf.append(" and obj.assignee = :assignee");
			if (assigner != null)
				buf.append(" and obj.assigner = :assigner");
			if (status != null)
				buf.append(" and obj.status = :status");
			if (correlation != null)
				buf.append(" and obj.correlation = :correlation");
			if (type != null)
				buf.append(" and obj.type = :type");
			if (refType != null)
				buf.append(" and obj.refType = :refType");
			if (processInstId != null)
				buf.append(" and obj.processInstId = :processInstId");
			if (name != null)
				buf.append(" and obj.name = :name");
			if (title != null)
				buf.append(" and obj.title = :title");
			if (titleLike != null)
				buf.append(" and obj.title like :titleLike");
			if (description != null)
				buf.append(" and obj.description = :description");
			if (descriptionLike != null)
				buf.append(" and obj.description like :descriptionLike");
			if (priority != null)
				buf.append(" and obj.priority = :priority");
			if (def != null)
				buf.append(" and obj.def = :def");
			if (form != null)
				buf.append(" and obj.form = :form");
			if (multiInstId != null)
				buf.append(" and obj.multiInstId = :multiInstId");
			if (multiInstOrdering != null)
				buf.append(" and obj.multiInstOrdering = :multiInstOrdering");
			if (multiInstFlowCondition != null)
				buf.append(" and obj.multiInstFlowCondition = :multiInstFlowCondition");
			if (isStartActivity != null)
				buf.append(" and obj.isStartActivity = :isStartActivity");
			if (fromRefType != null)
				buf.append(" and obj.fromRefType = :fromRefType");
			if (fromRefId != null)
				buf.append(" and obj.fromRefId = :fromRefId");
			if (approvalId != null)
				buf.append(" and obj.approvalId = :approvalId");
			if (forwardId != null)
				buf.append(" and obj.forwardId = :forwardId");
			if (workSpaceId != null)
				buf.append(" and obj.workSpaceId = :workSpaceId");
			if (workSpaceType != null)
				buf.append(" and obj.workSpaceType = :workSpaceType");
			if (accessLevel != null)
				buf.append(" and obj.accessLevel = :accessLevel");
			if (accessValue != null)
				buf.append(" and obj.accessValue = :accessValue");
			if (loopCounter > 0)
				buf.append(" and obj.loopCounter = :loopCounter");
			if (step > 0)
				buf.append(" and obj.step = :step");
			if (extProps != null && extProps.length != 0) {
				for (int i=0; i<extProps.length; i++) {
					Property extProp = extProps[i];
					String extName = extProp.getName();
					String extValue = extProp.getValue();
					if (extName != null)
						buf.append(" and extProp").append(i).append(".name = :extName").append(i);
					if (extValue != null)
						buf.append(" and extProp").append(i).append(".value = :extValue").append(i);
				}
			}
			if (statusIns != null && statusIns.length != 0) {
				buf.append(" and obj.status in (");
				for (int i=0; i<statusIns.length; i++) {
					if (i != 0)
						buf.append(", ");
					buf.append(":statusIn").append(i);
				}
				buf.append(")");
			}
			if (statusNotIns != null && statusNotIns.length != 0) {
				buf.append(" and obj.status not in (");
				for (int i=0; i<statusNotIns.length; i++) {
					if (i != 0)
						buf.append(", ");
					buf.append(":statusNotIn").append(i);
				}
				buf.append(")");
			}
			if (typeIns != null && typeIns.length != 0) {
				buf.append(" and obj.type in (");
				for (int i=0; i<typeIns.length; i++) {
					if (i != 0)
						buf.append(", ");
					buf.append(":typeIn").append(i);
				}
				buf.append(")");
			}
			if (typeNotIns != null && typeNotIns.length != 0) {
				buf.append(" and obj.type not in (");
				for (int i=0; i<typeNotIns.length; i++) {
					if (i != 0)
						buf.append(", ");
					buf.append(":typeNotIn").append(i);
				}
				buf.append(")");
			}
			if (priorityIns != null && priorityIns.length != 0) {
				buf.append(" and obj.priority in (");
				for (int i=0; i<priorityIns.length; i++) {
					if (i != 0)
						buf.append(", ");
					buf.append(":priorityIn").append(i);
				}
				buf.append(")");
			}
			if (priorityNotIns != null && priorityNotIns.length != 0) {
				buf.append(" and obj.priority not in (");
				for (int i=0; i<priorityNotIns.length; i++) {
					if (i != 0)
						buf.append(", ");
					buf.append(":priorityNotIn").append(i);
				}
				buf.append(")");
			}
			if (formIns != null && formIns.length != 0) {
				buf.append(" and obj.form in (");
				for (int i=0; i<formIns.length; i++) {
					if (i != 0)
						buf.append(", ");
					buf.append(":formIn").append(i);
				}
				buf.append(")");
			}
			if (searchKey != null)
				buf.append(" and (obj.name like :searchKey or obj.title like :searchKey or obj.description like :searchKey)");
			if (filters != null) {
				if (!CommonUtil.isEmpty(filters)) {
					if (CommonUtil.isEmpty(logicalOperator))
						logicalOperator = "and";
					String operator;
					String left;
					String right;
					String rightType;
					int i = 0;
					
					for (int j = 0; j < filters.length; j++) {
						Filter f = filters[j];
						operator = f.getOperator();
						left = f.getLeftOperandValue();
						right = f.getRightOperandValue();
						rightType = f.getRightOperandType();
						if (left == null)
							throw new Exception("left operand of filter condition is null.");
						if (operator == null) {
							operator = "=";
						} else {
							operator = operator.trim();
						}
						//left = CommonUtil.toDefault(fieldColumnMap.get(left), left);
						buf.append(CommonUtil.SPACE).append(logicalOperator);
						
						buf.append(CommonUtil.SPACE).append(left);
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
							}
							buf.append(CommonUtil.SPACE).append(operator);
							buf.append(CommonUtil.SPACE).append(CommonUtil.COLON).append(right);
						}
					}
				}
			}
		}
		this.appendOrderQuery(buf, "obj", cond);
		
		Query query = this.createQuery(buf.toString(), cond);
		if (cond != null) {
			if (objId != null)
				query.setString("objId", objId);
			if (creationUser != null)
				query.setString("creationUser", creationUser);
			if (creationDateFrom != null)
				query.setTimestamp("creationDateFrom", creationDateFrom);
			if (creationDateTo != null)
				query.setTimestamp("creationDateTo", creationDateTo);
			if (modificationUser != null)
				query.setString("modificationUser", modificationUser);
			if (modificationDateFrom != null)
				query.setTimestamp("modificationDateFrom", modificationDateFrom);
			if (modificationDateTo != null)
				query.setTimestamp("modificationDateTo", modificationDateTo);
			if (assignmentDateFrom != null)
				query.setTimestamp("assignmentDateFrom", assignmentDateFrom);
			if (assignmentDateTo != null)
				query.setTimestamp("assignmentDateTo", assignmentDateTo);
			if (executionDateFrom != null)
				query.setTimestamp("executionDateFrom", executionDateFrom);
			if (executionDateTo != null)
				query.setTimestamp("executionDateTo", executionDateTo);
			if (dueDateFrom != null)
				query.setTimestamp("dueDateFrom", dueDateFrom);
			if (dueDateTo != null)
				query.setTimestamp("dueDateTo", dueDateTo);
			if (assignee != null)
				query.setString("assignee", assignee);
			if (assigner != null)
				query.setString("assigner", assigner);
			if (status != null)
				query.setString("status", status);
			if (correlation != null)
				query.setString("correlation", correlation);
			if (type != null)
				query.setString("type", type);
			if (refType != null)
				query.setString("refType", refType);
			if (processInstId != null)
				query.setString("processInstId", processInstId);
			if (name != null)
				query.setString("name", name);
			if (title != null)
				query.setString("title", title);
			if (titleLike != null)
				query.setString("titleLike", CommonUtil.toLikeString(titleLike));
			if (description != null)
				query.setString("description", description);
			if (descriptionLike != null)
				query.setString("descriptionLike", CommonUtil.toLikeString(descriptionLike));
			if (priority != null)
				query.setString("priority", priority);
			if (def != null)
				query.setString("def", def);
			if (form != null)
				query.setString("form", form);
			if (multiInstId != null)
				query.setString("multiInstId", multiInstId);
			if (multiInstOrdering != null)
				query.setString("multiInstOrdering", multiInstOrdering);
			if (multiInstFlowCondition != null)
				query.setString("multiInstFlowCondition", multiInstFlowCondition);
			if (isStartActivity != null)
				query.setString("isStartActivity", isStartActivity);
			if (fromRefType != null)
				query.setString("fromRefType", fromRefType);
			if (fromRefId != null)
				query.setString("fromRefId", fromRefId);
			if (approvalId != null)
				query.setString("approvalId", approvalId);
			if (forwardId != null)
				query.setString("forwardId", forwardId);
			if (workSpaceId != null)
				query.setString("workSpaceId", workSpaceId);
			if (workSpaceType != null)
				query.setString("workSpaceType", workSpaceType);
			if (accessLevel != null)
				query.setString("accessLevel", accessLevel);
			if (accessValue != null)
				query.setString("accessValue", accessValue);
			if (loopCounter > 0)
				query.setInteger("loopCounter", loopCounter);
			if (step > 0)
				query.setInteger("step", step);
			if (extProps != null && extProps.length != 0) {
				for (int i=0; i<extProps.length; i++) {
					Property extProp = extProps[i];
					String extName = extProp.getName();
					String extValue = extProp.getValue();
					if (extName != null)
						query.setString("extName"+i, extName);
					if (extValue != null)
						query.setString("extValue"+i, extValue);
				}
			}
			if (statusIns != null && statusIns.length != 0) {
				for (int i=0; i<statusIns.length; i++) {
					query.setString("statusIn"+i, statusIns[i]);
				}
			}
			if (statusNotIns != null && statusNotIns.length != 0) {
				for (int i=0; i<statusNotIns.length; i++) {
					query.setString("statusNotIn"+i, statusNotIns[i]);
				}
			}
			if (typeIns != null && typeIns.length != 0) {
				for (int i=0; i<typeIns.length; i++) {
					query.setString("typeIn"+i, typeIns[i]);
				}
			}
			if (typeNotIns != null && typeNotIns.length != 0) {
				for (int i=0; i<typeNotIns.length; i++) {
					query.setString("typeNotIn"+i, typeNotIns[i]);
				}
			}
			if (priorityIns != null && priorityIns.length != 0) {
				for (int i=0; i<priorityIns.length; i++) {
					query.setString("priorityIn"+i, priorityIns[i]);
				}
			}
			if (priorityNotIns != null && priorityNotIns.length != 0) {
				for (int i=0; i<priorityNotIns.length; i++) {
					query.setString("priorityNotIn"+i, priorityNotIns[i]);
				}
			}
			if (formIns != null && formIns.length != 0) {
				for (int i=0; i<formIns.length; i++) {
					query.setString("formIn"+i, formIns[i]);
				}
			}
			if (searchKey != null)
				query.setString("searchKey", CommonUtil.toLikeString(searchKey));
			if (filters != null) {
				if (!CommonUtil.isEmpty(filterMap)) {
					Filter f;
					String operType;
					String operValue;
					String operator;
					
					Iterator keyItr = filterMap.keySet().iterator();
					String param = null;
					while (keyItr.hasNext()) {
						param = (String)keyItr.next();
						f = (Filter)filterMap.get(param);
						operType = f.getRightOperandType();
						operator = f.getOperator();
						if (operator.equalsIgnoreCase("like")) {
							operValue = CommonUtil.toLikeString(f.getRightOperandValue());
						} else {
							operValue = f.getRightOperandValue();
						}	
						if (operType == null || operType.equalsIgnoreCase(Filter.OPERANDTYPE_STRING)) {
							query.setString(param, operValue);
						} else if (operType.equalsIgnoreCase(Filter.OPERANDTYPE_INT)) {
							query.setInteger(param, CommonUtil.toInt(operValue));
						} else if (operType.equalsIgnoreCase(Filter.OPERANDTYPE_FLOAT)) {
							query.setFloat(param, CommonUtil.toFloat(operValue));
						} else if (operType.equalsIgnoreCase(Filter.OPERANDTYPE_DATE)) {
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
			}
		}
		return query;
	}
	
	public long getTaskSize(String user, TskTaskCond cond) throws TskException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			buf.append(" count(obj)");
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			long count =((Long)list.get(0)).longValue();
			return count;
		} catch (TskException e) {
			throw e;
		} catch (Exception e) {
			throw new TskException(e);
		}
	}
	
	public TskTask[] getTasks(String user, TskTaskCond cond, String level) throws TskException {
		try {
			if (level == null)
				level = LEVEL_LITE;
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			if (level.equals(LEVEL_ALL)) {
				buf.append(" obj");
			} else {
				buf.append(" obj.objId, obj.name, obj.creationUser, obj.creationDate, obj.modificationUser");
				buf.append(", obj.modificationDate, obj.status, obj.correlation, obj.type, obj.refType, obj.processInstId, obj.title");
				buf.append(", obj.description, obj.priority, obj.document, obj.assigner, obj.assignee, obj.performer");
				buf.append(", obj.startDate, obj.assignmentDate, obj.executionDate, obj.dueDate, obj.def, obj.form");
				buf.append(", obj.expectStartDate, obj.expectEndDate, obj.realStartDate, obj.realEndDate");
				buf.append(", obj.multiInstId, obj.multiInstOrdering, obj.multiInstFlowCondition, obj.isStartActivity, obj.fromRefType, obj.fromRefId, obj.approvalId, obj.forwardId, obj.isApprovalSourceTask, obj.targetApprovalStatus, obj.loopCounterInteger, obj.stepInteger");
				buf.append(", obj.instVariable, obj.workSpaceId, obj.workSpaceType, obj.accessLevel, obj.accessValue ");
			}
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			if (!level.equals(LEVEL_ALL)) {
				List objList = new ArrayList();
				for (Iterator itr = list.iterator(); itr.hasNext();) {
					Object[] fields = (Object[]) itr.next();
					TskTask obj = new TskTask();
					int j = 0;
					obj.setObjId((String)fields[j++]);
					obj.setName((String)fields[j++]);
					obj.setCreationUser((String)fields[j++]);
					obj.setCreationDate(((Timestamp)fields[j++]));
					obj.setModificationUser(((String)fields[j++]));
					obj.setModificationDate(((Timestamp)fields[j++]));
					obj.setStatus(((String)fields[j++]));
					obj.setCorrelation(((String)fields[j++]));
					obj.setType(((String)fields[j++]));
					obj.setRefType(((String)fields[j++]));
					obj.setProcessInstId(((String)fields[j++]));
					obj.setTitle(((String)fields[j++]));
					obj.setDescription(((String)fields[j++]));
					obj.setPriority(((String)fields[j++]));
					obj.setDocument((String)fields[j++]);
					obj.setAssigner((String)fields[j++]);
					obj.setAssignee(((String)fields[j++]));
					obj.setPerformer(((String)fields[j++]));
					obj.setStartDate(((Timestamp)fields[j++]));
					obj.setAssignmentDate(((Timestamp)fields[j++]));
					obj.setExecutionDate(((Timestamp)fields[j++]));
					obj.setDueDate(((Timestamp)fields[j++]));
					obj.setDef(((String)fields[j++]));
					obj.setForm(((String)fields[j++]));
					obj.setExpectStartDate(((Timestamp)fields[j++]));
					obj.setExpectEndDate(((Timestamp)fields[j++]));
					obj.setRealStartDate(((Timestamp)fields[j++]));
					obj.setRealEndDate(((Timestamp)fields[j++]));
					obj.setMultiInstId(((String)fields[j++]));
					obj.setMultiInstOrdering(((String)fields[j++]));
					obj.setMultiInstFlowCondition(((String)fields[j++]));
					obj.setIsStartActivity(((String)fields[j++]));
					obj.setFromRefType(((String)fields[j++]));
					obj.setFromRefId(((String)fields[j++]));
					obj.setApprovalId(((String)fields[j++]));
					obj.setForwardId(((String)fields[j++]));
					obj.setIsApprovalSourceTask(((String)fields[j++]));
					obj.setTargetApprovalStatus(((String)fields[j++]));
					obj.setLoopCounterInteger(((Integer)fields[j++]));
					obj.setStepInteger(((Integer)fields[j++]));
					obj.setInstVariable(((String)fields[j++]));
					obj.setWorkSpaceId(((String)fields[j++]));
					obj.setWorkSpaceType(((String)fields[j++]));
					obj.setAccessLevel(((String)fields[j++]));
					obj.setAccessValue(((String)fields[j++]));
					objList.add(obj);
				}
				list = objList;
			}
			TskTask[] objs = new TskTask[list.size()];
			list.toArray(objs);
			return objs;
		} catch (TskException e) {
			throw e;
		} catch (Exception e) {
			throw new TskException(e);
		}
	}
	
	public TskTask startTask(String user, String id) throws TskException {
		TskTask obj = this.getTask(user, id, LEVEL_LITE);
		if (obj.getStartDate() != null)
			return obj;
		obj.setStartDate(new LocalDate());//date to localdate - 
		setTask(user, obj, LEVEL_LITE);
		return obj;
	}

	public TskTask executeTask(String user, TskTask obj, String action) throws TskException {
		//date to localdate - Date date = new Date();
		LocalDate date = new LocalDate();
		if (obj.getStartDate() == null)
			obj.setStartDate(new Date(date.getTime() - 600000));
		obj.setPerformer(user);
		obj.setExecutionDate(date);
		this.setTask(user, obj, LEVEL_ALL);
		return obj;
	}
	private Query appendExtendQuery (StringBuffer queryBuffer, TskTaskCond cond) throws TskException {
		
		String tskAssignee = cond.getAssignee();
		Date creationDateFrom = cond.getCreationDateFrom();
		Date creationDateTo = cond.getCreationDateTo();
		
		int pageNo = cond.getPageNo();
		int pageSize = cond.getPageSize();
		
		queryBuffer.append(" from ( ");
		queryBuffer.append(" 	select tskobjid, tsktitle, tsktype, tskreftype, tskname, tskassignee, tskcreateDate, tskstatus , tskprcinstid, isStartActivity, tskWorkSpaceId, tskWorkSpaceType, tskAccessLevel, tskAccessValue ");
		queryBuffer.append(" 	from tsktask ");
		queryBuffer.append(" 	where (tsktask.tskstatus='11'  ");
		queryBuffer.append(" 	and tsktask.tskassignee= :tskAssignee) ");
		queryBuffer.append(" 	or  ");
		queryBuffer.append(" 	( ");
		queryBuffer.append(" 		tskTask.tskassignee= :tskAssignee ");
		queryBuffer.append(" 		and tskTask.isStartActivity = 'true' ");
		queryBuffer.append(" 	)	 ");
		queryBuffer.append(" ) myTask ");
		queryBuffer.append(" left outer join ");
		queryBuffer.append(" (select  ctgInfo.parentCtgId ");
		queryBuffer.append(" 			, ctgInfo.parentCtg ");
		queryBuffer.append(" 			, ctgInfo.subCtgId ");
		queryBuffer.append(" 			, ctgInfo.subCtg  ");
		queryBuffer.append(" 			, info.* ");
		queryBuffer.append(" from ( ");
		queryBuffer.append(" 		select ");
		queryBuffer.append(" 			 prcInst.prcObjId ");
		queryBuffer.append(" 			, prcInst.prcName ");
		queryBuffer.append(" 			, prcInst.prcCreateUser ");
		queryBuffer.append(" 			, prcInst.prcCreateDate ");
		queryBuffer.append(" 			, prcInst.prcModifyUser ");
		queryBuffer.append(" 			, prcInst.prcModifyDate ");
		queryBuffer.append(" 			, prcInst.prcStatus ");
		queryBuffer.append(" 			, prcInst.prcTitle ");
		queryBuffer.append(" 			, prcInst.prcDid ");
		queryBuffer.append(" 			, prcInst.prcPrcId ");
		//queryBuffer.append(" 			, prcInstInfo.lastTask_tskprcinstid ");
		queryBuffer.append(" 			, prcInstInfo.lastTask_tskobjid ");
		queryBuffer.append(" 			, prcInstInfo.lastTask_tskname ");
		queryBuffer.append(" 			, prcInstInfo.lastTask_tskcreateuser ");
		queryBuffer.append(" 			, prcInstInfo.lastTask_tskcreateDate ");
		queryBuffer.append(" 			, prcInstInfo.lastTask_tskstatus ");
		queryBuffer.append(" 			, prcInstInfo.lastTask_tsktype ");
		queryBuffer.append(" 			, prcInstInfo.lastTask_tsktitle ");
		queryBuffer.append(" 			, prcInstInfo.lastTask_tskassignee ");
		queryBuffer.append(" 			, prcInstInfo.lastTask_tskexecuteDate ");
		queryBuffer.append(" 			, prcInstInfo.lastTask_tskduedate ");
		queryBuffer.append(" 			, prcInstInfo.lastTask_tskform ");
		queryBuffer.append(" 			, (select count(*) from tsktask where tskstatus='11' and tsktype='common' and tskprcInstId = prcInst.prcObjid) as lastTaskCount ");
		queryBuffer.append(" 		from  ");
		queryBuffer.append(" 			prcprcinst prcInst,  ");
		queryBuffer.append(" 			( ");
		queryBuffer.append(" 				select a.tskprcinstid as lastTask_tskprcinstid ");
		queryBuffer.append(" 						, task.tskobjid as lastTask_tskobjid ");
		queryBuffer.append(" 						, task.tskname as lastTask_tskname ");
		queryBuffer.append(" 						, task.tskcreateuser as lastTask_tskcreateuser ");
		queryBuffer.append(" 						, task.tskcreateDate as lastTask_tskcreateDate ");
		queryBuffer.append(" 						, task.tskstatus as lastTask_tskstatus ");
		queryBuffer.append(" 						, task.tsktype as lastTask_tsktype ");
		queryBuffer.append(" 						, task.tsktitle as lastTask_tsktitle ");
		queryBuffer.append(" 						, task.tskassignee as lastTask_tskassignee ");
		queryBuffer.append(" 						, task.tskexecuteDate as lastTask_tskexecuteDate ");
		queryBuffer.append(" 						, task.tskduedate as lastTask_tskduedate ");
		queryBuffer.append(" 						, task.tskform as lastTask_tskform ");
		queryBuffer.append(" 				from ( ");
		queryBuffer.append(" 						select tskprcinstId , max(tskCreatedate) as createDate  ");
		queryBuffer.append(" 						from tsktask  ");
		queryBuffer.append(" 						where tsktype='common'  ");
		queryBuffer.append(" 						group by tskprcinstid ");
		queryBuffer.append(" 					  ) a,	 ");
		queryBuffer.append(" 					  TskTask task		 ");
		queryBuffer.append(" 				where  ");
		queryBuffer.append(" 					a.createDate = task.tskcreatedate ");
		queryBuffer.append(" 			) prcInstInfo	 ");
		queryBuffer.append(" 		where ");
		queryBuffer.append(" 			prcInst.prcobjid=prcInstInfo.lastTask_tskprcinstid ");
		queryBuffer.append("  ");
		queryBuffer.append(" 	) info ");
		queryBuffer.append(" 	left outer join ");
		queryBuffer.append(" 	( ");
		queryBuffer.append(" 		select prcinst.prcobjid as prcinstid ");
		queryBuffer.append(" 				, parentCtg.id as parentCtgId ");
		queryBuffer.append(" 				, parentCtg.name as parentCtg ");
		queryBuffer.append(" 				, ctg.id as subCtgId ");
		queryBuffer.append(" 				, ctg.name as subCtg ");
		queryBuffer.append(" 		from prcprcinst prcinst, swpackage pkg , swcategory ctg, swcategory parentCtg ");
		queryBuffer.append(" 		where prcinst.prcdid = pkg.packageid ");
		queryBuffer.append(" 			and pkg.categoryid = ctg.id ");
		queryBuffer.append(" 			and ctg.parentid = parentCtg.id ");
		queryBuffer.append(" 	) ctgInfo ");
		queryBuffer.append(" 	on info.prcobjid = ctginfo.prcinstid ");
		queryBuffer.append(" ) prcInstInfo ");
		queryBuffer.append(" on mytask.tskprcinstid = prcInstInfo.prcObjId ");
		
		Query query = this.getSession().createSQLQuery(queryBuffer.toString());
		
		if (pageSize > 0|| pageNo >= 0) {
			query.setFirstResult(pageNo * pageSize);
			query.setMaxResults(pageSize);
		}
		if (!CommonUtil.isEmpty(tskAssignee))
			query.setString("tskAssignee", tskAssignee);
		
		return query;	
		
	}
	public long getTskTaskExtendsSize(String userId, TskTaskCond cond) throws TskException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			buf.append(" count(*) ");
			Query query = this.appendExtendQuery(buf, cond);
			List list = query.list();
			
			long count =((Integer)list.get(0)).longValue();
			return count;
		} catch (TskException e) {
			throw e;
		} catch (Exception e) {
			throw new TskException(e);
		}
	}
	public TskTaskExtend[] getTaskExtend(String userId, TskTaskCond cond) throws Exception {
		
		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append(" select myTask.*, prcInstInfo.*  ");
		
		Query query = this.appendExtendQuery(queryBuffer, cond);
	
		List list = query.list();
		if (list == null || list.isEmpty())
			return null;
		List objList = new ArrayList();
		for (Iterator itr = list.iterator(); itr.hasNext();) {
			Object[] fields = (Object[]) itr.next();
			TskTaskExtend obj = new TskTaskExtend();
			int j = 0;
	
			obj.setTskObjId((String)fields[j++]);
			obj.setTskTitle((String)fields[j++]);
			obj.setTskType((String)fields[j++]);
			obj.setTskRefType((String)fields[j++]);
			obj.setTskName((String)fields[j++]);
			obj.setTskAssignee((String)fields[j++]);
			obj.setTskCreateDate((Timestamp)fields[j++]);
			obj.setTskStatus((String)fields[j++]);
			obj.setTskprcInstId((String)fields[j++]);
			obj.setIsStartActivity((String)fields[j++]);
			obj.setTskWorkSpaceId((String)fields[j++]);
			obj.setTskWorkSpaceType((String)fields[j++]);
			obj.setTskAccessLevel((String)fields[j++]);
			obj.setTskAccessValue((String)fields[j++]);
			obj.setParentCtgId((String)fields[j++]);
			obj.setParentCtg((String)fields[j++]);
			obj.setSubCtgId((String)fields[j++]);
			obj.setSubCtg((String)fields[j++]);
			obj.setPrcObjId((String)fields[j++]);
			obj.setPrcName((String)fields[j++]);
			obj.setPrcCreateUser((String)fields[j++]);
			obj.setPrcCreateDate((Timestamp)fields[j++]);
			obj.setPrcModifyUser((String)fields[j++]);
			obj.setPrcModifyDate((Timestamp)fields[j++]);
			obj.setPrcStatus((String)fields[j++]);
			obj.setPrcTitle((String)fields[j++]);
			obj.setPrcDid((String)fields[j++]);
			obj.setPrcPrcId((String)fields[j++]);
			obj.setLastTask_tskObjId((String)fields[j++]);
			obj.setLastTask_tskName((String)fields[j++]);
			obj.setLastTask_tskCreateUser((String)fields[j++]);
			obj.setLastTask_tskCreateDate((Timestamp)fields[j++]);
			obj.setLastTask_tskStatus((String)fields[j++]);
			obj.setLastTask_tskType((String)fields[j++]);
			obj.setLastTask_tskTitle((String)fields[j++]);
			obj.setLastTask_tskAssignee((String)fields[j++]);
			obj.setLastTask_tskExecuteDate((Timestamp)fields[j++]);
			obj.setLastTask_tskDueDate((Timestamp)fields[j++]);
			obj.setLastTask_tskForm((String)fields[j++]);
			int lastTaskCount = (Integer)fields[j] ==  null ? -1 : (Integer)fields[j];
			obj.setLastTask_tskCount(lastTaskCount == 0 ? 1 : lastTaskCount);
			objList.add(obj);
		}
		list = objList;
		TskTaskExtend[] objs = new TskTaskExtend[list.size()];
		list.toArray(objs);
		return objs;
		
	}

	public TskTaskDef getTaskDef(String user, String id, String level) throws TskException {
		try {
			if (CommonUtil.isEmpty(id))
				return null;
			if (level == null)
				level = LEVEL_ALL;
			if (level.equals(LEVEL_ALL)) {
				TskTaskDef obj = (TskTaskDef)this.get(TskTaskDef.class, id);
				return obj;
			} else {
				TskTaskDefCond cond = new TskTaskDefCond();
				cond.setObjId(id);
				TskTaskDef[] objs = this.getTaskDefs(user, cond, level);
				if (objs == null || objs.length == 0)
					return null;
				return objs[0];
			}
		} catch (TskException e) {
			throw e;
		} catch (Exception e) {
			throw new TskException(e);
		}
	}
	
	public TskTaskDef setTaskDef(String user, TskTaskDef obj, String level) throws TskException {
		if (level == null)
			level = LEVEL_ALL;
		try {
			if (level.equals(LEVEL_ALL)) {
				fill(user, obj);
				set(obj);
			} else {
				StringBuffer buf = new StringBuffer();
				buf.append("update TskTaskDef set");
				buf.append(" name=:name, creationUser=:creationUser, creationDate=:creationDate");
				buf.append(", modificationUser=:modificationUser, modificationDate=:modificationDate");
				buf.append(", description=:description");
				buf.append(", status=:status, correlation=:correlation, type=:type");
				buf.append(", processInstId=:processInstId, title=:title, priority=:priority");
				buf.append(", document=:document, assigner=:assigner, assignee=:assignee");
				buf.append(", assignmentDate=:assignmentDate, dueDate=:dueDate");
				buf.append(", form=:form");
				buf.append(", multiInstOrdering=:multiInstOrdering, multiInstFlowCondition=:multiInstFlowCondition");
				buf.append(", subFlowTargetId=:subFlowTargetId, subFlowTargetVersion=:subFlowTargetVersion, subFlowExecution=:subFlowExecution");
				buf.append(", serviceTargetId=:serviceTargetId");
				buf.append(" where objId=:objId");
				Query query = this.getSession().createQuery(buf.toString());
				query.setString(MisObject.A_NAME, obj.getName());
				query.setString(MisObject.A_CREATIONUSER, obj.getCreationUser());
				query.setTimestamp(MisObject.A_CREATIONDATE, obj.getCreationDate());
				query.setString(MisObject.A_MODIFICATIONUSER, obj.getModificationUser());
				query.setTimestamp(MisObject.A_MODIFICATIONDATE, obj.getModificationDate());
				query.setString(MisObject.A_DESCRIPTION, obj.getDescription());
				query.setString(MisObject.A_STATUS, obj.getStatus());
				query.setString(TskTaskDef.A_CORRELATION, obj.getCorrelation());
				query.setString(TskTaskDef.A_TYPE, obj.getType());
				query.setString(TskTaskDef.A_PROCESSINSTID, obj.getProcessInstId());
				query.setString(TskTaskDef.A_TITLE, obj.getTitle());
				query.setString(TskTaskDef.A_PRIORITY, obj.getPriority());
				query.setString(TskTaskDef.A_DOCUMENT, obj.getDocument());
				query.setString(TskTaskDef.A_ASSIGNER, obj.getAssigner());
				query.setString(TskTaskDef.A_ASSIGNEE, obj.getAssignee());
				query.setString(TskTaskDef.A_ASSIGNMENTDATE, obj.getAssignmentDate());
				query.setString(TskTaskDef.A_DUEDATE, obj.getDueDate());
				query.setString(TskTaskDef.A_FORM, obj.getForm());
				query.setString(TskTaskDef.A_MULTIINSTORDERING, obj.getMultiInstOrdering());
				query.setString(TskTaskDef.A_MULTIINSTFLOWCONDITION, obj.getMultiInstFlowCondition());
				query.setString(TskTaskDef.A_SUBFLOWTARGETID, obj.getSubFlowTargetId());
				query.setString(TskTaskDef.A_SUBFLOWTARGETVERSION, obj.getSubFlowTargetVersion());
				query.setString(TskTaskDef.A_SUBFLOWEXECUTION, obj.getSubFlowExecution());
				query.setString(TskTaskDef.A_SERVICETARGETID, obj.getServiceTargetId());
				query.setString(ClassObject.A_OBJID, obj.getObjId());
			}
			return obj;
		} catch (TskException e) {
			throw e;
		} catch (Exception e) {
			throw new TskException(e);
		}
	}
	
	public void removeTaskDef(String user, String id) throws TskException {
		try {
			TskTaskDef obj = this.getTaskDef(user, id, LEVEL_ALL);
			this.getHibernateTemplate().delete(obj);
			this.getHibernateTemplate().flush();
		} catch (TskException e) {
			throw e;
		} catch (Exception e) {
			throw new TskException(e);
		}
	}
	private Query appendQuery(StringBuffer buf, TskTaskDefCond cond) throws Exception{
		String objId = null;
		String creationUser = null;
		String modificationUser = null;
		String status = null;
		String correlation = null;
		String type = null;
		String priority = null;
		String processInstId = null;
		String title = null;
		String titleLike = null;
		String assigner = null;
		String assignee = null;
		String form = null;
		String multiInstOrdering = null;
		String multiInstFlowCondition = null;
		String subFlowTargetId = null;
		String subFlowTargetVersion = null;
		String subFlowExecution = null;
		Property[] extProps = null;
		if (cond != null) {
			objId = cond.getObjId();
			creationUser = cond.getCreationUser();
			modificationUser = cond.getModificationUser();
			status = cond.getStatus();
			correlation = cond.getCorrelation();
			type = cond.getType();
			priority = cond.getPriority();
			processInstId = cond.getProcessInstId();
			title = cond.getTitle();
			titleLike = cond.getTitleLike();
			assigner = cond.getAssigner();
			assignee = cond.getAssignee();
			form = cond.getForm();
			multiInstOrdering = cond.getMultiInstOrdering();
			multiInstFlowCondition = cond.getMultiInstFlowCondition();
			subFlowTargetId = cond.getSubFlowTargetId();
			subFlowTargetVersion = cond.getSubFlowTargetVersion();
			subFlowExecution = cond.getSubFlowExecution();
			extProps = cond.getExtendedProperties();
		}

		buf.append(" from TskTaskDef obj");
		if (extProps != null && extProps.length != 0) {
			for (int i=0; i<extProps.length; i++) {
				buf.append(" left join obj.extendedProperties as extProp").append(i);
			}
		}
		buf.append(" where obj.objId is not null");
		if (cond != null) {
			if (objId != null)
				buf.append(" and obj.objId = :objId");
			if (creationUser != null)
				buf.append(" and obj.creationUser = :creationUser");
			if (modificationUser != null)
				buf.append(" and obj.modificationUser = :modificationUser");
			if (status != null)
				buf.append(" and obj.status = :status");
			if (correlation != null)
				buf.append(" and obj.correlation = :correlation");
			if (type != null)
				buf.append(" and obj.type = :type");
			if (priority != null)
				buf.append(" and obj.priority = :priority");
			if (processInstId != null)
				buf.append(" and obj.processInstId = :processInstId");
			if (title != null)
				buf.append(" and obj.title = :title");
			if (titleLike != null)
				buf.append(" and obj.title like :titleLike");
			if (assigner != null)
				buf.append(" and obj.assigner = :assigner");
			if (assignee != null)
				buf.append(" and obj.assignee = :assignee");
			if (form != null)
				buf.append(" and obj.form = :form");
			if (multiInstOrdering != null)
				buf.append(" and obj.multiInstOrdering = :multiInstOrdering");
			if (multiInstFlowCondition != null)
				buf.append(" and obj.multiInstFlowCondition = :multiInstFlowCondition");
			if (subFlowTargetId != null)
				buf.append(" and obj.subFlowTargetId = :subFlowTargetId");
			if (subFlowTargetVersion != null)
				buf.append(" and obj.subFlowTargetVersion = :subFlowTargetVersion");
			if (subFlowExecution != null)
				buf.append(" and obj.subFlowExecution = :subFlowExecution");
			if (extProps != null && extProps.length != 0) {
				for (int i=0; i<extProps.length; i++) {
					Property extProp = extProps[i];
					String extName = extProp.getName();
					String extValue = extProp.getValue();
					if (extName != null)
						buf.append(" and extProp").append(i).append(".name = :extName").append(i);
					if (extValue != null)
						buf.append(" and extProp").append(i).append(".value = :extValue").append(i);
				}
			}

		}
		this.appendOrderQuery(buf, "obj", cond);
		
		Query query = this.createQuery(buf.toString(), cond);
		if (cond != null) {
			if (objId != null)
				query.setString("objId", objId);
			if (creationUser != null)
				query.setString("creationUser", creationUser);
			if (modificationUser != null)
				query.setString("modificationUser", modificationUser);
			if (status != null)
				query.setString("status", status);
			if (correlation != null)
				query.setString("correlation", correlation);
			if (type != null)
				query.setString("type", type);
			if (priority != null)
				query.setString("priority", priority);
			if (processInstId != null)
				query.setString("processInstId", processInstId);
			if (title != null)
				query.setString("title", title);
			if (titleLike != null)
				query.setString("titleLike", CommonUtil.toLikeString(titleLike));
			if (assigner != null)
				query.setString("assigner", assigner);
			if (assignee != null)
				query.setString("assignee", assignee);
			if (form != null)
				query.setString("form", form);
			if (multiInstOrdering != null)
				query.setString("multiInstOrdering", multiInstOrdering);
			if (multiInstFlowCondition != null)
				query.setString("multiInstFlowCondition", multiInstFlowCondition);
			if (subFlowTargetId != null)
				query.setString("subFlowTargetId", subFlowTargetId);
			if (subFlowTargetVersion != null)
				query.setString("subFlowTargetVersion", subFlowTargetVersion);
			if (subFlowExecution != null)
				query.setString("subFlowExecution", subFlowExecution);
			if (extProps != null && extProps.length != 0) {
				for (int i=0; i<extProps.length; i++) {
					Property extProp = extProps[i];
					String extName = extProp.getName();
					String extValue = extProp.getValue();
					if (extName != null)
						query.setString("extName"+i, extName);
					if (extValue != null)
						query.setString("extValue"+i, extValue);
				}
			}
		}
		return query;
	}
	
	public long getTaskDefSize(String user, TskTaskDefCond cond) throws TskException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			buf.append(" count(obj)");
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			long count =((Long)list.get(0)).longValue();
			return count;
		} catch (TskException e) {
			throw e;
		} catch (Exception e) {
			throw new TskException(e);
		}
	}
	
	public TskTaskDef[] getTaskDefs(String user, TskTaskDefCond cond, String level) throws TskException {
		try {
			if (level == null)
			level = LEVEL_LITE;
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			if (level.equals(LEVEL_ALL)) {
				buf.append(" obj");
			} else {
				buf.append(" obj.objId, obj.name, obj.creationUser, obj.creationDate, obj.modificationUser");
				buf.append(", obj.modificationDate, obj.status, obj.correlation, obj.type, obj.processInstId, obj.title");
				buf.append(", obj.description, obj.priority, obj.document, obj.assigner, obj.assignee");
				buf.append(", obj.assignmentDate, obj.dueDate, obj.form");
				buf.append(", obj.multiInstOrdering, obj.multiInstFlowCondition");
				buf.append(", obj.subFlowTargetId, obj.subFlowTargetVersion, obj.subFlowExecution");
				buf.append(", obj.serviceTargetId");
			}
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			if (!level.equals(LEVEL_ALL)) {
				List objList = new ArrayList();
				for (Iterator itr = list.iterator(); itr.hasNext();) {
					Object[] fields = (Object[]) itr.next();
					TskTaskDef obj = new TskTaskDef();
					int j = 0;
					obj.setObjId((String)fields[j++]);
					obj.setName((String)fields[j++]);
					obj.setCreationUser((String)fields[j++]);
					obj.setCreationDate(((Timestamp)fields[j++]));
					obj.setModificationUser(((String)fields[j++]));
					obj.setModificationDate(((Timestamp)fields[j++]));
					obj.setStatus(((String)fields[j++]));
					obj.setCorrelation(((String)fields[j++]));
					obj.setType(((String)fields[j++]));
					obj.setProcessInstId(((String)fields[j++]));
					obj.setTitle(((String)fields[j++]));
					obj.setDescription(((String)fields[j++]));
					obj.setPriority(((String)fields[j++]));
					obj.setDocument((String)fields[j++]);
					obj.setAssigner((String)fields[j++]);
					obj.setAssignee(((String)fields[j++]));
					obj.setAssignmentDate(((String)fields[j++]));
					obj.setDueDate(((String)fields[j++]));
					obj.setForm(((String)fields[j++]));
					obj.setMultiInstOrdering(((String)fields[j++]));
					obj.setMultiInstFlowCondition(((String)fields[j++]));
					obj.setSubFlowTargetId(((String)fields[j++]));
					obj.setSubFlowTargetVersion(((String)fields[j++]));
					obj.setSubFlowExecution(((String)fields[j++]));
					obj.setServiceTargetId(((String)fields[j++]));
					objList.add(obj);
				}
				list = objList;
			}
			TskTaskDef[] objs = new TskTaskDef[list.size()];
			list.toArray(objs);
			return objs;
		} catch (TskException e) {
			throw e;
		} catch (Exception e) {
			throw new TskException(e);
		}
	}
}
