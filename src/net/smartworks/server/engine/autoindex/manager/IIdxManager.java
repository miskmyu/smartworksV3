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
import net.smartworks.server.engine.autoindex.model.AutoIndexInst;
import net.smartworks.server.engine.autoindex.model.AutoIndexInstCond;
import net.smartworks.server.engine.common.manager.IManager;

public interface IIdxManager extends IManager {

	public AutoIndexDef getAutoIndexDef(String userId, String id, String level) throws AutoIndexException;
	public AutoIndexDef getAutoIndexDef(String userId, AutoIndexDefCond cond, String level) throws AutoIndexException;
	public void setAutoIndexDef(String userId, AutoIndexDef obj, String level) throws AutoIndexException;
	public void removeAutoIndexDef(String userId, String id) throws AutoIndexException;
	public void removeAutoIndexDef(String userId, AutoIndexDefCond cond) throws AutoIndexException;
	public long getAutoIndexDefSize(String userId, AutoIndexDefCond cond) throws AutoIndexException;
	public AutoIndexDef[] getAutoIndexDefs(String userId, AutoIndexDefCond cond, String level) throws AutoIndexException;
	
	public AutoIndexInst getAutoIndexInst(String userId, String id, String level) throws AutoIndexException;
	public AutoIndexInst getAutoIndexInst(String userId, AutoIndexInstCond cond, String level) throws AutoIndexException;
	public void setAutoIndexInst(String userId, AutoIndexInst obj, String level) throws AutoIndexException;
	public void removeAutoIndexInst(String userId, String id) throws AutoIndexException;
	public void removeAutoIndexInst(String userId, AutoIndexInstCond cond) throws AutoIndexException;
	public long getAutoIndexInstSize(String userId, AutoIndexInstCond cond) throws AutoIndexException;
	public AutoIndexInst[] getAutoIndexInsts(String userId, AutoIndexInstCond cond, String level) throws AutoIndexException;
	
	public void createAutoIndexDefByFormXml(String userId, String formXml) throws AutoIndexException;
	
}
