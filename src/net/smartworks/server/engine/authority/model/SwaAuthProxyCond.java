package net.smartworks.server.engine.authority.model;

import net.smartworks.server.engine.common.model.MisObjectCond;
import net.smartworks.server.engine.common.util.CommonUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

public class SwaAuthProxyCond extends MisObjectCond  {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(SwaAuthProxyCond.class);
	
	protected static final String PREFIX = "Swa";
	private static final String NAME = CommonUtil.toName(SwaAuthProxyCond.class, PREFIX);
	
	private String resourceId;
	private String type;
	private String accessLevel;
	private String accessValue;
	
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAccessValue() {
		return accessValue;
	}
	public void setAccessValue(String accessValue) {
		this.accessValue = accessValue;
	}
	
	public void addAccessValue(String id) throws Exception {
		if (CommonUtil.isEmpty(this.accessValue)) {
			this.accessValue = id + ";";
		} else {
			String[] accessValueArray = StringUtils.tokenizeToStringArray(this.accessValue, ";");
			if (accessValueArray == null || accessValueArray.length == 0) {
				this.accessValue = id + ";";
			} else {
				boolean isExistUser = false;
				for (int i = 0; i < accessValueArray.length; i++) {
					String accessValueStr = accessValueArray[i];
					if (accessValueStr.equalsIgnoreCase(id)) {
						isExistUser = true;
						break;
					}
				}
				if (!isExistUser)
					this.accessValue = accessValue + id + ";";
			}
		}
	}
	
	public void removeAccessValue(String id) throws Exception {
		if (CommonUtil.isEmpty(accessValue))
			return;
		String[] accessValueArray = StringUtils.tokenizeToStringArray(this.accessValue, ";");
		if (accessValueArray == null || accessValueArray.length == 0) {
			return;
		} else {
			for (int i = 0; i < accessValueArray.length; i++) {
				String accessValueStr = accessValueArray[i];
				if (accessValueStr.equalsIgnoreCase(id)) {
					this.accessValue = StringUtils.replace(this.accessValue, id + ";", "");
					break;
				}
			}
		}
	}
	public void resetAccessValue(String[] ids) throws Exception {
		if (CommonUtil.isEmpty(ids))
			return;
		this.accessValue = null;
		StringBuffer tempAccessValue = new StringBuffer();
		for (int i = 0; i < ids.length; i++) {
			tempAccessValue.append(ids[i] + ";");
		}
		this.accessValue = tempAccessValue.toString();
	}
	public String getAccessLevel() {
		return accessLevel;
	}
	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}
}
