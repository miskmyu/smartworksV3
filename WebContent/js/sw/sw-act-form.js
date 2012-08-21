$(function() {

	var refreshRecord = function(input, refreshData){
		var newIwork = input.parents('.js_new_iwork_page');
		var startPwork = input.parents('.js_start_pwork_page');
		var iworkSpace =input.parents('.js_iwork_space_page');
		var pworkSpace = input.parents('.js_pwork_space_page');
		var workId = "", target = [], recordId = "", taskInstId = "", formId = "";
		if(!isEmpty(newIwork)){
			workId = newIwork.attr('workId');
			target = newIwork.find('.js_form_content');		
		}else if(!isEmpty(startPwork)){
			workId = startPwork.attr('workId');
			target = startPwork.find('.js_form_content');
		}else if(!isEmpty(iworkSpace)){
			workId = iworkSpace.attr('workId');
			recordId = iworkSpace.attr('instId');
			target = iworkSpace.find('.js_form_content');			
		}else if(!isEmpty(pworkSpace)){
			workId = pworkSpace.attr('workId');
			recordId = pworkSpace.attr('instId');
			taskInstId = pworkSpace.attr('taskInstId');
			target = pworkSpace.find('.js_form_content');
			formId = $('.form_layout[name=frmSmartForm]').attr('formid');
		}
		refreshData['workId'] = workId;
		refreshData['recordId'] = recordId;
		refreshData['retry'] = true;
		new SmartWorks.GridLayout({
			target : target,
			mode : "edit",
			workId : workId,
			recordId : recordId,
			taskInstId : taskInstId,
			refreshData : refreshData,
			formId : formId,
			onSuccess : function(){
			}
		});
	};
		
	$('form[name="frmSmartForm"] .form_value > input').live('change', function(e) {
		var input = $(targetElement(e));
		var forms = input.parents('form[name="frmSmartForm"]');
		var paramsJson = {};
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
		refreshRecord(input, paramsJson);
	});

	$('form[name="frmSmartForm"] .form_value fieldset input').live('change', function(e) {
		var input = $(targetElement(e));
		var forms = input.parents('form[name="frmSmartForm"]');
		var paramsJson = {};
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
		refreshRecord(input, paramsJson);
	});

	$('form[name="frmSmartForm"] .form_value > select').live('change', function(e) {
		var input = $(targetElement(e));
		var forms = input.parents('form[name="frmSmartForm"]');
		var paramsJson = {};
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
		refreshRecord(input, paramsJson);
	});
	
	$('form[name="frmSmartForm"] .form_value input.js_number_input').live('blur', function(e) {
		var input = $(targetElement(e));
		var forms = input.parents('form[name="frmSmartForm"]');
		if(!isEmpty(input.attr('value'))) input.removeClass('sw_error');
		var paramsJson = {};
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
		refreshRecord(input, paramsJson);
	});

	$('form[name="frmSmartForm"] .form_value input.js_currency_input').live('blur', function(e) {
		var input = $(targetElement(e));
		var forms = input.parents('form[name="frmSmartForm"]');
		if(!isEmpty(input.attr('value'))) input.removeClass('sw_error');
		var paramsJson = {};
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
		refreshRecord(input, paramsJson);
	});

	$('form[name="frmSmartForm"] .form_value input.js_percent_input').live('blur', function(e) {
		var input = $(targetElement(e));
		var value = input.attr('value');
		if(value!='' && value.indexOf("%")==-1) input.attr('value', value+'%');
		var forms = input.parents('form[name="frmSmartForm"]');
		if(!isEmpty(input.attr('value'))) input.removeClass('sw_error');
		var paramsJson = {};
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
		refreshRecord(input, paramsJson);
	});

	$('form[name="frmSmartForm"] .form_value .js_community_names').live('change', function(e) {
		var input = $(targetElement(e));
		var forms = input.parents('form[name="frmSmartForm"]');
		var paramsJson = {};
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
		refreshRecord(input, paramsJson);
	});

	$('form[name="frmSmartForm"] .form_value input.js_todaypicker').live('change', function(e) {
		var input = $(targetElement(e));
		var forms = input.parents('form[name="frmSmartForm"]');
		var paramsJson = {};
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
		refreshRecord(input, paramsJson);
	});

	$('form[name="frmSmartForm"] .form_value input.js_todaytimepicker').live('change', function(e) {
		var input = $(targetElement(e));
		var forms = input.parents('form[name="frmSmartForm"]');
		var paramsJson = {};
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
		refreshRecord(input, paramsJson);
	});

	$('form[name="frmSmartForm"] .form_value input.js_timepicker').live('change', function(e) {
		var input = $(targetElement(e));
		var forms = input.parents('form[name="frmSmartForm"]');
		var paramsJson = {};
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
		refreshRecord(input, paramsJson);
	});

	$('form[name="frmSmartForm"] .js_type_refFormField').live('change', function(e) {
		var input = $(targetElement(e));
		var forms = input.parents('form[name="frmSmartForm"]');
		var paramsJson = {};
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
		refreshRecord(input, paramsJson);
	});

	$('.js_show_instance').live('click', function(e) {
		var input = $(targetElement(e));
		if(!input.hasClass('js_show_instance')) input = input.parents('.js_show_instance');
		var instanceId = input.attr('instanceId');
		var taskInstId = input.attr('taskInstId');
		var formId = input.attr('formId');
		var forwardId = input.attr('forwardId');
		smartPop.showInstance(isEmpty(instanceId) ? null : instanceId, isEmpty(taskInstId) ? null : taskInstId, null, formId, forwardId);
		return false;
	});

});
