<%@page import="net.smartworks.util.SmartTest"%>
<%@page import="net.smartworks.model.work.info.WorkInfo"%>
<%@page import="net.smartworks.model.work.info.SmartWorkInfo"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="net.smartworks.model.community.info.InstanceSpaceInfo"%>
<%@page import="net.smartworks.model.sera.Course"%>
<%@page import="net.smartworks.model.sera.MissionInstance"%>
<%@page import="net.smartworks.model.sera.info.MissionInstanceInfo"%>
<%@page import="net.smartworks.model.sera.info.CourseInfo"%>
<%@page import="net.smartworks.model.community.info.WorkSpaceInfo"%>
<%@page import="net.smartworks.model.sera.info.MissionReportInstanceInfo"%>
<%@page import="net.smartworks.model.sera.info.NoteInstanceInfo"%>
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
	
	String typeStr = request.getParameter("instanceType");
	int instanceType = (SmartUtil.isBlankObject(typeStr)) ? -1 : Integer.parseInt(typeStr); 
	String userId = request.getParameter("userId");
	if(userId!=null && userId.equals("null")) userId = null;
	String courseId = request.getParameter("courseId");
	if(courseId!=null && courseId.equals("null")) courseId = null;
	String missionId = request.getParameter("missionId");
	if(missionId!=null && missionId.equals("null")) missionId = null;
	
	InstanceInfo[] seraInstances = smartWorks.getSeraInstances(instanceType, userId, courseId, missionId,  new LocalDate(), 10);

	if(!SmartUtil.isBlankObject(seraInstances)){
		for(int i=0; i<seraInstances.length; i++){	
			InstanceInfo seraInstance = seraInstances[i];
			WorkSpaceInfo workSpace = seraInstance.getWorkSpace();
			CourseInfo course = null;
			MissionInstanceInfo mission = null;
			if(!SmartUtil.isBlankObject(workSpace)){
				if(workSpace.getClass().equals(CourseInfo.class)){
					course = (CourseInfo)workSpace;
				}else if(workSpace.getClass().equals(InstanceSpaceInfo.class)){
					mission = (MissionInstanceInfo)((InstanceSpaceInfo)workSpace).getInstance();
					if(!SmartUtil.isBlankObject(mission))
						course = SmartUtil.isBlankObject(mission.getWorkSpace()) ? null : (CourseInfo)mission.getWorkSpace();
				}
			}
			WorkInstanceInfo workInstance = (WorkInstanceInfo)seraInstance;
			WorkInfo work = workInstance.getWork();
			int workType = (SmartUtil.isBlankObject(work)) ? -1 : work.getType();
%>
			<div>
				<ul class="panel_area">
					<!-- photo-->
					<li>
						<a <%if(!seraInstance.getOwner().getId().equals(cUser.getId())){ %>href="othersPAGE.sw?userId=<%=seraInstance.getOwner().getId()%>" <%} %>>
							<div class="photo_bg">
								<img class="profile_size_72" src="<%=seraInstance.getOwner().getMidPicture() %>" />
								<div class="rgt_name"><%=seraInstance.getOwner().getNickName() %></div>
							</div>
							<div class="grade">
								<div class="icon_mentor current"></div>
								<div class="icon_star"></div>
								<div class="icon_heart"></div>
							</div>
						</a>
					</li>
					<!-- photo//-->
					<!-- comment -->
					<li class="fr js_sub_instance_list" instanceId="<%=workInstance.getId() %>" workType="<%=workType%>"">
						<div class="point"></div>
						<div class="panel_block fr">
							<dl class="content">
								<%
								if(seraInstance.getClass().equals(EventInstanceInfo.class))
									seraInstance.setType(Instance.TYPE_EVENT);
								else if(seraInstance.getClass().equals(BoardInstanceInfo.class))
									seraInstance.setType(Instance.TYPE_BOARD);
								else if(seraInstance.getClass().equals(NoteInstanceInfo.class))
									seraInstance.setType(Instance.TYPE_SERA_NOTE);
								else if(seraInstance.getClass().equals(MissionReportInstanceInfo.class))
									seraInstance.setType(Instance.TYPE_SERA_MISSION_REPORT);
								switch(seraInstance.getType()){
								case Instance.TYPE_BOARD:
									BoardInstanceInfo board = (BoardInstanceInfo)seraInstance;
									if(!SmartUtil.isBlankObject(course)){
								%>								
										<dt class="name">
											[코스. <span class=""><%=course.getName() %></span>] <span><%if(!SmartUtil.isBlankObject(mission)){ %>[미션<%=mission.getIndex()+1 %>. <%=mission.getSubject() %>]<%} %></span> <span
												class="icon_delete fr"><a href="">삭제</a> </span>
										</dt>
									<%
									}
									%>
									<dd>
										<div class="text"><%=board.getSubject() %></div>
										<div class="text"><%=board.getBriefContent() %></div>
									</dd>
								<%
									break;
								case Instance.TYPE_EVENT:
									EventInstanceInfo event = (EventInstanceInfo)seraInstance;
									if(!SmartUtil.isBlankObject(course)){
								%>								
										<dt class="name">
											[코스. <span class=""><%=course.getName() %></span>] <span><%if(!SmartUtil.isBlankObject(mission)){ %>[미션<%=mission.getIndex()+1 %>. <%=mission.getSubject() %>]<%} %></span> <span
												class="icon_delete fr"><a href="">삭제</a> </span>
										</dt>
									<%
									}
									%>
									<dd>
										<div class="text">
											<div class="name">[이벤트]<%=event.getSubject() %></div>
											<div class="event_more_info">
												<span>이벤트 기간 : <%=event.getStart().toLocalString() %></span>
												<%if(!SmartUtil.isBlankObject(event.getEnd())){ %>
												<span>~<%=event.getEnd().toLocalDateLongString() %></span>
												<div>이벤트 장소 : </div>
											</div><%} %>
										</div>
										<div class="text"><%=event.getContent() %></div>
									</dd>
								<%
									break;
								case Instance.TYPE_SERA_NOTE:
									NoteInstanceInfo seraNote = (NoteInstanceInfo)seraInstance;
									if(!SmartUtil.isBlankObject(course)){
								%>								
										<dt class="name">
											[코스. <span class=""><%=course.getName() %></span>] <span><%if(!SmartUtil.isBlankObject(mission)){ %>[미션<%=mission.getIndex()+1 %>. <%=mission.getSubject() %>]<%} %></span> <span
												class="icon_delete fr"><a href="">삭제</a> </span>
										</dt>
									<%
									}
									%>
									<dd>
										<div class="text"><%=seraNote.getContent() %></div>
										<!-- URL information -->
										<%
										if(!SmartUtil.isBlankObject(seraNote.getLinkUrl())){
										%>
											<div class="cb icon_link_s t_link"><a target="_blank" href="<%=seraNote.getLinkUrl()%>"><%=seraNote.getLinkUrl() %></a></div>
										<%
										} 
										%>
										<!-- URL information//-->
										
										<!-- File information -->
										<%
										if(!SmartUtil.isBlankObject(seraNote.getFileList())){
										%>
											<div class="cb fl"><%=SmartUtil.getFilesDetailInfo(seraNote.getFileList()) %></div>
										<%
										} 
										%>
										<!-- File information//-->
										
										<!-- Thum Image-->
										<%
										if(!SmartUtil.isBlankObject(seraNote.getImageSrc())){ 
										%>
											<div class="thum_image">
												<img class="thum_image_size" src="<%=seraNote.getImageSrc() %>" />
											</div>
										<%
										} 
										%>
										<!-- Thum Image//-->
										
										<%
										if(!SmartUtil.isBlankObject(seraNote.getVideoId())){
										%>
											<div class="cb thum_image mt3">
												<object class="thum_image_size">
													<param name="movie"
														value="https://www.youtube.com/v/<%=seraNote.getVideoId() %>?version=3&autohide=1&showinfo=0"></param>
													<param name="allowScriptAccess" value="always"></param>
													<param name="allowFullScreen" value="true"></param>
													<embed
														src="https://www.youtube.com/v/<%=seraNote.getVideoId() %>?version=3&autohide=1&showinfo=0"
														type="application/x-shockwave-flash" allowscriptaccess="always" allowfullscreen="true"
														class="thum_image_size"></embed>
												</object>
											</div>
										<%
										}
										%>
									</dd>
								<%
									break;
								case Instance.TYPE_SERA_MISSION_REPORT:
									MissionReportInstanceInfo seraReport = (MissionReportInstanceInfo)seraInstance;
									if(!SmartUtil.isBlankObject(course)){
								%>								
										<dt class="name">
											[코스. <span class=""><%=course.getName() %></span>] <span><%if(!SmartUtil.isBlankObject(mission)){ %>[미션<%=mission.getIndex()+1 %>. <%=mission.getSubject() %>]<%} %></span> <span
												class="icon_delete fr"><a href="">삭제</a> </span>
										</dt>
									<%
									}
									%>
									<dd>
										<div class="text"><%=seraReport.getContent() %></div>
										<!-- URL information -->
										<%
										if(!SmartUtil.isBlankObject(seraReport.getLinkUrl())){
										%>
											<div class="cb icon_link_s t_link"><a target="_blank" href="<%=seraReport.getLinkUrl()%>"><%=seraReport.getLinkUrl() %></a></div>
										<%
										} 
										%>
										<!-- URL information//-->
										
										<!-- File information -->
										<%
										if(!SmartUtil.isBlankObject(seraReport.getFileList())){
										%>
											<div class="db fl"><%=SmartUtil.getFilesDetailInfo(seraReport.getFileList()) %></div>
										<%
										} 
										%>
										<!-- File information//-->
										
										<!-- Thum Image-->
										<%
										if(!SmartUtil.isBlankObject(seraReport.getImageSrc())){ 
										%>
											<div class="thum_image">
												<img class="thum_image_size" src="<%=seraReport.getImageSrc() %>" />
											</div>
										<%
										} 
										%>
										<!-- Thum Image//-->
										
										<%
										if(!SmartUtil.isBlankObject(seraReport.getVideoId())){
										%>
											<div class="thum_image">
												<object class="thum_image_size">
													<param name="movie"
														value="https://www.youtube.com/v/<%=seraReport.getVideoId() %>?version=3&autohide=1&showinfo=0"></param>
													<param name="allowScriptAccess" value="always"></param>
													<param name="allowFullScreen" value="true"></param>
													<embed
														src="https://www.youtube.com/v/<%=seraReport.getVideoId() %>?version=3&autohide=1&showinfo=0"
														type="application/x-shockwave-flash" allowscriptaccess="always" allowfullscreen="true"
														class="thum_image_size"></embed>
												</object>
											</div>
										<%
										}
										%>
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
								<dd class="util js_action_btns">
									<span><a href="" class="js_add_sera_comment" >댓글달기</a> | </span>
									<span><a href="" class="js_add_sera_like" >공감하기</a> | </span>
									<span><a href="" class="js_show_more_content">더보기</a> | </span> 
									<span class="date"><%=seraInstance.getLastModifiedDate().toLocalDateLongString() %></span>
								</dd>
								<!-- Util //-->
							</dl>
							
							
							<!-- Reply -->
							<div class="js_comment_list">
								<div class="reply_section js_comment_instance" style="display:none">
									<div class="photo">
										<img src="<%=cUser.getMinPicture() %>"  class="profile_size_m"/>
									</div>
									<div class="reply_text">
										<span class="name"><%=cUser.getNickName() %> : </span><div class="js_comment_content"></div><div class="icon_date"><%=(new LocalDate()).toLocalString() %></div>
									</div>
								</div>
								<%
								if(seraInstance.getType()!=Instance.TYPE_ASYNC_MESSAGE){
									if(workInstance.getSubInstanceCount()>WorkInstance.DEFAULT_SUB_INSTANCE_FETCH_COUNT){
									%>
									<div class="stat_notice">
										<ul>
											<li>
												<span class="icon_like"></span><span class="t_blue">신현성</span>님이 좋아합니다
											</li>
											<li>
												<a href="comments_in_instance.sw?instanceId=<%=workInstance.getId()%>&fetchCount=<%=WorkInstance.FETCH_ALL_SUB_INSTANCE %>" class="js_show_all_sera_comments">
													<span class="icon_reply"></span>											
													<span class="t_blue"><%=workInstance.getSubInstanceCount() %></span>개의 댓글 모두보기
							            		</a>
						            		</li>
					            		</ul>
					            		</div>
									<%
									}
									if(workInstance.getSubInstanceCount()>0){
										CommentInstanceInfo[] comments = (CommentInstanceInfo[])workInstance.getSubInstances();
										//comments = SmartTest.getCommentInstances();
										for(int j=0; j<comments.length; j++){
											CommentInstanceInfo comment = comments[j];
									%>
											<!-- Reply-->
											<div class="reply_section">
												<a <%if(!comment.getOwner().getId().equals(cUser.getId())){ %>href="othersPAGE.sw?userId=<%=comment.getOwner().getId()%>" <%} %>>
													<div class="photo">
														<img src="<%=comment.getOwner().getMinPicture() %>"  class="profile_size_m"/>
													</div>
												</a>
												<div class="reply_text">
													<span class="name"><%=comment.getOwner().getNickName() %> : </span><div><%=comment.getComment() %></div><div class="icon_date"><%=comment.getLastModifiedDate().toLocalString() %></div>
												</div>
											</div>
											<!-- Reply//-->
								<%
										}
									}
								}
								%>
							</div>
					        <div class="reply_section js_return_on_sera_comment" style="display:none">
								<div class="photo">
									<img src="<%=cUser.getMinPicture()%>" class="profile_size_m"/>
								</div>
								<div class="reply_text">
									<textarea style="width:95%" class="up_textarea" name="txtaCommentContent" placeholder="댓글을 남겨주세요!"></textarea>
								</div>
					        </div>
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
