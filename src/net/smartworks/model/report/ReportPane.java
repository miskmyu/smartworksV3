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
	public static Matrix[] getAvailablePositions(ReportPane[] reportPanes, Matrix currentPosition){
		ReportPane[] removedReportPanes = null;
		if(reportPanes!=null){
			removedReportPanes = new ReportPane[reportPanes.length - (currentPosition==null?0:1)];
			int count = 0;
			for(int i=0; i<reportPanes.length; i++){
				if(reportPanes[i].getPosition().isSamePosition(currentPosition)) continue;
				removedReportPanes[count++] = reportPanes[i];
			}
		}
		Matrix[] availablePositions = null; 
		if(SmartUtil.isBlankObject(removedReportPanes)){
			if(SmartUtil.isBlankObject(currentPosition)){
				availablePositions = new Matrix[1];
				availablePositions[0] = new Matrix(0, 0);
				availablePositions[0].setNewRow(true);
			}
			return availablePositions;
		}
		
		Matrix[] newColumnPositions = new Matrix[ReportPane.MAX_ROWS*ReportPane.MAX_COLUMNS - (SmartUtil.isBlankObject(currentPosition)?0:1)];
		int rows =0, columns=0, panes=0, lastRow=-1;
		boolean isCurrentPositionAlone = false, currentRowExist=false;
		for(int i=0; i<removedReportPanes.length; i++){
			int thisRow = removedReportPanes[i].getPosition().getRow();
			if(currentPosition!=null && thisRow == currentPosition.getRow())
				currentRowExist = true;
			
			if(thisRow==lastRow){
				columns++;
				if(i==removedReportPanes.length-1){
					if(columns<ReportPane.MAX_COLUMNS){
						if(columns!=1 && !(new Matrix(thisRow, 1)).isSamePosition(currentPosition))
							newColumnPositions[panes++] = new Matrix(thisRow, 1);
						if(!(new Matrix(thisRow, 0)).isSamePosition(currentPosition))
							newColumnPositions[panes++] = new Matrix(thisRow, 0); 
						if(!(new Matrix(thisRow, 2)).isSamePosition(currentPosition))
							newColumnPositions[panes++] = new Matrix(thisRow, 2);
					}					
				}
			}else if(thisRow>lastRow ){
				if(lastRow>-1){
					if(columns<ReportPane.MAX_COLUMNS){
						if(columns!=1 && !(new Matrix(lastRow, 1)).isSamePosition(currentPosition))
							newColumnPositions[panes++] = new Matrix(lastRow, 1);
						if(!(new Matrix(lastRow, 0)).isSamePosition(currentPosition))
							newColumnPositions[panes++] = new Matrix(lastRow, 0); 
						if(!(new Matrix(lastRow, 2)).isSamePosition(currentPosition))
							newColumnPositions[panes++] = new Matrix(lastRow, 2);
					}
				}
				if(!SmartUtil.isBlankObject(currentPosition) && currentPosition.getRow()==lastRow && columns==1){
					isCurrentPositionAlone= true;
				}
				columns = 1;
				rows++;
				lastRow = thisRow;
				if(i==removedReportPanes.length-1){
					if(columns<ReportPane.MAX_COLUMNS){
						if(columns!=1 && !(new Matrix(thisRow, 1)).isSamePosition(currentPosition))
							newColumnPositions[panes++] = new Matrix(thisRow, 1);
						if(!(new Matrix(thisRow, 0)).isSamePosition(currentPosition))
							newColumnPositions[panes++] = new Matrix(thisRow, 0); 
						if(!(new Matrix(thisRow, 2)).isSamePosition(currentPosition))
							newColumnPositions[panes++] = new Matrix(thisRow, 2);
					}					
				}
			}
		}
		
		Matrix[] newRowPositions = null;
		if(rows<ReportPane.MAX_ROWS){
			Matrix[] tempRowPositions = new Matrix[rows + 1];
			int finalRows = 0;
			for(int i=0; i<rows + 1; i++){
				if(currentPosition!=null && currentPosition.isSamePosition(new Matrix(i,0)) && (isCurrentPositionAlone || currentPosition.getRow()==rows || !currentRowExist))
					continue;
				tempRowPositions[finalRows] = new Matrix(i,0);
				tempRowPositions[finalRows].setNewRow(true);
				finalRows++;
			}
			if(finalRows!=rows+1){
				newRowPositions = new Matrix[finalRows];
				for(int i=0; i<finalRows; i++)
					newRowPositions[i] = tempRowPositions[i];
			}else{
				newRowPositions = tempRowPositions;
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
