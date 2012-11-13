package pro.ucity.sso.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis.security.AuthenticatedUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;


public class UcityAuthenticationSuccesHandler extends SavedRequestAwareAuthenticationSuccessHandler{

	private org.springframework.security.web.savedrequest.RequestCache requestCache;
	
	public org.springframework.security.web.savedrequest.RequestCache getRequestCache() {
		return requestCache;
	}

	public void setRequestCache(org.springframework.security.web.savedrequest.RequestCache requestCache) {
		this.requestCache = requestCache;
	}

	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		
		SavedRequest sr = requestCache.getRequest(request, response);
		if (sr != null)
			System.out.println(sr.getRedirectUrl());
	}
}
