/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 9. 27.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package pro.ucity.manager.ucityWorkList.manager.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.smartworks.server.engine.common.manager.AbstractManager;
import net.smartworks.server.engine.common.model.Filter;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.common.util.DateUtil;

import org.hibernate.Query;

import pro.ucity.manager.ucityWorkList.exception.UcityWorkListException;
import pro.ucity.manager.ucityWorkList.manager.IUcityWorkListManager;
import pro.ucity.manager.ucityWorkList.model.UcityWorkList;
import pro.ucity.manager.ucityWorkList.model.UcityWorkListCond;

public class UcityWorkListManagerImpl extends AbstractManager implements IUcityWorkListManager {

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
		String packageId = null;
		String status = null;
		String title = null;
		String runningTaskId = null;
		String runningTaskName = null;

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
			packageId = cond.getPackageId();
			status = cond.getStatus();
			title = cond.getTitle();
			runningTaskId = cond.getRunningTaskId();
			runningTaskName = cond.getRunningTaskName();
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
							query.setTimestamp(param, DateUtil.toDate(operValue));
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
		tableBuff.append("	, TO_CHAR(createdtime + 9/24, 'yyyyMMdd') as eventTime_year ");
		tableBuff.append(" 	, case when TO_CHAR(createdtime + 9/24, 'HH24') between '03' and '05' then '새벽(3시~6시)'   ");
		tableBuff.append(" 		when TO_CHAR(createdtime + 9/24, 'HH24') between '06' and '10' then '아침(6시~11시)'   ");
		tableBuff.append(" 			when TO_CHAR(createdtime + 9/24, 'HH24') between '11' and '13' then '점심(11시~14시)' "); 
		tableBuff.append(" 				when TO_CHAR(createdtime + 9/24, 'HH24') between '14' and '17' then '낮(14시~18시)'  ");
		tableBuff.append(" 					when TO_CHAR(createdtime + 9/24, 'HH24') between '18' and '22' then '저녁(18시~23시)'  ");
		tableBuff.append(" 						when TO_CHAR(createdtime + 9/24, 'HH24') between '23' and '24' then '심야(23시~3시)'  ");
		tableBuff.append(" 							when TO_CHAR(createdtime + 9/24, 'HH24') between '00' and '02' then '심야(23시~3시)' ");						
		tableBuff.append(" 		end as eventTime_hour ");
		tableBuff.append("	, case when TO_CHAR(createdtime + 9/24, 'HH24') between '01' and '12' then 'A' when TO_CHAR(createdtime + 9/24, 'HH24') between '13' and '24' then 'P' end as eventTime_ampm  ");
		tableBuff.append("	, TO_CHAR(createdtime + 9/24, 'dy') || '요일' as eventTime_dy ");
		tableBuff.append("	, TO_CHAR(createdtime + 9/24, 'MM') || '월' as eventTime_month ");
		tableBuff.append("	, case when TO_CHAR(createdtime + 9/24, 'MM') between '01' and '06' then '상반기'   ");
		tableBuff.append("		when TO_CHAR(createdtime + 9/24, 'MM') between '04' and '12' then '하반기'   ");
		tableBuff.append("		end as eventTime_half  ");
		tableBuff.append("	, case when TO_CHAR(createdtime + 9/24, 'MM') between '01' and '03' then '1분기'   ");
		tableBuff.append("		when TO_CHAR(createdtime + 9/24, 'MM') between '04' and '06' then '2분기'   ");
		tableBuff.append("			when TO_CHAR(createdtime + 9/24, 'MM') between '07' and '09' then '3분기'  ");
		tableBuff.append("				when TO_CHAR(createdtime + 9/24, 'MM') between '10' and '12' then '4분기'    ");
		tableBuff.append("		end as eventTime_quarter  ");
		tableBuff.append("	, case when TO_CHAR(createdtime + 9/24, 'MM') between '03' and '05' then '봄'   ");
		tableBuff.append("		when TO_CHAR(createdtime + 9/24, 'MM') between '06' and '08' then '여름'   ");
		tableBuff.append("			when TO_CHAR(createdtime + 9/24, 'MM') between '09' and '11' then '가을'  ");
		tableBuff.append("				when TO_CHAR(createdtime + 9/24, 'MM') = '12' then '겨울'    ");
		tableBuff.append("					when TO_CHAR(createdtime + 9/24, 'MM') between '01' and '02' then '겨울'  ");
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
	private String getXmlDataForChart(String categoryName, Map<String, Object> resultMap) throws Exception {

		StringBuffer resultXml = new StringBuffer();
		List<String> categoryScop = getCategoryScop(categoryName);
		
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
		}
		resultXml.append("</ChartData>");
		
