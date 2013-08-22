SmartWorks.FormRuntime = SmartWorks.FormRuntime || {};

SmartWorks.FormRuntime.DataGridBuilder = {};

SmartWorks.FormRuntime.DataGridBuilder.build = function(config) {
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

	var value = "";
	if(options.dataField && options.dataField.value){
		value = JSON.parse(options.dataField.value);
	}
	value = isEmpty(value) ? "" : value.gridDatas;
	var $entity = options.entity;
	var $graphic = $entity.children('graphic');
	var readOnly = $graphic.attr('readOnly') === 'true' || options.mode === 'view';
	if(readOnly) options.mode = 'view';
	var fitWidth = $graphic.attr('fitWidth') === 'true';
	var verticalScroll = $graphic.attr('verticalScroll') === 'true';
	var id = $entity.attr('id');
	var name = $entity.attr('name');
	var contentHeightCss = (verticalScroll ? 'height:' : 'min-height:') + parseInt($graphic.attr('height')) + 'px';
	var $subEntities = $entity.find('formEntity');
	
	var $label = $('<div class="form_label ml10 tl" style="width:100%">' + name + '<span style="width:17px;height:17px"></span></div>');
	var required = $entity.attr('required');
	if(required === 'true' && !readOnly){
		$label.children('span:first').addClass('required_label');
		required = " class='fieldline required' ";
	}else{
		required = " class='fieldline' ";
	}
	if(!options.refreshData)
		$label.appendTo(options.container);
	
	var overflowPolicy = 'overflow-x:' + (fitWidth ? 'visible' : 'scroll') + ';overflow-y:' + (verticalScroll ? 'scroll' : 'visible') + ';';
	var $content = $('<div class="form_value list_contents ' + (required ? 'sw_required' : '') +'" fieldId="' + id + '" style="width:100%;' + contentHeightCss + '"><table><thead><tr class="tit_bg no_line grid_label"><th class="r_line" style="width:40px;"><span>' + smartMessage.get('rowNoText') + '</span></th></tr></thead></table></div>');
	var $table = $content.find('table');
	if(!isEmpty($subEntities)){
		var totalWidth = 70;
		if(fitWidth){
			for(var i=0; i<$subEntities.length; i++){
				if($($subEntities[i]).children('graphic').attr('hidden') === 'true') continue;
				totalWidth = totalWidth + parseInt($($subEntities[i]).children('graphic').attr('contentWidth'));
			}
		}
		var length = isEmpty(value) ? ( readOnly ? 0 : 1) : value.length;
		$table.append('<tbody style="' + overflowPolicy + '"></tbody>');
		var $gridRowTemp = $('<tr class="list_action_item border_bottom js_grid_row"><td class="tc js_grid_no"></td></tr>');
		var $gridRowHidden = $gridRowTemp.clone();
		for(var index=0; index<length; index++){
			$gridRow = $gridRowTemp.clone();
			$gridRow.find('.js_grid_no').text(index+1);
			var dataFields = value[index];
			for(var i=0; i<$subEntities.length; i++){
				var $subEntity = $($subEntities[i]);
				var $subGraphic = $subEntity.children('graphic');
				var isHidden = $subGraphic.attr('hidden') === 'true';
				if(index==0){
					var labelWidth = parseInt($subGraphic.attr('contentWidth'));
					var widthStr = (fitWidth && totalWidth>labelWidth) ? labelWidth/totalWidth*100 + '%' : labelWidth + 'px';
					var requiredStr = (!readOnly && $subEntity.attr('required') === 'true') ? '<span class="required_label" style="padding-top:5px;padding-left:2px;">&nbsp;&nbsp;</span>' : '';
					var displayStr = isHidden ? 'display:none' : '';
					$table.find('tr').append('<th class="r_line" style="width:' + widthStr + ';' + displayStr + '"><span>' + $subEntity.attr('name') + requiredStr + '</span></th>');
				}
				var fieldId = $subEntity.attr('id');
				var $cell = $('<td fieldId="' + fieldId + '"></td>');
				var dataField = {};
				if(!isEmpty(dataFields)){
					var fieldType = $subEntity.children('format').attr('type');
					if(fieldType === 'userField'){
						var tempUsers = (dataFields[fieldId])['users'];
						var users = new Array();
						if(!isEmpty(tempUsers)){
							for(var k=0; k<tempUsers.length; k++){
								var user = {};
								user['userId'] = tempUsers[k].id;
								user['longName'] = tempUsers[k].name;
								users.push(user);
							}
						}
						dataField['users'] = users;
					}else if(fieldType === 'refFormField'){
						dataField['refRecordId'] = (dataFields[fieldId])['refRecordId'];
						dataField['value'] = (dataFields[fieldId])['value'];
					}else{
						dataField['value'] = dataFields[fieldId];
					}
				}
				
				if(index==0 && !readOnly){
					var $cellHidden = $cell.clone();
					SmartWorks.FormFieldBuilder.build(options.mode, $cellHidden, $subEntity, null, options.layoutInstance, false, true);
					$gridRowHidden.append($cellHidden);
				}
				SmartWorks.FormFieldBuilder.build(options.mode, $cell, $subEntity, dataField, options.layoutInstance, isEmpty(value) ? false : options.refreshData, true);
				$gridRow.append($cell);
			}
			if(!readOnly){
				$gridRow.append('<td><div class="list_action"><div title="' + smartMessage.get('rowDeleteText') + '" class="js_delete_grid_row"> X </div></div></td>');
			}
			$table.append($gridRow);
		}
		if(!readOnly){
			$gridRowHidden.append('<td><div class="list_action"><div title="' + smartMessage.get('rowDeleteText') + '" class="js_delete_grid_row"> X </div></div></td>');
			$table.find('thead').append($gridRowHidden.hide().addClass('js_hidden_grid_row'));
			$table.find('tbody').append('<tr class="list_action_item"><td class="tc"><div class="list_action"><div title="' + smartMessage.get('rowAddText') + '" class="js_add_grid_row">+</div></div></td></tr>');
		}
	}
	if(!readOnly){
		$table.find('tr:first').append('<th style="width:20px;"><span></span></th>');
	}
	
	if ($graphic.attr('hidden') == 'true'){
		$label.hide();
		$content.hide();		
	}
	if(!options.refreshData){
		$content.appendTo(options.container);
	}else{
		var $oldGridRows = options.container.find('tbody tr.js_grid_row');
		if(isEmpty(value) || $oldGridRows.length != value.length){
			options.container.children('div.form_value').remove();
			$content.appendTo(options.container);			
		}else{
			for(var k=0; k<$oldGridRows.length; k++){
				$oldGridRow = $($oldGridRows[k]);
				var dataFields = value[k];
				for(var i=0; i<$subEntities.length; i++){
					var $subEntity = $($subEntities[i]);
					var fieldId = $subEntity.attr('id');
					var dataField = {};
					if(!isEmpty(dataFields)){
						var fieldType = $subEntity.children('format').attr('type');
						if(fieldType === 'userField'){
							var tempUsers = (dataFields[fieldId])['users'];
							var users = new Array();
							if(!isEmpty(tempUsers)){
								for(var k=0; k<tempUsers.length; k++){
									var user = {};
									user['userId'] = tempUsers[k].id;
									user['longName'] = tempUsers[k].name;
									users.push(user);
								}
							}
							dataField['users'] = users;
						}else if(fieldType === 'refFormField'){
							dataField['refRecordId'] = (dataFields[fieldId])['refRecordId'];
							dataField['value'] = (dataFields[fieldId])['value'];
						}else{
							dataField['value'] = dataFields[fieldId];
						}
					}
					var $oldCell = $oldGridRow.find('td[fieldId="' + fieldId + '"]');
					SmartWorks.FormFieldBuilder.build(options.mode, $oldCell, $subEntity, dataField, options.layoutInstance, options.refreshData, true);
				}
			}
			
		}
	}
	if(!readOnly){
		smartCommon.liveTimePicker();
		smartCommon.liveTodayPicker();
		smartCommon.liveTodayTimePicker();
	}
	return options.container;
};

