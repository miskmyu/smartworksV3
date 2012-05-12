package net.smartworks.model.sera;

import net.smartworks.model.instance.info.BoardInstanceInfo;

public class SeraBoardList{

	public static final int MAX_BOARD_LIST = 5;
	
	public static final int TYPE_SERA_NEWS = 1;
	public static final int TYPE_SERA_TREND = 2;
	
	BoardInstanceInfo[] seraNews;
	BoardInstanceInfo[] seraTrends;

	public BoardInstanceInfo[] getSeraNews() {
		return seraNews;
	}
	public void setSeraNews(BoardInstanceInfo[] seraNews) {
		this.seraNews = seraNews;
	}
	public BoardInstanceInfo[] getSeraTrends() {
		return seraTrends;
	}
	public void setSeraTrends(BoardInstanceInfo[] seraTrends) {
		this.seraTrends = seraTrends;
	}

	public SeraBoardList(){
		super();
	}
}