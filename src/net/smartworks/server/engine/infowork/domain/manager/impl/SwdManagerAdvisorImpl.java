package net.smartworks.server.engine.infowork.domain.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.model.Order;
import net.smartworks.server.engine.common.model.Property;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.common.util.DateUtil;
import net.smartworks.server.engine.common.util.MisUtil;
import net.smartworks.server.engine.common.util.StringUtil;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.infowork.domain.manager.AbstractSwdManagerAdvisor;
import net.smartworks.server.engine.infowork.domain.manager.ISwdManager;
import net.smartworks.server.engine.infowork.domain.model.SwdDataField;
import net.smartworks.server.engine.infowork.domain.model.SwdDataRef;
import net.smartworks.server.engine.infowork.domain.model.SwdDataRefCond;
import net.smartworks.server.engine.infowork.domain.model.SwdDomain;
import net.smartworks.server.engine.infowork.domain.model.SwdDomainCond;
import net.smartworks.server.engine.infowork.domain.model.SwdField;
import net.smartworks.server.engine.infowork.domain.model.SwdRecord;
import net.smartworks.server.engine.infowork.domain.model.SwdRecordCond;
import net.smartworks.server.engine.infowork.form.manager.ISwfManager;
import net.smartworks.server.engine.infowork.form.model.SwfField;
import net.smartworks.server.engine.infowork.form.model.SwfForm;
import net.smartworks.server.engine.infowork.form.model.SwfMapping;
import net.smartworks.server.engine.infowork.form.model.SwfMappings;
import net.smartworks.server.engine.process.process.manager.IPrcManager;
import net.smartworks.server.engine.process.task.manager.ITskManager;
import net.smartworks.server.engine.process.task.model.TskTask;
import net.smartworks.server.engine.process.task.model.TskTaskCond;
import net.smartworks.util.LocalDate;

public class SwdManagerAdvisorImpl extends AbstractSwdManagerAdvisor {
	public SwdManagerAdvisorImpl() {
		super();
	}

	private ISwfManager swfManager;
	private ISwdManager swdManager;
	private ITskManager tskManager;
	private IPrcManager prcManager;

