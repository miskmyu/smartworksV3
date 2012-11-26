<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%System.out.println("############### login.jsp ###############"); 
	String to = request.getParameter("referer");
	System.out.println(to);
	
	%>
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<script type="text/javascript" src="js/jquery/jquery-1.6.2.min.js"></script>
	</head>
	<body style="background-color:22282f;">
		<form class="lo_idp t_wh" action="j_spring_security_check" method="post" style="display:none">
		
			<input id="j_username" name="j_username" maxlength="50" type="text" value="anonymous@smartworks.net"/>
			<input id="j_password" name="j_password" maxlength="50" type="password" value="1"/>
			<input type="hidden" name="_to" value=<%=to%>>
			<input class="fl btn_login" type="submit">
		</form>
	</body>
</html>
<script type="text/javascript">
 $(function() {
	
    $('input[type="submit"]').click();
});
</script>
