/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 12. 28.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.scheduling.sample;

import java.util.Calendar;

import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.model.Filter;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.infowork.domain.model.SwdDataField;
import net.smartworks.server.engine.infowork.domain.model.SwdDomain;
import net.smartworks.server.engine.infowork.domain.model.SwdDomainCond;
import net.smartworks.server.engine.infowork.domain.model.SwdRecord;
import net.smartworks.server.engine.infowork.domain.model.SwdRecordCond;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class SemiteqSpecWatcher  extends QuartzJobBean   {

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		try {
			String formId = "frm_d170b5bdf2314881bdcc73e5ab3d8da2";
			//유효기간필드아이디
			String targetDateFieldId = "4";
			String targetDraftUserFieldId = "1";
			String targetSpecChargerFieldId = "2";
			
			SwdDomainCond swdDomainCond = new SwdDomainCond();
			swdDomainCond.setFormId(formId);
			SwdDomain swdDomain = SwManagerFactory.getInstance().getSwdManager().getDomain("", swdDomainCond, IManager.LEVEL_LITE);

			if(swdDomain == null)
				throw new Exception("Not Exist Spec InfomationWork Or Not Exist FormId : " + formId);

			SwdRecordCond swdRecordCond = new SwdRecordCond();
			swdRecordCond.setFormId(swdDomain.getFormId());
			swdRecordCond.setDomainId(swdDomain.getObjId());
			
			String tableColName = SwManagerFactory.getInstance().getSwdManager().getTableColName(swdDomain.getObjId(), targetDateFieldId);

			String formFieldType = "date";
			String operator = "=";
			
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 6);
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			int day = cal.get(Calendar.DATE);
			String rightOperand = year + "-" + month + "-" + day + " 15:00:00";
			//String rightOperand = "2013-01-04";
			
			Filter filter = new Filter();
			filter.setLeftOperandType(formFieldType);
			filter.setLeftOperandValue(tableColName);
			filter.setOperator(operator);
			filter.setRightOperandType(formFieldType);
			
			//1주일전 스펙 메일 발송
			filter.setRightOperandValue(rightOperand);
			swdRecordCond.setFilter(new Filter[]{filter});
			
			SwdRecord[] records = SwManagerFactory.getInstance().getSwdManager().getRecords("SpecWatcher", swdRecordCond, IManager.LEVEL_LITE);
			
			if (records == null) {
				System.out.println("7일전 유효기간의 스펙이 없습니다.");
			} else {
				System.out.println("유효기간 7일전 스펙 발견!!");
				for (int i = 0; i < records.length; i++) {
					System.out.println("############################################");
					SwdRecord record = records[i];
					SwdDataField[] fields = record.getDataFields();
					System.out.println("-------------------------------------");
					for (int j = 0; j < fields.length; j++) {
						SwdDataField field = fields[j];
						System.out.println(field.getName() + " : " + field.getValue());
					}
					System.out.println("-------------------------------------");
					String targetDraftUserName = record.getDataFieldValue(targetDraftUserFieldId);
					SwdDataField userField = record.getDataField(targetDraftUserFieldId);
					System.out.println("스펙 유효기간 7일전 메일 발송 - 기안자 :" + targetDraftUserName + "("+userField.getRefRecordId()+")");
					System.out.println("############################################");
				}
				
			}
			
			Filter filter2 = new Filter();
			filter2.setLeftOperandType(formFieldType);
			filter2.setLeftOperandValue(tableColName);
			filter2.setOperator(operator);
			filter2.setRightOperandType(formFieldType);
			
			Calendar cal2 = Calendar.getInstance();
			cal2.add(Calendar.DATE, -2);
			int year2 = cal2.get(Calendar.YEAR);
			int month2 = cal2.get(Calendar.MONTH) + 1;
			int day2 = cal2.get(Calendar.DATE);
			String rightOperand2 = year2 + "-" + month2 + "-" + day2 + " 15:00:00";
			//String rightOperand = "2013-01-04";

			SwdRecordCond swdRecordCond2 = new SwdRecordCond();
			swdRecordCond2.setFormId(swdDomain.getFormId());
			swdRecordCond2.setDomainId(swdDomain.getObjId());

			//유효기간이 1일 지나 스펙의 메일 발송
			filter2.setRightOperandValue(rightOperand2);
			swdRecordCond2.setFilter(new Filter[]{filter2});
			
			SwdRecord[] records2 = SwManagerFactory.getInstance().getSwdManager().getRecords("SpecWatcher", swdRecordCond2, IManager.LEVEL_LITE);
			
			if (records2 == null) {
				System.out.println("1일 지난 유효기간의 스펙이 없습니다.");
			} else {
				System.out.println("유효기간 1일 지난 스펙 발견!!");
				for (int i = 0; i < records2.length; i++) {
					System.out.println("############################################");
					SwdRecord record = records2[i];
					SwdDataField[] fields = record.getDataFields();
					System.out.println("-------------------------------------");
					for (int j = 0; j < fields.length; j++) {
						SwdDataField field = fields[j];
						System.out.println(field.getName() + " : " + field.getValue());
					}
					System.out.println("-------------------------------------");
					String targetDraftUserName = record.getDataFieldValue(targetDraftUserFieldId);
					SwdDataField userField = record.getDataField(targetDraftUserFieldId);
					System.out.println("스펙 유효기간 1일 지난 메일 발송 - 기안자 :" + targetDraftUserName + "("+userField.getRefRecordId()+")");
					System.out.println("############################################");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
