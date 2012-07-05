function loadMyProfileField() {
	var myProfileFields = $('div.js_my_profile_field');
	if(!isEmpty(myProfileFields)) {
		for(var i=0; i<myProfileFields.length; i++) {
			var myProfileField = $(myProfileFields[i]);
			
			var gridRow = SmartWorks.GridLayout.newGridRow();
			var gridTable = SmartWorks.GridLayout.newGridTable();
			myProfileField.html(gridTable.html(gridRow));

			SmartWorks.FormRuntime.ImageBoxBuilder.buildEx({
				container: gridRow,
				fieldId: "imgMyProfile",
				fieldName: "picture profile",
				imgSource: currentUser.orgPicture,
				pictureWidth: 110,
				pictureHeight: 110,
				required: false
			});
		}		
	}
	
	var mySignPictureFields = $('div.js_my_signpic_field');
	if(!isEmpty(mySignPictureFields)) {
		for(var i=0; i<mySignPictureFields.length; i++) {
			var mySignPictureField = $(mySignPictureFields[i]);
			
			var gridRow = SmartWorks.GridLayout.newGridRow();
			var gridTable = SmartWorks.GridLayout.newGridTable();
			mySignPictureField.html(gridTable.html(gridRow));

			SmartWorks.FormRuntime.ImageBoxBuilder.buildEx({
				container: gridRow,
				fieldId: "imgMySignPic",
				fieldName: "picture sign",
				imgSource: currentUser.signPicture,
				pictureWidth: 67,
				pictureHeight: 67,
				required: false
			});
		}		
	}
};

function loadCompanyLogoField() {
	var companyLogoFields = $('div.js_company_logo_field');
	if(!isEmpty(companyLogoFields)) {
		for(var i=0; i<companyLogoFields.length; i++) {
			var companyLogoField = $(companyLogoFields[i]);
			
			var imgSource = companyLogoField.attr('imgSource');
			var gridRow = SmartWorks.GridLayout.newGridRow();
			var gridTable = SmartWorks.GridLayout.newGridTable();
			companyLogoField.html(gridTable.html(gridRow));

			SmartWorks.FormRuntime.ImageBoxBuilder.buildEx({
				container: gridRow,
				fieldId: "imgCompanyLogo",
				fieldName: "picture profile",
				imgSource: imgSource,
				columns: 1,
				pictureWidth: 130,
				pictureHeight: 35,
				required: false
			});
		}		
	}
};

function loadCompanyLoginImageField() {
	var companyLoginImageFields = $('div.js_company_loginimage_field');
	if(!isEmpty(companyLoginImageFields)) {
		for(var i=0; i<companyLoginImageFields.length; i++) {
			var companyLoginImageField = $(companyLoginImageFields[i]);
			
			var imgSource = companyLoginImageField.attr('imgSource');
			var gridRow = SmartWorks.GridLayout.newGridRow();
			var gridTable = SmartWorks.GridLayout.newGridTable();
			companyLoginImageField.html(gridTable.html(gridRow));
			
			SmartWorks.FormRuntime.ImageBoxBuilder.buildEx({
				container: gridRow,
				fieldId: "imgCompanyLoginImage",
				fieldName: "picture profile",
				imgSource: imgSource,
				columns: 1,
				pictureWidth: 130,
				pictureHeight: 60,
				required: false
			});
		}		
	}
};

function loadCheckScheduleFields() {
	var checkScheduleFields = $('div.js_check_schedule_fields');
	if(!isEmpty(checkScheduleFields)) {
		for(var i=0; i<checkScheduleFields.length; i++) {
			var checkScheduleField = $(checkScheduleFields[i]);
			
			var gridRow = SmartWorks.GridLayout.newGridRow();
			var gridTable = SmartWorks.GridLayout.newGridTable();
			checkScheduleField.html(gridTable.html(gridRow));

			var startDateName = checkScheduleField.attr("startDateName");
			var endDateName = checkScheduleField.attr("endDateName");
			var performerName = checkScheduleFields.attr("performerName");
			SmartWorks.FormRuntime.DateTimeChooserBuilder.buildEx({
				container: gridRow,
				fieldId: "txtScheduleStartDate",
				fieldName: startDateName,
				columns: 2,
				colSpan: 1,
				required: true
			});
		  	
			SmartWorks.FormRuntime.DateTimeChooserBuilder.buildEx({
				container: gridRow,
				fieldId: "txtScheduleEndDate",
				fieldName: endDateName,
				columns: 2,
				colSpan: 1,
				required: true
			});
		  	
			gridRow = SmartWorks.GridLayout.newGridRow().appendTo(gridTable);
			SmartWorks.FormRuntime.UserFieldBuilder.buildEx({
				container: gridRow,
				fieldId: "txtSchedulePerformer",
				fieldName: performerName,
				columns: 2,
				colSpan: 1,
				multiUsers: false,
				required: true,
				users: new Array({
					userId: currentUser.userId,
					longName: currentUser.longName
				})
			});
		}		
	}
};

