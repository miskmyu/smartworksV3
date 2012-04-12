/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 4. 12.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.sera.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.smartworks.server.engine.common.model.Cond;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.process.task.model.TskTaskCond;

public class SeraFriendCond extends Cond{

	private static final long serialVersionUID = 1L;
	
	private String objId;
	private String userId;
	private String friendId;
	private String friendNameOrder;
	private boolean isFriendNameAsc = true;
	
	public String getObjId() {
		return objId;
	}
	public void setObjId(String objId) {
		this.objId = objId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFriendId() {
		return friendId;
	}
	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}
	public String getFriendNameOrder() {
		return friendNameOrder;
	}
	public void setFriendNameOrder(String friendNameOrder) {
		this.friendNameOrder = friendNameOrder;
	}
	public boolean isFriendNameAsc() {
		return isFriendNameAsc;
	}
	public void setFriendNameAsc(boolean isFriendNameAsc) {
		this.isFriendNameAsc = isFriendNameAsc;
	}
	
}
