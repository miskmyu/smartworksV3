
var APPNAME_SMART_BUILDER = "SmartWorkbenchEditor";
var APPNAME_INSTANCE_VIEWER = "InstanceViewerAppUcity";
var APPNAME_DIAGRAM_VIEWER = "DiagramViewerApp";
var APPNAME_GANTT_VIEWER = "GanttViewerApp";
var APPNAME_GANTT_INSTANCE_VIEWER = "GanttInstanceViewerApp";
var APPNAME_GANTT_TASK_LIST_VIEWER = "GanttTaskListViewerApp";
var APPNAME_CHART_VIEWER = "ChartGadgetApp";

function loadFlash(target, appName, params){
	var htm = 	'<object type="application:x-shockwave-flash" classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"' +
					'id="' + appName + '" width="100%" height="100%"' + 'codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">' +
					'<param name="src" value="resources/flash/' + appName + '.swf" />' +
					'<param name="quality" value="high" />' +
					'<param name="wmode" value="transparent">' +
					'<param name="bgcolor" value="#ffffff" />' +
					'<param name="allowScriptAccess" value="always" />' +
					'<param name="flashVars" value="' + params + '" />' +
					'<embed src="resources/flash/' + appName + '.swf" wmode="transparent" quality="high" bgcolor="#ffffff"' +
						'width="100%" height="100%" name="VinoTester" align="middle" play="true" loop="false" quality="high" allowScriptAccess="sameDomain"' +
						'type="application/x-shockwave-flash" pluginspage="http://www.adobe.com/go/getflashplayer" flashVars="' + params + '">' +
					'</embed>' +
				'</object>';
	$(target).html(htm);	
};

function getServiceUrl() {
	var splitArray = window.location.href.split('/');
	return splitArray[0] + '//' + splitArray[2] + '/';
};

function getGeneralParams(){
	return 	'serviceUrl=' + getServiceUrl() + 
			'&compId=' + currentUser.companyId + 
			'&userId=' + currentUser.userId + 
			'&defaultLanguage=' + currentUser.locale;	
};

function loadSmartBuilder(target, params){
	var options = {
		packageId : '',
		packageVersion : '1',
		dueDate : '' //"2010-10-01 00:00:00"
	};
	SmartWorks.extend(options, params);
	loadFlash(target, APPNAME_SMART_BUILDER, getGeneralParams() 
												+ '&packId=' + options.packageId 
												+ '&packVer=' + options.packageVersion
												+ '&dueDate=' + options.dueDate);
};

function loadInstanceViewer(target, params){
	var options = {
			instanceId : ''
	};
	SmartWorks.extend(options, params);
	loadFlash(target, APPNAME_INSTANCE_VIEWER, getGeneralParams() 
											+ '&prcInstId=' + options.instanceId);
};

function loadDiagramViewerApp(target, params){
	var options = {
			processId : '',
			version : '1'
	};
	SmartWorks.extend(options, params);
	loadFlash(target, APPNAME_DIAGRAM_VIEWER, getGeneralParams() 
											+ '&processId=' + options.processId 
											+ '&version=' + options.version);
};

function loadGanttViewer(target, params){
	var options = {
			processId : '',
			version : '1',
			contentWidth : '654.0'
	};
	loadFlash(target, APPNAME_GANTT_VIEWER, getGeneralParams() 
											+ '&processId=' + options.processId 
											+ '&version=' + options.version
											+ '&contentWidth=' + options.contentWidth);
};

function loadGanttInstanceViewer(target, params){
	var options = {
			instanceId : ''
		};
	SmartWorks.extend(options, params);
	loadFlash(target, APPNAME_GANTT_INSTANCE_VIEWER, getGeneralParams() 
												+ '&prcInstId=' + options.instanceId);
};

function loadGanttTaskListViewerApp(target, params){
	var options = {
			formDate : '', //"2010-06-28 00:00:00"
			viewScope : 'MONTH', // 'DAY', 'WEEK', 'MONTH'
			conditions : '',
			pageSize : '20'
	};
	SmartWorks.extend(options, params);
	loadFlash(target, APPNAME_GANTT_TASK_LIST_VIEWER, getGeneralParams() 
												+ '&fromDate=' + options.fromDate,
												+ '&viewScope=' + options.viewScope,
												+ '&conditions=' + options.conditions,
												+ '&pageSize=' + options.pageSize);

};

