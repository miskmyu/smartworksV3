package net.smartworks.server.service;

import net.smartworks.model.YTMetaInfo;

import com.google.gdata.data.youtube.FormUploadToken;


public interface IYouTubeService {

	public abstract FormUploadToken getUploadToken(YTMetaInfo metaInfo, String ytUserId, String ytPassword) throws Exception;

}