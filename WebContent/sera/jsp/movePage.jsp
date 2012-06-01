<%@ page contentType="text/html; charset=utf-8"%>
<%
	String href = (String)request.getAttribute("href") == null ? "" : (String)request.getAttribute("href");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base target="_self">
<script type="text/javascript">
function initMovePage() {
	var f = document.form;
	f.action = "<%=href%>";
	f.submit();
}
</script>
</head>
<body onload="javaScript:initMovePage()">
<form name="form" method="post">
</form>
</body>
</html>