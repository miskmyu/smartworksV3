<%@page import="net.smartworks.model.instance.Instance"%>
<%@page import="java.util.Calendar"%>
<%@page import="net.smartworks.model.sera.info.MissionInstanceInfo"%>
<%@page import="net.smartworks.model.sera.Course"%>
<%@page import="net.smartworks.model.instance.info.EventInstanceInfo"%>
<%@page import="java.util.Date"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();

	String courseId = request.getParameter("courseId");
	Course course = (Course)session.getAttribute("course");
	if(SmartUtil.isBlankObject(course) || !course.getId().equals(courseId)) course = smartWorks.getCourseById(courseId);
	
	LocalDate thisDate = new LocalDate();
	String todayStr = request.getParameter("today");
	if(!SmartUtil.isBlankObject(todayStr)){
		thisDate = LocalDate.convertLocalDateStringToLocalDate(todayStr);
	}
	LocalDate firstDateOfMonth = LocalDate.convertLocalMonthWithDiffMonth(thisDate, 0);
	String prevMonthStr = LocalDate.convertLocalMonthWithDiffMonth(thisDate, -1).toLocalDateSimpleString();
	String nextMonthStr = LocalDate.convertLocalMonthWithDiffMonth(thisDate, 1).toLocalDateSimpleString();
%>
<!-- Nav SNB -->
<div id="panel_section" class="js_mission_list_page" prevMonth="<%=prevMonthStr %>" nextMonth="<%=nextMonthStr%>" courseId="<%=courseId%>" startDate="" endDate="">
	<table>
		<tr class="tit_bg" style="height:2px"></tr>
		<tr class="js_calendar_space"></tr>
	</table>
</div>
<!-- Nav SNB //-->

<script type="text/javascript">
$(document).ready(function(){

	var columnFormat =  {
		    month: 'dddd',    // Mon
		    week: 'ddd M/d', // Mon 9/7
		    day: 'dddd M/d'  // Monday 9/7
		};
	var titleFormat = {
		    month: 'MMMM yyyy',                             // September 2009
		    week: "MMM d[ yyyy]{ '&#8212;'[ MMM] d yyyy}", // Sep 7 - 13 2009
		    day: 'dddd, MMM d, yyyy'                  // Tuesday, Sep 8, 2009
		};
	var monthNames = ['January', 'February', 'March', 'April', 'May', 'June', 'July',
	                  'August', 'September', 'October', 'November', 'December']; 
	var monthNamesShort = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
	                       'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']; 
	var dayNames = ['Sunday', 'Monday', 'Tuesday', 'Wednesday',
	                'Thursday', 'Friday', 'Saturday'];
	var dayNamesShort = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
	if(currentUser.locale === 'ko'){
		columnFormat = {
			    month: 'dddd',    // Mon
			    week: 'M월d일 ddd', // Mon 9/7
			    day: 'M월d일 dddd'  // Monday 9/7				
		};
		titleFormat = {
			    month: 'yyyy년 MMMM',                             // September 2009
			    week: "yyyy년 MMM d일{ '&#8212;'[yyyy년][MMM] d일}", // Sep 7 - 13 2009
			    day: 'yyyy년 MMM d일 dddd'                  // Tuesday, Sep 8, 2009
			};
		monthNames = ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'];
		monthNamesShort = ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'];
		dayNames = ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'];
		dayNamesShort = ['일', '월', '화', '수', '목', '금', '토'];
	}
	
	$('.js_calendar_space').fullCalendar({
		header: {
			left: '',
			center: 'prev title next',
			right: ''
		},
		editable: true,
		buttonText : {
		    today:   "미션생성"
		},
	    events: function(start, end, callback) {
	    	smartPop.progressCenter();
	    	var courseId = $('.js_mission_list_page').attr('courseId');
	        $.ajax({
	            url: 'get_mission_list.sw',
	            data: {
	            	courseId: courseId,
	            	fromDate: start.format('yyyy.mm.dd'),
	            	toDate: end.format('yyyy.mm.dd')
	            },
	            success: function(data) {
	                var missions = [];
	                var missionInstances = data.missions;
	                var courseId = data.courseId;
 	                if(!isEmpty(missionInstances)){
		                for(var i=0; i<missionInstances.length; i++){
		                	var mission = missionInstances[i];
		                	var today = new Date();
		                	var openDate = new Date(mission.openDate);
		                	var iconClass = (openDate>today) ? "icon_reserve" : (mission.isClearedByMe) ? "icon_mission" :  "icon_mission current";
            	
	                		var title = iconClass + '&' + '[미션' + (mission.index+1) + '] ' + mission.subject + '&' + mission.id;

	                		missions.push({
			                 	id: mission.id,
			            		title: title,
			                	start: new Date(mission.openDate),
			                 	end: new Date(mission.closeDate),
			                 	allDay: isEmpty(mission.closeDate),
			                 	editable: false,
			                 	backgroundColor: "#ffffff",
			                 	textColor: "#000000",
			                 	borderColor: "#cccccc"
			            	});
		                }
	                }
 					callback(missions);
 					smartPop.closeProgress();
	            },
	            error: function(){
	            	smartPop.closeProgress();
	            }
	        });
	    },
		timeFormat: {
		    agenda: 'H:mm{ - H:mm}',
		    '': 'H(:mm)'
		},
 		dayClick: function(date, allDay, jsEvent, view){
 			var toDate = null;
 			console.log('hours=', date.getHours());
 			if(date.getHours()>0) toDate = new Date(date.getTime() + 60*60*1000);
			loadNewEventFields(date, toDate);
			$('div.js_new_event_fields .form_value:first input').click();			
		},
		
		todayClick: function(event, jsEvent, vie){
			$('.js_create_mission').click();			
			
		},
		
	    eventRender: function(event, element) {
	    	var title = $(element).find('.fc-event-title');
	    	var titleText = title.html();
	    	var tokens = titleText.split('&amp;');
	    	var titleHtml = (tokens.length==3) ? '<a href="" class="js_select_mission" missionId="' + tokens[2] + '"><span class="' + tokens[0] + '" title="' + tokens[1] + '">' +  tokens[1] + '</span></a>' : token[0]; 
	    	title.html(titleHtml);
	    	var eventTime = $(element).find('.fc-event-time').addClass("fl").html();
	    	if(eventTime === '0') $(element).find('.fc-event-time').html('');
	    },
		firstDay: 1,
		weekMode: 'liquid',
		columnFormat: columnFormat,
		titleFormat: titleFormat,
		monthNames : monthNames,
		monthNamesShort : monthNamesShort,
		dayNames : dayNames,
		dayNamesShort : dayNamesShort,
		allDayText : smartMessage.get('wholeDayText'),
		axisFormat : 'HH:mm',
		aspectRatio : 1.8,
		defaultEventMinutes : 30
	});	
});
</script>
