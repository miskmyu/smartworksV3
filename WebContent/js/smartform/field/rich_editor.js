SmartWorks.FormRuntime = SmartWorks.FormRuntime || {};

SmartWorks.FormRuntime.RichEditorBuilder = {};

var oEditors = [];
SmartWorks.FormRuntime.RichEditorBuilder.build = function(config) {
	var options = {
		mode : 'edit', // view or edit
		container : $('<div></div>'),
		entity : null,
		dataField : '',
		resizer : true,
		refreshData : false,
		layoutInstance : null
	};
	SmartWorks.extend(options, config);
	if(!options.refreshData)
		options.container.html('');
	var value = (options.dataField && options.dataField.value) || '';
//	value = smartDecode(value);
	var $entity = options.entity;
	var $graphic = $entity.find('graphic');
	var readOnly = $graphic.attr('readOnly') === 'true' || options.mode === 'view';
	var height = $graphic.attr('height');
	var id = $entity.attr('id');
	var name = $entity.attr('name');
	
	var labelWidth = (isEmpty(options.layoutInstance)) ? parseInt($graphic.attr('labelWidth')) : options.layoutInstance.getLabelWidth(id);
	var valueWidth = 100 - labelWidth;
	var $label = $('<div class="form_label" style="width:' + labelWidth + '%"><span>' + name + '</span></div>');
	var required = $entity.attr('required');
	if(required === 'true' && !readOnly){
		$label.addClass('required_label');
		required = " class='sw_required js_rich_editor_event' ";
	}else{
		required = "";
	}
	if(!options.refreshData)
		$label.appendTo(options.container);
	
	var $textarea = null;
	if(readOnly){
//		$textarea = $('<div class="form_value" style="width:' + valueWidth + '%"><iframe align="center" frameborder="0" height="100%" width="100%" class="autoHeight" scrolling="no" border="0" onload="richEditorSetValue( $(this), ' + id + ', \'' + smartEncode(value) + '\');"></iframe></div>');
		$textarea = $('<div class="form_value" style="width:' + valueWidth + '%"><span>' + (isEmpty(value) ? '&nbsp;' : value) + '</span></div>');
	}else{
		$textarea = $('<div class="form_value" style="width:' + valueWidth + '%"><span' + required + '><textarea style="width:100%; height:' + height + 'px;display:none" id="' + id + '">'+ value.replace(/textarea/g, "div") +'</textarea></span></div>');
	}
	if ($graphic.attr('hidden') == 'true'){
		$label.hide();
		$textarea.hide();		
	}
	if(!options.refreshData){
		$textarea.appendTo(options.container);
		if (!readOnly) {
			var skinURI = (currentUser.locale == 'ko') ? "smarteditor/SEditorSkinKOR.html" : "smarteditor/SEditorSkinENG.html";
			nhn.husky.EZCreator.createInIFrame({
				oAppRef: oEditors,
				elPlaceHolder: id,
				sSkinURI: skinURI,
				fCreator: "createSEditorInIFrame"
			});
			options.container.find('iframe').contents().find('iframe').css({width:"100%"});
		}
	}else{
		if(readOnly) {
//			options.container.find('.form_value').find('iframe').contents().find('html').html(value);
//			doIframeAutoHeight();
			options.container.find('.form_value').html('');
			options.container.find('.form_value').append(isEmpty(value) ? '<span>&nbsp;</span>' : '<span>' + value + '</span>');
		} else {
			options.container.find('.form_value textarea').html(value);
			options.container.find('iframe').contents().find('iframe').css({width:"100%"}).contents().find('.smartOutput').html(value);
		}
	}

	if (readOnly) {
		var $richEditorHiddenInput = $('#richEditorHiddenInput'+id);
		if ($richEditorHiddenInput.length === 0) {
			options.container.append("<input id='richEditorHiddenInput"+id+"' type='hidden' name='" + id + "' value='" + smartEncode(value) + "'>");
		} else {
			$richEditorHiddenInput.attr('value', smartEncode(value));
		}
	}
	
	return options.container;
};

