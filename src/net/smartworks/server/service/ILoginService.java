package net.smartworks.server.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ILoginService {

	public abstract void logout(HttpServletRequest request, HttpServletResponse response) throws Exception;

}