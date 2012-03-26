package net.smartworks.server.engine.common.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.smartworks.server.engine.common.model.Cond;
import net.smartworks.server.engine.common.model.MisObject;
import net.smartworks.server.engine.common.model.Order;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.util.LocalDate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.engine.SessionFactoryImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public abstract class AbstractManager extends HibernateDaoSupport implements IManager {
	protected final Log logger = LogFactory.getLog(getClass());
	
	private static final String SPACE = CommonUtil.SPACE;
	private static final String DOT = CommonUtil.DOT;
	private static final String COMMA = CommonUtil.COMMA;
	private static final String ORDERBY = " order by";
	private static final String ASC = " asc";
	private static final String DESC = " desc";
	private static final String CASE = " case";
	private static final String WHEN = " when";
	private static final String THEN = " then";
	private static final String END = " end";
	private static final String ELSE = " else";
	private static final String DECODE = " decode";
	
	
	private boolean enableLogging = true;
	private String dbType;

	@Autowired
	public void anyMethod(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	public AbstractManager() {
		super();
		if (logger.isInfoEnabled())
			logger.info(this.getClass().getName() + " created");
	}
	protected void fill(String user, MisObject obj) throws Exception {
		if (obj == null)
			return;
		//date to localdate - Date date = new Date();
		LocalDate date = new LocalDate();
		obj.setModificationUser(user);
		obj.setModificationDate(date);
		if (obj.getCreationDate() == null) {
			obj.setCreationUser(user);
			obj.setCreationDate(date);
		}
	}
	protected void appendOrderQuery(StringBuffer buf, String objName, Cond cond) {
		if (cond == null)
			return;
		Order[] orders = cond.getOrders();
		if (orders == null || orders.length == 0)
			return;
		boolean first = true;
		boolean whenExist = false;
		for (int i=0; i<orders.length; i++) {
			Order order = orders[i];
			String whenColumnName = order.getWhenColumnName();
			String whenColumnValue = order.getWhenColumnValue();
			if (!CommonUtil.isEmpty(whenColumnName)) {
				if (whenColumnName.indexOf(DOT) == -1 && !CommonUtil.isEmpty(objName))
					whenColumnName = new StringBuffer(objName).append(DOT).append(whenColumnName).toString();
				if(this.getDbType().equalsIgnoreCase("sqlserver")) {
					if (first) {
						buf.append(ORDERBY).append(CASE);
						buf.append(SPACE).append(whenColumnName).append(WHEN).append(SPACE).append("'").append(whenColumnValue).append("'");
						first = false;
					} else {
						buf.append(WHEN).append(SPACE).append("'").append(whenColumnValue).append("'");
					}
					buf.append(THEN).append(SPACE).append(i);
				} else if(this.getDbType().equalsIgnoreCase("oracle")) {
					if (first) {
						buf.append(ORDERBY).append(DECODE).append("(");
						buf.append(whenColumnName).append(COMMA).append("'").append(whenColumnValue).append("'");
						first = false;
					} else {
						buf.append(COMMA).append("'").append(whenColumnValue).append("'");
					}
					buf.append(COMMA).append(i);
				}
				whenExist = true;
			} else {
				if(whenExist) {
					if(this.getDbType().equalsIgnoreCase("sqlserver")) {
						buf.append(ELSE).append(SPACE).append(i);
						buf.append(END);
					} else if(this.getDbType().equalsIgnoreCase("oracle")) {
						buf.append(COMMA).append(i).append(")");
					}
				}
				String field = order.getField();
				if (CommonUtil.isEmpty(field))
					continue;
				if (field.indexOf(DOT) == -1 && !CommonUtil.isEmpty(objName))
					field = new StringBuffer(objName).append(DOT).append(field).toString();
				boolean isAsc = order.isAsc();
				if (first) {
					buf.append(ORDERBY);
					first = false;
				} else {
					buf.append(COMMA);
				}
				buf.append(SPACE).append(field);
				if (isAsc) {
					buf.append(ASC);
				} else {
					buf.append(DESC);
				}
			}
		}
	}
	protected List executeQuery(String qStr, Cond cond) throws Exception {
		Query query = this.createQuery(qStr, cond);
		List list = query.list();
		return list;
	}
	protected void evict(List list) throws Exception {
		if (CommonUtil.isEmpty(list))
			return;
		for (Iterator itr = list.iterator(); itr.hasNext();)
			this.getSession().evict(itr.next());
	}
	public Query createQuery(String qStr, Cond cond) throws Exception {
		Query query = this.getSession().createQuery(qStr);
		this.paging(query, cond);
		return query;
	}
	public Query createSqlQuery(String qStr, Cond cond) throws Exception {
		Query query = this.getSession().createSQLQuery(qStr);
		this.paging(query, cond);
		return query;
	}
	private void paging(Query query, Cond cond) throws Exception {
		if (query == null || cond == null)
			return;
		int pageSize = cond.getPageSize();
		int pageNo = cond.getPageNo();
		if (pageSize < 1 || pageNo < 0)
			return;
		query.setFirstResult(pageNo * pageSize);
		query.setMaxResults(pageSize);
	}
	protected Object get(Class cls, String objId) throws Exception {
		Object obj = null;
		try {
			obj = this.getHibernateTemplate().get(cls, objId);
		} catch (HibernateObjectRetrievalFailureException e) {
			Throwable t = e.getCause();
			if (!(t instanceof ObjectNotFoundException))
				throw e;
		}
		this.getSession().evict(obj);
		return obj;
	}
	protected Object get(Class cls, Map map) throws Exception {
		DetachedCriteria cond = DetachedCriteria.forClass(cls);
		if (map != null && !map.isEmpty()) {
			Set keySet = map.keySet();
			for (Iterator keyItr = keySet.iterator(); keyItr.hasNext();) {
				String key = (String)keyItr.next();
				Object value = map.get(key);
				if (value == null)
					continue;
				cond.add(Property.forName(key).eq(value));
			}
		}
		List list = this.getHibernateTemplate().findByCriteria(cond);
		if (list == null || list.isEmpty())
			return null;
		Object obj = list.get(0);
		return obj;
	}
	protected Object create(Object obj) throws Exception {
		if (obj == null)
			return null;
		this.getHibernateTemplate().save(obj);
		this.getHibernateTemplate().flush();
		return obj;
	}
	protected Object[] set(Object[] objs) throws Exception {
		if (objs == null || objs.length == 0)
			return null;
		for (int i=0; i<objs.length; i++)
			set(objs[i]);
		return objs;
	}
	protected Object merge(Object obj) throws Exception {
		if (obj == null)
			return null;
		this.getHibernateTemplate().merge(obj);
		this.getHibernateTemplate().flush();
		return obj;
	}
	protected Object set(Object obj) throws Exception {
		if (obj == null)
			return null;
		this.getHibernateTemplate().saveOrUpdate(obj);
		this.getHibernateTemplate().flush();
		return obj;
	}
	protected Object update(Object obj) throws Exception {
		if (obj == null)
			return null;
		this.getHibernateTemplate().update(obj);
		this.getHibernateTemplate().flush();
		return obj;
	}
	protected void remove(String tableName, net.smartworks.server.engine.common.model.Property[] objIdProps) throws Exception {
		if (CommonUtil.isEmpty(tableName) || CommonUtil.isEmpty(objIdProps))
			return;
		StringBuffer buf = new StringBuffer("delete from ").append(tableName).append(" where ");
		net.smartworks.server.engine.common.model.Property objIdProp;
		Map paramValueMap = new HashMap();
		String name;
		String value;
		String param;
		for (int i=0; i<objIdProps.length; i++) {
			objIdProp = objIdProps[i];
			name = objIdProp.getName();
			value = objIdProp.getValue();
			if (CommonUtil.isEmpty(name) || CommonUtil.isEmpty(value))
				return;
			if (i != 0)
				buf.append(" and ");
			param = "a" + i;
			paramValueMap.put(param, value);
			buf.append(name).append(" = :").append(param);
		}
		Query query = this.getSession().createSQLQuery(buf.toString());
		for (Iterator paramItr = paramValueMap.keySet().iterator(); paramItr.hasNext();) {
			param = (String)paramItr.next();
			value = (String)paramValueMap.get(param);
			query.setString(param, value);
		}
		query.executeUpdate();
	}
	protected Object remove(Class cls, String objId) throws Exception {
		if (objId == null)
			return null;
		Object obj = this.get(cls, objId);
		if (obj == null)
			return null;
		this.getHibernateTemplate().delete(obj);
		this.getHibernateTemplate().flush();
		return obj;
	}
	protected void removeAll(Object[] objs) throws Exception {
		if (objs == null || objs.length == 0)
			return;
		List list = CommonUtil.toList(objs);
		this.getHibernateTemplate().deleteAll(list);
	}
	public boolean isEnableLogging() {
		return enableLogging;
	}
	public void setEnableLogging(boolean enableLogging) {
		this.enableLogging = enableLogging;
	}
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
}