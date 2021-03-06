<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContextAware"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>

<script type="text/javascript">
	var bcastRepeat = null;
	function updateBcastBoard(messages) {
		if (bcastRepeat != null)
			clearInterval(bcastRepeat);
		if (!isEmpty(messages)) {
			var count = 0;
			var changeBcastMessage = function() {
				var target = $('#divBroadcastingBoard');
				target.hide().html(messages[count]);
				target.slideDown();
				if (count == (messages.length-1)) count = 0;
				else count++;
			};
			bcastRepeat = setInterval(changeBcastMessage, 10000);
		}else{
			var target = $('#divBroadcastingBoard');
			target.hide().html("");			
		}
	};
</script>

<%
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	String[] messages = smartWorks.getBroadcastingMessages();
%>

<!-- Broadcasting Board -->
<%-- 
<b>[<fmt:message key="broadcasting.board.title" />] </b>
 --%>
<span id="divBroadcastingBoard"></span>

<%-- 
<script type="text/javascript">
var messageArray = new Array();
<%if (messages != null) {
	for (int i = 0; i < messages.length; i++) {
		String message = messages[i];%>
		messageArray[<%=i%>] = "<%=i+1%>/<%=messages.length%> <%=message%>";
	<%
	}
}
%>
updateBcastBoard(messageArray);
</script>
 --%>