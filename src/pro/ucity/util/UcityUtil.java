/*	
 * $Id$
 * created by    : SHIN HYUN SEONG
 * creation-date : 2011. 7. 15.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package pro.ucity.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.smartworks.model.instance.TaskInstance;
import net.smartworks.model.security.AccessPolicy;
import net.smartworks.model.work.FormField;
import net.smartworks.model.work.SmartForm;
import net.smartworks.server.service.factory.SwServiceFactory;
import net.smartworks.service.ISmartWorks;
import net.smartworks.util.SmartUtil;

public class UcityUtil {

	public UcityUtil() {
		super();
	}

	public static void startUServiceProcess(TaskInstance taskInstance, Map<String, Object> data) throws Exception{
		if(taskInstance == null || data == null){
			throw new Exception("Invalid parameters exception !!!");
		}
		
		HttpServletRequest request = null;
		
		SwServiceFactory.getInstance().getWorkService().getRecord(request);
		
		Map<String, Object> requestBody = new HashMap<String, Object>();
		SmartForm form = taskInstance.getSmartForm();
		if(SmartUtil.isBlankObject(form) || SmartUtil.isBlankObject(form.getFields())){
			throw new Exception("Invalid Form information exception !!!");
		}
		
		requestBody.put("workId", taskInstance.getWork().getId());
		requestBody.put("formId", form.getId());
		requestBody.put("formName", form.getName());
		
		Map<String, Object> fieldData = new HashMap<String, Object>();
		for(int i=0; i<form.getFields().length; i++){
			FormField field = form.getFields()[i];
			if(data.containsKey(field.getName())){
				fieldData.put(field.getId(), data.get(field.getName()));
			}
		}
		requestBody.put("frmSmartForm", fieldData);
		
		Map<String, Object> accessData = new HashMap<String, Object>();	
		accessData.put("selWorkSpace", SmartUtil.getSystemUser().getId());
		accessData.put("selWorkSpaceType", ISmartWorks.SPACE_TYPE_USER);
		accessData.put("selAccessLevel", AccessPolicy.LEVEL_PUBLIC);
		requestBody.put("frmAccessSpace", accessData);
		
		SwServiceFactory.getInstance().getInstanceService().startProcessWorkInstance(requestBody, null);
		
	}
	
	public static void performUServiceTask(TaskInstance taskInstance, Map<String, Object> data) throws Exception{
		
	}
}

