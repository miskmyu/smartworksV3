/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 7. 19.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.autoindex.manager;

import net.smartworks.server.engine.autoindex.exception.AutoIndexException;
import net.smartworks.server.engine.autoindex.model.AutoIndexDef;
import net.smartworks.server.engine.autoindex.model.AutoIndexDefCond;
import net.smartworks.server.engine.autoindex.model.AutoIndexSeq;
import net.smartworks.server.engine.autoindex.model.AutoIndexSeqCond;
import net.smartworks.server.engine.common.manager.IManager;

public interface IIdxManager extends IManager {

	public AutoIndexDef getAutoIndexDef(String userId, String id, String level) throws AutoIndexException;
	public AutoIndexDef getAutoIndexDef(String userId, AutoIndexDefCond cond, String level) throws AutoIndexException;
	public void setAutoIndexDef(String userId, AutoIndexDef obj, String level) throws AutoIndexException;
	public void removeAutoIndexDef(String userId, String id) throws AutoIndexException;
	public void removeAutoIndexDef(String userId, AutoIndexDefCond cond) throws AutoIndexException;
	public long getAutoIndexDefSize(String userId, AutoIndexDefCond cond) throws AutoIndexException;
	public AutoIndexDef[] getAutoIndexDefs(String userId, AutoIndexDefCond cond, String level) throws AutoIndexException;
	
	public AutoIndexSeq getAutoIndexSeq(String userId, String id, String level) throws AutoIndexException;
	public AutoIndexSeq getAutoIndexSeq(String userId, AutoIndexSeqCond cond, String level) throws AutoIndexException;
	public void setAutoIndexSeq(String userId, AutoIndexSeq obj, String level) throws AutoIndexException;
	public void removeAutoIndexSeq(String userId, String id) throws AutoIndexException;
	public void removeAutoIndexSeq(String userId, AutoIndexSeqCond cond) throws AutoIndexException;
	public long getAutoIndexSeqSize(String userId, AutoIndexSeqCond cond) throws AutoIndexException;
	public AutoIndexSeq[] getAutoIndexSeqs(String userId, AutoIndexSeqCond cond, String level) throws AutoIndexException;
	
	public void createAutoIndexDefByFormXml(String userId, String formXml) throws AutoIndexException;
	
}
