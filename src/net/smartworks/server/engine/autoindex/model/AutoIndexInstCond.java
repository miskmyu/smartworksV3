/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 7. 19.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.autoindex.model;

import net.smartworks.server.engine.common.model.MisObjectCond;

public class AutoIndexInstCond extends MisObjectCond {

	String instanceId = null;
	String formId = null;
	String fieldId = null;
	String refType = null;//해당 업무가 프로세스냐 정보관리냐
	String type = null;//자동생성타입이 사용자지정, 날짜, 시퀀스등....
	String Value = null;
	String seperator = null;
	int seq = -1;
	
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public String getFieldId() {
		return fieldId;
	}
	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}
	public String getRefType() {
		return refType;
	}
	public void setRefType(String refType) {
		this.refType = refType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}
	public String getSeperator() {
		return seperator;
	}
	public void setSeperator(String seperator) {
		this.seperator = seperator;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
}