function loadNewPictureFields() {
	var newPictureFields = $('div.js_new_picture_fields');
	if(!isEmpty(newPictureFields)) {
		for(var i=0; i<newPictureFields.length; i++) {
			var newPictureField = $(newPictureFields[i]);
			var gridRow = SmartWorks.GridLayout.newGridRow();
			var gridTable = SmartWorks.GridLayout.newGridTable();
			newPictureField.html(gridTable.html(gridRow));
			
			var pictureDescTitle = newPictureField.attr("pictureDescTitle");

			SmartWorks.FormRuntime.ImageBoxBuilder.buildEx({
				container: gridRow,
				fieldId: "imgPictureFile",
				fieldName: "picture profile",
				columns: 1,
				pictureWidth: 512,
				required: true
			});

			gridRow = SmartWorks.GridLayout.newGridRow().appendTo(gridTable);
			gridRow.hide();
			SmartWorks.FormRuntime.TextInputBuilder.buildEx({
				container: gridRow,
				fieldId: "txtPictureDesc",
				fieldName: pictureDescTitle,
				columns: 1,
				multiLines: 2,
				required: false
			});
		}		
	}
};

function loadNewFileFields() {
	var newFileFields = $('div.js_new_file_fields');
	if(!isEmpty(newFileFields)) {
		for(var i=0; i<newFileFields.length; i++) {
			var newFileField = $(newFileFields[i]);
			var gridRow = SmartWorks.GridLayout.newGridRow();
			var gridTable = SmartWorks.GridLayout.newGridTable();
			newFileField.html(gridTable.html(gridRow));
			
			var fileNameTitle = newFileField.attr("fileNameTitle");
			var fileDescTitle = newFileField.attr("fileDescTitle");

			SmartWorks.FormRuntime.FileFieldBuilder.buildEx({
				container: gridRow,
				fieldId: "txtFileField",
				fieldName: fileNameTitle,
				columns: 1,
				required: true
			});
			gridRow.find('.form_label').hide();
			
			gridRow = SmartWorks.GridLayout.newGridRow().appendTo(gridTable);
			gridRow.hide();
			SmartWorks.FormRuntime.TextInputBuilder.buildEx({
				container: gridRow,
				fieldId: "txtFileDesc",
				fieldName: fileDescTitle,
				columns: 1,
				multiLines: 2,
				required: false
			});
		}		
	}
};

