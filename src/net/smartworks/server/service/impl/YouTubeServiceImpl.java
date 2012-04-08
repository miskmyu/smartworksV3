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
		return null;
	}

	@Override
	public void uploadYTVideo(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try{
			String fileName = "";
			try {
				fileName = URLDecoder.decode(request.getHeader("X-File-Name"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return;
			}
			if (fileName.indexOf(File.separator) > 1)
				fileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1);
			String fileExtension = fileName.lastIndexOf(".") > 1 ? fileName.substring(fileName.lastIndexOf(".") + 1) : null;
			Long fileSize = Long.parseLong(request.getHeader("Content-Length"));
			String videoSubject = request.getParameter("videoSubject");
			String videoContent = request.getParameter("videoContent");
			String ytUserId = request.getParameter("ytUserId");
			String ytPassword = request.getParameter("ytPassword");
				
			YouTubeService service = new YouTubeService(YOUTUBE_CLIENT_ID, YOUTUBE_DEVELOPER_KEY);
			if(SmartUtil.isBlankObject(ytUserId) || SmartUtil.isBlankObject(ytPassword)){
				ytUserId = YOUTUBE_YSJUNG_USERID;
				ytPassword = YOUTUBE_YSJUNG_PASSWORD;
			}
			try{
				service.setUserCredentials("ysjung@maninsoft.co.kr",  "ysjung5775");
		    }catch (AuthenticationException e) {
		        System.out.println("Invalid login credentials.");
		        e.printStackTrace();
		        return;
		    }
			
			VideoEntry newEntry = new VideoEntry();

			YouTubeMediaGroup mg = newEntry.getOrCreateMediaGroup();
			mg.setTitle(new MediaTitle());
			mg.getTitle().setPlainTextContent("비디오 제목");
//			mg.addCategory(new MediaCategory(YouTubeNamespace.CATEGORY_SCHEME, "SmartWorks.net"));
			mg.setKeywords(new MediaKeywords());
			mg.getKeywords().addKeyword("SmartWorks");
			mg.getKeywords().addKeyword("Business");
			mg.setDescription(new MediaDescription());
			mg.getDescription().setPlainTextContent("비디오 내용입니다.");
			mg.setPrivate(false);
//			mg.addCategory(new MediaCategory(YouTubeNamespace.DEVELOPER_TAG_SCHEME, "smartworks"));
//			mg.addCategory(new MediaCategory(YouTubeNamespace.DEVELOPER_TAG_SCHEME, "maninsoft"));

//			newEntry.setGeoCoordinates(new GeoRssWhere(37.0,-122.0));
			// alternatively, one could specify just a descriptive string
			newEntry.setLocation("Mountain View, CA");

			try{
				smartworks.uploadTempFile(request, response);
			}catch (Exception e){
				throw e;
			}finally{
				MediaFileSource ms = new MediaFileSource(new File("/Users/ysjung/smartworksV3/apache-tomcat-7.0.22-imageServer/webapps/imageServer/SmartFiles/Semiteq/Temps/temp_1a3fe0b87f834e44a7e913e1dd32df74.mp4"), "video/quicktime");
				newEntry.setMediaSource(ms);	
				String uploadUrl = "http://uploads.gdata.youtube.com/feeds/api/users/default/uploads";
				VideoEntry createdEntry = service.insert(new URL(uploadUrl), newEntry);
				createdEntry.getId();	
			}
		}catch (Exception e){
			// Exception Handling Required
			e.printStackTrace();
			// Exception Handling Required			
		}		
	}
}
