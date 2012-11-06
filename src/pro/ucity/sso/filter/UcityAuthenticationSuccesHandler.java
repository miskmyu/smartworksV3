package pro.ucity.sso.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

public class UcityAuthenticationSuccesHandler extends SavedRequestAwareAuthenticationSuccessHandler{
	
	public void  onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		
		System.out.println("######### UcityAuthenticationHandler ##########");
		super.onAuthenticationSuccess(request, response, authentication);
	}

}
