<%@ page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head></head>
<body>
<script type="text/javascript">
// Popup window code
function newPopup(url) {
	popupWindow = window.open(url,'popUpWindow','height=700,width=1000,left=10,top=10');
}
</script>
<ul>
	<li><a href="JavaScript:newPopup('http://localhost:8080/smartworksV3/situationMonitoring.sw');">통합 상황 모니터링</a></li>
	<li><a href="JavaScript:newPopup('http://localhost:8080/smartworksV3/situationMonitoring.sw');">진행 상황 모니터링</a></li>
	<li><a href="JavaScript:newPopup('http://localhost:8080/smartworksV3/situationMonitoring.sw');">업무 가이드</a></li>
</ul>

</body>
</html>
