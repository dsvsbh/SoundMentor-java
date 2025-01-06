package com.soundmentor.soundmentorweb.service.impl;

import cn.hutool.core.lang.UUID;
import com.soundmentor.soundmentorbase.constants.SoundMentorConstant;
import com.soundmentor.soundmentorbase.enums.OrganizationRole;
import com.soundmentor.soundmentorbase.enums.ResultCodeEnum;
import com.soundmentor.soundmentorbase.exception.BizException;
import com.soundmentor.soundmentorpojo.DO.OrganizationDO;
import com.soundmentor.soundmentorpojo.DO.OrganizationUserDO;
import com.soundmentor.soundmentorpojo.DO.UserDO;
import com.soundmentor.soundmentorpojo.DTO.organization.JoinOrganizationDTO;
import com.soundmentor.soundmentorpojo.DTO.organization.OrganizationListDTO;
import com.soundmentor.soundmentorpojo.DTO.user.req.CreateOrganizationDTO;
import com.soundmentor.soundmentorweb.config.OrganizationProperties;
import com.soundmentor.soundmentorweb.mapper.OrganizationMapper;
import com.soundmentor.soundmentorweb.service.IOrganizationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soundmentor.soundmentorweb.service.IOrganizationUserService;
import com.soundmentor.soundmentorweb.service.UserInfoApi;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    private final RedisTemplate<String,String> redisTemplate;

    @Override
    @Transactional
    public Integer createOrganization(CreateOrganizationDTO dto) {
        Integer capacity = dto.getCapacity();
        if(Objects.isNull(capacity))
        {
            capacity=organizationProperties.getDefaultOrganizationCapacity();
        } else {
            if(capacity<0||capacity>organizationProperties.getDefaultOrganizationCapacity())
            {
                throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(),"组织容量应该在"+organizationProperties.getDefaultOrganizationCapacity()+"以内");
            }
        }
        Integer userId = userInfoApi.getUser().getId();
        Integer count = ouService.lambdaQuery()
                .eq(OrganizationUserDO::getUserId, userId)
                .count();
        if(count>=organizationProperties.getMaxOrganizations())
        {
            throw new BizException(ResultCodeEnum.INTERNAL_ERROR.getCode(),"用户最多加入"+organizationProperties.getMaxOrganizations()+"个组织");
        }
        OrganizationDO organizationDO = OrganizationDO.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .capacity(capacity)
                .createdTime(LocalDateTime.now())
                .updatedTime(LocalDateTime.now())
                .build();
        this.save(organizationDO);
        OrganizationUserDO organizationUserDO = OrganizationUserDO.builder()
                .organizationId(organizationDO.getId())
                .userId(userId)
                .organizationRole(OrganizationRole.CREATOR.getCode())
                .build();
        ouService.save(organizationUserDO);
        return organizationDO.getId();
    }

    @Override
    public List<OrganizationListDTO> OrganizationList() {
        Integer userId = userInfoApi.getUser().getId();
        List<OrganizationUserDO> list = ouService.lambdaQuery()
                .eq(OrganizationUserDO::getUserId, userId)
                .list();
        List<Integer> organizationIds = list.stream()
                .map(OrganizationUserDO::getOrganizationId)
                .collect(Collectors.toList());
        return this.lambdaQuery()
                .in(OrganizationDO::getId, organizationIds)
                .list()
                .stream()
                .map(i -> {
                    OrganizationListDTO organizationListDTO = new OrganizationListDTO();
                    organizationListDTO.setName(i.getName());
                    organizationListDTO.setDescription(i.getDescription());
                    organizationListDTO.setCapacity(i.getCapacity());
                    organizationListDTO.setId(i.getId());
                    organizationListDTO.setCreatedTime(i.getCreatedTime());
                    organizationListDTO.setUpdatedTime(i.getUpdatedTime());
                    return organizationListDTO;
                }).collect(Collectors.toList());
    }

    @Override
    public String getShareCode(Integer organizationId) {
        OrganizationRole organizationRole = userInfoApi.getOrganizationRole(organizationId);
        if(Objects.isNull(organizationRole))
        {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(),"您不是该组织成员");
        }
        if(organizationRole.equals(OrganizationRole.USER))
        {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(),"您的权限不够");
        }
        String shareCode = redisTemplate.opsForValue().get(SoundMentorConstant.ORGANIZATION_SHARE_CODE_KEY + organizationId);
        if(Objects.isNull(shareCode))
        {
            shareCode= UUID.fastUUID().toString();
            redisTemplate.opsForValue().set(SoundMentorConstant.ORGANIZATION_SHARE_CODE_KEY + organizationId,shareCode,1, TimeUnit.DAYS);
        }
        return shareCode;
    }

    @Override
    public void join(JoinOrganizationDTO dto) {
        //todo 根据验证码进入组织
    }
}
