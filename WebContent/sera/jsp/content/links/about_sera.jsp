<%@page import="net.smartworks.model.sera.Course"%>
<%@page import="net.smartworks.model.sera.info.CourseInfo"%>
<%@page import="net.smartworks.model.sera.CourseList"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
%>

<div id="course_list_section">
	<!-- SNB Left -->
	<div class="snb2">
		<ul class="snb_menu">
			<li><img height="112" width="149"
				src="sera/images/function_title10.gif"></li>
			<li class="selected"><a href="">SERA는</a></li>
			<li class=""><a href="">SERA의 철학</a></li>
			<li class=""><a href="">SERA의 사업</a></li>
			<li class=""><a href="">SERA의 Network</a></li>
			<li class=""><a href="">Contact Us</a></li>
		</ul>
	</div>
	<!-- SNB Left//-->
	<!-- Content -->
	<div id="content_list_section">
		<!-- About Content 001 -->
		<div>
			<h3>SERA는</h3>
			<div>
				<img src="sera/images/aboutSERA_intro1.jpg"> <img
					src="sera/images/aboutSERA_intro2.jpg"> <img
					src="sera/images/aboutSERA_intro3.jpg">
			</div>
		</div>
		<!-- About Content 001 //-->
		<!-- About Content 002 -->
		<div style="display: none">
			<h3>SERA의 철학</h3>
			<div>
				<img src="sera/images/aboutSERA_philo.jpg">
			</div>
		</div>
		<!-- About Content 002 //-->
		<!-- About Content 003 -->
		<div style="display: none">
			<h3>SERA의 사업</h3>
			<div>
				<img src="sera/images/aboutSERA_biz.jpg">
			</div>
		</div>
		<!-- About Content 003 //-->
		<!-- About Content 004 -->
		<div style="display: none">
			<h3>SERA의 Network</h3>
			<div>
				<img src="sera/images/aboutSERA_network.jpg">
			</div>
		</div>
		<!-- About Content 004 //-->
		<!-- About Content 005 -->
		<div style="display: none">
			<h3>Contact Us</h3>
			<div>
				<img src="sera/images/aboutSera_Contact us.jpg">
			</div>
		</div>
		<!-- About Content 005 //-->

	</div>
	<!-- Content //-->
</div>
