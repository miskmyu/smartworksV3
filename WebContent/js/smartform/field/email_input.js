SmartWorks.FormRuntime = SmartWorks.FormRuntime || {};

SmartWorks.FormRuntime.EmailInputBuilder = {};

SmartWorks.FormRuntime.EmailInputBuilder.build = function(config) {
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
	var readOnly = $graphic.attr('readOnly') === 'true' || options.mode === 'view';
	var id = $entity.attr('id');
	var name = $entity.attr('name');
	
	var labelWidth = (isEmpty(options.layoutInstance)) ? parseInt($graphic.attr('labelWidth')) : options.layoutInstance.getLabelWidth(id);
	var valueWidth = 100 - labelWidth;
	var $label = $('<div class="form_label" style="width:' + labelWidth + '%"><span>' + name + '</span></div>');
	var required = $entity.attr('required');
	if(required === 'true' && !readOnly){
		$label.addClass('required_label');
		required = " class='fieldline required email' ";
	}else{
		required = " class='fieldline email' ";
	}
	if(!options.refreshData)	
		$label.appendTo(options.container);
	
	var $email = null;
	if(readOnly){
		$email = $('<div class="form_value" style="width:' + valueWidth + '%"></div>').text(value);
	}else{	
		$email = $('<div class="form_value" style="width:' + valueWidth + '%"><input type="text" name="' + id + '"' + required + '></div>');
		$email.find('input').attr('value', value);
	}
	if ($graphic.attr('hidden') == 'true'){
		$label.hide();
		$email.hide();		
	}
	if(!options.refreshData){	
		$email.appendTo(options.container);
	}else{
		if(readOnly)
			options.container.find('.form_value').text(value);
		else
			options.container.find('.form_value input').attr('value', value);
	}

	if (readOnly)
		options.container.append('<input type="hidden" name="' + id + '" value="' + value + '">');
	
	return options.container;
};

SmartWorks.FormRuntime.EmailInputBuilder.buildEx = function(config){
	var options = {
			container : $('<tr></tr>'),
			fieldId: '',
			fieldName: '',
			value: '',
			columns: 1,
			colSpan: 1, 
			required: false,
			readOnly: false		
	};
	SmartWorks.extend(options, config);

	var labelWidth = 12;
	if(options.columns >= 1 && options.columns <= 4 && options.colSpan <= options.columns) labelWidth = 12 * options.columns/options.colSpan;
	$formEntity =  $($.parseXML('<formEntity id="' + options.fieldId + '" name="' + options.fieldName + '" systemType="string" required="' + options.required + '" system="false">' +
						'<format type="emailInput" viewingType="emailInput"/>' +
					    '<graphic hidden="false" readOnly="'+ options.readOnly +'" labelWidth="'+ labelWidth + '"/>' +
					'</formEntity>')).find('formEntity');
	var $formCol = $('<td class="form_col js_type_emailInput" fieldid="' + options.fieldId+ '" colspan="' + options.colSpan + '" width="' + options.colSpan/options.columns*100 + '%" rowspan="1">');
	$formCol.appendTo(options.container);
	SmartWorks.FormRuntime.EmailInputBuilder.build({
			mode : options.readOnly, // view or edit
			container : $formCol,
			entity : $formEntity,
			dataField : SmartWorks.FormRuntime.EmailInputBuilder.dataField({
				fieldId: options.fieldId,
				value: options.value			
			})
	});
	
};

SmartWorks.FormRuntime.EmailInputBuilder.dataField = function(config){
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
