package com.soundmentor.soundmentorweb.service;

import com.soundmentor.soundmentorpojo.DO.UserFavoriteDO;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
