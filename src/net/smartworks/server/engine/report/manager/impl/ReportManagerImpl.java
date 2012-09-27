/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 9. 28.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.report.manager.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import net.smartworks.model.report.Data;
import net.smartworks.server.engine.common.manager.AbstractManager;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.report.manager.IReportManager;
import net.smartworks.util.SmartTest;

public class ReportManagerImpl extends AbstractManager implements IReportManager  {

	private String getProcessInfoTableForOracleReport() throws Exception {
		StringBuffer tableBuff = new StringBuffer();
		tableBuff.append("select	prcInst.prcObjid  ");
		tableBuff.append("	, case when prcInst.prcStatus = '2' then '진행중' when prcInst.prcStatus='3' then '완료' end as prcStatus ");
		tableBuff.append("	, prcInst.prcTitle ");
		tableBuff.append("	, prcInst.prccreateDate ");
		tableBuff.append("	, TO_CHAR(prcInst.prccreatedate, 'yyyy-MM-dd HH24') as prccreatedate_yyyymmddhh  ");
		tableBuff.append("	, TO_CHAR(prcInst.prccreatedate, 'yyyy-MM-dd') as prccreatedate_yyyymmdd  ");
		tableBuff.append("	, TO_CHAR(prcInst.prccreatedate, 'yyyy-MM') as prccreatedate_yyyymm ");
		tableBuff.append("	, TO_CHAR(prcInst.prccreatedate, 'yyyy') as prccreatedate_yyyy ");
		tableBuff.append("	, TO_CHAR(prcInst.prccreatedate, 'yyyy-MM') || ' ' ||TO_CHAR(prcInst.prccreatedate,'W') || '주' as prccreatedate_week ");
		tableBuff.append("	, case when TO_CHAR(prcInst.prccreatedate, 'MM') between '01' and '03' then TO_CHAR(prcInst.prccreatedate, 'yyyy') || ' 1분기'  ");
		tableBuff.append("		when TO_CHAR(prcInst.prccreatedate, 'MM') between '04' and '06' then TO_CHAR(prcInst.prccreatedate, 'yyyy') || ' 2분기'  ");
		tableBuff.append("			when TO_CHAR(prcInst.prccreatedate, 'MM') between '07' and '09' then TO_CHAR(prcInst.prccreatedate, 'yyyy') || ' 3분기' ");
		tableBuff.append("				when TO_CHAR(prcInst.prccreatedate, 'MM') between '10' and '12' then TO_CHAR(prcInst.prccreatedate, 'yyyy') || ' 4분기'   ");
		tableBuff.append("		end as prccreatedate_quarter ");
		tableBuff.append("	, case when TO_CHAR(prcInst.prccreatedate, 'MM') between '01' and '06' then TO_CHAR(prcInst.prccreatedate, 'yyyy') || ' 전반기'  ");
		tableBuff.append("		when TO_CHAR(prcInst.prccreatedate, 'MM') between '04' and '12' then TO_CHAR(prcInst.prccreatedate, 'yyyy') || ' 후반기'  ");
		tableBuff.append("		end as prccreatedate_half ");
		tableBuff.append("	, usr.name as prcCreateUser ");
		tableBuff.append("	, usr.pos as prcCreateUser_pos ");
		tableBuff.append("	, case when usr.authid = 'ADMINISTRATOR' then '관리자' when usr.authid='USER' then '사용자' end as prcCreateUser_authId ");
		tableBuff.append("	, case when usr.locale = 'ko' then '한국어' when usr.locale ='en' then '영어' end as prcCreateUser_locale ");
		tableBuff.append("	, dept.name as prccreateuser_dept ");
		tableBuff.append("	, prcInst.prcmodifyDate ");
		tableBuff.append("	, TO_CHAR(prcInst.prcmodifyDate, 'yyyy-MM-dd HH24') as prcmodifyDate_yyyymmddhh  ");
		tableBuff.append("	, TO_CHAR(prcInst.prcmodifyDate, 'yyyy-MM-dd') as prcmodifyDate_yyyymmdd  ");
		tableBuff.append("	, TO_CHAR(prcInst.prcmodifyDate, 'yyyy-MM') as prcmodifyDate_yyyymm ");
		tableBuff.append("	, TO_CHAR(prcInst.prcmodifyDate, 'yyyy') as prcmodifyDate_yyyy ");
		tableBuff.append("	, TO_CHAR(prcInst.prcmodifyDate, 'yyyy-MM') || ' ' ||TO_CHAR(prcInst.prcmodifyDate,'W') || '주' as prcmodifyDate_week ");
		tableBuff.append("	, case when TO_CHAR(prcInst.prcmodifyDate, 'MM') between '01' and '03' then TO_CHAR(prcInst.prcmodifyDate, 'yyyy') || ' 1분기'  ");
		tableBuff.append("		when TO_CHAR(prcInst.prcmodifyDate, 'MM') between '04' and '06' then TO_CHAR(prcInst.prcmodifyDate, 'yyyy') || ' 2분기'  ");
		tableBuff.append("			when TO_CHAR(prcInst.prcmodifyDate, 'MM') between '07' and '09' then TO_CHAR(prcInst.prcmodifyDate, 'yyyy') || ' 3분기' ");
		tableBuff.append("				when TO_CHAR(prcInst.prcmodifyDate, 'MM') between '10' and '12' then TO_CHAR(prcInst.prcmodifyDate, 'yyyy') || ' 4분기'   ");
		tableBuff.append("		end as prcmodifyDate_quarter ");
		tableBuff.append("	, case when TO_CHAR(prcInst.prcmodifyDate, 'MM') between '01' and '06' then TO_CHAR(prcInst.prcmodifyDate, 'yyyy') || ' 전반기'  ");
		tableBuff.append("		when TO_CHAR(prcInst.prcmodifyDate, 'MM') between '04' and '12' then TO_CHAR(prcInst.prcmodifyDate, 'yyyy') || ' 후반기'  ");
		tableBuff.append("		end as prcmodifyDate_half ");
		tableBuff.append("	, lastTask.tskname as lastTaskName ");
		tableBuff.append("	, usr2.name as lastTaskuserName ");
		tableBuff.append("	, usr2.pos as lastTaskuserName_pos ");
		tableBuff.append("	, case when usr2.authid = 'ADMINISTRATOR' then '관리자' when usr2.authid='USER' then '사용자' end as lastTaskuserName_authId ");
		tableBuff.append("	, case when usr2.locale = 'ko' then '한국어' when usr2.locale ='en' then '영어' end as lastTaskuserName_locale ");
		tableBuff.append("	, dept2.name as lastTaskuserName_dept ");
		tableBuff.append("from prcprcinst prcInst, sworguser usr, sworgdept dept, sworgdept dept2, ");
		tableBuff.append("( ");
		tableBuff.append("	select maxTask.lastTskPrcInstId, task.tskName, task.tskAssignee ");
		tableBuff.append("	from ( ");
		tableBuff.append("		select tskPrcInstId lastTskPrcInstId , max(tskCreateDate) as createDate ");
		tableBuff.append("		from tsktask ");
		tableBuff.append("		where tsktype not in ('and','route','SUBFLOW','xor', 'reference', 'approval') ");
		tableBuff.append("		group by tskprcinstid ");
		tableBuff.append("	) maxTask, ");
		tableBuff.append("	tskTask task ");
		tableBuff.append("	where maxTask.createdate = task.tskcreatedate ");
		tableBuff.append(") lastTask, sworguser usr2 ");
		tableBuff.append("where 1=1 ");
		tableBuff.append("and prctype='PROCESS' ");
		tableBuff.append("and usr.id = prcInst.prccreateUser ");
		tableBuff.append("and usr.deptid = dept.id ");
		tableBuff.append("and lastTask.lastTskPrcInstId = prcInst.prcObjid ");
		tableBuff.append("and usr2.id = lastTask.tskAssignee ");
		tableBuff.append("and usr2.deptid = dept2.id ");
				
		return tableBuff.toString();
		
	}
	private String getColumnName(String argName) {
		
		if (argName.equalsIgnoreCase("status")) {
			//상태
			return "prcStatus";
		} else if (argName.equalsIgnoreCase("subject")) {
			//제목
			return "prcTitle";
		} else if (argName.equalsIgnoreCase("lastTask")) {
			//마지막태스크
			return "lastTaskName";
		} else {
			return null;
		}
	}
	private String getColumnName(String argName, String argName2) {
		if (argName.equalsIgnoreCase("creator")) {
			if (argName2.equalsIgnoreCase("userName")) {
				//사용자이름
				return "prcCreateUser";
			} else if (argName2.equalsIgnoreCase("userDepartment")) {
				//사용자부서
				return "prccreateuser_dept";
			} else if (argName2.equalsIgnoreCase("userPosition")) {
				//사용자직급
				return "prcCreateUser_pos";
			} else if (argName2.equalsIgnoreCase("userLevel")) {
				//사용자레벨
				return "prcCreateUser_authId";
			} else if (argName2.equalsIgnoreCase("userLocale")) {
				//사용자언어
				return "prcCreateUser_locale";
			} else {
				return null;
			}
		} else if (argName.equalsIgnoreCase("createdTime")) {
			if (argName2.equalsIgnoreCase("byHour")) {
				//시간별
				return "prccreatedate_yyyymmddhh";
			} else if (argName2.equalsIgnoreCase("byDay")) {
				//일별
				return "prccreatedate_yyyymmdd";
			} else if (argName2.equalsIgnoreCase("byWeek")) {
				//주별
				return "prccreatedate_week";
			} else if (argName2.equalsIgnoreCase("byMonth")) {
				//월별
				return "prccreatedate_yyyymm";
			} else if (argName2.equalsIgnoreCase("byQuarter")) {
				//분기별
				return "prccreatedate_quarter";
			} else if (argName2.equalsIgnoreCase("byHalfYear")) {
				//반기별
				return "prccreatedate_half";
			} else if (argName2.equalsIgnoreCase("byYear")) {
				//년별
				return "prccreatedate_yyyy";
			} else {
				return null;
			}
		} else if (argName.equalsIgnoreCase("modifier")) {
			if (argName2.equalsIgnoreCase("userName")) {
				//사용자이름
				return "lastTaskuserName";
			} else if (argName2.equalsIgnoreCase("userDepartment")) {
				//사용자부서
				return "lastTaskuserName_dept";
			} else if (argName2.equalsIgnoreCase("userPosition")) {
				//사용자직급
				return "lastTaskuserName_pos";
			} else if (argName2.equalsIgnoreCase("userLevel")) {
				//사용자레벨
				return "lastTaskuserName_authId";
			} else if (argName2.equalsIgnoreCase("userLocale")) {
				//사용자언어
				return "lastTaskuserName_locale";
			} else {
				return null;
			}
		} else if (argName.equalsIgnoreCase("modifiedTime")) {
			if (argName2.equalsIgnoreCase("byHour")) {
				//시간별
				return "prcmodifyDate_yyyymmddhh";
			} else if (argName2.equalsIgnoreCase("byDay")) {
				//일별
				return "prcmodifyDate_yyyymmdd";
			} else if (argName2.equalsIgnoreCase("byWeek")) {
				//주별
				return "prcmodifyDate_week";
			} else if (argName2.equalsIgnoreCase("byMonth")) {
				//월별
				return "prcmodifyDate_yyyymm";
			} else if (argName2.equalsIgnoreCase("byQuarter")) {
				//분기별
				return "prcmodifyDate_quarter";
			} else if (argName2.equalsIgnoreCase("byHalfYear")) {
				//반기별
				return "prcmodifyDate_half";
			} else if (argName2.equalsIgnoreCase("byYear")) {
				//년별
				return "prcmodifyDate_yyyy";
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	private String getZaxisFunction(String argName) {
		if (argName.equalsIgnoreCase("count")) {
			//시간별
			return "count";
		} else if (argName.equalsIgnoreCase("sum")) {
			//일별
			return "sum";
		} else if (argName.equalsIgnoreCase("mean")) {
			//주별
			return "avg";
		} else if (argName.equalsIgnoreCase("min")) {
			//월별
			return "min";
		} else if (argName.equalsIgnoreCase("max")) {
			//분기별
			return "max";
		} else {
			return null;
		}
	}
	
	@Override
	public Data getReportData(String dbType, Map<String, Object> requestBody) throws Exception {
		/*{
			"frmWorkReport":
				{
					"rdoWorkReportType":"1",
					"selReportChartType":"3",
					"selReportXAxis":"modifiedTime",
					"selReportXAxisSelectorDate":"byMonth",
					"rdoReportXAxisSort":"ascend",
					"selReportYAxis":"modifiedTime",
					"selReportYAxisValue":"count",
					"selReportFilterName":"system.allInstances"
				},
			"frmAccessPolicy":
				{
					"selAccessPolicy":"3"
				}
		}*/
		Map<String, Object> frmWorkReport = (Map<String, Object>)requestBody.get("frmWorkReport");
		String rdoWorkReportType = (String)frmWorkReport.get("rdoWorkReportType");
		String selReportChartType = (String)frmWorkReport.get("selReportChartType");
		String selReportXAxis = (String)frmWorkReport.get("selReportXAxis");
		String selReportXAxisSelectorDate = (String)frmWorkReport.get("selReportXAxisSelectorDate");
		String selReportXAxisSelectorUser = (String)frmWorkReport.get("selReportXAxisSelectorUser");
		String rdoReportXAxisSort = (String)frmWorkReport.get("rdoReportXAxisSort");
		String selReportYAxis = (String)frmWorkReport.get("selReportYAxis");
		String selReportYAxisValue = (String)frmWorkReport.get("selReportYAxisValue");
		String selReportZAxis = (String)frmWorkReport.get("selReportZAxis");
		String selReportZAxisSelectorDate = (String)frmWorkReport.get("selReportZAxisSelectorDate");
		String selReportZAxisSelectorUser = (String)frmWorkReport.get("selReportZAxisSelectorUser");
		String rdoReportZAxisSort = (String)frmWorkReport.get("rdoReportZAxisSort");
		
		StringBuffer sqlBuf = new StringBuffer();
		int resultColumnCount = 2;

		Data reportData = new Data();
		
		if (dbType.equalsIgnoreCase("ORACLE")) {
			//ORACLE
			sqlBuf.append(" select ");
			
			String xAxis =  (getColumnName(selReportXAxis) == null ? selReportXAxisSelectorDate == null || selReportXAxisSelectorDate == "" ? getColumnName(selReportXAxis, selReportXAxisSelectorUser) : getColumnName(selReportXAxis, selReportXAxisSelectorDate)  : getColumnName(selReportXAxis));
			sqlBuf.append(xAxis);
			String zAxis = null;
			if (!CommonUtil.isEmpty(selReportZAxis)) {
				resultColumnCount = 3;
				zAxis =  (getColumnName(selReportZAxis) == null ? selReportZAxisSelectorDate == null || selReportZAxisSelectorDate == "" ? getColumnName(selReportZAxis, selReportZAxisSelectorUser) : getColumnName(selReportZAxis, selReportZAxisSelectorDate)  : getColumnName(selReportZAxis));
				sqlBuf.append(" , ").append(zAxis);
			}
			String function = getZaxisFunction(selReportYAxisValue);
			sqlBuf.append(" , ").append(function).append("(").append(getColumnName(selReportYAxis)).append(")");	
			
			sqlBuf.append(" from ( ").append(getProcessInfoTableForOracleReport()).append(" ) tbl");
			
			sqlBuf.append(" group by ").append(xAxis);
			if (zAxis != null) {
				sqlBuf.append(" , ").append(zAxis);
			}
			sqlBuf.append(" order by ").append(xAxis);
			if (!CommonUtil.isEmpty(rdoReportXAxisSort)) {
				if (rdoReportXAxisSort.equalsIgnoreCase("descend")) {
					sqlBuf.append(" desc");
				} else if (rdoReportXAxisSort.equalsIgnoreCase("ascend")) {
					sqlBuf.append(" asc");
				}
			}
			if (zAxis != null) {
				sqlBuf.append(" , ").append(zAxis);
				if (!CommonUtil.isEmpty(rdoReportZAxisSort)) {
					if (rdoReportZAxisSort.equalsIgnoreCase("descend")) {
						sqlBuf.append(" desc");
					} else if (rdoReportZAxisSort.equalsIgnoreCase("ascend")) {
						sqlBuf.append(" asc");
					}
				}
			}
			Query query = this.getSession().createSQLQuery(sqlBuf.toString());
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			List objList = new ArrayList();

			List<Map<String , Object>> values  = new java.util.ArrayList<Map<String,Object>>();
			
			List zAxisValueList = new ArrayList();
			Map<String, Map<String, BigDecimal>> resultMap = new HashMap<String, Map<String, BigDecimal>>();
			for (Iterator itr = list.iterator(); itr.hasNext();) {
				Object[] fields = (Object[]) itr.next();
				int j = 0;
				if (resultColumnCount == 2) {
				    Map<String,Object> value = new HashMap<String, Object>();
				    value.put(selReportXAxis, (String)fields[j++]);
				    value.put(selReportYAxisValue, (BigDecimal)fields[j++]);
				    values.add(value);
				} else if (resultColumnCount == 3) {
					
					String value1 = (String)fields[j++];
					String value2 = (String)fields[j++];
					if (!zAxisValueList.contains(value2))
						zAxisValueList.add(value2);
					
					BigDecimal value3 = (BigDecimal)fields[j++];

				    if (resultMap.get(value1) != null) {
				    	Map<String, BigDecimal> zAxisMap = resultMap.get(value1);
				    	zAxisMap.put(value2, value3);
				    } else {
				    	Map<String, BigDecimal> zAxisMap = new HashMap<String, BigDecimal>();
				    	zAxisMap.put(value2, value3);
				    	resultMap.put(value1, zAxisMap);
				    }
				}
			}
			if (resultColumnCount == 2) {
			    reportData.setValues(values);
			    reportData.setGroupNames(new String[]{selReportYAxisValue});
			    reportData.setxFieldName(selReportXAxis);
			    reportData.setyValueName(selReportYAxisValue);
			} else if (resultColumnCount == 3) {
				
				Iterator itr = resultMap.keySet().iterator();
				while (itr.hasNext()) {
				    Map<String,Object> value = new HashMap<String, Object>();
					String key = (String)itr.next();
				    value.put(selReportXAxis, key);
					Map<String, BigDecimal> zAxisMap = resultMap.get(key);
					for (int i = 0; i < zAxisValueList.size(); i++) {
						BigDecimal tempKey = zAxisMap.get((String)zAxisValueList.get(i));
						if (CommonUtil.isEmpty(tempKey)) {
							value.put((String)zAxisValueList.get(i), 0);
						} else {
							value.put((String)zAxisValueList.get(i), tempKey);
						}
					}
					values.add(value);
				}

			    reportData.setValues(values);
			    String[] groupNames = new String[zAxisValueList.size()];
			    zAxisValueList.toArray(groupNames);
			    reportData.setGroupNames(groupNames);
			    reportData.setxFieldName(selReportXAxis);
			    reportData.setyValueName(selReportYAxisValue);
			}
			
		} else {
			//MSSQL
		}
		
		return reportData;
//		return SmartTest.getReportData2();
	}

}
