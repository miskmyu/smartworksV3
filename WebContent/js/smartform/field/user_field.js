SmartWorks.FormRuntime = SmartWorks.FormRuntime || {};

SmartWorks.FormRuntime.UserFieldBuilder = {};

SmartWorks.FormRuntime.UserFieldBuilder.build = function(config) {
	var options = {
		mode : 'edit', // view or edit
		container : $('<td></td>'),
		entity : null,
		dataField : '',
		courseId : null,
		friendOnly : false,
		emailAddress : false,
		refreshData : false,
		layoutInstance : null
	};

	SmartWorks.extend(options, config);
	if(!options.refreshData)
		options.container.html('');

	var users = (options.dataField && options.dataField.users) || new Array();
	var usersHtml = (options.dataField && options.dataField.usersHtml) || "";
	var $entity = options.entity;
	var $graphic = $entity.find('graphic');
	var readOnly = $graphic.attr('readOnly') === 'true' || options.mode === 'view';
	console.log('users=', users);
	var multiUsers = $graphic.attr('multipleUsers');
	options.container.attr('multiUsers', multiUsers);
	var id = $entity.attr('id');
	var name = $entity.attr('name');
	
	var labelWidth = (isEmpty(options.layoutInstance)) ? parseInt($graphic.attr('labelWidth')) : options.layoutInstance.getLabelWidth(id);
	var valueWidth = 100 - labelWidth;
	var $label = $('<div class="form_label" style="width:' + labelWidth + '%"><span>' + name + '</span></div>');
	var required = $entity.attr('required');
	if(required === 'true' && !readOnly){
		$label.addClass('required_label');
		required = " class='fieldline community_names js_community_names sw_required'";
	}else{
		required = " class='fieldline community_names js_community_names'";
	}
	if(!options.refreshData)
		$label.appendTo(options.container);
	
	var $user = null;
	
	var href = "user_name.sw";
	if(!isEmpty(options.courseId))
		href = "course_member.sw?courseId=" + options.courseId;
	else if(options.friendOnly)
		href = "sera_user.sw";
	else if(options.emailAddress)
		href = "email_address.sw";
	
	var icoClass = ' class="icon_fb_user"';
	var userPicker = 'class="js_userpicker_button"';
	if(!isEmpty(options.courseId))
		userPicker = 'class="js_coursememberpicker_button" courseId="' + options.courseId + '"';
	else if(options.friendOnly)
		userPicker = 'class="js_friendpicker_button"';
	else if(options.emailAddress)
		userPicker = 'class="js_emailpicker_button"';
	
	if(multiUsers === 'true'){
		if(!isEmpty(users) && isEmpty(usersHtml)){
			for(var i=0; i<users.length; i++)
				usersHtml = usersHtml +  "<span class='js_community_item user_select' comId='" + users[i].userId + "'>" + users[i].longName + "<a class='js_remove_community' href=''>&nbsp;x</a></span>";		
		}
		href = "community_name.sw";
		if(!isEmpty(options.courseId))
			href =  "course_member.sw?courseId=" + options.courseId;
		else if(options.emailAddress)
			href = "email_address.sw";
		icoClass = ' class="icon_fb_users"';
	}else if (!isEmpty(users) && isEmpty(usersHtml)) {
		usersHtml = "<span class='js_community_item user_select' comId='" + users[0].userId + "'>" + users[0].longName + "<a class='js_remove_community' href=''> x</a></span>";
	}

	var $html = $('<div class="form_value" style="width:' + valueWidth + '%"> <div class="icon_fb_space">\
					<div ' + required + '>\
						' + usersHtml + '\
						<input class="m0 js_auto_complete" style="width:100px" href="' + href + '" type="text">\
					</div>\
					<div class="js_community_list srch_list_nowid" style="display: none"></div><span class="js_community_popup"></span><a href=""' + userPicker + '><span ' + icoClass + '></span></a></div></div>');

	if(readOnly){
		$user = $('<div class="form_value" style="width:' + valueWidth + '%"></div>');
		var viewUsersHtml = '';
		for(var i=0; i<users.length; i++) {
			var separator = ', ';
			var href = '';
			if(isEmailAddress(users[i].userId)){
				href = 'user_space.sw?cid=us.sp.';
			}else if(users[i].userId.substring(0,6) === "group_"){
				href = 'group_space.sw?cid=gp.sp.';
			}else if(users[i].userId.substring(0,5) === "dept_"){
				href = 'department_space.sw?cid=dp.sp.';
			}
			href = href + users[i].userId + '&wid=' + users[i].userId;
			if(i == users.length - 1)
				separator = '';
			viewUsersHtml = viewUsersHtml + '<a href="' + href + '"><span>' + users[i].longName + separator + '</span></a>';
		}
		$user.html(viewUsersHtml);
	}else{	
		$user = $html;
	}
	if ($graphic.attr('hidden') == 'true'){
		$label.hide();
		$user.hide();		
	}
	if(!options.refreshData)
		$user.appendTo(options.container);
	else
		options.container.find('.form_value').html($user.children());

	if (readOnly)
		options.container.append($('<div style="display:none"></div>').html(usersHtml));
	
	return options.container;
};

