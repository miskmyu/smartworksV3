<%@page import="net.smartworks.server.engine.common.manager.IManager"%>
<%@page import="net.smartworks.server.engine.pkg.model.PkgPackage"%>
<%@page import="net.smartworks.server.engine.pkg.model.PkgPackageCond"%>
<%@page import="net.smartworks.server.engine.factory.SwManagerFactory"%>
<%@page import="java.io.File"%>
<%@page import="net.smartworks.util.OSValidator"%>
<%@page import="net.smartworks.util.SmartUtil"%>
<%@page import="net.smartworks.model.work.Work"%>
<%@page import="net.smartworks.server.engine.common.util.FileUtil"%>
<%@page import="net.smartworks.server.engine.common.exception.MisException"%>
<%@page import="net.smartworks.server.engine.common.util.CommonUtil"%>
<%@page import="net.smartworks.server.engine.common.model.Result"%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/xml;charset=UTF-8"%>
<%
Result res = new Result();
try {
	String method = CommonUtil.toNull(request.getParameter("method"));
	
	if (method == null)
		throw new MisException("errorCodeNullMethod", "Method is null.");
	if (method.equals("setImage")) {
		String companyId = CommonUtil.toNotNull(request.getParameter("companyId"));
		String group = CommonUtil.toNotNull(request.getParameter("group"));
		String id = CommonUtil.toNull(request.getParameter("id"));
		String base64 = CommonUtil.toNull(request.getParameter("base64"));
		if (id == null)
			throw new MisException("errorCodeNullId", "Id is null.");
		if (base64 == null)
			throw new MisException("errorCodeNullBase64", "Base64 is null.");
		
		StringBuffer buf = new StringBuffer(OSValidator.getImageDirectory()).append("/SmartFiles/").append(companyId).append("/").append(Work.WORKDEF_IMG_DIR).append("/");
		if (!CommonUtil.isEmpty(group))
			buf.append(group).append("/");
		
		buf.append(id).append(".png");
		
		FileUtil.writeByBase64(buf.toString(), base64, true);
		
	} else {
		throw new MisException("errorCodeNoSuchMethod", "No such method has been found.");
	}
	
	res.setStatus("success");
	
} catch (MisException e) {
	res.setStatus("failure");
	res.setType(e.getClass().getName());
	res.setObject(e.toNode());
} catch (Exception e) {
	res.setStatus("failure");
	res.setType(e.getClass().getName());
	res.setObject(new MisException("errorCodeUnknown", "Unknown error has been thrown.").toNode());
} finally {
	out.print(res.toString());
}
%>