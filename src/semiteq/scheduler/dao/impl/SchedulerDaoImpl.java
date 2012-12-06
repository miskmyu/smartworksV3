/*	
 * $Id: SchedulerDaoImpl.java,v 1.1 2012/01/18 02:13:56 hsshin Exp $
 * created by    : SHIN HYUN SEONG
 * creation-date : 2011. 12. 23.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package semiteq.scheduler.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.jdbc.object.SqlUpdate;

import semiteq.scheduler.dao.SchedulerDao;
import semiteq.scheduler.model.PurchaseModel;
import semiteq.scheduler.model.RequestModel;

/**
 * @spring.bean id="schedulerDao"
 * @spring.property name="dataSource" ref="dataSource"
 */
public class SchedulerDaoImpl extends JdbcDaoSupport implements SchedulerDao {

	private GetPurchaseNewData getPurchaseNewData;
	private GetRequestNewData getRequestNewData;
	private UpdatePurchaseSelectStatus updatePurchaseSelectStatus;
	private UpdateRequestSelectStatus updateRequestSelectStatus;

	protected void initDao() throws Exception {
		this.getPurchaseNewData = new GetPurchaseNewData(getDataSource());
		this.getRequestNewData = new GetRequestNewData(getDataSource());
		this.updatePurchaseSelectStatus = new UpdatePurchaseSelectStatus(getDataSource());
		this.updateRequestSelectStatus = new UpdateRequestSelectStatus(getDataSource());
	}

	// 발주등록 프로세스 신규 데이터 조회
	public List getPurchaseNewData() throws DataAccessException {
		return this.getPurchaseNewData.execute();
	}
	protected class GetPurchaseNewData extends MappingSqlQuery {
		protected GetPurchaseNewData(DataSource ds) {
			super(ds,
					"	SELECT	PO_NO, 		REQ_PRSN, 		PO_DT,			BP_NM,		DLVY_DT,		" +
					"			PO_DOC_AMT, VAT_LOC_AMT,	TOT_LOC_AMT,	PAY_Meth,	REQ_DOC_PATH,	" +
					"		    INSRT_USER_ID															" +
					"     FROM  MX_PURCHASE_IF_KO578													" +
					"	 WHERE 	CON_FLAG = 'N' 															"
			);

			compile();
		}
		protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

			PurchaseModel purchaseModel = new PurchaseModel();
			purchaseModel.setPoNo(rs.getString("PO_NO"));
			purchaseModel.setReqPrsn(rs.getString("REQ_PRSN"));
			purchaseModel.setPoDt(rs.getDate("PO_DT"));
			purchaseModel.setBpNm(rs.getString("BP_NM"));
			purchaseModel.setDlvyDt(rs.getDate("DLVY_DT"));
			purchaseModel.setPoDocAmt(rs.getBigDecimal("PO_DOC_AMT"));
			purchaseModel.setVatLocAmt(rs.getBigDecimal("VAT_LOC_AMT"));
			purchaseModel.setTotLocAmt(rs.getBigDecimal("TOT_LOC_AMT"));
			purchaseModel.setPayMeth(rs.getString("PAY_Meth"));
			purchaseModel.setReqDocPath(rs.getString("REQ_DOC_PATH"));
			purchaseModel.setInsertUserId(rs.getString("INSRT_USER_ID"));

			return purchaseModel;
		}
	}

	//S-Part 구매요청 접수/승인 프로세스 신규 데이터 조회
	public List getRequestNewData() throws DataAccessException {
		return this.getRequestNewData.execute();
	}
	protected class GetRequestNewData extends MappingSqlQuery {
		protected GetRequestNewData(DataSource ds) {
			super(ds,
					"	SELECT	PR_NO, REQ_PRSN, REQ_DEPT, BP_CD, BP_PRSN_NM,		" +
					"			TEL_NO1, FAX_NO, REQ_DT, REQ_DOC_PATH, 				" +
					"		    INSRT_USER_ID										" +
					"     FROM  MX_REQUEST_IF_KO578									" +
					"	 WHERE 	CON_FLAG = 'N' 										"
			);

			compile();
		}
		protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

			RequestModel requestModel = new RequestModel();
			requestModel.setPrNo(rs.getString("PR_NO"));
			requestModel.setReqPrsn(rs.getString("REQ_PRSN"));
			requestModel.setReqDept(rs.getString("REQ_DEPT"));
			requestModel.setBpCd(rs.getString("BP_CD"));
			requestModel.setBpPrsnNm(rs.getString("BP_PRSN_NM"));
			requestModel.setTelNo1(rs.getString("TEL_NO1"));
			requestModel.setFaxNo(rs.getString("FAX_NO"));
			requestModel.setReqDt(rs.getDate("REQ_DT"));
			requestModel.setReqDocPath(rs.getString("REQ_DOC_PATH"));
			requestModel.setInsertUserId(rs.getString("INSRT_USER_ID"));

			return requestModel;
		}
	}

	// 발주등록 프로세스 데이터 조회여부 상태 값 업데이트
	public void updatePurchaseSelectStatus(PurchaseModel purchaseModel) throws DataAccessException {
		this.updatePurchaseSelectStatus.update(purchaseModel);
	}
	protected class UpdatePurchaseSelectStatus extends SqlUpdate {
		protected UpdatePurchaseSelectStatus(DataSource ds) {
		    super(ds,
		    		" 	UPDATE 	MX_PURCHASE_IF_KO578	" +
		    		"      SET 	CON_FLAG = 'Q' 			" +		
		    		"  	 WHERE 	PO_NO = ? 	    		"
		    );

			declareParameter(new SqlParameter(Types.VARCHAR));

			compile();
		}
		protected int update(PurchaseModel purchaseModel) throws DataAccessException {
		    return this.update(new Object[]{
	    		purchaseModel.getPoNo()
		    });
		}
	}

	//S-Part 구매요청 접수/승인 프로세스 데이터 조회여부 상태 값 업데이트
	public void updateRequestSelectStatus(RequestModel requestModel) throws DataAccessException {
		this.updateRequestSelectStatus.update(requestModel);
	}
	protected class UpdateRequestSelectStatus extends SqlUpdate {
		protected UpdateRequestSelectStatus(DataSource ds) {
		    super(ds,
		    		" 	UPDATE 	MX_REQUEST_IF_KO578		" +
		    		"      SET 	CON_FLAG = 'Q' 			" +
		    		"  	 WHERE 	PR_NO = ? 	    		"
		    );

			declareParameter(new SqlParameter(Types.VARCHAR));

			compile();
		}
		protected int update(RequestModel requestModel) throws DataAccessException {
		    return this.update(new Object[]{
		    		requestModel.getPrNo()
		    });
		}
	}

}