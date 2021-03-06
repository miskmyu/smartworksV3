package org.claros.commons.mail.utility;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import net.smartworks.model.community.Department;
import net.smartworks.model.community.Group;
import net.smartworks.model.community.User;
import net.smartworks.model.community.info.UserInfo;
import net.smartworks.server.service.factory.SwServiceFactory;
import net.smartworks.util.SmartUtil;
import org.claros.commons.mail.models.EmailPart;

/**
 * @author Umut Gokbayrak
 *
 */
public class Utility {
	private Utility() {
		super();
	}

	/**
	 * 
	 * @param addr
	 * @return
	 */
	public static String[] addressArrToStringArr(Address[] addr) {
		if (addr != null && addr.length > 0) {
			String[] str = new String[addr.length];
			for (int j = 0; j < addr.length; j++) {
				InternetAddress add = (InternetAddress) addr[j];
				String personal = org.claros.commons.utility.Utility.doCharsetCorrections(add.getPersonal());
				String address = org.claros.commons.utility.Utility.doCharsetCorrections(add.getAddress());

				if (personal != null && personal.length() > 0) {
					personal = personal.replaceAll("\"", " ");
					personal = personal.replaceAll("\'", " ");
					personal = personal.replaceAll("&quot;", " ");
					personal = personal.replaceAll("&#39;", " ");
					if (address != null && address.length() > 0) {
						str[j] = personal + " <" + address + ">";
					} else {
						str[j] = personal;
					}
				} else {
					if (address != null && address.length() > 0) {
						str[j] = address;
					} else {
						str[j] = "";
					}
				}
			}
			return str;
		}
		return null;
	}

	public static String userToString(User user) {
		String str = null;
		String personal = org.claros.commons.utility.Utility.doCharsetCorrections(user.getLongName());
		String address = org.claros.commons.utility.Utility.doCharsetCorrections(user.getId());

		if (personal != null && personal.length() > 0) {
			personal = personal.replaceAll("\"", " ");
			personal = personal.replaceAll("\'", " ");
			personal = personal.replaceAll("&quot;", " ");
			personal = personal.replaceAll("&#39;", " ");
			if (address != null && address.length() > 0) {
				str = personal + " <" + address + ">";
			} else {
				str = personal;
			}
		} else {
			if (address != null && address.length() > 0) {
				str = address;
			} else {
				str = "";
			}
		}
		return str;
	}

	/**
	 * 
	 * @param addr
	 * @return
	 */
	public static String addressArrToString(Address[] addr) {
		String addrStr = "";
		String str[] = addressArrToStringArr(addr);
		if (str != null && str.length > 0) {
			addrStr = "";
			for (int i = 0; i < str.length; i++) {
				addrStr += str[i] + ", ";
			}
		}
		String msg = org.claros.commons.utility.Utility.extendedTrim(addrStr, ",");
		return org.claros.commons.utility.Utility.doCharsetCorrections(msg);
	}

	/**
	 * 
	 * @param addr
	 * @return
	 */
	public static String[] addressArrToStringArrShort(Address[] addr) {
		if (addr != null && addr.length > 0) {
			String[] str = new String[addr.length];
			for (int j = 0; j < addr.length; j++) {
				InternetAddress add = (InternetAddress) addr[j];
				String personal = org.claros.commons.utility.Utility.doCharsetCorrections(add.getPersonal());
				String address = org.claros.commons.utility.Utility.doCharsetCorrections(add.getAddress());

				if (personal != null && personal.length() > 0) {
					personal = personal.replaceAll("\"", " ");
					personal = personal.replaceAll("\'", " ");
					personal = personal.replaceAll("&quot;", " ");
					personal = personal.replaceAll("&#39;", " ");
					str[j] = personal;
				} else if (address != null && address.length() > 0) { 
					str[j] = address;
				} else {
					str[j] = "Unknown";
				}
			}
			return str;
		}
		return null;
	}
	
