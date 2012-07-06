
	$('.js_more_list').live('click', function(e) {
		var anchor = $(targetElement(e));
		if(!isEmpty(anchor.siblings('.js_progress_span').find('.js_progress_icon'))) 
			return false;
		smartPop.progressCont(anchor.siblings('.js_progress_span'));
		var runningPage = anchor.parents('.js_my_running_instance_list_page');
		var lastDate = runningPage.find('.js_more_instance_item:last').attr('dateValue');
		var viewType = runningPage.find('.js_view_my_instances:current').attr('viewType');
		var assignedOnly = (viewType == 'assigned_instances');
		var runningOnly = (viewType == 'running_instances');
		$.ajax({
			url : anchor.attr('href'),
			data : {
				lastDate : lastDate,
				assignedOnly : assignedOnly,
				runningOnly : runningOnly
			},
			success : function(data, status, jqXHR) {
				anchor.remove();
				$(data).appendTo(runningPage.find('.js_instance_list_table'));
				smartPop.closeProgress();
			},
			error : function(xhr, ajaxOptions, thrownError){
				smartPop.closeProgress();				
			}
		});
	
		return false;
	});

	$(window).scroll( function() {
		var more_anchor = $('#work_ing .js_more_list a');
		var more_smartcaster = $('.js_smartcaster_page a.js_space_more_history');
		var more_smartcaster_home = $('.js_my_running_instance_list_page a.js_space_more_history');
		if ($(window).scrollTop() == $(document).height() - $(window).height()){
			
			if(!isEmpty(more_anchor) && !more_anchor.isWaiting){
				more_anchor.isWaiting = true;
				setTimeout(function() {
					if ($(window).scrollTop() == $(document).height() - $(window).height())
						more_anchor.trigger('click');
					more_anchor.isWaiting = false;
				}, 2000);
			}else if(!isEmpty(more_smartcaster) && !more_smartcaster.isWaiting){
				more_smartcaster.isWaiting = true;
				setTimeout(function() {
					if ($(window).scrollTop() == $(document).height() - $(window).height())
						more_smartcaster.trigger('click');
					more_smartcaster.isWaiting = false;
				}, 2000);
			}else if(!isEmpty(more_smartcaster_home) && !more_smartcaster_home.isWaiting){
				more_smartcaster_home.isWaiting = true;
				setTimeout(function() {
					if ($(window).scrollTop() == $(document).height() - $(window).height())
						more_smartcaster_home.trigger('click');
					more_smartcaster_home.isWaiting = false;
				}, 2000);
			}
		}
	});
