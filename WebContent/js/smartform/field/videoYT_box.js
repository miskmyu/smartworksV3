SmartWorks.FormRuntime = SmartWorks.FormRuntime || {};

SmartWorks.FormRuntime.VideoYTBoxBuilder = {};

SmartWorks.FormRuntime.VideoYTBoxBuilder.build = function(config) {
	var options = {
		mode : 'view', // view or edit
		container : $('<div></div>'),
		entity : null,
		dataField : '',
		refreshData : false,
		layoutInstance : null
	};

	SmartWorks.extend(options, config);

	if(options.refreshData) return options.container;
	
	options.container.html('');

	var value = (options.dataField && options.dataField.value) || '';
	var fileList = (options.dataField && options.dataField.fileList) || '';
	var $entity = options.entity;
	var $graphic = $entity.children('graphic');
	var videoWidth = parseFloat($graphic.attr('videoWidth'));
	var videoHeight = parseFloat($graphic.attr('videoHeight'));
	var readOnly = $graphic.attr('readOnly') === 'true' || options.mode === 'view';
	var id = $entity.attr('id');
	var name = $entity.attr('name');

	var labelWidth = (isEmpty(options.layoutInstance)) ? parseInt($graphic.attr('labelWidth')) : options.layoutInstance.getLabelWidth(id);
	var valueWidth = 100 - labelWidth;

	var spanRequired = "";
	var required = $entity[0].getAttribute('required');
	if(required === 'true' && !readOnly){
		spanRequired = '<div class="label_required"></div>';
		required = ' class="form_value sw_required" ';
	}else{
		required = ' class="form_value" ';
	}
	
	var videoSize = 'style="min-height:0px;width:' + ((videoWidth) ? videoWidth : 300) + 'px; height: ' + ((videoHeight && !isEmpty(value)) ? videoHeight : 0) + 'px"';
	var params = (isBlank(value)) ? '' : '<param name="movie" value="https://www.youtube.com/v/' + value + '?version=3&autohide=1&showinfo=0"></param>' +
										 '<param name="allowScriptAccess" value="always"></param>' +
										 '<embed src="https://www.youtube.com/v/' + value + '?version=3&autohide=1&showinfo=0"' + 
										 		'type="application/x-shockwave-flash" allowscriptaccess="always" ' + videoSize + '>' +
										 '</embed>';
 
	var $video = $('<div ' + required + ' style="width:' + valueWidth + '%"><object ' + videoSize + '>' + params + '</object></div>');
	var $label = null;
		
	$label = $('<div class="form_label" style="width:' + labelWidth + '%"><span id="' + id + '"></span></div>').append(spanRequired);
	$label.appendTo(options.container);
	$video.appendTo(options.container);	
	if ($graphic.attr('hidden') == 'true'){
		$label.hide();
		$video.hide();		
	}

	if (!readOnly) {
		createYTUploader(value, $('#'+id), fileList);
	}
	return options.container;

};
SmartWorks.FormRuntime.VideoYTBoxBuilder.buildEx = function(config){
	var options = {
			container : $('<tr></tr>'),
			fieldId: '',
			fieldName: '',
			videoYTId: '',	//데이터 값으로 YouTube Id를 입력하면 동영상을 가져와 보여 준다.
			fileList: null,
			columns: 1,
			colSpan: 1,
			videoWidth: 300,
			videoHeight: 0,
			required: false,
			readOnly: false		
	};
	SmartWorks.extend(options, config);
	var labelWidth = 12;
	if(options.columns >= 1 && options.columns <= 4 && options.colSpan <= options.columns) labelWidth = 12 * options.columns/options.colSpan;
	$formEntity =  $('<formEntity id="' + options.fieldId + '" name="' + options.fieldName + '" systemType="string" required="' + options.required + '" system="false">' +
						'<format type="videoYTBox" viewingType="videoYTBox"/>' +
					    '<graphic hidden="false" readOnly="'+ options.readOnly + '" labelWidth="'+ labelWidth + '" videoWidth="'+ options.videoWidth + '" videoHeight="' + options.videoHeight + '"/>' +
					'</formEntity>');
	var $formCol = $('<td class="form_col js_type_videoYTBox" fieldid="' + options.fieldId + '"  colspan="' + options.colSpan + '" width="' + options.colSpan/options.columns*100 + '%" rowspan="1">');
	$formCol.appendTo(options.container);
	SmartWorks.FormRuntime.VideoYTBoxBuilder.build({
			mode : options.readOnly, // view or edit
			container : $formCol,
			entity : $formEntity,
			dataField : SmartWorks.FormRuntime.VideoYTBoxBuilder.dataField({
				fieldName: options.fieldName,
				formXml: $formEntity,
				videoYTId: options.videoYTId,
				fileList: options.fileList
			})
	});
	
};

SmartWorks.FormRuntime.VideoYTBoxBuilder.serializeObject = function(videoYTBoxs){
	var fileUploaders = videoYTBoxs.find('.qq-uploader');
	var videosJson = {};
	for(var i=0; i<fileUploaders.length; i++){
		var fileUploader = $(fileUploaders[i]);
		var fieldId = fileUploader.parent('span').attr('id');
		var video = fileUploader.find('.qq-upload-success');
		var videoJson = {};
		if(!isEmpty(video)){
			videoJson['video'] = {videoYTId : video.attr('videoYTId'), fileName : video.attr('fileName'), fileSize : video.attr('fileSize')};
		}
		videosJson[fieldId] =  videoJson;
	}
	return videosJson;
};

SmartWorks.FormRuntime.VideoYTBoxBuilder.validate = function(videoYTBoxs){
	if(isEmpty(videoYTBoxs.find('.sw_required'))) return true;
	var fileUploaders = videoYTBoxs.find('.qq-uploader');
	var videosValid = true;
	for(var i=0; i<fileUploaders.length; i++){
		var fileUploader = $(fileUploaders[i]);
		var videos = fileUploader.find('.qq-upload-success');
		if(isEmpty(videos)){
			fileUploader.parents('.js_type_videoYTBox:first').find('div.sw_required').addClass("sw_error");
			videosValid = false;
		}
	}
	return videosValid;
};

SmartWorks.FormRuntime.VideoYTBoxBuilder.dataField = function(config){
	var options = {
			fieldName: '',
			formXml: '',
			videoYTId: '',
			fileList: null
	};

	SmartWorks.extend(options, config);

	$formXml = $(options.formXml);
	var dataField = {};
	var fieldId = $formXml.find('formEntity[name="'+options.fieldName+'"]').attr('id');
	if(isEmpty(fieldId)) fieldId = ($formXml.attr("name") === options.fieldName) ? $formXml.attr('id') : "";
	if(isEmpty($formXml) || isEmpty(fieldId)) return dataField;
	
	dataField = {
			id: fieldId,
			value: options.videoYTId,
			fileList: options.fileList
	};
	return dataField;
};
