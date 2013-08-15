package net.smartworks.model.report;

import net.smartworks.model.instance.info.ReportInstanceInfo;
import net.smartworks.model.security.AccessPolicy;
import net.smartworks.model.work.FormField;
import net.smartworks.model.work.Work;
import net.smartworks.util.SmartUtil;

public class TableReport extends Report {

	public static final TableReport[] DEFAULT_TABLES_INFORMATION = new TableReport[]{};
	public static final TableReport[] DEFAULT_TABLES_PROCESS = new TableReport[]{};
	public static final TableReport[] DEFAULT_TABLES_SCHEDULE = new TableReport[]{};
	public static final TableReport[] DEFAULT_TABLES_ALL_WORKS = new TableReport[]{};

	private FormField[] displayFields;
	private FormField sortingField = FormField.FIELD_CREATED_DATE;
	private boolean sortingAscend;
	private int pageSize;
	
	public FormField[] getDisplayFields() {
		return displayFields;
	}
	public void setDisplayFields(FormField[] displayFields) {
		this.displayFields = displayFields;
	}
	public FormField getSortingField() {
		return sortingField;
	}
	public void setSortingField(FormField sortingField) {
		this.sortingField = sortingField;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public boolean isSortingAscend() {
		return sortingAscend;
	}
	public void setSortingAscend(boolean sortingAscend) {
		this.sortingAscend = sortingAscend;
	}	
	
	public static ReportInstanceInfo[] getDefaultInstancesInformation(){
		if(SmartUtil.isBlankObject(TableReport.DEFAULT_TABLES_INFORMATION)) return null;
		
		ReportInstanceInfo[] instances = new ReportInstanceInfo[TableReport.DEFAULT_TABLES_INFORMATION.length];
		for(int i=0; i<TableReport.DEFAULT_TABLES_INFORMATION.length; i++){
			TableReport report = TableReport.DEFAULT_TABLES_INFORMATION[i];
			if(!SmartUtil.isBlankObject(report)){
				instances[i] = report.getReportInstanceInfo();
			}
		}
		return instances;
	}
	
	public static ReportInstanceInfo[] getDefaultInstancesProcess(){
		if(SmartUtil.isBlankObject(TableReport.DEFAULT_TABLES_PROCESS)) return null;
		
		ReportInstanceInfo[] instances = new ReportInstanceInfo[TableReport.DEFAULT_TABLES_PROCESS.length];
		for(int i=0; i<TableReport.DEFAULT_TABLES_PROCESS.length; i++){
			TableReport report = TableReport.DEFAULT_TABLES_PROCESS[i];
			if(!SmartUtil.isBlankObject(report)){
				instances[i] = report.getReportInstanceInfo();
			}
		}
		return instances;
	}
	
	public static ReportInstanceInfo[] getDefaultInstancesSchedule(){
		if(SmartUtil.isBlankObject(TableReport.DEFAULT_TABLES_SCHEDULE)) return null;
		
		ReportInstanceInfo[] instances = new ReportInstanceInfo[TableReport.DEFAULT_TABLES_SCHEDULE.length];
		for(int i=0; i<TableReport.DEFAULT_TABLES_SCHEDULE.length; i++){
			TableReport report = TableReport.DEFAULT_TABLES_SCHEDULE[i];
			if(!SmartUtil.isBlankObject(report)){
				instances[i] = report.getReportInstanceInfo();
			}
		}
		return instances;
	}
	
	public static ReportInstanceInfo[] getDefaultInstancesAllWorks(){
		if(SmartUtil.isBlankObject(TableReport.DEFAULT_TABLES_ALL_WORKS)) return null;
		
		ReportInstanceInfo[] instances = new ReportInstanceInfo[TableReport.DEFAULT_TABLES_ALL_WORKS.length];
		for(int i=0; i<TableReport.DEFAULT_TABLES_ALL_WORKS.length; i++){
			TableReport report = TableReport.DEFAULT_TABLES_ALL_WORKS[i];
			if(!SmartUtil.isBlankObject(report)){
				ReportInstanceInfo instance = report.getReportInstanceInfo();
				instance.setTargetWorkType(Work.TYPE_NONE);
				instances[i] = instance;
			}
		}
		return instances;
	}
		
	public ReportInstanceInfo getReportInstanceInfo(){
		ReportInstanceInfo instance = new ReportInstanceInfo();
		instance.setId(this.getId());
		instance.setSubject(this.getName());
		instance.setOwner(this.getOwner().getUserInfo());
		instance.setLastModifier(this.getLastModifier().getUserInfo());
		instance.setLastModifiedDate(this.getLastModifiedDate());
		instance.setAccessPolicy(new AccessPolicy(AccessPolicy.LEVEL_PUBLIC));
		instance.setReportType(Report.TYPE_TABLE);
		return instance;
	}
}
