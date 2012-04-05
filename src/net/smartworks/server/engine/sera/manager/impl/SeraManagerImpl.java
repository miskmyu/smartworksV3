/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 4. 2.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.sera.manager.impl;

import net.smartworks.server.engine.common.manager.AbstractManager;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.sera.exception.SeraException;
import net.smartworks.server.engine.sera.manager.ISeraManager;
import net.smartworks.server.engine.sera.model.MentorDetail;

public class SeraManagerImpl extends AbstractManager implements ISeraManager {

	public MentorDetail getMentorDetailById(String userId, String mentorId) throws SeraException {
		try {
			if (CommonUtil.isEmpty(mentorId)) 
				return null;
			MentorDetail mentor = (MentorDetail)this.get(MentorDetail.class, mentorId);
			return mentor;
		} catch (SeraException e) {
			throw e;
		} catch (Exception e) {
			throw new SeraException(e);
		}
	}
	public MentorDetail setMentorDetail(String userId, MentorDetail mentorDetail) throws SeraException {
		try {
			if (mentorDetail == null)
				return null;
			this.set(mentorDetail);
			return mentorDetail;
		} catch (SeraException e) {
			throw e;
		} catch (Exception e) {
			throw new SeraException(e);
		}
	}
	public void removeMentorDetail(String user, String mentorId) throws SeraException {
		try {
			MentorDetail obj = this.getMentorDetailById(user, mentorId);
			this.getHibernateTemplate().delete(obj);
			this.getHibernateTemplate().flush();
		} catch (SeraException e) {
			throw e;
		} catch (Exception e) {
			throw new SeraException(e);
		}
	}

}
