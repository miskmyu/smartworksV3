/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 9. 27.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package pro.ucity.manager.ucityWorkList.manager.impl;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;
import net.smartworks.server.engine.common.manager.AbstractManager;
import net.smartworks.server.engine.common.model.Filter;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.common.util.DateUtil;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Query;

import pro.ucity.manager.ucityWorkList.exception.UcityWorkListException;
import pro.ucity.manager.ucityWorkList.manager.IUcityWorkListManager;
import pro.ucity.manager.ucityWorkList.model.UcityWorkList;
import pro.ucity.manager.ucityWorkList.model.UcityWorkListCond;
import pro.ucity.model.Service;
import pro.ucity.model.System;
import pro.ucity.util.UcityUtil;


public class UcityWorkListManagerImpl extends AbstractManager implements IUcityWorkListManager {

	List tempList1 = new ArrayList();
	List tempList2 = new ArrayList();
	List tempList3 = new ArrayList();
	List tempList4 = new ArrayList();
	List tempList5 = new ArrayList();
	List tempList6 = new ArrayList();
	List tempList7 = new ArrayList();
	List tempList8 = new ArrayList();
	List tempList9 = new ArrayList();
	List tempList10 = new ArrayList();
	List tempList11 = new ArrayList();
	List tempList12 = new ArrayList();
	List tempList13 = new ArrayList();
	List tempList14 = new ArrayList();
	List tempList15 = new ArrayList();
	List tempList16 = new ArrayList();
	List tempList17 = new ArrayList();
	List tempList18 = new ArrayList();
	List tempList19 = new ArrayList();
	List tempList20 = new ArrayList();
	List tempList21 = new ArrayList();
	List tempList22= new ArrayList();
	List tempList23 = new ArrayList();
	List tempList24 = new ArrayList();
	List tempList25 = new ArrayList();
	
	int listNumber = 0;
	int number = 0;
	
