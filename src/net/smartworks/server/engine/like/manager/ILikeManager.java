/*	
 * $Id$
 * created by    : hsshin
 * creation-date : 2012. 3. 23.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.like.manager;

import net.smartworks.server.engine.like.exception.LikeException;
import net.smartworks.server.engine.like.model.Like;
import net.smartworks.server.engine.like.model.LikeCond;

public interface ILikeManager {

	public void createLike(String user, Like obj) throws LikeException;
	public void removeLike(String user, String objId) throws LikeException;
	public long getLikeSize(String user, LikeCond cond) throws LikeException;
	public Like[] getLikes(String user, LikeCond cond, String level) throws LikeException;

}