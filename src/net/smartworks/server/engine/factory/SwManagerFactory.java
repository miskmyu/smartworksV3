/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2011. 11. 7.
 * =========================================================
 * Copyright (c) 2011 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.factory;

//import net.smartworks.server.engine.infowork.domain.manager.ISwdManager;
import net.smartworks.server.engine.authority.manager.ISwaManager;
import net.smartworks.server.engine.autoindex.manager.IIdxManager;
import net.smartworks.server.engine.category.manager.ICtgManager;
import net.smartworks.server.engine.common.collection.manager.IColManager;
import net.smartworks.server.engine.common.loginuser.manager.ILoginUserManager;
import net.smartworks.server.engine.common.menuitem.manager.IItmManager;
import net.smartworks.server.engine.common.script.manager.ISctManager;
import net.smartworks.server.engine.common.searcher.manager.ISchManager;
import net.smartworks.server.engine.config.manager.ISwcManager;
import net.smartworks.server.engine.docfile.manager.IDocFileManager;
import net.smartworks.server.engine.folder.manager.IFdrManager;
import net.smartworks.server.engine.infowork.domain.manager.ISwdManager;
import net.smartworks.server.engine.infowork.form.manager.ISwfManager;
import net.smartworks.server.engine.like.manager.ILikeManager;
import net.smartworks.server.engine.mail.manager.IMailManager;
import net.smartworks.server.engine.message.manager.IMessageManager;
import net.smartworks.server.engine.opinion.manager.IOpinionManager;
import net.smartworks.server.engine.organization.manager.ISwoManager;
import net.smartworks.server.engine.pkg.manager.IPkgManager;
import net.smartworks.server.engine.process.approval.manager.IAprManager;
import net.smartworks.server.engine.process.deploy.manager.IDepManager;
import net.smartworks.server.engine.process.link.manager.ILnkManager;
import net.smartworks.server.engine.process.monitoring.manager.IMonManager;
import net.smartworks.server.engine.process.process.manager.IPrcManager;
import net.smartworks.server.engine.process.task.manager.ITskManager;
import net.smartworks.server.engine.publishnotice.manager.IPublishNoticeManager;
import net.smartworks.server.engine.report.manager.IReportManager;
import net.smartworks.server.engine.resource.manager.IResourceDesigntimeManager;
import net.smartworks.server.engine.resource.manager.IResourceRuntimeManager;
import net.smartworks.server.engine.scheduling.manager.impl.TimerManager;
import net.smartworks.server.engine.sera.manager.ISeraManager;
import net.smartworks.server.engine.worklist.manager.impl.WorkListManagerImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pro.ucity.manager.ucityWorkList.manager.IUcityWorkListManager;
import pro.ucity.sso.manager.ISsoManager;

public class SwManagerFactory {

	private static SwManagerFactory factory;
	
	protected final Log logger = LogFactory.getLog(getClass());
	private ILnkManager lnkManager;
	private IColManager colManager;
	private ITskManager tskManager;
	private IDepManager depManager;
	private IAprManager aprManager;
	private IPrcManager prcManager;
	private ISwdManager swdManager;
	private ISwfManager swfManager;
	private ISwoManager swoManager;
	private IMonManager monManager;
	private IPkgManager pkgManager;
	private ICtgManager ctgManager;
	private IDocFileManager docManager;
	private ISwaManager swaManager;
	private IItmManager itmManager;
	private ISchManager schManager;
	private ISwcManager swcManager;
	private ISctManager sctManager;
	private IFdrManager fdrManager;
	private IOpinionManager opinionManager;
	private ILikeManager likeManager;
	private IMessageManager messageManager;
	private ILoginUserManager loginUserManager;
	private IResourceDesigntimeManager designtimeManager;
	private IResourceRuntimeManager runtimeManager;
	private IPublishNoticeManager publishNoticeManager;
	private IMailManager mailManager;
	private IIdxManager autoIndexManager;
	private IReportManager reportManager;
	private TimerManager timerManager;
	
