SmartWorks.FormRuntime = SmartWorks.FormRuntime || {};

SmartWorks.FormRuntime.DepartmentFieldBuilder = {};

SmartWorks.FormRuntime.DepartmentFieldBuilder.build = function(config) {
	var options = {
		mode : 'edit', // view or edit
		container : $('<td></td>'),
		entity : null,
		dataField : '',
		refreshData : false,
		layoutInstance : null,
		isDataGrid : false
	};

	SmartWorks.extend(options, config);
	if(!options.refreshData)
		options.container.html('');

	var department = (options.dataField && options.dataField.department) || {};
	var departmentHtml = (options.dataField && options.dataField.departmentHtml) || "";
	var $entity = options.entity;
	var $graphic = $entity.find('graphic');
	var readOnly = $graphic.attr('readOnly') === 'true' || options.mode === 'view';
	var id = $entity.attr('id');
	var name = $entity.attr('name');
	
	var labelWidth = (isEmpty(options.layoutInstance)) ? parseInt($graphic.attr('labelWidth')) : options.layoutInstance.getLabelWidth(id);
	var valueWidth = 100 - (options.isDataGrid ? 0 : labelWidth);
	var $label = $('<div class="form_label" style="width:' + labelWidth + '%"><span>' + name + '</span></div>');
	var required = $entity.attr('required');
	if(required === 'true' && !readOnly){
		$label.addClass('required_label');
		required = " class='fieldline community_names js_community_names sw_required'";
	}else{
		required = " class='fieldline community_names js_community_names'";
	}
	if(!options.refreshData && !options.isDataGrid)
		$label.appendTo(options.container);
	
	var $department = null;
	
	var href = "department_name.sw";
	
	var icoClass = ' class="icon_fb_depart"';
	var departPicker = 'class="js_departpicker_button"';
	

	var $html = $('<div class="form_value" style="width:' + valueWidth + '%"> <div class="icon_fb_space">\
					<div ' + required + '>\
						' + departmentHtml + '\
						<input class="m0 js_auto_complete" style="width:100px" href="' + href + '" type="text">\
					</div>\
					<div class="js_community_list srch_list_nowid" style="display: none"></div><span class="js_community_popup"></span><a href=""' + departPicker + '><span ' + icoClass + '></span></a></div></div>');

	if(readOnly){
		$department = $('<div class="form_value" style="width:' + valueWidth + '%"><span></span></div>');
		var viewDepartmentHtml = '';
		if(!isEmpty(departmentHtml)){
			viewDepartmentHtml = departmentHtml;
		}else{
			var href = 'department_space.sw?cid=dp.sp.' + department.departId + '&wid=' + department.departId;
			viewDepartmentHtml = '<a href="' + href + '"><span>' + department.departName + '</span></a>';
		}
		$department.find('span').html(viewDepartmentHtml);
	}else{	
		$department = $html;
	}
	if ($graphic.attr('hidden') == 'true'){
		$label.hide();
		$department.hide();		
	}
	if(!options.refreshData)
		$department.appendTo(options.container);
	else
		options.container.find('.form_value').html($department.children());

	if (readOnly) {
		var $departmentHiddenDiv = options.container.find('#departmentHiddenDiv' + id);
		if ($departmentHiddenDiv.length === 0) {
			options.container.append($('<div id="departmentHiddenDiv' + id + '" style="display:none"></div>').html(departmentHtml));
		} else {
			$departmentHiddenDiv.html(departmentHtml);
		}
	}	
		
	return options.container;
};

SmartWorks.FormRuntime.DepartmentFieldBuilder.buildEx = function(config){
	var options = {
			container : $('<tr></tr>'),
			fieldId: '',
			fieldName: '',
			department: {}, //{departId: '',departName: '}
			columns: 1,
			colSpan: 1,
			required: false,
			readOnly: false		
	};
	SmartWorks.extend(options, config);

	var labelWidth = 12;
	if(options.columns >= 1 && options.columns <= 4 && options.colSpan <= options.columns) labelWidth = 12 * options.columns/options.colSpan;
	$formEntity =  $($.parseXML('<formEntity id="' + options.fieldId + '" name="' + options.fieldName + '" systemType="string" required="' + options.required + '" system="false">' +
						'<format type="departmentField" viewingType="departmentField"/>' +
					    '<graphic hidden="false" readOnly="'+ options.readOnly +'" labelWidth="'+ labelWidth + '"/>' +
					'</formEntity>')).find('formEntity');
	var $formCol = $('<td class="form_col js_type_departmentField" fieldid="' + options.fieldId+ '" colspan="' + options.colSpan + '" width="' + options.colSpan/options.columns*100 + '%" rowspan="1">');
	$formCol.appendTo(options.container);
	SmartWorks.FormRuntime.DepartmentFieldBuilder.build({
			mode : options.readOnly, // view or edit
			container : $formCol,
			entity : $formEntity,
			dataField : SmartWorks.FormRuntime.DepartmentFieldBuilder.dataField({
				fieldId: options.fieldId,
				department : options.department,
				departmentHtml : options.departmentHtml
			})
	});
	
};

SmartWorks.FormRuntime.DepartmentFieldBuilder.serializeObject = function(departmentFields){
	var departmentsJson = {};
	for(var i=0; i<departmentFields.length; i++){
		var departmentField = $(departmentFields[i]);
		var fieldId = departmentField.attr('fieldId');
		var departmentList = departmentField.find('.form_value .js_community_item');
		var department = {};
		if(!isEmpty(departmentList)){
			department = {
				id : $(departmentList[0]).attr('comId'),
				name : $.trim($(departmentList[0]).attr('comName'))
			};
		}
		departmentsJson[fieldId] =  {department: department};
	}
	return departmentsJson;
};

SmartWorks.FormRuntime.DepartmentFieldBuilder.validate = function(departmentFields){
	var departmentsValid = true;
	for(var i=0; i<departmentFields.length; i++){
		var departmentField = $(departmentFields[i]);
		var departId = departmentField.find('.js_community_item:first').attr('comId');
		var required = departmentField.find('div.sw_required');
		if(!isEmpty(required) && isBlank(departId)){
			departmentField.find('div.sw_required').addClass("sw_error");
			departmentsValid = false;
		}
	}
	return departmentsValid;
};

SmartWorks.FormRuntime.DepartmentFieldBuilder.dataField = function(config){
	var options = {
			fieldName: '',
			formXml: '',
			fieldId: '',
			department: '', //{departId: '',departName: ''}
			departmentHtml: ''
	};

	SmartWorks.extend(options, config);
	$formXml = isEmpty(options.formXml) ? [] : $($.parseXML(options.formXml)).find('form');
	var dataField = {};
	
	var fieldId = (isEmpty(options.fieldId)) ? $formXml.find('formEntity[name="'+options.fieldName+'"]').attr('id') : options.fieldId;
	if(isEmpty(fieldId)) fieldId = ($formXml.attr("name") === options.fieldName) ? $formXml.attr('id') : "";
	if(isEmpty(fieldId)) return dataField;
	dataField = {
			id: fieldId,
			department: options.deparment,
			departmentHtml : options.departmentHtml
	};
	return dataField;
};
