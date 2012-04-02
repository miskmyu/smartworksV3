package net.smartworks.server.service.impl;

import java.net.URL;

import net.smartworks.model.YTMetaInfo;
import net.smartworks.server.service.IYouTubeService;
import net.smartworks.util.SmartUtil;

import org.springframework.stereotype.Service;

import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.media.mediarss.MediaCategory;
import com.google.gdata.data.media.mediarss.MediaDescription;
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.data.media.mediarss.MediaTitle;
import com.google.gdata.data.youtube.FormUploadToken;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.YouTubeMediaGroup;
import com.google.gdata.data.youtube.YouTubeNamespace;

@Service
public class YouTubeServiceImpl implements IYouTubeService {
	
	private static String YOUTUBE_CLIENT_ID = "SmartWorks.net";
	private static String YOUTUBE_DEVELOPER_KEY = "AI39si54VKd-UHVmOAWrouh1q_CWAyh84EpAXkdhck4LKw2wHxBaeQg6qwzH--1o1ymXtdBJaUDLtXC4ZTOBuLiQpGi96WjzyA";
	private static String YOUTUBE_SMARTWORKS_USERID = "smartworksnet@gmail.com";
	private static String YOUTUBE_SMARTWORKS_PASSWORD = "smartworks.net";
	
	@Override
	public FormUploadToken getUploadToken(YTMetaInfo metaInfo, String ytUserId, String ytPassword) throws Exception {

		try{
			String authToken = null;
			YouTubeService service = new YouTubeService(YOUTUBE_CLIENT_ID, YOUTUBE_DEVELOPER_KEY);
			if(SmartUtil.isBlankObject(ytUserId) || SmartUtil.isBlankObject(ytPassword)){
				ytUserId = YOUTUBE_SMARTWORKS_USERID;
				ytPassword = YOUTUBE_SMARTWORKS_PASSWORD;
			}
			VideoEntry newEntry = new VideoEntry();

			YouTubeMediaGroup mg = newEntry.getOrCreateMediaGroup();
			mg.setTitle(new MediaTitle());
			mg.getTitle().setPlainTextContent(metaInfo.getTitle());
			mg.addCategory(new MediaCategory(YouTubeNamespace.CATEGORY_SCHEME, metaInfo.getCategory()));
			if(!SmartUtil.isBlankObject(metaInfo.getKeywords())){
				mg.setKeywords(new MediaKeywords());
				for(int i=0; i<metaInfo.getKeywords().length; i++)
					mg.getKeywords().addKeyword(metaInfo.getKeywords()[i]);
			}
			mg.setDescription(new MediaDescription());
			mg.getDescription().setPlainTextContent(metaInfo.getDesc());
			mg.setPrivate(metaInfo.isPrivate());
//			mg.addCategory(new MediaCategory(YouTubeNamespace.DEVELOPER_TAG_SCHEME, "mydevtag"));
//			mg.addCategory(new MediaCategory(YouTubeNamespace.DEVELOPER_TAG_SCHEME, "anotherdevtag"));

//			newEntry.setGeoCoordinates(new GeoRssWhere(37.0,-122.0));
			// alternatively, one could specify just a descriptive string
			// newEntry.setLocation("Mountain View, CA");

			URL uploadUrl = new URL("http://gdata.youtube.com/action/GetUploadToken");
			FormUploadToken token = service.getFormUploadToken(uploadUrl, newEntry);			
			return token;
			
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			return null;			
			// Exception Handling Required			
		}		
	}


}
