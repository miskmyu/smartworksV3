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

	$('.js_cancel_mail_btn').live('click', function(e) {
		var input = $(targetElement(e));
		var newMail = input.parents('.js_new_mail_page');
		var lastHref = newMail.attr('lastHref');
		if(isEmpty(lastHref) || lastHref.indexOf('iwork_list.sw')!=-1 || lastHref.indexOf('pwork_list.sw')!=-1 )
			document.location.href = "home.sw";
		else
			document.location.href = lastHref;
		return false;
	});

	$('.js_move_mail_btn').live('click', function(e) {
		var input = $(targetElement(e));
		var addJunk = input.hasClass('js_add_junk_btn');
		var removeJunk = input.hasClass('js_remove_junk_btn');
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
				if(addJunk){
					paramsJson = {};
					var senderIds = new Array();
					senderIds.push(mailSpace.attr('senderId'));
					paramsJson['senderIds'] = senderIds;
					console.log(JSON.stringify(paramsJson));
					$.ajax({
						url : "add_junk.sw",
						contentType : 'application/json',
						type : 'POST',
						data : JSON.stringify(paramsJson),
						success : function(data, status, jqXHR) {},
						error : function(e) {}
					});
				}else if(removeJunk){
					paramsJson = {};
					var senderIds = new Array();
					senderIds.push(mailSpace.attr('senderId'));
					paramsJson['senderIds'] = senderIds;
					console.log(JSON.stringify(paramsJson));
					$.ajax({
						url : "remove_junk.sw",
						contentType : 'application/json',
						type : 'POST',
						data : JSON.stringify(paramsJson),
						success : function(data, status, jqXHR) {},
						error: function(e){}
					});
				}
				smartPop.closeProgress();
				var lastHref = mailSpace.attr('lastHref');
				if(isEmpty(lastHref))
					window.location.reload(true);
				else
					document.location.href = lastHref;
			},
			error : function(e) {
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.showInfo(smartPop.ERROR, smartMessage.get("moveMailError"));
				smartPop.closeProgress();
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
				smartPop.showInfo(smartPop.WARN, smartMessage.get("moveItemsNotSelected"), function(){});
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
		smartPop.confirm(smartMessage.get("moveConfirmation"), function(){
			smartPop.progressCenter();
			$.ajax({
				url : "move_mails.sw",
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					smartPop.closeProgress();
					input.find('option:first').attr('selected', 'selected').siblings('selected', '');
					var currentHref = mailList.attr('currentHref');
					if(isEmpty(currentHref))
						window.location.reload(true);
					else
						document.location.href = currentHref; 
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					input.find('option:first').attr('selected', 'selected').siblings('selected', '');
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("moveMailError"));
					smartPop.closeProgress();
				}
			});
		});
	});

	$('.js_move_mails_btn').live('click', function(e) {
		var input = $(targetElement(e));
		var addJunk = input.hasClass('js_add_junk_btn');
		var removeJunk = input.hasClass('js_remove_junk_btn');
		var mailList = input.parents('.js_mail_list_page');
		var sourceId = mailList.attr('folderId');
		var targetId = input.attr('targetId');
		var mails = mailList.find('input[name="chkSelectMail"]:checked');
		if(isEmpty(mails)){
			smartPop.showInfo(smartPop.WARN, smartMessage.get("moveItemsNotSelected"), function(){});
			return false;
		}
		var paramsJson = {};
		var msgIds = new Array();
		var senderIds = new Array();
		for(var i=0; i<mails.length; i++){
			msgIds.push($(mails[i]).attr('value'));
			senderIds.push($(mails[i]).attr('senderId'));
		}
		paramsJson['ids'] = msgIds;
		paramsJson['source'] = sourceId;
		paramsJson['target'] = targetId;
		console.log(JSON.stringify(paramsJson));
		smartPop.confirm(smartMessage.get("moveConfirmation"), function(){
			smartPop.progressCenter();
			$.ajax({
				url : "move_mails.sw",
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					if(addJunk){
						paramsJson = {};
						paramsJson['senderIds'] = senderIds;
						console.log(JSON.stringify(paramsJson));
						$.ajax({
							url : "add_junk.sw",
							contentType : 'application/json',
							type : 'POST',
							data : JSON.stringify(paramsJson),
							success : function(data, status, jqXHR) {},
							error: function(e){}
						});
					}else if(removeJunk){
						paramsJson = {};
						paramsJson['senderIds'] = senderIds;
						console.log(JSON.stringify(paramsJson));
						$.ajax({
							url : "remove_junk.sw",
							contentType : 'application/json',
							type : 'POST',
							data : JSON.stringify(paramsJson),
							success : function(data, status, jqXHR) {},
							error: function(e){}
						});
					}
					smartPop.closeProgress();
					var currentHref = mailList.attr('currentHref');
					if(isEmpty(currentHref))
						window.location.reload(true);
					else
						document.location.href = currentHref;
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("moveMailError"));
					smartPop.closeProgress();
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
		smartPop.confirm(smartMessage.get("removeConfirmation"), function(){
			smartPop.progressCenter();
			$.ajax({
				url : "delete_mails.sw",
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					smartPop.closeProgress();
					var lastHref = mailSpace.attr('lastHref');
					
					if(isEmpty(lastHref)){
						window.location.reload(true);
					}else{
						document.location.href = lastHref;
					}
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("removeMailError"));
					smartPop.closeProgress();
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
			smartPop.showInfo(smartPop.WARN, smartMessage.get("removeItemsNotSelected"), function(){});
			return false;
		}
		var paramsJson = {};
		var msgIds = new Array();
		for(var i=0; i<mails.length; i++)
			msgIds.push($(mails[i]).attr('value'));
		paramsJson['ids'] = msgIds;
		paramsJson['folderId'] = folderId;
		console.log(JSON.stringify(paramsJson));
		smartPop.confirm(smartMessage.get("removeConfirmation"), function(){
			smartPop.progressCenter();
			$.ajax({
				url : "delete_mails.sw",
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					smartPop.closeProgress();
					var currentHref = mailList.attr('currentHref');
					if(isEmpty(currentHref))
						window.location.reload(true);
					else
						document.location.href = currentHref; 
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("removeMailError"));
					smartPop.closeProgress();
				}
			});
		});
		return false;
	});

	$('.js_empty_trash_btn').live('click', function(e) {
		var input = $(targetElement(e));
		var mailList = input.parents('.js_mail_list_page');
		var folderId = mailList.attr('folderId');
		var paramsJson = {};
		paramsJson['folderId'] = folderId;
		paramsJson['removeAll'] = true;
		console.log(JSON.stringify(paramsJson));
		smartPop.confirm(smartMessage.get("emptyTrashConfirmation"), function(){
			smartPop.progressCenter();
			$.ajax({
				url : "delete_mails.sw",
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					smartPop.closeProgress();
					var currentHref = mailList.attr('currentHref');
					if(isEmpty(currentHref))
						window.location.reload(true);
					else
						document.location.href = currentHref; 
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("emptyTrashError"));
					smartPop.closeProgress();
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
			smartPop.showInfo(smartPop.WARN, smartMessage.get("replyItemNotSelected"), function(){});
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
				smartPop.showInfo(smartPop.ERROR, smartMessage.get("replyMailError"));
				smartPop.closeProgress();
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
			smartPop.showInfo(smartPop.WARN, smartMessage.get("replyItemNotSelected"), function(){});
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
				smartPop.showInfo(smartPop.ERROR, smartMessage.get("replyMailError"));
				smartPop.closeProgress();
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
			smartPop.showInfo(smartPop.WARN, smartMessage.get("forwardItemNotSelected"), function(){});
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
				smartPop.showInfo(smartPop.ERROR, smartMessage.get("forwardMailError"));
				smartPop.closeProgress();
			}
		});
		return false;
	});

	$('.js_new_mail_folder_btn').live('click', function(e){
		var input = $(targetElement(e));
		if(isEmpty(input.siblings('span'))){
			smartPop.createMailFolder(null, null, null, null, null);
		}else{
			var parentId = input.attr('parentId');
			var parentName = input.attr('parentName');
			smartPop.createMailFolder(null, null, null, parentId, parentName);
		}
		return false;
		
	});

	$('.js_change_mail_password_btn').live('click', function(e){
		var input = $(targetElement(e));
		var mailServerId = input.attr('mailServerId');
		var emailId = input.attr('emailId');
		var userName = input.attr('userName');
		var oldPassword = input.attr('oldPassword');
		smartPop.changeMailPassword(mailServerId, emailId, userName, oldPassword);
		return false;
		
	});

	$('.js_remove_mail_folder_btn').live('click', function(e) {
		var input = $(targetElement(e));
		var folderId = input.attr('folderId');
		var paramsJson = {};
		paramsJson['folderId'] = folderId;
		console.log(JSON.stringify(paramsJson));
		smartPop.confirm(smartMessage.get("removeMailFolderConfirmation"), function(){
			smartPop.progressCenter();
			$.ajax({
				url : "delete_mail_folder.sw",
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					// 성공시에 프로그래스바를 제거하고 성공메시지를 보여준다...
					window.location.reload(true);
					smartPop.closeProgress();
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.ERROR, smartMessage.get("removeMailFolderError"));
					smartPop.closeProgress();
				}
			});
		});
		return false;
	});
	
	$('.js_text_mail_folder_btn').live('click', function(e){
		var input = $(targetElement(e));
		var folderType = input.attr('folderType');
		var parentId = input.attr('parentId');
		var parentName = input.attr('parentName');
		var folderId = input.attr('folderId');
		var folderName = input.attr('folderName');
		var folderDesc = input.attr('folderDesc');
		smartPop.createMailFolder(folderId, folderName, folderDesc, parentId, parentName, folderType);
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
				smartPop.showInfo(smartPop.ERROR, smartMessage.get("fetchMailsError"));
				smartPop.closeProgress();
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

	$('.js_show_all_users_shown').live('click', function(e){
		var input = $(targetElement(e));
		var usersShown = input.attr('usersShown');
		usersShown = usersShown.replace(/</g, "&lt;");
		usersShown = usersShown.replace(/>/g, "&gt;");
		input.parent().html(usersShown);
		return false;
	});

	$('.js_junk_list_btn').live('click', function(e) {
		var input = $(targetElement(e));
		smartPop.progressCont(input.next('span:first'));
		var target = $('.js_junk_list_form');
		$.ajax({
			url : "junk_list.sw",
			data : {},
			success : function(data, status, jqXHR) {
				target.html(data);
				target.slideDown(500);
				input.parent('a.js_junk_list_btn').hide();
				smartPop.closeProgress();
			},
			error : function(xhr, ajaxOptions, thrownError){
				smartPop.closeProgress();
			}
		});
		return false;
	});

	$('a.js_junk_list_close').live('click', function(e) {
		var input = $(targetElement(e));
		$('.js_junk_list_form').slideUp(500).html('');
		$('a.js_junk_list_btn').show();
		return false;
	});

	$('.js_add_junk_mail').live('click', function(e) {
		var input = $(targetElement(e)).parents('td:first').find('input:first');
		if(!isEmailAddress(input.attr('value'))){
			smartPop.showInfo(smartPop.ERROR, smartMessage.get("notMailError"));			
			return false;
		}
		var paramsJson = {};
		var senderIds = new Array();
		senderIds.push(input.attr('value'));
		paramsJson['senderIds'] = senderIds;
		console.log(JSON.stringify(paramsJson));
		smartPop.progressCenter();
		$.ajax({
			url : "add_junk.sw",
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				var target = $('.js_junk_list_form');
				$.ajax({
					url : "junk_list.sw",
					data : {},
					success : function(data, status, jqXHR) {
						target.html(data);
						smartPop.closeProgress();
					},
					error : function(xhr, ajaxOptions, thrownError){
						smartPop.closeProgress();
					}
				});
			},
			error : function(e) {
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.showInfo(smartPop.ERROR, smartMessage.get("addJunkMailError"));
				smartPop.closeProgress();
			}
		});
		return false;
	});
	
	$('.js_add_junk_domain').live('click', function(e) {
		var input = $(targetElement(e)).parents('td:first').find('input:first');
		if(!isEmailAddress('a@' + input.attr('value'))){
			smartPop.showInfo(smartPop.ERROR, smartMessage.get("notDomainError"));			
			return false;
		}
		var paramsJson = {};
		var senderDomains = new Array();
		senderDomains.push(input.attr('value'));
		paramsJson['senderDomains'] = senderDomains;
		console.log(JSON.stringify(paramsJson));
		smartPop.progressCenter();
		$.ajax({
			url : "add_junk.sw",
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				var target = $('.js_junk_list_form');
				$.ajax({
					url : "junk_list.sw",
					data : {},
					success : function(data, status, jqXHR) {
						target.html(data);
						smartPop.closeProgress();
					},
					error : function(xhr, ajaxOptions, thrownError){
						smartPop.closeProgress();
					}
				});
			},
			error : function(e) {
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.showInfo(smartPop.ERROR, smartMessage.get("addJunkDomainError"));
				smartPop.closeProgress();
			}
		});
		return false;
	});
	
	$('.js_remove_junk_mail').live('click', function(e) {
		var input = $(targetElement(e)).parents('li:first');
		var paramsJson = {};
		var senderIds = new Array();
		senderIds.push(input.attr('junkId'));
		paramsJson['senderIds'] = senderIds;
		console.log(JSON.stringify(paramsJson));
		smartPop.progressCenter();
		$.ajax({
			url : "remove_junk.sw",
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				input.remove();
				smartPop.closeProgress();
			},
			error : function(e) {
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.showInfo(smartPop.ERROR, smartMessage.get("removeJunkMailError"));
				smartPop.closeProgress();
			}
		});
		return false;
	});
	
	$('.js_remove_junk_domain').live('click', function(e) {
		var input = $(targetElement(e)).parents('li:first');
		var paramsJson = {};
		var senderDomains = new Array();
		senderDomains.push(input.attr('junkId'));
		paramsJson['senderDomains'] = senderDomains;
		console.log(JSON.stringify(paramsJson));
		smartPop.progressCenter();
		$.ajax({
			url : "remove_junk.sw",
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				input.remove();
				smartPop.closeProgress();
			},
			error : function(e) {
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.showInfo(smartPop.ERROR, smartMessage.get("removeJunkDomainError"));
				smartPop.closeProgress();
			}
		});
		return false;
	});
	
});
