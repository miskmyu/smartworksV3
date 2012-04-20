<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
	var currentUser = {
		locale : "<%=java.util.Locale.getDefault().getLanguage()%>"
	};
</script>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>세라캠퍼스에 오신걸 환영합니다.</title>
	<link href="css/default.css" type="text/css" rel="stylesheet"/>
	<link href="css/fileuploader/fileuploader.css" type="text/css" rel="stylesheet"/>
	<link href="sera/css/pop.css" type="text/css" rel="stylesheet" />
	<link href="sera/css/form.css" type="text/css" rel="stylesheet"/>
	<link href="sera/css/page.css" type="text/css" rel="stylesheet"/>

	<script type="text/javascript" src="js/jquery/jquery-1.6.2.min.js"></script>
	<script type="text/javascript" src="js/jquery/jquery.validate.js"></script>
	<script type="text/javascript" src="js/sw/sw-language.js"></script>
	<script type="text/javascript" src="js/sw/sw-language-ko.js"></script>
	<script type="text/javascript" src="js/sw/sw-language-en.js"></script>
	<script type="text/javascript" src="js/sw/sw-validate.js"></script>
	<script type="text/javascript" src="js/sw/sw-util.js"></script>
	<script type="text/javascript" src='js/sw/sw-popup.js'></script>
	<script type="text/javascript" src="js/sw/sw-file.js"></script>
	<script type="text/javascript" src='sera/js/sera-formFields.js'></script>
	<script type="text/javascript" src='js/smartform/smartworks.js'></script>
	<script type="text/javascript" src='js/smartform/sw-form-layout.js'></script>
	<script type="text/javascript" src='js/smartform/sw-form-field-builder.js'></script>
	<script type="text/javascript" src='js/smartform/sw-form-dataFields.js'></script>
	<script type="text/javascript" src='js/smartform/field/currency_input.js'></script>
	<script type="text/javascript" src='js/smartform/field/radio_button.js'></script>
	<script type="text/javascript" src='js/smartform/field/check_box.js'></script>
	<script type="text/javascript" src='js/smartform/field/combo_box.js'></script>
	<script type="text/javascript" src='js/smartform/field/date_chooser.js'></script>
	<script type="text/javascript" src='js/smartform/field/email_input.js'></script>
	<script type="text/javascript" src='js/smartform/field/file_field.js'></script>
	<script type="text/javascript" src='js/smartform/field/number_input.js'></script>
	<script type="text/javascript" src='js/smartform/field/percent_input.js'></script>
	<script type="text/javascript" src='js/smartform/field/rich_editor.js'></script>
	<script type="text/javascript" src='js/smartform/field/text_input.js'></script>
	<script type="text/javascript" src='js/smartform/field/time_chooser.js'></script>
	<script type="text/javascript" src='js/smartform/field/datetime_chooser.js'></script>
	<script type="text/javascript" src='js/smartform/field/user_field.js'></script>
	<script type="text/javascript" src='js/smartform/field/ref_form_field.js'></script>
	<script type="text/javascript" src='js/smartform/field/image_box.js'></script>
	<script type="text/javascript" src='js/smartform/field/videoYT_box.js'></script>
	<script type="text/javascript" src="js/jquery/fileuploader/fileuploader.js" ></script>
	<script type="text/javascript" src="sera/js/rolling_img.js"></script>
	
</head>
<body>
	<div id="wrap" class="js_joinus_page">
		<div id="sera_header_join">
			<div class="hd_shadow bg_join">
				<!-- GNB -->
				<div class="gnb">
					<ul class="top_menu2" style="margin-left: 340px">
						<li class="fl">
							<a href="Course.sw"> <img width="101" height="28" src="sera/images/sera2_main_btnTopMenu1_off.png" /></a>
						</li>
						<li class="fl">
							<a href="myPAGE.sw"> <img width="101" height="28" src="sera/images/sera2_main_btnTopMenu2_off.png" /></a>
						</li>
						<li class="fl">
							<a href="http://blog.seracampus.com"> <img width="101" height="28" src="sera/images/sera2_main_btnTopMenu3_off.png" /> </a>
						</li>
					</ul>
					<ul class="util_menu fr">
						<li class="about"><a href="">about SERA</a></li>
						<li class="news"><a href="">sera 소식</a></li>
						<li class="btn_login"><a href=""> <img width="49" height="19" title="로그인" alt="로그인" src="sera/images/sera2_main_btnLogin.png" /> </a></li>
					</ul>
					<!-- Search -->
					<div class="top_srch_section" style="display: none">
						<input type="text" class="top_srch" />
						<div class="icon_srch"></div>
					</div>
					<!-- Search //-->
				</div>
				<!-- GNB //-->
				<!-- Top Navi -->
				<div class="logo_srch">
					<h1 class="logo">
						<a href="logins.sw"> <img width="201" height="36" alt="" src="sera/images/sera2_logo.png"></a>
					</h1>
					<ul class="srch">
						<li><input type="text" placeholder="SERA를 검색하세요." style="width: 235px; height: 20px" title="검색"></li>
						<li><div class="icon_srch_m"></div></li>
					</ul>
				</div>
				<!-- Top Navi //-->
			</div>
		</div>
		<div id="container_join">
			<div id="join_section" class="js_joinus_first">
				<!-- SNB Left -->
				<div class="snb">
					<img width="176" height="146" alt="" src="sera/images/function_title2.gif" />
				</div>
				<!-- SNB Left//-->
				<!-- Content -->
				<div id="join_content_section">
					<div class="tit"></div>
					<div class="join_process">
						<ul>
							<li class="prs1 current">1단계</li>
							<li class="ar"></li>
							<li class="prs2">2단계</li>
							<li class="ar"></li>
							<li class="prs3">3단계</li>
						</ul>
					</div>
					<!-- Joint Content -->
					<div class="join_content_area">
						<ul>
							<li class="sub_label">세라캠퍼스 <br /> 회원 이용 약관</li>
							<li>
								<div class="joinus_policy_section">
