<%@page import="net.smartworks.model.work.FormField"%>
<%@page import="net.smartworks.model.work.SmartForm"%>
<%@page import="net.smartworks.model.filter.SearchFilter"%>
<%@page import="net.smartworks.model.community.User"%>
<%@page import="net.smartworks.model.security.EditPolicy"%>
<%@page import="net.smartworks.model.security.WritePolicy"%>
<%@page import="net.smartworks.model.security.AccessPolicy"%>
<%@page import="net.smartworks.model.work.InformationWork"%>
<%@page import="net.smartworks.model.work.SmartWork"%>
<%@page import="net.smartworks.model.work.Work"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="net.smartworks.service.ISmartWorks"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	String companyId = (String) session.getAttribute("companyId");
	String userId = (String) session.getAttribute("userId");

	ISmartWorks smartWorks = (ISmartWorks) request.getAttribute("smartWorks");
	String cid = request.getParameter("cid");
	String wid = request.getParameter("wid");

	String workId = SmartUtil.getSpaceIdFromContentContext(cid);
	User cUser = SmartUtil.getCurrentUser();
	InformationWork work = (InformationWork) smartWorks.getWorkById(companyId, workId);
%>
<fmt:setLocale value="<%=cUser.getLocale() %>" scope="request" />
<fmt:setBundle basename="resource.smartworksMessage" scope="request" />

<!-- 컨텐츠 레이아웃-->
<div class="section_portlet">
    <div class="portlet_t">
      <div class="portlet_tl"></div>
    </div>
    <div class="portlet_l" style="display: block;">
    <ul class="portlet_r" style="display: block;">
            
            <!-- 타이틀 -->
            	<div class="body_titl">
                	<div class="body_titl_pworks title">신제품 기획 TFT > TFT 회의록 </div> 
                    
            <!-- 우측 버튼-->
            <div class="txt_btn">
                <div class="po_right"><a href="">2011.11.14 16:00</a></div>
                <div class="po_right"><a href="">최종수정: jisook kim</a></div>
                <div class="po_right"><a href="">공개</a></div>
                <div class="po_right"><a href="">전체등록 가능</a></div>
            </div>
            <!-- 우측 버튼 -->
            
                	<div class="solid_line"></div>
                </div>
            <!-- 타이틀 -->
            
            
            
            
<!-- 정의 영역-->
<div class="contents_space">
            	
            <!-- 업무 정의 -->
            <div class="">
            근태품의는 근태신청 시 품의를 올릴 수 있는 공간입니다
            </div>
            <!-- 업무 정의 //-->
            
        	<!-- 버튼 영역--> 
            <div class="txt_btn posi_ab">
                <div class="po_left padding_r10"><a href="">업무설명보기▼</a></div>
                <div class="po_left"><a href="">프로세스 다이어그램보기▼</a></div>
            </div>
            
            <div class="txt_btn">
                
            <div class="float_right padding_l10">
       		<span class="btn_gray">
            <span class="Btn01Start"></span>
            <span class="Btn01Center">수정하기</span>
            <span class="Btn01End"></span>
            </span>
            </div>
            
            <div class="float_right">사용설명서 : <a href="">동영상 보기</a> <a href="">웹설명서 보기</a></div>
            
            </div>
			<!-- 라인 -->
			<div class="solid_line_s"> </div>
          <!-- 버튼 영역 //-->

</div>
<!-- 정의 영역-->

