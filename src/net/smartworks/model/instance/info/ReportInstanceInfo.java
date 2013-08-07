package net.smartworks.model.instance.info;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import net.smartworks.model.community.info.UserInfo;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.SortingField;
import net.smartworks.model.instance.WorkInstance;
import net.smartworks.model.security.AccessPolicy;
import net.smartworks.model.work.FormField;
import net.smartworks.model.work.SmartWork;
import net.smartworks.model.work.SocialWork;
import net.smartworks.model.work.WorkCategory;
import net.smartworks.service.ISmartWorks;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartUtil;

public class ReportInstanceInfo extends InstanceInfo {

	private int targetWorkType = SmartWork.TYPE_NONE;
	private int reportType = -1;
	private int chartType = -1;
	private AccessPolicy accessPolicy = new AccessPolicy();
	
	public int getTargetWorkType() {
		return targetWorkType;
	}
	public void setTargetWorkType(int targetWorkType) {
		this.targetWorkType = targetWorkType;
	}
	public int getReportType() {
		return reportType;
	}
	public void setReportType(int reportType) {
		this.reportType = reportType;
	}
	public int getChartType() {
		return chartType;
	}
	public void setChartType(int chartType) {
		this.chartType = chartType;
	}
	public AccessPolicy getAccessPolicy() {
		return accessPolicy;
	}
	public void setAccessPolicy(AccessPolicy accessPolicy) {
		this.accessPolicy = accessPolicy;
	}

	public ReportInstanceInfo() {
		super();
		super.setType(Instance.TYPE_REPORT);
	}

	public ReportInstanceInfo(String id, String subject, UserInfo owner, UserInfo lastModifier, LocalDate lastModifiedDate) {
		super(id, subject, Instance.TYPE_REPORT, owner, lastModifier, lastModifiedDate);
	}

	public String getController(){
		return WorkInstance.CONTROLLER_REPORT_SPACE;	
	}
	
	public String getContextId(){
		return ISmartWorks.CONTEXT_PREFIX_REPORT_SPACE + this.getId();
	}
	
	public static ReportInstanceInfo[] getSortedInstances(ReportInstanceInfo[] instances, SortingField sortingField){
		if(SmartUtil.isBlankObject(instances)) return instances;
		ArrayList<ReportInstanceInfo> instanceList = new ArrayList<ReportInstanceInfo>();
		for(ReportInstanceInfo instance : instances){
			instanceList.add(instance);
		}
		if(!SmartUtil.isBlankObject(sortingField)){
			if(FormField.ID_SUBJECT.equals(sortingField.getFieldId())){
				Collections.sort(instanceList, new SubjectCompare(sortingField.isAscending()));
			}else if(FormField.ID_TARGET_WORK_TYPE.equals(sortingField.getFieldId())){
				Collections.sort(instanceList, new TargetWorkTypeCompare(sortingField.isAscending()));				
			}else if(FormField.ID_REPORT_TYPE.equals(sortingField.getFieldId())){
				Collections.sort(instanceList, new ReportTypeCompare(sortingField.isAscending()));				
			}else if(FormField.ID_CHART_TYPE.equals(sortingField.getFieldId())){
				Collections.sort(instanceList, new ChartTypeCompare(sortingField.isAscending()));				
			}else if(FormField.ID_ACCESS_LEVEL.equals(sortingField.getFieldId())){
				Collections.sort(instanceList, new AccessLevelCompare(sortingField.isAscending()));				
			}else if(FormField.ID_LAST_MODIFIER.equals(sortingField.getFieldId())){
				Collections.sort(instanceList, new LastModifierCompare(sortingField.isAscending()));				
			}else if(FormField.ID_LAST_MODIFIED_DATE.equals(sortingField.getFieldId())){
				Collections.sort(instanceList, new LastModifiedDateCompare(sortingField.isAscending()));				
			}
		}
		return instanceList.toArray(new ReportInstanceInfo[instances.length]);
	}
}

class SubjectCompare implements Comparator<ReportInstanceInfo>{
	int isAsc = 1;
	public SubjectCompare(boolean isAsc){
		super();
		this.isAsc = isAsc ? 1 : -1;
	}
	
	public int compare(ReportInstanceInfo s1, ReportInstanceInfo s2){
		return s1.getSubject().compareTo(s2.getSubject())*this.isAsc;
	}
}
class TargetWorkTypeCompare implements Comparator<ReportInstanceInfo>{
	int isAsc = 1;
	public TargetWorkTypeCompare(boolean isAsc){
		super();
		this.isAsc = isAsc ? 1 : -1;
	}
	
	public int compare(ReportInstanceInfo s1, ReportInstanceInfo s2){
		return (s1.getTargetWorkType() - s2.getTargetWorkType())*this.isAsc;
	}
}
class ReportTypeCompare implements Comparator<ReportInstanceInfo>{
	int isAsc = 1;
	public ReportTypeCompare(boolean isAsc){
		super();
		this.isAsc = isAsc ? 1 : -1;
	}
	
	public int compare(ReportInstanceInfo s1, ReportInstanceInfo s2){
		return (s1.getReportType() - s2.getReportType())*this.isAsc;
	}
}
class ChartTypeCompare implements Comparator<ReportInstanceInfo>{
	int isAsc = 1;
	public ChartTypeCompare(boolean isAsc){
		super();
		this.isAsc = isAsc ? 1 : -1;
	}
	
	public int compare(ReportInstanceInfo s1, ReportInstanceInfo s2){
		return (s1.getChartType() - s2.getChartType())*this.isAsc;
	}
}
class AccessLevelCompare implements Comparator<ReportInstanceInfo>{
	int isAsc = 1;
	public AccessLevelCompare(boolean isAsc){
		super();
		this.isAsc = isAsc ? 1 : -1;
	}
	
	public int compare(ReportInstanceInfo s1, ReportInstanceInfo s2){
		if(s1.getAccessPolicy() == null ) return -1*this.isAsc;
		if(s2.getAccessPolicy() == null ) return 1*this.isAsc;
		
		return (s1.getAccessPolicy().getLevel() - s2.getAccessPolicy().getLevel())*this.isAsc;
	}
}
class LastModifierCompare implements Comparator<ReportInstanceInfo>{
	int isAsc = 1;
	public LastModifierCompare(boolean isAsc){
		super();
		this.isAsc = isAsc ? 1 : -1;
	}
	
	public int compare(ReportInstanceInfo s1, ReportInstanceInfo s2){
		if(s1.getLastModifier() == null || s1.getLastModifier().getName() == null) return -1*this.isAsc;
		if(s2.getLastModifier() == null || s2.getLastModifier().getName() == null) return 1*this.isAsc;
		return s1.getLastModifier().getName().compareTo(s2.getLastModifier().getName())*this.isAsc;
	}
}
class LastModifiedDateCompare implements Comparator<ReportInstanceInfo>{
	int isAsc = 1;
	public LastModifiedDateCompare(boolean isAsc){
		super();
		this.isAsc = isAsc ? 1 : -1;
	}
	
	public int compare(ReportInstanceInfo s1, ReportInstanceInfo s2){
		if(s1.getLastModifiedDate() == null) return -1*this.isAsc;
		if(s2.getLastModifiedDate() == null) return 1*this.isAsc;
		return (s1.getLastModifiedDate().getTime() > s2.getLastModifiedDate().getTime() ? 1 : s1.getLastModifiedDate().getTime() < s2.getLastModifiedDate().getTime() ? -1 : 0 )*this.isAsc;
	}
}

