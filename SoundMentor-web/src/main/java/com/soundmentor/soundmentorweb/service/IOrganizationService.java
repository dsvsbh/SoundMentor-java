package com.soundmentor.soundmentorweb.service;

import com.soundmentor.soundmentorbase.enums.OrganizationRole;
import com.soundmentor.soundmentorpojo.DO.OrganizationDO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soundmentor.soundmentorpojo.DTO.basic.PageResult;
import com.soundmentor.soundmentorpojo.DTO.file.ShareFileDTO;
import com.soundmentor.soundmentorpojo.DTO.organization.*;
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

    List<OrganizationListDTO> OrganizationList(OrganizationRole role);

    String getShareCode(Integer organizationId);

    void join(JoinOrganizationDTO dto);

    List<OrganizationUserListDTO> userList(Integer organizationId);

    void updateRole(UpdateOrgUserRoleDTO dto);

    void removeUserFromOrg(RemoveOrganizationUserDTO dto);

    void removeOrganization(Integer organizationId);

    void shareFile(ShareFileDTO dto);

    PageResult<OrganizationFileListResDTO> fileList(OrganizationFileListReqDTO dto);
}
