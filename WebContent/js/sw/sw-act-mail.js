$(function() {
		
	$('.js_send_mail_btn').live('click', function(e) {
		submitForms("send");
		return false;
	});

	$('.js_save_mail_btn').live('click', function(e) {
		submitForms("save");
		return false;
	});

	$('.js_move_mail_btn').live('click', function(e) {
		var input = $(targetElement(e));
		var mailSpace = input.parents('.js_mail_space_page');
		var sourceId = mailSpace.attr('folderId');
		var msgId = mailSpace.attr('msgId');
		var targetId = input.attr('targetId');
		var paramsJson = {};
		var msgIds = new Array();
		msgIds.push(msgId);
		paramsJson['ids'] = msgIds;
		paramsJson['source'] = sourceId;
		paramsJson['target'] = targetId;
		console.log(JSON.stringify(paramsJson));
		// 서비스요청 프로그래스바를 나타나게 한다....
//		var progressSpan = newMail.find('.js_progress_span');
		smartPop.progressCenter();
		$.ajax({
			url : "move_mails.sw",
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				// 성공시에 프로그래스바를 제거하고 성공메시지를 보여준다...
				smartPop.closeProgress();
			},
			error : function(e) {
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.ERROR, smartMessage.get("sendMailError"));
			}
		});
		return false;
	});

	$('.js_move_mails_btn').live('click', function(e) {
		var input = $(targetElement(e));
		var mailList = input.parents('.js_mail_list_page');
		var sourceId = mailList.attr('folderId');
		var targetId = input.attr('targetId');
		var mails = mailList.find('input[name="chkSelectMail"]:checked');
		if(isEmpty(mails)) return false;
		var paramsJson = {};
		var msgIds = new Array();
		for(var i=0; i<mails.length; i++)
			msgIds.push($(mails[i]).attr('value'));
		paramsJson['ids'] = msgIds;
		paramsJson['source'] = sourceId;
		paramsJson['target'] = targetId;
		console.log(JSON.stringify(paramsJson));
		// 서비스요청 프로그래스바를 나타나게 한다....
//		var progressSpan = newMail.find('.js_progress_span');
		smartPop.progressCenter();
		$.ajax({
			url : "move_mails.sw",
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				// 성공시에 프로그래스바를 제거하고 성공메시지를 보여준다...
				smartPop.closeProgress();
			},
			error : function(e) {
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.ERROR, smartMessage.get("sendMailError"));
			}
		});
		return false;
	});

});
