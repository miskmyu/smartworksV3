/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 7. 19.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.autoindex.model;

import net.smartworks.server.engine.common.model.MisObject;
import net.smartworks.server.engine.common.util.CommonUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AutoIndexDef extends MisObject {
	
	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(AutoIndexDef.class);
	
	private static final String NAME = CommonUtil.toName(AutoIndexDef.class, PREFIX);

	String formId = null;
	int version = 1;
	String fieldId = null;
	AutoIndexRule[] rules = null;
	
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getFieldId() {
		return fieldId;
	}
	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}
	public AutoIndexRule[] getRules() {
		return rules;
	}
	public void setRules(AutoIndexRule[] rules) {
		this.rules = rules;
	}
	
}