<p>&#65279;서비스 이용약관</p>
    <p> </p>
    <p>제1장 총칙 </p>
    <p>제1조 목적 </p>
    <p>본 약관은 주식회사 세라캠퍼스(이하 회사)가 운영하는 세라캠퍼스 (www.seracampus.com)에서 제공하는 서비스(이하 합하여 "서비스"라 한다)를 회원 아이디와 비밀번호로 사이트에 가입하여 이용함에 있어 회사와 이용자의 권리, 의무 및 책임사항을 규정함을 목적으로 합니다.</p>
    <p>제2조 용어의 정의 </p>
    <p>1. '세라캠퍼스'란 회사가 이용자에게 서비스를 제공하기 위하여 컴퓨터 등 정보통신설비를 이용하여 구성한 가상의 공간을 의미하며, 서비스 자체를 의미하기도 합니다. </p>
    <p>2. ‘고객’이란 '세라캠퍼스'에 접속하여 이 약관에 따라 회사가 ‘세라캠퍼스’에서 제공하는 서비스를 받는 ‘회원’ 및 ‘비회원’을 말합니다. </p>
    <p>3. '일반회원(회원)'이란 회사에 개인정보를 제공하여 회원등록을 한 자로서, 회사와 서비스 이용 계약을 체결하고 회원 아이디를 부여받은 자를 의미합니다. </p>
    <p>4. '회원 아이디(이하 "ID"라 한다)'란 회원의 식별과 회원의 서비스 이용을 위하여 회원이 선정하고 회사가 승인하는 문자 또는 숫자의 조합을 의미합니다. </p>
    <p>5. '비밀번호'란 회원이 부여받은 ID와 일치된 회원임을 확인하고, 회원의 개인정보를 보호하기 위하여 회원이 정한 문자와 숫자의 조합을 의미합니다. </p>
    <p> 6. '메일회원'이란 회사에 성명과 e-Mail 주소만을 제공하여 회원등록을 하고 세라캠퍼스 홈페이지가 발송 하는 메일을 수신하는 것을 내용으로 하는 서비스 이용 계약을 회사와 체결한 자를 의미합니다. <br>
      단, 메일회원은 추가 가입 신청을 받거나 추가 회원 승인을 하지 않습니다. </p>
    <p>7. ’비회원’이란 회원에 가입하지 않고 회사가‘세라캠퍼스’에서 제공하는 서비스를 이용하는 자를 말합니다. </p>
    <p>8. 본 약관에서 사용하는 용어의 정의는 본 조에서 정하는 것을 제외하고는 관계법령 및 서비스별 안내에서 정하는 바에 의합니다. </p>
    <p>제3조 이용약관의 효력 및 변경 </p>
    <p>1. 회사는 본 약관의 내용을 회원이 쉽게 알 수 있도록 각 서비스 사이트의 초기 서비스화면에 게시합니다. </p>
    <p>2. 회사는 약관의 규제에 관한 법률, 전자거래기본법, 전자서명법, 정보통신망 이용촉진 및 정보보호 등에 관한 법률 등 관련법을 위배하지 않는 범위에서 본 약관을 개정할 수 있습니다. </p>
    <p> 3. 회사는 본 약관을 개정할 경우에는 적용일자 및 개정사유를 명시하여 현행 약관과 함께 회사가 제공하는 서비스 사이트의 초기 화면에 그 적용일자 7일 이전부터 적용일자 전일까지 공지합니다. <br>
      다만, 회원에게 불리하게 약관내용을 변경하는 경우에는 최소한 30일 이상의 사전 유예기간을 두고 공지합니다. 이 경우 회사는 개정 전 내용과 개정 후 내용을 명확하게 비교하여 회원이 알기 쉽도록 표시합니다. </p>
    <p> 4. 회원은 개정된 약관에 대해 거부할 권리가 있습니다. 회원은 개정된 약관에 동의하지 않을 경우 서비스 이용을 중단하고 이용계약을 해지할 수 있습니다. <br>
      단, 개정된 약관의 효력 발생일 이후에도 서비스를 계속 이용할 경우에는 약관의 변경사항에 동의한 것으로 간주합니다. </p>
    <p>변경된 약관에 대한 정보를 알지 못해 발생하는 회원 피해는 회사가 책임지지 않습니다. </p>
    <p>제4조 약관 외 준칙 </p>
    <p> 본 약관은 회사가 제공하는 서비스에 관해 별도의 정책 및 운영규칙과 함께 적용됩니다. <br>
      본 약관에 명시되지 아니한 사항과 본 약관의 해석에 관하여는 관계법령에 따릅니다. </p>
    <p> </p>
    <p>제2장 이용계약 체결 </p>
    <p>제5조 이용계약의 성립 </p>
    <p>1. 이용계약은 회원의 본 이용약관 내용에 대한 동의와 이용신청에 대하여 회사의 이용승낙으로 성립합니다. </p>
    <p>2. 본 이용약관에 대한 동의는 회원등록 당시 본 약관을 읽고 "위 서비스 약관에 동의합니다"의 대화창에 표시를 한 후 등록하기 단추를 누름으로써 의사표시를 한 것으로 간주합니다. </p>
    <p>제6조 서비스 이용신청 </p>
    <p>1. 회원으로 가입하여 본 서비스를 이용하고자 하는 고객은 회사에서 요청하는 제반정보(이름, 주민등록번호, 연락처 등)를 제공하여야 합니다. 단, 이 때 제공된 주민등록번호는 개인정보의 적극적 보호를 위하여 실명 인증 절차에만 활용될 뿐, 회사가 제공하는 서비스의 운영을 위한 어떠한 장비나 문서에도 수집/저장되지 않습니다. </p>
    <p> 2. 회원은 반드시 회사가 제공하는 서비스가 요구하는 회원 본인의 실명 인증 절차를 거쳐야만 서비스를 이용할 수 있으며, 실명으로 등록하지 않은 회원은 일체의 권리를 주장할 수 없습니다. <br>
      회원가입은 반드시 실명으로만 가입할 수 있으며 회사는 실명확인조치를 할 수 있습니다. </p>
    <p>3. 타인의 명의(이름 및 주민등록번호)를 도용하여 이용신청을 한 회원의 모든 ID는 삭제되며, 관계법령에 따라 처벌을 받을 수 있습니다. </p>
    <p>4. 회사는 본 서비스를 이용하는 회원에 대하여 등급별로 구분하여 이용시간, 이용회수, 서비스 메뉴 등을 세분하여 이용에 차등을 둘 수 있습니다. </p>
    <p>5. 만14세 미만의 아동이 회원으로 가입하기 위해서는 반드시 개인정보의 수집 및 이용목적에 대하여 충분히 숙지하고 법정대리인(부모)의 동의를 받아야 합니다. 법정대리인의 허락을 받지 않은 14세 미만의 아동에 대해서는 회원에서 제외할 수 있습니다. </p>
    <p>제7조 개인정보의 보호 및 사용 </p>
    <p>1. 회사는 관계법령이 정하는 바에 따라 회원등록정보를 포함한 회원의 개인정보를 보호하기 위해 노력 합니다. 회원 개인 정보의 보호 및 사용에 대해서는 관련법령 및 회사의 개인정보보호정책이 적용됩니다. 단, 회사의 공식사이트 이외의 웹에서 링크된 사이트에서는 회사의 개인정보보호정책이 적용되지 않습니다. 또한 회사는 회원의 귀책사유로 인해 노출된 정보에 대해서 일체의 책임을 지지 않습니다. </p>
    <p>2. 회사는 고객에게 제공하는 서비스의 양적, 질적 향상을 위하여 고객의 개인정보를 제휴사에게 제공, 공유할 수 있으며, 이 때에는 반드시 고객의 동의를 받아 필요한 최소한의 정보를 제공, 공유하며 누구에게 어떤 목적으로 어떤 정보를 제공, 공유하는지 명시합니다. </p>
    <p>3. 회원은 원하는 경우 언제든 회사에 제공한 개인정보의 수집과 이용에 대한 동의를 철회할 수 있으며, 동의의 철회는 회원 탈퇴를 하는 것으로 이루어집니다. </p>
    <p>4. 회사는 서비스 개선 및 회원 대상의 서비스 소개 등의 목적으로 회원의 동의하에 추가적인 개인 정보를 요구할 수 있습니다. </p>
    <p>제8조 이용신청의 승낙과 제한 </p>
    <p>1. 회사는 제6조의 규정에 의한 이용신청고객에 대하여 업무 수행상 또는 기술상 지장이 없는 경우에 원칙적으로 접수순서에 따라 서비스 이용을 승낙합니다. </p>
    <p>2. 회사는 다음 각 호의 내용에 해당하는 경우 이용신청에 대한 승낙을 제한할 수 있고, 그 사유가 해소 될 때까지 승낙을 유보할 수 있습니다. </p>
    <p>① 회사의 서비스 관련설비에 여유가 없는 경우 </p>
    <p>② 회사의 기술상 지장이 있는 경우 </p>
    <p>③ 기타 회사의 사정상 필요하다고 인정되는 경우 </p>
    <p>1. 회사는 다음 각 호의 내용에 해당하는 경우 이용신청에 대한 승낙을 하지 아니할 수도 있습니다. </p>
    <p>① 실명이 아니거나 타인의 명의를 이용하여 신청한 경우 </p>
    <p>② 이용계약 신청서의 내용을 허위로 기재한 경우</p>
    <p>③ 사회의 안녕과 질서, 미풍양속을 저해할 목적으로 신청한 경우</p>
    <p>④ 부정한 용도로 본 서비스를 이용하고자 하는 경우 </p>
    <p>⑤ 영리를 추구할 목적으로 본 서비스를 이용하고자 하는 경우 </p>
    <p>⑥ 기타 회사가 정한 등록신청 요건이 미비된 경우 </p>
    <p>⑦ 본 서비스와 경쟁관계가 있는 이용자가 신청하는 경우 </p>
    <p>⑧ 기타 규정한 제반사항을 위반하며 신청하는 경우 </p>
    <p>1. 회사는 이용신청고객이 관계법령에서 규정하는 미성년자일 경우에 서비스별 안내에서 정하는 바에 따라 승낙을 보류할 수 있습니다. </p>
    <p>제9조 회원 아이디 부여 및 변경 등 </p>
    <p>1. 회사는 회원에 대하여 본 약관에 정하는 바에 따라 회원 ID를 부여합니다.</p>
    <p>2. 회원 ID는 원칙적으로 변경이 불가하며 부득이한 사유로 인하여 변경 하고자 하는 경우에는 해당 ID를 해지하고 재가입해야 합니다. </p>
    <p>3. 회원은 서비스 페이지 My Page 또는 기타 해당 페이지로 링크된 메뉴를 통하여 자신의 개인정보를 관리할 수 있는 페이지를 열람할 수 있으며, 해당 페이지에서 언제든지 본인의 개인정보를 열람하고 수정할 수 있습니다. 다만, 서비스 관리를 위하여 반드시 필요한 실명, 회원 ID, 성별 등은 수정할 수 없습니다. </p>
    <p>4. 회사가 제공하는 서비스의 회원 ID는 회원 본인의 동의하에 회사가 운영하는 자사 사이트의 회원 ID와 연결될 수 있습니다. </p>
    <p>5. 회원 ID는 다음 각 호에 해당하는 경우에 회원 또는 회사의 요청으로 변경할 수 있습니다. </p>
    <p>① 회원 ID가 회원의 전화번호 또는 주민등록번호 등으로 등록되어 사생활 침해가 우려되는 경우</p>
    <p>② 타인에게 혐오감을 주거나 미풍양속에 어긋나는 경우</p>
    <p>③ 기타 합리적인 사유가 있는 경우 </p>
    <p>1. 기타 회원개인정보 관리 및 변경에 관한 사항은 서비스별 안내에 정하는 바에 의합니다.</p>
    <p> </p>
    <p>제3장 당사자의 의무 </p>
    <p>제10조 회사의 의무 </p>
    <p>1. 회사는 관련법과 본 약관이 금지하거나 공서양속에 반하는 행위를 하지 않으며, 본 약관이 정하는 바에 따라 지속적이고, 안정적으로 서비스를 제공하기 위하여 최선을 다하여야 합니다. </p>
    <p>2. 회사는 회원이 안전하게 서비스를 이용할 수 있도록 회원의 개인정보보호를 위한 보안시스템을 구축하며 개인정보 보호정책을 공시하고 준수합니다. </p>
    <p>3. 회사는 수신거절의 의사를 명백히 표시한 회원에 대해서는 회원이 원하지 않는 영리목적의 광고성 전자우편(이메일)을 발송하지 않습니다. </p>
    <p>4. 회사는 이용계약의 체결, 계약사항의 변경 및 해지 등 회원과의 계약관계절차 및 내용 등에 있어 회원에게 편의를 제공하도록 노력합니다. 회사 관리자에게 회원이 탈퇴 신청 메일을 보낼 경우 즉시 탈퇴 처리를 합니다. </p>
    <p>5. 회사는 이용고객으로부터 제기되는 의견이나 불만이 정당하다고 객관적으로 인정될 경우에는 적절한 절차를 거쳐 즉시 처리하여야 합니다. 다만, 즉시 처리가 곤란한 경우는 고객에게 그 사유와 처리 일정을 통보하여야 합니다. </p>
    <p>제11조 회원의 ID 및 비밀번호에 대한 의무 </p>
    <p>1. 회사가 관계법령 및 개인정보보호정책에 의하여 그 책임을 지는 경우를 제외하고, 회원 ID와 비밀번호의 관리책임은 회원에게 있습니다. </p>
    <p>2. 회원은 자신의 ID 및 비밀번호를 제3자가 이용하게 해서는 안됩니다. </p>
    <p>3. 회원이 자신의 ID 및 비밀번호를 도용당하거나 제3자가 사용하고 있음을 인지한 경우에는 바로 회사에 통보하고 회사의 안내가 있는 경우에는 그에 따라야 합니다. </p>
    <p>4. 회사는 회원이 상기 제1항, 제2항, 제3항을 위반하여 회원에게 발생한 손해에 대하여 책임을 일절 지지 않습니다. </p>
    <p>제12조 고객의 의무 </p>
    <p>1. 고객은 본 약관에 규정하는 사항과 기타 회사가 정한 제반 규정, 서비스 이용안내 또는 공지사항 등 회사가 공지 또는 통지하는 사항 및 관계법령을 준수하여야 하며, 기타 회사의 업무에 방해가 되는 행위, 회사의 명예를 손상 시키는 행위를 하여서는 안됩니다. </p>
    <p>2. 고객은 회사의 사전승낙 없이 서비스를 이용하여 영업활동을 할 수 없으며, 고객 간 또는 고객과 제3자 간에 서비스를 매개로 한 물품거래 및 서비스의 이용과 관련하여 기대하는 이익 등에 관하여 발생한 손해에 대하여 회사는 책임을 지지 않습니다. 이와 같은 영업활동으로 회사가 손해를 입은 경우 고객은 회사에 대하여 손해배상의 의무를 지며, 회사는 해당 고객에 대해 서비스 이용제한 및 적법한 절차를 거쳐 손해배상 등을 청구할 수 있습니다. </p>
    <p>3. 고객은 회사의 명시적인 동의가 없는 한 서비스의 이용권한, 기타 이용계약 상의 지위를 타인에게 양도, 증여할 수 없으며, 이를 담보로 제공할 수 없습니다. </p>
    <p>4. 고객은 서비스 이용과 관련하여 제24조 제1항의 각 호에 해당하는 행위를 하여서는 안됩니다. </p>
    <p>5. 고객은 회원가입 신청 또는 회원정보 변경 시 실명으로 모든 사항을 사실에 근거하여 작성하여야 하며, 허위 또는 타인의 정보를 등록할 경우 일체의 권리를 주장할 수 없습니다. </p>
    <p>6. 회원은 주소, 연락처, 전자우편 주소 등 이용계약사항이 변경된 경우에 해당 절차를 거쳐 이를 회사에 알려야 합니다. </p>
    <p>7. 고객은 회사 및 제3자의 지적 재산권을 침해해서는 안됩니다. </p>
    <p>8. 고객은 다음 각 호에 해당하는 행위를 하여서는 안되며, 해당 행위를 하는 경우에 회사는 회원의 서비스 이용제한 및 적법 조치를 포함한 제재를 가할 수 있습니다. </p>
    <p>① 회원가입 신청 또는 회원정보 변경 시 허위내용을 등록하는 행위 </p>
    <p>② 회사 운영진, 직원 또는 관계자를 사칭하는 행위 </p>
    <p>③ 회사로부터 특별한 권리를 부여받지 않고 회사의 프로그램을 변경하거나, 회사의 서버를 해킹하거나, 웹사이트 또는 게시된 정보의 일부분 또는 전체를 임의로 변경하는 행위 </p>
    <p>④ 서비스에 위해를 가하거나 고의로 방해하는 행위 </p>
    <p>⑤ 본 서비스를 통해 얻은 정보를 회사의 사전 승낙 없이 서비스 이용 외의 목적으로 복제하거나, 이를 출판 및 방송 등에 사용하거나, 제 3자에게 제공하는 행위 </p>
    <p>⑥ 회사 또는 제3자의 저작권 등 기타 지적재산권을 침해하는 내용을 전송, 게시, 전자우편 또는 기타의 방법으로 타인에게 유포하는 경우 </p>
    <p>⑦ 공공질서 및 미풍양속에 위반되는 저속, 음란한 내용의 정보, 문장, 도형, 음향, 동영상을 전송, 게시, 전자우편 또는 기타의 방법으로 타인에게 유포하는 행위</p>
    <p>⑧ 모욕적이거나 개인신상에 대한 내용이어서 타인의 명예나 프라이버시를 침해할 수 있는 내용을 전송, 게시, 전자우편 또는 기타의 방법으로 타인에게 유포하는 행위</p>
    <p>⑨ 다른 회원을 희롱 또는 위협하거나, 특정 고객에게 지속적으로 고통 또는 불편을 주는 행위</p>
    <p>⑩ 회사의 승인을 받지 않고 다른 고객의 개인정보를 수집 또는 저장하는 행위</p>
    <p>⑪ 범죄와 결부된다고 객관적으로 판단되는 행위 </p>
    <p>⑫ 본 약관을 포함하여 기타 회사가 정한 제반 규정 또는 이용 조건을 위반하는 행위</p>
    <p>⑬ 기타 관계법령에 위배되는 행위 </p>
    <p> </p>
    <p>제4장 서비스 이용 </p>
    <p>제13조 서비스의 제공 및 변경 </p>
    <p>1. 회사는 ‘세라캠퍼스’에서 다음과 같은 서비스를 제공합니다. </p>
    <p>① 재화 또는 용역에 대한 정보제공 및 구매계약의 체결</p>
    <p>② 구매계약이 체결된 재화 또는 용역의 공급</p>
    <p>③ 기타 ‘세라캠퍼스’를 통해 제공하는 제반 서비스 </p>
    <p>1. 회사는 재화의 품절 또는 기술적 사양의 변경 등의 경우에는 장차 체결되는 계약에 의해 제공될 재화 또는 용역의 내용을 변경할 수 있습니다. 이 경우에는 변경된 재화 또는 용역의 내용 및 제공일자를 명시하여 현재의 재화 또는 용역의 내용을 게시한 곳에 즉시 공지합니다.</p>
    <p>2. 회사가 제공하기로 ‘고객’과 계약을 체결한 서비스의 내용을 재화의 품절 또는 기술적 사양의 변경 등의 사유로 변경할 경우에는 제14조에 정한 방법으로 고객에게 통지합니다. </p>
    <p>제14조 정보의 제공 및 통지 </p>
    <p>1. 회사는 회원이 서비스 이용 중 필요하다고 인정되는 다양한 정보를 공지사항 혹은 전자우편 등의 방법으로 회원에게 제공할 수 있습니다. </p>
    <p>2. 회사는 불특정다수 회원에 대한 통지를 하는 경우 7일 이상 공지 게시판에 게시함으로써 개별 통지에 갈음할 수 있습니다. </p>
    <p>제15조 게시물의 저작권 및 관리 </p>
    <p> 1. 회사는 회원의 게시물을 소중하게 생각하며 변조, 훼손, 삭제되지 않도록 최선을 다하여 보호합니다. <br>
      다만, 다음 각 호에 해당하는 게시물이나 자료의 경우 사전통지 없이 삭제하거나 이동 또는 등록 거부를 할 수 있으며, 해당 회원의 자격을 제한, 정지 또는 상실시킬 수 있습니다. </p>
    <p>① 다른 회원 또는 제3자에게 심한 모욕을 주거나 명예를 손상시키는 내용인 경우</p>
    <p>② 공공질서 및 미풍양속에 위반되는 내용을 유포하거나 링크시키는 경우 </p>
    <p>③ 불법복제 또는 해킹을 조장하는 내용인 경우 </p>
    <p>④ 영리를 목적으로 하는 광고일 경우 </p>
    <p>⑤ 범죄적 행위에 결부된다고 인정되는 내용인 경우 </p>
    <p>⑥ 회사나 다른 회원의 저작권 혹은 제3자의 저작권 등 기타 권리를 침해하는 내용인 경우 </p>
    <p>⑦ 회사에서 규정한 게시물 원칙에 어긋나거나, 게시판 성격에 부합하지 않는 경우</p>
    <p>⑧ 스팸성 게시물인 경우 </p>
    <p>⑨ 기타 관계법령에 위배된다고 판단되는 경우 </p>
    <p> 1. 회사가 작성한 저작물에 대한 저작권 및 기타 지적재산권은 회사에 귀속됩니다. <br>
      회원이 서비스 화면 내에 게시한 게시물의 저작권은 게시한 회원에게 귀속됩니다. 또한 회사는 게시자의 동의없이 게시물을 상업적으로 이용할 수 없습니다. 다만, 비영리 목적인 경우는 그러하지 아니하며, 또한 본 사이트 내에서의 게재권을 갖습니다. </p>
    <p>2. 회원은 서비스를 이용하여 취득한 정보를 회사의 사전 승낙없이 임의가공, 판매, 복제, 송신, 출판, 배포, 방송 기타 방법에 의하여 영리목적으로 이용하거나 제3자에게 이용하게 하여서는 안됩니다. </p>
    <p>제16조 광고게재 및 광고주와의 거래 </p>
    <p>1. 회사가 제공하는 서비스 내에 다양한 배너와 링크(Link)를 포함할 수 있으며, 이는 광고주와의 계약관계에 의하거나 제공받은 콘텐츠의 출처를 밝히기 위한 조치입니다. 회원은 서비스 이용 시 노출되는 광고게재에 대해 동의합니다. </p>
    <p>2. 서비스 내에 포함되어 있는 링크를 클릭하여 타 사이트의 페이지로 옮겨갈 경우 해당 사이트의 개인정보보호정책은 회사와 무관합니다. </p>
    <p>제17조 서비스 이용시간 </p>
    <p>1. 서비스 이용은 회사의 업무상 또는 기술상 특별한 지장이 없는 한 연중무휴, 1일 24시간 운영을 원칙으로 합니다. 다만, 회사는 시스템 정기점검, 증설 및 교체를 위해 회사가 정한 날이나 시간에 서비스를 일시 중단할 수 있으며, 예정되어 있는 작업으로 인한 서비스 일시중단은 회사가 제공하는 서비스를 통해 사전에 공지합니다. </p>
    <p>2. 회사는 서비스를 일정 범위로 분할하여 각 범위별로 이용가능시간을 별도로 지정할 수 있습니다. 다만, 이 경우 그 내용을 공지합니다. </p>
    <p>제18조 서비스 제공의 중단 등 </p>
    <p>1. 회사는 다음 각 호의 내용에 해당하는 경우 서비스 제공의 일부 혹은 전부를 제한하거나 중단할 수 있습니다. </p>
    <p>① 정보통신설비의 보수 점검, 교체 및 고장 등 공사로 인한 부득이 한 경우</p>
    <p>② 기간통신사업자가 전기통신서비스를 중단했을 경우 </p>
    <p>③ 서비스 이용의 폭주 등으로 정상적인 서비스 이용에 지장이 있는 경우</p>
    <p>④ 국가비상사태 등 기타 불가항력적인 사유가 있는 경우 </p>
    <p>1. 전항에 의한 서비스 중단의 경우에는 회사는 제14조에 정한 방법으로 그 사유 및 기간 등을 공지합니다. 다만, 회사가 통제할 수 없는 사유로 발생한 서비스의 중단(시스템관리자의 고의, 과실없는 디스크 장애, 시스템 다운 등)에 대하여 사전공지가 불가능한 경우에는 예외로 합니다. </p>
    <p>제19조 회원 탈퇴 및 자격 상실 </p>
    <p> 1. 회원은 회사에 언제든지 탈퇴를 요청할 수 있으며 회사는 즉시 회원탈퇴를 처리합니다. <br>
      예)단, 회원 탈퇴시점부터 1주일 동안은 동일한 아이디로 회원가입을 할 수 없습니다. </p>
    <p>2. 회원이 다음 각 호의 사유에 해당하는 경우 회사는 회원자격을 제한 및 정지시킬 수 있습니다. </p>
    <p>① 가입신청 시에 허위 내용을 기재한 경우</p>
    <p>② ‘세라캠퍼스’를 통하여 구입한 재화나 용역 등의 대금, 기타 ‘세라캠퍼스’ 이용과 관련하여 회원이 부담하는 채무를 기일에 지급하지 않은 경우</p>
    <p>③ 다른 고객의 ‘세라캠퍼스’ 이용을 방해하거나 그 정보를 도용하는 등 전자 상거래 질서를 위협하는 경우 </p>
    <p>④ ‘세라캠퍼스’를 이용하여 법령과 이 약관이 금지하거나 공서양속에 반하는 행위를 하는 경우 </p>
    <p>⑤ 회사가 회원자격을 제한, 정지시킨 후 동일한 행위가 2회 이상 반복되거나 30일 이내에 그 사유가 시정되지 아니하는 경우 회사는 회원자격을 상실시킬 수 있습니다.</p>
    <p>⑥ 회사가 회원자격을 상실시키는 경우에는 회원등록을 말소합니다. 이 경우 회원에게 통지하고, 회원등록 말소 전에 소명할 기회를 부여합니다. </p>
    <p> </p>
    <p>제5장 구매계약 체결 </p>
    <p>제20조 구매신청 </p>
    <p>1. ‘회원’은 ‘세라캠퍼스’에서 제공하는 유료서비스에 대해 해당서비스의 검색, 필요사항 기재, 결제방법의 선택 등을 통해 구매를 신청할 수 있고 회사는 이에 대해 수신확인통지를 합니다. </p>
    <p>2. 수신확인통지를 받은 ‘회원’은 의사표시의 불일치 등이 있는 경우에는 수신확인통지를 받은 후 즉시 구매신청 변경 및 취소를 요청할 수 있고, 이 경우 회사는 제23조의 구매계약 해지 등에 관한 규정에 따릅니다. </p>
    <p>3. 회사는 본 조 제1항의 구매신청에 대하여 다음 각 호에 해당할 경우 승낙하지 않을 수 있습니다. 단, 미성년자와 계약을 체결하는 경우에는 법정대리인의 동의를 얻지 못하면 미성년자 본인 또는 법정대리인이 계약을 취소할 수 있다는 내용을 고지하여야 합니다. </p>
    <p>① 신청 내용에 허위, 기재누락, 오기가 있는 경우 </p>
    <p>② 기타 구매신청에 승낙하는 것이 ‘세라캠퍼스’기술상 현저히 지장이 있다고 판단하는 경우 </p>
    <p>제21조 구매계약의 성립 </p>
    <p>회사의 승낙이 수신확인통지의 형태로 ‘회원’에게 도달한 시점에 구매계약이 성립된 것으로 봅니다. </p>
    <p>제22조 지급방법 </p>
    <p>‘세라캠퍼스’에서 구매한 재화 또는 용역에 대한 대금지급방법은 다음 각 호의 방법 중 가용한 방법으로 할 수 있습니다. </p>
    <p>① 폰뱅킹, 인터넷뱅킹, 메일 뱅킹 등의 각종 계좌 이체</p>
    <p>② 선불카드, 직불카드, 신용카드 등의 각종 카드 결제 </p>
    <p>③ 온라인 무통장입금</p>
    <p>④ 전자화폐에 의한 결제 </p>
    <p>⑤ 회사와 계약을 맺었거나 회사가 인정한 상품권에 의한 결제 </p>
    <p>⑥ 기타 전자적 지급 방법에 의한 대금 지급 등 </p>
    <p> </p>
    <p>제6장 계약해지 및 이용제한 </p>
    <p>제23조 구매계약 해지 등 </p>
    <p>1. ‘세라캠퍼스’에서 제공하는 서비스에 대해 회사와 구매계약을 체결한 ‘회원’은 회원 본인이 실명, ID, 주민등록번호 등을 회사에 통보하여 해지신청을 하여야 합니다. </p>
    <p>2. ‘회원’이 서비스 구매계약에 대해 해지신청(취소 및 환불 요청)한 경우, 회사는 ‘회원’의 해지 의사표시가 회사에 도달한 시점을 기준으로 전체 서비스 기간 중 잔여 기간에 상당하는 단위대금을 정산하여 환급합니다. </p>
    <p> 3. ‘회원’이 서비스이용가능일로부터 7일이 경과한 이후 해지신청(취소 및 환불 요청)한 경우, 회사는 총계약대금의 10%에 해당하는 위약금을 환급 시 구매대금에서 공제할 수 있습니다. <br>
      단, 회사의 귀책사유로 인해 해지된 경우에는 그러하지 않습니다. </p>
    <p>제24조 서비스 이용제한 </p>
    <p>1. 회원이 본 약관 제11조, 제12조 내용을 위반하거나 다음 각 호에 해당하는 행위를 하는 경우에 회사는 회원의 서비스 이용을 제한할 수 있습니다. </p>
    <p>① 미풍양속을 저해하는 비속한 ID 및 별명 사용</p>
    <p>② 타 고객에게 심한 모욕을 주거나, 서비스 이용을 방해한 경우</p>
    <p>③ 정보통신 윤리위원회 등 관련 공공기관의 시정 요구가 있는 경우 </p>
    <p>④ 6개월 이상 서비스를 이용한 적이 없는 경우</p>
    <p> ⑤ 불법 게시물을 게재한 경우 <br>
      - 상용소프트웨어나 크랙파일을 올린 경우 <br>
      - 정보통신윤리위원회의 심의 세칙 제 7조에 어긋나는 음란물을 게재한 경우 <br>
      - 반국가적 행위의 수행을 목적으로 하는 내용을 포함한 경우 <br>
      - 저작권이 있는 글을 무단 복제하거나 mp3를 게재한 경우 </p>
    <p>⑥ 기타 정상적인 서비스 운영에 방해가 될 경우 </p>
    <p>2. 상기 이용제한 규정에 따라 서비스를 이용하는 회원에게 서비스 이용에 대하여 별도 공지 없이 서비스 이용의 일시정지, 초기화, 이용계약 해지 등을 불량이용자 처리규정에 따라 취할 수 있으며, 회원은 전항의 귀책사유로 인하여 회사나 다른 고객에게 입힌 손해를 배상할 책임이 있습니다. </p>
    <p> </p>
    <p>제7장 기타사항 </p>
    <p>제25조 면책조항 </p>
    <p>1. 회사는 천재지변, 전쟁 및 기타 불가항력, 회사의 합리적인 통제범위를 벗어난 사유로 인하여 서비스를 제공할 수 없는 경우에는 그에 대한 책임이 면제됩니다. </p>
    <p>2. 회사는 기간통신 사업자가 전기통신 서비스를 중지하거나 정상적으로 제공하지 아니하여 손해가 발생한 경우 책임이 면제됩니다. </p>
    <p>3. 회사는 서비스용 설비의 보수, 교체, 정기점검, 공사 등 부득이한 사유로 발생한 손해에 대한 책임이 면제됩니다. </p>
    <p>4. 회사는 고객의 귀책사유로 인한 서비스 이용의 장애 또는 손해에 대하여 책임을 지지 않습니다. </p>
    <p>5. 회사는 고객의 컴퓨터 오류에 의해 손해가 발생한 경우, 또는 회원이 신상정보 및 전자우편 주소를 부실하게 기재하여 손해가 발생한 경우 책임을 지지 않습니다. </p>
    <p>6. 회사는 회원이 서비스를 이용하여 기대하는 수익을 얻지 못하거나 상실한 것에 대하여 책임을 지지 않습니다. </p>
    <p>7. 회사는 회원이 서비스를 이용하면서 얻은 자료로 인한 손해에 대하여 책임을 지지 않습니다. 또한 회사는 회원이 서비스를 이용하며 타 회원으로 인해 입게 되는 정신적 피해에 대하여 보상할 책임을 지지 않습니다. </p>
    <p>8. 회원이 서비스 화면에 게재한 정보, 자료, 사실 등의 내용에 관한 신뢰도 혹은 정확성에 대하여는 해당회원이 책임을 부담하며, 회사는 내용의 부정확 또는 허위로 인해 회원 또는 제3자에게 발생한 손해에 대하여는 아무런 책임을 부담하지 않습니다. </p>
    <p>9. 회사는 회원 상호 간 및 회원과 제3자 상호 간에 서비스를 매개로 발생한 분쟁에 대해 개입할 의무가 없으며, 이로 인한 손해를 배상할 책임도 없습니다. </p>
    <p>10. 회사는 서비스 이용과 관련하여 회원의 고의 또는 과실로 인하여 회원 또는 제3자에게 발생한 손해에 대하여는 아무런 책임을 부담하지 않습니다. </p>
    <p>11. 회사에서 회원에게 무료로 제공하는 서비스의 이용과 관련해서는 개인정보보호정책에서 정하는 내용을 제외하고 어떠한 손해도 책임을 지지 않습니다. </p>
    <p>제26조 분쟁의 해결 </p>
    <p> 회사와 회원은 서비스와 관련하여 발생한 분쟁을 원만하게 해결하기 위하여 필요한 노력을 합니다. <br>
      회사는 회원으로부터 제출되는 불만사항 및 의견을 우선적으로 처리합니다. 다만, 신속한 처리가 곤란한 경우에는 회원에게 그 사유와 처리일정을 즉시 통보합니다. </p>
    <p>제27조 재판권 및 준거법 </p>
    <p>회사와 회원 간에 서비스 이용으로 발생한 분쟁에 대하여는 대한민국 법을 적용하며, 본 분쟁으로 인하여 소송이 제기될 경우 민사소송법상의 관할을 가지는 대한민국의 법원에 제기합니다. </p>
    <p>[부칙] </p>
    <p>본 약관은 2012년 10월 07일부터 적용됩니다.</p>
              					</div>
								<div class="fr" style="margin:5px 10px 0 0">
									<input name="rdoUserAgreement" type="radio" value="agree"/><label class="mr5">동의합니다</label>
									<input name="rdoUserAgreement" type="radio" value="disagree"/>동의하지 않습니다
								</div></li>
						</ul>
					</div>
					<!-- Joint Content //-->
					<!-- Joint Content -->
					<div class="join_content_area">
						<ul>
							<li class="sub_label">세라캠퍼스<br /> 개인 정보 취급방침</li>
							<li>
								<div class="joinus_policy_section">
