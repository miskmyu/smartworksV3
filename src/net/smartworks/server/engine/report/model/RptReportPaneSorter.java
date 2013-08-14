package net.smartworks.server.engine.report.model;

import java.util.ArrayList;
import java.util.List;

public class RptReportPaneSorter {

	private List<RptReportPane> pane0 = new ArrayList<RptReportPane>();
	private List<RptReportPane> pane1 = new ArrayList<RptReportPane>();
	private List<RptReportPane> pane2 = new ArrayList<RptReportPane>();
	private List<RptReportPane> pane3 = new ArrayList<RptReportPane>();
	private List<RptReportPane> pane4 = new ArrayList<RptReportPane>();
	private List<RptReportPane> pane5 = new ArrayList<RptReportPane>();
	
	public void setReportPane(RptReportPane pane) throws Exception {
		
		int position = pane.getPosition();
		
		if (position < 10) {
			pane0.add(pane);
		} else if (10 <= position && position < 20) {
			pane1.add(pane);
		} else if (20 <= position && position < 30) {
			pane2.add(pane);
		} else if (30 <= position && position < 40) {
			pane3.add(pane);
		} else if (40 <= position && position < 50) {
			pane4.add(pane);
		} else if (50 <= position && position < 60) {
			pane5.add(pane);
		}
	}
	
	public void setRowSpanToRptReportPane(RptReportPane pane) throws Exception {
		//setReportPane 을 모두 마친후 실행하여 각로우의 span을 구한
		int position = pane.getPosition();
		
		if (position < 10) {
			pane.setColumnSpans(pane0.size());
		} else if (10 <= position && position < 20) {
			pane.setColumnSpans(pane1.size());
		} else if (20 <= position && position < 30) {
			pane.setColumnSpans(pane2.size());
		} else if (30 <= position && position < 40) {
			pane.setColumnSpans(pane3.size());
		} else if (40 <= position && position < 50) {
			pane.setColumnSpans(pane4.size());
		} else if (50 <= position && position < 60) {
			pane.setColumnSpans(pane5.size());
		}
	}
	
	public List<RptReportPane> getPane0() {
		return pane0;
	}
	public void setPane0(List<RptReportPane> pane0) {
		this.pane0 = pane0;
	}
	public List<RptReportPane> getPane1() {
		return pane1;
	}
	public void setPane1(List<RptReportPane> pane1) {
		this.pane1 = pane1;
	}
	public List<RptReportPane> getPane2() {
		return pane2;
	}
	public void setPane2(List<RptReportPane> pane2) {
		this.pane2 = pane2;
	}
	public List<RptReportPane> getPane3() {
		return pane3;
	}
	public void setPane3(List<RptReportPane> pane3) {
		this.pane3 = pane3;
	}
	public List<RptReportPane> getPane4() {
		return pane4;
	}
	public void setPane4(List<RptReportPane> pane4) {
		this.pane4 = pane4;
	}
	public List<RptReportPane> getPane5() {
		return pane5;
	}
	public void setPane5(List<RptReportPane> pane5) {
		this.pane5 = pane5;
	}
	
	
}
