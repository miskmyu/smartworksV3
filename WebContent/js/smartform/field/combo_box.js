SmartWorks.FormRuntime = SmartWorks.FormRuntime || {};

SmartWorks.FormRuntime.ComboBoxBuilder = {};

SmartWorks.FormRuntime.ComboBoxBuilder.build = function(config) {
	var options = {
		mode : 'edit', // view or edit
		container : $('<div></div>'),
		entity : null,
		dataField : '',
		refreshData : false,
		layoutInstance : null
	};

	SmartWorks.extend(options, config);
	if(!options.refreshData)
		options.container.html('');

	var value = (options.dataField && options.dataField.value) || '';
	
	var $entity = options.entity;
	var $graphic = $entity.find('graphic');
	var $format = $entity.find('format');

	var readOnly = $graphic.attr('readOnly') == 'true' || options.mode == 'view';
	var id = $entity.attr('id');
	var name = $entity.attr('name');
	
	var labelWidth = (isEmpty(options.layoutInstance)) ? parseInt($graphic.attr('labelWidth')) : options.layoutInstance.getLabelWidth(id);
	var valueWidth = 100 - labelWidth;
	var $label = $('<div class="form_label" style="width:' + labelWidth + '%"><span>' + name + '</span></div>');
	var required = $entity.attr('required');
	if(required === 'true' && !readOnly){
		$label.addClass('required_label');
		required = " class='form_select_box required' ";
	}else{
		required = " class='form_select_box' ";		
	}
		
	if(!options.refreshData)
		$label.appendTo(options.container);
	
	var $staticItems = $format.find('list staticItems staticItem');
	// 가져오기 값이 1개이상일 경우 그값으로 staticItmes를 변경한다
	if (options.dataField != null && options.dataField.dataFields != null) {
		var staticItemsStr = '';
		var subDataFields = options.dataField.dataFields;
		for (var i = 0; i < subDataFields.length; i++) {
			var subDataField = subDataFields[i];
			staticItemsStr = staticItemsStr + '<staticItem>' + subDataField.value + '</staticItem>';
		}
		$staticItems = $(staticItemsStr);
	}
	//
	var $input = $('<div class="form_value" style="width:' + valueWidth + '%"><select name="' + id + '"' + required + '></select><div>');

	$input.attr('fieldId', id);
	if (readOnly) {
		$input.find('select').attr('disabled', 'disabled');
	}else{
		required = "";
	}
	
	for ( var i = 0; i < $staticItems.length; i++) {
		var $staticItem = $staticItems.eq(i);
		var text = $staticItem.text();
		var selected = (value === text ) ? 'selected' : '' ;

		$option = $('<option value="' + text + '" ' + selected + '>'+text+'</option>');
		
		$option.appendTo($input.find('select'));
	}

	if(!options.refreshData)
		$input.appendTo(options.container);
	else
		options.container.find('.form_value').html($input.children());

	if ($graphic.attr('hidden') == 'true') {
		$label.hide();
		$input.hide();
	}
	
	if (readOnly)
		options.container.append('<input type="hidden" name="' + id + '" value="' + value + '">');
	
	return options.container;
};

SmartWorks.FormRuntime.ComboBoxBuilder.buildEx = function(config){
	var options = {
			container : $('<tr></tr>'),
			fieldId: '',
			fieldName: '',
			value: '',
			staticItems : [],
			columns: 1,
			colSpan: 1,
			required: false,
			readOnly: false		
	};
	SmartWorks.extend(options, config);

	var staticList = "";
	if(!isEmpty(options.staticItems)){
		staticList = '<list type="" refCodeCategoryId="null" refCodeCategoryName="null" listType="static"><staticItems>';
			for(var i=0; i<options.staticItems.length; i++)
				staticList = staticList + '<staticItem>' + options.staticItems[i] + '</staticItem>';
		staticList = staticList + '</staticItems></list>';		
	}

	var labelWidth = 12;
	if(options.columns >= 1 && options.columns <= 4 && options.colSpan <= options.columns) labelWidth = 12 * options.columns/options.colSpan;
	$formEntity =  $($.parseXML('<formEntity id="' + options.fieldId + '" name="' + options.fieldName + '" systemType="string" required="' + options.required + '" system="false">' +
						'<format type="comboBox" viewingType="comboBox">' + staticList + '</format>' + 
					    '<graphic hidden="false" readOnly="'+ options.readOnly +'" labelWidth="'+ labelWidth + '"/>' +
					'</formEntity>')).find('formEntity');
	var $formCol = $('<td class="form_col js_type_comboBox" fieldid="' + options.fieldId+ '" colspan="' + options.colSpan +  '" width="' + options.colSpan/options.columns*100 + '%" rowspan="1">');
	$formCol.appendTo(options.container);
	SmartWorks.FormRuntime.ComboBoxBuilder.build({
			mode : options.readOnly, // view or edit
			container : $formCol,
			entity : $formEntity,
			dataField : SmartWorks.FormRuntime.ComboBoxBuilder.dataField({
				fieldId: options.fieldId,
				value: options.value
			}),
			staticItems : options.staticItems
	});
	
};

SmartWorks.FormRuntime.ComboBoxBuilder.dataField = function(config){
	var options = {
			fieldName: '',
			formXml: '',
			fieldId: '',
			value: ''
	};

	SmartWorks.extend(options, config);
	$formXml = isEmpty(options.formXml) ? [] : $($.parseXML(options.formXml)).find('form');
	var dataField = {};
	var fieldId = (isEmpty(options.fieldId)) ? $formXml.find('formEntity[name="'+options.fieldName+'"]').attr('id') : options.fieldId;
	if(isEmpty(fieldId)) fieldId = ($formXml.attr("name") === options.fieldName) ? $formXml.attr('id') : "";
	if(isEmpty(fieldId)) return dataField;
	
	dataField = {
			id: fieldId,
			value: options.value
	};
	return dataField;
};