	public TimerManager getTimerManager() {
		return timerManager;
	}
	public void setTimerManager(TimerManager timerManager) {
		this.timerManager = timerManager;
	}
	//UCITY
	private ISsoManager ssoManager;
	private IUcityWorkListManager ucityWorkListManager;

	public ISsoManager getSsoManager() {
		return ssoManager;
	}
	public void setSsoManager(ISsoManager ssoManager) {
		this.ssoManager = ssoManager;
	}
	public IUcityWorkListManager getUcityWorkListManager() {
		return ucityWorkListManager;
	}
	public void setUcityWorkListManager(IUcityWorkListManager ucityWorkListManager) {
		this.ucityWorkListManager = ucityWorkListManager;
	}
	//SERA
	private ISeraManager seraManager;

	public IReportManager getReportManager() {
		return reportManager;
	}
	public void setReportManager(IReportManager reportManager) {
		this.reportManager = reportManager;
	}
	public ISeraManager getSeraManager() {
		return seraManager;
	}
	public void setSeraManager(ISeraManager seraManager) {
		this.seraManager = seraManager;
	}
	private WorkListManagerImpl workListManager;

	public WorkListManagerImpl getWorkListManager() {
		return workListManager;
	}
	public void setWorkListManager(WorkListManagerImpl workListManager) {
		this.workListManager = workListManager;
	}
	
//	private IChtManager chtManager;
//	private IMdlManager mdlManager;
//	private ILcsManager lcsManager;
//	private IUpdManager updManager;
	//private IRepManager repManager;

