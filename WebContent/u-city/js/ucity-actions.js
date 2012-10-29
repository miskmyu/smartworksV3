$(function() {
	
	/*
	 * 어디에서든 class 값이 js_container로 지정된 anchor 가 선택이 되면, anchor 의 href 값으로 ajax 를 호출하여
	 * 가져온 값을 container(메인컨텐트)화면에 보여준다.
	 */
/*	$('.js_ucity_content').swnavi({
		history : true,
		before : function(event){
			smartPop.progressCenter();				
		},
		target : 'content',
		after : function(event){
			smartPop.closeProgress();
		}
	});
*/
	$('.js_ucity_content').live('click', function(e){
		return true;
	});
	
	$('.js_view_situation_report').live('click', function(e) {
		var input = $(targetElement(e));
		input.hide().siblings().show();
		var workReport = $('div.js_work_report_page');
		var target = workReport.find('div.js_work_report_view');
		var url = input.attr('href');
		target.slideDown(500);
		$.ajax({
			url : url,
			data : {
				chartType: 'line'
			},
			success : function(data, status, jqXHR) {
				$.ajax({
					url : "ucity_get_chart_xml.sw",
					contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
					data : {
						categoryName : 'option.category.byTime', 
						periodName : 'option.period.recentAYear', 
						serviceName : 'option.service.all', 
						eventName : 'option.event.all', 
					},
					success : function(xmlData, status, jqXHR) {
						target.html(data).slideDown(500);
						loadChartViewer($('#chart_target'), {chartType : 'LINE_CHART', xmlData : xmlData.record});
					},
					error : function(xhr, ajaxOptions, thrownError){
					}
				});
			},
			error : function(xhr, ajaxOptions, thrownError){
			}
		});
		
		return false;
	});

	$('.js_close_situation_report').live('click', function(e) {
		var input = $(targetElement(e));
		input.hide().siblings().show();
		var workReport = $('div.js_work_report_page');
		var target = workReport.find('div.js_work_report_view');
		target.slideUp();
		return false;
	});

	$('.js_situation_report_close').live('click', function(e) {
		$('div.js_work_report_page').find('.js_close_situation_report').click().hide().siblings().show();
		return false;
	});

	$('.js_situation_report_execute').live('click', function(e) {
		var input = $(targetElement(e));
		var workReport = $('div.js_work_report_page');
		var categoryName = workReport.find('.js_select_ucity_category option:selected').attr('value');
		var periodName = workReport.find('.js_select_ucity_period option:selected').attr('value');
		var serviceName = workReport.find('.js_select_ucity_service option:selected').attr('value');
		var eventName = workReport.find('.js_select_ucity_event:visible option:selected').attr('value');
		$.ajax({
			url : "ucity_get_chart_xml.sw",
			contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
			data : {
				categoryName : categoryName, 
				periodName : periodName, 
				serviceName : serviceName, 
				eventName : eventName, 
			},
			success : function(data, status, jqXHR) {
				loadChartViewer($('#chart_target'), {chartType : 'LINE_CHART', xmlData : data.record});
			},
			error : function(xhr, ajaxOptions, thrownError){
			}
		});
		return false;
	});

	$('.js_select_ucity_service').live('change', function(e) {
		var input = $(targetElement(e));
		var workReport = $('div.js_work_report_page');
		var serviceName = input.find('option:selected').attr('value');
		workReport.find('.js_select_ucity_event[service="' + serviceName + '"]').show().siblings('.js_select_ucity_event').hide();
		return false;
	});

	$('.js_select_situation_manual').live('mouseenter', function(e) {
		var input = $(targetElement(e)).parents('.js_select_situation_manual');
		input.addClass('current').siblings().removeClass('current');
		var taskId = input.attr('taskId');
		var target = $('.js_situation_task[taskId="'+taskId+'"]');
		target.show().siblings().hide();
		return false;
	});
	
	$('.js_modify_situation_manual').live('click', function(e){
		var target = $(targetElement(e)).parents('.js_modify_situation_manual');
		target.hide().siblings().show();
		var workManual = target.parents('.js_pwork_manual_page');
		workManual.find('.js_form_desc_view').hide().next().show();
		return false;
	});

	$('.js_situation_editor_box').live('click', function(e){
		var input = $(targetElement(e));
		var formDescEdit = input.parents('.js_form_desc_edit');
		var formDescText = formDescEdit.find('.js_form_desc_text');
		var formDescEditor = formDescEdit.find('.js_form_desc_editor');
		var formDesc = formDescText.attr('value');
		var fieldName = input.parents('.js_situation_editor_box').attr('fieldName');
		if(input.attr('value') == 'editor' && isEmpty(formDescEditor.html())){
			formDescEdit.find('.js_form_desc_text').hide().attr('name', '');
			var gridRow = SmartWorks.GridLayout.newGridRow();
			var gridTable = SmartWorks.GridLayout.newGridTable();
			formDescEditor.html(gridTable.html(gridRow));

			SmartWorks.FormRuntime.RichEditorBuilder.buildEx({
				container: gridRow,
				fieldId: fieldName,
				fieldName: "",
				columns: 1,
				value: formDesc,
				resizer: false,
				required: false
			});
			gridRow.find('.form_label').hide();
			gridRow.find('.form_value').css({width:"100%"});
			gridRow.find('.form_value > span').css({width:"687px"});
			gridRow.find('#'+ fieldName).css({height:"280px"});
						
		}else if(input.attr('value') == 'text' && !formDescText.is(':visible')){
			formDescEdit.find('.js_form_desc_text').show().attr('name', fieldName);
			formDescEditor.html('');
		}
		return;
	});

	$('.js_cancel_situation_manual').live('click', function(e){
		var target = $(targetElement(e)).parents('.js_cancel_situation_manual');
		target.hide().siblings('.js_modify_situation_manual').show().siblings('.js_save_situation_manual').hide();
		var workManual = target.parents('.js_iwork_manual_page');
		if(isEmpty(workManual)) workManual = target.parents('.js_pwork_manual_page');
		workManual.find('.js_work_desc_view').show().next().hide();
		workManual.find('.js_form_desc_view').show().next().hide();
		return false;
	});
	
	$('.js_save_situation_manual').live('click', function(e){
		submitForms();
		return false;
	});
	
	$('.js_situation_space_reload').live('click', function(e) {
		window.location.reload(true);
		return false;
	});

});
