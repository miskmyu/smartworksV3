<%@page import="net.smartworks.server.engine.common.model.Order"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="net.smartworks.server.engine.common.manager.IManager"%>
<%@page import="net.smartworks.server.engine.process.process.model.PrcProcess"%>
<%@page import="net.smartworks.server.engine.process.process.model.PrcProcessCond"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.server.engine.factory.SwManagerFactory"%>
<%  
	PrcProcessCond cond = new PrcProcessCond();
	cond.setStatus("DEPLOYED");
	cond.setOrders(new Order[]{new Order(PrcProcess.A_NAME, true)});	// 가나다라순
	PrcProcess[] process = SwManagerFactory.getInstance().getPrcManager().getProcesses("", cond, null);
	String[] processName = new String[process.length];
	String[] processId = new String[process.length];
	for (int i = 0 ; i < process.length ; i ++){
		processId[i] = process[i].getProcessId();
		processName[i] = process[i].getName();
	}
%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="true"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link href="../css/default.css" type="text/css" rel="stylesheet" />
<link href="../css/black/detail.css" type="text/css" rel="stylesheet" />
<link href="../css/black/chat.css" type="text/css" rel="stylesheet" />
<link href="../css/black/form.css" type="text/css" rel="stylesheet" />
<link href="../css/black/pop.css" type="text/css" rel="stylesheet" />
<link href="../css/black/media.css" type="text/css" rel="stylesheet"/>
<link href="../css/ui-lightness/jquery-ui-1.8.16.custom.css" rel="stylesheet" type="text/css" title="ui-theme" />
<link href="../css/ext/ext-all.css" type="text/css" rel="stylesheet" />
<link href="../smarteditor/css/default_kor.css" rel="stylesheet" type="text/css" />
<link rel="shortcut icon" href="../images/favicon/smartworks.ico"/>
<!--[if lte IE 8]><link rel="stylesheet" href="css/black/ie8.css" type="text/css" media="all"><![endif]-->
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="../js/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="../smarteditor/js/HuskyEZCreator.js" charset="utf-8"></script>
<script type="text/javascript" src="./js/jquery.corner.js"></script>
<title>메일 관리</title>
</head>
<!-- 스크립트 영역 -->
<script type="text/javascript">
var field_data = "";
$(document).ready(function(){
	$("#process_select").corner();
	$("#form_select").corner();
	$("#field_select").corner();
	
	$("#add_div").hide();
	$("#save_click").css('cursor','pointer');
	$("#mail_click").css('cursor','pointer');
	var processArray = new Array();
	var processId= new Array();
	var processName= new Array();
	processArray = "<%=processName.length%>";
	var process = "";
	process = process + "<option value='0'>";
	process = process + "프로세스를 선택하세요";
	process = process + "</option>";
	process = process + "<option value='1'>";
	process = process + "정보관리업무선택";
	process = process + "</option>";
	// 프로세스를 가져다가 클라이언트에 적재합니다.
	for ( var i = 0 ; i < processArray.length ; i ++ ){
		<% for(int i=0;i< processName.length; i++){%>
			processName[i] = "<%=processName[i]%>";
			processId[i] = "<%=processId[i]%>";
			process = process + "<option value='"+processId[i]+"'>";
			process = process + processName[i];
			process = process + "</option>";
		<% }%>	
	}
	
	$("#process_select").append(process);
	
	$("#process_select").change(function() {		// 프로세스선택이 바뀌었을 경우에 
		if($("#process_select option:selected").val() == 0){
			$("select[name=form_select] option:not(option:eq(0))").remove();
			return;
		}
		ajax("./mail_value/mail_form.jsp?data="+$("#process_select option:selected").val()+"&method="+$("#process_select option:selected").val(),"form");		// 프로세스 Id 가져와서 서버로 갈것임.
	});
	$("#form_select").change(function() {		// 폼선택이 바뀌었을 경우에 
		if($("#form_select option:selected").val() == 0){
			$("select[name=form_select] option:not(option:eq(0))").remove();
			return;
		}
		ajax("./mail_value/mail_field.jsp?data="+$("#form_select option:selected").val()+"&method="+$("#process_select option:selected").val(),"field");		// 프로세스 Id 가져와서 서버로 갈것임.
	});
	
	$("#field_select").change(function() {		// 필드 내용이 바뀌었을 경우에
		if($("#process_select option:selected").val() == 0) {return;}
		if($("#form_select option:selected").val() == 0) {return;}
		if($("#field_select option:selected").text() == "필드를 선택하세요") {return;}
		
		// 저장내용 테이블 뿌리기
		$("#add_div").hide();
		$("#edit_div").hide();
		var text =     $("#process_select option:selected").text() + " > ";
		text = text +  $("#form_select option:selected").text() + " > ";
		text = text + $("#field_select option:selected").text();
		text = text + "　메일 관리 시작";
		$("#Modify_mail_text").text(text);		// Modify_mail_text <- id span에 뿌려준다
		$("#add_div").show(500);
		
		// 현재 프로세스 -> 폼 -> 필드에 대한 값 가져옵니다.
	 	
		if($("#mail_send_list tr").length == 0)
	 	{
	 		var  head = "<tr>";
			head = head + "<th width='20%' style='text-align: center;'>기준</th>";
			head = head + "<th width='10%' style='text-align: center;'>전송일</th>";
			head = head + "<th width='47%' style='text-align: center;'>조건</th>";
			head = head + "<th width='8%' style='text-align: center;'>메일편집</th>";
			head = head + "<th width='15%' style='text-align: center;'>삭제(바로삭제<input type='checkbox' id='warning' />)</th>";
			head = head + "</tr>";
			$("#mail_send_list").append(head);
	 	}else{
	 		$('#mail_send_list tr:not(:first)').remove();
	 	}
	 });
});
function ajax(uri,method){
	if( method == "field"){	// 프로세스 업무 선택
		// $("#process_select option:selected").val() != "1" &&
		$.ajax({
			url : uri,
			contentType : 'application/json',
			type : 'POST',
			dataType : "json",
			data : { "method" : method },
			success : function(data, status, jqXHR) {
				//if(data.id.length == 0) return;
				var html = "";
				var test = "";
				for(var i=0; i<data.id.length; i++){		// 프로세스 업무 필드 세팅
					field_data = data;
					if(data.type[i] == "datetime")
					{
						html = html + "<option value='"+data.id[i]+"'>"+data.name[i]+"</option>";
					}
				}
				$("select[name=field_select] option:not(option:eq(0))").remove();
				$("#field_select").append(html);
			},
			error : function(xhr, ajaxOptions, thrownError) {
				// alert("실패");
			}
		});
	}else{
		$.ajax({
			url : uri,
			contentType : 'application/json',
			type : 'POST',
			dataType : "json",
			data : { "method" : method },
			success : function(data, status, jqXHR) {
				if (method == "form"){
					var temp = data;
					var count = temp.count;
					var list = temp.list;
					formSetting(list , count);
				}else if(method == "field"){
					var temp = data;
					var count = temp.count;
					var list = temp.list;
					fieldSetting(list , count);
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				// alert("실패! 관리자에게 문의하세요");
			}
		});
	}
}
 /**
 	DB 연동
 	1. 프로세스 불러오기
 	2. 폼 불러오기
 	3. 필드(날짜) 불러오기
 	4. 추가하기
 	5. 삭제하기
 	6. 에디터 내용 편집하기
 	스크립트 응용
 	1. 추가일 관리하기
 */
function add(){
	var cnt = $("#mail_send_list tr").length;
	var today = new Date();
	var M = today.getMinutes();
	var s = today.getSeconds();
	if(cnt == 0){
		
	}else{
		var body = "<tr id='tr"+M+s+cnt+"'>";
		body = body + "<td>" +"<input type='radio' id='stand"+M+s+cnt+"' name='stand"+M+s+cnt+"' checked='checked' value='0'>업무 전　<input type='radio' id='stand"+M+s+cnt+"' name='stand"+M+s+cnt+"' value='1'>업무 후 "+ "</td>";
		body = body + "<td>" +"<input type='text' id='day"+M+s+cnt+"' name='day"+M+s+cnt+"' style='width:40%;border:1px solid #c7c7c7;  text-align: center;'/>"+ "</td>";
		body = body + "<td> <select id='select_if"+M+s+cnt+"' name='elect_if"+M+s+cnt+"'><option value='no'> </option></select>";
		body = body + "<input type='text' id='txt_if"+M+s+cnt+"' name='txt_if"+M+s+cnt+"' style='width:40%;border:1px solid #c7c7c7;'/> ";
		body = body + "<input type='checkbox' id='attach"+M+s+cnt+"' name='attach"+M+s+cnt+"' /> 첨부파일확인 </td>";
		body = body + "<td>" +"<a class='js_edit_search_filter' title='편집' id='edit"+M+s+cnt+"' onclick='edit("+M+s+cnt+")'><div class='icon_btn_edit'></div></a>"+ "</td>";
		body = body + "<td>" +"<a class='js_edit_search_filter' title='삭제' id='del"+M+s+cnt+"' onclick='del("+M+s+cnt+")'><div class='btn_tree_minus'></div></a>"+ "</td>";
		body = body + "<input type='hidden' id='textarea_hidden"+M+s+cnt+"' name='textarea_hidden"+M+s+cnt+"' />";
		body = body + "</tr>";
		$("#mail_send_list").append(body);
		
		var option = "";
		for(var i = 0 ; i < field_data.id.length; i++){
			option = option + "<option value='"+field_data.id[i]+"'>"+field_data.name[i]+"</option>";
		}
		$("#select_if"+M+s+cnt).append(option);
		$("#day"+M+s+cnt).corner();
		$("#select_if"+M+s+cnt).corner();
		$("#txt_if"+M+s+cnt).corner();
		
	}
}
function edit(num){
	$("#edit_div").fadeOut();			
	$("#edit_div").fadeIn();			
	var html = $("#textarea_hidden"+num).val();
	oEditors.getById["textedit"].exec("SET_IR", [html]);	// 해당 ROW에 있는 에디터 내용을 스마트 에디터에 보여줍니다.
	$("#editId_info").val(num);								// 현재 수정 중인 tr을 editId_info에 저장합니다.
}
function del(num){
	if( $("#textarea_hidden"+num).val() == oEditors.getById["textedit"].getIR()){
		$("#edit_div").hide();
	}
	if($('input:checkbox[id="warning"]').is(":checked")){
		$('#tr'+num).remove();
	}else{
		if(confirm("삭제하시겠습니까?"))
		$('#tr'+num).remove();
	}
}
function save(){
	 $("#mail_save_btn").fadeOut();
	 $("#mail_save_btn").fadeIn();
	 $("#textarea_hidden"+$("#editId_info").val()).val(oEditors.getById["textedit"].getIR());
}
function table_Save(){
	if($('#mail_send_list tr').length == 1) 
	{	
		alert("테이블이 비었습니다.");
		return;
	}
	
	$("#table_list_save_div").fadeOut();
	$("#table_list_save_div").fadeIn();
	var id = new Array();
	var i = 0;
	var temp = "";
	
	// 테이블 번호 저장
	$('#mail_send_list tr').each(function() {
		temp = $(this).attr('id');
		if(temp == null){}else{
			id[i] = temp.substring(2);
			i = i + 1;
		}
	 });
	// 테이블 번호 저장 완료
	var stand = new Array();
	var day = new Array();
	var condition = new Array();
	var condition_text = new Array();
	var file = new Array();
	var context = new Array();
	// 프로세스 폼 필드 가져오기
	var process = $("#process_select option:selected").val();
	var form = $("#form_select option:selected").val();
	var field =  $("#field_select option:selected").val();
	
	// 테이블 내용 가져오기
	for(var j=0;j<i;j++){
		//라디오박스 내용 가져오기 0<- 업무 전, 1<-업무 후
		stand[j] = $(":radio[name='stand"+id[j]+"']:checked").val();
		//전송일자 가져오기 (숫자가 아니면 튕겨냄)
		day[j] = $("#day"+id[j]).val();
		//필드조건 가져오기
		condition[j] = $("#select_if"+id[j]+" option:selected").val();
		//조건 내용 가져오기
		condition_text[j] = $("#txt_if"+id[j]).val();
		//첨부파일확인내용 가져오기
		file[j] = $("#attach"+id[j]).is(':checked');
		//메일 편집 내용 가져오기
		context[j] = $("#textarea_hidden"+id[j]).val();
		// alert(stand[j] + " " + day[j] + " " + condition[j] + " " + condition_text[j] + " " + file[j]  + " " + context[j]);
	}
	// 서버로 통신
	
	
}
// select  box 세팅 -----------------------------------------------------------------------------------------
function formSetting(list,count){	// 폼 세팅
	var html = "";
	for(var i=0;i<count;i++){
		if($("#process_select option:selected").val() == "1"){
			html = html + "<option value='"+list[i].formId+"'>"+list[i].formName+"</option>";
		}else{
			html = html + "<option value='"+list[i].id+"'>"+list[i].name+"</option>";
		}
	}
	$("select[name=form_select] option:not(option:eq(0))").remove();
	$("#form_select").append(html);
}
function fieldSetting(list,count){	// 필드 세팅
	var html = "";
	for(var i=0;i<count;i++){
		html = html + "<option value='"+list[i]+"'>"+list[i].formFieldName+"</option>";
	}
	$("select[name=field_select] option:not(option:eq(0))").remove();
	$("#field_select").append(html);
}
//select  box 세팅 -----------------------------------------------------------------------------------------
</script>
<!-- 스크립트 영역 끝-->
<body bgcolor="#ECE9E2">

<!-- 콘텐츠 영역 -->
<div id='main_content'>
<center>
	<!-- 상단 SELECT BOX -->
	<div>
	<font size='2'><b>업무</b></font>
	<select id="process_select" name="process_select"  style="font-size: 15px;font-weight: bold;">
	</select>
	<select id="form_select" name="form_select" style="font-size: 15px;font-weight: bold;">
		<option value = '0'>폼을 선택하세요</option>
	</select>
	<select id="field_select" name="field_select" style="font-size: 15px;font-weight: bold;">
		<option  value = '0'>필드를 선택하세요</option>
	</select>
	</div>

	<!-- 상단 SELECT BOX 끝-->
	<br/>
	<!-- 중단 메일 발송 규칙 -->
	<div width='900px' align="center">
<!-- 		<span><font size="4">메일 발송 규칙</font></span><br/> -->
<!-- 		<INPUT TYPE="radio" id="stand" name="stand" checked="checked" /> 기준 전 -->
<!-- 		<INPUT TYPE="radio" id="stand" name="stand" /> 기준 후 -->
<!-- 		<input id="day" name="day" class="nav_input" /> 일 -->
<!-- 		<a class='js_edit_search_filter' title='추가' id='add' onclick='add()'><div class='btn_tree_plus'></div></a> -->
<!-- 		<br /> -->
			
			<div id ="add_div">
			<font size=3>
			<b><span id='Modify_mail_text'>업무를 선택하여 주세요.</span></b>
			</font>
			<a class='js_edit_search_filter' title='추가' id='add' onclick='add()'><div class='btn_tree_plus'></div></a>
			<br />
			<br />
			<div id="table_list_save_div">
				<span class="btn_gray">
					<a class='js_edit_search_save' onclick='table_Save()'>
						<span class='txt_btn_start'></span><span class='txt_btn_center' id="save_click">테이블저장</span><span class='txt_btn_end'></span>
					</a>
				</span>
			</div>
			</div>
			<br />
			<table id='mail_send_list' style="width:60%" >

			</table>
	</div>
	<!-- 중단 메일 발송 규칙 끝-->
	 
	<!-- 하단 스마트 에디터 영역 -->
	<div id="edit_div" style="min-height:300px">
		<br></br>
		<table id='save_edit' style="width:60%" >
			<tr>
				<td>
					<div id="mail_save_btn">
						<span class="btn_gray">
							<a class='js_edit_search_save' onclick='save()'>
								<span class='txt_btn_start'></span><span class='txt_btn_center' id="mail_click">메일내용저장</span><span class='txt_btn_end'></span>
							</a>
						</span>
					</div>
				</td>
			</tr>
		</table>
		<input type='hidden' id='editId_info' />
		<textarea id='textedit' name='textedit' style='width:60%;' rows='15'> </textarea>
		<script type="text/javascript">
			var oEditors = [];
			nhn.husky.EZCreator.createInIFrame({
				oAppRef: oEditors,
				elPlaceHolder: "textedit", //textarea에서 지정한 id와 일치해야 합니다.
				sSkinURI: "../smarteditor/SEditorSkinKOR.html",
				fCreator: "createSEditorInIFrame"
			});
			$("#edit_div").fadeOut();
		</script>
	</div>
	<!-- 하단 스마트 에디터 영역 끝 -->
</center>
</div>

<!-- 콘텐츠 영역 끝 -->
</body>
</html>