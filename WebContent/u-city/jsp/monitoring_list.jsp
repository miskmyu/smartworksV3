<%@page import="pro.ucity.model.Service"%>
<%@page import="pro.ucity.model.OPSituation"%>
<%@ page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head></head>
<body>
<script type="text/javascript">
// Popup window code
function newPopup(url) {
	popupWindow = window.open(url,'windowPop','toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=1000,height=768 top=10 left=10');
}
</script>
<ul>
	<li><a href="JavaScript:newPopup('http://10.2.10.147:8862/smartworksV3/situationMonitoring.sw');">통합 상황 모니터링</a></li>
	<li>운영가이드</li>
	<div>
		<select onchange="window.open(this.value,'windowPop','toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=1000,height=600 top=10 left=10')">
			<option selected=""> - 운영 가이드 선택 - </option>
    		<option value = 'http://10.2.10.147:8862/smartworksV3/situationManual.sw?userviceCode=<%=Service.USERVICE_CODE_SECURITY%>&serviceCode=091&eventCode=11&situationStatus=<%=OPSituation.STATUS_SITUATION_OCCURRED%>'> 환경(호우) 발생 운영가이드 </option>
    		<option value = 'http://10.2.10.147:8862/smartworksV3/situationManual.sw?userviceCode=<%=Service.USERVICE_CODE_SECURITY%>&serviceCode=091&eventCode=14&situationStatus=<%=OPSituation.STATUS_SITUATION_PROCESSING%>'> 방범(용의차량)상황 처리 운영가이드 </option>
    	</select>
	</div>
</ul>

</body>
</html>
