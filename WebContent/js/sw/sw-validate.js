
$.validator.messages.required = "";
$.validator.messages.email = "";
$.validator.messages.number = "";

$.validator.addMethod('positiveNumber', function (value) { return Number(value) > 0; }, '0보다 큰숫자를 입력바랍니다.');

var showErrors = function(errorMap, errorList) {
	if(!isEmpty(errorMap)){
		this.noOfErrors = $('form.js_validation_required').find('.sw_required.sw_error:visible').length + $('form.js_validation_required').find('.required.error:visible').length;
		if(this.noOfErrors>0){
			$(".sw_error_message:visible:first").html("입력한 내용중에 " + this.noOfErrors + "개 항목이 입력되지 않았습니다. 위의 붉은색으로 선택된 항목(들)을 입력바랍니다.");
		}else{
			$(".sw_error_message:visible:first").html("");
		}
		this.defaultShowErrors();
	}else{
		this.noOfErrors = $('form.js_validation_required').find('.sw_required.sw_error:visible').length + $('form.js_validation_required').find('.required.error:visible').length;
		if(this.noOfErrors>0){
			$(".sw_error_message:visible:first").html("입력한 내용중에 " + this.noOfErrors + "개 항목이 입력되지 않았습니다. 위의 붉은색으로 선택된 항목(들)을 입력바랍니다.");
		}else{
			$(".sw_error_message:visible:first").html("");
		}
	}
};
