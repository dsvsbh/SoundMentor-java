package com.soundmentor.soundmentorweb.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.soundmentor.soundmentorpojo.DO.UserFavoriteDO;
import com.soundmentor.soundmentorweb.mapper.UserFavoriteMapper;
import com.soundmentor.soundmentorweb.service.IUserFavoriteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用于存储用户收藏信息的表 服务实现类
 * </p>
 *
 * @author Make
 * @since 2025-01-21
 */
@Service
public class UserFavoriteServiceImpl extends ServiceImpl<UserFavoriteMapper, UserFavoriteDO> implements IUserFavoriteService {

    /**
     * 添加收藏
     * @param objectId
     * @PARAM: @param userId
     * @RETURN: @return
     **/
    @Override
    public Boolean addFavorite(Integer userId, Integer objectId) {
        UserFavoriteDO userFavoriteDO = new UserFavoriteDO();
        userFavoriteDO.setUserId(userId);
        userFavoriteDO.setFavoriteId(objectId);
        return this.save(userFavoriteDO);
    }

    /**
     * 取消收藏
     * @param objectId
     * @PARAM: @param userId
     * @RETURN: @return
     **/
    @Override
    public Boolean delFavorite(Integer userId, Integer objectId) {
        return this.remove(Wrappers.<UserFavoriteDO> lambdaQuery()
                .eq(UserFavoriteDO::getUserId, userId)
                .eq(UserFavoriteDO::getFavoriteId, objectId));
    }
}
