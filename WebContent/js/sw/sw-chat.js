function updateChattingBoxTitle(chatId, chatterInfos) {
	var title = "";
	var chatterList = new Array();
	if (!isEmpty(chatterInfos)) {
		for ( var i = 0, j = 0; i < chatterInfos.length; i++) {
			if (chatterInfos[i].userId !== currentUser.userId)
				chatterList[j++] = chatterInfos[i];
		}
		if (!isEmpty(chatterList)) {
			title = chatterList[0].longName;
			if (chatterList.length > 2) {
				title = title + smartMessage.get('chatUserAnd') + (chatterList.length - 1) + smartMessage.get('chatUserMore');
			} else {
				for ( var i = 1; i < chatterList.length; i++) {
					title = title + "," + chatterList[i].longName;
				}
			}
		} else {
			title = smartMessage.get('chatNoChatters');
		}
	}
	$('#' + chatId).find('div.js_chatting_title').html(title);

}

function fetchAllasyncMessages(chatId){

	$.ajax({
		url : "fetch_async_messages_by_chatid.sw",
		data : {
			chatId : chatId,
			receiverId : currentUser.userId
		},
		success : function(data, status, jqXHR) {
			console.log('fetched messages =', data.messages);
			if(!isEmpty(data.messages)){
				for(var i=0; i<data.messages.length; i++){
					var message = {};
					message['chatId'] = data.messages[i].chatId;
					message['chatMessage'] = data.messages[i].chatMessage;
					message['msgType'] = msgType.CHAT_MESSAGE;
					var senderInfo = {};
					senderInfo['userId'] = data.messages[i].senderInfo.userId;
					senderInfo['longName'] = data.messages[i].senderInfo.longName;
					senderInfo['nickName'] = data.messages[i].senderInfo.nickName;
					senderInfo['minPicture'] = data.messages[i].senderInfo.minPicture;
					message['senderInfo'] = senderInfo;
					message['sendDate'] = (new Date((new Date(data.messages[i].lastModifiedDate)).getTime() + currentUser.timeOffset*1000*60*60));
					receivedMessageOnChatId(message);
					chatHistory.setHistory(chatId, message);
				}
			}
		},
		error : function(xhr, ajaxOptions, thrownError){
		}
	});		
};



var chattingBoxPadding = 3;

function getGroupPrevWidth(){
	var width = 0;
	var groupPrev = $('div.js_chatting_group_prev');
	if(groupPrev.css("display")==="none")
		return 0;
	else
		return (groupPrev.width() + chattingBoxPadding);
}

