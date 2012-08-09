<%@ page contentType="text/html; charset=euc-kr" %>
<%@ page import="java.net.InetAddress" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>

<%
  if (request.getParameter("gc") != null) {
    System.gc();
    System.runFinalization();
  }
%>

<HTML>
<HEAD>
<META content="text/html; charset=euc-kr" http-equiv=Content-Type>
<!--META http-equiv="Refresh" content="10;url=<%= request.getRequestURI() %>"-->
<link href="./style.css" rel=stylesheet type="text/css">
</HEAD>

<body leftmargin=15 topmargin=10>
<center><p>

<table width="600" cellpadding="7" cellspacing="0" border="1" bordercolordark="WHITE" bordercolorlight="BLACK">
<tr><td>
■ 호스트 : <%= InetAddress.getLocalHost().getHostName() %>
(<%= InetAddress.getLocalHost().getHostAddress() %>)&nbsp;&nbsp;
<% SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월dd일 a hh시mm분 ss초", Locale.KOREA); %>
■ 측정시각 : <%= formatter.format(new Date()) %>
</td></tr>

<tr><td align=center>

<%
   Runtime rt = Runtime.getRuntime();
   long free = rt.freeMemory();
   long total = rt.totalMemory();
   long usedRatio = (total - free) * 100 / total;
   long unusedRatio = free * 100 / total;
%>

<table width=100% bgcolor="lightgrey" border=1 cellpadding=6 cellspacing=0>
<tr>
<td align="center" colspan="2">전체 가상머신 메모리 (<b><%= total/1024 %> KB</b>)</td>
</tr>
<tr bgcolor=#E3E3E3>
<td align="center">사용중인 메모리 (<b><%= (total - free)/1024 %> KB</b>)</td>
<td align="center">남아있는 메모리 (<b><%= free/1024 %> KB</b>)</td>
</tr>
<tr bgcolor=#E8EEEC>
<td><hr color="#CC3366" align=left size=10 width="<%= usedRatio %>%" noshade>
(<%= usedRatio %> %)</td>
<td><hr color="#0066FF" align=left size=10 width="<%= unusedRatio %>%" noshade>
(<%= unusedRatio %> %)</td>
</tr>
</table>

<p>
<a href="<%= request.getRequestURI() %>?gc="> 
<img src="trash.gif" valign=middle border=0> 가비지컬렉션 실행</a>
&nbsp;&nbsp;&nbsp;
&nbsp;&nbsp;&nbsp;
<a href="<%= request.getRequestURI() %>">
<img src="refresh.gif" valign=middle border=0> 다시 읽기</a>

</td></tr>
</table>

</center>
</BODY>
</HTML>
