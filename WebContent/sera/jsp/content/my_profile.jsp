<%@page import="net.smartworks.model.sera.SeraUser"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.model.community.WorkSpace"%>
<%@page
	import="net.smartworks.model.instance.info.AsyncMessageInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.YTVideoInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.EventInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.BoardInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.CommentInstanceInfo"%>
<%@page import="net.smartworks.model.instance.WorkInstance"%>
<%@page import="net.smartworks.model.instance.info.WorkInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.MemoInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.ImageInstanceInfo"%>
<%@page import="net.smartworks.model.instance.Instance"%>
<%@page import="net.smartworks.model.instance.info.InstanceInfo"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");

	User cUser = SmartUtil.getCurrentUser();
	SeraUser seraUser = smartWorks.getSeraUserById(cUser.getId());
%>
<script type="text/javascript">

	// 개인정보프로파일 수정하기 버튼을 클릭하면, 
	// 모든정보를 JSON형식으로 Serialize해서 서버의 update_my_profile.sw 서비스를 호출하여 수정한다.
	function submitForms(e) {
		var myProfile = $('.js_my_profile_page');
		if (SmartWorks.GridLayout.validate(myProfile.find('form.js_validation_required'), $('.js_profile_error_message'))) {
			var forms = myProfile.find('form');
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
			var url = "update_sera_profile.sw";
			var progressSpan = myProfile.find('.js_progress_span');
			smartPop.progressCont(progressSpan);
			$.ajax({
				url : url,
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					// 사용자정보 수정이 정상적으로 완료되었으면, 현재 페이지에 그대로 있는다.
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.INFO, smartMessage.get('setMyProfileSucceed'));
				},
				error : function(e) {
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, smartMessage.get('setMyProfileError'));
				}
			});
		}
	};
</script>

<div class="js_my_profile_page" userId="<%=seraUser.getId()%>">
	<!-- Header Title -->
	<div class="header_tit">
		<div class="tit_account">닉네임, 프로필 사진, 비밀번호 등 프로필 정보를 수정할 수 있습니다.</div>
	</div>
	<!-- Header Title //-->
	<!-- Input Section -->
	<form name="frmSeraProfile" class="form_layout js_validation_required">
		<div class="t_refe mt10">* 선택입력란입니다</div>
		<table style="padding: 10px 25px">
			<tr>
				<td rowspan="12" valign="top" width="150px">
					<!-- 사진 올리기 -->
					<div class="js_sera_profile_field js_auto_load_profile myaccount_photo"></div>
					<div class="t_refe" style="margin: 15px 0 0 13px">
						* 사진은 자동으로<br /> 118x118으로 변경됩니다
					</div> <!-- 사진 올리기 //-->
				</td>
				<td>
					<div class="form_label">이름</div>
					<div class="form_value">
						<span class="t_blueb"><%=seraUser.getName() %></span>
					</div>
				</td>
	
			</tr>
			<tr>
				<td>
					<div class="form_label">회원ID</div>
					<div class="form_value">
						<span class="t_blueb"><%=seraUser.getId() %></span>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label">닉네임*</div>
					<div class="form_value">
						<input name="txtNickName" type="text" class="fieldline fl mr5" style="width: 150px" value="<%=CommonUtil.toNotNull(seraUser.getNickName())%>">
						<div class="t_refe mt5 cb">* 닉네임은 한/영.숫자 최대 15자까지 가능합니다.</div>
					</div>
					<div class="cb t_red pt5">* 닉네임을 입력하시면 닉네임으로, 그렇지 않으면 이름으로 표시됩니다.</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label">이메일</div>
					<div class="form_value">
						<input name="txtEmail" type="text" class="fieldline email" style="width: 150px" value="<%=seraUser.getEmail()%>"/>
						<div class="t_refe mt5">* 정보수신에 대한 이메일 변경입니다.</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label">생일/성별</div>
					<div class="form_value">
						<%
						LocalDate birthDate = seraUser.getBirthday();
						String birthYear = (SmartUtil.isBlankObject(birthDate)) ? "" : birthDate.toLocalYearString();
						String birthMonth = (SmartUtil.isBlankObject(birthDate)) ? "" : birthDate.toLocalMonthOnlyString();
						String birthDay = (SmartUtil.isBlankObject(birthDate)) ? "" : birthDate.toLocalDateOnlyString();
						%>
						<input name="txtBirthYear" class="fieldline form_date_input number tr" type="text" value="<%=birthYear%>"/> 년
						<input name="txtBirthMonth" class="fieldline form_date_input number tr" type="text" value="<%=birthMonth%>"/> 월
						<input name="txtBirthDay" class="fieldline form_date_input number tr" type="text" value="<%=birthDay%>"/> 일
						<select name="selSex" class="required">
							<option <%if(seraUser.getSex()==SeraUser.SEX_FEMALE){ %>selected<%} %> value="<%=SeraUser.SEX_FEMALE %>">여자</option>
							<option <%if(seraUser.getSex()==SeraUser.SEX_MALE){ %>selected<%} %> value="<%=SeraUser.SEX_MALE%>">남자</option>
						</select>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label">도전목표*</div>
					<div class="form_value">
						<input class=" fieldline" name="txtGoal" type="text" value="<%=CommonUtil.toNotNull(seraUser.getGoal()) %>" style="width: 300px" />
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label">관심분야</div>
					<div class="form_value">
						<input class=" fieldline" name="txtInterests" type="text" value="<%=CommonUtil.toNotNull(seraUser.getInterests()) %>" style="width: 300px" />
						<div class="cb t_refe pt5">* 관심있는 키워드를 코마(,)로 구분하여 입력해 주세요.</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label">학 교*</div>
					<div class="form_value">
						<input name="txtEducations" type="text" class="fieldline fl" style="width: 250px" placeholder="내가 나온 학교를 입력해주세요" value="<%=CommonUtil.toNotNull(seraUser.getEducations()) %>" />
						<div class="btn_mid_l ml5"><div class="btn_mid_r">추가</div></div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label">직 장*</div>
					<div class="form_value">
						<input name="txtWorks" type="text" class="fieldline fl" style="width: 250px" placeholder="내가 다닌 직장을 입력해주세요" value="<%=CommonUtil.toNotNull(seraUser.getWorks())%>"/>
						<div class="btn_mid_l ml5">
							<div class="btn_mid_r">추가</div>
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label">SNS연동 *</div>
					<div class="form_value">
						<div class="icon_facebook fl mr5"></div>
						<div class="icon_twitter fl mr5"></div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label">비밀번호</div>
					<div class="form_value">
						<input name="txtPassword" type="password" class="fieldline fl" style="width: 100px" value="<%=seraUser.getPassword() %>" />
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form_label">비밀번호 확인</div>
					<div class="form_value">
						<input name="txtConfirmPassword" type="password" class="fieldline" style="width: 100px" value="<%=seraUser.getPassword() %>" />
					</div>
				</td>
			</tr>
		</table>
	</form>
	<!-- Input Section //-->
	<!-- Btn -->
	<div class="btn_space">
		<div style="clear: both; display: inline-block">
			<div class="btn_blu_l mr10 js_modify_profile_btn">
				<div class="btn_blu_r">적 용</div>
			</div>
			<div href="myPAGE.sw" class="btn_blu_l mr10 js_sera_content">
				<div class="btn_blu_r">취 소</div>
			</div>
			<div class="btn_red_l js_leave_sera_btn">
				<div class="btn_red_r">회원탈퇴</div>
			</div>
		</div>
	</div>
	<!-- Btn //-->
</div>

<script type="text/javascript">
	loadSeraProfileField();
</script>
