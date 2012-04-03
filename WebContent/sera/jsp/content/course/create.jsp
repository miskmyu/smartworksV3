<%@page import="net.smartworks.model.security.AccessPolicy"%>
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
					smartPop.closeProgress();
					document.location.href = data.href;
				},
				error : function(e) {
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, smartMessage.get('createCourseError'));
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
					<div class="form_label w101">코스 제목</div>
					<div class="form_value">
						<input name="txtCourseName" type="text" class="fieldline fl required" style="width: 490px">
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label w101">코스 목적</div>
					<div class="form_value">
						<input name="txtCourseObject" type="text" class="fieldline fl required" style="width: 490px">
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label w101">상세설명</div>
					<div class="form_value">
						<textarea name="txtaCourseDesc" class="fieldline fl" name="textarea" rows="3" style="width: 490px"></textarea>
						<div class="cb t_refe pt2">* 선택입력란입니다</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label w101">이미지</div>
					<div class="form_value">
						<div class="js_course_profile_field js_auto_load_course_profile"></div>
						<div class="cb t_refe pt2">* 이미지를 등록하지 않은 경우 SERA에서 제공하는 기본 이미지가 제공되어 보여집니다</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label w101">카테고리</div>
					<div class="form_value">
						<input name="chkCourseCategories" type="checkbox" value="예술" />예술
						<input name="chkCourseCategories" type="checkbox" value="엔터테인먼트" />엔터테인먼트
						<input name="chkCourseCategories" type="checkbox" value="스타일" />스타일
						<input name="chkCourseCategories" type="checkbox" value="생활" />생활
						<input name="chkCourseCategories" type="checkbox" value="영화/애니메이션" />영화/애니메이션
						<input name="chkCourseCategories" type="checkbox" value="게임" />게임<br />
						<input name="chkCourseCategories" type="checkbox" value="영화" />영화
						<input name="chkCourseCategories" type="checkbox" value="이벤트" />이벤트
						<input name="chkCourseCategories" type="checkbox" value="스포츠" />스포츠
						<input name="chkCourseCategories" type="checkbox" value="이슈" />이슈
						<input name="chkCourseCategories" type="checkbox" value="시사" />시사
						<input name="chkCourseCategories" type="checkbox" value="경제" />경제
						<input name="chkCourseCategories" type="checkbox" value="비즈니스" />비즈니스
						<input name="chkCourseCategories" type="checkbox" value="미디어" />미디어<br />
						<input name="chkCourseCategories" type="checkbox" value="환경" />환경
						<input name="chkCourseCategories" type="checkbox" value="동물" />동물
						<input name="chkCourseCategories" type="checkbox" value="비영리/사회운동" />비영리/사회운동
						<input name="chkCourseCategories" type="checkbox" value="역사" />역사
						<input name="chkCourseCategories" type="checkbox" value="문학" />문학
						<input name="chkCourseCategories" type="checkbox" value="심리" />심리
						<input name="chkCourseCategories" type="checkbox" value="인물" />인물<br /> 
						<input name="chkCourseCategories" type="checkbox" value="과학" />과학
						<input name="chkCourseCategories" type="checkbox" value="첨단기술" />첨단기술
						<input name="chkCourseCategories" type="checkbox" value="의학" />의학
						<input name="chkCourseCategories" type="checkbox" value="건축" />건축
						<input name="chkCourseCategories" type="checkbox" value="교육" />교육
						<input name="chkCourseCategories" type="checkbox" value="기타" />기타
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label w101">키워드</div>
					<div class="form_value">
						<input name="txtCourseKeywords" type="text" style="width: 490px" class="fieldline fl required" />
						<div class="cb t_refe pt2">* 코스의 이해를 도울수 있는 키워드 입력 (코스 검색 또는 추천코스에 사용 됨), 2개이상은 콤마(,)로 구분</div>
					</div>
				</td>
			</tr>
			<tr>
				<td><div class="form_label w101">코스 기간</div>
					<div class="form_value">
						<input name="txtCourseDays" type="text" style="width: 100px" class="fieldline fl tr number" />
						<div class="fl mt2 ml2"> 일(Days)</div>
						<div class="fl ml10">
							<input class="fl pt2" name="chkUserDefineDays" type="checkbox"/>
							<label class="fl">사용자 정의</label>
							<div class="fl js_course_start_date_field" width="110px"></div>
							<div class="fl mr10">~</div> 
							<div class="fl js_course_end_date_field" width="110px"></div>
						</div>
						<div class="cb t_refe">* 코스 기간은 6개월 이내로 설정해 주세요</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label w101">공개 설정</div>
					<div class="form_value">
						<input name="chkCourseSecurity" type="radio" checked value="<%=AccessPolicy.LEVEL_PUBLIC %>" /><label>공개</label>
						<input name="chkCourseSecurity" type="radio" value="<%=AccessPolicy.LEVEL_PRIVATE %>" />비공개
						<div class="cb t_refe pt2">* 비공개 코스 설정 시 코스의 제반 내용이 노출되지 않습니다</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label w101">멘티 인원 제한</div>
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
					<div class="form_label w101">멘티 가입 승인</div>
					<div class="form_value">
						<input name="chkJoinApproval" type="radio" checked value="autoApporval" /><label>자동승인</label>
						<input name="chkJoinApproval" type="radio" value="mentorApproval" />멘토승인
						<div class="cb t_refe pt2">* 회원이 코스를 참여하는 데 있어 승인절차를 선택할 수 있습니다</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label w101">코스 유료 설정</div>
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
					<div class="js_course_mentor_field"></div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label">학 력</div>
					<div class="form_value w580">
						<textarea name="txtaMentorEducations" class="fieldline required" name="textarea" rows="3"></textarea>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label">경 력</div>
					<div class="form_value w580">
						<textarea name="txtaMentorWorks" class="fieldline required" name="textarea" rows="3" value=""></textarea>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label">세라활동 *</div>
					<div class="form_value" style="width: 580px">
						<div class="fl" style="width: 285px">
							<textarea name="txtaMentorHistory" class="fieldline" name="" rows="3" value="">멘토활동</textarea>
						</div>
						<div class="fr" style="width: 285px">
							<textarea name="txtaMenteeHistory" class="fieldline" name="" rows="3" value="">멘티활동</textarea>
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label">강의활동 *</div>
					<div class="form_value w580">
						<textarea name="txtaMentorLectures" class="fieldline" name="textarea" rows="3" value=""></textarea>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label">수상경력*</div>
					<div class="form_value w580">
						<textarea name="txtaMentorAwards" class="fieldline" name="textarea" rows="3" value=""></textarea>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label">기타활동*</div>
					<div class="form_value w580">
						<textarea name="txtaMentorEtc" class="fieldline" name="textarea" rows="3" value=""></textarea>
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
				<div class="btn_blu_r">코스 만들기</div>
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
