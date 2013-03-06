package net.smartworks.server.engine.publishnotice.manager;

import net.smartworks.server.engine.common.manager.IManager;
import net.smartworks.server.engine.publishnotice.exception.PublishNoticeException;
import net.smartworks.server.engine.publishnotice.model.PublishNotice;
import net.smartworks.server.engine.publishnotice.model.PublishNoticeCond;
import net.smartworks.server.engine.publishnotice.model.SpaceNotice;
import net.smartworks.server.engine.publishnotice.model.SpaceNoticeCond;

public interface IPublishNoticeManager extends IManager {
	
	public PublishNotice getPublishNotice(String userId, String id, String level) throws PublishNoticeException;
	public PublishNotice getPublishNotice(String userId, PublishNoticeCond cond, String level) throws PublishNoticeException;
	public void setPublishNotice(String userId, PublishNotice obj, String level) throws PublishNoticeException;
	public void removePublishNotice(String userId, String id) throws PublishNoticeException;
	public void removePublishNotice(String userId, PublishNoticeCond cond) throws PublishNoticeException;
	public long getPublishNoticeSize(String userId, PublishNoticeCond cond) throws PublishNoticeException;
	public PublishNotice[] getPublishNotices(String userId, PublishNoticeCond cond, String level) throws PublishNoticeException;
	
	public SpaceNotice getSpaceNotice(String userId, String id, String level) throws PublishNoticeException;
	public SpaceNotice getSpaceNotice(String userId, SpaceNoticeCond cond, String level) throws PublishNoticeException;
	public void setSpaceNotice(String userId, SpaceNotice obj, String level) throws PublishNoticeException;
	public void removeSpaceNotice(String userId, String id) throws PublishNoticeException;
	public void removeSpaceNotice(String userId, SpaceNoticeCond cond) throws PublishNoticeException;
	public long getSpaceNoticeSize(String userId, SpaceNoticeCond cond) throws PublishNoticeException;
	public SpaceNotice[] getSpaceNotices(String userId, SpaceNoticeCond cond, String level) throws PublishNoticeException;
	
}
