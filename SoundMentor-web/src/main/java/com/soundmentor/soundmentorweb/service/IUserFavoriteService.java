package com.soundmentor.soundmentorweb.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.soundmentor.soundmentorpojo.DO.UserFavoriteDO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soundmentor.soundmentorpojo.DTO.userSound.req.UserFavoriteQueryParam;
import com.soundmentor.soundmentorpojo.DTO.userSound.req.UserSoundLibQueryParam;
import com.soundmentor.soundmentorpojo.DTO.userSound.res.UserSoundLibDTO;

/**
 * <p>
 * 用于存储用户收藏信息的表 服务类
 * </p>
 *
 * @author Make
 * @since 2025-01-21
 */
public interface IUserFavoriteService extends IService<UserFavoriteDO> {

    /**
     * 添加收藏
     * @param objectId
     * @PARAM: @param userId
     * @RETURN: @return
     **/
    Boolean addFavorite(Integer userId, Integer objectId);

    /**
     * 取消收藏
     * @param objectId
     * @PARAM: @param userId
     * @RETURN: @return
     **/
    Boolean delFavorite(Integer userId, Integer objectId);

    /**
     * 分页查询收藏列表
     * @PARAM: @param param
     * @RETURN: @return
     **/
    IPage<UserSoundLibDTO> pageFavoriteSound(UserFavoriteQueryParam param);
}