function setRightPosition(type, chattingBox){
	if(type==="new" && chattingBox!=null){
		var prevElements = chattingBox.prevAll('div.js_chatting_box');
		var prevElementsWidth = $('div.js_chatter_list').width() + chattingBoxPadding;
		if(isEmpty(prevElements)){
			chattingBox.css({"right" : + prevElementsWidth + "px"});
		}else{
			prevElementsWidth = prevElementsWidth + getGroupPrevWidth();
			for(var i=0; i<prevElements.length; i++){
				prevElementsWidth = prevElementsWidth + $(prevElements[i]).width() + chattingBoxPadding;
			}
			chattingBox.css({"right" : + prevElementsWidth + "px"});
			prevElementsWidth = prevElementsWidth + chattingBox.width() + chattingBoxPadding;
			$('div.js_chatting_group_next').css({"right" : + prevElementsWidth + "px"});
		}
	}else if(type==="resize" && chattingBox!=null){
		var prevElements = chattingBox.prevAll('div.js_chatting_box');
		var prevElementsWidth = $('div.js_chatter_list').width() + chattingBoxPadding;
		prevElementsWidth = prevElementsWidth + getGroupPrevWidth();
		for(var i=0; i<prevElements.length; i++)
			prevElementsWidth = prevElementsWidth + $(prevElements[i]).width() + chattingBoxPadding;
		prevElementsWidth = prevElementsWidth + chattingBox.width() + chattingBoxPadding;
		var nextElements = chattingBox.nextAll('div.js_chatting_box');
		for(var i=0; i<nextElements.length; i++){
			$(nextElements[i]).css({"right" : + prevElementsWidth + "px"});
			prevElementsWidth = prevElementsWidth + $(nextElements[i]).width() + chattingBoxPadding;
		}
		$('div.js_chatting_group_next').css({"right" : + prevElementsWidth + "px"});
	}else if(type==="remove" && chattingBox!=null){
		var prevElements = chattingBox.prevAll('div.js_chatting_box');
		var prevElementsWidth = $('div.js_chatter_list').width() + chattingBoxPadding;
		prevElementsWidth = prevElementsWidth + getGroupPrevWidth();
		for(var i=0; i<prevElements.length; i++)
			prevElementsWidth = prevElementsWidth + $(prevElements[i]).width() + chattingBoxPadding;
		var nextElements = chattingBox.nextAll('div.js_chatting_box');
		for(var i=0; i<nextElements.length; i++){
			$(nextElements[i]).css({"right" : + prevElementsWidth + "px"});
			prevElementsWidth = prevElementsWidth + $(nextElements[i]).width() + chattingBoxPadding;
		}
		$('div.js_chatting_group_next').css({"right" : + prevElementsWidth + "px"});		
	}else if(type==="resize" && chattingBox==null){
		var prevElementsWidth = $('div.js_chatter_list').width() + chattingBoxPadding;
		var groupPrev = $('div.js_chatting_group_prev'); 
		groupPrev.css({"right" : + prevElementsWidth + "px"});
		prevElementsWidth = prevElementsWidth + getGroupPrevWidth();
		var nextElements = $('div.js_chatting_box_list').children('div.js_chatting_box');
		for(var i=0; i<nextElements.length; i++){
			$(nextElements[i]).css({"right" : + prevElementsWidth + "px"});
			prevElementsWidth = prevElementsWidth + $(nextElements[i]).width() + chattingBoxPadding;
		}
		$('div.js_chatting_group_next').css({"right" : + prevElementsWidth + "px"});
	}else if(type==="groupPrev" && chattingBox!=null){
		var prevElementsWidth = $('div.js_chatter_list').width() + chattingBoxPadding;
		chattingBox.css({"right" : + prevElementsWidth + "px"});
		prevElementsWidth = prevElementsWidth + getGroupPrevWidth();
		var nextElements = $('div.js_chatting_box_list').children('div.js_chatting_box');
		for(var i=0; i<nextElements.length; i++){
			$(nextElements[i]).css({"right" : + prevElementsWidth + "px"});
			prevElementsWidth = prevElementsWidth + $(nextElements[i]).width() + chattingBoxPadding;
		}
		$('div.js_chatting_group_next').css({"right" : + prevElementsWidth + "px"});
	}else if(type==="groupNext" && chattingBox!=null){
		var prevElementsWidth = $('div.js_chatter_list').width() + chattingBoxPadding;
		var prevElements = $('div.js_chatting_box_list').children('div.js_chatting_box');
		for(var i=0; i<prevElements.length; i++){
			prevElementsWidth = prevElementsWidth + $(prevElements[i]).width() + chattingBoxPadding;
		}
		chattingBox.css({"right" : + prevElementsWidth + "px"});
	}
}
function shiftBoxToGroup(type, chattingBox){
	if(type === "prev"){
		chattingBox.hide();
		var groupPrev = chattingBox.parent().prev('div.js_chatting_group_prev');
		chattingBox.remove();
		groupPrev.append(chattingBox);
		groupPrev.find('span.js_group_prev_count').text(groupPrev.children('div.js_chatting_box').length);
		groupPrev.show();
		setRightPosition("groupPrev", groupPrev);
	}else if(type === "next"){
		chattingBox.hide();
		var groupNext = chattingBox.parent().siblings('div.js_chatting_group_next');
		chattingBox.remove();
		groupNext.append(chattingBox);
		groupNext.find('span.js_group_next_count').text(groupNext.children('div.js_chatting_box').length);
		groupNext.show();
		setRightPosition("groupNext", groupNext);
	}
}

