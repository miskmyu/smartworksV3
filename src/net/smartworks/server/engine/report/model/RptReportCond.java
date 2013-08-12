package net.smartworks.server.engine.report.model;

import net.smartworks.server.engine.common.model.MisObjectCond;
import net.smartworks.server.engine.common.util.CommonUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RptReportCond extends MisObjectCond {


	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(RptReportCond.class);
	protected static final String PREFIX = "Rpt";
	
	private static final String NAME = CommonUtil.toName(RptReportCond.class, PREFIX);
	
	private static final String A_TYPE = "type";
	private static final String A_TARGETWORKID = "targetWorkId";
	private static final String A_TARGETWORKTYPE = "targetWorkType";
	private static final String A_DATASOURCETYPE = "dataSourceType";
	private static final String A_EXTERNALSERVICEID = "externalServiceId";
	private static final String A_REPORTTABLEKEY = "reportTableKey";
	private static final String A_SEARCHFILTERID = "searchFilterId";
	private static final String A_OWNER = "owner";

	private static final String A_CHARTTYPE = "chartType";
	private static final String A_XAXIS = "xAxis";
	private static final String A_XAXISSELECTOR = "xAxisSelector";
	private static final String A_XAXISSORT = "xAxisSort";
	private static final String A_XAXISMAXRECORDS = "xAxisMaxRecords";
	private static final String A_XSECONDAXIS = "xSecondAxis";
	private static final String A_XSECONDAXISSELECTOR = "xSecondAxisSelector";
	private static final String A_XSECONDAXISSORT = "xSecondAxisSort";
	private static final String A_YAXIS = "yAxis";
	private static final String A_YAXISSELECTOR = "yAxisSelector";
	private static final String A_VALUETYPE = "valueType";
	private static final String A_ZAXIS = "zAxis";
	private static final String A_ZAXISSELECTOR = "zAxisSelector";
	private static final String A_ZAXISSORT = "zAxisSort";
	private static final String A_ZSECONDAXIS = "zSecondAxis";
	private static final String A_ZSECONDAXISSELECTOR = "zSecondAxisSelector";
	private static final String A_ZSECONDAXISSORT = "zSecondAxisSort";
	
	private static final String A_WORKSPACETYPE = "workSpaceType";
	private static final String A_WORKSPACEID = "workSpaceId";
	private static final String A_ACCESSLEVEL = "accessLevel";
	private static final String A_ACCESSVALUE = "accessValue";
	

	private int type;
	private String targetWorkId;
	private int targetWorkType;
	private int dataSourceType;
	private String externalServiceId;
	private String reportTableKey;
	private String searchFilterId;
	private String owner;
	
	private int chartType;
	private String xAxis;
	private String xAxisSelector;
	private String xAxisSort;
	private int xAxisMaxRecords;
	private String xSecondAxis;
	private String xSecondAxisSelector;
	private String xSecondAxisSort;
	private String yAxis;
	private String yAxisSelector;
	private String valueType;
	private String zAxis;
	private String zAxisSelector;
	private String zAxisSort;
	private String zSecondAxis;
	private String zSecondAxisSelector;
	private String zSecondAxisSort;
	private String workSpaceType;
	private String workSpaceId;
	private String accessLevel;
	private String accessValue;
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
	public String getSearchFilterId() {
		return searchFilterId;
	}
	public void setSearchFilterId(String searchFilterId) {
		this.searchFilterId = searchFilterId;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public int getChartType() {
		return chartType;
	}
	public void setChartType(int chartType) {
		this.chartType = chartType;
	}
	public String getxAxis() {
		return xAxis;
	}
	public void setxAxis(String xAxis) {
		this.xAxis = xAxis;
	}
	public String getxAxisSelector() {
		return xAxisSelector;
	}
	public void setxAxisSelector(String xAxisSelector) {
		this.xAxisSelector = xAxisSelector;
	}
	public String getxAxisSort() {
		return xAxisSort;
	}
	public void setxAxisSort(String xAxisSort) {
		this.xAxisSort = xAxisSort;
	}
	public int getxAxisMaxRecords() {
		return xAxisMaxRecords;
	}
	public void setxAxisMaxRecords(int xAxisMaxRecords) {
		this.xAxisMaxRecords = xAxisMaxRecords;
	}
	public String getxSecondAxis() {
		return xSecondAxis;
	}
	public void setxSecondAxis(String xSecondAxis) {
		this.xSecondAxis = xSecondAxis;
	}
	public String getxSecondAxisSelector() {
		return xSecondAxisSelector;
	}
	public void setxSecondAxisSelector(String xSecondAxisSelector) {
		this.xSecondAxisSelector = xSecondAxisSelector;
	}
	public String getxSecondAxisSort() {
		return xSecondAxisSort;
	}
	public void setxSecondAxisSort(String xSecondAxisSort) {
		this.xSecondAxisSort = xSecondAxisSort;
	}
	public String getyAxis() {
		return yAxis;
	}
	public void setyAxis(String yAxis) {
		this.yAxis = yAxis;
	}
	public String getyAxisSelector() {
		return yAxisSelector;
	}
	public void setyAxisSelector(String yAxisSelector) {
		this.yAxisSelector = yAxisSelector;
	}
	public String getValueType() {
		return valueType;
	}
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}
	public String getzAxis() {
		return zAxis;
	}
	public void setzAxis(String zAxis) {
		this.zAxis = zAxis;
	}
	public String getzAxisSelector() {
		return zAxisSelector;
	}
	public void setzAxisSelector(String zAxisSelector) {
		this.zAxisSelector = zAxisSelector;
	}
	public String getzAxisSort() {
		return zAxisSort;
	}
	public void setzAxisSort(String zAxisSort) {
		this.zAxisSort = zAxisSort;
	}
	public String getzSecondAxis() {
		return zSecondAxis;
	}
	public void setzSecondAxis(String zSecondAxis) {
		this.zSecondAxis = zSecondAxis;
	}
	public String getzSecondAxisSelector() {
		return zSecondAxisSelector;
	}
	public void setzSecondAxisSelector(String zSecondAxisSelector) {
		this.zSecondAxisSelector = zSecondAxisSelector;
	}
	public String getzSecondAxisSort() {
		return zSecondAxisSort;
	}
	public void setzSecondAxisSort(String zSecondAxisSort) {
		this.zSecondAxisSort = zSecondAxisSort;
	}
	public String getWorkSpaceType() {
		return workSpaceType;
	}
	public void setWorkSpaceType(String workSpaceType) {
		this.workSpaceType = workSpaceType;
	}
	public String getWorkSpaceId() {
		return workSpaceId;
	}
	public void setWorkSpaceId(String workSpaceId) {
		this.workSpaceId = workSpaceId;
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
}