<!-- 프로세스 영역 -->
<div class="contents_space">
        <div class="proce_space">
        
        <!-- 시작 --> 
        <div class="proc_start_compl float_left padding_r10"> 시작 </div>
        <!-- 시작 //--> 
        
        <!--화살표-->
        <div class="proc_arr_next float_left padding_r10"></div>
        <!--화살표-->
        
        <!-- 태스크 --> 
        <div class="proc_task_compl float_left padding_r10">
        	<a href="">
            <span class="pstart"></span>
            <span class="pcenter">
            	<!-- task 정보 -->
            	<div class="float_left">
                <img align="bottom" src="../images/pic_size_29.jpg">
                </div>
                <div class="noti_in">
                <span>근태계 기안</span>
                <div class="t_date">2011.11.15 14:00</div>
                </div>
                <!-- task 정보 //-->
            </span>
            <span class="pend"></span>
            </a>
        </div>
        <!-- 태스크 //--> 
        
        <!--화살표-->
        <div class="proc_arr_next float_left padding_r10"></div>
        <!--화살표-->
        
        <!-- 태스크 --> 
        <div class="proc_task_yet float_left padding_r10">
        	<a href="">
            <span class="pstart"></span>
            <span class="pcenter">
            	<!-- task 정보 -->
            	<div class="float_left">
                <img align="bottom" src="../images/pic_size_29.jpg">
                </div>
                <div class="noti_in">
                <span>이력 기록</span>
                <div class="t_date">2011.11.15 14:00</div>
                </div>
                <!-- task 정보 //-->
            </span>
            <span class="pend"></span>
            </a>
        </div>
        <!-- 태스크 //--> 
        
        <!--화살표-->
        <div class="proc_arr_next float_left padding_r10"></div>
        <!--화살표-->
        
       <!-- 태스크 --> 
        <div class="proc_task_rturn float_left padding_r10">
        	<a href="">
            <span class="pstart"></span>
            <span class="pcenter">
            	<!-- task 정보 -->
            	<div class="float_left">
                <img align="bottom" src="../images/pic_size_29.jpg">
                </div>
                <div class="noti_in">
                <span>개인별 근태현황</span>
                <div class="t_date">2011.11.15 14:00</div>
                </div>
                <!-- task 정보 //-->
            </span>
            <span class="pend"></span>
            </a>
        </div>
        <!-- 태스크 //-->
        
        <!--화살표-->
        <div class="proc_arr_next float_left padding_r10"></div>
        <!--화살표-->
        
        <!-- 태스크 --> 
        <div class="proc_task_ing float_left padding_r10">
        	<a href="">
            <span class="pstart"></span>
            <span class="pcenter">
            	<!-- task 정보 -->
            	<div class="float_left">
                <img align="bottom" src="../images/pic_size_29.jpg">
                </div>
                <div class="noti_in">
                <span>승인자 결재</span>
                <div class="t_date">2011.11.15 14:00</div>
                </div>
                <!-- task 정보 //-->
            </span>
            <span class="pend"></span>
            </a>
        </div>
        <!-- 태스크 //-->
        
        <!--화살표-->
        <div class="proc_arr_next float_left padding_r10"></div>
        <!--화살표-->
        
        <!-- 태스크 --> 
        <div class="proc_task_dlay float_left padding_r10">
        	<a href="">
            <span class="pstart"></span>
            <span class="pcenter">
            	<!-- task 정보 -->
            	<div class="float_left">
                <img align="bottom" src="../images/pic_size_29.jpg">
                </div>
                <div class="noti_in">
                <span>승인자 결재</span>
                <div class="t_date">2011.11.15 14:00</div>
                </div>
                <!-- task 정보 //-->
            </span>
            <span class="pend"></span>
            </a>
        </div>
        <!-- 태스크 //-->        
        
        </div>
</div>
<!--프로세스 영역//-->

