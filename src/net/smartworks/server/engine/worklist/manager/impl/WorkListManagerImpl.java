/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2011. 12. 4.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.worklist.manager.impl;

import java.math.BigInteger;
import java.sql.Clob;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.smartworks.server.engine.common.manager.AbstractManager;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.process.process.exception.PrcException;
import net.smartworks.server.engine.process.process.model.PrcProcessInstCond;
import net.smartworks.server.engine.process.process.model.PrcProcessInstExtend;
import net.smartworks.server.engine.process.task.model.TskTask;
import net.smartworks.server.engine.worklist.manager.IWorkListManager;
import net.smartworks.server.engine.worklist.model.SubTaskWorkCond;
import net.smartworks.server.engine.worklist.model.TaskWork;
import net.smartworks.server.engine.worklist.model.TaskWorkCond;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.engine.SessionFactoryImplementor;

public class WorkListManagerImpl extends AbstractManager implements IWorkListManager {

	private String dbType;

	public String getDbType() {
		if (dbType == null) {
			SessionFactory sf = getSessionFactory();
			SessionFactoryImplementor sfi = (SessionFactoryImplementor)sf;
			Dialect dialect = sfi.getDialect();
			if (dialect instanceof PostgreSQLDialect) {
				dbType = "postgresql";
			} else if (dialect instanceof SQLServerDialect) {
				dbType = "sqlserver";
			} else {
				dbType = "oracle";
			}
		}
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	private Query appendCastQuery(StringBuffer queryBuffer , TaskWorkCond cond) throws Exception {
		
		String userIdIns = cond.getTskAssigneeIdIns();
		String userId = cond.getTskAssignee();
		Date fromDate = cond.getTskModifyDateFrom();
		int pageNo = cond.getPageNo();
		int pageSize = cond.getPageSize();
		Date expectEndDateFrom = cond.getExpectEndDateFrom();
		Date expectEndDateTo = cond.getExpectEndDateTo();
		String packageStatus = cond.getPackageStatus();
		
		queryBuffer.append(" from ");
		queryBuffer.append(" (  ");
		queryBuffer.append(" 	select task.tskobjId  ");
		queryBuffer.append(" 		, task.tsktitle  ");
		queryBuffer.append(" 		, task.tskDoc  ");
		queryBuffer.append(" 		, task.tsktype  ");
		queryBuffer.append(" 		, task.tskReftype  ");
		queryBuffer.append(" 		, task.tskstatus  ");
		queryBuffer.append(" 		, task.tskassignee  ");
		queryBuffer.append(" 		, case when task.tskstatus='11' then task.tskassigndate else task.tskexecuteDate end as taskLastModifyDate  ");
		queryBuffer.append(" 		, task.tskcreatedate  ");
		queryBuffer.append(" 		, task.tskname  ");
		queryBuffer.append(" 		, task.tskprcinstid  ");
		queryBuffer.append(" 		, task.tskform  ");
		queryBuffer.append(" 		, task.isStartActivity  ");
		queryBuffer.append(" 		, task.tskWorkSpaceId  ");
		queryBuffer.append(" 		, task.tskDef  ");
		queryBuffer.append(" 		, task.tskApprovalId  ");
		queryBuffer.append(" 		, task.tskForwardId  ");
		queryBuffer.append(" 		, form.packageId  ");
		queryBuffer.append(" 		, pkg.name as packageName  ");
		queryBuffer.append(" 		, pkg.status as packageStatus  ");
		queryBuffer.append(" 		, ctg.id as childCtgId  ");
		queryBuffer.append(" 		, ctg.name as childCtgName  ");
		queryBuffer.append(" 		, case when ctg.parentId = '_PKG_ROOT_' then null else ctg2.id end as parentCtgId  ");
		queryBuffer.append(" 		, case when ctg.parentId = '_PKG_ROOT_' then null else ctg2.name end as parentCtgName  ");
		queryBuffer.append(" 	from  ");
		queryBuffer.append(" 		( ");
		queryBuffer.append(" 			select *  ");
		queryBuffer.append(" 			from ( ");
		queryBuffer.append(" 				select * from tsktask where tskassignee in (").append(userIdIns).append(") ");
		queryBuffer.append(" 				and tsktype not in ('SUBFLOW','xor','route','and') ");
		queryBuffer.append(" 				union all ");
		queryBuffer.append(" 				select * from tsktask where tskprcinstid in ( ");
		queryBuffer.append(" 					select prcobjid from prcprcinst where prccreateuser='").append(userId).append("' ");
		queryBuffer.append(" 				) ");
		queryBuffer.append(" 				and tskobjid not in ( ");
		queryBuffer.append(" 					select tskobjid from tsktask where tskassignee in (").append(userIdIns).append(") ");
		queryBuffer.append(" 					 ");
		queryBuffer.append(" 				) ");
		queryBuffer.append(" 				and tsktype not in ('SUBFLOW','xor','route','and') ");
		queryBuffer.append(" 				and tskassignee != '' ");
		queryBuffer.append(" 			) tsktask ");
		queryBuffer.append(" 			where 1=1 ");
		queryBuffer.append("			and (tsktask.tskAssignee ='").append(userId).append("' or (tsktask.tskAccessLevel not in ('1','2')) or (tsktask.tskaccessLevel = '2' and tsktask.tskaccessValue like '%").append(userId).append("%')) ");
		if (fromDate != null)
			queryBuffer.append(" 			and tsktask.tskModifyDate < :fromDate ");
		if (expectEndDateFrom != null)
			queryBuffer.append(" 			and tsktask.tskExpectEndDate > :expectEndDateFrom ");
		if (expectEndDateTo != null)
			queryBuffer.append(" 			and tsktask.tskExpectEndDate < :expectEndDateTo ");
		
		
		queryBuffer.append(" 		) task,  ");
		queryBuffer.append(" 		swform form  ");
		queryBuffer.append(" 		left outer join  ");
		queryBuffer.append(" 		swpackage pkg  ");
		queryBuffer.append(" 		on form.packageId = pkg.packageId  ");
		queryBuffer.append(" 		left outer join  ");
		queryBuffer.append(" 		swcategory ctg  ");
		queryBuffer.append(" 		on ctg.id = pkg.categoryId  ");
		queryBuffer.append(" 		left outer join  ");
		queryBuffer.append(" 		swcategory ctg2  ");
		queryBuffer.append(" 		on ctg.parentId = ctg2.id  ");
		queryBuffer.append(" 	where task.tskform = form.formid  ");
		if (!CommonUtil.isEmpty(packageStatus))
			queryBuffer.append("	and pkg.status = :packageStatus ");
		queryBuffer.append(" ) taskInfo  ");
		queryBuffer.append(" join  ");
		queryBuffer.append(" (  ");
		queryBuffer.append(" 	select  ");
		queryBuffer.append(" 		 prcInst.prcObjId  ");
		queryBuffer.append(" 		, prcInst.prcTitle  ");
		queryBuffer.append(" 		, prcInst.prcType  ");
		queryBuffer.append(" 		, prcInst.prcStatus  ");
		queryBuffer.append(" 		, prcInst.prcCreateUser  ");
		queryBuffer.append(" 		, prcInst.prcDid  ");
		queryBuffer.append(" 		, prcInst.prcPrcId  ");
		queryBuffer.append(" 		, prcInst.prcCreateDate  ");
		queryBuffer.append(" 		, prcInst.prcWorkSpaceId   ");
		queryBuffer.append(" 		, prcInstInfo.lastTask_tskobjid  ");
		queryBuffer.append(" 		, prcInstInfo.lastTask_tskname  ");
		queryBuffer.append(" 		, prcInstInfo.lastTask_tskcreateuser  ");
		queryBuffer.append(" 		, prcInstInfo.lastTask_tskcreateDate  ");
		queryBuffer.append(" 		, prcInstInfo.lastTask_tskstatus  ");
		queryBuffer.append(" 		, prcInstInfo.lastTask_tsktype  ");
		queryBuffer.append(" 		, prcInstInfo.lastTask_tsktitle  ");
		queryBuffer.append(" 		, prcInstInfo.lastTask_tskassignee  ");
		queryBuffer.append(" 		, prcInstInfo.lastTask_tskexecuteDate  ");
		queryBuffer.append(" 		, prcInstInfo.lastTask_tskduedate  ");
		queryBuffer.append(" 		, prcInstInfo.lastTask_tskform  ");
		queryBuffer.append(" 		, prcInstInfo.lastTask_tskWorkSpaceId   ");
		queryBuffer.append(" 		, (select count(*) from tsktask where tskstatus='11' and tsktype='common' and tskprcInstId = prcInst.prcObjid) as lastTaskCount  ");
		queryBuffer.append(" 	from   ");
		queryBuffer.append(" 		prcprcinst prcInst,   ");
		queryBuffer.append(" 		(  ");
		queryBuffer.append(" 			select a.tskprcinstid as lastTask_tskprcinstid  ");
		queryBuffer.append(" 					, task.tskobjid as lastTask_tskobjid  ");
		queryBuffer.append(" 					, task.tskname as lastTask_tskname  ");
		queryBuffer.append(" 					, task.tskcreateuser as lastTask_tskcreateuser  ");
		queryBuffer.append(" 					, task.tskcreateDate as lastTask_tskcreateDate  ");
		queryBuffer.append(" 					, task.tskstatus as lastTask_tskstatus  ");
		queryBuffer.append(" 					, task.tsktype as lastTask_tsktype  ");
		queryBuffer.append(" 					, task.tsktitle as lastTask_tsktitle  ");
		queryBuffer.append(" 					, task.tskassignee as lastTask_tskassignee  ");
		queryBuffer.append(" 					, task.tskexecuteDate as lastTask_tskexecuteDate  ");
		queryBuffer.append(" 					, task.tskduedate as lastTask_tskduedate  ");
		queryBuffer.append(" 					, task.tskform as lastTask_tskform  ");
		queryBuffer.append(" 					, task.tskWorkSpaceId as lastTask_tskWorkSpaceId   ");
		queryBuffer.append(" 			from (  ");
		queryBuffer.append(" 					select tskprcinstId , max(tskCreatedate) as createDate   ");
		queryBuffer.append(" 					from tsktask   ");
		queryBuffer.append(" 					where tsktype not in ('and','route','SUBFLOW','xor')  ");
		queryBuffer.append(" 					group by tskprcinstid  ");
		queryBuffer.append(" 				  ) a,	  ");
		queryBuffer.append(" 				  TskTask task		  ");
		queryBuffer.append(" 			where   ");
		queryBuffer.append(" 				a.createDate = task.tskcreatedate  ");
		queryBuffer.append(" 		) prcInstInfo	  ");
		queryBuffer.append(" 	where  ");
		queryBuffer.append(" 		prcInst.prcobjid=prcInstInfo.lastTask_tskprcinstid  ");
		queryBuffer.append(" ) prcInstInfo  ");
		queryBuffer.append(" on taskInfo.tskPrcInstId = prcInstInfo.prcObjId  ");
		
		this.appendOrderQuery(queryBuffer, "taskInfo", cond);

		Query query = this.getSession().createSQLQuery(queryBuffer.toString());

		if (fromDate != null)
			query.setTimestamp("fromDate", fromDate);
		if (expectEndDateFrom != null)
			query.setTimestamp("expectEndDateFrom", expectEndDateFrom);
		if (expectEndDateTo != null)
			query.setTimestamp("expectEndDateTo", expectEndDateTo);
		if (!CommonUtil.isEmpty(packageStatus))
			query.setString("packageStatus", packageStatus);
		
		if (pageSize > 0|| pageNo >= 0) {
			query.setFirstResult(pageNo * pageSize);
			query.setMaxResults(pageSize);
		}
		return query;
	}
	public long getCastWorkListSize(String user, TaskWorkCond cond) throws Exception {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			buf.append(" count(*) ");
			Query query = this.appendCastQuery(buf, cond);
			List list = query.list();

			Object sizeObj = list.get(0);
			long count = 0;
			if (sizeObj instanceof BigInteger) {
				count = ((BigInteger)sizeObj).longValue();
			} else if (sizeObj instanceof Long) {
				count = ((Long)sizeObj).longValue();
			} else {
				count = Long.parseLong(sizeObj.toString());
			}
			return count;
		} catch (PrcException e) {
			throw e;
		} catch (Exception e) {
			throw new PrcException(e);
		}
	}
	public TaskWork[] getCastWorkList(String user, TaskWorkCond cond) throws Exception {
		try {

			StringBuffer queryBuffer = new StringBuffer();
			queryBuffer.append(" select taskInfo.*, ");
			queryBuffer.append(" prcInstInfo.* ");
			
			Query query = this.appendCastQuery(queryBuffer, cond);
		
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			List objList = new ArrayList();
			for (Iterator itr = list.iterator(); itr.hasNext();) {
				Object[] fields = (Object[]) itr.next();
				TaskWork obj = new TaskWork();
				int j = 0;
		
				obj.setTskObjId((String)fields[j++]);
				obj.setTskTitle((String)fields[j++]);
				String tempCountStr = null;
				if(this.getDbType().equals("sqlserver")) {
					Clob varData = (Clob)fields[j++];
					if (varData != null) {
						long length = varData.length();
						tempCountStr = varData.getSubString(1, (int)length);
					}
				} else {
					tempCountStr = (String)fields[j++];
				}
				obj.setTskDoc(tempCountStr);
				obj.setTskType((String)fields[j++]);     
				obj.setTskRefType((String)fields[j++]);     
				obj.setTskStatus((String)fields[j++]);   
				obj.setTskAssignee((String)fields[j++]); 
				obj.setTaskLastModifyDate((Timestamp)fields[j++]);
				obj.setTskCreateDate((Timestamp)fields[j++]);
				obj.setTskName((String)fields[j++]);     
				obj.setTskPrcInstId((String)fields[j++]);
				obj.setTskForm((String)fields[j++]);     
				obj.setIsStartActivity((String)fields[j++]); 
				obj.setTskWorkSpaceId((String)fields[j++]);     
				obj.setTskDef((String)fields[j++]);     
				obj.setTskApprovalId((String)fields[j++]);     
				obj.setTskForwardId((String)fields[j++]);     
				obj.setPackageId((String)fields[j++]);     
				obj.setPackageName((String)fields[j++]);   
				obj.setPackageStatus((String)fields[j++]);   
				obj.setChildCtgId((String)fields[j++]);  
				obj.setChildCtgName((String)fields[j++]);
				obj.setParentCtgId((String)fields[j++]); 
				obj.setParentCtgName((String)fields[j++]);
				obj.setPrcObjId((String)fields[j++]);                           
				obj.setPrcTitle((String)fields[j++]);                           
				obj.setPrcType((String)fields[j++]);                            
				obj.setPrcStatus((String)fields[j++]);                          
				obj.setPrcCreateUser((String)fields[j++]);                      
				obj.setPrcDid((String)fields[j++]);                             
				obj.setPrcPrcId((String)fields[j++]); 
				obj.setPrcCreateDate((Timestamp)fields[j++]);                    
				obj.setPrcWorkSpaceId((String)fields[j++]); 
				obj.setLastTskObjId((String)fields[j++]);                       
				obj.setLastTskName((String)fields[j++]);                        
				obj.setLastTskCreateUser((String)fields[j++]);                  
				obj.setLastTskCreateDate((Timestamp)fields[j++]);                  
				obj.setLastTskStatus((String)fields[j++]);                      
				obj.setLastTskType((String)fields[j++]);                        
				obj.setLastTskTitle((String)fields[j++]);                       
				obj.setLastTskAssignee((String)fields[j++]);                    
				obj.setLastTskExecuteDate((Timestamp)fields[j++]);                 
				obj.setLastTskDueDate((Timestamp)fields[j++]); 
				obj.setLastTskForm((String)fields[j++]);    
				obj.setLastTskWorkSpaceId((String)fields[j++]);
				Object lastTaskCountObj = fields[j];
				int lastTaskCount = 0;
				if(lastTaskCountObj == null) {
					lastTaskCount = -1;
				} else {
					if (lastTaskCountObj instanceof BigInteger) {
						lastTaskCount = ((BigInteger)lastTaskCountObj).intValue();
					} else if (lastTaskCountObj instanceof Long) {
						lastTaskCount = ((Long)lastTaskCountObj).intValue();
					} else {
						lastTaskCount = Integer.parseInt(lastTaskCountObj.toString());
					}
				}
				obj.setLastTskCount(lastTaskCount == 0 ? 1 : lastTaskCount);
				objList.add(obj);
			}
			list = objList;
			TaskWork[] objs = new TaskWork[list.size()];
			list.toArray(objs);
			return objs;
				
		} catch (Exception e) {
			throw new PrcException(e);
		}
	}
	private Query appendQuery(StringBuffer queryBuffer , TaskWorkCond cond) throws Exception {
		
		String packageStatus = cond.getPackageStatus();
		String tskAssignee = cond.getTskAssignee();
		String tskAssigneeOrTskSpaceId = cond.getTskAssigneeOrSpaceId();
		String tskStartOrAssigned = cond.getTskStartOrAssigned();
		String tskStartOnly = cond.getTskStartOnly();
		//assingnedOnly 값이 true 라면 실행중인(11) 태스크만 조회를 한다.
		String tskStatus =  cond.getTskStatus();
		String prcStatus = cond.getPrcStatus();
		String[] prcStatusIns = cond.getPrcStatusIns();
		Date lastInstanceDate = cond.getLastInstanceDate();
		String tskRefType = cond.getTskRefType();
		int pageNo = cond.getPageNo();
		int pageSize = cond.getPageSize();
		
		String searchKey = cond.getSearchKey();
		
		String worksSpaceId = cond.getTskWorkSpaceId();
		Date executionDateFrom = cond.getTskExecuteDateFrom();
		Date executionDateTo = cond.getTskExecuteDateTo();
		Date executionDateBefore = cond.getTskExecuteDateBefore();

		String[] taskObjIdIns = cond.getTskObjIdIns();
		
		queryBuffer.append("from ");
		queryBuffer.append("( ");
		queryBuffer.append("	select task.tskobjId ");
		queryBuffer.append("		, task.tsktitle ");
		queryBuffer.append("		, task.tskDoc ");
		queryBuffer.append("		, task.tsktype ");
		queryBuffer.append("		, task.tskReftype ");
		queryBuffer.append("		, task.tskstatus ");
		queryBuffer.append("		, task.tskassignee ");
		queryBuffer.append("		, case when task.tskstatus='11' then task.tskassigndate else task.tskexecuteDate end as taskLastModifyDate ");
		queryBuffer.append("		, task.tskcreatedate ");
		queryBuffer.append("		, task.tskname ");
		queryBuffer.append("		, task.tskprcinstid ");
		queryBuffer.append("		, task.tskform ");
		queryBuffer.append("		, task.isStartActivity ");
		queryBuffer.append("		, task.tskWorkSpaceId ");//workSpaceId
		queryBuffer.append("		, task.tskWorkSpaceType ");//workSpaceId
		queryBuffer.append("		, task.tskAccessLevel ");
		queryBuffer.append("		, task.tskAccessValue ");
		queryBuffer.append("		, task.tskDef ");
		queryBuffer.append("		, task.tskApprovalId ");
		queryBuffer.append("		, task.tskForwardId ");
		queryBuffer.append("		, form.packageId ");
		queryBuffer.append("		, pkg.name as packageName ");
		queryBuffer.append("		, pkg.status as packageStatus ");
		queryBuffer.append("		, ctg.id as childCtgId ");
		queryBuffer.append("		, ctg.name as childCtgName ");
		queryBuffer.append("		, case when ctg.parentId = '_PKG_ROOT_' then null else ctg2.id end as parentCtgId ");
		queryBuffer.append("		, case when ctg.parentId = '_PKG_ROOT_' then null else ctg2.name end as parentCtgName ");
		queryBuffer.append("	from tsktask task, ");
		queryBuffer.append("		swform form ");
		queryBuffer.append("		left outer join ");
		queryBuffer.append("		swpackage pkg ");
		queryBuffer.append("		on form.packageId = pkg.packageId ");
		queryBuffer.append("		left outer join ");
		queryBuffer.append("		swcategory ctg ");
		queryBuffer.append("		on ctg.id = pkg.categoryId ");
		queryBuffer.append("		left outer join ");
		queryBuffer.append("		swcategory ctg2 ");
		queryBuffer.append("		on ctg.parentId = ctg2.id ");
		queryBuffer.append("	where tsktype not in ('and','route','SUBFLOW','xor') ");
		queryBuffer.append("	and task.tskform = form.formid ");
		queryBuffer.append("	and form.packageId is not null ");
		if (!CommonUtil.isEmpty(packageStatus))
			queryBuffer.append("	and pkg.status = :packageStatus ");
		if (!CommonUtil.isEmpty(tskAssignee))
			queryBuffer.append("	and task.tskassignee = :tskAssignee ");
		if (!CommonUtil.isEmpty(tskAssigneeOrTskSpaceId))
			queryBuffer.append("	and (task.tskassignee = :tskAssigneeOrTskSpaceId or task.tskWorkSpaceId = :tskAssigneeOrTskSpaceId) ");
		if (!CommonUtil.isEmpty(tskStartOrAssigned))
			queryBuffer.append("	and ((task.tskassignee = :tskStartOrAssigned and task.isStartActivity = 'true' and tsktype in('SINGLE','COMMON')) or (task.tskStatus = '11' and task.tskassignee = :tskStartOrAssigned)) ");
		if (!CommonUtil.isEmpty(tskStartOnly))
			queryBuffer.append("	and (task.tskassignee = :tskStartOnly and task.isStartActivity = 'true' and tsktype in('SINGLE','COMMON')) ");
		if (!CommonUtil.isEmpty(tskStatus))
			queryBuffer.append("	and task.tskstatus = :tskStatus ");
		if (!CommonUtil.isEmpty(worksSpaceId))
			queryBuffer.append("	and task.tskWorkSpaceId = :worksSpaceId ");
		if (executionDateFrom != null)
			queryBuffer.append("	and task.tskExecuteDate > :executionDateFrom ");
		if (executionDateTo != null)
			queryBuffer.append("	and task.tskExecuteDate < :executionDateTo ");
		if (executionDateBefore != null)
			queryBuffer.append("	and task.tskExecuteDate < :executionDateBefore ");
		if (taskObjIdIns != null && taskObjIdIns.length != 0) {
			queryBuffer.append(" 	and task.tskObjId in (");
			for (int i=0; i<taskObjIdIns.length; i++) {
				if (i != 0)
					queryBuffer.append(", ");
				queryBuffer.append(":taskObjIdIn").append(i);
			}
			queryBuffer.append(")");
		}
		queryBuffer.append(") taskInfo ");
		//queryBuffer.append("left outer join ");
		queryBuffer.append("join ");
		queryBuffer.append("( ");
		queryBuffer.append("	select ");
		queryBuffer.append("		 prcInst.prcObjId ");
		queryBuffer.append("		, prcInst.prcTitle ");
		queryBuffer.append("		, prcInst.prcType ");
		queryBuffer.append("		, prcInst.prcStatus ");
		queryBuffer.append("		, prcInst.prcCreateUser ");
		queryBuffer.append("		, prcInst.prcDid ");
		queryBuffer.append("		, prcInst.prcPrcId ");
		queryBuffer.append("		, prcInst.prcCreateDate ");
		queryBuffer.append("		, prcInst.prcWorkSpaceId "); //workSpaceId
		queryBuffer.append("		, prcInst.prcWorkSpaceType "); //workSpaceId
		queryBuffer.append("		, prcInst.prcAccessLevel ");
		queryBuffer.append("		, prcInst.prcAccessValue ");
		queryBuffer.append("		, prcInstInfo.lastTask_tskobjid ");
		queryBuffer.append("		, prcInstInfo.lastTask_tskname ");
		queryBuffer.append("		, prcInstInfo.lastTask_tskcreateuser ");
		queryBuffer.append("		, prcInstInfo.lastTask_tskcreateDate ");
		queryBuffer.append("		, prcInstInfo.lastTask_tskstatus ");
		queryBuffer.append("		, prcInstInfo.lastTask_tsktype ");
		queryBuffer.append("		, prcInstInfo.lastTask_tsktitle ");
		queryBuffer.append("		, prcInstInfo.lastTask_tskassignee ");
		queryBuffer.append("		, prcInstInfo.lastTask_tskexecuteDate ");
		queryBuffer.append("		, prcInstInfo.lastTask_tskduedate ");
		queryBuffer.append("		, prcInstInfo.lastTask_tskform ");
		queryBuffer.append("		, prcInstInfo.lastTask_tskWorkSpaceId "); //workSpaceId
		queryBuffer.append("		, prcInstInfo.lastTask_tskWorkSpaceType ");
		queryBuffer.append("		, (select count(*) from tsktask where tskstatus='11' and tsktype='common' and tskprcInstId = prcInst.prcObjid) as lastTaskCount ");
		queryBuffer.append("	from  ");
		queryBuffer.append("		prcprcinst prcInst,  ");
		queryBuffer.append("		( ");
		queryBuffer.append("			select a.tskprcinstid as lastTask_tskprcinstid ");
		queryBuffer.append("					, task.tskobjid as lastTask_tskobjid ");
		queryBuffer.append("					, task.tskname as lastTask_tskname ");
		queryBuffer.append("					, task.tskcreateuser as lastTask_tskcreateuser ");
		queryBuffer.append("					, task.tskcreateDate as lastTask_tskcreateDate ");
		queryBuffer.append("					, task.tskstatus as lastTask_tskstatus ");
		queryBuffer.append("					, task.tsktype as lastTask_tsktype ");
		queryBuffer.append("					, task.tsktitle as lastTask_tsktitle ");
		queryBuffer.append("					, task.tskassignee as lastTask_tskassignee ");
		queryBuffer.append("					, task.tskexecuteDate as lastTask_tskexecuteDate ");
		queryBuffer.append("					, task.tskduedate as lastTask_tskduedate ");
		queryBuffer.append("					, task.tskform as lastTask_tskform ");
		queryBuffer.append("					, task.tskWorkSpaceId as lastTask_tskWorkSpaceId ");
		queryBuffer.append("					, task.tskWorkSpaceType as lastTask_tskWorkSpaceType ");
		queryBuffer.append("			from ( ");
		queryBuffer.append("					select tskprcinstId , max(tskCreatedate) as createDate  ");
		queryBuffer.append("					from tsktask  ");
		queryBuffer.append("					where tsktype not in ('and','route','SUBFLOW','xor') ");
		queryBuffer.append("					group by tskprcinstid ");
		queryBuffer.append("				  ) a,	 ");
		queryBuffer.append("				  TskTask task		 ");
		queryBuffer.append("			where  ");
		queryBuffer.append("				a.createDate = task.tskcreatedate ");
		queryBuffer.append("		) prcInstInfo	 ");
		queryBuffer.append("	where ");
		queryBuffer.append("		prcInst.prcobjid=prcInstInfo.lastTask_tskprcinstid ");
		if (!CommonUtil.isEmpty(prcStatus))
			queryBuffer.append("		and prcInst.prcStatus = :prcStatus ");
		if (prcStatusIns != null && prcStatusIns.length != 0) {
			queryBuffer.append(" 		and prcInst.prcStatus in (");
			for (int i=0; i<prcStatusIns.length; i++) {
				if (i != 0)
					queryBuffer.append(", ");
				queryBuffer.append(":prcStatusIn").append(i);
			}
			queryBuffer.append(")");
		}
		queryBuffer.append(") prcInstInfo ");
		queryBuffer.append("on taskInfo.tskPrcInstId = prcInstInfo.prcObjId ");
		queryBuffer.append(" where 1=1 ");
		if (lastInstanceDate != null) {
			queryBuffer.append("and taskInfo.tskCreateDate < :lastInstanceDate ");
			if (tskRefType != null) {
				if(tskRefType.equals(TskTask.TASKREFTYPE_NOTHING))
					queryBuffer.append("and taskInfo.tskReftype is null ");
				else 
					queryBuffer.append("and taskInfo.tskReftype = :tskRefType ");
			}
		} else {
			if (tskRefType != null) {
				if(tskRefType.equals(TskTask.TASKREFTYPE_NOTHING))
					queryBuffer.append("and taskInfo.tskReftype is null ");
				else 
					queryBuffer.append("and taskInfo.tskReftype = :tskRefType ");
			}
		}
		if (!CommonUtil.isEmpty(searchKey)) {
			queryBuffer.append("and (taskInfo.tskName like :searchKey or taskInfo.tskTitle like :searchKey) ");
		}
			
		this.appendOrderQuery(queryBuffer, null, cond);
		//queryBuffer.append("order by taskInfo.tskCreatedate desc ");

		Query query = this.getSession().createSQLQuery(queryBuffer.toString());

		if (pageSize > 0|| pageNo >= 0) {
			query.setFirstResult(pageNo * pageSize);
			query.setMaxResults(pageSize);
		}
		if (!CommonUtil.isEmpty(packageStatus))
			query.setString("packageStatus", packageStatus);
		if (!CommonUtil.isEmpty(tskAssignee))
			query.setString("tskAssignee", tskAssignee);
		if (!CommonUtil.isEmpty(tskAssigneeOrTskSpaceId))
			query.setString("tskAssigneeOrTskSpaceId",tskAssigneeOrTskSpaceId);
		if (!CommonUtil.isEmpty(tskStartOrAssigned))
			query.setString("tskStartOrAssigned",tskStartOrAssigned);
		if (!CommonUtil.isEmpty(tskStartOnly))
			query.setString("tskStartOnly",tskStartOnly);
		if (!CommonUtil.isEmpty(tskStatus))
			query.setString("tskStatus", tskStatus);
		if (lastInstanceDate != null)
			query.setTimestamp("lastInstanceDate", lastInstanceDate);
		if (!CommonUtil.isEmpty(worksSpaceId))
			query.setString("worksSpaceId", worksSpaceId);
		if (executionDateFrom != null)
			query.setTimestamp("executionDateFrom", executionDateFrom);
		if (executionDateTo != null)
			query.setTimestamp("executionDateTo", executionDateTo);
		if (executionDateBefore != null)
			query.setTimestamp("executionDateBefore", executionDateBefore);
		if (!CommonUtil.isEmpty(prcStatus)) 
			query.setString("prcStatus", prcStatus);
		if (!CommonUtil.isEmpty(tskRefType) && !tskRefType.equals(TskTask.TASKREFTYPE_NOTHING)) 
			query.setString("tskRefType", tskRefType);
		if (!CommonUtil.isEmpty(searchKey)) 
			query.setString("searchKey", CommonUtil.toLikeString(searchKey));
		if (taskObjIdIns != null && taskObjIdIns.length != 0) {
			for (int i=0; i<taskObjIdIns.length; i++) {
				query.setString("taskObjIdIn"+i, taskObjIdIns[i]);
			}
		}
		if (prcStatusIns != null && prcStatusIns.length != 0) {
			for (int i=0; i<prcStatusIns.length; i++) {
				query.setString("prcStatusIn"+i, prcStatusIns[i]);
			}
		}
		return query;
	}

