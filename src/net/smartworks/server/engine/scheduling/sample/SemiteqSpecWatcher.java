/* 
 * $Id$
 * created by    : yukm
 * creation-date : 2012. 12. 28.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.scheduling.sample;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.common.model.Filter;
import net.smartworks.server.engine.common.util.CommonUtil;
import net.smartworks.server.engine.common.util.DateUtil;
import net.smartworks.server.engine.factory.SwManagerFactory;
import net.smartworks.server.engine.infowork.domain.model.SwdDataField;
import net.smartworks.server.engine.infowork.domain.model.SwdDomain;
import net.smartworks.server.engine.infowork.domain.model.SwdDomainCond;
import net.smartworks.server.engine.infowork.domain.model.SwdRecord;
import net.smartworks.server.engine.infowork.domain.model.SwdRecordCond;
import net.smartworks.server.engine.organization.model.SwoConfig;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class SemiteqSpecWatcher  extends QuartzJobBean   {
	
	private final static String from = "administrator@semiteq.co.kr";
	private final static String companyId = "Semiteq";
	private final static String formId = "frm_5c111516a4474d8baa963eeb8e0e487c";
	//유효기간필드아이디
	private final static String targetDateFieldId = "5";
	private final static String targetDraftUserFieldId = "4";
	private final static String targetSpecChargerFieldId = "20";
	private final static String targetSpecTitleFieldId = "0";
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		try {
			
			Date now = new Date();
			String time = DateUtil.toDateString(now);
			System.out.println("############################ SPEC WATCHER BEGIN at " + time + " ############################");
			
			SwdDomainCond swdDomainCond = new SwdDomainCond();
			swdDomainCond.setFormId(formId);
			SwdDomain swdDomain = SwManagerFactory.getInstance().getSwdManager().getDomain("", swdDomainCond, IManager.LEVEL_LITE);

			if(swdDomain == null)
				throw new Exception("Not Exist Spec InfomationWork Or Not Exist FormId : " + formId);
			
			String tableColName = SwManagerFactory.getInstance().getSwdManager().getTableColName(swdDomain.getObjId(), targetDateFieldId);
			
			//스팩 유효기간 7일전건들을 찾아 스팩담당자와 기안자에게 메일을 발송한다
			boolean result = sendSpecMailBefore(swdDomain, tableColName);
			//스팩 유효기간이 하루 지난건을 찾아 스팩담당자에게 메일을 발송하며 해당 스팩은 제거한다
			boolean result2 = sendSpecMailAfter(swdDomain, tableColName);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private boolean sendSpecMailBefore(SwdDomain swdDomain, String tableColName) throws Exception {
		
		String formFieldType = "date";
		String operator = "=";

		Filter filter = new Filter();
		filter.setLeftOperandType(formFieldType);
		filter.setLeftOperandValue(tableColName);
		filter.setOperator(operator);
		filter.setRightOperandType(formFieldType);
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 6);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DATE);
		String rightOperand = year + "-" + month + "-" + day + " 15:00:00";
		//String rightOperand = "2013-01-04";

		SwdRecordCond swdRecordCond = new SwdRecordCond();
		swdRecordCond.setFormId(swdDomain.getFormId());
		swdRecordCond.setDomainId(swdDomain.getObjId());
		
		//1주일전 스펙 메일 발송
		filter.setRightOperandValue(rightOperand);
		swdRecordCond.setFilter(new Filter[]{filter});
		
		SwdRecord[] records = SwManagerFactory.getInstance().getSwdManager().getRecords("SpecWatcher", swdRecordCond, IManager.LEVEL_ALL);
		
		if (records == null) {
			System.out.println("7일전 유효기간의 스펙이 없습니다.");
		} else {
			System.out.println("유효기간 7일전 스펙 발견!! 총 " + records.length + " 건");
			sendMail(records, false);
		}
		return true;
	}
	private boolean sendSpecMailAfter(SwdDomain swdDomain, String tableColName) throws Exception {

		String formFieldType = "date";
		String operator = "=";
		
		Filter filter = new Filter();
		filter.setLeftOperandType(formFieldType);
		filter.setLeftOperandValue(tableColName);
		filter.setOperator(operator);
		filter.setRightOperandType(formFieldType);
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -2);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DATE);
		String rightOperand = year + "-" + month + "-" + day + " 15:00:00";
		//String rightOperand = "2013-01-04";

		SwdRecordCond swdRecordCond = new SwdRecordCond();
		swdRecordCond.setFormId(swdDomain.getFormId());
		swdRecordCond.setDomainId(swdDomain.getObjId());

		//유효기간이 1일 지나 스펙의 메일 발송
		filter.setRightOperandValue(rightOperand);
		swdRecordCond.setFilter(new Filter[]{filter});
		
		SwdRecord[] records = SwManagerFactory.getInstance().getSwdManager().getRecords("SpecWatcher", swdRecordCond, IManager.LEVEL_ALL);
		
		if (records == null) {
			System.out.println("1일 지난 유효기간의 스펙이 없습니다.");
		} else {
			System.out.println("유효기간 1일 지난 스펙 발견!! 총 " + records.length + " 건!");
			sendMail(records, true);
			removeSpec(records);
		}
		return true;
	}
	private void removeSpec(SwdRecord[] records) throws Exception {
		if (CommonUtil.isEmpty(records))
			return;
		for (int i = 0; i < records.length; i++) {
			SwdRecord record = records[i];
			removeSpec(record);
		}
	}
	private boolean removeSpec(SwdRecord record) throws Exception {
		if (CommonUtil.isEmpty(record))
			return false;
		SwdRecordCond cond = new SwdRecordCond();
		cond.setFormId(record.getFormId());
		cond.setRecordId(record.getRecordId());
		SwManagerFactory.getInstance().getSwdManager().removeRecord("", cond);
		return true;
	}
	private void sendMail(SwdRecord[] records, boolean chargerOnly) throws Exception {
		
		SwoConfig config = SwManagerFactory.getInstance().getSwoManager().getConfig("", companyId, null);
		if (!config.isActivity())
			return;

		if (config == null || config.getSmtpAddress() == null || config.getUserId() == null || config.getPassword() == null) 
			throw new Exception("Mail info is null");
		
		String host = config.getSmtpAddress();
		String id = config.getUserId();
		String pass = config.getPassword();
		
		
		for (int i = 0; i < records.length; i++) {
			SwdRecord record = records[i];
			new Thread(new SendMailTask(host, id, pass, record, chargerOnly)).start();
			//new SendMailTask(record, chargerOnly));
		}
	}
	private class SendMailTask implements Runnable {
		private SwdRecord record = null;
		private boolean chargerOnly = false;
		private String host = null;
		private String id = null;
		private String pass = null;
		
		public SendMailTask(String host, String id, String pass, SwdRecord record, boolean chargerOnly) {
			this.record = record;
			this.chargerOnly = chargerOnly;
			this.host = host;
			this.id = id;
			this.pass = pass;
		}
		public void run() {
			try {
				String[] to = getTargetMailIds(this.record, this.chargerOnly);
				if (to == null || to.length == 0)
					return;

				String title = "";
				if (this.chargerOnly) {
					title = "임시 스팩의 유효기간이 만료 되었습니다. SPEC : " + record.getDataFieldValue(targetSpecTitleFieldId) ;
				} else {
					title = "임시 스팩의 유효기간이 1주일 남았습니다. SPEC : " + record.getDataFieldValue(targetSpecTitleFieldId) ;
				}
				String content = toMailContent(this.record, this.chargerOnly);
				
				for (int i = 0; i < to.length; i++) {
					if (to[i].indexOf("@") == -1)
						continue;
					sendMail(this.host, this.id, this.pass, title, to[i], from, content);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private String[] getTargetMailIds(SwdRecord record, boolean chargerOnly) {
		if (CommonUtil.isEmpty(record))
			return null;
		if (chargerOnly) {
			SwdDataField userField = record.getDataField(targetSpecChargerFieldId);
			String charger = userField.getRefRecordId();
			if (CommonUtil.isEmpty(charger)) {
				return null;
			} else {
				return new String[]{charger};
			}
		} else {
			SwdDataField userField = record.getDataField(targetSpecChargerFieldId);
			String charger = userField.getRefRecordId();
			SwdDataField userField2 = record.getDataField(targetDraftUserFieldId);
			String drafter = userField2.getRefRecordId();
			List userList = new ArrayList();
			if (!CommonUtil.isEmpty(charger))
				userList.add(charger);
			if (!CommonUtil.isEmpty(drafter))
				userList.add(drafter);
			if (userList.size() == 0)
				return null;
			String[] resultUsers = new String[userList.size()];
			userList.toArray(resultUsers);
			return resultUsers;
		}		
	}
	private String toMailContent(SwdRecord record, boolean chargerOnly) {
		StringBuffer buff = new StringBuffer();
		buff.append("<html>");
		buff.append("	<head>");
		buff.append("		<style type=\"text/css\">");
		buff.append("			BODY {");
		buff.append("				background-color: #FFFFFF; COLOR: #000000;   FONT-SIZE: 9pt; LINE-HEIGHT: 150%; TEXT-DECORATION: none; scrollbar-face-color:  #FFFFFF; ");
		buff.append("scrollbar-highlight-color: #FFFFFF; scrollbar-3dlight-color: #EBEBEB; scrollbar-shadow-color: #EBEBEB; scrollbar-darkshadow-color: #FFFFFF; scrollbar-track-color: #FFFFFF; ");
		buff.append("scrollbar-arrow-color: #EBEBEB");
		buff.append("			}");
		buff.append("			TABLE {");
		buff.append("				  FONT-SIZE: 9pt; LINE-HEIGHT: 150%; TEXT-DECORATION: none");
		buff.append("			}");
		buff.append("			TR {");
		buff.append("				  FONT-SIZE: 9pt; LINE-HEIGHT: 150%; TEXT-DECORATION: none");
		buff.append("			}");
		buff.append("			TD {");
		buff.append("				  FONT-SIZE: 9pt; LINE-HEIGHT: 150%; TEXT-DECORATION: none");
		buff.append("			}");
		buff.append("			");
		buff.append("			A:link {");
		buff.append("				COLOR: #616161;   FONT-SIZE: 9pt; LINE-HEIGHT: 150%; TEXT-DECORATION: none");
		buff.append("			}");
		buff.append("			A:visited {");
		buff.append("				COLOR: #616161;   FONT-SIZE: 9pt; LINE-HEIGHT: 150%; TEXT-DECORATION: none");
		buff.append("			}");
		buff.append("			A:active {");
		buff.append("				COLOR: #616161;   FONT-SIZE: 9pt; LINE-HEIGHT: 150%; TEXT-DECORATION: none");
		buff.append("			}");
		buff.append("			A:hover {");
		buff.append("				COLOR: #B87B29;   FONT-SIZE: 9pt; LINE-HEIGHT: 150%; TEXT-DECORATION: none");
		buff.append("			}");
		buff.append("		</style>");
		buff.append("	</head>");
		buff.append("	<body>");
		buff.append("		<table width=\"737\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
		buff.append("			<tr>");
		buff.append("				<td width=\"3%\"><img src=\"<%=basePath%>/images/bullet_03.gif\"></td>");
		buff.append("				<td align=\"left\" valign=\"middle\">[스마트웍스닷넷] 임시 스펙 유효기간 알림 메일</td>");
		buff.append("			</tr>");
		buff.append("			<tr	height=\"10\"><td></td></tr>");
		buff.append("		</table>");
		buff.append("		<table width=\"600\" cellpadding=\"0\" cellspacing=\"1\" border=\"0\" bgcolor=\"#DADADA\">");
		buff.append("			<tr height=\"3\" bgcolor=\"#DADADA\">");
		buff.append("				<td colspan=\"4\"></td>");
		buff.append("			</tr>	");
		buff.append("			<tr>");
		
		
		SwdDataField titleField = record.getDataField(targetSpecTitleFieldId);
		String titleName = CommonUtil.toNotNull(titleField.getName());
		String titleValue = CommonUtil.toNotNull(titleField.getValue());
		
		buff.append("				<td width=\"20%\" bgcolor=\"#ECECEC\" style=\"padding-left: 5px;padding-top: 7px;\" valign=\"middle\">").append(titleName).append("</td>");
		buff.append("				<td width=\"30%\" colspan=\"3\" bgcolor=\"FFFFFF\" style=\"padding-left: 5px;padding-top: 7px;\" valign=\"middle\">").append(titleValue).append("</td>");
		buff.append("			</tr>");
		SwdDataField[] fields = record.getDataFields();
		for (int i = 0; i < fields.length; i++) {
			SwdDataField field = fields[i];
			String fieldId = field.getId();
			if (fieldId.equalsIgnoreCase(targetSpecTitleFieldId))
				continue;
			
			String name = field.getName();
			String value = CommonUtil.toNotNull(field.getValue());
			if (fieldId.equalsIgnoreCase(targetDateFieldId)) {
				try {
					Date date = DateUtil.toDate(value, "yyyy-MM-dd HH:mm");
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					cal.add(Calendar.HOUR, 9);
					value = "<font style=\"color:red\">" + cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE) + "</font>";
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			buff.append("			<tr height=\"1\" bgcolor=\"#DADADA\">");
			buff.append("				<td colspan=\"4\"></td>");
			buff.append("			</tr>				");
			buff.append("			<tr>");
			buff.append("				<td width=\"20%\" bgcolor=\"#ECECEC\" style=\"padding-left: 5px;padding-top: 7px;\">").append(name).append("</td>");
			buff.append("				<td width=\"30%\" colspan=\"3\" bgcolor=\"FFFFFF\" style=\"padding-left: 5px;padding-top: 7px;\">").append(value).append("</td>");
			buff.append("			</tr>");
		}
		buff.append("		</table>	");	
		buff.append("	</body>");
		buff.append("</html>");
		return buff.toString();
	}
	
	private static void sendMail(String mailServerName, String id, String pass, String subject, String to, 
			String from, String messageText) throws AddressException, MessagingException, UnsupportedEncodingException {

		Authenticator auth = new PassAuthenticator(id, pass);
		Properties mailProps = new Properties();
		mailProps.put("mail.smtp.host", mailServerName);
		mailProps.put("mail.smtp.auth", "true");
		if(mailServerName.equals("smtp.gmail.com")) {
			mailProps.put("mail.smtp.startls.enable", "true");
			mailProps.put("mail.transport.protocol", "smtp");
			mailProps.put("mail.smtp.port", "465");
			mailProps.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		} else if (mailServerName.equals("smtp.daum.net")) {
			mailProps.put("mail.smtp.port", "465");
			mailProps.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		}
		Session mailSession = Session.getInstance(mailProps, auth);

		InternetAddress toAddrs = new InternetAddress(to);
		InternetAddress fromAddr = new InternetAddress(id, "스마트웍스닷넷");

		Message message = new MimeMessage(mailSession);
		message.setFrom(fromAddr);
		message.setRecipient(Message.RecipientType.TO, toAddrs);
		message.setSubject(subject);
		message.setContent(messageText.toString(), "text/html; charset=euc-kr");
		
		try {
			System.out.println("임시 SPEC MAIL 발송중 - " + to + "(" + subject + ")");
			Transport.send(message);
			System.out.println("임시 SPEC Mail 발송 완료! - " + to + "(" + subject + ")");
		} catch(Exception ex) {
			System.out.println("임시 SPEC Mail 발송 실패 - " + to + "(" + subject + ")");
			ex.printStackTrace();
			throw new MessagingException(ex.getMessage());
		}
	}
}

class PassAuthenticator extends Authenticator {
	private String id;
	private String pass;
	public PassAuthenticator(String id, String pass) {
		this.id = id;
		this.pass = pass;
	}
	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(this.id, this.pass);
	}
}