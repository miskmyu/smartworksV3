<%@ page contentType="text/html; charset=utf-8"%>
<%@ page session="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<link href="./css/detail.css" type="text/css" rel="stylesheet" />
<link href="./css/chat.css" type="text/css" rel="stylesheet" />
<link href="./css/form.css" type="text/css" rel="stylesheet" />
<link href="./css/pop.css" type="text/css" rel="stylesheet" />
<link href="./css/media.css" type="text/css" rel="stylesheet"/>
<link href="./css/detail_semiteq.css" type="text/css" rel="stylesheet" />
<link href="./css/ui-lightness/jquery-ui-1.8.16.custom.css" rel="stylesheet" type="text/css" title="ui-theme" />
<link href="./css/ext/ext-all.css" type="text/css" rel="stylesheet" />
<link rel="shortcut icon" href="./images/favicon/smartworks.ico"/>
<!--[if lte IE 8]><link rel="stylesheet" href="css/black/ie8.css" type="text/css" media="all"><![endif]-->
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>SPEC조회</title>
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
<script type="text/javascript" src="./js/jquery/jquery.ui.core.min.js"></script>
<script type="text/javascript" src="./js/jquery/jquery.validate.js"></script>
<script type="text/javascript" src="./js/jquery/jquery.effects.core.js"></script>
<script type="text/javascript" src="./js/jquery/jquery.effects.explode.js"></script>
<script type="text/javascript" src="./js/jquery/jquery-ui-1.8.16.custom.min.js"></script>
<script type="text/javascript" src="./js/jquery/jquery.ui.widget.min.js"></script>
<script type="text/javascript" src="./js/jquery/jquery.ui.mouse.min.js"></script>
<script type="text/javascript" src="./js/jquery/jquery.ui.draggable.min.js"></script>
<script type="text/javascript" src="./js/jquery/jquery.ui.resizable.min.js"></script>
<script type="text/javascript" src="./js/jquery/jquery.ui.slider.min.js"></script>
<script type="text/javascript" src="./js/jquery/jquery.ui.datepicker.min.js"></script>
<script type="text/javascript" src="./js/jquery/jquery.ui.datepicker-ko.js"></script>
<script type="text/javascript" src="./js/jquery/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="./js/jquery/jquery-ui-timepicker-ko.js"></script>
<script type="text/javascript" src="./js/jquery/history/jquery.history.js"></script>
<script type="text/javascript" src="./js/jquery/jquery.json-2.3.min.js"></script>
<script type="text/javascript" src="./js/jquery/jquery.zclip.min.js"></script>
<script type="text/javascript" src="./js/jquery/jshashtable-2.1.js"></script>
<script type="text/javascript" src="./js/jquery/jquery.numberformatter-1.2.2.min.js"></script>
<script type="text/javascript" src="./js/jquery/jquery.formatCurrency-1.4.0.min.js"></script>
<script type="text/javascript" src="./js/jquery/jquery.simplemodal.1.4.2.min.js"></script>
<script type="text/javascript" src="./js/jquery/fullcalendar.js"></script>
<script type="text/javascript" src="./js/jquery/jquery-ui-1.8.21.custom.min.js"></script>

