package com.soundmentor.soundmentorweb.mapper;

import com.soundmentor.soundmentorpojo.DO.UserDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soundmentor.soundmentorpojo.DTO.organization.OrganizationUserListDTO;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author Make
 * @since 2025-01-05
 */
public interface UserMapper extends BaseMapper<UserDO> {

    List<OrganizationUserListDTO> getOrganizationUserList(Integer organizationId);
}
