
function loadCreateCourseFields() {
	var courseProfileField = $('div.js_course_profile_field');
	if(!isEmpty(courseProfileField)) {
		var gridRow = SmartWorks.GridLayout.newGridRow();
		courseProfileField.html(gridRow);
		var imgSrc = courseProfileField.attr('imgSrc');
		if(isEmpty(imgSrc)) imgSrc =	"sera/images/default_img_course.gif";
		

		SmartWorks.FormRuntime.ImageBoxBuilder.buildEx({
			container: gridRow,
			fieldId: "imgCourseProfile",
			fieldName: "course profile",
			imgSource: imgSrc,
			columns: 1,
			pictureWidth: 110,
			pictureHeight: 110,
			required: false
		});
		courseProfileField.find('.form_col').css({padding:"0"});
		courseProfileField.find('.form_label').css({width:"100%", padding:"0 0 2px 20px"});
		courseProfileField.find('.form_value').css({width:"100%"});
	}
	
	var courseStartDateField = $('div.js_course_start_date_field');
	if(!isEmpty(courseStartDateField)) {
		
		var gridRow = SmartWorks.GridLayout.newGridRow();
		courseStartDateField.html(gridRow);

		var startDateStr = courseStartDateField.attr('openDate');
		if(isEmpty(startDateStr)) startDateStr = (new Date()).format('yyyy.mm.dd');

		SmartWorks.FormRuntime.DateChooserBuilder.buildEx({
			container: gridRow,
			fieldId: "txtCourseStartDate",
			fieldName: "course start date",
			value: startDateStr,
			columns: 4,
			colSpan: 1,
			required: false
		});
		gridRow.find('.form_col').css({width:"100px", padding:"0px"});
		gridRow.find('.form_label').hide();
	}
	
	var courseEndDateField = $('div.js_course_end_date_field');
	if(!isEmpty(courseEndDateField)) {
		
		var gridRow = SmartWorks.GridLayout.newGridRow();
		courseEndDateField.html(gridRow);
		var endDateStr = courseEndDateField.attr('closeDate');
		if(isEmpty(endDateStr)) endDateStr = "";
		SmartWorks.FormRuntime.DateChooserBuilder.buildEx({
			container: gridRow,
			fieldId: "txtCourseEndDate",
			fieldName: "course end date",
			value: endDateStr,
			columns: 4,
			colSpan: 1,
			required: false
		});
		gridRow.find('.form_col').css({width:"100px", padding:"0px"});
		gridRow.find('.form_label').hide();		
	}

};

function loadCreateMissionFields() {
	var missionOpenDateField = $('div.js_mission_open_date_field');
	if(!isEmpty(missionOpenDateField)) {
		
		var gridRow = SmartWorks.GridLayout.newGridRow();
		missionOpenDateField.html(gridRow);

		var openDateStr = missionOpenDateField.attr('openDate');
		if(isEmpty(openDateStr)) openDateStr = (new Date()).format('yyyy.mm.dd');
		console.log('openDateStr=', openDateStr);

		SmartWorks.FormRuntime.DateChooserBuilder.buildEx({
			container: gridRow,
			fieldId: "txtMissionOpenDate",
			fieldName: "mission open date",
			value: openDateStr,
			columns: 4,
			colSpan: 1,
			required: true
		});
		gridRow.find('.form_col').css({width:"100px", padding:"0px"});
		gridRow.find('.form_value').css({width:"100%"});
		gridRow.find('.form_label').hide();
	}
	
	var missionCloseDateField = $('div.js_mission_close_date_field');
	if(!isEmpty(missionCloseDateField)) {
		
		var gridRow = SmartWorks.GridLayout.newGridRow();
		missionCloseDateField.html(gridRow);

		var closeDateStr = missionCloseDateField.attr('closeDate');
		if(isEmpty(closeDateStr)) closeDateStr = "";
		console.log('closeDateStr=', closeDateStr);
		SmartWorks.FormRuntime.DateChooserBuilder.buildEx({
			container: gridRow,
			fieldId: "txtMissionCloseDate",
			fieldName: "mission close date",
			value: closeDateStr,
			columns: 4,
			colSpan: 1,
			required: true
		});
		gridRow.find('.form_col').css({width:"110px", padding:"0px"});
		gridRow.find('.form_value').css({width:"100%"});
		gridRow.find('.form_label').hide();		
	}

	var missionContentField = $('div.js_mission_content_field');
	if(!isEmpty(missionContentField)) {
		
		var gridRow = SmartWorks.GridLayout.newGridRow();
		missionContentField.html(gridRow);
		
		var missionContent = missionContentField.attr('content');
		if(isEmpty(missionContent)) missionContent = "";

		console.log('content=', missionContent);
		SmartWorks.FormRuntime.RichEditorBuilder.buildEx({
			container: gridRow,
			fieldId: "txtaMissionContent",
			fieldName: "mission content",
			value: missionContent,
			columns: 1,
			colSpan: 1,
			required: true
		});
		gridRow.find('.form_col').css({width:"580px", padding:"0px"});
		gridRow.find('textarea').css({height:"300px"});
		gridRow.find('.form_value').css({width:"100%"});
		gridRow.find('.form_label').hide();		
	}
};

