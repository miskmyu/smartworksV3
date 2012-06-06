/*	
 * $Id$
 * created by    : hsshin
 * creation-date : 2012. 6. 4.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.service.util;

import net.smartworks.model.community.User;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.util.Semaphore;

public class SearchParallelProcessing extends ParallelProcessing {

	private User currentUser;
	private int type;
	private String key;

	public SearchParallelProcessing(Semaphore semaphore, Thread currentThread, User currentUser, int type, String key){
		super(semaphore, currentThread);
		this.currentUser = currentUser;
		this.type = type;
		this.key = key;
	}

	@Override
	public void doRun() throws Exception {

		if(this.key == null) return;

		UserInfo[] userInfos = null;

		switch(this.type){
		case 1:
			userInfos = communityService.searchCompanyUser(key);
			break;
		case 2:
			userInfos = communityService.searchContact(currentUser, key);
			break;
		}

		setArrayResult(userInfos);
	}

}