	/**
	 * 
	 * @param addr
	 * @return
	 */
	public static String addressArrToStringShort(Address[] addr) {
		String addrStr = "";
		String str[] = addressArrToStringArrShort(addr);
		if (str != null && str.length > 0) {
			addrStr = "";
			for (int i = 0; i < str.length; i++) {
				addrStr += str[i] + ", ";
			}
		}
		String msg = org.claros.commons.utility.Utility.extendedTrim(addrStr, ",");
		msg =  org.claros.commons.utility.Utility.doCharsetCorrections(msg);
		return msg;
	}

	/**
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static Address[] stringToAddressArray(String str) throws Exception {
		if (str == null)
			return null;
		
		str = org.claros.commons.utility.Utility.extendedTrim(str, ",");
		StringTokenizer token = new StringTokenizer(str, ",");
		if (token.countTokens() != 0) {
			Address[] outAddr = new Address[token.countTokens()];
			int counter = 0;
			while (token.hasMoreTokens()) {
				String addr = token.nextToken().trim();
				addr = org.claros.commons.utility.Utility.replaceAllOccurances(addr, "&lt;", "<");
				addr = org.claros.commons.utility.Utility.replaceAllOccurances(addr, "&gt;", ">");
				String fullname = "";
				String email = "";
				int j;
				try {
					if ((j = addr.indexOf("<")) > 0) {
						fullname = org.claros.commons.utility.Utility.extendedTrim(addr.substring(0, j).trim(), "\"");
						email = org.claros.commons.utility.Utility.extendedTrim(org.claros.commons.utility.Utility.extendedTrim(addr.substring(j + 1), ">"), "\"").trim();
//						String charset = "utf-8";
						String charset = "EUC-KR";
						outAddr[counter] = new InternetAddress(email, fullname, charset);
					} else {
						outAddr[counter] = new InternetAddress(addr);
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (AddressException e) {
					e.printStackTrace();
				}
				counter++;
			}
			return outAddr;
		} else {
			return null;
		}
	}

	private static UserInfo[] addUserInfoArrays(UserInfo[] array1, UserInfo[] array2){
		if(SmartUtil.isBlankObject(array1)) return array2;
		if(SmartUtil.isBlankObject(array2)) return array1;
		UserInfo[] result = new UserInfo[array1.length + array2.length];
		for(int i=0; i<array1.length; i++)
			result[i] = array1[i];
		for(int i=0; i<array2.length; i++)
			result[array1.length+i] = array2[i];
		return result;
		
	}
	
	public static Address[] stringListToAddressArray(List<Map<String, String>> strList) throws Exception {
		if (strList == null)
			return null;
		int size = strList.size();
		if (size > 0) {
			UserInfo[] users = new UserInfo[size];
			UserInfo[] departmentUsers = null;
			UserInfo[] groupUsers = null;
			UserInfo[] categoryUsers = null;
			int userCount = 0;
			
			for(int i=0; i<size; i++){
				Map<String, String> addr = (Map<String, String>)strList.get(i);
				String fullname = addr.get("name");
				String email = addr.get("id");
				if(SmartUtil.isEmailAddress(email)){
					users[userCount++] = new UserInfo(email, fullname);
				}else if(!SmartUtil.isBlankObject(email)){
					if(email.startsWith(Department.DEPARTMENT_ID_PREFIX)){
						departmentUsers = Utility.addUserInfoArrays(departmentUsers, SwServiceFactory.getInstance().getCommunityService().getAllUsersByDepartmentId(email));
					}else if(email.startsWith(Group.GROUP_ID_PREFIX)){
						groupUsers = Utility.addUserInfoArrays(groupUsers, (UserInfo[])SwServiceFactory.getInstance().getCommunityService().getAllComsByGroupId(email));						
					}else{
						categoryUsers = Utility.addUserInfoArrays(categoryUsers, (UserInfo[])SwServiceFactory.getInstance().getCommunityService().getAllComsByCategoryId(email));
						if(SmartUtil.isBlankObject(categoryUsers)){
							departmentUsers = Utility.addUserInfoArrays(departmentUsers, SwServiceFactory.getInstance().getCommunityService().getAllUsersByDepartmentId(email));							
						}
					}
				}
			}
			
			UserInfo[] newUsers = new UserInfo[userCount];
			for(int i=0; i<userCount; i++)
				newUsers[i] = users[i];
			
			UserInfo[] totalUsers = null;
			totalUsers = Utility.addUserInfoArrays(totalUsers, newUsers);
			totalUsers = Utility.addUserInfoArrays(totalUsers, departmentUsers);
			totalUsers = Utility.addUserInfoArrays(totalUsers, groupUsers);
			totalUsers = Utility.addUserInfoArrays(totalUsers, categoryUsers);
			
			int totalCount = (SmartUtil.isBlankObject(totalUsers)) ? 0 : totalUsers.length;
			int newTotalCount = 0;
			UserInfo[] newTotalUsers = new UserInfo[totalCount];
			for(int i=0; i<totalCount; i++){
				int j=i+1;
				for(; j<totalCount; j++)
					if(totalUsers[i].getId().equals(totalUsers[j].getId()))
						break;
				if(j==totalCount){
					newTotalUsers[newTotalCount++] = totalUsers[i];
				}
			}
			size = newTotalCount;
			Address[] outAddr = new Address[size];
			for(int counter=0; counter<size; counter++) {
				try {
					UserInfo user = newTotalUsers[counter];
//					String charset = "utf-8";
					String charset = "EUC-KR";
					outAddr[counter] = new InternetAddress(user.getId(), user.getLongName(), charset);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			return outAddr;
		} else {
			return null;
		}
	}

	public static ArrayList stringListToEmailPartArray(List<Map<String, String>> strList) throws Exception {
		if (strList == null)
			return null;
		int size = strList.size();
		if (size > 0) {
			ArrayList outEmailPart = new ArrayList();
			for(int counter=0; counter<size; counter++) {
				Map<String, String> att = (Map<String, String>)strList.get(counter);
				EmailPart tmp = new EmailPart();
				if(!SmartUtil.isBlankObject(att.get("fileId"))){
					tmp.setFilename(att.get("fileName"));
					long fileSize = Long.parseLong(att.get("fileSize"));
					tmp.setSize(fileSize);
					tmp.setSizeReadable(SmartUtil.getBytesAsString(fileSize));
					tmp.setDisposition(att.get("localFilePath"));
				}else{
					tmp.setFolderId(att.get("folderId"));
					tmp.setMsgId(att.get("msgId"));
					tmp.setId(Integer.parseInt(att.get("partId")));
				}
				outEmailPart.add(tmp);
			}
			return outEmailPart;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param size
	 * @return
	 */
	public static String sizeToHumanReadable(long size) {
		String out = Math.round(size / 1024) + "K";
		if (out.equals("0K")) {
			out = size + "B";
		}
		return out;
	}

	/**
	 * 
	 * @param message
	 * @return
	 */
	public static String stripHTMLTags(String message) {
		StringBuffer returnMessage = new StringBuffer(message);
		try {
			int startPosition = message.indexOf("<"); // encountered the first opening brace
			int endPosition = message.indexOf(">"); // encountered the first closing braces
			while (startPosition != -1) {
				returnMessage.delete(startPosition, endPosition + 1); // remove the tag
				returnMessage.insert(startPosition, " ");
				startPosition = (returnMessage.toString()).indexOf("<"); // look for the next opening brace
				endPosition = (returnMessage.toString()).indexOf(">"); // look for the next closing brace
			}
		} catch (Throwable e) {
			// do nothing sier
		}
		return returnMessage.toString();
	}
}