SmartWorks.FormRuntime.DataGridBuilder.buildEx = function(config){
	var options = {
			container : $('<tr></tr>'),
			fieldId: '',
			fieldName: '',
			value: '',
			columns: 1,
			colSpan: 1, 
			multiLines: 1,
			required: false,
			readOnly: false		
	};
	SmartWorks.extend(options, config);

//	var labelWidth = 12;
//	if(options.columns >= 1 && options.columns <= 4 && options.colSpan <= options.columns) labelWidth = 12 * options.columns/options.colSpan;
//	$formEntity =  $($.parseXML('<formEntity id="' + options.fieldId + '" name="' + options.fieldName + '" systemType="string" required="' + options.required + '" system="false">' +
//						'<format type="dataGrid" viewingType="dataGrid"/>' +
//					    '<graphic hidden="false" readOnly="'+ options.readOnly +'" labelWidth="'+ labelWidth + '" multipleLines="' + options.multiLines + '"/>' +
//					'</formEntity>')).find('formEntity');
//	var $formCol = $('<td class="form_col js_type_dataGrid" fieldid="' + options.fieldId+ '" colspan="' + options.colSpan + '" width="' + options.colSpan/options.columns*100 + '%" rowspan="1">');
//	$formCol.appendTo(options.container);
//	SmartWorks.FormRuntime.DataGridBuilder.build({
//			mode : options.readOnly, // view or edit
//			container : $formCol,
//			entity : $formEntity,
//			dataField : SmartWorks.FormRuntime.DataGridBuilder.dataField({
//				fieldId: options.fieldId,
//				value: options.value			
//			})
//	});
	
};

