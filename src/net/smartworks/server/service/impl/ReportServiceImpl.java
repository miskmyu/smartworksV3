package net.smartworks.server.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.ManagerFactoryParameters;
import javax.servlet.http.HttpServletRequest;

import net.smartworks.model.Matrix;
import net.smartworks.model.community.User;
import net.smartworks.model.instance.Instance;
import net.smartworks.model.instance.info.InstanceInfoList;
import net.smartworks.model.instance.info.ReportInstanceInfo;
import net.smartworks.model.instance.info.RequestParams;
import net.smartworks.model.report.ChartReport;
import net.smartworks.model.report.Data;
import net.smartworks.model.report.MatrixReport;
import net.smartworks.model.report.Report;
import net.smartworks.model.report.ReportPane;
import net.smartworks.model.security.AccessPolicy;
import net.smartworks.model.work.FormField;
import net.smartworks.model.work.SmartWork;
import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.model.Order;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.infowork.form.model.SwfForm;
import net.smartworks.server.engine.infowork.form.model.SwfFormCond;
import net.smartworks.server.engine.infowork.form.model.SwfFormFieldDef;
import net.smartworks.server.engine.pkg.model.PkgPackage;
import net.smartworks.server.engine.pkg.model.PkgPackageCond;
import net.smartworks.server.engine.report.manager.IReportManager;
import net.smartworks.server.engine.report.model.RptReport;
import net.smartworks.server.engine.report.model.RptReportCond;
import net.smartworks.server.engine.report.model.RptReportPane;
import net.smartworks.server.engine.report.model.RptReportPaneCond;
import net.smartworks.server.engine.report.model.RptReportPaneSorter;
import net.smartworks.server.service.IReportService;
import net.smartworks.server.service.util.ModelConverter;
import net.smartworks.util.LocalDate;
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
			//return SmartTest.getReportById();
			return getUserReportById(reportId);
			// TO DO 리포트 아이디로 리포트 정보를 가져다 주는 코드 구현 필요
			// TO DO 리포트 아이디로 리포트 정보를 가져다 주는 코드 구현 필요
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}
	}

	private FormField getFormFieldByValue(String value, Map<String, SwfFormFieldDef> fieldMap) throws Exception {
		
		if (CommonUtil.isEmpty(value))
			return null;
		
		FormField result = FormField.FORM_FIELD_MAP.get(value);
		if (CommonUtil.isEmpty(result)) {
			
			SwfFormFieldDef field = fieldMap.get(value);
			if (field == null)
				return null;
			
			FormField formField = new FormField(field.getId(), field.getName(), field.getViewingType());
			return formField;
		} else {
			return result;
		}
	}
	
	
	private Report getReportByRptReport(RptReport report) throws Exception {
		
		int reportType = report.getType();
		Map<String, SwfFormFieldDef> fieldInfoMap = null;
		String targetWorkId = report.getTargetWorkId();
		if (!CommonUtil.isEmpty(targetWorkId) && !targetWorkId.equalsIgnoreCase(SmartWork.ID_ALL_WORKS)) {
			
			PkgPackageCond cond = new PkgPackageCond();
			cond.setPackageId(targetWorkId);
			PkgPackage pkg = SwManagerFactory.getInstance().getPkgManager().getPackage("", cond, IManager.LEVEL_ALL);
			if (!CommonUtil.isEmpty(pkg)) {
				
				String pkgType = pkg.getType();
				if (pkgType.equalsIgnoreCase("PROCESS")) {
					
					
				} else if (pkgType.equalsIgnoreCase("SINGLE")) {
					
					SwfFormCond formCond = new SwfFormCond();
					formCond.setPackageId(targetWorkId);
					SwfForm[] forms = SwManagerFactory.getInstance().getSwfManager().getForms("", formCond, IManager.LEVEL_LITE);
					
					if (!CommonUtil.isEmpty(forms)) {
						String formId = forms[0].getId();
						
						List<SwfFormFieldDef>  formFieldDefs = SwManagerFactory.getInstance().getSwfManager().findFormFieldByForm(formId, false);
						
						if (!CommonUtil.isEmpty(formFieldDefs)) {
							fieldInfoMap = new HashMap<String, SwfFormFieldDef>();
							for (SwfFormFieldDef field : formFieldDefs) {
								fieldInfoMap.put(field.getId(), field);
							}
						}
					}
				}
			}
		}
		
		Report result = null;
		if (reportType == Report.TYPE_CHART) {
			User owner = ModelConverter.getUserByUserId(report.getOwner());
			User lastModifier = report.getOwner().equalsIgnoreCase(report.getModificationUser()) ? owner : ModelConverter.getUserByUserId(report.getModificationUser());
			ChartReport chartResult = new ChartReport(report.getObjId(), report.getName(), owner, lastModifier, new LocalDate(report.getModificationDate().getTime()));
			
			chartResult.setAccessPolicy(new AccessPolicy(Integer.parseInt(report.getAccessLevel())));
			chartResult.setChartType(report.getChartType());
			chartResult.setCreatedDate(new LocalDate(report.getCreationDate().getTime()));
			chartResult.setDataSourceType(report.getDataSourceType());
			chartResult.setExternalServiceId(report.getExternalServiceId());
			chartResult.setReportTableKey(report.getReportTableKey());
			//TODO
			chartResult.setSearchFilter(null);
			chartResult.setTargetWorkId(report.getTargetWorkId());
			chartResult.setTargetWorkType(report.getTargetWorkType());
			chartResult.setType(report.getType());
			chartResult.setValueType(report.getValueType());
			chartResult.setXAxis(getFormFieldByValue(report.getxAxis(), fieldInfoMap));
			chartResult.setXAxisMaxRecords(report.getxAxisMaxRecords());
			chartResult.setXAxisSelector(report.getxAxisSelector());
			chartResult.setXAxisSort(report.getxAxisSort());
			chartResult.setYAxis(getFormFieldByValue(report.getyAxis(), fieldInfoMap));
			chartResult.setYAxisSelector(report.getyAxisSelector());
			chartResult.setZAxis(getFormFieldByValue(report.getzAxis(), fieldInfoMap));
			chartResult.setZAxisSelector(report.getzAxisSelector());
			chartResult.setZAxisSort(report.getzAxisSort());
			
			result = chartResult;
			
		} else if (reportType == Report.TYPE_MATRIX) {
			
			User owner = ModelConverter.getUserByUserId(report.getOwner());
			User lastModifier = report.getOwner().equalsIgnoreCase(report.getModificationUser()) ? owner : ModelConverter.getUserByUserId(report.getModificationUser());
			MatrixReport matrixResult = new MatrixReport(report.getObjId(), report.getName(), owner, lastModifier, new LocalDate(report.getModificationDate().getTime()));
			
			
			matrixResult.setAccessPolicy(new AccessPolicy(Integer.parseInt(report.getAccessLevel())));
			matrixResult.setChartType(report.getChartType());
			matrixResult.setCreatedDate(new LocalDate(report.getCreationDate().getTime()));
			matrixResult.setDataSourceType(report.getDataSourceType());
			matrixResult.setExternalServiceId(report.getExternalServiceId());
			matrixResult.setReportTableKey(report.getReportTableKey());
			//TODO
			matrixResult.setSearchFilter(null);
			matrixResult.setTargetWorkId(report.getTargetWorkId());
			matrixResult.setTargetWorkType(report.getTargetWorkType());
			matrixResult.setType(report.getType());
			matrixResult.setValueType(report.getValueType());
			matrixResult.setXAxis(getFormFieldByValue(report.getxAxis(), fieldInfoMap));
			matrixResult.setXAxisMaxRecords(report.getxAxisMaxRecords());
			matrixResult.setXAxisSelector(report.getxAxisSelector());
			matrixResult.setXAxisSort(report.getxAxisSort());
			matrixResult.setXSecondAxis(getFormFieldByValue(report.getxSecondAxis(), fieldInfoMap));
			matrixResult.setXSecondAxisSelector(report.getxSecondAxisSelector());
			matrixResult.setXSecondAxisSort(report.getxSecondAxisSort());
			matrixResult.setYAxis(getFormFieldByValue(report.getyAxis(), fieldInfoMap));
			matrixResult.setYAxisSelector(report.getyAxisSelector());
			matrixResult.setZAxis(getFormFieldByValue(report.getzAxis(), fieldInfoMap));
			matrixResult.setZAxisSelector(report.getzAxisSelector());
			matrixResult.setZAxisSort(report.getzAxisSort());
			matrixResult.setZSecondAxis(getFormFieldByValue(report.getzSecondAxis(), fieldInfoMap));
			matrixResult.setZSecondAxisSelector(report.getzSecondAxisSelector());
			matrixResult.setZSecondAxisSort(report.getzSecondAxisSort());
			
			result = matrixResult;
			
		} else {
			//TableReport
		}
		
		return result;
	}
	
	
	public Report getUserReportById(String reportId) throws Exception {
		
		if (CommonUtil.isEmpty(reportId))
			return null;
		
		RptReport report = SwManagerFactory.getInstance().getReportManager().getRptReport("", reportId, IManager.LEVEL_ALL);
		if (CommonUtil.isEmpty(report))
			return null;
		
		return getReportByRptReport(report);
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
		
		System.out.println(request.getParameter("reportId"));//ex: chart.pcnt.monthly
		
		return SmartTest.getReportData4();
	}
	@Override
	public String removeWorkReport(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		
		String reportId = (String)requestBody.get("reportId");
		if (CommonUtil.isEmpty(reportId))
			return null;
		
		SwManagerFactory.getInstance().getReportManager().removeRptReport("", reportId);
		
		return reportId;
	}

	@Override
	public String setWorkReport(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		/**{
			workId=reportManagement, 
			targetWorkId=allSmartWorks, 
			frmWorkReport={
				txtWorkReportName=1, 
				rdoWorkReportType=1, 
				selTargetWorkType=-1, 
				selReportChartType=4, 
				selReportXAxis=subInstanceCount, 
				rdoReportXAxisSort=descend, 
				selReportXAxisMaxRecords=unlimited, 
				selReportYAxis=subInstanceCount, 
				selReportYAxisValue=count, 
				selReportFilterName=system.allInstances
			}, 
			frmAccessPolicy={
				selAccessPolicy=3
			}, 
			frmReportSaveAsName={
			
			}
		}*/

		String userId = SmartUtil.getCurrentUser().getId();
	
		Map<String, Object> frmWorkReportMap = (Map<String, Object>)requestBody.get("frmWorkReport");
		Map<String, Object> frmAccessPolicyMap = (Map<String, Object>)requestBody.get("frmAccessPolicy");
		Map<String, Object> frmReportSaveAsNameMap = (Map<String, Object>)requestBody.get("frmReportSaveAsName");
	
		String workId = (String)requestBody.get("workId");
		String targetWorkId = (String)requestBody.get("targetWorkId");
		String reportId = (String)requestBody.get("reportId");

		String saveAsName = (String)frmReportSaveAsNameMap.get("txtReportSaveAsName");
		
		RptReport report = null;
		if (CommonUtil.isEmpty(reportId) || !CommonUtil.isEmpty(saveAsName)) {
			report = new RptReport();
		} else {
			report = SwManagerFactory.getInstance().getReportManager().getRptReport(userId, reportId, IManager.LEVEL_ALL);
			if (CommonUtil.isEmpty(report))
				report = new RptReport();
		}
		
		String txtWorkReportName = (String)frmWorkReportMap.get("txtWorkReportName");
		
		if (CommonUtil.isEmpty(txtWorkReportName) || !CommonUtil.isEmpty(saveAsName)) {
			if (!CommonUtil.isEmpty(saveAsName)) {
				txtWorkReportName = saveAsName;
			} else {
				txtWorkReportName = report.getName();
			}
			
		}
		
		String rdoWorkReportType = (String)frmWorkReportMap.get("rdoWorkReportType");
		String selTargetWorkType = (String)frmWorkReportMap.get("selTargetWorkType");
		String selReportChartType = (String)frmWorkReportMap.get("selReportChartType");
		
		String selReportXAxis = (String)frmWorkReportMap.get("selReportXAxis");
		String selReportXAxisSelectorUser = (String)frmWorkReportMap.get("selReportXAxisSelectorUser");
		String selReportXAxisSelectorDate = (String)frmWorkReportMap.get("selReportXAxisSelectorDate");
		String rdoReportXAxisSort = (String)frmWorkReportMap.get("rdoReportXAxisSort");
		String selReportXAxisMaxRecords = (String)frmWorkReportMap.get("selReportXAxisMaxRecords");
		String numReportXAxisMaxRecords = (String)frmWorkReportMap.get("numReportXAxisMaxRecords");

		String selReportYAxis = (String)frmWorkReportMap.get("selReportYAxis");
		String selReportYAxisValue = (String)frmWorkReportMap.get("selReportYAxisValue");
		
		String selReportZAxis = (String)frmWorkReportMap.get("selReportZAxis");
		String selReportZAxisSelectorUser = (String)frmWorkReportMap.get("selReportZAxisSelectorUser");
		String selReportZAxisSelectorDate = (String)frmWorkReportMap.get("selReportZAxisSelectorDate");
		String rdoReportZAxisSort = (String)frmWorkReportMap.get("rdoReportZAxisSort");
		
		String selReportZSecondAxis = (String)frmWorkReportMap.get("selReportZSecondAxis");
		String selReportZSecondAxisSelectorUser = (String)frmWorkReportMap.get("selReportZSecondAxisSelectorUser");
		String selReportZSecondAxisSelectorDate = (String)frmWorkReportMap.get("selReportZSecondAxisSelectorDate");
		String rdoReportZSecondAxisSort = (String)frmWorkReportMap.get("rdoReportZSecondAxisSort");
		
		String selReportFilterName = (String)frmWorkReportMap.get("selReportFilterName");
			
		String selAccessPolicy = (String)frmAccessPolicyMap.get("selAccessPolicy");
		
		report.setOwner(userId);
		report.setTargetWorkId(targetWorkId);
		report.setName(txtWorkReportName);
		report.setType(Integer.parseInt(rdoWorkReportType));
		
		if (selTargetWorkType == null && targetWorkId.indexOf("pkg_") != -1) {
			PkgPackageCond cond = new PkgPackageCond();
			cond.setPackageId(targetWorkId);
			PkgPackage pkg = SwManagerFactory.createInstance().getPkgManager().getPackage(userId, cond, IManager.LEVEL_LITE);
			if (!CommonUtil.isEmpty(pkg)) {
				String type = pkg.getType();
				if (type.equalsIgnoreCase("SINGLE")) {
					selTargetWorkType = SmartWork.TYPE_INFORMATION + "";
				} else if (type.equalsIgnoreCase("PROCESS")) {
					selTargetWorkType = SmartWork.TYPE_PROCESS + "";
				} else {
					selTargetWorkType = "-1";
				}
			} else {
				selTargetWorkType = "-1";
			}
		} else {
			selTargetWorkType = "-1";
		}
		
		report.setTargetWorkType(Integer.parseInt(selTargetWorkType));
		report.setChartType(selReportChartType != null ? Integer.parseInt(selReportChartType) : -1);
		
		report.setxAxis(selReportXAxis);
		report.setxAxisSelector(selReportXAxisSelectorUser != null ? selReportXAxisSelectorUser : selReportXAxisSelectorDate);
		report.setxAxisSort(rdoReportXAxisSort);
		report.setxAxisMaxRecords(selReportXAxisMaxRecords != null && !selReportXAxisMaxRecords.equalsIgnoreCase("max") ? -1 : Integer.parseInt(numReportXAxisMaxRecords));
		
		report.setyAxis(selReportYAxis);
		report.setValueType(selReportYAxisValue);
		
		report.setzAxis(selReportZAxis);
		report.setzAxisSelector(selReportZAxisSelectorUser != null ? selReportZAxisSelectorUser : selReportZAxisSelectorDate);
		report.setzAxisSort(rdoReportZAxisSort);
		report.setzSecondAxis(selReportZSecondAxis);
		report.setzSecondAxisSelector(selReportZSecondAxisSelectorUser != null ? selReportZSecondAxisSelectorUser : selReportZSecondAxisSelectorDate);
		report.setzSecondAxisSort(rdoReportZSecondAxisSort);
		
		report.setSearchFilterId(selReportFilterName);
		
		report.setAccessLevel(selAccessPolicy);
		
		SwManagerFactory.getInstance().getReportManager().setRptReport(userId, report, IManager.LEVEL_ALL);
		
		return null;
	}

	@Override
	public String setWorkReportPane(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		
//		{
//				reportId=402880bb4072397101407513dd450004, 
//				frmNewReportPane={
//							txtPaneName=123, 
//							selReportPanePosition=11
//				}
//		}

		String userId = SmartUtil.getCurrentUser().getId();
		
		String reportId = (String)requestBody.get("reportId");
		if (CommonUtil.isEmpty(reportId))
			return null;
		
		IReportManager reportManager = SwManagerFactory.getInstance().getReportManager();
		
		RptReport report = reportManager.getRptReport(userId, reportId, IManager.LEVEL_ALL);
		if (CommonUtil.isEmpty(report))
			return null;
		
		Map<String, Object> frmNewReportPane = (Map<String, Object>)requestBody.get("frmNewReportPane");
		String txtPaneName = (String)frmNewReportPane.get("txtPaneName");
		String selReportPanePosition = (String)frmNewReportPane.get("selReportPanePosition");
		
		RptReportPane pane = new RptReportPane();
		pane.setName(txtPaneName);
		pane.setReportName(report.getName());
		pane.setReportId(reportId);
		pane.setReportType(report.getType());
		pane.setChartType(report.getChartType());
		pane.setTargetWorkId(report.getTargetWorkId());
		pane.setOwner(userId);
		//TODO
		pane.setChartView(true);
		pane.setStacked(true);
		pane.setShowLegend(true);
		pane.setStringLabelRotation(ChartReport.STRING_LABEL_ROTATION_AUTO);

		//pane.setColumnSpans(columnSpans);
		pane.setPosition(Integer.parseInt(selReportPanePosition));
		
		reportManager.setRptReportPane(userId, pane, IManager.LEVEL_ALL);
		
		return pane.getObjId();
	}

	private void reSortingPaneByNewPane(RptReportPane pane) throws Exception {
		
		if (CommonUtil.isEmpty(pane))
			return;
		
		String owner = pane.getOwner();
		
		RptReportPaneCond cond = new RptReportPaneCond();
		cond.setOwner(owner);
		
		RptReportPane[] panes = SwManagerFactory.getInstance().getReportManager().getRptReportPanes("", cond, IManager.LEVEL_ALL);
		
		if (CommonUtil.isEmpty(panes))
			return;
		
		int position = pane.getPosition();
		
		
		
		
		
		
		
		
		
	}
	
	
	
	
	
	@Override
	public int getUserReportCount(String targetWorkId) throws Exception {
		RptReportCond cond = new RptReportCond();
		cond.setTargetWorkId(targetWorkId);
		long totalSize = SwManagerFactory.getInstance().getReportManager().getRptReportSize("", cond);
		return (int)totalSize;
		
		//return SmartTest.getUserReportCount(targetWorkId);
	}

	@Override
	public InstanceInfoList getReportInstanceList(String targetWorkId, int targetWorkType, String producedBy, RequestParams params) throws Exception {

		String userId = SmartUtil.getCurrentUser().getId();
		
		InstanceInfoList list = new InstanceInfoList();
		
		ReportInstanceInfo[] instances = null;
		if(Report.PRODUCED_BY_SMARTWORKS.equals(producedBy)){
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
		} else {
			int currentPage = params.getCurrentPage();
			int pageSize = params.getPageSize();
			int totalPages = 0;
			long totalSize = 0;
			
			RptReportCond cond = new RptReportCond();
			if (!CommonUtil.isEmpty(targetWorkId)) {
				cond.setTargetWorkId(targetWorkId);
				if (!targetWorkId.equalsIgnoreCase(SmartWork.ID_ALL_WORKS))
					cond.setTargetWorkType(targetWorkType);
			}
			
			totalSize = SwManagerFactory.getInstance().getReportManager().getRptReportSize(userId, cond);
			
			totalPages = (int)totalSize % pageSize;
			if(totalPages == 0)
				totalPages = (int)totalSize / pageSize;
			else
				totalPages = (int)totalSize / pageSize + 1;
			
			cond.setPageNo(currentPage -1);
			cond.setPageSize(pageSize);
			
			cond.setOrders(new Order[]{new Order(RptReport.A_CREATIONDATE, false)});
			
			RptReport[] reports = SwManagerFactory.getInstance().getReportManager().getRptReports(userId, cond, IManager.LEVEL_ALL);
			if (CommonUtil.isEmpty(reports))
				return list;
			
			instances = ModelConverter.getReportInstanceInfoArrayByRptReportArray(userId, reports);
			
			if(!SmartUtil.isBlankObject(instances)){
				list.setInstanceDatas(ReportInstanceInfo.getSortedInstances(instances, params.getSortingField()));
				list.setCurrentPage(currentPage);
				list.setPageSize(pageSize);
				list.setTotalPages(totalPages);
				list.setTotalSize((int)totalSize);
				list.setType(Instance.TYPE_REPORT);
				//list.setType(InstanceInfoList.TYPE_INFORMATION_INSTANCE_LIST);
				//list.setType(InstanceInfoList.TYPE_PROCESS_INSTANCE_LIST);
				list.setSortedField(params.getSortingField());
			}
			return list;
			
			//return SmartTest.getReportInstanceList(targetWorkId, params);
			
		}
	}

	@Override
	public ReportPane[] getMyDashboard() throws Exception {
		
		String userId = SmartUtil.getCurrentUser().getId();
		IReportManager reportManager = SwManagerFactory.getInstance().getReportManager();
		
		RptReportPaneCond cond = new RptReportPaneCond();
		cond.setOwner(userId);
		cond.setOrders(new Order[]{new Order("position", true)});
		
		RptReportPane[] panes = reportManager.getRptReportPanes(userId, cond, IManager.LEVEL_ALL);
		
		if (CommonUtil.isEmpty(panes))
			return null;
		
		RptReportPaneSorter paneSorter = new RptReportPaneSorter();
		for (int i = 0; i < panes.length; i++) {
			RptReportPane pane = panes[i];
			paneSorter.setReportPane(pane);
		}
		for (int i = 0; i < panes.length; i++) {
			RptReportPane pane = panes[i];
			paneSorter.setRowSpanToRptReportPane(pane);
		}
		ReportPane[] resultPanes = new ReportPane[panes.length];
		for (int i = 0; i < panes.length; i++) {
			RptReportPane pane = panes[i];
			ReportPane resultPane = new ReportPane();
			
			resultPane.setChartType(pane.getChartType());
			resultPane.setChartView(pane.isChartView());
			resultPane.setColumnSpans(pane.getColumnSpans());
			resultPane.setId(pane.getObjId());
			resultPane.setName(pane.getName());
			
			int position = pane.getPosition();
			int row = 0;
			int column = 0;
			
			if (position < 10) {
				column = position;
			} else {
				String positionStr = position + "";
				row = Integer.parseInt(positionStr.substring(0, 1));
				column = Integer.parseInt(positionStr.substring(1, 2));
			}
			resultPane.setPosition(new Matrix(row, column));
			resultPane.setReportId(pane.getReportId());
			resultPane.setReportName(pane.getReportName());
			resultPane.setReportType(pane.getReportType());
			resultPane.setShowLegend(pane.isShowLegend());
			resultPane.setStacked(pane.getIsChartView());
			resultPane.setStringLabelRotation(pane.getStringLabelRotation());
			resultPane.setTargetWork(ModelConverter.getSmartWorkInfoByPackageId(pane.getTargetWorkId()));
			
			resultPanes[i] = resultPane;
		}
		return resultPanes;
		//return SmartTest.getMyDashboard();
	}

	@Override
	public String removeWorkReportPane(Map<String, Object> requestBody, HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