function loadNewEventFields(startDate, endDate) {
	var newEventFields = $('div.js_new_event_fields');
	if(!isEmpty(newEventFields)) {
		for(var i=0; i<newEventFields.length; i++) {
			var newEventField = $(newEventFields[i]);
			var gridRow = SmartWorks.GridLayout.newGridRow();
			var gridTable = SmartWorks.GridLayout.newGridTable();
			newEventField.html(gridTable.html(gridRow));
			
			var placeHolderTitle = newEventField.attr("placeHolderTitle");
			var eventNameTitle = newEventField.attr("eventNameTitle");
			var startDateTitle = newEventField.attr("startDateTitle");
			var endDateTitle = newEventField.attr("endDateTitle");
			var alarmPolicyTitle = newEventField.attr("alarmPolicyTitle");
			var placeTitle = newEventField.attr("placeTitle");
			var relatedUsersTitle = newEventField.attr("relatedUsersTitle");
			var contentTitle = newEventField.attr("contentTitle");
			SmartWorks.FormRuntime.TextInputBuilder.buildEx({
				container: gridRow,
				fieldId: "txtEventName",
				fieldName: eventNameTitle,
				columns: 3,
				colSpan: 3,
				required: true
			});
			gridRow.find('.form_label').hide();
			gridRow.find('.form_value input').removeClass('fieldline').attr('placeholder', placeHolderTitle);
			var startDateStr = "";
			if(isEmpty(startDate)){
				var today = new Date();
				startDate = new Date(today.getTime() - today.getMinutes()*60*1000);
			}
			startDateStr = startDate.format('yyyy.mm.dd hh:MM');
			gridRow = SmartWorks.GridLayout.newGridRow().appendTo(gridTable);
			gridRow.hide();
			SmartWorks.FormRuntime.DateTimeChooserBuilder.buildEx({
				container: gridRow,
				fieldId: "txtEventStartDate",
				fieldName: startDateTitle,
				value: startDateStr,
				columns: 3,
				colSpan: 1,
				required: true
			});

			var endDateStr = "";
			if(!isEmpty(endDate))
				endDateStr = endDate.format('yyyy.mm.dd hh:MM');
			SmartWorks.FormRuntime.DateTimeChooserBuilder.buildEx({
				container: gridRow,
				fieldId: "txtEventEndDate",
				fieldName: endDateTitle,
				value: endDateStr,
				columns: 3,
				colSpan: 1,
				required: false
			});
			
			var staticItems = new Array();
			staticItems.push(smartMessage.get("alarmPolicyNone"));
			staticItems.push(smartMessage.get("alarmPolicyOnTime"));
			staticItems.push(smartMessage.get("alarmPolicy5m"));
			staticItems.push(smartMessage.get("alarmPolicy10m"));
			staticItems.push(smartMessage.get("alarmPolicy15m"));
			staticItems.push(smartMessage.get("alarmPolicy30m"));
			staticItems.push(smartMessage.get("alarmPolicy1h"));
			staticItems.push(smartMessage.get("alarmPolicy1d"));
			SmartWorks.FormRuntime.ComboBoxBuilder.buildEx({
				container: gridRow,
				fieldId: "selEventAlarmPolicy",
				fieldName: alarmPolicyTitle,
				columns: 3,
				colSpan: 1,
				staticItems : staticItems,
				required: false
			});
		  	
			gridRow = SmartWorks.GridLayout.newGridRow().appendTo(gridTable);
			gridRow.hide();
			SmartWorks.FormRuntime.TextInputBuilder.buildEx({
				container: gridRow,
				fieldId: "txtEventPlace",
				fieldName: placeTitle,
				columns: 3,
				colSpan: 3,
				required: false
			});

			gridRow = SmartWorks.GridLayout.newGridRow().appendTo(gridTable);
			gridRow.hide();
			SmartWorks.FormRuntime.UserFieldBuilder.buildEx({
				container: gridRow,
				fieldId: "txtEventRelatedUsers",
				fieldName: relatedUsersTitle,
				columns: 3,
				colSpan: 3,
				multiUsers: true,
				required: false
			});

			gridRow = SmartWorks.GridLayout.newGridRow().appendTo(gridTable);
			gridRow.hide();
			SmartWorks.FormRuntime.TextInputBuilder.buildEx({
				container: gridRow,
				fieldId: "txtEventContent",
				fieldName: contentTitle,
				columns: 3,
				colSpan: 3,
				multiLines: 3,
				required: false
			});
		}		
	}
};

function loadNewBoardFields() {
	var newBoardFields = $('div.js_new_board_fields');
	if(!isEmpty(newBoardFields)) {
		for(var i=0; i<newBoardFields.length; i++) {
			var newBoardField = $(newBoardFields[i]);
			var gridRow = SmartWorks.GridLayout.newGridRow();
			var gridTable = SmartWorks.GridLayout.newGridTable();
			newBoardField.html(gridTable.html(gridRow));
			
			var placeHolderTitle = newBoardField.attr("placeHolderTitle");
			var boardNameTitle = newBoardField.attr("boardNameTitle");
			var boardDetailsTitle = newBoardField.attr("boardDetailsTitle");
			var boardFilesTitle = newBoardField.attr("boardFilesTitle");

			SmartWorks.FormRuntime.TextInputBuilder.buildEx({
				container: gridRow,
				fieldId: "txtBoardName",
				fieldName: boardNameTitle,
				columns: 1,
				required: true
			});
			gridRow.find('.form_label').hide();
			gridRow.find('.form_value input').removeClass('fieldline').attr('placeholder', placeHolderTitle);
			
			gridRow = SmartWorks.GridLayout.newGridRow().appendTo(gridTable);
			gridRow.hide();
			SmartWorks.FormRuntime.TextInputBuilder.buildEx({
				container: gridRow,
				fieldId: "txtBoardDetails",
				fieldName: boardDetailsTitle,
				columns: 1,
				multiLines: 4,
				required: true
			});
			
			gridRow = SmartWorks.GridLayout.newGridRow().appendTo(gridTable);
			gridRow.hide();
			SmartWorks.FormRuntime.FileFieldBuilder.buildEx({
				container: gridRow,
				fieldId: "txtBoardFiles",
				fieldName: boardFilesTitle,
				columns: 1,
				required: false
			});
		}		
	}
};

