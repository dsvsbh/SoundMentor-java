package com.soundmentor.soundmentorweb.service.impl;

import com.soundmentor.soundmentorbase.enums.ResultCodeEnum;
import com.soundmentor.soundmentorbase.exception.BizException;
import com.soundmentor.soundmentorpojo.DO.OrganizationDO;
import com.soundmentor.soundmentorpojo.DO.UserDO;
import com.soundmentor.soundmentorpojo.DTO.user.req.CreateOrganizationDTO;
import com.soundmentor.soundmentorweb.config.OrganizationProperties;
import com.soundmentor.soundmentorweb.mapper.OrganizationMapper;
import com.soundmentor.soundmentorweb.service.IOrganizationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soundmentor.soundmentorweb.service.IOrganizationUserService;
import com.soundmentor.soundmentorweb.service.UserInfoApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 组织表 服务实现类
 * </p>
 *
 * @author Make
 * @since 2025-01-05
 */
@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, OrganizationDO> implements IOrganizationService {
    private final UserInfoApi userInfoApi;
    private final IOrganizationUserService ouService;
    private final OrganizationProperties organizationProperties;
    @Override
    @Transactional
    public Long createOrganization(CreateOrganizationDTO dto) {
        Integer capacity = dto.getCapacity();
        if(capacity<0||capacity>organizationProperties.getDefaultOrganizationCapacity())
        {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(),"组织容量应该在"+organizationProperties.getDefaultOrganizationCapacity()+"以内");
        }
        return null;
    }
}
