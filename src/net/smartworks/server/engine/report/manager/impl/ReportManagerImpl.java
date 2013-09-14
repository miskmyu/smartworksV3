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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.smartworks.model.report.Data;
import net.smartworks.model.work.SmartWork;
import net.smartworks.server.engine.common.manager.AbstractManager;
import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.infowork.domain.model.SwdRecordCond;
import net.smartworks.server.engine.infowork.form.model.SwfForm;
import net.smartworks.server.engine.infowork.form.model.SwfFormCond;
import net.smartworks.server.engine.report.Exception.RptException;
import net.smartworks.server.engine.report.manager.IReportManager;
import net.smartworks.server.engine.report.model.RptReport;
import net.smartworks.server.engine.report.model.RptReportCond;
import net.smartworks.server.engine.report.model.RptReportPane;
import net.smartworks.server.engine.report.model.RptReportPaneCond;

import org.hibernate.Query;

public class ReportManagerImpl extends AbstractManager implements IReportManager  {

	@Override
	public Data getReportData(String user, Map<String, Object> requestBody) throws Exception {
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
		
		String targetWorkId = (String)requestBody.get("targetWorkId");
		String dbType = this.getDbType();
		if (targetWorkId.equalsIgnoreCase(SmartWork.ID_ALL_WORKS)) {
			return getReportDataAllWork(user, dbType, requestBody);
		} else {
			return getReportDataTargetWork(user, dbType, requestBody);
		}
//		return SmartTest.getReportData2();
	}
	
	
	public String getInforworkSqlQuery() throws Exception {
		
//		select t.c30, (select dept.name from sworguser usr, sworgdept dept where t.c30_id = usr.id and usr.deptid=dept.id) , t.c3, (select usr.authid from sworguser usr where t.c3_id = usr.id)
//		from (
//			select tbl.c30, ref.refrecordid as c30_id, c3, ref2.refrecordid as c3_id from dt_1373531630210 tbl, swdomainfield field, swdataref ref, swdomainfield field2, swdataref ref2 
//			where tbl.id = ref.myrecordid and field.domainid='md_fc704a7b2fa645608ef302798e69185c' and field.tablecolname='c30' and field.formfieldid=ref.myformfieldid
//			and tbl.id = ref2.myrecordid and field2.domainid='md_fc704a7b2fa645608ef302798e69185c' and field2.tablecolname='c3' and field2.formfieldid=ref2.myformfieldid
//		) t
		 
		return null;
		
	}
	
