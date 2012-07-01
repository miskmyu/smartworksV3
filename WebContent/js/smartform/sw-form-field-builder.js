SmartWorks.FormFieldBuilder = {};
SmartWorks.FormFieldBuilder.build = function(mode, $target, $entity, dataField, layoutInstance, refreshData) {
	var type = $entity.find('format').attr('type');
	$target.addClass('js_type_'+ type);
	switch(type) {
	case 'checkBox' :
		SmartWorks.FormRuntime.CheckBoxBuilder.build({
			mode : mode, // view or edit
			container : $target,
			entity : $entity,
			dataField : dataField,
			refreshData : refreshData,
			layoutInstance : layoutInstance
		});
		return;		
	case 'comboBox' :
		SmartWorks.FormRuntime.ComboBoxBuilder.build({
			mode : mode, // view or edit
			container : $target,
			entity : $entity,
			dataField : dataField,
			refreshData : refreshData,
			layoutInstance : layoutInstance
		});
		return;		
	case 'autoIndex' :
		SmartWorks.FormRuntime.AutoIndexBuilder.build({
			mode : mode, // view or edit
			container : $target,
			entity : $entity,
			dataField : dataField,
			refreshData : refreshData,
			layoutInstance : layoutInstance
		});
		return;		
	case 'currencyInput' :
		SmartWorks.FormRuntime.CurrencyInputBuilder.build({
			mode : mode, // view or edit
			container : $target,
			entity : $entity,
			dataField : dataField,
			refreshData : refreshData,
			layoutInstance : layoutInstance
		});
		return;		
	case 'dateChooser' :
		SmartWorks.FormRuntime.DateChooserBuilder.build({
			mode : mode, // view or edit
			container : $target,
			entity : $entity,
			dataField : dataField,
			refreshData : refreshData,
			layoutInstance : layoutInstance
		});
		return;
	case 'emailIDInput' :
		SmartWorks.FormRuntime.EmailInputBuilder.build({
			mode : mode, // view or edit
			container : $target,
			entity : $entity,
			dataField : dataField,
			refreshData : refreshData,
			layoutInstance : layoutInstance
		});
		return;
	case 'fileField' :
		SmartWorks.FormRuntime.FileFieldBuilder.build({
			mode : mode, // view or edit
			container : $target,
			entity : $entity,
			dataField : dataField,
			refreshData : refreshData,
			layoutInstance : layoutInstance
		});
		return;
	case 'numberInput' :
		SmartWorks.FormRuntime.NumberInputBuilder.build({
			mode : mode, // view or edit
			container : $target,
			entity : $entity,
			dataField : dataField,
			refreshData : refreshData,
			layoutInstance : layoutInstance
		});
		return;
	case 'percentInput' :
		SmartWorks.FormRuntime.PercentInputBuilder.build({
			mode : mode, // view or edit
			container : $target,
			entity : $entity,
			dataField : dataField,
			refreshData : refreshData,
			layoutInstance : layoutInstance
		});
		return;
	case 'radioButton' :
		SmartWorks.FormRuntime.RadioButtonBuilder.build({
			mode : mode, // view or edit
			container : $target,
			entity : $entity,
			dataField : dataField,
			refreshData : refreshData,
			layoutInstance : layoutInstance
		});
		return;
	case 'richEditor' :
		SmartWorks.FormRuntime.RichEditorBuilder.build({
			mode : mode, // view or edit
			container : $target,
			entity : $entity,
			dataField : dataField,
			refreshData : refreshData,
			layoutInstance : layoutInstance
		});
		return;
	case 'textInput' :
		SmartWorks.FormRuntime.TextInputBuilder.build({
			mode : mode, // view or edit
			container : $target,
			entity : $entity,
			dataField : dataField,
			refreshData : refreshData,
			layoutInstance : layoutInstance
		});
		return;
	case 'timeField' :
		SmartWorks.FormRuntime.TimeChooserBuilder.build({
			mode : mode, // view or edit
			container : $target,
			entity : $entity,
			dataField : dataField,
			refreshData : refreshData,
			layoutInstance : layoutInstance
		});
		return;
	case 'userField' :
		SmartWorks.FormRuntime.UserFieldBuilder.build({
			mode : mode, // view or edit
			container : $target,
			entity : $entity,
			dataField : dataField,
			refreshData : refreshData,
			layoutInstance : layoutInstance
		});
		return;	
	case 'departmentField' :
		SmartWorks.FormRuntime.DepartmentFieldBuilder.build({
			mode : mode, // view or edit
			container : $target,
			entity : $entity,
			dataField : dataField,
			refreshData : refreshData,
			layoutInstance : layoutInstance
		});
		return;	
	case 'dateTimeChooser' :
		SmartWorks.FormRuntime.DateTimeChooserBuilder.build({
			mode : mode, // view or edit
			container : $target,
			entity : $entity,
			dataField : dataField,
			refreshData : refreshData,
			layoutInstance : layoutInstance
		});
		return;	
	case 'refFormField' :
		SmartWorks.FormRuntime.RefFormFieldBuilder.build({
			mode : mode, // view or edit
			container : $target,
			entity : $entity,
			dataField : dataField,
			refreshData : refreshData,
			layoutInstance : layoutInstance
		});
		return;	
	case "imageBox":
		SmartWorks.FormRuntime.ImageBoxBuilder.build({
			mode : mode, // view or edit
			container : $target,
			entity : $entity,
			dataField : dataField,
			refreshData : refreshData,
			layoutInstance : layoutInstance
		});
		return;	
	case "dataGrid":
	case "numericStepper":
	case "textArea":
	default :
		SmartWorks.FormRuntime.TextInputBuilder.build({
			mode : mode, // view or edit
			container : $target,
			entity : $entity,
			dataField : dataField,
			refreshData : refreshData,
			layoutInstance : layoutInstance
		});
		return;
	};
};