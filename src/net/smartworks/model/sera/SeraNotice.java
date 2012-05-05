package net.smartworks.model.sera;

import net.smartworks.model.notice.Notice;

public class SeraNotice extends Notice {

	public final static int 	NUMBER_OF_NOTICES = 7;
	
//	public final static int	TYPE_INVALID 		= -1;
//	public final static int	TYPE_NOTIFICATION 	= 0;
//	public final static int	TYPE_MESSAGE 		= 1;

	public final static int	TYPE_MY_COURSE	= 11;
	public final static int	TYPE_FRIEND 	= 12;
	public final static int	TYPE_CALENDAR	= 13;
	public final static int	TYPE_BADGE 		= 14;
	public final static int	TYPE_EVENT 		= 15;

	public final static int INDEX_NOTIFICATION = 0;
	public final static int INDEX_MY_COURSE = 1;
	public final static int INDEX_FRIEND = 2;
	public final static int INDEX_MESSAGE = 3;
	public final static int INDEX_CALENDAR = 4;
	public final static int INDEX_BADGE = 5;
	public final static int INDEX_EVENT = 6;

	public SeraNotice(){
		super();
	}
	public SeraNotice(int type, int length){
		super(type, length);
	}
}
