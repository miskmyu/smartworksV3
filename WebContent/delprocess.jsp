<%@page import="net.smartworks.server.engine.common.model.Order"%>
<%@page import="net.smartworks.server.engine.process.link.model.LnkLinkCond"%>
<%@page import="net.smartworks.server.engine.process.link.model.LnkLink"%>
<%@page import="net.smartworks.server.engine.process.task.model.TskTask"%>
<%@page import="net.smartworks.server.engine.process.task.model.TskTaskCond"%>
<%@page import="net.smartworks.server.engine.process.process.model.PrcProcessInstCond"%>
<%@page import="net.smartworks.server.engine.common.util.DateUtil"%>
<%@page import="java.util.Date"%>
<%@page import="net.smartworks.server.engine.process.process.model.PrcProcessInst"%>
<%@page import="net.smartworks.server.engine.common.manager.IManager"%>
<%@page import="net.smartworks.server.engine.factory.SwManagerFactory"%>
<%@page import="net.smartworks.model.instance.ProcessWorkInstance"%>
<%@page import="net.smartworks.model.instance.WorkInstance"%>
<%@page import="pro.ucity.util.UcityUtil"%>
<%@page import="net.smartworks.util.SmartTest"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.server.engine.common.model.Property"%>
<%@page import="net.smartworks.model.work.info.SmartTaskInfo"%>
<%@page import="net.smartworks.model.instance.Instance"%>
<%@page import="net.smartworks.model.instance.SortingField"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.service.impl.SmartWorks"%>
<%@page import="net.smartworks.model.instance.info.TaskInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.PWInstanceInfo"%>
<%@page import="net.smartworks.model.community.info.UserInfo"%>
<%@page import="net.smartworks.model.instance.info.InstanceInfo"%>
<%@page import="net.smartworks.model.work.ProcessWork"%>
<%@page import="net.smartworks.model.instance.FieldData"%>
<%@page import="net.smartworks.model.instance.info.InstanceInfoList"%>
<%@page import="net.smartworks.model.instance.info.RequestParams"%>
<%@page import="net.smartworks.model.work.FormField"%>
<%@page import="net.smartworks.model.work.SmartForm"%>
<%@page import="net.smartworks.model.filter.SearchFilter"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.model.security.EditPolicy"%>
<%@page import="net.smartworks.model.security.WritePolicy"%>
<%@page import="net.smartworks.model.security.AccessPolicy"%>
<%@page import="net.smartworks.model.work.InformationWork"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.work.Work"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<html>
<%
    ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
    User cUser = SmartUtil.getCurrentUser();

   final String LNK_TYPE = "processinst";
   final String PROCESS_TYPE = "process";
    // 삭제 소스
    String objId = request.getParameter("objId") == null ? "":request.getParameter("objId");
    
    if(!objId.equalsIgnoreCase("")){    	
    	TskTaskCond tskCond = new TskTaskCond();  // taskCond 선언.
        LnkLinkCond lnkCond = new LnkLinkCond();  // linkCond 선언.
        
        tskCond.setProcessInstId(objId);
        TskTask[] tskTasks = SwManagerFactory.getInstance().getTskManager().getTasks("admin", tskCond, IManager.LEVEL_LITE);
        //태스크 갯수만큼 돌면서 삭제메소드 호출
        int tasksLen = tskTasks.length;
        for(int i=0;i<tasksLen;i++){
        	SwManagerFactory.getInstance().getTskManager().removeTask("admin", tskTasks[i].getObjId());
        }
        
        //해당 lnk들을 삭제함.
/*         lnkCond.setType(LNK_TYPE);
        lnkCond.setCorrelation(objId);
        SwManagerFactory.getInstance().getLnkManager().removeLinks("admin", lnkCond); */
        
        SwManagerFactory.getInstance().getPrcManager().removeProcessInst("admin", objId);
    }

    //페이징 및 조회 소스
	PrcProcessInstCond cond = new PrcProcessInstCond();
	PrcProcessInstCond cond2 = new PrcProcessInstCond();
	
    request.setCharacterEncoding("UTF-8");
    int pageNo = request.getParameter("pageNo") == null ? 0 : Integer.parseInt(request.getParameter("pageNo"));
	String search_Text = request.getParameter("searchText") == null ? "":request.getParameter("searchText");
	String search_Select = request.getParameter("searchType") == null ? "1":request.getParameter("searchType");
	
	cond.setPageSize(20);
	cond.setPageNo(pageNo);
	cond.setType(PROCESS_TYPE);
	cond2.setType(PROCESS_TYPE);
	cond.setOrders(new Order[]{new Order(PrcProcessInst.A_CREATIONDATE, false)});
