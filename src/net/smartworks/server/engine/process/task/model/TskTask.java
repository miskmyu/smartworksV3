package net.smartworks.server.engine.process.task.model;

import java.util.Date;

import net.smartworks.server.engine.common.model.BaseObject;
import net.smartworks.server.engine.common.model.ClassObject;
import net.smartworks.server.engine.common.model.MisObject;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.common.util.DateUtil;
import net.smartworks.server.engine.common.util.XmlUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TskTask extends MisObject {
	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(TskTask.class);
	protected static final String PREFIX = "Tsk";
	
	private static final String NAME = CommonUtil.toName(TskTask.class, PREFIX);
	
	public static final String TASKSTATUS_ASSIGN = "11";
	public static final String TASKSTATUS_COMPLETE = "21";
	public static final String TASKSTATUS_RETURNED = "23";
	public static final String TASKSTATUS_CANCEL = "24";
	public static final String TASKSTATUS_ABORTED = "25";
	public static final String TASKSTATUS_CREATE = "1";
	public static final String TASKSTATUS_TEMPSAVE = "0";
	
	public static final String TASKTYPE_COMMON = "COMMON";
	public static final String TASKTYPE_REFERENCE = "REFERENCE";
	public static final String TASKTYPE_APPROVAL = "APPROVAL";
	public static final String TASKTYPE_SINGLE = "SINGLE";
	public static final String TASKTYPE_SUBFLOW = "SUBFLOW";
	public static final String TASKTYPE_SERVICE = "SERVICE";
	public static final String TASKTYPE_TEMPSAVE = "TEMPSAVE";
	
	public static final String TASKREFTYPE_MEMO = "MEMO";
	public static final String TASKREFTYPE_IMAGE ="IMAGE";
	public static final String TASKREFTYPE_BOARD = "BOARD";
	public static final String TASKREFTYPE_EVENT = "EVENT";
	public static final String TASKREFTYPE_FILE = "FILE";
	public static final String TASKREFTYPE_MOVIE = "MOVIE";
	public static final String TASKREFTYPE_NOTHING = "NOTHING";

	public static final String[] NOTUSERTASKTYPES = new String[]{"route", "and", "xor", "SUBFLOW", "SERVICE"};
	
	public static final String A_CORRELATION = "correlation";
	public static final String A_TYPE = "type";
	public static final String A_REFTYPE = "refType";
	public static final String A_PROCESSINSTID = "processInstId";
	public static final String A_TITLE = "title";
	public static final String A_PRIORITY = "priority";
	public static final String A_DOCUMENT = "document";
	public static final String A_ASSIGNER = "assigner";
	public static final String A_ASSIGNEE = "assignee";
	public static final String A_PERFORMER = "performer";
	public static final String A_ASSIGNMENTDATE = "assignmentDate";
	public static final String A_STARTDATE = "startDate";
	public static final String A_EXECUTIONDATE = "executionDate";
	public static final String A_DUEDATE = "dueDate";
	public static final String A_DEF = "def";
	public static final String A_FORM = "form";
	public static final String A_MULTIINSTID = "multiInstId";
	public static final String A_MULTIINSTORDERING = "multiInstOrdering";
	public static final String A_MULTIINSTFLOWCONDITION = "multiInstFlowCondition";
	public static final String A_LOOPCOUNTER = "loopCounter";
	public static final String A_STEP = "step";
	public static final String A_INSTVARIABLE = "instVariable";
	public static final String A_ISSTARTACTIVITY = "isStartActivity";
	public static final String A_FROMREFTYPE = "fromRefType";
	public static final String A_FROMREFID = "fromRefId";
	public static final String A_APPROVALID = "approvalId";
	public static final String A_FORWARDID = "forwardId";
	public static final String A_ISAPPROVALSOURCETASK = "isApprovalSourceTask";
	public static final String A_TARGETAPPROVALSTATUS = "targetApprovalStatus";
	public static final String A_WORKSPACEID = "workSpaceId";
	public static final String A_WORKSPACETYPE = "workSpaceType";
	public static final String A_ACCESSLEVEL = "accessLevel";
	public static final String A_ACCESSVALUE = "accessValue";
	
	private String correlation;
	
	private String type;
	private String refType;
	private String processInstId;
	private String title;
	private String priority;
	private String document;
	private String assigner;
	private String assignee;
	private String performer;
	private Date assignmentDate;
	private Date startDate;
	private Date executionDate;
	private Date dueDate;
	private String def;
	private String form;
	private String multiInstId;
	private String multiInstOrdering;
	private String multiInstFlowCondition;
	private Integer loopCounterInteger;
	private Integer stepInteger;
	private String instVariable;
	private String isStartActivity;
	private String fromRefType;
	private String fromRefId;
	private String approvalId;
	private String forwardId;
	private String isApprovalSourceTask;
	private String targetApprovalStatus;
	private String workSpaceId;
	private String workSpaceType;
	private String isUserSetAccessLevel;
	private String accessLevel;
	private String accessValue;
	
	public TskTask() {
		super();
	}
	public String toString(String name, String tab){
		if(name == null || name.trim().length() == 0)
			name = NAME;
		return super.toString(name, tab);
	}
	public String toAttributesString(){
		StringBuffer buf = new StringBuffer();
		buf.append(super.toAttributesString());
		appendAttributeString(A_CORRELATION, correlation, buf);
		appendAttributeString(A_TYPE, type, buf);
		appendAttributeString(A_REFTYPE, refType, buf);
		appendAttributeString(A_PROCESSINSTID, processInstId, buf);
		appendAttributeString(A_PRIORITY, priority, buf);
		appendAttributeString(A_ASSIGNER, assigner, buf);
		appendAttributeString(A_ASSIGNEE, assignee, buf);
		appendAttributeString(A_PERFORMER, performer, buf);
		appendAttributeString(A_ASSIGNMENTDATE, assignmentDate, buf);
		appendAttributeString(A_STARTDATE, startDate, buf);
		appendAttributeString(A_EXECUTIONDATE, executionDate, buf);
		appendAttributeString(A_DUEDATE, dueDate, buf);
		appendAttributeString(A_MULTIINSTID, multiInstId, buf);
		appendAttributeString(A_MULTIINSTORDERING, multiInstOrdering, buf);
		appendAttributeString(A_MULTIINSTFLOWCONDITION, multiInstFlowCondition, buf);
		appendAttributeString(A_LOOPCOUNTER, getLoopCounter(), buf);
		appendAttributeString(A_ISSTARTACTIVITY, isStartActivity, buf);
		appendAttributeString(A_FROMREFTYPE, fromRefType, buf);
		appendAttributeString(A_FROMREFID, fromRefId, buf);
		appendAttributeString(A_APPROVALID, approvalId, buf);
		appendAttributeString(A_FORWARDID, forwardId, buf);
		appendAttributeString(A_ISAPPROVALSOURCETASK, isApprovalSourceTask, buf);
		appendAttributeString(A_TARGETAPPROVALSTATUS, targetApprovalStatus, buf);
		appendAttributeString(A_WORKSPACEID, workSpaceId, buf);
		appendAttributeString(A_WORKSPACETYPE, workSpaceType, buf);
		appendAttributeString(A_ACCESSLEVEL, accessLevel, buf);
		appendAttributeString(A_ACCESSVALUE, accessValue, buf);
		appendAttributeString(A_STEP, getStep(), buf);
		return buf.toString();
	}
	public String toElementsString(String tab) {
		StringBuffer buf = new StringBuffer();
		buf.append(super.toElementsString(tab));
		appendElementString(A_TITLE, getTitle(), tab, buf);
		appendElementString(A_DOCUMENT, getDocument(), tab, true, buf);
		appendElementString(A_DEF, getDef(), tab, true, buf);
		appendElementString(A_FORM, getForm(), tab, true, buf);
		appendElementString(A_INSTVARIABLE, getInstVariable(), tab, true, buf);
		return buf.toString();
	}
	public static BaseObject toObject(Node node, BaseObject baseObj) throws Exception {
		if (node == null)
			return null;
		
		TskTask obj = null;
		if (baseObj == null || !(baseObj instanceof TskTask))
			obj = new TskTask();
		else
			obj = (TskTask)baseObj;
		
		MisObject.toObject(node, obj);
		
		NamedNodeMap attrMap = node.getAttributes();
		if (attrMap != null) {
			Node correlation = attrMap.getNamedItem(A_CORRELATION);
			Node type = attrMap.getNamedItem(A_TYPE);
			Node refType = attrMap.getNamedItem(A_REFTYPE);
			Node processInstId = attrMap.getNamedItem(A_PROCESSINSTID);
			Node priority = attrMap.getNamedItem(A_PRIORITY);
			Node assigner = attrMap.getNamedItem(A_ASSIGNER);
			Node assignee = attrMap.getNamedItem(A_ASSIGNEE);
			Node performer = attrMap.getNamedItem(A_PERFORMER);
			Node assignmentDate = attrMap.getNamedItem(A_ASSIGNMENTDATE);
			Node startDate = attrMap.getNamedItem(A_STARTDATE);
			Node executionDate = attrMap.getNamedItem(A_EXECUTIONDATE);
			Node dueDate = attrMap.getNamedItem(A_DUEDATE);
			Node multiInstId = attrMap.getNamedItem(A_MULTIINSTID);
			Node multiInstOrdering = attrMap.getNamedItem(A_MULTIINSTORDERING);
			Node multiInstFlowCondition = attrMap.getNamedItem(A_MULTIINSTFLOWCONDITION);
			Node loopCounter = attrMap.getNamedItem(A_LOOPCOUNTER);
			Node isStartActivity = attrMap.getNamedItem(A_ISSTARTACTIVITY);
			Node fromRefType = attrMap.getNamedItem(A_FROMREFTYPE);
			Node fromRefId = attrMap.getNamedItem(A_FROMREFID);
			Node approvalId = attrMap.getNamedItem(A_APPROVALID);
			Node forwardId = attrMap.getNamedItem(A_FORWARDID);
			Node isApprovalSourceTask = attrMap.getNamedItem(A_ISAPPROVALSOURCETASK);
			Node targetApprovalStatus = attrMap.getNamedItem(A_TARGETAPPROVALSTATUS);
			Node workSpaceId = attrMap.getNamedItem(A_WORKSPACEID);
			Node workSpaceType = attrMap.getNamedItem(A_WORKSPACETYPE);
			Node accessLevel = attrMap.getNamedItem(A_ACCESSLEVEL);
			Node accessValue = attrMap.getNamedItem(A_ACCESSVALUE);
			Node step = attrMap.getNamedItem(A_STEP);
			if (correlation != null)
				obj.setCorrelation(correlation.getNodeValue());
			if (type != null)
				obj.setType(type.getNodeValue());
			if (refType != null)
				obj.setRefType(refType.getNodeValue());
			if (processInstId != null)
				obj.setProcessInstId(processInstId.getNodeValue());
			if (priority != null)
				obj.setPriority(priority.getNodeValue());
			if (assigner != null)
				obj.setAssigner(assigner.getNodeValue());
			if (assignee != null)
				obj.setAssignee(assignee.getNodeValue());
			if (performer != null)
				obj.setPerformer(performer.getNodeValue());
			if (assignmentDate != null)
				obj.setAssignmentDate(DateUtil.toDate(assignmentDate.getNodeValue()));
			if (startDate != null)
				obj.setStartDate(DateUtil.toDate(startDate.getNodeValue()));
			if (executionDate != null)
				obj.setExecutionDate(DateUtil.toDate(executionDate.getNodeValue()));
			if (dueDate != null)
				obj.setDueDate(DateUtil.toDate(dueDate.getNodeValue()));
			if (multiInstId != null)
				obj.setMultiInstId(multiInstId.getNodeValue());
			if (multiInstOrdering != null)
				obj.setMultiInstOrdering(multiInstOrdering.getNodeValue());
			if (multiInstFlowCondition != null)
				obj.setMultiInstFlowCondition(multiInstFlowCondition.getNodeValue());
			if (loopCounter != null)
				obj.setLoopCounter(Integer.parseInt(loopCounter.getNodeValue()));
			if (isStartActivity != null)
				obj.setIsStartActivity(isStartActivity.getNodeValue());
			if (fromRefType != null)
				obj.setFromRefType(fromRefType.getNodeValue());
			if (fromRefId != null)
				obj.setFromRefId(fromRefId.getNodeValue());
			if (approvalId != null)
				obj.setApprovalId(approvalId.getNodeValue());
			if (forwardId != null)
				obj.setForwardId(forwardId.getNodeValue());
			if (isApprovalSourceTask != null)
				obj.setIsApprovalSourceTask(isApprovalSourceTask.getNodeValue());
			if (targetApprovalStatus != null)
				obj.setTargetApprovalStatus(targetApprovalStatus.getNodeValue());
			if (workSpaceId != null)
				obj.setWorkSpaceId(workSpaceId.getNodeValue());
			if (workSpaceType != null)
				obj.setWorkSpaceType(workSpaceType.getNodeValue());
			if (step != null)
				obj.setStep(Integer.parseInt(step.getNodeValue()));
			if (accessLevel != null)
				obj.setAccessLevel(accessLevel.getNodeValue());
			if (accessValue != null)
				obj.setAccessValue(accessValue.getNodeValue());
		}
		
		NodeList childNodeList = node.getChildNodes();
		if (childNodeList == null || childNodeList.getLength() == 0)
			return obj;
		for (int i=0; i<childNodeList.getLength(); i++) {
			Node childNode = childNodeList.item(i);
			if (childNode.getNodeType() != Node.ELEMENT_NODE || childNode.getNodeName() == null)
				continue;
			if (childNode.getNodeName().equals(A_TITLE)) {
				obj.setTitle(getNodeValue(childNode));
			} else if (childNode.getNodeName().equals(A_DOCUMENT)) {
				obj.setDocument(getNodeValue(childNode, true));
			} else if (childNode.getNodeName().equals(A_DEF)) {
				obj.setDef(getNodeValue(childNode, true));
			} else if (childNode.getNodeName().equals(A_FORM)) {
				obj.setForm(getNodeValue(childNode, true));
			} else if (childNode.getNodeName().equals(A_INSTVARIABLE)) {
				obj.setInstVariable(getNodeValue(childNode, true));
			}
		}
		return obj;
	}
	public static BaseObject toObject(String str) throws Exception {
		if (str == null)
			return null;
		Document doc = XmlUtil.toDocument(str);
		if (doc == null)
			return null;
		return toObject(doc.getDocumentElement(), null);
	}
	
	public static TskTask[] add(TskTask[] objs, TskTask obj) {
		if (obj == null)
			return objs;
		int size = 0;
		if (objs != null)
			size = objs.length;
		TskTask[] newObjs = new TskTask[size+1];
		int i;
		for (i=0; i<size; i++)
			newObjs[i] = objs[i];
		newObjs[i] = obj;
		return newObjs;
	}
	public static TskTask[] remove(TskTask[] objs, TskTask obj) {
		if (obj == null)
			return objs;
		int size = 0;
		if (objs != null)
			size = objs.length;
		if (size == 0)
			return objs;
		TskTask[] newObjs = new TskTask[size-1];
		int i;
		int j = 0;
		for (i=0; i<size; i++) {
			if (objs[i].equals(obj))
				continue;
			newObjs[j++] = objs[i];
		}
		return newObjs;
	}
	public static TskTask[] left(TskTask[] objs, TskTask obj) {
		if (objs == null || objs.length == 0 || obj == null)
			return objs;
		int idx = -1;
		for (int i=0; i<objs.length; i++) {
			if (!objs[i].equals(obj))
				continue;
			idx = i;
			break;
		}
		if (idx < 1)
			return objs;
		TskTask[] newObjs = new TskTask[objs.length];
		for (int i=0; i<objs.length; i++) {
			if (i == idx) {
				newObjs[i] = objs[idx-1];
				continue;
			} else if (i == idx-1) {
				newObjs[i] = objs[idx];
				continue;
			}
			newObjs[i] = objs[i];
		}
		return newObjs;
	}
	public static TskTask[] right(TskTask[] objs, TskTask obj) {
		if (objs == null || objs.length == 0 || obj == null)
			return objs;
		int idx = -1;
		for (int i=0; i<objs.length; i++) {
			if (!objs[i].equals(obj))
				continue;
			idx = i;
			break;
		}
		if (idx == -1 || idx+1 == objs.length)
			return objs;
		TskTask[] newObjs = new TskTask[objs.length];
		for (int i=0; i<objs.length; i++) {
			if (i == idx) {
				newObjs[i] = objs[idx+1];
				continue;
			} else if (i == idx+1) {
				newObjs[i] = objs[idx];
				continue;
			}
			newObjs[i] = objs[i];
		}
		return newObjs;
	}
	public Object clone() throws CloneNotSupportedException {
		try {
			return toObject(this.toString());
		} catch (Exception e) {
			logger.warn(e, e);
			return null;
		}
	}
	public ClassObject cloneNew() {
		TskTask obj = null;
		try {
			obj = (TskTask)this.clone();
			obj.setObjId(null);
			obj.setStatus(null);
			obj.setCreationDate(null);
			obj.setModificationDate(null);
			obj.setStartDate(null);
			obj.setMultiInstId(null);
		} catch (CloneNotSupportedException e) {
			logger.warn(e, e);
		}
		return obj;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getAssignmentDate() {
		return assignmentDate;
	}
	public void setAssignmentDate(Date assignmentDate) {
		this.assignmentDate = assignmentDate;
	}
	public String getDocument() {
		return document;
	}
	public void setDocument(String document) {
		this.document = document;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public Date getExecutionDate() {
		return executionDate;
	}
	public void setExecutionDate(Date executionDate) {
		this.executionDate = executionDate;
	}
	public String getForm() {
		return form;
	}
	public void setForm(String form) {
		this.form = form;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getProcessInstId() {
		return processInstId;
	}
	public void setProcessInstId(String processInstId) {
		this.processInstId = processInstId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRefType() {
		return refType;
	}
	public void setRefType(String refType) {
		this.refType = refType;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public String getAssigner() {
		return assigner;
	}
	public void setAssigner(String assigner) {
		this.assigner = assigner;
	}
	public String getCorrelation() {
		return correlation;
	}
	public void setCorrelation(String correlation) {
		this.correlation = correlation;
	}
	public String getDef() {
		return def;
	}
	public void setDef(String def) {
		this.def = def;
	}
	public int getLoopCounter() {
		if (loopCounterInteger == null)
			return 0;
		return loopCounterInteger.intValue();
	}
	public void setLoopCounter(int loopCounter) {
		if (loopCounter == 0) {
			if (this.loopCounterInteger != null)
				this.loopCounterInteger = null;
			return;
		}
		this.loopCounterInteger = new Integer(loopCounter);
	}
	public Integer getLoopCounterInteger() {
		return loopCounterInteger;
	}
	public void setLoopCounterInteger(Integer loopCounterInteger) {
		this.loopCounterInteger = loopCounterInteger;
	}
	public String getMultiInstFlowCondition() {
		return multiInstFlowCondition;
	}
	public void setMultiInstFlowCondition(String multiInstFlowCondition) {
		this.multiInstFlowCondition = multiInstFlowCondition;
	}
	public String getMultiInstId() {
		return multiInstId;
	}
	public void setMultiInstId(String multiInstId) {
		this.multiInstId = multiInstId;
	}
	public String getMultiInstOrdering() {
		return multiInstOrdering;
	}
	public void setMultiInstOrdering(String multiInstOrdering) {
		this.multiInstOrdering = multiInstOrdering;
	}
	public Integer getStepInteger() {
		return stepInteger;
	}
	public void setStepInteger(Integer stepInteger) {
		this.stepInteger = stepInteger;
	}
	public int getStep() {
		if (stepInteger == null)
			return 0;
		return stepInteger.intValue();
	}
	public void setStep(int step) {
		if (step == 0) {
			if (this.stepInteger != null)
				this.stepInteger = null;
			return;
		}
		this.stepInteger = new Integer(step);
	}
	public String getPerformer() {
		return performer;
	}
	public void setPerformer(String performer) {
		this.performer = performer;
	}
	public String getInstVariable() {
		return instVariable;
	}
	public void setInstVariable(String instVariable) {
		this.instVariable = instVariable;
	}
	public String getIsStartActivity() {
		return isStartActivity;
	}
	public void setIsStartActivity(String isStartActivity) {
		this.isStartActivity = isStartActivity;
	}
	public String getFromRefType() {
		return fromRefType;
	}
	public void setFromRefType(String fromRefType) {
		this.fromRefType = fromRefType;
	}
	public String getFromRefId() {
		return fromRefId;
	}
	public void setFromRefId(String fromRefId) {
		this.fromRefId = fromRefId;
	}
	public String getWorkSpaceId() {
		return workSpaceId;
	}
	public String getApprovalId() {
		return approvalId;
	}
	public void setApprovalId(String approvalId) {
		this.approvalId = approvalId;
	}
	public String getForwardId() {
		return forwardId;
	}
	public void setForwardId(String forwardId) {
		this.forwardId = forwardId;
	}
	public void setWorkSpaceId(String workSpaceId) {
		this.workSpaceId = workSpaceId;
	}
	public String getWorkSpaceType() {
		return workSpaceType;
	}
	public void setWorkSpaceType(String workSpaceType) {
		this.workSpaceType = workSpaceType;
	}
	public String getAccessLevel() {
		return accessLevel;
	}
	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}
	public String getAccessValue() {
		return accessValue;
	}
	public void setAccessValue(String accessValue) {
		this.accessValue = accessValue;
	}
	public String getIsApprovalSourceTask() {
		return isApprovalSourceTask;
	}
	public void setIsApprovalSourceTask(String isApprovalSourceTask) {
		this.isApprovalSourceTask = isApprovalSourceTask;
	}
	public String getTargetApprovalStatus() {
		return targetApprovalStatus;
	}
	public void setTargetApprovalStatus(String targetApprovalStatus) {
		this.targetApprovalStatus = targetApprovalStatus;
	}
	public String getIsUserSetAccessLevel() {
		return isUserSetAccessLevel;
	}
	public void setIsUserSetAccessLevel(String isUserSetAccessLevel) {
		this.isUserSetAccessLevel = isUserSetAccessLevel;
	}
}
