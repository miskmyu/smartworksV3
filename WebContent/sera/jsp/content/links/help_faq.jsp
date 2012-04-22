<%@page import="net.smartworks.model.sera.Course"%>
<%@page import="net.smartworks.model.sera.info.CourseInfo"%>
<%@page import="net.smartworks.model.sera.CourseList"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
%>

<div id="course_list_section">
	<!-- SNB Left -->
	<div class="snb2">
		<ul class="snb_menu">
			<li><img height="112" width="149" src="sera/images/function_title_helpcenter.gif"></li>
			<li class=""><a href="helpCenter.sw">사용 설명서</a></li>
			<li class="selected"><a href="helpFAQ.sw">자주 묻는 질문</a></li>
			<li class=""><a href="helpQNA.sw">Q&A </a></li>
		</ul>
	</div>
	<!-- SNB Left//-->
	<!-- Content -->
	<div id="content_list_section">
		<!-- FAQ -->
		<div id="menu2" class="content_box" style="display: block;">
			<!-- FAQ Menu -->
			<!-- <ul class="faq_menu">
				<li class="menu_on"><a href="Faq">전체보기</a></li>
				<li class="line">|</li>
				<li class="menu_off"><a href="Faq?cIdx=1">멘토에 관한 질문</a></li>
				<li class="line">|</li>
				<li class="menu_off"><a href="Faq?cIdx=2">멘티에 관한 질문</a></li>
				<li class="line">|</li>
				<li class="menu_off"><a href="Faq?cIdx=3">My Page</a></li>
			</ul> -->
			<!-- FAQ Menu//-->
			<!-- FAQ List -->
			<ul class="faq_list">
				<li>
					<dl>
						<dt class="js_toggle_item_btn">
							<label class="faq_number">Q.1</label> 
							<label class="faq_name" style="cursor: pointer;">개인정보를 수정하려면 어디로 가서 변경해야 하나요?</label>
						</dt>
						<dd class="pnlFaqContents" style="display: none;">개인정보 수정은 마이페이지 프로필 옆에 계정설정으로 들어가면 개인정보 수정 및 비밀번호 변경, 회원탈퇴 등을 할 수 있습니다.</dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt class="js_toggle_item_btn">
							<label class="faq_number">Q.2</label> 
							<label class="faq_name" style="cursor: pointer;">Face Book과 Twitter는 어떻게 연동되나요?</label>
						</dt>
						<dd class="pnlFaqContents" style="display: none;">현재 마이페이지에 등록된 글은 연동되지 않고 코스페이지에 등록 된 글만 연동되어 보여집니다. 하지만 비공개 코스일 경우 페이스북과 트위터 연동이 되어 있어도 노출되지 않습니다.</dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt class="js_toggle_item_btn">
							<label class="faq_number">Q.3</label> <label class="faq_name" style="cursor: pointer;">멘티 초대나 친구요청은 어디서 해야하나요?</label>
						</dt>
						<dd class="pnlFaqContents" style="display: none;">멘티초대: 코스에서 친구 초대를 하는 경우 코스페이지에서 코스 프로필사진 아래에 친구 초대하기 버튼이 있습니다. 초대하기 버튼을 클릭하시면 SERA에 친구 등록이 되어있는 친구에게 코스로 초대할 수 있습니다. 
