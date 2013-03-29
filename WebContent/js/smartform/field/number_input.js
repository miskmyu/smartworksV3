SmartWorks.FormRuntime = SmartWorks.FormRuntime || {};

SmartWorks.FormRuntime.NumberInputBuilder = {};

SmartWorks.FormRuntime.NumberInputBuilder.build = function(config) {
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
	var value = (options.dataField==null || isEmpty(options.dataField.value)) ? '' : parseFloat(options.dataField.value);
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
		required = " class='js_number_input fieldline tr sw_required' ";
	}else{
		required = " class='js_number_input fieldline tr' ";
	}
	if(!options.refreshData & !options.isDataGrid)
		$label.appendTo(options.container);
	
	var $number = null;
	if(readOnly){
		if(value==='')
			$number = $('<div class="form_value form_number_input" style="width:' + valueWidth + '%"><span>&nbsp;</span></div>');
		else
			//$number = $('<div class="form_value form_number_input" style="width:' + valueWidth + '%"><span>' + $(value).formatCurrency({ symbol: '' ,colorize: true, negativeFormat: '-%s%n', roundToDecimalPlace: -1, eventOnDecimalsEntered: true }) + '</span></div>');
			$number = $('<div class="form_value form_number_input tr" style="width:' + valueWidth + '%"><span>' + value + '</span></div>');
			$number.find('span').formatCurrency({ symbol: '' ,colorize: true, negativeFormat: '-%s%n', roundToDecimalPlace: -1, eventOnDecimalsEntered: true });
	}else{	
		$number = $('<div name="' + id + '" class="form_value form_number_input" style="width:' + valueWidth + '%"><input type="text"' + required + '></div>');
		if(value!='')
			$number.find('input').attr('value',value).formatCurrency({ symbol: '' ,colorize: true, negativeFormat: '-%s%n', roundToDecimalPlace: -1, eventOnDecimalsEntered: true });
	}
	if ($graphic.attr('hidden') == 'true'){
		$label.hide();
		$number.hide();		
	}
	
	if(!options.refreshData){
		$number.appendTo(options.container);
	}else if(value!=''){
		if(readOnly)
			options.container.find('.form_value span').html(isEmpty(value) ? '&nbsp;' : value).formatCurrency({ symbol: '' ,colorize: true, negativeFormat: '-%s%n', roundToDecimalPlace: -1, eventOnDecimalsEntered: true });
		else
			options.container.find('.form_value input').attr('value', value).formatCurrency({ symbol: '' ,colorize: true, negativeFormat: '-%s%n', roundToDecimalPlace: -1, eventOnDecimalsEntered: true });
	}

	if (readOnly) {
		var $numberHiddenInput = $('#numberHiddenInput'+id);
		if ($numberHiddenInput.length === 0) {
			options.container.append('<input id="numberHiddenInput'+id+'" type="hidden" name="' + id + '" value="' + value + '">');
		} else {
			$numberHiddenInput.attr('value', value);
		}
	}
	
	return options.container;
};

$('input.js_number_input').live('keyup', function(e) {
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

SmartWorks.FormRuntime.NumberInputBuilder.buildEx = function(config){
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
						'<format type="numberInput" viewingType="numberInput"/>' +
					    '<graphic hidden="false" readOnly="'+ options.readOnly +'" labelWidth="'+ labelWidth + '"/>' +
					'</formEntity>')).find('formEntity');
	var $formCol = $('<td class="form_col js_type_numberInput" fieldid="' + options.fieldId+ '" colspan="' + options.colSpan + '" width="' + options.colSpan/options.columns*100 + '%" rowspan="1">');
	$formCol.appendTo(options.container);
	SmartWorks.FormRuntime.NumberInputBuilder.build({
			mode : options.readOnly, // view or edit
			container : $formCol,
			entity : $formEntity,
			dataField : SmartWorks.FormRuntime.NumberInputBuilder.dataField({
				fieldId: options.fieldId,
				value: options.value			
			})
	});
	
};

SmartWorks.FormRuntime.NumberInputBuilder.dataField = function(config){
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

SmartWorks.FormRuntime.NumberInputBuilder.serializeObject = function(numberInputs){
	
	var numberInputsJson = {};
	for(var i=0; i<numberInputs.length; i++){
		var numberInput = $(numberInputs[i]);
		var valueStr = numberInput.find('input').attr('value');
		numberInputsJson[numberInput.attr('fieldId')] = isEmpty(valueStr) ? '' : $.parseNumber( valueStr, {format:"-0,000.0", locale: currentUser.locale })+'';
	}
	return numberInputsJson;
};

SmartWorks.FormRuntime.NumberInputBuilder.validate = function(numberInputs){
	var numberInputsValid = true;
	for(var i=0; i<numberInputs.length; i++){
		var numberInput = $(numberInputs[i]);
		var input = numberInput.find('input.sw_required');
		if(isEmpty(input)) continue;
		if(isEmpty(input.attr('value'))){
			input.addClass("sw_error");
			numberInputsValid = false;
		}
	}
	return numberInputsValid;
};