if(!search_Text.equals("")){
	if(search_Select.equals("1")){
		cond.setTitleLike(search_Text);
		cond2.setTitleLike(search_Text);
	}else if(search_Select.equals("2")){
		cond.setNameLike(search_Text);
		cond2.setNameLike(search_Text);
	}else{
	}
}
    long prcInstListPaging = SwManagerFactory.getInstance().getPrcManager().getProcessInstSize("admin", cond2);
	PrcProcessInst[] prcInstList = SwManagerFactory.getInstance().getPrcManager().getProcessInsts("admin", cond , IManager.LEVEL_LITE);
	long prcInstListsize = SwManagerFactory.getInstance().getPrcManager().getProcessInstSize("admin", null);
	
	
	int listsize = 1;
	if(search_Text != ""){
		if(prcInstListPaging != 0){
			listsize = (int)prcInstListPaging;
		}else{
			listsize = 1;
		}
	}else{
		listsize = (int)prcInstListsize;
	}
	int totalpage = listsize > 0 ? (listsize/cond.getPageSize()) : 1;
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>관리자용 프로세스 삭제페이지</title>
 <style type="text/css">
    .party_tbl tr td{
	 font-family: "굴림";
	 font-size: 12px;
	 color: #666666;
	 text-align: "left";
	}
	.party_tbl_paging tr td{
	 font-family: "굴림";
	 font-size: 12px;
	 align: "center";
	}
	.party_tbl_new {
	 border-top:1px solid #bbbbbb;
	}
	.party_tbl_new tr td {
	 font-family: "굴림";
	 font-size: 12px;
	 color: #666666;
	 text-align: center;
	 border-bottom:1px solid #bbbbbb; border-left:1px solid #bbbbbb; border-right:1px solid #bbbbbb;
	}
	.party_tbl_new tr th {
	 font-family: "굴림";
	 font-size: 12px;
	 color: white;
	 font-weight: bold;
	 text-align: center;
	 border-bottom:1px solid #bbbbbb; border-left:1px solid #bbbbbb; background-color: #99ccff;
	}
	.border_last{
	 border-right:1px solid #bbbbbb; 
	}
   input{color:white; font-size: 9pt; background-color:#99ccff; border:1 solid #6699ff;size= 20;height:20px;}
   textarea{color:white; font-size: 9pt; background-color:#99ccff; border:1 solid #6699ff}
   .font { font-size: 9px; }
</style>

<script type="text/javascript">
<%
if(cUser.getId().equals("anonymous@smartworks.net") || cUser == null){
	%>
	  window.location.href = "./error404.jsp";
	<%   
}
%>
	function del(objId){
		var option;
		var url = "/smartworksV3/delprocess.jsp?objId=" + objId;
		option = confirm("삭제 할까요?");
		if(option == true )
		{
		 	var form = document.getElementById("delProcess");
		    form.action = url;
			form.submit();
		}
		else if(option == false )
		{
		 alert("취소 되었습니다.");
		}
	}
	
	function search(){
		var search_Text = document.delProcess.search_Text.value;
		var search_Select = document.delProcess.search_Select.value;
	    var url = "./delprocess.jsp?pageNo=0&searchType=" + search_Select + "&searchText=" + search_Text;
		
	 	var form = document.getElementById("delProcess");
	    form.action = url;
		form.submit();
	}


</script>
</head>
<body>
<!-- -->

<!-- 목록페이지 -->
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />
<form id ="delProcess" name="delProcess" action="delProcess.jsp" method="post">
<table class="party_tbl" width="80%" cellspacing="1" cellpadding="1">
        <td> 목록 수 : <%if(prcInstList != null){%><%=prcInstList.length%><%}else{%>0<%} %> 개 </td>	
</table>
<table class="party_tbl_new" width="80%" cellspacing="1" cellpadding="1">
	<tr>
		<th width="10%">프로세스이름</th>
		<th width="10%">제목</th>
		<th width="10%">작성일</th>
		<th width="10%">삭제</th>
	</tr>
	<%
		if(prcInstList != null) {
			for (int i = 0; i < prcInstList.length; i++) {
				
				String prcName = prcInstList[i].getName();
				String prcTitle = prcInstList[i].getTitle();
				Date prcTime2 = prcInstList[i].getCreationDate();
				String prcObjId = prcInstList[i].getObjId();
				String prcTime = "";
				 if (!CommonUtil.isEmpty(prcTime2)) {
					 long time = prcTime2.getTime() + (1000 * 60 * 60 * 9);
					 prcTime2.setTime(time);
					 prcTime = DateUtil.toDateString(prcTime2);
				 }
    %>
     <tr>
 		<td width="10%"><%=prcName %></td>
		<td width="10%"><%=prcTitle %></td>
		<td width="10%"><%=prcTime %></td>
		<td width="10%"><input type="button" onclick="javascript:del('<%=prcObjId%>')" value="삭제하기"></td>
	</tr>
	<%
			} 
		}else{
			out.println("<td colspan ='4'>해당 프로세스 목록이 없습니다.</td>");
		}
%>
</table>
<table class ="party_tbl_paging" width="80%" cellspacing="1" cellpadding="1">
<tr align = "center">
 <td>
 	<a href="delprocess.jsp?pageNo=0&searchType=<%=search_Select%>&searchText=<%=search_Text%>">[처음]</a>
 	<%
 	if(totalpage <= 10){
 		for(int i=0; i<=totalpage;i++){
 	%>		
 		<a href="delprocess.jsp?pageNo=<%=i%>&searchType=<%=search_Select%>&searchText=<%=search_Text%>">[<%=i+1%>]</a>
 	<%	
 		}
 	}else{
 		if(pageNo >= 6){
 			for(int i = pageNo-5; i<pageNo+5; i++){
 				if( i == totalpage+1) break;
 				%>
 				<a href="delprocess.jsp?pageNo=<%=i%>&searchType=<%=search_Select%>&searchText=<%=search_Text%>">[<%=i+1%>]</a>
 				<%
 			}
 		}else{
 			for(int i=0; i<=10; i++){
 				%>
 				<a href="delprocess.jsp?pageNo=<%=i%>&searchType=<%=search_Select%>&searchText=<%=search_Text%>">[<%=i+1%>]</a>
 				<%
 			}
 		}
 	}
 	%>
 	<a href="delprocess.jsp?pageNo=<%=totalpage%>&searchType=<%=search_Select%>&searchText=<%=search_Text%>">[끝]</a>
 	</td>
</tr>

<tr align = "center">
 	<td>
		<select name="search_Select">
	 	<option value="1">제목</option>
		 <option value="2">프로세스</option>
		</select>
 		<input type="text" name="search_Text" OnKeyDown="if(event.keyCode==13){javascript:search();}">
 		<input type="button" onclick="javascript:search()" value="조회하기">
 	</td>
</tr>
</table>
</form>
</body>
</html>
