package com.soundmentor.soundmentorweb.service.impl;

import cn.hutool.core.lang.UUID;
import com.soundmentor.soundmentorbase.constants.SoundMentorConstant;
import com.soundmentor.soundmentorbase.enums.OrganizationRole;
import com.soundmentor.soundmentorbase.enums.ResultCodeEnum;
import com.soundmentor.soundmentorbase.exception.BizException;
import com.soundmentor.soundmentorpojo.DO.OrganizationDO;
import com.soundmentor.soundmentorpojo.DO.OrganizationUserDO;
import com.soundmentor.soundmentorpojo.DTO.organization.JoinOrganizationDTO;
import com.soundmentor.soundmentorpojo.DTO.organization.OrganizationListDTO;
import com.soundmentor.soundmentorpojo.DTO.organization.OrganizationUserListDTO;
import com.soundmentor.soundmentorpojo.DTO.user.req.CreateOrganizationDTO;
import com.soundmentor.soundmentorweb.config.properties.OrganizationProperties;
import com.soundmentor.soundmentorweb.mapper.OrganizationMapper;
import com.soundmentor.soundmentorweb.mapper.UserMapper;
import com.soundmentor.soundmentorweb.service.IOrganizationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soundmentor.soundmentorweb.service.IOrganizationUserService;
import com.soundmentor.soundmentorweb.service.UserInfoApi;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
    private final RedissonClient redissonClient;
    private final OrganizationMapper organizationMapper;
    private final UserMapper userMapper;

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
                .createTime(LocalDateTime.now())
                .build();
        ouService.save(organizationUserDO);
        return organizationDO.getId();
    }

    @Override
    public List<OrganizationListDTO> OrganizationList(OrganizationRole role) {
        Integer userId = userInfoApi.getUser().getId();
        OrganizationUserDO queryParam = new OrganizationUserDO();
        queryParam.setUserId(userId);
        if(Objects.isNull(role))
        {
            queryParam.setOrganizationRole(null);
        } else {
            queryParam.setOrganizationRole(role.getCode());
        }
        return organizationMapper.getUserOrganizationList(queryParam);
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

    /**
     * 通过分享码进入组织
     * @param dto
     */
    @Override
    public void join(JoinOrganizationDTO dto) {
        String shareCode = dto.getShareCode();
        Integer organizationId = dto.getOrganizationId();
        String s = redisTemplate.opsForValue().get(SoundMentorConstant.ORGANIZATION_SHARE_CODE_KEY + organizationId);
        if(Objects.isNull(s))
        {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(),"分享码不存在或已经失效");
        }
        if(!StringUtils.equals(shareCode,s))
        {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(),"分享码不正确");
        }
        OrganizationDO organization = this.getById(organizationId);
        if(Objects.isNull(organization))
        {
            throw new BizException(ResultCodeEnum.INVALID_PARAM.getCode(),"组织不存在");
        }
        OrganizationUserDO one = ouService.lambdaQuery()
                .eq(OrganizationUserDO::getOrganizationId, organizationId)
                .eq(OrganizationUserDO::getUserId, userInfoApi.getUser().getId())
                .one();
        if (!Objects.isNull(one))
        {
            throw new BizException(ResultCodeEnum.INTERNAL_ERROR.getCode(),"您已经加入该组织");
        }
        Integer capacity = organization.getCapacity();
        joinOrganization(capacity,organizationId);
    }

    @Override
    public List<OrganizationUserListDTO> userList(Integer organizationId) {
        return userMapper.getOrganizationUserList(organizationId);
    }

    /**
     *  加入组织
     * @param capacity
     * @param organizationId
     */
    private void joinOrganization(Integer capacity,Integer organizationId) {
        Integer count = ouService.lambdaQuery()
                .eq(OrganizationUserDO::getOrganizationId, organizationId)
                .count();
        if(count>=capacity)
        {
            throw new BizException(ResultCodeEnum.INTERNAL_ERROR.getCode(),"组织已满");
        }
        //做分布式锁防止并发加入组织导致的人数超出
        RLock lock = redissonClient.getLock(SoundMentorConstant.ORGANIZATION_JOIN_LOCK_KEY + organizationId);
        try {
            boolean isLock = lock.tryLock(5000, TimeUnit.MILLISECONDS);
            if(isLock)//拿到锁加入组织
            {
                Integer userId = userInfoApi.getUser().getId();
                OrganizationUserDO build = OrganizationUserDO.builder()
                        .userId(userId)
                        .organizationId(organizationId)
                        .organizationRole(OrganizationRole.USER.getCode())//通过邀请码加入默认为普通用户
                        .createTime(LocalDateTime.now())
                        .build();
                ouService.save(build);
            } else {
                //没拿到锁的自旋
                Thread.sleep(50);
                joinOrganization(capacity,organizationId);
            }
        } catch (Exception e)
        {
            throw new BizException(ResultCodeEnum.INTERNAL_ERROR.getCode(),"加入组织失败");
        }
    }
}
