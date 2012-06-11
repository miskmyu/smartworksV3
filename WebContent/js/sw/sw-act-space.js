$(function() {
	
	var timeOffset = (new Date()).getTimezoneOffset()/60 + parseInt(currentUser.timeOffset);
	var today = new Date();
	today.setTime(today.getTime() + timeOffset*60*60*1000);
	function updateNowString(){
		var now = new Date();
		now.setTime(now.getTime() + timeOffset*60*60*1000);
		if(!(today.getFullYear() == now.getFullYear() && today.getMonth() == now.getMonth() && today.getDate() == now.getDate())){
			today = new Date();
			today.setTime(today.getTime() + timeOffset*60*60*1000);
			$.ajax({url : "localdate_string.sw", success : function(data, status, jqXHR) {
					$('.js_now_date_string').html(data);
				}
			});
		}	
		$('.js_now_time_string').html(now.format("TT h:MM:ss"));		
		setTimeout(function(){
			updateNowString();
		}, 1000);
	};
	updateNowString();

	$('a.js_space_tab_index').live('click',function(e) {
		var input = $(targetElement(e)).parents('a:first');
		if(isEmpty(input)) input = $(targetElement(e));
		var target = input.parents('.js_space_instance_list');
		var url = input.attr('href');
		$.ajax({
			url : url,
			data : {},
			success : function(data, status, jqXHR) {
				target.html(data);
			},
			error : function(xhr, ajaxOptions, thrownError){
			}
		});
		return false;
	});

	$('a.js_space_datepicker_button').live('click', function(e) {
		var input = $(targetElement(e));
		input.parents('.js_space_instance_list').find('.js_space_datepicker').datepicker("show");
		return false;
	});

	$('select.js_space_select_scope').live('change',function(e) {
		var input = $(targetElement(e));
		var target = input.parents('.js_space_instance_list');
		var url = input.find(':selected').attr('value');
		$.ajax({
			url : url,
			data : {},
			success : function(data, status, jqXHR) {
				target.html(data);
			},
			error : function(xhr, ajaxOptions, thrownError){
			}
		});
		return false;
	});
	
	$('a.js_space_more_history').live('click',function(e) {
		var input = $(targetElement(e));
		if(!isEmpty(input.siblings('.js_progress_span').find('.js_progress_icon'))) 
			return false;
		smartPop.progressCont(input.siblings('.js_progress_span'));
		var fromDate = input.attr('lastDate');
		var target = input.parents('ul:first');
		var spaceDayly = input.parents('.js_space_dayly_page');
		var spaceWeekly = input.parents('.js_space_weekly_page');
		var spaceMonthly = input.parents('.js_space_monthly_page');
		var spaceInstanceList = input.parents('.js_space_instance_list_page');
		var smartcaster = input.parents('.js_smartcaster_page');
		var spacePage = [];
		var toDate = "";
		if(!isEmpty(spaceDayly)){
			spacePage = spaceDayly;
			toDate = input.parents('.js_space_dayly_work_hour').attr('toDate');
		}else if(!isEmpty(spaceWeekly)){
			spacePage = spaceWeekly;
			toDate = input.parents('.js_space_weekly_day').attr('toDate');
		}else if(!isEmpty(spaceMonthly)){
			spacePage = spaceMonthly;
			toDate = input.parents('.js_space_monthly_week').attr('toDate');
		}else if(!isEmpty(spaceInstanceList)){
			spacePage = spaceInstanceList;
		}else if(!isEmpty(smartcaster)){
			$.ajax({
				url : "more_smartcast.sw",
				data : {
					fromDate : fromDate,
					maxSize : 20
				},
				success : function(data, status, jqXHR) {
					input.parents('li:first').remove();
					target.append(data);
					smartPop.closeProgress();
				},
				error : function(xhr, ajaxOptions, thrownError){
					smartPop.closeProgress();
				}
			});
			return false;
		}
		
		var contextId = spacePage.attr('contextId');
		var spaceId = spacePage.attr('spaceId');		
		$.ajax({
			url : "more_space_task_histories.sw",
			data : {
				contextId : contextId,
				spaceId : spaceId,
				fromDate : fromDate,
				toDate : toDate,
				maxSize : 20
			},
			success : function(data, status, jqXHR) {
				input.parents('li:first').remove();
				target.append(data);
				smartPop.closeProgress();
			},
			error : function(xhr, ajaxOptions, thrownError){
				smartPop.closeProgress();
			}
		});
		return false;
	});
	
	$('a.js_space_more_instance').live('click',function(e) {
		var input = $(targetElement(e));
		if(!isEmpty(input.siblings('.js_progress_span').find('.js_progress_icon'))) 
			return false;
		smartPop.progressCont(input.siblings('.js_progress_span'));
		var fromDate = input.attr('lastDate');
		var target = input.parents('ul:first');
		var spacePage = input.parents('.js_space_instance_list_page');
		var spaceId = spacePage.attr('spaceId');		
		$.ajax({
			url : "more_space_sub_instances.sw",
			data : {
				spaceId : spaceId,
				fromDate : fromDate,
				maxSize : 20
			},
			success : function(data, status, jqXHR) {
				input.parents('li:first').remove();
				target.append(data);
				smartPop.closeProgress();
			},
			error : function(xhr, ajaxOptions, thrownError){
				smartPop.closeProgress();
			}
		});
		return false;
	});
	
	$('a.js_view_instance_diagram').live('click',function(e) {
		var input = $(targetElement(e));
		input.parent().hide().next().show();
		var pworkSpace = input.parents('.js_pwork_space_page');
		var target = pworkSpace.find('.js_process_instance_viewer');
		var instanceId = pworkSpace.attr('instId');
		loadInstanceViewer(target, {
				instanceId : instanceId
		});
		target.show();
		return false;
	});

	$('a.js_close_instance_diagram').live('click',function(e) {
		var input = $(targetElement(e));
		input.parent().hide().prev().show();
		var pworkSpace = input.parents('.js_pwork_space_page');
		var target = pworkSpace.find('.js_process_instance_viewer');
		target.hide().html('');
		return false;
	});
	
	var userInfoTimer = null;
	var onUserInfoTimer = null;
	$('.js_pop_user_info').live('mouseenter', function(e){
		if(userInfoTimer!=null){
			clearTimeout(userInfoTimer);
			userInfoTimer = null;
		}
		if(onUserInfoTimer!=null){
			clearTimeout(onUserInfoTimer);
			onUserInfoTimer = null;
		}

		var input = $(targetElement(e)).parents('.js_pop_user_info');
		if(input.attr('userId') === currentUser.userId){
			smartPop.closeUserInfo();
			return;
		}
		onUserInfoTimer = setTimeout(function(){
			onUserInfoTimer = null;
			var picture = input.find('img');
			var top = picture.offset().top+ picture.height();
			var scrollHeight = $(window).scrollTop() + window.innerHeight;
			var directionUp = true;
			var popUserInfo = $('#sw_pop_user_info');
			if((top+popUserInfo.height()) > scrollHeight){
				top = picture.offset().top;
				directionUp = false;
			}
			var left = picture.offset().left + picture.width()/2;
			smartPop.showUserInfo(input, top, left, directionUp);		
		}, 1000);
		
	});

	$('a.js_pop_user_info').live('mouseleave', function(e){
		if(onUserInfoTimer!=null){
			clearTimeout(onUserInfoTimer);
			onUserInfoTimer = null;
			return;
		}
		userInfoTimer = setTimeout(function(){
			smartPop.closeUserInfo();
			userInfoTimer = null;
		}, 300);
	});
	
	$('#sw_pop_user_info').live('mouseenter', function(e){
		if(onUserInfoTimer!=null){
			clearTimeout(onUserInfoTimer);
			onUserInfoTimer = null;
		}
		if(userInfoTimer!=null){
			clearTimeout(userInfoTimer);
			userInfoTimer = null;
		}		
	});

	$('#sw_pop_user_info').live('mouseleave', function(e){
		if(onUserInfoTimer!=null){
			clearTimeout(onUserInfoTimer);
			onUserInfoTimer = null;
			return;
		}
		userInfoTimer = setTimeout(function(){
			smartPop.closeUserInfo();
			userInfoTimer = null;
		}, 300);
	});
	
	$('.js_image_display_by').live('change', function(e){
		var input = $(targetElement(e));
		var displayType = input.attr('value');
		$.ajax({
			url : "image_instance_list.sw",
			data : {
				displayType : displayType,
				parentId : ""
			},
			success : function(data, status, jqXHR) {
				var target = input.parents('.js_image_list_page').find('.js_image_instance_list');
				target.html(data);
			},
			error : function(xhr, ajaxOptions, thrownError){
			}
		});
		return false;
		
	});
	$('a.js_image_instance_list').live('click', function(e){
		var input = $(targetElement(e)).parents('a');
		var imageInstanceList = input.parents('.js_image_instance_list_page');
		var parentId = input.attr('categoryId');
		var displayType = imageInstanceList.attr('displayType');
		$.ajax({
			url : "image_instance_list.sw",
			data : {
				displayType : displayType,
				parentId : parentId
			},
			success : function(data, status, jqXHR) {
				var target = input.parents('.js_image_list_page').find('.js_image_instance_list');
				target.html(data);
			},
			error : function(xhr, ajaxOptions, thrownError){
			}
		});
		return false;
		
	});
	
	$('.js_file_display_by').live('change', function(e){
		var input = $(targetElement(e));
		var fileList = input.parents('.js_file_list_page');
		var displayType = input.attr('value');
		var wid = fileList.attr("workSpaceId");
		$.ajax({
			url : "categories_by_type.sw",
			data : {
				displayType : displayType,
				wid : wid,
				parentId : ""
			},
			success : function(data, status, jqXHR) {
				var target = fileList.find('.js_file_categories');
				target.html(data);
				fileList.attr('displayType', displayType);
				fileList.attr('categoryId', "AllFiles");
				selectListParam();
			},
			error : function(xhr, ajaxOptions, thrownError){
			}
		});
		return false;
		
	});
	$('.js_file_category_list').live('click', function(e){
		var input = $(targetElement(e)).parents('a');
		input.parents('.js_file_list_page').attr('categoryId', input.attr('categoryId'));
		selectListParam();
		return false;		
	});
	
	$('a.js_file_instance_list').live('click', function(e){
		var input = $(targetElement(e)).parents('a');
		var fileList = input.parents('.js_file_list_page');
		var categoryId = input.attr('categoryId');
		var displayType = fileList.attr('displayType');
		$.ajax({
			url : "set_file_instance_list.sw",
			data : {
				displayType : displayType,
				catetoryId : categoryId
			},
			success : function(data, status, jqXHR) {
				var target = fileList.find('.js_file_instance_list');
				target.html(data);
			},
			error : function(xhr, ajaxOptions, thrownError){
			}
		});
		return false;
		
	});
	
	$('.js_return_on_comment').live('keydown', function(e) {
		var e = window.event || e;
		var keyCode = e.which || e.keyCode;
		if(e.shiftKey && keyCode==$.ui.keyCode.SHIFT){ return true;
		}else if(e.shiftKey && keyCode==$.ui.keyCode.ENTER){
			e.keyCode = $.ui.keyCode.ENTER;
			e.which = $.ui.keyCode.ENTER;
			return true;
		}else if(keyCode != $.ui.keyCode.ENTER){
			return;
		}
		var input = $(targetElement(e));
		var subInstanceList = input.parents('.js_sub_instance_list');
		var comment = input.attr('value');
		if(isEmpty(comment)) return false;
		var iworkManual = input.parents('.js_iwork_manual_page');
		var pworkManual = input.parents('.js_pwork_manual_page');
		var workId="", workInstanceId="", workType="", url="";
		if(!isEmpty(iworkManual)){
			workId = iworkManual.attr('workId');
			workType = iworkManual.attr('workType');
		}else if(!isEmpty(pworkManual)){
			workId = pworkManual.attr('workId');
			workType = pworkManual.attr('workType');
		}else{
			workInstanceId = input.parents('li:first').attr('instanceId');
			workType = input.parents('li:first').attr('workType');
		}
		var paramsJson = {};
		paramsJson['workType'] = parseInt(workType);
		if(!isEmpty(workId)){
			paramsJson['workId'] = workId;
			url = "add_comment_on_work.sw";
		}else if(!isEmpty(workInstanceId)){
			paramsJson['workInstanceId'] = workInstanceId;
			url = "add_comment_on_instance.sw";
		}
		paramsJson['comment'] = comment;
		console.log(JSON.stringify(paramsJson));
		$.ajax({
			url : url,
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				var target = subInstanceList.find('.js_comment_list');
				var showAllComments = target.find('.js_show_all_comments');
				if(!isEmpty(showAllComments)){
					showAllComments.find('span').click();
					input.attr('value', '');
				}else{
					var newComment = target.find('.js_comment_instance').clone().show().removeClass('js_comment_instance');
					newComment.find('.js_comment_content').html(comment).append("<span class='icon_new'></span>");
					target.append(newComment);
					input.attr('value', '');
				}
			},
			error : function(e) {
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.showInfo(smartPop.ERROR, smartMessage.get("addCommentError"), function(){
				});
				
			}
			
		});
		
	});
	
	$('.js_return_on_forward').live('keydown', function(e) {
		var e = window.event || e;
		var keyCode = e.which || e.keyCode;
		if(e.shiftKey && keyCode==$.ui.keyCode.SHIFT){ return true;
		}else if(e.shiftKey && keyCode==$.ui.keyCode.ENTER){
			e.keyCode = $.ui.keyCode.ENTER;
			e.which = $.ui.keyCode.ENTER;
			return true;
		}else if(keyCode != $.ui.keyCode.ENTER){
			return;
		}
		var input = $(targetElement(e));
		var appendTaskForward = input.parents('.js_append_task_forward_page');
		var comment = input.attr('value');
		if(isEmpty(comment)) return false;
		smartPop.confirm(smartMessage.get("commentTaskForwardConfirm"), function(){
			var paramsJson = {};
			paramsJson['workInstId'] = appendTaskForward.attr('workInstId');
			paramsJson['forwardId'] = appendTaskForward.attr('forwardId');
			paramsJson['taskInstId'] = appendTaskForward.attr('taskInstId');
			paramsJson['comments'] = comment;
			console.log(JSON.stringify(paramsJson));
			$.ajax({
				url : "comment_on_task_forward.sw",
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.INFO, smartMessage.get("commentTaskForwardSucceed"), function(){
						document.location.href = "";
	 					smartPop.close();
					});
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("commentTaskForwardError"), function(){
						smartPop.close();					
					});
				}
				
			});			
		});
	});
	
	$('.js_reply_forward').live('click', function(e) {
		var input = $(targetElement(e));
		var appendTaskForward = input.parents('.js_iwork_space_page').find('.js_append_task_forward_page');
		var comment = appendTaskForward.find('textarea[name="txtaCommentContent"]').attr('value');
		if(isEmpty(comment)) return false;
		smartPop.confirm(smartMessage.get("commentTaskForwardConfirm"), function(){
			var paramsJson = {};
			paramsJson['workInstId'] = appendTaskForward.attr('workInstId');
			paramsJson['forwardId'] = appendTaskForward.attr('forwardId');
			paramsJson['taskInstId'] = appendTaskForward.attr('taskInstId');
			paramsJson['comments'] = comment;
			console.log(JSON.stringify(paramsJson));
			$.ajax({
				url : "comment_on_task_forward.sw",
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.INFO, smartMessage.get("commentTaskForwardSucceed"), function(){
						document.location.href = "";
	 					smartPop.close();
					});
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("commentTaskForwardError"), function(){
	 					smartPop.close();
					});
					
				}
				
			});			
		});
		return false;
	});
	
	$('.js_show_all_comments').live('click', function(e) {
		var input = $(targetElement(e)).parents('.js_show_all_comments');
		var subInstanceList = input.parents('.js_sub_instance_list');
		var href = input.attr('href');
		$.ajax({
			url : href,
			data : {},
			success : function(data, status, jqXHR) {
				var target = subInstanceList.find('.js_comment_list');
				target.find(':visible').remove();
				target.append(data);
				input.remove();
			},
			error : function(e) {
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.showInfo(smartPop.ERROR, smartMessage.get("addCommentError"), function(){
				});
				
			}
			
		});
		return false;
	});
	
	$('a.js_add_comment').live('click', function(e){
		var input = $(targetElement(e)).parents('a.js_add_comment').removeAttr('href').addClass('no_hover_line');
		input.find('.t_action').addClass('t_action_disabled');
		input.parents('.js_action_btns').prev('.js_comments_box').show().find('.js_return_on_comment').show();
		
		return false;
	});

	$('a.js_add_like').live('click', function(e){
		var input = $(targetElement(e)).parents('a.js_add_like').removeAttr('href').addClass('no_hover_line');
		input.find('.t_action').addClass('t_action_disabled');
		
	});
	
});
