package com.soundmentor.soundmentorweb.mapper;

import com.soundmentor.soundmentorpojo.DO.OrganizationDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soundmentor.soundmentorpojo.DO.OrganizationUserDO;
import com.soundmentor.soundmentorpojo.DTO.organization.OrganizationListDTO;

import java.util.List;

/**
 * <p>
 * 组织表 Mapper 接口
 * </p>
 *
 * @author Make
 * @since 2025-01-05
 */
public interface OrganizationMapper extends BaseMapper<OrganizationDO> {

    List<OrganizationListDTO> getUserOrganizationList(OrganizationUserDO queryParam);
}