		return resultXml.toString();
	}
	public String getUcityChartXml(String categoryName, String periodName, String serviceName, String eventName) throws Exception {
		StringBuffer chartQuery = new StringBuffer();
		chartQuery.append("select tbl.").append(getCategoryColumnName(categoryName)).append(", count(*) as count from (");
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
		chartQuery.append(" group by tbl.").append(getCategoryColumnName(categoryName)).append(" order by tbl.").append(getCategoryColumnName(categoryName));

		Query query = this.getSession().createSQLQuery(chartQuery.toString());
		
		List list = query.list();
		Map<String,Object> resultMap = new HashMap<String, Object>();
		if (list == null || list.isEmpty())
			return getXmlDataForChart(categoryName, resultMap);
		for (Iterator itr = list.iterator(); itr.hasNext();) {
			Object[] fields = (Object[]) itr.next();
			if (fields[0] instanceof Character) {
				if(((Character)fields[0]).toString().equalsIgnoreCase("상")) {
					resultMap.put("상반기", (BigDecimal)fields[1]);
				} else if (((Character)fields[0]).toString().equalsIgnoreCase("하")) {
					resultMap.put("하반기", (BigDecimal)fields[1]);
				} else if (((Character)fields[0]).toString().equalsIgnoreCase("1")) {
					resultMap.put("1분기", (BigDecimal)fields[1]);
				} else if (((Character)fields[0]).toString().equalsIgnoreCase("2")) {
					resultMap.put("2분기", (BigDecimal)fields[1]);
				} else if (((Character)fields[0]).toString().equalsIgnoreCase("3")) {
					resultMap.put("3분기", (BigDecimal)fields[1]);
				} else if (((Character)fields[0]).toString().equalsIgnoreCase("4")) {
					resultMap.put("4분기", (BigDecimal)fields[1]);
				} else if (((Character)fields[0]).toString().equalsIgnoreCase("A")) {
					resultMap.put("오전(AM)", (BigDecimal)fields[1]);
				} else if (((Character)fields[0]).toString().equalsIgnoreCase("P")) {
					resultMap.put("오후(PM)", (BigDecimal)fields[1]);
				}
				
			} else if(fields[0] instanceof String){
				if(((String)fields[0]).toString().equalsIgnoreCase("sun요일")) {
					resultMap.put("일요일", (BigDecimal)fields[1]);
				} else if (((String)fields[0]).toString().equalsIgnoreCase("mon요일")) {
					resultMap.put("월요일", (BigDecimal)fields[1]);
				} else if (((String)fields[0]).toString().equalsIgnoreCase("tue요일")) {
					resultMap.put("화요일", (BigDecimal)fields[1]);
				} else if (((String)fields[0]).toString().equalsIgnoreCase("wed요일")) {
					resultMap.put("수요일", (BigDecimal)fields[1]);
				} else if (((String)fields[0]).toString().equalsIgnoreCase("thu요일")) {
					resultMap.put("목요일", (BigDecimal)fields[1]);
				} else if (((String)fields[0]).toString().equalsIgnoreCase("fri요일")) {
					resultMap.put("금요일", (BigDecimal)fields[1]);
				} else if (((String)fields[0]).toString().equalsIgnoreCase("sat요일")) {
					resultMap.put("토요일", (BigDecimal)fields[1]);
				}else{
					resultMap.put((String)fields[0], (BigDecimal)fields[1]);
				}
			}
		}
		return getXmlDataForChart(categoryName, resultMap);
	}
}