	@Override
	public UcityWorkList getUcityWorkList(String userId, String id, String level) throws UcityWorkListException {
		try {
			if (level == null)
				level = LEVEL_ALL;
			if (level.equals(LEVEL_ALL)) {
				UcityWorkList obj = (UcityWorkList)this.get(UcityWorkList.class, id);
				return obj;
			} else {
				UcityWorkListCond cond = new UcityWorkListCond();
				cond.setObjId(id);
				return getUcityWorkList(userId, cond, level);
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw new UcityWorkListException(e);
		}
	}

	@Override
	public UcityWorkList getUcityWorkList(String userId, UcityWorkListCond cond, String level) throws UcityWorkListException {
		if (level == null)
			level = LEVEL_ALL;
		cond.setPageSize(2);
		UcityWorkList[] objs = getUcityWorkLists(userId, cond, level);
		if (CommonUtil.isEmpty(objs))
			return null;
		try {
			if (objs.length != 1)
				throw new UcityWorkListException("More than 1 Object");
		} catch (UcityWorkListException e) {
			logger.error(e, e);
			throw e;
		}
		return objs[0];
	}

	@Override
	public void setUcityWorkList(String userId, UcityWorkList obj, String level) throws UcityWorkListException {
		try {
			fill(userId, obj);
			set(obj);
		} catch (UcityWorkListException e) {
			throw e;
		} catch (Exception e) {
			throw new UcityWorkListException(e);
		}
	}

	@Override
	public void removeUcityWorkList(String userId, String id) throws UcityWorkListException {
		try {
			remove(UcityWorkList.class, id);
		} catch (Exception e) {
			logger.error(e, e);
			throw new UcityWorkListException(e);
		}
	}

	@Override
	public void removeUcityWorkList(String userId, UcityWorkListCond cond) throws UcityWorkListException {
		UcityWorkList obj = getUcityWorkList(userId, cond, null);
		if (obj == null)
			return;
		removeUcityWorkList(userId, obj.getObjId());
	}
	private Query appendQuery(StringBuffer buf, UcityWorkListCond cond) throws Exception {
		String objId = null;
		
		String prcInstId = null;
		String[] prcInstIdIns = null;
		String packageId = null;
		String status = null;
		String title = null;
		String runningTaskId = null;
		String runningTaskName = null;
		String[] runningTaskNameIns = null;

		String serviceName = null;
		String eventId = null;
		Date eventTime = null;
		String eventName = null;
		String type = null;
		String externalDisplay = null;
		String isSms = null;
		String eventPlace = null;
		
		String searchKey = null;
		
		String creationUser = null;
		Date creationDate = null;
		String modificationUser = null;
		Date modificationDate = null;

		Filter[] filters = null;
		String logicalOperator = null;
		
		if (cond != null) {
			objId = cond.getObjId();
			
			prcInstId = cond.getPrcInstId();
			prcInstIdIns = cond.getPrcInstIdIns();
			packageId = cond.getPackageId();
			status = cond.getStatus();
			title = cond.getTitle();
			runningTaskId = cond.getRunningTaskId();
			runningTaskName = cond.getRunningTaskName();
			runningTaskNameIns = cond.getRunningTaskNameIns();
			serviceName = cond.getServiceName();
			eventId = cond.getEventId();
			eventTime = cond.getEventTime();
			eventName = cond.getEventName();
			type = cond.getType();
			externalDisplay = cond.getExternalDisplay();
			isSms = cond.getIsSms();
			eventPlace = cond.getEventPlace();
			
			searchKey = cond.getSearchKey();
			
			creationUser = cond.getCreationUser();
			creationDate = cond.getCreationDate();
			modificationUser = cond.getModificationUser();
			modificationDate = cond.getModificationDate();
			
			filters = cond.getFilter();
			logicalOperator = cond.getOperator();
		}
		buf.append(" from UcityWorkList obj");
		buf.append(" where obj.objId is not null");
		Map filterMap = new HashMap();
		//TODO 시간 검색에 대한 확인 필요
		if (cond != null) {
			if (objId != null)
				buf.append(" and obj.objId = :objId");
			if (prcInstId != null)
				buf.append(" and obj.prcInstId = :prcInstId");
			if (prcInstIdIns != null && prcInstIdIns.length != 0) {
				buf.append(" and obj.prcInstId in (");
				for (int i=0; i<prcInstIdIns.length; i++) {
					if (i != 0)
						buf.append(", ");
					buf.append(":prcInstIdIn").append(i);
				}
				buf.append(")");
			}
			if (packageId != null)
				buf.append(" and obj.packageId = :packageId");
			if (status != null)
				buf.append(" and obj.status = :status");
			if (title != null)
				buf.append(" and obj.title = :title");
			if (runningTaskId != null)
				buf.append(" and obj.runningTaskId = :runningTaskId");
			if (runningTaskName != null)
				buf.append(" and obj.runningTaskName = :runningTaskName");
			if (runningTaskNameIns != null && runningTaskNameIns.length != 0) {
				buf.append(" and obj.runningTaskName in (");
				for (int i=0; i<runningTaskNameIns.length; i++) {
					if (i != 0)
						buf.append(", ");
					buf.append(":runningTaskNameIn").append(i);
				}
				buf.append(")");
			}
			if (serviceName != null)
				buf.append(" and obj.serviceName = :serviceName");
			if (eventId != null)
				buf.append(" and obj.eventId = :eventId");
			if (eventTime != null)
				buf.append(" and obj.eventTime = :eventTime");
			if (eventName != null)
				buf.append(" and obj.eventName = :eventName");
			if (type != null)
				buf.append(" and obj.type = :type");
			if (externalDisplay != null)
				buf.append(" and obj.externalDisplay = :externalDisplay");
			if (isSms != null)
				buf.append(" and obj.isSms = :isSms");
			if (eventPlace != null)
				buf.append(" and obj.eventPlace = :eventPlace");
			if (creationUser != null)
				buf.append(" and obj.creationUser = :creationUser");
			if (creationDate != null)
				buf.append(" and obj.creationDate = :creationDate");
			if (modificationUser != null)
				buf.append(" and obj.modificationUser = :modificationUser");
			if (modificationDate != null)
				buf.append(" and obj.modificationDate = :modificationDate");
			if (searchKey != null) {
				buf.append(" and (obj.status like :searchKey or obj.title like :searchKey or obj.runningTaskName like :searchKey or " +
						"obj.serviceName like :searchKey or obj.eventName like :searchKey or obj.externalDisplay like :searchKey or obj.isSms like :searchKey or obj.creationDate like :searchKey or obj.eventPlace like :searchKey )");
			}
			if (filters != null) {
				if (!CommonUtil.isEmpty(filters)) {
					if (CommonUtil.isEmpty(logicalOperator))
						logicalOperator = "and";
					String operator;
					String left;
					String right;
					String rightType;
					int i = 0;
					
					for (int j = 0; j < filters.length; j++) {
						Filter f = filters[j];
						operator = f.getOperator();
						left = f.getLeftOperandValue();
						right = f.getRightOperandValue();
						rightType = f.getRightOperandType();
						if (left == null)
							throw new Exception("left operand of filter condition is null.");
						if (operator == null) {
							operator = "=";
						} else {
							operator = operator.trim();
						}
						//left = CommonUtil.toDefault(fieldColumnMap.get(left), left);
						buf.append(CommonUtil.SPACE).append(logicalOperator);
						
						String oper1;
						String oper2;
						if (operator.equalsIgnoreCase("datein")) {
							oper1 = ">=";
							oper2 = "<=";
							right = "a" + i++;
							filterMap.put(right, f);
							buf.append(CommonUtil.SPACE).append("(" + left);
							buf.append(CommonUtil.SPACE).append(oper1);
							buf.append(CommonUtil.SPACE).append(CommonUtil.COLON).append(right);
							buf.append(CommonUtil.SPACE).append("and");
							buf.append(CommonUtil.SPACE).append(left);
							buf.append(CommonUtil.SPACE).append(oper2);
							buf.append(CommonUtil.SPACE).append(CommonUtil.COLON).append("_" + right).append(")");
						} else if (operator.equalsIgnoreCase("datenotin")) {
							oper1 = "<";
							oper2 = ">";
							right = "a" + i++;
							filterMap.put(right, f);
							buf.append(CommonUtil.SPACE).append("(" + left);
							buf.append(CommonUtil.SPACE).append(oper1);
							buf.append(CommonUtil.SPACE).append(CommonUtil.COLON).append(right);
							buf.append(CommonUtil.SPACE).append("or");
							buf.append(CommonUtil.SPACE).append(left);
							buf.append(CommonUtil.SPACE).append(oper2);
							buf.append(CommonUtil.SPACE).append(CommonUtil.COLON).append("_" + right).append(")");
						} else {
							buf.append(CommonUtil.SPACE).append(left);
							if (right == null) {
								if (operator.equals("!=") || 
										(operator.indexOf("=") == -1 && !operator.equalsIgnoreCase("is"))) {
									buf.append(" is not null");
								} else {
									buf.append(" is null");
								}
							} else {
								if (rightType == null || !rightType.equalsIgnoreCase(Filter.OPERANDTYPE_FIELD)) {
									right = "a" + i++;
									filterMap.put(right, f);
								}
								buf.append(CommonUtil.SPACE).append(operator);
								buf.append(CommonUtil.SPACE).append(CommonUtil.COLON).append(right);
							}
							
						}
					}
				}
			}
		}
		this.appendOrderQuery(buf, "obj", cond);
		
		Query query = this.createQuery(buf.toString(), cond);
		if (cond != null) {
			if (objId != null)
				query.setString("objId", objId);
			

			prcInstId = cond.getPrcInstId();
			packageId = cond.getPackageId();
			status = cond.getStatus();
			title = cond.getTitle();
			runningTaskId = cond.getRunningTaskId();
			runningTaskName = cond.getRunningTaskName();
			serviceName = cond.getServiceName();
			eventName = cond.getEventName();
			type = cond.getType();
			externalDisplay = cond.getExternalDisplay();
			isSms = cond.getIsSms();
			eventPlace = cond.getEventPlace();
			
			if (prcInstId != null)
				query.setString("prcInstId", prcInstId);
			if (prcInstIdIns != null && prcInstIdIns.length != 0) {
				for (int i=0; i<prcInstIdIns.length; i++) {
					query.setString("prcInstIdIn"+i, prcInstIdIns[i]);
				}
			}
			if (packageId != null)
				query.setString("packageId", packageId);
			if (status != null)
				query.setString("status", status);
			if (title != null)
				query.setString("title", title);
			if (runningTaskId != null)
				query.setString("runningTaskId", runningTaskId);
			if (runningTaskName != null)
				query.setString("runningTaskName", runningTaskName);
			if (runningTaskNameIns != null && runningTaskNameIns.length != 0) {
				for (int i=0; i<runningTaskNameIns.length; i++) {
					query.setString("runningTaskNameIn"+i, runningTaskNameIns[i]);
				}
			}
			if (serviceName != null)
				query.setString("serviceName", serviceName);
			if (eventId != null)
				query.setString("eventId", eventId);
			if (eventTime != null)
				query.setTimestamp("eventTime", eventTime);
			if (eventName != null)
				query.setString("eventName", eventName);
			if (type != null)
				query.setString("type", type);
			if (externalDisplay != null)
				query.setString("externalDisplay", externalDisplay);
			if (isSms != null)
				query.setString("isSms", isSms);
			if (eventPlace != null)
				query.setString("eventPlace", eventPlace);
			if (searchKey != null)
				query.setString("searchKey", CommonUtil.toLikeString(searchKey));
			if (creationUser != null)
				query.setString("creationUser", creationUser);
			if (creationDate != null)
				query.setTimestamp("creationDate", creationDate);
			if (modificationUser != null)
				query.setString("modificationUser", modificationUser);
			if (modificationDate != null)
				query.setTimestamp("modificationDate", modificationDate);
			if (filters != null) {
				if (!CommonUtil.isEmpty(filterMap)) {
					Filter f;
					String operType;
					String operValue;
					String operator;
					
					Iterator keyItr = filterMap.keySet().iterator();
					String param = null;
					while (keyItr.hasNext()) {
						param = (String)keyItr.next();
						f = (Filter)filterMap.get(param);
						operType = f.getRightOperandType();
						operator = f.getOperator();
						if (operator.equalsIgnoreCase("like")) {
							operValue = CommonUtil.toLikeString(f.getRightOperandValue());
						} else {
							operValue = f.getRightOperandValue();
						}	
						if (operType == null || operType.equalsIgnoreCase(Filter.OPERANDTYPE_STRING)) {
							query.setString(param, operValue);
						} else if (operType.equalsIgnoreCase(Filter.OPERANDTYPE_INT)) {
							query.setInteger(param, CommonUtil.toInt(operValue));
						} else if (operType.equalsIgnoreCase(Filter.OPERANDTYPE_FLOAT)) {
							query.setFloat(param, CommonUtil.toFloat(operValue));
						} else if (operType.equalsIgnoreCase(Filter.OPERANDTYPE_DATE)) {
							 if (operator.equalsIgnoreCase("datein") || operator.equalsIgnoreCase("datenotin")) {
								query.setTimestamp(param, DateUtil.toFromDate(DateUtil.toDate(operValue), DateUtil.CYCLE_DAY));
								query.setTimestamp("_" + param, DateUtil.toToDate(DateUtil.toDate(operValue), DateUtil.CYCLE_DAY));
							 } else {
								query.setTimestamp(param, DateUtil.toDate(operValue));
							 }		
						} else if (operType.equalsIgnoreCase("number")) {
							query.setDouble(param, Double.parseDouble(operValue));
						} else if (operType.equalsIgnoreCase("boolean")) {
							query.setBoolean(param, CommonUtil.toBoolean(operValue));
						} else if (operType.equalsIgnoreCase("datechooser")) {
							Date tempDate = new Date();
							tempDate.setTime(DateUtil.toDate(operValue).getTime() - TimeZone.getDefault().getRawOffset());
							query.setTimestamp(param, tempDate);
						} else {
							query.setParameter(param, operValue);
						}
					}
				}
			}
		}
		return query;
	}
	@Override
	public long getUcityWorkListSize(String userId, UcityWorkListCond cond) throws UcityWorkListException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			buf.append(" count(obj)");
			Query query = this.appendQuery(buf,cond);
			List list = query.list();
			long count = ((Long)list.get(0)).longValue();
			return count;
		} catch (Exception e) {
			logger.error(e, e);
			throw new UcityWorkListException(e);
		}
	}

	@Override
	public UcityWorkList[] getUcityWorkLists(String userId, UcityWorkListCond cond, String level) throws UcityWorkListException {
		try {
			if (level == null)
				level = LEVEL_ALL;
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			buf.append(" obj");
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			UcityWorkList[] objs = new UcityWorkList[list.size()];
			list.toArray(objs);
			return objs;
		} catch (Exception e) {
			logger.error(e, e);
			throw new UcityWorkListException(e);
		}
	}
	private String getUcityWorkListTableQueryForChart() {
		StringBuffer tableBuff = new StringBuffer();
		tableBuff.append(" select ");
		tableBuff.append("	servicename ");
		tableBuff.append("	,eventname ");
		tableBuff.append("	,eventTime ");
		tableBuff.append("	,createdtime ");
		tableBuff.append("	,title ");
		tableBuff.append("	,status ");
		tableBuff.append("	, TO_CHAR(eventTime + 9/24, 'yyyyMMdd') as eventTime_year ");
		tableBuff.append(" 	, case when TO_CHAR(eventTime + 9/24, 'HH24') between '03' and '05' then '새벽(3시~6시)'   ");
		tableBuff.append(" 		when TO_CHAR(eventTime + 9/24, 'HH24') between '06' and '10' then '아침(6시~11시)'   ");
		tableBuff.append(" 			when TO_CHAR(eventTime + 9/24, 'HH24') between '11' and '13' then '점심(11시~14시)' "); 
		tableBuff.append(" 				when TO_CHAR(eventTime + 9/24, 'HH24') between '14' and '17' then '낮(14시~18시)'  ");
		tableBuff.append(" 					when TO_CHAR(eventTime + 9/24, 'HH24') between '18' and '22' then '저녁(18시~23시)'  ");
		tableBuff.append(" 						when TO_CHAR(eventTime + 9/24, 'HH24') between '23' and '24' then '심야(23시~3시)'  ");
		tableBuff.append(" 							when TO_CHAR(eventTime + 9/24, 'HH24') between '00' and '02' then '심야(23시~3시)' ");						
		tableBuff.append(" 		end as eventTime_hour ");
		tableBuff.append("	, case when TO_CHAR(eventTime + 9/24, 'HH24') between '01' and '12' then 'A' when TO_CHAR(createdtime + 9/24, 'HH24') between '13' and '24' then 'P' end as eventTime_ampm  ");
		tableBuff.append("	, TO_CHAR(eventTime + 9/24, 'dy') || '요일' as eventTime_dy ");
		tableBuff.append("	, TO_CHAR(eventTime + 9/24, 'MM') || '월' as eventTime_month ");
		tableBuff.append("	, case when TO_CHAR(eventTime + 9/24, 'MM') between '01' and '06' then '상반기'   ");
		tableBuff.append("		when TO_CHAR(eventTime + 9/24, 'MM') between '04' and '12' then '하반기'   ");
		tableBuff.append("		end as eventTime_half  ");
		tableBuff.append("	, case when TO_CHAR(eventTime + 9/24, 'MM') between '01' and '03' then '1분기'   ");
		tableBuff.append("		when TO_CHAR(eventTime + 9/24, 'MM') between '04' and '06' then '2분기'   ");
		tableBuff.append("			when TO_CHAR(eventTime + 9/24, 'MM') between '07' and '09' then '3분기'  ");
		tableBuff.append("				when TO_CHAR(eventTime + 9/24, 'MM') between '10' and '12' then '4분기'    ");
		tableBuff.append("		end as eventTime_quarter  ");
		tableBuff.append("	, case when TO_CHAR(eventTime + 9/24, 'MM') between '03' and '05' then '봄'   ");
		tableBuff.append("		when TO_CHAR(eventTime + 9/24, 'MM') between '06' and '08' then '여름'   ");
		tableBuff.append("			when TO_CHAR(eventTime + 9/24, 'MM') between '09' and '11' then '가을'  ");
		tableBuff.append("				when TO_CHAR(eventTime + 9/24, 'MM') = '12' then '겨울'    ");
		tableBuff.append("					when TO_CHAR(eventTime + 9/24, 'MM') between '01' and '02' then '겨울'  ");
		tableBuff.append("		end as eventTime_season ");
		tableBuff.append(" from ");
		tableBuff.append(" ucityworklist ");
		return tableBuff.toString();
	}
	private String getCategoryColumnName(String categoryName) {
		if (categoryName.equalsIgnoreCase("option.category.byTime")) {
			return "eventTime_hour";
		} else if (categoryName.equalsIgnoreCase("option.category.byAmPm")) {
			return "eventTime_ampm";
		} else if (categoryName.equalsIgnoreCase("option.category.byDay")) {
			return "eventTime_dy";
		} else if (categoryName.equalsIgnoreCase("option.category.byMonth")) {
			return "eventTime_month";
		} else if (categoryName.equalsIgnoreCase("option.category.bySeason")) {
			return "eventTime_season";
		} else if (categoryName.equalsIgnoreCase("option.category.byQuarter")) {
			return "eventTime_quarter";
		} else if (categoryName.equalsIgnoreCase("option.category.byHalfYear")) {
			return "eventTime_half";
		}
		return null;
	}
	private String getPeriod(String periodName) {
		
		Calendar cal = Calendar.getInstance();
		int nowYear = cal.get(Calendar.YEAR);
		String nowMonth = cal.get(Calendar.MONTH) + "";
		String nowDay = cal.get(Calendar.DATE) + "";
		
		if (periodName.equalsIgnoreCase("option.period.thisYear")) {
			return nowYear + "0101";
		} else if (periodName.equalsIgnoreCase("option.period.recentAYear")) {
			return (nowYear - 1) + nowMonth + nowDay;
		} else if (periodName.equalsIgnoreCase("option.period.recentThreeYears")) {
			return (nowYear - 3) + nowMonth + nowDay;
		} else if (periodName.equalsIgnoreCase("option.period.recentFiveYears")) {
			return (nowYear - 5) + nowMonth + nowDay;
		}
		return null;
	}
	private List<String> getCategoryScop(String categoryName) {
		List<String> result = new ArrayList<String>();

		if (categoryName.equalsIgnoreCase("option.category.byTime")) {
			result.add("새벽(3시~6시)");
			result.add("아침(6시~11시)");
			result.add("점심(11시~14시)");
			result.add("낮(14시~18시)");
			result.add("저녁(18시~23시)");
			result.add("심야(23시~3시)");
			return result;
		} else if (categoryName.equalsIgnoreCase("option.category.byAmPm")) {
			result.add("오전(AM)");
			result.add("오후(PM)");
			return result;
		} else if (categoryName.equalsIgnoreCase("option.category.byDay")) {
			result.add("월요일");
			result.add("화요일");
			result.add("수요일");
			result.add("목요일");
			result.add("금요일");
			result.add("토요일");
			result.add("일요일");
			return result;
		} else if (categoryName.equalsIgnoreCase("option.category.byMonth")) {
			result.add("1월");
			result.add("2월");
			result.add("3월");
			result.add("4월");
			result.add("5월");
			result.add("6월");
			result.add("7월");
			result.add("8월");
			result.add("9월");
			result.add("10월");
			result.add("11월");
			result.add("12월");
			return result;
		} else if (categoryName.equalsIgnoreCase("option.category.bySeason")) {
			result.add("봄");
			result.add("여름");
			result.add("가을");
			result.add("겨울");
			return result;
		} else if (categoryName.equalsIgnoreCase("option.category.byQuarter")) {
			result.add("1분기");
			result.add("2분기");
			result.add("3분기");
			result.add("4분기");
			return result;
		} else if (categoryName.equalsIgnoreCase("option.category.byHalfYear")) {
			result.add("상반기");
			result.add("하반기");
			return result;
		}
		return null;
	}
	
	private Object[] findResultSet(List<Object[]> valueList, String group2ObjName) throws Exception {
		for(Object[] value : valueList) {
			logger.info("value[0]은? " + value[0]);
			logger.info("value[1]은? " + value[1]);
			logger.info("value[2]은? " + value[2]);
			Object value1 = value[0] == null? "null" : value[0];
			if(value1.equals(group2ObjName))
				return value;
		}
		return null;
	}
	private String getXmlDataForChart(String serviceName, String eventName, String categoryName, Map<String, Object> resultMap, List<Object> group2List) throws Exception {

		boolean in = false;
		Map<String,Object> chartMap = new HashMap<String, Object>();
		StringBuffer resultXml = new StringBuffer();
		List<String> categoryScop = getCategoryScop(categoryName);
		
		if(!serviceName.equalsIgnoreCase("option.service.all") && !eventName.equalsIgnoreCase("option.event.all")) {
			resultXml.append("<ChartData type=\"COLUMN_CHART\" dimension=\"2\">");
			if (categoryName.equalsIgnoreCase("option.category.byTime")) {
				resultXml.append("<groupingDefineName><![CDATA[").append("시간대별").append("]]></groupingDefineName>");
				resultXml.append("<valueInfoDefineUnit><![CDATA[시간대별]]></valueInfoDefineUnit>");
			} else if (categoryName.equalsIgnoreCase("option.category.byAmPm")) {
				resultXml.append("<groupingDefineName><![CDATA[").append("오전/오후").append("]]></groupingDefineName>");
				resultXml.append("<valueInfoDefineUnit><![CDATA[오전/오후]]></valueInfoDefineUnit>");
			} else if (categoryName.equalsIgnoreCase("option.category.byDay")) {
				resultXml.append("<groupingDefineName><![CDATA[").append("요일별").append("]]></groupingDefineName>");
				resultXml.append("<valueInfoDefineUnit><![CDATA[요일별]]></valueInfoDefineUnit>");
			} else if (categoryName.equalsIgnoreCase("option.category.byMonth")) {
				resultXml.append("<groupingDefineName><![CDATA[").append("월별").append("]]></groupingDefineName>");
				resultXml.append("<valueInfoDefineUnit><![CDATA[월별]]></valueInfoDefineUnit>");
			} else if (categoryName.equalsIgnoreCase("option.category.bySeason")) {
				resultXml.append("<groupingDefineName><![CDATA[").append("계절별").append("]]></groupingDefineName>");
				resultXml.append("<valueInfoDefineUnit><![CDATA[계절별]]></valueInfoDefineUnit>");
			} else if (categoryName.equalsIgnoreCase("option.category.byQuarter")) {
				resultXml.append("<groupingDefineName><![CDATA[").append("분기별").append("]]></groupingDefineName>");
				resultXml.append("<valueInfoDefineUnit><![CDATA[분기별]]></valueInfoDefineUnit>");
			} else if (categoryName.equalsIgnoreCase("option.category.byHalfYear")) {
				resultXml.append("<groupingDefineName><![CDATA[").append("반기별").append("]]></groupingDefineName>");
				resultXml.append("<valueInfoDefineUnit><![CDATA[반기별]]></valueInfoDefineUnit>");
			}
			resultXml.append("<valueInfoDefineName><![CDATA[발생건수]]></valueInfoDefineName>");
			resultXml.append("<valueInfoDefineUnit><![CDATA[건]]></valueInfoDefineUnit>");
			
			for (int i = 0; i < categoryScop.size(); i++) {
				resultXml.append("<grouping>");
				resultXml.append("<name><![CDATA[").append(categoryScop.get(i)).append("]]></name>");
				resultXml.append("<value><![CDATA[").append(CommonUtil.toDefault((BigDecimal)resultMap.get(categoryScop.get(i)) + "", "0")).append("]]></value>");
				resultXml.append("</grouping>");
				logger.info(categoryScop.get(i));
			}
			resultXml.append("</ChartData>");
		}else{
			resultXml.append("<ChartData type=\"COLUMN_CHART\" dimension=\"3\">");
			if (categoryName.equalsIgnoreCase("option.category.byTime")) {
				resultXml.append("<groupingDefineName><![CDATA[").append("시간대별").append("]]></groupingDefineName>");
				resultXml.append("<valueInfoDefineUnit><![CDATA[시간대별]]></valueInfoDefineUnit>");
			} else if (categoryName.equalsIgnoreCase("option.category.byAmPm")) {
				resultXml.append("<groupingDefineName><![CDATA[").append("오전/오후").append("]]></groupingDefineName>");
				resultXml.append("<valueInfoDefineUnit><![CDATA[오전/오후]]></valueInfoDefineUnit>");
			} else if (categoryName.equalsIgnoreCase("option.category.byDay")) {
				resultXml.append("<groupingDefineName><![CDATA[").append("요일별").append("]]></groupingDefineName>");
				resultXml.append("<valueInfoDefineUnit><![CDATA[요일별]]></valueInfoDefineUnit>");
			} else if (categoryName.equalsIgnoreCase("option.category.byMonth")) {
				resultXml.append("<groupingDefineName><![CDATA[").append("월별").append("]]></groupingDefineName>");
				resultXml.append("<valueInfoDefineUnit><![CDATA[월별]]></valueInfoDefineUnit>");
			} else if (categoryName.equalsIgnoreCase("option.category.bySeason")) {
				resultXml.append("<groupingDefineName><![CDATA[").append("계절별").append("]]></groupingDefineName>");
				resultXml.append("<valueInfoDefineUnit><![CDATA[계절별]]></valueInfoDefineUnit>");
			} else if (categoryName.equalsIgnoreCase("option.category.byQuarter")) {
				resultXml.append("<groupingDefineName><![CDATA[").append("분기별").append("]]></groupingDefineName>");
				resultXml.append("<valueInfoDefineUnit><![CDATA[분기별]]></valueInfoDefineUnit>");
			} else if (categoryName.equalsIgnoreCase("option.category.byHalfYear")) {
				resultXml.append("<groupingDefineName><![CDATA[").append("반기별").append("]]></groupingDefineName>");
				resultXml.append("<valueInfoDefineUnit><![CDATA[반기별]]></valueInfoDefineUnit>");
			}
			resultXml.append("<valueInfoDefineName><![CDATA[발생건수]]></valueInfoDefineName>");
			resultXml.append("<valueInfoDefineUnit><![CDATA[건]]></valueInfoDefineUnit>");
			
			Iterator<String> keyIt = resultMap.keySet().iterator();
			logger.info("키잇은 멀까??" + keyIt);
			
			while(keyIt.hasNext()) {
				int k = 0;
				in = false;
				String groupName = (String)keyIt.next();
				List<Object[]> valueList = (List)resultMap.get(groupName);
				
				resultXml.append("<grouping>");
				resultXml.append("<name><![CDATA[").append(groupName).append("]]></name>");
				for(int i = 0 ; i < group2List.size() ; i++) {    // 그룹리스트 만큼 돔 ( 환경, 교통, 5번 돌겠지)
					Object group2Obj = group2List.get(i);         // 0 이니까 아마 환경?
					logger.info("투스트링!!" + group2Obj.toString());
					Object[] resultSet = findResultSet(valueList, group2Obj.toString());		
					if(resultSet == null) {
						 logger.info("result는 null");
						resultXml.append("<remark>");
						resultXml.append("<name><![CDATA[").append(group2Obj.toString()).append("]]></name>");
						resultXml.append("<value><![CDATA[0]]></value>");
						resultXml.append("</remark>");	
					} else {
						logger.info("result값이 있음");
						resultXml.append("<remark>");
						resultXml.append("<name><![CDATA[").append(resultSet[0]).append("]]></name>");
						resultXml.append("<value><![CDATA[").append((BigDecimal)resultSet[2]).append("]]></value>");
						resultXml.append("</remark>");
					}								
				}				
				resultXml.append("</grouping>");
			}
			if (categoryName.equalsIgnoreCase("option.category.byTime")) {
				resultXml.append("<remarkDefineName><![CDATA[").append("시간대 별").append("]]></remarkDefineName>");
			} else if (categoryName.equalsIgnoreCase("option.category.byAmPm")) {
				resultXml.append("<remarkDefineName><![CDATA[").append("오전/오후").append("]]></remarkDefineName>");
			} else if (categoryName.equalsIgnoreCase("option.category.byDay")) {
				resultXml.append("<remarkDefineName><![CDATA[").append("요일일별").append("]]></remarkDefineName>");
			} else if (categoryName.equalsIgnoreCase("option.category.byMonth")) {
				resultXml.append("<remarkDefineName><![CDATA[").append("월별").append("]]></remarkDefineName>");
			} else if (categoryName.equalsIgnoreCase("option.category.bySeason")) {
				resultXml.append("<remarkDefineName><![CDATA[").append("계절별").append("]]></remarkDefineName>");
			} else if (categoryName.equalsIgnoreCase("option.category.byQuarter")) {
				resultXml.append("<remarkDefineName><![CDATA[").append("분기별").append("]]></remarkDefineName>");
			} else if (categoryName.equalsIgnoreCase("option.category.byHalfYear")) {
				resultXml.append("<remarkDefineName><![CDATA[").append("반기별").append("]]></remarkDefineName>");
			}
			resultXml.append("</ChartData>");
		}
		return resultXml.toString();
	}
	public String getUcityChartXml(String categoryName, String periodName, String serviceName, String eventName) throws Exception {
		StringBuffer chartQuery = new StringBuffer();
		//모든 서비스와 모든 이벤트일 때 Select 달라짐.
		if(!serviceName.equalsIgnoreCase("option.service.all") && !eventName.equalsIgnoreCase("option.event.all")) {
			chartQuery.append("select tbl.").append(getCategoryColumnName(categoryName)).append(", count(*) as count from (");
		}else if(serviceName.equalsIgnoreCase("option.service.all")){
			chartQuery.append("select tbl.serviceName").append(", tbl.").append(getCategoryColumnName(categoryName)).append(", count(*) as count from (");
		}else if(eventName.equalsIgnoreCase("option.event.all")){
			chartQuery.append("select tbl.eventName").append(", tbl.").append(getCategoryColumnName(categoryName)).append(", count(*) as count from (");
		}
		chartQuery.append(getUcityWorkListTableQueryForChart());
		chartQuery.append(") tbl where 1=1 ");
		if(!serviceName.equalsIgnoreCase("option.service.all") || !eventName.equalsIgnoreCase("option.event.all")) {
			if (!serviceName.equalsIgnoreCase("option.service.all")) {
				chartQuery.append(" and tbl.serviceName = '").append(serviceName).append("' ");
			}
			if (!eventName.equalsIgnoreCase("option.event.all")) {
				chartQuery.append(" and tbl.eventName = '").append(eventName).append("' ");
			}
		}
		if (!periodName.equalsIgnoreCase("option.period.all")) {
			chartQuery.append("and tbl.eventTime_year >= '").append(getPeriod(periodName)).append("' ");
		}
		//위랑 동일.
		if(!serviceName.equalsIgnoreCase("option.service.all") && !eventName.equalsIgnoreCase("option.event.all")) {
			chartQuery.append(" group by tbl.").append(getCategoryColumnName(categoryName)).append(" order by tbl.").append(getCategoryColumnName(categoryName));
		}else if(serviceName.equalsIgnoreCase("option.service.all")){
			chartQuery.append(" group by tbl.serviceName").append(", tbl.").append(getCategoryColumnName(categoryName)).append(" order by tbl.").append(getCategoryColumnName(categoryName));
		}else if(eventName.equalsIgnoreCase("option.event.all")){
			chartQuery.append(" group by tbl.eventName").append(", tbl.").append(getCategoryColumnName(categoryName)).append(" order by tbl.").append(getCategoryColumnName(categoryName));
		}
		
		Query query = this.getSession().createSQLQuery(chartQuery.toString());
		
		List list = query.list();
		
	    int groupByCount = 0;
		Map<String,Object> resultMap = new HashMap<String, Object>();   //resultMap이 group1Map임
		Map<String,Object> resultMap2 = new HashMap<String, Object>();
		
		List<Object> group2List = new ArrayList<Object>();
		List<Object> group3List = new ArrayList<Object>();
		List<Object> valueList2 = new ArrayList<Object>();
		
		if(list != null && !list.isEmpty()) {
			
			String groupNames = null;
			String groupName = null;
			Object groupValue = null;
			Object[] groupLists = null;
			
			for (Iterator itr = list.iterator(); itr.hasNext();) {
				Object[] fields = (Object[]) itr.next();
				
				if(groupByCount == 0){
					groupByCount = fields.length - 1;

					//카운트가 1이면 2차원 차트 생성함.
					if(groupByCount == 1) {
						groupName = fields[0] == null? "null" : fields[0] instanceof Character ? ((Character)fields[0]).toString() : ((String)fields[0]).toString();
						groupValue = fields[1] == null? "null" : (BigDecimal)fields[1];
						
						if (fields[0] instanceof Character) {
							if(groupName.toString().equalsIgnoreCase("상")) {
								resultMap.put("상반기", groupValue);
							} else if (groupName.toString().equalsIgnoreCase("하")) {
								resultMap.put("하반기", groupValue);
							} else if (groupName.toString().equalsIgnoreCase("1")) {
								resultMap.put("1분기", groupValue);
							} else if (groupName.toString().equalsIgnoreCase("2")) {
								resultMap.put("2분기", groupValue);
							} else if (groupName.toString().equalsIgnoreCase("3")) {
								resultMap.put("3분기", groupValue);
							} else if (groupName.toString().equalsIgnoreCase("4")) {
								resultMap.put("4분기", groupValue);
							} else if (groupName.toString().equalsIgnoreCase("A")) {
								resultMap.put("오전(AM)", groupValue);
							} else if (groupName.toString().equalsIgnoreCase("P")) {
								resultMap.put("오후(PM)", groupValue);
							}
							
						} else if(fields[0] instanceof String){
							if(groupName.toString().equalsIgnoreCase("sun요일")) {
								resultMap.put("일요일", groupValue);
							} else if (groupName.toString().equalsIgnoreCase("mon요일")) {
								resultMap.put("월요일", groupValue);
							} else if (groupName.toString().equalsIgnoreCase("tue요일")) {
								resultMap.put("화요일", groupValue);
							} else if (groupName.toString().equalsIgnoreCase("wed요일")) {
								resultMap.put("수요일", groupValue);
							} else if (groupName.toString().equalsIgnoreCase("thu요일")) {
								resultMap.put("목요일", groupValue);
							} else if (groupName.toString().equalsIgnoreCase("fri요일")) {
								resultMap.put("금요일", groupValue);
							} else if (groupName.toString().equalsIgnoreCase("sat요일")) {
								resultMap.put("토요일", groupValue);
							}else{
								resultMap.put(groupName, groupValue);
							}
						}
					}
				}else if(groupByCount == 2) {
					groupNames = fields[0] == null? "null" : fields[0].toString();
					groupName = fields[1] == null? "null" : fields[1] instanceof Character ? ((Character)fields[1]).toString() : ((String)fields[1]).toString();
					
					if (fields[1] instanceof Character) {
						if(groupName.toString().equalsIgnoreCase("상")) {
							groupName = "상반기";
						} else if (groupName.toString().equalsIgnoreCase("하")) {
							groupName = "하반기";
						} else if (groupName.toString().equalsIgnoreCase("1")) {
							groupName = "1분기";
						} else if (groupName.toString().equalsIgnoreCase("2")) {
							groupName = "2분기";
						} else if (groupName.toString().equalsIgnoreCase("3")) {
							groupName = "3분기";
						} else if (groupName.toString().equalsIgnoreCase("4")) {
							groupName = "4분기";
						} else if (groupName.toString().equalsIgnoreCase("A")) {
							groupName = "오전(AM)";
						} else if (groupName.toString().equalsIgnoreCase("P")) {
							groupName = "오후(PM)";
						}		
					} else if(fields[1] instanceof String){
						if(groupName.toString().equalsIgnoreCase("sun요일")) {
							groupName = "일요일";
						} else if (groupName.toString().equalsIgnoreCase("mon요일")) {
							groupName = "월요일";
						} else if (groupName.toString().equalsIgnoreCase("tue요일")) {
							groupName = "화요일";
						} else if (groupName.toString().equalsIgnoreCase("wed요일")) {
							groupName = "수요일";
						} else if (groupName.toString().equalsIgnoreCase("thu요일")) {
							groupName = "목요일";
						} else if (groupName.toString().equalsIgnoreCase("fri요일")) {
							groupName = "금요일";
						} else if (groupName.toString().equalsIgnoreCase("sat요일")) {
							groupName = "토요일";
						} else {
							groupName = ((String)fields[1]).toString();
						}
					}
					List<Object[]> valueList = null;
					if(resultMap.containsKey(groupName)) {          // 만든 resultMap에서 해당 groupNames(환경,방범)가 잇으면 불러옴.
						valueList = (List)resultMap.get(groupName);  // valueList는 만든 resultMap에 있는 주소,
					} else {
						valueList = new ArrayList<Object[]>();
						resultMap.put(groupName, valueList);        // 없으면 valueList를 새로 만들어, resultMap을 만듬.
					}
					valueList.add(fields);
					if(!group2List.contains(groupNames)) // 용도
						group2List.add(groupNames);
				}
			}	
		}else if (list == null || list.isEmpty())
			return getXmlDataForChart(serviceName, eventName, categoryName, resultMap, group2List);
		return getXmlDataForChart(serviceName, eventName, categoryName, resultMap, group2List);
	}
	
	//Excel Download 구현 ( jxls 라이브러리가 한글 변수를 인식 못하여, 영어변수로 재정의함. )
	public void getUcityChartExcel(String categoryName, String periodName, String serviceName, String eventName, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		BigDecimal value = new BigDecimal(0);
		StringBuffer chartQuery = new StringBuffer();
		
		//모든 서비스와 모든 이벤트일 때 Select 달라짐.
		if(!serviceName.equalsIgnoreCase("option.service.all") && !eventName.equalsIgnoreCase("option.event.all")) {
			chartQuery.append("select tbl.").append(getCategoryColumnName(categoryName)).append(", count(*) as count from (");
		}else if(serviceName.equalsIgnoreCase("option.service.all")){
			chartQuery.append("select tbl.serviceName").append(", tbl.").append(getCategoryColumnName(categoryName)).append(", count(*) as count from (");
		}else if(eventName.equalsIgnoreCase("option.event.all")){
			chartQuery.append("select tbl.eventName").append(", tbl.").append(getCategoryColumnName(categoryName)).append(", count(*) as count from (");
		}

		chartQuery.append(getUcityWorkListTableQueryForChart());
		chartQuery.append(") tbl where 1=1 ");
		if(!serviceName.equalsIgnoreCase("option.service.all") || !eventName.equalsIgnoreCase("option.event.all")) {
			if (!serviceName.equalsIgnoreCase("option.service.all")) {
				chartQuery.append(" and tbl.serviceName = '").append(serviceName).append("' ");
			}
			if (!eventName.equalsIgnoreCase("option.event.all")) {
				chartQuery.append(" and tbl.eventName = '").append(eventName).append("' ");
			}
		}
		if (!periodName.equalsIgnoreCase("option.period.all")) {
			chartQuery.append("and tbl.eventTime_year >= '").append(getPeriod(periodName)).append("' ");
		}
		if(!serviceName.equalsIgnoreCase("option.service.all") && !eventName.equalsIgnoreCase("option.event.all")) {
			chartQuery.append(" group by tbl.").append(getCategoryColumnName(categoryName)).append(" order by tbl.").append(getCategoryColumnName(categoryName));
		}else if(serviceName.equalsIgnoreCase("option.service.all")){
			chartQuery.append(" group by tbl.serviceName").append(", tbl.").append(getCategoryColumnName(categoryName)).append(" order by tbl.serviceName");
		}else if(eventName.equalsIgnoreCase("option.event.all")){
			chartQuery.append(" group by tbl.eventName").append(", tbl.").append(getCategoryColumnName(categoryName)).append(" order by tbl.eventName");
		}

		Query query = this.getSession().createSQLQuery(chartQuery.toString());
		
		List list = query.list();
		List<Map<String, Object>> dataValue = new ArrayList<Map<String,Object>>();		
		
		Map beans = new HashMap();
		Map<String,Object> resultMap = new HashMap<String, Object>();
		if (list == null || list.isEmpty())
			getXmlDataForExcel(dataValue, serviceName, eventName, categoryName, periodName, resultMap, request, response);
			for (Iterator itr = list.iterator(); itr.hasNext();) {
				Object[] fields = (Object[]) itr.next();
				if(!serviceName.equalsIgnoreCase("option.service.all") || !eventName.equalsIgnoreCase("option.event.all")){
					if (fields[0] instanceof Character) {
						if(((Character)fields[0]).toString().equalsIgnoreCase("상")) {
							resultMap.put("firstHalf", (BigDecimal)fields[1]);
						} else if (((Character)fields[0]).toString().equalsIgnoreCase("하")) {
							resultMap.put("secondHalf", (BigDecimal)fields[1]);
						} else if (((Character)fields[0]).toString().equalsIgnoreCase("1")) {
							resultMap.put("oQuarter", (BigDecimal)fields[1]);
						} else if (((Character)fields[0]).toString().equalsIgnoreCase("2")) {
							resultMap.put("sQuarter", (BigDecimal)fields[1]);
						} else if (((Character)fields[0]).toString().equalsIgnoreCase("3")) {
							resultMap.put("tQuarter", (BigDecimal)fields[1]);
						} else if (((Character)fields[0]).toString().equalsIgnoreCase("4")) {
							resultMap.put("fQuarter", (BigDecimal)fields[1]);
						} else if (((Character)fields[0]).toString().equalsIgnoreCase("A")) {
							resultMap.put("am", (BigDecimal)fields[1]);
						} else if (((Character)fields[0]).toString().equalsIgnoreCase("P")) {
							resultMap.put("pm", (BigDecimal)fields[1]);
						}
						
					} else if(fields[0] instanceof String){
						if(((String)fields[0]).toString().equalsIgnoreCase("sun요일")) {
							resultMap.put("sun", (BigDecimal)fields[1]);
						} else if (((String)fields[0]).toString().equalsIgnoreCase("mon요일")) {
							resultMap.put("mon", (BigDecimal)fields[1]);
						} else if (((String)fields[0]).toString().equalsIgnoreCase("tue요일")) {
							resultMap.put("tue", (BigDecimal)fields[1]);
						} else if (((String)fields[0]).toString().equalsIgnoreCase("wed요일")) {
							resultMap.put("wed", (BigDecimal)fields[1]);
						} else if (((String)fields[0]).toString().equalsIgnoreCase("thu요일")) {
							resultMap.put("thu", (BigDecimal)fields[1]);
						} else if (((String)fields[0]).toString().equalsIgnoreCase("fri요일")) {
							resultMap.put("fri", (BigDecimal)fields[1]);
						} else if (((String)fields[0]).toString().equalsIgnoreCase("sat요일")) {
							resultMap.put("sat", (BigDecimal)fields[1]);
						} else if (((String)fields[0]).toString().equalsIgnoreCase("새벽(3시~6시)")) {
							if((BigDecimal)fields[1] == null || ((BigDecimal)fields[1]).equals(0)){
								resultMap.put("dawn", value);
							}else{
								resultMap.put("dawn", (BigDecimal)fields[1]);
							}
						} else if (((String)fields[0]).toString().equalsIgnoreCase("아침(6시~11시)")) {
							if((BigDecimal)fields[1] == null || "".equals((BigDecimal)fields[1])){
								resultMap.put("morning", value);
							}else{
								resultMap.put("morning", (BigDecimal)fields[1]);
							}
						} else if (((String)fields[0]).toString().equalsIgnoreCase("점심(11시~14시)")) {
							if((BigDecimal)fields[1] == null || "".equals((BigDecimal)fields[1])){
								resultMap.put("lunch", value);
							}else{
								resultMap.put("lunch", (BigDecimal)fields[1]);
							}
						} else if (((String)fields[0]).toString().equalsIgnoreCase("낮(14시~18시)")) {
							if((BigDecimal)fields[1] == null || "".equals((BigDecimal)fields[1])){
								resultMap.put("day", value);
							}else{
								resultMap.put("day", (BigDecimal)fields[1]);
							}
						} else if (((String)fields[0]).toString().equalsIgnoreCase("저녁(18시~23시)")) {
							if((BigDecimal)fields[1] == null || "".equals((BigDecimal)fields[1])){
								resultMap.put("evening", value);
							}else{
								resultMap.put("evening", (BigDecimal)fields[1]);
							}
						} else if (((String)fields[0]).toString().equalsIgnoreCase("심야(23시~3시)")) {
							if((BigDecimal)fields[1] == null || "".equals((BigDecimal)fields[1])){
								resultMap.put("night", value);
							}else{
								resultMap.put("night", (BigDecimal)fields[1]);
							}
						} else if (((String)fields[0]).toString().equalsIgnoreCase("1월")) {
							resultMap.put("jan", (BigDecimal)fields[1]);
						} else if (((String)fields[0]).toString().equalsIgnoreCase("2월")) {
							resultMap.put("feb", (BigDecimal)fields[1]);
						} else if (((String)fields[0]).toString().equalsIgnoreCase("3월")) {
							resultMap.put("mar", (BigDecimal)fields[1]);
						} else if (((String)fields[0]).toString().equalsIgnoreCase("4월")) {
							resultMap.put("apr", (BigDecimal)fields[1]);
						} else if (((String)fields[0]).toString().equalsIgnoreCase("5월")) {
							resultMap.put("may", (BigDecimal)fields[1]);
						} else if (((String)fields[0]).toString().equalsIgnoreCase("6월")) {
							resultMap.put("jun", (BigDecimal)fields[1]);
						} else if (((String)fields[0]).toString().equalsIgnoreCase("7월")) {
							resultMap.put("jul", (BigDecimal)fields[1]);
						} else if (((String)fields[0]).toString().equalsIgnoreCase("8월")) {
							resultMap.put("aug", (BigDecimal)fields[1]);
						} else if (((String)fields[0]).toString().equalsIgnoreCase("9월")) {
							resultMap.put("sep", (BigDecimal)fields[1]);
						} else if (((String)fields[0]).toString().equalsIgnoreCase("10월")) {
							resultMap.put("oct", (BigDecimal)fields[1]);
						} else if (((String)fields[0]).toString().equalsIgnoreCase("11월")) {
							resultMap.put("nov", (BigDecimal)fields[1]);
						} else if (((String)fields[0]).toString().equalsIgnoreCase("12월")) {
							resultMap.put("dec", (BigDecimal)fields[1]);
						} else if (((String)fields[0]).toString().equalsIgnoreCase("봄")) {
							resultMap.put("spring", (BigDecimal)fields[1]);
						} else if (((String)fields[0]).toString().equalsIgnoreCase("여름")) {
							resultMap.put("summer", (BigDecimal)fields[1]);
						} else if (((String)fields[0]).toString().equalsIgnoreCase("가을")) {
							resultMap.put("fall", (BigDecimal)fields[1]);
						} else if (((String)fields[0]).toString().equalsIgnoreCase("겨울")) {
							resultMap.put("winter", (BigDecimal)fields[1]);
						}
					}
				}else{
					//allServiceName이거나,allEventName일때.
					if(categoryName.equalsIgnoreCase(System.REPORT_OPTION_CATEGORY_BY_TIME)){
						if(((String)fields[1]).toString().equalsIgnoreCase("새벽(3시~6시)")){
							getTempList((String)fields[0],(BigDecimal)fields[2],0,6);
						}else if(((String)fields[1]).toString().equalsIgnoreCase("아침(6시~11시)")){
							getTempList((String)fields[0],(BigDecimal)fields[2],1,6);
						}else if(((String)fields[1]).toString().equalsIgnoreCase("점심(11시~14시)")){
							getTempList((String)fields[0],(BigDecimal)fields[2],2,6);
						}else if(((String)fields[1]).toString().equalsIgnoreCase("낮(14시~18시)")){
							getTempList((String)fields[0],(BigDecimal)fields[2],3,6);
						}else if(((String)fields[1]).toString().equalsIgnoreCase("저녁(18시~23시)")){
							getTempList((String)fields[0],(BigDecimal)fields[2],4,6);
						}else if(((String)fields[1]).toString().equalsIgnoreCase("심야(23시~3시)")){
							getTempList((String)fields[0],(BigDecimal)fields[2],5,6);
						}
						
					}else if(categoryName.equalsIgnoreCase(System.REPORT_OPTION_CATEGORY_BY_AMPM)){
						if(((Character)fields[1]).toString().equalsIgnoreCase("A")){
							getTempList((String)fields[0],(BigDecimal)fields[2],0,2);
						}else if(((Character)fields[1]).toString().equalsIgnoreCase("P")){
							getTempList((String)fields[0],(BigDecimal)fields[2],1,2);
						}
					}else if(categoryName.equalsIgnoreCase(System.REPORT_OPTION_CATEGORY_BY_DAY)){
						if(((String)fields[1]).toString().equalsIgnoreCase("sun요일")) {
							getTempList((String)fields[0],(BigDecimal)fields[2],6,7);
						} else if (((String)fields[1]).toString().equalsIgnoreCase("mon요일")) {
							getTempList((String)fields[0],(BigDecimal)fields[2],0,7);
						} else if (((String)fields[1]).toString().equalsIgnoreCase("tue요일")) {
							getTempList((String)fields[0],(BigDecimal)fields[2],1,7);
						} else if (((String)fields[1]).toString().equalsIgnoreCase("wed요일")) {
							getTempList((String)fields[0],(BigDecimal)fields[2],2,7);
						} else if (((String)fields[1]).toString().equalsIgnoreCase("thu요일")) {
							getTempList((String)fields[0],(BigDecimal)fields[2],3,7);
						} else if (((String)fields[1]).toString().equalsIgnoreCase("fri요일")) {
							getTempList((String)fields[0],(BigDecimal)fields[2],4,7);
						} else if (((String)fields[1]).toString().equalsIgnoreCase("sat요일")) {
							getTempList((String)fields[0],(BigDecimal)fields[2],5,7);
						}
					}else if(categoryName.equalsIgnoreCase(System.REPORT_OPTION_CATEGORY_BY_MONTH)){
						if (((String)fields[1]).toString().equalsIgnoreCase("1월")) {
							getTempList((String)fields[0],(BigDecimal)fields[2],0,12);
						} else if (((String)fields[1]).toString().equalsIgnoreCase("2월")) {
							getTempList((String)fields[0],(BigDecimal)fields[2],1,12);
						} else if (((String)fields[1]).toString().equalsIgnoreCase("3월")) {
							getTempList((String)fields[0],(BigDecimal)fields[2],2,12);
						} else if (((String)fields[1]).toString().equalsIgnoreCase("4월")) {
							getTempList((String)fields[0],(BigDecimal)fields[2],3,12);
						} else if (((String)fields[1]).toString().equalsIgnoreCase("5월")) {
							getTempList((String)fields[0],(BigDecimal)fields[2],4,12);
						} else if (((String)fields[1]).toString().equalsIgnoreCase("6월")) {
							getTempList((String)fields[0],(BigDecimal)fields[2],5,12);
						} else if (((String)fields[1]).toString().equalsIgnoreCase("7월")) {
							getTempList((String)fields[0],(BigDecimal)fields[2],6,12);
						} else if (((String)fields[1]).toString().equalsIgnoreCase("8월")) {
							getTempList((String)fields[0],(BigDecimal)fields[2],7,12);
						} else if (((String)fields[1]).toString().equalsIgnoreCase("9월")) {
							getTempList((String)fields[0],(BigDecimal)fields[2],8,12);
						} else if (((String)fields[1]).toString().equalsIgnoreCase("10월")) {
							getTempList((String)fields[0],(BigDecimal)fields[2],9,12);
						} else if (((String)fields[1]).toString().equalsIgnoreCase("11월")) {
							getTempList((String)fields[0],(BigDecimal)fields[2],10,12);
						} else if (((String)fields[1]).toString().equalsIgnoreCase("12월")) {
							getTempList((String)fields[0],(BigDecimal)fields[2],11,12);
						}
					}else if(categoryName.equalsIgnoreCase(System.REPORT_OPTION_CATEGORY_BY_SEASON)){
						if (((String)fields[1]).toString().equalsIgnoreCase("봄")) {
							getTempList((String)fields[0],(BigDecimal)fields[2],0,4);
						} else if (((String)fields[1]).toString().equalsIgnoreCase("여름")) {
							getTempList((String)fields[0],(BigDecimal)fields[2],1,4);
						} else if (((String)fields[1]).toString().equalsIgnoreCase("가을")) {
							getTempList((String)fields[0],(BigDecimal)fields[2],2,4);
						} else if (((String)fields[1]).toString().equalsIgnoreCase("겨울")) {
							getTempList((String)fields[0],(BigDecimal)fields[2],3,4);
						}
					}else if(categoryName.equalsIgnoreCase(System.REPORT_OPTION_CATEGORY_BY_QUARTER)){
						if (((Character)fields[1]).toString().equalsIgnoreCase("1")) {
							getTempList((String)fields[0],(BigDecimal)fields[2],0,4);
						} else if (((Character)fields[1]).toString().equalsIgnoreCase("2")) {
							getTempList((String)fields[0],(BigDecimal)fields[2],1,4);
						} else if (((Character)fields[1]).toString().equalsIgnoreCase("3")) {
							getTempList((String)fields[0],(BigDecimal)fields[2],2,4);
						} else if (((Character)fields[1]).toString().equalsIgnoreCase("4")) {
							getTempList((String)fields[0],(BigDecimal)fields[2],3,4);
						}
					}else if(categoryName.equalsIgnoreCase(System.REPORT_OPTION_CATEGORY_BY_HALFYEAR)){
						if(((Character)fields[1]).toString().equalsIgnoreCase("상")) {
							getTempList((String)fields[0],(BigDecimal)fields[2],0,2);
						} else if (((Character)fields[1]).toString().equalsIgnoreCase("하")) {
							getTempList((String)fields[0],(BigDecimal)fields[2],1,2);
						}
					}
				}
			}
			List att = getCategoryScop(categoryName);
			resultMap.put("att", att );
			
			resultMap.put("env", tempList1);
			resultMap.put("traffic", tempList2);
			resultMap.put("crime", tempList3);
			resultMap.put("facility", tempList4);
			resultMap.put("form", tempList5);
			resultMap.put("env1", tempList6);
			resultMap.put("env2", tempList7);
			resultMap.put("env3", tempList8);
			resultMap.put("traffic1", tempList9);
			resultMap.put("traffic2", tempList10);
			resultMap.put("traffic3", tempList11);
			resultMap.put("traffic4", tempList12);
			resultMap.put("crime1", tempList13);
			resultMap.put("crime2", tempList14);
			resultMap.put("crime3", tempList15);
			resultMap.put("crime4", tempList16);
			resultMap.put("crime5", tempList17);
			resultMap.put("crime6", tempList18);
			resultMap.put("crime7", tempList19);
			resultMap.put("crime8", tempList20);
			resultMap.put("crime9", tempList21);
			resultMap.put("crime10", tempList22);
			resultMap.put("facility1", tempList23);
			resultMap.put("facility2", tempList24);
			resultMap.put("form1", tempList25);
				
			dataValue.add(resultMap);
			getXmlDataForExcel(dataValue, serviceName, eventName, categoryName, periodName, resultMap, request, response);
	}
	private void getTempList(String column,BigDecimal value ,int number, int size) {
		// TODO Auto-generated method stub
		int i;
		if ((column).toString().equalsIgnoreCase("환경")) {
			if(tempList1.size() == 0){
				for(i = 0;i < size; i++){
					tempList1.add(new BigDecimal(0));
				}
			}
			tempList1.add(number,value);
			tempList1.remove(number+1);
			listNumber = 1;
		}else if ((column).toString().equalsIgnoreCase("교통")) {
			if(tempList2.size() == 0){
				for(i = 0;i < size; i++){
					tempList2.add(new BigDecimal(0));
				}
			}
			tempList2.add(number,value);
			tempList2.remove(number+1);
			listNumber = 2;
		}else if ((column).toString().equalsIgnoreCase("방범/방재")) {
			if(tempList3.size() == 0){
				for(i = 0;i < size; i++){
					tempList3.add(new BigDecimal(0));
				}
			}
			tempList3.add(number,value);
			tempList3.remove(number+1);
			listNumber = 3;
		} else if ((column).toString().equalsIgnoreCase("시설물관리")) {
			if(tempList4.size() == 0){
				for(i = 0;i < size; i++){
					tempList4.add(new BigDecimal(0));
				}
			}
			tempList4.add(number,value);
			tempList4.remove(number+1);
			listNumber = 4;
		} else if ((column).toString().equalsIgnoreCase("플랫폼")) {
			if(tempList5.size() == 0){
				for(i = 0;i < size; i++){
					tempList5.add(new BigDecimal(0));
				}
			}
			tempList5.add(number,value);
			tempList5.remove(number+1);
			listNumber = 5;
		} else if ((column).toString().equalsIgnoreCase("대기오염")) {
			if(tempList6.size() == 0){
				for(i = 0;i < size; i++){
					tempList6.add(new BigDecimal(0));
				}
			}
			tempList6.add(number,value);
			tempList6.remove(number+1);
			listNumber = 6;
		} else if ((column).toString().equalsIgnoreCase("환경경보")) {
			if(tempList7.size() == 0){
				for(i = 0;i < size; i++){
					tempList7.add(new BigDecimal(0));
				}
			}
			tempList7.add(number,value);
			tempList7.remove(number+1);
			listNumber = 7;
		} else if ((column).toString().equalsIgnoreCase("수질")) {
			if(tempList8.size() == 0){
				for(i = 0;i < size; i++){
					tempList8.add(new BigDecimal(0));
				}
			}
			tempList8.add(number,value);
			tempList8.remove(number+1);
			listNumber = 8;
		} else if ((column).toString().equalsIgnoreCase("교통혼잡")) {
			if(tempList9.size() == 0){
				for(i = 0;i < size; i++){
					tempList9.add(new BigDecimal(0));
				}
			}
			tempList9.add(number,value);
			tempList9.remove(number+1);
			listNumber = 9;
		} else if ((column).toString().equalsIgnoreCase("차량고장")) {
			if(tempList10.size() == 0){
				for(i = 0;i < size; i++){
					tempList10.add(new BigDecimal(0));
				}
			}
			tempList10.add(number,value);
			tempList10.remove(number+1);
			listNumber = 10;
		} else if ((column).toString().equalsIgnoreCase("뺑소니")) {
			if(tempList11.size() == 0){
				for(i = 0;i < size; i++){
					tempList11.add(new BigDecimal(0));
				}
			}
			tempList11.add(number,value);
			tempList11.remove(number+1);
			listNumber = 11;
		} else if ((column).toString().equalsIgnoreCase("교통사고")) {
			if(tempList12.size() == 0){
				for(i = 0;i < size; i++){
					tempList12.add(new BigDecimal(0));
				}
			}
			tempList12.add(number,value);
			tempList12.remove(number+1);
			listNumber = 12;
		} else if ((column).toString().equalsIgnoreCase("호우")) {
			if(tempList13.size() == 0){
				for(i = 0;i < size; i++){
					tempList13.add(new BigDecimal(0));
				}
			}
			tempList13.add(number,value);
			tempList13.remove(number+1);
			listNumber = 13;
		} else if ((column).toString().equalsIgnoreCase("지하차도침수")) {
			if(tempList14.size() == 0){
				for(i = 0;i < size; i++){
					tempList14.add(new BigDecimal(0));
				}
			}
			tempList14.add(number,value);
			tempList14.remove(number+1);
			listNumber = 14;
		} else if ((column).toString().equalsIgnoreCase("태풍")) {
			if(tempList15.size() == 0){
				for(i = 0;i < size; i++){
					tempList15.add(new BigDecimal(0));
				}
			}
			tempList15.add(number,value);
			tempList15.remove(number+1);
			listNumber = 15;
		} else if ((column).toString().equalsIgnoreCase("화재")) {
			if(tempList16.size() == 0){
				for(i = 0;i < size; i++){
					tempList16.add(new BigDecimal(0));
				}
			}
			tempList16.add(number,value);
			tempList16.remove(number+1);
			listNumber = 16;
		} else if ((column).toString().equalsIgnoreCase("수위경보")) {
			if(tempList17.size() == 0){
				for(i = 0;i < size; i++){
					tempList17.add(new BigDecimal(0));
				}
			}
			tempList17.add(number,value);
			tempList17.remove(number+1);
			listNumber = 17;
		} else if ((column).toString().equalsIgnoreCase("비상벨요청")) {
			if(tempList18.size() == 0){
				for(i = 0;i < size; i++){
					tempList18.add(new BigDecimal(0));
				}
			}
			tempList18.add(number,value);
			tempList18.remove(number+1);
			listNumber = 18;
		} else if ((column).toString().equalsIgnoreCase("용의차량추적")) {
			if(tempList19.size() == 0){
				for(i = 0;i < size; i++){
					tempList19.add(new BigDecimal(0));
				}
			}
			tempList19.add(number,value);
			tempList19.remove(number+1);
			listNumber = 19;
		} else if ((column).toString().equalsIgnoreCase("응급")) {
			if(tempList20.size() == 0){
				for(i = 0;i < size; i++){
					tempList20.add(new BigDecimal(0));
				}
			}
			tempList20.add(number,value);
			tempList20.remove(number+1);
			listNumber = 20;
		} else if ((column).toString().equalsIgnoreCase("미아")) {
			if(tempList21.size() == 0){
				for(i = 0;i < size; i++){
					tempList21.add(new BigDecimal(0));
				}
			}
			tempList21.add(number,value);
			tempList21.remove(number+1);
			listNumber = 21;
		} else if ((column).toString().equalsIgnoreCase("강도")) {
			if(tempList22.size() == 0){
				for(i = 0;i < size; i++){
					tempList22.add(new BigDecimal(0));
				}
			}
			tempList22.add(number,value);
			tempList22.remove(number+1);
			listNumber = 22;
		} else if ((column).toString().equalsIgnoreCase("상수도누수발생가능")) {
			if(tempList23.size() == 0){
				for(i = 0;i < size; i++){
					tempList23.add(new BigDecimal(0));
				}
			}
			tempList23.add(number,value);
			tempList23.remove(number+1);
			listNumber = 23;
		} else if ((column).toString().equalsIgnoreCase("시설물고장")) {
			if(tempList24.size() == 0){
				for(i = 0;i < size; i++){
					tempList24.add(new BigDecimal(0));
				}
			}
			tempList24.add(number,value);
			tempList24.remove(number+1);
			listNumber = 24;
		} else if ((column).toString().equalsIgnoreCase("도로통제")) {
			if(tempList25.size() == 0){
				for(i = 0;i < size; i++){
					tempList25.add(new BigDecimal(0));
				}
			}
			tempList25.add(number,value);
			tempList25.remove(number+1);
			listNumber = 25;
		} 
		return;
	}

	//엑셀 템플릿으로 원하는 엑셀파일 생성함.
	private void getXmlDataForExcel(List<Map<String, Object>> dataValue, String serviceName,String eventName, String categoryName, String periodName, Map<String, Object> resultMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		FileOutputStream fileOut = null;
		OutputStream out = null;
		
		//템플릿을 선택에 따라 골라 사용함.
		//PATH는 절대경로사용.
		final String TEMPLATE_PATH = "/ssw/bpm/was/Template/";
		
		String templateFileName = "";
		List<String> categoryScop = getCategoryScop(categoryName);
		if(!serviceName.equalsIgnoreCase(System.REPORT_OPTION_ALL_SERVICES) || !eventName.equalsIgnoreCase(System.REPORT_OPTION_ALL_EVENTS)){
			if(categoryName.equalsIgnoreCase("option.category.byTime")){
				templateFileName = TEMPLATE_PATH + "MonitoringTemplate1.xls";
			}else if(categoryName.equalsIgnoreCase("option.category.byAmPm")){
				templateFileName = TEMPLATE_PATH + "MonitoringTemplate2.xls";
			}else if(categoryName.equalsIgnoreCase("option.category.byDay")){
				templateFileName = TEMPLATE_PATH + "MonitoringTemplate3.xls";
			}else if(categoryName.equalsIgnoreCase("option.category.byMonth")){
				templateFileName = TEMPLATE_PATH + "MonitoringTemplate4.xls";
			}else if(categoryName.equalsIgnoreCase("option.category.bySeason")){
				templateFileName = TEMPLATE_PATH + "MonitoringTemplate5.xls";
			}else if(categoryName.equalsIgnoreCase("option.category.byQuarter")){
				templateFileName = TEMPLATE_PATH + "MonitoringTemplate6.xls";
			}else if(categoryName.equalsIgnoreCase("option.category.byHalfYear")){
				templateFileName = TEMPLATE_PATH + "MonitoringTemplate7.xls";
			}else{
				logger.error("해당 템플릿이 없습니다.");
			}		
		}else if(serviceName.equalsIgnoreCase(System.REPORT_OPTION_ALL_SERVICES)){
			if(categoryName.equalsIgnoreCase("option.category.byTime")){
				templateFileName = TEMPLATE_PATH + "MonitoringTemplate8.xls";
			}else if(categoryName.equalsIgnoreCase("option.category.byAmPm")){
				templateFileName = TEMPLATE_PATH + "MonitoringTemplate9.xls";
			}else if(categoryName.equalsIgnoreCase("option.category.byDay")){
				templateFileName = TEMPLATE_PATH + "MonitoringTemplate10.xls";
			}else if(categoryName.equalsIgnoreCase("option.category.byMonth")){
				templateFileName = TEMPLATE_PATH + "MonitoringTemplate11.xls";
			}else if(categoryName.equalsIgnoreCase("option.category.bySeason")){
				templateFileName = TEMPLATE_PATH + "MonitoringTemplate12.xls";
			}else if(categoryName.equalsIgnoreCase("option.category.byQuarter")){
				templateFileName = TEMPLATE_PATH + "MonitoringTemplate13.xls";
			}else if(categoryName.equalsIgnoreCase("option.category.byHalfYear")){
				templateFileName = TEMPLATE_PATH + "MonitoringTemplate14.xls";
			}
	
		}else if(!serviceName.equalsIgnoreCase(System.REPORT_OPTION_ALL_SERVICES) && eventName.equalsIgnoreCase(System.REPORT_OPTION_ALL_EVENTS)){
			templateFileName = TEMPLATE_PATH + "MonitoringTemplate9.xls";
			templateFileName = TEMPLATE_PATH + "MonitoringTemplate10.xls";
			templateFileName = TEMPLATE_PATH + "MonitoringTemplate11.xls";
			templateFileName = TEMPLATE_PATH + "MonitoringTemplate12.xls";
			templateFileName = TEMPLATE_PATH + "MonitoringTemplate13.xls";
		}else{
			logger.error("해당 템플릿이 없습니다.");
		}	
		
		//템플릿을 사용하여, 엑셀파일을 만듬.
		final String DESTFILE_NAME = "/ssw/bpm/was/file/MonitoringChart.xls";
		
		//해당 파라미터를 알맞게 변환
		String eventNameEdit = UcityUtil.getEventName(eventName,serviceName);
		String serviceNameEdit = UcityUtil.getServiceName(serviceName);
		String periodNameEdit = UcityUtil.getPeriodName(periodName);
		String categoryNameEdit = UcityUtil.getCategoryName(categoryName);
		
		//해당 이벤트네임과 서비스 이름 등을 지정 이름
		Map<String, String> title = new HashMap<String, String>();
		title.put("titleEvent", eventNameEdit);
		title.put("titleService", serviceNameEdit);
		title.put("titlePeriod", periodNameEdit);
		title.put("titleCategory", categoryNameEdit );
		
		//컬럼도 지정하려 했으나, 컬럼은 변하지 않으므로 그냥 템플릿에 적음.
//		Map <String, List> beans = new HashMap< String, List>();
//		beans.put("monitoring", categoryScop);
		
		//내용쓰기(시간대 별, 계절 별 등등 과 해당 값 )
		Map <String, Object> beans2 = new HashMap< String, Object>();
		beans2.put("title", title);
		beans2.put("monitoringData", dataValue);	

		XLSTransformer transformer = new XLSTransformer();
		if((!serviceName.equalsIgnoreCase(System.REPORT_OPTION_ALL_SERVICES) || !eventName.equalsIgnoreCase(System.REPORT_OPTION_ALL_EVENTS))){	
			try{
				//엑셀템플릿으로 데이터넣은 엑셀파일 생성
				transformer.transformXLS(templateFileName, beans2, DESTFILE_NAME);
			}catch(ParsePropertyException e){
				logger.error("jxls 변환 중에 오류 발생");
			}
		}else{
			try{
				transformer.markAsFixedSizeCollection("monitoringData");
				transformer.transformXLS(templateFileName, beans2, DESTFILE_NAME);
			}catch(ParsePropertyException e){
				logger.error("jxls 변환 중에 오류 발생");
			}finally{
				tempList1 = new ArrayList();
				tempList2 = new ArrayList();
				tempList3 = new ArrayList();
				tempList4 = new ArrayList();
				tempList5 = new ArrayList();
				tempList6 = new ArrayList();
				tempList7 = new ArrayList();
				tempList8 = new ArrayList();
				tempList9 = new ArrayList();
				tempList10 = new ArrayList();
				tempList11 = new ArrayList();
				tempList12 = new ArrayList();
				tempList13 = new ArrayList();
				tempList14 = new ArrayList();
				tempList15 = new ArrayList();
				tempList16 = new ArrayList();
				tempList17 = new ArrayList();
				tempList18 = new ArrayList();
				tempList19 = new ArrayList();
				tempList20 = new ArrayList();
				tempList21 = new ArrayList();
				tempList22= new ArrayList();
				tempList23 = new ArrayList();
				tempList24 = new ArrayList();
				tempList25 = new ArrayList();
			}
		}
	}
}
