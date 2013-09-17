$(function() {
	
	$('a.js_new_work_hour').live('click', function(e) {
		var input = $(targetElement(e));
		var target = input.parents('.js_company_work_hour_page').find('div.js_new_work_hour');
		$.ajax({
			url : "edit_work_hour.sw",
			success : function(data, status, jqXHR) {
				target.html(data).slideDown(500);
			}			
		});
		return false;
	});

	$('.js_edit_work_hour').live('click', function(e) {
		var input = $(targetElement(e));
		var target = input.parents('.js_company_work_hour_page').find('div.js_new_work_hour');
		var policyId = input.parents('.js_edit_work_hour').attr('policyId');
		$.ajax({
			url : "edit_work_hour.sw?policyId=" + policyId,
			success : function(data, status, jqXHR) {
				target.html(data).slideDown(500);
			}			
		});
		return false;
	});

	$('.js_delete_work_hour').live('click', function(e) {
		var input = $(targetElement(e));
		
		smartPop.confirm(smartMessage.get("removeConfirmation"), function(){
			var policyId = input.parents('.js_edit_work_hour').attr('policyId');
			var paramsJson = {};
			paramsJson['policyId'] = policyId;
			console.log(JSON.stringify(paramsJson));
			$.ajax({
				url : "remove_work_hour_policy.sw",
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					document.location.href = "company_work_hour.sw";					
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("removeWorkHourError"), function(){
					});
					
				}
				
			});
		});
		return false;
	});

	$('a.js_new_company_event').live('click', function(e) {
		var input = $(targetElement(e));
		var target = input.parents('.js_company_event_page').find('div.js_new_company_event');
		$.ajax({
			url : "edit_company_event.sw",
			success : function(data, status, jqXHR) {
				target.html(data).slideDown(500);
			}			
		});
		return false;
	});

	$('.js_edit_company_event').live('click', function(e) {
		var input = $(targetElement(e));
		var target = input.parents('.js_company_event_page').find('div.js_new_company_event');
		var eventId = input.parents('.js_edit_company_event').attr('eventId');
		$.ajax({
			url : "edit_company_event.sw?eventId=" + eventId,
			success : function(data, status, jqXHR) {
				target.html(data).slideDown(500);
			}			
		});
		return false;
	});

	$('.js_delete_company_event').live('click', function(e) {
		var input = $(targetElement(e));
		
		smartPop.confirm(smartMessage.get("removeConfirmation"), function(){
			var eventId = input.parents('.js_edit_company_event').attr('eventId');
			var paramsJson = {};
			paramsJson['eventId'] = eventId;
			console.log(JSON.stringify(paramsJson));
			$.ajax({
				url : "remove_company_event.sw",
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					document.location.href = "company_event.sw";					
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("removeCompanyEventError"), function(){
					});
					
				}
				
			});
		});
		return false;
	});

	$('a.js_new_approval_line').live('click', function(e) {
		var input = $(targetElement(e));
		var target = input.parents('.js_approval_line_page').find('div.js_new_approval_line');
		$.ajax({
			url : "edit_approval_line.sw",
			success : function(data, status, jqXHR) {
				target.html(data).slideDown(500);
			}			
		});
		return false;
	});

	$('.js_edit_approval_line').live('click', function(e) {
		var input = $(targetElement(e));
		var target = input.parents('.js_approval_line_page').find('div.js_new_approval_line');
		var lineId = input.parents('.js_edit_approval_line').attr('lineId');
		$.ajax({
			url : "edit_approval_line.sw?lineId=" + lineId,
			success : function(data, status, jqXHR) {
				target.html(data).slideDown(500);
			}			
		});
		return false;
	});

	$('.js_delete_approval_line').live('click', function(e) {
		var input = $(targetElement(e));
		
		smartPop.confirm(smartMessage.get("removeConfirmation"), function(){
			var lineId = input.parents('.js_edit_approval_line').attr('lineId');
			var paramsJson = {};
			paramsJson['lineId'] = lineId;
			console.log(JSON.stringify(paramsJson));
			$.ajax({
				url : "remove_approval_line.sw",
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					document.location.href = "approval_line.sw";					
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("removeApprovalLineError"), function(){
					});
					
				}
				
			});
		});
		return false;
	});

	$('select.js_approval_line_level').live('change', function(e) {
		var input = $(targetElement(e));
		var approvalLevel = parseInt(input.attr('value'));
		var approvalLine = input.parents('.js_approval_line_page');
		var approvalLevelNames = approvalLine.find('.js_approval_level_name').hide();
		approvalLevelNames.find('input').attr('name','');
		var approverTypes = approvalLine.find('.js_approver_type').css('visibility', 'hidden');
		approverTypes.find('select').attr('name', '');
		var approvalMeanTimes = approvalLine.find('.js_approval_mean_time').hide();
		var approvalSecond5 = approvalLine.find('.js_approval_second_5').hide();
		var widthVal = (approvalLevel<5) ? 80/approvalLevel : 80/5;
		for(var i=0; i<approvalLevel; i++){
			//if(approvalLevelNames.length>i) $(approvalLevelNames[i]).show();
			if(approvalLevelNames.length>i) $(approvalLevelNames[i]).show().find('input').attr('name', 'txtLevelName' + (i+1));
			if(approverTypes.length>i) $(approverTypes[i]).css('visibility', 'visible').find('select').attr('name', 'selLevelApproverType' + (i+1));
			if(approvalMeanTimes.length>i) $(approvalMeanTimes[i]).show();
		}
		if(approvalLevel>5)
			approvalSecond5.show();
		return false;
	});

	$('select.js_approval_approver_type').live('change', function(e) {
		var input = $(targetElement(e));
		if(input.attr('value') === '3')
			input.next('.js_type_userField').show();
		else
			input.next('.js_type_userField').hide();
		return false;
	});

	$('a.js_new_web_service').live('click', function(e) {
		var input = $(targetElement(e));
		var target = input.parents('.js_web_service_page').find('div.js_new_web_service');
		$.ajax({
			url : "edit_web_service.sw",
			success : function(data, status, jqXHR) {
				target.html(data).slideDown(500);
			}			
		});
		return false;
	});

	$('.js_edit_web_service').live('click', function(e) {
		var input = $(targetElement(e));
		var target = input.parents('.js_web_service_page').find('div.js_new_web_service');
		var serviceId = input.parents('.js_edit_web_service').attr('serviceId');
		$.ajax({
			url : "edit_web_service.sw?serviceId=" + serviceId,
			success : function(data, status, jqXHR) {
				target.html(data).slideDown(500);
			}			
		});
		return false;
	});

	$('.js_delete_web_service').live('click', function(e) {
		var input = $(targetElement(e));
		
		smartPop.confirm(smartMessage.get("removeConfirmation"), function(){
			var serviceId = input.parents('.js_edit_web_service').attr('serviceId');
			var paramsJson = {};
			paramsJson['serviceId'] = serviceId;
			console.log(JSON.stringify(paramsJson));
			$.ajax({
				url : "remove_web_service.sw",
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					document.location.href = "web_service.sw";					
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("removeWebServiceError"), function(){
					});
					
				}
				
			});
		});
		return false;
	});

	$('a.js_new_external_form').live('click', function(e) {
		var input = $(targetElement(e));
		var target = input.parents('.js_external_form_page').find('div.js_new_external_form');
		$.ajax({
			url : "edit_external_form.sw",
			success : function(data, status, jqXHR) {
				target.html(data).slideDown(500);
			}			
		});
		return false;
	});

	$('.js_edit_external_form').live('click', function(e) {
		var input = $(targetElement(e));
		var target = input.parents('.js_external_form_page').find('div.js_new_external_form');
		var formId = input.parents('.js_edit_external_form').attr('formId');
		$.ajax({
			url : "edit_external_form.sw?formId=" + formId,
			success : function(data, status, jqXHR) {
				target.html(data).slideDown(500);
			}			
		});
		return false;
	});

	$('.js_delete_external_form').live('click', function(e) {
		var input = $(targetElement(e));
		
		smartPop.confirm(smartMessage.get("removeConfirmation"), function(){
			var formId = input.parents('.js_edit_external_form').attr('formId');
			var paramsJson = {};
			paramsJson['formId'] = formId;
			console.log(JSON.stringify(paramsJson));
			$.ajax({
				url : "remove_external_form.sw",
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					document.location.href = "external_form.sw";					
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("removeExternalFormError"), function(){
					});
					
				}
				
			});
		});
		return false;
	});

	$('a.js_new_department').live('click', function(e) {
		var input = $(targetElement(e));
		var organizationManagement = input.parents('.js_organization_management_page');
		var parentId = organizationManagement.find('.js_edit_department_page').attr('departId');
		if(isEmpty(parentId)) parentId = currentUser.companyId;
		var target = organizationManagement.find('.js_edit_member');
		$.ajax({
			url : "edit_department.sw?parentId=" + parentId,
			success : function(data, status, jqXHR) {
				target.html(data).slideDown(500);
			}			
		});
		return false;
	});

	$('a.js_delete_department').live('click', function(e) {
		var input = $(targetElement(e));
		
		smartPop.confirm(smartMessage.get("removeConfirmation"), function(){
			var departId = input.parents('.js_edit_department_page').attr('departId');
			var paramsJson = {};
			paramsJson['departmentId'] = departId;
			console.log(JSON.stringify(paramsJson));
			$.ajax({
				url : "remove_department.sw",
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					document.location.href = "organization_management.sw";					
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("removeDepartmentError"), function(){
					});
					
				}
				
			});
		});
		return false;
	});

	$('a.js_new_member').live('click', function(e) {
		var input = $(targetElement(e));
		var organizationManagement = input.parents('.js_organization_management_page');
		var departId = organizationManagement.find('.js_edit_department_page').attr('departId');
		if(isEmpty(departId)) departId = currentUser.companyId;
		var target = organizationManagement.find('.js_edit_member');
		$.ajax({
			url : "edit_member.sw?departId=" + departId,
			success : function(data, status, jqXHR) {
				target.html(data).slideDown(500);
			}			
		});
		return false;
	});

	$('a.js_adjunct_member').live('click', function(e) {
		var input = $(targetElement(e));
		var organizationManagement = input.parents('.js_organization_management_page');
		var departId = organizationManagement.find('.js_edit_department_page').attr('departId');
		if(isEmpty(departId)) departId = currentUser.companyId;
		var target = organizationManagement.find('.js_edit_member');
		$.ajax({
			url : "edit_adjunct_member.sw?departId=" + departId,
			success : function(data, status, jqXHR) {
				target.html(data).slideDown(500);
			}			
		});
		return false;
	});

	$('a.js_delete_member').live('click', function(e) {
		var input = $(targetElement(e));
		
		smartPop.confirm(smartMessage.get("removeConfirmation"), function(){
			var userId = input.parents('.js_edit_member_page').attr('userId');
			var departmentId = input.parents('.js_edit_member_page').attr('parentId');
			var paramsJson = {};
			paramsJson['userId'] = userId;
			paramsJson['departmentId'] = departmentId;
			console.log(JSON.stringify(paramsJson));
			$.ajax({
				url : "remove_member.sw",
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					document.location.href = "organization_management.sw";					
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("removeMemberError"), function(){
					});
					
				}
				
			});
		});
		return false;
	});
	
	$('a.js_retire_member').live('click', function(e) {
		var input = $(targetElement(e));
		var userId = input.parents('.js_edit_member_page').attr('userId');
		smartPop.retireMember(userId);
//		smartPop.confirm(smartMessage.get("retireMemberConfirmation"), function(){
//			var userId = input.parents('.js_edit_member_page').attr('userId');
//			var departmentId = input.parents('.js_edit_member_page').attr('parentId');
//			var paramsJson = {};
//			paramsJson['userId'] = userId;
//			paramsJson['departmentId'] = departmentId;
//			console.log(JSON.stringify(paramsJson));
//			$.ajax({
//				url : "remove_member.sw",
//				contentType : 'application/json',
//				type : 'POST',
//				data : JSON.stringify(paramsJson),
//				success : function(data, status, jqXHR) {
//					document.location.href = "organization_management.sw";					
//				},
//				error : function(e) {
//					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
//					smartPop.showInfo(smartPop.ERROR, smartMessage.get("removeMemberError"), function(){
//					});
//					
//				}
//				
//			});
//		});
		return false;
	});
	
	$('a.js_check_id_duplication').live('click', function(e) {
		var input = $(targetElement(e));
		var target = input.parents('.js_edit_member_page').find('input[name="txtMemberId"]');
		var userId = target.attr('value');
		$.ajax({
			url : "check_id_duplication.sw",
			data : {
				userId : userId
			},
			success : function(data, status, jqXHR) {
				smartPop.showInfo(smartPop.INFO, smartMessage.get('usableUserId'));
				target.addClass('sw_dup_checked').attr('readonly', true);
				input.hide().siblings().show();;
			},
			error : function(xhr, ajaxOptions, thrownError){
				smartPop.showInfo(smartPop.WARN, smartMessage.get('duplicatedUserId'));
			}
		});
		return false;
	});

	$('a.js_change_id').live('click', function(e) {
		var input = $(targetElement(e));
		var target = input.parents('.js_edit_member_page').find('input[name="txtMemberId"]');
		target.removeClass('sw_dup_checked').attr('readonly', false);
		input.hide().siblings().show();;
		return false;
	});

	$('a.js_fetch_webservice_wsdl').live('click', function(e) {
		var input = $(targetElement(e));
		var target = input.parents('.js_webservice_wsdl:first input');
		var wsdlUri = target.attr('value');
		$.ajax({
			url : "wsdl_detail.sw",
			data : {
				wsdlUri : wsdlUri
			},
			success : function(data, status, jqXHR) {
				target.addClass('sw_fetched').attr('readonly', true);
				input.hide().siblings().show();
				var tbody = input.parents('.js_edit_webservice_tbody');
				tbody.find('tr.js_wsdl_detail').remove();
				$(data).appendTo(tbody);
			},
			error : function(xhr, ajaxOptions, thrownError){
				smartPop.showInfo(smartPop.ERROR, smartMessage.get('fetchWsdlError'));
			}
		});
		return false;
	});

	$('a.js_change_webservice_wsdl').live('click', function(e) {
		var input = $(targetElement(e));
		var target = input.parents('.js_webservice_wsdl:first input');
		target.removeClass('sw_fetched').attr('readonly', false);
		input.hide().siblings().show();;
		return false;
	});

	$('select.js_webservice_port').live('change', function(e) {
		var input = $(targetElement(e));
		var tbody = input.parents('.js_edit_webservice_tbody');
		var selectedPort = input.find('option:selected').attr('index');
		$.ajax({
			url : "wsdl_detail.sw",
			data : {
				selectedPort : selectedPort
			},
			success : function(data, status, jqXHR) {
				tbody.find('tr.js_wsdl_detail').remove();
				$(data).appendTo(tbody);
			}
		});
		return false;
	});

	$('select.js_webservice_operation').live('change', function(e) {
		var input = $(targetElement(e));
		var tbody = input.parents('.js_edit_webservice_tbody');
		var selectedPort = tbody.find('select.js_webservice_port:first').find('option:selected').attr('index');
		var selectedOperation = input.find('option:selected').attr('index');
		$.ajax({
			url : "wsdl_detail.sw",
			data : {
				selectedPort : selectedPort,
				selectedOperation : selectedOperation
			},
			success : function(data, status, jqXHR) {
				tbody.find('tr.js_wsdl_detail').remove();
				$(data).appendTo(tbody);
			}
		});
		return false;
	});

	$('select.js_first_day_of_week').live('change', function(e) {
		var input = $(targetElement(e));
		var editWorkHour = input.parents('.js_edit_work_hour_page');
		var firstDay = parseInt(input.attr('value'));
		var workingDays = parseInt(editWorkHour.find('.js_working_days').attr('value'));
		var workHours = editWorkHour.find('.js_work_hour');
		for(var i=0; i<workHours.length; i++){
			var workHour = $(workHours[i]);
			if(i+1<firstDay || i+1>firstDay+workingDays-1){
				workHour.find('.workStart').attr('value', '00:00');
				workHour.find('.workEnd').attr('value', '00:00');
				workHour.hide();
			}else{
				if(workHour.find('.workStart').attr('value') === "00:00") workHour.find('.workStart').attr('value', '09:00');
				if(workHour.find('.workEnd').attr('value') === "00:00") workHour.find('.workEnd').attr('value', '18:00');
				workHour.show();
			}
		}
		
		return false;
	});

	$('select.js_working_days').live('change', function(e) {
		var input = $(targetElement(e));
		var editWorkHour = input.parents('.js_edit_work_hour_page');
		var workingDays = parseInt(input.attr('value'));
		var firstDay = parseInt(editWorkHour.find('.js_first_day_of_week').attr('value'));
		var workHours = editWorkHour.find('.js_work_hour');
		for(var i=0; i<workHours.length; i++){
			var workHour = $(workHours[i]);
			if(i+1<firstDay || i+1>firstDay+workingDays-1){
				workHour.find('.workStart').attr('value', '00:00');
				workHour.find('.workEnd').attr('value', '00:00');
				workHour.hide();
			}else{
				if(workHour.find('.workStart').attr('value') === "00:00") workHour.find('.workStart').attr('value', '09:00');
				if(workHour.find('.workEnd').attr('value') === "00:00") workHour.find('.workEnd').attr('value', '18:00');
				workHour.show();
			}
		}
		
		return false;
	});
	
	$('.js_organization_member').live('click', function(e){
		var input = $(targetElement(e));
		var organizationManagement = input.parents('.js_organization_management_page');
		var target = organizationManagement.find('.js_edit_member');
		var userId = input.attr('userId');
		var parentId = input.attr('parentId');
		$.ajax({
			url : "edit_member.sw?userId=" + userId + "&parentId=" + parentId,
			success : function(data, status, jqXHR) {
				target.html(data).slideDown();;
			}			
		});
		
		return false;
	});

	$('a.js_add_variable_item').live('click', function(e){
		var input = $(targetElement(e));
		var hiddenItem = input.parents('tr:first').next('tr');
		var varCount = parseInt(hiddenItem.attr('varCount')) + 1;
		hiddenItem.attr('varCount', varCount);
		var newItem = hiddenItem.clone();
		var variableName = newItem.find('input.variableName');
		variableName.attr('name', variableName.attr('name')+varCount);
		var elementName = newItem.find('input.elementName');
		elementName.attr('name', elementName.attr('name')+varCount);
		var elementType = newItem.find('select.elementType');
		elementType.attr('name', elementType.attr('name')+varCount);
		newItem.show();
		var itemTable = input.parents('table:first');
		newItem.appendTo(itemTable);
		if(itemTable.find('tr').length == 3)
			itemTable.parents('tr:first td:first').addClass('required_label');
		return false;
	});

	$('a.js_remove_variable_item').live('click', function(e){
		var input = $(targetElement(e));
		var itemTable = input.parents('table:first');
		input.parents('tr:first').remove();
		if(itemTable.find('tr').length == 2){
			itemTable.parents('tr:first td:first').removeClass('required_label');
		}
		return false;
	});

	$('a.js_new_email_server').live('click', function(e) {
		var input = $(targetElement(e));
		var target = input.parents('.js_email_server_page').find('div.js_new_email_server');
		$.ajax({
			url : "edit_email_server.sw",
			success : function(data, status, jqXHR) {
				target.html(data).slideDown(500);
			}			
		});
		return false;
	});

	$('.js_edit_email_server').live('click', function(e) {
		var input = $(targetElement(e));
		var target = input.parents('.js_email_server_page').find('div.js_new_email_server');
		var emailServerId = input.parents('.js_edit_email_server').attr('emailServerId');
		$.ajax({
			url : "edit_email_server.sw?emailServerId=" + emailServerId,
			success : function(data, status, jqXHR) {
				target.html(data).slideDown(500);
			}			
		});
		return false;
	});

	$('.js_delete_email_server').live('click', function(e) {
		var input = $(targetElement(e));
		
		smartPop.confirm(smartMessage.get("removeConfirmation"), function(){
			var emailServerId = input.parents('.js_edit_email_server').attr('emailServerId');
			var paramsJson = {};
			paramsJson['emailServerId'] = emailServerId;
			console.log(JSON.stringify(paramsJson));
			$.ajax({
				url : "remove_email_server.sw",
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					document.location.href = "email_server.sw";					
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("removeEmailServerError"), function(){
					});
					
				}
				
			});
		});
		return false;
	});
	
	$('.js_toggle_use_email').live('click', function(e){
		var input = $(targetElement(e));
		var emailAccountInfos = input.parents('tr').nextAll('.js_email_account_info');
		if(input.is(':checked')){
			emailAccountInfos.show();
			loadMyProfileField();
		}else{
			emailAccountInfos.hide();			
		}
		return true;
	});

	$('.js_check_mail_auto_backup').live('click', function(e){
		var input = $(targetElement(e));
		if(input.is(':checked')) input.next().show();
		else input.next().hide();
	});
	
});
