package com.soundmentor.soundmentorweb.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soundmentor.soundmentorpojo.DO.UserSoundRelDO;
import com.soundmentor.soundmentorpojo.DTO.file.UserFileResDTO;
import com.soundmentor.soundmentorpojo.DTO.userSound.req.UserSoundLibQueryParam;
import com.soundmentor.soundmentorpojo.DTO.userSound.res.UserSoundLibDTO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用于存储用户声音相关记录的表 Mapper 接口
 * </p>
 *
 * @author Make
 * @since 2025-01-11
 */
public interface UserSoundRelMapper extends BaseMapper<UserSoundRelDO> {

    /**
     * 分页查询用户声音库
     * @param param
     * @PARAM: @param page
     * @RETURN: @return
     **/
    IPage<UserSoundLibDTO> pageSoundLib(Page<UserFileResDTO> page, @Param("param") UserSoundLibQueryParam param);
}
