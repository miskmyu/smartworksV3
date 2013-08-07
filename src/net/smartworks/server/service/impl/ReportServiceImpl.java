package net.smartworks.server.service.impl;

import java.util.Comparator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.info.InstanceInfoList;
import net.smartworks.model.instance.info.ReportInstanceInfo;
import net.smartworks.model.instance.info.RequestParams;
import net.smartworks.model.report.ChartReport;
import net.smartworks.model.report.Data;
import net.smartworks.model.report.Report;
import net.smartworks.model.report.ReportPane;
import net.smartworks.model.work.SmartWork;
import net.smartworks.server.service.IReportService;
import net.smartworks.util.SmartTest;
import net.smartworks.util.SmartUtil;

import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements IReportService {

	@Override
	public Report getReportById(String reportId) throws Exception {
		
		if(SmartUtil.isBlankObject(reportId) || reportId.equals(Report.REPORT_ID_NONE)) return null;

		try{
			ChartReport[] defaultInformationCharts = ChartReport.DEFAULT_CHARTS_INFORMATION;
			for(ChartReport report : defaultInformationCharts){
				if(report.getId().equals(reportId)) return report;
			}
			ChartReport[] defaultProcessCharts = ChartReport.DEFAULT_CHARTS_PROCESS;
			for(ChartReport report : defaultProcessCharts){
				if(report.getId().equals(reportId)) return report;
			}		
			ChartReport[] defaultShceduleCharts = ChartReport.DEFAULT_CHARTS_SCHEDULE;
			for(ChartReport report : defaultShceduleCharts){
				if(report.getId().equals(reportId)) return report;
			}
			
			// TO DO 리포트 아이디로 리포트 정보를 가져다 주는 코드 구현 필요
			// TO DO 리포트 아이디로 리포트 정보를 가져다 주는 코드 구현 필요
			return SmartTest.getReportById();
			// TO DO 리포트 아이디로 리포트 정보를 가져다 주는 코드 구현 필요
			// TO DO 리포트 아이디로 리포트 정보를 가져다 주는 코드 구현 필요
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}
	
	@Override
	public Data getReportDataByDef(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		
		return SmartTest.getReportData3();
//		try{
//			String dbType = "ORACLE";
//			return SwManagerFactory.getInstance().getReportManager().getReportData(dbType, requestBody);
//		}catch (Exception e){
//			// Exception Handling Required
//			e.printStackTrace();
//			return null;			
//			// Exception Handling Required			
//		}
	}

	@Override
	public Data getReportData(HttpServletRequest request) throws Exception {
		
		System.out.println(request.getParameter("reportId"));
		
		return SmartTest.getReportData4();
	}

	@Override
	public String setWorkReport(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String setWorkReportPane(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getUserReportCount(String targetWorkId) throws Exception {
		return SmartTest.getUserReportCount(targetWorkId);
	}

	@Override
	public InstanceInfoList getReportInstanceList(String targetWorkId, int targetWorkType, String producedBy, RequestParams params) throws Exception {
		if(Report.PRODUCED_BY_SMARTWORKS.equals(producedBy)){
			ReportInstanceInfo[] instances = null;
			if(SmartWork.ID_ALL_WORKS.equals(targetWorkId)){
				instances = ChartReport.getDefaultChartInstanceAllWorks();
			}else{
				switch(targetWorkType){
				case SmartWork.TYPE_INFORMATION:
					instances = ChartReport.getDefaultChartInstanceInformation();
					break;
				case SmartWork.TYPE_PROCESS:
					instances = ChartReport.getDefaultChartInstanceProcess();
					break;
				case SmartWork.TYPE_SCHEDULE:
					instances = ChartReport.getDefaultChartInstanceSchedule();
					break;
				}
			}
			InstanceInfoList list = new InstanceInfoList();
			if(!SmartUtil.isBlankObject(instances)){
				list.setInstanceDatas(ReportInstanceInfo.getSortedInstances(instances, params.getSortingField()));
				list.setCurrentPage(1);
				list.setPageSize(100);
				list.setTotalPages(1);
				list.setTotalSize(instances.length);
				list.setType(Instance.TYPE_REPORT);
				list.setSortedField(params.getSortingField());
			}
			return list;
		}
		return SmartTest.getReportInstanceList(targetWorkId, params);
	}

	@Override
	public ReportPane[] getMyDashboard() throws Exception {
		return SmartTest.getMyDashboard();
	}
}
