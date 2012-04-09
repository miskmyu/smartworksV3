package net.smartworks.server.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.smartworks.model.YTMetaInfo;

import com.google.gdata.data.youtube.FormUploadToken;


public interface IYouTubeService {

	public abstract FormUploadToken getUploadToken(YTMetaInfo metaInfo, String ytUserId, String ytPassword) throws Exception;

	public abstract void uploadYTVideo(HttpServletRequest request, HttpServletResponse response) throws Exception;

}