</head>
<body bgcolor="#ECE9E2">
<!-- <body> -->
<script type="text/javascript">
$(document).ready(function(){
	
	$("#lot_tr").addClass("end");
	for(var k=2;k<=12;k++){
 		$("#detail_tr" + k).hide();
 	}
	var pkg_temp = "";
	$.ajax({
		url : "./barcode_mes/PKGTypeList.jsp",
		contentType : 'application/json',
		type : 'POST',
		dataType : "json",
		data : "",
		success : function(data, status, jqXHR) {
			for(var i=0;i<data.list.length;i++){
				 pkg_temp = pkg_temp + "<option val='"+data.list[i]+"' >"+data.list[i]+"</option>";
			}
			$("#pkg_Select").append(pkg_temp);
		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert("통신실패");
		}
	});
	
	 $("#waring").hide();
	 $("#all_div").corner();
	 $("#process_div").corner();
	 $("#filter_div").corner();
	 
	var arg = "";
	arg = arg + "lot_number="+   $("#lot_code_txt").val();      
	arg = arg + "&device="+ 	 $("#device_txt").val();	
	arg = arg + "&spec="+ 	 $("#spec_txt").val();	
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
               height: "470px",
               colNames:['파일','DEVICE', 'ROMCODE', '공정','적용고객사','표준번호','개정번호','표준제목','기안자','등록일자','ID','recodeId','fileId','fileName','fileType','domainId','packageId'],
               colModel:[
                       {name:'attachment'		,index:'attachment'		, width:30, align:"center",  sortable:false, editable:false, formatter:downLoadFormmater},
                       {name:'device'			,index:'device'			, width:100, align:"center", sortable:false},
                       {name:'romcode'			,index:'romcode'		, width:100,align:"center",  sortable:false, hidden:true},
                       {name:'process'			,index:'process'		, width:45, align:"center",  sortable:false},
                       {name:'customer'			,index:'customer'		, width:60, align:"center",  sortable:false},        
                       {name:'standard_number'	,index:'standard_number', width:80, align:"center",  sortable:false},            
                       {name:'revision_number'	,index:'revision_number', width:50, align:"center",  sortable:false}, 
                       {name:'standard_title'	,index:'standard_title' , width:250, align:"left",   sortable:false}, 
                       {name:'repoter'			,index:'repoter'		, width:80, align:"center",  sortable:false}, 
                       {name:'date'				,index:'date'			, width:80, align:"center",  sortable:false}, 
                       {name:'id'				,index:'id'				, width:0, align:"center",   sortable:false,hidden:true}, 
                       {name:'recodeId'			,index:'recodeId'		, width:0, align:"center",   sortable:false,hidden:true},
                       {name:'fileId'			,index:'fileId'			, width:0, align:"center",   sortable:false,hidden:true},
                       {name:'fileName'			,index:'fileName'		, width:0, align:"center",   sortable:false,hidden:true}, 
                       {name:'fileType'			,index:'fileType'		, width:0, align:"center",   sortable:false,hidden:true},
                       {name:'domainId'			,index:'domainId'		, width:0, align:"center",   sortable:false,hidden:true},
                       {name:'pakageId'			,index:'pakageId'		, width:0, align:"center",   sortable:false,hidden:true}
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
    		   
		   		var fileName =  ret.fileName.split(",");
			 	var fileId 	 =  ret.fileId.split(",");
			 	var fileType =  ret.fileType.split(",");
			 	
			 	$("#lot_tr").addClass("end");
			 	for(var k=1;k<=12;k++){
			 		$("#detail_tr" + k).fadeOut("fast");
			 	}
			 	
    		   $.ajax({
    				url : "./list/barcode_detail.jsp?table="+ret.recodeId+"&id="+ret.id,
    				contentType : 'application/json',
    				type : 'POST',
    				dataType : "json",
    				data : "",
    				success : function(data, status, jqXHR) {
    					if(ret.recodeId == "dt_1330130316125"){
    						init();
    						$("#detail_addtechcount2").text(data.r1);   // 생산기술2팀 배포부수 
    						$("#detail_big").text(data.r2);   // 표준 대분류          
    						$("#detail_usedate").text(data.r3);	// 유효기간             
    						$("#detail_title").text(data.r4);	// 표준제목             
    						$("#detail_addcount3").text(data.r5);	// 품질보증팀 배포부수  
    						$("#detail_addprocess3").text(data.r6);	// 품질관리팀 배포공정  
    						$("#detail_one").text(data.r7);	// 적용 제품       
    						$("#customer_span").text(data.r8);
    						$("#detail_customer").text(data.r8);	// 적용 고객사          
    						$("#detail_process").text(data.r9);	// 공정                 
    						$("#detail_Packing").text(data.r10);	// Packing 배포요청부수 
    						$("#detail_dept").text(data.r11);	// 기안부서       
    						$("#detail_addtechprocess3").text(data.r13);	// 품질보증팀 배포공정  
    						$("#detail_STOCK").text(data.r14);	// STOCK 배포요청부수   
    						$("#detail_number2").text(data.r15);	// 개정번호             
    						$("#detail_re").text(data.r16);	// 제/개정(폐기)사유    
    						$("#detail_addprocess1").text(data.r17);	// 생산1부 배포공정     
    						$("#detail_addtechprocess2").text(data.r18);	// 생산기술2팀 배포공정 
    						$("#detail_number").text(data.r19);	// 표준번호             
    						$("#pkg_type_span").text(data.r20);	// 적용 PKG TYPE   
    						$("#detail_PKG").text(data.r20);	// 적용 PKG TYPE   
    						$("#detail_TEST").text(data.r21);	// TEST생산 배포요청부? 
    						$("#detail_small").text(data.r22);	// 표준 소분류          
    						$("#detail_addcount1").text(data.r23);	// 생산1부 배포부수     
    						$("#detail_middle").text(data.r24);	// 표준 중분류          
    						$("#detail_addtechcount3").text(data.r25);	// 품질관리팀 배포부수  
    						$("#detail_addcount2").text(data.r26);	// 생산2부 배포부수     
    						$("#detail_addprocess2").text(data.r27);	// 생산2부 배포공정     
    						$("#detail_addtechcount1").text(data.r28);	// 생산기술1팀 배포부수 
    						$("#detail_addtechprocess1").text(data.r29);	// 생산기술1팀 배포공정 
    						$("#detail_user").text(data.r30);	// 기안자               
    						$("#detail_day").text(data.r31);	// 등록일자             
    						$("#detail_ECN").text(data.r32);  // ECN 배포요청부수   
    						$("#detail_reason").text("" );       	// 변경사유        
    						var ul = "";
    						for(var i =0 ; i < fileType.length; i++){
	    						ul  = ul + "<span class='vm icon_file_" + fileType[i] + "'>";
	    						ul  = ul + "</span><a href='../download_file.sw?fileId=" + fileId[i] + "&fileName=" + fileName[i] + "&workId=" + ret.domainId + "&taskInstId=null&recordId=" + ret.recodeId;
	    						//ul  = ul + "</span> <a href='http://sw.semiteq.co.kr/smartserver/servlet/download?fileId=" + fileId[i] + "&userId="+ret.pakageId;
	    						ul  = ul + "' class='qq-upload-file'>" + fileName[i] + "</a><br />";
    						}
    						$("#detail_att").html(ul);
    						$("#lot_tr").removeClass("end");
    						for(var k=1;k<=12;k++){
    							if(k==11){}else{
    					 			$("#detail_tr" + k).fadeIn("fast");
    							}
    						}
    						
    					}else{
    						init();            
    						// 우선 초기화
    						$("#detail_ECN").text(data.r1 );	     // ECN 배포부수             
    						$("#detail_customer").text(data.r2);       // 적용 고객사  
    						$("#customer_span").text(data.r2);
    						$("#detail_day").text(data.r3);       		// 등록일자                 
    						$("#detail_TEST").text(data.r4);       	// TEST사업부 ECN 배포부수  
    						$("#detail_number2").text(data.r5 );       // 개정번호                 
    						$("#detail_reason").text(data.r6 );       	// 변경사유                 
    						$("#detail_number").text(data.r7 );       	// 표준번호                 
    						$("#detail_middle").text(data.r8 );       	// 표준 중분류               
    						$("#detail_one").text(data.r10);       		// 적용 제품                
    						$("#detail_usedate").text(data.r11 );       	// 유효기간                 
    						$("#detail_title").text(data.r12 );       	// 표준제목                 
    						$("#pkg_type_span").text(data.r13);       	// 적용 PKG TYPE 
    						$("#detail_PKG").text(data.r13);	// 적용 PKG TYPE   
    						$("#detail_dept").text(data.r14 );      		// 기안부서      
   
    						var ul = "";
    						for(var i =0 ; i < fileType.length; i++){
	    						ul  = ul + "<span class='vm icon_file_" + fileType[i] + "'>";
 	    						ul  = ul + "</span><a href='../download_file.sw?fileId=" + fileId[i] + "&fileName=" + fileName[i] + "&workId=" + ret.domainId + "&taskInstId=null&recordId=" + ret.recodeId;
//	    						ul  = ul + "</span> <a href='http://sw.semiteq.co.kr/smartserver/servlet/download?fileId=" + fileId[i] + "&userId="+ret.pakageId;
	    						ul  = ul + "' class='qq-upload-file'>" + fileName[i] + "</a><br />";
    						}
    						$("#lot_tr").removeClass("end");
    						for(var k=1;k<=12;k++){
    							if(k==10 || k == 9 || k == 8){}else{
    					 			$("#detail_tr" + k).fadeIn("fast");
    							}
    						}
    						$("#detail_att").html(ul);
    						
    					}
    					
    				},
    				error : function(xhr, ajaxOptions, thrownError) {
    					alert("통신실패");
    				}
    			});
    		
           },gridComplete: function() {
        	   init();
        	   $("#lot_tr").addClass("end");
        	   for(var k=2;k<=12;k++){
        	 		$("#detail_tr" + k).hide();
        	 	}
        	   $(".viewDivs").hide();	// 완료시 div가 보여지는 버그가 있다.
           }
        });
	  $("#list").jqGrid('navGrid','#pager',{edit:false,add:false,del:false});
	  $("#list").setGridWidth($(window).width() - 50 );     
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
function button_click(){
	if($("#lot_code_txt").val() == ""){
		 list_Search();
	 }else{
		 $("#lot_code_txt").val($("#lot_code_txt").val().toUpperCase());
		 Lot_Search_ajax($("#lot_code_txt").val()); 
	 }
}
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
 	var ul = "<div id='"+cellvalue+"' class='viewDivs' Style='position: absolute; width=auto; height=auto;  background-color: #ffffff;'>";
 	ul  = ul + "<table id='tb"+cellvalue+"'><tr><td>";
 	for(var i=0;i<fileName.length;i++){
		ul  = ul + "<span class='vm icon_file_" + fileType[i] + "'>";
 		ul  = ul + "</span><a href='../download_file.sw?fileId=" + fileId[i] + "&fileName=" + fileName[i] + "&workId=" + rowObject.domainId + "&taskInstId=null&recordId=" + rowObject.recodeId;
//		ul  = ul + "</span><a href='http://sw.semiteq.co.kr/smartserver/servlet/download?fileId=" + fileId[i] + "&userId="+rowObject.pakageId;
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
		$(".viewDivs").hide();
		$("#"+cellvalue).css({"left":30+"px"});
		$("#"+cellvalue).css({"top":mouseY+"px"});
		$("#"+cellvalue).show();
		temp = cellvalue;
	});
	$("#img"+cellvalue).live('mouseleave', function(e){
		$("#"+cellvalue).hide();
	});
	$("#"+cellvalue).live('mouseenter', function(e){
		$(".viewDivs").hide();
		$("#"+cellvalue).css({"left":30+"px"});
		$("#"+cellvalue).css({"top":(mouseY-10)+"px"});
		$("#"+cellvalue).show();
	});
	$("#"+cellvalue).live('mouseleave', function(e){
		$("#"+cellvalue).hide();
	});
 	return link;
}
function init(){
	
	if($("#device_txt").val() == ""){
		if($("#lot_code_txt").val() == ""){
			$("#device_txt").val("");	
			$("#pkg_type_span").text("");
			$("#customer_span").text("");
			$("#body_size_span").text("");
			$("#operation_span").text("");
		}
	}
	
	$("#detail_addtechcount2").text("");   // 생산기술2팀 배포부수 
	$("#detail_big").text("");   // 표준 대분류          
	$("#detail_usedate").text();	// 유효기간             
	$("#detail_title").text("");	// 표준제목             
	$("#detail_addcount3").text("");	// 품질보증팀 배포부수  
	$("#detail_addprocess3").text("");	// 품질관리팀 배포공정  
	$("#detail_one").text("");	// 적용 제품     
	$("#detail_customer").text("");	// 적용 고객사          
	$("#detail_process").text("");	// 공정                 
	$("#detail_Packing").text("");	// Packing 배포요청부수 
	$("#detail_dept").text("");	// 기안부서               
	$("#detail_addtechprocess3").text("");	// 품질보증팀 배포공정  
	$("#detail_STOCK").text("");	// STOCK 배포요청부수   
	$("#detail_number2").text("");	// 개정번호             
	$("#detail_re").text("");	// 제/개정(폐기)사유    
	$("#detail_addprocess1").text("");	// 생산1부 배포공정     
	$("#detail_addtechprocess2").text("");	// 생산기술2팀 배포공정 
	$("#detail_number").text("");	// 표준번호             
	$("#detail_TEST").text("");	// TEST생산 배포요청부? 
	$("#detail_small").text("");	// 표준 소분류          
	$("#detail_addcount1").text("");	// 생산1부 배포부수     
	$("#detail_middle").text("");	// 표준 중분류          
	$("#detail_PKG").text("");	// 적용 PKG TYPE   
	$("#detail_addtechcount3").text("");	// 품질관리팀 배포부수  
	$("#detail_addcount2").text("");	// 생산2부 배포부수     
	$("#detail_addprocess2").text("");	// 생산2부 배포공정     
	$("#detail_addtechcount1").text("");	// 생산기술1팀 배포부수 
	$("#detail_addtechprocess1").text("");	// 생산기술1팀 배포공정 
	$("#detail_reason").text("");       	// 변경사유          
	$("#detail_user").text("");	// 기안자               
	$("#detail_day").text("");	// 등록일자             
	$("#detail_ECN").text("");  // ECN 배포요청부수     
	$("#detail_att").html("");
}

