package net.smartworks.model.report;

import net.smartworks.model.BaseObject;
import net.smartworks.model.KeyMap;
import net.smartworks.model.community.User;
import net.smartworks.model.filter.SearchFilter;
import net.smartworks.model.instance.info.ReportInstanceInfo;
import net.smartworks.model.security.AccessPolicy;
import net.smartworks.model.work.SmartWork;
import net.smartworks.model.work.Work;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartMessage;
import net.smartworks.util.SmartUtil;

public class Report extends BaseObject {

	public final static int TYPE_CHART = 1;
	public final static int TYPE_MATRIX = 2;
	public final static int TYPE_TABLE = 3;
	
	public final static int DATA_SOURCE_WORKS = 1;
	public final static int DATA_SOURCE_REPORT_TABLE = 2;
	public final static int DATA_SOURCE_EXTERNAL_SERVICE = 3;
	public final static int DATA_SOURCE_DEFAULT = Report.DATA_SOURCE_WORKS;
	
	public final static String PRODUCED_BY_SMARTWORKS = "smartworks";
	public final static String PRODUCED_BY_USER = "user";
		
	public final static String REPORT_ID_NONE = "none";
	
	public static final KeyMap VALUE_TYPE_COUNT = new KeyMap("count", "report.value.type.count");
	public static final KeyMap VALUE_TYPE_SUM = new KeyMap("sum", "report.value.type.sum");
	public static final	KeyMap VALUE_TYPE_MEAN = new KeyMap("mean", "report.value.type.mean");
	public static final KeyMap VALUE_TYPE_MIN = new KeyMap("min", "report.value.type.min");
	public static final KeyMap VALUE_TYPE_MAX = new KeyMap("max", "report.value.type.max");
	public static final KeyMap[] VALUE_TYPES = new KeyMap[] {VALUE_TYPE_COUNT, VALUE_TYPE_SUM, VALUE_TYPE_MEAN, VALUE_TYPE_MIN, VALUE_TYPE_MAX};

	public static final KeyMap AXIS_SELECTOR_BY_HOUR = new KeyMap("byHour", "report.axis.date.by_hour");
	public static final KeyMap AXIS_SELECTOR_BY_DAY = new KeyMap("byDay", "report.axis.date.by_day");
	public static final KeyMap AXIS_SELECTOR_BY_WEEK = new KeyMap("byWeek", "report.axis.date.by_week");
	public static final KeyMap AXIS_SELECTOR_BY_MONTH = new KeyMap("byMonth", "report.axis.date.by_month");
	public static final KeyMap AXIS_SELECTOR_BY_QUARTER = new KeyMap("byQuarter", "report.axis.date.by_quarter");
	public static final KeyMap AXIS_SELECTOR_BY_HALF_YEAR = new KeyMap("byHalfYear", "report.axis.date.by_half_year");
	public static final KeyMap AXIS_SELECTOR_BY_YEAR = new KeyMap("byYear", "report.axis.date.by_year");
	public static final KeyMap[] AXIS_SELECTORS_DATE = new KeyMap[] {AXIS_SELECTOR_BY_HOUR, AXIS_SELECTOR_BY_DAY, AXIS_SELECTOR_BY_WEEK,
																	AXIS_SELECTOR_BY_MONTH, AXIS_SELECTOR_BY_QUARTER, AXIS_SELECTOR_BY_HALF_YEAR, AXIS_SELECTOR_BY_YEAR};
	
	public static final KeyMap AXIS_SELECTOR_USER_NAME = new KeyMap("userName", "report.axis.user.name");
	public static final KeyMap AXIS_SELECTOR_USER_DEPARTMENT = new KeyMap("userDepartment", "report.axis.user.department");
	public static final KeyMap AXIS_SELECTOR_USER_POSITION = new KeyMap("userPosition", "report.axis.user.position");
	public static final KeyMap AXIS_SELECTOR__LEVEL = new KeyMap("userLevel", "report.axis.user.level");
	public static final KeyMap AXIS_SELECTOR_USER_LOCALE = new KeyMap("userLocale", "report.axis.user.locale");
	public static final KeyMap[] AXIS_SELECTORS_USER = new KeyMap[] {AXIS_SELECTOR_USER_NAME, AXIS_SELECTOR_USER_DEPARTMENT, AXIS_SELECTOR_USER_POSITION,
																	AXIS_SELECTOR__LEVEL, AXIS_SELECTOR_USER_LOCALE};

