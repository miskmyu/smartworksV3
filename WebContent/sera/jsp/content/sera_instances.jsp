<%@page import="net.smartworks.model.instance.info.AsyncMessageInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.YTVideoInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.EventInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.BoardInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.CommentInstanceInfo"%>
<%@page import="net.smartworks.model.instance.WorkInstance"%>
<%@page import="net.smartworks.model.instance.info.WorkInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.MemoInstanceInfo"%>
<%@page import="net.smartworks.model.instance.info.ImageInstanceInfo"%>
<%@page import="net.smartworks.model.instance.Instance"%>
<%@page import="net.smartworks.model.instance.info.InstanceInfo"%>
<%@page import="net.smartworks.util.LocalDate"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.service.ISmartWorks"%>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	// 스마트웍스 서비스들을 사용하기위한 핸들러를 가져온다. 그리고 현재사용자 정보도 가져온다.	
	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	User cUser = SmartUtil.getCurrentUser();
	
	String userId = request.getParameter("userId");
	String courseId = request.getParameter("courseId");
	String missionId = request.getParameter("missionId");
	
	InstanceInfo[] seraInstances = smartWorks.getSeraInstances(userId, courseId, missionId,  new LocalDate(), 10);

	if(!SmartUtil.isBlankObject(seraInstances)){
		for(int i=0; i<seraInstances.length; i++){	
			InstanceInfo seraInstance = seraInstances[i];
%>
			<div>
				<ul class="panel_area">
					<!-- photo-->
					<li class="">
						<div class="photo_bg">
							<img class="sz_s_72" src="<%=seraInstance.getOwner().getMidPicture() %>" />
							<div class="rgt_name"><%=seraInstance.getOwner().getNickName() %></div>
						</div>
						<div class="grade">
							<div class="icon_mentor current"></div>
							<div class="icon_star"></div>
							<div class="icon_heart"></div>
						</div>
					</li>
					<!-- photo//-->
					<!-- comment -->
					<li class="fr">
						<div class="point"></div>
						<div class="panel_block fr">
							<dl class="content">
								<%
								switch(seraInstance.getType()){
								case Instance.TYPE_BOARD:
									BoardInstanceInfo board = (BoardInstanceInfo)seraInstance;
								%>
									<dt class="name">
										코스명 <span class="t_redb">[미션1. 자화상 그리기]</span> <span
											class="icon_delete fr"><a href="">삭제</a> </span>
									</dt>
									<dd>
										<div class="text"><%=board.getBriefContent() %></div>
									</dd>
								<%
									break;
								case Instance.TYPE_EVENT:
									EventInstanceInfo event = (EventInstanceInfo)seraInstance;
								%>
									<dt class="name">
										코스명 <span class="t_redb">[미션1. 자화상 그리기]</span> <span
											class="icon_delete fr"><a href="">삭제</a> </span>
									</dt>
									<dd>
										<div class="text"><%=event.getContent() %></div>
									</dd>
								<%
									break;
								case Instance.TYPE_IMAGE:
									ImageInstanceInfo image = (ImageInstanceInfo)seraInstance;
								%>
									<dt class="name">
										코스명 <span class="t_redb">[미션1. 자화상 그리기]</span> <span
											class="icon_delete fr"><a href="">삭제</a> </span>
									</dt>
									<dd>
										<div class="text"><%=image.getContent() %></div>
										<!-- Thum Image-->
										<div class="thum_image">
											<img src="<%=image.getImgSource() %>" />
										</div>
										<!-- Thum Image//-->
									</dd>
								<%
									break;
								case Instance.TYPE_YTVIDEO:
									YTVideoInstanceInfo video = (YTVideoInstanceInfo)seraInstance;
								%>
									<dt class="name">
										코스명 <span class="t_redb">[미션1. 자화상 그리기]</span> <span
											class="icon_delete fr"><a href="">삭제</a> </span>
									</dt>
									<dd>
										<div class="text"><%=video.getContent() %></div>
										<!-- Thum Image-->
										<div class="thum_image">
											<object class="thum_image">
												<param name="movie"
													value="https://www.youtube.com/v/<%=video.getId() %>?version=3&autohide=1&showinfo=0"></param>
												<param name="allowScriptAccess" value="always"></param>
												<param name="allowFullScreen" value="true"></param>
												<embed
													src="https://www.youtube.com/v/<%=video.getId() %>?version=3&autohide=1&showinfo=0"
													type="application/x-shockwave-flash" allowscriptaccess="always" allowfullscreen="true"
													class="thum_image"></embed>
											</object>
										</div>
										<!-- Thum Image//-->
									</dd>
								<%
									break;
								case Instance.TYPE_MEMO:
									MemoInstanceInfo memo = (MemoInstanceInfo)seraInstance;
								%>
									<dt class="name">
										코스명 <span class="t_redb">[미션1. 자화상 그리기]</span> <span
											class="icon_delete fr"><a href="">삭제</a> </span>
									</dt>
									<dd>
										<div class="text"><%=memo.getContent() %></div>
									</dd>
								<%
									break;
								case Instance.TYPE_ASYNC_MESSAGE:
									AsyncMessageInstanceInfo message = (AsyncMessageInstanceInfo)seraInstance;
								%>
									<dt class="icon_sm_notes"> 쪽지 <span class="icon_delete fr"><a href="">삭제</a> </span></dt>
									<dd>
										<div class="notes"><%=message.getMessage() %></div>
									</dd>
								<%
									break;
								}
								%>
								
								<!-- Util -->
								<dd class="util">
									<span><a href="">댓글달기</a> | </span> <span><a href="">공감하기</a>
										| </span> <span class=""><a href="">더보기</a> | </span> <span
										class="date"><%=seraInstance.getLastModifiedDate().toLocalDateLongString() %></span>
								</dd>
								<!-- Util //-->
							</dl>
							
							<%
							if(seraInstance.getType()!=Instance.TYPE_ASYNC_MESSAGE){
								WorkInstanceInfo workInstance = (WorkInstanceInfo)seraInstance;
								if(workInstance.getSubInstanceCount()>WorkInstance.DEFAULT_SUB_INSTANCE_FETCH_COUNT){
								%>
									<div class="stat_notice"><%=workInstance.getSubInstanceCount() %>개의 댓글이 모두보기</div>
								<%
								}
								if(workInstance.getSubInstanceCount()>0){
									CommentInstanceInfo[] comments = (CommentInstanceInfo[])workInstance.getSubInstances();
									for(int j=0; j<comments.length; j++){
										CommentInstanceInfo comment = comments[j];
								%>
									<!-- Reply-->
									<div class="reply_section">
										<div class="photo">
											<img src="<%=comment.getOwner().getMinPicture() %>" />
										</div>
										<div class="reply_text">
											<span class="name"><%=comment.getOwner().getNickName() %> : </span><%=comment.getComment() %><div class="icon_date"><%=comment.getLastModifiedDate().toLocalString() %></div>
										</div>
									</div>
									<!-- Reply//-->
							<%
									}
								}
							}
							%>
						</div>
					</li>
					<!-- comment //-->
				</ul>
			</div>
			<!-- Panel1 //-->
	<%
		}
	}
	%>
	<%
	if(!SmartUtil.isBlankObject(seraInstances) && seraInstances.length>10){
	%>
		<!-- 더보기 -->
		<div class="more">
			<div class="icon_more">더보기</div>
		</div>
		<!-- 더보기 //-->
	<%
	}
	%>