richEditorSetValue = function($this, id, value){
	var smartForm = $this.parents('form[name="frmSmartForm"]');
	var richEditor = smartForm.find('.js_type_richEditor[fieldId="' + id + '"]');
	var frame = richEditor.find('iframe');
	frame.contents().find('html').html("");
	value = smartDecode(value);
	frame.contents().find('html').html('<link href="css/default-iframe.css" type="text/css" rel="stylesheet" />' + value);
	doIframeAutoHeight();
};

SmartWorks.FormRuntime.RichEditorBuilder.buildEx = function(config){
	var options = {
			container : $('<tr></tr>'),
			fieldId: '',
			fieldName: '',
			value: '',
			columns: 1,
			colSpan: 1,
			resizer: true,
			required: false,
			readOnly: false		
	};
	SmartWorks.extend(options, config);

	var labelWidth = 12;
	if(options.columns >= 1 && options.columns <= 4 && options.colSpan <= options.columns) labelWidth = 12 * options.columns/options.colSpan;
	$formEntity =  $($.parseXML('<formEntity id="' + options.fieldId + '" name="' + options.fieldName + '" systemType="string" required="' + options.required + '" system="false">' +
						'<format type="richEditor" viewingType="richEditor"/>' +
					    '<graphic hidden="false" readOnly="'+ options.readOnly +'" labelWidth="'+ labelWidth + '"/>' +
					'</formEntity>')).find('formEntity');
	var $formCol = $('<td class="form_col js_type_richEditor" fieldid="' + options.fieldId+ '" colspan="' + options.colSpan + '" width="' + options.colSpan/options.columns*100 + '%" rowspan="1">');
	$formCol.appendTo(options.container);
	SmartWorks.FormRuntime.RichEditorBuilder.build({
			mode : options.readOnly, // view or edit
			container : $formCol,
			entity : $formEntity,
			resizer : options.resizer,
			dataField : SmartWorks.FormRuntime.RichEditorBuilder.dataField({
				fieldId: options.fieldId,
				value: options.value
			})
	});
	
};

SmartWorks.FormRuntime.RichEditorBuilder.serializeObject = function(richEditors, valueChanged){
	var richEditorsJson = {};
	for(var i=0; i<richEditors.length; i++){
		var richEditor = $(richEditors[i]);
		var id = richEditor.attr('fieldId');
		if(valueChanged && !isEmpty(oEditors) && !isEmpty(oEditors.getById[id])){
			oEditors.getById[id].exec("UPDATE_IR_FIELD", []);
		}
		var valueField = richEditor.find('textarea');
		if(!isEmpty(valueField)) richEditorsJson[richEditor.attr('fieldId')] = valueField[0].value;
	}
	return richEditorsJson;
};

SmartWorks.FormRuntime.RichEditorBuilder.validate = function(richEditors){
	var richEditorsValid = true;
	for(var i=0; i<richEditors.length; i++){
		var richEditor = $(richEditors[i]);
		var id = richEditor.attr('fieldId');
		
		if(!isEmpty(oEditors) && !isEmpty(oEditors.getById[id])) oEditors.getById[id].exec("UPDATE_IR_FIELD", []);
		var value = richEditor.find('textarea')[0] ? richEditor.find('textarea')[0].value : (richEditor.find('input')[0]) ? richEditor.find('input')[0].value : null;
		//var value = richEditor.find('textarea')[0].value;
		
		var required = richEditor.find('span.sw_required');
		if(!isEmpty(required) && (isEmpty(value) || value === "<br>")){
			richEditor.find('span.sw_required').addClass("sw_error");
			richEditorsValid = false;
		}else{
			richEditor.find('span.sw_required').removeClass("sw_error");
		}
	}
	return richEditorsValid;
};

SmartWorks.FormRuntime.RichEditorBuilder.dataField = function(config){
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

SmartWorks.FormRuntime.RichEditorBuilder.getValue = function(richEditors){
	if(isEmpty(richEditors)) return "";
	
	var richEditor = $(richEditors[0]);
	var id = richEditor.attr('fieldId');
	if(!isEmpty(oEditors) && !isEmpty(oEditors.getById[id])){
		oEditors.getById[id].exec("UPDATE_IR_FIELD", []);
	}
	var valueField = richEditor.find('textarea');
	if(!isEmpty(valueField)) return valueField[0].value;
	return "";
};


