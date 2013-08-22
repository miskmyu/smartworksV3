package net.smartworks.model.report;

import net.smartworks.model.community.User;
import net.smartworks.model.instance.info.ReportInstanceInfo;
import net.smartworks.model.security.AccessPolicy;
import net.smartworks.model.work.FormField;
import net.smartworks.model.work.Work;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartUtil;

public class MatrixReport extends ChartReport {
	
	public static final MatrixReport[] DEFAULT_MATRIXS_INFORMATION = new MatrixReport[]{};
	public static final MatrixReport[] DEFAULT_MATRIXS_PROCESS = new MatrixReport[]{};
	public static final MatrixReport[] DEFAULT_MATRIXS_SCHEDULE = new MatrixReport[]{};
	public static final MatrixReport[] DEFAULT_MATRIXS_ALL_WORKS = new MatrixReport[]{};

	private FormField xSecondAxis;
	private String xSecondAxisSelector;
	private String xSecondAxisSort;
	private FormField zSecondAxis;
	private String zSecondAxisSelector;
	private String zSecondAxisSort;
	
	
	public FormField getXSecondAxis() {
		return xSecondAxis;
	}
	public void setXSecondAxis(FormField xSecondAxis) {
		this.xSecondAxis = xSecondAxis;
	}
	public String getXSecondAxisSelector() {
		return xSecondAxisSelector;
	}
	public void setXSecondAxisSelector(String xSecondAxisSelector) {
		this.xSecondAxisSelector = xSecondAxisSelector;
	}
	public String getXSecondAxisSort() {
		return xSecondAxisSort;
	}
	public void setXSecondAxisSort(String xSecondAxisSort) {
		this.xSecondAxisSort = xSecondAxisSort;
	}
	public FormField getZSecondAxis() {
		return zSecondAxis;
	}
	public void setZSecondAxis(FormField zSecondAxis) {
		this.zSecondAxis = zSecondAxis;
	}
	public String getZSecondAxisSelector() {
		return zSecondAxisSelector;
	}
	public void setZSecondAxisSelector(String zSecondAxisSelector) {
		this.zSecondAxisSelector = zSecondAxisSelector;
	}
	public String getZSecondAxisSort() {
		return zSecondAxisSort;
	}
	public void setZSecondAxisSort(String zSecondAxisSort) {
		this.zSecondAxisSort = zSecondAxisSort;
	}

	public MatrixReport() {
		super();
	}	

	public MatrixReport(String id, String name){
		super(id, name);
	}
	public MatrixReport(String id, String name, User owner, User lastModifier,
			LocalDate lastModifiedDate) {
		super(id, name, owner, lastModifier, lastModifiedDate);
		super.setType(Report.TYPE_MATRIX);
	}

	public static ReportInstanceInfo[] getDefaultInstancesInformation(){
		if(SmartUtil.isBlankObject(MatrixReport.DEFAULT_MATRIXS_INFORMATION)) return null;
		
		ReportInstanceInfo[] instances = new ReportInstanceInfo[MatrixReport.DEFAULT_MATRIXS_INFORMATION.length];
		for(int i=0; i<MatrixReport.DEFAULT_MATRIXS_INFORMATION.length; i++){
			MatrixReport report = MatrixReport.DEFAULT_MATRIXS_INFORMATION[i];
			if(!SmartUtil.isBlankObject(report)){
				instances[i] = report.getReportInstanceInfo();
			}
		}
		return instances;
	}
	
	public static ReportInstanceInfo[] getDefaultInstancesProcess(){
		if(SmartUtil.isBlankObject(MatrixReport.DEFAULT_MATRIXS_PROCESS)) return null;
		
		ReportInstanceInfo[] instances = new ReportInstanceInfo[MatrixReport.DEFAULT_MATRIXS_PROCESS.length];
		for(int i=0; i<MatrixReport.DEFAULT_MATRIXS_PROCESS.length; i++){
			MatrixReport report = MatrixReport.DEFAULT_MATRIXS_PROCESS[i];
			if(!SmartUtil.isBlankObject(report)){
				instances[i] = report.getReportInstanceInfo();
			}
		}
		return instances;
	}
	
	public static ReportInstanceInfo[] getDefaultInstancesSchedule(){
		if(SmartUtil.isBlankObject(MatrixReport.DEFAULT_MATRIXS_SCHEDULE)) return null;
		
		ReportInstanceInfo[] instances = new ReportInstanceInfo[MatrixReport.DEFAULT_MATRIXS_SCHEDULE.length];
		for(int i=0; i<MatrixReport.DEFAULT_MATRIXS_SCHEDULE.length; i++){
			MatrixReport report = MatrixReport.DEFAULT_MATRIXS_SCHEDULE[i];
			if(!SmartUtil.isBlankObject(report)){
				instances[i] = report.getReportInstanceInfo();
			}
		}
		return instances;
	}
	
	public static ReportInstanceInfo[] getDefaultInstancesAllWorks(){
		if(SmartUtil.isBlankObject(MatrixReport.DEFAULT_MATRIXS_ALL_WORKS)) return null;
		
		ReportInstanceInfo[] instances = new ReportInstanceInfo[MatrixReport.DEFAULT_MATRIXS_ALL_WORKS.length];
		for(int i=0; i<MatrixReport.DEFAULT_MATRIXS_ALL_WORKS.length; i++){
			MatrixReport report = MatrixReport.DEFAULT_MATRIXS_ALL_WORKS[i];
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
		instance.setReportType(Report.TYPE_MATRIX);
		return instance;
	}
}
