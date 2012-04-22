
	$(window).scroll( function() {
		var moreSeraInstances = $('.js_sera_instance_item .js_more_sera_instances_btn');
		var moreFriends = $('.js_friend_page .js_more_friends_btn');
		var moreOthersFriends = $('.js_others_friend_page .js_more_friends_btn');
		if ($(window).scrollTop() == $(document).height() - $(window).height()){
			alert('scrollin');
			if(!isEmpty(moreSeraInstances) && !moreSeraInstances.isWaiting){
				moreSeraInstances.isWaiting = true;
				setTimeout(function() {
					if ($(window).scrollTop() == $(document).height() - $(window).height())
						moreSeraInstances.trigger('click');
					moreSeraInstances.isWaiting = false;
				}, 2000);
			}else if(!isEmpty(moreFriends) && !moreFriends.isWaiting){
				alert('in');
				moreFriends.isWaiting = true;
				setTimeout(function() {
					alert('timerin');
					if ($(window).scrollTop() == $(document).height() - $(window).height())
						moreFriends.find('span').trigger('click');
					moreFriends.isWaiting = false;
				}, 2000);
			}else if(!isEmpty(moreOthersFriends) && !moreOthersFriends.isWaiting){
				moreOthersFriends.isWaiting = true;
				setTimeout(function() {
					if ($(window).scrollTop() == $(document).height() - $(window).height())
						moreOthersFriends.find('span').trigger('click');
					moreOthersFriends.isWaiting = false;
				}, 2000);
			}
		}
	});
