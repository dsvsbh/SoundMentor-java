package com.soundmentor.soundmentorweb.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soundmentor.soundmentorpojo.DO.FileDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soundmentor.soundmentorpojo.DTO.file.UserFileQueryParam;
import com.soundmentor.soundmentorpojo.DTO.file.UserFileReqDTO;
import com.soundmentor.soundmentorpojo.DTO.file.UserFileResDTO;
import org.apache.ibatis.annotations.Param;


/**
 * <p>
 * 文件表，用于存储文件的基本信息 Mapper 接口
 * </p>
 *
 * @author liuzhicheng
 * @since 2025-01-10
 */
public interface FileMapper extends BaseMapper<FileDO> {

    IPage<UserFileResDTO> selectUserFiles(IPage<UserFileResDTO> page,@Param(Constants.WRAPPER) UserFileQueryParam param);
}
