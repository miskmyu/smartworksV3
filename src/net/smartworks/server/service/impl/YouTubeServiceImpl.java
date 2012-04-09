package net.smartworks.server.service.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.smartworks.model.YTMetaInfo;
import net.smartworks.server.service.IYouTubeService;
import net.smartworks.service.ISmartWorks;
import net.smartworks.util.SmartUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.data.media.mediarss.MediaCategory;
import com.google.gdata.data.media.mediarss.MediaDescription;
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.data.media.mediarss.MediaTitle;
import com.google.gdata.data.youtube.FormUploadToken;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.YouTubeMediaGroup;
import com.google.gdata.data.youtube.YouTubeNamespace;
import com.google.gdata.util.AuthenticationException;

@Service
public class YouTubeServiceImpl implements IYouTubeService {
	
	private static String YOUTUBE_CLIENT_ID = "SmartWorks.net";
	private static String YOUTUBE_DEVELOPER_KEY = "AI39si5ITgaYnxRo9xpWzW-BDmhg127Rtlj2M5jB0OZ7Yz7hWlc7S0iu8opQ6LEhLKoS0e4Jp9_UproHtKftR3-I_CVMQW5ibQ";
	private static String YOUTUBE_SMARTWORKS_USERID = "smartworksnet@gmail.com";
	private static String YOUTUBE_SMARTWORKS_PASSWORD = "smartworks.net";
	private static String YOUTUBE_YSJUNG_USERID = "ysjung@maninsoft.co.kr";
	private static String YOUTUBE_YSJUNG_PASSWORD = "ysjung5775";
	
	ISmartWorks smartworks;

	@Autowired
	public void setSmartworks(ISmartWorks smartworks) {
		this.smartworks = smartworks;
	}
	
	@Override
	public FormUploadToken getUploadToken(YTMetaInfo metaInfo, String ytUserId, String ytPassword) throws Exception {
		try{
			YouTubeService service = new YouTubeService(YOUTUBE_CLIENT_ID, YOUTUBE_DEVELOPER_KEY);
			if(SmartUtil.isBlankObject(ytUserId) || SmartUtil.isBlankObject(ytPassword)){
				ytUserId = YOUTUBE_SMARTWORKS_USERID;
				ytPassword = YOUTUBE_SMARTWORKS_PASSWORD;
			}
			try{
				service.setUserCredentials(ytUserId,  ytPassword);
		    }catch (AuthenticationException e) {
		        System.out.println("Invalid login credentials.");
		        e.printStackTrace();
		        return null;
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

//	@Override
//	public void uploadYTVideo(HttpServletRequest request, HttpServletResponse response) throws Exception {
//
//		try{
//			String videoSubject = request.getParameter("videoSubject");
//			String videoContent = request.getParameter("videoContent");
//			String ytUserId = request.getParameter("ytUserId");
//			String ytPassword = request.getParameter("ytPassword");
//				
//			YouTubeService service = new YouTubeService(YOUTUBE_CLIENT_ID, YOUTUBE_DEVELOPER_KEY);
//			if(SmartUtil.isBlankObject(ytUserId) || SmartUtil.isBlankObject(ytPassword)){
//				ytUserId = YOUTUBE_SMARTWORKS_USERID;
//				ytPassword = YOUTUBE_SMARTWORKS_PASSWORD;
//			}
//			
//			try{
//				service.setUserCredentials(ytUserId,  ytPassword);
//		    }catch (AuthenticationException e) {
//		        System.out.println("Invalid login credentials.");
//		        e.printStackTrace();
//		        return;
//		    }
//			
//			VideoEntry newEntry = new VideoEntry();
//
//			YouTubeMediaGroup mg = newEntry.getOrCreateMediaGroup();
//			mg.setTitle(new MediaTitle());
//			mg.getTitle().setPlainTextContent((SmartUtil.isBlankObject(videoSubject)) ? fileName : videoSubject);
//		    mg.addCategory(new MediaCategory(YouTubeNamespace.CATEGORY_SCHEME, "Tech"));
//			mg.setKeywords(new MediaKeywords());
//			mg.getKeywords().addKeyword("SmartWorks");
//			mg.setDescription(new MediaDescription());
//			if(!SmartUtil.isBlankObject(videoContent))
//				mg.getDescription().setPlainTextContent(SmartUtil.isBlankObject(videoContent) ? fileName : videoContent);
//			mg.setPrivate(true);
//			//newEntry.setGeoCoordinates(new GeoRssWhere(37.0,-122.0));
//			// alternatively, one could specify just a descriptive string
//			//newEntry.setLocation("Mountain View, CA");
//
//			try{
//				smartworks.uploadTempFile(request, response);
//			}catch (Exception e){
//				throw e;
//			}finally{
//				MediaFileSource ms = new MediaFileSource(new File("/Users/ysjung/smartworksV3/apache-tomcat-7.0.22-imageServer/webapps/imageServer/SmartFiles/Semiteq/Temps/temp_9476d093e7164661a35040abda402e49.AVI"), "video/quicktime");
//				newEntry.setMediaSource(ms);	
//				String uploadUrl = "http://uploads.gdata.youtube.com/feeds/api/users/default/uploads";
//				VideoEntry createdEntry = service.insert(new URL(uploadUrl), newEntry);
//				createdEntry.getId();	
//			}
//		}catch (Exception e){
//			// Exception Handling Required
//			e.printStackTrace();
//			// Exception Handling Required			
//		}		
//	}
}