<p>회사는 개인정보취급방침을 통하여 고객님께서 제공하시는 개인정보가 어떠한 용도와 방식으로 이용되고 있으며, 개인정보보호를 위해 어떠한 조치가 취해지고 있는지 알려드립니다. 회사는 개인정보취급방침을 개정하는 경우 웹사이트 공지사항(또는 개별공지)를 통하여 공지할 것입니다.</p>
    <p> </p>
    <p>제1조 수집하는 개인정보 항목</p>
    <p>1) 회사는 회원가입, 상담, 서비스 신청 등을 위해 아래와 같은 개인정보를 수집하고 있습니다. </p>
    <p>① 수집항목 : 성명, 아이디, 비밀번호, 주민등록번호, 이메일, 주소, 전화번호, 휴대전화번호, 은행계좌정보, 신용카드 정보, 법정대리인 정보, 서비스 이용기록, 접속 로그, 쿠키 </p>
    <p>② 개인정보 수집방법 : 홈페이지(www.seracampus.com) </p>
    <p>2) 개인정보의 수집 및 이용목적</p>
    <p>회사는 수집한 개인정보를 다음의 목적을 위해 활용합니다.</p>
    <p>① 서비스 제공에 관한 계약 이행 및 서비스 제공에 따른 요금정산 목적, 학습진행, 컨텐츠 제공, 구매 및 요금 결제</p>
    <p> ② 회원 관리 <br>
      회원제 서비스 이용에 따른 본인확인, 개인 식별, 불량회원의 부정 이용 방지와 비인가사용 방지, 가입 의사 확인, 연령확인, 불만처리 등 민원처리, 고지사항 전달 </p>
    <p> ③ 마케팅 및 광고에 활용 <br>
      신규 서비스(제품) 개발 및 특화, 이벤트 등 광고성 정보 전달, 인구통계학적 특성에 따른 서비스 제공 및 광고 게재, 접속 빈도 파악 또는 회원의 서비스 이용에 대한 통계 </p>
    <p> ④ 고용보험 과정의 노동부 신고 <br>
      회원이 신청한 과정이 고용보험 대상 과정인 경우 고용보험 환급을 이유로 노동부에 신고하기 위해 개인정보를 수집합니다. </p>
    <p>3) 개인정보의 보유 및 이용기간 </p>
    <p>회사는 수집한 개인정보를 다음의 목적을 위해 활용합니다. </p>
    <p>제2조 개인정보의 파기절차 및 방법 </p>
    <p>회사는 원칙적으로 개인정보 수집 및 이용목적이 달성된 후에는 해당 정보를 지체 없이 파기합니다. 파기절차 및 방법은 다음과 같습니다. </p>
    <p>① 파기절차 : 회사는 고객님께서 제공하신 모든 정보를 고객님께서 서비스를 제공받으시는 동안 보유하며 서비스 제공을 위해서만 이용하게 됩니다. 또한, 고객님께서 회원탈퇴를 요청한 경우 고객님의 개인정보는 재생 불가능한 방법으로 저장장치에서 완전히 삭제되며, 어떠한 방법으로도 열람 또는 이용할 수 없도록 처리됩니다. </p>
    <p>② 파기방법 : 전자적 파일형태로 저장된 개인정보는 기록을 재생할 수 없는 기술적 방법을 사용하여 삭제합니다. </p>
    <p>제3조 수집한 개인정보의 위탁 </p>
    <p>회사는 고객님의 동의 없이 고객님의 정보를 외부 업체에 위탁하지 않습니다. 향후 그러한 필요가 생길 경우, 위탁 대상자와 위탁 업무 내용에 대해 고객님에게 통지하고 필요한 경우 사전 동의를 받도록 하겠습니다. </p>
    <p>① 이용자들이 사전에 동의한 경우 </p>
    <p>② 법령의 규정에 의거하거나, 수사 목적으로 법령에 정해진 절차와 방법에 따라 수사기관의 요구가 있는 경우 </p>
    <p>제4조 이용자 및 법정대리인의 권리와 그 행사방법 </p>
    <p>① 이용자는 언제든지 등록되어 있는 자신의 개인정보를 조회하거나 수정할 수 있으며 가입해지를 요청할 수도 있습니다. </p>
    <p>② 이용자들의 개인정보 조회, 수정을 위해서는 ‘환경설정’(내의 ‘프로필 관리’)를 클릭하여 본인 확인 절차를 거치신 후 직접 열람, 정정이 가능합니다.</p>
    <p>③ 고객님은 언제든지 등록되어 있는 고객님의 개인정보를 열람하거나 정정하실 수 있으며 회원탈퇴를 요청하실 수 있습니다. </p>
    <p>④ 회원탈퇴는 고객님의 성함과 아이디, 주민등록번호를 이메일(help@seracampus.com)이나 고객센터(02-701-0564)로 통보해 주시면 처리됩니다. </p>
    <p>⑤ 회원탈퇴 후, 고객님의 개인정보는 세라캠퍼스에서 완전히 삭제됩니다. 개인정보에 관한 불만이나 의견이 있으신 분은 세라캠퍼스 고객센터로 문의주시면 접수 즉시 조치하고 처리결과를 통보해 드립니다. </p>
    <p>제5조 개인정보 자동수집 장치의 설치, 운영 및 그 거부에 관한 사항 </p>
    <p>회사는 귀하의 정보를 수시로 저장하고 찾아내는 ‘쿠키(cookie)’ 등을 운용합니다. 쿠키란 회사의 웹사이트를 운영하는데 이용되는 서버가 귀하의 브라우저에 보내는 작은 텍스트 파일로서 귀하의 컴퓨터 하드디스크에 저장됩니다. 회사는 다음과 같은 목적을 위해 쿠키를 사용합니다. </p>
    <p>1) 개인정보의 보유 및 이용기간</p>
    <p>① 회원과 비회원의 접속 빈도나 방문 시간 등을 분석, 이용자의 취향과 관심분야를 파악 및 자취 추적, 각종 이벤트 참여 정도 및 방문 회수 파악 등을 통한 타겟 마케팅 및 개인 맞춤 서비스 제공</p>
    <p>② 귀하는 쿠키 설치에 대한 선택권을 가지고 있습니다. 따라서, 귀하는 웹브라우저에서 옵션을 설정함으로써 모든 쿠키를 허용하거나, 쿠키가 저장될 때마다 확인을 거치거나, 아니면 모든 쿠키의 저장을 거부할 수도 있습니다. </p>
    <p>2) 쿠키 설정 거부 방법 </p>
    <p>① 예 : 쿠키 설정을 거부하는 방법으로는 회원님이 사용하시는 웹 브라우저의 옵션을 선택함으로써 모든 쿠키를 허용하거나 쿠키를 저장할 때마다 확인을 거치거나, 모든 쿠키의 저장을 거부할 수 있습니다. </p>
    <p>② 설정방법 예(인터넷 익스플로어의 경우) : 웹 브라우저 상단의 도구 &gt;인터넷 옵션 &gt;개인정보 </p>
    <p>제6조 개인정보에 관한 민원서비스 </p>
    <p>회사는 고객의 개인정보를 보호하고 개인정보와 관련한 불만을 처리하기 위하여 아래와 같이 관련 부서 및 개인정보관리책임자를 지정하고 있습니다. </p>
    <p>개인정보/청소년보호 관리 책임자</p>
    <p>이름 : 김성환</p>
    <p>소속 : 세라인재개발원(주)</p>
    <p>연락처: 02-701-0564</p>
    <p>E-mail: help@seracampus.com</p>
    <p> </p>
    <p>① 귀하께서는 회사의 서비스를 이용하시며 발생하는 모든 개인정보보호 관련 민원을 개인정보관리책임자 혹은 담당부서로 신고하실 수 있습니다. </p>
    <p>② 설정방법 예(인터넷 익스플로어의 경우) : 웹 브라우저 상단의 도구 &gt;인터넷 옵션 &gt;개인정보 </p>
    <p>③ 개인정보침해에 대한 신고나 상담이 필요하신 경우에는 아래 기관에 문의하시기 바랍니다. </p>
    <p>① 개인분쟁조정위원회 (http://www.1336.or.kr/1336) </p>
    <p>② 정보보호마크인증위원회 (http://www.eprivacy.or.kr/02-580-0533~4) </p>
    <p>③ 대검찰청 인터넷범죄수사센터 (http://icic.sppo.go.kr/02-3480-3600) </p>
    <p> ④ 경찰청 사이버테러대응센터 (http://www.ctrc.go.kr/02-392-0330)    </p>
              					</div>
								<div class="fr" style="margin:5px 10px 0 0">
									<input name="rdoPrivacyAgreement" type="radio" value="agree"/><label class="mr5">동의합니다</label>
									<input name="rdoPrivacyAgreement" type="radio" value="disagree"/>동의하지 않습니다
								</div></li>
						</ul>
					</div>
					<!-- Joint Content //-->
					<!-- Joint Content -->
					<div class="join_content_area">
						<ul>
							<li class="sub_label">주민등록번호 <br /> 본인 인증</li>
							<li>세라 캠퍼스는 깨끗한 인터넷 환경을 위해 실명확인으로 본인인증을 시행하고 있습니다.  회원정보는 동의
								없이 공개되지 않으며, 개인정보취급방침에 의해 보호받고 있습니다.<br> <br> 국내 거주
										외국인인 경우 외국인등록증에 쓰여진 순서대로 이름과 외국인등록번호를 정확히 입력해 주시기 바랍니다.
										<div class="mt5">
											<input name="chkForeigner" type="checkbox"/> 외국인인 경우 체크
										</div>
										<div class="mt30"> 이름 
											<input name="txtUserName" type="text" class="fieldline mr10" style="width: 100px" /> 주민등록번호 
											<input name="txtUserRRN1" type="text" class="fieldline" style="width: 100px" /> - 
											<input name="txtUserRRN2" type="text" class="fieldline" style="width: 100px" />
										</div>
										<div class="t_refe mt10">
											타인의 주민등록번호를 도용하여 가입하는 행위는 3년 이하의 징역 또는 1천 만원 이하의 벌금에 부과될 수
											있습니다. <br /> [관련 법률: 주민등록법 제37조(벌칙) 제9호(시행일 2006.09.24)]
										</div>
							</li>
						</ul>
					</div>
					<!-- Joint Content //-->
					<!-- Btn -->
					<div class="btn_space">
						<div class="btn_area">
							<div class="btn_blu_l js_joinus_first_btn">
								<div class="btn_blu_r">본인인증 회원가입</div>
							</div>
						</div>
					</div>
					<!-- Btn //-->
				</div>
				<!-- Content //-->
			</div>
			<div id="join_section" class="js_joinus_second" style="display:none"">
				<!-- SNB Left -->
				<div class="snb"><img width="176" height="146" alt="" src="sera/images/function_title2.gif" />
				</div>
				<!-- SNB Left//-->
				<!-- Content -->
				<div id="join_content_section">
					<div class="tit"></div>
					<div class="join_process">
						<ul>
							<li class="prs1">1단계</li>
							<li class="ar"></li>
							<li class="prs2 current">2단계</li>
							<li class="ar"></li>
							<li class="prs3">3단계</li>
						</ul>
					</div>
					<!-- Joint Content -->
					<div class="join_content_area">
						<form name="frmCreateSeraUser" class="form_layout js_validation_required">
							<div class="t_refe mt10">* 선택입력란입니다</div>
							<table>
								<tr>
									<td>
										<div class="form_label">회원ID</div>
										<div class="form_value">
											<input name="txtUserId" type="text" class="fieldline fl required email" style="width:150px" value=""/>
											<div class="btn_mid_l ml5 js_check_iddup_btn"><div class="btn_mid_r">중복확인</div></div>
											<div class="btn_mid_l ml5 js_change_id_btn" style="display:none"><div class="btn_mid_r">아이디변경</div></div>
										</div>
									</td>
									<td rowspan="9" valign="top" class="m0">
										<!-- 사진 올리기 -->
										<div class="profile_photo js_joinus_profile_field" style="width:145px"></div>
										<div class="t_refe" style="margin:24px 0 0 25px">
											* 사진은 자동으로<br />118x118으로 변경됩니다
										</div> <!-- 사진 올리기 //--></td>
								</tr>
								<tr>
									<td>
										<div class="form_label">닉네임*</div>
										<div class="form_value">
											<input name="txtNickName" type="text" class="fieldline" style="width:150px" value=""/>
											<div class="t_refe mt5">
												* 닉네임은 한/영.숫자 최대 15자까지 가능합니다.<br /> * 닉네임은 추후 프로필 수정에서 입력
												혹은 변경 가능합니다.
											</div>
										</div>
									</td>
								</tr>
 								<tr>
									<td>
										<div class="form_label">비밀번호</div>
										<div class="form_value">
											<input name="txtPassword" type="password" class="fieldline fl required" style="width:100px" value=""/>
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div class="form_label">비밀번호 확인</div>
										<div class="form_value">
											<input name="txtConfirmPassword" type="password" class="fieldline required" style="width:100px" value=""/>
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div class="form_label">생일/성별</div>
										<div class="form_value">
											<input name="txtBirthYear" class="fieldline form_date_input number required" maxlength="4" type="text" value=" "/> 년 
											<input name="txtBirthMonth" class="fieldline form_date_input number required" maxlength="2" type="text" value=" "/> 월 
											<input name="txtBirthDay" class="fieldline form_date_input number required" maxlength="2" type="text" value=" "/> 일 
											<select name="selSex">
												<option value="female">여자</option>
												<option value="male">남자</option>
											</select>
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div class="form_label">도전목표*</div>
										<div class="form_value">
											<input name="txtChallengingTarget" class="fieldline" type="text" style="width:300px" value=" "/>
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div class="form_label">관심분야</div>
										<div class="form_value">
											<input name="txtInterestPart" class="fieldline" type="text" value=" "/>
											<div class="cb t_refe pt5">* 관심있는 키워드를 입력해 주세요.</div>
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div class="form_label">SNS연동 *</div>
										<div class="form_value">
											<div class="icon_facebook fl mr5"></div>
											<div class="icon_twitter fl mr5"></div>
											<input name="" type="checkbox"/><label>연동여부</label>
										</div>
									</td>
								</tr>
							</table>
						</form>
					</div>
					<!-- Joint Content //-->
					<div class="sw_error_message tl" style="color: red"></div>
					<!-- Btn -->
					<div class="btn_space">
						<div class="btn_area">
							<div class="btn_blu_l mr5 js_joinus_second_btn">
								<div class="btn_blu_r">가입하기</div>
							</div>
							<div class="btn_blu_l">
								<a href="joinUs.sw"><div class="btn_blu_r">취 소</div></a>
							</div>
						</div>
					</div>
					<!-- Btn //-->
				</div>
				<!-- Content //-->
			</div>
			<div id="join_section" class="js_joinus_third" style="display:none">
				<!-- SNB Left -->
				<div class="snb">
					<img width="176" height="146" 
						src="sera/images/function_title2.gif" />
				</div>
				<!-- SNB Left//-->
				<!-- Content -->
				<div id="join_content_section">
					<div class="tit"></div>
					<div class="join_process">
						<ul>
							<li class="prs1">1단계</li>
							<li class="ar"></li>
							<li class="prs2">2단계</li>
							<li class="ar"></li>
							<li class="prs3 current">3단계</li>
						</ul>
					</div>
					<!-- Joint Content -->
					<div class="join_content_area">
						<ul>
							<li class="sub_label">회원가입 확인</li>
							<li>
								<div class="tb">회원 가입이 완료되었습니다.</div> 상단에 있는 로그인 입력란에 ID와 비밀번호를
								입력 후, <br /> 로그인하여 SERA 서비스를 이용해 주시기 바랍니다.</li>
						</ul>
					</div>
					<!-- Joint Content //-->
					<!-- Btn -->
					<div class="btn_space">
						<div class="btn_area">
							<div class="btn_blu_l js_joinus_third_btn">
								<div class="btn_blu_r">확 인</div>
							</div>
						</div>
					</div>
					<!-- Btn //-->
				</div>
				<!-- Content //-->
			</div>
		</div>
		<!-- Footer -->
		<div id="sera_footer">
			<ul class="footer_box">
				<li class="copyright"><img width="270" height="20" src="sera/images/sera2_footer_Copyright.png" /></li>
				<li class="policy"><a href="">회사소개</a> | <a href="">이용약관</a> | <a href="">개인정보취급방침</a> | <a href="">고객센터</a></li>
			</ul>
		</div>
		<!-- Footer //-->
	</div>
</body>
</html>

<script type="text/javascript">

loadJoinUsFields();

function submitForms() {
	var joinUs = $('.js_joinus_page');
	if(!joinUs.find('input[name="txtUserId"]').hasClass('sw_dup_checked')){
		smartPop.showInfo(smartPop.ERROR, "아이디 중복확인을 수행하지 않았습니다!");
		return false;
	}
	if (SmartWorks.GridLayout.validate(joinUs.find('form.js_validation_required'), $('.js_profile_error_message'))) {
		var forms = joinUs.find('form');
		var paramsJson = {};
		for(var i=0; i<forms.length; i++){
			var form = $(forms[i]);
			if(form.attr('name') === 'frmSmartForm'){
				paramsJson['formId'] = form.attr('formId');
				paramsJson['formName'] = form.attr('formName');
			}
			paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
		}
		console.log(JSON.stringify(paramsJson));
		var url = "create_sera_user.sw";
		var progressSpan = joinUs.find('.js_progress_span');
		smartPop.progressCont(progressSpan);
		$.ajax({
			url : url,
			contentType : 'application/json',
			type : 'POST',
			data : JSON.stringify(paramsJson),
			success : function(data, status, jqXHR) {
				// 사용자정보 수정이 정상적으로 완료되었으면, 현재 페이지에 그대로 있는다.
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.INFORM, "세라회원으로 가입되셨습니다. 가입을 축하드립니다!");
			},
			error : function(e) {
				smartPop.closeProgress();
				smartPop.showInfo(smartPop.ERROR, "가입하는데 문제가 발생하였습니다. 세라직원에게 문의하시기 바랍니다!");
			}
		});
	}
};

$(function() {
	$('.js_joinus_first_btn').live('click', function(e){
		var input = $(e.target);
		var joinusFirst = input.parents('.js_joinus_first');
		var userAgreement = joinusFirst.find('input[name="rdoUserAgreement"]:checked').attr('value');
		if(userAgreement !== "agree"){
			smartPop.showInfo(smartPop.WARN, '회원이용약관에 동의하여야 회원가입을 할 수 있습니다.');
			return false;
		}
		var privacyAgreement = joinusFirst.find('input[name="rdoPrivacyAgreement"]:checked').attr('value');
		if(privacyAgreement !== "agree"){
			smartPop.showInfo(smartPop.WARN, '개인정보취급방침에 동의하여야 회원가입을 할 수 있습니다.');			
			return false;
		}
		var chkForeigner = joinusFirst.find('input[name="chkForeigner"]').attr('value');
		var userName = joinusFirst.find('input[name="txtUserName"]').attr('value');
		var userRRN1 = joinusFirst.find('input[name="txtUserRRN1"]').attr('value');
		var userRRN2 = joinusFirst.find('input[name="txtUserRRN2"]').attr('value');
		if(isEmpty(userName) || isEmpty(userRRN1) || isEmpty(userRRN2)){
			smartPop.showInfo(smartPop.WARN, '실명확인을 위해 이름과 주민등록번호를 반드시 입력하여야 합니다.');			
			return false;			
		}
		input.parents('.js_joinus_first').hide().next().show();
		return false;
	});
	
	$('.js_joinus_second_btn').live('click', function(e){
		var input = $(e.target);
		var joinUs = $('.js_joinus_page');
		if(!joinUs.find('input[name="txtUserId"]').hasClass('sw_dup_checked')){
			smartPop.showInfo(smartPop.WARN, "아이디 중복확인을 수행하지않았습니다. 중복확인을 수행바랍니다!");
			return false;
		}
		if (SmartWorks.GridLayout.validate(joinUs.find('form.js_validation_required'), $('.js_profile_error_message'))) {
			var forms = joinUs.find('form');
			var paramsJson = {};
			for(var i=0; i<forms.length; i++){
				var form = $(forms[i]);
				if(form.attr('name') === 'frmSmartForm'){
					paramsJson['formId'] = form.attr('formId');
					paramsJson['formName'] = form.attr('formName');
				}
				paramsJson[form.attr('name')] = mergeObjects(form.serializeObject(), SmartWorks.GridLayout.serializeObject(form));
			}
			console.log(JSON.stringify(paramsJson));
			var url = "create_sera_user.sw";
			var progressSpan = joinUs.find('.js_progress_span');
			smartPop.progressCont(progressSpan);
			$.ajax({
				url : url,
				contentType : 'application/json',
				type : 'POST',
				data : JSON.stringify(paramsJson),
				success : function(data, status, jqXHR) {
					// 사용자정보 수정이 정상적으로 완료되었으면, 현재 페이지에 그대로 있는다.
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.INFORM, "세라회원으로 가입되셨습니다. 가입을 축하드립니다!");					
					input.parents('.js_joinus_second').hide().next().show();
				},
				error : function(e) {
					smartPop.closeProgress();
					smartPop.showInfo(smartPop.ERROR, "가입하는데 문제가 발생하였습니다. 세라직원에게 문의하시기 바랍니다!");
				}
			});
		}
		return false;
	});
	$('.js_joinus_third_btn').live('click', function(e){
		var input = $(e.target);
		return false;
	});
	
	$('.js_check_iddup_btn').live('click', function(e){
		var input = $(e.target).parent();
		var target = input.siblings('input[name="txtUserId"]');
		var userId = target.attr('value');
		if(!target.valid()){
			smartPop.showInfo(smartPop.ERROR, "이메일만 아이디로 사용할 수 있습니다. 유호한 이메일을 입력바랍니다.");
			return false;
		}
		$.ajax({
			url : "check_id_duplication.sw",
			data : {
				userId : userId
			},
			success : function(data, status, jqXHR) {
				smartPop.showInfo(smartPop.INFORM, "사용가능한 아이디 입니다.");
				target.addClass('sw_dup_checked').attr('readonly', true);
				input.hide().siblings('.js_change_id_btn').show();;
			},
			error : function(xhr, ajaxOptions, thrownError){
				smartPop.showInfo(smartPop.WARN, "이미 사용중인 아이디 입니다.");
			}
		});
		return false;
	});
	
	$('.js_change_id_btn').live('click', function(e) {
		var input = $(e.target).parent();
		var target = input.siblings('input[name="txtUserId"]');
		target.removeClass('sw_dup_checked').attr('readonly', false);
		input.hide().siblings('.js_check_iddup_btn').show();;
		return false;
	});

});
</script>
