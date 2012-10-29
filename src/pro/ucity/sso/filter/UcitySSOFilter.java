/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 10. 22.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package pro.ucity.sso.filter;

import ifez.framework.session.SessionVO;
import ifez.framework.session.UserProgramAuthInfoVO;
import ifez.framework.session.service.UserSessionVO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import pro.ucity.model.UcityConstant;

import net.smartworks.server.engine.factory.SwManagerFactory;

public class UcitySSOFilter implements Filter {
    
	private FilterConfig fc; 
	private List<String> passUrls = new ArrayList<String>();

/*	@Override
	public void init(FilterConfig config) throws ServletException {
        this.fc = config;
        //this.passUrls.add("");//SSO를 거치지 않을 URL
	}*/

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		System.out.println("####################   SSO FILTER START!   ########################");
		
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		
		boolean result = false;
		
		String url = request.getRequestURI();   // 호출  URL
		String addr = request.getRemoteAddr();  // 클라이언트 IP
		
		HttpSession session = request.getSession(false);  // 미리 생성된 Session 이 없으면  null 리턴
		ArrayList<UserProgramAuthInfoVO> authProgramList = null;  // 프로그램 권한 리스트
		
		System.out.println("호출 url = " + url);    // /cimb/wti/retrieveWeatherInfoList.do
		System.out.println("Referer IP = " + addr);    // 10.2.10.20
		
		// 직접 호출하였거나, eco 플랫폼 운영 포털을 통해 호출하지 않은 경우, 또는 예외 신청 URL 로 호출한 경우
//		if ( !urlRefererCheck(request)) {
//			
//			System.out.println("U-Service URL 직접 호출");
//			System.out.println("Main Portal을 통한 호출만 가능합니다.");
//			request.setAttribute("errorCode", "100"); 
//			throw new CommonException(commonMessageSource.getMessage("biz.error.com.001"));  // 잘못된 접근입니다. 로그인 후 이용해 주십시오.
//		}
		
		if( session != null && session.getAttribute("SSO_ID") != null ){

			    UserSessionVO userSessionVO = new UserSessionVO();
			    
			    // SSO 에서 userId 가져온 후  user 정보 조회 
//			    userSessionVO.setUserId(userId);		

			    // SSO 에서 생성된 Session 에서 로그인ID 가져오기
//			    SSOApiAdmin mnApi = null;
//
//		    	try {
//		    		mnApi = APIMainJni.getInstance().getAPIAdmin();
//		    	} catch (Exception e) {
//		    		e.printStackTrace();
//		    	}
		    	
		    	String ssoId = session.getAttribute("SSO_ID").toString(); //mnApi.getID();
		    	System.out.println("ession.getAttribute('SSO_ID') = " + session.getAttribute("SSO_ID"));
		    	
			    userSessionVO.setUserId(ssoId);
			    
		        //userSessionVO = userService.selectUser(userSessionVO);
			    try {
			    	userSessionVO = SwManagerFactory.getInstance().getSsoManager().selectUser(userSessionVO);
				} catch (Exception e) {
					System.out.println("############### SsoManagerImpl Excetpion!!!");
					throw new ServletException(e);
				}
		        userSessionVO.setReqLocal(request.getLocale().getCountry());

		        //authProgramList = (ArrayList<UserProgramAuthInfoVO>)userService.retriveUserProgramAuthInfo( userSessionVO );
		        try {
		        	authProgramList = (ArrayList<UserProgramAuthInfoVO>)SwManagerFactory.getInstance().getSsoManager().retriveUserProgramAuthInfo( userSessionVO );
		        } catch (Exception e) {
					System.out.println("############### SsoManagerImpl Excetpion!!!");
					throw new ServletException(e);
				}		        		        
		        SessionVO svo = new SessionVO();          
		        svo.setUserInfo(userSessionVO);
		        svo.setAuthProgramList(authProgramList);
		        
		        session = request.getSession();
		        session.setAttribute("ubi.pouser", svo);
		        
		        result = true;
		             
		}else if( exceptionPassUrlCheck(url) ) {  // SSO 를 거치지 않은 예외 통과 URL 인 경우
			result = true;
		}else{
			System.out.println("SSO 연계를 통한 접근이 아닙니다. Session 이 생성되지 않았습니다. ");
			request.setAttribute("errorCode", "100"); 
			//throw new CommonException(commonMessageSource.getMessage("biz.error.com.001"));  // 잘못된 접근입니다. 로그인 후 이용해 주십시오.
			throw new ServletException("잘못된 접근입니다. 로그인 후 이용해 주십시오.");  // 잘못된 접근입니다. 로그인 후 이용해 주십시오.
		}
		
		if( !retriveUserProgramAuthInfo(authProgramList , url) && !exceptionPassUrlCheck(url) ){
			System.out.println("해당 화면에 대한 접근 권한이 없습니다.");
			request.setAttribute("errorCode", "400"); 
			//throw new CommonException(commonMessageSource.getMessage("biz.error.com.004"));  // 해당 화면에 대한 접근 권한이 없습니다.        	
			throw new ServletException("해당 화면에 대한 접근 권한이 없습니다.");  // 해당 화면에 대한 접근 권한이 없습니다.    	
        }

//		return result;
		return;
	}
	private boolean retriveUserProgramAuthInfo(ArrayList<UserProgramAuthInfoVO> authProgramList , String url ){
		boolean authCheck = false;
		
		if( authProgramList != null){
			for( UserProgramAuthInfoVO userProgram : authProgramList ){
				if(  userProgram.getProgramUrl().indexOf(url) >= 0 ){
					authCheck = true;
					break;
				}
			}
		}
		return authCheck;
	}	
	
	private boolean exceptionPassUrlCheck(String url){
		//SSO 를 거치지 않을 URL이 존재한다면 passUrls에 등록한다
//		if (this.passUrls.contains(url)) {
//			return true;
//		} else {
//			return false;
//		}
		String exceptionPassUrlList = UcityConstant.getUrlByKey("BPM.PASSURLLIST");
		if( exceptionPassUrlList != null && exceptionPassUrlList.trim().length() != 0 ){
			String[] exceptionPassUrlArray = exceptionPassUrlList.split(":");
			for( int i = 0 ; i < exceptionPassUrlArray.length ; i++  ){
				if(  url.indexOf(exceptionPassUrlArray[i]) >= 0 ){
					return true;
				}
			}  
		}
		System.out.println("return false");
		return false;
	}
	
	
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
