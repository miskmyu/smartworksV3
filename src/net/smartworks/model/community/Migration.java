/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 8. 28.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.model.community;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.model.Order;
import net.smartworks.server.engine.common.model.Property;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.infowork.domain.model.SwdRecord;
import net.smartworks.server.engine.infowork.domain.model.SwdRecordCond;
import net.smartworks.server.engine.process.task.model.TskTask;
import net.smartworks.server.engine.process.task.model.TskTaskCond;

public class Migration {

	public void test() throws Exception{
		String domainId = "md_b0ee0f7b7a3e479a9790890c7afcab25";
		String userId = "kmyu@maninsoft.co.kr";
		String fileFieldId = "1";
		boolean isAllPdf = true;
		Map nonPdfFileInfoMap = new HashMap();
		
		
		SwdRecordCond cond = new SwdRecordCond();
		cond.setDomainId(domainId);
		
		SwdRecord[] records = SwManagerFactory.getInstance().getSwdManager().getRecords(userId, cond, IManager.LEVEL_ALL);
		
		if (records == null)
			return;
		
		for (int i = 0; i < records.length; i++) {
			SwdRecord record = records[i];
			
			TskTaskCond taskCond = new TskTaskCond();
			taskCond.setExtendedProperties(new Property[]{new Property("recordId", record.getRecordId())});
			taskCond.setOrders(new Order[]{new Order("tskCreateDate", true)});
			TskTask[] task = SwManagerFactory.getInstance().getTskManager().getTasks(userId, taskCond, IManager.LEVEL_LITE);
			
			if (task == null)
				continue;
			
			String taskId = task[0].getObjId();
			String groupId = record.getDataFieldValue(fileFieldId);
			
			String fileName = StringUtils.replace(groupId, "fg_", "");
			String fileExt = ".pdf";
			if (!isAllPdf) {
				String tempFileExt = (String)nonPdfFileInfoMap.get(fileName);
				if (!CommonUtil.isEmpty(tempFileExt))
					fileExt = "." + tempFileExt;
			}
			String fileId = "file_" + fileName;
			
			SwManagerFactory.getInstance().getDocManager().insertFiles("", taskId, groupId, fileName, fileName+fileExt, "0");
			
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
