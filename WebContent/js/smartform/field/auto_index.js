SmartWorks.FormRuntime = SmartWorks.FormRuntime || {};

SmartWorks.FormRuntime.AutoIndexBuilder = {};

SmartWorks.FormRuntime.AutoIndexBuilder.build = function(config) {
	var options = {
		mode : 'edit', // view or edit
		container : $('<div></div>'),
		entity : null,
		dataField : '',
		refreshData : false,
		layoutInstance : null,
		isDataGrid : false
	};

	SmartWorks.extend(options, config);
	if(!options.refreshData)
		options.container.html('');

	var value = (options.dataField && options.dataField.value) || '';
	var selectedValue = (options.dataField && options.dataField.selectedValue) || '';
	
	var $entity = options.entity;
	var $graphic = $entity.find('graphic');
	var $format = $entity.find('format');

	var readOnly = $graphic.attr('readOnly') == 'true' || options.mode == 'view';
	var id = $entity.attr('id');
	var name = $entity.attr('name');
	
	var labelWidth = (isEmpty(options.layoutInstance)) ? parseInt($graphic.attr('labelWidth')) : options.layoutInstance.getLabelWidth(id);
	var valueWidth = 100 - (options.isDataGrid ? 0 : labelWidth);
	var $label = $('<div class="form_label" style="width:' + labelWidth + '%"><span>' + name + '</span></div>');
	var required = $entity.attr('required');
	if(required === 'true' && !readOnly){
		$label.addClass('required_label');
		required = " class='form_select_box required' ";
	}else{
		required = " class='form_select_box' ";		
	}
		
	if(!options.refreshData && !options.isDataGrid)
		$label.appendTo(options.container);
	
	var $listItems = $format.find('autoIndexRule listItems listItem');
	// 가져오기 값이 1개이상일 경우 그값으로 staticItmes를 변경한다
	if (options.dataField != null && options.dataField.dataFields != null) {
		var listItemsStr = '';
		var subDataFields = options.dataField.dataFields;
		for (var i = 0; i < subDataFields.length; i++) {
			var subDataField = subDataFields[i];
			if(!isEmpty(subDataField.value))
				listItemsStr = listItemsStr + '<listItem>' + subDataField.value + '</listItem>';
		}
		$listItems = $(listItemsStr);
	}
	
	var selectStr = "";
	if(!isEmpty($listItems) && !readOnly){
		selectStr = '<select name="' + id + '"' + required + '></select>';
	}
	//
	var $input = $('<div class="form_value" style="width:' + valueWidth + '%"><span>' + value + '</span>' + selectStr + '<div>');

	$input.attr('fieldId', id);
	if (!readOnly) {
		required = "";
	}
	
	if ($listItems.length == 0 && !isEmpty(selectedValue)) {
		$option = $('<option value="' + selectedValue + '" selected>'+selectedValue+'</option>');
		$option.appendTo($input.find('select'));
	} else {
		for ( var i = 0; i < $listItems.length; i++) {
			var $listItem = $listItems.eq(i);
			var text = $listItem.text();
			var selected = (selectedValue === text ) ? 'selected' : '' ;

			$option = $('<option value="' + text + '" ' + selected + '>'+text+'</option>');
			
			$option.appendTo($input.find('select'));
		}
	}
	
	
	if(!options.refreshData)
		$input.appendTo(options.container);
	else
		options.container.find('.form_value').html($input.children());

	if ($graphic.attr('hidden') == 'true') {
		$label.hide();
		$input.hide();
	}
	
	if (readOnly) {
		var $autoIndexHiddenInput = $('#autoIndexHiddenInput'+id);
		if ($autoIndexHiddenInput.length === 0) {
			options.container.append('<input id="autoIndexHiddenInput'+id+'" type="hidden" name="' + id + '" value="' + value + '">');
		} else {
			$autoIndexHiddenInput.attr('value', value);
		}
	}
	
	return options.container;
};

SmartWorks.FormRuntime.AutoIndexBuilder.buildEx = function(config){
	var options = {
			container : $('<tr></tr>'),
			fieldId: '',
			fieldName: '',
			value: '',
			selectedValue: '',
			listItems : [],
			columns: 1,
			colSpan: 1,
			required: false,
			readOnly: false		
	};
	SmartWorks.extend(options, config);

	var itemList = "";
	if(!isEmpty(options.listItems)){
		itemList = '<list type="" refCodeCategoryId="null" refCodeCategoryName="null" listType="static"><listItems>';
			for(var i=0; i<options.listItems.length; i++)
				itemList = itemList + '<listItem>' + options.listItems[i] + '</listItem>';
		itemList = itemList + '</listItems></list>';		
	}

	var labelWidth = 12;
	if(options.columns >= 1 && options.columns <= 4 && options.colSpan <= options.columns) labelWidth = 12 * options.columns/options.colSpan;
	$formEntity =  $($.parseXML('<formEntity id="' + options.fieldId + '" name="' + options.fieldName + '" systemType="string" required="' + options.required + '" system="false">' +
						'<format type="autoIndex" viewingType="autoIndex">' + itemList + '</format>' + 
					    '<graphic hidden="false" readOnly="'+ options.readOnly +'" labelWidth="'+ labelWidth + '"/>' +
					'</formEntity>')).find('formEntity');
	var $formCol = $('<td class="form_col js_type_autoIndex" fieldid="' + options.fieldId+ '" colspan="' + options.colSpan +  '" width="' + options.colSpan/options.columns*100 + '%" rowspan="1">');
	$formCol.appendTo(options.container);
	SmartWorks.FormRuntime.AutoIndexBuilder.build({
			mode : options.readOnly, // view or edit
			container : $formCol,
			entity : $formEntity,
			dataField : SmartWorks.FormRuntime.AutoIndexBuilder.dataField({
				fieldId: options.fieldId,
				value: options.value,
				selectedValue: options.selectedValue
			}),
			listItems : options.listItems
	});
	
};

SmartWorks.FormRuntime.AutoIndexBuilder.dataField = function(config){
	var options = {
			fieldName: '',
			formXml: '',
			fieldId: '',
			value: '',
			selectedValue: ''
	};

	SmartWorks.extend(options, config);
	$formXml = isEmpty(options.formXml) ? [] : $($.parseXML(options.formXml)).find('form');
	var dataField = {};
	var fieldId = (isEmpty(options.fieldId)) ? $formXml.find('formEntity[name="'+options.fieldName+'"]').attr('id') : options.fieldId;
	if(isEmpty(fieldId)) fieldId = ($formXml.attr("name") === options.fieldName) ? $formXml.attr('id') : "";
	if(isEmpty(fieldId)) return dataField;
	
	dataField = {
			id: fieldId,
			value: options.value,
			selectedValue: options.selectedValue
	};
	return dataField;
};

SmartWorks.FormRuntime.AutoIndexBuilder.serializeObject = function(autoIndexs){
	var autoIndexsJson = {};
	for(var i=0; i<autoIndexs.length; i++){
		var autoIndex = $(autoIndexs[i]);
		var fieldId = autoIndex.attr('fieldId');
		var value = autoIndex.find('.form_value > span').html();
		var selectedValue = autoIndex.find('select > option:selected').attr('value');
		autoIndexsJson[fieldId] =  {value: value, selectedValue: selectedValue};
	}
	return autoIndexsJson;
};
