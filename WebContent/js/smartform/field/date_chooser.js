SmartWorks.FormRuntime = SmartWorks.FormRuntime || {};

SmartWorks.FormRuntime.DateChooserBuilder = {};

SmartWorks.FormRuntime.DateChooserBuilder.build = function(config) {
	var options = {
		mode : 'edit', // view or edit
		container : $('<div></div>'),
		entity : null,
		dataField : '',
		layoutInstance : null
	};

	SmartWorks.extend(options, config);
	var value = (options.dataField && options.dataField.value) || '';
	var $entity = options.entity;
	//var $graphic = $entity.children('graphic');
	var $graphic = $entity.children('graphic');
	var readOnly = $graphic.attr('readOnly') === 'true' || options.mode === 'view';
	var id = $entity.attr('id');
	var name = $entity.attr('name');
	
	var labelWidth = options.layoutInstance.getLabelWidth(id);
	var valueWidth = 100 - labelWidth;
	var $label = $('<div class="form_label" style="width:' + labelWidth + '%">' + name + '</div>');
	var required = $entity[0].getAttribute('required');
	if(required === 'true' && !readOnly){
		$('<span class="essen_n"></span>').appendTo($label);
		required = " class='fieldline js_todaypicker required' ";
	}else{
		required = " class='fieldline js_todaypicker' ";
	}
	$label.appendTo(options.container);
	
	var $text = null;
	if(readOnly){
		$text = $('<div class="form_value form_value_max_width" style="width:' + valueWidth + '%"></div>').text(value);
	}else{	
		$text = $('<div class="form_value form_value_max_width" style="width:' + valueWidth + '%"><div class="ico_fb_space form_date_input"><input readonly="readonly" type="text" name="' + id + '"' + required + '><a href="#" class="js_todaypicker_button"><span class="ico_fb_date"></span></a></div></div>').attr('value', value);
	}
	if ($graphic.attr('hidden') == 'true'){
		$label.hide();
		$text.hide();		
	}
	$text.appendTo(options.container);
	
	$.datepicker.setDefaults($.datepicker.regional[currentUser.locale]);
	$('input.js_todaypicker').datepicker({
		defaultDate : new Date(),
		dateFormat : 'yy.mm.dd',
		onSelect: function(date) {
			if($('form.js_validation_required').find('.error').length>0){
				$('form.js_validation_required').validate({ showErrors: showErrors}).form();
			}
        }
	});
	
	return options.container;
};

SmartWorks.FormRuntime.DateChooserBuilder.dataField = function(config){
	var options = {
			fieldName: '',
			formXml: '',
			value: ''
	};

	SmartWorks.extend(options, config);
	$formXml = $(options.formXml);
	var dataField = {};
	var fieldId = $formXml.find('formEntity[name="'+options.fieldName+'"]').attr('id');
	if(isZeroLength($formXml) || isEmpty(fieldId)) return dataField;
	
	dataField = {
			id: fieldId,
			value: options.value
	};
	return dataField;
};
