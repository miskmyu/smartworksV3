$.fn.serializeObject = function()
{
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

$('.js_new').live('click', function(e) {
	var input = $(e.target);
	var url = input.attr('href');
	var objId = '';
	var targetDiv = $('#detailScheduleDiv');
	$.ajax({
		url : url,
		data : {
			scheduleObjId: objId
		},
		success : function(data, status, jqXHR) {
			targetDiv.html(data).slideDown(500);
		},
		error : function(xhr, ajaxOptions, thrownError){
		}
	});
	
	return false;
});
$('.js_cancel').live('click', function() {
	$('#detailScheduleDiv').hide();
});
$('.js_delete').live('click', function() {
	var paramsJson = {};
	var form = $('#detailScheduleForm');
	//paramsJson[form.attr('name')] = form.serializeObject();
	
	$.ajax({
		url : "saveSchedule.jsp?method=delete&" + form.serialize(),
		//contentType : 'application/json',
		type : 'POST',
		//data : JSON.stringify(paramsJson),
		//data : paramsJson,
		success : function(formData, status, jqXHR) {
	        document.location.reload();
		},
		error : function(e) {
			return;
		}
	});	
	
	
});
$('.js_save').live('click', function() {
	var paramsJson = {};
	var form = $('#detailScheduleForm');
	//paramsJson[form.attr('name')] = form.serializeObject();
	
	$.ajax({
		url : "saveSchedule.jsp?method=save&" + form.serialize(),
		//contentType : 'application/json',
		type : 'POST',
		//data : JSON.stringify(paramsJson),
		//data : paramsJson,
		success : function(formData, status, jqXHR) {
	        document.location.reload();
		},
		error : function(e) {
			return;
		}
	});	
});
$('.js_run').live('click', function(e) {
	var input = $(e.target);
	var objId = input.attr('objId');
	
	$.ajax({
		url : "saveSchedule.jsp?method=run&objId=" + objId,
		type : 'POST',
		success : function(formData, status, jqXHR) {
	        document.location.reload();
		},
		error : function(e) {
			return;
		}
	});	
});
$('.js_stop').live('click', function(e) {
	var input = $(e.target);
	var objId = input.attr('objId');
	
	$.ajax({
		url : "saveSchedule.jsp?method=stop&objId=" + objId,
		type : 'POST',
		success : function(formData, status, jqXHR) {
	        document.location.reload();
		},
		error : function(e) {
			return;
		}
	});	
});

$('.js_select_list').live('click', function(e) {
	var input = $(e.target);
	var url = input.attr('href');
	var objId = input.attr('objId');
	var targetDiv = $('#detailScheduleDiv');
	$.ajax({
		url : url,
		data : {
			scheduleObjId: objId
		},
		success : function(data, status, jqXHR) {
			targetDiv.html(data).slideDown(500);
		},
		error : function(xhr, ajaxOptions, thrownError){
		}
	});
	
	return false;
});




