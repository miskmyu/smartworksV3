<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
	String cid = request.getParameter("cid");
	if (SmartUtil.isBlankObject(cid))
		cid = ISmartWorks.CONTEXT_PREFIX_HOME + cUser.getId();
	String wid = request.getParameter("wid");
	if (SmartUtil.isBlankObject(wid))
		wid = cUser.getId();
%>
<!-- Header Title -->
<div class="header_tit">
	<div class="tit_course">자신이 멘토로 운영하는 코스와 멘티로 참여하는 코스를 한번에 확인할수
		있습니다.</div>
</div>
<!-- Header Title //-->
<!-- Content Section -->
<div class="content_section">
	<div class="header mt10">
		<div>홍길동님의 운영코스</div>
	</div>
	<!-- 리스트1 -->
	<div class="category_list">
		<ul class="category_box">
			<li class="photo"><a href=""> <img width="218" height="148"
					src="../images/course_01.jpg"> </a></li>
			<li class="subject"><a href="">어느 젊은 마법사의 코스</a></li>
			<li class="maker"><a href="">SERA캠퍼스</a></li>
			<li class="info">
				<dl>
					<dd class="menteeNo">34명</dd>
					<dd class="makeDate">2012.02.07</dd>
					<dd class="category"></dd>
				</dl></li>
			<li class="detail"><a href="/Course/Detail/212">안녕하세요
					선린인터넷고등학교 여러분! 세라캠퍼스에 오신걸 환영합니다. ^^* 꿈그리기란, 어렵고 전문적인 것이 아니라 '나'자신을
					관찰하고 그것을 밖으로 표현하는 그림입니다...</a></li>
			<!-- Gauge -->
			<li class="gauge_box w_auto mt5">
				<dl>
					<dd class="gauge_outline w170">
						<label class="gauge" style="width: 50%;"></label>
					</dd>
					<dd class="process">(6/10)</dd>
				</dl></li>
			<!-- Gauge //-->
		</ul>

		<ul class="category_box">
			<li class="photo"><a href=""> <img width="218" height="148"
					src="../images/course_01.jpg"> </a></li>
			<li class="subject"><a href="">어느 젊은 마법사의 코스</a></li>
			<li class="maker"><a href="">SERA캠퍼스</a></li>
			<li class="info">
				<dl>
					<dd class="menteeNo">34명</dd>
					<dd class="makeDate">2012.02.07</dd>
					<dd class="category"></dd>
				</dl></li>
			<li class="detail"><a href="/Course/Detail/212">안녕하세요
					선린인터넷고등학교 여러분! 세라캠퍼스에 오신걸 환영합니다. ^^* 꿈그리기란, 어렵고 전문적인 것이 아니라 '나'자신을
					관찰하고 그것을 밖으로 표현하는 그림입니다...</a></li>
			<!-- Gauge -->
			<li class="gauge_box w_auto mt5">
				<dl>
					<dd class="gauge_outline w170">
						<label class="gauge" style="width: 50%;"></label>
					</dd>
					<dd class="process">(6/10)</dd>
				</dl></li>
			<!-- Gauge //-->
		</ul>

		<ul class="category_box end">
			<li class="photo"><a href=""> <img width="218" height="148"
					src="../images/course_01.jpg"> </a></li>
			<li class="subject"><a href="">어느 젊은 마법사의 코스</a></li>
			<li class="maker"><a href="">SERA캠퍼스</a></li>
			<li class="info">
				<dl>
					<dd class="menteeNo">34명</dd>
					<dd class="makeDate">2012.02.07</dd>
					<dd class="category"></dd>
				</dl></li>
			<li class="detail"><a href="/Course/Detail/212">안녕하세요
					선린인터넷고등학교 여러분! 세라캠퍼스에 오신걸 환영합니다. ^^* 꿈그리기란, 어렵고 전문적인 것이 아니라 '나'자신을
					관찰하고 그것을 밖으로 표현하는 그림입니다...</a></li>
			<!-- Gauge -->
			<li class="gauge_box w_auto mt5">
				<dl>
					<dd class="gauge_outline w170">
						<label class="gauge" style="width: 50%;"></label>
					</dd>
					<dd class="process">(6/10)</dd>
				</dl></li>
			<!-- Gauge //-->
		</ul>

		<ul class="category_box">
			<li class="photo"><a href=""> <img width="218" height="148"
					src="../images/course_01.jpg"> </a></li>
			<li class="subject"><a href="">어느 젊은 마법사의 코스</a></li>
			<li class="maker"><a href="">SERA캠퍼스</a></li>
			<li class="info">
				<dl>
					<dd class="menteeNo">34명</dd>
					<dd class="makeDate">2012.02.07</dd>
					<dd class="category"></dd>
				</dl></li>
			<li class="detail"><a href="/Course/Detail/212">안녕하세요
					선린인터넷고등학교 여러분! 세라캠퍼스에 오신걸 환영합니다. ^^* 꿈그리기란, 어렵고 전문적인 것이 아니라 '나'자신을
					관찰하고 그것을 밖으로 표현하는 그림입니다...</a></li>
			<!-- Gauge -->
			<li class="gauge_box w_auto mt5">
				<dl>
					<dd class="gauge_outline w170">
						<label class="gauge" style="width: 50%;"></label>
					</dd>
					<dd class="process">(6/10)</dd>
				</dl></li>
			<!-- Gauge //-->
		</ul>

		<ul class="category_box">
			<li class="photo"><a href=""> <img width="218" height="148"
					src="../images/course_01.jpg"> </a></li>
			<li class="subject"><a href="">어느 젊은 마법사의 코스</a></li>
			<li class="maker"><a href="">SERA캠퍼스</a></li>
			<li class="info">
				<dl>
					<dd class="menteeNo">34명</dd>
					<dd class="makeDate">2012.02.07</dd>
					<dd class="category"></dd>
				</dl></li>
			<li class="detail"><a href="/Course/Detail/212">안녕하세요
					선린인터넷고등학교 여러분! 세라캠퍼스에 오신걸 환영합니다. ^^* 꿈그리기란, 어렵고 전문적인 것이 아니라 '나'자신을
					관찰하고 그것을 밖으로 표현하는 그림입니다...</a></li>
			<!-- Gauge -->
			<li class="gauge_box w_auto mt5">
				<dl>
					<dd class="gauge_outline w170">
						<label class="gauge" style="width: 50%;"></label>
					</dd>
					<dd class="process">(6/10)</dd>
				</dl></li>
			<!-- Gauge //-->
		</ul>

		<ul class="category_box end">
			<li class="photo"><a href=""> <img width="218" height="148"
					src="../images/course_01.jpg"> </a></li>
			<li class="subject"><a href="">어느 젊은 마법사의 코스</a></li>
			<li class="maker"><a href="">SERA캠퍼스</a></li>
			<li class="info">
				<dl>
					<dd class="menteeNo">34명</dd>
					<dd class="makeDate">2012.02.07</dd>
					<dd class="category"></dd>
				</dl></li>
			<li class="detail"><a href="/Course/Detail/212">안녕하세요
					선린인터넷고등학교 여러분! 세라캠퍼스에 오신걸 환영합니다. ^^* 꿈그리기란, 어렵고 전문적인 것이 아니라 '나'자신을
					관찰하고 그것을 밖으로 표현하는 그림입니다...</a></li>
			<!-- Gauge -->
			<li class="gauge_box w_auto mt5">
				<dl>
					<dd class="gauge_outline w170">
						<label class="gauge" style="width: 50%;"></label>
					</dd>
					<dd class="process">(6/10)</dd>
				</dl></li>
			<!-- Gauge //-->
		</ul>
	</div>
	<!-- 리스트1 //-->