function shiftBoxFromGroup(type, chattingBox){
	if(type === "prev"){
		var groupPrev = $('div.js_chatting_group_prev');
		chattingBox.show();
		chattingBox.remove();
		$('div.js_chatting_box_list').prepend(chattingBox);
		chattingBox.find('div.js_chatting_title_icons').show();
		chattingBox.find('div.js_chatting_body').slideDown(500);
		var prevCount = groupPrev.children('div.js_chatting_box').length;
		groupPrev.find('span.js_group_prev_count').text(prevCount);
		if(prevCount==0){
			groupPrev.hide();
			setRightPosition("groupPrev", groupPrev);
		}
	}else if(type === "next"){
		var groupNext = $('div.js_chatting_group_next');
		chattingBox.show();
		chattingBox.remove();
		$('div.js_chatting_box_list').append(chattingBox);
		chattingBox.find('div.js_chatting_title_icons').show();
		chattingBox.find('div.js_chatting_body').slideDown(500);
		var nextCount = groupNext.children('div.js_chatting_box').length;
		groupNext.find('span.js_group_next_count').text(nextCount);
		if(nextCount==0){
			groupNext.hide();
		}		
	}
}

function startChattingWindow(message) {
	var target = $('div.js_chatting_box_list');
	var chattingBoxs = target.find('div.js_chatting_box');
	console.log('chattingBoxs=', chattingBoxs);
	for(var i=0; i<chattingBoxs.length; i++ )
		if($(chattingBoxs[i]).attr('id') === message.chatId)
			return;
	
	var chatId = message.chatId;
	var chatterInfos = chatManager.chatterInfos(message.chatId);
	$.ajax({
		url : "chatting_box.sw",
		data : {},
		success : function(data, status, jqXHR) {
			if(chattingBoxs.length == 3){
				shiftBoxToGroup("prev", $(chattingBoxs[0]));
			}
			var chattingBox = $(data);
			chattingBox.attr("id", chatId);
			target.append(chattingBox);
			setRightPosition("new", $('#'+chatId));
			updateChattingBoxTitle(chatId, chatterInfos);
			fetchAllasyncMessages(chatId);
			for ( var i = 0; i < chatterInfos.length; i++){
				updateChatterStatus(chatId, chatterInfos[i], chatterInfos[i].status);
			}
			chattingBox.slideDown(1000);
		},
		error : function(xhr, ajaxOptions, thrownError){
			
		}
	});
}

var blinkingOn = new Array();
function isBlinkingOn(chatId){
	for(var i=0; i<blinkingOn.length; i++)
		if(blinkingOn[i] === chatId)
			return true;
	return false;
}
function removeBlinkingOn(chatId){
	for(var i=0; i<blinkingOn.length; i++)
		if(blinkingOn[i] === chatId){
			blinkingOn.splice(i,1);
			return;
		}
}

function receivedMessageOnChatId(message) {
	var chatId = message.chatId;
	var senderInfo = message.senderInfo;
	var chatMessage = message.chatMessage;
	var sendDate = new Date(message.sendDate);
	var target = $('#' + chatId).find('div.js_chatting_message_list');
	var data = "<li>" + 
					"<div class='noti_pic'>" +
						"<img src='" + senderInfo.minPicture + "' class='profile_size_s' title='" + senderInfo.longName+ "'>" + 
					"</div>" + 
					"<div class='noti_in'>" + chatMessage + "<span class='t_date ml3' >" + printDateTime(sendDate) + "</span></div>" +
				"</li>";
	target.find('ul').append(data);
	target[0].scrollTop = target[0].scrollHeight;
	var chattingBox = $('#'+chatId);
	if(chattingBox.find('div.js_chatting_body').css('display') === "none"){
		blinkingOn.push(chatId);
		var repeatBlinking = function(){
			setTimeout(function(){
				chattingBox.fadeTo('slow', 0.2).fadeTo('slow', 1.0);							
				if(isBlinkingOn(chatId)) repeatBlinking();
			}, 3000);			
		};
		repeatBlinking();
	}
}

function writingStatusOnChatId(chatId, sender) {
}

