<%@page import="net.smartworks.model.security.AccessPolicy"%>
<%@page import="net.smartworks.model.instance.info.AsyncMessageInstanceInfo"%>
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
	int spaceType = Integer.parseInt(request.getParameter("spaceType"));
	String spaceId = request.getParameter("spaceId");
%>

<script type="text/javascript">

	// 개인정보프로파일 수정하기 버튼을 클릭하면, 
	// 모든정보를 JSON형식으로 Serialize해서 서버의 update_my_profile.sw 서비스를 호출하여 수정한다.
	function submitForms(e) {
		var seraNote = $('.js_sera_note_page');
		if (SmartWorks.GridLayout.validate(seraNote.find('form.js_validation_required'),  seraNote.find('.sw_error_message'))) {
			var forms = seraNote.find('form');
			var paramsJson = {};
			paramsJson['spaceType'] = seraNote.attr('spaceType');
			paramsJson['spaceId'] = seraNote.attr('spaceId');
			for(var i=0; i<forms.length; i++){
				var form = $(forms[i]);
				if(form.attr('name') === 'frmSmartForm'){
					paramsJson['formId'] = form.attr('formId');
					paramsJson['formName'] = form.attr('formName');
				}
				paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
			}
			console.log(JSON.stringify(paramsJson));
			var progressSpan = seraNote.find('.js_progress_span');
			smartPop.progressCont(progressSpan);
			var url = "create_sera_note.sw";
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
					smartPop.showInfo(smartPop.ERROR, smartMessage.get('createSeraNoteError'));
				}
			});
		}
	};
</script>

<div class="js_sera_note_page" spaceType="<%=spaceType%>" spaceId="<%=spaceId%>">
	<form name="frmCreateSeraNote" class="js_validation_required">	
		<div class="comment_txt">
			<textarea name="txtNoteContent" class="required" rows="5" placeholder="남기고 싶은 이야기를 적어 주세요!"></textarea>
		</div>
	
		<div class="cb mt6">
			
			<table class="js_note_attachment_table cb attach_file_detail" border="0" cellspacing="0" cellpadding="0" style="display:none">
				<tr class="js_note_file" style="display:none">
					<td>
						<div class="form_label">첨부파일</div>
						<div class="form_value">
							<div class="js_note_file_field"></div>
						</div>
					</td>
				</tr>
				<tr class="js_note_video" style="display:none">
					<td>
						<div class="form_label">동영상</div>
						<div class="form_value">
							<div class="js_note_video_field"></div>
						</div>
					</td>
				</tr>
				<tr class="js_note_image" style="display:none">
					<td>
						<div class="form_label">이미지</div>
						<div class="form_value">
							<div class="js_note_image_field"></div>
						</div>
					</td>
				</tr>
				<tr class="js_note_link" style="display:none">
					<td>
						<div class="form_label">링크</div>
						<div class="form_value">
							<input name="txtNoteUrl" type="text" class="fieldline url" style="width:450px;display:block"/>
						</div>
					</td>
				</tr>
			</table>
			
	
			<div class="sw_error_message tl" style="color: red"></div>
			<!-- 우측 버튼 영역 -->

			<div class="attach_file js_note_buttons">
				<ul>
					<li>
						<span class="insert_text">500</span>
					</li>
					<li class="ml5">
						<select name="selAccessLevel" >
						<option value="<%=AccessPolicy.LEVEL_PUBLIC%>">전체공개</option>
						<option value="<%=AccessPolicy.LEVEL_PRIVATE%>">비공개</option>
						</select>
					</li>
					<li class="icon_memo ml10"><a href="" class="js_note_file_btn" title="첨부파일"> </a></li>
					<li class="icon_video"><a href="" class="js_note_video_btn" title="동영상"> </a></li>
					<li class="icon_photo"><a href="" class="js_note_image_btn" title="사진"> </a></li>
					<li class="icon_link"><a href="" class="js_note_link_btn" title="링크"> </a></li>
					<!-- Btn 등록-->
					<li class="btn_default_l ml10 js_create_note_btn">
						<div class="btn_default_r">등록</div>
					</li>
					<!-- Btn 등록//-->
				</ul>
			</div>

			<!-- 우측 버튼 영역 //-->
		</div>
	</form>
</div>

<script type="text/javascript">
	loadSeraNoteFields();
</script>
