$(function() {
	
	var autoPictures = $('img.js_auto_picture');
	if(!isEmpty(autoPictures)) {
		for(var i=0; i<autoPictures.length; i++) {
			if(isEmpty($(autoPictures[i]).next().find('div.js_file_uploader'))) continue;
			createUploader(null, $(autoPictures[i]).next().find('div.js_file_uploader'), false, true);
		}		
	}

	var autoLoadProfiles = $('div.js_auto_load_profile');
	if(!isEmpty(autoLoadProfiles)) {
		for(var i=0; i<autoLoadProfiles.length; i++) {			
			loadMyProfileField();
		}		
	}

	$('.js_select_action a').live('click',function(e) {
		var input = $(targetElement(e));
		$('.js_select_action').find('a').removeClass('current');
		var currentAction = input.parents('.up_icon_list');
		currentAction.find('a').addClass('current');
		var url = input.attr('href');
		var target = $('.js_upload_form');
		$.ajax({
			url : url,
			data : {},
			success : function(data, status, jqXHR) {
				target.html(data).show();
				target.find('.js_up_pointer').css({"left": currentAction.position().left + currentAction.outerWidth()/2 + "px"});
			},
			error : function(xhr, ajaxOptions, thrownError){
				
			}
		});
		return false;
	});

	$('a.js_cancel_action').live('click',function(e) {

		if ($('.js_select_action').find('a:first').length != 0) {
			var input = $('.js_select_action').find('a:first');
			if(isEmpty(input)) return false;
			$('.js_select_action').find('a').removeClass('current');
			var currentAction = input.parents('.up_icon_list');
			currentAction.find('a').addClass('current');
			var target = $('.js_upload_form');
			var url = input.attr('href');
			$.ajax({
				url : url,
				data : {},
				success : function(data, status, jqXHR) {
					target.html(data).show();
					$(data).find('.js_up_pointer').css({"left": (currentAction.position().left + currentAction.outerWidth()/2) + "px"});
				},
				error : function(xhr, ajaxOptions, thrownError){
				}
			});
		} else if ($(targetElement(e)).parents('.js_work_list_page').find('div.js_new_work_form').length != 0 ){
			var input = $(targetElement(e)).parents('.js_work_list_page').find('div.js_new_work_form');
			if(isEmpty(input)) return false;
			var workform = input.parents('.js_work_list_page').find('div.js_new_work_form');
			if(isEmpty(workform)) return false;
			workform.slideUp();
		}
		
		return false;
	});
	
	$('.js_click_start_form').live('click', function(e){
		var input = $(targetElement(e)).parents('.js_click_start_form:first');
		var newMemo = input.parents('.js_new_memo_page');
		var newPicture = input.parents('.js_new_picture_page');
		var newFile = input.parents('.js_new_file_page');
		var newEvent = input.parents('.js_new_event_page');
		var newBoard = input.parents('.js_new_board_page');
		var newIWork = input.parents('.js_new_iwork_page');
		var startPWork = input.parents('.js_start_pwork_page');
		var planSWork = input.parents('.js_plan_swork_page');
		var workId = "";
		var target = [];
		if(!isEmpty(newMemo)){
			input.find('textarea').removeClass('bn').addClass('up_textarea');
			newMemo.find('form[name="frmNewMemo"]').addClass('form_title');
			workId = newMemo.attr('workId');
			target = newMemo.find('.js_upload_buttons');
		}else if(!isEmpty(newPicture)){
			newPicture.find('tr').show();
			newPicture.find('form[name="frmNewPicture"]').addClass('form_title');
			workId = newPicture.attr('workId');
			target = newPicture.find('.js_upload_buttons');
		}else if(!isEmpty(newFile)){
			newFile.find('td[fieldId="txtFileField"] .form_label').show();
			newFile.find('tr').show();
			newFile.find('.js_file_detail_form').show();
			newFile.find('form[name="frmNewFile"]').addClass('form_title');
			workId = newFile.attr('workId');
			target = newFile.find('.js_upload_buttons');
		}else if(!isEmpty(newEvent)){
			newEvent.find('td[fieldId="txtEventName"] .form_label').show();
			newEvent.find('td[fieldId="txtEventName"] .form_value input').addClass('fieldline');
			newEvent.find('tr').show();
			newEvent.find('form[name="frmNewEvent"]').addClass('form_title');
			workId = newEvent.attr('workId');
			target = newEvent.find('.js_upload_buttons');
		}else if(!isEmpty(newBoard)){
			newBoard.find('td[fieldId="txtBoardName"] .form_label').show();
			newBoard.find('td[fieldId="txtBoardName"] .form_value input').addClass('fieldline');
			newBoard.find('tr').show();
			newBoard.find('form[name="frmNewBoard"]').addClass('form_title');
			workId = newBoard.attr('workId');
			target = newBoard.find('.js_upload_buttons');
		}else if(!isEmpty(newIWork)){
			workId = newIWork.attr('workId');
			target = newIWork.find('.js_upload_buttons');
		}else if(!isEmpty(startPWork)){
			workId = startPWork.attr('workId');
			target = startPWork.find('.js_upload_buttons');
		}else if(!isEmpty(planSWork)){
			workId = planSWork.attr('workId');
			target = planSWork.find('.js_upload_buttons');
		}
		if(isEmpty(target) || !isEmpty(target.html())) return;
		$.ajax({
			url : 'upload_buttons.sw',
			data : {
				workId : workId
			},
			success : function(data, status, jqXHR) {
				target.html(data);
				if(!isEmpty(newBoard) && newBoard.attr('seraBoard')==="true"){
					target.find('.js_select_work_space').hide();
					target.find('.js_select_access_level option.js_access_level_custom').remove();
					
				}else if(!isEmpty(newEvent) && newEvent.attr('seraEvent')==="true"){
					var selectWorkSpace = target.find('.js_select_work_space');
					selectWorkSpace.find('.js_optgroup_department').remove();
					selectWorkSpace.find('.js_optgroup_group').attr('label', '나의 코스공간');					
					target.find('.js_select_access_level option.js_access_level_custom').remove();					
				}
			},
			error : function(xhr, ajaxOptions, thrownError){
				
			}
		});			
		return true;
	});

	/*
	 * 세업무시작하기에서, 입력창에 값을 입력하여 나오는 검색결과를 선택하면 실행되는 이벤트로, 검색결과항목의 href값으로 ajax를
	 * 실행하여 가져온 값으로 id가 form_works 인 곳 화면을 그려서, 아래로 펼쳐준다.
	 */
	$('.js_select_work').swnavi({
		before : function(event) {
			smartPop.progressCenter();
			$('#form_works').html('');
			$(targetElement(event)).parents(".js_start_work_page").hide();
		},
		target : 'form_works',
		after : function(e) {
			var input = $(targetElement(e)).parents('li:first').children('a');
			var formContent = $('#form_works').find('div.js_form_content');
			var workId = input.attr('workId');
			new SmartWorks.GridLayout({
				target : formContent,
				mode : "edit",
				first : true,
				workId : workId,
				onSuccess : function(){
					$('#form_works').parent().show();
					var startPwork = formContent.parents('.js_start_pwork_page');
					if(!isEmpty(startPwork)){
						var approvalLineId = startPwork.attr("approvalLineId");
						var formTaskApproval = formContent.siblings('.js_form_task_approval');
						if(!isEmpty(approvalLineId)){
							$.ajax({
								url : 'append_task_approval.sw',
								data : { 
									approvalLineId : approvalLineId
								},
								success : function(data, status, jqXHR) {
									formTaskApproval.html(data).show();
									smartPop.closeProgress();																		
								},
								error : function(xhr, ajaxOptions, thrownError){					
									smartPop.closeProgress();																		
								}
							});
							
						}else{
							smartPop.closeProgress();																		
						}
					}else{
						smartPop.closeProgress();											
					}					
				},
				onError : function(){
					smartPop.closeProgress();					
				}
			});
		}
	});

//	$('.js_toggle_form_detail').live('click', function(event){
//		var input = $(event.target);
//		input.parent().hide().siblings().show();
//		var formContent = $('#form_works').find('div.js_form_content');
//		if(isEmpty(formContent)) formContent = input.parents('.js_work_list_page').find('div.js_form_content');
//		var workId = input.attr('workId');
//		var requiredOnly = input.attr('requiredOnly');
//		formContent.html('');
//		new SmartWorks.GridLayout({
//			target : formContent,
//			mode : "edit",
//			requiredOnly : requiredOnly,						
//			workId : workId
//		});
//		return false;
//	});
	
	$('input.js_toggle_schedule_work').live('click', function(e) {
		var input = $(targetElement(e));
		var target = $(targetElement(e)).parent().next('span');
		if(input.is(':checked')){
			loadCheckScheduleFields();
			target.show();
		}else{
			target.find('.js_check_schedule_fields').html('');
			target.hide();
		}
	});
	
	var ACCESS_LEVEL_CUSTOM = 2;
	$('select.js_select_access_level').live('change', function(e) {
		var input = $(targetElement(e));
		var target = input.parents('.js_upload_buttons_page').find('div.js_access_level_custom');
		if(isEmpty(target)){
			target = input.parents('.js_iwork_space_page').find('div.js_access_level_custom');
			if(isEmpty(target)){
				target = input.parents('.js_import_from_excel_page').find('div.js_access_level_custom');				
			}
		}
		var accessLevel = input.attr('value');
		if(accessLevel == ACCESS_LEVEL_CUSTOM)
			target.show();
		else
			target.hide();
	});
	
	$('select.js_select_work_space').live('change', function(e) {
		var input = $(targetElement(e));
		var target = input.parents('form[name="frmAccessSpace"]').find('input[name="selWorkSpaceType"]');
		target.attr('value', input.find('option:selected').attr('workSpaceType'));
	});
	
	$('a.js_create_new_work').live('click', function(e) {
		var input = $(targetElement(e));
		var url = input.attr('href');
		var target = input.parents('.js_work_list_page').find('div.js_new_work_form');
		$('a.js_search_filter_close').click();
		$('a.js_excel_import_close').click();
		$.ajax({
			url : url,
			data : {},
			success : function(data, status, jqXHR) {
				target.html(data).slideDown(500);
				var formContent = target.find('div.js_form_content');
				var workId = input.attr('workId');
				new SmartWorks.GridLayout({
					target : formContent,
					mode : "edit",
					workId : workId
				});
				var startPwork = formContent.parents('.js_start_pwork_page');
				if(!isEmpty(startPwork)){
					var approvalLineId = startPwork.attr("approvalLineId");
					var formTaskApproval = formContent.siblings('.js_form_task_approval');
					if(!isEmpty(approvalLineId)){
						$.ajax({
							url : 'append_task_approval.sw',
							data : { 
								approvalLineId : approvalLineId
							},
							success : function(data, status, jqXHR) {
								formTaskApproval.html(data).show();
							},
							error : function(xhr, ajaxOptions, thrownError){					
							}
						});
					}
				}					

			}
			
		});
		return false;
	});

	$('input.js_whole_day').live('click', function(e){
		var input = $(targetElement(e));
		input.parent().siblings('div.js_start_time').toggle();
		var endtime = input.parent().siblings('div.js_end_datetime').find('div.js_end_time').hide();
		if(input[0].checked) endtime.hide();
		else endtime.show();
	});
	$('a.js_toggle_file_detail').swnavi(
		{
			target : 'form_import',
			after : function(e) {
				var input = $(targetElement(e));
				input.parents('.js_file_detail_form').parent().prev().slideToggle(500);
				input.parent().toggle().siblings().toggle();
				var form = input.parents('form[name="frmNewFile"]');
				var uploader = form.find('.qq-uploader');
				var comments = form.find('textarea[name="txtFileDesc"]').attr("value");
				var groupId = uploader.attr('groupId');
				var fileList = uploader.find('.qq-upload-list li');
				var fileName = $(fileList[0]).attr('fileName');
				if(isEmpty(fileName))
					fileName = "";

				$('#error_message_span').html("");
				
				var formContent = $('#form_import').find('div.js_form_content');
				if(!isEmpty(formContent)) {
					var workId = formContent.attr('workId');
					$.ajax({
						url : "get_form_xml.sw",
						data : {
							workId : workId
						},
						success : function(formXml, status, jqXHR) {
							var record = createFileDataFields({
									formXml : formXml,
									groupId : groupId,
									fileName : fileName,
									fileList : fileList,
									comments : comments								
							});
							new SmartWorks.GridLayout({
								target : formContent,
								formXml : formXml,
								formValues : record,
								mode : "edit"
							});
						},
						error : function(xhr, ajaxOptions, thrownError){
							
						}
					});
				}
			}
		});

	$('a.js_view_work_manual').live('click', function(e){
		var input = $(targetElement(e));
		var target = input.parents("div.js_work_list_page").find('#work_manual').slideToggle(500);
		input.hide();
		input.siblings().show();

		var href = input.attr('href');
		if(isEmpty(href)){
			target.html('');
		}else{
			var progressSpan = input.prev('span.js_progress_span');
			smartPop.progressCont(progressSpan);
			$.ajax({
				url : href,
				data : {},
				success : function(data, status, jqXHR) {
					target.html(data);
					var pworkManual = $('.js_pwork_manual_page');
					if(isEmpty(pworkManual)){
						smartPop.closeProgress();
						return;
					}
					var manualTasksHolder = pworkManual.find(".js_manual_tasks_holder");
					var manualTasks = manualTasksHolder.find(".js_manual_tasks");
					var manualTasksRight = pworkManual.find('.js_manual_tasks_right');
					pworkManual.find('.js_manual_tasks_left').hide();
					for(var i=0; i<manualTasks.length; i++){
						var manualTask = $(manualTasks[i]);
						if(manualTask.position().top>=manualTask.height())
							break;
					}
					if(i<manualTasks.length)
						manualTasksRight.show();
					else
						manualTasksRight.hide();
					
					pworkManual.find('a.js_select_task_manual:first div:first').click();
					
					smartPop.closeProgress();
				},
				error : function(xhr, ajaxOptions, thrownError){
					smartPop.closeProgress();					
				}
			});
		}
		return false;
	});

	$('.js_select_task_manual').live('click', function(e){
		var input = $(targetElement(e));
		if(!input.hasClass('js_manual_task')) input = input.parents('.js_manual_task:first');
		var target = $("#"+input.attr("taskId"));
		var target_point = $(target).find("div.up_point:first");
		var selectedManualTask = input;
		input.parents('.js_pwork_manual_page').find('.js_manual_task').removeClass('selected');
		selectedManualTask.addClass('selected');
		target_point.css({"left": (selectedManualTask.position().left + selectedManualTask.outerWidth()/2) + "px"});
		$(target).show().siblings('div.js_task_manual').hide();
		return false;
	});
	
	$('a.js_manual_tasks_right').live('click', function(e){
		var input = $(targetElement(e)).parents('a:first');
		var pworkManual = input.parents('.js_pwork_manual_page');
		var manualTasksHolder = pworkManual.find('.js_manual_tasks_holder');
		var manualLeft = pworkManual.find('.js_manual_tasks_left');	
		var manualRight = pworkManual.find('.js_manual_tasks_right');

		var tasksVisible = manualTasksHolder.find(".js_manual_task:visible");
		var viewWidth = manualTasksHolder.width();
		var tasksOverflew = new Array();
		for(var i=0; i<tasksVisible.length; i++){
			var task  =$(tasksVisible[i]);
			if(task.position().top>=task.height())
				tasksOverflew.push(tasksVisible[i]);
		}
		if(isEmpty(tasksOverflew) || tasksOverflew.length==0){
			manualRight.hide();
			return false;
		}

		var overflewWidth = 0;
		for(var i=0; i<tasksOverflew.length; i++){
			overflewWidth = overflewWidth + $(tasksOverflew[i]).outerWidth() + 10;
			if(overflewWidth>viewWidth/2) break;
		}

		for(var i=0; i<tasksVisible.length && overflewWidth>0; i++){
			var task = $(tasksVisible[i]);
			overflewWidth = overflewWidth - task.outerWidth() - 10;
			task.hide();
		}

		var tasksHidden = manualTasksHolder.find(".js_manual_task:hidden");
		if(isEmpty(tasksHidden))
			manualLeft.hide();
		else
			manualLeft.show();
		
		tasksVisible = manualTasksHolder.find(".js_manual_task:visible");
		for(var i=0; i<tasksVisible.length; i++){
			var task = $(tasksVisible[i]);
			if(task.position().top>=task.height())
				break;
		}
		if(tasksVisible.length==0 || i==tasksVisible.length){
			manualRight.hide();
		}else{
			manualRight.show();
		}
		
		if(tasksVisible.length>0)
			$(tasksVisible[i-1]).click();
		return false;
	});
	
	$('a.js_manual_tasks_left').live('click', function(e){

		var input = $(targetElement(e)).parents('a:first');
		var pworkManual = input.parents('.js_pwork_manual_page');
		var manualTasksHolder = pworkManual.find('.js_manual_tasks_holder');
		var manualLeft = pworkManual.find('.js_manual_tasks_left');	
		var manualRight = pworkManual.find('.js_manual_tasks_right');

		var tasksHidden = manualTasksHolder.find(".js_manual_task:hidden");
		var viewWidth = manualTasksHolder.width();
		if(isEmpty(tasksHidden) || tasksHidden.length==0){
			manualLeft.hide();
			return false;
		}

		var hiddenWidth = 0;
		for(var i=tasksHidden.length-1; i>=0; i--){
			var task = $(tasksHidden[i]).show();
			hiddenWidth = hiddenWidth + task.outerWidth() + 10;
			if(hiddenWidth>viewWidth/2) break;
		}

		if(i>0)
			manualLeft.show();
		else
			manualLeft.hide();
		
		tasksVisible = manualTasksHolder.find(".js_manual_task:visible");
		for(var i=0; i<tasksVisible.length; i++){
			var task = $(tasksVisible[i]);
			if(task.position().top>=task.height())
				break;
		}
		if(tasksVisible.length==0 || i==tasksVisible.length){
			manualRight.hide();
		}else{
			manualRight.show();
		}

		if(tasksVisible.length>0)
			$(tasksVisible[0]).click();
		return false;
	});
	
	$('.js_select_task_instance').live("click", function(e){
		smartPop.progressCenter();
		var input = $(targetElement(e));
		if(!input.hasClass('js_instance_task')) input = input.parents('.js_instance_task:first');
		input.parents('.js_pwork_space_page').find('.js_subprocess_space').hide().find('.js_subprocess_diagram').html('');
		clickOnTask(input);
		smartPop.closeProgress();
		return false;
	});

	$('.js_select_subtask_instance').live("click", function(e){
		smartPop.progressCenter();
		var input = $(targetElement(e));
		if(!input.hasClass('js_instance_task')) input = input.parents('.js_instance_task:first');
		var pworkSpace = input.parents('.js_pwork_space_page');
		var subprocessSpace = pworkSpace.find('.js_subprocess_space').show();
		subprocessSpace.find('.js_form_content_pointer').css({"left": input.position().left + input.outerWidth()/2 + "px"});
//TODO	
		smartPop.closeProgress();
		return false;
	});

	$('a.js_instance_tasks_right').live('click', function(e){
		var input = $(targetElement(e)).parents('a:first');
		var pworkSpace = input.parents('.js_pwork_space_page');
		var instanceTasksHolder = pworkSpace.find('.js_instance_tasks_holder');
		var instanceLeft = pworkSpace.find('.js_instance_tasks_left');	
		var instanceRight = pworkSpace.find('.js_instance_tasks_right');
		pworkSpace.find('.js_subprocess_space').hide().find('.js_subprocess_diagram').html('');
		
		var tasksVisible = instanceTasksHolder.find(".js_instance_task:visible");
		var arrowsVisible = instanceTasksHolder.find('.js_instance_task_arrow:visible');
		var viewWidth = instanceTasksHolder.width();
		var tasksOverflew = new Array();
		for(var i=0; i<tasksVisible.length; i++){
			var task  =$(tasksVisible[i]);
			if(task.position().top>=task.height())
				tasksOverflew.push(tasksVisible[i]);
		}
		if(isEmpty(tasksOverflew) || tasksOverflew.length==0){
			instanceRight.hide();
			return false;
		}

		var overflewWidth = 0;
		for(var i=0; i<tasksOverflew.length; i++){
			overflewWidth = overflewWidth + $(tasksOverflew[i]).outerWidth() + $(arrowsVisible[0]).outerWidth() + 10;
			if(overflewWidth>viewWidth/2) break;
		}

		for(var i=0; i<tasksVisible.length && overflewWidth>0; i++){
			var task = $(tasksVisible[i]);
			var arrow = $(arrowsVisible[i]);
			overflewWidth = overflewWidth - task.outerWidth() - arrow.outerWidth() - 10;
			task.hide();
			arrow.hide();
		}

		var tasksHidden = instanceTasksHolder.find(".js_instance_task:hidden");
		if(isEmpty(tasksHidden))
			instanceLeft.hide();
		else
			instanceLeft.show();
		
		tasksVisible = instanceTasksHolder.find(".js_instance_task:visible");
		for(var i=0; i<tasksVisible.length; i++){
			var task = $(tasksVisible[i]);
			if(task.position().top>=task.height())
				break;
		}
		if(tasksVisible.length==0 || i==tasksVisible.length){
			instanceRight.hide();
		}else{
			instanceRight.show();
		}
		
		if(tasksVisible.length>0)
			$(tasksVisible[i-1]).click();
		return false;
	});
	
	$('a.js_instance_tasks_left').live('click', function(e){
		
		var input = $(targetElement(e)).parents('a:first');
		var pworkSpace = input.parents('.js_pwork_space_page');
		var instanceTasksHolder = pworkSpace.find('.js_instance_tasks_holder');
		var instanceLeft = pworkSpace.find('.js_instance_tasks_left');	
		var instanceRight = pworkSpace.find('.js_instance_tasks_right');
		pworkSpace.find('.js_subprocess_space').hide().find('.js_subprocess_diagram').html('');

		var tasksHidden = instanceTasksHolder.find(".js_instance_task:hidden");
		var arrowsHidden = instanceTasksHolder.find('.js_instance_task_arrow:hidden');
		var viewWidth = instanceTasksHolder.width();
		if(isEmpty(tasksHidden) || tasksHidden.length==0){
			instanceLeft.hide();
			return false;
		}

		var hiddenWidth = 0;
		for(var i=tasksHidden.length-1; i>=0; i--){
			var task = $(tasksHidden[i]).show();
			var arrow = $(arrowsHidden[i]).show();
			hiddenWidth = hiddenWidth + task.outerWidth() + arrow.outerWidth() + 10;
			if(hiddenWidth>viewWidth/2) break;
		}

		if(i>0)
			instanceLeft.show();
		else
			instanceLeft.hide();
		
		tasksVisible = instanceTasksHolder.find(".js_instance_task:visible");
		for(var i=0; i<tasksVisible.length; i++){
			var task = $(tasksVisible[i]);
			if(task.position().top>=task.height())
				break;
		}
		if(tasksVisible.length==0 || i==tasksVisible.length){
			instanceRight.hide();
		}else{
			instanceRight.show();
		}

		if(tasksVisible.length>0)
			$(tasksVisible[0]).click();
		return false;
	});

	$('a.js_modify_iwork_instance').live('click', function(e){
		var input = $(targetElement(e));
		var iworkSpace = input.parents('.js_iwork_space_page');
		var workId = iworkSpace.attr("workId");
		var instId = iworkSpace.attr("instId");
		var formContent = iworkSpace.find('div.js_form_content');
		formContent.html('');
		formContent.removeClass('list_contents');			
		new SmartWorks.GridLayout({
			target : formContent,
			mode : "edit",
			workId : workId,
			recordId : instId
		});
		iworkSpace.find('.js_btn_modify').hide();
		iworkSpace.find('.js_btn_delete').hide();
		iworkSpace.find('.js_btn_save').show();
		iworkSpace.find('.js_btn_cancel').show();
		iworkSpace.find('form[name="frmAccessSpace"]').show();
		return false;
	});

	$('a.js_cancel_iwork_instance').live('click', function(e){
		var input = $(targetElement(e));
		var iworkSpace = input.parents('.js_iwork_space_page');
		var workId = iworkSpace.attr("workId");
		var instId = iworkSpace.attr("instId");
		var formContent = iworkSpace.find('div.js_form_content');
		iworkSpace.find('.js_form_task_approval').hide().html('');
		iworkSpace.find('.js_form_task_forward').hide().html('');
		iworkSpace.find('.js_form_task_email').hide().html('');
		formContent.html('');
		formContent.addClass('list_contents');
		new SmartWorks.GridLayout({
			target : formContent,
			mode : "view",
			workId : workId,
			recordId : instId
		});
		showErrors();
		iworkSpace.find('.js_btn_modify').show().siblings().hide();
		iworkSpace.find('.js_btn_delete').show();
		iworkSpace.find('form[name="frmAccessSpace"]').hide();
		return false;
	});

	$('a.js_save_iwork_instance').live('click', function(e){
		var input = $(targetElement(e));
		var iworkSpace = input.parents('.js_iwork_space_page');
		var workId = iworkSpace.attr("workId");
		var instId = iworkSpace.attr("instId");
		var formContent = iworkSpace.find('div.js_form_content');
		var approvers = iworkSpace.find('.js_approval_box input[type="hidden"]');
		if(!isEmpty(approvers)){
			for(var i=0; i<approvers.length; i++){
				var approver = $(approvers[i]);
				var autoComplete = approver.parents('.js_approval_box').find('.js_auto_complete');
				if(isEmpty(approver.attr('value'))) autoComplete.addClass('required');
				else autoComplete.removeClass('required');
			}
		}
		// iwork_instance 에 있는 활성화되어 있는 모든 입력화면들을 validation하여 이상이 없으면 submit를 진행한다...
		if (!SmartWorks.GridLayout.validate(iworkSpace.find('form.js_validation_required'), $('.js_space_error_message'))) return false;
		
		smartPop.confirm(smartMessage.get("saveConfirmation"), function(){
			var forms = iworkSpace.find('form');
			var paramsJson = {};
			paramsJson['workId'] = workId;
			paramsJson['instanceId'] = instId;
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
			console.log(JSON.stringify(paramsJson));
			var url = "set_iwork_instance.sw";
			
			// 서비스요청 프로그래스바를 나타나게 한다....
			var progressSpan = iworkSpace.find('.js_progress_span');
			smartPop.progressCont(progressSpan);
			
			// set_iwork_instance.sw서비스를 요청한다..
			$.ajax({
				url : url,
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					
					// 성공시에 프로그래스바를 제거하고 성공메시지를 보여준다...
					formContent.html('');
					formContent.addClass('list_contents');
					new SmartWorks.GridLayout({
						target : formContent,
						mode : "view",
						workId : workId,
						recordId : instId,
						onSuccess : function(){
							iworkSpace.find('.js_btn_modify').show();
							iworkSpace.find('.js_btn_delete').show();
							iworkSpace.find('.js_btn_save').hide();
							iworkSpace.find('.js_btn_cancel').hide();
							iworkSpace.find('form[name="frmAccessSpace"]').hide();
							smartPop.closeProgress();								
						},
						onError : function(){
							smartPop.closeProgress();																
						}
					});
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.closeProgress();
					if(e.responseText === "duplicateKeyException")
						smartPop.showInfo(smartPop.ERROR, smartMessage.get("duplicateKeyException"));
					else
						smartPop.showInfo(smartPop.ERROR, smartMessage.get("setIWorkInstanceError"), function(){
						return false;
					});
					
				}
			});
		},
		function(){
			return false;
		});
		return false;
	});

	$('a.js_forward_work_instance').live('click', function(e){
		var input = $(targetElement(e));
		var workSpacePage = input.parents('.js_iwork_space_page');
		if(isEmpty(workSpacePage)) workSpacePage = input.parents('.js_pwork_space_page');
		if(isEmpty(workSpacePage)){
			smartPop.showInfo(smartPop.ERROR, smartMessage.get("forwardWorkInstanceError"));
			return false;
		}
		var workId = workSpacePage.attr("workId");
		var instId = workSpacePage.attr("instId");
		// iwork_instance 에 있는 활성화되어 있는 모든 입력화면들을 validation하여 이상이 없으면 submit를 진행한다...
		if (!SmartWorks.GridLayout.validate(workSpacePage.find('.js_form_task_forward form'), $('.js_space_error_message'))) return false;
		
		smartPop.confirm(smartMessage.get("forwardConfirmation"), function(){
			var forms = workSpacePage.find('form');
			var isIworkView = (workSpacePage.hasClass('js_iwork_space_page') && isEmpty(workSpacePage.find('form[name="frmAccessSpace"]:visible')));
			var paramsJson = {};
			paramsJson['workId'] = workId;
			paramsJson['instanceId'] = instId;
			for(var i=0; i<forms.length; i++){
				var form = $(forms[i]);
				
				// 폼이 스마트폼이면 formId와 formName 값을 전달한다...
				if(form.attr('name') === 'frmSmartForm'){
					if(isIworkView) continue;
					paramsJson['formId'] = form.attr('formId');
					paramsJson['formName'] = form.attr('formName');
				}
				
				// 폼이름 키값으로 하여 해당 폼에 있는 모든 입력항목들을 JSON형식으로 Serialize 한다...
				paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
			}
			console.log(JSON.stringify(paramsJson));
			var url = workSpacePage.hasClass('js_iwork_space_page') ? "forward_iwork_instance.sw" : "forward_pwork_instance.sw";			
			
			// 서비스요청 프로그래스바를 나타나게 한다....
			var progressSpan = workSpacePage.find('.js_progress_span');
			smartPop.progressCont(progressSpan);
			
			// set_iwork_instance.sw서비스를 요청한다..
			$.ajax({
				url : url,
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					
					smartPop.closeProgress();
					refreshCurrentContent(workSpacePage);
					//window.location.reload(true);
					return false;
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("forwardWorkInstanceError"), function(){
						return false;
					});
					smartPop.closeProgress();
				}
			});
		},
		function(){
			return false;
		});
		return false;
	});

	$('a.js_approval_work_instance').live('click', function(e){
		var input = $(targetElement(e));
		var workSpacePage = input.parents('.js_iwork_space_page');
		if(isEmpty(workSpacePage)) workSpacePage = input.parents('.js_pwork_space_page');
		if(isEmpty(workSpacePage)){
			smartPop.showInfo(smartPop.ERROR, smartMessage.get("approvalWorkInstanceError"));
			return false;
		}
		var workId = workSpacePage.attr("workId");
		var instId = workSpacePage.attr("instId");
		var approvers = workSpacePage.find('.js_approval_box input[type="hidden"]');
		if(!isEmpty(approvers)){
			for(var i=0; i<approvers.length; i++){
				var approver = $(approvers[i]);
				var autoComplete = approver.parents('.js_approval_box').find('.js_auto_complete');
				if(isEmpty(approver.attr('value'))) autoComplete.addClass('required');
				else autoComplete.removeClass('required');
			}
		}
		// iwork_instance 에 있는 활성화되어 있는 모든 입력화면들을 validation하여 이상이 없으면 submit를 진행한다...
		if (!SmartWorks.GridLayout.validate(workSpacePage.find('.js_form_task_approval form'), $('.js_space_error_message'))) return false;
		
		smartPop.confirm(smartMessage.get("approvalConfirmation"), function(){
			var forms = workSpacePage.find('form');
			var paramsJson = {};
			paramsJson['workId'] = workId;
			paramsJson['instanceId'] = instId;
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
			console.log(JSON.stringify(paramsJson));
			var url = workSpacePage.hasClass('js_iwork_space_page') ? "approval_iwork_instance.sw" : "approval_pwork_instance.sw";			
			
			// 서비스요청 프로그래스바를 나타나게 한다....
			var progressSpan = workSpacePage.find('.js_progress_span');
			smartPop.progressCont(progressSpan);
			
			// set_iwork_instance.sw서비스를 요청한다..
			$.ajax({
				url : url,
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					
					smartPop.closeProgress();
					refreshCurrentContent(workSpacePage);
					//window.location.reload(true);
					return false;
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("forwardWorkInstanceError"), function(){
						return false;
					});
					smartPop.closeProgress();					
				}
			});
		},
		function(){
			return false;
		});
		return false;
	});

	$('a.js_delete_iwork_instance').live('click', function(e){
		smartPop.confirm(smartMessage.get('removeConfirmation'), function(){
			var input = $(targetElement(e));
			var iworkSpace = input.parents('.js_iwork_space_page');
			var repeatEventId = iworkSpace.attr('repeatEventId');
			var removeAllRepeatEvent = false;
			var workId = iworkSpace.attr("workId");
			var instId = iworkSpace.attr("instId");
			var paramsJson = {};
			paramsJson['workId'] = workId;
			paramsJson['instanceId'] = instId;
			if(!isEmpty(repeatEventId)){
				smartPop.confirm(smartMessage.get('removeAllRepeatConfirmation'), function(){
					paramsJson['repeatEventId'] = repeatEventId;
					console.log(JSON.stringify(paramsJson));
					var url = "remove_iwork_instance.sw";
					
					// 서비스요청 프로그래스바를 나타나게 한다....
					var progressSpan = iworkSpace.find('.js_progress_span');
					smartPop.progressCont(progressSpan);
					
					// set_iwork_instance.sw서비스를 요청한다..
					$.ajax({
						url : url,
						contentType : 'application/json',
						type : 'POST',
						data : JSON.stringify(paramsJson),
						success : function(data, status, jqXHR) {
							
							// 정보관리업무 목록 페이지로 이동한다.....
							smartPop.closeProgress();
							$('.js_goto_work_list_btn:first').click();
						},
						error : function(e) {
							// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
							smartPop.showInfo(smartPop.ERROR, smartMessage.get("removeIWorkInstanceError"), function(){
								return false;
							});
							smartPop.closeProgress();					
						}
					});
				}, function(){
					console.log(JSON.stringify(paramsJson));
					var url = "remove_iwork_instance.sw";
					
					// 서비스요청 프로그래스바를 나타나게 한다....
					var progressSpan = iworkSpace.find('.js_progress_span');
					smartPop.progressCont(progressSpan);
					
					// set_iwork_instance.sw서비스를 요청한다..
					$.ajax({
						url : url,
						contentType : 'application/json',
						type : 'POST',
						data : JSON.stringify(paramsJson),
						success : function(data, status, jqXHR) {
							
							// 정보관리업무 목록 페이지로 이동한다.....
							smartPop.closeProgress();
							$('.js_goto_work_list_btn:first').click();
						},
						error : function(e) {
							// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
							smartPop.showInfo(smartPop.ERROR, smartMessage.get("removeIWorkInstanceError"), function(){
								return false;
							});
							smartPop.closeProgress();					
						}
					});					
				});
			}else{
				console.log(JSON.stringify(paramsJson));
				var url = "remove_iwork_instance.sw";
				
				// 서비스요청 프로그래스바를 나타나게 한다....
				var progressSpan = iworkSpace.find('.js_progress_span');
				smartPop.progressCont(progressSpan);
				
				// set_iwork_instance.sw서비스를 요청한다..
				$.ajax({
					url : url,
					contentType : 'application/json',
					type : 'POST',
					data : JSON.stringify(paramsJson),
					success : function(data, status, jqXHR) {
						
						// 정보관리업무 목록 페이지로 이동한다.....
						smartPop.closeProgress();
						$('.js_goto_work_list_btn:first').click();
					},
					error : function(e) {
						// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
						smartPop.showInfo(smartPop.ERROR, smartMessage.get("removeIWorkInstanceError"), function(){
							return false;
						});
						smartPop.closeProgress();					
					}
				});
			}
			
		},
		function(){
			return false;
		});
		return false;
	});

	$('a.js_perform_task_instance').live('click', function(e){
		var input = $(targetElement(e)).parents('a.js_perform_task_instance');
		var pworkSpace = input.parents('.js_pwork_space_page');
		var workId = pworkSpace.attr("workId");
		var instId = pworkSpace.attr("instId");
		var formContent = pworkSpace.find('div.js_form_content');
		var taskInstId = formContent.attr("taskInstId");
		// iwork_instance 에 있는 활성화되어 있는 모든 입력화면들을 validation하여 이상이 없으면 submit를 진행한다...
		if (!SmartWorks.GridLayout.validate(formContent.find('form.js_validation_required'), $('.js_space_error_message'))) return false;
		
		smartPop.confirm(smartMessage.get("performConfirmation"), function(){
			var forms = formContent.find('form');
			var paramsJson = {};
			paramsJson['workId'] = workId;
			paramsJson['instanceId'] = instId;
			paramsJson['taskInstId'] = taskInstId;
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
			console.log(JSON.stringify(paramsJson));
			var url = "perform_task_instance.sw";
			
			// 서비스요청 프로그래스바를 나타나게 한다....
			var progressSpan = pworkSpace.find('.js_progress_span');
			smartPop.progressCont(progressSpan);
			
			// perform_task_instance.sw서비스를 요청한다..
			$.ajax({
				url : url,
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					
					// 성공시에 프로그래스바를 제거하고 성공메시지를 보여준다...
					var lastHref = pworkSpace.attr('lastHref');
					if(isEmpty(lastHref))
						document.location.href = "pwork_list.sw?cid=pw.li." + workId;
					else
						document.location.href = lastHref; 
					smartPop.closeProgress();
					return;
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("performTaskInstanceError"), function(){
						return;
					});
					smartPop.closeProgress();					
				}
			});
		},
		function(){
			return false;
		});
		return false;
	});

	$('a.js_return_task_instance').live('click', function(e){
		var input = $(targetElement(e)).parents('a.js_return_task_instance');
		var pworkSpace = input.parents('.js_pwork_space_page');
		var workId = pworkSpace.attr("workId");
		var instId = pworkSpace.attr("instId");
		var formContent = pworkSpace.find('div.js_form_content');
		var taskInstId = formContent.attr("taskInstId");
		
		smartPop.confirm(smartMessage.get("returnConfirmation"), function(){
			var forms = formContent.find('form');
			var paramsJson = {};
			paramsJson['workId'] = workId;
			paramsJson['instanceId'] = instId;
			paramsJson['taskInstId'] = taskInstId;
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
			console.log(JSON.stringify(paramsJson));
			var url = "return_task_instance.sw";
			
			// 서비스요청 프로그래스바를 나타나게 한다....
			var progressSpan = pworkSpace.find('.js_progress_span');
			smartPop.progressCont(progressSpan);
			
			// perform_task_instance.sw서비스를 요청한다..
			$.ajax({
				url : url,
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					
					// 성공시에 프로그래스바를 제거하고 성공메시지를 보여준다...
					var lastHref = pworkSpace.attr('lastHref');
					if(isEmpty(lastHref))
						document.location.href = "pwork_list.sw?cid=pw.li." + workId;
					else
						document.location.href = lastHref; 
					smartPop.closeProgress();
					return;
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("returnTaskInstanceError"), function(){
						return;
					});
					smartPop.closeProgress();					
				}
			});
		},
		function(){
			return false;
		});
		return false;
	});

	$('a.js_temp_save_task_instance').live('click', function(e){
		var input = $(targetElement(e)).parents('a.js_temp_save_task_instance');
		var pworkSpace = input.parents('.js_pwork_space_page');
		var workId = pworkSpace.attr("workId");
		var instId = pworkSpace.attr("instId");
		var formContent = pworkSpace.find('div.js_form_content');
		var taskInstId = formContent.attr("taskInstId");
		
		smartPop.confirm(smartMessage.get("tempSaveConfirmation"), function(){
			var forms = formContent.find('form');
			var paramsJson = {};
			paramsJson['workId'] = workId;
			paramsJson['instanceId'] = instId;
			paramsJson['taskInstId'] = taskInstId;
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
			console.log(JSON.stringify(paramsJson));
			var url = "temp_save_task_instance.sw";
			
			// 서비스요청 프로그래스바를 나타나게 한다....
			var progressSpan = pworkSpace.find('.js_progress_span');
			smartPop.progressCont(progressSpan);
			
			// perform_task_instance.sw서비스를 요청한다..
			$.ajax({
				url : url,
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					
					// 성공시에 프로그래스바를 제거하고 성공메시지를 보여준다...
					smartPop.closeProgress();
					return;
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					if(e.responseText === "duplicateKeyException")
						smartPop.showInfo(smartPop.ERROR, smartMessage.get("duplicateKeyException"));
					else
						smartPop.showInfo(smartPop.ERROR, smartMessage.get("tempSaveTaskInstanceError"), function(){
						return;
					});
					smartPop.closeProgress();					
				}
			});
		},
		function(){
			return false;
		});
		return false;
	});

	$('a.js_reassign_task_instance').live('click', function(e){
		var input = $(targetElement(e)).parents('a.js_reassign_task_instance');
		var pworkSpace = input.parents('.js_pwork_space_page');
		var workId = pworkSpace.attr("workId");
		var instId = pworkSpace.attr("instId");
		var formContent = pworkSpace.find('div.js_form_content');
		var taskInstId = formContent.attr("taskInstId");
		smartPop.reassignPerformer(workId, instId, taskInstId);
		return false;
	});

	$('a.js_delete_pwork_instance').live('click', function(e){
		smartPop.confirm(smartMessage.get('removeConfirmation'), function(){
			var input = $(targetElement(e));
			var pworkSpace = input.parents('.js_pwork_space_page');
			var workId = pworkSpace.attr("workId");
			var instId = pworkSpace.attr("instId");
			var lastHref = pworkSpace.attr("lastHref");

			var paramsJson = {};
			paramsJson['workId'] = workId;
			paramsJson['instanceId'] = instId;
			console.log(JSON.stringify(paramsJson));
			var url = "remove_pwork_instance.sw";
			
			// 서비스요청 프로그래스바를 나타나게 한다....
			var progressSpan = pworkSpace.find('.js_progress_span');
			smartPop.progressCont(progressSpan);
			
			// set_iwork_instance.sw서비스를 요청한다..
			$.ajax({
				url : url,
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					
					// 정보관리업무 목록 페이지로 이동한다.....
					smartPop.closeProgress();
					document.location.href = lastHref;					
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("removeIWorkInstanceError"), function(){
						return false;
					});
					smartPop.closeProgress();					
				}
			});
			
		},
		function(){
			return false;
		});
		return false;
	});

	$('input.js_file_upload').live('change', function(e) {
		var input = $(targetElement(e));
		var newInput = document.createElement( 'input' );
		newInput.type = 'file';
		$(newInput).addClass('js_file_upload');
		
		input.parent().add( newInput);
//		e.target.style.display = 'none';
		var target = input.parent().next('div.js_selected_files');
		var oldHTML = target.html();
		if (oldHTML == null)
			oldHTML = "";		
		var newHTML = oldHTML
		+ "<span class='js_file_item user_select' >"
		+ input.attr('value')
		+ "<span class='btn_x_gr'><a class='js_remove_file' href=''> x</a></span></span>";
		target.html(newHTML);
		$(target).find('span.js_file_item').add(targetElement(e));
	});

	$('.qq-delete-text').live('click', function(e) {
		$.ajax({
			url : "delete_file.sw",
			data : {
				fileId : $(targetElement(e)).parent('li').attr('fileId'),
				fileName : $(targetElement(e)).siblings('a').attr('filename')
			},
			type : "POST",
			context : this,
			success : function(data, status, jqXHR) {
				$(targetElement(e)).parent().remove();
			},
			error : function(xhr, ajaxOptions, thrownError){
				
			}
		});
		return false;
	});

	$('a#js_copy_address').zclip({
        path: 'resources/flash/ZeroClipboard.swf',
        copy: function(){return document.location.href;}
    });

	$('.js_pop_all_works').live('click', function(e) {
		var startWork = $(targetElement(e)).parents('.js_start_work_page');
		var target = startWork.find('.js_all_work_popup');
		var width = startWork.find('.js_auto_complete:first').parent().outerWidth();
		smartPop.selectWork(target, width);
		return false;
	});

	$('a.js_todaypicker_button').live('click', function(e) {
		var input = $(targetElement(e)).parent();
		input.prevAll('.js_todaypicker').datepicker("show");
		return false;
	});

	$('a.js_timepicker_button').live('click', function(e) {
		var input = $(targetElement(e)).parent();
		input.prevAll('.js_timepicker').timepicker("show");
		return false;
	});
	
	$('a.js_todaytimepicker_button').live('click', function(e) {
		var input = $(targetElement(e)).parent();
		input.prevAll('.js_todaytimepicker').datetimepicker("show");
		return false;
	});

	var MAX_USERPICKER_HEIGHT = 330;
	$('a.js_userpicker_button').live('click', function(e) {
		var input = $(targetElement(e)).parents('.js_userpicker_button');
		if(input.hasClass('js_selected_approver_info')){
			var appendTaskApproval = input.parents('.js_append_task_approval_page');
			var approvalLineBox = appendTaskApproval.find('.js_approval_line_box');
			var inputPosition = input.position(); 
			var approvalLineBoxPosition = approvalLineBox.position(); 
			target = approvalLineBox.nextAll('.js_community_popup');
			listWidth = 360;
			listTop = inputPosition.top + input.height();
			listLeft = inputPosition.left;
			var widthGap = listWidth - (approvalLineBox.width() - (inputPosition.left - approvalLineBoxPosition.left));
			if(widthGap>0)
				listLeft = listLeft-widthGap;
			
			target.css({ "top" : listTop + "px"});
			target.css({ "left" : listLeft + "px"});
			target.css({ "position" : "absolute"});
			smartPop.selectUser(input, target, listWidth, false);			
		}else if(!isEmpty(input.parents('.js_search_filter_page'))){
			var userField = $(targetElement(e)).parents('.js_type_userField:first');
			var communityItems = userField.find('.js_community_item');
			var target = userField.find('.js_community_popup:first');
			smartPop.selectUser(communityItems, target, 360, false);
		}else{
			var userField = $(targetElement(e)).parents('.js_type_userField:first');
			var communityItems = userField.find('.js_community_item');
			var target = userField.find('.js_community_popup:first');
			var width = userField.find('.form_value').find('div:first').width();
			var isMultiUsers = userField.attr('multiUsers');

			var documentHeight = (document.height !== undefined) ? document.height : document.body.offsetHeight;
			if($.browser.msie) documentHeight = document.body.scrollHeight;
			var inputPosition = input.position();
			var inputOffset = input.offset();
			var listTop = inputOffset.top + input.height();
			var bottomUp = false;
			if(listTop + MAX_USERPICKER_HEIGHT > documentHeight){
				target.css({ "bottom" : inputPosition.top + "px"});
				target.css({ "position" : "absolute"});
				bottomUp = true;
			}
			smartPop.selectUser(communityItems, target, width, isMultiUsers, null, null, bottomUp);
		}
		return false;
	});

	$('a.js_emailpicker_button').live('click', function(e) {
		var userField = $(targetElement(e)).parents('.js_type_userField:first');
		var communityItems = userField.find('.js_community_item');
		var target = userField.find('.js_community_popup:first');
		var width = userField.find('.form_value').find('div:first').width();
		var isMultiUsers = userField.attr('multiUsers');

		var documentHeight = (document.height !== undefined) ? document.height : document.body.offsetHeight;
		var inputPosition = userField.position();
		var inputOffset = userField.offset();
		var listTop = inputOffset.top + userField.height();
		var bottomUp = false;
//		if(listTop + MAX_USERPICKER_HEIGHT > documentHeight){
//			target.css({ "bottom" : inputPosition.top + "px"});
//			target.css({ "position" : "absolute"});
//			bottomUp = true;
//		}
		smartPop.selectEmailAddress(communityItems, target, width, isMultiUsers, bottomUp);
		return false;
	});

	$('a.js_departpicker_button').live('click', function(e) {
		var departField = $(targetElement(e)).parents('.js_type_departmentField:first');
		var communityItems = departField.find('.js_community_item');
		var target = departField.find('.js_community_popup:first');
		var width = departField.find('.form_value').find('div:first').width();

		var documentHeight = (document.height !== undefined) ? document.height : document.body.offsetHeight;
		var inputPosition = departField.position();
		var inputOffset = departField.offset();
		var listTop = inputOffset.top + departField.height();
		var bottomUp = false;
//		if(listTop + MAX_USERPICKER_HEIGHT > documentHeight){
//			target.css({ "bottom" : inputPosition.top + "px"});
//			target.css({ "position" : "absolute"});
//			bottomUp = true;
//		}
		smartPop.selectDepartment(communityItems, target, width, bottomUp);
		return false;
	});

	$('a.js_workitempicker_button').live('click', function(e) {
		var target = $(targetElement(e)).parents('td.js_type_refFormField:first');
		var formId = target.attr('refForm');
		if(isEmpty(formId)){
			smartPop.showInfo(smartPop.WARN, smartMessage.get("noRefFormDefinedError"));
			return false;
		}
		smartPop.selectWorkItem(formId, target);
		return false;
	});
	
	$('.js_type_radioButton input').live('click', function(e){
		var target = $(targetElement(e)).parents('.js_type_radioButton').find('.sw_required');
		if(target.hasClass('sw_error')){
			target.removeClass('sw_error');
			$('form.js_validation_required').validate({ showErrors: showErrors}).form();
		}
	});

	$('a.js_toggle_forward_btn').live('click',function(e) {
		var input = $(targetElement(e));
		var workSpacePage = input.parents('.js_iwork_space_page');
		if(isEmpty(workSpacePage)) workSpacePage = input.parents('.js_pwork_space_page');
		var isPworkSpace = workSpacePage.hasClass('js_pwork_space_page');
		var target = (isPworkSpace) ? workSpacePage.find('.js_form_task_forward') : input.parents('.js_form_header').siblings('.js_form_task_forward');
		if(target.is(':visible')){
			target.hide().html('');
			if(!isEmpty(workSpacePage)){
				if(isPworkSpace){
					var formMode = workSpacePage.attr("formMode");
					if(formMode==="edit"){
						workSpacePage.find('.js_btn_complete').show().siblings().hide();
						workSpacePage.find('.js_btn_return').show();
						workSpacePage.find('.js_btn_reassign').show();
						workSpacePage.find('.js_btn_temp_save').show();
					}else{
						workSpacePage.find('.js_btn_complete').hide().siblings().hide();
					}					
				}else{
					workSpacePage.find('.js_btn_save').show().siblings().hide();						
					workSpacePage.find('.js_btn_modify').show();					
				}
			}
			return false;
		}
		if((isPworkSpace && target.is(":visible")) || (!isPworkSpace && !isEmpty(input.parents('.js_form_header').siblings('.js_form_task_forward:visible')))) return false;
		$.ajax({
			url : 'append_task_forward.sw',
			data : {},
			success : function(data, status, jqXHR) {
				target.html(data).show();
				if(!isEmpty(workSpacePage)){
					workSpacePage.find('.js_btn_do_forward').show().siblings().hide();
					workSpacePage.find('.js_btn_cancel').show();
				}
			},
			error : function(xhr, ajaxOptions, thrownError){
				
			}
		});
		return false;
	});

	$('a.js_toggle_approval_btn').live('click',function(e) {
		var input = $(targetElement(e));
		var workSpacePage = input.parents('.js_iwork_space_page');
		if(isEmpty(workSpacePage)) workSpacePage = input.parents('.js_pwork_space_page');
		var isPworkSpace = workSpacePage.hasClass('js_pwork_space_page');
		var target = (isPworkSpace) ? workSpacePage.find('.js_form_task_approval') : input.parents('.js_form_header').siblings('.js_form_task_approval');
		if(target.is(':visible')){
			target.hide().html('');
			if(!isEmpty(workSpacePage)){
				if(isPworkSpace){
					workSpacePage.find('.js_btn_complete').show().siblings().hide();
					workSpacePage.find('.js_btn_return').show();
					workSpacePage.find('.js_btn_reassign').show();
					workSpacePage.find('.js_btn_temp_save').show();
				}else{
					workSpacePage.find('.js_btn_save').show().siblings().hide();						
					workSpacePage.find('.js_btn_modify').show();					
				}
			}
			return false;
		}
		if(!isEmpty(input.parents('.js_form_header').siblings('.js_form_task:visible')) || (isPworkSpace && !isEmpty(workSpacePage.find('.js_form_task:visible')))) return false;
		$.ajax({
			url : 'append_task_approval.sw',
			data : {},
			success : function(data, status, jqXHR) {
				target.html(data).show();
				if(!isEmpty(workSpacePage)){
					workSpacePage.find('.js_btn_do_approval').show().siblings().hide();
					workSpacePage.find('.js_btn_cancel').show();
				}
			},
			error : function(xhr, ajaxOptions, thrownError){
				
			}
		});
		return false;
	});

	$('a.js_email_content_btn').live('click',function(e) {
		var input = $(targetElement(e));
		var workSpacePage = input.parents('.js_iwork_space_page');
		if(isEmpty(workSpacePage)) workSpacePage = input.parents('.js_pwork_space_page');
		if(isEmpty(workSpacePage)) return false;
		var target = $('#content');
		var hostNPort = getHostNPort();
		var header = 	'<link href="' + hostNPort + '/smartworksV3/css/default.css" type="text/css" rel="stylesheet" />' +
		'<link href="' + hostNPort + '/smartworksV3/css/black/layout_2.css" type="text/css" rel="stylesheet" />' +
		'<link href="' + hostNPort + '/smartworksV3/css/black/detail.css" type="text/css" rel="stylesheet" />' +
		'<link href="' + hostNPort + '/smartworksV3/css/black/form.css" type="text/css" rel="stylesheet" />' +
		'<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />';
		var companyLogoSrc = $('.js_company_logo_src').attr('src');
		var topLogo = $('<div class="company_logo"><img src="' + companyLogoSrc + '" /><div>' + currentUser.company + '</div></div>');
		var bottomLogo = $('<div class="footer"><img src="' + hostNPort + '/smartworksV3/images/footer_sw_logo.jpg"></div>');
		var body = $('ul.portlet_r').clone();
		body.find('#js_copy_address').parents('.txt_btn').remove();
		body.find('.js_toggle_forward_btn').remove();
		body.find('.js_toggle_approval_btn').remove();
		body.find('.js_email_content_btn').remove();
		body.find('.js_print_content_btn').remove();
		body.find('.js_view_instance_diagram').remove();
		body.find('.body_titl_pic .solid_line').before(topLogo);
		body.find('input[type="hidden"]').remove();
//		body.find('.js_instance_tasks_left').remove();
//		body.find('.js_instance_tasks_right').remove();
//		body.find('.js_task_start').remove();
//		body.find('.js_task_stop').remove();
//		body.find('.js_instance_task_arrow').remove();
//		body.find('.js_instance_tasks li.selected').siblings().remove();
		if(workSpacePage.hasClass('js_iwork_space_page'))
			body.find('.js_form_content').removeClass('up');
		body.find('.glo_btn_space span.btn_gray').remove();
		body.find('.glo_btn_space span.js_space_error_message').remove();
		body.find('.glo_btn_space').append(bottomLogo);
		body.find('a').attr('userDetail', '');
		body.find('textarea').css({height:"56px"});
		body.html(body.html().replace(/\"images\//g, "\"" + hostNPort + "/smartworksV3/images/"));
		body.html(body.html().replace(/textarea/g,  "div"));
		var contents = '<html><head>' + header + '</head><body><br/><br/><br/><div id="wrap"><div>' + body.html() + '</div></div><br/><br/><br/></body></html>';
		
		var paramsJson = {};
		paramsJson['contents'] = contents;
		$.ajax({
			url : "new_mail_post.sw",
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				target.html(data).show();
			},
			error : function(xhr, ajaxOptions, thrownError){
				
			}
		});
		return false;
	});

	$('.js_print_content_btn').live("click", function(e){

		var input = $(targetElement(e));
		var workSpacePage = input.parents('.js_iwork_space_page');
		if(isEmpty(workSpacePage)) workSpacePage = input.parents('.js_pwork_space_page');
		if(isEmpty(workSpacePage)) workSpacePage = input.parents('.js_mail_space_page');
		if(isEmpty(workSpacePage)) return false;
		var isMailSpace = workSpacePage.hasClass('js_mail_space_page');
		var hostNPort = getHostNPort();
		var header = 	'<link href="' + hostNPort + '/smartworksV3/css/default.css" type="text/css" media="all" rel="stylesheet" />' +
		'<link href="' + hostNPort + '/smartworksV3/css/black/layout_2.css" type="text/css" media="all" rel="stylesheet" />' +
		'<link href="' + hostNPort + '/smartworksV3/css/black/detail.css" type="text/css" media="all" rel="stylesheet" />' +
		'<link href="' + hostNPort + '/smartworksV3/css/black/form.css" type="text/css" media="all" rel="stylesheet" />' +
		'<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />';
//		var header = 	'<link href="css/default.css" type="text/css" media="all" rel="stylesheet" />' +
//		'<link href="css/black/layout_2.css" type="text/css" media="all" rel="stylesheet" />' +
//		'<link href="css/black/detail.css" type="text/css" media="all" rel="stylesheet" />' +
//		'<link href="css/black/form.css" type="text/css" media="all" rel="stylesheet" />';
		var companyLogoSrc = $('.js_company_logo_src').attr('src');
		var topLogo = $('<div class="company_logo"><img src="' + companyLogoSrc + '" /><div>' + currentUser.company + '</div></div>');
		var bottomLogo = $('<div class="footer"><img src="' + hostNPort + '/smartworksV3/images/footer_sw_logo.jpg"></div>');
		var body = $('ul.portlet_r').clone();
		if(isMailSpace){
			body = $('ul.portlet_r .mail_list_section').clone();
			body.find('.move_btn_space').remove();
		}else{
			body.find('#js_copy_address').parents('.txt_btn').remove();
			body.find('.js_toggle_forward_btn').remove();
			body.find('.js_toggle_approval_btn').remove();
			body.find('.js_email_content_btn').remove();
			body.find('.js_print_content_btn').remove();
			body.find('.js_view_instance_diagram').remove();
			body.find('.body_titl_pic .solid_line').before(topLogo);
			body.find('input[type="hidden"]').remove();
	//		body.find('.js_instance_tasks_left').remove();
	//		body.find('.js_instance_tasks_right').remove();
	//		body.find('.js_task_start').remove();
	//		body.find('.js_task_stop').remove();
	//		body.find('.js_instance_task_arrow').remove();
	//		body.find('.js_instance_tasks li.selected').siblings().remove();
			if(workSpacePage.hasClass('js_iwork_space_page'))
				body.find('.js_form_content').removeClass('up');
			body.find('.glo_btn_space span.btn_gray').remove();
			body.find('.glo_btn_space').append(bottomLogo);
			body.find('a').attr('userDetail', '');
			body.find('textarea').css({height:"56px"});
		}
		body.html(body.html().replace(/\"images\//g, "\"" + hostNPort + "/smartworksV3/images/"));
		body.html(body.html().replace(/textarea/g,  "div"));
		var contents = '<div id="' + ((isMailSpace) ? 'wrap_noborder' : 'wrap') + '" style="overflow:auto;"><div>' + body.html() + '</div><div class="info tr">' + smartMessage.get("printUserText") + ' : ' +  currentUser.longName + ' / ' +  smartMessage.get("printTimeText") + ' : ' + printCurrentTime() + '</div></div>';
		smartPop.printContent(header, contents);
		return false;
	});

	$('.js_doc_writer_plugin').live("click", function(e){

		var input = $(targetElement(e));
		var docWriterTarget = input.attr('docTarget');
		var workSpacePage = input.parents('.js_iwork_space_page');
		if(isEmpty(workSpacePage)) workSpacePage = input.parents('.js_pwork_space_page');
		if(isEmpty(workSpacePage)) workSpacePage = input.parents('.js_mail_space_page');
		if(isEmpty(workSpacePage)) return false;
		var isMailSpace = workSpacePage.hasClass('js_mail_space_page');
		var hostNPort = getHostNPort();
		var header = 	'<link href="' + hostNPort + '/smartworksV3/css/default.css" type="text/css" media="all" rel="stylesheet" />' +
		'<link href="' + hostNPort + '/smartworksV3/css/black/layout_2.css" type="text/css" media="all" rel="stylesheet" />' +
		'<link href="' + hostNPort + '/smartworksV3/css/black/detail.css" type="text/css" media="all" rel="stylesheet" />' +
		'<link href="' + hostNPort + '/smartworksV3/css/black/form.css" type="text/css" media="all" rel="stylesheet" />' +
		'<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />';
//		var header = 	'<link href="css/default.css" type="text/css" media="all" rel="stylesheet" />' +
//		'<link href="css/black/layout_2.css" type="text/css" media="all" rel="stylesheet" />' +
//		'<link href="css/black/detail.css" type="text/css" media="all" rel="stylesheet" />' +
//		'<link href="css/black/form.css" type="text/css" media="all" rel="stylesheet" />';
		var companyLogoSrc = $('.js_company_logo_src').attr('src');
		var topLogo = $('<div class="company_logo"><img src="' + companyLogoSrc + '" /><div>' + currentUser.company + '</div></div>');
		var bottomLogo = $('<div class="footer"><img src="' + hostNPort + '/smartworksV3/images/footer_sw_logo.jpg"></div>');
		var body = $('ul.portlet_r').clone();
		if(isMailSpace){
			body = $('ul.portlet_r .mail_list_section').clone();
			body.find('.move_btn_space').remove();
		}else{
			body.find('#js_copy_address').parents('.txt_btn').remove();
			body.find('.js_toggle_forward_btn').remove();
			body.find('.js_toggle_approval_btn').remove();
			body.find('.js_email_content_btn').remove();
			body.find('.js_print_content_btn').remove();
			body.find('.js_view_instance_diagram').remove();
			body.find('.body_titl_pic .solid_line').before(topLogo);
			body.find('input[type="hidden"]').remove();
	//		body.find('.js_instance_tasks_left').remove();
	//		body.find('.js_instance_tasks_right').remove();
	//		body.find('.js_task_start').remove();
	//		body.find('.js_task_stop').remove();
	//		body.find('.js_instance_task_arrow').remove();
	//		body.find('.js_instance_tasks li.selected').siblings().remove();
			if(workSpacePage.hasClass('js_iwork_space_page'))
				body.find('.js_form_content').removeClass('up');
			body.find('.glo_btn_space span.btn_gray').remove();
			body.find('.glo_btn_space').append(bottomLogo);
			body.find('a').attr('userDetail', '');
			body.find('textarea').css({height:"56px"});
		}
		body.html(body.html().replace(/\"images\//g, "\"" + hostNPort + "/smartworksV3/images/"));
		body.html(body.html().replace(/textarea/g,  "div"));
		var contents = '<div id="' + ((isMailSpace) ? 'wrap_noborder' : 'wrap') + '" style="overflow:auto;"><div>' + body.html() + '</div><div class="info tr">' + smartMessage.get("printUserText") + ' : ' +  currentUser.longName + ' / ' +  smartMessage.get("printTimeText") + ' : ' + printCurrentTime() + '</div></div>';

		var progressSpan = input.siblings('.js_progress_span');
		smartPop.progressCont(progressSpan);
		$.ajax({
			url : "get_content_from_doc_writer.sw",
			data : {
				header : header,
				content : contents,
				docTarget : docTarget
			},
			success : function(data, status, jqXHR) {
				var resultHead = "";
				var resultBody = "";
				if(!isEmpty(data)){
					resultHead = $(data).find('head:first');
					resultHead = $(data).find('body:first');
				}
				if(docTarget === "print"){
					smartPop.printContent(resultHead, resultBody);
					smartPop.closeProgress();
				}else if(docTarget === "email"){
					var paramsJson = {};
					paramsJson['contents'] = data;
					$.ajax({
						url : "new_mail_post.sw",
						contentType : 'application/json',
						type : 'POST',
						data : JSON.stringify(paramsJson),
						success : function(data, status, jqXHR) {
							target.html(data).show();
							smartPop.closeProgress();
						},
						error : function(xhr, ajaxOptions, thrownError){
							smartPop.closeProgress();							
						}
					});
				}
				else if(docTarget === "pdf"){
					smartPop.closeProgress();					
				}
			},
			error : function(xhr, ajaxOptions, thrownError){
				smartPop.closeProgress();
			}
		});		
		return false;
	});

	
	$('.js_view_my_instances').live('click',function(e) {
		var input = $(targetElement(e));
		if(isEmpty(input.attr('viewType'))) input = input.parent();
		var myRunningInstanceList = input.parents('.js_my_running_instance_list_page');
		input.parent().addClass('current').siblings().removeClass('current');
		var viewType = input.attr('viewType'); 
		var target = input.parents('.js_my_running_instance_list_page').find('.js_instance_list_table');
		var searchKey = input.parents('.js_my_running_instance_list_page').find('input[name]=txtSearchInstance').val();  
		var searchFilterId = input.parents('.js_my_running_instance_list_page').find('input[name]=txtSearchInstance').val();
		var progressSpan = myRunningInstanceList.find('.js_progress_span');
		smartPop.progressCont(progressSpan);
		if(viewType == 'smartcaster_instances'){
			$.ajax({
				url : "more_smartcast.sw",
				data : {
					fromDate : '',
					maxSize : 10
				},
				success : function(data, status, jqXHR) {
					target.html(data);
					smartPop.closeProgress();
				},
				error : function(xhr, ajaxOptions, thrownError){
					smartPop.closeProgress();
				}
			});
			
		}else if(viewType == 'assigned_instances' || viewType == 'running_instances'){
			$.ajax({
				url : 'more_instance_list.sw',
				data : {
					runningOnly : (viewType == 'running_instances'),
					assignedOnly : (viewType == 'assigned_instances'),
					searchKey : searchKey
				},
				success : function(data, status, jqXHR) {
					target.html(data);
					smartPop.closeProgress();
				},
				error : function(xhr, ajaxOptions, thrownError){
					smartPop.closeProgress();					
				}
			});			
		}else{
			smartPop.closeProgress();			
		}
		return false;
	});

	var filesDetailTimer = null;
	$('.js_pop_files_detail').live('mouseenter', function(e){
		if(filesDetailTimer!=null){
			clearTimeout(filesDetailTimer);
			filesDetailTimer = null;
		}
		var input = $(targetElement(e));
		var picture = input;
		var top = picture.offset().top+ picture.height() + 5;
		var left = picture.offset().left + picture.width() + 5;
		smartPop.showFilesDetail(input, top, left);		
	});

	$('.js_pop_files_detail').live('mouseleave', function(e){
		filesDetailTimer = setTimeout(function(){
			smartPop.closeFilesDetail();
			filesDetailTimer = null;
		}, 300);
	});
	
	$('#sw_pop_files_detail').live('mouseenter', function(e){
		if(filesDetailTimer!=null){
			clearTimeout(filesDetailTimer);
			filesDetailTimer = null;
		}		
	});

	$('#sw_pop_files_detail').live('mouseleave', function(e){
		filesDetailTimer = setTimeout(function(){
			smartPop.closeFilesDetail();
			filesDetailTimer = null;
		}, 300);
	});	

	$('.js_pop_approval_line').live('click', function(e) {
		var appendTaskApproval = $(targetElement(e)).parents('.js_append_task_approval_page');
		var target = appendTaskApproval.find('.js_select_approval_line');
//		var width = startWork.find('.js_auto_complete:first').parent().outerWidth();
//		smartPop.selectWork(target, width);
		smartPop.selectApprovalLine(target);
		return false;
	});

	$('.js_notice_message_box_page').live("click", function(e){
		$(targetElement(e)).parents('.js_notice_message_box_page').find('.js_close_message').click();
		return true;
	});

	$('.js_modify_work_manual').live('click', function(e){
		var target = $(targetElement(e)).parents('.js_modify_work_manual');
		target.hide().siblings().show();
		var workManual = target.parents('.js_iwork_manual_page');
		if(isEmpty(workManual)) workManual = target.parents('.js_pwork_manual_page');
		workManual.find('.js_work_comment_list').hide();
		workManual.find('.js_work_desc_view').hide().next().show();
		workManual.find('.js_form_desc_view').hide().next().show();

		var manualAttachmentsField = workManual.find('.js_manual_attachments_field');		
		var gridRow = SmartWorks.GridLayout.newGridRow();
		var gridTable = SmartWorks.GridLayout.newGridTable();
		manualAttachmentsField.html(gridTable.html(gridRow));
		
		var manualFileText = manualAttachmentsField.attr('manualFileText');
		var helpUrlText = manualAttachmentsField.attr('helpUrlText');
		var manualFile = manualAttachmentsField.attr('manualFile');
		var helpUrl = manualAttachmentsField.attr('helpUrl');
		
		SmartWorks.FormRuntime.FileFieldBuilder.buildEx({
			container: gridRow,
			fieldId: "fileManualFile",
			fieldName: manualFileText,
			groupId: manualFile,
			columns: 3,
			colSpan: 1,
			required: false,
			isMultiple: false
		});
		
		gridRow = SmartWorks.GridLayout.newGridRow().appendTo(gridTable);
		SmartWorks.FormRuntime.TextInputBuilder.buildEx({
			container: gridRow,
			fieldId: "txtHelpUrl",
			fieldName: helpUrlText,
			value: helpUrl,
			columns: 3,
			colSpan: 1,
			required: false
		});
		gridRow.find('input').attr('placeholder', "http://");
		manualAttachmentsField.show();
		return false;
	});

	$('.js_cancel_work_manual').live('click', function(e){
		var target = $(targetElement(e)).parents('.js_cancel_work_manual');
		target.hide().siblings('.js_modify_work_manual').show().siblings('.js_save_work_manual').hide();
		var workManual = target.parents('.js_iwork_manual_page');
		if(isEmpty(workManual)) workManual = target.parents('.js_pwork_manual_page');
		workManual.find('.js_work_comment_list').show();
		workManual.find('.js_work_desc_view').show().next().hide();
		workManual.find('.js_form_desc_view').show().next().hide();
		workManual.find('.js_manual_attachments_field').hide().html('');
		return false;
	});
	
	$('.js_save_work_manual').live('click', function(e){
		submitForms();
		return false;
	});
		
	$('.js_select_editor_box').live('click', function(e){
		var input = $(targetElement(e));
		var formDescEdit = input.parents('.js_form_desc_edit');
		var formDescText = formDescEdit.find('.js_form_desc_text');
		var formDescEditor = formDescEdit.find('.js_form_desc_editor');
		var formDesc = formDescText.attr('value');
		var fieldName = input.parents('.js_select_editor_box').attr('fieldName');
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
			gridRow.find('#'+ fieldName).css({height:"280px"});
						
		}else if(input.attr('value') == 'text' && !formDescText.is(':visible')){
			formDescEdit.find('.js_form_desc_text').show().attr('name', fieldName);
			formDescEditor.html('');
		}
		return;
	});

	$('.js_show_picture_detail').live("click", function(e){
		var input = $(targetElement(e)).parent();
		var instanceId = input.attr('instanceId');
		smartPop.showPicture(instanceId);
		return false;
	});

	$('.js_import_from_excel').live('click', function(e){
		var input = $(targetElement(e)).parent();		
		var target = input.parents('.js_work_list_page').find('div.js_new_work_form');
		$('a.js_search_filter_close').click();
		$.ajax({
			url : "import_from_excel.sw",
			data : {},
			success : function(data, status, jqXHR) {
				target.html(data).slideDown(500);
			}
			
		});
		return false;
	});

	$('a.js_export_to_pwork_list_excel').live('click', function(e) {
		var input = $(targetElement(e));		
		var iworkList = input.parents('.js_iwork_list_page');
		var iframe = input.parent().next();
		var url = 'download_pwork_excel_list.sw?workId=' + iworkList.attr('workId');
		iframe.attr('src', url);
		//window.open('download_pwork_excel_list.sw?workId=' + iworkList.attr('workId'));
		return false;
	});
	$('a.js_export_to_iwork_list_excel').live('click', function(e) {
		var input = $(targetElement(e));
		var iworkList = input.parents('.js_iwork_list_page');
		var iframe = input.parent().next();
		var url = 'download_iwork_excel_list.sw?workId=' + iworkList.attr('workId');
		iframe.attr('src', url);
		//window.open('download_iwork_excel_list.sw?workId=' + iworkList.attr('workId'));
		return false;
	});
	
	$('a.js_excel_import_close').live('click', function(e) {
		var input = $(targetElement(e));		
		input.parents('.js_work_list_page').find('div.js_new_work_form').slideUp(500).html('');
		return false;
	});

	$('a.js_download_excel_template').live('click', function(e) {
		var input = $(targetElement(e));		
		var iworkList = input.parents('.js_iwork_list_page');
		window.open('download_excel_template.sw?workId=' + iworkList.attr('workId'));
		return false;
	});

	$('a.js_excel_import_excute').live('click', function(e){
		submitForms();
		return false;
	});

});
