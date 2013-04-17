package net.smartworks.server.engine.invoker.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.smartworks.server.engine.common.manager.AbstractManager;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.invoker.exception.IvkException;
import net.smartworks.server.engine.invoker.manager.IInvokerManager;
import net.smartworks.server.engine.invoker.model.Invoker;

import org.hibernate.Query;

public class InvokerManagerImpl  extends AbstractManager implements IInvokerManager {

	@Override
	public Map invoke(Invoker invoker) throws Exception {
		try {

			boolean isFunction = invoker.isFunction();
			String sql = null;
			if (isFunction) {

				String[] args = invoker.getFunctionArgs();
				
				StringBuffer sf = new StringBuffer();
				sf.append("select ").append(invoker.getFunctionName()).append("(");
				boolean isFirst = true;
				for (int i = 0; i < args.length; i++) {
					if (isFirst) {
						sf.append("'").append(args[i]).append("'");
						isFirst = false;
					} else {
						sf.append(",'").append(args[i]).append("'");
					}
				}
				sf.append(") as ").append("returnValue");
				sql = sf.toString();
			} else {
				if (CommonUtil.isEmpty(invoker.getSql()))
					return null;
				sql = invoker.getSql();
			}
			
			Query query = this.getSession().createSQLQuery(sql);
		
			List list = query.list();
			if (list == null || list.isEmpty())
				return null;
			List objList = new ArrayList();
			String[] columns = invoker.getReturnColumn();
			
			Map returnMap = new HashMap();
			if (isFunction) {
				for (Iterator itr = list.iterator(); itr.hasNext();) {
					String value = (String) itr.next();
					returnMap.put(columns[0], value);
				}
			} else {
				for (Iterator itr = list.iterator(); itr.hasNext();) {
					Object[] fields = (Object[]) itr.next();
					for (int i = 0; i < columns.length; i++) {
						Object value = fields[i];
						returnMap.put(columns[i], value);
					}
				}
			}
			
			return returnMap;	
		} catch (Exception e) {
			throw new IvkException(e);
		}
	}
}
