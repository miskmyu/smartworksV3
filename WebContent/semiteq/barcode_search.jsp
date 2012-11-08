<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="true"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<!-- <link href="../css/default.css" type="text/css" rel="stylesheet" /> -->
<link href="../css/black/layout.css" type="text/css" rel="stylesheet" />
<link href="../css/black/detail.css" type="text/css" rel="stylesheet" />
<link href="../css/black/form.css" type="text/css" rel="stylesheet" />
<link href="../css/black/pop.css" type="text/css" rel="stylesheet" /> 
<link href="css/detail_semiteq.css" type="text/css" rel="stylesheet" />
<link href="../css/ui-lightness/jquery-ui-1.8.16.custom.css" rel="stylesheet" type="text/css" title="ui-theme" />
<link href="../css/ext/ext-all.css" type="text/css" rel="stylesheet" />
<link href="../smarteditor/css/default_kor.css" rel="stylesheet" type="text/css" />
<link rel="shortcut icon" href="../images/favicon/smartworks.ico"/>
<!--[if lte IE 8]><link rel="stylesheet" href="css/black/ie8.css" type="text/css" media="all"><![endif]-->
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>바코드 검색</title>
<link rel="stylesheet" type="text/css" media="screen" href="./js/jqgrid/themes/redmond/jquery-ui-1.8.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="./js/jqgrid/themes/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" media="screen" href="./js/jqgrid/themes/ui.multiselect.css" />

<script src="./js/jqgrid/js/jquery.min.js" type="text/javascript"></script>
<script src="./js/jqgrid/js/jquery-ui-1.8.1.custom.min.js" type="text/javascript"></script>
<script src="./js/jqgrid/js/jquery.layout.js" type="text/javascript"></script>
<script src="./js/jqgrid/js/i18n/grid.locale-en.js" type="text/javascript"></script>
<script src="./js/jqgrid/js/ui.multiselect.js" type="text/javascript"></script>
<script src="./js/jqgrid/js/jquery.jqGrid.min.js" type="text/javascript"></script>
<script src="./js/jqgrid/js/jquery.tablednd.js" type="text/javascript"></script>
<script src="./js/jqgrid/js/jquery.contextmenu.js" type="text/javascript"></script>
<script type="text/javascript" src="./js/jquery.corner.js"></script>



<script type="text/javascript" src="../js/jquery/jquery.ui.core.min.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.validate.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.effects.core.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.effects.explode.js"></script>
<script type="text/javascript" src="../js/jquery/jquery-ui-1.8.16.custom.min.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.ui.widget.min.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.ui.mouse.min.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.ui.draggable.min.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.ui.resizable.min.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.ui.slider.min.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.ui.datepicker.min.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.ui.datepicker-ko.js"></script>
<script type="text/javascript" src="../js/jquery/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="../js/jquery/jquery-ui-timepicker-ko.js"></script>
<script type="text/javascript" src="../js/jquery/history/jquery.history.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.json-2.3.min.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.zclip.min.js"></script>
<script type="text/javascript" src="../js/jquery/jshashtable-2.1.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.numberformatter-1.2.2.min.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.formatCurrency-1.4.0.min.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.simplemodal.1.4.2.min.js"></script>
<script type="text/javascript" src="../js/jquery/fullcalendar.js"></script>
<script type="text/javascript" src="../js/jquery/jquery-ui-1.8.21.custom.min.js"></script>

</head>

<body>
<script type="text/javascript">
var domain = "http://192.168.50.126:8080/smartworksV3";

