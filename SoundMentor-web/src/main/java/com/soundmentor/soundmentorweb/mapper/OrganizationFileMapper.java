package com.soundmentor.soundmentorweb.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.soundmentor.soundmentorpojo.DO.OrganizationFileDO;
import com.soundmentor.soundmentorpojo.DTO.organization.OrganizationFileListReqDTO;
import com.soundmentor.soundmentorpojo.DTO.organization.OrganizationFileListResDTO;
import com.soundmentor.soundmentorpojo.DTO.organization.OrganizationFilesQueryParam;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 组织文件关系表，用于存储组织与文件之间的关系 Mapper 接口
 * </p>
 *
 * @author liuzhicheng
 * @since 2025-01-12
 */
public interface OrganizationFileMapper extends BaseMapper<OrganizationFileDO> {

    IPage<OrganizationFileListResDTO> selectOrganizationFileList(IPage<OrganizationFileListResDTO> page,@Param(Constants.WRAPPER) OrganizationFilesQueryParam param);
}
