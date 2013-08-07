package net.smartworks.model.report;

import net.smartworks.model.BaseObject;
import net.smartworks.model.KeyMap;
import net.smartworks.model.Matrix;
import net.smartworks.model.community.User;
import net.smartworks.model.filter.SearchFilter;
import net.smartworks.model.security.AccessPolicy;
import net.smartworks.model.work.Work;
import net.smartworks.model.work.info.SmartWorkInfo;
import net.smartworks.model.work.info.WorkInfo;
import net.smartworks.util.LocalDate;
import net.smartworks.util.SmartMessage;
import net.smartworks.util.SmartUtil;

public class ReportPane extends BaseObject {

	public final static int MAX_ROWS = 6;
	public final static int MAX_COLUMNS = 3;
	
	public final static int COLUMN_LEFT = 0;
	public final static int COLUMN_CENTER = 1;
	public final static int COLUMN_RIGHT = 2;
	
	private int columnSpans = 1;
	private Matrix position;
	private String reportId;
	private String reportName;
	private int	reportType;
	private SmartWorkInfo targetWork;
	private int chartType;
	private boolean isChartView = true;
	private boolean isStacked = false;
	private boolean showLegend = true;
	private String stringLabelRotation = ChartReport.STRING_LABEL_ROTATION_AUTO; 
	
	public int getColumnSpans() {
		return columnSpans;
	}
	public void setColumnSpans(int columnSpans) {
		this.columnSpans = columnSpans;
	}
	public Matrix getPosition() {
		return position;
	}
	public void setPosition(Matrix position) {
		this.position = position;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
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
	public boolean isChartView() {
		return isChartView;
	}
	public void setChartView(boolean isChartView) {
		this.isChartView = isChartView;
	}
	public boolean isStacked() {
		return isStacked;
	}
	public void setStacked(boolean isStacked) {
		this.isStacked = isStacked;
	}
	public boolean isShowLegend() {
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
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public SmartWorkInfo getTargetWork() {
		return targetWork;
	}
	public void setTargetWork(SmartWorkInfo targetWork) {
		this.targetWork = targetWork;
	}
	public ReportPane() {
		super();
	}
	public ReportPane(String id, String name){
		super(id, name);
	}
	public static Matrix[] getAvailablePositions(ReportPane[] reportPanes, Matrix removePosition){
		if(reportPanes==null) return null;
		ReportPane[] removedReportPanes = new ReportPane[reportPanes.length - (removePosition==null?0:1)];
		int count = 0;
		for(int i=0; i<reportPanes.length; i++){
			if(reportPanes[i].getPosition().isSamePosition(removePosition)) continue;
			removedReportPanes[count++] = reportPanes[i];
		}
		return ReportPane.getAvailablePositions(removedReportPanes);
	}
	public static Matrix[] getAvailablePositions(ReportPane[] reportPanes){

		Matrix[] availablePositions = null; 
		if(SmartUtil.isBlankObject(reportPanes)){
			availablePositions = new Matrix[ReportPane.MAX_ROWS*ReportPane.MAX_COLUMNS];
			int count=0;
			for(int i=0; i<ReportPane.MAX_ROWS; i++)
				for(int j=0; j<ReportPane.MAX_COLUMNS; j++){
					availablePositions[count] = new Matrix(i, j);
					availablePositions[count++].setNewRow(true);
				}
			return availablePositions;
		}
		
		Matrix[] newColumnPositions = new Matrix[ReportPane.MAX_ROWS*ReportPane.MAX_COLUMNS];
		int rows =0, columns=0, panes=0, lastRow=-1;
		for(int i=0; i<reportPanes.length; i++){
			int thisRow = reportPanes[i].getPosition().getRow();
			int thisColumn = reportPanes[i].getPosition().getColumn();
			
			if(thisRow==lastRow){
				columns++;
				if(i==reportPanes.length-1){
					if(columns<ReportPane.MAX_COLUMNS){
						if(columns!=1){
							newColumnPositions[panes++] = new Matrix(rows-1, 1);
						}
						newColumnPositions[panes++] = new Matrix(rows-1, 0); 
						newColumnPositions[panes++] = new Matrix(rows-1, 2);
					}					
				}
			}else if(thisRow>lastRow ){
				if(lastRow>-1){
					if(columns<ReportPane.MAX_COLUMNS){
						if(columns!=1){
							newColumnPositions[panes++] = new Matrix(rows-1, 1);
						}
						newColumnPositions[panes++] = new Matrix(rows-1, 0); 
						newColumnPositions[panes++] = new Matrix(rows-1, 2);
					}
				}
				columns = 1;
				rows++;
				lastRow = thisRow;
				if(i==reportPanes.length-1){
					if(columns<ReportPane.MAX_COLUMNS){
						if(columns!=1){
							newColumnPositions[panes++] = new Matrix(rows-1, 1);
						}
						newColumnPositions[panes++] = new Matrix(rows-1, 0); 
						newColumnPositions[panes++] = new Matrix(rows-1, 2);
					}					
				}
			}
		}
		
		Matrix[] newRowPositions = null;
		if(rows<ReportPane.MAX_ROWS){
			newRowPositions = new Matrix[ReportPane.MAX_ROWS];
			for(int i=0; i<ReportPane.MAX_ROWS; i++){
				newRowPositions[i] = new Matrix(i,0);
				newRowPositions[i].setNewRow(true);
			}
		}
		
		int availables = panes + ((newRowPositions==null)?0:newRowPositions.length);
		if(availables>0){
			int count = 0;
			availablePositions = new Matrix[availables];
			for(int i=0; i<panes; i++)
				availablePositions[count++] = newColumnPositions[i];
			if(newRowPositions!=null){
				for(int i=0; i<newRowPositions.length; i++)
					availablePositions[count++] = newRowPositions[i];				
			}
		}
		return availablePositions;
	}
}