<!-- 업무설명 영역 -->
<div class="contents_space">
	<div class="up_point posit_first"></div>
        <div class="form_wrap up up_padding">
			<div class="area">
        
			<!-- 업무설명 -->
            <div class="det_contents">      
                        <table>
                            <colgroup>
                                <col width="50%">
                                <col width="50%">
                            </colgroup>
                        <tbody>
                            <tr>
                                <td><img src="../images/im_iworkscreen.gif" width="349" height="289" /></td>
                              <td>1. 업무설명이 들어갑니다<br />
                              1. 업무설명이 들어갑니다<br />
                              2. 업무설명이 들어갑니다<br />
                              3. 업무설명이 들어갑니다<br />
                              4. 업무설명이 들어갑니다<br />
                              5. 업무설명이 들어갑니다
                              </td>
                            </tr>
                        </tbody>
                        </table> 
              </div>
              
              <!-- 라인 -->
			<div class="solid_line_s"> </div>
			
            <!-- 업무 설명 //-->
           
   <!-- 댓글 -->
   <div class="replay_point posit_default"></div>
   <div class="replay_section">
   
        <div class="list_replay">
            <ul>
            <li class="repl_tinfo"><a href=""><strong>7</strong>개의 댓글 모두 보기</a></li>
            <li>
                    <div class="noti_pic"><img src="../images/pic_size_29.jpg" alt="신민아" align="bottom"/></div>
                    <div class="noti_in">
                        <span class="t_name">Minashin</span><span class="t_date"> 2011.10.13</span>
                        <div>회의록 내용 중 빠진 부분이나 수정할 사항이 있으시면 참석자 누구든 수정해주시기 바랍니다^^</div>
                </div>
            </li>
            <li>
                    <div class="noti_pic"><img src="../images/pic_size_29.jpg" alt="신민아" align="bottom"/></div>
                    <div class="noti_in">
                        <span class="t_name">Minashin</span><span class="t_date"> 2011.10.13</span>
                        <div>회의록 내용 중 빠진 부분이나 수정할 사항이 있으시면 참석자 누구든 수정해주시기 바랍니다^^</div>
                    </div>
            </li>
            <li>
                <div class="det_title">
                    <div class="noti_pic"><img src="../images/pic_size_29.jpg" alt="신민아" align="bottom"/></div>
                    <div class="noti_in">
                        <span class="t_name">Minashin</span><span class="t_date"> 2011.10.13</span>
                        <div>회의록 내용 중 빠진 부분이나 수정할 사항이 있으시면 참석자 누구든 수정해주시기 바랍니다^^</div>
                    </div>
                </div>
            </li>
            </ul>
        </div>
        
        <div class="replay_input">
        <textarea class="up_textarea" rows="5" cols="" name="txtaEventContent">궁금한 점을 올려주세요!</textarea>
        </div>
    
    </div>
    <!-- 댓글 //-->
    
    	</div>   
	</div>        
</div>
<!-- 업무설명 영역 //-->


