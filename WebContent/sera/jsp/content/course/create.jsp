<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.sera.Mentor"%>
<%@page import="net.smartworks.model.security.AccessPolicy"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
	Mentor mentor = smartWorks.getMentorById(cUser.getId());
%>

<script type="text/javascript">

	// 개인정보프로파일 수정하기 버튼을 클릭하면, 
	// 모든정보를 JSON형식으로 Serialize해서 서버의 update_my_profile.sw 서비스를 호출하여 수정한다.
	function submitForms(e) {
		var createCourse = $('.js_create_course_page');
		if (SmartWorks.GridLayout.validate(createCourse.find('form.js_validation_required'),  createCourse.find('.sw_error_message'))) {
			var forms = createCourse.find('form');
			var paramsJson = {};
			for(var i=0; i<forms.length; i++){
				var form = $(forms[i]);
				if(form.attr('name') === 'frmSmartForm'){
					paramsJson['formId'] = form.attr('formId');
					paramsJson['formName'] = form.attr('formName');
				}
				paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
			}
			console.log(JSON.stringify(paramsJson));
			var progressSpan = createCourse.find('.js_progress_span');
			smartPop.progressCont(progressSpan);
			var url = "create_new_course.sw";
			$.ajax({
				url : url,
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					// 사용자정보 수정이 정상적으로 완료되었으면, 현재 페이지에 그대로 있는다.
					smartPop.showInfo(smartPop.INFO, '코스가 정상적으로 생성되었습니다!', function(){
						document.location.href = data.href;						
						smartPop.closeProgress();
					});
				},
				error : function(e) {
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, '코스를 생성하는데 문제가 발생하였습니다. 관리자에게 문의하시기 바랍니다!');
				}
			});
		}
	};
</script>

