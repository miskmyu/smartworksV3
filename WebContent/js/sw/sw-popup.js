
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
								'<span class="pop_icon_chat"><a href="" class="js_start_chat_with_user" userId="' + userId + '" longName="' + longName + '" minPicture="' + minPicture + '" title="' + smartMessage.get("startChatText") + '"></a></span>' +
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
			$('#sw_pop_user_info .js_start_chat_with_user').die('click');
			$('#sw_pop_user_info .js_start_chat_with_user').live('click', function(e){
				var input = $(targetElement(e));
				var chatterId = input.attr('userId');
				var chatterName = input.attr('longName');
				var chatterPicture = input.attr('minPicture');
				smartTalk.chattingRequest(new Array({
					userId : currentUserId,
					longName : currentUser.longName,
					minPicture : currentUser.minPicture
				}, {
					userId : chatterId,
					longName : chatterName,
					minPicture : chatterPicture
				}));
				setTimeout(function(){
					setRightPosition("resize", null);
				}, 600);
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
		if(!$.browser.msie)
			smartPop.overlay();
	},
	
	progressCont : function(target){
		smartPop.progressTarget= target;
		$('<img class="js_progress_icon" src="images/load_wh.gif"/>').appendTo(target);
		if(!$.browser.msie)
			smartPop.overlay();
	},
	progressContGray : function(target){
		smartPop.progressTarget= target;
		$('<img class="js_progress_icon" src="images/load_wh_02.gif" align="bottom"/>').appendTo(target);
		if(!$.browser.msie)
			smartPop.overlay();
	},
	progressNav : function(target){
		smartPop.progressTarget= target;
		$('<img class="js_progress_icon" src="images/load_gr.gif" align="bottom"/>').appendTo(target);
		if(!$.browser.msie)
			smartPop.overlay();
	},
	progressNavGray : function(target){
		smartPop.progressTarget= target;
		$('<img class="js_progress_icon" src="images/load_gr_02.gif" align="bottom"/>').appendTo(target);
		if(!$.browser.msie)
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
		var conWidth = (!isEmpty(width) && width>0) ? width : 360;
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

						if (isEmpty(communityItems) || (!isEmpty(userField) && userField.attr('multiUsers') !== 'true'))
							communityItems.remove();
						var isSameId = false;
						for(var i=0; i<communityItems.length; i++){
							var oldComId = $(communityItems[i]).attr('comId');
							if(oldComId !=null && oldComId === comId){
								isSameId = true;
								break;
							}
						}
						if(!isSameId){
							$("<span class='js_community_item user_select' comId='" + comId+ "'>" + comName
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
		var conWidth = (!isEmpty(width) && width>0) ? width : 360;
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

						if (isEmpty(communityItems) || (!isEmpty(userField) && userField.attr('multiUsers') !== 'true'))
							communityItems.remove();
						var isSameId = false;
						for(var i=0; i<communityItems.length; i++){
							var oldComId = $(communityItems[i]).attr('comId');
							if(oldComId !=null && oldComId === comId){
								isSameId = true;
								break;
							}
						}
						if(!isSameId){
							$("<span class='js_community_item user_select' comId='" + comId+ "'>" + comName
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

	selectWork : function(target, width){
		target.html('');
		var conWidth = (!isEmpty(width) && width>0) ? width : 360;
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
					$('.js_pop_select_work').die('click');
					$('.js_pop_select_work').live( 'click', function(e){
						var input = $(targetElement(e)).parents('li:first').find('a');
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
					width:800
				},
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
	
	selectApprovalLine : function(){
		$.get("pop_select_approval_line.sw", {}, function(data){
			$(data).modal({
				opacity: 10,
				overlayCss: {backgroundColor:"#000"},
				containerCss:{
					width:600
				},
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

	createWorkDefinition : function(parentId, parentName, workId, workName, workTypeName, workDesc, categoryId, groupId){
		var url = "pop_new_work_definition.sw?parentId="+parentId+"&parentName="+parentName + "&workId=" + workId 
						+ "&workName=" + workName + "&workTypeName=" + workTypeName + "&workDesc=" + workDesc + "&categoryId=" + categoryId + "&groupId=" + groupId;
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

	downloadFromAppstore : function(formId, target){
		if(isEmpty(formId) || isEmpty(target)) return;
		$.get("pop_download_from_appstore.sw", {formId: formId}, function(data){
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

	createMailFolder : function(folderId, folderName, folderDesc, parentId, parentName){
		parentId = (isEmpty(parentId)) ? null : parentId;
		parentName = (isEmpty(parentName)) ? "" : parentName;
		$.get("pop_new_mail_folder.sw?parentId=" + parentId + "&parentName="+ parentName + "&folderId="+ folderId + "&folderName=" + folderName + "&folderDesc=" + folderDesc, function(data){
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
	}

};