function loadTaskForwardFields() {
	var taskForwardFields = $('div.js_task_forward_fields');
	if(!isEmpty(taskForwardFields)) {
		for(var i=0; i<taskForwardFields.length; i++) {
			var taskForwardField = $(taskForwardFields[i]);
			
			var gridRow = SmartWorks.GridLayout.newGridRow();
			var gridTable = SmartWorks.GridLayout.newGridTable();
			taskForwardField.html(gridTable.html(gridRow));
			
			var subjectTitle = taskForwardField.attr("subjectTitle");
			var forwardeeTitle = taskForwardField.attr("forwardeeTitle");
			var commentsTitle = taskForwardField.attr("commentsTitle");
			var subject = taskForwardField.attr("subject");
			var comments = taskForwardField.attr("content");
			var readOnly = isEmpty(subject) ? false : true;

			SmartWorks.FormRuntime.TextInputBuilder.buildEx({
				container: gridRow,
				fieldId: "txtForwardSubject",
				fieldName: subjectTitle,
				value: subject,
				columns: 1,
				required: true,
				readOnly: readOnly
			});
			
			if(!readOnly){
				gridRow = SmartWorks.GridLayout.newGridRow().appendTo(gridTable);
				SmartWorks.FormRuntime.UserFieldBuilder.buildEx({
					container: gridRow,
					fieldId: "txtForwardForwardee",
					fieldName: forwardeeTitle,
					columns: 1,
					multiUsers: true,
					required: true,
					readOnly: readOnly
				});
			}

			gridRow = SmartWorks.GridLayout.newGridRow().appendTo(gridTable);
			SmartWorks.FormRuntime.TextInputBuilder.buildEx({
				container: gridRow,
				fieldId: "txtForwardComments",
				fieldName: commentsTitle,
				value: comments,
				columns: 1,
				multiLines: 4,
				required: false,
				readOnly: readOnly
			});
			
			var iworkSpace = taskForwardFields.parents('.js_iwork_space_page');
			if(!isEmpty(iworkSpace)){
				var target = iworkSpace.find('.js_append_task_forward_page').addClass('up');
				if(readOnly){
					target.addClass('form_read');
					iworkSpace.find('.js_btn_reply_forward').show().siblings().hide();
					iworkSpace.find('.js_btn_cancel').show();						
				}
				target.parent().addClass('contents_space');
				target.find('.dash_line').remove();
			}
		}		
	}
};

