package com.soundmentor.soundmentorweb.service;

import com.soundmentor.soundmentorbase.enums.OrganizationRole;
import com.soundmentor.soundmentorpojo.DO.OrganizationDO;
import com.soundmentor.soundmentorpojo.DO.UserDO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 获取当前用户信息
 */
public interface UserInfoApi {
    void setUser(UserDO user);
    UserDO getUser();
    OrganizationRole getOrganizationRole(Integer organizationId);
    void removeUser();
}
