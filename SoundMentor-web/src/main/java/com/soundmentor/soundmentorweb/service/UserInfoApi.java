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
    public void setUser(UserDO user);
    public UserDO getUser();
    public List<OrganizationDO> getOrganizations();
    public OrganizationRole getOrganizationRole(Long organizationId);
    public void removeUser();
}