function loadTaskApprovalFields() {
	var taskApprovalFields = $('div.js_task_approval_fields');
	if(!isEmpty(taskApprovalFields)) {
		for(var i=0; i<taskApprovalFields.length; i++) {
			var taskApprovalField = $(taskApprovalFields[i]);
			
			var gridRow = SmartWorks.GridLayout.newGridRow();
			var gridTable = SmartWorks.GridLayout.newGridTable();
			taskApprovalField.html(gridTable.html(gridRow));
			
			var subjectTitle = taskApprovalField.attr("subjectTitle");
			var forwardeeTitle = taskApprovalField.attr("forwardeeTitle");
			var commentsTitle = taskApprovalField.attr("commentsTitle");
			var drafterTitle = taskApprovalField.attr("drafterTitle");
			var draftDateTitle = taskApprovalField.attr("draftDateTitle");
			var subject = taskApprovalField.attr("subject");
			var comments = taskApprovalField.attr("content");
			var drafterId = taskApprovalField.attr("drafterId");
			var drafterName = taskApprovalField.attr("drafterName");
			var draftDate = taskApprovalField.attr("draftDate");
			var readOnly = isEmpty(subject) ? false : true;
			var actionRequired = taskApprovalField.attr("actionRequired");

			SmartWorks.FormRuntime.TextInputBuilder.buildEx({
				container: gridRow,
				fieldId: "txtApprovalSubject",
				fieldName: subjectTitle,
				value: subject,
				columns: 2,
				colSpan: 2,
				required: true,
				readOnly: readOnly

			});
			if(!readOnly){
				gridRow = SmartWorks.GridLayout.newGridRow().appendTo(gridTable);
				SmartWorks.FormRuntime.UserFieldBuilder.buildEx({
					container: gridRow,
					fieldId: "txtApprovalForwardee",
					fieldName: forwardeeTitle,
					columns: 2,
					colSpan: 2,
					multiUsers: true,
					required: false,
					readOnly: readOnly
				});
			}

			gridRow = SmartWorks.GridLayout.newGridRow().appendTo(gridTable);
			SmartWorks.FormRuntime.TextInputBuilder.buildEx({
				container: gridRow,
				fieldId: "txtApprovalComments",
				fieldName: commentsTitle,
				value: comments,
				columns: 2,
				colSpan: 2,
				multiLines: 4,
				required: false,
				readOnly: readOnly
			});
			
			if(readOnly){
				var users = new Array();
				users.push({userId : drafterId, longName: drafterName});
				gridRow = SmartWorks.GridLayout.newGridRow().appendTo(gridTable);
				SmartWorks.FormRuntime.UserFieldBuilder.buildEx({
					container: gridRow,
					fieldId: "txtApprovalDrafter",
					fieldName: drafterTitle,
					columns: 2,
					colSpan: 1,
					users: users,
					multiUsers: false,
					required: false,
					readOnly: readOnly
				});
				SmartWorks.FormRuntime.TextInputBuilder.buildEx({
					container: gridRow,
					fieldId: "txtApprovalDraftDate",
					fieldName: draftDateTitle,
					value: draftDate,
					columns: 2,
					colSpan: 1,
					required: false,
					readOnly: readOnly

				});
			}

			var iworkSpace = taskApprovalFields.parents('.js_iwork_space_page');
			if(!isEmpty(iworkSpace)){
				var target = iworkSpace.find('.js_append_task_approval_page').addClass('up');
				if(actionRequired==="true"){
					target.addClass('form_read');
					iworkSpace.find('.js_btn_approve_approval').show().siblings().hide();
					iworkSpace.find('.js_btn_return_approval').show();
					iworkSpace.find('.js_btn_reject_approval').show();
				}else{
					iworkSpace.find('.js_btn_approve_approval').hide().siblings().hide();					
				}
				
				if(readOnly){
					iworkSpace.find('.js_toggle_forward_btn').hide();
					iworkSpace.find('.js_toggle_approval_btn').hide();
				}
				target.parent().addClass('contents_space');
				target.find('.dash_line').remove();
			}
		}		
	}
};

function loadGroupProfileField() {
	var groupProfileFields = $('div.js_group_profile_field');
	if(!isEmpty(groupProfileFields)) {
		for(var i=0; i<groupProfileFields.length; i++) {
			var groupProfileField = $(groupProfileFields[i]);
			
			var gridRow = SmartWorks.GridLayout.newGridRow();
			var gridTable = SmartWorks.GridLayout.newGridTable();
			groupProfileField.html(gridTable.html(gridRow));

			SmartWorks.FormRuntime.ImageBoxBuilder.buildEx({
				container: gridRow,
				fieldId: "imgGroupProfile",
				fieldName: "group profile",
				imgSource: "images/default_group_picture.gif",
				pictureWidth: 110,
				pictureHeight: 110,
				required: false
			});
		}		
	}
};

function loadNewGroupFields() {
	var newGroupFields = $('div.js_new_group_fields');
	if(!isEmpty(newGroupFields)) {
		for(var i=0; i<newGroupFields.length; i++) {
			var newGroupField = $(newGroupFields[i]);
			var gridRow = SmartWorks.GridLayout.newGridRow();
			var gridTable = SmartWorks.GridLayout.newGridTable();
			newGroupField.html(gridTable.html(gridRow));
			var groupMembersTitle = newGroupField.attr("groupMembersTitle");

			SmartWorks.FormRuntime.UserFieldBuilder.buildEx({
				container: gridRow,
				fieldId: "txtGroupMembers",
				fieldName: groupMembersTitle,
				columns: 2,
				colSpan: 1,
				multiUsers: true,
				required: false
			});
		}		
	}
};

