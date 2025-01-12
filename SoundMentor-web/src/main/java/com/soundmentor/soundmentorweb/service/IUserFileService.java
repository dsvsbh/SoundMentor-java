package com.soundmentor.soundmentorweb.service;

import com.soundmentor.soundmentorpojo.DO.UserFileDO;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuzhicheng
 * @since 2025-01-12
 */
public interface IUserFileService extends IService<UserFileDO> {

    void bindUserFile(UserFileDO userFileDO);
}
