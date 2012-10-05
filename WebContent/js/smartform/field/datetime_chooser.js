SmartWorks.FormRuntime = SmartWorks.FormRuntime || {};

SmartWorks.FormRuntime.DateTimeChooserBuilder = {};

SmartWorks.FormRuntime.DateTimeChooserBuilder.build = function(config) {
	var options = {
		mode : 'edit', // view or edit
		container : $('<td></td>'),
		entity : null,
		dataField : '',
		refreshData : false,
		layoutInstance : null,
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
		required = " class='fieldline js_todaytimepicker required' ";
	}else{
		required = " class='fieldline js_todaytimepicker' ";
	}
	if(!options.refreshData)
		$label.appendTo(options.container);

	var $text = null;
	if(readOnly){
		$text = $('<div class="form_value form_value_max_width" style="width:' + valueWidth + '%"><span></span></div>').find('span').text(isEmpty(value) ? '&nbsp;' : value);
	}else{	
		$text = $('<div class="form_value form_value_max_width" style="width:' + valueWidth + '%"><div class="icon_fb_space form_datetime_input"><input readonly="readonly" type="text" name="' + id + '"' + required + '><a href="" class="js_todaytimepicker_button"><span class="icon_fb_time"></span></a></div></div>');
		$text.find('input').attr('value', value);
	}
	if ($graphic.attr('hidden') == 'true'){
		$label.hide();
		$text.hide();		
	}
	
	if(!options.refreshData){
		$text.appendTo(options.container);	
		smartCommon.liveTodayTimePicker();
	}else{
		if(readOnly)
			options.container.find('.form_value span').text(isEmpty(value) ? '&nbsp' : value);
		else
			options.container.find('.form_value input').attr('value', value);
	}
	
	if (readOnly) {
		var $dateTimeChooserHiddenInput = $('#dateTimeChooserHiddenInput'+id);
		if ($dateTimeChooserHiddenInput.length === 0) {
			options.container.append('<input id="dateTimeChooserHiddenInput'+id+'" type="hidden" name="' + id + '" value="' + value + '">');
		} else {
			$dateTimeChooserHiddenInput.attr('value', value);
		}
	}

	return options.container;
};

SmartWorks.FormRuntime.DateTimeChooserBuilder.buildEx = function(config){
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
	$formEntity =  $($.parseXML('<formEntity id="' + options.fieldId + '" name="' + options.fieldName + '" systemType="datetime" required="' + options.required + '" system="false">' +
						'<format type="datetimeChooser" viewingType="datetimeChooser"/>' +
					    '<graphic hidden="false" readOnly="'+ options.readOnly +'" labelWidth="'+ labelWidth + '"/>' +
					'</formEntity>')).find('formEntity');
	var $formCol = $('<td class="form_col js_type_datetimeChooser" fieldid="' + options.fieldId+ '" colspan="' + options.colSpan + '" width="' + options.colSpan/options.columns*100 + '%" rowspan="1">');
	$formCol.appendTo(options.container);
	SmartWorks.FormRuntime.DateTimeChooserBuilder.build({
			mode : options.readOnly, // view or edit
			container : $formCol,
			entity : $formEntity,
			dataField : SmartWorks.FormRuntime.DateTimeChooserBuilder.dataField({
				fieldId: options.fieldId,
				value: options.value				
			})
	});
	
};

SmartWorks.FormRuntime.DateTimeChooserBuilder.dataField = function(config){
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