function loadWriteMailFields() {
	var writeMailFields = $('div.js_write_mail_fields');
	if(!isEmpty(writeMailFields)) {
		for(var i=0; i<writeMailFields.length; i++) {
			var writeMailField = $(writeMailFields[i]);
			
			var gridRow = SmartWorks.GridLayout.newGridRow();
			var gridTable = SmartWorks.GridLayout.newGridTable();
			writeMailField.html(gridTable.html(gridRow));
			
			var receiversTitle = writeMailField.attr("receiversTitle");
			var receiversHtml = writeMailField.attr('receivers') || "";
			var ccReceiversTitle = writeMailField.attr("ccReceiversTitle");
			var ccReceiversHtml = writeMailField.attr('ccReceivers') || "";
			var bccReceiversTitle = writeMailField.attr("bccReceiversTitle");
			var bccReceiversHtml = writeMailField.attr('bccReceivers') || "";
			var priorityTitle = writeMailField.attr("priorityTitle");
			var priority = writeMailField.attr('priority');
			var subjectTitle = writeMailField.attr("subjectTitle");
			var subject = writeMailField.attr('subject');
			var contents = writeMailField.attr('contents');
			var attachmentsTitle = writeMailField.attr("attachmentsTitle");
			var attachments = writeMailField.attr('attachments');

			SmartWorks.FormRuntime.UserFieldBuilder.buildEx({
				container: gridRow,
				fieldId: "receivers",
				fieldName: receiversTitle,
				columns: 1,
				multiUsers: true,
				emailAddress: true,
				usersHtml: receiversHtml,
				required: true
			});

			gridRow = SmartWorks.GridLayout.newGridRow().appendTo(gridTable);
			SmartWorks.FormRuntime.UserFieldBuilder.buildEx({
				container: gridRow,
				fieldId: "ccReceivers",
				fieldName: ccReceiversTitle,
				columns: 1,
				multiUsers: true,
				emailAddress: true,
				usersHtml: ccReceiversHtml,
				required: false
			});

			gridRow = SmartWorks.GridLayout.newGridRow().appendTo(gridTable);
			SmartWorks.FormRuntime.UserFieldBuilder.buildEx({
				container: gridRow,
				fieldId: "bccReceivers",
				fieldName: bccReceiversTitle,
				columns: 1,
				multiUsers: true,
				emailAddress: true,
				usersHtml: bccReceiversHtml,
				required: false
			});

			gridRow = SmartWorks.GridLayout.newGridRow().appendTo(gridTable);
			SmartWorks.FormRuntime.TextInputBuilder.buildEx({
				container: gridRow,
				fieldId: "subject",
				fieldName: subjectTitle,
				columns: 1,
				value: subject,
				required: true
			});
			
			gridRow = SmartWorks.GridLayout.newGridRow().appendTo(gridTable);
			SmartWorks.FormRuntime.CheckBoxBuilder.buildEx({
				container: gridRow,
				fieldId: "priority",
				fieldName: priorityTitle,
				columns: 1,
				required: false
			});
			
			gridRow = SmartWorks.GridLayout.newGridRow().appendTo(gridTable);
			SmartWorks.FormRuntime.FileFieldBuilder.buildEx({
				container: gridRow,
				fieldId: "attachments",
				fieldName: attachmentsTitle,
				columns: 1,
				required: false
			});
			if(!isEmpty(attachments)){
				gridRow.find('.form_value .qq-upload-list').append(attachments);
			}
			gridRow = SmartWorks.GridLayout.newGridRow().appendTo(gridTable);
			SmartWorks.FormRuntime.RichEditorBuilder.buildEx({
				container: gridRow,
				fieldId: "contents",
				fieldName: "",
				columns: 1,
				value: contents,
				required: true
			});
			gridRow.find('.form_label').hide();
			gridRow.find('.form_value').css({width:"100%"});
			gridRow.find('#contents').css({height:"400px"});
		}		
	}
};
