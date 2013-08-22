package net.smartworks.model.report;

import net.smartworks.model.community.User;
import net.smartworks.model.filter.SearchFilter;
import net.smartworks.model.instance.info.ReportInstanceInfo;
import net.smartworks.model.security.AccessPolicy;
import net.smartworks.model.work.FormField;
import net.smartworks.model.work.SmartWork;
import net.smartworks.model.work.Work;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartMessage;
import net.smartworks.util.SmartUtil;

public class ChartReport extends Report {

	public static final int CHART_TYPE_LINE = 1;
	public static final int CHART_TYPE_AREA = 2;
	public static final int CHART_TYPE_BAR = 3;
	public static final int CHART_TYPE_COLUMN = 4;
	public static final int CHART_TYPE_PIE = 5;
	public static final int CHART_TYPE_GAUGE = 6;
	public static final int CHART_TYPE_RADAR = 7;
	public static final int CHART_TYPE_SCATTER = 8;
	public static final int DEFAULT_CHART_TYPE = CHART_TYPE_COLUMN;
	
	public static String[] CHART_TYPES_STRING = new String[]{"", "line", "area", "bar", "column", "pie", "gauge", "radar", "scatter"};

	public final static String STRING_LABEL_ROTATION_AUTO = "auto";
	public final static String STRING_LABEL_ROTATION_HORIZONTAL = "horizontal";
	public final static String STRING_LABEL_ROTATION_ROTATED = "rotated";
	
	public static final String CHART_PCNT_MONTHLY = "chart.pcnt.monthly";
	public static final String CHART_PCNT_MONTHLY_DEPARTMENT = "chart.pcnt.monthly.by_department";
	public static final String CHART_PMEAN_MONTHLY = "chart.pmean.monthly";
	public static final String CHART_PMEAN_MONTHLY_DEPARTMENT = "chart.pmean.monthly.by_department";
	public static final String CHART_PSUM_MONTHLY = "chart.psum.monthly";
	public static final String CHART_PSUM_MONTHLY_DEPARTMENT = "chart.psum.monthly.by_department";
	public static final String CHART_PMAX_MONTHLY = "chart.pmax.monthly";
	public static final String CHART_PMAX_MONTHLY_DEPARTMENT = "chart.pmax.monthly.by_department";
	public static final String CHART_PMIN_MONTHLY = "chart.pmin.monthly";
	public static final String CHART_PMIN_MONTHLY_DEPARTMENT = "chart.pmin.monthly.by_department";
	

	public static final ChartReport[] DEFAULT_CHARTS_INFORMATION = new ChartReport[] { getChartPCntMonthly(), getChartPCntMonthlyByDepartment()};

	public static final ChartReport[] DEFAULT_CHARTS_PROCESS = new ChartReport[] { getChartPCntMonthly(), getChartPCntMonthlyByDepartment(),
		getChartPMeanMonthly(), getChartPMeanMonthlyByDepartment(), getChartPSumMonthly(), getChartPSumMonthlyByDepartment(), getChartPMaxMonthly(),
		getChartPMaxMonthlyByDepartment(), getChartPMinMonthly(), getChartPMinMonthlyByDepartment() };
	public static final ChartReport[] DEFAULT_CHARTS_SCHEDULE = new ChartReport[] { getChartPCntMonthly(), getChartPCntMonthlyByDepartment(),
		getChartPMeanMonthly(), getChartPMeanMonthlyByDepartment(), getChartPSumMonthly(), getChartPSumMonthlyByDepartment(), getChartPMaxMonthly(),
		getChartPMaxMonthlyByDepartment(), getChartPMinMonthly(), getChartPMinMonthlyByDepartment() };
	public static final ChartReport[] DEFAULT_CHARTS_ALL_WORKS = new ChartReport[] { getChartPCntMonthly(), getChartPCntMonthlyByDepartment(),
		getChartPMeanMonthly(), getChartPMeanMonthlyByDepartment(), getChartPSumMonthly(), getChartPSumMonthlyByDepartment(), getChartPMaxMonthly(),
		getChartPMaxMonthlyByDepartment(), getChartPMinMonthly(), getChartPMinMonthlyByDepartment() };

