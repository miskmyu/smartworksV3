
onOpenEffect = function(dialog){
	dialog.overlay.fadeIn('slow', function () {
		dialog.container.slideDown('slow', function () {
			dialog.data.fadeIn('slow');
		});
	});	
};

onCloseEffect = function(dialog){
	dialog.data.fadeOut('slow', function () {
		dialog.container.slideUp('slow', function () {
			dialog.overlay.fadeOut('slow', function () {
				$.modal.close(); // must call this!
			});
		});
	});
};

showInfoOptions = {
	opacity: 10,
	overlayCss: {backgroundColor:"#000"},
	containerCss:{
		backgroundColor:"#fff",
		borderColor:"#000",
		color: "#000",
		height:200,
		padding:1,
		width:500
	},
	overlayClose: false
};

smartPop = {
		
	INFO : 'Info',
	WARN : 'Warn',
	ERROR: 'Error',
	CONFIRM: 'Confirm',

	overlay : function(target){
		if(isEmpty($(target))) target = $(document.body);
		var curtain = $('<span id="sw_overlay_span" class="op10" style="position:absolute; top:0; left:0; width:' + $(document).width() + 'px; height:' + $(document).height() + 'px; z-index:10000; display:block; background-color:#000;"></span>');
		curtain.appendTo($(target));
	},
	
	overlayDark : function(target){
		if(isEmpty($(target))) target = $(document.body);
		var curtain = $('<span id="sw_overlay_span" class="op10" style="position:absolute; top:0; left:0; width:' + $(document).width() + 'px; height:' + $(document).height() + 'px; z-index:10000; display:block; background-color:#000;"></span>');
		curtain.appendTo($(target));
	},
	
	closeOverlay : function(){
		$("#sw_overlay_span").remove();
	},
	
	closeUserInfo : function(){
		smartPop.closeOverlay();
		$('#sw_pop_user_info').hide();		
	},

	showUserInfo : function(input, top, left, directionUp){
		var userId = input.attr("userId");
		var longName = input.attr("longName");
		var minPicture = input.attr("minPicture");
		var profile = input.attr("profile");
		var userDetail = input.attr("userDetail");		
		var popUserInfo = $('#sw_pop_user_info');
		var startChatStr = (companyGeneral.useChattingService==='true') ? '<span class="pop_icon_chat"><a href="" class="js_start_chat_with_user" userId="' + userId + '" longName="' + longName + '" minPicture="' + minPicture + '" title="' + smartMessage.get("startChatText") + '"></a></span>' : "";
		if(!isEmpty(popUserInfo)){
			popUserInfo.show();
		}else{
			$('<div id="sw_pop_user_info" style="z-index:10001; position:absolute; left:' + left + 'px; top:' + top + 'px;">' +
					'<div class="up_point" style="left: 10px; top: 1px; position: relative;display:none;"></div>' + 
					'<div class="pop_corner_all smart_userinfo_section">' + 
						'<div class="pop_contents">' + 
							'<img src="' + profile + '" class="profile_size_b fl">' + 
							' <div class="js_user_information userinfo_area"></div>' +
						'</div>' +
						'<div class="smartp_btn_space">' +
							'<div class="fr">' +
								'<span class="pop_icon_mail"><a href="" class="js_send_mail_to_user" userId="' + userId + '" title="' + smartMessage.get("sendMailText") + '"></a></span>' +
								startChatStr + 
							'</div>' +
						'</div>' +
					'</div>' +
					'<div class="up_point_b" style="left: 10px; position: relative;display:none;"></div>' +
			'</div>').appendTo($(document.body));

			popUserInfo = $('#sw_pop_user_info');
			$('#sw_pop_user_info .js_send_mail_to_user').die('click');
			$('#sw_pop_user_info .js_send_mail_to_user').live('click', function(e){
				var input = $(targetElement(e));
				var receiverId = input.attr('userId');
				smartPop.progressCenter();
				$.get('new_mail.sw?receiverId=' + receiverId, function(data){
					$('#content').html(data);
					smartPop.closeProgress();
				});
				popUserInfo.hide();
				return false;
			});
		}
		popUserInfo.find('img').attr('src', profile);
		popUserInfo.find('.js_user_information').html(userDetail);
		popUserInfo.find('.js_send_mail_to_user').attr('userId', userId);
		popUserInfo.find('.js_start_chat_with_user').attr('userId', userId).attr('longName', longName).attr('minPicture', minPicture);
		if(directionUp){
			popUserInfo.find('.up_point_b').hide();
			popUserInfo.find('.up_point').show();
		}else{
			popUserInfo.find('.up_point').hide();
			popUserInfo.find('.up_point_b').show();
		}
		if(!directionUp)
			top = top - popUserInfo.height();
		popUserInfo.css({"top": top + "px", "left": left + "px"});

	},
	
	closeFilesDetail : function(){
		smartPop.closeOverlay();
		$('#sw_pop_files_detail').hide();		
	},

	showFilesDetail : function(input, top, left){
		var filesDetail = input.attr("filesDetail");
		var popFilesDetail = $('#sw_pop_files_detail');
		if(!isEmpty(popFilesDetail)){
			popFilesDetail.show();
		}else{
			$('<div id="sw_pop_files_detail" style="z-index:10001; position:absolute; left:' + left + 'px; top:' + top + 'px;">' +
					'<div class="pop_corner_all pop_contents js_files_detail">' + filesDetail +
					'</div>' +
			'</div>').appendTo($(document.body));
		}
		popFilesDetail.find('.js_files_detail').html(filesDetail);
		popFilesDetail.css({"top": top + "px", "left": left + "px"});

	},
	
	closeInfo : function(){
		smartPop.closeOverlay();
		$('#sw_pop_show_info').remove();		
	},

	showInfo : function(infoType, message, onClose){
		if(infoType !== smartPop.INFO && infoType !== smartPop.WARN && infoType !== smartPop.ERROR) infoType = smartPop.INFO;
		smartPop.overlayDark();
		$('<div id="sw_pop_show_info" style="z-index:10001; position:absolute;" class="pop_corner_all smart_pop_section">' + 
					'<div class="pop_contents">' + 
						'<div class="icon_pop_' + infoType + '">' + smartMessage.get('popType'+infoType) + '</div>' +
					 	'<div class="pop_notice_section">' + message + '</div>' +
					 '</div>' +
					 '<div class="glo_btn_space">' +
					 	'<div class="fr">' +
					 		'<span class="btn_gray"> <a class="js_btn_close" href=""> <span class="txt_btn_start"></span>' +
					 			'<span class="txt_btn_center">' + smartMessage.get('buttonClose') + '</span> <span class="txt_btn_end"></span>' +
					 		'</a> </span>' +
					 	'</div>' +
					 '</div>' +
				  '</div>').appendTo($(document.body)).center();
		$('#sw_pop_show_info .js_btn_close').die('click');
		$('#sw_pop_show_info .js_btn_close').live('click', function(){
			if ($.isFunction(onClose)) {
				onClose.apply();
			}
			smartPop.closeInfo();
			return false;
		});

		$('#sw_pop_show_info .js_btn_close').focus();
		$('#sw_pop_show_info').keypress(function (e) {
			var e = window.event || e;
			var keyCode = e.which || e.keyCode;
	        if(keyCode == $.ui.keyCode.ENTER) {
	            $('#sw_pop_show_info .js_btn_close').click();
	            return false;
	        } else {
	            return true;
	        }
	    });
	},
	
	closeConfirm : function(){
		smartPop.closeOverlay();
		$('#sw_pop_confirm').remove();
	},
	
	confirm : function(message, onOk, onCancel){
		smartPop.overlayDark();
		$('<div id="sw_pop_confirm" class="pop_corner_all smart_pop_section" style="z-index:10001; position:absolute;">' + 
					'<div class="pop_contents">' + 
						'<div class="icon_pop_Confirm">' + smartMessage.get('popTypeConfirm') + '</div>' +
					 	'<div class="pop_notice_section mt10">' + message + '</div>' +
					 '</div>' +
					 '<div class="glo_btn_space">' +
					 	'<div class="fr">' +
					 		'<span class="btn_gray"> <a class="js_btn_ok" href=""> <span class="txt_btn_start"></span>' +
					 			'<span class="txt_btn_center">' + smartMessage.get('buttonConfirm') + '</span> <span class="txt_btn_end"></span>' +
					 		'</a> </span>' + 
				 			'<span class="btn_gray ml5"> <a class="js_btn_cancel" href=""> <span class="txt_btn_start"></span>' +
				 				'<span class="txt_btn_center">'  + smartMessage.get('buttonCancel') + '</span> <span class="txt_btn_end"></span>' +
				 			'</a> </span>' +
				 		'</div>' +
					 '</div>' +
				  '</div>').appendTo($(document.body)).center(); 

		$('#sw_pop_confirm .js_btn_ok').die('click');
		$('#sw_pop_confirm .js_btn_cancel').die('click');
		$('#sw_pop_confirm .js_btn_ok').live('click', function(){
			if ($.isFunction(onOk)) {
				onOk.apply();
			}
			smartPop.closeConfirm();
			return false;
		});
		
		$('#sw_pop_confirm .js_btn_cancel').live('click', function(){
			if ($.isFunction(onCancel)) {
				onCancel.apply();
			}
			smartPop.closeConfirm();
			return false;
		});
		$('#sw_pop_confirm .js_btn_ok').focus();
		$('#sw_pop_confirm').keypress(function (e) {
			var e = window.event || e;
			var keyCode = e.which || e.keyCode;
	        if (keyCode == $.ui.keyCode.ENTER) {
	            $('#sw_pop_confirm .js_btn_ok').click();
	            return false;
	        } else {
	            return true;
	        }
	    });
	},

	progressTarget : "",
	progressCenter : function(){
		$('<img class="js_progress_icon" src="images/load_wh.gif"/>').appendTo($(document));
		//if(!$.browser.msie)
			smartPop.overlay();
	},
	
	progressCont : function(target){
		smartPop.progressTarget= target;
		$('<img class="js_progress_icon" src="images/load_wh.gif"/>').appendTo(target);
		//if(!$.browser.msie)
			smartPop.overlay();
	},
	progressContGray : function(target){
		smartPop.progressTarget= target;
		$('<img class="js_progress_icon" src="images/load_wh_02.gif" align="bottom"/>').appendTo(target);
		//if(!$.browser.msie)
			smartPop.overlay();
	},
	progressNav : function(target){
		smartPop.progressTarget= target;
		$('<img class="js_progress_icon" src="images/load_gr.gif" align="bottom"/>').appendTo(target);
		//if(!$.browser.msie)
			smartPop.overlay();
	},
	progressNavGray : function(target){
		smartPop.progressTarget= target;
		$('<img class="js_progress_icon" src="images/load_gr_02.gif" align="bottom"/>').appendTo(target);
		//if(!$.browser.msie)
			smartPop.overlay();
	},

	closeProgress : function(){
		smartPop.closeOverlay();
		if(!isEmpty(smartPop.progressTarget))
			smartPop.progressTarget.find('.js_progress_icon').remove();
	},
	
	close : function(){
		$.modal.close();
	},
	
	selectUser : function(communityItems, target, width, isMultiUsers, courseId, friendOnly, bottomUp){
		target.html('');
		var conWidth = (!isEmpty(width) && width>360) ? width : 360;
		var url = (!isEmpty(courseId)) ? "pop_select_course_member.sw?multiUsers="+isMultiUsers + "&courseId=" + courseId 
					: (!isEmpty(friendOnly) && friendOnly) ? "pop_select_friend.sw?multiUsers=" + isMultiUsers 
					: "pop_select_user.sw?multiUsers="+isMultiUsers;
		
		var containerCss = (bottomUp) ? {width: conWidth, bottom: 0} : {width: conWidth}; 
		$.get(url, function(data){
			$(data).modal({
				appendTo: target,
				opacity: 0,
				autoPosition: false,
				fixed: false,
				overlayCss: {backgroundColor:"#000"},
				containerCss: containerCss,
				overlayClose: true,
				onShow: function(dialog){

					var selectionProc = function(comId, comName){
						var userField = target.parents('.js_type_userField:first');
						var inputTarget = userField.find('input.js_auto_complete:first');
						if(inputTarget.parents('.sw_required').hasClass('sw_error')){
							inputTarget.parents('.sw_required').removeClass('sw_error');
							$('form.js_validation_required').validate({ showErrors: showErrors}).form();
						}

						var isSameId = false;
						if (isEmpty(communityItems) || (!isEmpty(userField) && userField.attr('multiUsers') !== 'true')){
							communityItems.remove();
						}else{
							for(var i=0; i<communityItems.length; i++){
								var oldComId = $(communityItems[i]).attr('comId');
								if(oldComId !=null && oldComId === comId){
									isSameId = true;
									break;
								}
							}
						}
						if(!isSameId){
							$("<span class='js_community_item user_select' comId='" + comId + "' comName='" + comName + "'>" + comName
									+ "<a class='js_remove_community' href=''>&nbsp;x</a></span>").insertBefore(inputTarget);

							var searchFilter = target.parents('.js_search_filter_page');
							if(!isEmpty(searchFilter)){
								userField.find('input[name="txtFilterStringOperand"]').attr('value', comId);
							}
						}
						inputTarget.focus();
						userField.find('.js_community_names').change();
					};
					
					$('a.js_pop_select_user').die('click');
					$('a.js_pop_select_users').die('click');
					if(isEmpty(isMultiUsers) || isMultiUsers!== 'true'){
						$('a.js_pop_select_user').live('click', function(e){
							
							var input = $(targetElement(e));
							if(!input.hasClass('js_pop_select_user')) input = input.parent();
							var comId = input.attr('userId');
							var comName = input.text();
							if(!isEmpty(communityItems) && communityItems.hasClass('js_selected_approver_info')){
								var userName = input.attr('userName');
								var userPosition = input.attr('userPosition') || "";
								var userPicture = input.attr('userPicture');
								var approverInfo = '<div class="noti_pic">' +
														'<img class="profile_size_s" src="' + userPicture + '" title="' + userPosition + ' ' + userName + '">' +
													'</div>' +
													'<div class="noti_in">' +
														'<div class="t_name">' + userPosition + '</div>' +
														'<div class="t_name">' + userName + '</div>' +
													'</div>';
								communityItems.html(approverInfo);
								communityItems.nextAll('span').hide();
								communityItems.nextAll('input').attr('value', comId);
							}else{
								selectionProc(comId, comName);
							}
							smartPop.close();
							target.html('');
							return false;
						});
						$('a.js_pop_select_user').focus();
						$('a.js_pop_select_user').keypress(function (e) {
							var e = window.event || e;
							var keyCode = e.which || e.keyCode;
					        if (keyCode == $.ui.keyCode.ENTER) {
					            $('a.js_pop_select_user').click();
					            return false;
					        } else {
					            return true;
					        }
					    });
					}else{
						$('a.js_pop_select_users').live('click', function(e){
							var selections = $('form[name="frmUserSelections"]').find('input.js_checkbox:checked');
							if(isEmpty(selections)) return false;
							
							for(var i=0; i<selections.length; i++){
								var selection = $(selections[i]);
								var comId = selection.attr('value');
								var comName = selection.attr("comName");
								selectionProc(comId, comName);
							}
							smartPop.close();
							target.html('');
							return false;
						});
						$('a.js_pop_select_users').focus();
						$('a.js_pop_select_users').keypress(function (e) {
							var e = window.event || e;
							var keyCode = e.which || e.keyCode;
					        if (keyCode == $.ui.keyCode.ENTER) {
					            $('a.js_pop_select_users').click();
					            return false;
					        } else {
					            return true;
					        }
					    });
					}
				}
			});
		});
	},

	selectEmailAddress : function(communityItems, target, width, isMultiUsers, bottomUp){
		target.html('');
		var conWidth = (!isEmpty(width) && width>360) ? width : 360;
		var url = "pop_select_email.sw?multiUsers="+isMultiUsers; 
		var containerCss = (bottomUp) ? {width: conWidth, bottom: 0} : {width: conWidth}; 
		$.get(url, function(data){
			$(data).modal({
				appendTo: target,
				opacity: 0,
				autoPosition: false,
				fixed: false,
				overlayCss: {backgroundColor:"#000"},
				containerCss: containerCss,
				overlayClose: true,
				onShow: function(dialog){

					var selectionProc = function(comId, comName){
						var userField = target.parents('.js_type_userField:first');
						var inputTarget = userField.find('input.js_auto_complete:first');
						if(inputTarget.parents('.sw_required').hasClass('sw_error')){
							inputTarget.parents('.sw_required').removeClass('sw_error');
							$('form.js_validation_required').validate({ showErrors: showErrors}).form();
						}

						var isSameId = false;
						if (isEmpty(communityItems) || (!isEmpty(userField) && userField.attr('multiUsers') !== 'true')){
							communityItems.remove();
						}else{
							for(var i=0; i<communityItems.length; i++){
								var oldComId = $(communityItems[i]).attr('comId');
								if(oldComId !=null && oldComId === comId){
									isSameId = true;
									break;
								}
							}
						}
						if(!isSameId){
							var comNameLong = comName;
							if(inputTarget.attr('href') === "email_address.sw"){
								if(!isEmailAddress(inputTarget.attr('value')) && isEmailAddress(comId) && !isEmailAddress(comName)){
									comNameLong = comName + "&lt;" + comId + "&gt;";
								}
							}
							$("<span class='js_community_item user_select' comId='" + comId + "' comName='" + comName + "'>" + comNameLong
									+ "<a class='js_remove_community' href=''>&nbsp;x</a></span>").insertBefore(inputTarget);
						}
						inputTarget.focus();
						userField.find('.js_community_names').change();
					};
					
					$('a.js_pop_select_user').die('click');
					$('a.js_pop_select_users').die('click');
					if(isEmpty(isMultiUsers) || isMultiUsers!== 'true'){
						$('a.js_pop_select_user').live('click', function(e){
							var input = $(targetElement(e));
							var comId = input.attr('userId');
							var comName = input.text();
							selectionProc(comId, comName);
							smartPop.close();
							target.html('');
							return false;
						});
						$('a.js_pop_select_user').focus();
						$('a.js_pop_select_user').keypress(function (e) {
							var e = window.event || e;
							var keyCode = e.which || e.keyCode;
					        if (keyCode == $.ui.keyCode.ENTER) {
					            $('a.js_pop_select_user').click();
					            return false;
					        } else {
					            return true;
					        }
					    });
					}else{
						$('a.js_pop_select_users').live('click', function(e){
							var input = $(targetElement(e));
							var selections = input.parents('.pop_list_area').find('input.js_checkbox:checked');
							if(isEmpty(selections)) return false;
							
							for(var i=0; i<selections.length; i++){
								var selection = $(selections[i]);
								var comId = selection.attr('value');
								var comName = selection.attr("comName");
								selectionProc(comId, comName);
							}
							smartPop.close();
							target.html('');
							return false;
						});
						$('a.js_pop_select_users').focus();
						$('a.js_pop_select_users').keypress(function (e) {
							var e = window.event || e;
							var keyCode = e.which || e.keyCode;
					        if (keyCode == $.ui.keyCode.ENTER) {
					            $('a.js_pop_select_users').click();
					            return false;
					        } else {
					            return true;
					        }
					    });
					}
				}
			});
		});
	},

	selectCommunity : function(communityItems, target, width, isMultiUsers, bottomUp){
		target.html('');
		var conWidth = (!isEmpty(width) && width>360) ? width : 360;
		var url = "pop_select_community.sw?multiUsers="+isMultiUsers; 
		var containerCss = (bottomUp) ? {width: conWidth, bottom: 0} : {width: conWidth}; 
		$.get(url, function(data){
			$(data).modal({
				appendTo: target,
				opacity: 0,
				autoPosition: false,
				fixed: false,
				overlayCss: {backgroundColor:"#000"},
				containerCss: containerCss,
				overlayClose: true,
				onShow: function(dialog){

					var selectionProc = function(comId, comName){
						var userField = target.parents('.js_type_userField:first');
						var inputTarget = userField.find('input.js_auto_complete:first');
						if(inputTarget.parents('.sw_required').hasClass('sw_error')){
							inputTarget.parents('.sw_required').removeClass('sw_error');
							$('form.js_validation_required').validate({ showErrors: showErrors}).form();
						}

						var isSameId = false;
						if (isEmpty(communityItems) || (!isEmpty(userField) && userField.attr('multiUsers') !== 'true')){
							communityItems.remove();
						}else{
							for(var i=0; i<communityItems.length; i++){
								var oldComId = $(communityItems[i]).attr('comId');
								if(oldComId !=null && oldComId === comId){
									isSameId = true;
									break;
								}
							}
						}
						if(!isSameId){
							var comNameLong = comName;
							$("<span class='js_community_item user_select' comId='" + comId + "' comName='" + comName + "'>" + comNameLong
									+ "<a class='js_remove_community' href=''>&nbsp;x</a></span>").insertBefore(inputTarget);
						}
						inputTarget.focus();
						userField.find('.js_community_names').change();
					};
					
					$('a.js_pop_select_user').die('click');
					$('a.js_pop_select_users').die('click');
					$('.js_pop_select_community').die('click');
					if(isEmpty(isMultiUsers) || isMultiUsers!== 'true'){
						$('.js_pop_select_community').live('click', function(e){
							var input = $(targetElement(e));
							var comId = input.attr('comId');
							var comName = input.text();
							selectionProc(comId, comName);
							if(!isEmpty(input.parents('td[fieldId="txtFromCommunity"]'))){
								var workTransfer = input.parents('.js_work_transfer_page');
								workTransfer.attr('fromCommunityId', comId);
								workTransfer.find('tr').hide();
								workTransfer.find('input.js_click_transfer_all').click();
								input.parents('tr:first').show();
								if(isUserId(comId)){
									workTransfer.attr('comType', 'user');
									workTransfer.find('tr.js_transfer_user').show();
								}else if(isDepartmentId(comId)){
									workTransfer.attr('comType', 'department');
									workTransfer.find('tr.js_transfer_depart').show();				
								}else{
									workTransfer.attr('comType', 'group');
									workTransfer.find('tr.js_transfer_group').show();
									
								}
							}
							smartPop.close();
							target.html('');
							return false;
						});
						$('a.js_pop_select_user').live('click', function(e){
							var input = $(targetElement(e));
							var comId = input.attr('userId');
							var comName = input.text();
							selectionProc(comId, comName);
							if(!isEmpty(input.parents('td[fieldId="txtFromCommunity"]'))){
								var workTransfer = input.parents('.js_work_transfer_page');
								workTransfer.attr('fromCommunityId', comId);
								workTransfer.find('tr').hide();
								workTransfer.find('input.js_click_transfer_all').click();
								input.parents('tr:first').show();
								if(isUserId(comId)){
									workTransfer.attr('comType', 'user');
									workTransfer.find('tr.js_transfer_user').show();
								}else if(isDepartmentId(comId)){
									workTransfer.attr('comType', 'department');
									workTransfer.find('tr.js_transfer_depart').show();				
								}else{
									workTransfer.attr('comType', 'group');
									workTransfer.find('tr.js_transfer_group').show();
									
								}
							}
							smartPop.close();
							target.html('');
							return false;
						});
						$('a.js_pop_select_user').focus();
						$('a.js_pop_select_user').keypress(function (e) {
							var e = window.event || e;
							var keyCode = e.which || e.keyCode;
					        if (keyCode == $.ui.keyCode.ENTER) {
					            $('a.js_pop_select_user').click();
					            return false;
					        } else {
					            return true;
					        }
					    });
					}else{
						$('a.js_pop_select_users').live('click', function(e){
							var input = $(targetElement(e));
							var selections = input.parents('.pop_list_area').find('input.js_checkbox:checked');
							if(isEmpty(selections)) return false;
							
							for(var i=0; i<selections.length; i++){
								var selection = $(selections[i]);
								var comId = selection.attr('value');
								var comName = selection.attr("comName");
								selectionProc(comId, comName);
							}
							smartPop.close();
							target.html('');
							return false;
						});
						$('a.js_pop_select_users').focus();
						$('a.js_pop_select_users').keypress(function (e) {
							var e = window.event || e;
							var keyCode = e.which || e.keyCode;
					        if (keyCode == $.ui.keyCode.ENTER) {
					            $('a.js_pop_select_users').click();
					            return false;
					        } else {
					            return true;
					        }
					    });
					}
				}
			});
		});
	},

	selectDepartment : function(communityItems, target, width, bottomUp){
		target.html('');
		var conWidth = (!isEmpty(width) && width>360) ? width : 360;
		var url = "pop_select_depart.sw"; 
		var containerCss = (bottomUp) ? {width: conWidth, bottom: 0} : {width: conWidth}; 
		$.get(url, function(data){
			$(data).modal({
				appendTo: target,
				opacity: 0,
				autoPosition: false,
				fixed: false,
				overlayCss: {backgroundColor:"#000"},
				containerCss: containerCss,
				overlayClose: true,
				onShow: function(dialog){

					var selectionProc = function(comId, comName){
						var departField = target.parents('.js_type_departmentField:first');
						var inputTarget = departField.find('input.js_auto_complete:first');

						communityItems.remove();
						$("<span class='js_community_item user_select' comId='" + comId + "' comName='" + comName + "' title='" + comName + "'>" + getDepartNameFromFullpath(comName)
								+ "<a class='js_remove_community' href=''>&nbsp;x</a></span>").insertBefore(inputTarget);
						inputTarget.focus();
						departField.find('.js_community_names').change();
					};
					
					$('a.js_pop_select_depart').die('click');
					$('a.js_pop_select_depart').live('click', function(e){
						var input = $(targetElement(e));
						if(!input.hasClass('js_pop_select_depart')) input = input.parents().find('.js_pop_select_depart');
						var comId = input.attr('departId');
						var comName = input.attr('departName');
						selectionProc(comId, comName);
						smartPop.close();
						target.html('');
						return false;
					});
					$('a.js_pop_select_depart').focus();
					$('a.js_pop_select_depart').keypress(function (e) {
						var e = window.event || e;
						var keyCode = e.which || e.keyCode;
				        if (keyCode == $.ui.keyCode.ENTER) {
				            $('a.js_pop_select_depart').click();
				            return false;
				        } else {
				            return true;
				        }
				    });
				}
			});
		});
	},

	selectWork : function(target, width){
		target.html('');
		var conWidth = (!isEmpty(width) && width>360) ? width : 360;
		$.get("pop_select_work.sw", function(data){
			$(data).modal({
				appendTo: target,
				opacity: 0,
				autoPosition: false,
				fixed: false,
				overlayCss: {backgroundColor:"#000"},
				containerCss:{
					width:conWidth
				},
				overlayClose: true,
				onShow: function(dialog){
					var isAllTargetWork = target.hasClass('js_all_target_work_popup');
					if(isAllTargetWork){
						target.find('ul').prepend('<li><span class="dep"><a href="" class="js_pop_select_work" workId="allSmartWorks" workType="-1" fullpathName="' + smartMessage.get("companyAllWorks") + 
														'" iconClass="icon_depart"><span class="icon_depart"></span>' + smartMessage.get("companyAllWorks") + '</a></span></li>');
					}
					$('.js_pop_select_work').die('click');
					$('.js_pop_select_work').live( 'click', function(e){
						var input = $(targetElement(e)).parents('li:first').find('a');
						if(!isAllTargetWork){
							$('#form_works').html('').hide();
							$('#upload_work_list').hide().parents(".js_start_work_page").hide();
							var href = input.attr('href');
							smartPop.progressCenter();
							$.get(href,  function(data){
								$('#form_works').html(data);
								var formContent = $('#form_works').find('div.js_form_content');
								var workId = input.attr('workId');
								new SmartWorks.GridLayout({
									target : formContent,
									mode : "edit",
									workId : workId,
									onSuccess : function(){
										$('#form_works').show().parent().show();
										smartPop.close();
										target.html('');
										smartPop.closeProgress();
									},
									onError : function(){
										smartPop.close();
										target.html('');											
										smartPop.closeProgress();
									}
								});
							});
						}else{
							var reportList = target.parents(".js_report_list_page");
							var targetWorkId = input.attr('workId');
							var targetWorkName = input.attr('fullpathName');
							var targetWorkIcon = input.attr('iconClass');
							var targetWorkType = input.attr('workType');
							reportList.attr('targetWorkId', targetWorkId); 
							reportList.attr('targetWorkName', targetWorkName); 
							reportList.attr('targetWorkIcon', targetWorkIcon); 
							reportList.attr('targetWorkType', targetWorkType); 
							reportList.attr('producedBy', 'smartworks');
							reportList.find('form[name="frmSearchInstance"]').hide();
							reportList.find('.js_target_work_info > span:first').attr('class', input.attr('iconClass'));
							reportList.find('.js_target_work_info > span:eq(1)').html(input.attr('fullpathName'));
							reportList.find('.js_work_report_close').click();
							smartPop.close();
							target.html('');
							smartPop.progressCenter();
							$.get('get_user_report_count.sw?targetWorkId=' + targetWorkId,  function(data){
								reportList.find('.js_view_report_list:eq(0)').removeClass('disabled');
								reportList.find('.js_view_report_list:eq(1)').addClass('disabled');
								reportList.find('.js_user_report_count').html('[' + data + ']');
							});
							$.get('report_instance_list.sw?targetWorkId=' + targetWorkId + '&targetWorkType=' + targetWorkType + '&producedBy=smartworks',  function(data){
								reportList.find('#report_instance_list_page').html(data);
								smartPop.closeProgress();
							});
						}
						return false;
					});
				}
			});
		});
	},

	selectWorkItem : function(formId, target){
		if(isEmpty(formId) || isEmpty(target)) return;
		$.get("pop_select_work_item.sw", {formId: formId}, function(data){
			$(data).modal({
				opacity: 10,
				overlayCss: {backgroundColor:"#000"},
				containerCss:{
					height:500,
					width:960
				},
				autoResize : true,
				overlayClose: false,
				onShow: function(dialog){
					$('.js_pop_select_work_item').die('click');
					$('.js_pop_select_work_item').live( 'click', function(e){
						var input = $(targetElement(e));
						var recordId = input.parents('tr:first').attr('instId');
						var fieldId = target.attr('refFormField');
						//Start. jy.Bae 기존 소스는 keyField가 기존 필드들을 건너뛰고 있는 것만 숫자를 세어서, 빌더설정한 것과 값이 틀려 fieldId로 비교하게 함.
						//pop_iwork_instance_list.jsp의  td의 필드아이디와 빌더설정한 필드아이디가 같은걸 반환함.
						var value = input.parents('tr:first').find('td[fieldId="' + fieldId +'"]').text();
						//End.  2012.08.15
						target.attr('refRecordId', recordId);
						var inputTarget = target.find('input');
						inputTarget.attr('value', value);
						if(inputTarget.hasClass('sw_required') && inputTarget.hasClass('sw_error')){
							inputTarget.removeClass('sw_error');
							$('form.js_validation_required').validate({ showErrors: showErrors}).form();
						}
						target.change();
						smartPop.close();
						return false;
					});
				}
			});
		}).error( function(){
			smartPop.showInfo(smartPop.WARN, smartMessage.get("notRefFormWorkingError"));			
		});
	},
	
	retireMember : function(userId){
		if(isEmpty(userId)) return;
		$.get("pop_retire_member.sw", {userId: userId}, function(data){
			$(data).modal({
				opacity: 10,
				overlayCss: {backgroundColor:"#000"},
				containerCss:{
					height:600,
					width:740
				},
				autoResize : true,
				overlayClose: false,
				onShow: function(dialog){
					$('.js_close_retire_member').die('click');
					$('.js_close_retire_member').live( 'click', function(e){
						smartPop.close();
						return false;
					});
				}
			});
		});
	},
	
	abolishDepartment : function(departId){
		if(isEmpty(departId)) return;
		$.get("pop_abolish_department.sw", {departId: departId}, function(data){
			$(data).modal({
				opacity: 10,
				overlayCss: {backgroundColor:"#000"},
				containerCss:{
					height:600,
					width:740
				},
				autoResize : true,
				overlayClose: false,
				onShow: function(dialog){
					$('.js_close_abolish_department').die('click');
					$('.js_close_abolish_department').live( 'click', function(e){
						smartPop.close();
						return false;
					});
				}
			});
		});
	},
	
	selectApprovalLine : function(){
		$.get("pop_select_approval_line.sw", {}, function(data){
			$(data).modal({
				opacity: 10,
				overlayCss: {backgroundColor:"#000"},
				containerCss:{
					width:600
				},
				autoResize : true,
				overlayClose: false,
				onShow: function(dialog){
					$('.js_pop_select_approval_line').die('click');
					$('.js_pop_select_approval_line').live( 'click', function(e){
						var input = $(targetElement(e)).parents('.js_pop_select_approval_line');
						var appendTaskApproval = $('.js_append_task_approval_page');
						var url = 'approval_line_box.sw?approvalLineId=' + input.attr('approvalLineId');
						$.get(url,  function(data){
							appendTaskApproval.find('.js_approval_line_box').html(data);
							smartPop.close();
						});
						return false;
					});
				}
			});
		});
	},
	
	createGroup : function(){
		$.get("pop_new_group.sw", function(data){
			$(data).modal({
				opacity: 10,
				overlayCss: {backgroundColor:"#000"},
				containerCss:{
					height:500,
					width:600
				},
				overlayClose: false,
				onShow: function(dialog){
					loadGroupProfileField();
					loadNewGroupFields();
					$('.js_close_new_group').die('click');
					$('.js_close_new_group').live( 'click', function(e){
						smartPop.close();
						return false;
					});
					$('.js_close_new_group').focus();
					$('.js_close_new_group').keypress(function (e) {
						var e = window.event || e;
						var keyCode = e.which || e.keyCode;
				        if (keyCode == $.ui.keyCode.ENTER) {
				            $('.js_close_new_group').click();
				            return false;
				        } else {
				            return true;
				        }
				    });
				}
			});
		});
	},

	inviteGroupMembers : function(groupId){
		$.get("pop_invite_group_members.sw?groupId=" + groupId, function(data){
			$(data).modal({
				opacity: 10,
				overlayCss: {backgroundColor:"#000"},
				containerCss:{
					height:500,
					width:600
				},
				overlayClose: false,
				onShow: function(dialog){
					$('.js_close_invite_member').die('click');
					$('.js_close_invite_member').live( 'click', function(e){
						smartPop.close();
						return false;
					});
					$('.js_close_invite_member').focus();
					$('.js_close_invite_member').keypress(function (e) {
						var e = window.event || e;
						var keyCode = e.which || e.keyCode;
				        if (keyCode == $.ui.keyCode.ENTER) {
				            $('.js_close_invite_member').click();
				            return false;
				        } else {
				            return true;
				        }
				    });
				}
			});
		});
	},

	createWorkCategory : function(categoryId, categoryName, categoryDesc){
		$.get("pop_new_category.sw?categoryId="+ categoryId + "&categoryName=" + categoryName + "&categoryDesc=" + categoryDesc, function(data){
			$(data).modal({
				opacity: 10,
				overlayCss: {backgroundColor:"#000"},
				containerCss:{
					height:200,
					width:460
				},
				overlayClose: false,
				onShow: function(dialog){
					$('.js_close_new_category').die('click');
					$('.js_close_new_category').live( 'click', function(e){
						smartPop.close();
						return false;
					});
					$('input[name="txtCategoryName"]').focus();
					$('input[name="txtCategoryName"]').keypress(function (e) {
						var e = window.event || e;
						var keyCode = e.which || e.keyCode;
				        if (keyCode == $.ui.keyCode.ENTER) {
				            return false;
				        }
				    });
					/*$('.js_close_new_category').focus();
					$('.js_close_new_category').keypress(function (e) {
						var e = window.event || e;
						var keyCode = e.which || e.keyCode;
				        if (keyCode == $.ui.keyCode.ENTER) {
				            $('.js_close_new_category').click();
				            return false;
				        } else {
				            return true;
				        }
				    });*/
				}
			});
		});
	},

	createWorkDefinition : function(parentId, parentName, workId, workName, workTypeName, workDesc, categoryId, groupId, groupOption){
		var url = "pop_new_work_definition.sw?parentId="+parentId+"&parentName="+parentName + "&workId=" + workId 
						+ "&workName=" + workName + "&workTypeName=" + workTypeName + "&workDesc=" + workDesc + "&categoryId=" + categoryId + "&groupId=" + groupId + "&groupOption=" + groupOption;
		$.get( url, { contentType : "charset=utf-8"}, function(data){
			$(data).modal({
				opacity: 10,
				overlayCss: {backgroundColor:"#000"},
				containerCss:{
					height:300,
					width:560
				},
				overlayClose: false,
				onShow: function(dialog){
					$('.js_close_new_work').die('click');
					$('.js_close_new_work').live( 'click', function(e){
						smartPop.close();
						return false;
					});
					$('.js_close_new_work').focus();
					$('.js_close_new_work').keypress(function (e) {
						var e = window.event || e;
						var keyCode = e.which || e.keyCode;
				        if (keyCode == $.ui.keyCode.ENTER) {
				            $('.js_close_new_work').click();
				            return false;
				        } else {
				            return true;
				        }
				    });
				}
			});
		});
	},

	moveWorkDefinition : function(type, workId, workFullName, categoryId, groupId, workName, workDesc){
		var url = "pop_move_work_definition.sw?workId=" + workId + "&workName=" + workName + "&type=" + type + "&workFullName=" + workFullName + "&categoryId=" + categoryId + "&groupId=" + groupId + "&workDesc=" + workDesc;
		$.get( url, function(data){
			$(data).modal({
				opacity: 10,
				overlayCss: {backgroundColor:"#000"},
				containerCss:{
					height:300,
					width:560
				},
				overlayClose: false,
				onShow: function(dialog){
					$('.js_close_move_work').die('click');
					$('.js_close_move_work').live( 'click', function(e){
						smartPop.close();
						return false;
					});
					$('.js_close_move_work').focus();
					$('.js_close_move_work').keypress(function (e) {
						var e = window.event || e;
						var keyCode = e.which || e.keyCode;
				        if (keyCode == $.ui.keyCode.ENTER) {
				            $('.js_close_move_work').click();
				            return false;
				        } else {
				            return true;
				        }
				    });
				}
			});
		});
	},

	downloadFromAppstore : function(){
		$.get("pop_download_from_appstore.sw", {}, function(data){
			$(data).modal({
				opacity: 10,
				overlayCss: {backgroundColor:"#000"},
				containerCss:{
					height:500,
					width:800
				},
				overlayClose: false,
				onShow: function(dialog){
					$('.js_pop_select_work_item').die('click');
					$('.js_pop_select_work_item').live( 'click', function(e){
						var input = $(targetElement(e));
						var recordId = input.parents('tr:first').attr('instId');
						var fieldId = target.attr('refFormField');
						/*var keyField = input.parents('tbody').find('tr.js_instance_list_header').find('th[fieldId="'+fieldId+'"]');
						var keyPos = keyField.prevAll('th').length;*/
						var value = input.parents('tr:first').find('td[fieldId="' + fieldId +'"]').text();
						target.attr('refRecordId', recordId);
						var inputTarget = target.find('input');
						inputTarget.attr('value', value);
						if(inputTarget.hasClass('sw_required') && inputTarget.hasClass('sw_error')){
							inputTarget.removeClass('sw_error');
							$('form.js_validation_required').validate({ showErrors: showErrors}).form();
						}
						smartPop.close();
						return false;
					});
				}
			});
		});
	},	

	createMailFolder : function(folderId, folderName, folderDesc, parentId, parentName, folderType){
		parentId = (isEmpty(parentId)) ? null : parentId;
		parentName = (isEmpty(parentName)) ? "" : parentName;
		folderType = (isEmpty(folderType)) ? "" : folderType;
		$.get("pop_new_mail_folder.sw?parentId=" + parentId + "&parentName="+ parentName + "&folderType=" + folderType + "&folderId="+ folderId + "&folderName=" + folderName + "&folderDesc=" + folderDesc, function(data){
			$(data).modal({
				opacity: 10,
				overlayCss: {backgroundColor:"#000"},
				containerCss:{
					height:200,
					width:460
				},
				overlayClose: false,
				onShow: function(dialog){
					$('.js_close_new_mail_folder').die('click');
					$('.js_close_new_mail_folder').live( 'click', function(e){
						smartPop.close();
						return false;
					});
					$('.js_close_new_mail_folder').focus();
					$('.js_close_new_mail_folder').keypress(function (e) {
						var e = window.event || e;
						var keyCode = e.which || e.keyCode;
				        if (keyCode == $.ui.keyCode.ENTER) {
				            $('.js_close_new_mail_folder').click();
				            return false;
				        } else {
				            return true;
				        }
				    });
				}
			});
		});
	},

	createFileFolder : function(workSpaceId, parentId, folderId, folderName){
		$.get("pop_new_file_folder.sw?workSpaceId=" + workSpaceId + "&parentId="+ parentId + "&folderId=" + folderId + "&folderName=" + folderName, function(data){
			$(data).modal({
				opacity: 10,
				overlayCss: {backgroundColor:"#000"},
				containerCss:{
					height:200,
					width:460
				},
				overlayClose: false,
				onShow: function(dialog){
					$('.js_close_new_folder').die('click');
					$('.js_close_new_folder').live( 'click', function(e){
						smartPop.close();
						return false;
					});
					$('input[name="txtFolderName"]').focus();
					$('input[name="txtFolderName"]').keypress(function (e) {
						var e = window.event || e;
						var keyCode = e.which || e.keyCode;
				        if (keyCode == $.ui.keyCode.ENTER) {
				            return false;
				        }
				    });
				}
			});
		});
	},

	createImageFolder : function(workSpaceId, parentId, folderId, folderName){
		$.get("pop_new_image_folder.sw?workSpaceId=" + workSpaceId + "&parentId="+ parentId + "&folderId=" + folderId + "&folderName=" + folderName, function(data){
			$(data).modal({
				opacity: 10,
				overlayCss: {backgroundColor:"#000"},
				containerCss:{
					height:200,
					width:460
				},
				overlayClose: false,
				onShow: function(dialog){
					$('.js_close_new_folder').die('click');
					$('.js_close_new_folder').live( 'click', function(e){
						smartPop.close();
						return false;
					});
					$('input[name="txtFolderName"]').focus();
					$('input[name="txtFolderName"]').keypress(function (e) {
						var e = window.event || e;
						var keyCode = e.which || e.keyCode;
				        if (keyCode == $.ui.keyCode.ENTER) {
				            return false;
				        }
				    });
				}
			});
		});
	},

	printContent : function(header, contents){
		$.get("pop_print_content.sw", function(data){
			var width = 860;
			var left = (($(window).width() - width) / 2) + $(window).scrollLeft();
			$(data).modal({
				opacity: 10,
				overlayCss: {backgroundColor:"#000"},
				autoPosition: false,
				containerCss:{
					top: 30,
					left: left,
					width: width
				},
				overlayClose: false,
				onShow: function(dialog){
					$('iframe[name="printFrame"]').contents().find('head').html(header);
					$('iframe[name="printFrame"]').contents().find('body').html(contents);
					$('.js_close_print_content').die('click');
					$('.js_close_print_content').live( 'click', function(e){
						smartPop.close();
						return false;
					});
					$('.js_do_print_content').die('click');
					$('.js_do_print_content').live( 'click', function(e){
						frames["printFrame"].focus();
						frames["printFrame"].print();
						return false;
					});
					doIframeAutoHeight();
				}
			});
		});
	},

	showPicture : function(instanceId){
		$.get("pop_show_picture.sw?instId=" + instanceId, function(data){
			var width = 620;
			var left = (($(window).width() - width) / 2) + $(window).scrollLeft();
			$(data).modal({
				opacity: 10,
				overlayCss: {backgroundColor:"#000"},
				autoPosition: false,
				containerCss:{
					top: 30,
					left: left,
					width: width
				},
				overlayClose: true,
				onShow: function(dialog){
				}
			});
		});
	},

	showInstance : function(instanceId, taskInstId, workId, formId, forwardId){
		$.get("pop_show_instance.sw?instId=" + instanceId + "&taskInstId=" + taskInstId + "&workId=" + workId + "&formId=" + formId + "&forwardId=" + forwardId, function(data){
			var width = 800;
			var left = (($(window).width() - width) / 2) + $(window).scrollLeft();
			$(data).modal({
				opacity: 10,
				overlayCss: {backgroundColor:"#000"},
				autoPosition: false,
				containerCss:{
					top: 30,
					left: left,
					width: width
				},
				overlayClose: true,
				onShow: function(dialog){
				}
			});
		});
	},

	reassignPerformer : function(workId, instanceId, taskInstId){
		$.get("pop_reassign_performer.sw?workId=" + workId + "&instanceId="+ instanceId + "&taskInstId=" + taskInstId, function(data){
			$(data).modal({
				opacity: 10,
				overlayCss: {backgroundColor:"#000"},
				containerCss:{
					height:200,
					width:460
				},
				overlayClose: false,
				onShow: function(dialog){
					$('.js_close_reassign_performer').die('click');
					$('.js_close_reassign_performer').live( 'click', function(e){
						smartPop.close();
						return false;
					});
				}
			});
		});
	},

	changeMailPassword : function(mailServerId, emailId, userName, oldPassword){
		$.get("pop_change_mail_password.sw?mailServerId=" + mailServerId + "&emailId="+ emailId + "&userName=" + userName + "&oldPassword="+ oldPassword, function(data){
			$(data).modal({
				opacity: 10,
				overlayCss: {backgroundColor:"#000"},
				containerCss:{
					height:200,
					width:460
				},
				overlayClose: false,
				onShow: function(dialog){
					$('.js_close_change_mail_password').die('click');
					$('.js_close_change_mail_password').live( 'click', function(e){
						smartPop.close();
						return false;
					});
					$('.js_close_change_mail_password').focus();
					$('.js_close_change_mail_password').keypress(function (e) {
						var e = window.event || e;
						var keyCode = e.which || e.keyCode;
				        if (keyCode == $.ui.keyCode.ENTER) {
				            $('.js_close_change_mail_password').click();
				            return false;
				        } else {
				            return true;
				        }
				    });
				}
			});
		});
	},
	
	eventAlarmIsRunning : false,
	
	eventAlarm : function(notice){
		if(isEmpty(notice) || isEmpty(notice.event)) return;
		if(smartPop.eventAlarmIsRunning){
			setTimeout(function(){
				smartPop.eventAlarm(notice);
			},500, [notice]);
			return;
		}
		var event = notice.event;
		var workSpaceStr = (isEmpty(event.workSpaceId) || event.workSpaceId===currentUser.userId) ? "" : '<span class="arr">▶</span><span class="space_name">' + event.workSpaceName+ '</span>';
		var noticeData =  '<li class="sub_instance_list">' +
							'<div class="det_title">' +
								'<div class="noti_pic"><img src="' + event.owner.midPicture + '" class="profile_size_m"></div>' +
								'<div class="noti_in_m"><span class="t_name">' + event.owner.longName +  '</span>' + workSpaceStr + '</div>' +
								'<span class="t_date vb pl10 fr">' + notice.issuedDate + '</span>' +
								'<span>' + 
									'<a href="' + event.controller + '?cid=' + event.contextId + '&wid=' + event.workSpaceId + '&workId=' + event.workId + '">' +
										'<div>' + 
											'<span class="icon_event_works"></span>' +
											'<div>' + event.subject + '</div>' +
										'</div>' +
										'<div>' + event.startTitle + ' : ' + event.start +
											'<a href="" noticeId="' + notice.id + '" noticeType="' + notice.type + '"><span class="btn_x js_remove_notice" ></span></a>' +								
										'</div>' +
									'</a>' +
								'</span>' +
							'</div>' +
						'</li>';
		
		var popEventAlarm = $('.js_pop_event_alarm_page');
		if(isEmpty(popEventAlarm)){
			smartPop.eventAlarmIsRunning = true;
			$.get("pop_event_alarm.sw", function(data){
				$(data).modal({
					opacity: 10,
					overlayCss: {backgroundColor:"#000"},
					containerCss:{
						height:200,
						width:460
					},
					overlayClose: false,
					onShow: function(dialog){

						popEventAlarm = $('.js_pop_event_alarm_page');
						popEventAlarm.find('ul').append(noticeData);
						smartPop.eventAlarmIsRunning = false;
						$('.js_close_event_alarm').die('click');
						$('.js_close_event_alarm').live( 'click', function(e){
							smartPop.close();
							return false;
						});
						$('.js_close_event_alarm').focus();
						$('.js_close_event_alarm').keypress(function (e) {
							var e = window.event || e;
							var keyCode = e.which || e.keyCode;
					        if (keyCode == $.ui.keyCode.ENTER) {
					            $('.js_close_event_alarm').click();
					            return false;
					        } else {
					            return true;
					        }
					    });
					}
				});
			});
		}else{
			popEventAlarm.find('ul').append(noticeData);
		}
	},
	
	createReportPane : function(paneId, paneName, targetWorkId, targetWorkName, targetWorkIcon, reportId, reportName, reportType, config){
		var options = {
				chartType: null,
				isChartView: true,
				isStacked: false,
				showLegend: true,
				stringLabelRotation: 'auto',			
				paneColumnSpans: 1,
				panePosition: null
		};
		SmartWorks.extend(options, config);

		var url = "pop_new_report_pane.sw?paneId=" + paneId + "&paneName=" + paneName + "&targetWorkId=" + targetWorkId + "&targetWorkName=" + targetWorkName + "&targetWorkIcon=" + targetWorkIcon + "&reportId=" + reportId + "&reportName=" + reportName + "&reportType=" + reportType
						+ "&chartType=" + options.chartType + "&isChartView=" + options.isChartView + "&isStacked=" + options.isStacked + "&showLegend=" + options.showLegend
						+ "&stringLabelRotation=" + options.stringLabelRotation + "&paneColumnSpans=" + options.paneColumnSpans + "&panePosition=" + options.panePosition;
		$.get( url, { contentType : "charset=utf-8"}, function(data){
		$(data).modal({
			opacity: 10,
			overlayCss: {backgroundColor:"#000"},
			containerCss:{
				height:300,
				width:560
			},
			overlayClose: false,
			onShow: function(dialog){
				$('.js_close_new_report_pane').die('click');
				$('.js_close_new_report_pane').live( 'click', function(e){
					smartPop.close();
					return false;
				});
				$('.js_close_new_report_pane').focus();
				$('.js_close_new_report_pane').keypress(function (e) {
					var e = window.event || e;
					var keyCode = e.which || e.keyCode;
			        if (keyCode == $.ui.keyCode.ENTER) {
			            $('.js_close_new_report_pane').click();
			            return false;
			        } else {
			            return true;
			        }
			    });
			}
			});
		});
	}
	
};