function updateChatterStatus(chatId, chatterInfo, status) {
	if(!chatterInfo) return;
	var userId = chatterInfo.userId;
	var longName = chatterInfo.longName;
	var target = $('#' + chatId).find('div.js_chatter_status_list');
	var statusList = target.children();
	for ( var i = 0; i < statusList.length; i++) {
		if ($(statusList[i]).attr('userId') === userId) {
			$(statusList[i]).remove();
		}
	}
	if (status === userStatus.ONLINE) {
	} else if (status === userStatus.OFFLINE) {
		var data = "<div class='msg_section' userId='" + userId
				+ "'><span class='t_name'>" + longName
				+ "</span>" + smartMessage.get('chatUserOfflineMessage') + "</div>";
		target.append(data);
	}else if(status === userStatus.LEAVED){
		
	}
}
$(function() {
	$('#available_chatter_list a').live('click', function(e) {
		var input = $(targetElement(e)).parents('a');
		if(isEmpty(input)) input = $(targetElement(e));
		var userId = input.attr('userId');
		//var comId = input.attr('comId');
		var img = input.find('img');
		var longName = img.attr('title');
		var minPicture = img.attr('src');
		var nickNameBase = ($('.js_chatter_list_page').attr('nickNameBase') === 'true');
		smartTalk.chattingRequest(new Array({
			userId : currentUserId,
			longName : nickNameBase ? currentUser.nickName : currentUser.longName,
			minPicture : currentUser.minPicture
		}, {
			userId : userId,
			longName : longName,
			minPicture : minPicture
		}));
		input.parents('div.js_chatter_list').find('div.js_chatter_search_area').slideUp(500);
		setTimeout(function(){
			setRightPosition("resize", null);
		}, 600);
		return false;
	});
	
	$('a.js_close_chatting_box').live('click', function(e) {
		var input = $(targetElement(e));
		var target = input.parents('div.js_chatting_box:first');
		var chatId = target.attr('id');
		setRightPosition("remove", target);
		target.remove();
		smartTalk.stopSubOnChatId(chatId);
		return false;
	});
	
	$('a.js_min_chatting_box').live('click',function(e) {
		var input = $(targetElement(e));
		input.parents('div.js_chatting_title_icons:first').hide().parents('div.js_chatting_header').siblings('div.js_chatting_body').slideUp(500);
		var target = input.parents('div.js_chatting_box');
		setTimeout(function(){
			setRightPosition("resize", target);
		}, 600);
		return false;
	});
	
	$('div.js_chatting_header').live('click',function(e) {
		var input = $(targetElement(e)).parents('div.js_chatting_box:first').children('div.js_chatting_header');
		if (input.children('div.js_chatting_title_icons').css("display") === "none") {
			input.children('div.js_chatting_title_icons:first').show().parents('div.js_chatting_header').siblings('div.js_chatting_body').slideDown(500);
			var target = input.parents('div.js_chatting_box');
			setRightPosition("resize", target);

		}
		removeBlinkingOn(input.parents('div.js_chatting_box').attr('id'));
		return false;
	});
	
	$('a.js_admin_chatting_box').live('click',function(e) {
		var input = $(targetElement(e));
		input.parents('div.js_chatting_header:first').siblings('div.js_chatting_body').children('div.js_chatters_search_box').slideDown(500);
		return false;
	});
	
	$('a.js_toggle_chatter_list').live('click',function(e) {
		var input = $(targetElement(e));
		var chatterList = input.parents('div.js_chatter_list');
		var target = chatterList.find('div.js_chatter_search_area');
		var display = target.css('display');
		target.slideToggle(500);
		if (display !== "none") {
			setTimeout(function(){ setRightPosition("resize", null); }, 600);
		}else{
			setRightPosition("resize", null);				
			chatterList.find('input.js_auto_complete').focus();
		}
		return false;
	});
	
	$('div.js_chatting_group_prev a').live('click',function(e) {
		var input = $(targetElement(e)).parents('div.js_chatting_group_prev');
		var lastChattingBox = input.children('div.js_chatting_box:last');
		if(isEmpty(lastChattingBox)) return false;
		var chattingBoxs = $('div.js_chatting_box_list').children('div.js_chatting_box');
		if(chattingBoxs.length==3){
			shiftBoxToGroup("next", $(chattingBoxs[2]));
		}
		shiftBoxFromGroup("prev", lastChattingBox);
		setRightPosition("groupPrev", input);				
		return false;
	});
	
	$('div.js_chatting_group_next a').live('click',function(e) {
		var input = $(targetElement(e)).parents('div.js_chatting_group_next');
		var firstChattingBox = input.children('div.js_chatting_box:first');
		if(isEmpty(firstChattingBox)) return false;
		var chattingBoxs = $('div.js_chatting_box_list').children('div.js_chatting_box');
		if(chattingBoxs.length==3){
			shiftBoxToGroup("prev", $(chattingBoxs[0]));
		}
		shiftBoxFromGroup("next", firstChattingBox);
		setRightPosition("groupNext", input);				
		return false;
	});
	
	$('a.js_add_chatters').live('click',function(e) {
		var input = $(targetElement(e));
		var chatId = input.parents('div.js_chatting_box:first').attr('id');
		var target = input.parents('div.js_chatter_names').find(
				'div.js_selected_chatters');
		var chatterList = target.children('span.js_chatter_item');
		var chatterInfos = new Array();
		for ( var i = 0; i < chatterList.length; i++) {
			var chatter = $(chatterList[i]);
			chatterInfos.push({
				userId : chatter.attr('comId'),
				longName : chatter.attr('comName'),
				minPicture : chatter.attr('minPicture')
			});
		}
		chatterList.remove();
		smartTalk.addJoinChatters(chatId, chatterInfos);
		input.parents('div.js_chatters_search_box').slideUp(500);
		return false;
	});
	
	$('div.js_chat_input textarea').live('keypress', function(e) {
		var e = window.event || e;
		var keyCode = e.which || e.keyCode;
		if (keyCode == $.ui.keyCode.ENTER) {
			var input = $(targetElement(e));
			var chattingBox = input.parents('div.js_chatting_box:first');
			var chatId = chattingBox.attr('id');
			var message = input.attr('value');
			if (!isEmpty(message)) {
				smartTalk.publishChatMessage(chatId, message);
			}
			input.attr('value', '');
			return false;
		}
	});
	
	$('div.js_chat_input textarea').live('focusin', function(e) {
		var input = $(targetElement(e));
		var chatId = input.parents('div.js_chatting_box:first').attr('id');
		smartTalk.publishWritingStatus(chatId);
	});
	
	$('.js_invite_chatter a').live( 'click', function(e) {
		var input = $(targetElement(e));
		var comId = input.attr('userId');
		var comName = input.children('img').attr('title');
		var minPicture = input.children('img').attr('src');
		var target = input.parents('div.js_chatter_list').siblings('div.js_chatter_names').find('div.js_selected_chatters');
		var oldHTML = target.html();
		if (oldHTML == null)
			oldHTML = "";
		var chatterItems = $(target).find('span.js_chatter_item');
		var isSameId = false;
		for ( var i = 0; i < chatterItems.length; i++) {
			var oldComId = $(chatterItems[i]).attr('comId');
			if (oldComId != null && oldComId === comId) {
				isSameId = true;
				break;
			}
		}
		if (!isSameId) {
			var newHTML = oldHTML
					+ "<span class='js_chatter_item user_select' comId='"
					+ comId
					+ "' comName='"
					+ comName
					+ "' minPicture='"
					+ minPicture
					+ "'>"
					+ comName
					+ "<span class='btn_x_gr'><a class='js_remove_chatter' href=''> x</a></span></span>";
			target.html(newHTML);
		}
		target.next().focus();
		return false;
	});
	
	$('.js_remove_chatter').live('click', function(e) {
		var input = $(targetElement(e));
		var selected_users = input.parents('div.js_selected_chatters');
		input.parents('span.js_chatter_item').remove();
		selected_users.next().focus();
		return false;
	});
});