SmartWorks.FormRuntime.UserFieldBuilder.buildEx = function(config){
	var options = {
			container : $('<tr></tr>'),
			fieldId: '',
			fieldName: '',
			users: new Array(), //{userId: '',longName: '}
			columns: 1,
			colSpan: 1,
			multiUsers: false,
			courseId: null,
			friendOnly: false,
			emailAddress: false,
			usersHtml: "",
			required: false,
			readOnly: false		
	};
	SmartWorks.extend(options, config);

	var labelWidth = 12;
	if(options.columns >= 1 && options.columns <= 4 && options.colSpan <= options.columns) labelWidth = 12 * options.columns/options.colSpan;
	$formEntity =  $($.parseXML('<formEntity id="' + options.fieldId + '" name="' + options.fieldName + '" systemType="string" required="' + options.required + '" system="false">' +
						'<format type="userField" viewingType="userField"/>' +
					    '<graphic hidden="false" readOnly="'+ options.readOnly +'" labelWidth="'+ labelWidth + '" multipleUsers="' + options.multiUsers+ '"/>' +
					'</formEntity>')).find('formEntity');
	var $formCol = $('<td class="form_col js_type_userField" fieldid="' + options.fieldId+ '" colspan="' + options.colSpan + '" width="' + options.colSpan/options.columns*100 + '%" rowspan="1">');
	$formCol.appendTo(options.container);
	SmartWorks.FormRuntime.UserFieldBuilder.build({
			mode : options.readOnly, // view or edit
			container : $formCol,
			entity : $formEntity,
			courseId: options.courseId,
			friendOnly: options.friendOnly,
			emailAddress: options.emailAddress,
			dataField : SmartWorks.FormRuntime.UserFieldBuilder.dataField({
				fieldId: options.fieldId,
				users : options.users,
				usersHtml : options.usersHtml
			})
	});
	
};

SmartWorks.FormRuntime.UserFieldBuilder.serializeObject = function(userFields){
	var usersJson = {};
	for(var i=0; i<userFields.length; i++){
		var userField = $(userFields[i]);
		var fieldId = userField.attr('fieldId');
		var userList = userField.find('.js_community_item');
		var users = new Array();
		for(var j=0; j<userList.length; j++)
			users.push({
				id : $(userList[j]).attr('comId'),
				name : userList[j].childNodes[0].nodeValue
			});
		usersJson[fieldId] =  {users: users};
	}
	return usersJson;
};

SmartWorks.FormRuntime.UserFieldBuilder.validate = function(userFields){
	var usersValid = true;
	for(var i=0; i<userFields.length; i++){
		var userField = $(userFields[i]);
		var userId = userField.find('.js_community_item:first').attr('comId');
		var required = userField.find('div.sw_required');
		if(!isEmpty(required) && isBlank(userId)){
			userField.find('div.sw_required').addClass("sw_error");
			usersValid = false;
		}
	}
	return usersValid;
};

SmartWorks.FormRuntime.UserFieldBuilder.dataField = function(config){
	var options = {
			fieldName: '',
			formXml: '',
			fieldId: '',
			users: new Array(), //{userId: '',longName: ''}
			usersHtml: ""
	};

	SmartWorks.extend(options, config);
	$formXml = isEmpty(options.formXml) ? [] : $($.parseXML(options.formXml)).find('form');
	var dataField = {};
	
	var fieldId = (isEmpty(options.fieldId)) ? $formXml.find('formEntity[name="'+options.fieldName+'"]').attr('id') : options.fieldId;
	if(isEmpty(fieldId)) fieldId = ($formXml.attr("name") === options.fieldName) ? $formXml.attr('id') : "";
	if(isEmpty(fieldId)) return dataField;
	dataField = {
			id: fieldId,
			users : options.users,
			usersHtml : options.usersHtml
	};
	return dataField;
};
