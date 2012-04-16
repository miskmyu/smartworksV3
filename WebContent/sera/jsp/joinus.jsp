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
	<link type="text/css" rel="stylesheet" href="css/default.css">
	<link href="css/black/pop.css" type="text/css" rel="stylesheet" /></link>
	<link type="text/css" rel="stylesheet" href="sera/css/form.css">
	<link type="text/css" rel="stylesheet" href="sera/css/page.css">
	<script type="text/javascript" src="js/jquery/jquery-1.6.2.min.js"></script>
	<script type="text/javascript" src="js/sw/sw-language.js"></script>
	<script type="text/javascript" src="js/sw/sw-language-ko.js"></script>
	<script type="text/javascript" src="js/sw/sw-language-en.js"></script>
	<script type="text/javascript" src="sera/js/rolling_img.js"></script>
	<script type="text/javascript" src="js/sw/sw-util.js"></script>
	<script type="text/javascript" src='js/sw/sw-popup.js'></script>
	
</head>
<body>
	<div id="wrap" class="js_joinus_page">
		<div id="sera_header_join">
			<div class="hd_shadow bg_join">
				<!-- GNB -->
				<div class="gnb">
					<div class="top_logo" style="visibility: hidden; width: 360px">
					</div>
					<ul class="top_menu2">
						<li class="fl">
							<a href=""> <img width="101" height="28" src="sera/images/sera2_main_btnTopMenu1_off.png" /></a>
						</li>
						<li class="fl">
							<a href=""> <img width="101" height="28" src="sera/images/sera2_main_btnTopMenu2_off.png" /></a>
						</li>
						<li class="fl">
							<a href=""> <img width="101" height="28" src="sera/images/sera2_main_btnTopMenu3_off.png" /> </a>
						</li>
					</ul>
					<ul class="util_menu fr">
						<li class="about"><a href="">about SERA</a></li>
						<li class="news"><a href="">sera 소식</a></li>
						<li class="btn_login"><a href=""> <img width="49" height="19" title="로그인" alt="로그인" src="sera/images/sera2_main_btnLogout.png" /> </a></li>
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
						<a href="/"> <img width="201" height="36" alt="" src="sera/images/sera2_logo.png"></a>
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
								<textarea name="" cols="" rows="5" class="fieldline">
제 1조 (목적)
제 2조 (용어의 정의)
              					</textarea>
								<div class="mt5 fr">
									<input name="rdoUserAgreement" type="radio"/> <label>동의합니다</label>
									<input name="rdoUserAgreement" type="radio"/> 동의하지 않습니다
								</div></li>
						</ul>
					</div>
					<!-- Joint Content //-->
					<!-- Joint Content -->
					<div class="join_content_area">
						<ul>
							<li class="sub_label">세라캠퍼스<br /> 개인 정보 취급방침</li>
							<li>
								<textarea name="" cols="" rows="5" class="fieldline">
개인정보의 수집 및 이용 목적
수집하는 개인정보의 항목
개인정보 보유 및 이용기간
              					</textarea>
								<div class="mt5 fr">
									<input name="rdoPrivacyAgreement" type="radio"/> <label>동의합니다</label>
									<input name="rdoPrivacyAgreement" type="radio"/> 동의하지 않습니다
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
											<input name="" type="checkbox" value="" /> 외국인인 경우 체크
										</div>
										<div class="mt30">
											이름 <input name="" type="text" class="fieldline mr10"
												style="width: 100px" /> 주민등록번호 <input name="" type="text"
												class="fieldline" style="width: 100px" /> - <input name=""
												type="text" class="fieldline" style="width: 100px" />
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
						<div class="form_layout">
							<div class="t_refe mt10">* 선택입력란입니다</div>
							<table>
								<tr>
									<td>
										<div class="form_label">회원ID</div>
										<div class="form_value">
											<input type="text" class="fieldline fl" style="width: 150px">
												<div class="btn_mid_l ml5">
													<div class="btn_mid_r">중복확인</div>
												</div>
										</div>
									</td>
									<td rowspan="9" valign="top">
										<!-- 사진 올리기 -->
										<div class="my_photo fn m0">사진</div>
										<button class="styl_bl mt5" style="width: 118px; text-align: center">사진올리기</button>
										<div class="t_refe mt10">
											* 사진은 자동으로<br />77x77으로 변경됩니다
										</div> <!-- 사진 올리기 //--></td>
								</tr>
								<tr>
									<td>
										<div class="form_label">닉네임*</div>
										<div class="form_value">
											<input type="text" class="fieldline fl" style="width: 150px">
												<input name="" type="checkbox" value="" /> 사용
												<div class="t_refe mt10">
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
											<input type="text" class="fieldline fl" style="width: 100px" />
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div class="form_label">비밀번호 확인</div>
										<div class="form_value">
											<input type="text" class="fieldline" style="width: 100px" />
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div class="form_label">이메일</div>
										<div class="form_value">
											<input type="text" class="fieldline" style="width: 100px" />
											@ <input type="text" class="fieldline" style="width: 100px" />
											<div class="t_refe mt10">* 가입 완료 시 확인메일을 보내드립니다.</div>
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div class="form_label">생일/성별</div>
										<div class="form_value">
											<input name="" class="fieldline form_date_input" type="text" />
											년 <input name="" class="fieldline form_date_input"
												type="text" /> 월 <input name=""
												class="fieldline form_date_input" type="text" /> 일 <select>
												<option>여자</option>
												<option>남자</option>
											</select>
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div class="form_label">도전목표*</div>
										<div class="form_value">
											<input class=" fieldline" name="" type="text"
												style="width: 300px" />
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div class="form_label">관심분야</div>
										<div class="form_value">
											<div class="t_skyblueb fl">
												<span class="mr5">[예술]</span> <span class="mr5">[사회]</span>
											</div>
											<div class="btn_mid_l ml5">
												<div class="btn_mid_r">추가</div>
											</div>
											<div class="cb t_refe pt10">* 관심있는 키워드를 입력해 주세요.</div>
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div class="form_label">SNS연동 *</div>
										<div class="form_value">
											<div class="icon_facebook fl mr5"></div>
											<div class="icon_twitter fl mr5"></div>
											<input name="" type="checkbox" value="" /> 연동여부
										</div>
									</td>
								</tr>
							</table>
						</div>
					</div>
					<!-- Joint Content //-->
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
					<img width="176" height="146" alt=""
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
$(function() {
	$('.js_joinus_first_btn').live('click', function(e){
		var input = $(e.target);
		input.parents('.js_joinus_first').hide().next().show();
		return false;
	});
	$('.js_joinus_second_btn').live('click', function(e){
		var input = $(e.target);
		input.parents('.js_joinus_second').hide().next().show();
		return false;
	});
	$('.js_joinus_third_btn').live('click', function(e){
		var input = $(e.target);
		return false;
	});
});
</script>
