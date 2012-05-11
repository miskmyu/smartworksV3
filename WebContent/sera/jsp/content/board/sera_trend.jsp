<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.WorkSpace"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>
<script type="text/javascript">
	getIntanceList = function(paramsJson, progressSpan, isGray){
		if(isEmpty(progressSpan))
			progressSpan = $('.js_work_list_title').find('.js_progress_span:first');
		if(isGray)
			smartPop.progressContGray(progressSpan);
		else
			smartPop.progressCont(progressSpan);
		console.log(JSON.stringify(paramsJson));
		var url = "set_instance_list_params.sw";
		$.ajax({
			url : url,
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				$('#board_instance_list_page').html(data);
				smartPop.closeProgress();
			},
			error : function(xhr, ajaxOptions, thrownError) {
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.ERROR, smartMessage.get('workListError'));
			}
		});
	};

	selectListParam = function(progressSpan, isGray){
		var boardList = $('.js_board_list_page');
		var forms = boardList.find('form:visible');
		var paramsJson = {};
		var workId = boardList.attr('workId');
		paramsJson["href"] = "jsp/content/work/list/board_instance_list.jsp?workId=" + workId;
		var searchFilters = boardList.find('form[name="frmSearchFilter"]');
		for(var i=0; i<forms.length; i++){
			var form = $(forms[i]);
			if(form.attr('name') !== "frmSearchFilter" && !(!isEmpty(searchFilters) && form.attr('name') === "frmSearchInstance")){
				paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
			}
		}
		if(!isEmpty(searchFilters)){
			var searchFilterArray = new Array();
			for(var i=0; i<searchFilters.length; i++){
				var searchFilter = $(searchFilters[i]);
				if(searchFilter.is(':visible'))
					searchFilterArray.push(searchFilter.find(':visible').serializeObject());
			}
			paramsJson['frmSearchFilters'] = searchFilterArray;
		}
		getIntanceList(paramsJson, progressSpan, isGray);		
	};
</script>
<%
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	String cid = request.getParameter("cid");
	String wid = request.getParameter("wid");
	session.setAttribute("cid", cid);
	session.setAttribute("wid", wid);
	
	User cUser = SmartUtil.getCurrentUser();
	WorkSpace workSpace = smartWorks.getWorkSpaceById(wid);
	String workSpaceName = (SmartUtil.isBlankObject(wid)) ? cUser.getCompany() : workSpace.getName(); 

%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />
<div id="course_list_section">
	<!-- SNB Left -->
	<div class="snb2">
		<ul class="snb_menu">
			<li><img height="112" width="149" src="sera/images/function_title_helpcenter.gif"></li>
			<li class="selected"><a href="">세라 소식</a></li>
			<li class=""><a href="">트렌드 세라</a></li>
		</ul>
	</div>
	<!-- SNB Left//-->
	<!-- Content -->
	<div id="content_list_section">

		<div class="tit_header">
			<h2>세라 소식</h2>
			<!-- <ul>
				<li class="fl"><input id="rdoHandleOpen" class="pr3" type="radio" checked="checked" name="rdoHandle"></li>
				<li class="fl"><span class="pr5">펼쳐보기</span></li>
				<li class="fl"><input id="rdoHandleClose" class="pr3" type="radio" name="rdoHandle"></li>
				<li class="fl"><span class="pr5">접어보기</span></li>
			</ul> -->
		</div>

		<!-- News Content 001 -->
		<ul class="workshop_list">
			<li>
				<dl>
					<dt>
						<span class="num">1</span> <span class="subjt" style="cursor: pointer;">[나를 유능한 인재로 키우는 성공독서 전략] 멘티 모집 안내</span>
						<span class="date">2012-01-03 04:29:36</span>
					</dt>
					<!-- 펼쳐보기 영역 -->
					<dd class="text" style="display: block;">
						<P><b>[나를 유능한 인재로 키우는 성공독서 전략]</b></P>
						성공멘토를 만나는 최고의 방법인 독서!<br /> 하지만 책은 아직도 멀게만 느껴지고 독서는 부담스러운
						강박관념입니다.<br /> 책에 가까워지는 성공독서전략을 배웁니다.<br /> 나에게 맞는 올바른 독서방법을
						배웁니다.<br /> 책읽기를 실천력있는 독서습관으로 만듭니다.<br /> 미래인재의 요소들을 내 것으로 만들고,<br />
						적용함으로써 깊이와 넓이를 갖춘 리더로 자신의 운명을 개척해 나갑니다.<br /> 올바른 독서방법 및 독서습관
						만들기를 통해 새로운 미래의 유능한 인재로 거듭난다.<br /> [모집 사항]<br /> 멘토링 시작 : 2012년
						1월 16일(월)<br /> 멘토링 기간 : 12주<br /> 멘토 : 이용각 대표(생각정리디자인연구소, 생각정리
						전문가)<br /> 모집인원 : 24명(선착순 마감)<br /> 참여비용 : 12만원
					</dd>
					<!-- 펼쳐보기 영역 -->
				</dl></li>
		</ul>
		<!-- News Content 001 //-->

		<!-- News Content 002 -->
		<ul class="workshop_list">
			<li>
				<dl>
					<dt>
						<span class="num">2</span> <span class="subjt" style="cursor: pointer;">[나를 유능한 인재로 키우는 성공독서 전략] 멘티 모집 안내</span>
						<span class="date">2012-01-03 04:29:36</span>
					</dt>
					<!-- 펼쳐보기 영역 -->
					<dd class="text" style="display: block;">
						<P><b>[나를 유능한 인재로 키우는 성공독서 전략]</b></P>
						성공멘토를 만나는 최고의 방법인 독서!<br /> 하지만 책은 아직도 멀게만 느껴지고 독서는 부담스러운
						강박관념입니다.<br /> 책에 가까워지는 성공독서전략을 배웁니다.<br /> 나에게 맞는 올바른 독서방법을
						배웁니다.<br /> 책읽기를 실천력있는 독서습관으로 만듭니다.<br /> 미래인재의 요소들을 내 것으로 만들고,<br />
						적용함으로써 깊이와 넓이를 갖춘 리더로 자신의 운명을 개척해 나갑니다.<br /> 올바른 독서방법 및 독서습관
						만들기를 통해 새로운 미래의 유능한 인재로 거듭난다.<br /> [모집 사항]<br /> 멘토링 시작 : 2012년
						1월 16일(월)<br /> 멘토링 기간 : 12주<br /> 멘토 : 이용각 대표(생각정리디자인연구소, 생각정리
						전문가)<br /> 모집인원 : 24명(선착순 마감)<br /> 참여비용 : 12만원
					</dd>
					<!-- 펼쳐보기 영역 -->
				</dl>
			</li>
		</ul>
		<!-- News Content 002 //-->

	</div>
	<!-- Content //-->
</div>
