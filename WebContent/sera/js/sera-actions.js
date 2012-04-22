$(function() {
	
	/*
	 * 어디에서든 class 값이 js_container로 지정된 anchor 가 선택이 되면, anchor 의 href 값으로 ajax 를 호출하여
	 * 가져온 값을 container(메인컨텐트)화면에 보여준다.
	 */
	$('.js_sera_content').seranavi({
		history : true,
		before : function(event){
			smartPop.progressCenter();				
		},
		target : 'sera_content',
		after : function(event){
			smartPop.closeProgress();
		}
	});

	$('.js_course_menu').live('click', function(e){
		var input = $(e.target).parent();
		input.siblings().removeClass('current');
		input.addClass('current');
		var courseHome = input.parents('.js_course_home_page');
		var courseId = courseHome.attr('courseId');
		var subMenu = courseHome.find('.js_course_sub_menu').hide();
		var subMenus = subMenu.children().hide();
		var pos = input.prevAll().length;
		$(subMenus[pos]).addClass('current');
		if(!isEmpty($(subMenus[pos]).children())){
			$(subMenus[pos]).show();
			subMenu.show();
		}
		var url = "";
		var target = '.js_course_content';
		if(pos==0){
			url = "courseHome.sw";
			target = "#sera_content";
		}else if(pos==1){
			url = "courseMissionList.sw";
		}else if(pos==2){
			url = "courseBoard.sw";
		}else if(pos==3){
			url = "courseTeamActivity.sw";
		}else if(pos==4){
			url = "courseGeneral.sw";
		}else if(pos==5){
			url = "courseSetting.sw";
		}
		if(pos!=5 || (pos==5 && isEmpty($(subMenus[pos]).children()))){
			smartPop.progressCenter();				
			$.ajax({
				url : url,
				data : {courseId : courseId},
				success : function(data, status, jqXHR) {
					$(target).html(data);
					smartPop.closeProgress();
				},
				error : function(){
					smartPop.closeProgress();
				}
			});
		}else{
			$('.js_course_setting_menu li.js_course_setting_profile a').click();			
		}
		return false;
	});
	
	$('.js_course_setting_menu').live('click', function(e){
		var input = $(e.target).parent();
		input.siblings().removeClass('current');
		input.addClass('current');
		var courseHome = input.parents('.js_course_home_page');
		var courseId = courseHome.attr('courseId');
		var url ="";
		if(input.hasClass('js_course_setting_profile')){
			url = "courseSettingProfile.sw";		
		}else if(input.hasClass('js_create_mission')){
			url = "courseMissionCreate.sw";
		}else if(input.hasClass('js_course_setting_mentee')){
			url = "courseSettingMentee.sw";
		}else if(input.hasClass('js_course_setting_team')){
			url = "courseSettingTeam.sw";
		}
		smartPop.progressCenter();
		$.ajax({
			url : url,
			data : {courseId : courseId},
			success : function(data, status, jqXHR) {
				$('.js_course_content').html(data);
				smartPop.closeProgress();
			},
			error : function(){
				smartPop.closeProgress();
			}
		});
		return false;
	});

	$('.js_course_mission_menu').live('click', function(e){
		var input = $(e.target).parent();
		input.siblings().removeClass('current');
		input.addClass('current');
		var courseHome = input.parents('.js_course_home_page');
		var courseId = courseHome.attr('courseId');
		var url ="";
		var target = $('.js_course_content');
		if(input.hasClass('js_course_mission_space')){
			url = "courseMissionSpace.sw";			
		}else if(input.hasClass('js_course_mission_create')){
			url = "courseMissionCreate.sw";
		}else if(input.hasClass('js_course_mission_list')){
			url = "courseMissionList.sw";
		}else if(input.hasClass('js_course_mission_mine')){
			url = "courseMissionMine.sw";
		}
		smartPop.progressCenter();				
		$.ajax({
			url : url,
			data : {courseId : courseId},
			success : function(data, status, jqXHR) {
				target.html(data);
				smartPop.closeProgress();
			},
			error : function(){
				smartPop.closeProgress();
			}
		});
		return false;
	});

	$('.js_view_user_instances').live('click', function(e){
		var input = $(e.target);
		input.parent().siblings().find('a').removeClass('current');
		input.addClass('current');
		var userId = input.attr('userId');
		var courseInstanceList = input.parents('.js_course_instance_list_page');
		var courseId = (isEmpty(courseInstanceList)) ? null : courseInstanceList.attr('courseId');
		smartPop.progressCenter();				
		$.ajax({
			url : 'seraInstances.sw',
			data : {
				userId : userId,
				courseId : courseId
			},
			success : function(data, status, jqXHR) {
				$('.js_user_instance_list').html(data);
				smartPop.closeProgress();
			},
			error : function(){
				smartPop.closeProgress();
			}
		});
		return false;
	});

	$('.js_view_all_instances').live('click', function(e){
		var input = $(e.target);
		input.parent().siblings().find('a').removeClass('current');
		input.addClass('current');
		var courseInstanceList = input.parents('.js_course_instance_list_page');
		var courseId = (isEmpty(courseInstanceList)) ? null : courseInstanceList.attr('courseId');
		smartPop.progressCenter();				
		$.ajax({
			url : 'seraInstances.sw',
			data : {
				courseId : courseId
			},
			success : function(data, status, jqXHR) {
				$('.js_user_instance_list').html(data);
				smartPop.closeProgress();
			},
			error : function(){
				smartPop.closeProgress();
			}
		});
		return false;
	});
	
	$('.js_mentor_form_btn').live('click', function(e){
		var input = $(e.target).parents('.js_mentor_form_btn');
		var createCourse = input.parents('.js_create_course_page');
		if(createCourse.find('input[name="chkUserDefineDays"]').attr('checked')==='checked'){
			createCourse.find('input[name="txtCourseStartDate"]').addClass('required');
			createCourse.find('input[name="txtCourseEndDate"]').addClass('required');
			createCourse.find('input[name="txtCourseDays"]').removeClass('required').removeClass('error');			
			
		}else{
			createCourse.find('input[name="txtCourseStartDate"]').removeClass('required').removeClass('error');
			createCourse.find('input[name="txtCourseEndDate"]').removeClass('required').removeClass('error');
			createCourse.find('input[name="txtCourseDays"]').addClass('required');			
		}
		
		if(createCourse.find('input[name="chkCourseUsers"]:checked').attr('value')==='userInput'){
			createCourse.find('input[name="txtCourseUsers"]').addClass('required');
			
		}else{
			createCourse.find('input[name="txtCourseUsers"]').removeClass('required').removeClass('error');
		}
		
		if(createCourse.find('input[name="chkCourseFee"]:checked').attr('value')==='pay'){
			createCourse.find('input[name="txtCourseFee"]').addClass('required');
			
		}else{
			createCourse.find('input[name="txtCourseFee"]').removeClass('required').removeClass('error');
		}
		
		if (SmartWorks.GridLayout.validate(createCourse.find('form.js_validation_required'), createCourse.find('.sw_error_message'))) {		
			input.parents('.js_create_buttons').hide().siblings().css({clear:"both", display:"inline-block"});
			createCourse.find('.js_create_course_table').hide();
			createCourse.find('.js_mentor_profile_table').show();
			return false;
		}
		return false;
	});

	$('.js_create_course_btn').live('click', function(e){
		submitForms(e);
		return false;
	});
	
	$('.js_invite_course_members_btn').live('click', function(e){
		var courseId = $(e.target).parents('.js_course_home_page').attr('courseId');
		var target = $('.js_course_content');
		smartPop.progressCenter();				
		$.ajax({
			url : 'inviteCourseMembers.sw',
			data : {
				courseId : courseId
			},
			success : function(data, status, jqXHR) {
				target.html(data);
				smartPop.closeProgress();
			},
			error : function(){
				smartPop.closeProgress();
			}
		});
		return false;
	});
	
	$('.js_remove_course_btn').live('click', function(e){
		smartPop.confirm('코스를 삭제하시려고 합니다. 정말로 삭제하시겠습니까??', function(){
			var input = $(e.target);
			var courseId = input.parents('.js_setting_profile_page').attr('courseId');
			var paramsJson = {};
			paramsJson['courseId'] = courseId;
			console.log(JSON.stringify(paramsJson));
			smartPop.progressCenter();
			$.ajax({
				url : "remove_course.sw",
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					smartPop.closeProgress();					
					smartPop.showInfo(smartPop.INFO, "코스가 정상적으로 삭제되었습니다!", function(){
						document.location.href = "myPAGE.sw";									
					});
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, "코스를 삭제하는 중에 오류가 발생하였습니다. 관리자에게 문의하시기 바랍니다!", function(){
					});
				}
				
			});
		});
		return false;
	});
	
	$('.js_modify_course_btn').live('click', function(e){
		var input = $(e.target).parents('.js_modify_course_btn');
		var settingProfile = input.parents('.js_setting_profile_page');
		if(settingProfile.find('input[name="chkUserDefineDays"]').attr('checked')==='checked'){
			settingProfile.find('input[name="txtCourseStartDate"]').addClass('required');
			settingProfile.find('input[name="txtCourseEndDate"]').addClass('required');
			settingProfile.find('input[name="txtCourseDays"]').removeClass('required').removeClass('error');			
			
		}else{
			settingProfile.find('input[name="txtCourseStartDate"]').removeClass('required').removeClass('error');
			settingProfile.find('input[name="txtCourseEndDate"]').removeClass('required').removeClass('error');
			settingProfile.find('input[name="txtCourseDays"]').addClass('required');			
		}
		
		if(settingProfile.find('input[name="chkCourseUsers"]:checked').attr('value')==='userInput'){
			settingProfile.find('input[name="txtCourseUsers"]').addClass('required');
			
		}else{
			settingProfile.find('input[name="txtCourseUsers"]').removeClass('required').removeClass('error');
		}
		
		submitForms(e);
		return false;
	});
	
	$('.js_back_to_create_btn').live('click', function(e){
		var input = $(e.target).parents('.js_back_to_create_btn');		
		input.parents('.js_mentor_buttons').hide().siblings().css({clear:"both", display:"inline-block"});
		var createCourse = input.parents('.js_create_course_page');
		createCourse.find('.js_create_course_table').show();
		createCourse.find('.js_mentor_profile_table').hide();
		return false;
	});

	$('.js_join_course_request').live('click', function(e){
		var input = $(e.target).parents('.js_join_course_request');
		var paramsJson = {};
		paramsJson["courseId"] = input.parents('.js_course_content').attr('courseId');
		paramsJson["userId"] = currentUserId;
		smartPop.progressCenter();				
		$.ajax({
			url : 'join_group_request.sw',
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				smartPop.closeProgress();
				if(input.attr('autoApproval')==="true")
					smartPop.showInfo(smartPop.INFO, "코스가입 정상적으로 처리되었습니다. 코스에 방문하시면 미션들을 수행할 수 있습니다.");
				else
					smartPop.showInfo(smartPop.INFO, "코스가입 신청이 정상적으로 처리되었으며, 멘토의 가입승인을 기다리고 있습니다.");
				input.remove();
			},
			error : function(){
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.ERROR, "코스가입하기에 문제가 발생하였습니다. 관리자에게 문의하시기 바랍니다.");
			}
		});
		return false;
	});
	
	$('.js_select_mission').live('click', function(e){
		var input = $(e.target).parent('a');
		var missionList = input.parents('.js_mission_list_page');
		var courseId = missionList.attr('courseId');
		var missionId = input.attr('missionId');
		smartPop.progressCenter();				
		$.ajax({
			url : 'courseMissionPerform.sw',
			data : {
				courseId :courseId,
				missionId : missionId
			},
			success : function(data, status, jqXHR) {
				$('.js_course_content').html(data);
				smartPop.closeProgress();
			},
			error : function(){
				smartPop.closeProgress();
			}
		});
		return false;
	});
	
	$('.js_show_modify_mission').live('click', function(e){
		var input = $(e.target);
		var performMission = input.parents('.js_perform_mission_page');
		var courseId = performMission.attr('courseId');
		var missionId = performMission.attr('missionId');
		smartPop.progressCenter();				
		$.ajax({
			url : 'courseMissionModify.sw',
			data : {
				courseId :courseId,
				missionId : missionId
			},
			success : function(data, status, jqXHR) {
				$('.js_course_content').html(data);
				smartPop.closeProgress();
			},
			error : function(){
				smartPop.closeProgress();
			}
		});
		return false;
	});
	
	$('.js_create_mission_btn').live('click', function(e){
		submitForms(e);
		return false;
	});
	
	$('.js_modify_mission_btn').live('click', function(e){
		submitForms(e);
		return false;
	});
	
	$('.js_delete_mission_btn').live('click', function(e){
		smartPop.confirm('미션을 삭제하려고 합니다. 정말로 삭제하시겠습니까??', function(){
			var input = $(e.target);
			var performMission = input.parents('.js_perform_mission_page');
			var courseId = performMission.attr('courseId');
			var paramsJson = {};
			paramsJson["courseId"] = courseId;
			paramsJson["courseId"] = performMission.attr('missionId');
			smartPop.progressCenter();				
			$.ajax({
				url : 'remove_mission.sw',
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					document.location.href = "courseHome.sw?courseId=" + courseId;
					smartPop.closeProgress();
				},
				error : function(){
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, "코스삭제에 문제가 발생하였습니다. 관리자에게 문의하시기 바랍니다.");
				}
			});
		});
		return false;
	});
	
	$('.js_delete_instance_btn').live('click', function(e){
		smartPop.confirm('항목을 삭제하려고 합니다. 정말로 삭제하시겠습니까??', function(){
			var input = $(e.target);
			var subInstanceList = input.parents('.js_sub_instance_list');
			var	workInstanceId = subInstanceList.attr('instanceId');
			var	workType = subInstanceList.attr('workType');
			var paramsJson = {};
			paramsJson['workType'] = parseInt(workType);
			paramsJson['workInstanceId'] = workInstanceId;
			console.log(JSON.stringify(paramsJson));
			smartPop.progressCenter();				
			$.ajax({
				url : 'remove_sera_instance.sw',
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					input.parents('.js_sera_instance_item').remove();
					smartPop.closeProgress();
				},
				error : function(){
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, "항목삭제에 문제가 발생하였습니다. 관리자에게 문의하시기 바랍니다.");
				}
			});
		});
		return false;
	});
	
	$('.js_note_buttons').live('click', function(e){
		var input = $(e.target);
		var noteAttachmentTable = input.parents('.js_note_buttons').siblings('.js_note_attachment_table');
		var targetTable = [];
		if(!isEmpty(input.hasClass('js_note_file_btn')))
			targetTable = noteAttachmentTable.find('.js_note_file');
		else if(!isEmpty(input.hasClass('js_note_image_btn')))
			targetTable = noteAttachmentTable.find('.js_note_image');
		else if(!isEmpty(input.hasClass('js_note_video_btn')))
			targetTable = noteAttachmentTable.find('.js_note_video');
		else if(!isEmpty(input.hasClass('js_note_link_btn')))
			targetTable = noteAttachmentTable.find('.js_note_link');
		if(noteAttachmentTable.is(':visible')){
			noteAttachmentTable.show();
			targetTable.toggle();
			if(isEmpty(noteAttachmentTable.find('tr:visible')))
				noteAttachmentTable.hide();
		}else{
			noteAttachmentTable.show();
			targetTable.show();
		}
		return false;
	});
	
	$('.js_create_note_btn').live('click', function(e){
		submitForms(e);
		return false;
	});
	
	$('.js_report_content').live('click', function(e){
		$(e.target).attr('rows', 24);
	});
	
	$('.js_create_team_btn').live('click', function(e){
		submitForms(e);
		return false;
	});

	$('.js_modify_team_btn').live('click', function(e){
		submitForms(e);
		return false;
	});

	$('.js_remove_team_btn').live('click', function(e){
		smartPop.confirm('코스팀을 삭제하려고 합니다. 정말로 삭제하시겠습니까??', function(){
			var input = $(e.target);
			var form = input.parents('.js_course_setting_page').find('form');
			var courseId = form.attr('courseId');
			var teamId = form.attr('teamId');
			var paramsJson = {};
			paramsJson['courseId'] = courseId;
			paramsJson['teamId'] = teamId;
			console.log(JSON.stringify(paramsJson));
			smartPop.progressCenter();				
			$.ajax({
				url : "remove_course_team.sw",
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.INFO, "팀이 성공적으로 삭제 되었습니다.", function(){
						document.location.href = "courseHome.sw?courseId=" + courseId;						
					});
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, "회원탈퇴에 문제가 발생하였습니다. 관리자에게 문의하시기 바랍니다.", function(){
					});
				}
				
			});
			return false;
		}); 
		return false;
	});

	$('.js_modify_profile_btn').live('click', function(e){
		submitForms(e);
		return false;
	});
	
	$('.js_leave_sera_btn').live('click', function(e){
		smartPop.confirm('세라캠퍼스 회원을 탈퇴하시려고 합니다. 정말로 탈퇴하시겠습니까??', function(){
			var input = $(e.target);
			var userId = input.parents('.js_my_profile_page').attr('userId');
			var paramsJson = {};
			paramsJson['userId'] = userId;
			console.log(JSON.stringify(paramsJson));
			smartPop.progressCenter();				
			$.ajax({
				url : "leave_sera_user.sw",
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					document.location.href = "logins.sw";					
					smartPop.closeProgress();
				},
				error : function(e) {
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, "회원탈퇴에 문제가 발생하였습니다. 관리자에게 문의하시기 바랍니다.", function(){
					});
				}
				
			});
			return false;
		}); 
		return false;
	});
	
	$('.js_return_on_sera_comment').live('keydown', function(e) {
		if(e.which != $.ui.keyCode.ENTER) return;
		var input = $(e.target);
		var subInstanceList = input.parents('.js_sub_instance_list');
		var comment = input.attr('value');
		if(isEmpty(comment)) return false;
		var	workInstanceId = subInstanceList.attr('instanceId');
		var	workType = subInstanceList.attr('workType');
		var paramsJson = {};
		paramsJson['workType'] = parseInt(workType);
		paramsJson['workInstanceId'] = workInstanceId;
		url = "add_comment_on_instance.sw";
		paramsJson['comment'] = comment;
		console.log(JSON.stringify(paramsJson));
		smartPop.progressCenter();				
		$.ajax({
			url : url,
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				var target = subInstanceList.find('.js_comment_list');
				var showAllComments = target.find('.js_show_all_sera_comments');
				if(!isEmpty(showAllComments)){
					showAllComments.find('div').click();
					input.attr('value', '');
				}else{
					var newComment = target.find('.js_comment_instance').clone().show().removeClass('js_comment_instance');
					newComment.find('.js_comment_content').html(comment);
					target.append(newComment);
					input.attr('value', '');
				}
				smartPop.closeProgress();
			},
			error : function(e) {
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.ERROR, "댓글달기에 오류가 발생하였습니다. 관리자에게 문의하시기 바랍니다.", function(){
				});
			}
			
		});
		
	});
	
	$('.js_view_new_course_review').live('click', function(e) {
		var courseReview = $(e.target).parents('.js_course_general_page').find('.js_return_on_course_review');
		courseReview.parent().prepend(courseReview.clone());
		courseReview.show();
		return false;
	});
	
	$('.js_star_point_btn').live('click', function(e) {
		var input = $(e.target);
		if(input.hasClass('full')){
			input.removeClass('full').addClass('half');
			input.nextAll().removeClass('full').removeClass('half');
		}else if(input.hasClass('half')){
			input.removeClass('half').addClass('full');
			input.nextAll().removeClass('full').removeClass('half');
		}else{
			input.addClass('full').prevAll().addClass('full').removeClass('half');
			input.nextAll().removeClass('full').removeClass('half');
		}
		return false;
	});
	
	$('.js_return_on_course_review').live('keydown', function(e) {
		if(e.which != $.ui.keyCode.ENTER) return;
		var input = $(e.target);
		var courseGeneral = input.parents('.js_course_general_page');
		var review = input.attr('value');
		if(isEmpty(review)) return false;
		var courseId = courseGeneral.attr('courseId');
		var starPointList = courseGeneral.find('.js_return_on_course_review:visible .js_star_point_list:visible li');
		var starPoint = 0;
		for(var i=0; i<starPointList.length; i++){
			var item = $(starPointList[i]);
			if(item.hasClass('full'))
				starPoint = starPoint + 1;
			else if(item.hasClass('half'))
				starPoint = starPoint + 0.5;
		}
		var paramsJson = {};
		paramsJson['courseId'] = courseId;
		paramsJson['reviewContent'] = review;
		paramsJson['starPoint'] = starPoint;
		url = "add_review_on_course.sw";
		console.log(JSON.stringify(paramsJson));
		smartPop.progressCenter();				
		$.ajax({
			url : url,
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				input.hide().siblings().show();
				input.siblings('.js_review_content').html(review);
				input.parents('.js_return_on_course_review:visible').removeClass('js_return_on_course_review');
				starPointList.parent().removeClass('js_star_point_btn').find('a').remove();
				smartPop.closeProgress();
			},
			error : function(e) {
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.ERROR, "리뷰남기기에 오류가 발생하였습니다. 관리자에게 문의하시기 바랍니다.", function(){
				});
			}			
		});
		
	});
	
	$('.js_more_course_reviews_btn').live('click', function(e) {
		var input = $(e.target).parents('.js_more_course_reviews_btn');
		console.log('input=', input, ', target=', $(e.target));
		var courseId = input.attr('courseId');
		var fromDate = input.attr('fromDate');
		smartPop.progressCont(input.find('.js_progress_span'));
		$.ajax({
			url : "moreCourseReviews.sw",
			data : {
				courseId: courseId,
				fromDate: fromDate
			},
			success : function(data, status, jqXHR) {
				input.parents('.js_course_review_list').append(data);
				input.remove();
				smartPop.closeProgress();
			},
			error : function(e) {
				smartPop.closeProgress();
			}			
		});
		return false;
	});
	
	$('.js_show_all_sera_comments').live('click', function(e) {
		var input = $(e.target).parents('.js_show_all_sera_comments');
		if(isEmpty(input)) input = $(e.target);
		var subInstanceList = input.parents('.js_sub_instance_list');
		var href = input.attr('href');
		smartPop.progressCenter();				
		$.ajax({
			url : href,
			data : {},
			success : function(data, status, jqXHR) {
				var target = subInstanceList.find('.js_comment_list_target');
				target.find(':visible').remove();
				target.append(data);
				input.parent().remove();
				smartPop.closeProgress();
			},
			error : function(e) {
				smartPop.closeProgress();
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.showInfo(smartPop.ERROR, "댓글 모두보기에 오류가 발생하였습니다. 담당자에게 연락하시기 바랍니다.", function(){
				});
			}
			
		});
		return false;
	});
	
	$('a.js_add_sera_comment').live('click', function(e){
		var input = $(e.target).removeAttr('href');
		input.parents('.js_sub_instance_list').find('.js_return_on_sera_comment').show();
		return false;
	});

	$('a.js_add_sera_like').live('click', function(e){
		var input = $(e.target);
		var subInstanceList = input.parents('.js_sub_instance_list');
		var	workInstanceId = subInstanceList.attr('instanceId');
		var	workType = subInstanceList.attr('workType');
		var paramsJson = {};
		paramsJson['workType'] = parseInt(workType);
		paramsJson['workInstanceId'] = workInstanceId;
		url = "add_like_to_instance.sw";
		console.log(JSON.stringify(paramsJson));
		smartPop.progressCenter();				
		$.ajax({
			url : url,
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				var likesCountShown = subInstanceList.find('.js_likes_count_shown');
				var target = likesCountShown.find(".js_likes_count");
				if(isEmpty(target) || isEmpty(target.html())){
					likesCountShown.html('<li><span class="icon_like"></span><span class="t_blue js_likes_count">1</span>명이 공감합니다.</li>');
				}else{
					target.html(parseInt(target.html()) + 1);
				}
				input.removeClass('js_add_sera_like').addClass('js_remove_sera_like').html('공감취소');
				smartPop.closeProgress();
			},
			error : function(e) {
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.ERROR, "좋아요 추가에 오류가 발생하였습니다. 관리자에게 문의하시기 바랍니다.", function(){
				});
			}			
		});
		return false;
	});
	
	$('a.js_remove_sera_like').live('click', function(e){
		var input = $(e.target);
		var subInstanceList = input.parents('.js_sub_instance_list');
		var	workInstanceId = subInstanceList.attr('instanceId');
		var	workType = subInstanceList.attr('workType');
		var paramsJson = {};
		paramsJson['workType'] = parseInt(workType);
		paramsJson['workInstanceId'] = workInstanceId;
		url = "remove_like_from_instance.sw";
		console.log(JSON.stringify(paramsJson));
		smartPop.progressCenter();				
		$.ajax({
			url : url,
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				var likesCountShown = subInstanceList.find('.js_likes_count_shown');
				var target = likesCountShown.find(".js_likes_count");
				if(isEmpty(target) || isEmpty(target.html())){
				}else{
					var likesCount = parseInt(target.html());
					if(likesCount<=1){
						target.parent().remove();
					}else{
						target.html(likesCount - 1);
					}
				}
				input.addClass('js_add_sera_like').removeClass('js_remove_sera_like').html('공감하기');
				smartPop.closeProgress();
			},
			error : function(e) {
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.ERROR, "좋아요 추가에 오류가 발생하였습니다. 관리자에게 문의하시기 바랍니다.", function(){
				});
			}			
		});
		return false;
	});
	
	$('.js_accept_friend_btn').live('click', function(e){
		var input = $(e.target).parents('.js_accept_friend_btn');
		var friendRequest = input.parents('.js_friend_request_item');
		var requesterCount = input.parents('.js_friend_page').find('.js_requester_count');
		var userId = friendRequest.attr('userId');
		var paramsJson = {};
		paramsJson['userId'] = userId;
		paramsJson['accepted'] = true;
		console.log(JSON.stringify(paramsJson));
		smartPop.progressCenter();				
		$.ajax({
			url : 'reply_friend_request.sw',
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				friendRequest.remove();
				var count = requesterCount.attr('count');
				if(!isEmpty(count) && (count!=='0')){
					count = parseInt(count)-1;
					requesterCount.html('(' + (count) + ')');
					requesterCount.attr('count', count);
				}
				smartPop.closeProgress();
			},
			error : function(e) {
				smartPop.closeProgress();
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.showInfo(smartPop.ERROR, "친구요청 수락에 오류가 발생하였습니다. 관리자에게 문의하시기 바랍니다.", function(){
				});	
			}			
		});
		return false;
	});

	$('.js_deny_friend_btn').live('click', function(e){
		var input = $(e.target).parents('.js_deny_friend_btn');
		var friendRequest = input.parents('.js_friend_request_item');
		var requesterCount = input.parents('.js_friend_page').find('.js_requester_count');
		var userId = friendRequest.attr('userId');
		var paramsJson = {};
		paramsJson['userId'] = userId;
		paramsJson['accepted'] = false;
		console.log(JSON.stringify(paramsJson));
		smartPop.progressCenter();				
		$.ajax({
			url : 'reply_friend_request.sw',
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				friendRequest.remove();
				var count = requesterCount.attr('count');
				if(!isEmpty(count) && (count!=='0')){
					count = parseInt(count)-1;
					requesterCount.html('(' + (count) + ')');
					requesterCount.attr('count', count);
				}
				smartPop.closeProgress();
			},
			error : function(e) {
				smartPop.closeProgress();
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.showInfo(smartPop.ERROR, "친구요청 나중에확인에 오류가 발생하였습니다. 관리자에게 문의하시기 바랍니다.", function(){
				});				
			}
		});
		return false;
	});

	$('.js_destroy_friendship_btn').live('click', function(e){
		var input = $(e.target).parents('.js_destroy_friendship_btn');
		var friend = input.parents('.js_friend_item');
		var friendCount = input.parents('.js_friend_page').find('.js_friend_count');
		var userId = friend.attr('userId');
		var paramsJson = {};
		paramsJson['userId'] = userId;
		console.log(JSON.stringify(paramsJson));
		smartPop.progressCenter();				
		$.ajax({
			url : 'destroy_friendship.sw',
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				var friendPage = input.parents('.js_friend_page');
				if(!isEmpty(friendPage)){
					friend.remove();
					var count = friendCount.html();
					if(!isEmpty(count) && (count!=='0')){
						count = parseInt(count)-1;
						friendCount.html(count);
					}
				}else{
					input.hide().siblings().show();
				}
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.INFO, "친구끊기가 성공적으로 이루어 졌습니다.", function(){
				});
			},
			error : function(e) {
				smartPop.closeProgress();
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.showInfo(smartPop.ERROR, "친구끊기에 오류가 발생하였습니다. 관리자에게 문의하시기 바랍니다.", function(){
				});				
			}
		});
		return false;
	});

	$('.js_friend_request_btn').live('click', function(e){
		var input = $(e.target).parents('.js_friend_request_btn');
		var userId = input.attr('userId');
		var paramsJson = {};
		paramsJson['userId'] = userId;
		console.log(JSON.stringify(paramsJson));
		smartPop.progressCenter();
		$.ajax({
			url : 'friend_request.sw',
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.INFO, "친구요청이 성공적으로 이루어 졌습니다.", function(){
				});				
				input.hide().siblings().show();
			},
			error : function(e) {
				smartPop.closeProgress();
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.showInfo(smartPop.ERROR, "친구요청에 오류가 발생하였습니다. 관리자에게 문의하시기 바랍니다.", function(){
				});				
			}
		});
		return false;
	});

	$('.js_select_course_btn').live('click', function(e) {
		var input = $(e.target).parents('.js_select_course_btn');
		var courseType = input.attr('courseType');
		var categoryName = (courseType === "14") ? input.find('a').html() : "";
		
		smartPop.progressCenter();				
		$.ajax({
			url : "course_by_type.sw",
			data : {
				courseType: courseType,
				categoryName: categoryName
			},
			success : function(data, status, jqXHR) {
				var target = input.parents('.js_course_page').find('.js_course_list');
				target.html(data);
				input.siblings().removeClass('selected');
				input.addClass('selected');
				smartPop.closeProgress();
			},
			error : function(e) {
				smartPop.closeProgress();
			}			
		});
		return false;
	});
	
	$('.js_more_sera_instances_btn').live('click', function(e) {
		var input = $(e.target).parents('.js_more_sera_instances_btn');
		var instanceType = input.attr('instanceType');
		var userId = input.attr('userId');
		var courseId = input.attr('courseId');
		var missionId = input.attr('missoinId');
		var lastDate = input.attr('lastDate');
		smartPop.progressCont(input.find('.js_progress_span'));
		$.ajax({
			url : "seraInstances.sw",
			data : {
				instanceType: instanceType,
				userId: userId,
				courseId: courseId,
				missionId: missionId,
				lastDate: lastDate
			},
			success : function(data, status, jqXHR) {
				input.parent().append(data);
				input.remove();
				smartPop.closeProgress();
			},
			error : function(e) {
				smartPop.closeProgress();
			}			
		});
		return false;
	});
	
	$('.js_more_friends_btn').live('click', function(e) {
		var input = $(e.target).parents('.js_more_friends_btn');
		var userId = input.attr('userId');
		var lastId = input.attr('lastId');
		smartPop.progressCont(input.find('.js_progress_span'));
		$.ajax({
			url : "moreFriends.sw",
			data : {
				userId: userId,
				lastId: lastId
			},
			success : function(data, status, jqXHR) {
				var friendPage = input.parents('.js_friend_page');
				var othersFriendPage = input.parents('.js_others_friend_page');
				if(!isEmpty(friendPage)){
					friendPage.find('.js_friend_list').append(data);					
				}else if(!isEmpty(othersFriendPage)){
					othersFriendPage.find('.js_friend_list').append(data);					
				}
				input.remove();
				smartPop.closeProgress();
			},
			error : function(e) {
				smartPop.closeProgress();
			}			
		});
		return false;
	});
	
	$('.js_more_course_by_type').live('click', function(e) {
		var input = $(e.target).parents('.js_more_course_by_type');
		var courseType = input.attr('courseType');
		var categoryName = (courseType === "14") ? input.attr('categoryName') : "";
		var lastId = input.attr('lastId');
		if(isEmpty(lastId)) lastId = "";
		smartPop.progressCont(input.find('.js_progress_span'));
		$.ajax({
			url : "moreCourses.sw",
			data : {
				courseType: courseType,
				categoryName: categoryName,
				lastId: lastId
			},
			success : function(data, status, jqXHR) {
				input.parents('.js_course_by_type_list').append(data);
				input.remove();
				smartPop.closeProgress();
			},
			error : function(e) {
				smartPop.closeProgress();
			}			
		});
		return false;
	});
	
	$('a.js_coursememberpicker_button').live('click', function(e) {
		var input = $(e.target).parents('.js_coursememberpicker_button');
		var userField = $(e.target).parents('.js_type_userField:first');
		var communityItems = userField.find('.js_community_item');
		var target = userField.find('.js_community_popup:first');
		var width = userField.find('.form_value').find('div:first').width();
		var isMultiUsers = userField.attr('multiUsers');
		var courseId = input.attr('courseId');
		smartPop.selectCourseMember(communityItems, target, width, isMultiUsers, courseId);
		return false;
	});

	$('.js_more_mentee_informs_btn').live('click', function(e) {
		var input = $(e.target).parents('.js_more_mentee_informs_btn');
		var requestType = input.attr('requestType');
		var courseId = input.attr('courseId');
		var lastId = input.attr('lastId');
		smartPop.progressCont(input.find('.js_progress_span'));
		$.ajax({
			url : "moreMenteeInforms.sw",
			data : {
				type: requestType,
				courseId: courseId,
				lastId: lastId
			},
			success : function(data, status, jqXHR) {
				if(requestType==="1")
					input.parents('.js_join_requesters_list').append(data);
				else if(requestType==="2")
					input.parents('.js_course_mentees_list').append(data);
				if(requestType==="3")
					input.parents('.js_course_non_mentees_list').append(data);
				input.remove();
				smartPop.closeProgress();
			},
			error : function(e) {
				smartPop.closeProgress();
			}			
		});
		return false;
	});
	
	$('.js_approve_join_btn').live('click', function(e){
		var input = $(e.target).parents('.js_join_requester_item');
		var courseId = input.attr('courseId');
		var userId = input.attr('userId');
		var paramsJson = {};
		paramsJson['groupId'] = courseId;
		paramsJson['userId'] = userId;
		paramsJson['approval'] = true;
		smartPop.progressCenter();				
		$.ajax({
			url : 'approval_join_group.sw',
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.INFO, "가입승인 수락이 정상적으로 이루어 졌습니다.", function(){
				});				
				input.remove();
			},
			error : function(e) {
				smartPop.closeProgress();
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.showInfo(smartPop.ERROR, "가입승인 수락에 오류가 발생하였습니다. 관리자에게 문의하시기 바랍니다.", function(){
				});				
			}
		});
		return false;
	});

	$('.js_reject_join_btn').live('click', function(e){
		var input = $(e.target).parents('.js_join_requester_item');
		var courseId = input.attr('courseId');
		var userId = input.attr('userId');
		var paramsJson = {};
		paramsJson['groupId'] = courseId;
		paramsJson['userId'] = userId;
		paramsJson['approval'] = false;
		smartPop.progressCenter();				
		$.ajax({
			url : 'approval_join_group.sw',
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.INFO, "가입승인 거절이 정상적으로 이루어 졌습니다.", function(){
				});				
				input.remove();
			},
			error : function(e) {
				smartPop.closeProgress();
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.showInfo(smartPop.ERROR, "가입승인 거절에 오류가 발생하였습니다. 관리자에게 문의하시기 바랍니다.", function(){
				});				
			}
		});
		return false;
	});

	$('.js_pushout_mentee_btn').live('click', function(e){
		var input = $(e.target).parents('.js_mentee_item');
		var courseId = input.attr('courseId');
		var userId = input.attr('userId');
		var paramsJson = {};
		paramsJson['groupId'] = courseId;
		paramsJson['userId'] = userId;
		smartPop.progressCenter();				
		$.ajax({
			url : 'pushout_group_member.sw',
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.INFO, "멘티강퇴가 정상적으로 이루어 졌습니다.", function(){
				});				
				input.remove();
			},
			error : function(e) {
				smartPop.closeProgress();
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.showInfo(smartPop.ERROR, "멘티강퇴에 오류가 발생하였습니다. 관리자에게 문의하시기 바랍니다.", function(){
				});				
			}
		});
		return false;
	});

	$('.js_invite_mentee_btn').live('click', function(e){
		var input = $(e.target).parents('.js_non_mentee_item');
		var courseId = input.attr('courseId');
		var userId = input.attr('userId');
		var paramsJson = {};
		paramsJson['groupId'] = courseId;
		var users = new Array();
		users.push({userId: userId});
		paramsJson['users'] = users;
		
		smartPop.progressCenter();				
		$.ajax({
			url : 'invite_group_members.sw',
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.INFO, "멘티초대가 정상적으로 이루어 졌습니다.", function(){
				});				
				input.remove();
			},
			error : function(e) {
				smartPop.closeProgress();
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.showInfo(smartPop.ERROR, "멘티초대에 오류가 발생하였습니다. 관리자에게 문의하시기 바랍니다.", function(){
				});				
			}
		});
		return false;
	});

	$('.js_toggle_course_setting_btn').live('click', function(e){
		$(e.target).parent().next().toggle();
		return false;
	});

	$('.js_leave_course_btn').live('click', function(e){
		smartPop.confirm('코스를 탈퇴하시려고 합니다. 정말로 탈퇴하시겠습니까??', function(){
			var input = $(e.target);
			var courseSetting = input.parents('.js_course_setting_page');
			var courseId = courseSetting.attr('courseId');
			var leaveReason = courseSetting.find('textarea[name="txtaLeaveReason"]').attr('value');
			var paramsJson = {};
			paramsJson['groupId'] = courseId;
			paramsJson['leaveReason'] = leaveReason;
			console.log(JSON.stringify(paramsJson));
			smartPop.progressCenter();				
			$.ajax({
				url : 'leave_group.sw',
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.INFO, "코스탈퇴가 성공적으로 이루어졌습니다!", function(){
						document.location.href = "myPAGE.sw";											
					});
				},
				error : function(e) {
					smartPop.closeProgress();
					// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
					smartPop.showInfo(smartPop.ERROR, "코스탈퇴에 오류가 발생하였습니다. 관리자에게 문의하시기 바랍니다.", function(){
					});				
				}
			});
		});
		return false;
	});

	$('.js_defective_report_btn').live('click', function(e){
		var input = $(e.target);
		var courseSetting = input.parents('.js_course_setting_page');
		var courseId = courseSetting.attr('courseId');
		var defectiveReport = courseSetting.find('textarea[name="txtaDefectiveReport"]').attr('value');
		if(isEmpty(defectiveReport)){
			smartPop.showInfo(smartPop.WARN, "코스신고 내용을 입력하시기 바랍니다.");
			return false;
		}
		var paramsJson = {};
		paramsJson['groupId'] = courseId;
		paramsJson['defectiveReport'] = defectiveReport;
		console.log(JSON.stringify(paramsJson));
		smartPop.progressCenter();				
		$.ajax({
			url : 'defective_course_report.sw',
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.INFO, "코스신고가 성공적으로 이루어졌습니다!", function(){
					document.location.href = "courseHome.sw?courseId=" + courseId;											
				});
			},
			error : function(e) {
				smartPop.closeProgress();
				// 서비스 에러시에는 메시지를 보여주고 현재페이지에 그래도 있는다...
				smartPop.showInfo(smartPop.ERROR, "코스신고에 오류가 발생하였습니다. 관리자에게 문의하시기 바랍니다.", function(){
				});				
			}
		});
		return false;
	});

	$('.js_toggle_mission_btn').live('click', function(e){
		var input = $(e.target).parent();
		if(input.hasClass('icon_close_red')){
			input.removeClass('icon_close_red').addClass('icon_open_red');
		}else{
			input.addClass('icon_close_red').removeClass('icon_open_red');			
		}
		input.parents('.js_perform_mission_page').find('.js_mission_content_item').toggle();
		return false;
	});

});