	public long getTaskWorkListSize(String user, TaskWorkCond cond) throws Exception {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			buf.append(" count(*) ");
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			Object sizeObj = list.get(0);
			long size = 0;
			if (sizeObj instanceof BigInteger) {
				size = ((BigInteger)sizeObj).longValue();
			} else if (sizeObj instanceof Long) {
				size = ((Long)sizeObj).longValue();
			} else {
				size = Long.parseLong(sizeObj.toString());
			}
			return size;
		} catch (PrcException e) {
			throw e;
		} catch (Exception e) {
			throw new PrcException(e);
		}
	}
	public TaskWork[] getTaskWorkList(String user, TaskWorkCond cond) throws Exception {
		try {

			StringBuffer queryBuffer = new StringBuffer();
			queryBuffer.append(" select taskInfo.*, ");
			queryBuffer.append(" prcInstInfo.* ");
			
			Query query = this.appendQuery(queryBuffer, cond);
		
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			List objList = new ArrayList();
			for (Iterator itr = list.iterator(); itr.hasNext();) {
				Object[] fields = (Object[]) itr.next();
				TaskWork obj = new TaskWork();
				int j = 0;
		
				obj.setTskObjId((String)fields[j++]);    
				obj.setTskTitle((String)fields[j++]);
				String tempCountStr = null;
				if(this.getDbType().equals("sqlserver")) {
					Clob varData = (Clob)fields[j++];
					if (varData != null) {
						long length = varData.length();
						tempCountStr = varData.getSubString(1, (int)length);
					}
				} else {
					tempCountStr = (String)fields[j++];
				}
				obj.setTskDoc(tempCountStr);
				obj.setTskType((String)fields[j++]);
				obj.setTskRefType((String)fields[j++]);
				obj.setTskStatus((String)fields[j++]); 
				obj.setTskAssignee((String)fields[j++]);
				obj.setTaskLastModifyDate((Timestamp)fields[j++]);
				obj.setTskCreateDate((Timestamp)fields[j++]);
				obj.setTskName((String)fields[j++]);
				obj.setTskPrcInstId((String)fields[j++]);
				obj.setTskForm((String)fields[j++]);
				obj.setIsStartActivity((String)fields[j++]);
				obj.setTskWorkSpaceId((String)fields[j++]);
				obj.setTskWorkSpaceType((String)fields[j++]);
				obj.setTskAccessLevel((String)fields[j++]);
				obj.setTskAccessValue((String)fields[j++]);
				obj.setTskDef((String)fields[j++]);
				obj.setTskApprovalId((String)fields[j++]);
				obj.setTskForwardId((String)fields[j++]);
				obj.setPackageId((String)fields[j++]);
				obj.setPackageName((String)fields[j++]);
				obj.setPackageStatus((String)fields[j++]);
				obj.setChildCtgId((String)fields[j++]);
				obj.setChildCtgName((String)fields[j++]);
				obj.setParentCtgId((String)fields[j++]);
				obj.setParentCtgName((String)fields[j++]);
				obj.setPrcObjId((String)fields[j++]);                      
				obj.setPrcTitle((String)fields[j++]);
				obj.setPrcType((String)fields[j++]);
				obj.setPrcStatus((String)fields[j++]);
				obj.setPrcCreateUser((String)fields[j++]);
				obj.setPrcDid((String)fields[j++]);
				obj.setPrcPrcId((String)fields[j++]);
				obj.setPrcCreateDate((Timestamp)fields[j++]);
				obj.setPrcWorkSpaceId((String)fields[j++]);
				obj.setPrcWorkSpaceType((String)fields[j++]);
				obj.setPrcAccessLevel((String)fields[j++]);
				obj.setPrcAccessValue((String)fields[j++]);
				obj.setLastTskObjId((String)fields[j++]);
				obj.setLastTskName((String)fields[j++]);
				obj.setLastTskCreateUser((String)fields[j++]);
				obj.setLastTskCreateDate((Timestamp)fields[j++]);
				obj.setLastTskStatus((String)fields[j++]);
				obj.setLastTskType((String)fields[j++]);
				obj.setLastTskTitle((String)fields[j++]);
				obj.setLastTskAssignee((String)fields[j++]);
				obj.setLastTskExecuteDate((Timestamp)fields[j++]);
				obj.setLastTskDueDate((Timestamp)fields[j++]);
				obj.setLastTskForm((String)fields[j++]);
				obj.setLastTskWorkSpaceId((String)fields[j++]);
				obj.setLastTskWorkSpaceType((String)fields[j++]);
				Object lastTaskCountObj = fields[j];
				int lastTaskCount = 0;
				if(lastTaskCountObj == null) {
					lastTaskCount = -1;
				} else {
					if (lastTaskCountObj instanceof BigInteger) {
						lastTaskCount = ((BigInteger)lastTaskCountObj).intValue();
					} else if (lastTaskCountObj instanceof Long) {
						lastTaskCount = ((Long)lastTaskCountObj).intValue();
					} else {
						lastTaskCount = Integer.parseInt(lastTaskCountObj.toString());
					}
				}
				obj.setLastTskCount(lastTaskCount == 0 ? 1 : lastTaskCount);
				objList.add(obj);
			}
			list = objList;
			TaskWork[] objs = new TaskWork[list.size()];
			list.toArray(objs);
			return objs;
				
		} catch (Exception e) {
			throw new PrcException(e);
		}
	}
	
	private Query appendExtendQuery(StringBuffer queryBuffer, PrcProcessInstCond cond) throws PrcException {
		
		String packageId = cond.getPackageId();
		String[] objIdIns = cond.getObjIdIns();
		String createUser = cond.getCreationUser();
		String prcStatus = cond.getStatus();
		Date creationDateFrom = cond.getCreationDateFrom();
		Date creationDateTo = cond.getCreationDateTo();
		
		int pageNo = cond.getPageNo();
		int pageSize = cond.getPageSize();
		
		queryBuffer.append(" from ( ");
		queryBuffer.append(" 		select ");
		queryBuffer.append(" 			 prcInst.prcObjId ");
		queryBuffer.append(" 			, prcInst.prcName ");
		queryBuffer.append(" 			, prcInst.prcCreateUser ");
		queryBuffer.append(" 			, prcInst.prcCreateDate ");
		queryBuffer.append(" 			, prcInst.prcModifyUser ");
		queryBuffer.append(" 			, prcInst.prcModifyDate ");
		queryBuffer.append(" 			, prcInst.prcStatus ");
		queryBuffer.append(" 			, prcInst.prcTitle ");
		queryBuffer.append(" 			, prcInst.prcDid ");
		queryBuffer.append(" 			, prcInst.prcPrcId ");
		queryBuffer.append(" 			, prcInstInfo.lastTask_tskobjid ");
		queryBuffer.append(" 			, prcInstInfo.lastTask_tskname ");
		queryBuffer.append(" 			, prcInstInfo.lastTask_tskcreateuser ");
		queryBuffer.append(" 			, prcInstInfo.lastTask_tskcreateDate ");
		queryBuffer.append(" 			, prcInstInfo.lastTask_tskstatus ");
		queryBuffer.append(" 			, prcInstInfo.lastTask_tsktype ");
		queryBuffer.append(" 			, prcInstInfo.lastTask_tsktitle ");
		queryBuffer.append(" 			, prcInstInfo.lastTask_tskassignee ");
		queryBuffer.append(" 			, prcInstInfo.lastTask_tskexecuteDate ");
		queryBuffer.append(" 			, prcInstInfo.lastTask_tskduedate ");
		queryBuffer.append(" 			, prcInstInfo.lastTask_tskform ");
		queryBuffer.append(" 			, (select count(*) from tsktask where tskstatus='11' and tsktype='common' and tskprcInstId = prcInst.prcObjid) as lastTaskCount ");
		queryBuffer.append(" 		from  ");
		queryBuffer.append(" 			prcprcinst prcInst,  ");
		queryBuffer.append(" 			( ");
		queryBuffer.append(" 				select a.tskprcinstid as lastTask_tskprcinstid ");
		queryBuffer.append(" 						, task.tskobjid as lastTask_tskobjid ");
		queryBuffer.append(" 						, task.tskname as lastTask_tskname ");
		queryBuffer.append(" 						, task.tskcreateuser as lastTask_tskcreateuser ");
		queryBuffer.append(" 						, task.tskcreateDate as lastTask_tskcreateDate ");
		queryBuffer.append(" 						, task.tskstatus as lastTask_tskstatus ");
		queryBuffer.append(" 						, task.tsktype as lastTask_tsktype ");
		queryBuffer.append(" 						, task.tsktitle as lastTask_tsktitle ");
		queryBuffer.append(" 						, task.tskassignee as lastTask_tskassignee ");
		queryBuffer.append(" 						, task.tskexecuteDate as lastTask_tskexecuteDate ");
		queryBuffer.append(" 						, task.tskduedate as lastTask_tskduedate ");
		queryBuffer.append(" 						, task.tskform as lastTask_tskform ");
		queryBuffer.append(" 				from ( ");
		queryBuffer.append(" 						select tskprcinstId , max(tskCreatedate) as createDate  ");
		queryBuffer.append(" 						from tsktask  ");
		queryBuffer.append(" 						where tsktype='common'  ");
		queryBuffer.append(" 						group by tskprcinstid ");
		queryBuffer.append(" 					  ) a,	 ");
		queryBuffer.append(" 					  TskTask task		 ");
		queryBuffer.append(" 				where  ");
		queryBuffer.append(" 					a.createDate = task.tskcreatedate ");
		queryBuffer.append(" 			) prcInstInfo	 ");
		queryBuffer.append(" 		where ");
		queryBuffer.append(" 			prcInst.prcobjid=prcInstInfo.lastTask_tskprcinstid ");
		if (!CommonUtil.isEmpty(prcStatus))
			queryBuffer.append(" 			and prcInst.prcStatus = :prcStatus ");
		if (!CommonUtil.isEmpty(packageId))
			queryBuffer.append(" 			and prcInst.prcDid = :prcDid ");
		if (!CommonUtil.isEmpty(createUser))
			queryBuffer.append(" 			and prcInst.prcCreateUser = :createUser ");
		if (creationDateFrom != null)
			queryBuffer.append(" 			and prcInst.prcCreateDate > :creationDateFrom");
		if (creationDateTo != null)
			queryBuffer.append(" 			and prcInst.prcCreateDate < :creationDateTo");
		if (objIdIns != null && objIdIns.length != 0) {
			queryBuffer.append(" 			and prcInst.prcObjId in (");
			for (int i=0; i<objIdIns.length; i++) {
				if (i != 0)
					queryBuffer.append(", ");
				queryBuffer.append(":objIdIn").append(i);
			}
			queryBuffer.append(")");
		}
		queryBuffer.append(" 	)info ");
		queryBuffer.append(" 	left outer join ");
		queryBuffer.append(" 	( ");
		queryBuffer.append(" 		select prcinst.prcobjid as prcinstid ");
		queryBuffer.append(" 				, parentCtg.id as parentCtgId ");
		queryBuffer.append(" 				, parentCtg.name as parentCtg ");
		queryBuffer.append(" 				, ctg.id as subCtgId ");
		queryBuffer.append(" 				, ctg.name as subCtg ");
		queryBuffer.append(" 		from prcprcinst prcinst, swpackage pkg , swcategory ctg, swcategory parentCtg ");
		queryBuffer.append(" 		where prcinst.prcdid = pkg.packageid ");
		queryBuffer.append(" 			and pkg.categoryid = ctg.id ");
		queryBuffer.append(" 			and ctg.parentid = parentCtg.id ");
		queryBuffer.append(" 	) ctgInfo ");
		queryBuffer.append(" 	on info.prcobjid = ctginfo.prcinstid ");
		
		Query query = this.getSession().createSQLQuery(queryBuffer.toString());
		
		if (pageSize > 0|| pageNo >= 0) {
			query.setFirstResult(pageNo * pageSize);
			query.setMaxResults(pageSize);
		}
		if (!CommonUtil.isEmpty(prcStatus))
			query.setString("prcStatus", prcStatus);
		if (!CommonUtil.isEmpty(packageId))
			query.setString("prcDid", packageId);
		if (!CommonUtil.isEmpty(createUser))
			query.setString("createUser", createUser);	
		if (creationDateFrom != null)
			query.setTimestamp("creationDateFrom", creationDateFrom);
		if (creationDateTo != null)
			query.setTimestamp("creationDateTo", creationDateTo);	
		if (objIdIns != null && objIdIns.length != 0) {
			for (int i=0; i<objIdIns.length; i++) {
				query.setString("objIdIn"+i, objIdIns[i]);
			}
		}
		return query;
	}

	public long getProcessInstExtendsSize(String user, PrcProcessInstCond cond) throws PrcException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			buf.append(" count(*) ");
			Query query = this.appendExtendQuery(buf, cond);
			List list = query.list();
			
			long count =((Integer)list.get(0)).longValue();
			return count;
		} catch (PrcException e) {
			throw e;
		} catch (Exception e) {
			throw new PrcException(e);
		}
	}
	public PrcProcessInstExtend[] getProcessInstExtends(String user, PrcProcessInstCond cond) throws PrcException {
		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append(" select  ctgInfo.parentCtgId ");
		queryBuffer.append(" 			, ctgInfo.parentCtg ");
		queryBuffer.append(" 			, ctgInfo.subCtgId ");
		queryBuffer.append(" 			, ctgInfo.subCtg  ");
		queryBuffer.append(" 			, info.* ");
		
		Query query = this.appendExtendQuery(queryBuffer, cond);
	
		List list = query.list();
		if (list == null || list.isEmpty())
			return null;
		List objList = new ArrayList();
		for (Iterator itr = list.iterator(); itr.hasNext();) {
			Object[] fields = (Object[]) itr.next();
			PrcProcessInstExtend obj = new PrcProcessInstExtend();
			int j = 0;
	
			obj.setParentCtgId((String)fields[j++]); 
			obj.setParentCtg((String)fields[j++]);
			obj.setSubCtgId((String)fields[j++]);
			obj.setSubCtg((String)fields[j++]);
			obj.setPrcObjId((String)fields[j++]);
			obj.setPrcName((String)fields[j++]);
			obj.setPrcCreateUser((String)fields[j++]);
			obj.setPrcCreateDate((Timestamp)fields[j++]);
			obj.setPrcModifyUser((String)fields[j++]);
			obj.setPrcModifyDate((Timestamp)fields[j++]);
			obj.setPrcStatus((String)fields[j++]);
			obj.setPrcTitle((String)fields[j++]);
			obj.setPrcDid((String)fields[j++]);
			obj.setPrcPrcId((String)fields[j++]);
			obj.setLastTask_tskObjId((String)fields[j++]);
			obj.setLastTask_tskName((String)fields[j++]);
			obj.setLastTask_tskCreateUser((String)fields[j++]);
			obj.setLastTask_tskCreateDate((Timestamp)fields[j++]);
			obj.setLastTask_tskStatus((String)fields[j++]);
			obj.setLastTask_tskType((String)fields[j++]);
			obj.setLastTask_tskTitle((String)fields[j++]);
			obj.setLastTask_tskAssignee((String)fields[j++]);
			obj.setLastTask_tskExecuteDate((Timestamp)fields[j++]);
			obj.setLastTask_tskDueDate((Timestamp)fields[j++]);
			obj.setLastTask_tskForm((String)fields[j++]);
			int lastTaskCount = (Integer)fields[j++];
			obj.setLastTask_tskCount(lastTaskCount == 0 ? 1 : lastTaskCount);
			objList.add(obj);
		}
		list = objList;
		PrcProcessInstExtend[] objs = new PrcProcessInstExtend[list.size()];
		list.toArray(objs);
		return objs;
	}

	
	public TaskWork[] getSubTaskWorkList(String user, SubTaskWorkCond cond) throws Exception {
		try {
			StringBuffer queryBuffer = new StringBuffer();
			queryBuffer.append(" select taskInfo.*, ");
			queryBuffer.append(" prcInstInfo.* ");
			Query query = null;
			//Query query = this.appendSubTaskWorkQuery(queryBuffer, cond);

			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			List objList = new ArrayList();
			for (Iterator itr = list.iterator(); itr.hasNext();) {
				Object[] fields = (Object[]) itr.next();
				TaskWork obj = new TaskWork();
				int j = 0;
		
				obj.setTskObjId((String)fields[j++]);    
				obj.setTskTitle((String)fields[j++]);
				String tempCountStr = null;
				if(this.getDbType().equals("sqlserver")) {
					Clob varData = (Clob)fields[j++];
					if (varData != null) {
						long length = varData.length();
						tempCountStr = varData.getSubString(1, (int)length);
					}
				} else {
					tempCountStr = (String)fields[j++];
				}
				obj.setTskDoc(tempCountStr);
				obj.setTskType((String)fields[j++]);
				obj.setTskRefType((String)fields[j++]);
				obj.setTskStatus((String)fields[j++]); 
				obj.setTskAssignee((String)fields[j++]);
				obj.setTaskLastModifyDate((Timestamp)fields[j++]);
				obj.setTskCreateDate((Timestamp)fields[j++]);
				obj.setTskName((String)fields[j++]);
				obj.setTskPrcInstId((String)fields[j++]);
				obj.setTskForm((String)fields[j++]);
				obj.setIsStartActivity((String)fields[j++]);
				obj.setTskWorkSpaceId((String)fields[j++]);
				obj.setTskAccessLevel((String)fields[j++]);
				obj.setTskAccessValue((String)fields[j++]);
				obj.setTskDef((String)fields[j++]);
				obj.setTskApprovalId((String)fields[j++]);
				obj.setTskForwardId((String)fields[j++]);
				obj.setPackageId((String)fields[j++]);
				obj.setPackageName((String)fields[j++]);
				obj.setPackageStatus((String)fields[j++]);
				obj.setChildCtgId((String)fields[j++]);
				obj.setChildCtgName((String)fields[j++]);
				obj.setParentCtgId((String)fields[j++]);
				obj.setParentCtgName((String)fields[j++]);
				obj.setPrcObjId((String)fields[j++]);                      
				obj.setPrcTitle((String)fields[j++]);
				obj.setPrcType((String)fields[j++]);
				obj.setPrcStatus((String)fields[j++]);
				obj.setPrcCreateUser((String)fields[j++]);
				obj.setPrcDid((String)fields[j++]);
				obj.setPrcPrcId((String)fields[j++]);
				obj.setPrcCreateDate((Timestamp)fields[j++]);
				obj.setPrcWorkSpaceId((String)fields[j++]);
				obj.setPrcAccessLevel((String)fields[j++]);
				obj.setPrcAccessValue((String)fields[j++]);
				obj.setLastTskObjId((String)fields[j++]);
				obj.setLastTskName((String)fields[j++]);
				obj.setLastTskCreateUser((String)fields[j++]);
				obj.setLastTskCreateDate((Timestamp)fields[j++]);
				obj.setLastTskStatus((String)fields[j++]);
				obj.setLastTskType((String)fields[j++]);
				obj.setLastTskTitle((String)fields[j++]);
				obj.setLastTskAssignee((String)fields[j++]);
				obj.setLastTskExecuteDate((Timestamp)fields[j++]);
				obj.setLastTskDueDate((Timestamp)fields[j++]);
				obj.setLastTskForm((String)fields[j++]);
				obj.setLastTskWorkSpaceId((String)fields[j++]);
				Object lastTaskCountObj = fields[j];
				int lastTaskCount = 0;
				if(lastTaskCountObj == null) {
					lastTaskCount = -1;
				} else {
					if (lastTaskCountObj instanceof BigInteger) {
						lastTaskCount = ((BigInteger)lastTaskCountObj).intValue();
					} else if (lastTaskCountObj instanceof Long) {
						lastTaskCount = ((Long)lastTaskCountObj).intValue();
					} else {
						lastTaskCount = Integer.parseInt(lastTaskCountObj.toString());
					}
				}
				obj.setLastTskCount(lastTaskCount == 0 ? 1 : lastTaskCount);
				objList.add(obj);
			}
			list = objList;
			TaskWork[] objs = new TaskWork[list.size()];
			list.toArray(objs);
			return objs;
				
		} catch (Exception e) {
			throw new PrcException(e);
		}
	}

}
