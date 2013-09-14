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
	
	if(!isEmpty($('.js_now_time_string'))) updateNowString();

	$('a.js_space_tab_index').live('click',function(e) {
		var input = $(targetElement(e)).parents('a:first');
		if(isEmpty(input)) input = $(targetElement(e));
		var target = input.parents('.js_space_instance_list');
		var url = input.attr('href');
		smartPop.progressCont(input.parents('.js_space_tab_page').children('.js_progress_span:first'));
		$.ajax({
			url : url,
			data : {},
			success : function(data, status, jqXHR) {
				target.html(data);
				smartPop.closeProgress();
			},
			error : function(xhr, ajaxOptions, thrownError){
				smartPop.closeProgress();
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
		smartPop.progressCont(input.parents('.js_space_date_scope').children('.js_progress_span:first'));
		$.ajax({
			url : url,
			data : {},
			success : function(data, status, jqXHR) {
				target.html(data);
				smartPop.closeProgress();
			},
			error : function(xhr, ajaxOptions, thrownError){
				smartPop.closeProgress();
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
		var spaceTimeline = input.parents('.js_space_timeline_page');
		var spaceDayly = input.parents('.js_space_dayly_page');
		var spaceWeekly = input.parents('.js_space_weekly_page');
		var spaceMonthly = input.parents('.js_space_monthly_page');
		var spaceInstanceList = input.parents('.js_space_instance_list_page');
		var smartcaster = input.parents('.js_smartcaster_page');
		var myRunningInstanceList = input.parents('.js_my_running_instance_list_page');
		//if(!isEmpty(myRunningInstanceList)) target = target.find('.js_instance_list_table');
		var spacePage = [];
		var toDate = "";
		if(!isEmpty(spaceTimeline)){
			spacePage = spaceTimeline;
			toDate = input.parents('.js_space_timeline').attr('toDate');
		}else if(!isEmpty(spaceDayly)){
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
		}else if(!isEmpty(smartcaster) || !isEmpty(myRunningInstanceList)){
			$.ajax({
				url : "more_smartcast.sw",
				data : {
					fromDate : fromDate,
					maxSize : 10
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
				maxSize : 10
			},
			success : function(data, status, jqXHR) {
				input.parents('li:first').remove();
				target.append($(data).find('li.js_space_sub_instance'));
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
		var maxSize = input.attr('maxSize');
		var target = input.parents('ul:first');
		var spacePage = input.parents('.js_space_instance_list_page');
		var spaceId = spacePage.attr('spaceId');		
		$.ajax({
			url : "more_space_sub_instances.sw",
			data : {
				spaceId : spaceId,
				fromDate : fromDate,
				maxSize : maxSize
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

	$('.js_pop_user_info').live('mouseleave', function(e){
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
		var imageList = input.parents('.js_image_list_page');
		var wid = imageList.find('.js_image_instance_list_page').attr('spaceId');
		var displayType = input.attr('value');
		smartPop.progressCont(imageList.find('.js_image_list_header span.js_progress_span'));
		$.ajax({
			url : "get_image_category_list_page.sw",
			data : {
				displayType : displayType,
				wid : wid,
			},
			success : function(data, status, jqXHR) {
				imageList.find('.js_image_category_list').html(data.listPage);
			},
			error : function(xhr, ajaxOptions, thrownError){
			}
		});

		$.ajax({
			url : "image_instance_list.sw",
			data : {
				displayType : displayType,
				parentId : ""
			},
			success : function(data, status, jqXHR) {
				var target = input.parents('.js_image_list_page').find('.js_image_instance_list');
				target.html(data);
				imageList.find('.js_image_instance_list_page').attr('displayType', displayType).attr('categoryId', "AllFiles");
				if(displayType == '1'){
					imageList.find('.js_add_image_folder_btn').css('visibility', 'visible');
				}else{
					imageList.find('.js_add_image_folder_btn').css('visibility', 'hidden');
				}
				imageList.find('.js_image_select_buttons').hide();
				imageList.find('.js_move_selected_images').hide();
				imageList.find('.js_remove_selected_images').hide();					
				smartPop.closeProgress();
			},
			error : function(xhr, ajaxOptions, thrownError){
				smartPop.closeProgress();
			}
		});
		return false;
		
	});
	
	$('a.js_image_instance_item').live('click', function(e){
		var input = $(targetElement(e)).parents('a');
		var imageInstanceList = input.parents('.js_image_instance_list_page');
		var imageList = input.parents('.js_image_list_page');
		var parentId = input.attr('categoryId');
		var displayType = imageInstanceList.attr('displayType');
		input.parents('.js_image_list_page').find('.js_image_category_list option[value="' + parentId + '"]').attr('selected', 'true');
		smartPop.progressCont(imageInstanceList.parents('.js_image_list_page').find('.js_image_list_header span.js_progress_span'));
		$.ajax({
			url : "image_instance_list.sw",
			data : {
				displayType : displayType,
				parentId : parentId
			},
			success : function(data, status, jqXHR) {
				var target = input.parents('.js_image_list_page').find('.js_image_instance_list');
				target.html(data);
				imageList.find('.js_add_image_folder_btn').css('visibility', 'hidden');
				imageList.find('.js_goto_parent_list').show();
				if(displayType == '1'){
					imageList.find('.js_image_select_buttons').show();
					imageList.find('.js_move_selected_images').show();
					imageList.find('.js_remove_selected_images').show();
				}else{
					imageList.find('.js_image_select_buttons').hide();
					imageList.find('.js_move_selected_images').hide();
					imageList.find('.js_remove_selected_images').hide();					
				}
				smartPop.closeProgress();
			},
			error : function(xhr, ajaxOptions, thrownError){
				smartPop.closeProgress();
			}
		});
		return false;
		
	});

	$('.js_image_category_list').live('change', function(e){
		var input = $(targetElement(e));
		var imageList = input.parents('.js_image_list_page');
		var imageInstanceList = input.parents('.js_image_list_page').find('.js_image_instance_list_page');
		var displayType = imageInstanceList.attr('displayType');
		var parentId = input.find('option:selected').attr('value');
		smartPop.progressCont(imageInstanceList.parents('.js_image_list_page').find('.js_image_list_header span.js_progress_span'));
		$.ajax({
			url : "image_instance_list.sw",
			data : {
				displayType : displayType,
				parentId : parentId
			},
			success : function(data, status, jqXHR) {
				var target = input.parents('.js_image_list_page').find('.js_image_instance_list');
				target.html(data);
				if(displayType == '1'){
					if(parentId == "AllFiles"){
						imageList.find('.js_add_image_folder_btn').css('visibility', 'visible');
						imageList.find('.js_image_select_buttons').hide();
						imageList.find('.js_move_selected_images').hide();
						imageList.find('.js_remove_selected_images').hide();
						imageList.find('.js_goto_parent_list').hide();
					}else{
						imageList.find('.js_add_image_folder_btn').css('visibility', 'hidden');
						imageList.find('.js_image_select_buttons').show();
						imageList.find('.js_move_selected_images').show();
						imageList.find('.js_remove_selected_images').show();						
						imageList.find('.js_goto_parent_list').show();
					}
				}else{
					imageList.find('.js_add_image_folder_btn').css('visibility', 'hidden');
					imageList.find('.js_image_select_buttons').hide();
					imageList.find('.js_move_selected_images').hide();
					imageList.find('.js_remove_selected_images').hide();
					if(parentId == "AllFiles"){
						imageList.find('.js_goto_parent_list').hide();						
					}else{
						imageList.find('.js_goto_parent_list').show();						
					}
				}
				smartPop.closeProgress();
			},
			error : function(xhr, ajaxOptions, thrownError){
				smartPop.closeProgress();
			}
		});
		return false;		
	});
	
	$('.js_goto_parent_list').live('click', function(e){
		var input = $(targetElement(e));
		var imageList = input.parents('.js_image_list_page');
		var imageCategoryList = imageList.find('.js_image_category_list');
		imageCategoryList.find('option[value="AllFiles"]').attr('selected', 'selected');
		imageCategoryList.change();
		return false;
	});
	
	$('.js_file_display_by').live('change', function(e){
		var input = $(targetElement(e));
		var fileList = input.parents('.js_file_list_page');
		var displayType = input.attr('value');
		var wid = fileList.attr("workSpaceId");

		smartPop.progressCont(fileList.find('.js_file_list_header span.js_progress_span'));
		$.ajax({
			url : "get_file_category_list_page.sw",
			data : {
				displayType : displayType,
				wid : wid,
				parentId : ""
			},
			success : function(data, status, jqXHR) {
				fileList.find('.js_file_category_list').html(data.listPage);
				if(displayType === '1'){
					var moveSelectedFiles = fileList.find('.js_move_selected_files');
					var option1 = moveSelectedFiles.children(':first');
					var options = $(data.listPage);
					moveSelectedFiles.html(options);
					moveSelectedFiles.find('option:first').replaceWith(option1);
				}
			},
			error : function(xhr, ajaxOptions, thrownError){
			}
		});

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
				if(displayType == '1'){
					fileList.find('.js_add_file_folder_btn').css('visibility', 'visible');
				}else{
					fileList.find('.js_add_file_folder_btn').css('visibility', 'hidden');			
				}
				fileList.attr('displayType', displayType);
				fileList.attr('categoryId', "AllFiles");
				smartPop.closeProgress();
				selectListParam();
			},
			error : function(xhr, ajaxOptions, thrownError){
				smartPop.closeProgress();
			}
		});
		return false;
		
	});

	$('.js_file_category_item').live('click', function(e){
		var input = $(targetElement(e)).parents('a');
		input.parents('.js_file_list_page').attr('categoryId', input.attr('categoryId'));
		selectListParam();
		return false;		
	});
	
	$('.js_file_category_list').live('change', function(e){
		var input = $(targetElement(e));
		input.parents('.js_file_list_page').attr('categoryId', input.find('option:selected').attr('value'));
		selectListParam();
		return false;		
	});
	
	$('.js_file_category_tree').live('click', function(e){
		var input = $(targetElement(e)).parent();
		if(input.parent().hasClass('lft_fd')) input.parent().removeClass('lft_fd');
		else input.parent().addClass('lft_fd');
		return false;		
	});
		
	$('.js_add_file_folder_btn').live('click', function(e){
		var input = $(targetElement(e));
		var fileList = input.parents('.js_file_list_page');
		var workSpaceId = fileList.attr("workSpaceId");
		var parentId = fileList.attr("categoryId");
		smartPop.createFileFolder(workSpaceId, parentId, null, null);
		return false;
		
	});
		
	$('.js_text_file_folder_btn').live('click', function(e){
		var input = $(targetElement(e));
		var fileList = input.parents('.js_file_list_page');
		var workSpaceId = fileList.attr("workSpaceId");
		var parentId = fileList.attr("categoryId");
		var folderId = input.attr("folderId");
		var folderName = input.attr("folderName");
		smartPop.createFileFolder(workSpaceId, parentId, folderId, folderName);
		return false;
		
	});
		
	$('.js_remove_file_folder_btn').live('click', function(e){
		var input = $(targetElement(e));
		var fileList = input.parents('.js_file_list_page');
		var workSpaceId = fileList.attr("workSpaceId");
		var parentId = fileList.attr("categoryId");
		var folderId = input.attr("folderId");
		smartPop.confirm(smartMessage.get("removeConfirmation"), function(){
			var paramsJson = {};
			paramsJson['workSpaceId'] = workSpaceId;
			paramsJson['parentId'] = parentId;
			paramsJson['folderId'] = folderId;
			smartPop.progressCenter();
			console.log(JSON.stringify(paramsJson));
			$.ajax({
				url : "remove_file_folder.sw",
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					input.parents('li:first').remove();
					smartPop.closeProgress();				
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("removeFileFolderError"), function(){
						smartPop.closeProgress();
					});
				}
			});
		});
		return false;
		
	});
		
	$('.js_move_selected_files').live('change', function(e){
		var input = $(targetElement(e));
		var fileList = input.parents('.js_file_list_page');
		var workSpaceId = fileList.attr("workSpaceId");
		var selectedFiles = fileList.find('.js_file_instance_list_page .js_check_file_instance:checked');
		var targetId = input.find('option:selected').attr('value');
		if(isEmpty(targetId))
			return true;
		
		if(isEmpty(selectedFiles)){
			smartPop.showInfo(smartPop.WARN, smartMessage.get("moveItemsNotSelected"));			
			return false;
		}
		smartPop.confirm(smartMessage.get("moveConfirmation"), function(){
			var paramsJson = {};
			paramsJson['workSpaceId'] = workSpaceId;
			paramsJson['tagetFolderId'] = targetId;
			var instanceIds = new Array();
			for(var i=0; i<selectedFiles.length; i++){
				instanceIds.push($(selectedFiles[i]).attr('value'));
			}
			paramsJson['instanceIds'] = instanceIds;
			smartPop.progressCenter();
			console.log(JSON.stringify(paramsJson));
			$.ajax({
				url : "move_file_instances.sw",
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					window.location.reload(true);
					smartPop.closeProgress();				
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("moveFileInstancesError"), function(){
						smartPop.closeProgress();
					});
				}
			});
		});
		return false;
		
	});

	$('.js_add_image_folder_btn').live('click', function(e){
		var input = $(targetElement(e));
		var ImageList = input.parents('.js_image_list_page');
		var workSpaceId = ImageList.attr("workSpaceId");
		var parentId = ImageList.attr("categoryId");
		smartPop.createImageFolder(workSpaceId, parentId, null, null);
		return false;
		
	});
		
	$('.js_text_image_folder_btn').live('click', function(e){
		var input = $(targetElement(e));
		var imageList = input.parents('.js_image_list_page');
		var workSpaceId = imageList.attr("workSpaceId");
		var parentId = imageList.attr("categoryId");
		var folderId = input.attr("folderId");
		var folderName = input.attr("folderName");
		smartPop.createImageFolder(workSpaceId, parentId, folderId, folderName);
		return false;
		
	});
		
	$('.js_remove_image_folder_btn').live('click', function(e){
		var input = $(targetElement(e));
		var imageList = input.parents('.js_image_list_page');
		var workSpaceId = imageList.attr("workSpaceId");
		var parentId = imageList.attr("categoryId");
		var folderId = input.attr("folderId");
		smartPop.confirm(smartMessage.get("removeConfirmation"), function(){
			var paramsJson = {};
			paramsJson['workSpaceId'] = workSpaceId;
			paramsJson['parentId'] = parentId;
			paramsJson['folderId'] = folderId;
			smartPop.progressCenter();
			console.log(JSON.stringify(paramsJson));
			$.ajax({
				url : "remove_image_folder.sw",
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					input.parents('li:first').remove();
					smartPop.closeProgress();				
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("removeImageFolderError"), function(){
						smartPop.closeProgress();
					});
				}
			});
		});
		return false;
		
	});
		
	$('.js_remove_image_instance_btn').live('click', function(e){
		var input = $(targetElement(e));
		var imageList = input.parents('.js_image_list_page');
		var workSpaceId = imageList.attr("workSpaceId");
		var workId = input.attr("workId");
		var instanceId = input.attr("instanceId");
		smartPop.confirm(smartMessage.get("removeConfirmation"), function(){
			var paramsJson = {};
			paramsJson['workSpaceId'] = workSpaceId;
			paramsJson['instanceId'] = instanceId;
			paramsJson['workId'] = workId;
			smartPop.progressCenter();
			console.log(JSON.stringify(paramsJson));
			$.ajax({
				url : "remove_image_instance.sw",
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					input.parents('li:first').remove();
					smartPop.closeProgress();				
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("removeImageInstanceError"), function(){
						smartPop.closeProgress();
					});
				}
			});
		});
		return false;
		
	});

	$('.js_move_selected_images').live('change', function(e){
		var input = $(targetElement(e));
		var imageList = input.parents('.js_image_list_page');
		var workSpaceId = imageList.attr("workSpaceId");
		var selectedFolder = imageList.find('.js_image_instance_list_page').attr('parentId');
		var selectedImages = imageList.find('.js_image_instance_list_page .js_check_image_instance:checked');
		var targetId = input.find('option:selected').attr('value');
		if(isEmpty(targetId))
			return true;
		
		if(isEmpty(selectedImages)){
			smartPop.showInfo(smartPop.WARN, smartMessage.get("moveItemsNotSelected"));			
			return false;
		}
		smartPop.confirm(smartMessage.get("moveConfirmation"), function(){
			var paramsJson = {};
			paramsJson['workSpaceId'] = workSpaceId;
			paramsJson['tagetFolderId'] = targetId;
			paramsJson['sourceFolderId'] = selectedFolder;
			var instanceIds = new Array();
			for(var i=0; i<selectedImages.length; i++){
				instanceIds.push($(selectedImages[i]).attr('value'));
			}
			paramsJson['instanceIds'] = instanceIds;
			smartPop.progressCenter();
			console.log(JSON.stringify(paramsJson));
			$.ajax({
				url : "move_image_instances.sw",
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					window.location.reload(true);
					smartPop.closeProgress();				
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("moveImageInstancesError"), function(){
						smartPop.closeProgress();
					});
				}
			});
		});
		return false;
		
	});

	$('.js_check_all_image_instance').live('click', function(e){
		var input = $(targetElement(e));
		var imageInstanceList = input.parents('.js_image_list_page').find('.js_image_instance_list_page');
		imageInstanceList.find('.js_check_image_instance').attr('checked', (input.attr('checked')=='checked'));
		return true;
	});
	
	$('.js_check_all_file_instance').live('click', function(e){
		var input = $(targetElement(e));
		var fileInstanceList = input.parents('.js_file_list_page').find('.js_file_instance_list_page');
		fileInstanceList.find('.js_check_file_instance').attr('checked', (input.attr('checked')=='checked'));
		return true;
	});
	
	$('.js_check_file_instance').live('click', function(e){
		e.stopPropagation();
		return true;
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
		var subInstanceList = input.parents('.js_sub_instance_list:first');
		var comment = input.attr('value');
		if(isEmpty(comment)) return false;
		var iworkManual = input.parents('.js_iwork_manual_page');
		var pworkManual = input.parents('.js_pwork_manual_page');
		var newComment = input.parents('.js_new_comment_page');
		var showPicture = input.parents('.js_show_picture_page');
		var workId="", workInstanceId="", workType="", url="";

		if(!isEmpty(showPicture)){
			workInstanceId = showPicture.attr('instanceId');
			workType = showPicture.attr('workType');
		}else if(!isEmpty(iworkManual)){
			workId = iworkManual.attr('workId');
			workType = iworkManual.attr('workType');
		}else if(!isEmpty(pworkManual)){
			workId = pworkManual.attr('workId');
			workType = pworkManual.attr('workType');
		}else if(!isEmpty(newComment)){
			workInstanceId = newComment.attr('instanceId');
			workType = newComment.attr('workType');
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
		smartPop.progressCenter();
		console.log(JSON.stringify(paramsJson));
		$.ajax({
			url : url,
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				if(isEmpty(newComment)){
					var target = subInstanceList.find('.js_comment_list');
					var showAllComments = target.find('.js_show_more_comments');
					if(!isEmpty(showAllComments)){
						showAllComments.find('span').click();
						input.attr('value', '');
					}else{
						var newCommentInstance = target.find('.js_comment_instance').clone().show().removeClass('js_comment_instance');
						newCommentInstance.find('.js_comment_content').html(comment).append("<span class='ml5 icon_new'></span>");
						target.append(newCommentInstance);
						input.attr('value', '');
					}
				}else{
					input.attr('value', '');
					refreshCurrentContent(input.parents('.js_space_instance_list_page'));
					//window.location.reload(true);
				}
				smartPop.closeProgress();				
			},
			error : function(e) {
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.showInfo(smartPop.ERROR, smartMessage.get("addCommentError"), function(){
					smartPop.closeProgress();
				});
				
			}
			
		});
		
	});
	
	$('.js_reply_forward').live('click', function(e) {
		var input = $(targetElement(e));
		var workSpacePage = input.parents('.js_iwork_space_page');
		if(isEmpty(workSpacePage)) workSpacePage = input.parents('.js_pwork_space_page');
		if(isEmpty(workSpacePage)){
			smartPop.showInfo(smartPop.ERROR, smartMessage.get("commentTaskApprovalError"));
			return false;
		}
		var appendTaskForward = workSpacePage.find('.js_append_task_forward_page');
		var appendTaskApproval = workSpacePage.find('.js_append_task_approval_page');
		var comment = appendTaskForward.find('textarea[name="txtaCommentContent"]').attr('value');
		var comment2 = appendTaskApproval.find('textarea[name="txtaCommentContent"]').attr('value');
		if(isEmpty(comment) && isEmpty(comment2)){
			smartPop.showInfo(smartPop.WARN, smartMessage.get("missingCommentTaskForward"));			
			return false;
		}
		smartPop.confirm(smartMessage.get("commentTaskForwardConfirm"), function(){
			var paramsJson = {};
			if(!isEmpty(comment)){
				paramsJson['workInstId'] = appendTaskForward.attr('workInstId');
				paramsJson['forwardId'] = appendTaskForward.attr('forwardId');
				paramsJson['taskInstId'] = appendTaskForward.attr('taskInstId');
				paramsJson['comments'] = comment;
			}else if(!isEmpty(comment2)){
				paramsJson['workInstId'] = appendTaskApproval.attr('workInstId');
				paramsJson['forwardId'] = appendTaskApproval.attr('forwardId');
				paramsJson['taskInstId'] = appendTaskApproval.attr('taskInstId');
				paramsJson['comments'] = comment2;				
			}
			console.log(JSON.stringify(paramsJson));
			var progressSpan = workSpacePage.find('.js_progress_span');
			smartPop.progressCont(progressSpan);
			$.ajax({
				url : "comment_on_task_forward.sw",
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.closeProgress();
					var lastHref = workSpacePage.attr('lastHref');
					if(isEmpty(lastHref))
						window.location.reload(true); 
					else
						refreshCurrentContent(workSpacePage);
						//document.location.href = lastHref; 
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("commentTaskForwardError"), function(){
						smartPop.closeProgress();
					});					
				}
				
			});			
		});
		return false;
	});
	
	$('.js_reply_approval').live('click', function(e) {
		var input = $(targetElement(e)).parents('.js_reply_approval');
		var workSpacePage = input.parents('.js_iwork_space_page');
		if(isEmpty(workSpacePage)) workSpacePage = input.parents('.js_pwork_space_page');
		if(isEmpty(workSpacePage)){
			smartPop.showInfo(smartPop.ERROR, smartMessage.get("commentTaskApprovalError"));
			return false;
		}
		var appendTaskApproval = workSpacePage.find('.js_append_task_approval_page');
		var comment = appendTaskApproval.find('textarea[name="txtaCommentContent"]').attr('value');
		if(isEmpty(comment)){
			smartPop.showInfo(smartPop.WARN, smartMessage.get("missingCommentTaskApproval"));			
			return false;
		}
		smartPop.confirm(smartMessage.get("commentTaskApprovalConfirm"), function(){
			var result = (input.parents().hasClass('js_btn_approve_approval')) ? "approved" 
					: (input.parents().hasClass('js_btn_reject_approval')) ? "rejected" 
					: (input.parents().hasClass('js_btn_submit_approval')) ? "submited" 
					: (input.parents().hasClass('js_btn_return_approval')) ? "returned" : "";
			var paramsJson = {};
			paramsJson['workInstId'] = appendTaskApproval.attr('workInstId');
			paramsJson['approvalInstId'] = appendTaskApproval.attr('approvalInstId');
			paramsJson['taskInstId'] = appendTaskApproval.attr('taskInstId');
			paramsJson['comments'] = comment;
			paramsJson['result'] = result;
			if(result === "submited"){
				var forms = workSpacePage.find('form[name="frmSmartForm"]');
				for(var i=0; i<forms.length; i++){
					var form = $(forms[i]);
					
					// 폼이 스마트폼이면 formId와 formName 값을 전달한다...
					if(form.attr('name') === 'frmSmartForm'){
						paramsJson['formId'] = form.attr('formId');
						paramsJson['formName'] = form.attr('formName');
					}
					
					// 폼이름 키값으로 하여 해당 폼에 있는 모든 입력항목들을 JSON형식으로 Serialize 한다...
					paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
				}
				
			}
			console.log(JSON.stringify(paramsJson));
			var progressSpan = workSpacePage.find('.js_progress_span');
			smartPop.progressCont(progressSpan);
			$.ajax({
				url : "comment_on_task_approval.sw",
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.closeProgress();
					var lastHref =  workSpacePage.attr('lastHref');
					if(isEmpty(lastHref))
						window.location.reload(true);
					else
						refreshCurrentContent(workSpacePage);
						//document.location.href = lastHref;
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("commentTaskApprovalError"), function(){
						smartPop.closeProgress();					
					});
				}
				
			});			
		});
		return false;
	});
	
	$('.js_show_more_comments').live('click', function(e) {
		var input = $(targetElement(e)).parents('.js_show_more_comments');
		var subInstanceList = input.parents('.js_sub_instance_list');
		var href = input.attr('href');
		$.ajax({
			url : href,
			data : {},
			success : function(data, status, jqXHR) {
				var target = subInstanceList.find('.js_comment_list');
				target.children('li:first:visible').replaceWith(data);
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
	
	$('.js_invite_group_members').live('click', function(e){
		var input = $(targetElement(e));
		var spaceId = input.parents('.js_space_profile_page').attr('spaceId');
		smartPop.inviteGroupMembers(spaceId);
		return false;
	});
		
	$('.js_join_group_request').live('click', function(e){
		var input = $(targetElement(e));
		var spaceId = input.parents('.js_space_profile_page').attr('spaceId');
		var isAutoApproval = input.attr('isAutoApproval');
		var paramsJson = {};
		paramsJson['groupId'] = spaceId;
		paramsJson['userId'] = currentUser.userId;	
		console.log(JSON.stringify(paramsJson));
		$.ajax({
			url : "join_group_request.sw",
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				if(isAutoApproval == 'true'){
					smartPop.showInfo(smartPop.INFO, smartMessage.get("joinGroupSucceed"), function(){
						window.location.reload(true);
						smartPop.closeProgress();
					});
				}else{
					smartPop.showInfo(smartPop.INFO, smartMessage.get("joinGroupRequestSucceed"), function(){
						window.location.reload(true);
						smartPop.closeProgress();
					});					
				}
			},
			error : function(e) {
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.showInfo(smartPop.ERROR, smartMessage.get("commentTaskApprovalError"), function(){
					smartPop.closeProgress();					
				});
			}
			
		});
		return false;
	});
	
	$('.js_leave_group_request').live('click', function(e){
		smartPop.confirm(smartMessage.get("leaveGroupConfirmation"), function(){
			var input = $(targetElement(e));
			var spaceProfile = input.parents('.js_space_profile_page');
			var spaceTabGroupMembers = input.parents('.js_space_tab_group_members_page');
			var isGroupLeader = input.attr('isGroupLeader');
			var spaceId = "";
			if(!isEmpty(spaceProfile))
				spaceId = spaceProfile.attr('spaceId');
			else if(!isEmpty(spaceTabGroupMembers))
				spaceId = spaceTabGroupMembers.attr('groupId');
			var paramsJson = {};
			paramsJson['groupId'] = spaceId;
			paramsJson['leaveReason'] = "";	
			console.log(JSON.stringify(paramsJson));
			$.ajax({
				url : "leave_group.sw",
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					document.location.href = "home.sw";
					smartPop.closeProgress();
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("leaveGroupRequestError"), function(){
						smartPop.closeProgress();					
					});
				}
				
			});
		});
		return false;
	});
	
	$('.js_pushout_group_member').live('click', function(e){
		smartPop.confirm(smartMessage.get("pushoutGroupMemberConfirmation"), function(){
			var input = $(targetElement(e));
			var groupId = input.parents('.js_space_tab_group_members_page').attr('groupId');
			var userId = input.attr('memberId');
			var paramsJson = {};
			paramsJson['groupId'] = groupId;
			paramsJson['userId'] = userId;		
			console.log(JSON.stringify(paramsJson));
			smartPop.progressCenter();				
			$.ajax({
				url : 'pushout_group_member.sw',
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					input.parents('.js_space_tab_group_members_page').find('.js_group_members_tab').click();
					smartPop.closeProgress();
				},
				error : function(e) {
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("pushoutGroupMemberError"), function(){
						smartPop.closeProgress();					
					});
				}
			});
		});
		return false;
	});
	
	$('.js_accept_join_group').live('click', function(e){
		var input = $(targetElement(e));
		var groupId = input.parents('.js_space_tab_group_members_page').attr('groupId');
		var userId = input.attr('userId');
		var paramsJson = {};
		paramsJson['groupId'] = groupId;
		paramsJson['userId'] = userId;	
		paramsJson['approval'] = true;
		console.log(JSON.stringify(paramsJson));
		$.ajax({
			url : "approval_join_group.sw",
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				input.parents('.js_space_tab_group_members_page').find('.js_group_members_tab').click();
				smartPop.closeProgress();
			},
			error : function(e) {
				smartPop.showInfo(smartPop.ERROR, smartMessage.get("acceptJoinGroupError"), function(){
					smartPop.closeProgress();					
				});
			}
			
		});
		return false;
	});
	
	$('.js_reject_join_group').live('click', function(e){
		var input = $(targetElement(e));
		var groupId = input.parents('.js_space_tab_group_members_page').attr('groupId');
		var userId = input.attr('userId');
		var paramsJson = {};
		paramsJson['groupId'] = groupId;
		paramsJson['userId'] = userId;	
		paramsJson['approval'] = false;
		console.log(JSON.stringify(paramsJson));
		$.ajax({
			url : "approval_join_group.sw",
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				input.parents('.js_space_tab_group_members_page').find('.js_group_members_tab').click();
				smartPop.closeProgress();
			},
			error : function(e) {
				smartPop.showInfo(smartPop.ERROR, smartMessage.get("rejectJoinGroupError"), function(){
					smartPop.closeProgress();					
				});
			}
			
		});
		return false;
	});
	
	$('.js_toggle_policy_custom').live('change', function(e){
		var input = $(targetElement(e));
		input.nextAll('.js_space_policy_custom:first').toggle();		
	});
	
	$('.js_toggle_builder_policy_custom').live('change', function(e){
		var input = $(targetElement(e));
		input.nextAll('.js_builder_policy_custom:first').toggle();		
	});
	
	$('.js_select_group_space_tab').live('click', function(e){
		var input = $(targetElement(e));
		input.parent().addClass('current').siblings().removeClass('current');
		input.nextAll('.js_space_policy_custom:first').toggle();
		var groupId = input.parents('.js_space_tab_group').attr('groupId');
		var url = "";
		if(input.hasClass('js_setting'))
			url = "space_tab_group_setting.sw";
		else if(input.hasClass('js_members'))
			url = "space_tab_group_members.sw";

		$.ajax({
			url : url,
			data : {groupId : groupId},
			success : function(data, status, jqXHR) {
				input.parents('.js_space_tab_group_target').html(data);
			},
			error : function(e) {
			}
		});
		return false;
	});
	
	$('a.js_group_more_members').live('click',function(e) {
		var input = $(targetElement(e));
		if(!isEmpty(input.siblings('.js_progress_span').find('.js_progress_icon'))) 
			return false;
		smartPop.progressCont(input.siblings('.js_progress_span'));
		var lastId = input.attr('lastId');
		var target = input.parents('ul:first');
		var spacePage = input.parents('.js_space_tab_group_members_page');
		var groupId = spacePage.attr('groupId');		
		$.ajax({
			url : "more_group_members.sw",
			data : {
				groupId : groupId,
				lastId : lastId,
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
		
	$('a.js_more_image_instance_list').live('click',function(e) {
		var input = $(targetElement(e));
		if(!isEmpty(input.siblings('.js_progress_span').find('.js_progress_icon'))) 
			return false;
		smartPop.progressCont(input.siblings('.js_progress_span'));
		var fromDate = input.attr('lastDate');
		var target = input.parents('ul:first');
		var imageInstanceList = input.parents('.js_image_instance_list_page');
		var parentId = imageInstanceList.attr('parentId');
		var displayType = imageInstanceList.attr('displayType');
		$.ajax({
			url : "image_instance_list.sw",
			data : {
				parentId : parentId,
				displayType : displayType,
				lastDate : fromDate
			},
			success : function(data, status, jqXHR) {
				input.parents('div:first').remove();
				target.append(data);
				smartPop.closeProgress();
			},
			error : function(xhr, ajaxOptions, thrownError){
				smartPop.closeProgress();
			}
		});
		return false;
	});
	
	$('.js_toggle_update_histories').live('click', function(e){
		var input = $(targetElement(e));
		if(!input.hasClass('js_toggle_update_histories')) input = input.parents('.js_toggle_update_histories');
		var workSpace = input.parents('.js_iwork_space_page');
		if(isEmpty(workSpace)) workSpace = input.parents('.js_pwork_space_page');
		var target = workSpace.find('.js_instance_histories');
		if(isEmpty(target.children())){
			target.addClass('js_update');
		}else{
			target.html('').hide();
			if(!target.hasClass('js_update')){
				target.addClass('js_update').removeClass('js_download').removeClass('js_forward').removeClass('js_related');
			}else{
				target.removeClass('js_update');
				return false;
			}
		}
		var instanceId = workSpace.attr('instId');
		$.ajax({
			url : 'update_histories.sw',
			data : {
				instanceId : instanceId
			},
			success : function(data, status, jqXHR) {
				target.html(data).show();
				var target_point = $(target).find("div.up_point:first");
				target_point.css({"left": (input.position().left) + "px"});
			},
			error : function(e) {
			}
		});				
		return false;
	});
	
	$('.js_toggle_forward_histories').live('click', function(e){
		var input = $(targetElement(e));
		if(!input.hasClass('js_toggle_forward_histories')) input = input.parents('.js_toggle_forward_histories');
		var workSpace = input.parents('.js_iwork_space_page');
		if(isEmpty(workSpace)) workSpace = input.parents('.js_pwork_space_page');
		var target = workSpace.find('.js_instance_histories');
		
		if(isEmpty(target.children())){
			target.addClass('js_foward');			
		}else{
			target.html('').hide();
			if(!target.hasClass('js_forward')){
				target.addClass('js_forward').removeClass('js_download').removeClass('js_update').removeClass('js_related');
			}else{
				target.removeClass('js_forward');
				return false;
			}
		}
		var instanceId = workSpace.attr('instId');
		$.ajax({
			url : 'forward_histories.sw',
			data : {
				instanceId : instanceId
			},
			success : function(data, status, jqXHR) {
				target.html(data).show();
				var target_point = $(target).find("div.up_point:first");
				target_point.css({"left": (input.position().left) + "px"});
			},
			error : function(e) {
			}
		});
		return false;
	});
	
	$('.js_toggle_download_histories').live('click', function(e){
		var input = $(targetElement(e));
		if(!input.hasClass('js_toggle_download_histories')) input = input.parents('.js_toggle_download_histories');
		var workSpace = input.parents('.js_iwork_space_page');
		if(isEmpty(workSpace)) workSpace = input.parents('.js_pwork_space_page');
		var target = workSpace.find('.js_instance_histories');
		if(isEmpty(target.children())){
			target.addClass('js_download');			
		}else{
			target.html('').hide();
			if(!target.hasClass('js_download')){
				target.addClass('js_download').removeClass('js_update').removeClass('js_forward').removeClass('js_related');
			}else{
				target.removeClass('js_download');
				return false;
			}
		}
		var instanceId = workSpace.attr('instId');
		var taskInstId = workSpace.attr('taskInstId');
		$.ajax({
			url : 'download_histories.sw',
			data : {
				instanceId : instanceId,
				taskInstanceId : taskInstId
			},
			success : function(data, status, jqXHR) {
				target.html(data).show();
				var target_point = $(target).find("div.up_point:first");
				target_point.css({"left": (input.position().left) + "px"});
			},
			error : function(e) {
			}
		});
		return false;
	});
	
	$('.js_toggle_related_instances').live('click', function(e){
		var input = $(targetElement(e));
		if(!input.hasClass('js_toggle_related_instances')) input = input.parents('.js_toggle_related_instances');
		var workSpace = input.parents('.js_iwork_space_page');
		if(isEmpty(workSpace)) workSpace = input.parents('.js_pwork_space_page');
		var target = workSpace.find('.js_instance_histories');
		if(isEmpty(target.children())){
			target.addClass('js_related');			
		}else{
			target.html('').hide();
			if(!target.hasClass('js_related')){
				target.addClass('js_related').removeClass('js_download').removeClass('js_forward').removeClass('js_update');
			}else{
				target.removeClass('js_related');
				return false;
			}
		}
		var instanceId = workSpace.attr('instId');
		$.ajax({
			url : 'related_instances.sw',
			data : {
				instanceId : instanceId
			},
			success : function(data, status, jqXHR) {
				target.html(data).show();
				var target_point = $(target).find("div.up_point:first");
				target_point.css({"left": (input.position().left) + "px"});
			},
			error : function(e) {
			}
		});
		return false;
	});
	
	$('.js_delete_comment_btn').live('click', function(e) {
		smartPop.confirm(smartMessage.get("removeCommentConfirmation"), function(){
			var input = $(targetElement(e));
			var spaceSubInstance = input.parents('.js_space_sub_instance');
			var commentItem = input.parents('.js_sub_instance_list:first');
			var paramsJson = {};
			paramsJson['workType'] = parseInt(spaceSubInstance.attr('workType'));
			paramsJson['workInstanceId'] = spaceSubInstance.attr('instanceId');
			paramsJson['commentId'] = commentItem.attr('instanceId');
			url = "remove_comment_from_instance.sw";
			console.log(JSON.stringify(paramsJson));
			smartPop.progressCenter();				
			$.ajax({
				url : url,
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					var showAllComments = spaceSubInstance.find('.js_show_more_comments');
					if(!isEmpty(showAllComments)){
						showAllComments.find('span').click();
					}else{
						commentItem.remove();
					}
					smartPop.closeProgress();
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("removeCommentError"), function(){
					});
				}
				
			});
		});
		return false;		
	});
	
	$('.instance_list .js_send_mail_to_user').live('click', function(e) {
		var input = $(targetElement(e));
		var receiverId = input.attr('userId');
		smartPop.progressCenter();
		$.get('new_mail.sw?receiverId=' + receiverId, function(data){
			$('#content').html(data);
			smartPop.closeProgress();
		});
		return false;
	});

	$('.instance_list .js_start_chat_with_user').live('click', function(e) {
		var input = $(targetElement(e));
		var receiverId = input.attr('userId');
		smartPop.progressCenter();
		$.get('new_mail.sw?receiverId=' + receiverId, function(data){
			$('#content').html(data);
			smartPop.closeProgress();
		});
		return false;
	});
});
