$(function() {
		
	$('a.js_download_mail_attachment').live('click', function(e){
		var input = $(targetElement(e));
		var attachmentIframe = input.parents('.js_mail_space_page').find('#attachmentIframe');
		var partId = input.attr('partId');
		attachmentIframe.attr('src', 'webmail/dumpPart.service?partid=' + partId + '&dl=true');
		return false;
		
	});
	
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
		smartPop.progressCenter();
		$.ajax({
			url : "move_mails.sw",
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				smartPop.showInfo(smartPop.INFO, smartMessage.get("moveMailSucceed"), function(){
					smartPop.progressCenter();
					document.location.href = "smart.sw#" + mailSpace.attr('lastHref');
				});
			},
			error : function(e) {
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.ERROR, smartMessage.get("moveMailError"));
			}
		});
		return false;
	});
	
	$('.js_select_move_folder').live('change', function(e) {
		var input = $(targetElement(e));
		var targetId = input.find('option:selected').attr('value');
		if(isEmpty(targetId)) return;
		var mailList = input.parents('.js_mail_list_page');
		var mailSpace = input.parents('.js_mail_space_page');
		var sourceId = "";
		var msgIds = new Array();
		if(!isEmpty(mailList)){
			sourceId = mailList.attr('folderId');
			var mails = mailList.find('input[name="chkSelectMail"]:checked');
			if(isEmpty(mails)){
				smartPop.showInfo(smartPop.WARN, smartMessage.get("noSelectedMails"), function(){});
				input.find('option:first').attr('selected', 'selected').siblings('selected', '');
				return false;
			}
			for(var i=0; i<mails.length; i++)
				msgIds.push($(mails[i]).attr('value'));
		}else if(!isEmpty(mailSpace)){
			sourceId = mailSpace.attr('folderId');
			msgId = mailSpace.attr('msgId');
			msgIds.push(msgId);			
		}
		var paramsJson = {};
		paramsJson['ids'] = msgIds;
		paramsJson['source'] = sourceId;
		paramsJson['target'] = targetId;
		console.log(JSON.stringify(paramsJson));
		smartPop.confirm(smartMessage.get("moveMailConfirm"), function(){
			smartPop.progressCenter();
			$.ajax({
				url : "move_mails.sw",
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					smartPop.showInfo(smartPop.INFO, smartMessage.get("moveMailSucceed"), function(){
						smartPop.progressCenter();
						input.find('option:first').attr('selected', 'selected').siblings('selected', '');
						document.location.href = "smart.sw#" + mailList.attr('currentHref');
					});
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					input.find('option:first').attr('selected', 'selected').siblings('selected', '');
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("moveMailError"));
				}
			});
		});
	});

	$('.js_move_mails_btn').live('click', function(e) {
		var input = $(targetElement(e));
		var mailList = input.parents('.js_mail_list_page');
		var sourceId = mailList.attr('folderId');
		var targetId = input.attr('targetId');
		var mails = mailList.find('input[name="chkSelectMail"]:checked');
		if(isEmpty(mails)){
			smartPop.showInfo(smartPop.WARN, smartMessage.get("noSelectedMails"), function(){});
			return false;
		}
		var paramsJson = {};
		var msgIds = new Array();
		for(var i=0; i<mails.length; i++)
			msgIds.push($(mails[i]).attr('value'));
		paramsJson['ids'] = msgIds;
		paramsJson['source'] = sourceId;
		paramsJson['target'] = targetId;
		console.log(JSON.stringify(paramsJson));
		smartPop.confirm(smartMessage.get("moveMailConfirm"), function(){
			smartPop.progressCenter();
			$.ajax({
				url : "move_mails.sw",
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					smartPop.showInfo(smartPop.INFO, smartMessage.get("moveMailSucceed"), function(){
						smartPop.progressCenter();
						document.location.href = "smart.sw#" + mailList.attr('currentHref');
					});
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("moveMailError"));
				}
			});
		});
		return false;
	});

	$('.js_delete_mail_btn').live('click', function(e) {
		var input = $(targetElement(e));
		var mailSpace = input.parents('.js_mail_space_page');
		var folderId = mailSpace.attr('folderId');
		var msgId = mailSpace.attr('msgId');
		var paramsJson = {};
		var msgIds = new Array();
		msgIds.push(msgId);
		paramsJson['ids'] = msgIds;
		paramsJson['folderId'] = folderId;
		console.log(JSON.stringify(paramsJson));
		smartPop.confirm(smartMessage.get("deletMailConfirm"), function(){
			smartPop.progressCenter();
			$.ajax({
				url : "delete_mails.sw",
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					smartPop.showInfo(smartPop.INFO, smartMessage.get("deleteMailSucceed"), function(){
						smartPop.progressCenter();
						document.location.href = "smart.sw#" + mailSpace.attr('lastHref');
					});
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("deleteMailError"));
				}
			});
		});
		return false;
	});

	$('.js_delete_mails_btn').live('click', function(e) {
		var input = $(targetElement(e));
		var mailList = input.parents('.js_mail_list_page');
		var folderId = mailList.attr('folderId');
		var mails = mailList.find('input[name="chkSelectMail"]:checked');
		if(isEmpty(mails)){
			smartPop.showInfo(smartPop.WARN, smartMessage.get("noSelectedMails"), function(){});
			return false;
		}
		var paramsJson = {};
		var msgIds = new Array();
		for(var i=0; i<mails.length; i++)
			msgIds.push($(mails[i]).attr('value'));
		paramsJson['ids'] = msgIds;
		paramsJson['folderId'] = folderId;
		console.log(JSON.stringify(paramsJson));
		smartPop.confirm(smartMessage.get("deletMailConfirm"), function(){
			smartPop.progressCenter();
			$.ajax({
				url : "delete_mails.sw",
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					smartPop.showInfo(smartPop.INFO, smartMessage.get("deleteMailSucceed"), function(){
						smartPop.progressCenter();
						document.location.href = "smart.sw#" + mailList.attr('currentHref');
					});
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("deleteMailError"));
				}
			});
		});
		return false;
	});

	var SEND_TYPE_REPLY		= 1;
	var SEND_TYPE_REPLY_ALL	= 2;
	var SEND_TYPE_FORWARD	= 3;

	$('.js_reply_mail_btn').live('click', function(e) {
		var input = $(targetElement(e));
		var mailList = input.parents('.js_mail_list_page');
		var folderId = mailList.attr('folderId');
		var mails = mailList.find('input[name="chkSelectMail"]:checked');
		if(isEmpty(mails) || mails.length != 1){
			smartPop.showInfo(smartPop.WARN, smartMessage.get("noSelectedMails"), function(){});
			return false;
		}
			
		var msgId =	$(mails[0]).attr('value');
		smartPop.progressCenter();
		$.ajax({
			url : "new_mail.sw",
			data : {
				folderId: folderId,
				msgId: msgId,
				sendType: SEND_TYPE_REPLY
			},
			success : function(data, status, jqXHR) {
				$('#content').html(data);
				smartPop.closeProgress();
			},
			error : function(e) {
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.ERROR, smartMessage.get("replyMailError"));
			}
		});
		return false;
	});

	$('.js_reply_all_mail_btn').live('click', function(e) {
		var input = $(targetElement(e));
		var mailList = input.parents('.js_mail_list_page');
		var folderId = mailList.attr('folderId');
		var mails = mailList.find('input[name="chkSelectMail"]:checked');
		if(isEmpty(mails) || mails.length != 1){
			smartPop.showInfo(smartPop.WARN, smartMessage.get("noSelectedMails"), function(){});
			return false;
		}
			
		var msgId =	$(mails[0]).attr('value');
		smartPop.progressCenter();
		$.ajax({
			url : "new_mail.sw",
			data : {
				folderId: folderId,
				msgId: msgId,
				sendType: SEND_TYPE_REPLY_ALL
			},
			success : function(data, status, jqXHR) {
				$('#content').html(data);
				smartPop.closeProgress();
			},
			error : function(e) {
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.ERROR, smartMessage.get("replyMailError"));
			}
		});
		return false;
	});

	$('.js_forward_mail_btn').live('click', function(e) {
		var input = $(targetElement(e));
		var mailList = input.parents('.js_mail_list_page');
		var folderId = mailList.attr('folderId');
		var mails = mailList.find('input[name="chkSelectMail"]:checked');
		if(isEmpty(mails) || mails.length != 1){
			smartPop.showInfo(smartPop.WARN, smartMessage.get("noSelectedMails"), function(){});
			return false;
		}

		var msgId =	$(mails[0]).attr('value');
		smartPop.progressCenter();
		$.ajax({
			url : "new_mail.sw",
			data : {
				folderId: folderId,
				msgId: msgId,
				sendType: SEND_TYPE_FORWARD
			},
			success : function(data, status, jqXHR) {
				$('#content').html(data);
				smartPop.closeProgress();
			},
			error : function(e) {
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.ERROR, smartMessage.get("replyMailError"));
			}
		});
		return false;
	});

	$('a.js_new_mail_folder_btn').live('click', function(e){
		smartPop.createMailFolder(null, null, null);
		return false;
		
	});

	$('.js_remove_mail_folder_btn').live('click', function(e) {
		var input = $(targetElement(e));
		var folderId = input.attr('folderId');
		var paramsJson = {};
		paramsJson['folderId'] = folderId;
		console.log(JSON.stringify(paramsJson));
		smartPop.confirm(smartMessage.get("removeMailFolderConfirm"), function(){
			smartPop.progressCenter();
			$.ajax({
				url : "delete_mail_folder.sw",
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
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("removeMailFolderError"));
				}
			});
		});
		return false;
	});
	
	$('.js_text_mail_folder_btn').live('click', function(e){
		var input = $(targetElement(e));
		var folderId = input.attr('folderId');
		var folderName = input.attr('folderName');
		var folderDesc = input.attr('folderDesc');
		smartPop.createMailFolder(folderId, folderName, folderDesc);
		return false;
		
	});
	
	$('.js_fetch_unread_mails_btn').live('click', function(e){
		var allMailFolders = $(targetElement(e)).parents('.js_mail_folders').find('.js_all_mail_folders');
		smartPop.progressCenter();
		$.ajax({
			url : "my_all_mail_folders.sw",
			data : {},
			success : function(data, status, jqXHR) {
				allMailFolders.html(data);
				smartPop.closeProgress();
			},
			error : function(e) {
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.ERROR, smartMessage.get("replyMailError"));
			}
		});
		return false;
	});
	
	$('.js_toggle_select_all').live('click', function(e){
		var input = $(targetElement(e));
		var checkValue = input.is(':checked');
		var checkBoxes = input.parents('.js_mail_list_page').find('input[name="chkSelectMail"]');
		checkBoxes.attr('checked', checkValue);
		return true;
	});

});