	public IPrcManager getPrcManager() {
		if (prcManager == null)
			prcManager = SwManagerFactory.getInstance().getPrcManager();
		return prcManager;
	}
	public ITskManager getTskManager() {
		if (tskManager == null)
			tskManager = SwManagerFactory.getInstance().getTskManager();
		return tskManager;
	}
	public ISwfManager getSwfManager() {
		if (swfManager == null)
			swfManager = SwManagerFactory.getInstance().getSwfManager();
		return swfManager;
	}
	public ISwdManager getSwdManager() {
		if (swdManager == null)
			swdManager = SwManagerFactory.getInstance().getSwdManager();
		return swdManager;
	}
	public void postGetRecord(String user, String domainId, String recordId, String level, SwdRecord res) throws Exception {
		populateRecord(user, res);
	}
	public void postGetRecord(String user, SwdRecordCond cond, String level, SwdRecord res) throws Exception {
		populateRecord(user, res);
	}
	public void postGetRecords(String user, SwdRecordCond cond, String level, SwdRecord[] res) throws Exception {
		if(res != null) {
			if(level == null || level == IManager.LEVEL_ALL) {
				for(SwdRecord swdRecord: res) {
					populateRecord(user, swdRecord);
				}
			}
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
		SwdDataRef[] dataRefs = this.getSwdManager().getDataRefs(user, cond, null);
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
	public void preSetRecord(String user, SwdRecord obj, String level) throws Exception {
		//date to localdate - Date date = new Date();
		LocalDate date = new LocalDate();
		String cUser = obj.getCreationUser();
		Date cDate = obj.getCreationDate();
		if (cUser == null || cDate == null) {
			obj.setCreationUser(user);
			obj.setCreationDate(date);
		}
		obj.setModificationUser(user);
		obj.setModificationDate(date);
	}
	public void postSetRecord(String user, SwdRecord obj, String level) throws Exception {
		// 도메인 조회
		String formId = obj.getFormId();
		String domainId = null;
		String recordId = null;
		String name = null;
		String title = null;
		if ("DEFAULTFORM".equalsIgnoreCase(formId)) {
			
		} else {
			domainId = obj.getDomainId();
			SwdDomain domain = null;
			if (CommonUtil.isEmpty(formId)) {
				if (CommonUtil.isEmpty(domainId))
					return;
				domain = getSwdManager().getDomain(user, domainId, null);
				formId = domain.getFormId();
				obj.setFormId(formId);
			} else {
				SwdDomainCond domainCond = new SwdDomainCond();
				domainCond.setFormId(formId);
				domain = getSwdManager().getDomain(user, domainCond, null);
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
					getSwdManager().setDataRef(user, dataRef, null);
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
					getSwdManager().setDataRef(user, dataRef, null);
				}
			}
			
			// 이후 데이터 매핑
			postFieldMapping(user, domain, obj);

			name = domain.getFormName();
			String titleFieldId = domain.getTitleFieldId();
			if (!CommonUtil.isEmpty(titleFieldId))
				title = StringUtil.subString(obj.getDataFieldValue(titleFieldId), 0, 100, "...");
		}
		
		// 결재자 / 수신자 / 참조자
		
		
		String approvalLine = null;
		String forwardSubject = obj.getExtendedAttributeValue("txtForwardSubject");
		String forwardForwardee = obj.getExtendedAttributeValue("txtForwardForwardee");
		String forwardComments = obj.getExtendedAttributeValue("txtForwardComments");
		
		if (CommonUtil.isEmpty(obj.getExtendedAttributeValue("setMode")) || !obj.getExtendedAttributeValue("setMode").equals("process")) {
			
			//정보관리 업무를 저장할때 정보관리업무에 대한 태스크를 생성한다 
			//정보관리 업무를 수정할때 처음 정보관리 업무를 만들때 생성된 태스크를 조회 하여 clon하여 태스크를 생성한다.
			//기존에 만들어진 태스크에 초기 메타 데이터가 저장 되어 있기 때문이다
			TskTaskCond cond = new TskTaskCond();
			cond.setExtendedProperties(new Property[] {new Property("recordId", obj.getRecordId())});
			cond.setOrders(new Order[] {new Order("creationDate", false)});
			
			TskTask[] tasks = getTskManager().getTasks(user, cond, IManager.LEVEL_ALL);
			TskTask task = null;
			
			if (tasks != null && tasks.length != 0) {
				task = (TskTask)tasks[0].clone();
				
				task.setObjId(null);
				task.setStatus(null);
				task.setCreationDate(new LocalDate());
				task.setModificationDate(null);
				task.setCreationUser(user);
				task.setExecutionDate(null);
				task.setDocument(obj.toString(null, null));
				task.setAssigner(user);
				task.setAssignee(user);
				task.setAssignmentDate(new LocalDate());
				if (approvalLine != null) {
					task.setExtendedPropertyValue("approvalLine", approvalLine);
				} else {
					task.setExtendedPropertyValue("approvalLine", null);
				}
				//MEATADATA

				task.setExpectStartDate(new LocalDate());
				task.setRealStartDate(new LocalDate());
				task.setExpectEndDate(null);
				task.setRealEndDate(null);
				
				if (forwardForwardee != null) {
					task.setExtendedPropertyValue("referenceUser", forwardForwardee);
				} else {
					task.setExtendedPropertyValue("referenceUser", null);
				}
			} else {
				task = new TskTask();

				task.setName(name);
				task.setType("SINGLE");
				task.setPriority(null);
				
				task.setExtendedPropertyValue("subject", forwardSubject == null || forwardSubject.length() == 0 ? title : forwardSubject);
				task.setTitle(title);
				task.setDocument(obj.toString(null, null));
				task.setAssigner(user);
				task.setAssignee(user);
				task.setAssignmentDate(new LocalDate());
				task.setForm(formId);
				task.setDef(domainId + "|" + recordId);
				task.setWorkSpaceId(obj.getWorkSpaceId());
				task.setWorkSpaceType(obj.getWorkSpaceType());
				task.setAccessLevel(obj.getAccessLevel());
				task.setAccessValue(obj.getAccessValue());
				task.setExtendedPropertyValue("domainId", domainId);
				task.setExtendedPropertyValue("recordId", recordId);
				if (approvalLine != null)
					task.setExtendedPropertyValue("approvalLine", approvalLine);
		
				//MEATADATA
				
				if (forwardComments != null)
					task.setExtendedPropertyValue("workContents", forwardComments);
				if (forwardForwardee != null)
					task.setExtendedPropertyValue("referenceUser", forwardForwardee);
					
				task.setExpectStartDate(new LocalDate());
				task.setRealStartDate(new LocalDate());
				//task.setExpectEndDate(endDate);
				//task.setRealEndDate(endDate);
			}
			if (obj.getExtendedAttributeValue("extValues") != null && obj.getExtendedAttributeValue("extValues").length() != 0)
				task.setExtendedAttributeValue("extValues", obj.getExtendedAttributeValue("extValues"));
			if (obj.getExtendedAttributeValue("tskRefType") != null && obj.getExtendedAttributeValue("tskRefType").length() != 0)
				task.setRefType(obj.getExtendedAttributeValue("tskRefType"));
			this.getTskManager().executeTask(user, task, null);
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
	public void postRemoveRecord(String user, String domainId, String recordId) throws Exception {
		SwdDomain domain = getSwdManager().getDomain(user, domainId, IManager.LEVEL_LITE);
		
		// 데이터 참조 삭제
		this.removeDataRefsByRecordId(user, domain.getFormId(), recordId);
		
		// 댓글 삭제
		//getOpinionManager().deleteOpinionByRef("admin", SmartCommonConstants.TYPE_REF_SINGLE_WORK, domain.getFormId(), recordId);
		
		// 전달업무 삭제
		this.removeProcessInstsByRecordId(user, domain.getFormId(), recordId);
	}
	
	private void removeDataRefsByRecordId(String user, String formId, String recordId) throws Exception {
		SwdDataRefCond dataRefCond = new SwdDataRefCond();
		dataRefCond.setMyFormId(formId);
		dataRefCond.setMyRecordId(recordId);
		SwdDataRef[] dataRefs = getSwdManager().getDataRefs(user, dataRefCond, IManager.LEVEL_LITE);
		if (!CommonUtil.isEmpty(dataRefs)) {
			for (SwdDataRef dataRef : dataRefs)
				getSwdManager().removeDataRef(user, dataRef.getObjId());
		}
	}
	private void removeProcessInstsByRecordId(String user, String formId, String recordId) throws Exception {
		if (CommonUtil.isEmpty(formId) || CommonUtil.isEmpty(recordId))
			return;
		
		// TODO 진행 중인 업무만 지우기로 함.
		TskTaskCond cond = new TskTaskCond();
		cond.setType("SINGLE");
		cond.setForm(formId);
		cond.setExtendedProperties(new Property[] {new Property("recordId", recordId)});
		Set exeStatusSet = MisUtil.taskExecutedStatusSet();
		if (CommonUtil.isEmpty(exeStatusSet))
			return;
		String[] exeStatuses = new String[exeStatusSet.size()];
		exeStatusSet.toArray(exeStatuses);
		cond.setStatusNotIns(exeStatuses);
		TskTask[] tasks = getTskManager().getTasks(user, cond, null);
		if (CommonUtil.isEmpty(tasks))
			return;
		
		for (TskTask task : tasks)
			getTskManager().removeTask(user, task.getObjId());
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
	
	private void preFieldMapping(String user, SwdRecordCond cond, SwdRecord[] records) throws Exception {
		String domainId = cond.getDomainId();
		String formId = cond.getFormId();
		if (domainId != null) {
			this.preFieldMapping(user, domainId, records);
		} else if (formId != null) {
			this.preFieldMappingByFormId(user, formId, records);
		}
	}
	private void preFieldMapping(String user, String domainId, SwdRecord[] records) throws Exception {
		if (domainId == null || CommonUtil.isEmpty(records))
			return;
		SwdDomain domain = getSwdManager().getDomain(user, domainId, IManager.LEVEL_LITE);
		preFieldMapping(user, domain, records);
	}
	private void preFieldMappingByFormId(String user, String formId, SwdRecord[] records) throws Exception {
		if (formId == null || CommonUtil.isEmpty(records))
			return;
		SwdDomainCond cond = new SwdDomainCond();
		cond.setFormId(formId);
		SwdDomain domain = getSwdManager().getDomain(user, cond, IManager.LEVEL_LITE);
		preFieldMapping(user, domain, records);
	}
	private void preFieldMapping(String user, SwdDomain domain, SwdRecord[] records) throws Exception {
		if (domain == null || CommonUtil.isEmpty(records))
			return;
		SwfForm form = getSwfManager().getForm(null, domain.getFormId());
		this.getSwdManager().preFieldMapping(user, form, records, null);
	}
	private void postFieldMapping(String user, SwdDomain domain, SwdRecord record) throws Exception {
		if (domain == null || record == null)
			return;
		SwfForm form = getSwfManager().getForm(null, domain.getFormId());
		if (form == null)
			return;
		this.getSwdManager().postFieldMapping(user, form, record, null);
	}
}
