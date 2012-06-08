package net.smartworks.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.smartworks.model.community.info.UserInfo;
import net.smartworks.server.engine.common.util.CommonUtil;
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
			UserInfo[] userInfos = SwServiceFactory.getInstance().getCommunityService().getAvailableChatter(request);
			UserInfo[] finalUserInfos = null;
			List<UserInfo> finalUserInfoList = new ArrayList<UserInfo>();
			if(!CommonUtil.isEmpty(userInfos)) {
				SwManagerFactory.getInstance().getLoginUserManager().removeLoginUser(userId, userId);
				for(UserInfo userInfo : userInfos) {
					if(userInfo.getId().equals(userId))
						continue;
					finalUserInfoList.add(userInfo);
				}
			}
			if(finalUserInfoList.size() > 0) {
				finalUserInfos = new UserInfo[finalUserInfoList.size()];
				finalUserInfoList.toArray(finalUserInfos);
			}
			SmartUtil.publishAChatters(finalUserInfos);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}