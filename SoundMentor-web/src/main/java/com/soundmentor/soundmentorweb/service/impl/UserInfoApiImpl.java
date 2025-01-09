package com.soundmentor.soundmentorweb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soundmentor.soundmentorbase.enums.OrganizationRole;
import com.soundmentor.soundmentorpojo.DO.OrganizationDO;
import com.soundmentor.soundmentorpojo.DO.OrganizationUserDO;
import com.soundmentor.soundmentorpojo.DO.UserDO;
import com.soundmentor.soundmentorweb.mapper.UserMapper;
import com.soundmentor.soundmentorweb.service.IOrganizationService;
import com.soundmentor.soundmentorweb.service.IOrganizationUserService;
import com.soundmentor.soundmentorweb.service.IUserService;
import com.soundmentor.soundmentorweb.service.UserInfoApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserInfoApiImpl implements UserInfoApi {
    private ThreadLocal<UserDO> userInfo = new ThreadLocal<>();
    @Resource
    private IOrganizationUserService organizationUserService;
    @Resource
    private IOrganizationService organizationService;

    @Override
    public void setUser(UserDO user) {
        userInfo.set(user);
    }

    @Override
    public UserDO getUser() {
        return userInfo.get();
    }


    @Override
    public OrganizationRole getOrganizationRole(Integer organizationId) {
        OrganizationUserDO organizationUserDO = organizationUserService.getOne(new LambdaQueryWrapper<OrganizationUserDO>()
                .eq(OrganizationUserDO::getUserId, getUser().getId())
                .eq(OrganizationUserDO::getOrganizationId, organizationId));
        if (Objects.isNull(organizationUserDO))
        {
            return null;
        }
        return Arrays.stream(OrganizationRole.values()).filter(organizationRole -> organizationRole.getCode().equals(organizationUserDO.getOrganizationRole()))
                .findFirst().orElse(null);
    }

    @Override
    public void removeUser() {
        userInfo.remove();
    }
}
