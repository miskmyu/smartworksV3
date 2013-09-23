$(function() {
	/*
	 * 검색 입력창에서 사용하는 것으로, 값을 입력하고 500ms이상 정지하고 있으면 입렵박스의 href값으로 ajax를 호출하여, 가져온
	 * 값을, js_start_work class(새업무시작하기에서 업무검색하는 입력창)는 id가 upload_work_list인 곳에
	 * 보여주고, 그렇지 않으면 내부모와 같은 수준에 있는 div 영역에 보여준다.
	 */
	var requestedValue = "";
	var timeoutId = null;
	$('input.js_auto_complete').live('keyup', function(e) {
		var e = window.event || e;
		var keyCode = e.which || e.keyCode;
		if(keyCode>=9 && keyCode<=45 && keyCode!=13 && keyCode!=32 && keyCode!=186 && keyCode!=188) return;
		var input = $(targetElement(e));
		var listWidth = input.parent().outerWidth();
		var startWork = input.parents('div.js_start_work_page');
		var chatter_name = input.parents('div.js_chatter_names');
		var communityId = input.parents('ul.js_community_members').attr('communityId');
		var appendTaskApproval = input.parents('.js_append_task_approval_page');
		if(!input.hasClass('js_approver_box')) appendTaskApproval = [];
		var target, listTop, listLeft;
		if (!isEmpty(startWork)){
			target = startWork.find('#upload_work_list');
		}else if(!isEmpty(chatter_name)){
			target = chatter_name.siblings('div.js_chatter_list').addClass('searching');
			listWidth = target.width();
		}else if(!isEmpty(appendTaskApproval)){
			var approvalLineBox = appendTaskApproval.find('.js_approval_line_box');
			var inputPosition = input.position(); 
			var approvalLineBoxPosition = approvalLineBox.position(); 
			target = approvalLineBox.nextAll('.js_community_list').attr('approverIndex', input.attr('approverIndex'));
			listWidth = 300;
			listTop = inputPosition.top + input.height();
			listLeft = inputPosition.left;
			var widthGap = listWidth - (approvalLineBox.width() - (inputPosition.left - approvalLineBoxPosition.left));
			if(widthGap>0)
				listLeft = listLeft-widthGap;
		}else{
			target = input.parent().next('div');
		}
		var url = input.attr('href');
		var lastValue = input.attr('value');
		if(timeoutId != null) clearTimeout(timeoutId);
		if(keyCode==13 || keyCode==32 || keyCode==186 || keyCode==188){
			if(url !== 'email_address.sw') return false;
			var valueLength = (keyCode == $.ui.keyCode.ENTER) ? lastValue.length : lastValue.length-1;
			var emailAddress = lastValue.substr(0, valueLength);
			if(!isEmailAddress(emailAddress)) return false;
			var data = '<ul><li> <a href="" comName="' + emailAddress + '" comId="' + emailAddress + '" class="js_select_community">' +
						'<span>' + emailAddress + '</span></a></li></ul>';
			target.html(data).width(listWidth);
			target.show().find('li:first a').click();
			input.focusout();
			return;
		}
		timeoutId = setTimeout(function() {
			timeoutId = null;
			var currentValue = input.attr('value');
			if (lastValue === currentValue && currentValue !== requestedValue) {
				requestedValue = currentValue;
				$.ajax({
					url : url,
					data : {
						key : input.attr('value'),
						communityId : communityId
					},
					context : input,
					success : function(data, status, jqXHR) {
						if(isEmpty($(data).find('a')) && url === 'email_address.sw' && !isEmpty(requestedValue)){
							var emailAddress = requestedValue;
							if(isEmailAddress(emailAddress)){
								data = '<ul><li> <a href="" comName="' + emailAddress + '" comId="' + emailAddress + '" class="js_select_community">' +
										'<span>' + emailAddress + '</span></a></li></ul>';
							}
						}
						if(!isEmpty(appendTaskApproval)){
							target.css({ "top" : listTop + "px"});
							target.css({ "left" : listLeft + "px"});
						}
						target.html(data).width(listWidth);
						target.show();
					},
					error : function(xhr, ajaxOptions, thrownError){
					}
				});
			} else {
			}
		}, 500);
	});

	/*
	 * 검색 입력창에서 검색을 하고나서, 포커스가 다른곳으로 이동을 하면, 500ms후에 검색결과 창을 숨긴다.
	 */
	$('input.js_auto_complete').live('focusout', function(e) {
		var input = $(targetElement(e));
		input.attr('value', '');
		requestedValue = "";
		var startWork = input.parents('div.js_start_work_page');
		var user_name = input.parents('div.js_community_names');
		var chatter_name = input.parents('div.js_chatter_names');
		var appendTaskApproval = input.parents('.js_append_task_approval_page');
		if(!input.hasClass('js_approver_box')) appendTaskApproval = [];
		var target;
		if (!isEmpty(startWork))
			target = startWork.find('#upload_work_list');
		else if (!isEmpty(user_name))
			target = user_name.next('div');
		else if(!isEmpty(chatter_name))
			target = chatter_name.nextAll('div.js_chatter_list');
		else if(!isEmpty(appendTaskApproval))
			target = appendTaskApproval.find('.js_approval_line_box').nextAll('.js_community_list');
		else
			target = input.parent().nextAll('div');
		setTimeout(function() {
			var searchButton = input.next('div');
			if(searchButton.hasClass('js_icon_white'))
				searchButton.addClass('srch_icon_w');
			target.html('').hide();
		}, 500);
	});
	
	$('input.js_auto_complete').live('keydown', function(e) {
		var e = window.event || e;
		var keyCode = e.which || e.keyCode;
		if(keyCode == $.ui.keyCode.UP || keyCode == $.ui.keyCode.DOWN  ){
			var input = $(targetElement(e));
			var startWork = input.parents('div.js_start_work_page');
			var chatter_name = input.parents('div.js_chatter_names');
			var appendTaskApproval = input.parents('.js_append_task_approval_page');
			if(!input.hasClass('js_approver_box')) appendTaskApproval = [];

			var target = input.parent().next('div');
			if(!isEmpty(startWork)) target =  startWork.find('#upload_work_list');
			else if(!isEmpty(chatter_name)) target = chatter_name.siblings('div.js_chatter_list');
			else if(!isEmpty(appendTaskApproval)) target = appendTaskApproval.find('.js_approval_line_box').next('.js_community_list');

			var list = target.find('li');
			if(isEmpty(list)) return;
			var sw_hover = target.find('.sw_hover');
			if(keyCode == $.ui.keyCode.DOWN){
				if(isEmpty(sw_hover) || isEmpty(sw_hover.next()))
					$(list[0]).addClass('sw_hover').siblings().removeClass('sw_hover');					
				else
					sw_hover.next().first().addClass('sw_hover').siblings().removeClass('sw_hover');
				
			}else if(keyCode == $.ui.keyCode.UP){
				if(isEmpty(sw_hover) || isEmpty(sw_hover.prev()))
					$(list[list.length-1]).addClass('sw_hover').siblings().removeClass('sw_hover');					
				else
					sw_hover.prev().first().addClass('sw_hover').siblings().removeClass('sw_hover');					
			}
		}else if(keyCode == $.ui.keyCode.ENTER){
			var input = $(targetElement(e));

			var startWork = input.parents('div.js_start_work_page');
			var chatter_name = input.parents('div.js_chatter_names');
			var appendTaskApproval = input.parents('.js_append_task_approval_page');
			if(!input.hasClass('js_approver_box')) appendTaskApproval = [];

			var target = input.parent().next('div');
			if(!isEmpty(startWork)) target =  startWork.find('#upload_work_list');
			else if(!isEmpty(chatter_name)) target = chatter_name.siblings('div.js_chatter_list');
			else if(!isEmpty(appendTaskApproval)) target = appendTaskApproval.find('.js_approval_line_box').next('.js_community_list');

			var url = input.attr('href');
			var lastValue = input.attr('value');
			if(url === 'email_address.sw' && !isEmpty(lastValue)){
				if(isEmailAddress(lastValue)){
					if(isEmpty(target.find('.sw_hover:first a'))) return true;
				}
			}
			target.find('.sw_hover:first a').click();
			input.focusout();
			return false;
		}
	});
	
	$('.nav_srch_list').find('li').live('hover', function(e){
		$(targetElement(e)).parents('li:first').addClass('sw_hover').siblings().removeClass('sw_hover');
	});
	
	$('#upload_work_list').find('li').live('hover', function(e){
		$(targetElement(e)).parents('li:first').addClass('sw_hover').siblings().removeClass('sw_hover');
	});
	
	$('div.js_chatter_list').find('li').live('hover', function(e){
		$(targetElement(e)).parents('li:first').addClass('sw_hover').siblings().removeClass('sw_hover');
	});

	$('div.js_srch_x').live('click', function(e) {
		var input = $(targetElement(e)).prev();
		input.value = "";
		input.next('div').removeClass('btn_x').addClass('srch_icon');
		return false;
	});

	$('.js_select_community').live( 'click', function(e) {
		var input = $(targetElement(e));
		if(!input.hasClass('js_select_community')) input = input.parents('.js_select_community:first');
		var comName = input.attr('comName');
		var comId = input.attr('comId');
		var communityItems = input.parents('.js_community_list').prev().find('.js_community_item');
		var selectedApproverInfo = input.parents('.js_community_list').prev().find('.js_selected_approver_info');
		var userField = input.parents('.js_type_userField');
		var departmentField = input.parents('.js_type_departmentField');
		var inputTarget = (!isEmpty(userField)) ? userField.find('input.js_auto_complete') : (!isEmpty(departmentField)) ? departmentField.find('input.js_auto_complete') : $("");
		if(inputTarget.parents('.sw_required').hasClass('sw_error')){
			inputTarget.parents('.sw_required').removeClass('sw_error');
			$('form.js_validation_required').validate({ showErrors: showErrors}).form();
		}
		if(isEmpty(communityItems) && !isEmpty(selectedApproverInfo)){
			var index = parseInt(input.parents('.js_community_list').attr('approverIndex'));
			selectedApproverInfo = $(selectedApproverInfo[index]);
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
			selectedApproverInfo.html(approverInfo);
			selectedApproverInfo.nextAll('span').hide();
			selectedApproverInfo.nextAll('input').attr('value', comId);
		}else{
			var isSameId = false;
			if (isEmpty(communityItems) || (!isEmpty(userField) && userField.attr('multiUsers') !== 'true') || (!isEmpty(departmentField))){
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
				var titleDesc = "";
				if(inputTarget.attr('href') === "email_address.sw"){
					if(!isEmailAddress(inputTarget.attr('value')) && isEmailAddress(comId) && !isEmailAddress(comName)){
						comNameLong = comName + "&lt;" + comId + "&gt;";
					}
				}else if(inputTarget.attr('href') === "department_name.sw" || inputTarget.attr('href') === "community_name.sw"){
					comNameLong = getDepartNameFromFullpath(comName);
					if(comName !== comNameLong){
						titleDesc = ' title="' + comName + '"';
					}
				}

				$("<span class='js_community_item user_select' comId='" + comId + "' comName='" + comName+ "'" + titleDesc + ">" + comNameLong
						+ "<a class='js_remove_community' href=''>&nbsp;x</a></span>").insertBefore(inputTarget);

				var searchFilter = input.parents('.js_search_filter_page');
				if(!isEmpty(searchFilter)){
					userField.find('input[name="txtFilterStringOperand"]').attr('value', comId);
				}
			}
			inputTarget.focus().parents('.js_community_names').change();
		}		
		
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
		return false;
	});

	$('.js_remove_community').live('click', function(e) {
		var input = $(targetElement(e));
		
		var userField = input.parents('.js_type_userField');
		if(!isEmpty(userField) && userField.attr('multiUsers') !== 'true') {
			var inputTarget = userField.find('input.js_auto_complete');
			inputTarget.show();
			inputTarget.next('.js_srch_x').show();
		}
		var searchFilter = input.parents('.js_search_filter_page');
		if(!isEmpty(searchFilter)){
			userField.find('input[name="txtFilterStringOperand"]').attr('value', '');
		}
		if(!isEmpty(input.parents('td[fieldId="txtFromCommunity"]'))){
			var workTransfer = input.parents('.js_work_transfer_page');
			workTransfer.attr('fromCommunityId', '').attr('comType', '').find('input.js_click_transfer_all').click();
			workTransfer.find('tr').hide();
			input.parents('tr:first').show();
		}
		input.parents('span.js_community_item').remove();
		userField.find('.js_community_names').change();
		userField.find('input.js_auto_complete').focus();

		return false;
	});
});
