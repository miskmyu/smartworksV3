package net.smartworks.server.engine.report.model;

import net.smartworks.server.engine.common.model.MisObject;
import net.smartworks.server.engine.common.util.CommonUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RptReportPane extends MisObject {


	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(RptReportPane.class);
	protected static final String PREFIX = "Rpt";
	
	private static final String NAME = CommonUtil.toName(RptReportPane.class, PREFIX);
	
	private int columnSpans;
	private int position;
	private String reportId;
	private String reportName;
	private int reportType;
	private String targetWorkId;
	private int chartType;
	private boolean isChartView;
	private boolean isStacked;
	private boolean showLegend;
	private String stringLabelRotation;
	private String owner;
	
	public int getColumnSpans() {
		return columnSpans;
	}
	public void setColumnSpans(int columnSpans) {
		this.columnSpans = columnSpans;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public int getReportType() {
		return reportType;
	}
	public void setReportType(int reportType) {
		this.reportType = reportType;
	}
	public String getTargetWorkId() {
		return targetWorkId;
	}
	public void setTargetWorkId(String targetWorkId) {
		this.targetWorkId = targetWorkId;
	}
	public int getChartType() {
		return chartType;
	}
	public void setChartType(int chartType) {
		this.chartType = chartType;
	}
	public boolean isChartView() {
		return isChartView;
	}
	public boolean getIsChartView() {
		return isChartView;
	}
	public void setChartView(boolean isChartView) {
		this.isChartView = isChartView;
	}
	public void setIsChartView(boolean isChartView) {
		this.isChartView = isChartView;
	}
	public boolean isStacked() {
		return isStacked;
	}
	public boolean getIsStacked() {
		return isStacked;
	}
	public void setStacked(boolean isStacked) {
		this.isStacked = isStacked;
	}
	public void setIsStacked(boolean isStacked) {
		this.isStacked = isStacked;
	}
	public boolean isShowLegend() {
		return showLegend;
	}
	public boolean getShowLegend() {
		return showLegend;
	}
	public void setShowLegend(boolean showLegend) {
		this.showLegend = showLegend;
	}
	public String getStringLabelRotation() {
		return stringLabelRotation;
	}
	public void setStringLabelRotation(String stringLabelRotation) {
		this.stringLabelRotation = stringLabelRotation;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
}
