$(function() {
	
	/*
	 * 어디에서든 class 값이 js_container로 지정된 anchor 가 선택이 되면, anchor 의 href 값으로 ajax 를 호출하여
	 * 가져온 값을 container(메인컨텐트)화면에 보여준다.
	 */
	$('.js_sera_content').swnavi({
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
		if(pos==0){
			$.ajax({
				url : "courseGeneral.sw",
				data : {courseId : courseId},
				success : function(data, status, jqXHR) {
					$('.js_course_content').html(data);
				}
			});
		}else if(pos==1){
			$('.js_course_mission_menu li.js_course_mission_set a').click();
		}else if(pos==2){
			$.ajax({
				url : "courseBoard.sw",
				data : {courseId : courseId},
				success : function(data, status, jqXHR) {
					$('.js_course_content').html(data);
				}
			});
		}else if(pos==3){
			if(isEmpty($(subMenus[pos]).children())){
				$.ajax({
					url : "courseSetting.sw",
					data : {courseId : courseId},
					success : function(data, status, jqXHR) {
						$('.js_course_content').html(data);
					}
				});
				
			}else{
				$('.js_course_setting_menu li.js_course_setting_profile a').click();
			}
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
		}else if(input.hasClass('js_course_setting_mentee')){
			url = "courseSettingMentee.sw";
		}else if(input.hasClass('js_course_setting_team')){
			url = "courseSettingTeam.sw";
		}
		$.ajax({
			url : url,
			data : {courseId : courseId},
			success : function(data, status, jqXHR) {
				$('.js_course_content').html(data);
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
		if(input.hasClass('js_course_mission_set')){
			url = "courseMissionSet.sw";
		}else if(input.hasClass('js_course_mission_list')){
			url = "courseMissionList.sw";
		}else if(input.hasClass('js_course_mission_mine')){
			url = "courseMissionMine.sw";
		}
		$.ajax({
			url : url,
			data : {courseId : courseId},
			success : function(data, status, jqXHR) {
				$('.js_course_content').html(data);
			}
		});
		return false;
	});
});