</div>
<!-- Content Section //-->

<!-- Content Section2 -->
<div class="content_section">
	<div class="header mt10">
		<div>홍길동님의 참여코스</div>
	</div>
	<!-- 리스트1 -->
	<div class="category_list">
		<ul class="category_box">
			<li class="photo"><a href=""> <img width="218" height="148"
					src="../images/course_01.jpg"> </a></li>
			<li class="subject"><a href="">어느 젊은 마법사의 코스</a></li>
			<li class="maker"><a href="">SERA캠퍼스</a></li>
			<li class="info">
				<dl>
					<dd class="menteeNo">34명</dd>
					<dd class="makeDate">2012.02.07</dd>
					<dd class="category"></dd>
				</dl></li>
			<li class="detail"><a href="/Course/Detail/212">안녕하세요
					선린인터넷고등학교 여러분! 세라캠퍼스에 오신걸 환영합니다. ^^* 꿈그리기란, 어렵고 전문적인 것이 아니라 '나'자신을
					관찰하고 그것을 밖으로 표현하는 그림입니다...</a></li>
			<!-- Gauge -->
			<li class="gauge_box w_auto mt5">
				<dl>
					<dd class="gauge_outline w170">
						<label class="gauge" style="width: 50%;"></label>
					</dd>
					<dd class="process">(6/10)</dd>
				</dl></li>
			<!-- Gauge //-->
		</ul>

		<ul class="category_box">
			<li class="photo"><a href=""> <img width="218" height="148"
					src="../images/course_01.jpg"> </a></li>
			<li class="subject"><a href="">어느 젊은 마법사의 코스</a></li>
			<li class="maker"><a href="">SERA캠퍼스</a></li>
			<li class="info">
				<dl>
					<dd class="menteeNo">34명</dd>
					<dd class="makeDate">2012.02.07</dd>
					<dd class="category"></dd>
				</dl></li>
			<li class="detail"><a href="/Course/Detail/212">안녕하세요
					선린인터넷고등학교 여러분! 세라캠퍼스에 오신걸 환영합니다. ^^* 꿈그리기란, 어렵고 전문적인 것이 아니라 '나'자신을
					관찰하고 그것을 밖으로 표현하는 그림입니다...</a></li>
			<!-- Gauge -->
			<li class="gauge_box w_auto mt5">
				<dl>
					<dd class="gauge_outline w170">
						<label class="gauge" style="width: 50%;"></label>
					</dd>
					<dd class="process">(6/10)</dd>
				</dl></li>
			<!-- Gauge //-->
		</ul>

		<ul class="category_box end">
			<li class="photo"><a href=""> <img width="218" height="148"
					src="../images/course_01.jpg"> </a></li>
			<li class="subject"><a href="">어느 젊은 마법사의 코스</a></li>
			<li class="maker"><a href="">SERA캠퍼스</a></li>
			<li class="info">
				<dl>
					<dd class="menteeNo">34명</dd>
					<dd class="makeDate">2012.02.07</dd>
					<dd class="category"></dd>
				</dl></li>
			<li class="detail"><a href="/Course/Detail/212">안녕하세요
					선린인터넷고등학교 여러분! 세라캠퍼스에 오신걸 환영합니다. ^^* 꿈그리기란, 어렵고 전문적인 것이 아니라 '나'자신을
					관찰하고 그것을 밖으로 표현하는 그림입니다...</a></li>
			<!-- Gauge -->
			<li class="gauge_box w_auto mt5">
				<dl>
					<dd class="gauge_outline w170">
						<label class="gauge" style="width: 50%;"></label>
					</dd>
					<dd class="process">(6/10)</dd>
				</dl></li>
			<!-- Gauge //-->
		</ul>

		<ul class="category_box">
			<li class="photo"><a href=""> <img width="218" height="148"
					src="../images/course_01.jpg"> </a></li>
			<li class="subject"><a href="">어느 젊은 마법사의 코스</a></li>
			<li class="maker"><a href="">SERA캠퍼스</a></li>
			<li class="info">
				<dl>
					<dd class="menteeNo">34명</dd>
					<dd class="makeDate">2012.02.07</dd>
					<dd class="category"></dd>
				</dl></li>
			<li class="detail"><a href="/Course/Detail/212">안녕하세요
					선린인터넷고등학교 여러분! 세라캠퍼스에 오신걸 환영합니다. ^^* 꿈그리기란, 어렵고 전문적인 것이 아니라 '나'자신을
					관찰하고 그것을 밖으로 표현하는 그림입니다...</a></li>
			<!-- Gauge -->
			<li class="gauge_box w_auto mt5">
				<dl>
					<dd class="gauge_outline w170">
						<label class="gauge" style="width: 50%;"></label>
					</dd>
					<dd class="process">(6/10)</dd>
				</dl></li>
			<!-- Gauge //-->
		</ul>

		<ul class="category_box">
			<li class="photo"><a href=""> <img width="218" height="148"
					src="../images/course_01.jpg"> </a></li>
			<li class="subject"><a href="">어느 젊은 마법사의 코스</a></li>
			<li class="maker"><a href="">SERA캠퍼스</a></li>
			<li class="info">
				<dl>
					<dd class="menteeNo">34명</dd>
					<dd class="makeDate">2012.02.07</dd>
					<dd class="category"></dd>
				</dl></li>
			<li class="detail"><a href="/Course/Detail/212">안녕하세요
					선린인터넷고등학교 여러분! 세라캠퍼스에 오신걸 환영합니다. ^^* 꿈그리기란, 어렵고 전문적인 것이 아니라 '나'자신을
					관찰하고 그것을 밖으로 표현하는 그림입니다...</a></li>
			<!-- Gauge -->
			<li class="gauge_box w_auto mt5">
				<dl>
					<dd class="gauge_outline w170">
						<label class="gauge" style="width: 50%;"></label>
					</dd>
					<dd class="process">(6/10)</dd>
				</dl></li>
			<!-- Gauge //-->
		</ul>

		<ul class="category_box end">
			<li class="photo"><a href=""> <img width="218" height="148"
					src="../images/course_01.jpg"> </a></li>
			<li class="subject"><a href="">어느 젊은 마법사의 코스</a></li>
			<li class="maker"><a href="">SERA캠퍼스</a></li>
			<li class="info">
				<dl>
					<dd class="menteeNo">34명</dd>
					<dd class="makeDate">2012.02.07</dd>
					<dd class="category"></dd>
				</dl></li>
			<li class="detail"><a href="/Course/Detail/212">안녕하세요
					선린인터넷고등학교 여러분! 세라캠퍼스에 오신걸 환영합니다. ^^* 꿈그리기란, 어렵고 전문적인 것이 아니라 '나'자신을
					관찰하고 그것을 밖으로 표현하는 그림입니다...</a></li>
			<!-- Gauge -->
			<li class="gauge_box w_auto mt5">
				<dl>
					<dd class="gauge_outline w170">
						<label class="gauge" style="width: 50%;"></label>
					</dd>
					<dd class="process">(6/10)</dd>
				</dl></li>
			<!-- Gauge //-->
		</ul>
	</div>
	<!-- 리스트1 //-->

	<!-- 더보기 -->
	<div class="more cb">
		<div class="icon_more">더보기</div>
	</div>
	<!-- 더보기 //-->
</div>
<!-- Content Section2 //-->

