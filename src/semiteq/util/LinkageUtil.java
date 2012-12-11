/*	
 * $Id: LinkageUtil.java,v 1.1 2012/01/18 02:13:55 hsshin Exp $
 * created by    : SHIN HYUN SEONG
 * creation-date : 2011. 12. 27.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package semiteq.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.smartworks.model.community.WorkSpace;
import net.smartworks.model.community.info.WorkSpaceInfo;
import net.smartworks.model.security.AccessPolicy;
import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.model.Property;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.infowork.domain.model.SwdDataField;
import net.smartworks.server.engine.infowork.domain.model.SwdRecord;
import net.smartworks.server.engine.organization.model.SwoDepartment;
import net.smartworks.server.engine.organization.model.SwoDepartmentCond;
import net.smartworks.server.engine.organization.model.SwoUser;
import net.smartworks.server.engine.organization.model.SwoUserCond;
import net.smartworks.server.engine.process.process.manager.IPrcManager;
import net.smartworks.server.engine.process.process.model.PrcProcess;
import net.smartworks.server.engine.process.process.model.PrcProcessCond;
import net.smartworks.server.engine.process.task.model.TskTask;
import net.smartworks.server.engine.process.task.model.TskTaskDef;
import net.smartworks.server.engine.process.task.model.TskTaskDefCond;
import net.smartworks.server.service.util.ModelConverter;
import semiteq.scheduler.model.PurchaseModel;
import semiteq.scheduler.model.RequestModel;

public class LinkageUtil {
	public void allocationFirstTask(Object object) throws Exception {
		try {
			String userId = "";
			String empNo = "";
			String userName = "";
			String processId = "";
			String uniqueId = "";
			String title = "";
			SwoUserCond swoUserCond = new SwoUserCond();
			SwoUser swoUser = new SwoUser();
			if (object instanceof PurchaseModel) {
				PurchaseModel purchaseModel = (PurchaseModel) object;
				processId = "prc_5a93a7b1b0e84ce48327802a3b4a2578"; // semiteq
				swoUserCond.setEmpNo(purchaseModel.getInsertUserId());
				uniqueId = purchaseModel.getPoNo();
				title = "발주등록_";
			} else if (object instanceof RequestModel) {
				RequestModel requestModel = (RequestModel) object;
				// processId = "prc_ac0726240b4a422f8e1fbee4a3669964"; //semiteq
				processId = "prc_1b9fa5d299b547a0bdc2e25717e8010e"; // MRO,S-PART
																	// 구매요청
																	// 접수/승인(ERP연동)
				swoUserCond.setEmpNo(requestModel.getInsertUserId());
				uniqueId = requestModel.getPrNo();
				title = "MRO,S-PART 구매요청 접수/승인_";
				// title = "S-Part 구매요청_";
			}
			swoUser = SwManagerFactory.getInstance().getSwoManager().getUsers("", swoUserCond, IManager.LEVEL_LITE)[0];
			userId = swoUser.getId();
			empNo = swoUser.getEmpNo();
			userName = swoUser.getName();
			title = title + uniqueId + "_" + empNo + "_" + userName;
			TskTaskDefCond tskDefCond = new TskTaskDefCond();
			tskDefCond.setExtendedProperties(new Property[] { new Property("startActivity", "true"), new Property("processId", processId) });
			TskTaskDef[] defs = SwManagerFactory.getInstance().getTskManager().getTaskDefs(userId, tskDefCond, IManager.LEVEL_ALL);
			if (defs == null || defs.length == 0)
				System.out.println("TskTaskDef is Null!!!!!!!!!!!!!");
			if (defs.length > 1)
				System.out.println("TskTaskDef is more than 1!!!!!!!!1");
			TskTaskDef def = defs[0];
			TskTask task = new TskTask();
			task.setAssignee(userId);
			task.setDef(def.getObjId());
			task.setForm(def.getForm());
			task.setStatus("11");
			task.setName(def.getName());
			task.setTitle(title);
			task.setType(def.getType());
			task.setExpectStartDate(new Date());
			task.setRealStartDate(new Date());
			SwdRecord rec = new SwdRecord();
			rec.setFormId(def.getForm());
			rec.setFormVersion(1);
			List fieldDataList = new ArrayList();
			if (object instanceof PurchaseModel) {
				PurchaseModel purchaseModel = (PurchaseModel) object;
				swoUserCond = new SwoUserCond();
				swoUserCond.setEmpNo(purchaseModel.getInsertUserId());
				swoUser = SwManagerFactory.getInstance().getSwoManager().getUsers("", swoUserCond, IManager.LEVEL_LITE)[0];
				purchaseModel.setReqPrsnId(swoUser.getId());
				purchaseModel.setReqPrsn(swoUser.getPosition() + " " + swoUser.getName());
				fieldDataList = setPurchase(purchaseModel);
			} else if (object instanceof RequestModel) {
				RequestModel requestModel = (RequestModel) object;
				swoUserCond = new SwoUserCond();
				swoUserCond.setEmpNo(requestModel.getInsertUserId());
				swoUser = SwManagerFactory.getInstance().getSwoManager().getUsers("", swoUserCond, IManager.LEVEL_LITE)[0];
				requestModel.setReqPrsnId(swoUser.getId());
				requestModel.setReqPrsn(swoUser.getPosition() + " " + swoUser.getName());
				SwoDepartmentCond swoDepartmentCond = new SwoDepartmentCond();
				swoDepartmentCond.setId(swoUser.getDeptId());
				SwoDepartment swoDepartment = SwManagerFactory.getInstance().getSwoManager().getDepartments("", swoDepartmentCond, IManager.LEVEL_LITE)[0];
				requestModel.setReqDept(swoUser.getDeptId());
				requestModel.setReqDeptName(swoDepartment.getName());
				fieldDataList = setRequest(requestModel);
			}
			SwdDataField[] fieldDatas = new SwdDataField[fieldDataList.size()];
			fieldDataList.toArray(fieldDatas);
			rec.setDataFields(fieldDatas);
			task.setDocument(rec.toString());
			IPrcManager prcMgr = SwManagerFactory.getInstance().getPrcManager();
			PrcProcessCond prcProcessCond = new PrcProcessCond();
			prcProcessCond.setProcessId(processId);
			PrcProcess prcProcess = prcMgr.getProcesses(userId, prcProcessCond, IManager.LEVEL_LITE)[0];
			String diagramId = prcProcess.getDiagramId();
			task.setExtendedPropertyValue("diagramId", diagramId);
			task.setExtendedPropertyValue("diagramVersion", "1");
			task.setExtendedPropertyValue("processId", processId);
			task.setExtendedPropertyValue("activityId", "2");
			task.setExtendedPropertyValue("startActivity", "true");
			task.setExtendedPropertyValue("isPublic", "N");
			task.setExtendedPropertyValue("subject", title);
			task.setExtendedPropertyValue("processInstCreationUser", userId);
			SwManagerFactory.getInstance().getTskManager().setTask(userId, task, IManager.LEVEL_ALL);
		} catch (Exception e) {
			throw e;
		}
	}

	public List setPurchase(PurchaseModel purchaseModel) {
		List fieldDataList = new ArrayList();
		SwdDataField fieldData1 = new SwdDataField();
		fieldData1.setId("17");
		fieldData1.setName("발주번호");
		fieldData1.setValue(purchaseModel.getPoNo());
		fieldDataList.add(fieldData1);
		SwdDataField fieldData2 = new SwdDataField();
		fieldData2.setId("3");
		fieldData2.setName("발주일");
		fieldData2.setValue(purchaseModel.getPoDt().toString());
		fieldDataList.add(fieldData2);
		SwdDataField fieldData3 = new SwdDataField();
		fieldData3.setId("14");
		fieldData3.setName("요청자");
		fieldData3.setRefForm("frm_user_SYSTEM");
		fieldData3.setRefFormField("4");
		fieldData3.setRefRecordId(purchaseModel.getReqPrsnId());
		fieldData3.setValue(purchaseModel.getReqPrsn());
		fieldDataList.add(fieldData3);
		SwdDataField fieldData4 = new SwdDataField();
		fieldData4.setId("31");
		fieldData4.setName("공급처");
		fieldData4.setValue(purchaseModel.getBpNm());
		fieldDataList.add(fieldData4);
		SwdDataField fieldData5 = new SwdDataField();
		fieldData5.setId("4");
		fieldData5.setName("예상납기일");
		fieldData5.setValue(purchaseModel.getDlvyDt().toString());
		fieldDataList.add(fieldData5);
		SwdDataField fieldData6 = new SwdDataField();
		fieldData6.setId("6");
		fieldData6.setName("발주순금액");
		fieldData6.setValue(purchaseModel.getPoDocAmt().setScale(0, BigDecimal.ROUND_DOWN).toString());
		fieldDataList.add(fieldData6);
		SwdDataField fieldData7 = new SwdDataField();
		fieldData7.setId("9");
		fieldData7.setName("부가세");
		fieldData7.setValue(purchaseModel.getVatLocAmt().setScale(0, BigDecimal.ROUND_DOWN).toString());
		fieldDataList.add(fieldData7);
		SwdDataField fieldData8 = new SwdDataField();
		fieldData8.setId("7");
		fieldData8.setName("발주총금액");
		fieldData8.setValue(purchaseModel.getTotLocAmt().setScale(0, BigDecimal.ROUND_DOWN).toString());
		fieldDataList.add(fieldData8);
		SwdDataField fieldData9 = new SwdDataField();
		fieldData9.setId("8");
		fieldData9.setName("결제방법");
		fieldData9.setValue(purchaseModel.getPayMeth());
		fieldDataList.add(fieldData9);
		// SwdDataField fieldData10 = new SwdDataField();
		// fieldData10.setId("33");
		// fieldData10.setName("첨부파일");
		// fieldData10.setValue(purchaseModel.getGroupId());
		// fieldDataList.add(fieldData10);
		return fieldDataList;
	}

	public List setRequest(RequestModel requestModel) {
		List fieldDataList = new ArrayList();
		SwdDataField fieldData1 = new SwdDataField();
		fieldData1.setId("126");
		fieldData1.setName("구매 요청번호");
		fieldData1.setValue(requestModel.getPrNo());
		fieldDataList.add(fieldData1);
		SwdDataField fieldData2 = new SwdDataField();
		fieldData2.setId("1");
		fieldData2.setName("요청자");
		fieldData2.setRefForm("frm_user_SYSTEM");
		fieldData2.setRefFormField("4");
		fieldData2.setRefRecordId(requestModel.getReqPrsnId());
		fieldData2.setValue(requestModel.getReqPrsn());
		fieldDataList.add(fieldData2);
		SwdDataField fieldData3 = new SwdDataField();
		fieldData3.setId("127");
		fieldData3.setName("요청부서");
		fieldData3.setRefForm("frm_dept_SYSTEM");
		fieldData3.setRefFormField("10");
		fieldData3.setRefRecordId(requestModel.getReqDept());
		fieldData3.setValue(requestModel.getReqDeptName());
		fieldDataList.add(fieldData3);
		SwdDataField fieldData4 = new SwdDataField();
		fieldData4.setId("55");
		fieldData4.setName("공급처");
		fieldData4.setValue(requestModel.getBpCd());
		fieldDataList.add(fieldData4);
		SwdDataField fieldData5 = new SwdDataField();
		fieldData5.setId("56");
		fieldData5.setName("담당자");
		fieldData5.setValue(requestModel.getBpPrsnNm());
		fieldDataList.add(fieldData5);
		SwdDataField fieldData6 = new SwdDataField();
		fieldData6.setId("57");
		fieldData6.setName("TEL");
		fieldData6.setValue(requestModel.getTelNo1());
		fieldDataList.add(fieldData6);
		SwdDataField fieldData7 = new SwdDataField();
		fieldData7.setId("58");
		fieldData7.setName("FAX");
		fieldData7.setValue(requestModel.getFaxNo());
		fieldDataList.add(fieldData7);
		// SwdDataField fieldData8 = new SwdDataField();
		// fieldData8.setId("63");
		// fieldData8.setName("품의서");
		// fieldData8.setValue("fg_0ce92901a0d04856868f0593a3a48df3");
		// fieldDataList.add(fieldData8);
		//
		// SwdDataField fieldData9 = new SwdDataField();
		// fieldData9.setId("64");
		// fieldData9.setName("견적서");
		// fieldData9.setValue("fg_57ae2c18628a4a0ba6e3afa8cacc3416");
		// fieldDataList.add(fieldData9);
		//
		// SwdDataField fieldData10 = new SwdDataField();
		// fieldData10.setId("65");
		// fieldData10.setName("제품규격서");
		// fieldData10.setValue("fg_e938a89fb3e045799a524c539e9bd7aa");
		// fieldDataList.add(fieldData10);
		//
		// SwdDataField fieldData11 = new SwdDataField();
		// fieldData11.setId("66");
		// fieldData11.setName("카탈로그");
		// fieldData11.setValue("fg_640f442d139c4394a449ffdd1f54d180");
		// fieldDataList.add(fieldData11);
		//
		// SwdDataField fieldData12 = new SwdDataField();
		// fieldData12.setId("43");
		// fieldData12.setName("구매요청서");
		// fieldData12.setValue(requestModel.getGroupId());
		// fieldDataList.add(fieldData12);
		return fieldDataList;
	}
}