	public static final KeyMap AXIS_SORT_ASCEND = new KeyMap("ascend", "report.axis.sort.ascend");
	public static final KeyMap AXIS_SORT_DESCEND = new KeyMap("descend", "report.axis.sort.descend");

	private int type=-1;//리포트타입(차트, 메트릭스), 테이블 제
	private String targetWorkId = SmartWork.ID_ALL_WORKS; // 업무에 들어가서 만들면 workId, 대쉬보드에서 만들때 모든 업무, 해당 업무 선택 가
	private int targetWorkType = Work.TYPE_NONE; //모든 업무일때 정보관리, 프로세스, 간트.., none 이라면 모든 업
	private int dataSourceType = Report.DATA_SOURCE_DEFAULT; //스마트웍스, 인터페이스테이블, 웹서비
	private String externalServiceId;
	private String reportTableKey; 
	private SearchFilter searchFilter;
	private AccessPolicy accessPolicy = new AccessPolicy(); //공개 , 비공
	private User owner;
	private LocalDate createdDate;
	private User lastModifier;
	private LocalDate lastModifiedDate;

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTargetWorkId() {
		return targetWorkId;
	}
	public void setTargetWorkId(String targetWorkId) {
		this.targetWorkId = targetWorkId;
	}
	public int getTargetWorkType() {
		return targetWorkType;
	}
	public void setTargetWorkType(int targetWorkType) {
		this.targetWorkType = targetWorkType;
	}
	public int getDataSourceType() {
		return dataSourceType;
	}
	public void setDataSourceType(int dataSourceType) {
		this.dataSourceType = dataSourceType;
	}
	public String getExternalServiceId() {
		return externalServiceId;
	}
	public void setExternalServiceId(String externalServiceId) {
		this.externalServiceId = externalServiceId;
	}
	public String getReportTableKey() {
		return reportTableKey;
	}
	public void setReportTableKey(String reportTableKey) {
		this.reportTableKey = reportTableKey;
	}
	public SearchFilter getSearchFilter() {
		return searchFilter;
	}
	public void setSearchFilter(SearchFilter searchFilter) {
		this.searchFilter = searchFilter;
	}
	public AccessPolicy getAccessPolicy() {
		return accessPolicy;
	}
	public void setAccessPolicy(AccessPolicy accessPolicy) {
		this.accessPolicy = accessPolicy;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	public User getLastModifier() {
		return lastModifier;
	}
	public LocalDate getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}
	public void setLastModifier(User lastModifier) {
		this.lastModifier = lastModifier;
	}
	public LocalDate getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(LocalDate lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
	public Report() {
		super();
	}
	
	public Report(String id, String name){
		super(id, name);
	}

	public Report(String id, String name, int type, User owner, User lastModifier,
			LocalDate lastModifiedDate) {
		super(id, name);
		this.type = type;
		this.owner = owner;
		this.lastModifier = lastModifier;
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getName(){
		if(this.isSystemReport()){
			return SmartMessage.getString(super.getName());
		}
		return super.getName();
	}
	
	public boolean isSystemReport(){
		return Report.isSystemReport(this.getId(), this.getType());
	}
	
	private static boolean isSystemReport(String reportId, int type){
		if(SmartUtil.isBlankObject(reportId)) return false;
		switch(type){
		case Report.TYPE_CHART:
			for(ChartReport report : ChartReport.DEFAULT_CHARTS_INFORMATION)
				if(report.getId().equals(reportId)) return true;
			for(ChartReport report : ChartReport.DEFAULT_CHARTS_PROCESS)
				if(report.getId().equals(reportId)) return true;
			for(ChartReport report : ChartReport.DEFAULT_CHARTS_SCHEDULE)
				if(report.getId().equals(reportId)) return true;
			for(ChartReport report : ChartReport.DEFAULT_CHARTS_ALL_WORKS)
				if(report.getId().equals(reportId)) return true;
			break;
		case Report.TYPE_MATRIX:
			for(MatrixReport report : MatrixReport.DEFAULT_MATRIXS_INFORMATION)
				if(report.getId().equals(reportId)) return true;
			for(MatrixReport report : MatrixReport.DEFAULT_MATRIXS_PROCESS)
				if(report.getId().equals(reportId)) return true;
			for(MatrixReport report : MatrixReport.DEFAULT_MATRIXS_SCHEDULE)
				if(report.getId().equals(reportId)) return true;
			for(MatrixReport report : MatrixReport.DEFAULT_MATRIXS_ALL_WORKS)
				if(report.getId().equals(reportId)) return true;
			break;
		case Report.TYPE_TABLE:
			for(TableReport report : TableReport.DEFAULT_TABLES_INFORMATION)
				if(report.getId().equals(reportId)) return true;
			for(TableReport report : TableReport.DEFAULT_TABLES_PROCESS)
				if(report.getId().equals(reportId)) return true;
			for(TableReport report : TableReport.DEFAULT_TABLES_SCHEDULE)
				if(report.getId().equals(reportId)) return true;
			for(TableReport report : TableReport.DEFAULT_TABLES_ALL_WORKS)
				if(report.getId().equals(reportId)) return true;
			break;
		default:
			for(ChartReport report : ChartReport.DEFAULT_CHARTS_INFORMATION)
				if(report.getId().equals(reportId)) return true;
			for(ChartReport report : ChartReport.DEFAULT_CHARTS_PROCESS)
				if(report.getId().equals(reportId)) return true;
			for(ChartReport report : ChartReport.DEFAULT_CHARTS_SCHEDULE)
				if(report.getId().equals(reportId)) return true;
			for(ChartReport report : ChartReport.DEFAULT_CHARTS_ALL_WORKS)
				if(report.getId().equals(reportId)) return true;
			for(MatrixReport report : MatrixReport.DEFAULT_MATRIXS_INFORMATION)
				if(report.getId().equals(reportId)) return true;
			for(MatrixReport report : MatrixReport.DEFAULT_MATRIXS_PROCESS)
				if(report.getId().equals(reportId)) return true;
			for(MatrixReport report : MatrixReport.DEFAULT_MATRIXS_SCHEDULE)
				if(report.getId().equals(reportId)) return true;
			for(MatrixReport report : MatrixReport.DEFAULT_MATRIXS_ALL_WORKS)
				if(report.getId().equals(reportId)) return true;
			for(TableReport report : TableReport.DEFAULT_TABLES_INFORMATION)
				if(report.getId().equals(reportId)) return true;
			for(TableReport report : TableReport.DEFAULT_TABLES_PROCESS)
				if(report.getId().equals(reportId)) return true;
			for(TableReport report : TableReport.DEFAULT_TABLES_SCHEDULE)
				if(report.getId().equals(reportId)) return true;
			for(TableReport report : TableReport.DEFAULT_TABLES_ALL_WORKS)
				if(report.getId().equals(reportId)) return true;
			break;
		}
		return false;
		
	}
	
	public static boolean isSystemReport(String reportId){
		return Report.isSystemReport(reportId, -1);
	}
	
	public static ReportInstanceInfo getSystemReportById(String reportId){
		if(SmartUtil.isBlankObject(reportId)) return null;
		for(ReportInstanceInfo instance : Report.getDefaultInstancesAllWorks())
			if(instance.getId().equals(reportId)) return instance;
		for(ReportInstanceInfo instance : Report.getDefaultInstancesInformation())
			if(instance.getId().equals(reportId)) return instance;
		for(ReportInstanceInfo instance : Report.getDefaultInstancesProcess())
			if(instance.getId().equals(reportId)) return instance;
		for(ReportInstanceInfo instance : Report.getDefaultInstancesSchedule())
			if(instance.getId().equals(reportId)) return instance;
		return null;
	}

	public static ReportInstanceInfo[] getDefaultInstancesAllWorks(){
		ReportInstanceInfo[] chartInstances = ChartReport.getDefaultInstancesAllWorks();;
		ReportInstanceInfo[] matrixInstances = MatrixReport.getDefaultInstancesAllWorks();
		ReportInstanceInfo[] tableInstances = TableReport.getDefaultInstancesAllWorks();
		
		int chartCount = chartInstances==null?0:chartInstances.length;
		int matrixCount = matrixInstances==null?0:matrixInstances.length;
		int tableCount = tableInstances==null?0:tableInstances.length;
		ReportInstanceInfo[] defaultInstances = new ReportInstanceInfo[chartCount+matrixCount+tableCount];
		int defaultCount = 0;
		for(int i=0; i<chartCount; i++)
			defaultInstances[defaultCount++] = chartInstances[i];
		for(int i=0; i<matrixCount; i++)
			defaultInstances[defaultCount++] = matrixInstances[i];
		for(int i=0; i<tableCount; i++)
			defaultInstances[defaultCount++] = tableInstances[i];
		return defaultInstances;
		
	}
	
	public static ReportInstanceInfo[] getDefaultInstancesInformation(){
		ReportInstanceInfo[] chartInstances = ChartReport.getDefaultInstancesInformation();;
		ReportInstanceInfo[] matrixInstances = MatrixReport.getDefaultInstancesInformation();
		ReportInstanceInfo[] tableInstances = TableReport.getDefaultInstancesInformation();
		
		int chartCount = chartInstances==null?0:chartInstances.length;
		int matrixCount = matrixInstances==null?0:matrixInstances.length;
		int tableCount = tableInstances==null?0:tableInstances.length;
		ReportInstanceInfo[] defaultInstances = new ReportInstanceInfo[chartCount+matrixCount+tableCount];
		int defaultCount = 0;
		for(int i=0; i<chartCount; i++)
			defaultInstances[defaultCount++] = chartInstances[i];
		for(int i=0; i<matrixCount; i++)
			defaultInstances[defaultCount++] = matrixInstances[i];
		for(int i=0; i<tableCount; i++)
			defaultInstances[defaultCount++] = tableInstances[i];
		return defaultInstances;
		
	}
	
	public static ReportInstanceInfo[] getDefaultInstancesProcess(){
		ReportInstanceInfo[] chartInstances = ChartReport.getDefaultInstancesProcess();;
		ReportInstanceInfo[] matrixInstances = MatrixReport.getDefaultInstancesProcess();
		ReportInstanceInfo[] tableInstances = TableReport.getDefaultInstancesProcess();
		
		int chartCount = chartInstances==null?0:chartInstances.length;
		int matrixCount = matrixInstances==null?0:matrixInstances.length;
		int tableCount = tableInstances==null?0:tableInstances.length;
		ReportInstanceInfo[] defaultInstances = new ReportInstanceInfo[chartCount+matrixCount+tableCount];
		int defaultCount = 0;
		for(int i=0; i<chartCount; i++)
			defaultInstances[defaultCount++] = chartInstances[i];
		for(int i=0; i<matrixCount; i++)
			defaultInstances[defaultCount++] = matrixInstances[i];
		for(int i=0; i<tableCount; i++)
			defaultInstances[defaultCount++] = tableInstances[i];
		return defaultInstances;
		
	}
	
	public static ReportInstanceInfo[] getDefaultInstancesSchedule(){
		ReportInstanceInfo[] chartInstances = ChartReport.getDefaultInstancesSchedule();;
		ReportInstanceInfo[] matrixInstances = MatrixReport.getDefaultInstancesSchedule();
		ReportInstanceInfo[] tableInstances = TableReport.getDefaultInstancesSchedule();
		
		int chartCount = chartInstances==null?0:chartInstances.length;
		int matrixCount = matrixInstances==null?0:matrixInstances.length;
		int tableCount = tableInstances==null?0:tableInstances.length;
		ReportInstanceInfo[] defaultInstances = new ReportInstanceInfo[chartCount+matrixCount+tableCount];
		int defaultCount = 0;
		for(int i=0; i<chartCount; i++)
			defaultInstances[defaultCount++] = chartInstances[i];
		for(int i=0; i<matrixCount; i++)
			defaultInstances[defaultCount++] = matrixInstances[i];
		for(int i=0; i<tableCount; i++)
			defaultInstances[defaultCount++] = tableInstances[i];
		return defaultInstances;
		
	}
	
	
	public static ReportInstanceInfo[] getSystemReportInstances(String targetWorkId, int targetWorkType){
		ReportInstanceInfo[] instances = null;
		if(SmartWork.ID_ALL_WORKS.equals(targetWorkId)){
			instances = Report.getDefaultInstancesAllWorks();
		}else{
			switch(targetWorkType){
			case SmartWork.TYPE_INFORMATION:
				instances = Report.getDefaultInstancesInformation();
				break;
			case SmartWork.TYPE_PROCESS:
				instances = Report.getDefaultInstancesProcess();
				break;
			case SmartWork.TYPE_SCHEDULE:
				instances = Report.getDefaultInstancesSchedule();
				break;
			}
		}
		return instances;
	}
}
