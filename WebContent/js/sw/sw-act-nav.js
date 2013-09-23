$(function() {
	
	/*
	 * 좌측 "나의 업무" 박스의 좌측상단에 있는 탭(즐겨찾기, 최근처리, 전체업무)탭들이 class="js_nav_tab_work"로
	 * 지정되어 있으며, 이를 선택하면, 밑에 있는 id="my_works"인 div 영역에 탭에서 지정한 href 의 값을 ajax 로
	 * 호출하여 가져온 값을 보여준다. 그리고, 탭에서 선택된곳에 current 로 지정하고 기타것들은 current 를 제거한다.
	 */
	$('.js_nav_tab_work a').swnavi({
		history : false,
		before : function(event){
			smartPop.progressNavGray($(targetElement(event)).parents('.js_nav_tab_work span.js_progress_span:first'));
			var input = $(targetElement(event));
			input.addClass('current');
			input.parent().siblings().find('span').removeClass('current');
		},
		target : 'my_works',
		after : function(event) {
			smartPop.closeProgress();
		}
	});

	/*
	 * 좌측 "나의 커뮤너티" 박스이 좌측상단에 있는 탭(나의 부서, 나의 그룹)탭들이 class="js_nav_tab_com" 로
	 * 지정되어 있으며, 이를 선택하면, 밑에 있는 id="my_communities"로 지정되어있는 div 영억에, 탭에서 지장한
	 * href 의 값을 ajax 호출하여 가져온 값을 보여준다. 그리고, 탭에서 선택된곳에 current 로 지정하고 기타 것들은
	 * current 를 제거한다.
	 */
	$('.js_nav_tab_com a').swnavi({
		history : false,
		before : function(event){
			smartPop.progressNavGray($(targetElement(event)).parents('.js_nav_tab_com span.js_progress_span:first'));
			var input = $(targetElement(event)).parent('a');
			input.find('span:first').addClass('current');
			input.find('.btn_add_group').show();
			
			input.siblings().find('span:first').removeClass('current');
			input.siblings().find('.btn_add_group').hide();
		},
		target : 'my_communities',
		after : function(event) {
			smartPop.closeProgress();
		}
	});

	/*
	 * 어디에서든 class 값이 js_content로 지정된 anchor 가 선택이 되면, anchor 의 href 값으로 ajax 를 호출하여
	 * 가져온 값을 content(메인컨텐트)화면에 보여준다.
	 */
	$('.js_content').swnavi({
		history : true,
		before : function(event){
			var input = $(targetElement(event));
			if(!isEmpty(input.parents('.js_nav_my_works')) || !isEmpty(input.parents('.js_nav_my_com'))){
				smartPop.progressNavGray(input.parents('li:first').find('span:last'));
			}else if(!isEmpty(input.parents('.js_srch_my_works'))){
				smartPop.progressNav(input.parents('.js_srch_my_works').prev('li span:first'));
			}else if(!isEmpty(input.parents('.js_space_wall'))){
				smartPop.progressNav(input.parents('li:first').find('span:last'));
			}else{
				smartPop.progressCenter();				
			}
		},
		target : 'content',
		after : function(event){
			smartPop.closeProgress();
		}
	});

	$('.js_content_list').swnavi({
		history : true,
		before : function(event){
			smartPop.progressCenter();				
		},
		target : 'content',
		after : function(event){
			smartPop.closeProgress();
		}
	});

	$('a.js_location').live('click', function(e){
		
		var input = $(targetElement(e));
		if(!isEmpty(input.parents('.js_srch_my_com'))){
			smartPop.progressNavGray(input.parents('.js_srch_my_com').prev('li').find('span:last'));
			window.location = input.attr('href');
			smartPop.closeProgress();
		}else if(!isEmpty(input.parents('.js_srch_com_members'))){
			smartPop.progressNavGray(input.parents('.js_srch_com_members').prev('li').find('span:last'));
			window.location = input.attr('href');
			smartPop.closeProgress();
		}
	});

	$('.js_content_work_space').swnavi({
		history : true,
		before : function(e){
			smartPop.progressCenter();
		},
		target : 'content',
		after : function(e){
			smartPop.closeProgress();
		}
	});

	/*
	 * 좌측상단에 있는 알림아이콘들에 설정된 class 속성값들이 js_notice_count이고, 이를 클릭하면 그곳의 href값으로
	 * ajax를 호출하여 가져온 값을 id="notice_message_box"로 지정된 div영역에 보여준다. 실행전에는,
	 * id="notice_message_box"로 지정된곳을 찾아서 먼저 닫고(이전에 실행된화면을 지우기 위해서) 화면이 아래로 펼처지게
	 * 한다. 실행후에는, class="js_notice_message_box'로 지정된 알림아이콘들에서 모든 current를 지우고,
	 * 현재 선택된곳에만 current를 추가한다.
	 */
	$('.js_notice_count a').live('click', function(event){
		var input = $(targetElement(event)).parents('.js_notice_count:first').find('a');
		var noticeCount = $(targetElement(event)).parents('.js_notice_count:first');
		noticeCount.siblings('.js_notice_count').removeClass('mt4');
		smartPop.progressNav($('div.js_notice_icons_area li:last'));
		$('#notice_message_box').hide();
		$('.js_notice_count').find('a').removeClass('current');
		if(input.hasClass('js_mailbox_notice')){
			var mailInboxId = input.attr('mailInboxId');
			$.ajax({
				url : "mail_list.sw?cid=" + mailInboxId,
				data : {},
				success : function(data, status, jqXHR) {
					$('#content').html(data);
					smartPop.closeProgress();
				},
				error : function(xhr, ajaxOptions, thrownError){
					smartPop.closeProgress();
				}
			});
			
		}else if(input.hasClass('js_saved_notice')){
			$.ajax({
				url : "saved_list.sw?cid=tw.li",
				data : {},
				success : function(data, status, jqXHR) {
					$('#content').html(data);
					smartPop.closeProgress();
				},
				error : function(xhr, ajaxOptions, thrownError){
					smartPop.closeProgress();
				}
			});
				
		}else if(!isEmpty(input.children())){
			var href = input.attr('href');
			$.ajax({
				url : href,
				data : {},
				success : function(data, status, jqXHR) {
					noticeCount.addClass('mt4');
					input.addClass('current');
					$('#notice_message_box').html(data).show();
					smartPop.closeProgress();
				},
				error : function(xhr, ajaxOptions, thrownError){
					smartPop.closeProgress();
				}
			});
		}else{
			smartPop.closeProgress();			
		}
		return false;
	});
	
	$('.js_more_notice_list').live('click', function(event){
		var input = $(targetElement(event));
		smartPop.progressNav(input.next());
		var href = input.attr('href');
		var lastNoticeId = input.attr('lastTaskId');
		$.ajax({
			url : href,
			data : {
				lastNoticeId : lastNoticeId
			},
			success : function(data, status, jqXHR) {
				input.remove();
				$('#notice_message_box').find('.js_notice_message_box_list').append(data);
				smartPop.closeProgress();
			},
			error : function(xhr, ajaxOptions, thrownError){
				smartPop.closeProgress();
			}
		});
		return false;
	});
	
	// Notice Message Box 에서 항목의 삭제버튼을 클릭하면 실행되는 기능.
	// 선택된 항목에서, noticeId, noticeType, lastNoticeId 를 가져와서, ajax 호출을 한다.
	// 서버에서는 항목을 삭제하고, 삭제된 상태에서의 10개의 항목을 가져다고 전달해준다.
	$('.js_remove_notice').live('click', function(e) {
		var input = $(targetElement(e)).parent('a');
		var noticeId = input.attr('noticeId');
		$.ajax({
			url : "remove_notice_instance.sw",
			data : {
				removeNoticeId : noticeId
			},
			success : function(data, status, jqXHR) {
				input.parents('li:first').remove();
			},
			error : function(xhr, ajaxOptions, thrownError){
				
			}
		});
		return false;
	});

	/*
	 * 좌측상단에서 알림아이콘을 선택하면 펼쳐지는 message box 의 하단에 있는 close 버튼에 설정된 class 값이
	 * js_close_message이며, 이를 선택하면, message_box를 위로 올려닫는다. 그리고나서, 500ms 가 지난뒤에
	 * 알림아이콘에 있는 current 값들을 지운다(message box가 닫혀지는 시간만큼 기다리기위해...)
	 */
	$('.js_close_message').live('click', function(e) {
		$('#notice_message_box').hide();
		setTimeout(function() {
			$('.js_notice_count').removeClass('mt4').find('a').removeClass('current');
		}, 100);
		return false;
	});

	/*
	 * js_collase_parent_siblings class를 가지고있는 항목을 선택하면 부모와 같은 수준에 있는 곳에서
	 * js_collapsible class를 찾아서, 위로 닫고 아래로 여는것을 한번씩 실행해준다.
	 */
	$('.js_collapse_parent_siblings').live('click', function(e) {
		var input = $(targetElement(e));
		if(input.hasClass('arr_on')){
			input.removeClass('arr_on').addClass('arr_off');
			
		}else{
			input.removeClass('arr_off').addClass('arr_on');
		}
		input.parent().parent().siblings('.js_collapsible').toggle();
		return false;
	});

	/*
	 * js_collase_siblings class를 가지고있는 항목을 선택하면 현재와 같은 수준에 있는 곳에서
	 * js_collapsible class를 찾아서, 위로 닫고 아래로 여는것을 한번씩 실행해준다.
	 */
	$('.js_collapse_siblings').live('click', function(e) {
		$(targetElement(e)).siblings('.js_collapsible').toggle();
		return false;
	});

	$('.js_drill_down').live('click', function(e) {
		if($(targetElement(e)).hasClass('js_checkbox')) return true;
		if($(targetElement(e)).hasClass('js_department') || $(targetElement(e)).hasClass('js_user')) return true;
		var input = $(targetElement(e)).parents('li.js_drill_down:first').find('a:first');
		var target = input.siblings('div.js_drill_down_target:first');
		if(input.hasClass('js_popup')) target = input.parent().siblings('div.js_drill_down_target:first');
		if(input.parent().hasClass('ctgr_action_item') || input.parent().hasClass('group_action_item')){
			target = input.nextAll('div.js_drill_down_target:first');
		}
		var url = input.attr('href');
		var categoryId = input[0].getAttribute("categoryId");
		var groupId = input[0].getAttribute("groupId");
		var departmentId = input[0].getAttribute("departmentId");
		var comlistByDepart = input.parents('.js_comlist_by_depart_page');
		var selectCommunity = input.parents('.js_pop_select_community_page');
		var folderId = input[0].getAttribute("folderId");
		if(!isEmpty(comlistByDepart)){
			var editMember = comlistByDepart.parents('.js_organization_management_page').find('.js_edit_member');
			$.ajax({
				url : "edit_department.sw?departId=" + departmentId,
				success : function(data, status, jqXHR) {
					editMember.html(data).slideDown();;
				}			
			});			
		}else if(!isEmpty(selectCommunity)){
			var prevSpan = $(targetElement(e)).prev();
			if(!isEmpty(prevSpan) && prevSpan.hasClass('js_plus_minus_span') && (!isEmpty(departmentId) || !isEmpty(groupId))){
				$(targetElement(e)).addClass('js_pop_select_community').attr('comId', departmentId || groupId).click();
				smartPop.close();
				target.html('');				
				return false;		
			}
		}
		if (url == 'undefined' || (categoryId==null && isEmpty(groupId) && isEmpty(departmentId) && isEmpty(folderId))) {
			return false;
		}
		if(isEmpty($(target).children())){
			if(input.hasClass('js_popup'))
				smartPop.progressCont(input.find('span:last'));
			else if(input.hasClass('js_builder') || input.hasClass('js_mail_folder'))
				smartPop.progressNav(input.find('span.js_progress_span'));						
			else
				smartPop.progressNav(input.find('span:last'));

			$.ajax({
				url : url,
				data : {
					categoryId : categoryId,
					groupId : groupId,
					departmentId : departmentId,
					folderId : folderId
				},
				context : input,
				success : function(data, status, jqXHR) {
					target.show();
					target.html(data).prev().find('.btn_tree_plus').removeClass('btn_tree_plus').addClass('btn_tree_minus');
//					target.siblings('li.js_drill_down').find('.js_drill_down_target').html('').hide().prev().find('.btn_tree_minus').removeClass('btn_tree_minus').addClass('btn_tree_plus');
//					target.parents('li.js_drill_down').siblings('li.js_drill_down').find('.js_drill_down_target').html('').hide().prev().find('.btn_tree_minus').removeClass('btn_tree_minus').addClass('btn_tree_plus');
					target.siblings('li.js_drill_down').find('.js_drill_down_target').hide().prev().find('.btn_tree_minus').removeClass('btn_tree_minus').addClass('btn_tree_plus');
					target.parents('li.js_drill_down').siblings('li.js_drill_down').find('.js_drill_down_target').hide().prev().find('.btn_tree_minus').removeClass('btn_tree_minus').addClass('btn_tree_plus');
					smartPop.closeProgress();											
				},
				error : function(xhr, ajaxOptions, thrownError){
					smartPop.closeProgress();											
				}
			});
		}else if(!target.is(':visible')){
			target.show().prev().find('.btn_tree_plus').removeClass('btn_tree_plus').addClass('btn_tree_minus');
//			target.siblings('li.js_drill_down').find('.js_drill_down_target').html('').hide().prev().find('.btn_tree_minus').removeClass('btn_tree_minus').addClass('btn_tree_plus');
//			target.parents('li.js_drill_down').siblings('li.js_drill_down').find('.js_drill_down_target').html('').hide().prev().find('.btn_tree_minus').removeClass('btn_tree_minus').addClass('btn_tree_plus');
			target.siblings('li.js_drill_down').find('.js_drill_down_target').hide().prev().find('.btn_tree_minus').removeClass('btn_tree_minus').addClass('btn_tree_plus');
			target.parents('li.js_drill_down').siblings('li.js_drill_down').find('.js_drill_down_target').hide().prev().find('.btn_tree_minus').removeClass('btn_tree_minus').addClass('btn_tree_plus');
//		}else if(target.is(':visible') && !isEmpty($(target).children()) &&  $(targetElement(e)).parents('a:first').hasClass('js_expandable')){
		}else if(target.is(':visible') && !isEmpty($(target).children())){
			target.html('').hide().prev().find('.btn_tree_minus').removeClass('btn_tree_minus').addClass('btn_tree_plus');
		}
		return false;
	});
	
	$('.js_check_favorite_work').live('click', function(e){
		var input = $(targetElement(e));
		var workId = input.attr('workId');
		var favoriteWorks = input.parents('li.js_favorite_works:first');
		var url = 'remove_a_favorite_work.sw';
		var isAdd = false;
		if(!input.hasClass('checked') && isEmpty(favoriteWorks)){
			url = 'add_a_favorite_work.sw';
			isAdd = true;
		}
		var progressSpan = input.parent().prev().find('span:last');
		smartPop.progressNav(progressSpan);						
		$.ajax({
			url : url,
			data : {
				workId : workId
			},
			success : function(data, status, jqXHR) {
				if(isAdd){
					input.addClass('checked');
				}else{
					if(isEmpty(favoriteWorks))
						input.removeClass('checked');
					else
						favoriteWorks.remove();
				}
				smartPop.closeProgress();											
			},
			error : function(xhr, ajaxOptions, thrownError){
				if(isAdd) input.removeClass('checked');
				else input.addClass('checked');
				smartPop.closeProgress();											
			}
		});		
		return false;
	});

	$('.js_check_favorite_com').live('click', function(e){
		var input = $(targetElement(e));
		var comId = input.attr('comId');
		var favoriteComs = input.parents('li.js_favorite_coms:first');
		var url = 'remove_a_favorite_community.sw';
		var isAdd = false;
		if(!input.hasClass('checked') && isEmpty(favoriteComs)){
			url = 'add_a_favorite_community.sw';
			isAdd = true;
		}
		var progressSpan = input.parent().prev().find('span:last');
		smartPop.progressNav(progressSpan);						
		$.ajax({
			url : url,
			data : {
				comId : comId
			},
			success : function(data, status, jqXHR) {
				if(isAdd){
					input.addClass('checked');
				}else{
					if(isEmpty(favoriteComs))
						input.removeClass('checked');
					else
						favoriteComs.remove();
				}
				smartPop.closeProgress();											
			},
			error : function(xhr, ajaxOptions, thrownError){
				if(isAdd) input.removeClass('checked');
				else input.addClass('checked');
				smartPop.closeProgress();											
			}
		});		
		return false;
	});

	$('.js_add_new_group').live('click', function(e){
		smartPop.createGroup();
		return false;
	});
	
	$('span.js_collapse_detail').live('click', function(e){
		var input = $(targetElement(e));
		if(!input.hasClass('js_collapse_detail')) return;
		if(input.hasClass('arr_on')){
			input.removeClass('arr_on').addClass('arr_off');
			input.parents('ul:first').next().hide();
		}else{
			input.removeClass('arr_off').addClass('arr_on');
			input.parents('ul:first').next().show();			
		}
		return false;
	});
	
	$('.js_header_user_settings').live('click', function(e){
		$(targetElement(e)).parents('.js_header_user_settings').hide();
	});
});
