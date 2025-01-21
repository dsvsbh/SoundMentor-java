package com.soundmentor.soundmentorweb.service.impl;

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

}