<div class=" contents_space">    
    <!-- 목록보기 -->
    	<!-- 목록보기 타이틀-->
        <div class="list_title_space">
              
            <div class="txt_btn posi_ab">
                <div class="po_left title">목록보기</div>
                
                <div class="po_left">
                <div class="srch">
                    <input id="" type="text" value="채팅 가능한 사람" title="채팅 가능한 사람">
                    <button onclick="" title="검색"></button>
                    </div>
                </div>
                
                <div class="po_left">
                	<form id="" class="form_space" name="form">
                    <select name="">
                    <option value="3" selected="">모든항목</option>
                    <option value="1">항목1</option>
                    </select>
                    </form>
                </div>
                <div class="po_left">상세필터</div>
            </div>
            
             <div class="txt_btn">
                <div class="po_right"><a href="">목록보기</a></div>
                <div class="po_right"><a href="">엑셀 불러오기</a></div>
            </div>
            
        </div>
        <!-- 목록보기 타이틀-->
            
           
        <!-- 목록 테이블 -->   
        <div class="list_contents">      
                <table>
                    <colgroup>
                        <col class="item">
                        <col class="field">
                        <col class="field">
                    </colgroup>
                <tbody>
                    <tr>
                        <th class="r_line">등록자</th>
                        <th class="r_line">제 목</th>
                        <th>수정자/수정일</th>
                    </tr>
                    <tr>
                        <td><img src="../images/pic_size_m29.jpg" /></td>
                        <td>정보관리업무 제목 목록입니다</td>
                        <td>
                        <div class="noti_pic"><img src="../images/pic_size_29.jpg" alt="신민아" align="bottom"/></div>
                        <div class="noti_in">
                            <span class="t_name">Minashin</span>
                            <div class="t_date">2011.11.15 14:00</div>
                        </div>
                        </td>
                    </tr>
                    
                    <tr>
                        <td><img src="../images/pic_size_m29.jpg" /></td>
                        <td>정보관리업무 제목 목록입니다</td>
                        <td>
                        <div class="noti_pic"><img src="../images/pic_size_29.jpg" alt="신민아" align="bottom"/></div>
                        <div class="noti_in">
                            <span class="t_name">Minashin</span>
                            <div class="t_date">2011.11.15 14:00</div>
                        </div>
                        </td>
                    </tr>
                    
                    <tr>
                        <td><img src="../images/pic_size_m29.jpg" /></td>
                        <td>정보관리업무 제목 목록입니다</td>
                        <td>
                        <div class="noti_pic"><img src="../images/pic_size_29.jpg" alt="신민아" align="bottom"/></div>
                        <div class="noti_in">
                            <span class="t_name">Minashin</span>
                            <div class="t_date">2011.11.15 14:00</div>
                        </div>
                        </td>
                    </tr>
                    
                    <tr>
                        <td><img src="../images/pic_size_m29.jpg" /></td>
                        <td>정보관리업무 제목 목록입니다</td>
                        <td>
                        <div class="noti_pic"><img src="../images/pic_size_29.jpg" alt="신민아" align="bottom"/></div>
                        <div class="noti_in">
                            <span class="t_name">Minashin</span>
                            <div class="t_date">2011.11.15 14:00</div>
                        </div>
                        </td>
                    </tr>
                    
                    <tr>
                        <td><img src="../images/pic_size_m29.jpg" /></td>
                        <td>정보관리업무 제목 목록입니다</td>
                        <td>
                        <div class="noti_pic"><img src="../images/pic_size_29.jpg" alt="신민아" align="bottom"/></div>
                        <div class="noti_in">
                            <span class="t_name">Minashin</span>
                            <div class="t_date">2011.11.15 14:00</div>
                        </div>
                        </td>
                    </tr>
                    
                    <tr>
                        <td><img src="../images/pic_size_m29.jpg" /></td>
                        <td>정보관리업무 제목 목록입니다</td>
                        <td>
                        <div class="noti_pic"><img src="../images/pic_size_29.jpg" alt="신민아" align="bottom"/></div>
                        <div class="noti_in">
                            <span class="t_name">Minashin</span>
                            <div class="t_date">2011.11.15 14:00</div>
                        </div>
                        </td>
                    </tr>
                    
                    <tr>
                        <td><img src="../images/pic_size_m29.jpg" /></td>
                        <td>정보관리업무 제목 목록입니다</td>
                        <td>
                        <div class="noti_pic"><img src="../images/pic_size_29.jpg" alt="신민아" align="bottom"/></div>
                        <div class="noti_in">
                            <span class="t_name">Minashin</span>
                            <div class="t_date">2011.11.15 14:00</div>
                        </div>
                        </td>
                    </tr>
                </tbody>
                </table> 
        </div>
        <!-- 목록 테이블 //-->

	<!-- 목록보기 -->  
</div>   

<!-- 페이징 --> 
<div class="paginate">
	<a class="pre_end"><span class="spr"></span></a> 
	<a class="pre"><span class="spr"></span></a> 
	<strong>1</strong>
    <a class="num" href="">2</a>
    <a class="num" href="">3</a>
    <a class="num" href="">4</a>
    <a class="num" href="">5</a>
    <a class="num" href="">6</a>
    <a class="next"><span class="spr"></span></a> 
	<a class="next_end"><span class="spr"></span></a>
</div>

<div class="num_box">
    <select name="">
    <option selected="">15</option>
    <option>10</option>
    </select>

</div>
<!-- 페이징 //--> 

    
    
</ul>
</div>
<div class="portlet_b" style="display: block;"></div>
</div> 
<!-- 컨텐츠 레이아웃//-->
