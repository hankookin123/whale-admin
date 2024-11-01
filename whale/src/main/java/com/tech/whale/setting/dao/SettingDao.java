package com.tech.whale.setting.dao;

import java.util.List;

import com.tech.whale.setting.dto.*;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SettingDao {

	public UserInfoDto getProfile(String session_user_id);
	public void updateProfile(String nickname, String email, String newProfileImage, String session_user_id);
	public String getCurrentPassword(String session_user_id);
	public void updatePassword(String session_user_id, String encodedPassword);
	public void updateAccountPrivacy(String session_user_id, int accountPrivacy);
	public void updateNotificationSettings(String session_user_id, int allNotificationOff, int likeNotificationOnoff, int commentNotificationOnoff, int messageNotificationOnoff);
	public UserNotificationDto getNotificationSettingsByUserId(String session_user_id);
	public void updateLikeNotification(String session_user_id, int likeNotificationOnOff);
	public void updateCommentNotification(String session_user_id, int commentNotificationOnOff);
	public void updateMessageNotification(String session_user_id, int messageNotificationOnOff);
	public UserSettingDto getAccountPrivacyByUserId(String session_user_id);
	public List<BlockDto> getBlockList(String session_user_id);
	public void unblockUser(String session_user_id, String block_userId);
	public UserSettingDto getDarkmode(String session_user_id);
	public void updateDarkmode(String session_user_id, int darkmodeOn);
	public StartpageDto getStartpageSetting(String session_user_id);
	public void updateStartpageSetting(String userId, String left, String right);
	public PageAccessDto getPageAccessSetting(String session_user_id);
	public void updatePageAccessSetting(String userId, String settingType, String selectedValue);
	public List<LikeListDto> getFilteredPostLikeList(String session_user_id, String orderBy, String postType);
	public List<CommentListDto> getFilteredPostCommentList(String session_user_id, String orderBy, String postType);
	public List<HiddenFeedDto> getHiddenFeedList(String session_user_id);
	public void updateRepresentiveSong(String session_user_id, String trackId);
}