$(document).ready(function(){
	
// 	 $("#lastdiv").hide();
	 $("#waring").hide();
	 $("#all_div").corner();
	 $("#process_div").corner();
	 $("#view_div").corner();
	 
	var arg = "";
	arg = arg + "lot_number="+   $("#lot_code_txt").val();      
	arg = arg + "&device="+ 	 $("#device_txt").val();	      
	arg = arg + "&rom_code="+  	 $("#rom_txt").val();	          
	arg = arg + "&process="+	encodeURIComponent( $("#process").val());	          
	arg = arg + "&big="+ 		encodeURIComponent( $("#big").val());               
	arg = arg + "&middle="+		encodeURIComponent( $("#middle").val());            
	arg = arg + "&small="+		encodeURIComponent( $("#small").val());	            
	arg = arg + "&title="+ 		encodeURIComponent( $("#standard_title_txt").val());
	arg = arg + "&method=all";
		
	 
	 jQuery("#list").jqGrid({
		 	   url : "./list/barcode_list.jsp?"+arg,
		 	   postData : {},
		 	   datatype: "json",
               type : 'POST',
               height: "460px",
               colNames:['파일','DEVICE', 'ROMCODE', '공정','적용고객사','표준번호','개정번호','표준제목','기안자','등록일자','ID','PAKAGEID','fileId','fileName','fileType','domainId'],
               colModel:[
                       {name:'attachment'		,index:'attachment'		, width:20, align:"center",  sortable:false, editable:false, formatter:downLoadFormmater},
                       {name:'device'			,index:'device'			, width:100, align:"center", sortable:false},
                       {name:'romcode'			,index:'romcode'		, width:100,align:"center",  sortable:false},
                       {name:'process'			,index:'process'		, width:50, align:"center",  sortable:false},
                       {name:'customer'			,index:'customer'		, width:50, align:"center",  sortable:false},        
                       {name:'standard_number'	,index:'standard_number', width:80, align:"center",  sortable:false},            
                       {name:'revision_number'	,index:'revision_number', width:40, align:"center",  sortable:false}, 
                       {name:'standard_title'	,index:'standard_title' , width:350, align:"left",   sortable:false}, 
                       {name:'repoter'			,index:'repoter'		, width:100, align:"center", sortable:false}, 
                       {name:'date'				,index:'date'			, width:80, align:"center",  sortable:false}, 
                       {name:'id'				,index:'id'				, width:0, align:"center",   sortable:false,hidden:true}, 
                       {name:'recodeId'			,index:'recodeId'		, width:0, align:"center",   sortable:false,hidden:true},
                       {name:'fileId'			,index:'fileId'			, width:0, align:"center",   sortable:false,hidden:true},
                       {name:'file_name'		,index:'file_name'		, width:0, align:"center",   sortable:false,hidden:true}, 
                       {name:'file_type'		,index:'file_type'		, width:0, align:"center",   sortable:false,hidden:true},
                       {name:'domainId'			,index:'domainId'		, width:0, align:"center",   sortable:false,hidden:true}
               
               ],
               autowidth:true,
               pager:jQuery('#pager'), 
               rownumbers: true,
               rownumWidth: 30,
               gridview: true,
               rowNum:20,
               rowList:[10,20,30,50],
               page: 1,
               viewrecords: true,
               jsonReader :{
            	  page :"page",
            	  total :"total",
            	  root : "rows",
            	  records : "records",
            	  repeatitems: false
               },ondblClickRow:function(id){
    		   var ret = $(this).getRowData(id);
    		   window.open(domain + "/iwork_space.sw?cid=iw.sp."+ret.id+"&workId="+ ret.domainId,"상세보기","");
           },gridComplete: function() {
        	   $(".viewDiv").hide();	// 완료시 div가 보여지는 버그가 있다.
           }
        });
	  $("#list").jqGrid('navGrid','#pager',{edit:false,add:false,del:false});
	  $("#list").setGridWidth($(window).width() - 15 );     
});
$('*').keypress(function(e){
	 if(e.keyCode == 13){		// 엔터키 이벤트
		 if($("#lot_code_txt").val() == ""){
			 list_Search();
		 }else{
			 $("#lot_code_txt").val($("#lot_code_txt").val().toUpperCase());
			 Lot_Search_ajax($("#lot_code_txt").val()); 
		 }
	 }
});
var mouseX = 0;
var mouseY = 0;

