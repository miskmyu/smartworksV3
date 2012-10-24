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
			<option selected="">- 선 택 -</option>
    		<option value = 'http://10.2.10.147:8862/smartworksV3/situationManual.sw?userviceCode=<%=Service.USERVICE_CODE_SECURITY%>&serviceCode=091&eventCode=11&situationStatus=<%=OPSituation.STATUS_SITUATION_OCCURRED%>'>호우</option>
    		<option value = 'http://10.2.10.147:8862/smartworksV3/situationManual.sw?userviceCode=<%=Service.USERVICE_CODE_SECURITY%>&serviceCode=091&eventCode=11&situationStatus=<%=OPSituation.STATUS_SITUATION_OCCURRED%>'>강도</option>
    		<option value = 'http://10.2.10.147:8862/smartworksV3/situationManual.sw?userviceCode=<%=Service.USERVICE_CODE_TRAFFIC%>&serviceCode=091&eventCode=11&situationStatus=<%=OPSituation.STATUS_SITUATION_OCCURRED%>'>교통사고</option>
    		<option value = 'http://10.2.10.147:8862/smartworksV3/situationManual.sw?userviceCode=<%=Service.USERVICE_CODE_TRAFFIC%>&serviceCode=091&eventCode=15&situationStatus=<%=OPSituation.STATUS_SITUATION_OCCURRED%>'>교통혼잡</option>
    		<option value = 'http://10.2.10.147:8862/smartworksV3/situationManual.sw?userviceCode=<%=Service.USERVICE_CODE_SECURITY%>&serviceCode=091&eventCode=11&situationStatus=<%=OPSituation.STATUS_SITUATION_OCCURRED%>'>대기오염</option>
    		<option value = 'http://10.2.10.147:8862/smartworksV3/situationManual.sw?userviceCode=<%=Service.USERVICE_CODE_SECURITY%>&serviceCode=091&eventCode=11&situationStatus=<%=OPSituation.STATUS_SITUATION_OCCURRED%>'>도로통제</option>
    		<option value = 'http://10.2.10.147:8862/smartworksV3/situationManual.sw?userviceCode=<%=Service.USERVICE_CODE_SECURITY%>&serviceCode=091&eventCode=12&situationStatus=<%=OPSituation.STATUS_SITUATION_OCCURRED%>'>미아</option>
    		<option value = 'http://10.2.10.147:8862/smartworksV3/situationManual.sw?userviceCode=<%=Service.USERVICE_CODE_FACILITY%>&serviceCode=091&eventCode=15&situationStatus=<%=OPSituation.STATUS_SITUATION_OCCURRED%>'>비상벨요청</option>
    		<option value = 'http://10.2.10.147:8862/smartworksV3/situationManual.sw?userviceCode=<%=Service.USERVICE_CODE_TRAFFIC%>&serviceCode=091&eventCode=12&situationStatus=<%=OPSituation.STATUS_SITUATION_OCCURRED%>'>뺑소니</option>
    		<option value = 'http://10.2.10.147:8862/smartworksV3/situationManual.sw?userviceCode=<%=Service.USERVICE_CODE_SECURITY%>&serviceCode=091&eventCode=11&situationStatus=<%=OPSituation.STATUS_SITUATION_OCCURRED%>'>상하수도누수</option>
    		<option value = 'http://10.2.10.147:8862/smartworksV3/situationManual.sw?userviceCode=<%=Service.USERVICE_CODE_SECURITY%>&serviceCode=091&eventCode=11&situationStatus=<%=OPSituation.STATUS_SITUATION_OCCURRED%>'>수위경보</option>
    		<option value = 'http://10.2.10.147:8862/smartworksV3/situationManual.sw?userviceCode=<%=Service.USERVICE_CODE_ENVIRONMENT%>&serviceCode=093&situationStatus=<%=OPSituation.STATUS_SITUATION_OCCURRED%>'>수질</option>
    		<option value = 'http://10.2.10.147:8862/smartworksV3/situationManual.sw?userviceCode=<%=Service.USERVICE_CODE_SECURITY%>&serviceCode=091&eventCode=11&situationStatus=<%=OPSituation.STATUS_SITUATION_OCCURRED%>'>시설물고장</option>
    		<option value = 'http://10.2.10.147:8862/smartworksV3/situationManual.sw?userviceCode=<%=Service.USERVICE_CODE_SECURITY%>&serviceCode=091&eventCode=14&situationStatus=<%=OPSituation.STATUS_SITUATION_OCCURRED%>'>용의차량추적</option>
    		<option value = 'http://10.2.10.147:8862/smartworksV3/situationManual.sw?userviceCode=<%=Service.USERVICE_CODE_SECURITY%>&serviceCode=091&eventCode=13&situationStatus=<%=OPSituation.STATUS_SITUATION_OCCURRED%>'>응급</option>
    		<option value = 'http://10.2.10.147:8862/smartworksV3/situationManual.sw?userviceCode=<%=Service.USERVICE_CODE_SECURITY%>&serviceCode=092&eventCode=14&situationStatus=<%=OPSituation.STATUS_SITUATION_OCCURRED%>'>지하차도침수</option>
    		<option value = 'http://10.2.10.147:8862/smartworksV3/situationManual.sw?userviceCode=<%=Service.USERVICE_CODE_TRAFFIC%>&serviceCode=091&eventCode=13&situationStatus=<%=OPSituation.STATUS_SITUATION_OCCURRED%>'>차량고장</option>
    		<option value = 'http://10.2.10.147:8862/smartworksV3/situationManual.sw?userviceCode=<%=Service.USERVICE_CODE_SECURITY%>&serviceCode=092&eventCode=13&situationStatus=<%=OPSituation.STATUS_SITUATION_OCCURRED%>'>태풍</option>
    		<option value = 'http://10.2.10.147:8862/smartworksV3/situationManual.sw?userviceCode=<%=Service.USERVICE_CODE_SECURITY%>&serviceCode=091&eventCode=11&situationStatus=<%=OPSituation.STATUS_SITUATION_OCCURRED%>'>호우</option>
    		<option value = 'http://10.2.10.147:8862/smartworksV3/situationManual.sw?userviceCode=<%=Service.USERVICE_CODE_SECURITY%>&serviceCode=092&eventCode=12&situationStatus=<%=OPSituation.STATUS_SITUATION_OCCURRED%>'>화재</option>
    		<option value = 'http://10.2.10.147:8862/smartworksV3/situationManual.sw?userviceCode=<%=Service.USERVICE_CODE_ENVIRONMENT%>&serviceCode=091&situationStatus=<%=OPSituation.STATUS_SITUATION_OCCURRED%>'>환경경보</option>
    	</select>
	</div>
</ul>

</body>
</html>
