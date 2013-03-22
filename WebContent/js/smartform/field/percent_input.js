SmartWorks.FormRuntime = SmartWorks.FormRuntime || {};

SmartWorks.FormRuntime.PercentInputBuilder = {};

SmartWorks.FormRuntime.PercentInputBuilder.build = function(config) {
	var options = {
		mode : 'edit', // view or edit
		container : $('<div></div>'),
		entity : '',
		dataField : '',
		refreshData : false,
		layoutInstance : null,
		isDataGrid : false
	};

	SmartWorks.extend(options, config);
	if(!options.refreshData)
		options.container.html('');

	var value = (options.dataField==null || isEmpty(options.dataField.value)) ? '' : parseFloat(options.dataField.value);
	$entity = options.entity;
	$graphic = $entity.find('graphic');
	$format = $entity.find('format');

	var readOnly = $graphic.attr('readOnly') == 'true' || options.mode == 'view';
	var id = $entity.attr('id');
	var name = $entity.attr('name');

	var labelWidth = (isEmpty(options.layoutInstance)) ? parseInt($graphic.attr('labelWidth')) : options.layoutInstance.getLabelWidth(id);
	var valueWidth = 100 - (options.isDataGrid ? 0 : labelWidth);
	var $label = $('<div class="form_label" style="width:' + labelWidth + '%"><span>' + name + '</span></div>');
	var required = $entity.attr('required');
	if(required === 'true' && !readOnly){
		$label.addClass('required_label');
		required = " class='js_percent_input fieldline tr sw_required' ";
	}else{
		required = " class='js_percent_input fieldline tr' ";
	}
	if(!options.refreshData && !options.isDataGrid)
		$label.appendTo(options.container);

	var $percent = null;
	
	if (readOnly) {
		if(value==''){
			$percent = $('<div class="form_value form_number_input" style="width:' + valueWidth + '%"><span>&nbsp;</span></div>');
		}else{
			//$percent = $('<div class="form_value form_number_input" style="width:' + valueWidth + '%"><span>' + $(value).formatCurrency({ symbol: '' ,colorize: true, negativeFormat: '-%s%n', roundToDecimalPlace: -1, eventOnDecimalsEntered: true }) + '</span></div>');
			$percent = $('<div class="form_value form_number_input" style="width:' + valueWidth + '%"><span>' + value + '</span></div>');
			$percent.find('span').formatCurrency({ symbol: '' ,colorize: true, negativeFormat: '-%s%n', roundToDecimalPlace: -1, eventOnDecimalsEntered: true });
			$percent.text($percent.text() + "%");
		}
	} else {
		$percent = $('<div name="' + id + '" class="form_value form_number_input" style="width:' + valueWidth + '%"><input type="text"' + required + '/></div>');
		if(value!=''){
			$percent.find('input').attr('value', value).formatCurrency({ symbol: '' ,colorize: true, negativeFormat: '-%s%n', roundToDecimalPlace: -1, eventOnDecimalsEntered: true });
			$percent.find('input').attr('value', $percent.find('input').attr('value') + '%');
		}
	}

	if ($graphic.attr('hidden') == 'true'){
		$label.hide();
		$percent.hide();		
	}
	if(!options.refreshData){
		$percent.appendTo(options.container);
	}else if(value!=''){
		if(readOnly){
			options.container.find('.form_value span').html(isEmpty(value) ? '&nbsp;' : value).formatCurrency({ symbol: '' ,colorize: true, negativeFormat: '-%s%n', roundToDecimalPlace: -1, eventOnDecimalsEntered: true });
			options.container.find('.form_value span').text(options.container.find('.form_value span').text() + '%');
		}else{
			options.container.find('.form_value input').attr('value', value).formatCurrency({ symbol: '' ,colorize: true, negativeFormat: '-%s%n', roundToDecimalPlace: -1, eventOnDecimalsEntered: true });
			options.container.find('.form_value input').attr('value', options.container.find('.form_value input').attr('value') + '%');
		}
	}

	if (readOnly) {
		var $percentHiddenInput = $('#percentHiddenInput'+id);
		if ($percentHiddenInput.length === 0) {
			options.container.append('<input id="percentHiddenInput'+id+'" type="hidden" name="' + id + '" value="' + value + '">');
		} else {
			$percentHiddenInput.attr('value', value);
		}
	}
	
	return options.container;
};

