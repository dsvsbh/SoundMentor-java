package com.soundmentor.soundmentorweb.service;

import com.soundmentor.soundmentorpojo.DO.OrganizationDO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soundmentor.soundmentorpojo.DTO.organization.JoinOrganizationDTO;
import com.soundmentor.soundmentorpojo.DTO.organization.OrganizationListDTO;
import com.soundmentor.soundmentorpojo.DTO.organization.OrganizationUserListDTO;
import com.soundmentor.soundmentorpojo.DTO.user.req.CreateOrganizationDTO;

import java.util.List;

/**
 * <p>
 * 组织表 服务类
 * </p>
 *
 * @author Make
 * @since 2025-01-05
 */
public interface IOrganizationService extends IService<OrganizationDO> {

    Integer createOrganization(CreateOrganizationDTO dto);

    List<OrganizationListDTO> OrganizationList();

    String getShareCode(Integer organizationId);

    void join(JoinOrganizationDTO dto);

    List<OrganizationUserListDTO> userList(Integer organizationId);
}
