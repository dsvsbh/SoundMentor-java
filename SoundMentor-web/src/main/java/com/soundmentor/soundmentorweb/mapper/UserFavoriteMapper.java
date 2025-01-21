package com.soundmentor.soundmentorweb.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soundmentor.soundmentorpojo.DO.UserFavoriteDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soundmentor.soundmentorpojo.DTO.file.UserFileResDTO;
import com.soundmentor.soundmentorpojo.DTO.userSound.req.UserFavoriteQueryParam;
import com.soundmentor.soundmentorpojo.DTO.userSound.req.UserSoundLibQueryParam;
import com.soundmentor.soundmentorpojo.DTO.userSound.res.UserSoundLibDTO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用于存储用户收藏信息的表 Mapper 接口
 * </p>
 *
 * @author Make
 * @since 2025-01-21
 */
public interface UserFavoriteMapper extends BaseMapper<UserFavoriteDO> {

    /**
     * 分页查询用户收藏的声音
     * @param param
     * @PARAM: @param page
     * @RETURN: @return
     **/
    IPage<UserSoundLibDTO> pageFavoriteSound(Page<UserFileResDTO> page, @Param("param") UserFavoriteQueryParam param);
}
