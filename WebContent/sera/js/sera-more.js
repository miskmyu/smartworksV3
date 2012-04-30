
	$(window).scroll( function() {
		var moreSeraInstances = $('.js_sera_instance_item .js_more_sera_instances_btn');
		var moreFriends = $('.js_friend_page .js_more_friend_informs_btn');
		var moreOthersFriends = $('.js_others_friend_page .js_more_friend_informs_btn');
		var moreCourseMembers = $('.js_invite_course_member_page .js_more_mentee_informs_btn');
		var moreCourseMentees = $('.js_course_setting_mentee_page .js_more_mentee_informs_btn');
		var moreCourseReviews = $('.js_course_general_page .js_more_course_reviews_btn');
		if ($(window).scrollTop() == $(document).height() - $(window).height()){
			if(!isEmpty(moreSeraInstances) && !moreSeraInstances.isWaiting){
				moreSeraInstances.isWaiting = true;
				setTimeout(function() {
					if ($(window).scrollTop() == $(document).height() - $(window).height())
						moreSeraInstances.find('span').trigger('click');
					moreSeraInstances.isWaiting = false;
				}, 3000);
			}else if(!isEmpty(moreFriends) && !moreFriends.isWaiting){
				moreFriends.isWaiting = true;
				setTimeout(function() {
					if ($(window).scrollTop() == $(document).height() - $(window).height())
						moreFriends.find('span').trigger('click');
					moreFriends.isWaiting = false;
				}, 3000);
			}else if(!isEmpty(moreOthersFriends) && !moreOthersFriends.isWaiting){
				moreOthersFriends.isWaiting = true;
				setTimeout(function() {
					if ($(window).scrollTop() == $(document).height() - $(window).height())
						moreOthersFriends.find('span').trigger('click');
					moreOthersFriends.isWaiting = false;
				}, 3000);
			}else if(!isEmpty(moreCourseMembers) && !moreCourseMembers.isWaiting){
				moreCourseMembers.isWaiting = true;
				setTimeout(function() {
					if ($(window).scrollTop() == $(document).height() - $(window).height())
						moreCourseMembers.find('span').trigger('click');
					moreCourseMembers.isWaiting = false;
				}, 3000);
			}else if(!isEmpty(moreCourseMentees) && !moreCourseMentees.isWaiting){
				moreCourseMentees.isWaiting = true;
				setTimeout(function() {
					if ($(window).scrollTop() == $(document).height() - $(window).height())
						moreCourseMentees.find('span').trigger('click');
					moreCourseMentees.isWaiting = false;
				}, 3000);
			}else if(!isEmpty(moreCourseReviews) && !moreCourseReviews.isWaiting){
				moreCourseReviews.isWaiting = true;
				setTimeout(function() {
					if ($(window).scrollTop() == $(document).height() - $(window).height())
						moreCourseReviews.find('span').trigger('click');
					moreCourseReviews.isWaiting = false;
				}, 3000);
			}
		}
	});
