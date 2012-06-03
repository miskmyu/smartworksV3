package net.smartworks.server.service.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.smartworks.model.community.info.UserInfo;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.service.ILoginService;
import net.smartworks.server.service.factory.SwServiceFactory;
import net.smartworks.util.SmartUtil;

import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements ILoginService {

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String userId = request.getParameter("userId");
			request.getSession().removeAttribute(userId);
			SwManagerFactory.getInstance().getLoginUserManager().removeLoginUser(userId, userId);

			UserInfo[] userInfos = SwServiceFactory.getInstance().getCommunityService().getAvailableChatter(request);
			SmartUtil.publishAChatters(userInfos);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