$('input.js_percent_input').live('keyup', function(e) {
	var e = window.event || e;
	var keyUnicode = e.charCode || e.keyCode;
	if (e !== undefined) {
		switch (keyUnicode) {
			case 16: break; // Shift
			case 17: break; // Ctrl
			case 18: break; // Alt
			case 27: this.value = ''; break; // Esc: clear entry
			case 35: break; // End
			case 36: break; // Home
			case 37: break; // cursor left
			case 38: break; // cursor up
			case 39: break; // cursor right
			case 40: break; // cursor down
			case 78: break; // N (Opera 9.63+ maps the "." from the number key section to the "N" key too!) (See: http://unixpapa.com/js/key.html search for ". Del")
			case 110: break; // . number block (Opera 9.63+ maps the "." from the number block to the "N" key (78) !!!)
			case 190: break; // .
			default:
				value = $(this).attr('value');
				if(value && value.length>0 && (value=='%' || value.substring(value.length-1, 1)=='%')) $(this).attr('value', value.substirng(0, value.length-1));
				if($(this).attr('value') === '0-') $(this).attr('value', '-');
				var value = $(this).attr('value');
				var firstStr = value.substring(0,1);
				var secondStr = value.substring(1,2);
				if(value !== '-' && value !== '.')
					if(isEmpty(value) || (firstStr !== '-' && firstStr !== '.' && (firstStr<'0' || firstStr>'9')) || (((firstStr === '-' && secondStr !== '.') || firstStr === '.') && (secondStr<'0' || secondStr>'9'))){
						$(this).attr('value', 0);
					}
				$(this).formatCurrency({ symbol: '' ,colorize: true, negativeFormat: '-%s%n', roundToDecimalPlace: -1, eventOnDecimalsEntered: true });
		}
	}
});

SmartWorks.FormRuntime.PercentInputBuilder.buildEx = function(config){
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
						'<format type="percentInput" viewingType="percentInput"/>' +
					    '<graphic hidden="false" readOnly="'+ options.readOnly +'" labelWidth="'+ labelWidth + '"/>' +
					'</formEntity>')).find('formEntity');
	var $formCol = $('<td class="form_col js_type_percentInput" fieldid="' + options.fieldId+ '" colspan="' + options.colSpan + '" width="' + options.colSpan/options.columns*100 + '%" rowspan="1">');
	$formCol.appendTo(options.container);
	SmartWorks.FormRuntime.PercentInputBuilder.build({
			mode : options.readOnly, // view or edit
			container : $formCol,
			entity : $formEntity,
			dataField : SmartWorks.FormRuntime.PercentInputBuilder.dataField({
				fieldId: options.fieldId,
				value: options.value			
			})
	});
	
};

SmartWorks.FormRuntime.PercentInputBuilder.dataField = function(config){
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

SmartWorks.FormRuntime.PercentInputBuilder.serializeObject = function(percentInputs){
	var percentInputsJson = {};
	for(var i=0; i<percentInputs.length; i++){
		var percentInput = $(percentInputs[i]);
		var valueStr = percentInput.find('input').attr('value');
		percentInputsJson[percentInput.attr('fieldId')] = $.parseNumber( valueStr.substring(0, valueStr.length-1), {format:"-0,000.0", locale: currentUser.locale });
	}
	return percentInputsJson;
};


SmartWorks.FormRuntime.PercentInputBuilder.validate = function(percentInputs){
	var percentInputsValid = true;
	for(var i=0; i<percentInputs.length; i++){
		var percentInput = $(percentInputs[i]);
		var input = percentInput.find('input.sw_required');
		if(isEmpty(input)) continue;
		if(isEmpty(input.attr('value'))){
			input.addClass("sw_error");
			percentInputsValid = false;
		}
	}
	return percentInputsValid;
};