<div class="js_create_course_page">
	<!-- Header Title -->
	<div class="header_tit js_create_title">
		<div class="tit_newcourse">
			당신의 특별한 SERA Course 시작해보세요! 내 느낌과 내 스타일대로 만들고 싶은 코스를 자유롭게 만들어 보세요!<br />
			잠깐 스치는 생각, 다양한 기분과 이야기도 세라에선 당신만의 특별한 Story가 됩니다.
		</div>
		<div class="tit_dep2">
			<h2>코스 만들기</h2>
			<div>
				지적재산권, 음란물, 청소년 유해매체를 포함한 타인의 권리를 침해하는 자료 등 이용약관에 명시한 불법게시물을 <br />
				올리실 경우 경고 없이 삭제될 수있으며, 서비스 이용 제한 및 법적인 처벌을 받을 수 있습니다.
			</div>
		</div>
	</div>
	<div class="header_tit js_mentor_title" style="display:none">
		<div class="tit_newcourse">
			특별한 코스의 멘토가 누구인지 차근차근 알려주는 멘토 프로필!<br /> 멘티와 친구들이 공유하는 멘토와의 만남입니다.
		</div>
		<div class="tit_dep2">
			<h2>멘토 프로필 작성</h2>
			<div>
				사실이 아닌 자료 등을 올리 실 경우 경고가 이루어질 수 있으며, 서비스 이용 제한 등 불이익을 받을 수 있습니다.<br />
				<span class="t_refe">* 선택입력란입니다.</span>
			</div>
		</div>
	</div>
	<!-- Header Title //-->
	<!-- Input Form -->
	<form name="frmCreateCourse" class="form_layout js_validation_required">
		<table class="js_create_course_table" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<div class="form_label w110">코스 제목</div>
					<div class="form_value w540">
						<input name="txtCourseName" type="text" class="fieldline fl required">
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label w110">코스 목적</div>
					<div class="form_value w540">
						<input name="txtCourseObject" type="text" class="fieldline fl required">
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label w110">상세설명</div>
					<div class="form_value w540">
						<textarea name="txtaCourseDesc" class="fieldline fl" name="textarea" rows="7" style="width: 100%"></textarea>
						<div class="cb t_refe pt2">* 선택입력란입니다</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label w110">이미지</div>
					<div class="form_value">
						<div class="js_course_profile_field js_auto_load_course_profile"></div>
						<div class="cb t_refe pt2">* 이미지를 등록하지 않은 경우 SERA에서 제공하는 기본 이미지가 제공되어 보여집니다</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label w110">카테고리</div>
					<div class="form_value">
						<input name="chkCourseCategories" type="radio" value="예술" /><label>예술</label>
						<input name="chkCourseCategories" type="radio" value="엔터테인먼트" /><label>엔터테인먼트</label>
						<input name="chkCourseCategories" type="radio" value="스타일" /><label>스타일</label>
						<input name="chkCourseCategories" type="radio" value="생활" /><label>생활</label>
						<input name="chkCourseCategories" type="radio" value="영화/애니메이션" /><label>영화/애니메이션</label>
						<input name="chkCourseCategories" type="radio" value="게임" /><label>게임</label><br />
						<input name="chkCourseCategories" type="radio" value="영화" /><label>영화</label>
						<input name="chkCourseCategories" type="radio" value="이벤트" /><label>이벤트</label>
						<input name="chkCourseCategories" type="radio" value="스포츠" /><label>스포츠</label>
						<input name="chkCourseCategories" type="radio" value="이슈" /><label>이슈</label>
						<input name="chkCourseCategories" type="radio" value="시사" /><label>시사</label>
						<input name="chkCourseCategories" type="radio" value="경제" /><label>경제</label>
						<input name="chkCourseCategories" type="radio" value="비즈니스" /><label>비즈니스</label>
						<input name="chkCourseCategories" type="radio" value="미디어" /><label>미디어</label><br />
						<input name="chkCourseCategories" type="radio" value="환경" /><label>환경</label>
						<input name="chkCourseCategories" type="radio" value="동물" /><label>동물</label>
						<input name="chkCourseCategories" type="radio" value="비영리/사회운동" /><label>비영리/사회운동</label>
						<input name="chkCourseCategories" type="radio" value="역사" /><label>역사</label>
						<input name="chkCourseCategories" type="radio" value="문학" /><label>문학</label>
						<input name="chkCourseCategories" type="radio" value="심리" /><label>심리</label>
						<input name="chkCourseCategories" type="radio" value="인물" /><label>인물</label><br /> 
						<input name="chkCourseCategories" type="radio" value="과학" /><label>과학</label>
						<input name="chkCourseCategories" type="radio" value="첨단기술" /><label>첨단기술</label>
						<input name="chkCourseCategories" type="radio" value="의학" /><label>의학</label>
						<input name="chkCourseCategories" type="radio" value="건축" /><label>건축</label>
						<input name="chkCourseCategories" type="radio" value="교육" /><label>교육</label>
						<input name="chkCourseCategories" type="radio" value="기타" /><label>기타</label>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label w110">키워드</div>
					<div class="form_value w540">
						<input name="txtCourseKeywords" type="text" class="fieldline fl required" />
						<div class="cb t_refe pt2">* 코스의 이해를 도울수 있는 키워드 입력 (코스 검색 또는 추천코스에 사용 됨), 2개이상은 콤마(,)로 구분</div>
					</div>
				</td>
			</tr>
			<tr>
				<td><div class="form_label w110">코스 기간</div>
					<div class="form_value">
						<input name="txtCourseDays" type="text" style="width: 100px" class="fieldline fl tr number" />
						<div class="fl mt2 ml2"> 일(Days)</div>
						<div class="t_refe pt2 fl"> * 코스 기간은 6개월 이내로 설정해 주세요</div>
						
						<div class="cb pt10">
							<input class="fl pt2" name="chkUserDefineDays" type="checkbox"/>
							<label class="fl">사용자 정의</label>
							<div class="fl js_course_start_date_field"></div>
							<div class="fl mr5" style="line-height: 20px"> ~ </div> 
							<div class="fl js_course_end_date_field"></div>
						</div>
						<div class="cb t_refe pt2">* 정해진 날짜를 통해 코스를 진행해야 하는 경우 코스 상세기간 입력으로 해당 날짜를 입력해주세요</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label w110">공개 설정</div>
					<div class="form_value">
						<input name="chkCourseSecurity" type="radio" checked value="<%=AccessPolicy.LEVEL_PUBLIC %>" /><label>공개</label>
						<input name="chkCourseSecurity" type="radio" value="<%=AccessPolicy.LEVEL_PRIVATE %>" />비공개
						<div class="cb t_refe pt2">* 비공개 코스 설정 시 코스의 제반 내용이 노출되지 않습니다</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label w110">멘티 인원 제한</div>
					<div class="form_value">
						<input name="chkCourseUsers" type="radio" checked value="unlimited" /><label>무제한</label> 
						<input name="chkCourseUsers" type="radio" value="userInput" />직접입력 
						<input name="txtCourseUsers" class="fieldline number tr" type="text" style="width: 80px" /> 명
						<div class="cb t_refe pt2">* 코스 인원 제한 시 정해진 인원만 코스를 이용할 수 있습니다</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label w110">멘티 가입 승인</div>
					<div class="form_value">
						<input name="chkJoinApproval" type="radio" checked value="autoApporval" /><label>자동승인</label>
						<input name="chkJoinApproval" type="radio" value="mentorApproval" />멘토승인
						<div class="cb t_refe pt2">* 회원이 코스를 참여하는 데 있어 승인절차를 선택할 수 있습니다</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label w110">코스 유료 설정</div>
					<div class="form_value">
						<input name="chkCourseFee" type="radio" checked value="free" /><label>무료 </label>
						<input name="chkCourseFee" type="radio" value="pay" />유료 (금액 입력: 
						<input name="txtCourseFee" class="fieldline tr" type="text" style="width: 80px" /> 원)
						<div class="cb t_refe pt2">* 코스 유료화는 관리자 승인 후 개설이 가능하므로 유료버전의 코스인 경우 바로 코스 생성이 되지 않습니다</div>
					</div>
				</td>
			</tr>
		</table>
		<table class="js_mentor_profile_table" border="0" cellspacing="0" cellpadding="0" style="display:none">
			<tr>
				<td>
					<div class="form_label">멘토이름</div>
					<div class="form_value"><img class="profile_size_s" style="line-height:20px" src="<%=cUser.getMinPicture()%>"/><%=cUser.getNickName()%></div>
					<input name="txtCourseMentor" type="hidden" value="<%=cUser.getId() %>"/>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label">학 력</div>
					<div class="form_value w570">
						<textarea name="txtaMentorEducations" class="fieldline required" name="textarea" rows="3"><%=CommonUtil.toNotNull(mentor.getEducations()) %></textarea>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label">경 력</div>
					<div class="form_value w570">
						<textarea name="txtaMentorWorks" class="fieldline required" name="textarea" rows="3"><%=CommonUtil.toNotNull(mentor.getWorks()) %></textarea>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label">세라활동 *</div>
					<div class="form_value" style="width: 580px">
						<div class="fl" style="width: 285px">
							<textarea name="txtaMentorHistory" class="fieldline" name="" rows="3" value="" placeholder="멘토활동"><%=CommonUtil.toNotNull(mentor.getMentorHistory()) %></textarea>
						</div>
						<div class="fr" style="width: 285px">
							<textarea name="txtaMenteeHistory" class="fieldline" name="" rows="3" placeholder="멘티활동"><%=CommonUtil.toNotNull(mentor.getMenteeHistory()) %></textarea>
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label">강의활동 *</div>
					<div class="form_value w570">
						<textarea name="txtaMentorLectures" class="fieldline" name="textarea" rows="3"><%=CommonUtil.toNotNull(mentor.getLectures()) %></textarea>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label">수상경력*</div>
					<div class="form_value w570">
						<textarea name="txtaMentorAwards" class="fieldline" name="textarea" rows="3"><%=CommonUtil.toNotNull(mentor.getAwards()) %></textarea>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label">기타활동*</div>
					<div class="form_value w570">
						<textarea name="txtaMentorEtc" class="fieldline" name="textarea" rows="3"><%=CommonUtil.toNotNull(mentor.getEtc()) %></textarea>
					</div>
				</td>
			</tr>
		</table>
	</form>
	<div class="sw_error_message tl" style="color: red"></div>
	
	<!-- Btn -->
	<div class="btn_space">
		<div class="js_create_buttons" style="clear: both; display: inline-block">
			<div href="createCourse.sw" class="btn_blu_l mr10 js_sera_content">
				<div class="btn_blu_r">취 소</div>
			</div>
			<div href="" class="btn_blu_l js_mentor_form_btn">
				<div class="btn_blu_r">멘토 프로필 입력하기</div>
			</div>
		</div>
		<div class="js_mentor_buttons" style="clear: both; display: none">
			<div href="" class="btn_blu_l mr10 js_create_course_btn">
				<div class="btn_blu_r">코스 등록</div>
			</div>
	
			<div href="" class="btn_blu_l js_back_to_create_btn">
				<div class="btn_blu_r">취 소</div>
			</div>
		</div>
	</div>
	<!-- Btn //-->
</div>
<!-- Input Form //-->

<script type="text/javascript">
	loadCreateCourseFields();
</script>
