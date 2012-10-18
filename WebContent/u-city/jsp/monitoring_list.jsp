<%@ page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head></head>
<body>
<script type="text/javascript">
// Popup window code
function newPopup(url) {
	popupWindow = window.open(url,'windowPop','toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=1000,height=600 top=10 left=10');
}
</script>
<ul>
	<li><a href="JavaScript:newPopup('http://localhost:8080/smartworksV3/situationMonitoring.sw');">통합 상황 모니터링</a></li>
	<li><a href="JavaScript:newPopup('http://localhost:8080/smartworksV3/situationMonitoring.sw');">이벤트 감시화면</a></li>
	<li>운영가이드</li>
	<div>
		<select onchange="window.open(this.value,'windowPop','toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=1000,height=600 top=10 left=10')">
			<option selected=""> - 운영 가이드 선택 - </option>
    		<option value = 'http://localhost:8080/smartworksV3/situationManual.sw'> 강풍 운영 가이드 </option>
    		<option> 호우 운영가이드 </option>
    		<option> 대설 운영 가이드 </option>
    		<option> 건조 운영 가이드 </option>
    		<option> 폭풍해일 운영 가이드 </option>
    		<option> 지진해일 운영 가이드 </option>
    		<option> 한파 운영 가이드 </option>
    		<option> 태풍 운영 가이드 </option>
    		<option> 황사 운영 가이드 </option>
    		<option> 폭염 운영 가이드 </option>        		
    	</select>
	</div>
</ul>

</body>
</html>
