/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 4. 2.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.sera.manager;

import net.smartworks.server.engine.sera.exception.SeraException;
import net.smartworks.server.engine.sera.model.MentorDetail;

public interface ISeraManager {

	public abstract MentorDetail getMentorDetailById(String userId, String mentorId) throws SeraException;
	public abstract MentorDetail setMentorDetail(String userId, MentorDetail mentor) throws SeraException;
	public abstract void removeMentorDetail(String userId, String mentorId) throws SeraException;
	
}
