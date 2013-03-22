package net.smartworks.server.engine.common.model;

import net.smartworks.server.engine.common.util.XmlUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SwmProduct extends MisObject {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(SwmProduct.class);

	protected static final String PREFIX = "Swm";
	private static final String NAME = net.smartworks.server.engine.common.util.CommonUtil.toName(SwmProduct.class, PREFIX);
	
	public static final String A_TYPE = "type";
	public static final String A_COMPANY = "company";
	public static final String A_PRICE = "price";
	public static final String A_SCORE = "score";
	public static final String A_VALUE = "value";
	public static final String A_MAINIMG = "mainImg";
	public static final String A_EXTVALUE = "extValue";
	public static final String A_BUSINESSTYPECTGID = "businessTypeCtgId";
	public static final String A_BUSINESSCTGID = "businessCtgId";
	public static final String A_HITCOUNT = "hitCount";
	public static final String A_DOWNCOUNT = "downCount";
	public static final String A_ISPUBLISHED = "isPublished";
	public static final String A_PRODUCTCODE = "productCode";
	public static final String A_PACKAGERELS = "packageRels";
	public static final String A_PRODUCTPROPERTY = "productProperty";
	public static final String A_PRODUCTPROPERTIES = "productProperties";
	
	private String type;
	private String company;
	private String price;
	private double score;
	private String value;
	private String mainImg;
	private String extValue;
	private String businessTypeCtgId;
	private String businessCtgId;
	private long hitCount;
	private long downCount;
	private String isPublished;
	private String productCode;
	private String packageRels;
	
	private SwmProductProperty[] productProperties;
	
	public SwmProduct() {
		super();
	}

	public String toString(String name, String tab){
		if(name == null || name.trim().length() == 0)
			name = NAME;
		return super.toString(name, tab);
	}
	public String toAttributesString() {
		StringBuffer buf = new StringBuffer();
		buf.append(super.toAttributesString());
		appendAttributeString(A_TYPE, type, buf);
		appendAttributeString(A_COMPANY, company, buf);
		appendAttributeString(A_PRICE, price, buf);
		appendAttributeString(A_SCORE, score, buf);
		appendAttributeString(A_MAINIMG, mainImg, buf);
		appendAttributeString(A_BUSINESSTYPECTGID, businessTypeCtgId, buf);
		appendAttributeString(A_BUSINESSCTGID, businessCtgId, buf);
		appendAttributeString(A_HITCOUNT, hitCount, buf);
		appendAttributeString(A_DOWNCOUNT, downCount, buf);
		appendAttributeString(A_ISPUBLISHED, isPublished, buf);
		appendAttributeString(A_PRODUCTCODE, productCode, buf);
		return buf.toString();
	}
	public String toElementsString(String tab) {
		StringBuffer buf = new StringBuffer();
		buf.append(super.toElementsString(tab));
		appendElementString(A_VALUE, getValue(), tab, true, buf);
		appendElementString(A_PACKAGERELS, getPackageRels(), tab, true, buf);
		appendElementString(A_EXTVALUE, getExtValue(), tab, true, buf);
		appendElementsString(A_PRODUCTPROPERTIES, A_PRODUCTPROPERTY, getProductProperties(), tab, buf);
		return buf.toString();
	}
	public static BaseObject toObject(Node node, BaseObject baseObj) throws Exception {
		if (node == null)
			return null;
		SwmProduct obj = null;
		if (baseObj == null || !(baseObj instanceof SwmProduct))
			obj = new SwmProduct();
		else
			obj = (SwmProduct)baseObj;
		
		MisObject.toObject(node, obj);
		
		NamedNodeMap attrMap = node.getAttributes();
		if (attrMap != null) {
			Node type = attrMap.getNamedItem(A_TYPE);
			Node company = attrMap.getNamedItem(A_COMPANY);
			Node price = attrMap.getNamedItem(A_PRICE);
			Node score = attrMap.getNamedItem(A_SCORE);
			Node mainImg = attrMap.getNamedItem(A_MAINIMG);
			Node businessTypeCtgId = attrMap.getNamedItem(A_BUSINESSTYPECTGID);
			Node businessCtgId = attrMap.getNamedItem(A_BUSINESSCTGID);
			Node hitCount = attrMap.getNamedItem(A_HITCOUNT);
			Node downCount = attrMap.getNamedItem(A_DOWNCOUNT);
			Node isPublished = attrMap.getNamedItem(A_ISPUBLISHED);
			Node productCode = attrMap.getNamedItem(A_PRODUCTCODE);
			if (type != null)
				obj.setType(type.getNodeValue());
			if (company != null)
				obj.setCompany(company.getNodeValue());
			if (price != null)
				obj.setPrice(price.getNodeValue());
			if (score != null)
				obj.setScore(Double.parseDouble(score.getNodeValue()));
			if (mainImg != null)
				obj.setMainImg(mainImg.getNodeValue());
			if (businessTypeCtgId != null)
				obj.setBusinessTypeCtgId(businessTypeCtgId.getNodeValue());
			if (businessCtgId != null)
				obj.setBusinessCtgId(businessCtgId.getNodeValue());
			if (hitCount != null)
				obj.setHitCount(Long.parseLong(hitCount.getNodeValue()));
			if (downCount != null)
				obj.setDownCount(Long.parseLong(downCount.getNodeValue()));
			if (isPublished != null)
				obj.setIsPublished(isPublished.getNodeValue());
			if (productCode != null)
				obj.setProductCode(productCode.getNodeValue());
		}
		NodeList childNodeList = node.getChildNodes();
		if (childNodeList == null || childNodeList.getLength() == 0)
			return obj;
		for (int i=0; i<childNodeList.getLength(); i++) {
			Node childNode = childNodeList.item(i);
			if (childNode.getNodeType() != Node.ELEMENT_NODE || childNode.getNodeName() == null)
				continue;
			if (childNode.getNodeName().equals(A_VALUE)) {
				obj.setValue(getNodeValue(childNode, true));
			} else if (childNode.getNodeName().equals(A_EXTVALUE)) {
				obj.setExtValue(getNodeValue(childNode, true));
			} else if (childNode.getNodeName().equals(A_PACKAGERELS)) {
				obj.setPackageRels(getNodeValue(childNode, true));
			} else if (childNode.getNodeName().equals(A_PRODUCTPROPERTIES)) {
				Node[] nodes = getNodes(childNode);
				if (nodes == null || nodes.length == 0)
					continue;
				SwmProductProperty[] objs = new SwmProductProperty[nodes.length];
				for (int j=0; j<nodes.length; j++)
					objs[j] = (SwmProductProperty)SwmProductProperty.toObject(nodes[j], null);
				obj.setProductProperties(objs);
			} else if (childNode.getNodeName().equals(A_PRODUCTPROPERTY)) {
				obj.addProductProperty((SwmProductProperty)SwmProductProperty.toObject(childNode, null));
			}
		}
		return obj;
	}

	public static BaseObject toObject(String str) throws Exception {
		if (str == null)
			return null;
		Document doc = XmlUtil.toDocument(str);
		if (doc == null)
			return null;
		return toObject(doc.getDocumentElement(), null);
	}
	public static SwmProduct[] add(SwmProduct[] objs, SwmProduct obj) {
		if (obj == null)
			return objs;
		int size = 0;
		if (objs != null)
			size = objs.length;
		SwmProduct[] newObjs = new SwmProduct[size+1];
		int i;
		for (i=0; i<size; i++)
			newObjs[i] = objs[i];
		newObjs[i] = obj;
		return newObjs;
	}
	public static SwmProduct[] remove(SwmProduct[] objs, SwmProduct obj) {
		if (obj == null)
			return objs;
		int size = 0;
		if (objs != null)
			size = objs.length;
		if (size == 0)
			return objs;
		SwmProduct[] newObjs = new SwmProduct[size-1];
		int i;
		int j = 0;
		for (i=0; i<size; i++) {
			if (objs[i].equals(obj))
				continue;
			newObjs[j++] = objs[i];
		}
		return newObjs;
	}
	public static SwmProduct[] left(SwmProduct[] objs, SwmProduct obj) {
		if (objs == null || objs.length == 0 || obj == null)
			return objs;
		int idx = -1;
		for (int i=0; i<objs.length; i++) {
			if (!objs[i].equals(obj))
				continue;
			idx = i;
			break;
		}
		if (idx < 1)
			return objs;
		SwmProduct[] newObjs = new SwmProduct[objs.length];
		for (int i=0; i<objs.length; i++) {
			if (i == idx) {
				newObjs[i] = objs[idx-1];
				continue;
			} else if (i == idx-1) {
				newObjs[i] = objs[idx];
				continue;
			}
			newObjs[i] = objs[i];
		}
		return newObjs;
	}
	public static SwmProduct[] right(SwmProduct[] objs, SwmProduct obj) {
		if (objs == null || objs.length == 0 || obj == null)
			return objs;
		int idx = -1;
		for (int i=0; i<objs.length; i++) {
			if (!objs[i].equals(obj))
				continue;
			idx = i;
			break;
		}
		if (idx == -1 || idx+1 == objs.length)
			return objs;
		SwmProduct[] newObjs = new SwmProduct[objs.length];
		for (int i=0; i<objs.length; i++) {
			if (i == idx) {
				newObjs[i] = objs[idx+1];
				continue;
			} else if (i == idx+1) {
				newObjs[i] = objs[idx];
				continue;
			}
			newObjs[i] = objs[i];
		}
		return newObjs;
	}
	public Object clone() throws CloneNotSupportedException {
		try {
			return toObject(this.toString());
		} catch (Exception e) {
			logger.warn(e, e);
			return null;
		}
	}
	
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getBusinessTypeCtgId() {
		return businessTypeCtgId;
	}
	public void setBusinessTypeCtgId(String businessTypeCtgId) {
		this.businessTypeCtgId = businessTypeCtgId;
	}
	public String getBusinessCtgId() {
		return businessCtgId;
	}
	public void setBusinessCtgId(String businessCtgId) {
		this.businessCtgId = businessCtgId;
	}
	public long getHitCount() {
		return hitCount;
	}
	public void setHitCount(long hitCount) {
		this.hitCount = hitCount;
	}
	public long getDownCount() {
		return downCount;
	}
	public void setDownCount(long downCount) {
		this.downCount = downCount;
	}
	public SwmProductProperty[] getProductProperties() {
		return productProperties;
	}
	public void setProductProperties(SwmProductProperty[] productProperties) {
		this.productProperties = productProperties;
	}
	public void addProductProperty(SwmProductProperty productProperty) {
		if (productProperty == null)
			return;
		this.setProductProperties(SwmProductProperty.add(this.getProductProperties(), productProperty));
	}
	public void removeFormInfo(SwmProductProperty productProperty) {
		if (productProperty == null)
			return;
		this.setProductProperties(SwmProductProperty.remove(this.getProductProperties(), productProperty));
	}
	public String getMainImg() {
		return mainImg;
	}
	public void setMainImg(String mainImg) {
		this.mainImg = mainImg;
	}
	public String getExtValue() {
		return extValue;
	}
	public void setExtValue(String extValue) {
		this.extValue = extValue;
	}
	public String getIsPublished() {
		return isPublished;
	}
	public void setIsPublished(String isPublished) {
		this.isPublished = isPublished;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductCode() {
		return productCode;
	}
	public String getPackageRels() {
		return packageRels;
	}
	public void setPackageRels(String packageRels) {
		this.packageRels = packageRels;
	}
}
