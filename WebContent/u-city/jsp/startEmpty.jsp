<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="pro.ucity.sso.filter.UcityAuthenticationSuccesHandler"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="pro.ucity.model.Service"%>
<%@page import="pro.ucity.model.OPSituation"%>
<%@ page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head></head>
<body>
<!-- <pre>
상황모니터링
		*통합상황 모니터링
			통합상황 모니터링은, 현재업무의 진행 상태와 발생한 이벤트가 어떠한 항목에 속하는지, 현재 어디까지 처리되었는지, 발생일시, 발생장소, 외부표출 여부, SMS발송 여부를
			통합상황 모니터링 목록에서 한눈에 확인할 수 있습니다.
			또한, 목록에서 해당 사항을 선택하여 현재까지 진행된 업무의 흐름도를 모니터링 할 수 있습니다.
			각 태스크별로 진행단계에 맞추어 색을 다르게 표현함으로써 보다 효과적으로 진행상황을 파악하고,
			태스크 클릭시 관련 정보를 테이블 형식으로 보여줌으로써 업무의 효율성을 극대화 시킵니다.
 
		*상황 이벤트 감시
			상황 이벤트 감시는, 이벤트가 발생했지만 정상적인 종료가 되지 않았을 경우, 상황 이벤트 감시에서 관련 목록들을 보여줍니다.
			어떤 항목이 에러가 났는지, 그 항목이 어디까지 진행되었는지, 오류가 난 항목이 몇 개나 되는지 등
			관리자가 에러사항을 보다 쉽게 찾을 수 있도록 업무 흐름도의 모니터링을 제공합니다.
</pre> -->
</body>
</html>
