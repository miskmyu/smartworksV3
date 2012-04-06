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
	Course course = (Course) session.getAttribute("course");
	if (SmartUtil.isBlankObject(course) || !course.getId().equals(courseId))
		course = smartWorks.getCourseById(courseId);

	LocalDate thisDate = new LocalDate();
	String todayStr = request.getParameter("today");
	if (!SmartUtil.isBlankObject(todayStr)) {
		thisDate = LocalDate.convertLocalDateStringToLocalDate(todayStr);
	}
	LocalDate firstDateOfMonth = LocalDate.convertLocalMonthWithDiffMonth(thisDate, 0);
	String prevMonthStr = LocalDate.convertLocalMonthWithDiffMonth(thisDate, -1).toLocalDateSimpleString();
	String nextMonthStr = LocalDate.convertLocalMonthWithDiffMonth(thisDate, 1).toLocalDateSimpleString();
%>

<!-- Title -->
<div class="header mb10">
	<div>미션 등록하기</div>
</div>
<!-- Title //-->

<!-- Input Form -->
<div class="t_refe mb10">
	* 미션을 등록하기 위해서는 달력에서 <span class="t_red">미션 마감일</span>에 클릭하여 등록하여 주십시오.
	<br /> * 등록된 미션을 수정하기 위해서는 이미 등록된 미션을 클릭하여 수정하십시오
</div>

<div class="form_layout">

	<table class="bgn">
		<tr>
			<td><div class="form_label">미션6 제목</div>
				<div class="form_value">
					<input type="text" class="fieldline" style="width: 300px" />
					<div class="fr ml5">
						<span class="t_red">0</span> /150kbyte
					</div>
				</div></td>
		</tr>
		<tr>
			<td><div class="form_label">미션등록예약*</div>
				<div class="form_value">
					<input name="" class="fieldline form_date_input" type="text" /> <span
						class="icon_fb_date psr"></span>
				</div>
				<div class="cb pt10">
					<div class="t_refe">* 선택사항 : 미션을 예약 등록하고자 할 경우에는, 미션 시작 날짜를
						지정해 주세요.</div>
				</div>
			</td>
		</tr>
		<tr>
			<td><div class="form_label">선행미션*</div>
				<div class="form_value">
					<select>
						<option>없 음</option>
						<option>[미션1]미션제목 미션제목</option>
						<option>[미션2]미션제목 미션제목</option>
						<option>[미션3]미션제목 미션제목</option>
						<option>[미션4]미션제목 미션제목</option>
					</select>
				</div>
				<div class="cb pt10">
					<div class="t_refe">* 선택사항 : 선행미션을 선택하면, 선택한 미션을 수행해야 해당 미션을
						수행할 수 있습니다.</div>
				</div>
			</td>
		</tr>
		<tr>
			<td><textarea name="" cols="" rows="6">미션내용을 등록하여 주십시오.</textarea>
			</td>
		</tr>
		<tr>
			<td>
				<div class="cb" style="margin: -8px 0 0 0">
					<!-- 좌측 영역 -->
					<div class="option">
						<!-- select -->
						<div class="txt">
							<a href=""> 전체공개<span class="icon_bul_select ml5"></span> </a> |
						</div>
						<!-- select //-->
						<!-- 태그넣기 -->
						<div class="txt">
							<a href=""> 태그넣기<span class="icon_bul_select ml5"></span> </a>
						</div>
						<!-- 태그넣기//-->
					</div>
					<!-- 좌측 영역//-->
					<!-- 우측 버튼 영역 -->
					<div class="attach_file">
						<ul>
							<li class="t_s11"><span class="t_red">0</span> /1000kbyte</li>
							<li class="icon_memo ml10"><a href=""> </a></li>
							<li class="icon_video"><a href=""> </a></li>
							<li class="icon_photo"><a href=""> </a></li>
							<li class="icon_link"><a href=""> </a></li>
							<!-- Btn 등록-->
							<li class="btn_default_l ml5">
								<div class="btn_default_r">등록</div>
							</li>
							<!-- Btn 등록//-->
						</ul>
					</div>
					<!-- 우측 버튼 영역 //-->
				</div>
			</td>
		</tr>
	</table>
</div>
<!-- Input Form //-->