function loadCreateTeamFields() {
	var teamStartDateField = $('div.js_team_start_date_field');
	if(!isEmpty(teamStartDateField)) {
		
		var gridRow = SmartWorks.GridLayout.newGridRow();
		teamStartDateField.html(gridRow);

		var startDateStr = (new Date()).format('yyyy.mm.dd');
		SmartWorks.FormRuntime.DateChooserBuilder.buildEx({
			container: gridRow,
			fieldId: "txtTeamStartDate",
			fieldName: "team start date",
			value: startDateStr,
			columns: 4,
			colSpan: 1,
			required: true
		});
		gridRow.find('.form_col').css({width:"110px", padding:"0px"});
		gridRow.find('.form_value').css({width:"100%"});
		gridRow.find('.form_label').hide();
	}
	
	var teamEndDateField = $('div.js_team_end_date_field');
	if(!isEmpty(teamEndDateField)) {
		
		var gridRow = SmartWorks.GridLayout.newGridRow();
		teamEndDateField.html(gridRow);

		SmartWorks.FormRuntime.DateChooserBuilder.buildEx({
			container: gridRow,
			fieldId: "txtTeamEndDate",
			fieldName: "team end date",
			value: "",
			columns: 4,
			colSpan: 1,
			required: true
		});
		gridRow.find('.form_col').css({width:"110px", padding:"0px"});
		gridRow.find('.form_value').css({width:"100%"});
		gridRow.find('.form_label').hide();		
	}

	var teamMembersField = $('div.js_team_members_field');
	if(!isEmpty(teamMembersField)) {
		
		var gridRow = SmartWorks.GridLayout.newGridRow();
		teamMembersField.html(gridRow);
		var courseId = teamMembersField.attr('courseId');

		SmartWorks.FormRuntime.UserFieldBuilder.buildEx({
			container: gridRow,
			fieldId: "txtInviteMembers",
			fieldName: "team members invite",
			columns: 1,
			multiUsers: true,
			courseId: courseId,
			required: true
		});
		gridRow.find('.form_col').css({width:"491px", padding:"0px"});
		gridRow.find('.form_value').css({width:"100%"});
		gridRow.find('.form_label').hide();		
	}
};

function loadSeraNoteFields() {
	var noteImageField = $('div.js_note_image_field');
	if(!isEmpty(noteImageField)) {
		var gridRow = SmartWorks.GridLayout.newGridRow();
		noteImageField.html(gridRow);

		SmartWorks.FormRuntime.ImageBoxBuilder.buildEx({
			container: gridRow,
			fieldId: "imgNoteImage",
			fieldName: "note image",
			columns: 1,
			pictureWidth: 300,
			required: false
		});
		noteImageField.find('.form_label').removeClass('form_label').css({width:"100%", padding:"0 0 2px 0"});
		
	}
	
	var noteVideoField = $('div.js_note_video_field');
	if(!isEmpty(noteVideoField)) {
		var gridRow = SmartWorks.GridLayout.newGridRow();
		noteVideoField.html(gridRow);

		SmartWorks.FormRuntime.VideoYTBoxBuilder.buildEx({
			container: gridRow,
			fieldId: "ytNoteVideo",
			fieldName: "note video",
			columns: 1,
			videoWidth: 300,
			videoHeight: 200,
			required: false
		});
		noteVideoField.find('.form_label').removeClass('form_label').css({width:"100%", padding:"0 0 2px 0"});
		
	}
	
	var noteFileField = $('div.js_note_file_field');
	if(!isEmpty(noteFileField)) {
		var gridRow = SmartWorks.GridLayout.newGridRow();
		noteFileField.html(gridRow);

		SmartWorks.FormRuntime.FileFieldBuilder.buildEx({
			container: gridRow,
			fieldId: "txtNoteFile",
			fieldName: "note file",
			columns: 1,
			required: false
		});
		
		gridRow.find('.form_label').hide();		
	}
};

function loadJoinUsFields() {
	var joinusProfileField = $('div.js_joinus_profile_field');
	if(!isEmpty(joinusProfileField)) {
		var gridRow = SmartWorks.GridLayout.newGridRow();
		joinusProfileField.html(gridRow);

		SmartWorks.FormRuntime.ImageBoxBuilder.buildEx({
			container: gridRow,
			fieldId: "imgUserProfile",
			fieldName: "join us profile",
			imgSource: "",
			columns: 1,
			pictureWidth: 118,
			pictureHeight: 118,
			required: false
		});
//		joinusProfileField.find('.form_col').css({padding:"0"});
//		joinusProfileField.find('.form_label').css({width:"100%", padding:"0 0 2px 20px"});
//		joinusProfileField.find('.form_value').css({width:"100%"});
	}
};