친구요청: 친구요청은 어떤 페이지에서든 사용자 이름 또는 닉네임을 클릭하시면 그 사용자 페이지로 이동하게 됩니다. 그 페이지 프로필사진 아래에 친구요청 버튼을 누르게 되면 상대방에게 친구요청이 전달됩니다. 
</dd>
					</dl></li>
				<li>
					<dl>
						<dt class="js_toggle_item_btn">
							<label class="faq_number">Q.4</label> <label class="faq_name" style="cursor: pointer;">친구 요청이 들어오면 거절하는 방법은 없나요?</label>
						</dt>
						<dd class="pnlFaqContents" style="display: none;">본인의 친구 메뉴 페이지로 가면, [새 친구 요청]에 대한 확인과 나중에 확인 중 선택할 수 있습니다. [나중에 확인]을 클릭하면 계속 수락 대기 상태로 있게 됩니다.</dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt class="js_toggle_item_btn">
							<label class="faq_number">Q.5</label> <label class="faq_name" style="cursor: pointer;">코스를 비공개로 설정하면 이미 친구인 사람들에게도 게시가 되지 않나요?</label>
						</dt>
						<dd class="pnlFaqContents" style="display: none;">비공개 코스는 SERA사이트 내에서 검색을 해도 보이지 않는 코스이므로 비공개 코스에서 진행되는 미션에 대한 글 및 코스에 등록한 글에 대해서는 친구에게 보여지지 않습니다. 또한 자신의 페이지를 상대방이 들어온 경우 비공개 코스에 참여하고 있는지에 대한 정보도 노출되지 않습니다.</dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt class="js_toggle_item_btn">
							<label class="faq_number">Q.6</label> <label class="faq_name" style="cursor: pointer;">코스를 비공개로 설정하고 몇몇의 친구들과 코스를 공유하려면 어떻게 해야 하나요?</label>
						</dt>
						<dd class="pnlFaqContents" style="display: none;">비공개 코스는 멘티 또는 멘토가 코스페이지에서 초대하기 메일을 보내야만 비공개 코스로 참여할 수 있습니다.</dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt class="js_toggle_item_btn">
							<label class="faq_number">Q.7</label> <label class="faq_name" style="cursor: pointer;">친구로 등록되어 있어도 지정하는 사람에게만 코스개설이 오픈될 수도 있나요?</label>
						</dt>
						<dd class="pnlFaqContents" style="display: none;">또는 비공개코스 모두 코스 개설을 할 때 멘토가 승인해서 멘티를 참여시킬 수 있는 기능이 있기 때문에, 이럴 경우에는 지정한 사람만 멘티로 등록할 수 있습니다.</dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt class="js_toggle_item_btn">
							<label class="faq_number">Q.8</label> <label class="faq_name" style="cursor: pointer;">친구의 코스에 댓글을 달면 코스가 공개일 경우 모든 사람들이 다 볼 수 있나요?</label>
						</dt>
						<dd class="pnlFaqContents" style="display: none;">친구의 코스에 댓글을 달기 위해서는 현재 그 코스에 참여해야지 댓글을 등록하실 수 있습니다. 그렇기 때문에 코스에 참여해서 댓글을 등록한 경우 코스에 등록되어 있는 모든 사람들과 나의 친구들은 모두 그 댓글을 확인할 수 있습니다.</dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt class="js_toggle_item_btn">
							<label class="faq_number">Q.9</label> <label class="faq_name" style="cursor: pointer;">코스만들기에서 코스 내용을 작성 한 후, 멘토 프로필 입력란이 있는데 반드시 작성해야 하나요?</label>
						</dt>
						<dd class="pnlFaqContents" style="display: none;">코스를 가입하고자 하는 예비 멘티들의 이해를 돕고자 작성하는 멘토에 대한 정보이므로, 필수사항은 반드시 작성해야 하며 선택란은 작성을 권유합니다. 해당사항이 없는 항목은 제외하셔도 됩니다.</dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt class="js_toggle_item_btn">
							<label class="faq_number">Q.10</label> <label class="faq_name" style="cursor: pointer;">코스기간이 설정되고 나서 멘티를 모집하는 기간을 따로 설정해야 하나요?</label>
						</dt>
						<dd class="pnlFaqContents" style="display: none;">모집 기간을 따로 설정하는 기능은 없습니다. 코스 기간설정을 하실 때 모집하는 기간일정도 생각하시고 입력하셔야 합니다.</dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt class="js_toggle_item_btn">
							<label class="faq_number">Q.11</label> <label class="faq_name" style="cursor: pointer;">코스 유/무료는 금액 제한사항이 있는 건가요?</label>
						</dt>
						<dd class="pnlFaqContents" style="display: none;">유료코스를 개설할 때 금액 제한은 없지만 유료코스 개설은 관리자의 승인이 통과 되어야 개설할 수 있는 코스입니다. 그렇기 때문에 관리자에게 승인이 되지 않은 경우 코스개설이 되지 않습니다.</dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt class="js_toggle_item_btn">
							<label class="faq_number">Q.12</label> <label class="faq_name" style="cursor: pointer;">코스 제목을 변경하고 싶으면 어떻게 해야 하나요?</label>
						</dt>
						<dd class="pnlFaqContents" style="display: none;">코스 제목은 변경할 수 없습니다. 코스 제목이 변경되면 다른 사용자에게 혼란을 줄 수 있어 제목 변경은 불가능 합니다.</dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt class="js_toggle_item_btn">
							<label class="faq_number">Q.13</label> <label class="faq_name" style="cursor: pointer;">전체 코스에서 제가 원하는 코스를 검색하려면 어디에서 해야 하나요?</label>
						</dt>
						<dd class="pnlFaqContents" style="display: none;">모든 페이지 상단 오른쪽에 보시면 검색 창이 있습니다. 검색창에 코스 이름 또는 개설자 이름을 검색하시면 코스 목록이 보여집니다.</dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt class="js_toggle_item_btn">
							<label class="faq_number">Q.14</label> 
							<label class="faq_name" style="cursor: pointer;">코스 개설 후 미션 등록은 어디에서 하나요? 그리고 이미 등록된 미션도 변경이 가능한가요?</label>
						</dt>
						<dd class="pnlFaqContents" style="display: none;">미션 등록은 코스설정 하위 메뉴에 미션 등록이 있습니다. 또한, 등록된 미션의 수정은 가능하며, 멘티들이 이미 미션을 수행한 경우에는 삭제는 불가능하고 미션내용에 대한 오타 수정은 가능합니다.</dd>
					</dl></li>
				<li>
					<dl>
						<dt class="js_toggle_item_btn">
							<label class="faq_number">Q.15</label> <label class="faq_name" style="cursor: pointer;">멘토가 코스에 미션을 등록할 때, 미리 예약해서 등록할 수 있나요?</label>
						</dt>
						<dd class="pnlFaqContents" style="display: none;">미션등록하기에서 미션 수행기간을 설정해 놓을 수 있습니다. 그리하면 멘티들은 미션 시작일부터 미션 내용이 보이게 됩니다.</dd>
					</dl></li>
				<li>
					<dl>
						<dt class="js_toggle_item_btn">
							<label class="faq_number">Q.16</label> <label class="faq_name" style="cursor: pointer;">코스내의 모든 미션을 확인할 수 있는 기능이 있나요?</label>
						</dt>
						<dd class="pnlFaqContents" style="display: none;">코스의 [미션] 메뉴를 보면 모든 미션 내용과 본인의 미션 수행 상황을 캘린더 형태로 확인할 수 있습니다. 아직 수행하지 않은 미션을 확인하여, 바로 클릭하고 미션을 수행하면 됩니다.</dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt class="js_toggle_item_btn">
							<label class="faq_number">Q.17</label> <label class="faq_name" style="cursor: pointer;">미션을 수행하고자 할 때는 어디에서 하나요?</label>
						</dt>
						<dd class="pnlFaqContents" style="display: none;">공개코스 또는
							[미션] 메뉴로 들어가면, 등록된 모든 미션을 캘린더에서 확인할 수 있습니다. 미션제목을 클릭하면, 미션수행하기 페이지로 이동되어 미션을 수행할 수 있습니다.</dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt class="js_toggle_item_btn">
							<label class="faq_number">Q.18</label> <label class="faq_name" style="cursor: pointer;">팀 구성을 어디에서 하나요? 또한, 코스에서 미션을 수행하는 것과 팀 활동의 차이점은 무엇인가요?</label>
						</dt>
						<dd class="pnlFaqContents" style="display: none;">팀을 구성할 수 있는 장소는 코스 내에서만 할 수 있습니다. 해당 코스의 멘토가 미션 중 하나로 팀을 구성하여 활동한 후 그 결과를 미션 과제로 부여할 수도 있습니다. 따라서 코스에서 미션을 수행하는 다른 형태의 방법이라고 할 수 있습니다.</dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt class="js_toggle_item_btn">
							<label class="faq_number">Q.19</label> <label class="faq_name" style="cursor: pointer;">팀원의 구성은 코스내의 친구들끼리만 가능한가요? 아니면 외부의 친구를 초대할 수 있나요?</label>
						</dt>
						<dd class="pnlFaqContents" style="display: none;">팀원은 해당 코스내의 친구들하고만 구성이 가능합니다.</dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt class="js_toggle_item_btn">
							<label class="faq_number">Q.20</label> <label class="faq_name" style="cursor: pointer;">코스의 멘토도 팀을 만들 수 있나요? 그리고 코스내의 모든 팀을 관리할 수 있는 권한이 있나요?</label>
						</dt>
						<dd class="pnlFaqContents" style="display: none;">물론 멘토도 팀을 만들 수 있습니다. 또한, 코스의 멘토는 코스 내의 모든 팀을 관리할 수 있는 권한이 부여되어 올바른 팀활동을 하는지에 대하여 모니터링할 수 있습니다.</dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt class="js_toggle_item_btn">
							<label class="faq_number">Q.21</label> <label class="faq_name" style="cursor: pointer;">이벤트 만들기는 누가 할 수 있나요? 또한 특정 코스에 해당되는 이벤트는 어떻게 만드나요?</label>
						</dt>
						<dd class="pnlFaqContents" style="display: none;">이벤트는 누구나 만들 수 있습니다. 친구들과의 특별 미션 수행을 위해서 혹은 코스 친구들에게 특정한 날에 오프라인 모임을 개최하고 싶은 경우 등 다양하게 활용할 수 있습니다.</dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt class="js_toggle_item_btn">
							<label class="faq_number">Q.22</label> <label class="faq_name" style="cursor: pointer;">특정인에게 글을 따로 보낼 수 있는 기능이 있나요?</label>
						</dt>
						<dd class="pnlFaqContents" style="display: none;">특정인에게만 글을 보내고 싶다면, 쪽지기능을 이용하면 그 특정인만 받을 수 있고 다른 사람들은 볼 수 없습니다.</dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt class="js_toggle_item_btn">
							<label class="faq_number">Q.23</label> <label class="faq_name" style="cursor: pointer;">내가 보는 나의 마이페이지 하단에 올라오는 '전체보기'에는 어떤 게시물들이 보여지나요?</label>
						</dt>
						<dd class="pnlFaqContents" style="display: none;">자신이 쓴 모든 글과, 공개된 친구들의 글, 코스와 관련된 게시물, 담벼락에 쓴 글 등이 최신순으로 보입니다. </dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt class="js_toggle_item_btn">
							<label class="faq_number">Q.24</label> <label class="faq_name" style="cursor: pointer;">친구들이 보는 나의 마이페이지 하단에는 어떤 게시물들이 보여지나요?</label>
						</dt>
						<dd class="pnlFaqContents" style="display: none;">나와 친구들의 친구공개로 설정된 모든 글, 전체공개로 설정된 모든 글 등이 최신순으로 보입니다.</dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt class="js_toggle_item_btn">
							<label class="faq_number">Q.25</label> <label class="faq_name" style="cursor: pointer;">친구가 아닌 다른 사람이 보는 나의 마이페이지 하단에는 어떤 게시물들이 보여지나요?</label>
						</dt>
						<dd class="pnlFaqContents" style="display: none;">나와 친구들의 전체공개로 설정된 모든 글 등이 최신순으로 보입니다.</dd>
					</dl>
				</li>
			</ul>
			<!-- FAQ List //-->
		</div>
		<!-- FAQ //-->
	</div>
	<!-- Content //-->
</div>
<script type="text/javascript">

$(function() {
	$('.js_toggle_item_btn').live('click', function(e) {
		var input = $(e.target).parents('.js_toggle_item_btn');
		input.next().toggle();
		return false;
	});
});

</script>
