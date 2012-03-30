<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
	String cid = request.getParameter("cid");
	if (SmartUtil.isBlankObject(cid))
		cid = ISmartWorks.CONTEXT_PREFIX_HOME + cUser.getId();
	String wid = request.getParameter("wid");
	if (SmartUtil.isBlankObject(wid))
		wid = cUser.getId();
%>
<!-- Nav SNB -->
<div id="nav_snb">
	<div class="this_month">
		<div class="tit_area">
			<a href="" class="b_prev fl"></a> <span class="tit">2012.03</span> <a
				href="" class="b_next fl"></a>
		</div>
		<div class="cb">
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<th width="36px">날짜</th>
					<th width="146px">내용</th>
				</tr>
				<tr>
					<td>01(월)</td>
					<td>[이벤트] 이벤트명</td>
				</tr>
				<!-- 오늘날짜에 하늘색배경색이 들어간 today_cel 클래스를 씁니다 -->
				<tr class="today_cel">
					<td>02(화)</td>
					<td><span class="icon_mission"></span>미션4 미션제목은최대7</td>
				</tr>
				<tr>
					<td>03(수)</td>
					<td><span class="icon_mission current"></span>미션5 미션제목은최대7</td>
				</tr>
				<tr>
					<td>04(목)</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>05(금)</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>06(토)</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td class="t_red">07(일)</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>08(월)</td>
					<td><span class="icon_reserve"></span>미션6 미션제목은예약7</td>
				</tr>
				<tr>
					<td>09(화)</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>10(수)</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>11(목)</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>12(금)</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>13(토)</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td class="t_red">14(일)</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>15(월)</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>16(화)</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>17(수)</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>18(목)</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>19(금)</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>20(토)</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td class="t_red">21(일)</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>22(월)</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>23(화)</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>24(수)</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>25(목)</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>26(금)</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>27(토)</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td class="t_red">28(일)</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>29(월)</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>30(화)</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>31(수)</td>
					<td>&nbsp;</td>
				</tr>
			</table>
		</div>
	</div>
</div>
<!-- Nav SNB //-->
<!-- Navi Indication-->
<div class="icon_Indication" style="top: 215px">미션마감일을 선택한 후, 미션을
	등록할수 있습니다 = 미션 마감일 선택한 위치</div>
<!-- Navi Indication//-->
<!-- Section Center -->
<div id="section_cen">

	<!-- Tab -->
	<div id="" class="tab">
		<ul class="clear">
			<li class="current"><span> <a href="#">등 록</a> </span>
			</li>
			<li><span> <a href="#">수 행</a> </span>
			</li>
			<li><span> <a href="#">등록미션보기</a> </span>
			</li>
		</ul>
	</div>
	<!-- Tab //-->

	<!-- Input Form -->
	<div class="t_grayb mb10">
		* 미션을 등록하기 위해서는 달력에서 <span class="t_redb">미션 마감일</span>에 클릭하여 등록하여
		주십시오. <br /> * 등록된 미션을 수정하기 위해서는 이미 등록된 미션을 클릭하여 수정하십시오
	</div>

	<div class="form_layout">

		<table class="bgn">
			<tr>
				<td class="title">미션 등록하기</td>
			</tr>
			<tr>
				<td><div class="form_label">미션6 제목</div>
					<div class="form_value">
						<input type="text" class="fieldline" style="width: 300px" />
						<div class="fr ml5">
							<span class="t_red">0</span> /150kbyte
						</div>
					</div></td>
			</tr>
			<tr>
				<td><div class="form_label">미션등록예약*</div>
					<div class="form_value">
						<input name="" class="fieldline form_date_input" type="text" /> <span
							class="icon_fb_date psr"></span>
					</div>
					<div class="cb pt10">
						<div class="t_refe">* 선택사항 : 미션을 예약 등록하고자 할 경우에는, 미션 시작 날짜를
							지정해 주세요.</div>
					</div>
				</td>
			</tr>
			<tr>
				<td><div class="form_label">선행미션*</div>
					<div class="form_value">
						<select>
							<option>없 음</option>
							<option>[미션1]미션제목 미션제목</option>
							<option>[미션2]미션제목 미션제목</option>
							<option>[미션3]미션제목 미션제목</option>
							<option>[미션4]미션제목 미션제목</option>
						</select>
					</div>
					<div class="cb pt10">
						<div class="t_refe">* 선택사항 : 선행미션을 선택하면, 선택한 미션을 수행해야 해당 미션을
							수행할 수 있습니다.</div>
					</div>
				</td>
			</tr>
			<tr>
				<td><textarea name="" cols="" rows="6">미션내용을 등록하여 주십시오.</textarea>
				</td>
			</tr>
			<tr>
				<td>
					<div class="cb" style="margin: -8px 0 0 0">
						<!-- 좌측 영역 -->
						<div class="option">
							<!-- select -->
							<div class="txt">
								<a href=""> 전체공개<span class="icon_bul_select ml5"></span> </a> |
							</div>
							<!-- select //-->
							<!-- 태그넣기 -->
							<div class="txt">
								<a href=""> 태그넣기<span class="icon_bul_select ml5"></span> </a>
							</div>
							<!-- 태그넣기//-->
						</div>
						<!-- 좌측 영역//-->
						<!-- 우측 버튼 영역 -->
						<div class="attach_file">
							<ul>
								<li class="t_s11"><span class="t_red">0</span> /1000kbyte</li>
								<li class="icon_memo ml10"><a href=""> </a></li>
								<li class="icon_video"><a href=""> </a></li>
								<li class="icon_photo"><a href=""> </a></li>
								<li class="icon_link"><a href=""> </a></li>
								<!-- Btn 등록-->
								<li class="btn_default_l ml5">
									<div class="btn_default_r">등록</div>
								</li>
								<!-- Btn 등록//-->
							</ul>
						</div>
						<!-- 우측 버튼 영역 //-->
					</div>
				</td>
			</tr>
		</table>
	</div>
	<!-- Input Form //-->
</div>
<!-- Section Center //-->