$(document).bind('mousemove',function(e){
	mouseX = e.pageX;
	mouseY = e.pageY;
});
var temp = "";
function downLoadFormmater(cellvalue, options, rowObject){
	var fileName =  rowObject.fileName.split(",");
 	var fileId =  rowObject.fileId.split(",");
 	var fileType =  rowObject.fileType.split(",");
 	var ul = "<div id='"+cellvalue+"' class='viewDiv' Style='position: absolute; width=auto; height=auto;  background-color: #ffffff;'>";
 	ul  = ul + "<table id='tb"+cellvalue+"'><tr><td>";
 	for(var i=0;i<fileName.length;i++){
		ul  = ul + "<span class='vm icon_file_" + fileType[i] + "'>";
		ul  = ul + "</span><a href='http://sw.semiteq.co.kr/download_file.sw?fileId=" + fileId[i] + "&fileName=" + fileName[i] + "&workId=" + rowObject.domainId + "&taskInstId=null&recordId=" + rowObject.recodeId;
		ul  = ul + "' class='qq-upload-file'>" + fileName[i] + "</a><br />";
 	}
 	ul  = ul + "</td></tr></table></div>";
 	
	//var link = ul;
	var link = "<img id='img"+ cellvalue +"' src='../images/icon_file.gif'/>";
	$("#lastdiv").append(ul);
	$("#"+cellvalue).show();
	$("#"+cellvalue).hide();
	$("#tb"+cellvalue).corner();
	
	$("#img"+cellvalue).live('mouseenter', function(e){
		$(".viewDiv").hide();
		$("#"+cellvalue).css({"left":30+"px"});
		$("#"+cellvalue).css({"top":mouseY+"px"});
		$("#"+cellvalue).show();
		temp = cellvalue;
	});
	$("#img"+cellvalue).live('mouseleave', function(e){
		$("#"+cellvalue).hide();
	});
	$("#"+cellvalue).live('mouseenter', function(e){
		$(".viewDiv").hide();
		$("#"+cellvalue).css({"left":30+"px"});
		$("#"+cellvalue).css({"top":(mouseY-5)+"px"});
		$("#"+cellvalue).show();
	});
	$("#"+cellvalue).live('mouseleave', function(e){
		$("#"+cellvalue).hide();
	});
 	return link;
}

function Lot_Search_ajax(arg){
	
	$.ajax({
		url : "./barcode_mes/Lot_Search.jsp?lotcode="+arg,
		contentType : 'application/json',
		type : 'POST',
		dataType : "json",
		data : "",
		success : function(data, status, jqXHR) {
			if(data.device == "error"){ 
				$("#waring").show();
				$("#waring").fadeOut(2500);		
				$("#lot_code_txt").val("");
				$("#device_txt").val("");
			}else{
				$("#device_txt").val(data.device);	
				$("#pkg_type_span").text(data.pkg_type);
				$("#customer_span").text(data.customer);
				$("#body_size_span").text(data.body_size);
				$("#operation_span").text(data.operation);
				list_Search();
			}
			
		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert("통신실패");
		}
	});
	$("#lot_code_txt").focus();
}