	private int chartType = -1;
	private FormField xAxis;
	private String xAxisSelector;
	private String xAxisSort = Report.AXIS_SORT_ASCEND.getId();
	private int xAxisMaxRecords = -1;// Unlimited
	private FormField yAxis;
	private String yAxisSelector;
	private String valueType;
	private FormField zAxis;
	private String zAxisSelector;
	private String zAxisSort = Report.AXIS_SORT_ASCEND.getId();

	public int getChartType() {
		return chartType;
	}
	public String getChartTypeInString() {
		if(chartType<1 || chartType>=ChartReport.CHART_TYPES_STRING.length) return null;
		return ChartReport.CHART_TYPES_STRING[chartType];
	}
	public void setChartType(int chartType) {
		this.chartType = chartType;
	}
	public FormField getXAxis() {
		return xAxis;
	}
	public void setXAxis(FormField xAxis) {
		this.xAxis = xAxis;
	}
	public String getXAxisSelector() {
		return xAxisSelector;
	}
	public void setXAxisSelector(String xAxisSelector) {
		this.xAxisSelector = xAxisSelector;
	}
	public String getXAxisSort() {
		return xAxisSort;
	}
	public void setXAxisSort(String xAxisSort) {
		this.xAxisSort = xAxisSort;
	}
	public int getXAxisMaxRecords() {
		return xAxisMaxRecords;
	}
	public void setXAxisMaxRecords(int xAxisMaxRecords) {
		this.xAxisMaxRecords = xAxisMaxRecords;
	}
	public FormField getYAxis() {
		return yAxis;
	}
	public void setYAxis(FormField yAxis) {
		this.yAxis = yAxis;
	}
	public String getYAxisSelector() {
		return yAxisSelector;
	}
	public void setYAxisSelector(String yAxisSelector) {
		this.yAxisSelector = yAxisSelector;
	}
	public String getValueType() {
		return valueType;
	}
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}
	public FormField getZAxis() {
		return zAxis;
	}
	public void setZAxis(FormField zAxis) {
		this.zAxis = zAxis;
	}
	public String getZAxisSelector() {
		return zAxisSelector;
	}
	public void setZAxisSelector(String zAxisSelector) {
		this.zAxisSelector = zAxisSelector;
	}
	public String getZAxisSort() {
		return zAxisSort;
	}
	public void setZAxisSort(String zAxisSort) {
		this.zAxisSort = zAxisSort;
	}

	public ChartReport() {
		super();
	}

	public ChartReport(String id, String name) {
		super(id, name);
	}

	public ChartReport(String id, String name, User owner, User lastModifier, LocalDate lastModifiedDate) {
		super(id, name, Report.TYPE_CHART, owner, lastModifier, lastModifiedDate);
	}

	public static ChartReport getChartPCntMonthly() {
		ChartReport chart = new ChartReport(CHART_PCNT_MONTHLY, CHART_PCNT_MONTHLY, SmartUtil.getSystemUser(), SmartUtil.getSystemUser(),
				new LocalDate());
		chart.chartType = CHART_TYPE_BAR;
		chart.xAxis = FormField.FIELD_LAST_MODIFIED_DATE;
		chart.xAxisSelector = Report.AXIS_SELECTOR_BY_MONTH.getId();
		chart.yAxis = FormField.FIELD_LAST_MODIFIED_DATE;
		chart.valueType = Report.VALUE_TYPE_COUNT.getId();
		chart.setSearchFilter(SearchFilter.getRecent1YearInstancesFilter());
		return chart;
	}

	public static ChartReport getChartPCntMonthlyByDepartment() {
		ChartReport chart = new ChartReport(CHART_PCNT_MONTHLY_DEPARTMENT, CHART_PCNT_MONTHLY_DEPARTMENT, SmartUtil.getSystemUser(),
				SmartUtil.getSystemUser(), new LocalDate());
		chart.chartType = CHART_TYPE_BAR;
		chart.xAxis = FormField.FIELD_LAST_MODIFIED_DATE;
		chart.xAxisSelector = Report.AXIS_SELECTOR_BY_MONTH.getId();
		chart.yAxis = FormField.FIELD_LAST_MODIFIED_DATE;
		chart.valueType = Report.VALUE_TYPE_COUNT.getId();
		chart.zAxis = FormField.FIELD_LAST_MODIFIER;
		chart.zAxisSelector = Report.AXIS_SELECTOR_USER_DEPARTMENT.getId();
		chart.setSearchFilter(SearchFilter.getRecent1YearInstancesFilter());
		return chart;
	}

	public static ChartReport getChartPMeanMonthly() {
		ChartReport chart = new ChartReport(CHART_PMEAN_MONTHLY, CHART_PMEAN_MONTHLY, SmartUtil.getSystemUser(), SmartUtil.getSystemUser(),
				new LocalDate());
		chart.chartType = CHART_TYPE_COLUMN;
		chart.xAxis = FormField.FIELD_LAST_MODIFIED_DATE;
		chart.xAxisSelector = Report.AXIS_SELECTOR_BY_MONTH.getId();
		chart.yAxis = FormField.FIELD_PROCESS_TIME;
		chart.valueType = Report.VALUE_TYPE_MEAN.getId();
		chart.setSearchFilter(SearchFilter.getRecent1YearInstancesFilter());
		return chart;
	}

	public static ChartReport getChartPMeanMonthlyByDepartment() {
		ChartReport chart = new ChartReport(CHART_PMEAN_MONTHLY_DEPARTMENT, CHART_PMEAN_MONTHLY_DEPARTMENT, SmartUtil.getSystemUser(),
				SmartUtil.getSystemUser(), new LocalDate());
		chart.chartType = CHART_TYPE_COLUMN;
		chart.xAxis = FormField.FIELD_LAST_MODIFIED_DATE;
		chart.xAxisSelector = Report.AXIS_SELECTOR_BY_MONTH.getId();
		chart.yAxis = FormField.FIELD_PROCESS_TIME;
		chart.valueType = Report.VALUE_TYPE_MEAN.getId();
		chart.zAxis = FormField.FIELD_LAST_MODIFIER;
		chart.zAxisSelector = Report.AXIS_SELECTOR_USER_DEPARTMENT.getId();
		chart.setSearchFilter(SearchFilter.getRecent1YearInstancesFilter());
		return chart;
	}

	public static ChartReport getChartPSumMonthly() {
		ChartReport chart = new ChartReport(CHART_PSUM_MONTHLY, CHART_PSUM_MONTHLY, SmartUtil.getSystemUser(), SmartUtil.getSystemUser(),
				new LocalDate());
		chart.chartType = CHART_TYPE_LINE;
		chart.xAxis = FormField.FIELD_LAST_MODIFIED_DATE;
		chart.xAxisSelector = Report.AXIS_SELECTOR_BY_MONTH.getId();
		chart.yAxis = FormField.FIELD_PROCESS_TIME;
		chart.valueType = Report.VALUE_TYPE_SUM.getId();
		chart.setSearchFilter(SearchFilter.getRecent1YearInstancesFilter());
		return chart;
	}

	public static ChartReport getChartPSumMonthlyByDepartment() {
		ChartReport chart = new ChartReport(CHART_PSUM_MONTHLY_DEPARTMENT, CHART_PSUM_MONTHLY_DEPARTMENT, SmartUtil.getSystemUser(),
				SmartUtil.getSystemUser(), new LocalDate());
		chart.chartType = CHART_TYPE_LINE;
		chart.xAxis = FormField.FIELD_LAST_MODIFIED_DATE;
		chart.xAxisSelector = Report.AXIS_SELECTOR_BY_MONTH.getId();
		chart.yAxis = FormField.FIELD_PROCESS_TIME;
		chart.valueType = Report.VALUE_TYPE_SUM.getId();
		chart.zAxis = FormField.FIELD_LAST_MODIFIER;
		chart.zAxisSelector = Report.AXIS_SELECTOR_USER_DEPARTMENT.getId();
		chart.setSearchFilter(SearchFilter.getRecent1YearInstancesFilter());
		return chart;
	}

	public static ChartReport getChartPMaxMonthly() {
		ChartReport chart = new ChartReport(CHART_PMAX_MONTHLY, CHART_PMAX_MONTHLY, SmartUtil.getSystemUser(), SmartUtil.getSystemUser(),
				new LocalDate());
		chart.chartType = CHART_TYPE_LINE;
		chart.xAxis = FormField.FIELD_LAST_MODIFIED_DATE;
		chart.xAxisSelector = Report.AXIS_SELECTOR_BY_MONTH.getId();
		chart.yAxis = FormField.FIELD_PROCESS_TIME;
		chart.valueType = Report.VALUE_TYPE_MAX.getId();
		chart.setSearchFilter(SearchFilter.getRecent1YearInstancesFilter());
		return chart;
	}

	public static ChartReport getChartPMaxMonthlyByDepartment() {
		ChartReport chart = new ChartReport(CHART_PMAX_MONTHLY_DEPARTMENT, CHART_PMAX_MONTHLY_DEPARTMENT, SmartUtil.getSystemUser(),
				SmartUtil.getSystemUser(), new LocalDate());
		chart.chartType = CHART_TYPE_LINE;
		chart.xAxis = FormField.FIELD_LAST_MODIFIED_DATE;
		chart.xAxisSelector = Report.AXIS_SELECTOR_BY_MONTH.getId();
		chart.yAxis = FormField.FIELD_PROCESS_TIME;
		chart.valueType = Report.VALUE_TYPE_MAX.getId();
		chart.zAxis = FormField.FIELD_LAST_MODIFIER;
		chart.zAxisSelector = Report.AXIS_SELECTOR_USER_DEPARTMENT.getId();
		chart.setSearchFilter(SearchFilter.getRecent1YearInstancesFilter());
		return chart;
	}

	public static ChartReport getChartPMinMonthly() {
		ChartReport chart = new ChartReport(CHART_PMIN_MONTHLY, CHART_PMIN_MONTHLY, SmartUtil.getSystemUser(), SmartUtil.getSystemUser(),
				new LocalDate());
		chart.chartType = CHART_TYPE_LINE;
		chart.xAxis = FormField.FIELD_LAST_MODIFIED_DATE;
		chart.xAxisSelector = Report.AXIS_SELECTOR_BY_MONTH.getId();
		chart.yAxis = FormField.FIELD_PROCESS_TIME;
		chart.valueType = Report.VALUE_TYPE_MIN.getId();
		chart.setSearchFilter(SearchFilter.getRecent1YearInstancesFilter());
		return chart;
	}

	public static ChartReport getChartPMinMonthlyByDepartment() {
		ChartReport chart = new ChartReport(CHART_PMIN_MONTHLY_DEPARTMENT, CHART_PMIN_MONTHLY_DEPARTMENT, SmartUtil.getSystemUser(),
				SmartUtil.getSystemUser(), new LocalDate());
		chart.chartType = CHART_TYPE_LINE;
		chart.xAxis = FormField.FIELD_LAST_MODIFIED_DATE;
		chart.xAxisSelector = Report.AXIS_SELECTOR_BY_MONTH.getId();
		chart.yAxis = FormField.FIELD_PROCESS_TIME;
		chart.valueType = Report.VALUE_TYPE_MIN.getId();
		chart.zAxis = FormField.FIELD_LAST_MODIFIER;
		chart.zAxisSelector = Report.AXIS_SELECTOR_USER_DEPARTMENT.getId();
		chart.setSearchFilter(SearchFilter.getRecent1YearInstancesFilter());
		return chart;
	}
	
	public static ReportInstanceInfo[] getDefaultInstancesInformation(){
		if(SmartUtil.isBlankObject(ChartReport.DEFAULT_CHARTS_INFORMATION)) return null;
		
		ReportInstanceInfo[] instances = new ReportInstanceInfo[ChartReport.DEFAULT_CHARTS_INFORMATION.length];
		for(int i=0; i<ChartReport.DEFAULT_CHARTS_INFORMATION.length; i++){
			ChartReport report = ChartReport.DEFAULT_CHARTS_INFORMATION[i];
			if(!SmartUtil.isBlankObject(report)){
				instances[i] = report.getReportInstanceInfo();
			}
		}
		return instances;
	}
	
	public static ReportInstanceInfo[] getDefaultInstancesProcess(){
		if(SmartUtil.isBlankObject(ChartReport.DEFAULT_CHARTS_PROCESS)) return null;
		
		ReportInstanceInfo[] instances = new ReportInstanceInfo[ChartReport.DEFAULT_CHARTS_PROCESS.length];
		for(int i=0; i<ChartReport.DEFAULT_CHARTS_PROCESS.length; i++){
			ChartReport report = ChartReport.DEFAULT_CHARTS_PROCESS[i];
			if(!SmartUtil.isBlankObject(report)){
				instances[i] = report.getReportInstanceInfo();
			}
		}
		return instances;
	}
	
	public static ReportInstanceInfo[] getDefaultInstancesSchedule(){
		if(SmartUtil.isBlankObject(ChartReport.DEFAULT_CHARTS_SCHEDULE)) return null;
		
		ReportInstanceInfo[] instances = new ReportInstanceInfo[ChartReport.DEFAULT_CHARTS_SCHEDULE.length];
		for(int i=0; i<ChartReport.DEFAULT_CHARTS_SCHEDULE.length; i++){
			ChartReport report = ChartReport.DEFAULT_CHARTS_SCHEDULE[i];
			if(!SmartUtil.isBlankObject(report)){
				instances[i] = report.getReportInstanceInfo();
			}
		}
		return instances;
	}
	
	public static ReportInstanceInfo[] getDefaultInstancesAllWorks(){
		if(SmartUtil.isBlankObject(ChartReport.DEFAULT_CHARTS_ALL_WORKS)) return null;
		
		ReportInstanceInfo[] instances = new ReportInstanceInfo[ChartReport.DEFAULT_CHARTS_ALL_WORKS.length];
		for(int i=0; i<ChartReport.DEFAULT_CHARTS_ALL_WORKS.length; i++){
			ChartReport report = ChartReport.DEFAULT_CHARTS_ALL_WORKS[i];
			if(!SmartUtil.isBlankObject(report)){
				ReportInstanceInfo instance = report.getReportInstanceInfo();
				instance.setTargetWorkType(Work.TYPE_NONE);
				instances[i] = instance;
			}
		}
		return instances;
	}
		
	public static String getChartTypeInString(int chartType){
		if(chartType<1 || chartType>ChartReport.CHART_TYPES_STRING.length) return ChartReport.CHART_TYPES_STRING[ChartReport.DEFAULT_CHART_TYPE];
		return ChartReport.CHART_TYPES_STRING[chartType];
	}
	
	public ReportInstanceInfo getReportInstanceInfo(){
		ReportInstanceInfo instance = new ReportInstanceInfo();
		instance.setId(this.getId());
		instance.setSubject(this.getName());
		instance.setOwner(this.getOwner().getUserInfo());
		instance.setLastModifier(this.getLastModifier().getUserInfo());
		instance.setLastModifiedDate(this.getLastModifiedDate());
		instance.setAccessPolicy(new AccessPolicy(AccessPolicy.LEVEL_PUBLIC));
		instance.setReportType(Report.TYPE_CHART);
		instance.setChartType(this.getChartType());
		return instance;
	}
}
