
function createEventOccurrence(config){
	var options = {
			formXml : null,
			eventId : null,
			eventName : null,
			eventLevel : null
	};
	
	SmartWorks.extend(options, config);
	
	var formXml = options.formXml;
	var dataFields = new Array();
	dataFields.push(SmartWorks.FormRuntime.TextInputBuilder.dataField({
		fieldName: '이벤트아이디',
		formXml: formXml,
		value: options.eventId								
	}));
	dataFields.push(SmartWorks.FormRuntime.TextInputBuilder.dataField({
		fieldName: '이벤트명',
		formXml: formXml,
		value: options.eventName								
	}));
	dataFields.push(SmartWorks.FormRuntime.ComboBoxBuilder.dataField({
		fieldName: '이벤트등급',
		formXml: formXml,
		value: options.eventLevel								
	}));

	return {dataFields: dataFields};
}
