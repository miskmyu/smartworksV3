$(function() {

	$('.js_remove_condition').live('click', function(e) {
		$(targetElement(e)).parents('form.js_filter_condition:first').remove();
		return false;
	});

	$('.js_add_condition').live('click', function(e) {
		var target = $(targetElement(e)).parents('div.js_search_filter_page').find('form.js_filter_condition').parent();
		var newCondition = target.find('form.js_new_condition:first').clone().show().removeClass('js_new_condition');
		target.append(newCondition.show());
		return false;
	});

	$('a.js_edit_work_report').live('click', function(e) {
		var input = $(targetElement(e)).parent();
		var workReport = input.parents('div.js_work_report_page');
		var target = workReport.find('div.js_work_report_edit');
		var url = input.attr('href');
		if(isEmpty(url)) url = input.find('a').attr('href');
		var targetWorkId = workReport.attr('targetWorkId');
		var targetWorkName = workReport.attr('targetWorkName');
		var targetWorkIcon = workReport.attr('targetWorkIcon');
		var progressSpan = input.next('span.js_progress_span');
		var reportId = workReport.find('select[name="selMyReportList"]').attr('value');
		smartPop.progressCont(progressSpan);
		$.ajax({
			url : url,
			data : {
				reportId: reportId,
				targetWorkId: targetWorkId,
				targetWorkName: targetWorkName,
				targetWorkIcon: targetWorkIcon
			},
			success : function(data, status, jqXHR) {
				target.html(data).slideDown(500);
				smartPop.closeProgress();						
			},
			error : function(xhr, ajaxOptions, thrownError){
				smartPop.closeProgress();						
			}
		});
		return false;
	});

	$('a.js_work_report_close').live('click', function(e) {
		var input = $(targetElement(e));
		var reportId = input.parents('.js_work_report_edit').find('.js_work_report_edit_page').attr('reportId');
		if(isEmpty(reportId) || reportId == 'null' || !isEmpty(input.parents('.js_report_list_page'))){
			input.parents('.js_work_report_page').find('.js_work_report_view').html('').hide();
		}
		$(targetElement(e)).parents('.js_work_report_edit').slideUp().html('');
		return false;
	});

	
	$('a.js_work_report_execute').live('click', function(e) {
		var input = $(targetElement(e));
		var forms = input.parents('.js_work_report_edit').find('form');
		forms.find('.js_work_report_name input').removeClass('required');
		if (SmartWorks.GridLayout.validate(forms, $('.js_report_error_message'))) {
			var paramsJson = {};
			var workReportEdit = forms.parent().find('.js_work_report_edit_page');
			if(isEmpty(workReportEdit)) workReportEdit = forms.parents('.js_report_list_page').find('.js_work_report_edit_page');
			var workId = workReportEdit.attr('workId');
			var targetWorkId = workReportEdit.attr('targetWorkId');
			paramsJson['workId'] = workId;
			paramsJson['targetWorkId'] = targetWorkId;
			for(var i=0; i<forms.length; i++){
				var form = $(forms[i]);
				var visibleForm = form.find(':visible');
				paramsJson[form.attr('name')] = mergeObjects(visibleForm.serializeObject(), SmartWorks.GridLayout.serializeObject(visibleForm));
			}
			console.log(JSON.stringify(paramsJson));
			var url = "get_report_data_by_def.sw";
			smartPop.progressCont(input.parents('.js_work_report_edit').find('.js_progress_span'));
			$.ajax({
				url : url,
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					var reportData = data;
					var reportType = forms.find('input[name="rdoWorkReportType"]:checked').attr('value');
					var chartType = forms.find('select[name="selReportChartType"]').find('option:selected').attr('chartType');
					$.ajax({
						url : "work_report_view.sw",
						data : {
							workId: workId,
							reportType: reportType,
							chartType: chartType
						},
						success : function(data, status, jqXHR) {
							input.parents('.js_work_report_page').find('div.js_work_report_view').html(data).slideDown(500);
							smartChart.loadWithData(reportType, reportData, chartType, false, "chart_target");
							smartPop.closeProgress();
						},
						error : function(xhr, ajaxOptions, thrownError){
							smartPop.closeProgress();						
						}
					});
 				},
				error : function(e) {
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("createReportError"));
				}
			});
		
		}
		forms.find('.js_work_report_name input').addClass('required');
		return false;
	});
	
	$('a.js_work_report_register').live('click', function(e) {
		var input = $(targetElement(e));
		var workReportEdit = input.parents('.js_work_report_edit').find('.js_work_report_edit_page');
		var workReportView = input.parents('.js_work_report_edit').nextAll('.js_work_report_view').find('.js_work_report_view_page');
		var targetWorkId = workReportEdit.attr('targetWorkId');
		var targetWorkName = workReportEdit.attr('targetWorkName');
		var targetWorkIcon = workReportEdit.attr('targetWorkIcon');
		var reportId = workReportEdit.attr('reportId');
		var reportName = workReportEdit.find('input[name="txtWorkReportName"]').attr('value');
		var reportType = workReportEdit.find('input[name="rdoWorkReportType"]:checked').attr('value');
		
		var config = {};
		if(!isEmpty(workReportView)){
			config.chartType = workReportView.find('.js_change_chart_type option:selected').attr('chartType');
			config.isChartView = isEmpty(workReportView.find('.js_toggle_chart_table:visible'));
			config.isStacked = !isEmpty(workReportView.find('input[name="chkStackedChart"]:checked'));
		}else{
			config.chartType = workReportEdit.find('.js_report_chart_type option:selected').attr('chartType');
			config.isChartView = true;
			config.isStacked = false;			
		}
		
		config.showLegend = true;
		config.stringLabelRotation = null;
		config.paneColumnSpans = null;
		config.panePosition = null;
		smartPop.createReportPane(null, reportName, targetWorkId, targetWorkName, targetWorkIcon, reportId, reportName, reportType, config);
		return false;
	});

	$('a.js_work_report_save').live('click', function(e) {
		var workReportEdit = $(targetElement(e)).parents('.js_work_report_edit');
		var forms = workReportEdit.find('form');
		if (SmartWorks.GridLayout.validate(forms, $('.js_report_error_message'))) {
			var paramsJson = {};
			var workReportEditPage = workReportEdit.find('.js_work_report_edit_page');
			paramsJson['workId'] = workReportEditPage.attr('workId');
			paramsJson['targetWorkId'] = workReportEditPage.attr('targetWorkId');
			var url = "create_new_work_report.sw";
			paramsJson['reportId'] = workReportEditPage.attr('reportId');
			if(!workReportEdit.find(".js_work_report_name").is(':visible') && !workReportEdit.find('form[name="frmReportSaveAsName"]').is(':visible')){
				url = "set_work_report.sw";
			}
				
			for(var i=0; i<forms.length; i++){
				var form = $(forms[i]);
				var visibleForm = form.find(':visible');
				paramsJson[form.attr('name')] = mergeObjects(visibleForm.serializeObject(), SmartWorks.GridLayout.serializeObject(visibleForm));
			}
			console.log(JSON.stringify(paramsJson));
			smartPop.progressCont($(targetElement(e)).parents('.js_work_report_edit').find('form[name="frmAccessPolicy"]').nextAll('.js_progress_span'));
			$.ajax({
				url : url,
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					$('.js_work_report_list_box:first').html(data);
					$('a.js_work_report_close').click();
					$('.js_view_report_list a[producedBy="user"]').click();
					smartPop.closeProgress();
 				},
				error : function(e) {
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("createReportError"));
				}
			});
		
		}
		return false;
	});

	$('a.js_work_report_delete').live('click', function(e) {
		var paramsJson = {};
		var workReportEdit = $(targetElement(e)).parents('.js_work_report_edit');
		var workReportEditPage = workReportEdit.find('.js_work_report_edit_page');
		paramsJson['workId'] = workReportEditPage.attr('workId');
		paramsJson['reportId'] = workReportEditPage.attr('reportId');
		console.log(JSON.stringify(paramsJson));
		smartPop.confirm("[" + workReportEditPage.find('input[name="txtWorkReportName"]').attr('value') + "]" + smartMessage.get("removeConfirmation"), 
			function(){
				smartPop.progressCont($(targetElement(e)).parents('.js_work_report_edit').find('form[name="frmAccessPolicy"]').nextAll('.js_progress_span'));
				$.ajax({
					url : "remove_work_report.sw",
					contentType : 'application/json',
					type : 'POST',
					data : JSON.stringify(paramsJson),
					success : function(data, status, jqXHR) {
						$('.js_work_report_list_box:first').html(data);
						$('a.js_work_report_close').click();
						$('.js_view_report_list a[producedBy="user"]').click();
						smartPop.closeProgress();
					},
					error : function(e) {
						smartPop.closeProgress();
						smartPop.showInfo(smartPop.ERROR, smartMessage.get("removeReportError"));
					}
				});
		},
		function(){			
		});
		
		return false;
	});

	$('a.js_work_report_saveas').live('click', function(e) {
		var input = $(targetElement(e)).hide();
		var workReport = input.parents('.js_work_report_page:first');
		workReport.find('.js_button_save_as').hide();
		workReport.find('.js_button_save').show();
		workReport.find('form[name="frmReportSaveAsName"]').show();
		
		return false;
	});

	$('select.js_select_work_report').live('change', function(e) {
		var input = $(targetElement(e));
		var workReport = $('div.js_work_report_page');
		var target = workReport.find('.js_work_report_view');
		var url = input.attr('href');
		var selected = input.children('option:selected');
		var reportId = selected.attr('value');
		if(reportId==="none"){
			target.slideUp().html('');
			workReport.find('.js_work_report_edit').slideUp().html('');
			return false;
		}
		var reportType = selected.attr('reportType');
		var chartType = selected.attr('chartType');
		var progressSpan = input.nextAll('span.js_progress_span');
		smartPop.progressCont(progressSpan);						
		workReport.find('.js_work_report_edit').slideUp().html('');
		$.ajax({
			url : url,
			data : {
				reportType: reportType,
				chartType: chartType
			},
			success : function(data, status, jqXHR) {
				target.html(data).slideDown(500);
				smartChart.load(reportType, reportId, chartType, false, "chart_target");
				smartPop.closeProgress();						
			},
			error : function(xhr, ajaxOptions, thrownError){
				smartPop.closeProgress();						
			}
		});
		
		return false;
	});

	$('.js_select_work_report').live('click', function(e) {
		var input = $(targetElement(e)).parents('.js_select_work_report');
		var workReport = $('div.js_work_report_page');
		var target = workReport.find('.js_work_report_edit');
		var url = input.attr('href');
		var targetWorkId = workReport.attr('targetWorkId');
		var targetWorkName = workReport.attr('targetWorkName');
		var targetWorkIcon = workReport.attr('targetWorkIcon');
		var reportId = input.attr('reportId');
		var progressSpan = input.next('span.js_progress_span');
		smartPop.progressCont(progressSpan);
		$.ajax({
			url : url,
			data : {
				reportId: reportId,
				targetWorkId: targetWorkId,
				targetWorkName: targetWorkName,
				targetWorkIcon: targetWorkIcon
			},
			success : function(data, status, jqXHR) {
				target.html(data).slideDown(500);
				smartPop.closeProgress();						
			},
			error : function(xhr, ajaxOptions, thrownError){
				smartPop.closeProgress();						
			}
		});
		
		return false;
	});

	$('select.js_change_chart_type').live('change', function(e) {
		var input = $(targetElement(e));
		var chartType = input.attr('value');
		var stackedChart = input.parents('.js_work_report_view_page').find('.js_stacked_chart');
		if(chartType === "column" || chartType === "bar") stackedChart.show();
		else stackedChart.hide();
		
		smartChart.reload(chartType, stackedChart.find('.js_change_stacked_chart').is(':checked'), true);
		return false;
	});

	$('input.js_change_stacked_chart').live('click', function(e) {
		var input = $(targetElement(e));
		var chartType = input.parents('.js_work_report_view_page').find('select.js_change_chart_type').attr('value');
		smartChart.reload(chartType, input.is(':checked'), true);
		return true;
	});

	$('a.js_toggle_chart_table').live('click', function(e) {
		var input = $(targetElement(e));
		var workReportView = input.parents('.js_work_report_view_page');
		var chartType = workReportView.find('select.js_change_chart_type').attr('value');
		var isChartView = workReportView.attr('isChartView');
		if(isChartView === 'true'){
			workReportView.attr('isChartView', 'false');
			workReportView.find('.js_stacked_chart').hide();
			workReportView.find('.js_chart_type').hide();
		}else{
			workReportView.attr('isChartView', 'true'); 
			if(chartType === "column" || chartType === "bar") workReportView.find('.js_stacked_chart').show();
			workReportView.find('.js_chart_type').show();
		}
		workReportView.find('a.js_toggle_chart_table').toggle();
		smartChart.reload(chartType, workReportView.find('input.js_change_stacked_chart').is(':checked'), workReportView.attr('isChartView')==="true");
		return false;
	});

	$('tr.js_work_report_type td').live('change', function(e) {
		var input = $(targetElement(e));
		var target = input.parents('table.js_report_title').next('table.js_form_by_report_type');
		var url = input.attr('href');
		var reportType = input.attr('value');
		$.ajax({
			url : url,
			data : {reportType : reportType },
			success : function(data, status, jqXHR) {
				input.parents('.js_work_report_edit_page').find('.js_target_work_type option:first').attr('selected', 'selected');
				target.html(data).show();
			},
			error : function(xhr, ajaxOptions, thrownError){
				console.log(xhr, thrownError);
			}
		});
		return false;
	});

	$('tr.js_target_work_type select').live('change', function(e) {
		var input = $(targetElement(e));
		var target = input.parents('table.js_report_title').next('table.js_form_by_report_type');
		var workReportEdit = input.parents('.js_work_report_edit_page');
		var reportTypeElement = workReportEdit.find('.js_work_report_type input[name="rdoWorkReportType"]:checked');
		var url = reportTypeElement.attr('href');
		var reportType = reportTypeElement.attr('value');
		var targetWorkType = input.find('option:selected').attr('value');
		$.ajax({
			url : url,
			data : {
				reportType: reportType,
				targetWorkType: targetWorkType
			},
			success : function(data, status, jqXHR) {
				target.html(data).show();
			},
			error : function(xhr, ajaxOptions, thrownError){
				console.log(xhr, thrownError);
			}
		});
		return false;
	});

	$('td.js_select_chart_axis select').live('change', function(e) {
		var input = $(targetElement(e));		
		var type = input.children('option:selected').attr('type');
		var targetDate = input.nextAll('.js_axis_selector_date:first');
		var targetUser = input.nextAll('.js_axis_selector_user:first');
		if(type === "dateChooser" || type ==="dateTimeChooser"){
			targetDate.show();
			targetUser.hide();
		}else if(type === "userField"){
			targetDate.hide();
			targetUser.show();
		}else{
			targetDate.hide();
			targetUser.hide();			
		}
		return false;
	});
	
	$('select.js_select_xaxis_max').live('change', function(e) {
		var input = $(targetElement(e));		
		if(input.children('option:selected').attr('value') === "unlimited"){
			input.next().hide();
		}else{
			input.next().show();
		}
		return false;
	});

	$('tr.js_add_chart_zaxis a').live('click', function(e) {
		var input = $(targetElement(e)).parents('tr.js_add_chart_zaxis:first').hide();
		input.next('tr.js_chart_zaxis').show();
		var secondZaxis = input.nextAll('tr.js_chart_zsecondaxis:first');
		if(secondZaxis.not(":visible") && secondZaxis.prevAll('tr.js_report_chart_type').not(':visible'))
			secondZaxis.prev('tr.js_add_chart_zsecondaxis').show();
		return false;
	});
	$('.js_remove_chart_zaxis a').live('click', function(e){
		$(targetElement(e)).parents('tr.js_chart_zaxis:first').hide().prev('tr.js_add_chart_zaxis').show();
		$(targetElement(e)).parents('tr.js_chart_zaxis:first').nextAll('tr.js_add_chart_zsecondaxis:first').hide().next('tr.js_chart_zsecondaxis').hide();
		return false;
	});
	$('tr.js_add_chart_xsecondaxis a').live('click', function(e) {
		var input = $(targetElement(e)).parents('tr.js_add_chart_xsecondaxis:first').hide();
		input.next('tr.js_chart_xsecondaxis').show();
		return false;
	});
	$('.js_remove_chart_xsecondaxis a').live('click', function(e){
		$(targetElement(e)).parents('tr.js_chart_xsecondaxis:first').hide().prev('tr.js_add_chart_xsecondaxis').show();
		return false;
	});
	$('tr.js_add_chart_zsecondaxis a').live('click', function(e) {
		var input = $(targetElement(e)).parents('tr.js_add_chart_zsecondaxis:first').hide();
		input.next('tr.js_chart_zsecondaxis').show();
		return false;
	});
	$('.js_remove_chart_zsecondaxis a').live('click', function(e){
		$(targetElement(e)).parents('tr.js_chart_zsecondaxis:first').hide().prev('tr.js_add_chart_zsecondaxis').show();
		return false;
	});
	$('tr.js_toggle_chart_search_filter a').live('click', function(e) {
		var input = $(targetElement(e)).parent('td');
		var target = input.parent().next('tr.js_chart_search_filter');
		var actionType = input.attr('actionType');
		if(actionType!=null && actionType === "remove"){
			target.hide();
			input.hide().siblings().show();
		}else{
			$('#content').showLoading();						
			var url = input.parent().attr('url');
			$.ajax({
				url : url,
				data : {},
				success : function(data, status, jqXHR) {
					target.html(data).show();
					input.hide().siblings().show();
					$('#content').hideLoading();						
				},
				error : function(xhr, ajaxOptions, thrownError){
					$('#content').hideLoading();											
				}
			});
		}
		return false;
	});
	
	$('a.js_refresh_report_pane').live('click', function(e) {
		var input = $(targetElement(e));
		var workReportPane = input.parents('.js_work_report_pane_page');
		var paneId = workReportPane.attr('paneId');
		var paneName = workReportPane.attr('paneName');
		var paneColumnSpans = workReportPane.attr('paneColumnSpans');
		var panePosition = workReportPane.attr('panePosition');
		var targetWorkName = workReportPane.attr('targetWorkName');
		var targetWorkIcon = workReportPane.attr('targetWorkIcon');
		var reportId = workReportPane.attr('reportId');
		var reportName = workReportPane.attr('reportName');
		var reportType = workReportPane.attr('reportType');
		var chartType = workReportPane.attr('chartType');
		var isChartView = workReportPane.attr('isChartView');
		var isStacked = workReportPane.attr('isStacked');
		var showLegend = workReportPane.attr('showLegend');
		var stringLabelRotation = workReportPane.attr('stringLabelRotation');
		var progressSpan = input.nextAll('span.js_progress_span');
		smartPop.progressCont(progressSpan);						
		$.ajax({
			url : 'work_report_pane.sw',
			data : {
				paneId: paneId,
				paneName: paneName,
				paneColumnSpans: paneColumnSpans,
				panePosition: panePosition,
				targetWorkName: targetWorkName,
				targetWorkIcon: targetWorkIcon,
				reportId: reportId,
				reportName: reportName,
				reportType: reportType,
				chartType: chartType,
				isChartView: isChartView,
				isStacked: isStacked,
				showLegend: showLegend,
				stringLabelRotation: stringLabelRotation
			},
			success : function(data, status, jqXHR) {
				workReportPane.find('.js_chart_target_pane').removeClass('js_chart_target_pane').attr('id', '');
				workReportPane.parent().append(data);
				smartChart.loadPane(reportType, reportId, chartType, isStacked==='true', isChartView==='true', showLegend==='true', stringLabelRotation, "chart_target_"+panePosition, isEmpty(paneColumnSpans) ? 1 : parseInt(paneColumnSpans), workReportPane.parent().find('.js_work_report_pane_page:first'));
				smartPop.closeProgress();						
			},
			error : function(xhr, ajaxOptions, thrownError){
				smartPop.closeProgress();						
			}
		});
		return false;
	});
	
	$('.js_edit_report_pane').live('click', function(e) {
		var input = $(targetElement(e));
		var workReportPane = input.parents('.js_work_report_pane_page');
		var paneId = workReportPane.attr('paneId');
		var paneName = workReportPane.attr('paneName');
		var targetWorkId = workReportPane.attr('targetWorkId');
		var targetWorkName = workReportPane.attr('targetWorkName');
		var targetWorkIcon = workReportPane.attr('targetWorkIcon');
		var reportId = workReportPane.attr('reportId');
		var reportName = workReportPane.attr('reportName');
		var reportType = workReportPane.attr('reportType');
		
		var config = {};
		config.chartType = workReportPane.attr('chartType');
		config.isChartView = workReportPane.attr('isChartView');
		config.isStacked = workReportPane.attr('isStacked');
		config.showLegend = workReportPane.attr('showLegend');
		config.stringLabelRotation = workReportPane.attr('stringLabelRotation');
		config.paneColumnSpans = workReportPane.attr('paneColumnSpans');
		config.panePosition = workReportPane.attr('panePosition');
		smartPop.createReportPane(paneId, paneName, targetWorkId, targetWorkName, targetWorkIcon, reportId, reportName, reportType, config);
		return false;
	});
	
	$('.js_remove_report_pane').live('click', function(e) {
		var input = $(targetElement(e));
		var workReportPane = input.parents('.js_work_report_pane_page');
		var paneId = workReportPane.attr('paneId');
		var paneName = workReportPane.attr('paneName');
		var paneColumnSpans = parseInt(workReportPane.attr('paneColumnSpans'));
		var panePosition = parseInt(workReportPane.attr('panePosition'));
		var paramsJson = {};
		paramsJson['paneId'] = paneId;
		smartPop.confirm("[" + paneName + "]" + smartMessage.get("removeConfirmation"), 
			function(){
				$.ajax({
					url : "remove_work_report_pane.sw",
					contentType : 'application/json',
					type : 'POST',
					data : JSON.stringify(paramsJson),
					success : function(data, status, jqXHR) {
						var dashboardPaneRow = workReportPane.parents('.js_dashboard_pane_row');
						workReportPane.remove();
						if(paneColumnSpans>1 && panePosition>-1){
							var panesInSameRow = dashboardPaneRow.find('.js_work_report_pane_page');
							if(!isEmpty(panesInSameRow)){
								panesInSameRow.attr('paneColumnSpans', "" + (parseInt(paneColumnSpans)-1));
								for(var i=0; i<panesInSameRow.length; i++){
									smartChart.resizePane($(panesInSameRow[i]));
								}
							}
						}else if($('.js_dashboard_pane_row').length == 1){
							dashboardPaneRow.css('height','412px').css('text-align','center').html('<span><br><br><br><br><br>' + smartMessage.get('reportMessageNoReportPane') + '</span>');
						}else{
							dashboardPaneRow.remove();
						}
					},
					error : function() {
	 					smartPop.showInfo(smartPop.ERROR, smartMessage.get('removeReportPaneError'), function(){
	  					});
					}					
				});
			},
			function(){
			});
		return false;
	});
	
	$('.js_pop_all_target_works').live('click', function(e) {
		var input = $(targetElement(e));
		var reportList = input.parents('.js_report_list_page');
		var target = reportList.find('.js_all_target_work_popup');
		target.css('left', input.position().left + input.width() + 10 + 'px').css('top', target.position().top + 1 + 'px');
		smartPop.selectWork(target);
		return false;
	});

	$('.js_report_list_title').live('click', function(e) {
		var input = $(targetElement(e));
		if(input.hasClass('js_user_report_count')) input = input.parent();
		input.parent().removeClass('disabled').siblings().addClass('disabled');
		
		var reportList = input.parents(".js_report_list_page");
		var targetWorkId = reportList.attr('targetWorkId');
		var targetWorkType = reportList.attr('targetWorkType');
		var producedBy = input.attr('producedBy');
		if(producedBy === "smartworks") reportList.find('form[name="frmSearchInstance"]').hide();
		else reportList.find('form[name="frmSearchInstance"]').show();
		reportList.find('.js_work_report_close').click();
		smartPop.progressCenter();
		$.get('get_user_report_count.sw?targetWorkId=' + targetWorkId,  function(data){
			reportList.find('.js_user_report_count').html('[' + data + ']');
		});
		$.get('report_instance_list.sw?targetWorkId=' + targetWorkId + '&targetWorkType=' + targetWorkType + '&producedBy=' + producedBy,  function(data){
			reportList.find('#report_instance_list_page').html(data);
			reportList.attr('producedBy', producedBy);
			smartPop.closeProgress();
		});
		return false;
	});

	$(window).resize(function() {
		if(swReportResizing) return;
		
		if(!isEmpty($('.js_work_report_pane_page'))){
			swReportResizing = true;
			setTimeout(function(){
				smartChart.resizePane();
				swReportResizing = false;
			},1000);
		}else if(!isEmpty($('.js_work_report_view_page'))){
			swReportResizing = true;
			setTimeout(function(){
				smartChart.resize();
				swReportResizing = false;
			},1000);
		}
	});
});
