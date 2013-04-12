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

	var departments = (options.dataField && options.dataField.departments) || new Array();
	var departmentsHtml = (options.dataField && options.dataField.departmentsHtml) || "";
	var $entity = options.entity;
	var $graphic = $entity.find('graphic');
	var readOnly = $graphic.attr('readOnly') === 'true' || options.mode === 'view';
	var multiUsers = $graphic.attr('multipleUsers');
	options.container.attr('multiUsers', multiUsers);
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
	
	if(multiUsers === 'true'){
		if(!isEmpty(departments) && isEmpty(departmentsHtml)){
			for(var i=0; i<departments.length; i++)
				departmentsHtml = departmentsHtml +  "<span class='js_community_item user_select' comId='" + departments[i].comId + "' comName='" + departments[i].name + "'>" + departments[i].name + "<a class='js_remove_community' href=''>&nbsp;x</a></span>";		
		}
		href = "community_name.sw";
//		icoClass = ' class="icon_fb_departs"';
	}else if (!isEmpty(departments) && isEmpty(departmentsHtml)) {
		departmentsHtml = "<span class='js_community_item user_select' comId='" + departments[0].comId + "' comName='" + departments[0].name + "'>" + departments[0].name + "<a class='js_remove_community' href=''> x</a></span>";
	}

	var $html = $('<div class="form_value" style="width:' + valueWidth + '%"> <div class="icon_fb_space">\
					<div ' + required + '>\
						' + departmentsHtml + '\
						<input class="m0 js_auto_complete" style="min-width:100px !important;max-width:200px" href="' + href + '" type="text">\
					</div>\
					<div class="js_community_list srch_list_nowid" style="display: none"></div><span class="js_community_popup"></span><a href=""' + departPicker + '><span ' + icoClass + '></span></a></div></div>');

	if(readOnly){
		$department = $('<div class="form_value" style="width:' + valueWidth + '%"><span></span></div>');
		var viewDepartmentsHtml = '';
		if(isEmpty(departments) && !isEmpty(departmentsHtml)){
			viewDepartmentsHtml = departmentsHtml;
		}else{
			for(var i=0; i<departments.length; i++) {
				var separator = ', ';
				var href = '';
				var desc = '';
				href = 'department_space.sw?cid=dp.sp.' + departments[i].comId + '&wid=' + departments[i].comId;
				if(i == departments.length - 1)
					separator = '';
				viewDepartmentsHtml = viewDepartmentsHtml + '<a href="' + href + '"><span>' + departments[i].name + desc + separator + '</span></a>';
			}
		}
		$department.find('span').html(viewDepartmentsHtml);
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
			options.container.append($('<div id="departmentHiddenDiv' + id + '" style="display:none"></div>').html(departmentsHtml));
		} else {
			$departmentHiddenDiv.html(departmentsHtml);
		}
	}	
		
	return options.container;
};

SmartWorks.FormRuntime.DepartmentFieldBuilder.buildEx = function(config){
	var options = {
			container : $('<tr></tr>'),
			fieldId: '',
			fieldName: '',
			departments: new Array(), //{comId: '',name: '}
			columns: 1,
			colSpan: 1,
			multiUsers: false,
			departmentsHtml: "",
			required: false,
			readOnly: false		
	};
	SmartWorks.extend(options, config);

	var labelWidth = 12;
	if(options.columns >= 1 && options.columns <= 4 && options.colSpan <= options.columns) labelWidth = 12 * options.columns/options.colSpan;
	$formEntity =  $($.parseXML('<formEntity id="' + options.fieldId + '" name="' + options.fieldName + '" systemType="string" required="' + options.required + '" system="false">' +
						'<format type="departmentField" viewingType="departmentField"/>' +
					    '<graphic hidden="false" readOnly="'+ options.readOnly +'" labelWidth="'+ labelWidth + '" multipleUsers="' + options.multiUsers+ '"/>' +
					'</formEntity>')).find('formEntity');
	var $formCol = $('<td class="form_col js_type_departmentField" fieldid="' + options.fieldId+ '" colspan="' + options.colSpan + '" width="' + options.colSpan/options.columns*100 + '%" rowspan="1">');
	$formCol.appendTo(options.container);
	SmartWorks.FormRuntime.DepartmentFieldBuilder.build({
			mode : options.readOnly, // view or edit
			container : $formCol,
			entity : $formEntity,
			dataField : SmartWorks.FormRuntime.DepartmentFieldBuilder.dataField({
				fieldId: options.fieldId,
				departments : options.departments,
				departmentsHtml : options.departmentsHtml
			})
	});
	
};

SmartWorks.FormRuntime.DepartmentFieldBuilder.serializeObject = function(departmentFields){
	var departmentsJson = {};
	for(var i=0; i<departmentFields.length; i++){
		var departmentField = $(departmentFields[i]);
		var fieldId = departmentField.attr('fieldId');
		var departmentList = departmentField.find('.form_value .js_community_item');
		var departments = new Array();
		if (departmentList.length === 0) {
			departmentList = departmentField.find('.js_community_item');
		}
		for(var j=0; j<departmentList.length; j++)
			departments.push({
				id : $(departmentList[j]).attr('comId'),
				name : $.trim($(departmentList[j]).attr('comName'))
			});
		
		departmentsJson[fieldId] =  {departments: departments};
	}
	return departmentsJson;
};

SmartWorks.FormRuntime.DepartmentFieldBuilder.validate = function(departmentFields){
	var departmentsValid = true;
	for(var i=0; i<departmentFields.length; i++){
		var departmentField = $(departmentFields[i]);
		var departmentId = departmentField.find('.js_community_item:first').attr('comId');
		var required = departmentField.find('div.sw_required');
		if(!isEmpty(required) && isBlank(departmentId)){
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
			departments: new Array(), //{comId: '',name: ''}
			departmentsHtml: ""
	};

	SmartWorks.extend(options, config);
	$formXml = isEmpty(options.formXml) ? [] : $($.parseXML(options.formXml)).find('form');
	var dataField = {};
	
	var fieldId = (isEmpty(options.fieldId)) ? $formXml.find('formEntity[name="'+options.fieldName+'"]').attr('id') : options.fieldId;
	if(isEmpty(fieldId)) fieldId = ($formXml.attr("name") === options.fieldName) ? $formXml.attr('id') : "";
	if(isEmpty(fieldId)) return dataField;
	dataField = {
			id: fieldId,
			departments : options.departments,
			departmentsHtml : options.departmentsHtml
	};
	return dataField;
};