function Lot_Search_ajax(arg){
	
	init();
	$("#lot_tr").addClass("end");
	for(var k=2;k<=12;k++){
 		$("#detail_tr" + k).hide();
 	}
	$.ajax({
		url : "./barcode_mes/Lot_Search.jsp?lotcode="+arg,
		contentType : 'application/json',
		type : 'POST',
		dataType : "json",
		data : "",
		success : function(data, status, jqXHR) {
			init();
			if(data.device == "error"){ 
				$("#waring").show();
				$("#waring").fadeOut(3500);		
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
	arg = arg + "&spec="+ 	 $("#spec_txt").val();	
	arg = arg + "&pkg_type="+  	encodeURIComponent( $("#pkg_Select").val());
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
<input type="hidden" id="STD_NO" name="STD_NO" ></input>
	<div class="portlet_t" id ="div_all">
	<div class="portlet_tl"></div>
	</div>
	<div class="portlet_l" style="display: block;">
	<ul class="portlet_r" style="margin: 0; padding: 0;">
		<div class="contents_space">
			<div id="all_div" class="pt10">
				<table id="select_menu">
					<tr>	
						<th style="width: 10%" class="tr">
							<img src="./images/t_lotnumber.gif" />
						</th>
						<td style=" width: 22%">
							<input class="fieldline" type="text" id="lot_code_txt" Style="width: 95%;"/>
						</td>
						<th style="width: 10%" class="tr">
							<img src="./images/t_device.gif" />
						</th>
						<td style="width:22%">
							<input class="fieldline" type="text" id="device_txt" Style="width: 95%;"/>
						</td>
						<th style="width: 12%" class="tr">
							<img src="./images/t_romcode.gif" />
						</th>
						<td style="width:22%">
							<input class="fieldline" type="text" id="spec_txt" Style="width: 95%;"/>
							
						</td>
						<td style="width:4%" class="tr">
							<input type="button" onclick="button_click()" value="SPEC 조회" Style="width:100%;"/>
						</td>
					</tr>
					<tr>
						<td colspan="7">
							<div class="js_work_list_title">
								<div id="filter_div" class="smt_srch_section">
									<table width="100%" >
										<tr>
										<th style="width:6%" class="tr">PKGTYPE</th>
												<td style="width:5%">
													<select id="pkg_Select" name="pkg_Select">
														<option value="ALL">전체</option>
													</select>
												</td>
										<th style="width:6%" class="tr">공정</th>
												<td style="width:5%">
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
												<th style="width:7%" class="tr">대분류</th>
												<td style="width:7%">
													<select id="big" name="big">
														<option value="ALL">전체</option>
														<option value="제/개정">제/개정</option>
														<option value="폐기">폐기</option>
														<option value="임시스펙(ECN)등록">임시스펙(ECN)등록</option>
														<option value="임시스펙(ECN)연장">임시스펙(ECN)연장</option>
														<option value="임시스펙(ECN)폐기">임시스펙(ECN)폐기</option>
													</select>
												</td>
												<th style="width:7%" class="tr"><span>중분류</span></th>
												<td style="width:7%">
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
												<th style="width:7%" class="tr">소분류</th>
												<td style="width:7%" >
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
												<th style="width:6%" class="tr">제목</th>
												<td style="width:10%" >
													<input class="fieldline" type="text" id="standard_title_txt" name="standard_title_txt" Style="width:100%;"/>
												</td>
											</tr>
										</table>
									</div>
								</div>
							</td>
						</tr>
						<tr id="detail_tr1">
						<td colspan="7">
							<div class="js_work_list_title">
								<div id="process_div" class="smt_table_section">
									<table width="100%">
										<tr id="lot_tr"  class="end">
											<th style="width:13%;">PKG_TYPE</th>
											<td style="width:12%;" align="left"><span id="pkg_type_span"></span></td>
											<th style="width:13%;">고객사</th>
											<td style="width:12%;"  align="left"><span id="customer_span"></span></td>
											<th style="width:13%;">BODY_SIZE</th>
											<td style="width:12%;"  align="left"><span id="body_size_span"></span></td>
											<th style="width:13%;">현공정</th>
											<td style="width:12%;"  align="left"><span id="operation_span"></span></td>
										</tr>
										<tr  id="detail_tr2">
											<th>표준제목</th>
											<td colspan="7"><span id="detail_title"></span></td>
										</tr>	
										<tr  id="detail_tr3">
											<th>유효기간</th>
											<td><span id="detail_usedate"></span></td>
											<th>표준대분류</th>
											<td><span id="detail_big"></span></td>
											<th>표준중분류</th>
											<td><span id="detail_middle"></span></td>
											<th>표준소분류</th>
											<td><span id="detail_small"></span></td>
										</tr>			
										<tr  id="detail_tr4">
											<th>표준번호</th>
											<td><span id="detail_number"></span></td>
											<th>개정번호</th>
											<td><span id="detail_number2"></span></td>
											<th>기안부서</th>
											<td><span id="detail_dept"></span></td>
											<th>기안자</th>
											<td><span id="detail_user"></span></td>
										</tr>			
										<tr id="detail_tr5">
											<th>적용제품</th>
											<td><span id="detail_one"></span></td>
											<th>적용 PKG TYPE</th>
											<td><span id="detail_PKG"></span></td>
											<th>적용고객사</th>
											<td><span id="detail_customer"></span></td>
											<th>공정</th>
											<td><span id="detail_process"></span></td>
										</tr>
										<tr id="detail_tr6">
											<th>제/개정(폐기)사유</th>
											<td colspan="5"><span id="detail_re"></span></td>
											<th>등록일자</th>
											<td colspan="5"><span id="detail_day"></span></td>
										</tr>
										<tr id="detail_tr7">
											<th>생산1부 배포공정</th>
											<td><span id="detail_addprocess1"></span></td>
											<th>생산1부 배포부수</th>
											<td><span id="detail_addcount1"></span></td>
											<th>생산기술1팀 배포공정</th>
											<td><span id="detail_addtechprocess1"></span></td>
											<th>생산기술1팀 배포부수</th>
											<td><span id="detail_addtechcount1"></span></td>
										</tr>
										<tr id="detail_tr8">
											<th>생산2부 배포공정</th>
											<td><span id="detail_addprocess2"></span></td>
											<th>생산2부 배포부수</th>
											<td><span id="detail_addcount2"></span></td>
											<th>생산기술2팀 배포공정</th>
											<td><span id="detail_addtechprocess2"></span></td>
											<th>생산기술2팀 배포부수</th>
											<td><span id="detail_addtechcount2"></span></td>
										</tr>
										<tr id="detail_tr9">
											<th>품질관리 배포공정</th>
											<td><span id="detail_addprocess3"></span></td>
											<th>품질관리 배포부수</th>
											<td><span id="detail_addcount3"></span></td>
											<th>품질보증 배포공정</th>
											<td><span id="detail_addtechprocess3"></span></td>
											<th>품질보증 배포부수</th>
											<td><span id="detail_addtechcount3"></span></td>
										</tr>
										<tr id="detail_tr10">
											<th>STOCK 배포요청부수</th>
											<td><span id="detail_STOCK"></span></td>
											<th>Packing 배포요청부수</th>
											<td><span id="detail_Packing"></span></td>
											<th>TEST생산 배포요청부수</th>
											<td><span id="detail_TEST"></span></td>
											<th>ECN 배포요청부수</th>
											<td><span id="detail_ECN"></span></td>
										</tr>
										<tr id="detail_tr11">
											<th>변경사유</th>
											<td colspan="7" id ="detail_reason"></td>
										</tr>
										<tr id="detail_tr12" class="end">
											<th>첨부파일</th>
											<td colspan="7" id ="detail_att"></td>
										</tr>
									</table>
									</div>
								</div>
							</td>
						</tr>
					</table>
		    		<table id="list"></table>
					<div id="pager"></div>
				</div>
			</div>
		</ul>
	</div>
	<div class="portlet_b" style="display: block;"></div>	
</div>
<center>
<div id="waring" Style="position: absolute; top:38px; left:180px; width: 400px; height: 100px; visibility:;">
	<font size=2 color="#FF0000"><b>LOT_NUMBER가 존재하지 않습니다.</b></font>
</div>
</center>
<script type="text/javascript">
$(window).bind('resize', function() {
   $("#list").setGridWidth($(window).width() - 50);
}).trigger('resize');
</script>
<div id=lastdiv></div>
</body>
</html>