function loadChartViewer(target, params){
	var options = {
			chartType 	: 'LINE_CHART',
			viewType	: '',
			acumData	: 'true',
			fillColor 	: '0x000fff',
			gadgetId 	: '',
			xmlData		: ''
	};
	SmartWorks.extend(options, params);
	var xmlData = '<mx:XML id="xmlData" xmlns="">' +
'<ChartData type="COLUMN_CHART" dimension="2">' +
	'<groupingDefineName><![CDATA[요청구분]]></groupingDefineName>' +
	'<valueInfoDefineName><![CDATA[IT담당자]]></valueInfoDefineName>' +
	'<valueInfoDefineUnit><![CDATA[userField]]></valueInfoDefineUnit>' +
	'<grouping>' +
		'<name><![CDATA[계정등록(Mighty,스마트웍스)]]></name>' +
		'<value><![CDATA[8]]></value>' +
	'</grouping>' +
	'<grouping>' +
		'<name><![CDATA[공장자동화 개발/수정]]></name>' +
		'<value><![CDATA[18]]></value>' +
	'</grouping>' +
	'<grouping>' +
		'<name><![CDATA[기타]]></name>' +
		'<value><![CDATA[60]]></value>' +
	'</grouping>' +
	'<grouping>' +
		'<name><![CDATA[기타 프로그램 개발/수정]]></name>' +
		'<value><![CDATA[20]]></value>' +
	'</grouping>' +
	'<grouping>' +
		'<name><![CDATA[네트워크 유지보수]]></name>' +
		'<value><![CDATA[12]]></value>' +
	'</grouping>' +
	'<grouping>' +
		'<name><![CDATA[메일 계정 신청]]></name>' +
		'<value><![CDATA[5]]></value>' +
	'</grouping>' +
	'<grouping>' +
		'<name><![CDATA[스마트웍스 개발/수정]]></name>' +
		'<value><![CDATA[16]]></value>' +
	'</grouping>' +
	'<grouping>' +
		'<name><![CDATA[장애처리]]></name>' +
		'<value><![CDATA[10]]></value>' +
	'</grouping>' +
	'<grouping>' +
		'<name><![CDATA[전산기기 요청(PC,모니터,주변기기)]]></name>' +
		'<value><![CDATA[4]]></value>' +
	'</grouping>' +
	'<grouping>' +
		'<name><![CDATA[퇴사자전산처리]]></name>' +
		'<value><![CDATA[2]]></value>' +
	'</grouping>' +
	'<grouping>' +
		'<name><![CDATA[홈페이지 유지보수]]></name>' +
		'<value><![CDATA[2]]></value>' +
	'</grouping>' +
	'<grouping>' +
		'<name><![CDATA[MES 프로그램 개발/수정]]></name>' +
		'<value><![CDATA[53]]></value>' +
	'</grouping>' +
	'<grouping>' +
		'<name><![CDATA[PC A/S]]></name>' +
		'<value><![CDATA[15]]></value>' +
	'</grouping>' +
	'<grouping>' +
		'<name><![CDATA[PDA 프로그램 개발/수정]]></name>' +
		'<value><![CDATA[5]]></value>' +
	'</grouping>' +
'</ChartData>' +
'	</mx:XML>';
	options.xmlData = xmlData;
	loadFlash(target, APPNAME_CHART_VIEWER, getGeneralParams()
											+ '&chartType=' + options.chartType,
											+ '&viewType=' + options.viewType,
											+ '&acumData=' + options.acumData,
											+ '&fillColor=' + options.fillColor,
											+ '&gadgetId=' + options.gadgetId,
											+ '&xmlData=' + options.xmlData);
};

function taskSelectionCallback(id, formId, formName) {
	console.log("taskSelectionCallback Called!!! id=" + id + ", formId=" + formId + ", formName=" + formName);
	clickOnTaskInDiagram(formId);
};

function loadCallback(appName, height){
	console.log("loadCallback Called!!! status=" + height);
	var target = $(".js_process_instance_viewer");
	if(appName === APPNAME_SMART_BUILDER){
		target = $(".js_smart_workbench_editor");
	}else if(appName === APPNAME_DIAGRAM_VIEWER){
		target = $(".js_process_diagram_viewer");		
	}else if(appName === APPNAME_INSTANCE_VIEWER){
		target = $(".js_process_instance_viewer");		
	}else if(appName === APPNAME_GANTT_VIEWER){
		target = $(".js_gantt_viewer");		
	}else if(appName === APPNAME_GANTT_INSTANCE_VIEWER){
		target = $(".js_gantt_instance_viewer");		
	}else if(appName === APPNAME_GANTT_TASK_LIST_VIEWER){
		target = $(".js_gantt_task_list_viewer");		
	}else if(appName === APPNAME_CHART_VIEWER){
		target = $(".js_chart_viewer");		
	}
	target.height(height);
};

function ganttProcessCallback(packId, packName, mode, msg){
	console.log("ganttProcessCallback Called!!! packId=" + packId + ", packName=" + packName + ", mode=" + mode + ", msg=" + msg);
};

function dateCallback(startDate, endDate){
	console.log("dateCallback Called!!! startDate=" + startDate + ", endDate=" + endDate);
	var dueDate = startDate +" ~ "+ endDate;
	parent.document.getElementById('dueDateDiv').innerHTML = dueDate;
	parent.viewChartStartDate();
	//parent.document.getElementById('fromDate').setAttribute('value','');
};

function pagingCallback(totalPages, currentPage){
	console.log("pagingCallback Called!!! totalPages=" + totalPages + ", currentPage=" + currentPage);
	parent.pagingVchart(totalPages, currentPage);
};

function fullScreenCallback(param){
	console.log("fullScreenCallback Called!!! param=" + param);
	//if(param == 'NormalScreen'){
		parent.openerRefresh();
	//}
};
