package net.smartworks.server.engine.publishnotice.manager;

import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.publishnotice.exception.PublishNoticeException;
import net.smartworks.server.engine.publishnotice.model.AlarmNotice;
import net.smartworks.server.engine.publishnotice.model.AlarmNoticeCond;
import net.smartworks.server.engine.publishnotice.model.PublishNotice;
import net.smartworks.server.engine.publishnotice.model.PublishNoticeCond;
import net.smartworks.server.engine.publishnotice.model.MessageNotice;
import net.smartworks.server.engine.publishnotice.model.MessageNoticeCond;

public interface IPublishNoticeManager extends IManager {
	
	public PublishNotice getPublishNotice(String userId, String id, String level) throws PublishNoticeException;
	public PublishNotice getPublishNotice(String userId, PublishNoticeCond cond, String level) throws PublishNoticeException;
	public void setPublishNotice(String userId, PublishNotice obj, String level) throws PublishNoticeException;
	public void removePublishNotice(String userId, String id) throws PublishNoticeException;
	public void removePublishNotice(String userId, PublishNoticeCond cond) throws PublishNoticeException;
	public long getPublishNoticeSize(String userId, PublishNoticeCond cond) throws PublishNoticeException;
	public PublishNotice[] getPublishNotices(String userId, PublishNoticeCond cond, String level) throws PublishNoticeException;
	
	public MessageNotice getMessageNotice(String userId, String id, String level) throws PublishNoticeException;
	public MessageNotice getMessageNotice(String userId, MessageNoticeCond cond, String level) throws PublishNoticeException;
	public void setMessageNotice(String userId, MessageNotice obj, String level) throws PublishNoticeException;
	public void removeMessageNotice(String userId, String id) throws PublishNoticeException;
	public void removeMessageNotice(String userId, MessageNoticeCond cond) throws PublishNoticeException;
	public long getMessageNoticeSize(String userId, MessageNoticeCond cond) throws PublishNoticeException;
	public MessageNotice[] getMessageNotices(String userId, MessageNoticeCond cond, String level) throws PublishNoticeException;

	public AlarmNotice getAlarmNotice(String userId, String id, String level) throws PublishNoticeException;
	public AlarmNotice getAlarmNotice(String userId, AlarmNoticeCond cond, String level) throws PublishNoticeException;
	public void setAlarmNotice(String userId, AlarmNotice obj, String level) throws PublishNoticeException;
	public void removeAlarmNotice(String userId, String id) throws PublishNoticeException;
	public void removeAlarmNotice(String userId, AlarmNoticeCond cond) throws PublishNoticeException;
	public long getAlarmNoticeSize(String userId, AlarmNoticeCond cond) throws PublishNoticeException;
	public AlarmNotice[] getAlarmNotices(String userId, AlarmNoticeCond cond, String level) throws PublishNoticeException;
}
