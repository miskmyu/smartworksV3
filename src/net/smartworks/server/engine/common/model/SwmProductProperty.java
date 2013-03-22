package net.smartworks.server.engine.common.model;

import net.smartworks.server.engine.common.util.XmlUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SwmProductProperty extends BaseObject {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(SwmProductProperty.class);

	protected static final String PREFIX = "Swm";
	private static final String NAME = net.smartworks.server.engine.common.util.CommonUtil.toName(SwmProductProperty.class, PREFIX);
	
	public static final String A_TYPE = "type";
	public static final String A_NAME = "name";
	public static final String A_IMAGEID = "imageId";
	public static final String A_DESCRIPTION = "description";
	public static final String A_IMAGE = "image";
	public static final String A_IMAGETN = "imageTn";
	
	private String type;
	private String name;
	private String imageId;
	private String description;
	private String image;
	private String imageTn;

	public SwmProductProperty() {
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
		//appendAttributeString(A_NAME, name, buf);
		appendAttributeString(A_IMAGEID, imageId, buf);
		return buf.toString();
	}
	public String toElementsString(String tab) {
		StringBuffer buf = new StringBuffer();
		buf.append(super.toElementsString(tab));
		appendElementString(A_DESCRIPTION, getDescription(), tab, true, buf);
		appendElementString(A_NAME, getName(), tab, true, buf);
		appendElementString(A_IMAGE, getImage(), tab, true, buf);
		appendElementString(A_IMAGETN, getImageTn(), tab, true, buf);
		return buf.toString();
	}
	public static BaseObject toObject(Node node, BaseObject baseObj) throws Exception {
		if (node == null)
			return null;
		SwmProductProperty obj = null;
		if (baseObj == null || !(baseObj instanceof SwmProductProperty))
			obj = new SwmProductProperty();
		else
			obj = (SwmProductProperty)baseObj;
		
		MisObject.toObject(node, obj);
		
		NamedNodeMap attrMap = node.getAttributes();
		if (attrMap != null) {
			Node type = attrMap.getNamedItem(A_TYPE);
			//Node name = attrMap.getNamedItem(A_NAME);
			Node imageId = attrMap.getNamedItem(A_IMAGEID);
			if (type != null)
				obj.setType(type.getNodeValue());
			//if (name != null)
			//	obj.setName(name.getNodeValue());
			if (imageId != null)
				obj.setImageId(imageId.getNodeValue());
		}
		NodeList childNodeList = node.getChildNodes();
		if (childNodeList == null || childNodeList.getLength() == 0)
			return obj;
		for (int i=0; i<childNodeList.getLength(); i++) {
			Node childNode = childNodeList.item(i);
			if (childNode.getNodeType() != Node.ELEMENT_NODE || childNode.getNodeName() == null)
				continue;
			if (childNode.getNodeName().equals(A_DESCRIPTION)) {
				obj.setDescription(getNodeValue(childNode, true));
			}  else if (childNode.getNodeName().equals(A_NAME)) {
				obj.setName(getNodeValue(childNode, true));
			}  else if (childNode.getNodeName().equals(A_IMAGE)) {
				obj.setImage(getNodeValue(childNode, true));
			}  else if (childNode.getNodeName().equals(A_IMAGETN)) {
				obj.setImageTn(getNodeValue(childNode, true));
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
	public static SwmProductProperty[] add(SwmProductProperty[] objs, SwmProductProperty obj) {
		if (obj == null)
			return objs;
		int size = 0;
		if (objs != null)
			size = objs.length;
		SwmProductProperty[] newObjs = new SwmProductProperty[size+1];
		int i;
		for (i=0; i<size; i++)
			newObjs[i] = objs[i];
		newObjs[i] = obj;
		return newObjs;
	}
	public static SwmProductProperty[] remove(SwmProductProperty[] objs, SwmProductProperty obj) {
		if (obj == null)
			return objs;
		int size = 0;
		if (objs != null)
			size = objs.length;
		if (size == 0)
			return objs;
		SwmProductProperty[] newObjs = new SwmProductProperty[size-1];
		int i;
		int j = 0;
		for (i=0; i<size; i++) {
			if (objs[i].equals(obj))
				continue;
			newObjs[j++] = objs[i];
		}
		return newObjs;
	}
	public static SwmProductProperty[] left(SwmProductProperty[] objs, SwmProductProperty obj) {
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
		SwmProductProperty[] newObjs = new SwmProductProperty[objs.length];
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
	public static SwmProductProperty[] right(SwmProductProperty[] objs, SwmProductProperty obj) {
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
		SwmProductProperty[] newObjs = new SwmProductProperty[objs.length];
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

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getImageTn() {
		return imageTn;
	}
	public void setImageTn(String imageTn) {
		this.imageTn = imageTn;
	}
}