	public Data getReportDataTargetWork(String user, String dbType, Map<String, Object> requestBody) throws Exception {
		
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
		
		if (dbType.equalsIgnoreCase("oracle")) {
			
		} else if (dbType.equalsIgnoreCase("sqlserver")) {
			//MSSQL
		} else if (dbType.equalsIgnoreCase("postgresql")) {
			sqlBuf.append(" select ");
			
			//String xAxis =  (getColumnName(selReportXAxis) == null ? selReportXAxisSelectorDate == null || selReportXAxisSelectorDate == "" ? getColumnName(selReportXAxis, selReportXAxisSelectorUser) : getColumnName(selReportXAxis, selReportXAxisSelectorDate)  : getColumnName(selReportXAxis));

			String tempXSubKey = CommonUtil.toNull(selReportXAxisSelectorDate) == null ? selReportXAxisSelectorUser : selReportXAxisSelectorDate;
			
			String xAxis =  getInforWorkColumnNameByUserSelect(selReportXAxis, tempXSubKey);
			sqlBuf.append(xAxis);
			String zAxis = null;
			if (!CommonUtil.isEmpty(selReportZAxis)) {
				resultColumnCount = 3;
				//zAxis =  (getColumnName(selReportZAxis) == null ? selReportZAxisSelectorDate == null || selReportZAxisSelectorDate == "" ? getColumnName(selReportZAxis, selReportZAxisSelectorUser) : getColumnName(selReportZAxis, selReportZAxisSelectorDate)  : getColumnName(selReportZAxis));
				String tempZSubKey = CommonUtil.toNull(selReportZAxisSelectorDate) ==  null ? selReportZAxisSelectorUser : selReportZAxisSelectorDate;
				zAxis = getInforWorkColumnNameByUserSelect(selReportZAxis, tempZSubKey);
				sqlBuf.append(" , ").append(zAxis);
			}
			String function = getZaxisFunction(selReportYAxisValue);
			sqlBuf.append(" , ").append(function).append("(").append(getInforWorkColumnName(selReportYAxis)).append(")");	
			
			sqlBuf.append(" from ( ").append(getInforWorkTableForReport(user, requestBody)).append(" ) tbl");
			
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
			//Map<String, Map<String, BigInteger>> resultMap = new HashMap<String, Map<String, BigInteger>>();
			//Map<String, Map<String, Object>> resultMap = new HashMap<String, Map<String, Object>>();
			Map<Object, Map<Object, Object>> resultMap = new HashMap<Object, Map<Object, Object>>();
			for (Iterator itr = list.iterator(); itr.hasNext();) {
				Object[] fields = (Object[]) itr.next();
				int j = 0;
				if (resultColumnCount == 2) {
				    Map<String,Object> value = new HashMap<String, Object>();
				    value.put(selReportXAxis, (String)fields[j++]);
				    //value.put(selReportYAxisValue, (BigInteger)fields[j++]);
				    value.put(selReportYAxisValue, (Object)fields[j++]);
				    values.add(value);
				} else if (resultColumnCount == 3) {
					
//					String value1 = (String)fields[j++];
//					String value2 = (String)fields[j++];
					Object value1 = (Object)fields[j++];
					Object value2 = (Object)fields[j++];
					if (!zAxisValueList.contains(CommonUtil.toNotNull(value2)))
						zAxisValueList.add(CommonUtil.toNotNull(value2));
					
					//BigInteger value3 = (BigInteger)fields[j++];
					Object value3 = (Object)fields[j++];

				    if (resultMap.get(value1) != null) {
				    	//Map<String, BigInteger> zAxisMap = resultMap.get(value1);
//				    	Map<String, Object> zAxisMap = resultMap.get(value1);
				    	Map<Object, Object> zAxisMap = resultMap.get(value1);
				    	zAxisMap.put(value2, value3);
				    } else {
				    	//Map<String, BigInteger> zAxisMap = new HashMap<String, BigInteger>();
//				    	Map<String, Object> zAxisMap = new HashMap<String, Object>();
				    	Map<Object, Object> zAxisMap = new HashMap<Object, Object>();
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
//					String key = (String)itr.next();
					Object key = (Object)itr.next();
				    value.put(selReportXAxis, key);
					//Map<String, BigInteger> zAxisMap = resultMap.get(key);
				    //Map<String, Object> zAxisMap = resultMap.get(key);
				    Map<Object, Object> zAxisMap = resultMap.get(key);
					for (int i = 0; i < zAxisValueList.size(); i++) {
					//	BigInteger tempKey = zAxisMap.get((String)zAxisValueList.get(i));
						Object tempKey = zAxisMap.get((String)zAxisValueList.get(i));
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
		}
		return reportData;
	}
	
	public String getInforWorkColumnNameByUserSelect(String key, String subKey) throws Exception {
		return null;
	}
	public String getInforWorkColumnName(String key) throws Exception {
		return null;
	}
	public String getInforWorkTableForReport(String user, Map<String, Object> requestBody) throws Exception {
		
		String targetWorkId = (String)requestBody.get("targetWorkId");

		SwfFormCond formCond = new SwfFormCond();
		formCond.setPackageId(targetWorkId);
		
		SwfForm[] form = SwManagerFactory.getInstance().getSwfManager().getForms("", formCond, IManager.LEVEL_LITE);
		
		if (form == null || form.length == 0)
			return null;
		
		SwdRecordCond cond = new SwdRecordCond();
		cond.setFormId(form[0].getId());
		
		Query query = SwManagerFactory.getInstance().getSwdManager().getRecordQuery(user, cond);
		
		return query.getQueryString();
	}
	
	public Data getReportDataAllWork(String user, String dbType, Map<String, Object> requestBody) throws Exception {
		
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
		
		if (dbType.equalsIgnoreCase("oracle")) {
			//ORACLE
			sqlBuf.append(" select ");
			
			//String xAxis =  (getColumnName(selReportXAxis) == null ? selReportXAxisSelectorDate == null || selReportXAxisSelectorDate == "" ? getColumnName(selReportXAxis, selReportXAxisSelectorUser) : getColumnName(selReportXAxis, selReportXAxisSelectorDate)  : getColumnName(selReportXAxis));

			String tempXSubKey = CommonUtil.toNull(selReportXAxisSelectorDate) == null ? selReportXAxisSelectorUser : selReportXAxisSelectorDate;
			
			String xAxis =  getColumnNameByUserSelect(selReportXAxis, tempXSubKey);
			sqlBuf.append(xAxis);
			String zAxis = null;
			if (!CommonUtil.isEmpty(selReportZAxis)) {
				resultColumnCount = 3;
				//zAxis =  (getColumnName(selReportZAxis) == null ? selReportZAxisSelectorDate == null || selReportZAxisSelectorDate == "" ? getColumnName(selReportZAxis, selReportZAxisSelectorUser) : getColumnName(selReportZAxis, selReportZAxisSelectorDate)  : getColumnName(selReportZAxis));
				String tempZSubKey = CommonUtil.toNull(selReportZAxisSelectorDate) ==  null ? selReportZAxisSelectorUser : selReportZAxisSelectorDate;
				zAxis = getColumnNameByUserSelect(selReportZAxis, tempZSubKey);
				sqlBuf.append(" , ").append(zAxis);
			}
			String function = getZaxisFunction(selReportYAxisValue);
			sqlBuf.append(" , ").append(function).append("(").append(getColumnName(selReportYAxis)).append(")");	
			
			sqlBuf.append(" from ( ").append(getProcessInfoTableForOracleReport(requestBody)).append(" ) tbl");
			
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
					if (!zAxisValueList.contains(CommonUtil.toNotNull(value2)))
						zAxisValueList.add(CommonUtil.toNotNull(value2));
					
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
			
		} else if (dbType.equalsIgnoreCase("sqlserver")) {

			sqlBuf.append(" select ");
			
			//String xAxis =  (getColumnName(selReportXAxis) == null ? selReportXAxisSelectorDate == null || selReportXAxisSelectorDate == "" ? getColumnName(selReportXAxis, selReportXAxisSelectorUser) : getColumnName(selReportXAxis, selReportXAxisSelectorDate)  : getColumnName(selReportXAxis));

			String tempXSubKey = CommonUtil.toNull(selReportXAxisSelectorDate) == null ? selReportXAxisSelectorUser : selReportXAxisSelectorDate;
			
			String xAxis =  getColumnNameByUserSelect(selReportXAxis, tempXSubKey);
			sqlBuf.append(xAxis);
			String zAxis = null;
			if (!CommonUtil.isEmpty(selReportZAxis)) {
				resultColumnCount = 3;
				//zAxis =  (getColumnName(selReportZAxis) == null ? selReportZAxisSelectorDate == null || selReportZAxisSelectorDate == "" ? getColumnName(selReportZAxis, selReportZAxisSelectorUser) : getColumnName(selReportZAxis, selReportZAxisSelectorDate)  : getColumnName(selReportZAxis));
				String tempZSubKey = CommonUtil.toNull(selReportZAxisSelectorDate) ==  null ? selReportZAxisSelectorUser : selReportZAxisSelectorDate;
				zAxis = getColumnNameByUserSelect(selReportZAxis, tempZSubKey);
				sqlBuf.append(" , ").append(zAxis);
			}
			String function = getZaxisFunction(selReportYAxisValue);
			sqlBuf.append(" , ").append(function).append("(").append(getColumnName(selReportYAxis)).append(")");	
			
			sqlBuf.append(" from ( ").append(getProcessInfoTableForMsSqlReport(requestBody)).append(" ) tbl");
			
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
			//Map<String, Map<String, BigInteger>> resultMap = new HashMap<String, Map<String, BigInteger>>();
			//Map<String, Map<String, Object>> resultMap = new HashMap<String, Map<String, Object>>();
			Map<Object, Map<Object, Object>> resultMap = new HashMap<Object, Map<Object, Object>>();
			for (Iterator itr = list.iterator(); itr.hasNext();) {
				Object[] fields = (Object[]) itr.next();
				int j = 0;
				if (resultColumnCount == 2) {
				    Map<String,Object> value = new HashMap<String, Object>();
				    value.put(selReportXAxis, (String)fields[j++]);
				    //value.put(selReportYAxisValue, (BigInteger)fields[j++]);
				    value.put(selReportYAxisValue, (Object)fields[j++]);
				    values.add(value);
				} else if (resultColumnCount == 3) {
					
//					String value1 = (String)fields[j++];
//					String value2 = (String)fields[j++];
					Object value1 = (Object)fields[j++];
					Object value2 = (Object)fields[j++];
					if (!zAxisValueList.contains(CommonUtil.toNotNull(value2)))
						zAxisValueList.add(CommonUtil.toNotNull(value2));
					
					//BigInteger value3 = (BigInteger)fields[j++];
					Object value3 = (Object)fields[j++];

				    if (resultMap.get(value1) != null) {
				    	//Map<String, BigInteger> zAxisMap = resultMap.get(value1);
//				    	Map<String, Object> zAxisMap = resultMap.get(value1);
				    	Map<Object, Object> zAxisMap = resultMap.get(value1);
				    	zAxisMap.put(value2, value3);
				    } else {
				    	//Map<String, BigInteger> zAxisMap = new HashMap<String, BigInteger>();
//				    	Map<String, Object> zAxisMap = new HashMap<String, Object>();
				    	Map<Object, Object> zAxisMap = new HashMap<Object, Object>();
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
//					String key = (String)itr.next();
					Object key = (Object)itr.next();
				    value.put(selReportXAxis, key);
					//Map<String, BigInteger> zAxisMap = resultMap.get(key);
				    //Map<String, Object> zAxisMap = resultMap.get(key);
				    Map<Object, Object> zAxisMap = resultMap.get(key);
					for (int i = 0; i < zAxisValueList.size(); i++) {
					//	BigInteger tempKey = zAxisMap.get((String)zAxisValueList.get(i));
						Object tempKey = zAxisMap.get((String)zAxisValueList.get(i));
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
			
		} else if (dbType.equalsIgnoreCase("postgresql")) {
			sqlBuf.append(" select ");
			
			//String xAxis =  (getColumnName(selReportXAxis) == null ? selReportXAxisSelectorDate == null || selReportXAxisSelectorDate == "" ? getColumnName(selReportXAxis, selReportXAxisSelectorUser) : getColumnName(selReportXAxis, selReportXAxisSelectorDate)  : getColumnName(selReportXAxis));

			String tempXSubKey = CommonUtil.toNull(selReportXAxisSelectorDate) == null ? selReportXAxisSelectorUser : selReportXAxisSelectorDate;
			
			String xAxis =  getColumnNameByUserSelect(selReportXAxis, tempXSubKey);
			sqlBuf.append(xAxis);
			String zAxis = null;
			if (!CommonUtil.isEmpty(selReportZAxis)) {
				resultColumnCount = 3;
				//zAxis =  (getColumnName(selReportZAxis) == null ? selReportZAxisSelectorDate == null || selReportZAxisSelectorDate == "" ? getColumnName(selReportZAxis, selReportZAxisSelectorUser) : getColumnName(selReportZAxis, selReportZAxisSelectorDate)  : getColumnName(selReportZAxis));
				String tempZSubKey = CommonUtil.toNull(selReportZAxisSelectorDate) ==  null ? selReportZAxisSelectorUser : selReportZAxisSelectorDate;
				zAxis = getColumnNameByUserSelect(selReportZAxis, tempZSubKey);
				sqlBuf.append(" , ").append(zAxis);
			}
			String function = getZaxisFunction(selReportYAxisValue);
			sqlBuf.append(" , ").append(function).append("(").append(getColumnName(selReportYAxis)).append(")");	
			
			sqlBuf.append(" from ( ").append(getProcessInfoTableForOracleReport(requestBody)).append(" ) tbl");
			
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
			//Map<String, Map<String, BigInteger>> resultMap = new HashMap<String, Map<String, BigInteger>>();
			//Map<String, Map<String, Object>> resultMap = new HashMap<String, Map<String, Object>>();
			Map<Object, Map<Object, Object>> resultMap = new HashMap<Object, Map<Object, Object>>();
			for (Iterator itr = list.iterator(); itr.hasNext();) {
				Object[] fields = (Object[]) itr.next();
				int j = 0;
				if (resultColumnCount == 2) {
				    Map<String,Object> value = new HashMap<String, Object>();
				    value.put(selReportXAxis, (String)fields[j++]);
				    //value.put(selReportYAxisValue, (BigInteger)fields[j++]);
				    value.put(selReportYAxisValue, (Object)fields[j++]);
				    values.add(value);
				} else if (resultColumnCount == 3) {
					
//					String value1 = (String)fields[j++];
//					String value2 = (String)fields[j++];
					Object value1 = (Object)fields[j++];
					Object value2 = (Object)fields[j++];
					if (!zAxisValueList.contains(CommonUtil.toNotNull(value2)))
						zAxisValueList.add(CommonUtil.toNotNull(value2));
					
					//BigInteger value3 = (BigInteger)fields[j++];
					Object value3 = (Object)fields[j++];

				    if (resultMap.get(value1) != null) {
				    	//Map<String, BigInteger> zAxisMap = resultMap.get(value1);
//				    	Map<String, Object> zAxisMap = resultMap.get(value1);
				    	Map<Object, Object> zAxisMap = resultMap.get(value1);
				    	zAxisMap.put(value2, value3);
				    } else {
				    	//Map<String, BigInteger> zAxisMap = new HashMap<String, BigInteger>();
//				    	Map<String, Object> zAxisMap = new HashMap<String, Object>();
				    	Map<Object, Object> zAxisMap = new HashMap<Object, Object>();
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
//					String key = (String)itr.next();
					Object key = (Object)itr.next();
				    value.put(selReportXAxis, key);
					//Map<String, BigInteger> zAxisMap = resultMap.get(key);
				    //Map<String, Object> zAxisMap = resultMap.get(key);
				    Map<Object, Object> zAxisMap = resultMap.get(key);
					for (int i = 0; i < zAxisValueList.size(); i++) {
					//	BigInteger tempKey = zAxisMap.get((String)zAxisValueList.get(i));
						Object tempKey = zAxisMap.get((String)zAxisValueList.get(i));
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
		}
		
		return reportData;
	}
	
	private String getProcessInfoTableForMsSqlReport(Map<String, Object> requestBody) throws Exception {
		
		Map<String, Object> frmWorkReportMap = (Map<String, Object>)requestBody.get("frmWorkReport");
		String selTargetWorkType = (String)frmWorkReportMap.get("selTargetWorkType");
		int targetWorkType = Integer.parseInt(selTargetWorkType);
		String targetWorkId = (String)requestBody.get("targetWorkId");
		
		StringBuffer tableBuff = new StringBuffer();
		
		tableBuff.append("select result.*, case when result.recordId is null then (select count(*) from swopinion opinion where result.prcobjid = opinion.refid) when result.recordId is not null then (select count(*) from swopinion opinion where result.recordId = opinion.refid) end as opinioncount ");
		tableBuff.append("from ( ");
		tableBuff.append("select instance.*, instanceExtProp.prcValue as recordId ");
		tableBuff.append("from ( ");
		
		tableBuff.append("select	prcInst.prcObjid "); 
		tableBuff.append(" , case when prcInst.prcStatus = '2' then '진행중' when prcInst.prcStatus='3' then '완료' end as prcStatus  ");
		tableBuff.append(" , prcInst.prcTitle  ");
		tableBuff.append(" , prcInst.prccreateDate  ");
		tableBuff.append(" , convert(varchar, prccreatedate, 23) +' '+ datename(HH, prccreatedate) as prccreatedate_yyyymmddhh  ");
		tableBuff.append(" , convert(varchar, prccreatedate, 23) as prccreatedate_yyyymmdd     ");
		tableBuff.append(" , datename(yyyy, prccreatedate)+ '-' + datename(mm, prccreatedate) as prccreatedate_yyyymm  ");
		tableBuff.append(" , datename(yyyy, prccreatedate) as prccreatedate_yyyy ");
		tableBuff.append(" , datename(yyyy, prccreatedate)+ '-' + datename(mm, prccreatedate) + ' ' + datename(ww, prccreatedate) + '주' as prccreatedate_week  ");
		tableBuff.append(" , case when datename(MM, prccreatedate) between '01' and '03' then datename(yyyy, prccreatedate) + ' 1분기'   ");
		tableBuff.append(" 	when datename(MM, prccreatedate) between '04' and '06' then datename(yyyy, prccreatedate) + ' 2분기'   ");
		tableBuff.append(" 		when datename(MM, prccreatedate) between '07' and '09' then datename(yyyy, prccreatedate) + ' 3분기'  ");
		tableBuff.append(" 			when datename(MM, prccreatedate) between '10' and '12' then datename(yyyy, prccreatedate) + ' 4분기'  ");  
		tableBuff.append(" 	end as prccreatedate_quarter  ");
		tableBuff.append(" , case when datename(MM, prccreatedate) between '01' and '06' then datename(yyyy, prccreatedate) + ' 전반기'   ");
		tableBuff.append(" 	when datename(MM, prccreatedate) between '04' and '12' then datename(yyyy, prccreatedate) + ' 후반기'   ");
		tableBuff.append(" 	end as prccreatedate_half  ");
		tableBuff.append(" , usr.name as prcCreateUser  ");
		tableBuff.append(" , usr.pos as prcCreateUser_pos  ");
		tableBuff.append(" , case when usr.locale = 'ko' then '한국어' when usr.locale ='en' then '영어' end as prcCreateUser_locale  ");

		tableBuff.append(" , cast(case when usr.authid = 'ADMINISTRATOR' then '관리자' when usr.authid='USER' then '사용자' end as character varying(100)) as prcCreateUser_authId  ");
		tableBuff.append(" , dept.name as prccreateuser_dept  ");
		tableBuff.append(" , prcInst.prcmodifyDate  ");
		tableBuff.append(" , convert(varchar, prcmodifyDate, 23) +' '+ datename(HH, prcmodifyDate) as prcmodifyDate_yyyymmddhh  ");
		tableBuff.append(" , convert(varchar, prcmodifyDate, 23) as prcmodifyDate_yyyymmdd  ");
		tableBuff.append(" , datename(yyyy, prcmodifyDate)+ '-' + datename(mm, prcmodifyDate) as prcmodifyDate_yyyymm  ");
		tableBuff.append(" , datename(yyyy, prcmodifyDate) as prcmodifyDate_yyyy  ");
		tableBuff.append(" , datename(yyyy, prcmodifyDate)+ '-' + datename(mm, prcmodifyDate) + ' ' + datename(ww, prcmodifyDate) + '주' as prcmodifyDate_week  ");
		tableBuff.append(" , case when datename(MM, prcmodifyDate) between '01' and '03' then datename(yyyy, prcmodifyDate) + ' 1분기'   ");
		tableBuff.append(" 	when datename(MM, prcmodifyDate) between '04' and '06' then datename(yyyy, prcmodifyDate) + ' 2분기'   ");
		tableBuff.append(" 		when datename(MM, prcmodifyDate) between '07' and '09' then datename(yyyy, prcmodifyDate) + ' 3분기'  ");
		tableBuff.append(" 			when datename(MM, prcmodifyDate) between '10' and '12' then datename(yyyy, prcmodifyDate) + ' 4분기'    ");
		tableBuff.append(" 	end as prcmodifyDate_quarter  ");
		tableBuff.append(" , case when datename(MM, prcmodifyDate) between '01' and '06' then datename(yyyy, prcmodifyDate) + ' 전반기'   ");
		tableBuff.append(" 	when datename(MM, prcmodifyDate) between '04' and '12' then datename(yyyy, prcmodifyDate) + ' 후반기'   ");
		tableBuff.append(" 	end as prcmodifyDate_half  ");
		tableBuff.append(" , lastTask.tskname as lastTaskName  ");
		tableBuff.append(" , usr2.name as lastTaskuserName  ");
		tableBuff.append(" , usr2.pos as lastTaskuserName_pos  ");
		tableBuff.append(" , case when usr2.authid = 'ADMINISTRATOR' then '관리자' when usr2.authid='USER' then '사용자' end as lastTaskuserName_authId  ");
		tableBuff.append(" , case when usr2.locale = 'ko' then '한국어' when usr2.locale ='en' then '영어' end as lastTaskuserName_locale  ");
		tableBuff.append(" , dept2.name as lastTaskuserName_dept  ");
		tableBuff.append(" , case when prcInst.prcworkspacetype='4' then (select name from sworguser where id=prcInst.prcworkspaceid) when prcInst.prcworkspacetype='6' then (select name from sworgdept where id=prcInst.prcworkspaceid) end as workspace "); 
		tableBuff.append(" from prcprcinst prcInst, sworguser usr, sworgdept dept, sworgdept dept2,  ");
		tableBuff.append(" (  ");
		tableBuff.append(" 	select maxTask.lastTskPrcInstId, task.tskName, task.tskAssignee  ");
		tableBuff.append(" 	from (  ");
		tableBuff.append(" 		select tskPrcInstId lastTskPrcInstId , max(tskCreateDate) as createDate  ");
		tableBuff.append(" 		from tsktask  ");
		tableBuff.append(" 		where tsktype not in ('and','route','SUBFLOW','xor', 'reference', 'approval')  ");
		tableBuff.append(" 		group by tskprcinstid  ");
		tableBuff.append(" 	) maxTask,  ");
		tableBuff.append(" 	tskTask task  ");
		tableBuff.append(" 	where maxTask.createdate = task.tskcreatedate  ");
		tableBuff.append(" ) lastTask, sworguser usr2  ");
		tableBuff.append(" where 1=1 ");
		
		if (targetWorkId.equals(SmartWork.ID_ALL_WORKS)) {
			switch (targetWorkType) {
			case -1:
				//모든 업무
			break;
			case SmartWork.TYPE_INFORMATION:
				//정보관리업무만
				tableBuff.append("and prctype='SINGLE' ");
			break;
			case SmartWork.TYPE_PROCESS:
				//프로세스업무만
				tableBuff.append("and prctype='PROCESS' ");
			break;
			}
			
		} else {
			//특정업
		}
		
		tableBuff.append("and usr.id = prcInst.prccreateUser ");
		tableBuff.append("and usr.deptid = dept.id ");
		tableBuff.append("and lastTask.lastTskPrcInstId = prcInst.prcObjid ");
		tableBuff.append("and usr2.id = lastTask.tskAssignee ");
		tableBuff.append("and usr2.deptid = dept2.id ");
				
		
		tableBuff.append(") instance ");
		tableBuff.append("left outer join ");
		tableBuff.append("prcprcinstextprop instanceExtProp ");
		tableBuff.append("on instance.prcobjid = instanceExtProp.prcobjid ");
		tableBuff.append("and instanceExtProp.prcname='recordId' ");
		tableBuff.append(") result ");
		
		
		return tableBuff.toString();
		
	}
	
	
	
	private String getProcessInfoTableForOracleReport(Map<String, Object> requestBody) throws Exception {
		
		Map<String, Object> frmWorkReportMap = (Map<String, Object>)requestBody.get("frmWorkReport");
		String selTargetWorkType = (String)frmWorkReportMap.get("selTargetWorkType");
		int targetWorkType = Integer.parseInt(selTargetWorkType);
		String targetWorkId = (String)requestBody.get("targetWorkId");
		
		StringBuffer tableBuff = new StringBuffer();
		
		tableBuff.append("select result.*, case when result.recordId is null then (select count(*) from swopinion opinion where result.prcobjid = opinion.refid) when result.recordId is not null then (select count(*) from swopinion opinion where result.recordId = opinion.refid) end as opinioncount ");
		tableBuff.append("from ( ");
		tableBuff.append("select instance.*, instanceExtProp.prcValue as recordId ");
		tableBuff.append("from ( ");
		
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
		tableBuff.append("	, case when usr.locale = 'ko' then '한국어' when usr.locale ='en' then '영어' end as prcCreateUser_locale ");
		
		//tableBuff.append("	, cast(case when usr.authid = 'ADMINISTRATOR' then '관리자' when usr.authid='USER' then '사용자' end as varchar2(100)) as prcCreateUser_authId ");
		tableBuff.append("	, cast(case when usr.authid = 'ADMINISTRATOR' then '관리자' when usr.authid='USER' then '사용자' end as character varying(100)) as prcCreateUser_authId ");
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
		tableBuff.append("	, case when prcInst.prcworkspacetype='4' then (select name from sworguser where id=prcInst.prcworkspaceid) when prcInst.prcworkspacetype='6' then (select name from sworgdept where id=prcInst.prcworkspaceid) end as workspace ");
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
		
		if (targetWorkId.equals(SmartWork.ID_ALL_WORKS)) {
			switch (targetWorkType) {
			case -1:
				//모든 업무
			break;
			case SmartWork.TYPE_INFORMATION:
				//정보관리업무만
				tableBuff.append("and prctype='SINGLE' ");
			break;
			case SmartWork.TYPE_PROCESS:
				//프로세스업무만
				tableBuff.append("and prctype='PROCESS' ");
			break;
			}
			
		} else {
			//특정업
		}
		
		tableBuff.append("and usr.id = prcInst.prccreateUser ");
		tableBuff.append("and usr.deptid = dept.id ");
		tableBuff.append("and lastTask.lastTskPrcInstId = prcInst.prcObjid ");
		tableBuff.append("and usr2.id = lastTask.tskAssignee ");
		tableBuff.append("and usr2.deptid = dept2.id ");
				
		
		tableBuff.append(") instance ");
		tableBuff.append("left outer join ");
		tableBuff.append("prcprcinstextprop instanceExtProp ");
		tableBuff.append("on instance.prcobjid = instanceExtProp.prcobjid ");
		tableBuff.append("and instanceExtProp.prcname='recordId' ");
		tableBuff.append(") result ");
		
		
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
		} else if (argName.equalsIgnoreCase("creator")) {
			//생성자
			return "prccreateUser";
		} else if (argName.equalsIgnoreCase("createdTime")) {
			//생성일
			return "prccreateDate";
		} else if (argName.equalsIgnoreCase("modifier")) {
			//수정자
			return "lastTaskuserName";
		} else if (argName.equalsIgnoreCase("modifiedTime")) {
			//수정일
			return "prcmodifyDate";
		} else if (argName.equalsIgnoreCase("subInstanceCount")) {
			return "opinioncount";
		} else {
			return null;
		}
	}
	/*private String getColumnName(String argName, String argName2) {
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
	}*/
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
	
	public String getColumnNameByUserSelect(String key, String subKey) throws Exception {
		
		key = CommonUtil.toNotNull(key);
		subKey = CommonUtil.toNull(subKey);
		
		if (key.equalsIgnoreCase("status")) {
			//상태
			return "prcStatus";
		} else if (key.equalsIgnoreCase("subject")) {
			//제목
			return "prcTitle";
		} else if (key.equalsIgnoreCase("lastTask")) {
			//마지막태스크
			return "lastTaskName";
		} else if (key.equalsIgnoreCase("subinstancecount")) {
			//마지막태스크
			return "opinioncount";
		}
		
		if (key.equalsIgnoreCase("creator") && subKey != null) {
			if (subKey.equalsIgnoreCase("userName")) {
				//사용자이름
				return "prcCreateUser";
			} else if (subKey.equalsIgnoreCase("userDepartment")) {
				//사용자부서
				return "prccreateuser_dept";
			} else if (subKey.equalsIgnoreCase("userPosition")) {
				//사용자직급
				return "prcCreateUser_pos";
			} else if (subKey.equalsIgnoreCase("userLevel")) {
				//사용자레벨
				return "prcCreateUser_authId";
			} else if (subKey.equalsIgnoreCase("userLocale")) {
				//사용자언어
				return "prcCreateUser_locale";
			}
		} else if(key.equalsIgnoreCase("creator")) {
			return "prccreateUser";
		}
		
		if (key.equalsIgnoreCase("createdTime") && subKey != null) {
			if (subKey.equalsIgnoreCase("byHour")) {
				//시간별
				return "prccreatedate_yyyymmddhh";
			} else if (subKey.equalsIgnoreCase("byDay")) {
				//일별
				return "prccreatedate_yyyymmdd";
			} else if (subKey.equalsIgnoreCase("byWeek")) {
				//주별
				return "prccreatedate_week";
			} else if (subKey.equalsIgnoreCase("byMonth")) {
				//월별
				return "prccreatedate_yyyymm";
			} else if (subKey.equalsIgnoreCase("byQuarter")) {
				//분기별
				return "prccreatedate_quarter";
			} else if (subKey.equalsIgnoreCase("byHalfYear")) {
				//반기별
				return "prccreatedate_half";
			} else if (subKey.equalsIgnoreCase("byYear")) {
				//년별
				return "prccreatedate_yyyy";
			}
		} else if(key.equalsIgnoreCase("createdTime")) {
			return "prccreateDate";
		}
		
		if (key.equalsIgnoreCase("modifier") && subKey != null) {
			if (subKey.equalsIgnoreCase("userName")) {
				//사용자이름
				return "lastTaskuserName";
			} else if (subKey.equalsIgnoreCase("userDepartment")) {
				//사용자부서
				return "lastTaskuserName_dept";
			} else if (subKey.equalsIgnoreCase("userPosition")) {
				//사용자직급
				return "lastTaskuserName_pos";
			} else if (subKey.equalsIgnoreCase("userLevel")) {
				//사용자레벨
				return "lastTaskuserName_authId";
			} else if (subKey.equalsIgnoreCase("userLocale")) {
				//사용자언어
				return "lastTaskuserName_locale";
			} else {
				return null;
			}
		} else if(key.equalsIgnoreCase("modifier")) {
			return "lastTaskuserName";
		}
		
		if (key.equalsIgnoreCase("modifiedTime") && subKey != null) {
			if (subKey.equalsIgnoreCase("byHour")) {
				//시간별
				return "prcmodifyDate_yyyymmddhh";
			} else if (subKey.equalsIgnoreCase("byDay")) {
				//일별
				return "prcmodifyDate_yyyymmdd";
			} else if (subKey.equalsIgnoreCase("byWeek")) {
				//주별
				return "prcmodifyDate_week";
			} else if (subKey.equalsIgnoreCase("byMonth")) {
				//월별
				return "prcmodifyDate_yyyymm";
			} else if (subKey.equalsIgnoreCase("byQuarter")) {
				//분기별
				return "prcmodifyDate_quarter";
			} else if (subKey.equalsIgnoreCase("byHalfYear")) {
				//반기별
				return "prcmodifyDate_half";
			} else if (subKey.equalsIgnoreCase("byYear")) {
				//년별
				return "prcmodifyDate_yyyy";
			}
		} else if(key.equalsIgnoreCase("modifiedTime")) {
			return "prcmodifyDate";
		}
		return key;
	}
	
	@Override
	public RptReport getRptReport(String user, String objId, String level) throws RptException {
		if (level == null)
			level = LEVEL_ALL;
		if (level.equals(LEVEL_ALL)) {
			try {
				RptReport obj = (RptReport)get(RptReport.class, objId);
				return obj;
			} catch (Exception e) {
				throw new RptException(e);
			}
		} else {
			RptReportCond cond = new RptReportCond();
			cond.setObjId(objId);
			RptReport[] objs = this.getRptReports(user, cond, level);
			if (CommonUtil.isEmpty(objs))
				return null;
			return objs[0];
		}
	}
	@Override
	public RptReport getRptReport(String user, RptReportCond cond, String level) throws RptException {
		if (cond == null)
			return null;
		if (level == null)
			level = LEVEL_ALL;
		cond.setPageSize(2);
		RptReport[] objs = getRptReports(user, cond, level);
		if (CommonUtil.isEmpty(objs))
			return null;
		if (objs.length > 1)
			throw new RptException("More than 1 RptReport. ");
		return objs[0];
	}
	@Override
	public void setRptReport(String user, RptReport obj, String level) throws RptException {
		
		if (level == null)
			level = LEVEL_ALL;
		try {
			fill(user, obj);
			set(obj);
		} catch (Exception e) {
			logger.error(e, e);
			throw new RptException(e);
		}
		
	}
	@Override
	public void removeRptReport(String user, String objId) throws RptException {
		try {
			remove(RptReport.class, objId);
		} catch (Exception e) {
			throw new RptException(e);
		}
		
	}
	@Override
	public void removeRptReport(String user, RptReportCond cond) throws RptException {
		RptReport obj = getRptReport(user, cond, null);
		if (obj == null)
			return;
		removeRptReport(user, obj.getObjId());
	}
	private Query appendQuery(StringBuffer buf, RptReportCond cond) throws Exception {
		String objId = null;
		String owner = null;
		int targetWorkType = 0;
		String targetWorkId = null;
		
		String creationUser = null;
		Date creationDate = null;
		String modificationUser = null;
		Date modificationDate = null;

		if (cond != null) {
			objId = cond.getObjId();
			owner = cond.getOwner();
			targetWorkType = cond.getTargetWorkType();
			targetWorkId = cond.getTargetWorkId();
			creationUser = cond.getCreationUser();
			creationDate = cond.getCreationDate();
			modificationUser = cond.getModificationUser();
			modificationDate = cond.getModificationDate();
		}
		buf.append(" from RptReport obj");
		buf.append(" where obj.objId is not null");
		//TODO 시간 검색에 대한 확인 필요
		if (cond != null) {
			if (objId != null) 
				buf.append(" and obj.objId = :objId");
			if (owner != null) 
				buf.append(" and obj.owner = :owner");
			if (targetWorkType != 0)
				buf.append(" and obj.targetWorkType = :targetWorkType");
			if (targetWorkId != null) 
				buf.append(" and obj.targetWorkId = :targetWorkId");
			if (creationUser != null)
				buf.append(" and obj.creationUser = :creationUser");
			if (creationDate != null)
				buf.append(" and obj.creationDate = :creationDate");
			if (modificationUser != null)
				buf.append(" and obj.modificationUser = :modificationUser");
			if (modificationDate != null)
				buf.append(" and obj.modificationDate = :modificationDate");
		}
		this.appendOrderQuery(buf, "obj", cond);
		
		Query query = this.createQuery(buf.toString(), cond);
		if (cond != null) {
			if (objId != null)
				query.setString("objId", objId);
			if (owner != null)
				query.setString("owner", owner);
			if (targetWorkType != 0)
				query.setInteger("targetWorkType", targetWorkType);
			if (targetWorkId != null)
				query.setString("targetWorkId", targetWorkId);
			if (creationUser != null)
				query.setString("creationUser", creationUser);
			if (creationDate != null)
				query.setTimestamp("creationDate", creationDate);
			if (modificationUser != null)
				query.setString("modificationUser", modificationUser);
			if (modificationDate != null)
				query.setTimestamp("modificationDate", modificationDate);
		}
		return query;
	}
	
	@Override
	public long getRptReportSize(String user, RptReportCond cond) throws RptException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			buf.append(" count(obj)");
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			long count = ((Long)list.get(0)).longValue();
			return count;
		} catch (Exception e) {
			logger.error(e, e);
			throw new RptException(e);
		}
	}
	@Override
	public RptReport[] getRptReports(String user, RptReportCond cond, String level) throws RptException {
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
			RptReport[] objs = new RptReport[list.size()];
			list.toArray(objs);
			return objs;
		} catch (Exception e) {
			logger.error(e, e);
			throw new RptException(e);
		}
	}
	
	
	@Override
	public RptReportPane getRptReportPane(String user, String objId, String level) throws RptException {
		if (level == null)
			level = LEVEL_ALL;
		if (level.equals(LEVEL_ALL)) {
			try {
				RptReportPane obj = (RptReportPane)get(RptReportPane.class, objId);
				return obj;
			} catch (Exception e) {
				throw new RptException(e);
			}
		} else {
			RptReportPaneCond cond = new RptReportPaneCond();
			cond.setObjId(objId);
			RptReportPane[] objs = this.getRptReportPanes(user, cond, level);
			if (CommonUtil.isEmpty(objs))
				return null;
			return objs[0];
		}
	}
	@Override
	public RptReportPane getRptReportPane(String user, RptReportPaneCond cond, String level) throws RptException {
		if (cond == null)
			return null;
		if (level == null)
			level = LEVEL_ALL;
		cond.setPageSize(2);
		RptReportPane[] objs = getRptReportPanes(user, cond, level);
		if (CommonUtil.isEmpty(objs))
			return null;
		if (objs.length > 1)
			throw new RptException("More than 1 RptReportPane. ");
		return objs[0];
	}
	@Override
	public void setRptReportPane(String user, RptReportPane obj, String level) throws RptException {
		if (level == null)
			level = LEVEL_ALL;
		try {
			fill(user, obj);
			set(obj);
		} catch (Exception e) {
			logger.error(e, e);
			throw new RptException(e);
		}
	}
	@Override
	public void removeRptReportPane(String user, String objId) throws RptException {
		try {
			remove(RptReportPane.class, objId);
		} catch (Exception e) {
			throw new RptException(e);
		}
	}
	@Override
	public void removeRptReportPane(String user, RptReportPaneCond cond) throws RptException {
		RptReportPane obj = getRptReportPane(user, cond, null);
		if (obj == null)
			return;
		removeRptReportPane(user, obj.getObjId());
	}
	private Query appendQuery(StringBuffer buf, RptReportPaneCond cond) throws Exception {
		String objId = null;
		String owner = null;
		
		String creationUser = null;
		Date creationDate = null;
		String modificationUser = null;
		Date modificationDate = null;

		if (cond != null) {
			objId = cond.getObjId();
			owner = cond.getOwner();
			creationUser = cond.getCreationUser();
			creationDate = cond.getCreationDate();
			modificationUser = cond.getModificationUser();
			modificationDate = cond.getModificationDate();
		}
		buf.append(" from RptReportPane obj");
		buf.append(" where obj.objId is not null");
		//TODO 시간 검색에 대한 확인 필요
		if (cond != null) {
			if (objId != null) 
				buf.append(" and obj.objId = :objId");
			if (owner != null) 
				buf.append(" and obj.owner = :owner");
			if (creationUser != null)
				buf.append(" and obj.creationUser = :creationUser");
			if (creationDate != null)
				buf.append(" and obj.creationDate = :creationDate");
			if (modificationUser != null)
				buf.append(" and obj.modificationUser = :modificationUser");
			if (modificationDate != null)
				buf.append(" and obj.modificationDate = :modificationDate");
		}
		this.appendOrderQuery(buf, "obj", cond);
		
		Query query = this.createQuery(buf.toString(), cond);
		if (cond != null) {
			if (objId != null)
				query.setString("objId", objId);
			if (owner != null)
				query.setString("owner", owner);
			if (creationUser != null)
				query.setString("creationUser", creationUser);
			if (creationDate != null)
				query.setTimestamp("creationDate", creationDate);
			if (modificationUser != null)
				query.setString("modificationUser", modificationUser);
			if (modificationDate != null)
				query.setTimestamp("modificationDate", modificationDate);
		}
		return query;
	}
	
	@Override
	public long getRptReportPaneSize(String user, RptReportPaneCond cond) throws RptException {
		try {
			StringBuffer buf = new StringBuffer();
			buf.append("select");
			buf.append(" count(obj)");
			Query query = this.appendQuery(buf, cond);
			List list = query.list();
			long count = ((Long)list.get(0)).longValue();
			return count;
		} catch (Exception e) {
			logger.error(e, e);
			throw new RptException(e);
		}
	}
	@Override
	public RptReportPane[] getRptReportPanes(String user, RptReportPaneCond cond, String level) throws RptException {
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
			RptReportPane[] objs = new RptReportPane[list.size()];
			list.toArray(objs);
			return objs;
		} catch (Exception e) {
			logger.error(e, e);
			throw new RptException(e);
		}
	}
}