	public ISwaManager getSwaManager() {
		return swaManager;
	}
	public void setSwaManager(ISwaManager swaManager) {
		this.swaManager = swaManager;
	}
	public IDocFileManager getDocManager() {
		return docManager;
	}
	public void setDocManager(IDocFileManager docManager) {
		this.docManager = docManager;
	}
	public SwManagerFactory() {
		super();
		if (logger.isInfoEnabled())
			logger.info(this.getClass().getName() + " created");
	}
	public synchronized static SwManagerFactory createInstance() {
		if(factory == null) 
			factory = new SwManagerFactory();
		return factory;
	}
	public IDepManager getDepManager() {
		return depManager;
	}
	public void setDepManager(IDepManager depManager) {
		this.depManager = depManager;
	}
	public IColManager getColManager() {
		return colManager;
	}
	public void setColManager(IColManager colManager) {
		this.colManager = colManager;
	}
	public ISwfManager getSwfManager() {
		return swfManager;
	}
	public void setSwfManager(ISwfManager swfManager) {
		this.swfManager = swfManager;
	}
	public static SwManagerFactory getInstance() {
		return factory;
	}
	public ILnkManager getLnkManager() {
		return lnkManager;
	}
	public void setLnkManager(ILnkManager lnkManager) {
		this.lnkManager = lnkManager;
	}
	public ITskManager getTskManager() {
		return tskManager;
	}
	public void setTskManager(ITskManager tskManager) {
		this.tskManager = tskManager;
	}
	public ISwoManager getSwoManager() {
		return swoManager;
	}
	public void setSwoManager(ISwoManager swoManager) {
		this.swoManager = swoManager;
	}
	public IPrcManager getPrcManager() {
		return prcManager;
	}
	public void setPrcManager(IPrcManager prcManager) {
		this.prcManager = prcManager;
	}
	public IAprManager getAprManager() {
		return aprManager;
	}
	public void setAprManager(IAprManager aprManager) {
		this.aprManager = aprManager;
	}
	public ISwdManager getSwdManager() {
		return swdManager;
	}
	public void setSwdManager(ISwdManager swdManager) {
		this.swdManager = swdManager;
	}
	public IMonManager getMonManager() {
		return monManager;
	}
	public void setMonManager(IMonManager monManager) {
		this.monManager = monManager;
	}
	public IPkgManager getPkgManager() {
		return pkgManager;
	}
	public void setPkgManager(IPkgManager pkgManager) {
		this.pkgManager = pkgManager;
	}
	public ICtgManager getCtgManager() {
		return ctgManager;
	}
	public void setCtgManager(ICtgManager ctgManager) {
		this.ctgManager = ctgManager;
	}
	public IItmManager getItmManager() {
		return itmManager;
	}
	public void setItmManager(IItmManager itmManager) {
		this.itmManager = itmManager;
	}
	public ISchManager getSchManager() {
		return schManager;
	}
	public void setSchManager(ISchManager schManager) {
		this.schManager = schManager;
	}
	public ISwcManager getSwcManager() {
		return swcManager;
	}
	public void setSwcManager(ISwcManager swcManager) {
		this.swcManager = swcManager;
	}
	public ISctManager getSctManager() {
		return sctManager;
	}
	public void setSctManager(ISctManager sctManager) {
		this.sctManager = sctManager;
	}
	public IFdrManager getFdrManager() {
		return fdrManager;
	}
	public void setFdrManager(IFdrManager fdrManager) {
		this.fdrManager = fdrManager;
	}
	public IOpinionManager getOpinionManager() {
		return opinionManager;
	}
	public void setOpinionManager(IOpinionManager opinionManager) {
		this.opinionManager = opinionManager;
	}
	public ILikeManager getLikeManager() {
		return likeManager;
	}
	public void setLikeManager(ILikeManager likeManager) {
		this.likeManager = likeManager;
	}
	public ILoginUserManager getLoginUserManager() {
		return loginUserManager;
	}
	public void setLoginUserManager(ILoginUserManager loginUserManager) {
		this.loginUserManager = loginUserManager;
	}
	public IResourceDesigntimeManager getDesigntimeManager() {
		return designtimeManager;
	}
	public void setDesigntimeManager(IResourceDesigntimeManager designtimeManager) {
		this.designtimeManager = designtimeManager;
	}
	public IResourceRuntimeManager getRuntimeManager() {
		return runtimeManager;
	}
	public void setRuntimeManager(IResourceRuntimeManager runtimeManager) {
		this.runtimeManager = runtimeManager;
	}
	public IMessageManager getMessageManager() {
		return messageManager;
	}
	public void setMessageManager(IMessageManager messageManager) {
		this.messageManager = messageManager;
	}
	public IPublishNoticeManager getPublishNoticeManager() {
		return publishNoticeManager;
	}
	public void setPublishNoticeManager(IPublishNoticeManager publishNoticeManager) {
		this.publishNoticeManager = publishNoticeManager;
	}
	public IMailManager getMailManager() {
		return mailManager;
	}
	public void setMailManager(IMailManager mailManager) {
		this.mailManager = mailManager;
	}
	public IIdxManager getAutoIndexManager() {
		return autoIndexManager;
	}
	public void setAutoIndexManager(IIdxManager autoIndexManager) {
		this.autoIndexManager = autoIndexManager;
	}

//	public IChtManager getChtManager() {
//		return chtManager;
//	}
//	public void setChtManager(IChtManager chtManager) {
//		this.chtManager = chtManager;
//	}
//	public IMdlManager getMdlManager() {
//		return mdlManager;
//	}
//	public void setMdlManager(IMdlManager mdlManager) {
//		this.mdlManager = mdlManager;
//	}
////	public IRepManager getRepManager() {
////		return repManager;
////	}
////	public void setRepManager(IRepManager repManager) {
////		this.repManager = repManager;
////	}
//	public ILcsManager getLcsManager() {
//		return lcsManager;
//	}
//	public void setLcsManager(ILcsManager lcsManager) {
//		this.lcsManager = lcsManager;
//	}
//	public IUpdManager getUpdManager() {
//		return updManager;
//	}
//	public void setUpdManager(IUpdManager updManager) {
//		this.updManager = updManager;
//	}
}