function list_Search(){
	var arg = "";
	arg = arg + "lot_number="+   $("#lot_code_txt").val();      
	arg = arg + "&device="+ 	 $("#device_txt").val();	      
	arg = arg + "&rom_code="+  	 $("#rom_txt").val();	          
	arg = arg + "&process="+	encodeURIComponent( $("#process").val());	          
	arg = arg + "&big="+ 		encodeURIComponent( $("#big").val());               
	arg = arg + "&middle="+		encodeURIComponent( $("#middle").val());            
	arg = arg + "&small="+		encodeURIComponent( $("#small").val());	            
	arg = arg + "&title="+ 		encodeURIComponent( $("#standard_title_txt").val());
	arg = arg + "&method=lite";
	$("#list").setGridParam(
	{
		url : "./list/barcode_list.jsp?"+arg,
		page : 1,
		postData : {},
		datatype:"json"
	}).trigger("reloadGrid");
}
</script>
<div id='in_barcode' class="section_portlet">
	<div class="portlet_t" id ="div_all">
		<div class="portlet_tl"></div>
	</div>
	<div class="portlet_l" style="display: block;">
	
	<ul class="portlet_r" style="margin: 0; padding: 0;">
		<div class="contents_space">
		<div id="all_div" class="all_group">
			<table id="select_menu">
				<tr>	
					<th style="width: 10%" class="tr">
						LOT_NUMBER
					</th>
					<td style=" width: 23%">
						<input class="fieldline" type="text" id="lot_code_txt"/>
					</td>
					<th style="width: 10%" class="tr">
						DEVICE
					</th>
					<td style="width:23%">
						<input class="fieldline" type="text" id="device_txt"/>
					</td>
					<th style="width: 10%" class="tr">
						ROM_CODE
					</th>
					<td style="width:24%">
						<input class="fieldline" type="text" id="rom_txt"/>
					</td>
				</tr>
				<tr>
					<td colspan="6">
							<div class="list_title_space js_work_list_title mt15">
								<div id="filter_div" class="filter_group">
									<table width="100%" >
										
										<tbody>
											<tr>
												<th style="width:5%" class="tr">
													공정
												</th>
												<td style="width:15%">
													<select id="process" name="process">
														<option value="ALL">전체</option>
														<option value="SAW / B/G">SAW / B/G</option>
														<option value="D/A">D/A</option>
														<option value="W/B">W/B</option>
														<option value="M/D">M/D</option>
														<option value="M/K">M/K</option>
														<option value="T/F">T/F</option>
														<option value="BOC">BOC</option>
														<option value="Packing">Packing</option>
														<option value="STOCK">STOCK</option>
														<option value="IQC">IQC</option>
														<option value="QCI">QCI</option>
														<option value="QVI">QVI</option>
														<option value="QEI">QEI</option>
														<option value="신뢰성">신뢰성</option>
														<option value="FVI">FVI</option>
														<option value="TEST">TEST</option>
														<option value="설비">설비</option>
														<option value="기타">기타</option>
														<option value="공통">공통</option>
													</select>
												</td>
												
												<th style="width:5%" class="tr">
													대분류
												</th>
												<td style="width:15%">
													<select id="big" name="big">
														<option value="ALL">전체</option>
														<option value="제/개정">제/개정</option>
														<option value="폐기">폐기</option>
														<option value="임시스펙(ECN)등록">임시스펙(ECN)등록</option>
														<option value="임시스펙(ECN)연장">임시스펙(ECN)연장</option>
														<option value="임시스펙(ECN)폐기">임시스펙(ECN)폐기</option>
													</select>
												</td>
												<th style="width:5%" class="tr">
													<span>중분류</span>
												</th>
												<td style="width:15%">
													<select id="middle" name="middle">
														<option value="ALL">전체</option>
														<option value="매뉴얼(품질/환경/안전)">매뉴얼(품질/환경/안전)</option>
														<option value="프로세스(전사)">프로세스(전사)</option>
														<option value="양식(CSS 양식)">양식(CSS 양식)</option>
														<option value="기술스펙(ST-)">기술스펙(ST-)</option>
														<option value="관리스펙(SA-)">관리스펙(SA-)</option>
														<option value="기타도면">기타도면</option>
														<option value="사규">사규</option>
														<option value="임시스펙 유효기간 연장">임시스펙 유효기간 연장</option>
													</select>
												</td>
												<th style="width:5%" class="tr">
													소분류
												</th>
												<td style="width:15%" >
													<select id="small" name="small">
														<option value="ALL">전체</option>
														<option value="공정 SPEC(STPS-">공정 SPEC(STPS-)</option>
														<option value="장비 SPEC(STMC-)">장비 SPEC(STMC-)</option>
														<option value="자재 SPEC(STMA-)">자재 SPEC(STMA-)</option>
														<option value="검사 SPEC(STIN-)">검사 SPEC(STIN-)</option>
														<option value="BASE LINE(STBA-)">BASE LINE(STBA-)</option>
														<option value="Control Plan(STCP-)">Control Plan(STCP-)</option>
														<option value="시험 SPEC(STRT-)">시험 SPEC(STRT-)</option>
														<option value="작업지침(STWO-)">작업지침(STWO-)</option>
														<option value="개발 SPEC(STPD-)">개발 SPEC(STPD-)</option>
														<option value="B/D SPEC(STDO-)">B/D SPEC(STDO-)</option>
														<option value="M/K SPEC(STDO-)">M/K SPEC(STDO-)</option>
														<option value="Lead Frame 도면(STDO-SL)">Lead Frame 도면(STDO-SL)</option>
														<option value="Substrate 도면(STDO-SL)">ubstrate 도면(STDO-SL)</option>
														<option value="관리_환경 SPEC(SAEN-)">관리_환경 SPEC(SAEN-)</option>
														<option value="관리_기타 SPEC(SAMA-)">관리_기타 SPEC(SAMA-)</option>
														<option value="관리안전보건 SPEC(SAHS-)">관리안전보건 SPEC(SAHS-)</option>
														<option value="안전보건 매뉴얼(SM-)">안전보건 매뉴얼(SM-)</option>
														<option value="품질/환경 프로세스(QEP-)">품질/환경 프로세스(QEP-)</option>
														<option value="안전보건 매뉴얼(HSP-)">안전보건 매뉴얼(HSP-)</option>
														<option value="사규(S-Ax-x)">사규(S-Ax-x)</option>
														<option value="C-KIT 도면">C-KIT 도면</option>
													</select>
												</td>
												<th style="width:5%" class="tr">
													제목
												</th>
												<td style="width:15%" >
													<input class="fieldline" type="text" id="standard_title_txt" name="standard_title_txt" />
												</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
					</td>
				</tr>
				<tr>
					<td colspan="6">
						<center>
							<div class="list_title_space js_work_list_title mt15">
								<div id="process_div" class="filter_group" style="height: autox; border: 1px solid #FFFF90; background-color:#cfe6fc;">
									<table width="100%" border='1'>
										<tr>
											<td style="width:12%;">
												PKG_TYPE
											</td>
											<td style="width:12%;" align="left">
													<span id="pkg_type_span"></span>
											</td>
											<td style="width:12%;">
												고객사
											</td>
											<td style="width:12%;"  align="left">
													<span id="customer_span"></span>
											</td>
											<td style="width:12%;">
												BODY_SIZE
											</td>
											<td style="width:12%;"  align="left">
													<span id="body_size_span"></span>
											</td>
											<td style="width:12%;">
												현공정
											</td>
											<td style="width:12%;"  align="left">											
													<span id="operation_span"></span>
											</td>
										</tr>
										
										<tr>
											<td style="width:12%;">
												표준제목
											</td>
											<td style="width:12%;" align="left">
													<span id="detail_title"></span>
											</td>
											<td style="width:12%;">
											표준대분류
											</td>
											<td style="width:12%;"  align="left">
													<span id="detail_big"></span>
											</td>
											<td style="width:12%;">
												표준중분류
											</td>
											<td style="width:12%;"  align="left">
													<span id="detail_middle"></span>
											</td>
											<td style="width:12%;">
												표준소분류
											</td>
											<td style="width:12%;"  align="left">
													<span id="detail_small"></span>
											</td>
										</tr>
										
										<tr>
											<td style="width:12%;">
												표준번호
											</td>
											<td style="width:12%;" align="left">
													<span id="detail_number"></span>
											</td>
											<td style="width:12%;">
												개정번호
											</td>
											<td style="width:12%;"  align="left">
													<span id="detail_number2"></span>
											</td>
											<td style="width:12%;">
												기안부서
											</td>
											<td style="width:12%;"  align="left">
													<span id="detail_dept"></span>
											</td>
											<td style="width:12%;">
												기안자
											</td>
											<td style="width:12%;"  align="left">
													<span id="detail_user"></span>
											</td>
										</tr>
										
										<tr>
											<td style="width:12%;">
												적용제품
											</td>
											<td style="width:12%;" align="left">
													<span id="detail_one"></span>
											</td>
											<td style="width:12%;">
												적용 PKG TYPE
											</td>
											<td style="width:12%;"  align="left">
													<span id="detail_PKG"></span>
											</td>
											<td style="width:12%;">
												적용고객사
											</td>
											<td style="width:12%;"  align="left">
													<span id="detail_customer"></span>
											</td>
											<td style="width:12%;">
												공정
											</td>
											<td style="width:12%;"  align="left">
													<span id="detail_process"></span>
											</td>
										</tr>
										<!-- colspan!! --><!-- colspan!! --><!-- colspan!! -->
										<tr>
											<td style="width:12%;">
												제/개정(폐기)사유
											</td>
											<td style="width:12%;"  align="left"  colspan="5">
													<span id="detail_re"></span>
											</td>
											
											<td style="width:12%;">
												등록일자
											</td>
											<td style="width:12%;"  align="left"  colspan="5">
												
													<span id="detail_day"></span>
												
											</td>
											
										</tr>
										<!-- colspan!! --><!-- colspan!! --><!-- colspan!! -->
										
										<tr>
											<td style="width:12%;">
												생산1부 배포공정
											</td>
											<td style="width:12%;" align="left">
												
													<span id="detail_addprocess1"></span>
												
											</td>
											<td style="width:12%;">
												생산1부 배포부수
											</td>
											<td style="width:12%;"  align="left">
												
													<span id="detail_addcount1"></span>
												
											</td>
											<td style="width:12%;">
												생산기술1팀 배포공정
											</td>
											<td style="width:12%;"  align="left">
												
													<span id="detail_addtechprocess1"></span>
												
											</td>
											<td style="width:12%;">
												생산기술1팀 배포부수
											</td>
											<td style="width:12%;"  align="left">
												
													<span id="detail_addtechcount1"></span>
												
											</td>
										</tr>
										
										<tr>
											<td style="width:12%;">
												생산2부 배포공정
											</td>
											<td style="width:12%;" align="left">
												
													<span id="detail_addprocess2"></span>
												
											</td>
											<td style="width:12%;">
												생산2부 배포부수
											</td>
											<td style="width:12%;"  align="left">
												
													<span id="detail_addcount2"></span>
												
											</td>
											<td style="width:12%;">
												생산기술2팀 배포공정
											</td>
											<td style="width:12%;"  align="left">
												
													<span id="detail_addtechprocess2"></span>
												
											</td>
											<td style="width:12%;">
												생산기술2팀 배포부수
											</td>
											<td style="width:12%;"  align="left">
												
													<span id="detail_addtechcount2"></span>
												
											</td>
										</tr>
										
										<tr>
											<td style="width:12%;">
												품질관리 배포공정
											</td>
											<td style="width:12%;" align="left">
												
													<span id="detail_addprocess3"></span>
												
											</td>
											<td style="width:12%;">
												품질관리 배포부수
											</td>
											<td style="width:12%;"  align="left">
												
													<span id="detail_addcount3"></span>
												
											</td>
											<td style="width:12%;">
												품질보증 배포공정
											</td>
											<td style="width:12%;"  align="left">
												
													<span id="detail_addtechprocess3"></span>
												
											</td>
											<td style="width:12%;">
												품질보증 배포부수
											</td>
											<td style="width:12%;"  align="left">
												
													<span id="detail_addtechcount3"></span>
												
											</td>
										</tr>
										
										<tr>
											<td style="width:12%;">
												STOCK 배포요청부수
											</td>
											<td style="width:12%;" align="left">
												
													<span id="detail_STOCK"></span>
												
											</td>
											<td style="width:12%;">
												Packing 배포요청부수
											</td>
											<td style="width:12%;"  align="left">
												
													<span id="detail_Packing"></span>
												
											</td>
											<td style="width:12%;">
												TEST생산 배포요청부수
											</td>
											<td style="width:12%;"  align="left">
												
													<span id="detail_TEST"></span>
												
											</td>
											<td style="width:12%;">
												ECN 배포요청부수
											</td>
											<td style="width:12%;"  align="left">
												
													<span id="detail_ECN"></span>
												
											</td>
										</tr>
									</table>
								</div>
							</div>
						</center>
					</td>
				</tr>
			</table>

		    <table id="list"></table>
			<div id="pager"></div>
		</div>
	</ul>
	</div>
	
	<div class="portlet_b" style="display: block;"></div>	
</div>

<center>
<div id="waring" Style="position: absolute; top:33px; left:200px; width: 400px; height: 100px; visibility:;">
	존재하지 않는 LOT_NUMBER 입니다.
</div>
</center>
<!-- <script type="text/javascript">
$(window).bind('resize', function() {
   $("#list").setGridWidth($(window).width() - 15);
}).trigger('resize');
</script> -->
<div id=lastdiv></div>
</body>
</html>