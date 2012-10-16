$(function() {
	
	/*
	 * 어디에서든 class 값이 js_container로 지정된 anchor 가 선택이 되면, anchor 의 href 값으로 ajax 를 호출하여
	 * 가져온 값을 container(메인컨텐트)화면에 보여준다.
	 */
	$('.js_ucity_content').swnavi({
		history : true,
		before : function(event){
			smartPop.progressCenter();				
		},
		target : 'content',
		after : function(event){
			smartPop.closeProgress();
		}
	});
	
	$('.js_select_situation_report').live('click', function(e) {
		var input = $(targetElement(e));
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
				target.html(data).slideDown(500);
				loadChartViewer($('#chart_target'));
			},
			error : function(xhr, ajaxOptions, thrownError){
			}
		});
		
		return false;
	});

});