SmartWorks.FormRuntime.DataGridBuilder.serializeObject = function(dataGrids){
	var gridsJson = {};
	for(var i=0; i<dataGrids.length; i++){
		var dataGrid = $(dataGrids[i]);
		var fieldId = dataGrid.attr('fieldId');
		var gridRows = dataGrid.find('.form_value tbody .js_grid_row');
		var gridDatas = new Array();
		for(var j=0; j<gridRows.length; j++){
			var gridRow = $('<form></form>').append($(gridRows[j]).clone());
			var rowSelects = $(gridRows[j]).find('select');
			var clonedSelects = gridRow.find('select');
			if(!isEmpty(rowSelects) && !isEmpty(clonedSelects) && rowSelects.length == clonedSelects.length ){
				for(var index=0; index<rowSelects.length; index++){
					$(clonedSelects[index]).attr('value', $(rowSelects[index]).attr('value'));
				}
			}
			gridDatas.push(mergeObjects(gridRow.serializeObject(), SmartWorks.GridLayout.serializeObject(gridRow)));
		}
		gridsJson[fieldId] =  {gridDatas: gridDatas};
	}
	return gridsJson;
};

SmartWorks.FormRuntime.DataGridBuilder.validate = function(dataGrids){
	var gridsValid = true;
//	for(var i=0; i<dataGrids.length; i++){
//		var dataGrid = $(dataGrids[i]);
//		var gridRows = dataGrid.find('.form_value tbody .js_grid_row');
//		var required = dataGrid.find('div.sw_required');
//		if(!isEmpty(required) && isEmpty(gridRows)){
//			dataGrid.find('div.sw_required').addClass("sw_error");
//			gridsValid = false;
//		}else{
//			dataGrid.find('div.sw_required').removeClass("sw_error");
//		}
//	}
	return gridsValid;
};

SmartWorks.FormRuntime.DataGridBuilder.dataField = function(config){
	var options = {
			fieldName: '',
			formXml: '',
			fieldId: '',
			value: ''
	};

//	SmartWorks.extend(options, config);
//	$formXml = isEmpty(options.formXml) ? [] : $($.parseXML(options.formXml)).find('form');
//	var dataField = {};
//	var fieldId = (isEmpty(options.fieldId)) ? $formXml.find('formEntity[name="'+options.fieldName+'"]').attr('id') : options.fieldId;
//	if(isEmpty(fieldId)) fieldId = ($formXml.attr("name") === options.fieldName) ? $formXml.attr('id') : "";
//	if(isEmpty(fieldId)) return dataField;
//	
//	dataField = {
//			id: fieldId,
//			value: options.value
//	};
//	return dataField;
};
