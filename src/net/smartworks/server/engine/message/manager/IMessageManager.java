package net.smartworks.server.engine.message.manager;

import net.smartworks.server.engine.message.exception.MessageException;
import net.smartworks.server.engine.message.model.Message;
import net.smartworks.server.engine.message.model.MessageCond;

public interface IMessageManager {
	
	public Message getMessage(String user, String objId, String level) throws MessageException;
	public Message getMessage(String user, MessageCond cond, String level) throws MessageException;
	public void setMessage(String user, Message obj, String level) throws MessageException;
	public void createMessage(String user, Message obj) throws MessageException;
	public void removeMessage(String user, String objId) throws MessageException;
	public void removeMessage(String user, MessageCond cond) throws MessageException;
	public long getMessageSize(String user, MessageCond cond) throws MessageException;
	public Message[] getMessages(String user, MessageCond cond, String level)throws MessageException;

}
